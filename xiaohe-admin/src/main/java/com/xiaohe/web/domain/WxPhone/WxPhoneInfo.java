package com.xiaohe.web.domain.WxPhone;

import lombok.Data;

@Data
public class WxPhoneInfo {
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 	countryCode
     */
    private int countryCode;


}
