package com.xiaohe.common.core.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * WebSocket消息模型
 *
 * @author xiaohe
 */
public class WsMessage implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 消息类型 notice/system/alert */
    private String type;

    /** 标题 */
    private String title;

    /** 正文 */
    private String text;

    /** 跳转路径 */
    private String path;

    /** 筛选参数 */
    private Map<String, String> params;

    public WsMessage()
    {
    }

    public WsMessage(String type, String title, String text, String path, Map<String, String> params)
    {
        this.type = type;
        this.title = title;
        this.text = text;
        this.path = path;
        this.params = params;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public Map<String, String> getParams()
    {
        return params;
    }

    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }
}
