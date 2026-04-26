package com.xiaohe.web.domain.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain
 * @Description: 记账表
 * @date Date : 2024年02月08日 19:25
 */

@Data
public class SysAccountsDto {
    /**
     * 记功表id
     */
    @ApiModelProperty("记功表id")
    private Long accountsId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @Excel(name = "项目名称", width = 30)
    private String projectName;

    /**
     * 项目id
     */
    @ApiModelProperty("项目名称")
    @NotBlank(message = "项目名称必填")
    private Long projectId;


    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @NotBlank(message = "名称必填")
    @Excel(name = "名称", width = 30)
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号必填")
    @Excel(name = "手机号", width = 30)
    private String phone;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id")
    private Long userId;

    /**
     * 工时
     */
    @ApiModelProperty("工时")
    @NotBlank(message = "工时必填")
    @Excel(name = "工时", width = 30)
    private BigDecimal manHour;




    /**
     * 审核状态 1 审核中 2 通过 3 不通过
     */
    @ApiModelProperty("不通过审核意见")
    @Excel(name = "不通过审核意见", readConverterExp = "0=审核中,1=通过,2=不通过",width = 30)
    private String examineState;

    /**
     * 不通过审核意见
     */
    @ApiModelProperty("不通过审核意见")
    @Excel(name = "不通过审核意见", width = 30)
    private String examineRemark;

    /**
     * 当前时间
     */
    @ApiModelProperty("当前时间")
    @NotBlank(message = "当前时间必填")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "当前时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date  createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String createBy;

    /** 审核人 */
    @ApiModelProperty("审核人")
    @Excel(name = "审核人")
    private String examineBy;

    /** 审核时间 */
    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date  examineTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String updateBy;

    /**
     * 类型 0=点工,1=包工
     */
    @ApiModelProperty("类型 0=点工,1=包工")
    @Excel(name = "类型", readConverterExp = "0=点工,1=包工")
    private String orderType;
}
