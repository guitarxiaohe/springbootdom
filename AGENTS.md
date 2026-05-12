# AGENTS.md — xiaohe 后端仓库

本文供 **AI 编码助手** 快速理解项目结构与约定。具体的编码规范、API 协议、工具类用法见 `.skills/SKILL.md`；通用编码纪律见 `.skills/code-expert/SKILL.md`。

## 项目是什么

- **基座：** Spring Boot 多模块，包统一 `com.xiaohe`
- **定位：** 配置化快速开发后台（扩展自 RuoYi），支持通过 `entity_config` + `field_config` 动态生成 CRUD API
- **JDK：** Java 8（`java.version=1.8`），编码 UTF-8

## 模块速览

| 模块 | 职责 |
|------|------|
| **xiaohe-admin** | Web 入口（`XiaoHeApplication`），Controller 层，配置与应用 yml，部分 Mapper XML |
| **xiaohe-framework** | 安全、数据源、全局异常、MyBatis 装配（含 `@MapperScan("com.xiaohe.**.mapper")`） |
| **xiaohe-system** | 核心业务域：用户/菜单/角色/字典、FieldConfig、EntityConfig、动态实体查询、通用 Mapper/Service |
| **xiaohe-cms** | 内容管理：博客、评论、标签、分类、消息、图表、文件 |
| **xiaohe-common** | 通用工具：`AjaxResult`、`BaseController`、分页、`PageHelper`、`StringUtils` |
| **xiaohe-quartz** | 定时任务管理 |
| **xiaohe-generator** | 代码生成器 |

**依赖方向：** `admin → framework + system + cms + quartz + generator`，`common` 不得引用业务模块。

## 关键文件位置

| 内容 | 路径 |
|------|------|
| 系统 Controller | `xiaohe-admin/src/main/java/com/xiaohe/web/controller/system/` |
| 字段配置 Controller | `xiaohe-admin/.../controller/system/FieldConfigController.java` |
| 动态实体 Service | `xiaohe-system/.../service/impl/DynamicEntityDataServiceImpl.java` |
| Mapper XML | `xiaohe-system/src/main/resources/mapper/system/` |
| 数据库初始化 SQL | `sql/field_config_init.sql` |
| 字段元数据表 | `entity_config`（entity_key → table_name）、`field_config`（列配置） |
| 应用配置 | `xiaohe-admin/src/main/resources/application.yml` |
| 全局异常处理 | `xiaohe-framework/.../GlobalExceptionHandler.java` |

## 编码约束

1. **最小改动**：只改任务相关文件，不要顺带重构或格式化无关代码
2. **风格一致**：新代码与相邻类保持同一套异常/日志/校验/工具用法
3. **安全**：新接口默认加 `@PreAuthorize`；动态 SQL 必须白名单化标识符（表名/列名）
4. **分页**：列表分页优先用 `startPage()` + `getDataTable()`
5. **新需求优先方案**：能用动态实体 API 解决的，不要写标准 CRUD

## 与 AI 协作

- 本文件 + `.skills/SKILL.md` + `.skills/code-expert/SKILL.md` 是 AI 的完整上下文，每次新任务请先阅读
- 新增功能前，先判断是否可以用动态实体（entity_config + field_config）解决
- 后端新增接口时，同步通知前端同学更新对应的 API 模块
- 统一返回结构，不要随意换一套 JSON 结构

> 若本文件与代码不一致，**以代码与 `application.yml` 为准**，并建议更新本文件。
