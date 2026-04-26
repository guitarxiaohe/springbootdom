package com.xiaohe.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 微信登录人员表对象 wx_user
 *
 * @author xiaohe
 * @date 2023-09-21
 */
@Data
public class WxUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id）")
    private Long id;

    /**
     * 微信openId
     */
    @ApiModelProperty("微信openId）")
//    @Excel(name = "微信openId")
    private String openId;



    /**
     * 手机号
     */
    @ApiModelProperty("手机号）")
    @Excel(name = "手机号")
    private String phone;

    /**
     * 微信昵称
     */
    @ApiModelProperty("名称")

    @Excel(name = "名称")
    private String name;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称）")
    @Excel(name = "部门名称")
    private String deptName;

    /**
     * 微信头像
     */
    @ApiModelProperty("微信头像")
    @Excel(name = "微信头像")
    private String avatar;



    /**
     * 是否为审核人（1：是，2：不是）
     */
    @ApiModelProperty("是否为审核人（1：是，2：不是）")
    @Excel(name = "是否为审核人", readConverterExp = "0=不是,1=待确认,2=是")
    private String isExamine;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


    private String value;
    private String label;
    /**
     * 子集
     */
    private WxUser children;

}
