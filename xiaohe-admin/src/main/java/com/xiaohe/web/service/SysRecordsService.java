package com.xiaohe.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohe.web.domain.Export.SysRecords;

import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年03月05日 15:04
 */

public interface SysRecordsService extends IService<SysRecords> {

    /**
     * 列表
     *
     * @param sysRecords
     * @return sysProjectDto
     */
    List<SysRecords> selectList(SysRecords sysRecords);

    /**
     * 新增
     *
     * @param sysRecords
     * @return SysRecords
     */
    int add(SysRecords sysRecords);

    /**
     * 修改
     *
     * @param sysRecords
     * @return SysRecords
     */
    int edit(SysRecords sysRecords);

    /**
     * 删除
     *
     * @param id
     * @return SysRecords
     */
    int remove(String id);

    /**
     * 查看
     *
     * @param id
     * @return sysProjectDto
     */
    SysRecords view(Long id);


}
