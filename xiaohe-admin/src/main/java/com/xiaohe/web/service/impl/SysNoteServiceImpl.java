package com.xiaohe.web.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.xiaohe.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.web.mapper.SysNoteMapper;
import com.xiaohe.web.domain.Entity.SysNote;
import com.xiaohe.web.service.ISysNoteService;
import com.xiaohe.common.exception.ServiceException;

/**
 * 笔记Service业务层处理
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
@Service
public class SysNoteServiceImpl implements ISysNoteService 
{
    @Autowired
    private SysNoteMapper sysNoteMapper;

    /**
     * 查询笔记
     * 
     * @param noteId 笔记主键
     * @return 笔记
     */
    @Override
    public SysNote selectSysNoteByNoteId(Long noteId)
    {
        return sysNoteMapper.selectSysNoteByNoteId(noteId);
    }

    /**
     * 查询笔记列表
     * 
     * @param sysNote 笔记
     * @return 笔记
     */
    @Override
    public List<SysNote> selectSysNoteList(SysNote sysNote)
    {
        return sysNoteMapper.selectSysNoteList(sysNote);
    }

    /**
     * 查询笔记树形列表
     * 
     * @param sysNote 笔记
     * @return 笔记树形结构
     */
    @Override
    public List<SysNote> selectSysNoteTreeList(SysNote sysNote)
    {
        List<SysNote> notes = sysNoteMapper.selectSysNoteTreeList(sysNote);
        return buildNoteTree(notes);
    }

    /**
     * 构建树形结构
     * 
     * @param notes 笔记列表
     * @return 树形结构
     */
    @Override
    public List<SysNote> buildNoteTree(List<SysNote> notes)
    {
        List<SysNote> returnList = new ArrayList<>();
        List<Long> tempList = notes.stream().map(SysNote::getNoteId).collect(Collectors.toList());
        
        for (SysNote note : notes)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(note.getParentId()))
            {
                recursionFn(notes, note);
                returnList.add(note);
            }
        }
        
        if (returnList.isEmpty())
        {
            returnList = notes;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysNote> list, SysNote t)
    {
        // 得到子节点列表
        List<SysNote> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysNote tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysNote> getChildList(List<SysNote> list, SysNote t)
    {
        List<SysNote> tlist = new ArrayList<>();
        Iterator<SysNote> it = list.iterator();
        while (it.hasNext())
        {
            SysNote n = it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getNoteId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysNote> list, SysNote t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 新增笔记
     * 
     * @param sysNote 笔记
     * @return 结果
     */
    @Override
    public int insertSysNote(SysNote sysNote)
    {
        sysNote.setCreateTime(DateUtils.getNowDate());
        return sysNoteMapper.insertSysNote(sysNote);
    }

    /**
     * 修改笔记
     * 
     * @param sysNote 笔记
     * @return 结果
     */
    @Override
    public int updateSysNote(SysNote sysNote)
    {
        sysNote.setUpdateTime(DateUtils.getNowDate());
        return sysNoteMapper.updateSysNote(sysNote);
    }

    /**
     * 批量删除笔记
     * 
     * @param noteIds 需要删除的笔记主键
     * @return 结果
     */
    @Override
    public int deleteSysNoteByNoteIds(Long[] noteIds)
    {
        for (Long noteId : noteIds)
        {
            SysNote note = selectSysNoteByNoteId(noteId);
            if (hasChildByNoteId(noteId))
            {
                throw new ServiceException(String.format("笔记 %s 存在子节点，不允许删除", note.getNoteTitle()));
            }
        }
        return sysNoteMapper.deleteSysNoteByNoteIds(noteIds);
    }

    /**
     * 删除笔记信息
     * 
     * @param noteId 笔记主键
     * @return 结果
     */
    @Override
    public int deleteSysNoteByNoteId(Long noteId)
    {
        if (hasChildByNoteId(noteId))
        {
            throw new ServiceException("该笔记存在子节点，不允许删除");
        }
        return sysNoteMapper.deleteSysNoteByNoteId(noteId);
    }

    /**
     * 是否存在子节点
     * 
     * @param noteId 笔记ID
     * @return 结果
     */
    private boolean hasChildByNoteId(Long noteId)
    {
        int result = sysNoteMapper.selectChildrenCountByParentId(noteId);
        return result > 0;
    }

    /**
     * 增加阅读次数
     * 
     * @param noteId 笔记ID
     * @return 结果
     */
    @Override
    public int increaseReadCount(Long noteId)
    {
        return sysNoteMapper.increaseReadCount(noteId);
    }
}

