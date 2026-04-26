package com.xiaohe.web.controller.system;

import java.io.IOException;

import com.xiaohe.common.constant.Constants;
import com.xiaohe.common.utils.file.FileUtils;
import com.xiaohe.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.xiaohe.common.annotation.Log;
import com.xiaohe.common.config.XiaoHeConfig;
import com.xiaohe.common.constant.UserConstants;
import com.xiaohe.common.core.controller.BaseController;
import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.common.core.domain.entity.SysUser;
import com.xiaohe.common.core.domain.model.LoginUser;
import com.xiaohe.common.enums.BusinessType;
import com.xiaohe.common.utils.MessageUtils;
import com.xiaohe.common.utils.SecurityUtils;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.common.utils.file.FileUploadUtils;
import com.xiaohe.framework.web.service.TokenService;
import com.xiaohe.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 *
 * @author xiaohe
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        user.setUserName(sysUser.getUserName());
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error(MessageUtils.message("user.update.fail", user.getUserName(), MessageUtils.message("user.phone.exist")));
        }
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error(MessageUtils.message("user.update.fail", user.getUserName(), MessageUtils.message("user.email.exist")));
        }
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        if (userService.updateUserProfile(user) > 0)
        {
            // 更新缓存用户信息
            sysUser.setNickName(user.getNickName());
            sysUser.setPhonenumber(user.getPhonenumber());
            sysUser.setEmail(user.getEmail());
            sysUser.setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error(MessageUtils.message("user.profile.update.error"));
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return AjaxResult.error(MessageUtils.message("user.old.password.error"));
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return AjaxResult.error(MessageUtils.message("user.new.password.same"));
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error(MessageUtils.message("user.password.update.error"));
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {    // 上传文件路径
            // 上传并返回新文件名称




            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload( XiaoHeConfig.getUploadPath(), file);
            /*2021-12-29 新增删除原头像*/
            String filePath = XiaoHeConfig.getProfile() + StringUtils.substringAfter(loginUser.getUser().getAvatar(), Constants.RESOURCE_PREFIX);
            FileUtils.deleteFile(filePath);
            /*结束*/
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error(MessageUtils.message("user.avatar.upload.error"));
    }
}
