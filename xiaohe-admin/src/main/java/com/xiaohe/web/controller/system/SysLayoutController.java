package com.xiaohe.web.controller.system;

import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.web.service.SysLayoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.controller.system
 * @Description: 首页Controller层
 * @date Date : 2024年02月21日 13:31
 */

@RestController
@Api(value = "首页")
@RequestMapping("/system/layout")
public class SysLayoutController extends BaseController {

    @Resource
    SysLayoutService sysLayoutService;


    /**
     * 获取项目数量 人员数量 领班数量
     * @return SysLayoutDto
     */
    @GetMapping("/getNum")
    @ApiOperation(value = "获取项目数量 人员数量 领班数量")
    public AjaxResult getNum() {
        return AjaxResult.success(sysLayoutService.getNum());
    }


    /**
     * 获取小程序 趋势信息
     * @return SysLayoutDto
     */
    @GetMapping("/getWxTendency/{wxType}")
    @ApiOperation(value = "获取小程序 趋势信息")
    public AjaxResult getWxTendency(@PathVariable("wxType") String wxType) {
        return AjaxResult.success(sysLayoutService.getWxTendency(wxType));
    }

}