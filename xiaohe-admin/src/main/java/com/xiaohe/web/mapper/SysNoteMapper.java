package com.xiaohe.web.mapper;

import com.xiaohe.web.domain.Entity.SysNote;
import java.util.List;

/**
 * 笔记Mapper接口
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
public interface SysNoteMapper 
{
    /**
     * 查询笔记
     * 
     * @param noteId 笔记主键
     * @return 笔记
     */
    public SysNote selectSysNoteByNoteId(Long noteId);

    /**
     * 查询笔记列表
     * 
     * @param sysNote 笔记
     * @return 笔记集合
     */
    public List<SysNote> selectSysNoteList(SysNote sysNote);

    /**
     * 查询笔记树形列表
     * 
     * @param sysNote 笔记
     * @return 笔记集合
     */
    public List<SysNote> selectSysNoteTreeList(SysNote sysNote);

    /**
     * 新增笔记
     * 
     * @param sysNote 笔记
     * @return 结果
     */
    public int insertSysNote(SysNote sysNote);

    /**
     * 修改笔记
     * 
     * @param sysNote 笔记
     * @return 结果
     */
    public int updateSysNote(SysNote sysNote);

    /**
     * 删除笔记
     * 
     * @param noteId 笔记主键
     * @return 结果
     */
    public int deleteSysNoteByNoteId(Long noteId);

    /**
     * 批量删除笔记
     * 
     * @param noteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNoteByNoteIds(Long[] noteIds);

    /**
     * 查询子笔记数量
     * 
     * @param parentId 父笔记ID
     * @return 结果
     */
    public int selectChildrenCountByParentId(Long parentId);

    /**
     * 增加阅读次数
     * 
     * @param noteId 笔记ID
     * @return 结果
     */
    public int increaseReadCount(Long noteId);

    /**
     * 查询某分类下的笔记数量
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int selectNoteCountByCategoryId(Long categoryId);
}

