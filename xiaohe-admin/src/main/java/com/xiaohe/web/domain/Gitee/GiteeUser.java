package com.xiaohe.web.domain.Gitee;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Gitee用户信息
 */
@Data
public class GiteeUser {
    private Long id;
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
    private String htmlUrl;
    private String type;
    private String siteAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 其他属性...
}