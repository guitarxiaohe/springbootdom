package com.xiaohe.web.service;

import java.util.List;
import com.xiaohe.web.domain.Entity.SysNoteCategory;

/**
 * 笔记分类Service接口
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
public interface ISysNoteCategoryService 
{
    /**
     * 查询笔记分类
     * 
     * @param categoryId 笔记分类主键
     * @return 笔记分类
     */
    public SysNoteCategory selectSysNoteCategoryByCategoryId(Long categoryId);

    /**
     * 查询笔记分类列表
     * 
     * @param sysNoteCategory 笔记分类
     * @return 笔记分类集合
     */
    public List<SysNoteCategory> selectSysNoteCategoryList(SysNoteCategory sysNoteCategory);

    /**
     * 新增笔记分类
     * 
     * @param sysNoteCategory 笔记分类
     * @return 结果
     */
    public int insertSysNoteCategory(SysNoteCategory sysNoteCategory);

    /**
     * 修改笔记分类
     * 
     * @param sysNoteCategory 笔记分类
     * @return 结果
     */
    public int updateSysNoteCategory(SysNoteCategory sysNoteCategory);

    /**
     * 批量删除笔记分类
     * 
     * @param categoryIds 需要删除的笔记分类主键集合
     * @return 结果
     */
    public int deleteSysNoteCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 删除笔记分类信息
     * 
     * @param categoryId 笔记分类主键
     * @return 结果
     */
    public int deleteSysNoteCategoryByCategoryId(Long categoryId);

    /**
     * 校验分类编码是否唯一
     * 
     * @param category 分类信息
     * @return 结果
     */
    public String checkCategoryCodeUnique(SysNoteCategory category);
}

