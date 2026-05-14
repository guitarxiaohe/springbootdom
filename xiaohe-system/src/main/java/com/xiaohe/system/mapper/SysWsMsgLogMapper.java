package com.xiaohe.system.mapper;

import java.util.List;
import com.xiaohe.system.domain.SysWsMsgLog;

/**
 * WebSocket消息日志表 数据层
 *
 * @author xiaohe
 */
public interface SysWsMsgLogMapper
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
     * @param targetUserId 目标用户ID
     * @param limit 条数
     * @return 消息日志集合
     */
    public List<SysWsMsgLog> selectRecentLogs(Long targetUserId, int limit);

    /**
     * 新增消息日志
     *
     * @param wsMsgLog 消息日志
     * @return 结果
     */
    public int insertWsMsgLog(SysWsMsgLog wsMsgLog);

    /**
     * 修改消息日志
     *
     * @param wsMsgLog 消息日志
     * @return 结果
     */
    public int updateWsMsgLog(SysWsMsgLog wsMsgLog);

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
