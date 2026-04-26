# AGENTS.md — xiaohe 后端仓库

本文供 **AI 编码助手** 与协作者快速理解本仓库边界、惯例与常用入口，避免重复踩坑。

## 项目是什么

- **基座**：**Spring Boot 多模块** 后台，业务包统一为 `com.xiaohe`。
- **业务**：README 定位为基于 xiaohe 的博客 / 管理扩展；含 **CMS**、系统管理、定时任务、代码生成等。
- **JDK**：父 POM `java.version` 为 **1.8**；编码 **UTF-8**。

## 模块与职责

| 模块 | 说明 |
|------|------|
| `xiaohe-admin` | Web 入口（`XiaoHeApplication`）、Controller 主要放 `com.xiaohe.web.controller.*`，资源里 `application.yml`、`mapper/**/*.xml`（部分业务） |
| `xiaohe-framework` | 安全、数据源、全局异常、MyBatis 等框架装配（含 `@MapperScan("com.xiaohe.**.mapper")`） |
| `xiaohe-system` | 系统域：用户/菜单/角色/字典、`FieldConfig`、`EntityConfig` 与通用动态表查询等 **domain + mapper + service** |
| `xiaohe-common` | 通用工具、`AjaxResult`、`BaseController`、分页 `TableSupport` / `PageHelper` 等 |
| `xiaohe-cms` | 博客/CMS 相关 controller、mapper |
| `xiaohe-quartz` | 定时任务 |
| `xiaohe-generator` | 代码生成 |

依赖方向典型为：`admin` → `framework` + `system` + `cms` + …；**不要在 `common` 里引用业务模块**。

## 包与文件惯例

- **Controller**：`xiaohe-admin/src/main/java/com/xiaohe/web/controller/**`（系统相关在 `controller/system`）。
- **领域与持久层**：`xiaohe-system/.../domain`、`mapper`、`service`、`service/impl`；MyBatis XML 多在 `xiaohe-system/src/main/resources/mapper/system/**`。
- **类型别名**：`application.yml` 中 `mybatis.typeAliasesPackage: com.xiaohe.**.domain`。
- **Mapper 扫描**：`com.xiaohe.**.mapper`；XML 通配 `classpath*:mapper/**/*Mapper.xml`。
- **接口风格**：沿用xiaohe习惯 — `AjaxResult`、`TableDataInfo` + `BaseController.getDataTable()`、`@PreAuthorize("@ss.hasPermi('...')")`、`@Log` + `BusinessType`。

新增接口时：**对齐现有 Controller 的返回类型与权限字**，不要随意换一套 JSON 结构。

## 构建与运行

```bash
./mvnw clean package -DskipTests    # 根目录
# 或仅编译部分模块
./mvnw -pl xiaohe-admin -am package -DskipTests
```

- 启动类：`com.xiaohe.XiaoHeApplication`（`xiaohe-admin`）。
- 需本地 **JDK 8+**、**Maven**；数据库配置见 `xiaohe-admin/src/main/resources/application*.yml`。
- 若 CLI 编译报 Lombok 生成方法缺失，检查 IDE/ Maven **注解处理器** 是否启用（`RouterVo`、`FieldConfig` 等依赖 Lombok `@Data`）。

## 数据库与初始化脚本

- 业务/系统表 SQL 多在 `sql/` 下。
- **字段元数据**：`sql/field_config_init.sql` 含 `entity_config`（`entity_key` → `table_name`）、`field_config`（列元数据、是否模糊查询等）。通用「按实体 key 动态查表/删行」依赖这两张表已部署且数据一致。

## 通用动态实体 API（与前端约定）

以下由 `FieldConfigController` 暴露，供动态表单 / 通用 CRUD 使用：

| 能力 | HTTP | 说明 |
|------|------|------|
| 列表 / 字段配置 | `GET /system/fieldConfig/listByEntityKey/{entityKey}` | **无** `pageNum`、`pageSize`：返回该实体的 **field_config 列表**；**同时带**分页参数：按 `entity_config` 解析表名，分页查业务表行（`TableDataInfo`）。查询参数键、`dataParams`（JSON 字符串）中的键须为 `field_config.field_key`。 |
| 批量删除业务行 | `DELETE /system/fieldConfig/delete/{entityKey}/{ids}` | `ids` 为逗号分隔的数值主键；表名来自 `entity_config`；删除条件为 `id IN (...)`。 |

实现入口：`IDynamicEntityDataService` / `DynamicEntityDataServiceImpl`，以及 `EntityConfigMapper`、`DynamicEntityMapper`、`FieldConfigMapper` 中用于白名单的扩展方法。

修改此类行为时：**同步检查 SQL 注入面**（表名、列名仅允许来自配置与白名单，勿把用户输入直接拼进 `${}`）。

## 编码约束（给 Agent）

1. **最小改动**：只改任务相关文件；禁止顺带大重构、删注释或与需求无关的格式化。
2. **风格一致**：新代码与相邻类保持同一套异常、日志、校验、`StringUtils`/`ServletUtils` 用法。
3. **安全**：新接口默认考虑 `@PreAuthorize`；动态 SQL 必须白名单化标识符。
4. **分页**：列表分页优先用现有 `startPage()` + `getDataTable()`；若自定义 `PageHelper.startPage`，须与 `SqlUtil.escapeOrderBySql`、列白名单一致（参考 `listByEntityKey` 分页分支）。
5. **文档**：**不要**主动新增 README/设计文档，除非用户明确要求。

## 相关路径速查

- 系统字段配置 Controller：`xiaohe-admin/.../FieldConfigController.java`
- 字段配置 SQL 初始化：`sql/field_config_init.sql`
- 全局异常：`xiaohe-framework/.../GlobalExceptionHandler.java`

---

若本文件与代码不一致，**以代码与 `application.yml` 为准**，并建议更新本文件以保持 Agent 上下文正确。
