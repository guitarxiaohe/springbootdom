package com.xiaohe.web.service.impl;

import java.util.List;
import com.xiaohe.common.utils.DateUtils;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.common.constant.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.web.mapper.SysNoteCategoryMapper;
import com.xiaohe.web.mapper.SysNoteMapper;
import com.xiaohe.web.domain.Entity.SysNoteCategory;
import com.xiaohe.web.service.ISysNoteCategoryService;
import com.xiaohe.common.exception.ServiceException;

/**
 * 笔记分类Service业务层处理
 * 
 * @author xiaohe
 * @date 2025-01-07
 */
@Service
public class SysNoteCategoryServiceImpl implements ISysNoteCategoryService 
{
    @Autowired
    private SysNoteCategoryMapper sysNoteCategoryMapper;

    @Autowired
    private SysNoteMapper sysNoteMapper;

    /**
     * 查询笔记分类
     * 
     * @param categoryId 笔记分类主键
     * @return 笔记分类
     */
    @Override
    public SysNoteCategory selectSysNoteCategoryByCategoryId(Long categoryId)
    {
        return sysNoteCategoryMapper.selectSysNoteCategoryByCategoryId(categoryId);
    }

    /**
     * 查询笔记分类列表
     * 
     * @param sysNoteCategory 笔记分类
     * @return 笔记分类
     */
    @Override
    public List<SysNoteCategory> selectSysNoteCategoryList(SysNoteCategory sysNoteCategory)
    {
        return sysNoteCategoryMapper.selectSysNoteCategoryList(sysNoteCategory);
    }

    /**
     * 新增笔记分类
     * 
     * @param sysNoteCategory 笔记分类
     * @return 结果
     */
    @Override
    public int insertSysNoteCategory(SysNoteCategory sysNoteCategory)
    {
        if (UserConstants.NOT_UNIQUE.equals(checkCategoryCodeUnique(sysNoteCategory)))
        {
            throw new ServiceException("新增笔记分类'" + sysNoteCategory.getCategoryName() + "'失败，分类编码已存在");
        }
        sysNoteCategory.setCreateTime(DateUtils.getNowDate());
        return sysNoteCategoryMapper.insertSysNoteCategory(sysNoteCategory);
    }

    /**
     * 修改笔记分类
     * 
     * @param sysNoteCategory 笔记分类
     * @return 结果
     */
    @Override
    public int updateSysNoteCategory(SysNoteCategory sysNoteCategory)
    {
        if (UserConstants.NOT_UNIQUE.equals(checkCategoryCodeUnique(sysNoteCategory)))
        {
            throw new ServiceException("修改笔记分类'" + sysNoteCategory.getCategoryName() + "'失败，分类编码已存在");
        }
        sysNoteCategory.setUpdateTime(DateUtils.getNowDate());
        return sysNoteCategoryMapper.updateSysNoteCategory(sysNoteCategory);
    }

    /**
     * 批量删除笔记分类
     * 
     * @param categoryIds 需要删除的笔记分类主键
     * @return 结果
     */
    @Override
    public int deleteSysNoteCategoryByCategoryIds(Long[] categoryIds)
    {
        for (Long categoryId : categoryIds)
        {
            SysNoteCategory category = selectSysNoteCategoryByCategoryId(categoryId);
            if (sysNoteMapper.selectNoteCountByCategoryId(categoryId) > 0)
            {
                throw new ServiceException(String.format("分类 %s 下存在笔记，不允许删除", category.getCategoryName()));
            }
        }
        return sysNoteCategoryMapper.deleteSysNoteCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除笔记分类信息
     * 
     * @param categoryId 笔记分类主键
     * @return 结果
     */
    @Override
    public int deleteSysNoteCategoryByCategoryId(Long categoryId)
    {
        if (sysNoteMapper.selectNoteCountByCategoryId(categoryId) > 0)
        {
            throw new ServiceException("该分类下存在笔记，不允许删除");
        }
        return sysNoteCategoryMapper.deleteSysNoteCategoryByCategoryId(categoryId);
    }

    /**
     * 校验分类编码是否唯一
     * 
     * @param category 分类信息
     * @return 结果
     */
    @Override
    public String checkCategoryCodeUnique(SysNoteCategory category)
    {
        Long categoryId = StringUtils.isNull(category.getCategoryId()) ? -1L : category.getCategoryId();
        SysNoteCategory info = sysNoteCategoryMapper.checkCategoryCodeUnique(category.getCategoryCode());
        if (StringUtils.isNotNull(info) && info.getCategoryId().longValue() != categoryId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}

