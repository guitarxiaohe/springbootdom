package com.xiaohe.web.service;

import com.xiaohe.web.domain.WxQuestionnaire;

import java.util.List;

public interface WxQuestionnaireService  {

    /**
     * 新增调查问卷维护
     *
     * @param wxQuestionnaire 调查问卷维护
     * @return 结果
     */
    public int addQuestionnaire(WxQuestionnaire wxQuestionnaire);

    /**
     * 通过预约表id获取对应的调查问卷
     *
     * @param reservationId 预约表主键id
     * @return 结果
     */
    public List<WxQuestionnaire> selQuestionnaireByReservationId(Long reservationId);

}
