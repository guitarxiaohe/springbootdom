package com.xiaohe.common.core.domain.vo;

import java.io.Serializable;

/**
 * 创建人/修改人简要信息（头像、名称、性别、部门），用于接口返回包装。
 *
 * @author xiaohe
 */
public class OperatorUserVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户账号 */
    private String userName;

    /** 用户昵称（展示名称） */
    private String nickName;

    /** 头像地址 */
    private String avatar;

    /** 性别（0男 1女 2未知，与 sys_user.sex 一致） */
    private String sex;

    /** 部门名称 */
    private String deptName;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }
}
