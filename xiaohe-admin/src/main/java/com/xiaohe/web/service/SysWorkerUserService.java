package com.xiaohe.web.service;

import com.xiaohe.web.domain.Dto.SysWorkerUserViewDto;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.Entity.SysWorkerUserView;
import com.xiaohe.web.domain.SelectDto;
import com.xiaohe.web.domain.Vo.SysWorkerUserVo;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年02月09日 20:19
 */

public interface SysWorkerUserService {

    /**
     * 查询工人信息列表
     *
     * @param sysWorkerUserVo 查询工人信息列表
     * @return 工人信息列表
     */
    public List<SysWorkerUser> getList(SysWorkerUserVo sysWorkerUserVo);

    /**
     * 新增
     * @param sysWorkerUserVo
     * @return sysProjectDto
     */
    public  int add(SysWorkerUserVo sysWorkerUserVo);

    /**
     * 修改
     * @param sysWorkerUserVo
     * @return sysProjectDto
     */
    public  int  edit(SysWorkerUserVo sysWorkerUserVo);


    /**
     * 删除
     * @param id
     * @return int
     */
    public  int  remove(Long id);


    /**
     * 查看
     * @param id
     * @return sysProjectDto
     */
    public  SysWorkerUser view(Long id);

    /**
     * 查询工人信息 下拉数据
     * @return sysProjectDto
     */
    List<SelectDto> getWorkerUserAll();

    /**
     * 工人借支或者生活费修改
     * @param sysWorkerUserView
     * @return sysProjectDto
     */
    int lendMoney(SysWorkerUserView sysWorkerUserView);

    /**
     * 工人借支或者生活费明细
     * @param id
     * @return sysProjectDto
     */
    List<SysWorkerUserViewDto> expenseDetail(SysWorkerUserView sysWorkerUserView);

    /**
     * 查询领班人信息 下拉数据
     * @return sysProjectDto
     */
    List<SelectDto> getWorkerUserGaffer();
}
