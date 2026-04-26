package com.xiaohe.web.service;

import com.xiaohe.web.domain.Gitee.GiteeToken;

/**
 * Gitee OAuth服务接口
 */
public interface GiteeOAuthService {
    
    /**
     * 生成授权URL
     * @param state 状态参数，用于防止CSRF攻击
     * @return 授权URL
     */
    String getAuthorizeUrl(String state);
    
    /**
     * 通过授权码获取access_token
     * @param code 授权码
     * @return Token信息
     */
    GiteeToken getTokenByCode(String code);
    
    /**
     * 通过refresh_token刷新access_token
     * @param refreshToken refresh_token
     * @return Token信息
     */
    GiteeToken refreshToken(String refreshToken);
    
    /**
     * 获取存储的access_token
     * @return access_token
     */
    String getAccessToken();
    
    /**
     * 获取存储的refresh_token
     * @return refresh_token
     */
    String getRefreshToken();
    
    /**
     * 保存Token到Redis
     * @param token Token信息
     */
    void saveToken(GiteeToken token);
}

