package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectInfoMapper  extends BaseMapper<ProjectInfo> {

    /**
     * 查询【项目信息】
     *
     * @param projectId 【项目信息】主键
     * @return 【项目信息】
     */
    public ProjectInfo selectProjectInfoByProjectId(Long projectId);

    /**
     * 查询【项目信息】列表
     *
     * @param projectInfo 【项目信息】
     * @return 【项目信息】集合
     */
    public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo);

    /**
     * 新增【项目信息】
     *
     * @param projectInfo 【项目信息】
     * @return 结果
     */
    public int insertProjectInfo(ProjectInfo projectInfo);

    /**
     * 修改【项目信息】
     *
     * @param projectInfo 【项目信息】
     * @return 结果
     */
    public int updateProjectInfo(ProjectInfo projectInfo);

    /**
     * 删除【项目信息】
     *
     * @param projectId 【项目信息】主键
     * @return 结果
     */
    public int deleteProjectInfoByProjectId(Long projectId);

    /**
     * 批量删除【项目信息】
     *
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectInfoByProjectIds(Long[] projectIds);

}
