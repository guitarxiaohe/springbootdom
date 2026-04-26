package com.xiaohe.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.common.exception.ServiceException;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.system.domain.FieldConfig;
import com.xiaohe.system.domain.FieldConfigSortItem;
import com.xiaohe.system.mapper.FieldConfigMapper;
import com.xiaohe.system.service.IFieldConfigService;

/**
 * 字段配置 服务层实现
 *
 * @author xiaohe
 */
@Service
public class FieldConfigServiceImpl implements IFieldConfigService
{
    @Autowired
    private FieldConfigMapper fieldConfigMapper;

    /**
     * 查询字段配置
     *
     * @param id 字段配置ID
     * @return 字段配置
     */
    @Override
    public FieldConfig selectFieldConfigById(Long id)
    {
        return fieldConfigMapper.selectFieldConfigById(id);
    }

    /**
     * 查询字段配置列表
     *
     * @param fieldConfig 字段配置
     * @return 字段配置集合
     */
    @Override
    public List<FieldConfig> selectFieldConfigList(FieldConfig fieldConfig)
    {
        return fieldConfigMapper.selectFieldConfigList(fieldConfig);
    }

    /**
     * 根据实体标识查询字段配置列表
     *
     * @param entityKey 实体标识
     * @return 字段配置集合
     */
    @Override
    public List<FieldConfig> selectFieldConfigByEntityKey(String entityKey)
    {
        return fieldConfigMapper.selectFieldConfigByEntityKey(entityKey);
    }

    /**
     * 根据实体标识和字段标识查询字段配置
     *
     * @param entityKey 实体标识
     * @param fieldKey 字段标识
     * @return 字段配置
     */
    @Override
    public FieldConfig selectFieldConfigByEntityKeyAndFieldKey(String entityKey, String fieldKey)
    {
        return fieldConfigMapper.selectFieldConfigByEntityKeyAndFieldKey(entityKey, fieldKey);
    }

    /**
     * 新增字段配置
     *
     * @param fieldConfig 字段配置
     * @return 结果
     */
    @Override
    public int insertFieldConfig(FieldConfig fieldConfig)
    {
        return fieldConfigMapper.insertFieldConfig(fieldConfig);
    }

    /**
     * 修改字段配置
     *
     * @param fieldConfig 字段配置
     * @return 结果
     */
    @Override
    public int updateFieldConfig(FieldConfig fieldConfig)
    {
        return fieldConfigMapper.updateFieldConfig(fieldConfig);
    }

    /**
     * 批量删除字段配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFieldConfigByIds(Long[] ids)
    {
        return fieldConfigMapper.deleteFieldConfigByIds(ids);
    }

    /**
     * 删除字段配置信息
     *
     * @param id 字段配置ID
     * @return 结果
     */
    @Override
    public int deleteFieldConfigById(Long id)
    {
        return fieldConfigMapper.deleteFieldConfigById(id);
    }

    /**
     * 批量更新排序
     *
     * @param entityKey 实体标识
     * @param items 排序项列表
     * @param updatedBy 更新人
     * @return 结果
     */
    @Override
    public int updateSortBatch(String entityKey, List<FieldConfigSortItem> items, Long updatedBy)
    {
        if (StringUtils.isEmpty(entityKey))
        {
            throw new ServiceException("entityKey不能为空");
        }
        if (items == null || items.isEmpty())
        {
            throw new ServiceException("排序项不能为空");
        }
        List<Long> ids = new ArrayList<>(items.size());
        List<FieldConfig> list = new ArrayList<>(items.size());
        for (FieldConfigSortItem item : items)
        {
            if (item == null || item.getId() == null || item.getSort() == null)
            {
                throw new ServiceException("排序项必须包含id和sort");
            }
            ids.add(item.getId());
            FieldConfig fieldConfig = new FieldConfig();
            fieldConfig.setId(item.getId());
            fieldConfig.setSort(item.getSort());
            fieldConfig.setUpdatedBy(updatedBy);
            list.add(fieldConfig);
        }
        int count = fieldConfigMapper.countFieldConfigByEntityKeyAndIds(entityKey, ids);
        if (count != ids.size())
        {
            throw new ServiceException("存在不属于当前entityKey的字段配置");
        }
        return fieldConfigMapper.updateSortBatch(list);
    }
}
