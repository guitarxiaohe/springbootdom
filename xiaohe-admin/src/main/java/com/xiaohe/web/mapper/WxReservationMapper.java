package com.xiaohe.web.mapper;
import com.xiaohe.web.domain.WxReservationVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 预约Mapper接口
 * 
 * @author GuitarXiaohe
 * @date 2023-09-20
 */
@Mapper
public interface WxReservationMapper
{

    /**
     * 新增预约
     * 
     * @param wxReservationVo 预约
     * @return 结果
     */
    public int insertWxReservation(WxReservationVo wxReservationVo);




}
