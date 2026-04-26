package com.xiaohe.web.domain.Gitee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.stream.events.Namespace;
import java.security.Permission;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Gitee 仓库实体类
 */
@Data
public class GiteeRepository {

    /**
     * 仓库ID
     */
    private Long id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 仓库路径
     */
    private String path;

    /**
     * 完整名称（用户名/仓库名）
     */
    @JsonProperty("full_name")
    private String fullName;

    /**
     * 人类可读名称
     */
    @JsonProperty("human_name")
    private String humanName;

    /**
     * 仓库描述
     */
    private String description;

    /**
     * 主页
     */
    private String homepage;

    /**
     * 仓库地址
     */
    @JsonProperty("html_url")
    private String htmlUrl;

    /**
     * SSH地址
     */
    @JsonProperty("ssh_url")
    private String sshUrl;

    /**
     * 默认分支
     */
    @JsonProperty("default_branch")
    private String defaultBranch;

    /**
     * 语言
     */
    private String language;

    /**
     * 开源许可
     */
    private String license;

    /**
     * 是否私有
     */
    private Boolean isPrivate;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 是否是fork仓库
     */
    private Boolean fork;

    /**
     * 是否内部开源
     */
    private Boolean internal;

    /**
     * 仓库类型（内部/外包）
     */
    private Boolean outsourced;

    /**
     * 是否是 GVP 仓库
     */
    private Boolean gvp;

    /**
     * 是否是推荐仓库
     */
    private Boolean recommend;

    /**
     * 是否开启issue功能
     */
    @JsonProperty("has_issues")
    private Boolean hasIssues;

    /**
     * 是否开启了 Pages
     */
    @JsonProperty("has_page")
    private Boolean hasPage;

    /**
     * 是否开启Wiki功能
     */
    @JsonProperty("has_wiki")
    private Boolean hasWiki;

    /**
     * 是否允许用户对仓库进行评论
     */
    @JsonProperty("can_comment")
    private Boolean canComment;

    /**
     * 是否允许用户对"关闭"状态的 Issue 进行评论
     */
    @JsonProperty("issue_comment")
    private Boolean issueComment;

    /**
     * 是否接受 Pull Request，协作开发
     */
    @JsonProperty("pull_requests_enabled")
    private Boolean pullRequestsEnabled;

    /**
     * 仓库star数量
     */
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;

    /**
     * 仓库watch数量
     */
    @JsonProperty("watchers_count")
    private Integer watchersCount;

    /**
     * 仓库fork数量
     */
    @JsonProperty("forks_count")
    private Integer forksCount;

    /**
     * 开启的issue数量
     */
    @JsonProperty("open_issues_count")
    private Integer openIssuesCount;

    /**
     * 代码审查设置，审查人数
     */
    @JsonProperty("assignees_number")
    private Integer assigneesNumber;

    /**
     * 代码审查设置，测试人数
     */
    @JsonProperty("testers_number")
    private Integer testersNumber;

    /**
     * Issue 模版来源
     * project: 使用仓库 Issue Template 作为模版
     * enterprise: 使用企业工作项作为模版
     */
    @JsonProperty("issue_template_source")
    private String issueTemplateSource;

    /**
     * 仓库状态
     */
    private String status;

    /**
     * 当前用户相对于仓库的角色
     */
    private String relation;

    /**
     * 仓库创建者的 username
     */
    @JsonProperty("project_creator")
    private String projectCreator;

    /**
     * 是否 star（此字段已废弃，固定返回 false）
     */
    @JsonProperty("stared")
    private Boolean stared;

    /**
     * 是否 watch
     */
    @JsonProperty("watched")
    private Boolean watched;

    /**
     * PAAS
     */
    private String paas;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime updatedAt;

    /**
     * 最近一次代码推送时间
     */
    @JsonProperty("pushed_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime pushedAt;

    /**
     * 命名空间信息
     */
    private Namespace namespace;

    /**
     * 所有者信息
     */
    private GiteeUser owner;

    /**
     * 分配者信息
     */
    private GiteeUser assigner;

    /**
     * 父仓库信息（如果是fork的仓库）
     */
    private GiteeRepository parent;

    /**
     * 企业信息
     */
    private Enterprise enterprise;

    /**
     * 操作权限
     */
    private Permission permission;

    /**
     * 项目标签
     */
    @JsonProperty("project_labels")
    private List<ProjectLabel> projectLabels;

    /**
     * 成员信息
     */
    private List<GiteeUser> members;

    /**
     * 测试人员信息
     */
    private List<GiteeUser> testers;

    /**
     * 程序信息
     */
    private List<Object> programs;

    /**
     * Root信息
     */
    private Object root;

    // API URLs
    @JsonProperty("blobs_url")
    private String blobsUrl;

    @JsonProperty("branches_url")
    private String branchesUrl;

    @JsonProperty("collaborators_url")
    private String collaboratorsUrl;

    @JsonProperty("comments_url")
    private String commentsUrl;

    @JsonProperty("commits_url")
    private String commitsUrl;

    @JsonProperty("contributors_url")
    private String contributorsUrl;

    @JsonProperty("forks_url")
    private String forksUrl;

    @JsonProperty("hooks_url")
    private String hooksUrl;

    @JsonProperty("issue_comment_url")
    private String issueCommentUrl;

    @JsonProperty("issues_url")
    private String issuesUrl;

    @JsonProperty("keys_url")
    private String keysUrl;

    @JsonProperty("labels_url")
    private String labelsUrl;

    @JsonProperty("milestones_url")
    private String milestonesUrl;

    @JsonProperty("notifications_url")
    private String notificationsUrl;

    @JsonProperty("pulls_url")
    private String pullsUrl;

    @JsonProperty("releases_url")
    private String releasesUrl;

    @JsonProperty("stargazers_url")
    private String stargazersUrl;

    @JsonProperty("tags_url")
    private String tagsUrl;

    private String url;
}