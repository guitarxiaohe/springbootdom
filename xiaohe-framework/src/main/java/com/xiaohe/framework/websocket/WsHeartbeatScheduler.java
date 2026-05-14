package com.xiaohe.framework.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * WebSocket心跳调度器
 * 每30秒向所有在线连接发送ping，保活连接
 *
 * @author xiaohe
 */
@Component
public class WsHeartbeatScheduler
{
    private static final Logger log = LoggerFactory.getLogger(WsHeartbeatScheduler.class);

    @Autowired
    private NotifyWebSocketHandler handler;

    @Scheduled(fixedRate = 30000)
    public void sendPing()
    {
        int count = handler.getOnlineCount();
        if (count > 0)
        {
            handler.sendPing();
            log.debug("WebSocket心跳已发送，当前在线: {}", count);
        }
    }
}
