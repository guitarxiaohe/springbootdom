package com.xiaohe.system.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.xiaohe.system.domain.DynamicEntityColumnValue;
import com.xiaohe.system.domain.DynamicEntityRowFilter;

/**
 * Generic row query/delete after resolving table from entity_config.
 *
 * @author xiaohe
 */
public interface DynamicEntityMapper
{
    List<LinkedHashMap<String, Object>> selectEntityRowList(@Param("tableName") String tableName,
            @Param("filters") List<DynamicEntityRowFilter> filters);

    LinkedHashMap<String, Object> selectEntityRowById(@Param("tableName") String tableName,
            @Param("pkColumn") String pkColumn, @Param("id") Long id);

    int deleteEntityRowsByIds(@Param("tableName") String tableName, @Param("pkColumn") String pkColumn,
            @Param("ids") Long[] ids);

    int updateEntityRowById(@Param("tableName") String tableName, @Param("pkColumn") String pkColumn,
            @Param("id") Long id, @Param("values") List<DynamicEntityColumnValue> values);

    int insertEntityRow(@Param("tableName") String tableName, @Param("values") List<DynamicEntityColumnValue> values);

    String selectPrimaryKeyColumn(@Param("tableName") String tableName);

    List<String> selectColumnNames(@Param("tableName") String tableName);

    Long selectLastInsertId();

    /**
     * Delete rows by a single column-value condition.
     * tableName and column must be white-listed by caller via {@code ^[a-zA-Z0-9_]+$}.
     */
    int deleteRowsByColumn(@Param("tableName") String tableName, @Param("column") String column,
            @Param("value") Object value);
}
