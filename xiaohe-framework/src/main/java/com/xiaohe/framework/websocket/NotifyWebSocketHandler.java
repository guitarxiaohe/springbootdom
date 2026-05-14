package com.xiaohe.framework.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket消息处理器
 * 维护用户session映射，处理消息收发
 *
 * @author xiaohe
 */
@Component
public class NotifyWebSocketHandler extends TextWebSocketHandler
{
    private static final Logger log = LoggerFactory.getLogger(NotifyWebSocketHandler.class);

    /** userId → session 映射 */
    private static final ConcurrentHashMap<Long, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
    {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null)
        {
            // 关闭该用户已有的旧连接
            WebSocketSession oldSession = SESSIONS.get(userId);
            if (oldSession != null && oldSession.isOpen())
            {
                try
                {
                    oldSession.close(CloseStatus.NORMAL);
                }
                catch (IOException e)
                {
                    log.warn("关闭旧WebSocket连接失败: userId={}", userId, e);
                }
            }
            SESSIONS.put(userId, session);
            log.info("WebSocket连接已建立: userId={}, sessionId={}", userId, session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
    {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null)
        {
            // 只移除当前session，避免误删新连接
            SESSIONS.remove(userId, session);
            log.info("WebSocket连接已关闭: userId={}, status={}", userId, status);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
    {
        String payload = message.getPayload();
        if ("pong".equals(payload))
        {
            // 心跳回复，忽略
            return;
        }
        log.debug("收到WebSocket消息: {}", payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
    {
        log.error("WebSocket传输错误: sessionId={}", session.getId(), exception);
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null)
        {
            SESSIONS.remove(userId, session);
        }
    }

    /**
     * 推送消息给指定用户
     *
     * @param userId 用户ID
     * @param jsonMessage JSON格式消息
     * @return 是否发送成功
     */
    public boolean sendToUser(Long userId, String jsonMessage)
    {
        WebSocketSession session = SESSIONS.get(userId);
        if (session != null && session.isOpen())
        {
            try
            {
                synchronized (session)
                {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
                return true;
            }
            catch (IOException e)
            {
                log.error("推送消息失败: userId={}", userId, e);
                SESSIONS.remove(userId, session);
                return false;
            }
        }
        return false;
    }

    /**
     * 广播消息给所有在线用户
     *
     * @param jsonMessage JSON格式消息
     */
    public void broadcast(String jsonMessage)
    {
        SESSIONS.forEach((userId, session) ->
        {
            if (session.isOpen())
            {
                try
                {
                    synchronized (session)
                    {
                        session.sendMessage(new TextMessage(jsonMessage));
                    }
                }
                catch (IOException e)
                {
                    log.error("广播消息失败: userId={}", userId, e);
                    SESSIONS.remove(userId, session);
                }
            }
        });
    }

    /**
     * 发送心跳ping
     */
    public void sendPing()
    {
        SESSIONS.forEach((userId, session) ->
        {
            if (session.isOpen())
            {
                try
                {
                    synchronized (session)
                    {
                        session.sendMessage(new TextMessage("ping"));
                    }
                }
                catch (IOException e)
                {
                    log.warn("发送心跳失败: userId={}", userId, e);
                    SESSIONS.remove(userId, session);
                }
            }
            else
            {
                SESSIONS.remove(userId, session);
            }
        });
    }

    /**
     * 获取在线用户数
     */
    public int getOnlineCount()
    {
        return SESSIONS.size();
    }

    /**
     * 判断用户是否在线
     */
    public boolean isOnline(Long userId)
    {
        WebSocketSession session = SESSIONS.get(userId);
        return session != null && session.isOpen();
    }
}
