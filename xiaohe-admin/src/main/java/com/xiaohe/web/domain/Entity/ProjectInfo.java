package com.xiaohe.web.domain.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.apache.poi.sl.usermodel.Background;

import java.util.Date;

/**
 * 【项目信息】对象 project_info
 *
 * @author xiaohe
 * @date 2025-11-13
 */
@Data
@TableName("project_info")
public class ProjectInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String projectType;

    /** 项目内容 */
    @Excel(name = "项目内容")
    private String projectContent;

    /** 面向终端（1：web，2：移动端，3：桌面端，4：其他）。字典类型：sys_client_type */
    @Excel(name = "面向终端", readConverterExp = "1=：web，2：移动端，3：桌面端，4：其他")
    private String projectClientSide;

    /** 技术栈id */
    @Excel(name = "技术栈", readConverterExp = "1=：web，2：移动端，3：桌面端，4：其他")
    private String technologyId;


    /** 项目开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目开始时间")
    private Date projectStartTime;

    /** 项目结束时间 */
    @Excel(name = "项目结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date projectEndTime;


    /** 项目截图 */
    @Excel(name = "项目截图")
    @ApiModelProperty("项目截图")
    private String projectScreenshot;

    /** 项目背景 */
    @Excel(name = "项目背景")
    @ApiModelProperty("项目背景")
    private String projectBackground;

    /** 项目职责 */
    @Excel(name = "项目职责")
    @ApiModelProperty("项目职责")
    private String projectResponsibilities;

}