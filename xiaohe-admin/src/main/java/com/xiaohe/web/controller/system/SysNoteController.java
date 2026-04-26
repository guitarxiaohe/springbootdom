package com.xiaohe.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.web.domain.Entity.SysNote;
import com.xiaohe.web.service.ISysNoteService;
import com.xiaohe.common.utils.poi.ExcelUtil;
import com.xiaohe.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 笔记Controller
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
@Api(tags = "笔记管理")
@RestController
@RequestMapping("/note/manage")
public class SysNoteController extends BaseController
{
    @Autowired
    private ISysNoteService sysNoteService;

    /**
     * 查询笔记列表
     */
    @ApiOperation("查询笔记列表")
    @PreAuthorize("@ss.hasPermi('note:manage:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNote sysNote)
    {
        startPage();
        List<SysNote> list = sysNoteService.selectSysNoteList(sysNote);
        return getDataTable(list);
    }

    /**
     * 查询笔记树形列表
     */
    @ApiOperation("查询笔记树形列表")
    @PreAuthorize("@ss.hasPermi('note:manage:list')")
    @GetMapping("/treeList")
    public AjaxResult treeList(SysNote sysNote)
    {
        List<SysNote> list = sysNoteService.selectSysNoteTreeList(sysNote);
        return AjaxResult.success(list);
    }

    /**
     * 导出笔记列表
     */
    @ApiOperation("导出笔记列表")
    @PreAuthorize("@ss.hasPermi('note:manage:export')")
    @Log(title = "笔记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNote sysNote)
    {
        List<SysNote> list = sysNoteService.selectSysNoteList(sysNote);
        ExcelUtil<SysNote> util = new ExcelUtil<SysNote>(SysNote.class);
        util.exportExcel(response, list, "笔记数据");
    }

    /**
     * 获取笔记详细信息
     */
    @ApiOperation("获取笔记详细信息（自动增加阅读次数）")
    @PreAuthorize("@ss.hasPermi('note:manage:query')")
    @GetMapping(value = "/{noteId}")
    public AjaxResult getInfo(@ApiParam("笔记ID") @PathVariable("noteId") Long noteId)
    {
        // 增加阅读次数
        sysNoteService.increaseReadCount(noteId);
        return AjaxResult.success(sysNoteService.selectSysNoteByNoteId(noteId));
    }

    /**
     * 新增笔记
     */
    @ApiOperation("新增笔记")
    @PreAuthorize("@ss.hasPermi('note:manage:add')")
    @Log(title = "笔记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam("笔记对象") @RequestBody SysNote sysNote)
    {
        return toAjax(sysNoteService.insertSysNote(sysNote));
    }

    /**
     * 修改笔记
     */
    @ApiOperation("修改笔记")
    @PreAuthorize("@ss.hasPermi('note:manage:edit')")
    @Log(title = "笔记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam("笔记对象") @RequestBody SysNote sysNote)
    {
        return toAjax(sysNoteService.updateSysNote(sysNote));
    }

    /**
     * 删除笔记
     */
    @ApiOperation("删除笔记")
    @PreAuthorize("@ss.hasPermi('note:manage:remove')")
    @Log(title = "笔记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{noteIds}")
    public AjaxResult remove(@ApiParam("笔记ID数组") @PathVariable Long[] noteIds)
    {
        return toAjax(sysNoteService.deleteSysNoteByNoteIds(noteIds));
    }
}

