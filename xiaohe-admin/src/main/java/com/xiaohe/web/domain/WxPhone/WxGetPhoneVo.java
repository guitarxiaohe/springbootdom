package com.xiaohe.web.domain.WxPhone;

import lombok.Data;

@Data
public class WxGetPhoneVo {
    /**
     * 状态码
     */
    private int errcode;

    /**
     * 提示信息
     */
    private String errmsg;

    /**
     * 返回结构
     */
    private WxPhoneInfo phone_info;

}
