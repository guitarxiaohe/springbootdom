package com.xiaohe.web.wxConfig;

public interface WxConstant {

    /**
     * 小程序id
     */
    String APP_ID= "wxd46d3f4025e9afc1";

    /**
     * 小程序秘钥 secret
     */
     String SECRET = "12835622ee6376ba0128c58319d6686f";

    /**
     * 提醒记功模板ID visitorTemplateId
     */
     String VISITOR_TEMPLATE_ID = "IOouVxDnGAjETl0MA7IGpCPOuu0jtX1lKNu7OGw3sfs";

    /**
     * 审核结果通知模板ID examineTemplateId
     */
     String EXAMINE_TEMPLATE_ID = "2P8hi0tYidK04SZEjW8Y2cKPG-YCZDNCoFRKx2dlBns";

    /**
     * 项目审核提醒模板ID examineTemplateId
     */
    String EXAMINE_REMINDER_ID = "Abf_o2tZnyszAEH6jrV17jRFSVFKjtIbzUxMCIKEL5A";

    /**
     * 获取小程序月访问量Url monthUrl
     */
    String MONTH_URL = "https://api.weixin.qq.com/datacube/getweanalysisappidmonthlyvisittrend?access_token=";

    /**
     * 该接口用于获取用户访问小程序数据 周趋势
     */
    String WX_WEEK_TENDENCY_URL = "https://api.weixin.qq.com/datacube/getweanalysisappidweeklyvisittrend?access_token=";

    /**
     * 审核模板发送请求地址
     */

    String  WX_TEMPLATE_EXAMINE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";


    /**
     * 预约通知模板请求地址
     */

    String  WX_TEMPLATE_VISITOR_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    /**
     * 开发版本 正式版本 formal 开发版本 developer 体验版本 trial
     */
    String  WX_TEMPLATE_VERSION ="formal";

}
