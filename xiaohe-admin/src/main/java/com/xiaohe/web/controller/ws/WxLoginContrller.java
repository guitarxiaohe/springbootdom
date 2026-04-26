package com.xiaohe.web.controller.ws;

import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.web.domain.WX;
import com.xiaohe.web.service.WxLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx")
@Api("微信用户表")
public class WxLoginContrller extends BaseController {



    @Resource
    private WxLoginService wxLoginService;

    /**
     * 微信登录
     *
     * @return
     */
    @ApiOperation("微信登录")
    @Log(title = "微信登录", businessType = BusinessType.GRANT)
    @PostMapping("/login")
    public AjaxResult wxLogin(@RequestBody WX wx) throws Exception {
        return wxLoginService.wxLogin(wx);
    }


    /**
     * 微信注册
     *
     * @return
     */
    @PostMapping("/re")
    @ApiOperation("微信注册")
    @Log(title = "微信注册,授权手机号", businessType = BusinessType.GRANT)
    public AjaxResult wxRe(@RequestBody WX wx) throws Exception {
        return wxLoginService.wr(wx);

    }


}
