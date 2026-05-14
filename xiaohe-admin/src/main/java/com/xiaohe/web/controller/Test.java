package com.xiaohe.web.controller;

import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.domain.WsMessage;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.framework.websocket.NotifyPushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/ws")
public class Test extends BaseController {

    @Autowired
    private NotifyPushService notifyPushService;

    @ApiOperation("测试发通知")
    @Log(title = "测试发通知", businessType = BusinessType.GRANT)
    @PostMapping("/send")
    public AjaxResult send(@RequestBody WsMessage send){

        WsMessage msg = new WsMessage();
        msg.setPath(send.getPath());
        msg.setTitle(send.getTitle());
        msg.setType(send.getType());
        msg.setParams(send.getParams());
        msg.setText(send.getText());
        notifyPushService.pushToUser(Long.valueOf("1"), msg);
       return  AjaxResult.success();
   }


}
