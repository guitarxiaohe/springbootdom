package com.xiaohe.web.domain.Dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月29日 11:37
 */

@Data
@TableName("sys_statistics")
public class SysStatisticsDto {

    /**
     * id
     */
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId("id")
    private Long id;


    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    @Excel(name = "姓名", width = 30)
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @Excel(name = "手机号", width = 30)
    private String phone;

    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    @NotBlank(message = "身份证号码必填")
    @Excel(name = "身份证号码", width = 30)
    private String cardNumber;

    /**
     * 银行卡号
     */
    @ApiModelProperty("银行卡号")
    @NotBlank(message = "银行卡号必填")
    @Excel(name = "银行卡号", width = 30)
    private String bankNumber;

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
     * 工种 1=大工,2=小工
     */
    @ApiModelProperty("工种")
    @NotBlank(message = "工种必填")
    @Excel(name = "工种",readConverterExp = "1=大工,2=小工", width = 30)
    private String craft;

    /**
     * 点工工时
     */
    @ApiModelProperty("点工工时")
    @Excel(name = "点工工时", width = 30)
    private BigDecimal dayManHour;

    /**
     * 工时
     */
    @ApiModelProperty("工时")
    @NotBlank(message = "工时必填")
    @Excel(name = "工时", width = 30)
    private BigDecimal manHour;

    /**
     * 在岗天数
     */
    @ApiModelProperty("在岗天数")
    @Excel(name = "在岗天数", width = 30)
    private BigDecimal days;

    /**
     * 总工资
     */
    @ApiModelProperty("总工资")
    @Excel(name = "总工资", width = 30)
    private BigDecimal wage;

    /**
     * 总生活费
     */
    @ApiModelProperty("总生活费")
    @Excel(name = "总生活费", width = 30)
    private BigDecimal livingExpenses;

    /**
     * 总借支
     */
    @ApiModelProperty("总借支")
    @Excel(name = "总借支", width = 30)
    private BigDecimal lendMoney;

    /**
     * 应发工资
     */
    @ApiModelProperty("应发工资")
    @Excel(name = "应发工资", width = 30)
    private BigDecimal wagePayable;


}