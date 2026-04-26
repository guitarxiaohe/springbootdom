package com.xiaohe.system.domain;

import java.io.Serializable;

/**
 * Dynamic insert/update column-value pair; column must be server-side whitelisted.
 *
 * @author xiaohe
 */
public class DynamicEntityColumnValue implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String column;

    private Object value;

    public String getColumn()
    {
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
}
