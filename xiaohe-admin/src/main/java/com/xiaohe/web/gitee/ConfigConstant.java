package com.xiaohe.web.gitee;

public interface ConfigConstant {

    /**
     * username: 您的 Gitee 用户名
     * type (可选): all, owner, member（默认: all）
     * sort (可选): created, updated, pushed, full_name（默认: created）
     * direction (可选): asc, desc（默认: desc）
     * page (可选): 页码
     * per_page (可选): 每页数量（默认: 20，最大: 100）
     */
    String CONFIG_GITEE_URL = "https://gitee.com/api/v5/users/181631043593/repos";


    /**
     * 获取所有仓库信息 url
     *
     */
    String CONFIG_GITEE_CK_URL = "https://gitee.com/api/v5/user/repos";


}
