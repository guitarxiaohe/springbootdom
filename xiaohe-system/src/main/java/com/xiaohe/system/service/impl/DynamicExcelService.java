package com.xiaohe.system.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaohe.common.exception.ServiceException;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.system.domain.DynamicEntityColumnValue;
import com.xiaohe.system.domain.EntityConfig;
import com.xiaohe.system.domain.FieldConfig;
import com.xiaohe.system.mapper.DynamicEntityMapper;
import com.xiaohe.system.mapper.EntityConfigMapper;
import com.xiaohe.system.mapper.FieldConfigMapper;

/**
 * 动态实体 Excel 导入导出服务。
 *
 * @author xiaohe
 */
@Service
public class DynamicExcelService
{
    private static final Logger log = LoggerFactory.getLogger(DynamicExcelService.class);

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EntityConfigMapper entityConfigMapper;

    @Autowired
    private FieldConfigMapper fieldConfigMapper;

    @Autowired
    private DynamicEntityMapper dynamicEntityMapper;

    /**
     * 导出实体数据到 Excel 并通过 HTTP 响应下载。
     */
    public void exportEntityToExcel(String entityKey, HttpServletResponse response) throws IOException
    {
        EntityConfig ec = requireEntityConfig(entityKey);
        List<FieldConfig> fieldConfigs = fieldConfigMapper.selectFieldConfigByEntityKey(entityKey);
        if (fieldConfigs.isEmpty())
        {
            throw new ServiceException("No field_config found for entity_key: " + entityKey);
        }
        List<LinkedHashMap<String, Object>> rows = dynamicEntityMapper.selectEntityRowList(ec.getTableName(),
                new ArrayList<>(), ec.getTableName() + ".*");
        writeExcelToResponse(response, ec.getEntityName() != null ? ec.getEntityName() : entityKey, fieldConfigs,
                rows);
    }

    /**
     * 导出 Excel 导入模板（仅表头 + 一行示例数据）。
     */
    public void exportTemplate(String entityKey, HttpServletResponse response) throws IOException
    {
        EntityConfig ec = requireEntityConfig(entityKey);
        List<FieldConfig> fieldConfigs = fieldConfigMapper.selectFieldConfigByEntityKey(entityKey);
        if (fieldConfigs.isEmpty())
        {
            throw new ServiceException("No field_config found for entity_key: " + entityKey);
        }
        writeExcelToResponse(response, ec.getEntityName() != null ? ec.getEntityName() : entityKey, fieldConfigs,
                new ArrayList<>());
    }

    /**
     * 从 Excel 导入数据。
     *
     * @param entityKey 实体标识
     * @param file      上传的 Excel 文件
     * @param mode      导入模式：create / update / upsert
     * @param children  子表卸除 JSON，格式：[{"tableName":"child_table","childKey":"parent_id","parentValue":123}]
     * @return 导入结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importFromExcel(String entityKey, MultipartFile file, String mode, String children)
            throws IOException
    {
        EntityConfig ec = requireEntityConfig(entityKey);
        String tableName = ec.getTableName();

        // 1. 子表卸除：先删除子表关联数据
        if (StringUtils.isNotEmpty(children))
        {
            applyChildrenDeletion(children);
        }

        // 2. 获取字段配置
        List<FieldConfig> fieldConfigs = fieldConfigMapper.selectFieldConfigByEntityKey(entityKey);
        if (fieldConfigs.isEmpty())
        {
            throw new ServiceException("No field_config found for entity_key: " + entityKey);
        }
        String pkColumn = requirePrimaryKeyColumn(tableName);
        Map<String, FieldConfig> fieldConfigByKey = buildFieldConfigByKeyMap(fieldConfigs);
        Map<String, String> headerToFieldKey = buildHeaderToFieldKeyMap(fieldConfigs);
        headerToFieldKey.put(pkColumn, pkColumn);
        headerToFieldKey.put(StringUtils.toCamelCase(pkColumn), pkColumn);

        // 3. 解析 Excel
        int success = 0;
        int fail = 0;
        List<String> errors = new ArrayList<>();
        String finalMode = (mode == null || mode.trim().isEmpty()) ? "create" : mode.trim().toLowerCase();

        try (InputStream is = file.getInputStream())
        {
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null || sheet.getLastRowNum() < 1)
            {
                throw new ServiceException("Empty Excel file");
            }

            // 解析表头
            Row headerRow = sheet.getRow(0);
            if (headerRow == null)
            {
                throw new ServiceException("Missing header row");
            }
            Map<Integer, String> colToFieldKey = new HashMap<>();
            Map<Integer, String> colToType = new HashMap<>();
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++)
            {
                String header = getCellStringValue(headerRow.getCell(i));
                if (StringUtils.isEmpty(header))
                {
                    continue;
                }
                String fieldKey = headerToFieldKey.get(header);
                if (fieldKey != null)
                {
                    colToFieldKey.put(i, fieldKey);
                    FieldConfig fc = fieldConfigByKey.get(fieldKey);
                    if (fc != null)
                    {
                        colToType.put(i, fc.getFieldType());
                    }
                }
            }
            if (colToFieldKey.isEmpty())
            {
                throw new ServiceException("No matching columns found in Excel header");
            }

            Set<String> actualColumns = new LinkedHashSet<>(dynamicEntityMapper.selectColumnNames(tableName));

            // 4. 逐行导入
            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++)
            {
                Row row = sheet.getRow(rowIdx);
                if (row == null || isRowEmpty(row))
                {
                    continue;
                }
                try
                {
                    Map<String, Object> rowData = new LinkedHashMap<>();
                    for (Map.Entry<Integer, String> entry : colToFieldKey.entrySet())
                    {
                        int colIdx = entry.getKey();
                        String fieldKey = entry.getValue();
                        Object rawValue = getCellValue(row, colIdx);
                        if (rawValue == null || StringUtils.isEmpty(rawValue.toString()))
                        {
                            continue;
                        }
                        String fieldType = colToType.get(colIdx);
                        rowData.put(fieldKey, convertValueByFieldType(rawValue, fieldType));
                    }
                    if (rowData.isEmpty())
                    {
                        continue;
                    }

                    if ("create".equals(finalMode))
                    {
                        rowData.remove(pkColumn);
                        List<DynamicEntityColumnValue> values = buildWriteValues(rowData, actualColumns, pkColumn);
                        if (!values.isEmpty())
                        {
                            dynamicEntityMapper.insertEntityRow(tableName, values);
                        }
                    }
                    else if ("update".equals(finalMode))
                    {
                        Object pkValue = rowData.remove(pkColumn);
                        if (pkValue == null)
                        {
                            throw new ServiceException("Missing primary key value");
                        }
                        List<DynamicEntityColumnValue> values = buildWriteValues(rowData, actualColumns, pkColumn);
                        if (!values.isEmpty())
                        {
                            dynamicEntityMapper.updateEntityRowById(tableName, pkColumn,
                                    toLong(pkValue), values);
                        }
                    }
                    else if ("upsert".equals(finalMode))
                    {
                        Object pkValue = rowData.remove(pkColumn);
                        if (pkValue != null)
                        {
                            Map<String, Object> existing = dynamicEntityMapper.selectEntityRowById(
                                    tableName, pkColumn, toLong(pkValue));
                            List<DynamicEntityColumnValue> values = buildWriteValues(rowData, actualColumns,
                                    pkColumn);
                            if (existing != null && !existing.isEmpty())
                            {
                                if (!values.isEmpty())
                                {
                                    dynamicEntityMapper.updateEntityRowById(tableName, pkColumn,
                                            toLong(pkValue), values);
                                }
                            }
                            else
                            {
                                if (pkValue instanceof Number && ((Number) pkValue).longValue() > 0)
                                {
                                    DynamicEntityColumnValue pkCv = new DynamicEntityColumnValue();
                                    pkCv.setColumn(pkColumn);
                                    pkCv.setValue(pkValue);
                                    values.add(0, pkCv);
                                }
                                if (!values.isEmpty())
                                {
                                    dynamicEntityMapper.insertEntityRow(tableName, values);
                                }
                            }
                        }
                        else
                        {
                            List<DynamicEntityColumnValue> values = buildWriteValues(rowData, actualColumns,
                                    pkColumn);
                            if (!values.isEmpty())
                            {
                                dynamicEntityMapper.insertEntityRow(tableName, values);
                            }
                        }
                    }
                    else
                    {
                        throw new ServiceException("Unsupported import mode: " + finalMode);
                    }
                    success++;
                }
                catch (Exception e)
                {
                    fail++;
                    errors.add("Row " + (rowIdx + 1) + ": " + e.getMessage());
                    log.warn("Import row {} failed: {}", rowIdx + 1, e.getMessage());
                }
            }
            wb.close();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", success);
        result.put("fail", fail);
        result.put("errors", errors);
        return result;
    }

    // ======================== 子表卸除 ========================

    /**
     * 解析 children JSON 参数，对每个子表执行 DELETE WHERE childKey = parentValue。
     * children 格式：[{"tableName":"child_table","childKey":"parent_id","parentValue":123}]
     */
    private void applyChildrenDeletion(String childrenJson)
    {
        JSONArray childrenArray;
        try
        {
            childrenArray = JSON.parseArray(childrenJson);
        }
        catch (Exception e)
        {
            throw new ServiceException("Invalid children JSON: " + e.getMessage());
        }
        if (childrenArray == null || childrenArray.isEmpty())
        {
            return;
        }
        for (int i = 0; i < childrenArray.size(); i++)
        {
            JSONObject child = childrenArray.getJSONObject(i);
            String tableName = child.getString("tableName");
            String childKey = child.getString("childKey");
            Object parentValue = child.get("parentValue");

            if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(childKey) || parentValue == null)
            {
                throw new ServiceException(
                        "children entry missing tableName/childKey/parentValue at index " + i);
            }
            if (!isSafeIdentifier(tableName) || !isSafeIdentifier(childKey))
            {
                throw new ServiceException(
                        "Invalid table name or column in children at index " + i);
            }
            int deleted = dynamicEntityMapper.deleteRowsByColumn(tableName, childKey, parentValue);
            log.info("Children deletion: DELETE FROM {} WHERE {} = {}, deleted {} rows",
                    tableName, childKey, parentValue, deleted);
        }
    }

    // ======================== Excel 写入 ========================

    private void writeExcelToResponse(HttpServletResponse response, String sheetName,
            List<FieldConfig> fieldConfigs, List<LinkedHashMap<String, Object>> rows) throws IOException
    {
        SXSSFWorkbook wb = new SXSSFWorkbook(500);
        try
        {
            Sheet sheet = wb.createSheet(sheetName);
            CellStyle headerStyle = createHeaderStyle(wb);
            CellStyle dataStyle = createDataStyle(wb);

            // 写表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fieldConfigs.size(); i++)
            {
                FieldConfig fc = fieldConfigs.get(i);
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(StringUtils.isNotEmpty(fc.getFieldName()) ? fc.getFieldName()
                        : StringUtils.toCamelCase(fc.getFieldKey()));
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 18 * 256);
            }

            // 写数据行
            if (rows != null && !rows.isEmpty())
            {
                for (int r = 0; r < rows.size(); r++)
                {
                    Row dataRow = sheet.createRow(r + 1);
                    Map<String, Object> rowData = rows.get(r);
                    for (int c = 0; c < fieldConfigs.size(); c++)
                    {
                        FieldConfig fc = fieldConfigs.get(c);
                        Cell cell = dataRow.createCell(c);
                        Object value = rowData.get(StringUtils.toCamelCase(fc.getFieldKey()));
                        if (value == null)
                        {
                            value = rowData.get(fc.getFieldKey());
                        }
                        setCellFormattedValue(cell, value, fc.getFieldType());
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            // 响应头
            String filename = URLEncoder.encode(sheetName, "UTF-8").replace("+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + filename + ".xlsx");
            wb.write(response.getOutputStream());
            response.getOutputStream().flush();
        }
        finally
        {
            IOUtils.closeQuietly(wb);
        }
    }

    private void setCellFormattedValue(Cell cell, Object value, String fieldType)
    {
        if (value == null)
        {
            cell.setCellValue("");
            return;
        }
        String type = (fieldType == null) ? "input" : fieldType.trim().toLowerCase();
        switch (type)
        {
            case "date":
                cell.setCellValue(formatDate(value));
                break;
            case "datetime":
                cell.setCellValue(formatDatetime(value));
                break;
            case "switch":
                cell.setCellValue(
                        (value instanceof Number && ((Number) value).intValue() == 1) ? "是" : "否");
                break;
            default:
                cell.setCellValue(String.valueOf(value));
                break;
        }
    }

    private String formatDate(Object value)
    {
        if (value instanceof Number)
        {
            long ms = ((Number) value).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault()).format(DATE_FMT);
        }
        return String.valueOf(value);
    }

    private String formatDatetime(Object value)
    {
        if (value instanceof Number)
        {
            long ms = ((Number) value).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())
                    .format(DATETIME_FMT);
        }
        return String.valueOf(value);
    }

    // ======================== Excel 读取 ========================

    private String getCellStringValue(Cell cell)
    {
        if (cell == null)
        {
            return "";
        }
        Object val = getCellValue(cell.getRow(), cell.getColumnIndex());
        return val == null ? "" : val.toString().trim();
    }

    private Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return null;
        }
        Cell cell = row.getCell(column);
        if (cell == null)
        {
            return null;
        }
        try
        {
            if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA)
            {
                double val = cell.getNumericCellValue();
                if (DateUtil.isCellDateFormatted(cell))
                {
                    return DateUtil.getJavaDate(val);
                }
                if (val % 1 != 0)
                {
                    return val;
                }
                return (long) val;
            }
            else if (cell.getCellType() == CellType.STRING)
            {
                return cell.getStringCellValue();
            }
            else if (cell.getCellType() == CellType.BOOLEAN)
            {
                return cell.getBooleanCellValue();
            }
        }
        catch (Exception ignored)
        {
            return null;
        }
        return null;
    }

    private boolean isRowEmpty(Row row)
    {
        if (row == null)
        {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK)
            {
                String s = getCellStringValue(cell);
                if (StringUtils.isNotEmpty(s))
                {
                    return false;
                }
            }
        }
        return true;
    }

    // ======================== 类型转换 ========================

    private Object convertValueByFieldType(Object rawValue, String fieldType)
    {
        if (rawValue == null)
        {
            return null;
        }
        if (rawValue instanceof Date)
        {
            return ((Date) rawValue).getTime();
        }
        String str = rawValue.toString().trim();
        if (str.isEmpty())
        {
            return null;
        }
        String type = (fieldType == null) ? "input" : fieldType.trim().toLowerCase();

        switch (type)
        {
            case "number":
                try
                {
                    if (str.contains("."))
                    {
                        return Double.valueOf(str);
                    }
                    return Long.valueOf(str);
                }
                catch (NumberFormatException ignored)
                {
                    return str;
                }
            case "switch":
                if ("true".equalsIgnoreCase(str) || "1".equals(str) || "是".equals(str)
                        || "yes".equalsIgnoreCase(str))
                {
                    return 1;
                }
                return 0;
            case "date":
            case "datetime":
                return parseDateToMillis(str);
            default:
                return str;
        }
    }

    private Long parseDateToMillis(String str)
    {
        try
        {
            if (str.contains(":"))
            {
                LocalDateTime ldt = LocalDateTime.parse(str, DATETIME_FMT);
                return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            LocalDate ld = LocalDate.parse(str, DATE_FMT);
            return ld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        catch (Exception ignored)
        {
        }
        try
        {
            return Long.parseLong(str);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    // ======================== 字段映射 ========================

    /**
     * 构建 field_key → FieldConfig 的快速查找映射。
     */
    private Map<String, FieldConfig> buildFieldConfigByKeyMap(List<FieldConfig> fieldConfigs)
    {
        Map<String, FieldConfig> map = new LinkedHashMap<>();
        for (FieldConfig fc : fieldConfigs)
        {
            if (StringUtils.isNotEmpty(fc.getFieldKey()))
            {
                map.put(fc.getFieldKey(), fc);
            }
        }
        return map;
    }

    /**
     * 构建 Excel 表头到 field_key 的匹配映射，支持蛇形、驼峰、字段名称三种匹配方式。
     */
    private Map<String, String> buildHeaderToFieldKeyMap(List<FieldConfig> fieldConfigs)
    {
        Map<String, String> map = new HashMap<>();
        for (FieldConfig fc : fieldConfigs)
        {
            if (StringUtils.isEmpty(fc.getFieldKey()))
            {
                continue;
            }
            map.put(fc.getFieldKey(), fc.getFieldKey());
            map.put(StringUtils.toCamelCase(fc.getFieldKey()), fc.getFieldKey());
            if (StringUtils.isNotEmpty(fc.getFieldName()))
            {
                map.put(fc.getFieldName(), fc.getFieldKey());
            }
        }
        return map;
    }

    // ======================== 数据写入辅助 ========================

    private List<DynamicEntityColumnValue> buildWriteValues(Map<String, Object> data,
            Set<String> actualColumns, String pkColumn)
    {
        List<DynamicEntityColumnValue> values = new ArrayList<>();
        if (data == null || data.isEmpty())
        {
            return values;
        }
        for (Map.Entry<String, Object> entry : data.entrySet())
        {
            String column = entry.getKey();
            if (StringUtils.isEmpty(column) || pkColumn.equals(column))
            {
                continue;
            }
            if (!isSafeIdentifier(column) || !actualColumns.contains(column))
            {
                continue;
            }
            DynamicEntityColumnValue cv = new DynamicEntityColumnValue();
            cv.setColumn(column);
            cv.setValue(entry.getValue());
            values.add(cv);
        }
        return values;
    }

    // ======================== 公共验证方法 ========================

    private EntityConfig requireEntityConfig(String entityKey)
    {
        EntityConfig ec = entityConfigMapper.selectEntityConfigByEntityKey(entityKey);
        if (ec == null || StringUtils.isEmpty(ec.getTableName()))
        {
            throw new ServiceException("Unknown entity_key: " + entityKey);
        }
        if (!isSafeIdentifier(ec.getTableName()))
        {
            throw new ServiceException("Invalid table_name in entity_config");
        }
        return ec;
    }

    private String requirePrimaryKeyColumn(String tableName)
    {
        String pkColumn = dynamicEntityMapper.selectPrimaryKeyColumn(tableName);
        if (!isSafeIdentifier(pkColumn))
        {
            throw new ServiceException("Primary key not found for table: " + tableName);
        }
        return pkColumn;
    }

    private static boolean isSafeIdentifier(String s)
    {
        return s != null && s.matches("^[a-zA-Z0-9_]+$");
    }

    private static long toLong(Object value)
    {
        if (value == null)
        {
            return 0L;
        }
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        try
        {
            return Long.parseLong(value.toString());
        }
        catch (NumberFormatException e)
        {
            return 0L;
        }
    }

    // ======================== 样式 ========================

    private CellStyle createHeaderStyle(SXSSFWorkbook wb)
    {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private CellStyle createDataStyle(SXSSFWorkbook wb)
    {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }
}
