package com.xiaohe.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.xiaohe.framework.websocket.NotifyWebSocketHandler;
import com.xiaohe.framework.websocket.WsAuthInterceptor;

/**
 * WebSocket配置
 *
 * @author xiaohe
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{
    @Autowired
    private NotifyWebSocketHandler notifyHandler;

    @Autowired
    private WsAuthInterceptor wsAuthInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(notifyHandler, "/ws/notify")
                .addInterceptors(wsAuthInterceptor)
                .setAllowedOrigins("*");
    }
}
