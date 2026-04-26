package com.xiaohe.web.domain.WxTemplate;

import lombok.Data;

/**
 * 审核结果模版
 */

@Data
public class WxExamineVo {
    /**
     * OPENID
     */
    private String touser;

    /**
     * 模版id
     */
    private String template_id;

    /**
     * 点击进入对应页面路径
     */
    private String page;

    /**
     * ****
     */
    private String  miniprogram_state;



    /**
     * 中英文
     */
    private String  lang;

    /**
     * 数据层
     */
    private Object data;


}
