package com.xiaohe.web.domain.WxDataAnalyze;

import lombok.Data;

import java.util.List;

@Data
public class WxDataPorfileDto {
    /**
     * 日期，格式为 yyyymmdd
     */
    private String ref_date;

    /**
     * 累计用户数
     */
    private String visit_total;

    /**
     * 转发次数
     */
    private String share_pv;

    /**
     * 转发人数
     */
    private String share_uv;

    /**
     * 开始时间
     */
    private String begin_date;

    /**
     * 结束时间
     */
    private String end_date;

    private List<WxDataPorfileDto> list;
}
