package com.xiaohe.web.service;

import com.xiaohe.web.domain.Entity.ProjectType;

import java.util.List;

/**
 * 项目类型Service接口
 *
 * @author xiaohe
 * @date 2025-11-14
 */
public interface IProjectTypeService
{
    /**
     * 查询项目类型
     *
     * @param projectTypeId 项目类型主键
     * @return 项目类型
     */
    public ProjectType selectProjectTypeByProjectTypeId(Long projectTypeId);

    /**
     * 查询项目类型列表
     *
     * @param projectType 项目类型
     * @return 项目类型集合
     */
    public List<ProjectType> selectProjectTypeList(ProjectType projectType);

    /**
     * 新增项目类型
     *
     * @param projectType 项目类型
     * @return 结果
     */
    public int insertProjectType(ProjectType projectType);

    /**
     * 修改项目类型
     *
     * @param projectType 项目类型
     * @return 结果
     */
    public int updateProjectType(ProjectType projectType);

    /**
     * 批量删除项目类型
     *
     * @param projectTypeIds 需要删除的项目类型主键集合
     * @return 结果
     */
    public int deleteProjectTypeByProjectTypeIds(Long[] projectTypeIds);

    /**
     * 删除项目类型信息
     *
     * @param projectTypeId 项目类型主键
     * @return 结果
     */
    public int deleteProjectTypeByProjectTypeId(Long projectTypeId);
}