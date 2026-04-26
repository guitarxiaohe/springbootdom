package com.xiaohe.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe.web.domain.Export.SysRecords;
import com.xiaohe.web.mapper.SysRecordsMapper;
import com.xiaohe.web.service.SysRecordsService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.service.impl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年03月05日 15:04
 */

@Service
public class SysRecordsServiceImp extends ServiceImpl<SysRecordsMapper, SysRecords> implements SysRecordsService {
  @Resource
  private SysRecordsMapper sysRecordsMapper;

    /**
     * 列表
     *
     * @param sysRecords
     * @return sysProjectDto
     */
    @Override
    public List<SysRecords> selectList(SysRecords sysRecords) {
        return sysRecordsMapper.selectList(null);
    }

    /**
     * 新增
     *
     * @param sysRecords
     * @return SysRecords
     */
    @Override
    public int add(SysRecords sysRecords) {
        return sysRecordsMapper.insert(sysRecords);
    }

    /**
     * 修改
     *
     * @param sysRecords
     * @return SysRecords
     */
    @Override
    public int edit(SysRecords sysRecords) {
        return sysRecordsMapper.updateById(sysRecords);
    }

    /**
     * 删除
     *
     * @param id
     * @return SysRecords
     */
    @Override
    public int remove(String id) {
        return sysRecordsMapper.deleteBatchIds(Arrays.asList(id));
    }


    /**
     * 查看
     *
     * @param id
     * @return sysProjectDto
     */
    @Override
    public SysRecords view(Long id) {
        return sysRecordsMapper.selectById(id);
    }
}