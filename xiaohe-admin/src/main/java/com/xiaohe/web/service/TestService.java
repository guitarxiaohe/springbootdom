package com.xiaohe.web.service;
import com.xiaohe.web.domain.Dto.TestDto;
import com.xiaohe.web.domain.Entity.SysTest;
import com.xiaohe.web.domain.Vo.TestVo;

import java.util.List;


public interface TestService {

    /**
     * 查询列表
     * @return 列表
     */
    public List<SysTest> getList(TestVo testVo);


    /**
     * 新增
     * @param testVo
     * @return
     */

    int add(TestVo testVo);

    boolean removeById(Long id);

    boolean removeByIds(List<Long> ids);

    int updateById(TestVo testVo);

    TestDto getById(Long id);
}
