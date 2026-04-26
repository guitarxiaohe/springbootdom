package com.xiaohe.web.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.xiaohe.common.annotation.Excel;
import com.xiaohe.common.core.domain.BaseEntity;

/**
 * 预约对象 reservation
 * 
 * @author GuitarXiaohe
 * @date 2023-09-20
 */
@Data
public class Reservation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 查询*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 预约人 */
    @Excel(name = "预约人")
    private String reservationName;

    /**
     * 性别  1：男，2：女
     */
    @Excel(name = "性别")
    private String sex;


    /** 联系方式 */
//    @Excel(name = "联系方式")
    private String contactInformation;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;


    /** 事由 */
    @Excel(name = "事由")
    private String reason;

    /** 不通过理由 */
    @Excel(name = "不通过理由")
    private String remark;

    /** 接待人id */
    @Excel(name = "接待人id")
    private Long receptionById;

    /** 接待人名称 */
    @Excel(name = "接待人名称")
    private String receptionByName;

    /** 接待人电话 */
    @Excel(name = "接待人电话")
    private String receptionByPhone;

    /** openid */
    // @Excel(name = "openid")
    private String openId;

    /** 接待人openid */
//    @Excel(name = "接待人openid")
    private String receptionOpenId;

    /** 状态（1：审核中，2：通过，3：未通过） */
    @Excel(name = "状态", readConverterExp = "1=审核中,2=通过,3=未通过")
    private String state;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;

    /** 审核人 */
    @Excel(name = "审核人")
    private String examineBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 开始时间 查询*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 平均时长
     */
    private String takeTime;

    /**
     * 总条数
     */
    private  int totalNum;
}
