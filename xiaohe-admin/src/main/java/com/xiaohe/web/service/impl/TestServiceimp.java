package com.xiaohe.web.service.impl;

import com.xiaohe.common.utils.SecurityUtils;
import com.xiaohe.common.utils.bean.BeanUtils;
import com.xiaohe.web.domain.Dto.TestDto;
import com.xiaohe.web.domain.Entity.SysTest;
import com.xiaohe.web.domain.Vo.TestVo;
import com.xiaohe.web.mapper.TestMapper;
import com.xiaohe.web.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service("testService")
public class TestServiceimp implements TestService {


    @Resource
    private TestService testService;


    @Resource
    TestMapper testMapper;
    /**
     * 得到指定日期的一天的的最后时刻23:59:59
     *
     * @param testVo
     * @return
     */
    public List<SysTest> getList(TestVo testVo) {
     return testMapper.selectListQuery(testVo);
    }



    public int add(TestVo testVo) {
        SysTest test = new SysTest();
        BeanUtils.copyProperties(testVo, test);
        test.setCreateTime(new Date());
        test.setCreateBy(SecurityUtils.getUsername());
        return   testMapper.insert(test);

    }



    @Override
    public boolean removeById(Long id) {
        return false;
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        return false;
    }

    @Override
    public int updateById(TestVo testVo) {
        SysTest test = new SysTest();
        BeanUtils.copyProperties(testVo, test);
        test.setCreateTime(new Date());
        test.setCreateBy(SecurityUtils.getUsername());
        return testMapper.updateById(test);
    }

    @Override
    public TestDto getById(Long id) {
        return null;
    }


}
