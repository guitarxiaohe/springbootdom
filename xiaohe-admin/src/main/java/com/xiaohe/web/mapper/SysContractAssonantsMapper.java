package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Dto.SysContractAssonantsDto;
import com.xiaohe.web.domain.Entity.SysContractAssonants;
import com.xiaohe.web.domain.Vo.SysContractAssonantsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.mapper
 * @Description:
 * @date Date : 2024年02月28日 17:48
 */
@Mapper
public interface SysContractAssonantsMapper extends BaseMapper<SysContractAssonants> {

    /**
     * 列表
     *
     * @param sysContractAssonantsVo
     * @return sysProjectDto
     */
    List<SysContractAssonantsDto> queryList(@Param("sysContractAssonantsVo") SysContractAssonantsVo sysContractAssonantsVo);
}
