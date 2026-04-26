package com.xiaohe.web.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaohe.common.constant.Constants;
import com.xiaohe.common.core.redis.RedisCache;
import com.xiaohe.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiaohe.web.mapper.WxUserMapper;
import com.xiaohe.web.domain.WxUser;
import com.xiaohe.web.service.WxUserService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.xiaohe.common.utils.SecurityUtils.getLoginUser;

/**
 * 微信登录人员表Service业务层处理
 * 
 * @author xiaohe
 * @date 2023-09-21
 */


@Service("wxUserService")
public class WxUserServiceImpl implements WxUserService
{
    @Autowired
    private WxUserMapper wxUserMapper;
    @Resource
    public RedisCache redisCache;

    private static WxUserServiceImpl wxUtils;
    @Resource
    private WxUserService wxUserService;


    @PostConstruct
    private void into() {
        wxUtils = this;
        wxUtils.redisCache = this.redisCache;

    }


    /**
     * 查询微信登录人员表
     * 
     * @param id 微信登录人员表主键
     * @return 微信登录人员表
     */
    @Override
    public WxUser selectWxUserById(Long id)
    {
        return wxUserMapper.selectWxUserById(id);
    }

    /**
     * 根据openid获取微信登录人员表详细信息
     *
     * @param openId 微信登录人员表openId
     * @return 微信登录人员表
     */
    @Override
    public WxUser selectWxUserByOpenId(String openId) throws Exception {
        if( wxUserMapper.selectWxUserByOpenId(openId) ==null){
            wxUtils.redisCache.deleteObject(Constants.LOGIN_USER_KEY);
        }
        return  wxUserMapper.selectWxUserByOpenId(openId);
    }

    /**
     * 查询微信登录人员表列表
     * 
     * @param wxUser 微信登录人员表
     * @return 微信登录人员表
     */
    @Override
    public List<WxUser> selectWxUserList(WxUser wxUser)
    {
        return wxUserMapper.selectWxUserList(wxUser);
    }



    /**
     * 查询接待人下拉数据
     * @return 查询接待人下拉数据
     */
    @Override
    public List<WxUser> selectExamineWxUserList(String deptId)
    {
        return   wxUserMapper.allSelectExamineWxUserList(deptId);

    }




    /**
     * 新增微信登录人员表
     * 
     * @param wxUser 微信登录人员表
     * @return 结果
     */
    @Override
    public int insertWxUser(WxUser wxUser)
    {


        wxUser.setCreateTime(DateUtils.getNowDate());
        wxUser.setIsExamine("0");
        // 首次注册默认1816310 为其他人
        wxUser.setDeptId("1816310");
        return wxUserMapper.insertWxUser(wxUser);
    }

    /**
     * 修改微信登录人员表
     * 
     * @param wxUser 微信登录人员表
     * @return 结果
     */
    @Override
    public int updateWxUser(WxUser wxUser) throws Exception {
        WxUser isName = wxUserService.getName(wxUser.getName());
        WxUser isPhone = wxUserService.getPhone(wxUser.getPhone());
        WxUser userInfo =    wxUserMapper. selectWxUserByOpenId(getLoginUser().getOpenid());


        // 判断是否存在名称
        if (!userInfo.getName().equals(wxUser.getName()) &&  isName != null) {
            throw new Exception("该名称已存在");
        }

        // 判断是否存在手机号
        if (!userInfo.getPhone().equals(wxUser.getPhone()) && isPhone != null) {
            throw new Exception("该手机号已存在");
        }


        Pattern kong = Pattern.compile("\\s");//\\s匹配空格符、制表符、换行符和回车符
        Matcher kongRe = kong.matcher(wxUser.getName());
        if (kongRe.find()) {
               throw new Exception("名称不允许出现空格！");
        }

        String regex = "^[\\u4E00-\\u9FA5A-Za-z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(wxUser.getName());
        if (!matcher.find()) {
               throw new Exception("只允许出现中文、字母的名称！");
        }
        return wxUserMapper.updateWxUser(wxUser);
    }

    /**
     * 批量删除微信登录人员表
     * 
     * @param ids 需要删除的微信登录人员表主键
     * @return 结果
     */
    @Override
    public int deleteWxUserByIds(Long[] ids)
    {
        return wxUserMapper.deleteWxUserByIds(ids);
    }

    /**
     * 删除微信登录人员表信息
     * 
     * @param id 微信登录人员表主键
     * @return 结果
     */
    @Override
    public int deleteWxUserById(Long id)
    {
        return wxUserMapper.deleteWxUserById(id);
    }


    /**
     * 查询是否有openid
     *
     * @param openid 微信登录openid
     * @return 结果
     */
    public  WxUser getOpenId(String openid){

        return  wxUserMapper.getOpenId(openid);

    }


    /**
     * 查询是否有openid
     * @param name 微信登录openid
     * @return 结果
     */
    public  WxUser getName(String name){

        return  wxUserMapper.getName(name);

    }

    /**
     * 查询是否有Phone
     * @param phone
     * @return 结果
     */
    public  WxUser getPhone(String phone){
        return  wxUserMapper.getPhone(phone);
    }



    /**
     * 绑定部门
     * @param wxUser
     * @return 结果
     */
    public  int dept(WxUser wxUser){
        return  wxUserMapper.dept(wxUser);
    }

    /**
     * 确认成为审核人
     * @param wxUser
     * @return 结果
     */
    public int upExamine(WxUser wxUser){
        wxUser.setOpenId(getLoginUser().getOpenid());
        return wxUserMapper.upExamine(wxUser);
    }
    /**
     * 成为审核人
     * @param wxUser
     * @return 结果
     */
    public int isExamine(WxUser wxUser){
        return wxUserMapper.isExamine(wxUser);
    }





}
