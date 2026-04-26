package com.xiaohe.web.service;

import java.util.List;
import com.xiaohe.web.domain.WxUser;

/**
 * 微信登录人员表Service接口
 * 
 * @author xiaohe
 * @date 2023-09-21
 */

public interface WxUserService
{
    /**
     * 查询微信登录人员表
     * 
     * @param id 微信登录人员表主键
     * @return 微信登录人员表
     */
    public WxUser selectWxUserById(Long id);

    /**
     * 根据openid获取微信登录人员表详细信息
     *
     * @param openid 微信登录人员表openid
     * @return 微信登录人员表
     */
    public WxUser selectWxUserByOpenId(String openid) throws Exception;


    /**
     * 查询接待人下拉数据
     * @return 查询接待人下拉数据
     */
    public List<WxUser> selectExamineWxUserList(String deptId);


    /**
     * 新增微信登录人员表
     * 
     * @param wxUser 微信登录人员表
     * @return 结果
     */
    public int insertWxUser(WxUser wxUser);

    /**
     * 修改微信登录人员表
     * 
     * @param wxUser 微信登录人员表
     * @return 结果
     */
    public int updateWxUser(WxUser wxUser) throws Exception;

    /**
     * 批量删除微信登录人员表
     * 
     * @param ids 需要删除的微信登录人员表主键集合
     * @return 结果
     */
    public int deleteWxUserByIds(Long[] ids);

    /**
     * 删除微信登录人员表信息
     * 
     * @param id 微信登录人员表主键
     * @return 结果
     */
    public int deleteWxUserById(Long id);

    /**
     * 查询是否有openid
     *
     * @param openid 微信登录openid
     * @return 结果
     */
    public WxUser getOpenId(String openid);


    /**
     * 查询是否有getName
     *
     * @param name 微信登录openid
     * @return 结果
     */
    public WxUser getName(String name);

    /**
     * 查询是否有Phone
     * @param phone
     * @return 结果
     */
    public WxUser getPhone(String phone);

    /**
     * 绑定部门
     *
     * @param wxUser 绑定部门
     * @return 结果
     */
    public int dept(WxUser wxUser) throws Exception;

    /**
     * 确认成为审核人
     *
     * @param wxUser
     * @return 结果
     */
    public int upExamine(WxUser wxUser);

    /**
     * 成为审核人
     *
     * @param wxUser
     * @return 结果
     */
    public int isExamine(WxUser wxUser);

    /**
     * 查询微信登录人员表列表
     *
     * @return 查询微信登录人员表列表
     */
    public List<WxUser> selectWxUserList(WxUser wxUser);
}
