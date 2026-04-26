package com.xiaohe.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohe.common.annotation.Excel;
import lombok.Data;

/**
 * 微信调查问卷对象 questionnaire
 *
 * @author GuitarXioahe
 * @date 2023-10-17
 */
@Data
public class WxQuestionnaire {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** id */
    private Long reservationId;

    /** 调查内容 */
    @Excel(name = "调查内容")
    private String itemText;

    /** 是或者否（1：是，2：否） */
    @Excel(name = "是或者否", readConverterExp = "1=：是，2：否")
    private String answer;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;


}
