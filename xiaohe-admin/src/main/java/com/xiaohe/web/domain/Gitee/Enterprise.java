package com.xiaohe.web.domain.Gitee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 企业信息
 */
@Data
public class Enterprise {
    private Long id;
    private String type;
    private String name;
    private String path;

    @JsonProperty("html_url")
    private String htmlUrl;
}