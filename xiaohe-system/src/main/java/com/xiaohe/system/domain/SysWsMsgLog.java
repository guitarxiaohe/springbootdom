package com.xiaohe.system.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.core.domain.BaseEntity;

/**
 * WebSocket消息日志表 sys_ws_msg_log
 *
 * @author xiaohe
 */
public class SysWsMsgLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long msgId;

    /** 消息类型 notice/system/alert */
    private String msgType;

    /** 标题 */
    private String title;

    /** 正文 */
    private String content;

    /** 跳转路径 */
    private String path;

    /** JSON格式筛选参数 */
    @TableField("params")
    private String queryParams;

    /** 目标用户ID，null=广播 */
    private Long targetUserId;

    /** 发送状态 0=待发送 1=已发送 2=发送失败 */
    private String sendStatus;

    /** 实际发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    public Long getMsgId()
    {
        return msgId;
    }

    public void setMsgId(Long msgId)
    {
        this.msgId = msgId;
    }

    public String getMsgType()
    {
        return msgType;
    }

    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getQueryParams()
    {
        return queryParams;
    }

    public void setQueryParams(String queryParams)
    {
        this.queryParams = queryParams;
    }

    public Long getTargetUserId()
    {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId)
    {
        this.targetUserId = targetUserId;
    }

    public String getSendStatus()
    {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus)
    {
        this.sendStatus = sendStatus;
    }

    public Date getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(Date sendTime)
    {
        this.sendTime = sendTime;
    }
}
