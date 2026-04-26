// GiteeController.java
package com.xiaohe.web.controller.common;

import com.xiaohe.common.core.domain.AjaxResult;
import com.xiaohe.web.service.GiteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gitee")
@RequiredArgsConstructor
public class GiteeController {

    private final GiteeService giteeService;


    /**
     * 获取用户仓库列表
     */
    @GetMapping("/users/ck/repos")
    public AjaxResult getUserRepos() {
        return AjaxResult.success(giteeService.getUserRepos());
    }

//    /**
//     * 获取仓库统计信息
//     */
//    @GetMapping("/users/{username}/stats")
//    public ResponseEntity<Map<String, Object>> getRepoStats(@PathVariable String username) {
//        return ResponseEntity.ok(giteeService.getRepoStats(username).block());
//    }
}