package com.xiaohe.web.core.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.xiaohe.common.config.XiaoHeConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger2的接口配置
 * 
 * @author xiaohe
 */
@Configuration
public class SwaggerConfig
{
    /** 系统基础配置 */
    @Autowired
    private XiaoHeConfig xiaoheConfig;

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    /** 设置请求的统一前缀 */
    @Value("${swagger.pathMapping}")
    private String pathMapping;

    /**
     * 创建API - 全部接口
     */
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.OAS_30)
                .groupName("全部接口")
                // 是否启用Swagger
                .enable(enabled)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
                // .apis(RequestHandlerSelectors.basePackage("com.xiaohe.project.tool.swagger"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping(pathMapping);
    }

    /**
     * 创建API - 笔记模块
     */
    @Bean
    public Docket noteApi()
    {
        return new Docket(DocumentationType.OAS_30)
                .groupName("笔记模块")
                .enable(enabled)
                .apiInfo(noteApiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/note/**"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping(pathMapping);
    }

    /**
     * 创建API - 系统模块
     */
    @Bean
    public Docket systemApi()
    {
        return new Docket(DocumentationType.OAS_30)
                .groupName("系统模块")
                .enable(enabled)
                .apiInfo(systemApiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/system/**"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping(pathMapping);
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes()
    {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts()
    {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                        .build());
        return securityContexts;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /**
     * 添加摘要信息 - 全部
     */
    private ApiInfo apiInfo()
    {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题：澜庭浩然建筑有限公司接口文档")
                // 描述
                .description("描述：系统全部接口文档")
                // 作者信息
                .contact(new Contact(xiaoheConfig.getName(), null, null))
                // 版本
                .version("版本号:" + xiaoheConfig.getVersion())
                .build();
    }

    /**
     * 添加摘要信息 - 笔记模块
     */
    private ApiInfo noteApiInfo()
    {
        return new ApiInfoBuilder()
                .title("笔记管理模块API")
                .description("笔记管理、笔记分类相关接口\n\n" +
                        "## 功能特性\n" +
                        "- 笔记分类管理（React、Vue、UniApp等）\n" +
                        "- 树形目录结构（支持无限层级）\n" +
                        "- Markdown格式支持\n" +
                        "- 标签管理\n" +
                        "- 阅读统计\n" +
                        "- 公开/私有权限控制")
                .contact(new Contact(xiaoheConfig.getName(), null, null))
                .version("v1.0.0")
                .build();
    }

    /**
     * 添加摘要信息 - 系统模块
     */
    private ApiInfo systemApiInfo()
    {
        return new ApiInfoBuilder()
                .title("系统管理模块API")
                .description("系统管理相关接口")
                .contact(new Contact(xiaoheConfig.getName(), null, null))
                .version("版本号:" + xiaoheConfig.getVersion())
                .build();
    }
}
