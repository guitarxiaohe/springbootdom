package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Dto.SysContractDto;
import com.xiaohe.web.domain.Entity.SysContract;
import com.xiaohe.web.domain.Vo.SysContractVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.mapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月28日 14:13
 */
@Mapper
public interface SysContractMapper extends BaseMapper<SysContract> {

    /**
     * 列表
     * @param sysContractVo
     * @return
     */
    List<SysContractDto> queryList(@Param("sysContractVo") SysContractVo sysContractVo);
}
