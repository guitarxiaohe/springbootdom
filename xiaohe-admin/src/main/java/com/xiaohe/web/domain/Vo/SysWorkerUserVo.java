package com.xiaohe.web.domain.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;
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
 * @Description: 工人信息表
 * @date Date : 2024年02月08日 19:25
 */

@Data
public class SysWorkerUserVo extends BaseEntity{
    /**
     * id
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    @ApiModelProperty("id")
    private Long id;




    /**
     * 头像
     */
    @ApiModelProperty("头像")
    @Excel(name = "头像", width = 30)
    private String avatar;

    /**
     * 工种
     */
    @ApiModelProperty("工种")
    @NotBlank(message = "工种必填")
    @Excel(name = "工种", width = 30)
    private String craft;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @NotBlank(message = "名称必填")
    @Excel(name = "名称", width = 30)
    private String name;

    /**
     * 状态 1启用 2停用
     */
    @ApiModelProperty("状态")
    @Excel(name = "状态", width = 30)
    private String state;


    /**
     * 是否为领班人 1是 2不是
     */
    @ApiModelProperty("是否为领班人 1是 2不是")
    @Excel(name = "是否为领班人", width = 30)
    private String gaffer;


    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号必填")
    @Excel(name = "手机号", width = 30)
    private String phone;


    /**
     * 银行卡号
     */
    @ApiModelProperty("银行卡号")
    @NotBlank(message = "银行卡号必填")
    @Excel(name = "银行卡号", width = 30)
    private String bankNumber;


    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    @NotBlank(message = "身份证号码必填")
    @Excel(name = "身份证号码", width = 30)
    private String cardNumber;

    /**
     * 户籍所在地
     */
    @ApiModelProperty("户籍所在地")
    @NotBlank(message = "户籍所在地必填")
    @Excel(name = "户籍所在地", width = 30)
    private String domicile;

    /**
     * 开户账号所在地
     */
    @ApiModelProperty("开户账号所在地")
    @NotBlank(message = "开户账号所在地必填")
    @Excel(name = "开户账号所在地", width = 30)
    private String accountLocation;

    /**
     * 日薪/一个工多少钱
     */
    @ApiModelProperty("日薪")
    @NotBlank(message = "日薪必填")
    @Excel(name = "日薪", width = 30)
    private BigDecimal perDiem;

    /**
     * 微信openid
     */
    @ApiModelProperty("微信openid")
    private String openId;

    /**
     * 总工时
     */
    @ApiModelProperty("总工时")
    @Excel(name = "总工时", width = 30)
    private BigDecimal manHour;

    /**
     * 总生活费
     */
    @ApiModelProperty("生活费")
    @NotBlank(message = "生活费必填")
    @Excel(name = "生活费", width = 30)
    private BigDecimal livingExpenses;

    /**
     * 总借支
     */
    @ApiModelProperty("总借支")
    @NotBlank(message = "总借支必填")
    @Excel(name = "总借支", width = 30)
    private BigDecimal lendMoney;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String createBy;

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
}
