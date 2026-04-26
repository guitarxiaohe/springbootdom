package com.xiaohe.system.service;

import java.util.List;
import com.xiaohe.system.domain.FieldConfig;
import com.xiaohe.system.domain.FieldConfigSortItem;

/**
 * 字段配置 服务层
 *
 * @author xiaohe
 */
public interface IFieldConfigService
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
    public FieldConfig selectFieldConfigByEntityKeyAndFieldKey(String entityKey, String fieldKey);

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
     * 批量删除字段配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFieldConfigByIds(Long[] ids);

    /**
     * 删除字段配置信息
     *
     * @param id 字段配置ID
     * @return 结果
     */
    public int deleteFieldConfigById(Long id);

    /**
     * 批量更新排序
     *
     * @param entityKey 实体标识
     * @param items 排序项列表
     * @param updatedBy 更新人
     * @return 结果
     */
    public int updateSortBatch(String entityKey, List<FieldConfigSortItem> items, Long updatedBy);
}
