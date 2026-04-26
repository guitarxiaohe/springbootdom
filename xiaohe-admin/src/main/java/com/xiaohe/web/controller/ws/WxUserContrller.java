package com.xiaohe.web.controller.ws;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.page.TableDataInfo;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.common.utils.poi.ExcelUtil;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.WxUser;
import com.xiaohe.web.mapper.SysWorkerUserMapper;
import com.xiaohe.web.service.WxUserService;
import com.xiaohe.web.service.impl.SysWorkerUserServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api("微信用户表")
@RequestMapping("/wx/system/user")
public class WxUserContrller extends BaseController {

    @Resource
    private WxUserService wxUserService;

    @Resource
    SysWorkerUserMapper sysWorkerUserMapper;

    @Resource
    SysWorkerUserServiceImp sysWorkerUserServiceImp;

    /**
     * 查询微信登录人员表列表
     */
    @ApiOperation("查询微信登录人员表列表")
//    @Log(title = "查询微信登录人员表列表", businessType = BusinessType.INSERT)
    @GetMapping("/list")
    public TableDataInfo list(WxUser wxUser) {
        startPage();
        List<WxUser> list = wxUserService.selectWxUserList(wxUser);
        return getDataTable(list);
    }

    /**
     * 导出微信登录人员表列表
     */
    @Log(title = "微信登录人员表", businessType = BusinessType.EXPORT)
    @ApiOperation("导出微信登录人员表列表")
    @PostMapping("/export")
    public void export(HttpServletResponse response, WxUser wxUser) {
        List<WxUser> list = wxUserService.selectWxUserList(wxUser);
        ExcelUtil<WxUser> util = new ExcelUtil<WxUser>(WxUser.class);
        util.exportExcel(response, list, "微信登录人员表数据");
    }

    /**
     * 根据openid获取微信登录人员表详细信息
     */
    @ApiOperation("根据openid获取微信登录人员表详细信息")
    @Log(title = "根据openid获取微信登录人员表详细信息", businessType = BusinessType.OTHER)
    @GetMapping("/openidBy")
    public AjaxResult getInfo() throws Exception {
        AjaxResult ajaxResult = new AjaxResult();

        SysWorkerUser sysWorkerUser = sysWorkerUserMapper.selectOne(new QueryWrapper<SysWorkerUser>().eq("open_id", getLoginUser().getOpenid()));


        if (sysWorkerUser == null) {
            ajaxResult.put("code", 401);
            ajaxResult.put("msg", "请重新登录");
            return ajaxResult;
        }

        return AjaxResult.success(sysWorkerUserServiceImp.view(sysWorkerUser.getId()));
    }

    /**
     * 根据openid获取微信登录人员表详细信息
     */
    @ApiOperation("根据openid获取微信登录人员表详细信息")
    @Log(title = "根据openid获取微信登录人员表详细信息", businessType = BusinessType.OTHER)
    @GetMapping("/getUserById")
    public AjaxResult getUserById() throws Exception {
        AjaxResult ajaxResult = new AjaxResult();

        SysWorkerUser sysWorkerUser = sysWorkerUserMapper.selectOne(new QueryWrapper<SysWorkerUser>().eq("id", getLoginUser().getUserId()));

        if (sysWorkerUser == null) {
            ajaxResult.put("code", 401);
            ajaxResult.put("msg", "请重新登录");
            return ajaxResult;
        }

        return AjaxResult.success(sysWorkerUserServiceImp.view(sysWorkerUser.getId()));
    }


    /**
     * 获取微信登录人员表详细信息
     */
    @ApiOperation("获取微信登录人员表详细信息")
//    @Log(title = "获取微信登录人员表详细信息", businessType = BusinessType.EXPORT)
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(wxUserService.selectWxUserById(id));
    }

    /**
     * 新增微信登录人员表
     */
    @Log(title = "微信登录人员表", businessType = BusinessType.INSERT)
    @ApiOperation("新增微信登录人员表")
    @PostMapping
    public AjaxResult add(@RequestBody WxUser wxUser) {
        return toAjax(wxUserService.insertWxUser(wxUser));
    }

    /**
     * 修改微信登录人员表
     */
    @Log(title = "修改微信登录人员表", businessType = BusinessType.UPDATE)
    @ApiOperation("修改微信登录人员表")
    @PutMapping
    public AjaxResult edit(@RequestBody WxUser wxUser) throws Exception {
        return toAjax(wxUserService.updateWxUser(wxUser));
    }

    /**
     * 绑定部门
     */
    @Log(title = "绑定部门", businessType = BusinessType.UPDATE)
    @ApiOperation("绑定部门")
    @PutMapping(value = "/dept")
    public AjaxResult dept(@RequestBody WxUser wxUser) throws Exception {
        return toAjax(wxUserService.dept(wxUser));
    }

    /**
     * 删除微信登录人员表
     */
    @Log(title = "微信登录人员表", businessType = BusinessType.DELETE)
    @ApiOperation("删除微信登录人员表")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wxUserService.deleteWxUserByIds(ids));
    }

    /**
     * 确认成为审核人
     */
    @Log(title = "确认成为审核人", businessType = BusinessType.UPDATE)
    @ApiOperation("确认成为审核人")
    @PutMapping("/upExamine")
    public AjaxResult upExamine(@RequestBody WxUser wxUser) {
        return toAjax(wxUserService.upExamine(wxUser));
    }

    /**
     * 成为审核人\取消审核人（1：成为审核人 0：取消审核人）
     */
    @Log(title = "成为审核人", businessType = BusinessType.UPDATE)
    @ApiOperation("成为审核人")
    @PutMapping("/isExamine")
    public AjaxResult isExamine(@RequestBody WxUser wxUser) {
        return toAjax(wxUserService.isExamine(wxUser));
    }


    /**
     * 查询接待人下拉数据
     */
    @GetMapping("/reception-select/{deptId}")
    public AjaxResult getSelectData(@PathVariable("deptId") String deptId) {
        return AjaxResult.success(wxUserService.selectExamineWxUserList(deptId));
    }

}
