package com.xiaohe.web.domain.WxDataAnalyze;

import lombok.Data;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.WxDataAnalyze
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年03月05日 17:51
 */

@Data
public class WXWeekDataDto {

    /**
     * 时间，格式为 yyyymm，如："201702"
     */
    private String refDate;

    /**
     * 打开次数（自然月内汇总）
     */
    private int sessionCnt;

    /**
     * 访问次数（自然月内汇总）
     */
    private int visitPv;

    /**
     * 访问人数（自然月内去重）
     */
    private int visitUv;

    /**
     * 新用户数（自然月内去重）
     */
    private int visitUvNew;

    /**
     * 人均停留时长 (浮点型，单位：秒)
     */
    private int stayTimeUv;

    /**
     * 次均停留时长 (浮点型，单位：秒)
     */
    private int stayTimeSession;

    /**
     * 平均访问深度 (浮点型)
     */
    private int visitDepth;


}