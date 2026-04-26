package com.xiaohe.web.domain.Gitee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目标签
 */
@Data
public class ProjectLabel {
    private Long id;
    private String name;
    private String ident;
    private String color;
    private String description;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}