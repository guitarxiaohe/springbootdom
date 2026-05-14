package com.xiaohe.system.service;

import java.util.List;
import com.xiaohe.system.domain.SysWsMsgLog;

/**
 * WebSocket消息日志 服务层
 *
 * @author xiaohe
 */
public interface ISysWsMsgLogService
{
    /**
     * 查询消息日志
     *
     * @param msgId 消息ID
     * @return 消息日志
     */
    public SysWsMsgLog selectWsMsgLogById(Long msgId);

    /**
     * 查询消息日志列表
     *
     * @param wsMsgLog 消息日志
     * @return 消息日志集合
     */
    public List<SysWsMsgLog> selectWsMsgLogList(SysWsMsgLog wsMsgLog);

    /**
     * 查询用户最近消息
     *
     * @param userId 用户ID
     * @param limit 条数
     * @return 消息日志集合
     */
    public List<SysWsMsgLog> selectRecentLogs(Long userId, int limit);

    /**
     * 记录消息日志
     *
     * @param wsMsgLog 消息日志
     * @return 消息ID
     */
    public Long insertWsMsgLog(SysWsMsgLog wsMsgLog);

    /**
     * 标记已发送
     *
     * @param msgId 消息ID
     * @return 结果
     */
    public int markSent(Long msgId);

    /**
     * 标记发送失败
     *
     * @param msgId 消息ID
     * @return 结果
     */
    public int markFailed(Long msgId);

    /**
     * 删除消息日志
     *
     * @param msgId 消息ID
     * @return 结果
     */
    public int deleteWsMsgLogById(Long msgId);

    /**
     * 批量删除消息日志
     *
     * @param msgIds 需要删除的消息ID
     * @return 结果
     */
    public int deleteWsMsgLogByIds(Long[] msgIds);
}
