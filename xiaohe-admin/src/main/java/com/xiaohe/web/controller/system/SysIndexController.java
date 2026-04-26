package com.xiaohe.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaohe.common.config.XiaoHeConfig;
import com.xiaohe.common.utils.StringUtils;

/**
 * 首页
 *
 * @author xiaohe
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private XiaoHeConfig xiaoheConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("请通过网页系统进去。");
    }
}
