package com.xiaohe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohe.common.constant.Constants;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.domain.entity.SysUser;
import com.xiaohe.common.core.domain.model.LoginUser;
import com.xiaohe.common.utils.ServletUtils;
import com.xiaohe.common.utils.ip.AddressUtils;
import com.xiaohe.common.utils.ip.IpUtils;
import com.xiaohe.framework.web.service.TokenService;
import com.xiaohe.system.domain.SysLogininfor;
import com.xiaohe.system.service.ISysLogininforService;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.Vo.SysWorkerUserVo;
import com.xiaohe.web.domain.WX;
import com.xiaohe.web.domain.WxUser;
import com.xiaohe.web.mapper.SysWorkerUserMapper;
import com.xiaohe.web.service.SysWorkerUserService;
import com.xiaohe.web.service.WxLoginService;
import com.xiaohe.web.utils.WxUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class WxLoginServiceImp implements WxLoginService {
    @Resource
    private TokenService tokenService;

    @Resource
    WxUtils wxUtils;

    @Autowired
    private ISysLogininforService iSysLogininforService;


    @Resource
    private  SysWorkerUserService sysWorkerUserService;

    @Resource
    private SysWorkerUserMapper sysWorkerUserMapper;

    /**
     * 微信登录验证
     *
     * @return 结果
     */

    public AjaxResult wxLogin(WX wx) throws Exception {
        WxUser wxUser = new WxUser();
        AjaxResult ajax = AjaxResult.success();
        // 通过code 获取openid
       String openId = wxUtils.getOpenId(wx.getCode());
        wxUser.setOpenId(openId);
        /**
         * 判断wx用户表是否存在该openid 存在登录 否则注册登录
         */

        SysWorkerUser sysWorkerUser = sysWorkerUserMapper.getOpenId(openId);


        // 注册
        if ( sysWorkerUser ==null) {
//            wxUserService.insertWxUser(wxUser);
            return AjaxResult.success("请注册信息");
        }

        // 登录
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = new SysUser();
        loginUser.setOpenid(openId);
        loginUser.setUserId(sysWorkerUser.getId());
        sysUser.setUserName(sysWorkerUser.getName());
        sysUser.setPhonenumber(sysWorkerUser.getPhone());
        loginUser.setUser(sysUser);
        // 记录登录
       // wxLogininfor(sysWorkerUser.getName(), "0");
        ajax.put(Constants.TOKEN, tokenService.createToken(loginUser));
        return AjaxResult.success(ajax);
    }


    public AjaxResult wr(WX wx) throws Exception {

        // 通过code 获取openid
     String openId = wxUtils.getOpenId(wx.getCode());



        // 判断是否存在名称
        if (wx.getName() == null) {
            return AjaxResult.error("请填写名称");
        }


        // 判断是否存在名称
        SysWorkerUser isName = sysWorkerUserMapper.selectOne(new QueryWrapper<SysWorkerUser>().eq("name", wx.getName()));
        if (isName != null) {
            return AjaxResult.error("该名称已存在");
        }

        // 判断是否存在手机号
        SysWorkerUser isPhone = sysWorkerUserMapper.selectOne(new QueryWrapper<SysWorkerUser>().eq("phone", wx.getPhone()));
        if (isPhone != null) {
            return AjaxResult.error("该手机号已存在");
        }

        Pattern kong = Pattern.compile("\\s");//\\s匹配空格符、制表符、换行符和回车符
        Matcher kongRe = kong.matcher(wx.getName());
        if (kongRe.find()) {
            return AjaxResult.error("名称不允许出现空格！");
        }

        String regex = "^[\\u4E00-\\u9FA5A-Za-z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(wx.getName());
        if (!matcher.find()) {
            return AjaxResult.error("只允许出现中文、字母的名称！");
        }


        SysWorkerUserVo sysWorkerUserVo = new SysWorkerUserVo();
        sysWorkerUserVo.setOpenId(openId);
        sysWorkerUserVo.setName(wx.getName().trim());
        sysWorkerUserVo.setPhone(wxUtils.getWxPhone(wx.getPhoneCode()));
        sysWorkerUserVo.setManHour(new BigDecimal(0));
        sysWorkerUserVo.setGaffer("2");
        sysWorkerUserVo.setLendMoney(new BigDecimal(0));
        sysWorkerUserVo.setLivingExpenses(new BigDecimal(0));
        sysWorkerUserService.add(sysWorkerUserVo);


        AjaxResult ajax = AjaxResult.success();
        // 登录
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = new SysUser();
        sysUser.setPhonenumber(wx.getPhone());
        loginUser.setOpenid(wx.getOpenId());
        loginUser.setUser(sysUser);
//        // 记录登录
//        wxLogininfor(wx.getName(), "0");
        ajax.put(Constants.TOKEN, tokenService.createToken(loginUser));
        return AjaxResult.success("注册成功", ajax);
    }


    /**
     * 记录登录信息
     *
     * @param userName state
     */
    public void wxLogininfor(String userName, String state) {
        // 记录登录日志
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        String address = AddressUtils.getRealAddressByIP(ip);


        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));

        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(userName);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(address);
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg("登录成功");
        logininfor.setStatus(state);
        iSysLogininforService.insertLogininfor(logininfor);
    }
}
