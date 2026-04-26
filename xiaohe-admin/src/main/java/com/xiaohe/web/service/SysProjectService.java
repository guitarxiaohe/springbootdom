package com.xiaohe.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.web.domain.Dto.SysProjectDto;
import com.xiaohe.web.domain.Entity.SysProject;
import com.xiaohe.web.domain.Vo.SysProjectVo;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service
 * @Description: 项目service层
 * @date Date : 2024年02月08日 22:55
 */

public interface SysProjectService extends IService<SysProject> {


    /**
     * 查询项目列表
     *
     * @param sysProjectVo 查询项目列表
     * @return 项目列表
     */
    public  List<SysProjectDto> selectProjectList(SysProjectVo sysProjectVo);

    /**
     * 新增
     * @param sysProjectVo
     * @return sysProjectDto
     */
    public  int add(SysProjectVo sysProjectVo);

    /**
     * 修改
     * @param sysProjectVo
     * @return sysProjectDto
     */
    public  int  edit(SysProjectVo sysProjectVo);


    /**
     * 删除
     * @param projectId
     * @return int
     */
    public  int  remove(Long projectId);


    /**
     * 查看
     * @param projectId
     * @return sysProjectDto
     */
    public  SysProjectDto view(Long projectId);

    /**
     * 查询所有列表
     * @return sysProjectDto
     */
    List<SysProject> getProjectAll();


    /**
     * 通过人员id 查询项目
     * @return sysProjectDto
     */
    List<SysProject> selProjectByUserId(Long userId);



}
