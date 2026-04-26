package com.xiaohe.web.domain.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Entity
 * @Description: 项目表
 * @date Date : 2024年02月08日 19:45
 */

@Data
public class SysProjectUserDto implements Serializable {
    /**
     * 项目id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("项目id")
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
     * 项目领班人
     */
    @ApiModelProperty("项目领班人")
    @NotBlank(message = "项目领班人")
    @Excel(name = "项目领班人", width = 30)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String projectHeadName;



    private List<SysWorkerUser> userList;

    /**
     * 人员 工人ids
     */
    @ApiModelProperty("工人")
    @NotBlank(message = "工人")
//    @Excel(name = "工人", width = 30)
    private List<String>   userIds;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建人 */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String createBy;

    /** 修改时间 */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 修改人 */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String updateBy;

}