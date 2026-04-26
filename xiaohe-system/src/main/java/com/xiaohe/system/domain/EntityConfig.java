package com.xiaohe.system.domain;

import java.io.Serializable;

/**
 * entity_config: maps entity_key to physical table_name.
 *
 * @author xiaohe
 */
public class EntityConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String entityKey;

    private String entityName;

    private String tableName;

    private String description;

    private Integer sort;

    private Integer isVisible;

    private Long createdBy;

    private Long createdTime;

    private Long updatedBy;

    private Long updatedTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEntityKey()
    {
        return entityKey;
    }

    public void setEntityKey(String entityKey)
    {
        this.entityKey = entityKey;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getIsVisible()
    {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible)
    {
        this.isVisible = isVisible;
    }

    public Long getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy)
    {
        this.createdBy = createdBy;
    }

    public Long getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime)
    {
        this.createdTime = createdTime;
    }

    public Long getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedTime()
    {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime)
    {
        this.updatedTime = updatedTime;
    }
}
