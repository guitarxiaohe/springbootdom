package com.xiaohe.web.service;

import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.web.domain.WX;

public interface WxLoginService {


    /**
     * 微信登录验证
     * @return  结果
     */

    public AjaxResult wxLogin(WX wx) throws Exception;



    /**
     * 微信注册验证
     * @return  结果
     */

    public AjaxResult wr(WX wx) throws Exception;
}
