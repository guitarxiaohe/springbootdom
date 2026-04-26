package com.xiaohe.system.domain;

import java.io.Serializable;

/**
 * 字段配置排序项
 *
 * @author xiaohe
 */
public class FieldConfigSortItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer sort;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }
}
