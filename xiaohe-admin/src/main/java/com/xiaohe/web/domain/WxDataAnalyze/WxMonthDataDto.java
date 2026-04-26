package com.xiaohe.web.domain.WxDataAnalyze;

import lombok.Data;

import java.util.List;

@Data
public class WxMonthDataDto {

    /**
     * 时间，格式为 yyyymm，如："201702"
     */
    private String ref_date;


    /**
     * 打开次数（自然月内汇总）
     */
    private String session_cnt;


    /**
     * 访问次数（自然月内汇总）
     */
    private String visit_pv;


    /**
     * 访问人数（自然月内去重）
     */
    private String visit_uv;

    /**
     * 新用户数（自然月内去重）
     */
    private String visit_uv_new;

    /**
     * 人均停留时长 (浮点型，单位：秒)
     */
    private String 	stay_time_uv;

    /**
     * 次均停留时长 (浮点型，单位：秒)
     */
    private String stay_time_session;

    /**
     * 平均访问深度 (浮点型)
     */
    private String visit_depth;

    /**
     * 平均访问深度 (浮点型)
     */

    /**
     * 开始时间
     */
    private String begin_date;

    /**
     * 结束时间
     */
    private String end_date;


    private List<WxMonthDataDto> list;



}
