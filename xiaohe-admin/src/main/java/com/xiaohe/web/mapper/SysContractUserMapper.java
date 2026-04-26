package com.xiaohe.web.mapper;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.mapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月28日 15:16
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Dto.SysContractDto;
import com.xiaohe.web.domain.Entity.SysContractUser;
import com.xiaohe.web.domain.Vo.SysContractVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysContractUserMapper extends BaseMapper<SysContractUser> {

    /**
     * 通过人员id 和项目id 查询包工团体
     *
     * @return sysProjectDto
     */
    public SysContractDto selContractByUserId(@Param("sysContractVo") SysContractVo sysContractVo);

}
