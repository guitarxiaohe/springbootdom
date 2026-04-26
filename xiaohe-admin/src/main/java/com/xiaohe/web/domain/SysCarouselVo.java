package com.xiaohe.web.domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.xiaohe.common.annotation.Excel;

import java.util.Date;

/**
 * 轮播图对象 sys_carousel
 *
 * @author xiaohe
 * @date 2023-09-26
 */
@Data
public class SysCarouselVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("id")
    private Long id;

    /** 图片地址 */
    @ApiModelProperty("图片地址")
    @Excel(name = "图片地址")
    private String url;

    /** 跳转地址 */
    @ApiModelProperty("跳转地址")
    @Excel(name = "跳转地址")
    private String path;

    /** 图片介绍 */
    @ApiModelProperty("图片介绍")
    @Excel(name = "图片介绍")
    private String name;

    /** 创建人 */
    @ApiModelProperty("创建人")
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}