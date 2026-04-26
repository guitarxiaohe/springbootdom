package com.xiaohe.web.domain.Vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月28日 17:26
 */

@Data
@TableName("sys_contract_assounts")
public class SysContractAssonantsVo extends BaseEntity {
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
    @Excel(name = "工时", width = 30)
    private Long projectId;

    /**
     * 工人id
     */
    @ApiModelProperty("工人id")
    private Long userId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @Excel(name = "项目名称", width = 30)
    private String contractName;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @Excel(name = "手机号", width = 30)
    private String phone;

    /**
     * 包工团队主键id
     */
    @ApiModelProperty("包工团队主键id")
    private Long contractId;

    /**
     * 领班人id
     */
    @ApiModelProperty("领班人id")
    private Long headId;

    /**
     * 工时
     */
    @ApiModelProperty("工时")
    @Excel(name = "工时", width = 30)
    private BigDecimal manHour;


    /**
     * 审核状态 1 审核中 2 通过 3 不通过
     */
    @ApiModelProperty("不通过审核意见")
    @Excel(name = "不通过审核意见", readConverterExp = "0=审核中,1=通过,2=不通过", width = 30)
    private String examineState;

    /**
     * 不通过审核意见
     */
    @ApiModelProperty("不通过审核意见")
    @Excel(name = "不通过审核意见", width = 30)
    private String examineRemark;
    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    @Excel(name = "审核人")
    private String examineBy;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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