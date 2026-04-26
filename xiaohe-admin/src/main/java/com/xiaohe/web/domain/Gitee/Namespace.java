package com.xiaohe.web.domain.Gitee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 命名空间信息
 */
@Data
class Namespace {
    private Long id;
    private String type;
    private String name;
    private String path;

    @JsonProperty("html_url")
    private String htmlUrl;
}