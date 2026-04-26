package com.xiaohe.web.domain.Dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.domain.Dto
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月21日 13:34
 */

@Data
public class SysLayoutNumDto {
    /**
     * 人员数量
     */
    private int userSize;

    /**
     * 项目数量
     */
    private int projectSize;


    /**
     * 所有人员累计总工时数量
     */
    private BigDecimal manHourSize;
}