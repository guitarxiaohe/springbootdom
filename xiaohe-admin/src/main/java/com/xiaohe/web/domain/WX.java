package com.xiaohe.web.domain;

import com.xiaohe.common.annotation.Excel;
import lombok.Data;

@Data
public class WX {
    /**
     * 公众号通知类型:1服务号 2订阅号
     */
    private Integer noticeType;
    private Integer schoolId;
    private int expiresIn;
    /** id */
    private Long id;

    /** 微信openId */
    @Excel(name = "微信openId")
    private String openId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 微信昵称 */
    @Excel(name = "微信昵称")
    private String name;

    /** 微信头像 */
    @Excel(name = "微信头像")
    private String avatar;

    /**
     * wx唯一凭证
     */
    private String accessToken;

    /**
     * wx code
     */
    private String code;

    /**
     * wx sessionKey
     */
    private String sessionKey;

    /**
     * wx phoneCode 获取手机号code
     */
    private String phoneCode;




}
