package com.xiaohe.web.mapper;

import java.util.List;
import com.xiaohe.web.domain.WxUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信登录人员表Mapper接口
 * 
 * @author xiaohe
 * @date 2023-09-21
 */
@Mapper
public interface WxUserMapper 
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
     * @param openId 微信登录人员表openId
     * @return 微信登录人员表
     */
    public WxUser selectWxUserByOpenId(String openId);



    /**
     * 查询微信登录人员表列表
     * 
     * @param wxUser 微信登录人员表
     * @return 微信登录人员表集合
     */
    public List<WxUser> selectWxUserList(WxUser wxUser);

    /**
     * 查询接待人下拉数据
     *
     * @return 查询接待人下拉数据
     */
    public List<WxUser> selectExamineWxUserList();


    /**
     * 多个查询
     *
     * @return 多个查询
     */
    public List<WxUser> allSelectExamineWxUserList(  String deptId);

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
    public int updateWxUser(WxUser wxUser);

    /**
     * 删除微信登录人员表
     * 
     * @param id 微信登录人员表主键
     * @return 结果
     */
    public int deleteWxUserById(Long id);

    /**
     * 批量删除微信登录人员表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxUserByIds(Long[] ids);

    /**
     * 查询是否有openid
     *
     * @param openId 需要删除的数据主键集合
     * @return 结果
     */
    public WxUser getOpenId(String openId);


    /**
     * 查询是否有name
     *
     * @param name 需要删除的数据主键集合
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
     * @param wxUser
     * @return
     */
    public int dept(WxUser wxUser);

    /**
     * 确认成为审核人
     * @param wxUser
     * @return
     */
    public int upExamine(WxUser wxUser);

    /**
     * 成为审核人
     * @param wxUser
     * @return
     */
    public int isExamine(WxUser wxUser);



}
