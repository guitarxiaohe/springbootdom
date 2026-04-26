package com.xiaohe.web.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: 包工团队
 * @date Date : 2024年02月28日 14:06
 */

@Data
@TableName("sys_contract")
public class SysContract {

    /**
     * id
     */
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId("id")
    private Long id;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;

    /**
     * 领班人id
     */
    @ApiModelProperty("领班人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long headId;

    /**
     * 包工团队名称
     * , readConverterExp = "0=审核中,1=通过,2=不通过"
     */
    @ApiModelProperty("包工团队名称")
    @Excel(name = "包工团队名称",width = 30)
    private String contractName;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @Excel(name = "状态",width = 30)
    private String state;


    /**
     * 总面积
     */
    @ApiModelProperty("总面积")
    @Excel(name = "总面积",width = 30)
    private String area;

    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    @Excel(name = "总金额",width = 30)
    private BigDecimal amount;

    /** 创建人 */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String createBy;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

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