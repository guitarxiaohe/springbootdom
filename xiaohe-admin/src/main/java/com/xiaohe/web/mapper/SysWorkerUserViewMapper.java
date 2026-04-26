package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Dto.SysWorkerUserViewDto;
import com.xiaohe.web.domain.Entity.SysWorkerUserView;
import org.apache.ibatis.annotations.Mapper;

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
public interface SysWorkerUserViewMapper extends BaseMapper<SysWorkerUserView> {
    /**
     * 查询借支 生活费明细
     *
     * @param sysWorkerUserView
     * @return
     */
    public List<SysWorkerUserViewDto> expenseDetail(SysWorkerUserView sysWorkerUserView);


}
