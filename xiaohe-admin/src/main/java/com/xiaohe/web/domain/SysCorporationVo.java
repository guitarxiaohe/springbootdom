package com.xiaohe.web.domain;

import com.xiaohe.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysCorporationVo {
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("id")
    private Long id;

    /** 文字内容（长文字） */
    @Excel(name = "文字内容", readConverterExp = "长=文字")
    @ApiModelProperty("文字内容")
    private String content;

    /** 图片 */
    @Excel(name = "图片")
    @ApiModelProperty("图片")
    private String imgUrl;

    /** 预约须知 */
    @Excel(name = "预约须知")
    @ApiModelProperty("预约须知")
    private String instructions;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty("创建时间")
    private String createTime;

    /** 创建人 */
    @Excel(name = "创建人")
    @ApiModelProperty("创建人")
    private String createBy;

    /** 管理员手机号 */
    @Excel(name = "管理员手机号")
    @ApiModelProperty("管理员手机号")
    private String phoneNumber;

}
