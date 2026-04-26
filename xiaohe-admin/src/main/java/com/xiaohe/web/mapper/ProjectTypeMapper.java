package com.xiaohe.web.mapper;

import com.xiaohe.web.domain.Entity.ProjectType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目类型Mapper接口
 *
 * @author
 * @date 2025-11-14
 */
@Mapper
public interface ProjectTypeMapper
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
     * 删除项目类型
     *
     * @param projectTypeId 项目类型主键
     * @return 结果
     */
    public int deleteProjectTypeByProjectTypeId(Long projectTypeId);

    /**
     * 批量删除项目类型
     *
     * @param projectTypeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectTypeByProjectTypeIds(Long[] projectTypeIds);
}