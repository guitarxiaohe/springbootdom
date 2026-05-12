# SKILL.md — xiaohe 后端（Spring Boot 多模块快速开发平台）

本 Skill 指导 AI 编码工具在 **xiaohe 后端** 项目中进行有效的代码编写与维护。请先阅读根目录的 `AGENTS.md` 获取整体框架，再参照此文件进行具体开发。

## 1. 项目核心理念：配置化快速开发

本框架基于 **RuoYi** 改造，扩展了 **动态实体（Dynamic Entity）** 能力：
- 无需为每个业务表写单独的 CRUD Controller/Service
- 通过 `entity_config` + `field_config` 两张元数据表驱动通用 CRUD API
- 新增一个业务模块 = ① 建数据库表 → ② 配 `entity_config` → ③ 配 `field_config` → ④（可选）前台注册 EntityModule

**新增实体/模块时的首选方案：** 尽量走动态通用 API，只在需要特殊业务逻辑时才写 Controller/Service。

## 2. 模块依赖关系（必须遵守）

```
xiaohe-admin → xiaohe-framework
             → xiaohe-system
             → xiaohe-cms
             → xiaohe-quartz
             → xiaohe-generator

xiaohe-system → xiaohe-common
xiaohe-cms   → xiaohe-common
```

**禁止：** xiaohe-common 引用任何业务模块。xiaohe-framework 只引用 xiaohe-common + xiaohe-system（已有）。

## 3. 包结构与代码位置

| 层 | 模块 | 包路径 |
|----|------|--------|
| Controller | xiaohe-admin | `com.xiaohe.web.controller.{system,cms,common,monitor}` |
| Domain | xiaohe-system | `com.xiaohe.system.domain` |
| Mapper | xiaohe-system | `com.xiaohe.system.mapper` |
| Service | xiaohe-system | `com.xiaohe.system.service.{impl}` |
| Mapper XML | xiaohe-system | `src/main/resources/mapper/system/` |
| 通用工具 | xiaohe-common | `com.xiaohe.common.{core,utils,exception,annotation}` |
| CMS | xiaohe-cms | `com.xiaohe.cms.{blog,comment,tag,type,message,charts,fileInfo}` |

**新增功能时的放置原则：**
- 系统管理功能 → xiaohe-system
- 业务功能（博客/内容/文件）→ xiaohe-cms
- 页面入口/配置 → xiaohe-admin
- 通用工具/常量 → xiaohe-common

## 4. 动态实体 API 协议（前后端约定）

### 4.1 核心接口

| 端点 | 方法 | 说明 |
|------|------|------|
| `/system/fieldConfig/listByEntityKey/{entityKey}` | GET | **无分页参数** → 返回 field_config 列表；**有分页参数** → 查业务表数据（分页） |
| `/system/fieldConfig/data/{entityKey}` | POST | 新增业务行 |
| `/system/fieldConfig/data/{entityKey}/{id}` | GET | 查询单条 |
| `/system/fieldConfig/data/{entityKey}/{id}` | PUT | 更新业务行 |
| `/system/fieldConfig/data/{entityKey}/{id}` | DELETE | 删除单条 |
| `/system/fieldConfig/delete/{entityKey}/{ids}` | DELETE | 批量删除（逗号分隔 ids） |
| `/system/fieldConfig/getByEntityKeyAndFieldKey/{entityKey}/{fieldKey}` | GET | 查特定字段配置 |
| `/system/fieldConfig/sort` | PUT | 批量更新字段排序 |

### 4.2 分页查询参数约定

- `pageNum`（当前页）、`pageSize`（每页条数）、`orderByColumn`（排序列）、`isAsc`（排序方向）
- **筛选条件：** 直接作为 query params，key 须为 `field_config.field_key` 值
- **dataParams：** JSON 字符串，用于传递必须精确相等的固定查询条件（如用户只能看自己创建的记录）
- **日期时间值：** 后端返回毫秒时间戳（`Long`），前端自行格式化

### 4.3 新增动态实体步骤

1. **建表：** 业务表必须有一个数值自增主键（默认列名 `id`，可用其他名）
2. **配 entity_config：** `table_name` 必须与物理表名一致，实体 key 可自定义（前端路由用）
3. **配 field_config：**
   - `field_key` = 蛇形列名（`create_by`、`user_name`）
   - `field_type` 可选值：`input`, `text`, `select`, `dict`, `date`, `datetime`, `textarea`, `number`, `switch`, `file`, `by`, `user`
   - `field_role` 可选值：`createUser`（创建人字段）、`updateUser`（更新人字段）、`fileInfo`（文件关联字段）
   - `is_fuzzy_search = 1` 开启模糊查询（仅 text/varchar 列）
   - `select_entity_key` 关联查询另一实体的数据做筛选（如：关联 `entrustUnit` 表）
   - `dict_code` 关联字典值（type=dict 时必填）
4. **数据库 SQL 示例见 `sql/field_config_init.sql`**

### 4.4 字段类型与前端映射

```
input     → el-input
text      → el-input（常规文本）
select    → el-select（静态选项）
dict      → el-select（字典值）
date      → el-date-picker（日期）
datetime  → el-date-picker（日期时间）
textarea  → el-input type="textarea"
number    → el-input-number
switch    → el-switch
file      → file-upload（文件上传 + 自动填充 SysFileInfo）
by        → 创建人/修改人字段
user      → 用户选择器
```

## 5. CRUD 编码规范

### 标准 CRUD（非动态实体）

```java
@PreAuthorize("@ss.hasPermi('system:xxx:list')")
@GetMapping("/list")
public TableDataInfo list(Xxx xxx) {
    startPage();
    List<Xxx> list = xxxService.selectXxxList(xxx);
    return getDataTable(list);
}

@PreAuthorize("@ss.hasPermi('system:xxx:add')")
@Log(title = "xxx", businessType = BusinessType.INSERT)
@PostMapping
public AjaxResult add(@Validated @RequestBody Xxx xxx) {
    return toAjax(xxxService.insertXxx(xxx));
}

@PreAuthorize("@ss.hasPermi('system:xxx:edit')")
@Log(title = "xxx", businessType = BusinessType.UPDATE)
@PutMapping
public AjaxResult edit(@Validated @RequestBody Xxx xxx) {
    return toAjax(xxxService.updateXxx(xxx));
}

@PreAuthorize("@ss.hasPermi('system:xxx:remove')")
@Log(title = "xxx", businessType = BusinessType.DELETE)
@DeleteMapping("/{ids}")
public AjaxResult remove(@PathVariable Long[] ids) {
    return toAjax(xxxService.deleteXxxByIds(ids));
}
```

**规则：**
- 始终使用 `startPage()` + `getDataTable()` 处理分页
- 增删改用 `@Log` 注解
- 权限表达式使用 `@PreAuthorize`，格式统一为 `system:{模块}:{操作}`
- 返回类型统一：列表用 `TableDataInfo`，单条/操作用 `AjaxResult`

### 审计字段填充

```java
// 创建时
xxx.setCreatedBy(getUserId());
xxx.setCreatedTime(System.currentTimeMillis());

// 更新时  
xxx.setUpdatedBy(getUserId());
// 可选的 updatedTime 可在 Mapper XML 中设定
```

### 查询参数处理

- 使用 `@Data`（Lombok）的 domain 类接收查询参数
- 字符串模糊查询默认走 MyBatis XML 中的 `<if test="xxx != null and xxx != ''"> AND xxx LIKE concat('%', #{xxx}, '%')</if>`
- 日期范围查询建议用 `beginXxx` + `endXxx` 参数名（参见 DictData）

## 6. 常用工具类

```java
// 返回成功
AjaxResult.success(data);
AjaxResult.success(msg, data);

// 返回影响行数
toAjax(xxxMapper.update(xxx));  // int → AjaxResult

// 分页
startPage();  // 自动从请求参数中解析 pageNum/pageSize
PageHelper.startPage(pageNum, pageSize);  // 手动指定（需配合 SqlUtil 清洗排序）

// ID 转换
toLong(obj);      // Object → Long
toInt(obj);       // Object → Integer
toStr(obj);       // Object → String

// 字符串
SecurityUtils.getUsername();     // 当前用户名
SecurityUtils.getUserId();       // 当前用户 ID
getUsername();                   // BaseController 快捷方法
getUserId();                     // BaseController 快捷方法
StringUtils.toCamelCase(str);    // 蛇形 → 驼峰
StringUtils.toUnderScoreCase(str); // 驼峰 → 蛇形
```

## 7. 操作人信息填充

```java
@Autowired private IOperatorUserFillService operatorUserFillService;

// 单条
operatorUserFillService.fillAuditUser(fieldConfig);

// 列表
operatorUserFillService.fillAuditUsers(list);

// Map 列表（动态实体使用）
operatorUserFillService.fillAuditUserMaps(rows, "createdBy", "updatedBy");
```

## 8. 安全与 SQL 注入防护

```java
// 白名单校验表名/列名
StringUtils.matches("^[a-zA-Z0-9_]+$", tableName)

// 排序字段转义
SqlUtil.escapeOrderBySql(orderBy)

// 动态实体中：列名必须来自 field_config
dynamicEntityDataService.isAllowedOrderColumn(entityKey, column)
```

**所有动态 SQL 必须：** 表名来自 `entity_config` 且校验白名单，列名来自 `field_config` 或使用 `#{param}`（非 `${}`）

## 9. MyBatis XML 约定

- 放在 `xiaohe-system/src/main/resources/mapper/system/` 下
- 命名：`XxxMapper.xml`（与 Java Mapper 同名）
- namespace：全限定类名
- resultMap：id=基本 result map，扩展加后缀
- SQL 片段：提取公共列到 `<sql id="xxxColumns">` 中复用

## 10. 字段配置 Controller（FieldConfigController）详细说明

位于 `xiaohe-admin/.../system/FieldConfigController.java`，实现了以下能力：

### 10.1 元数据查询（不带分页参数）
```http
GET /system/fieldConfig/listByEntityKey/{entityKey}
# 返回所有 field_config 行，带操作人信息
```

### 10.2 业务数据分页查询（带分页参数）
```http
GET /system/fieldConfig/listByEntityKey/{entityKey}?pageNum=1&pageSize=20&orderByColumn=createdTime&isAsc=desc&fieldKey=xxx
# 动态解析表名 → 安全排序 → 注入查询条件 → 操作人填充 → 文件关联填充
```

### 10.3 业务数据增删改查
```http
POST   /system/fieldConfig/data/{entityKey}           # 新增
GET    /system/fieldConfig/data/{entityKey}/{id}       # 查询单条
PUT    /system/fieldConfig/data/{entityKey}/{id}       # 更新
DELETE /system/fieldConfig/data/{entityKey}/{id}       # 删除单条
DELETE /system/fieldConfig/delete/{entityKey}/{id1,id2} # 批量删除
```

### 10.4 字段配置 CRUD
```http
GET    /system/fieldConfig/list                        # 字段配置分页列表
POST   /system/fieldConfig                             # 新增字段配置
PUT    /system/fieldConfig                             # 修改字段配置
DELETE /system/fieldConfig/{ids}                       # 删除字段配置
PUT    /system/fieldConfig/sort                        # 更新字段排序（批量）
GET    /system/fieldConfig/{id}                        # 字段配置详情
```

## 11. 文件处理（SysFileInfo）

```java
@Autowired private ISysFileInfoService sysFileInfoService;

// 根据 URL 列表批量查询文件信息
sysFileInfoService.selectSysFileInfoByFileUrls(urls);

// 文件 URL 前缀配置
XiaoHeConfig.getFileUrl();
```

**动态实体文件字段：** 返回的`Map<String, Object>`行中，如果`field_type=file`或`field_role=fileInfo`的字段有值，Controller 会自动查询 SysFileInfo 并附加为 `{fieldKey}Info` 字段。

## 12. 多语言/国际化

- 后端不处理国际化，纯返回数据
- 字典值的前端显示由 `dict_code` + `dict_label` 字段在前端字典管理模块中完成

## 13. 通用 Mapper 方法

```java
// DynamicEntityMapper — 动态 SQL（MyBatis XML 中使用 ${tableName}）
List<LinkedHashMap<String, Object>> selectEntityRowList(tableName, filters)
Map<String, Object> selectEntityRowById(tableName, pkColumn, id)
int insertEntityRow(tableName, values)       // List<DynamicEntityColumnValue>
int updateEntityRowById(tableName, pkColumn, id, values)
int deleteEntityRowsByIds(tableName, pkColumn, ids)
String selectPrimaryKeyColumn(tableName)
Set<String> selectColumnNames(tableName)
Long selectLastInsertId()

// FieldConfigMapper
List<FieldConfig> selectFieldConfigList(fieldConfig)
List<FieldConfig> selectFieldConfigByEntityKey(entityKey)
FieldConfig selectFieldConfigByEntityKeyAndFieldKey(entityKey, fieldKey)
int countFieldConfigByEntityKeyAndFieldKey(entityKey, fieldUnderScore)
List<Map<String, Object>> selectFieldFilterMetaByEntityKey(entityKey)
List<String> selectFieldKeysByEntityKey(entityKey)
int insertFieldConfig(fieldConfig)
int updateFieldConfig(fieldConfig)
int deleteFieldConfigByIds(ids)
int updateSortBatch(entityKey, items) // 批量更新排序

// EntityConfigMapper
EntityConfig selectEntityConfigByEntityKey(entityKey)
```

## 14. 新增标准模块速查

```
1. 建表（POJO 标准字段：id, create_by, create_time, update_by, update_time）
2. 建 Domain（extends BaseEntity + implements Serializable）
3. 建 Mapper（interface + XML）
4. 建 Service（interface + impl）
5. 建 Controller（extends BaseController）
6. 注册权限（数据库 sys_menu / 前端路由配置）
7. （可选）配 entity_config + field_config 使通用 CRUD API 可用
```

## 15. 常见问题

- **分页不生效：** 检查 `startPage()` 是否在调用 Mapper 之前执行
- **动态实体 500：** 检查 `entity_config` 中 `table_name` 是否与物理表名一致
- **字段配置不显示：** 检查 `field_config.is_visible = 1`
- **操作人信息不显示：** 检查 `field_role` 是否设为 `createUser`/`updateUser`
- **日期显示为数字：** 后端将 Date/LocalDateTime 统一转为毫秒时间戳，前端需转义
- **Lombok 编译报错：** 检查 Maven 注解处理器是否启用
- **动态 SQL 注入：** 所有 `${}` 拼接的标识符必须白名单校验通过
