package com.xiaohe.web.domain;

import lombok.Data;

import java.util.List;

@Data
public class ExceptionDto {

    private String code;

    private String msg;
    private List<Reservation> data;
    private String errcode;
    private String errmsg;
}
