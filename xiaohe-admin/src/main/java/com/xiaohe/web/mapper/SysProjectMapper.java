package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Dto.SysProjectDto;
import com.xiaohe.web.domain.Entity.SysProject;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.Vo.SysProjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.mapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月08日 22:57
 */
@Mapper
public interface SysProjectMapper extends BaseMapper<SysProject> {

    /**
     * 通过领班人员id
     */
    public List<SysProject> selProjectByUserId(Long userId);


    /**
     * 通过人员id 查询项目
     */
    public List<SysProject> selProjectUserId(Long userId);




    /**
     * 列表
     *
     * @param sysProjectVo 查询项目列表
     * @return
     */
    public List<SysProjectDto> selectListQuery(@Param("sysProjectVo") SysProjectVo sysProjectVo);

    /**
     * 详情
     *
     * @param projectId 查询项目id
     * @return
     */
    public SysProject selectView(Long projectId);



    /**
     * 通过项目id 查询当前人员
     *
     * @return sysProjectDto
     */
    List<SysWorkerUser> selUserByProjectId(String userIds);
}
