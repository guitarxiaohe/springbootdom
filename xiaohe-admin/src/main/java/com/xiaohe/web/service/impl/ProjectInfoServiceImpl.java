package com.xiaohe.web.service.impl;

import java.util.List;
import com.xiaohe.common.utils.DateUtils;
import com.xiaohe.web.domain.Entity.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.web.mapper.ProjectInfoMapper;
import com.xiaohe.web.service.IProjectInfoService;

import static com.xiaohe.common.utils.SecurityUtils.getUsername;

/**
 * 【项目信息】Service业务层处理
 *
 * @author xiaohe
 * @date 2025-11-13
 */
@Service
public class ProjectInfoServiceImpl implements IProjectInfoService
{
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 查询【项目信息】
     *
     * @param projectId 【项目信息】主键
     * @return 【项目信息】
     */
    @Override
    public ProjectInfo selectProjectInfoByProjectId(Long projectId)
    {
        return projectInfoMapper.selectProjectInfoByProjectId(projectId);
    }

    /**
     * 查询【项目信息】列表
     *
     * @param projectInfo 【项目信息】
     * @return 【项目信息】
     */
    @Override
    public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo)
    {
        return projectInfoMapper.selectProjectInfoList(projectInfo);
    }

    /**
     * 新增【项目信息】
     *
     * @param projectInfo 【项目信息】
     * @return 结果
     */
    @Override
    public int insertProjectInfo(ProjectInfo projectInfo)
    {
        projectInfo.setCreateTime(DateUtils.getNowDate());
        return projectInfoMapper.insertProjectInfo(projectInfo);
    }

    /**
     * 修改【项目信息】
     *
     * @param projectInfo 【项目信息】
     * @return 结果
     */
    @Override
    public int updateProjectInfo(ProjectInfo projectInfo)
    {
        projectInfo.setUpdateTime(DateUtils.getNowDate());
        projectInfo.setUpdateBy(getUsername());
        return projectInfoMapper.updateProjectInfo(projectInfo);
    }

    /**
     * 批量删除【项目信息】
     *
     * @param projectIds 需要删除的【项目信息】主键
     * @return 结果
     */
    @Override
    public int deleteProjectInfoByProjectIds(Long[] projectIds)
    {
        return projectInfoMapper.deleteProjectInfoByProjectIds(projectIds);
    }

    /**
     * 删除【项目信息】信息
     *
     * @param projectId 【项目信息】主键
     * @return 结果
     */
    @Override
    public int deleteProjectInfoByProjectId(Long projectId)
    {
        return projectInfoMapper.deleteProjectInfoByProjectId(projectId);
    }
}