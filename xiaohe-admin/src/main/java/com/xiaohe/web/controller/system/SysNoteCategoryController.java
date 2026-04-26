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
import com.xiaohe.web.domain.Entity.SysNoteCategory;
import com.xiaohe.web.service.ISysNoteCategoryService;
import com.xiaohe.common.utils.poi.ExcelUtil;
import com.xiaohe.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 笔记分类Controller
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
@Api(tags = "笔记分类管理")
@RestController
@RequestMapping("/note/category")
public class SysNoteCategoryController extends BaseController
{
    @Autowired
    private ISysNoteCategoryService sysNoteCategoryService;

    /**
     * 查询笔记分类列表
     */
    @ApiOperation("查询笔记分类列表")
    @PreAuthorize("@ss.hasPermi('note:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNoteCategory sysNoteCategory)
    {
        startPage();
        List<SysNoteCategory> list = sysNoteCategoryService.selectSysNoteCategoryList(sysNoteCategory);
        return getDataTable(list);
    }

    /**
     * 导出笔记分类列表
     */
    @ApiOperation("导出笔记分类列表")
    @PreAuthorize("@ss.hasPermi('note:category:export')")
    @Log(title = "笔记分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNoteCategory sysNoteCategory)
    {
        List<SysNoteCategory> list = sysNoteCategoryService.selectSysNoteCategoryList(sysNoteCategory);
        ExcelUtil<SysNoteCategory> util = new ExcelUtil<SysNoteCategory>(SysNoteCategory.class);
        util.exportExcel(response, list, "笔记分类数据");
    }

    /**
     * 获取笔记分类详细信息
     */
    @ApiOperation("获取笔记分类详细信息")
    @PreAuthorize("@ss.hasPermi('note:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@ApiParam("分类ID") @PathVariable("categoryId") Long categoryId)
    {
        return AjaxResult.success(sysNoteCategoryService.selectSysNoteCategoryByCategoryId(categoryId));
    }

    /**
     * 新增笔记分类
     */
    @ApiOperation("新增笔记分类")
    @PreAuthorize("@ss.hasPermi('note:category:add')")
    @Log(title = "笔记分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam("笔记分类对象") @RequestBody SysNoteCategory sysNoteCategory)
    {
        return toAjax(sysNoteCategoryService.insertSysNoteCategory(sysNoteCategory));
    }

    /**
     * 修改笔记分类
     */
    @ApiOperation("修改笔记分类")
    @PreAuthorize("@ss.hasPermi('note:category:edit')")
    @Log(title = "笔记分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam("笔记分类对象") @RequestBody SysNoteCategory sysNoteCategory)
    {
        return toAjax(sysNoteCategoryService.updateSysNoteCategory(sysNoteCategory));
    }

    /**
     * 删除笔记分类
     */
    @ApiOperation("删除笔记分类")
    @PreAuthorize("@ss.hasPermi('note:category:remove')")
    @Log(title = "笔记分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@ApiParam("分类ID数组") @PathVariable Long[] categoryIds)
    {
        return toAjax(sysNoteCategoryService.deleteSysNoteCategoryByCategoryIds(categoryIds));
    }
}

