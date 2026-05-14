package com.xiaohe.framework.websocket;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.xiaohe.common.core.domain.WsMessage;
import com.xiaohe.system.domain.SysWsMsgLog;
import com.xiaohe.system.service.ISysWsMsgLogService;

/**
 * WebSocket通知推送服务
 * 负责消息推送和日志记录
 *
 * @author xiaohe
 */
@Service
public class NotifyPushService
{
    private static final Logger log = LoggerFactory.getLogger(NotifyPushService.class);

    @Autowired
    private NotifyWebSocketHandler handler;

    @Autowired
    private ISysWsMsgLogService wsMsgLogService;

    /**
     * 推送消息给指定用户并记录日志
     *
     * @param userId 目标用户ID
     * @param msg 消息内容
     */
    public void pushToUser(Long userId, WsMessage msg)
    {
        SysWsMsgLog msgLog = buildMsgLog(msg, userId);
        Long msgId = wsMsgLogService.insertWsMsgLog(msgLog);

        String json = JSON.toJSONString(msg);
        boolean sent = handler.sendToUser(userId, json);

        if (sent)
        {
            wsMsgLogService.markSent(msgId);
            log.info("消息推送成功: userId={}, msgId={}, title={}", userId, msgId, msg.getTitle());
        }
        else
        {
            wsMsgLogService.markFailed(msgId);
            log.warn("消息推送失败(用户不在线): userId={}, msgId={}, title={}", userId, msgId, msg.getTitle());
        }
    }

    /**
     * 广播消息给所有在线用户并记录日志
     *
     * @param msg 消息内容
     */
    public void broadcast(WsMessage msg)
    {
        SysWsMsgLog msgLog = buildMsgLog(msg, null);
        Long msgId = wsMsgLogService.insertWsMsgLog(msgLog);

        String json = JSON.toJSONString(msg);
        handler.broadcast(json);

        wsMsgLogService.markSent(msgId);
        log.info("广播消息已发送: msgId={}, title={}, onlineCount={}", msgId, msg.getTitle(), handler.getOnlineCount());
    }

    /**
     * 构建消息日志对象
     */
    private SysWsMsgLog buildMsgLog(WsMessage msg, Long targetUserId)
    {
        SysWsMsgLog msgLog = new SysWsMsgLog();
        msgLog.setMsgType(msg.getType());
        msgLog.setTitle(msg.getTitle());
        msgLog.setContent(msg.getText());
        msgLog.setPath(msg.getPath());
        if (msg.getParams() != null)
        {
            msgLog.setQueryParams(JSON.toJSONString(msg.getParams()));
        }
        msgLog.setTargetUserId(targetUserId);
        msgLog.setSendStatus("0");
        msgLog.setCreateTime(new Date());
        return msgLog;
    }
}
