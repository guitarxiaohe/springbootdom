package com.xiaohe.web.mapper;

import com.xiaohe.web.domain.Dto.SysStatisticsDto;
import com.xiaohe.web.domain.Export.SysAccountsEx;
import com.xiaohe.web.domain.Export.SysWorkerUserViewEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.mapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月29日 11:46
 */
@Mapper
public interface SysStatisticsMapper {


    public List<SysStatisticsDto> getAccountByUser();

    /**
     * 根据用户id 查询相关数据 累计工时 和 借支 生活费
     */

    public SysStatisticsDto getAccountsByUserId(@Param("userId")Long userId);


    /**
     * 根据用户id 查询相关数据
     */

    public List<SysAccountsEx> getAccountsEx(@Param("userId") Long userId);

    /**
     * 根据用户id 查询相关数据
     */

    List<SysWorkerUserViewEx> getWorkerUserViewEx(@Param("userId") Long userId);


    /**
     * 个人
     *
     * @param sysStatisticsVo
     * @return sysProjectDto
     */
    SysStatisticsDto getViewDeilt(@Param("userId") Long userId);
}
