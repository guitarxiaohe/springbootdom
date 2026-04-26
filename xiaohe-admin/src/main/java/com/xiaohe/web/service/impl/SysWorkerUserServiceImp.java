package com.xiaohe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohe.common.utils.SecurityUtils;
import com.xiaohe.common.utils.bean.BeanUtils;
import com.xiaohe.web.domain.Dto.SysWorkerUserViewDto;
import com.xiaohe.web.domain.Entity.SysAccounts;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.Entity.SysWorkerUserView;
import com.xiaohe.web.domain.SelectDto;
import com.xiaohe.web.domain.Vo.SysWorkerUserVo;
import com.xiaohe.web.mapper.SysAccountsMapper;
import com.xiaohe.web.mapper.SysWorkerUserMapper;
import com.xiaohe.web.mapper.SysWorkerUserViewMapper;
import com.xiaohe.web.service.SysWorkerUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service.impl
 * @Description: 工人信息imp层
 * @date Date : 2024年02月08日 22:55
 */

@Service
public class SysWorkerUserServiceImp implements SysWorkerUserService {

    @Resource
    SysWorkerUserMapper sysWorkerUserMapper;

    @Resource
    SysWorkerUserViewMapper sysWorkerUserViewMapper;

    @Resource
    SysAccountsMapper sysAccountsMapper;

    /**
     * 列表
     *
     * @param sysWorkerUserVo 查询项目列表
     * @return
     */
    @Override
    public List<SysWorkerUser> getList(SysWorkerUserVo sysWorkerUserVo) {
        QueryWrapper<SysWorkerUser> sysProjectQueryWrapper = new QueryWrapper<>();
        sysProjectQueryWrapper.like(ObjectUtils.isNotEmpty(sysWorkerUserVo.getName()), "name", sysWorkerUserVo.getName())
                .between(sysWorkerUserVo.getCreateTime() != null && sysWorkerUserVo.getEndTime() != null,
                        "create_time", sysWorkerUserVo.getCreateTime(), sysWorkerUserVo.getEndTime());


        List<SysWorkerUser> sysWorkerUserList = sysWorkerUserMapper.selectList(sysProjectQueryWrapper);
        // 生活费 借支累计
        sysWorkerUserList.forEach(t -> {
            // 生活费
            BigDecimal livingExpenses = new BigDecimal(0);
            // 借支
            BigDecimal lendMoney = new BigDecimal(0);

            // 总工时
            BigDecimal manHour = new BigDecimal(0);

            SysWorkerUserView sysWorkerUserView = new SysWorkerUserView();
            sysWorkerUserView.setWorkerUserId(t.getId());
            List<SysWorkerUserViewDto> sysWorkerUserViewDtos = expenseDetail(sysWorkerUserView);
            // 查询审核通过的数据
            List<SysAccounts> accounts = sysAccountsMapper.selectList(new QueryWrapper<SysAccounts>()
                    .eq("user_id", t.getId()).eq("examine_state","2")

            );

            for (SysAccounts p : accounts) {
                manHour = manHour.add(p.getManHour());
            }

            for (SysWorkerUserViewDto y :
                    sysWorkerUserViewDtos) {
                livingExpenses = livingExpenses.add(y.getLivingExpenses());
                lendMoney = lendMoney.add(y.getLendMoney());
            }
            t.setLivingExpenses(livingExpenses);
            t.setLendMoney(lendMoney);
            t.setManHour(manHour);
        });


        return sysWorkerUserList;
    }

    /**
     * 新增
     *
     * @param sysWorkerUserVo
     * @return
     */
    @Override
    public int add(SysWorkerUserVo sysWorkerUserVo) {
        SysWorkerUser sysWorkerUser = new SysWorkerUser();
        BeanUtils.copyProperties(sysWorkerUserVo, sysWorkerUser);
        sysWorkerUser.setState("1");
        sysWorkerUser.setCreateTime(new Date());
//         sysWorkerUser.setCreateBy(SecurityUtils.getUsername());
        return sysWorkerUserMapper.insert(sysWorkerUser);
    }

    /**
     * 修改
     *
     * @param sysWorkerUserVo
     * @return
     */
    @Override
    public int edit(SysWorkerUserVo sysWorkerUserVo) {
        SysWorkerUser sysWorkerUser = new SysWorkerUser();
        BeanUtils.copyProperties(sysWorkerUserVo, sysWorkerUser);
        sysWorkerUser.setUpdateTime(new Date());
        sysWorkerUser.setUpdateBy(SecurityUtils.getUsername());
        return sysWorkerUserMapper.updateById(sysWorkerUser);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public int remove(Long id) {
        return sysWorkerUserMapper.deleteById(id);
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @Override
    public SysWorkerUser view(Long id) {

        SysWorkerUser sysWorkerUser = sysWorkerUserMapper.selectById(id);


        // 生活费
        BigDecimal livingExpenses = new BigDecimal(0);
        // 借支
        BigDecimal lendMoney = new BigDecimal(0);
        // 总工时
        BigDecimal manHour = new BigDecimal(0);
        SysWorkerUserView sysWorkerUserView = new SysWorkerUserView();
        sysWorkerUserView.setWorkerUserId(sysWorkerUser.getId());
        List<SysWorkerUserViewDto> sysWorkerUserViewDtos = expenseDetail(sysWorkerUserView);
        for (SysWorkerUserViewDto y :
                sysWorkerUserViewDtos) {
            livingExpenses = livingExpenses.add(y.getLivingExpenses());
            lendMoney = lendMoney.add(y.getLendMoney());
        }

        List<SysAccounts> accounts = sysAccountsMapper.selectList(new QueryWrapper<SysAccounts>()
                .eq("user_id", id).eq("examine_state","2"));

        for (SysAccounts p : accounts) {
            manHour = manHour.add(p.getManHour());
        }
        sysWorkerUser.setLendMoney(lendMoney);
        sysWorkerUser.setLivingExpenses(livingExpenses);
        sysWorkerUser.setManHour(manHour);
        return sysWorkerUser;
    }

    /**
     * 查询工人信息 下拉数据
     *
     * @return sysProjectDto
     */
    @Override
    public List<SelectDto> getWorkerUserAll() {
        List<SysWorkerUser> sysWorkerUserList = sysWorkerUserMapper.selectList(new QueryWrapper<SysWorkerUser>().eq("state", "1"));

        List<SelectDto> selectDtoList = new ArrayList<>();
        sysWorkerUserList.forEach(t -> {
            SelectDto selectDto = new SelectDto();
            selectDto.setLabel(t.getName());
            selectDto.setValue(t.getId());
            selectDtoList.add(selectDto);
        });
        return selectDtoList;
    }

    /**
     * 工人借支或者生活费修改
     *
     * @param sysWorkerUserViewVo
     * @return sysProjectDto
     */
    @Override
    public int lendMoney(SysWorkerUserView sysWorkerUserViewVo) {
        SysWorkerUserView sysWorkerUserView = new SysWorkerUserView();
        BeanUtils.copyProperties(sysWorkerUserViewVo, sysWorkerUserView);
        sysWorkerUserView.setCreateTime(new Date());
        sysWorkerUserView.setCreateBy(SecurityUtils.getUsername());
        return sysWorkerUserViewMapper.insert(sysWorkerUserView);
    }

    /**
     * 工人借支或者生活费明细
     *
     * @param sysWorkerUserView
     * @return sysProjectDto
     */
    @Override
    public List<SysWorkerUserViewDto> expenseDetail(SysWorkerUserView sysWorkerUserView) {
//        .eq("createTime", sysWorkerUserView.getCreateTime())
        return sysWorkerUserViewMapper.expenseDetail(sysWorkerUserView);
    }

    /**
     * 查询领班人信息 下拉数据
     *
     * @return sysProjectDto
     */
    @Override
    public List<SelectDto> getWorkerUserGaffer() {
        QueryWrapper<SysWorkerUser> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("gaffer", "1");

        List<SelectDto> selectDtoList = new ArrayList<>();
        List<SysWorkerUser> sysWorkerUserList = sysWorkerUserMapper.selectList(objectQueryWrapper);
        sysWorkerUserList.forEach(t -> {
            SelectDto selectDto = new SelectDto();
            selectDto.setLabel(t.getName());
            selectDto.setValue(t.getId());
            selectDtoList.add(selectDto);
        });
        return selectDtoList;
    }
}