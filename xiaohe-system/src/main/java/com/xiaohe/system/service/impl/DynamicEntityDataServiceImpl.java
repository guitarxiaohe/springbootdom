package com.xiaohe.system.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.xiaohe.common.exception.ServiceException;
import com.xiaohe.common.utils.ServletUtils;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.system.domain.DynamicEntityColumnValue;
import com.xiaohe.system.domain.DynamicEntityRowFilter;
import com.xiaohe.system.domain.EntityConfig;
import com.xiaohe.system.mapper.DynamicEntityMapper;
import com.xiaohe.system.mapper.EntityConfigMapper;
import com.xiaohe.system.mapper.FieldConfigMapper;
import com.xiaohe.system.service.IDynamicEntityDataService;

import static com.xiaohe.common.core.text.Convert.toLong;

@Service
public class DynamicEntityDataServiceImpl implements IDynamicEntityDataService
{
    private static final Set<String> RESERVED_PARAMS = new HashSet<>();

    static
    {
        RESERVED_PARAMS.add("pageNum");
        RESERVED_PARAMS.add("pageSize");
        RESERVED_PARAMS.add("orderByColumn");
        RESERVED_PARAMS.add("isAsc");
        RESERVED_PARAMS.add("reasonable");
        RESERVED_PARAMS.add("dataParams");
    }

    @Autowired
    private EntityConfigMapper entityConfigMapper;

    @Autowired
    private FieldConfigMapper fieldConfigMapper;

    @Autowired
    private DynamicEntityMapper dynamicEntityMapper;

    @Override
    public boolean isAllowedOrderColumn(String entityKey, String columnUnderScore)
    {
        if (StringUtils.isEmpty(columnUnderScore))
        {
            return false;
        }
        return fieldConfigMapper.countFieldConfigByEntityKeyAndFieldKey(entityKey, columnUnderScore) > 0;
    }

    @Override
    public List<Map<String, Object>> selectEntityRowList(String entityKey)
    {
        EntityConfig ec = requireEntityConfig(entityKey);
        Map<String, FieldFilterMeta> metaByKey = buildFieldMetaByKey(entityKey);
        Map<String, String> fieldKeyAliases = buildFieldKeyAliases(metaByKey.keySet());
        Map<String, DynamicEntityRowFilter> filterMap = buildFilterMap(metaByKey, fieldKeyAliases);
        String columns = buildVisibleColumnList(entityKey, ec.getTableName());
        List<LinkedHashMap<String, Object>> rows = dynamicEntityMapper.selectEntityRowList(ec.getTableName(),
                new ArrayList<>(filterMap.values()), columns);
        return convertRowsToCamelCase(rows);
    }

    @Override
    public List<Map<String, Object>> selectEntityRowList(String entityKey, Integer pageNum, Integer pageSize, String orderBy,
            Boolean reasonable)
    {
        if (pageNum == null || pageSize == null)
        {
            return selectEntityRowList(entityKey);
        }
        EntityConfig ec = requireEntityConfig(entityKey);
        Map<String, FieldFilterMeta> metaByKey = buildFieldMetaByKey(entityKey);
        Map<String, String> fieldKeyAliases = buildFieldKeyAliases(metaByKey.keySet());
        Map<String, DynamicEntityRowFilter> filterMap = buildFilterMap(metaByKey, fieldKeyAliases);
        String columns = buildVisibleColumnList(entityKey, ec.getTableName());
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        List<LinkedHashMap<String, Object>> rows = dynamicEntityMapper.selectEntityRowList(ec.getTableName(),
                new ArrayList<>(filterMap.values()), columns);
        return convertRowsToCamelCase(rows);
    }

    @Override
    public Map<String, Object> selectEntityRowById(String entityKey, Long id)
    {
        if (id == null)
        {
            throw new ServiceException("Missing primary key id");
        }
        EntityConfig ec = requireEntityConfig(entityKey);
        String pkColumn = requirePrimaryKeyColumn(ec.getTableName());
        String columns = buildVisibleColumnList(entityKey, ec.getTableName());
        return convertRowToCamelCase(dynamicEntityMapper.selectEntityRowById(ec.getTableName(), pkColumn, id, columns));
    }

    /**
     * 从 field_config 构建列表查询字段白名单（is_visible=1 的 field_key 集合）
     */
    private String buildVisibleColumnList(String entityKey, String tableName)
    {
        try
        {
            com.xiaohe.system.domain.FieldConfig queryConfig = new com.xiaohe.system.domain.FieldConfig();
            queryConfig.setEntityKey(entityKey);
            List<com.xiaohe.system.domain.FieldConfig> configs = fieldConfigMapper.selectFieldConfigList(queryConfig);
            if (configs == null || configs.isEmpty())
            {
                return tableName + ".*";
            }
            StringBuilder sb = new StringBuilder();
            for (com.xiaohe.system.domain.FieldConfig fc : configs)
            {
                if (fc.getIsVisible() != null && fc.getIsVisible() == 1 && StringUtils.isNotEmpty(fc.getFieldKey()))
                {
                    if (sb.length() > 0) sb.append(", ");
                    String fieldKey = fc.getFieldKey().replaceAll("[^a-zA-Z0-9_]", "");
                    sb.append("`").append(fieldKey).append("`");
                }
            }
            if (sb.length() == 0)
            {
                return tableName + ".*";
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            // fallback: 出错时返回整表列
            return tableName + ".*";
        }
    }

    @Override
    public int deleteEntityRowsByIds(String entityKey, Long[] ids)
    {
        if (ids == null || ids.length == 0)
        {
            throw new ServiceException("No ids to delete");
        }
        EntityConfig ec = requireEntityConfig(entityKey);
        String pkColumn = requirePrimaryKeyColumn(ec.getTableName());
        return dynamicEntityMapper.deleteEntityRowsByIds(ec.getTableName(), pkColumn, ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> insertEntityRow(String entityKey, Map<String, Object> data, String operatorUserName,
            Long operatorUserId)
    {
        EntityConfig ec = requireEntityConfig(entityKey);
        String tableName = ec.getTableName();
        String pkColumn = requirePrimaryKeyColumn(tableName);
        List<DynamicEntityColumnValue> values = buildWriteValues(entityKey, tableName, pkColumn, data, false,
                operatorUserName, operatorUserId);
        if (values.isEmpty())
        {
            throw new ServiceException("No writable fields provided");
        }
        dynamicEntityMapper.insertEntityRow(tableName, values);
        Long id = extractPrimaryKeyValue(pkColumn, data, values);
        if (id == null)
        {
            id = dynamicEntityMapper.selectLastInsertId();
        }
        if (id == null)
        {
            return Collections.emptyMap();
        }
        return selectEntityRowById(entityKey, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateEntityRow(String entityKey, Long id, Map<String, Object> data, String operatorUserName,
            Long operatorUserId)
    {
        if (id == null)
        {
            throw new ServiceException("Missing primary key id");
        }
        EntityConfig ec = requireEntityConfig(entityKey);
        String tableName = ec.getTableName();
        String pkColumn = requirePrimaryKeyColumn(tableName);
        List<DynamicEntityColumnValue> values = buildWriteValues(entityKey, tableName, pkColumn, data, true,
                operatorUserName, operatorUserId);
        if (values.isEmpty())
        {
            throw new ServiceException("No writable fields provided");
        }
        int rows = dynamicEntityMapper.updateEntityRowById(tableName, pkColumn, id, values);
        if (rows <= 0)
        {
            throw new ServiceException("Data does not exist or update failed");
        }
        return selectEntityRowById(entityKey, id);
    }

    private static DynamicEntityRowFilter buildFilter(String column, String value, Integer fuzzyFlag)
    {
        DynamicEntityRowFilter f = new DynamicEntityRowFilter();
        f.setColumn(column);
        f.setValue(value);
        f.setFuzzy(fuzzyFlag != null && fuzzyFlag == 1);
        return f;
    }

    private Map<String, FieldFilterMeta> buildFieldMetaByKey(String entityKey)
    {
        List<Map<String, Object>> metaRows = fieldConfigMapper.selectFieldFilterMetaByEntityKey(entityKey);
        Map<String, FieldFilterMeta> metaByKey = new LinkedHashMap<>();
        for (Map<String, Object> row : metaRows)
        {
            Object fkObj = row.get("fieldKey");
            if (fkObj == null)
            {
                continue;
            }
            String fk = String.valueOf(fkObj);
            if (StringUtils.isEmpty(fk))
            {
                continue;
            }
            FieldFilterMeta meta = new FieldFilterMeta();
            meta.setFieldKey(fk);
            meta.setFuzzy(toInt(row.get("isFuzzySearch")) == 1);
            Object selectEntityKey = row.get("selectEntityKey");
            if (selectEntityKey != null)
            {
                meta.setSelectEntityKey(String.valueOf(selectEntityKey));
            }
            metaByKey.put(fk, meta);
        }
        return metaByKey;
    }

    private Map<String, String> buildFieldKeyAliases(Set<String> fieldKeys)
    {
        Map<String, String> aliases = new LinkedHashMap<>();
        for (String fieldKey : fieldKeys)
        {
            aliases.put(fieldKey, fieldKey);
            aliases.put(StringUtils.toCamelCase(fieldKey), fieldKey);
        }
        return aliases;
    }

    private Map<String, DynamicEntityRowFilter> buildFilterMap(Map<String, FieldFilterMeta> metaByKey,
            Map<String, String> fieldKeyAliases)
    {
        Map<String, DynamicEntityRowFilter> filterMap = new LinkedHashMap<>();
        HttpServletRequest req = ServletUtils.getRequest();
        for (String paramName : Collections.list(req.getParameterNames()))
        {
            if (RESERVED_PARAMS.contains(paramName))
            {
                continue;
            }
            String column = fieldKeyAliases.get(paramName);
            if (StringUtils.isEmpty(column))
            {
                continue;
            }
            String val = req.getParameter(paramName);
            if (StringUtils.isEmpty(val))
            {
                continue;
            }
            FieldFilterMeta meta = metaByKey.get(column);
            filterMap.put(column, buildFilter(column, val, meta));
        }
        String dataParamsJson = req.getParameter("dataParams");
        if (StringUtils.isNotEmpty(dataParamsJson))
        {
            Map<String, Object> dp = JSON.parseObject(dataParamsJson, new TypeReference<Map<String, Object>>()
            {
            });
            if (dp != null)
            {
                for (Map.Entry<String, Object> e : dp.entrySet())
                {
                    String column = fieldKeyAliases.get(e.getKey());
                    if (StringUtils.isEmpty(column) || e.getValue() == null)
                    {
                        continue;
                    }
                    String v = String.valueOf(e.getValue()).trim();
                    if (StringUtils.isEmpty(v))
                    {
                        continue;
                    }
                    FieldFilterMeta meta = metaByKey.get(column);
                    filterMap.put(column, buildFilter(column, v, meta));
                }
            }
        }
        return filterMap;
    }

    private DynamicEntityRowFilter buildFilter(String column, String value, FieldFilterMeta meta)
    {
        if (meta != null && StringUtils.isNotEmpty(meta.getSelectEntityKey()))
        {
            DynamicEntityRowFilter relatedFilter = buildRelatedFilter(column, value, meta.getSelectEntityKey());
            if (relatedFilter != null)
            {
                return relatedFilter;
            }
        }
        return buildFilter(column, value, meta != null && meta.isFuzzy() ? 1 : 0);
    }

    private DynamicEntityRowFilter buildRelatedFilter(String column, String value, String selectEntityKey)
    {
        EntityConfig relatedEntityConfig = entityConfigMapper.selectEntityConfigByEntityKey(selectEntityKey);
        if (relatedEntityConfig == null || StringUtils.isEmpty(relatedEntityConfig.getTableName()))
        {
            return null;
        }
        String relatedTableName = relatedEntityConfig.getTableName();
        if (!isSafeIdentifier(relatedTableName))
        {
            return null;
        }
        String relatedPkColumn = requirePrimaryKeyColumn(relatedTableName);
        Map<String, FieldFilterMeta> relatedMetaByKey = buildFieldMetaByKey(selectEntityKey);
        List<String> relatedColumns = new ArrayList<>();
        for (FieldFilterMeta meta : relatedMetaByKey.values())
        {
            if (!meta.isFuzzy() || !isSafeIdentifier(meta.getFieldKey()))
            {
                continue;
            }
            relatedColumns.add(meta.getFieldKey());
        }
        if (relatedColumns.isEmpty())
        {
            return null;
        }
        DynamicEntityRowFilter filter = new DynamicEntityRowFilter();
        filter.setColumn(column);
        filter.setValue(value);
        filter.setRelated(true);
        filter.setRelatedTableName(relatedTableName);
        filter.setRelatedPkColumn(relatedPkColumn);
        filter.setRelatedColumns(relatedColumns);
        return filter;
    }

    private List<Map<String, Object>> convertRowsToCamelCase(List<LinkedHashMap<String, Object>> rows)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        if (rows == null || rows.isEmpty())
        {
            return result;
        }
        for (Map<String, Object> row : rows)
        {
            result.add(convertRowToCamelCase(row));
        }
        return result;
    }

    private Map<String, Object> convertRowToCamelCase(Map<String, Object> row)
    {
        if (row == null || row.isEmpty())
        {
            return row;
        }
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : row.entrySet())
        {
            String key = entry.getKey();
            if (StringUtils.isEmpty(key))
            {
                continue;
            }
            result.put(StringUtils.toCamelCase(key), normalizeValue(entry.getValue()));
        }
        return result;
    }

    private Object normalizeValue(Object value)
    {
        if (value == null)
        {
            return null;
        }
        if (value instanceof Date)
        {
            return ((Date) value).getTime();
        }
        if (value instanceof LocalDateTime)
        {
            return ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        if (value instanceof LocalDate)
        {
            return ((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        if (value instanceof ZonedDateTime)
        {
            return ((ZonedDateTime) value).toInstant().toEpochMilli();
        }
        if (value instanceof OffsetDateTime)
        {
            return ((OffsetDateTime) value).toInstant().toEpochMilli();
        }
        if (value instanceof Instant)
        {
            return ((Instant) value).toEpochMilli();
        }
        return value;
    }

    private static int toInt(Object o)
    {
        if (o == null)
        {
            return 0;
        }
        if (o instanceof Boolean)
        {
            return ((Boolean) o).booleanValue() ? 1 : 0;
        }
        if (o instanceof Number)
        {
            return ((Number) o).intValue();
        }
        return 0;
    }

    private static boolean isSafeIdentifier(String s)
    {
        return s != null && s.matches("^[a-zA-Z0-9_]+$");
    }

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

    private List<DynamicEntityColumnValue> buildWriteValues(String entityKey, String tableName, String pkColumn,
            Map<String, Object> data, boolean update, String operatorUserName, Long operatorUserId)
    {
        Map<String, Object> input = data == null ? Collections.emptyMap() : data;
        Set<String> allowedFieldKeys = new LinkedHashSet<>(fieldConfigMapper.selectFieldKeysByEntityKey(entityKey));
        Map<String, String> fieldKeyAliases = buildFieldKeyAliases(allowedFieldKeys);
        Set<String> actualColumns = new LinkedHashSet<>(dynamicEntityMapper.selectColumnNames(tableName));
        if (allowedFieldKeys.isEmpty())
        {
            throw new ServiceException("No field_config found for entity_key: " + entityKey);
        }
        List<String> illegalKeys = new ArrayList<>();
        List<DynamicEntityColumnValue> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : input.entrySet())
        {
            String key = entry.getKey();
            if (StringUtils.isEmpty(key))
            {
                continue;
            }
            if ("createUser".equals(key) || "updateUser".equals(key))
            {
                continue;
            }
            String column = fieldKeyAliases.get(key);
            if (StringUtils.isEmpty(column))
            {
                illegalKeys.add(key);
                continue;
            }
            if (!actualColumns.contains(column) || pkColumn.equals(column) || isAuditManagedColumn(column))
            {
                continue;
            }
            DynamicEntityColumnValue value = new DynamicEntityColumnValue();
            value.setColumn(column);
            value.setValue(entry.getValue());
            values.add(value);
        }
        if (!illegalKeys.isEmpty())
        {
            throw new ServiceException("Unsupported fields: " + String.join(",", illegalKeys));
        }
        appendAuditValues(values, actualColumns, update, operatorUserName, operatorUserId);
        return values;
    }

    private void appendAuditValues(List<DynamicEntityColumnValue> values, Set<String> actualColumns, boolean update,
            String operatorUserName, Long operatorUserId)
    {
        if (!update)
        {
            appendIfColumnExists(values, actualColumns, "create_by", operatorUserName);
            appendIfColumnExists(values, actualColumns, "created_by", operatorUserId);
            appendIfColumnExists(values, actualColumns, "create_time", new Date());
            appendIfColumnExists(values, actualColumns, "created_time", System.currentTimeMillis());
        }
        appendIfColumnExists(values, actualColumns, "update_by", operatorUserName);
        appendIfColumnExists(values, actualColumns, "updated_by", operatorUserId);
        appendIfColumnExists(values, actualColumns, "update_time", new Date());
        appendIfColumnExists(values, actualColumns, "updated_time", System.currentTimeMillis());
    }

    private void appendIfColumnExists(List<DynamicEntityColumnValue> values, Set<String> actualColumns, String column,
            Object value)
    {
        if (!actualColumns.contains(column) || value == null)
        {
            return;
        }
        DynamicEntityColumnValue item = new DynamicEntityColumnValue();
        item.setColumn(column);
        item.setValue(value);
        values.add(item);
    }

    private boolean isAuditManagedColumn(String key)
    {
        return "create_by".equals(key)
                || "created_by".equals(key)
                || "create_time".equals(key)
                || "created_time".equals(key)
                || "update_by".equals(key)
                || "updated_by".equals(key)
                || "update_time".equals(key)
                || "updated_time".equals(key);
    }

    private Long extractPrimaryKeyValue(String pkColumn, Map<String, Object> data, List<DynamicEntityColumnValue> values)
    {
        if (data != null && data.containsKey(pkColumn))
        {
            return toLong(data.get(pkColumn));
        }
        String camelPkColumn = StringUtils.toCamelCase(pkColumn);
        if (data != null && data.containsKey(camelPkColumn))
        {
            return toLong(data.get(camelPkColumn));
        }
        for (DynamicEntityColumnValue value : values)
        {
            if (pkColumn.equals(value.getColumn()))
            {
                return toLong(value.getValue());
            }
        }
        return null;
    }

    private static class FieldFilterMeta
    {
        private String fieldKey;

        private boolean fuzzy;

        private String selectEntityKey;

        public String getFieldKey()
        {
            return fieldKey;
        }

        public void setFieldKey(String fieldKey)
        {
            this.fieldKey = fieldKey;
        }

        public boolean isFuzzy()
        {
            return fuzzy;
        }

        public void setFuzzy(boolean fuzzy)
        {
            this.fuzzy = fuzzy;
        }

        public String getSelectEntityKey()
        {
            return selectEntityKey;
        }

        public void setSelectEntityKey(String selectEntityKey)
        {
            this.selectEntityKey = selectEntityKey;
        }
    }
}
