package com.xiaohe.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.common.utils.SecurityUtils;
import com.xiaohe.system.domain.SysWsMsgLog;
import com.xiaohe.system.service.ISysWsMsgLogService;

/**
 * WebSocket消息日志 信息操作处理
 *
 * @author xiaohe
 */
@RestController
@RequestMapping("/system/wslog")
public class SysWsMsgLogController extends BaseController
{
    @Autowired
    private ISysWsMsgLogService wsMsgLogService;

    /**
     * 获取当前用户最近消息（无需特殊权限）
     */
    @GetMapping("/recent")
    public AjaxResult recent()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysWsMsgLog> list = wsMsgLogService.selectRecentLogs(userId, 20);
        return AjaxResult.success(list);
    }

    /**
     * 查询消息日志列表（管理用）
     */
    @PreAuthorize("@ss.hasPermi('system:wslog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysWsMsgLog wsMsgLog)
    {
        startPage();
        List<SysWsMsgLog> list = wsMsgLogService.selectWsMsgLogList(wsMsgLog);
        return getDataTable(list);
    }

    /**
     * 删除消息日志
     */
    @PreAuthorize("@ss.hasPermi('system:wslog:remove')")
    @Log(title = "消息日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{msgIds}")
    public AjaxResult remove(@PathVariable Long[] msgIds)
    {
        return toAjax(wsMsgLogService.deleteWsMsgLogByIds(msgIds));
    }
}
