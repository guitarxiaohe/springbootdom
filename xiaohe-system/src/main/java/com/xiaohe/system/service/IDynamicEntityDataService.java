package com.xiaohe.system.service;

import java.util.List;
import java.util.Map;

/**
 * Generic business-table list/delete using entity_config + field_config.
 *
 * @author xiaohe
 */
public interface IDynamicEntityDataService
{
    boolean isAllowedOrderColumn(String entityKey, String columnUnderScore);

    List<Map<String, Object>> selectEntityRowList(String entityKey);

    List<Map<String, Object>> selectEntityRowList(String entityKey, Integer pageNum, Integer pageSize, String orderBy,
            Boolean reasonable);

    Map<String, Object> selectEntityRowById(String entityKey, Long id);

    int deleteEntityRowsByIds(String entityKey, Long[] ids);

    Map<String, Object> insertEntityRow(String entityKey, Map<String, Object> data, String operatorUserName, Long operatorUserId);

    Map<String, Object> updateEntityRow(String entityKey, Long id, Map<String, Object> data, String operatorUserName,
            Long operatorUserId);
}
