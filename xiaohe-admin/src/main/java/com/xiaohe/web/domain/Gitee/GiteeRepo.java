// GiteeRepo.java
package com.xiaohe.web.domain.Gitee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GiteeRepo {
    private Long id;

    String access_token;
    String visibility;
    String affiliation;
    String type;
    String sort;
    String direction;
    String q;
    Integer per_page;
    Integer page;

    @JsonProperty("full_name")
    private String fullName;

    private String name;
    private String description;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("ssh_url")
    private String sshUrl;

    @JsonProperty("clone_url")
    private String cloneUrl;

    @JsonProperty("default_branch")
    private String defaultBranch;

    private String language;

    @JsonProperty("stargazers_count")
    private Integer stargazersCount;

    @JsonProperty("watchers_count")
    private Integer watchersCount;

    @JsonProperty("forks_count")
    private Integer forksCount;

    @JsonProperty("open_issues_count")
    private Integer openIssuesCount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("pushed_at")
    private LocalDateTime pushedAt;

    private Boolean isPrivate;
    private Boolean fork;
    private GiteeRepo parent;
}