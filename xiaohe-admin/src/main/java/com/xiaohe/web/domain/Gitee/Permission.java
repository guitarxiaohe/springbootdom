package com.xiaohe.web.domain.Gitee;

import lombok.Data;

/**
 * 操作权限
 */
@Data
public class Permission {
    private Boolean pull;
    private Boolean push;
    private Boolean admin;
}