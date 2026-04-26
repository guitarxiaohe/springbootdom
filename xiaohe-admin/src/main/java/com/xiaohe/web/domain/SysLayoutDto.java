package com.xiaohe.web.domain;

import lombok.Data;

@Data
public class SysLayoutDto {

    /**
     * x轴
     */
   private  String[]  x;


    /**
     * y轴
     */
    private  String[]  y;

    /**
     * 天
     */
    private  String day;

    /**
     * 数据
     */
    private  String value;


    /**
     * 预约总人数
     */
    private  int reservationNumber;

    /**
     * 用户总数
     */
    private  int userNumber;

    /**
     * 小程序访问量
     */
    private  int wxNumber;

  /**
   * 平均预约时长
   */
     private String takeTime;

}
