package com.xiaohe.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.system.domain.SysWsMsgLog;
import com.xiaohe.system.mapper.SysWsMsgLogMapper;
import com.xiaohe.system.service.ISysWsMsgLogService;

/**
 * WebSocket消息日志 服务层实现
 *
 * @author xiaohe
 */
@Service
public class SysWsMsgLogServiceImpl implements ISysWsMsgLogService
{
    @Autowired
    private SysWsMsgLogMapper wsMsgLogMapper;

    @Override
    public SysWsMsgLog selectWsMsgLogById(Long msgId)
    {
        return wsMsgLogMapper.selectWsMsgLogById(msgId);
    }

    @Override
    public List<SysWsMsgLog> selectWsMsgLogList(SysWsMsgLog wsMsgLog)
    {
        return wsMsgLogMapper.selectWsMsgLogList(wsMsgLog);
    }

    @Override
    public List<SysWsMsgLog> selectRecentLogs(Long userId, int limit)
    {
        return wsMsgLogMapper.selectRecentLogs(userId, limit);
    }

    @Override
    public Long insertWsMsgLog(SysWsMsgLog wsMsgLog)
    {
        wsMsgLogMapper.insertWsMsgLog(wsMsgLog);
        return wsMsgLog.getMsgId();
    }

    @Override
    public int markSent(Long msgId)
    {
        SysWsMsgLog update = new SysWsMsgLog();
        update.setMsgId(msgId);
        update.setSendStatus("1");
        update.setSendTime(new Date());
        return wsMsgLogMapper.updateWsMsgLog(update);
    }

    @Override
    public int markFailed(Long msgId)
    {
        SysWsMsgLog update = new SysWsMsgLog();
        update.setMsgId(msgId);
        update.setSendStatus("2");
        return wsMsgLogMapper.updateWsMsgLog(update);
    }

    @Override
    public int deleteWsMsgLogById(Long msgId)
    {
        return wsMsgLogMapper.deleteWsMsgLogById(msgId);
    }

    @Override
    public int deleteWsMsgLogByIds(Long[] msgIds)
    {
        return wsMsgLogMapper.deleteWsMsgLogByIds(msgIds);
    }
}
