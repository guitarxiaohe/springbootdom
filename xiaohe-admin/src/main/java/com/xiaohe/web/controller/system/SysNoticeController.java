package com.xiaohe.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.common.utils.DateUtils;
import com.xiaohe.system.domain.SysNotice;
import com.xiaohe.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 * 
 * @author xiaohe
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 获取最近一周内的通知公告（无需权限，用于首页滚动播报）
     */
    @GetMapping("/recent")
    public AjaxResult recentNotices()
    {
        SysNotice notice = new SysNotice();
        notice.setStatus("0");
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime", DateUtils.addDays(new java.util.Date(), -7));
        notice.setParams(params);
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        List<Map<String, Object>> result = list.stream().map(n -> {
            Map<String, Object> item = new HashMap<>();
            item.put("noticeId", n.getNoticeId());
            item.put("noticeTitle", n.getNoticeTitle());
            item.put("noticeType", n.getNoticeType());
            item.put("createTime", n.getCreateTime());
            return item;
        }).collect(Collectors.toList());
        return AjaxResult.success(result);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return AjaxResult.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(getUsername());
        Long noticeId = noticeService.insertNotice(notice);
        if (noticeId==null){
            return AjaxResult.error();
        }
        return AjaxResult.success(noticeId);
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
