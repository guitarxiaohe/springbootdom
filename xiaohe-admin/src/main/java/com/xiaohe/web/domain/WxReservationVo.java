package com.xiaohe.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WxReservationVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("用户ID")
    private Long id;


    /**
     * openId
     */
    @ApiModelProperty("微信openId")
    private String openId;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 预约人
     */
    @ApiModelProperty("预约人")
    private String reservationName;

    /**
     * 性别
     */
    @ApiModelProperty("性别")

    private String sex;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String contactInformation;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String phone;

    /**
     * 事由
     */
    @ApiModelProperty("事由")
    private String reason;


    /** 不通过理由 */
    @Excel(name = "不通过理由")
    private String remark;


    /**
     * 接待人id
     */
    @ApiModelProperty("接待人id")
    private Long receptionById;

    /**
     * 接待人openid
     */
    @ApiModelProperty("接待人openid")
    private String receptionOpenId;

    /**
     * 接待人名称
     */
    @ApiModelProperty("接待人名称")
    private String receptionByName;

    /**
     * 状态（1：审核中，2：通过，3：未通过）
     */
    @ApiModelProperty("状态（1：审核中，2：通过，3：未通过）")
    private String state;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String createBy;


    /**
     * 调查问卷集合
     */
    private List<WxQuestionnaire> list;



}
