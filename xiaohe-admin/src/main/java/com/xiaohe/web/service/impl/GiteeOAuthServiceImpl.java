package com.xiaohe.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaohe.common.core.redis.RedisCache;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.web.domain.Gitee.GiteeToken;
import com.xiaohe.web.gitee.GiteeOAuthConfig;
import com.xiaohe.web.service.GiteeOAuthService;
import com.xiaohe.web.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Gitee OAuth服务实现
 */
@Slf4j
@Service
public class GiteeOAuthServiceImpl implements GiteeOAuthService {
    
    private static final String GITEE_TOKEN_KEY = "gitee:oauth:token";
    private static final String GITEE_ACCESS_TOKEN_KEY = "gitee:oauth:access_token";
    private static final String GITEE_REFRESH_TOKEN_KEY = "gitee:oauth:refresh_token";
    
    @Autowired
    private GiteeOAuthConfig oauthConfig;
    
    @Autowired
    private RedisCache redisCache;
    
    @Override
    public String getAuthorizeUrl(String state) {
        try {
            StringBuilder url = new StringBuilder(GiteeOAuthConfig.AUTHORIZE_URL);
            url.append("?client_id=").append(URLEncoder.encode(oauthConfig.getClientId(), StandardCharsets.UTF_8.name()));
            url.append("&redirect_uri=").append(URLEncoder.encode(oauthConfig.getRedirectUri(), StandardCharsets.UTF_8.name()));
            url.append("&response_type=code");
            
            // 如果有scope配置，添加scope参数
            if (StringUtils.isNotEmpty(oauthConfig.getScope())) {
                url.append("&scope=").append(URLEncoder.encode(oauthConfig.getScope(), StandardCharsets.UTF_8.name()));
            }
            
            // 如果有state参数，添加state
            if (StringUtils.isNotEmpty(state)) {
                url.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8.name()));
            }
            
            return url.toString();
        } catch (Exception e) {
            log.error("生成授权URL失败", e);
            throw new RuntimeException("生成授权URL失败", e);
        }
    }
    
    @Override
    public GiteeToken getTokenByCode(String code) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("code", code);
            params.put("client_id", oauthConfig.getClientId());
            params.put("redirect_uri", oauthConfig.getRedirectUri());
            params.put("client_secret", oauthConfig.getClientSecret());
            
            // 使用POST请求，将参数放在Body中
            // 注意：需要设置User-Agent，避免403错误
            String response = HttpUtil.doPostForm(GiteeOAuthConfig.TOKEN_URL, params);
            
            if (StringUtils.isEmpty(response)) {
                log.error("获取Token失败：响应为空");
                throw new RuntimeException("获取Token失败：响应为空");
            }
            
            log.info("Gitee Token响应: {}", response);
            
            // 解析响应
            JSONObject jsonObject = JSON.parseObject(response);
            
            // 检查是否有错误
            if (jsonObject.containsKey("error")) {
                String error = jsonObject.getString("error");
                String errorDescription = jsonObject.getString("error_description");
                log.error("获取Token失败：{} - {}", error, errorDescription);
                throw new RuntimeException("获取Token失败：" + error + " - " + errorDescription);
            }
            
            GiteeToken token = JSON.parseObject(response, GiteeToken.class);
            
            // 保存Token
            saveToken(token);
            
            return token;
        } catch (Exception e) {
            log.error("通过授权码获取Token失败", e);
            throw new RuntimeException("通过授权码获取Token失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public GiteeToken refreshToken(String refreshToken) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "refresh_token");
            params.put("refresh_token", refreshToken);
            
            String response = HttpUtil.doPostForm(GiteeOAuthConfig.TOKEN_URL, params);
            
            if (StringUtils.isEmpty(response)) {
                log.error("刷新Token失败：响应为空");
                throw new RuntimeException("刷新Token失败：响应为空");
            }
            
            log.info("Gitee刷新Token响应: {}", response);
            
            // 解析响应
            JSONObject jsonObject = JSON.parseObject(response);
            
            // 检查是否有错误
            if (jsonObject.containsKey("error")) {
                String error = jsonObject.getString("error");
                String errorDescription = jsonObject.getString("error_description");
                log.error("刷新Token失败：{} - {}", error, errorDescription);
                throw new RuntimeException("刷新Token失败：" + error + " - " + errorDescription);
            }
            
            GiteeToken token = JSON.parseObject(response, GiteeToken.class);
            
            // 保存Token
            saveToken(token);
            
            return token;
        } catch (Exception e) {
            log.error("刷新Token失败", e);
            throw new RuntimeException("刷新Token失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public String getAccessToken() {
        // 先从Redis获取access_token
        String accessToken = redisCache.getCacheObject(GITEE_ACCESS_TOKEN_KEY);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        
        // 如果access_token不存在，尝试从完整Token对象中获取
        GiteeToken token = redisCache.getCacheObject(GITEE_TOKEN_KEY);
        if (token != null && StringUtils.isNotEmpty(token.getAccessToken())) {
            return token.getAccessToken();
        }
        
        return null;
    }
    
    @Override
    public String getRefreshToken() {
        // 从Redis获取refresh_token
        String refreshToken = redisCache.getCacheObject(GITEE_REFRESH_TOKEN_KEY);
        if (StringUtils.isNotEmpty(refreshToken)) {
            return refreshToken;
        }
        
        // 如果refresh_token不存在，尝试从完整Token对象中获取
        GiteeToken token = redisCache.getCacheObject(GITEE_TOKEN_KEY);
        if (token != null && StringUtils.isNotEmpty(token.getRefreshToken())) {
            return token.getRefreshToken();
        }
        
        return null;
    }
    
    @Override
    public void saveToken(GiteeToken token) {
        if (token == null) {
            return;
        }
        
        // 保存完整的Token对象
        // expires_in通常是秒数，转换为毫秒
        long expireTime = token.getExpiresIn() != null ? token.getExpiresIn() : 86400L; // 默认24小时
        redisCache.setCacheObject(GITEE_TOKEN_KEY, token, (int)expireTime, TimeUnit.SECONDS);
        
        // 单独保存access_token，方便快速获取
        if (StringUtils.isNotEmpty(token.getAccessToken())) {
            redisCache.setCacheObject(GITEE_ACCESS_TOKEN_KEY, token.getAccessToken(), (int)expireTime, TimeUnit.SECONDS);
        }
        
        // 保存refresh_token，用于刷新access_token
        if (StringUtils.isNotEmpty(token.getRefreshToken())) {
            // refresh_token通常有效期更长，这里设置为30天
            redisCache.setCacheObject(GITEE_REFRESH_TOKEN_KEY, token.getRefreshToken(), 30, TimeUnit.DAYS);
        }
        
        log.info("Token已保存到Redis，access_token有效期：{}秒", expireTime);
    }
}

