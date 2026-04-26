package com.xiaohe.web.domain.Dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 工人信息表
 * @date Date : 2024年02月08日 19:25
 */

@Data
@TableName("sys_worker_user_view")
public class SysWorkerUserViewDto {
    /**
     * id
     */
    @TableId(value="id")
    private Long id;


    /**
     * 主列表id
     */
    private Long workerUserId;

    /**
     * 名称"
     */
    @ApiModelProperty("名称")
    @Excel(name = "名称", width = 30)
    private String name;


    /**
     * 生活费
     */
    @ApiModelProperty("生活费")
    @NotBlank(message = "生活费必填")
    @Excel(name = "生活费", width = 30)
    private BigDecimal livingExpenses;

    /**
     * 借支
     */
    @ApiModelProperty("总借支")
    @NotBlank(message = "总借支必填")
    @Excel(name = "总借支", width = 30)
    private BigDecimal lendMoney;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @NotBlank(message = "备注")
    @Excel(name = "备注", width = 30)
    private String remark;


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
