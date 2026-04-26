package com.xiaohe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohe.web.domain.Entity.SysTest;
import com.xiaohe.web.domain.Vo.TestVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestMapper extends BaseMapper<SysTest> {


    /**
     * 列表
     *
     * @param testVo 查询列表
     * @return
     */
     List<SysTest> selectListQuery(@Param("testVo") TestVo testVo);



//    /**
//     * 新增
//     * @param testVo
//     * @return
//     */
//    boolean add(TestVo testVo);
}
