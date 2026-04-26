package com.xiaohe.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.xiaohe.system.domain.FieldConfig;

/**
 * 字段配置 数据层
 *
 * @author xiaohe
 */
public interface FieldConfigMapper
{
    /**
     * 查询字段配置
     *
     * @param id 字段配置ID
     * @return 字段配置
     */
    public FieldConfig selectFieldConfigById(Long id);

    /**
     * 查询字段配置列表
     *
     * @param fieldConfig 字段配置
     * @return 字段配置集合
     */
    public List<FieldConfig> selectFieldConfigList(FieldConfig fieldConfig);

    /**
     * 根据实体标识查询字段配置列表
     *
     * @param entityKey 实体标识
     * @return 字段配置集合
     */
    public List<FieldConfig> selectFieldConfigByEntityKey(String entityKey);

    /**
     * 根据实体标识和字段标识查询字段配置
     *
     * @param entityKey 实体标识
     * @param fieldKey 字段标识
     * @return 字段配置
     */
    public FieldConfig selectFieldConfigByEntityKeyAndFieldKey(@Param("entityKey") String entityKey, @Param("fieldKey") String fieldKey);

    /**
     * Count field_config row for entityKey + fieldKey (used for safe order-by whitelist).
     */
    int countFieldConfigByEntityKeyAndFieldKey(@Param("entityKey") String entityKey, @Param("fieldKey") String fieldKey);

    /**
     * field_key and is_fuzzy_search for dynamic list filters.
     */
    List<Map<String, Object>> selectFieldFilterMetaByEntityKey(String entityKey);

    /**
     * All allowed field_key values for an entity_key.
     */
    List<String> selectFieldKeysByEntityKey(String entityKey);

    /**
     * 新增字段配置
     *
     * @param fieldConfig 字段配置
     * @return 结果
     */
    public int insertFieldConfig(FieldConfig fieldConfig);

    /**
     * 修改字段配置
     *
     * @param fieldConfig 字段配置
     * @return 结果
     */
    public int updateFieldConfig(FieldConfig fieldConfig);

    /**
     * 删除字段配置
     *
     * @param id 字段配置ID
     * @return 结果
     */
    public int deleteFieldConfigById(Long id);

    /**
     * 批量删除字段配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFieldConfigByIds(Long[] ids);

    /**
     * 批量更新排序
     *
     * @param list 字段配置列表
     * @return 结果
     */
    public int updateSortBatch(List<FieldConfig> list);

    /**
     * 校验指定 id 集合是否都属于同一个 entity_key
     */
    int countFieldConfigByEntityKeyAndIds(@Param("entityKey") String entityKey, @Param("ids") List<Long> ids);
}
