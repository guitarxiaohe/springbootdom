package com.xiaohe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.common.exception.GlobalException;
import com.xiaohe.common.utils.SecurityUtils;
import com.xiaohe.common.utils.bean.BeanUtils;
import com.xiaohe.web.domain.Dto.SysProjectDto;
import com.xiaohe.web.domain.Entity.SysProject;
import com.xiaohe.web.domain.Entity.SysUserProject;
import com.xiaohe.web.domain.Vo.SysProjectVo;
import com.xiaohe.web.mapper.SysProjectMapper;
import com.xiaohe.web.mapper.SysUserProjectMapper;
import com.xiaohe.web.mapper.SysWorkerUserMapper;
import com.xiaohe.web.service.SysProjectService;
import com.xiaohe.web.service.SysUserProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service.impl
 * @Description: 项目imp层
 * @date Date : 2024年02月08日 22:55
 */

@Service
public class SysProjectServiceImp extends ServiceImpl<SysProjectMapper, SysProject> implements SysProjectService {

    @Resource
    SysProjectMapper sysProjectMapper;

    @Resource
    SysWorkerUserMapper sysWorkerUserMapper;

    @Resource
    SysUserProjectMapper sysUserProjectMapper;

    @Resource
    SysUserProjectService sysUserProjectService;

    /**
     * 列表
     *
     * @param sysProjectVo 查询项目列表
     * @return
     */
    @Override
    public List<SysProjectDto> selectProjectList(SysProjectVo sysProjectVo) {

        List<SysProjectDto> sysProjectDtos = sysProjectMapper.selectListQuery(sysProjectVo);
//        for (SysProjectDto t : sysProjectDtos) {
//            List<SysUserProject> userProjectList = sysUserProjectMapper.selectList(new QueryWrapper<SysUserProject>()
//                    .eq("project_id", t.getProjectId()));
//            List<Long> collect = userProjectList.stream().map(SysUserProject::getUserId).collect(Collectors.toList());
//            t.setUserList(sysWorkerUserMapper.selectList(new QueryWrapper<SysWorkerUser>().in("id", collect)));
//        }

        return sysProjectDtos;
    }

    /**
     * 新增
     *
     * @param sysProjectVo
     * @return
     */
    @Override
    public int add(SysProjectVo sysProjectVo) {

        // 新增项目
        SysProject sysProject = new SysProject();
        BeanUtils.copyProperties(sysProjectVo, sysProject);
        sysProject.setCreateBy(SecurityUtils.getUsername());
        sysProject.setCreateTime(new Date());

//        for (String t :
//                sysProjectVo.getUserIds()) {
//            SysUserProject sysUserProject = new SysUserProject();
//            sysUserProject.setProjectId(sysProject.getProjectId());
//            sysUserProject.setUserId(Long.valueOf(t));
//            sysUserProjectMapper.insert(sysUserProject);
//        }
        return sysProjectMapper.insert(sysProject);
    }

    /**
     * 修改
     *
     * @param sysProjectVo
     * @return
     */
    @Override
    public int edit(SysProjectVo sysProjectVo) {
        int result;
        // 修改项目
        SysProject sysProject = new SysProject();
        BeanUtils.copyProperties(sysProjectVo, sysProject);
        sysProject.setUpdateBy(SecurityUtils.getUsername());
        sysProject.setUpdateTime(new Date());
        result = sysProjectMapper.updateById(sysProject);

//        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

//        objectObjectHashMap.put("project_id", sysProjectVo.getProjectId());
//        sysUserProjectMapper.deleteByMap(objectObjectHashMap);

//        for (String t :
//                sysProjectVo.getUserIds()) {
//            SysUserProject sysUserProject = new SysUserProject();
//            sysUserProject.setProjectId(sysProject.getProjectId());
//            sysUserProject.setUserId(Long.valueOf(t));
//            sysUserProjectMapper.insert(sysUserProject);
//        }
        return result;
    }

    /**
     * 删除
     *
     * @param projectId
     * @return
     */
    @Override
    public int remove(Long projectId) {
//        try {
//            Map<String, Object> ObjectHashMap = new HashMap<>();
//            ObjectHashMap.put("project_id", projectId);
//            sysUserProjectService.removeByMap(ObjectHashMap);
//        } catch (Exception e) {
//            throw new GlobalException("删除关系表报错：" + e);
//        }

        return sysProjectMapper.deleteById(projectId);
    }

    /**
     * 查看
     *
     * @param projectId
     * @return
     */
    @Override
    public SysProjectDto view(Long projectId) {
        SysProjectDto sysProjectDto = new SysProjectDto();
        SysProject sysProject = sysProjectMapper.selectById(projectId);
//
        BeanUtils.copyProperties(sysProject, sysProjectDto);
//
//        List<SysUserProject> projectId1 = sysUserProjectMapper.selectList(new QueryWrapper<SysUserProject>().eq("project_id", projectId));
//
//
//        Set<String> list = new HashSet<>();
//        projectId1.forEach(t -> list.add(String.valueOf(t.getUserId())));
//        sysProjectDto.setUserIds(list);
        return sysProjectDto;
    }

    /**
     * 查询所有列表
     *
     * @return sysProjectDto
     */
    @Override
    public List<SysProject> getProjectAll() {


        return sysProjectMapper.selectList(null);
    }

    /**
     * 通过人员id 查询项目
     *
     * @return sysProjectDto
     */
    @Override
    public List<SysProject> selProjectByUserId(Long userId) {
        List<SysProject> sysProjects = new ArrayList<>();
        SysProject projectHeadId = sysProjectMapper.selectOne(new QueryWrapper<SysProject>()
                .eq("project_head_id", userId));
        List<SysUserProject> userId1 = sysUserProjectMapper.selectList(new QueryWrapper<SysUserProject>().eq("user_id", userId));
        if (userId1.size() > 0) {
            sysProjects = sysWorkerUserMapper.selProjectByUserId(userId1);
        }
        List<Long> userIdList = sysProjects.stream().map(SysProject::getProjectId).
                collect(Collectors.toList());
        if (projectHeadId != null && !userIdList.contains(projectHeadId.getProjectId())) {
            sysProjects.add(projectHeadId);
        }
        if (sysProjects.size() < 1 && projectHeadId == null) {
            new GlobalException("未找到项目信息,请联系管理员");
        }

        return sysProjects;
    }


}