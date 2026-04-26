package com.xiaohe.web.service;

import com.xiaohe.web.domain.Dto.SysLayoutNumDto;
import com.xiaohe.web.domain.WxDataAnalyze.WXWeekDataDto;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月21日 13:39
 */

public interface SysLayoutService {
    /**
     * 获取项目数量 人员数量 领班数量
     * @return SysLayoutDto
     */
    SysLayoutNumDto getNum();


    /**
     * 获取小程序 趋势信息
     * @return SysLayoutDto
     */
    WXWeekDataDto getWxTendency(String wxType);
}
