package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Entity.SysProject;
import com.xiaohe.web.domain.Entity.SysUserProject;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
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
public interface SysWorkerUserMapper extends BaseMapper<SysWorkerUser> {
    /**
     * 查询是否有openid
     *
     * @param openId 需要删除的数据主键集合
     * @return 结果
     */
    public SysWorkerUser getOpenId(String openId);




    /**
     * 通过人查询项目
     *
     * @param openId 需要删除的数据主键集合
     * @return 结果
     */
    public List<SysProject> selProjectByUserId(@Param("sysProjectList")   List<SysUserProject> sysProjectList );

}
