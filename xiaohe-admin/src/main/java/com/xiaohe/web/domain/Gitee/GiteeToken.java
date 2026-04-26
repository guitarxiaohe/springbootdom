package com.xiaohe.web.domain.Gitee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Gitee OAuth Token响应实体
 */
@Data
public class GiteeToken {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("expires_in")
    private Long expiresIn;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    private String scope;
    
    @JsonProperty("created_at")
    private Long createdAt;
}

