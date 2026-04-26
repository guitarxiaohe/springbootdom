package com.xiaohe.web.gitee;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Gitee OAuth配置
 */
@Data
@Component
public class GiteeOAuthConfig {
    
    /**
     * 客户端ID
     */
    @Value("${gitee.oauth.client-id:}")
    private String clientId;
    
    /**
     * 客户端密钥
     */
    @Value("${gitee.oauth.client-secret:}")
    private String clientSecret;
    
    /**
     * 回调地址
     */
    @Value("${gitee.oauth.redirect-uri:}")
    private String redirectUri;
    
    /**
     * 授权范围，多个用空格分隔，如：user_info projects pull_requests
     */
    @Value("${gitee.oauth.scope:user_info}")
    private String scope;
    
    /**
     * 授权地址
     */
    public static final String AUTHORIZE_URL = "https://gitee.com/oauth/authorize";
    
    /**
     * 获取Token地址
     */
    public static final String TOKEN_URL = "https://gitee.com/oauth/token";
}

