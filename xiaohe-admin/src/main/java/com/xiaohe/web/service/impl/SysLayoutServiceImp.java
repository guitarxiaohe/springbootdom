package com.xiaohe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohe.web.domain.Dto.SysLayoutNumDto;
import com.xiaohe.web.domain.Entity.SysAccounts;
import com.xiaohe.web.domain.Entity.SysProject;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.WxDataAnalyze.WXWeekDataDto;
import com.xiaohe.web.mapper.SysAccountsMapper;
import com.xiaohe.web.mapper.SysProjectMapper;
import com.xiaohe.web.mapper.SysWorkerUserMapper;
import com.xiaohe.web.service.SysLayoutService;
import com.xiaohe.web.utils.WxUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service.impl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月21日 13:39
 */

@Service
public class SysLayoutServiceImp implements SysLayoutService {
    @Resource
    private SysProjectMapper sysProjectMapper;

    @Resource
    private SysWorkerUserMapper sysWorkerUserMapper;

    @Resource
    private SysAccountsMapper sysAccountsMapper;

    @Resource
    private WxUtils wxUtils;

    /**
     * 获取项目数量 人员数量 领班数量
     *
     * @return SysLayoutDto
     */
    @Override
    public SysLayoutNumDto getNum() {

        SysLayoutNumDto sysLayoutNumDto = new SysLayoutNumDto();
        // 查询项目总数
        List<SysProject> sysProjects = sysProjectMapper.selectList(null);
        sysLayoutNumDto.setProjectSize(sysProjects.size());
        // 查询启用人员总数
        List<SysWorkerUser> sysWorkerUser = sysWorkerUserMapper.selectList(new QueryWrapper<SysWorkerUser>().eq("state", "1"));
        sysLayoutNumDto.setUserSize(sysWorkerUser.size());

        // 查询所有工人累计工时
        List<SysAccounts> sysAccounts = sysAccountsMapper.selectList(null);
        BigDecimal bigDecimal = new BigDecimal(0);


        for (SysAccounts t : sysAccounts) {
            bigDecimal = bigDecimal.add(t.getManHour());
        }

        sysLayoutNumDto.setManHourSize(bigDecimal);
        return sysLayoutNumDto;
    }

    /**
     * 获取小程序 趋势信息
     *
     * @return SysLayoutDto
     */
    @Override
    public WXWeekDataDto getWxTendency(String wxType) {

        return wxUtils.getWxWeekTendency();
    }
}