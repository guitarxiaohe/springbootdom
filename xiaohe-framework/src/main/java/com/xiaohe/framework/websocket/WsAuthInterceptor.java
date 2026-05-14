package com.xiaohe.framework.websocket;

import java.net.URI;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.xiaohe.common.core.domain.model.LoginUser;
import com.xiaohe.framework.web.service.TokenService;

/**
 * WebSocket认证拦截器
 * 从URL参数?token=xxx中提取JWT进行认证
 *
 * @author xiaohe
 */
@Component
public class WsAuthInterceptor implements HandshakeInterceptor
{
    private static final Logger log = LoggerFactory.getLogger(WsAuthInterceptor.class);

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes)
    {
        String token = extractToken(request);
        if (token == null)
        {
            log.warn("WebSocket握手失败：缺少token参数");
            return false;
        }

        try
        {
            LoginUser loginUser = tokenService.getLoginUserByToken(token);
            if (loginUser == null)
            {
                log.warn("WebSocket握手失败：token无效或已过期");
                return false;
            }
            attributes.put("userId", loginUser.getUserId());
            attributes.put("loginUser", loginUser);
            log.info("WebSocket握手成功：用户{}", loginUser.getUserId());
            return true;
        }
        catch (Exception e)
        {
            log.error("WebSocket握手认证异常", e);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception)
    {
    }

    /**
     * 从URL query参数中提取token
     */
    private String extractToken(ServerHttpRequest request)
    {
        URI uri = request.getURI();
        String query = uri.getQuery();
        if (query != null)
        {
            for (String param : query.split("&"))
            {
                String[] pair = param.split("=", 2);
                if (pair.length == 2 && "token".equals(pair[0]))
                {
                    return pair[1];
                }
            }
        }
        return null;
    }
}
