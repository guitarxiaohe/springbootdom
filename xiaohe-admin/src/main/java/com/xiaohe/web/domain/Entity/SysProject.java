package com.xiaohe.web.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: 项目表
 * @date Date : 2024年02月08日 19:45
 */

@Data
@TableName("sys_project")
public class SysProject {
    /**
     * 项目id
     */
    
    @ApiModelProperty("项目id")
    @TableId("project_id")
    private Long projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @NotBlank(message = "项目名称必填")
    @Excel(name = "项目名称", width = 30)
    private String projectName;

    /**
     * 项目领班人
     */
    @ApiModelProperty("项目领班人")
    @NotBlank(message = "项目领班人")
    @Excel(name = "项目领班人", width = 30)
    
    private Long projectHeadId;




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