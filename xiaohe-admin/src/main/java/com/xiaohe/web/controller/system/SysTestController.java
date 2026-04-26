package com.xiaohe.web.controller.system;

import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.web.domain.Vo.TestVo;
import com.xiaohe.web.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@Api(value="测试")
@RequestMapping("/test")
public class SysTestController extends BaseController {
    @Resource
    TestService testService;

    /**
     * 列表
     * @param testVo
     * @return testVo
     */
    @GetMapping("/list")
    @ApiOperation(value="列表")
    public TableDataInfo list(TestVo testVo){
        startPage();
        return getDataTable(testService.getList(testVo));
    }

//
///// 新增
//    @PostMapping("/add")
//    public AjaxResult add(@RequestBody TestVo testVo) {
//        return AjaxResult.success(testService.add(testVo));
//    }
//
//    // 根据ID删除
//    @DeleteMapping("/delete/{id}")
//    public boolean delete(@PathVariable Long id) {
//        return testService.removeById(id);
//    }
//
//    // 批量删除
//    @DeleteMapping("/delete/batch")
//    public boolean deleteBatch(@RequestBody List<Long> ids) {
//        return testService.removeByIds(ids);
//    }
//
//    // 修改
//    @PutMapping("/update")
//    public AjaxResult update(@RequestBody TestVo testVo) {
//        return   AjaxResult.success(testService.updateById(testVo));
//    }
//
//    // 根据ID查询
//    @GetMapping("/detail/{id}")
//    public AjaxResult detail(@PathVariable Long id) {
//        return AjaxResult.success(testService.getById(id));
//    }





}
