package com.xiaohe.web.service;

import java.util.List;
import com.xiaohe.web.domain.Entity.SysNote;

/**
 * 笔记Service接口
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
public interface ISysNoteService 
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
     * @return 笔记树形结构集合
     */
    public List<SysNote> selectSysNoteTreeList(SysNote sysNote);

    /**
     * 构建树形结构
     * 
     * @param notes 笔记列表
     * @return 树形结构
     */
    public List<SysNote> buildNoteTree(List<SysNote> notes);

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
     * 批量删除笔记
     * 
     * @param noteIds 需要删除的笔记主键集合
     * @return 结果
     */
    public int deleteSysNoteByNoteIds(Long[] noteIds);

    /**
     * 删除笔记信息
     * 
     * @param noteId 笔记主键
     * @return 结果
     */
    public int deleteSysNoteByNoteId(Long noteId);

    /**
     * 增加阅读次数
     * 
     * @param noteId 笔记ID
     * @return 结果
     */
    public int increaseReadCount(Long noteId);
}

