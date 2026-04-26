package com.xiaohe.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 字段配置排序请求
 *
 * @author xiaohe
 */
public class FieldConfigSortRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String entityKey;

    private List<FieldConfigSortItem> items;

    public String getEntityKey()
    {
        return entityKey;
    }

    public void setEntityKey(String entityKey)
    {
        this.entityKey = entityKey;
    }

    public List<FieldConfigSortItem> getItems()
    {
        return items;
    }

    public void setItems(List<FieldConfigSortItem> items)
    {
        this.items = items;
    }
}
