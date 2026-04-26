package com.xiaohe.web.service;
import com.xiaohe.web.domain.Reservation;
import com.xiaohe.web.domain.WxReservationVo;


public interface WxReservationService {


    /**
     * 新增预约并且发送消息
     *
     * @param wxReservationVo 预约
     * @return 结果
     */
    public int insertReservation( WxReservationVo wxReservationVo) throws Exception;

    /**
     * 新增预约并且发送消息
     *
     * @param reservation 预约
     * @return 结果
     */
    public Boolean upState( Reservation reservation) throws Exception;



}
