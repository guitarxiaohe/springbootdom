package com.xiaohe.web.gitee;

import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.web.utils.HttpUtil;
import org.springframework.stereotype.Component;

import static com.xiaohe.web.gitee.ConfigConstant.CONFIG_GITEE_URL;

@Component
public class GiteeUtils {

    /**
     * username: 您的 Gitee 用户名
     * type (可选): all, owner, member（默认: all）
     * sort (可选): created, updated, pushed, full_name（默认: created）
     * direction (可选): asc, desc（默认: desc）
     * page (可选): 页码
     * per_page (可选): 每页数量（默认: 20，最大: 100）
     */
    public AjaxResult getGiteeProject() {
        String configGiteeUrl = CONFIG_GITEE_URL;
        configGiteeUrl += "？type=all";
        configGiteeUrl += "&page=" + 1;
        configGiteeUrl += "&per_page=" + 50;
        String result = HttpUtil.doGet(configGiteeUrl);
        if (result == null || result.isEmpty()) {
            System.out.println("返回值为空！");
        }

//        redisCache.setCacheObject("access_token", token, JSON.parseObject(result, WX.class).getExpiresIn(), TimeUnit.SECONDS);
        return AjaxResult .success(result);
            }
}
