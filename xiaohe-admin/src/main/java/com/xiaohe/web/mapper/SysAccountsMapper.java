package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Entity.SysAccounts;
import com.xiaohe.web.domain.Vo.SysAccountsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.mapper
 * @Description: 记功表Mapper
 * @date Date : 2024年02月12日 21:36
 */

public interface SysAccountsMapper extends BaseMapper<SysAccounts> {


    /**
     * 列表
     *
     * @param sysProjectVo 查询列表
     * @return
     */
    public List<SysAccounts> selectListQuery(@Param("sysAccountsVo") SysAccountsVo sysAccountsVo);
    public List<SysAccounts> selectListQuery1(@Param("sysAccountsVo") SysAccountsVo sysAccountsVo);


    List<SysAccounts> selUserByProjectId(@Param("sysAccountsVo") SysAccountsVo sysAccountsVo);
}
