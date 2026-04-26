package com.xiaohe.web.domain.Export;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.xiaohe.common.annotation.Excel;

import java.util.Date;

/**
 * 发送消息记录对象 sys_records
 *
 * @author GuitarXiaohe
 * @date 2023-10-11
 */
@Data
@TableName("wx_records")
public class SysRecords  {
    private static final long serialVersionUID = 1L;

    /**
     * 发送信息模板状态
     */
    @Excel(name = "发送信息模板状态")

    private String sendState;

    /**
     * 信息模板类型
     */
    @Excel(name = "信息模板类型")
    private String sendType;

    /**
     * id
     */
    @TableId("id")
    private Long id;

    /**
     * 发送人
     */
    @Excel(name = "发送人")
    private String sendBy;

    /**
     * 接受人
     */
    @Excel(name = "接受人")
    private String acceptBy;

    /**
     * 接收人电话
     */
    @Excel(name = "接收人电话")
    private String acceptPhone;

    /**
     * 发送人电话
     */
    @Excel(name = "发送人电话")
    private String sendByPhone;

    /**
     * 接收人openId
     */
    @Excel(name = "接收人openId")
    private String acceptOpenId;

    /**
     * 微信官方返回提示
     */
    @Excel(name = "微信官方返回提示")
    private String errorMsg;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}