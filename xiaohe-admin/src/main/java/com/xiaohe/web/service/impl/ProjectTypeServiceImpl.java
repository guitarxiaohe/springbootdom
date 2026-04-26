package com.xiaohe.web.service.impl;

import com.xiaohe.common.utils.DateUtils;
import com.xiaohe.web.domain.Entity.ProjectType;
import com.xiaohe.web.mapper.ProjectTypeMapper;
import com.xiaohe.web.service.IProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xiaohe.common.utils.SecurityUtils.getUsername;
import static org.apache.commons.lang3.SystemUtils.getUserName;

/**
 * 项目类型Service业务层处理
 *
 * @author xiaohe
 * @date 2025-11-14
 */
@Service
public class ProjectTypeServiceImpl implements IProjectTypeService
{
    @Autowired
    private ProjectTypeMapper projectTypeMapper;

    /**
     * 查询项目类型
     *
     * @param projectTypeId 项目类型主键
     * @return 项目类型
     */
    @Override
    public ProjectType selectProjectTypeByProjectTypeId(Long projectTypeId)
    {
        return projectTypeMapper.selectProjectTypeByProjectTypeId(projectTypeId);
    }

    /**
     * 查询项目类型列表
     *
     * @param projectType 项目类型
     * @return 项目类型
     */
    @Override
    public List<ProjectType> selectProjectTypeList(ProjectType projectType)
    {
        return projectTypeMapper.selectProjectTypeList(projectType);
    }

    /**
     * 新增项目类型
     *
     * @param projectType 项目类型
     * @return 结果
     */
    @Override
    public int insertProjectType(ProjectType projectType)
    {
        projectType.setCreateTime(DateUtils.getNowDate());
        projectType.setCreateBy(getUsername());
        return projectTypeMapper.insertProjectType(projectType);
    }

    /**
     * 修改项目类型
     *
     * @param projectType 项目类型
     * @return 结果
     */
    @Override
    public int updateProjectType(ProjectType projectType)
    {
        projectType.setUpdateTime(DateUtils.getNowDate());
        projectType.setUpdateBy(getUsername());
        return projectTypeMapper.updateProjectType(projectType);
    }

    /**
     * 批量删除项目类型
     *
     * @param projectTypeIds 需要删除的项目类型主键
     * @return 结果
     */
    @Override
    public int deleteProjectTypeByProjectTypeIds(Long[] projectTypeIds)
    {
        return projectTypeMapper.deleteProjectTypeByProjectTypeIds(projectTypeIds);
    }

    /**
     * 删除项目类型信息
     *
     * @param projectTypeId 项目类型主键
     * @return 结果
     */
    @Override
    public int deleteProjectTypeByProjectTypeId(Long projectTypeId)
    {
        return projectTypeMapper.deleteProjectTypeByProjectTypeId(projectTypeId);
    }
}