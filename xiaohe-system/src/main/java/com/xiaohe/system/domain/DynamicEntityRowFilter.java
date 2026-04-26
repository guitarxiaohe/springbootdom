package com.xiaohe.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Dynamic row filter; column must match field_config.field_key (whitelist on server).
 *
 * @author xiaohe
 */
public class DynamicEntityRowFilter implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String column;

    private String value;

    private boolean fuzzy;

    private boolean related;

    private String relatedTableName;

    private String relatedPkColumn;

    private List<String> relatedColumns;

    public String getColumn()
    {
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public boolean isFuzzy()
    {
        return fuzzy;
    }

    public void setFuzzy(boolean fuzzy)
    {
        this.fuzzy = fuzzy;
    }

    public boolean isRelated()
    {
        return related;
    }

    public void setRelated(boolean related)
    {
        this.related = related;
    }

    public String getRelatedTableName()
    {
        return relatedTableName;
    }

    public void setRelatedTableName(String relatedTableName)
    {
        this.relatedTableName = relatedTableName;
    }

    public String getRelatedPkColumn()
    {
        return relatedPkColumn;
    }

    public void setRelatedPkColumn(String relatedPkColumn)
    {
        this.relatedPkColumn = relatedPkColumn;
    }

    public List<String> getRelatedColumns()
    {
        return relatedColumns;
    }

    public void setRelatedColumns(List<String> relatedColumns)
    {
        this.relatedColumns = relatedColumns;
    }
}
