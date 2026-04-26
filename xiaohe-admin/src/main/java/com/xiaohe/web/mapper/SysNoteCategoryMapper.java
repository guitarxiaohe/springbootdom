package com.xiaohe.web.mapper;

import com.xiaohe.web.domain.Entity.SysNoteCategory;
import java.util.List;

/**
 * 笔记分类Mapper接口
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
public interface SysNoteCategoryMapper 
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
     * 删除笔记分类
     * 
     * @param categoryId 笔记分类主键
     * @return 结果
     */
    public int deleteSysNoteCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除笔记分类
     * 
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNoteCategoryByCategoryIds(Long[] categoryIds);

    /**
     * 检查分类编码是否唯一
     * 
     * @param categoryCode 分类编码
     * @return 结果
     */
    public SysNoteCategory checkCategoryCodeUnique(String categoryCode);
}

