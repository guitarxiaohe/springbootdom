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
import java.util.concurrent.ConcurrentHashMap;
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
import com.github.pagehelper.Page;
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

    /******************************** 元数据缓存 ********************************/

    private static final long CACHE_TTL_MS = 60_000; // 1 分钟
    private static class CacheEntry<T> { final T value; final long expireAt; CacheEntry(T v) { this.value = v; this.expireAt = System.currentTimeMillis() + CACHE_TTL_MS; } boolean expired() { return System.currentTimeMillis() > expireAt; } }
    private final ConcurrentHashMap<String, CacheEntry<EntityConfig>> entityConfigCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CacheEntry<List<com.xiaohe.system.domain.FieldConfig>>> fieldConfigCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CacheEntry<String>> pkColumnCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CacheEntry<Set<String>>> columnNamesCache = new ConcurrentHashMap<>();

    private EntityConfig getCachedEntityConfig(String entityKey) {
        CacheEntry<EntityConfig> entry = entityConfigCache.get(entityKey);
        if (entry != null && !entry.expired()) return entry.value;
        EntityConfig ec = entityConfigMapper.selectEntityConfigByEntityKey(entityKey);
        if (ec != null) entityConfigCache.put(entityKey, new CacheEntry<>(ec));
        return ec;
    }

    private List<com.xiaohe.system.domain.FieldConfig> getCachedFieldConfigs(String entityKey) {
        CacheEntry<List<com.xiaohe.system.domain.FieldConfig>> entry = fieldConfigCache.get(entityKey);
        if (entry != null && !entry.expired()) return entry.value;
        com.xiaohe.system.domain.FieldConfig query = new com.xiaohe.system.domain.FieldConfig();
        query.setEntityKey(entityKey);
        List<com.xiaohe.system.domain.FieldConfig> list = fieldConfigMapper.selectFieldConfigList(query);
        List<com.xiaohe.system.domain.FieldConfig> safeList = list != null ? list : Collections.emptyList();
        fieldConfigCache.put(entityKey, new CacheEntry<>(safeList));
        return safeList;
    }

    private String getCachedPkColumn(String tableName) {
        CacheEntry<String> entry = pkColumnCache.get(tableName);
        if (entry != null && !entry.expired()) return entry.value;
        String pk = dynamicEntityMapper.selectPrimaryKeyColumn(tableName);
        if (pk != null) pkColumnCache.put(tableName, new CacheEntry<>(pk));
        return pk;
    }

    private Set<String> getCachedColumnNames(String tableName) {
        CacheEntry<Set<String>> entry = columnNamesCache.get(tableName);
        if (entry != null && !entry.expired()) return entry.value;
        List<String> cols = dynamicEntityMapper.selectColumnNames(tableName);
        Set<String> set = cols != null ? new LinkedHashSet<>(cols) : Collections.emptySet();
        columnNamesCache.put(tableName, new CacheEntry<>(set));
        return set;
    }

    // 清除元数据缓存（field_config/entity_config 变更后调用）
    public void evictMetadataCache(String entityKey) {
        entityConfigCache.remove(entityKey);
        fieldConfigCache.remove(entityKey);
    }

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
        String columns = buildReadableColumnList(entityKey, ec.getTableName(), requirePrimaryKeyColumn(ec.getTableName()));
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
        String columns = buildReadableColumnList(entityKey, ec.getTableName(), requirePrimaryKeyColumn(ec.getTableName()));
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
        String columns = buildReadableColumnList(entityKey, ec.getTableName(), pkColumn);
        return convertRowToCamelCase(dynamicEntityMapper.selectEntityRowById(ec.getTableName(), pkColumn, id, columns));
    }

    /**
     * 从 field_config 构建动态查询字段白名单。
     */
    private String buildReadableColumnList(String entityKey, String tableName, String pkColumn)
    {
        List<com.xiaohe.system.domain.FieldConfig> configs = getCachedFieldConfigs(entityKey);
        if (configs == null || configs.isEmpty())
        {
            throw new ServiceException("No field_config found for entity_key: " + entityKey);
        }
        Set<String> actualColumns = getCachedColumnNames(tableName);
        LinkedHashSet<String> readableColumns = new LinkedHashSet<>();
        appendReadableColumn(readableColumns, actualColumns, pkColumn);
        for (com.xiaohe.system.domain.FieldConfig fc : configs)
        {
            if (!isReadableField(fc))
            {
                continue;
            }
            appendReadableColumn(readableColumns, actualColumns, fc.getFieldKey());
        }
        if (readableColumns.isEmpty())
        {
            throw new ServiceException("No readable columns configured for entity_key: " + entityKey);
        }
        StringBuilder sb = new StringBuilder();
        for (String column : readableColumns)
        {
            if (sb.length() > 0)
            {
                sb.append(", ");
            }
            sb.append("`").append(column).append("`");
        }
        return sb.toString();
    }

    /**
     * 判断字段是否需要被动态查询读取。
     */
    private boolean isReadableField(com.xiaohe.system.domain.FieldConfig fc)
    {
        if (fc == null || StringUtils.isEmpty(fc.getFieldKey()))
        {
            return false;
        }
        if (fc.getIsVisible() != null && fc.getIsVisible() == 1)
        {
            return true;
        }
        String fieldType = StringUtils.trim(fc.getFieldType());
        String fieldRole = StringUtils.trim(fc.getFieldRole());
        return "by".equalsIgnoreCase(fieldType)
                || "file".equalsIgnoreCase(fieldType)
                || "createUser".equalsIgnoreCase(fieldRole)
                || "updateUser".equalsIgnoreCase(fieldRole)
                || "fileInfo".equalsIgnoreCase(fieldRole);
    }

    /**
     * 追加经过物理表列校验的安全列名。
     */
    private void appendReadableColumn(Set<String> readableColumns, Set<String> actualColumns, String column)
    {
        if (StringUtils.isEmpty(column) || !isSafeIdentifier(column) || !actualColumns.contains(column))
        {
            return;
        }
        readableColumns.add(column);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Map<String, Object>> convertRowsToCamelCase(List<LinkedHashMap<String, Object>> rows)
    {
        if (rows == null || rows.isEmpty())
        {
            return new ArrayList<>();
        }
        // 保留 PageHelper 的 Page 元数据（total 等），避免 getDataTable 拿不到 total
        if (rows instanceof Page)
        {
            @SuppressWarnings("rawtypes")
            Page page = (Page) rows;
            Page<Map<String, Object>> result = new Page<>(page.getPageNum(), page.getPageSize());
            result.setTotal(page.getTotal());
            for (Object item : page)
            {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> row = (LinkedHashMap<String, Object>) item;
                result.add(convertRowToCamelCase(row));
            }
            return result;
        }
        List<Map<String, Object>> result = new ArrayList<>();
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
        EntityConfig ec = getCachedEntityConfig(entityKey);
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
        String pkColumn = getCachedPkColumn(tableName);
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
