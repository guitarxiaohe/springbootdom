# Swagger API 接口文档使用说明

## 📚 功能概述

系统已集成 Swagger UI 接口文档，可以方便地查看和测试所有 API 接口。特别是新增的**笔记模块**已完整集成 Swagger 注解，支持在线测试。

## 🌐 访问方式

### 1. 启动项目

确保项目已启动：

```bash
# Windows
.\xiaohe.bat

# Linux/Mac
./xiaohe.sh
```

### 2. 访问 Swagger UI

在浏览器中访问以下地址：

```
http://localhost:8080/swagger-ui/index.html
```

或者通过系统菜单访问（需要权限 `tool:swagger:view`）：
```
系统管理 -> 系统工具 -> 系统接口
```

## 📑 API 分组

Swagger 文档已按模块分组，方便查找和测试：

### 1. 全部接口
显示系统所有的 API 接口

### 2. 笔记模块 ⭐
专门展示笔记管理相关的接口，包括：
- **笔记分类管理** - 标签：`笔记分类管理`
  - GET `/note/category/list` - 查询分类列表
  - GET `/note/category/{categoryId}` - 获取分类详情
  - POST `/note/category` - 新增分类
  - PUT `/note/category` - 修改分类
  - DELETE `/note/category/{categoryIds}` - 删除分类
  - POST `/note/category/export` - 导出分类

- **笔记管理** - 标签：`笔记管理`
  - GET `/note/manage/list` - 查询笔记列表
  - GET `/note/manage/treeList` - 查询笔记树形列表
  - GET `/note/manage/{noteId}` - 获取笔记详情
  - POST `/note/manage` - 新增笔记
  - PUT `/note/manage` - 修改笔记
  - DELETE `/note/manage/{noteIds}` - 删除笔记
  - POST `/note/manage/export` - 导出笔记

### 3. 系统模块
系统管理相关接口

## 🔐 认证配置

### 1. 获取 Token

首先需要登录系统获取 Token：

1. 展开 `/login` 接口
2. 点击 "Try it out"
3. 输入用户名和密码：
```json
{
  "username": "admin",
  "password": "admin123"
}
```
4. 点击 "Execute"
5. 复制响应中的 `token` 值

### 2. 配置 Authorization

1. 点击页面右上角的 **Authorize** 按钮（或锁图标）
2. 在弹出的对话框中输入 Token：
```
Bearer <your_token>
```
例如：
```
Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tl...
```
3. 点击 "Authorize" 确认
4. 关闭对话框

现在你可以测试需要认证的接口了！

## 🎯 使用示例

### 示例1：查询笔记分类列表

1. 选择 **笔记模块** 分组
2. 找到 `笔记分类管理` 标签
3. 展开 `GET /note/category/list`
4. 点击 "Try it out"
5. （可选）填写查询参数：
   - `categoryName`: 分类名称（如：React）
   - `status`: 状态（0正常 1停用）
   - `pageNum`: 页码（默认1）
   - `pageSize`: 每页条数（默认10）
6. 点击 "Execute"
7. 查看响应结果

### 示例2：新增笔记分类

1. 找到 `POST /note/category`
2. 点击 "Try it out"
3. 编辑请求体：
```json
{
  "categoryName": "Spring Boot",
  "categoryCode": "springboot",
  "categoryIcon": "🍃",
  "categoryDesc": "Spring Boot框架学习笔记",
  "orderNum": 4,
  "status": "0"
}
```
4. 点击 "Execute"
5. 查看响应：
```json
{
  "msg": "操作成功",
  "code": 200
}
```

### 示例3：创建笔记

1. 找到 `POST /note/manage`
2. 点击 "Try it out"
3. 编辑请求体：
```json
{
  "parentId": 0,
  "categoryId": 1,
  "noteTitle": "React性能优化",
  "noteContent": "# React性能优化\n\n## useMemo\n\n使用 useMemo 可以避免不必要的计算...",
  "noteType": "2",
  "noteTags": "React,性能优化,useMemo",
  "orderNum": 1,
  "status": "0",
  "isPublic": "1"
}
```
4. 点击 "Execute"
5. 查看响应

### 示例4：查询笔记树形列表

1. 找到 `GET /note/manage/treeList`
2. 点击 "Try it out"
3. 输入查询参数：
   - `categoryId`: 1（查询React分类下的笔记）
4. 点击 "Execute"
5. 查看树形结构的响应数据

## 📊 数据模型说明

### SysNoteCategory（笔记分类）

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| categoryId | Long | 否 | 分类ID（新增时不填） | 1 |
| categoryName | String | 是 | 分类名称 | "React" |
| categoryCode | String | 是 | 分类编码（唯一） | "react" |
| categoryIcon | String | 否 | 分类图标 | "⚛️" |
| categoryDesc | String | 否 | 分类描述 | "React框架相关笔记" |
| orderNum | Integer | 否 | 显示顺序 | 1 |
| status | String | 否 | 状态 | "0"正常 "1"停用 |

### SysNote（笔记）

| 字段 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| noteId | Long | 否 | 笔记ID | 1 |
| parentId | Long | 否 | 父笔记ID | 0（根节点） |
| categoryId | Long | 是 | 分类ID | 1 |
| noteTitle | String | 是 | 笔记标题 | "React简介" |
| noteContent | String | 否 | 笔记内容（Markdown） | "# React简介..." |
| noteType | String | 否 | 笔记类型 | "1"目录 "2"笔记 |
| noteTags | String | 否 | 笔记标签（逗号分隔） | "React,入门,基础" |
| orderNum | Integer | 否 | 显示顺序 | 1 |
| status | String | 否 | 状态 | "0"正常 "1"停用 |
| isPublic | String | 否 | 是否公开 | "0"私有 "1"公开 |
| readCount | Long | 否 | 阅读次数 | 0 |

## 🎨 Swagger 注解说明

### Controller 层注解

```java
@Api(tags = "笔记管理")  // 标签分组
@RestController
@RequestMapping("/note/manage")
public class SysNoteController {

    @ApiOperation("查询笔记列表")  // 接口描述
    @GetMapping("/list")
    public TableDataInfo list(SysNote sysNote) {
        // ...
    }

    @ApiOperation("获取笔记详细信息")
    @GetMapping("/{noteId}")
    public AjaxResult getInfo(
        @ApiParam("笔记ID") @PathVariable Long noteId  // 参数说明
    ) {
        // ...
    }
}
```

### Entity 层注解

```java
@ApiModel("笔记")  // 模型描述
public class SysNote extends BaseEntity {

    @ApiModelProperty("笔记ID")  // 字段说明
    private Long noteId;

    @ApiModelProperty(value = "笔记标题", required = true)  // 必填标记
    private String noteTitle;

    @ApiModelProperty(value = "笔记类型", example = "2",
                     allowableValues = "1,2",
                     notes = "1=目录 2=笔记")  // 详细说明
    private String noteType;
}
```

## 💡 使用技巧

### 1. 快速测试接口

- 使用 Swagger UI 可以不用 Postman 等工具直接测试接口
- 响应数据会实时显示，方便调试
- 支持下载响应数据

### 2. 查看请求示例

- 每个接口都有 "Example Value" 和 "Model" 两个选项卡
- "Example Value" 显示请求体示例
- "Model" 显示数据结构说明

### 3. 导出 API 文档

可以通过以下方式导出：
```
http://localhost:8080/v3/api-docs
```

### 4. 切换分组

- 通过页面顶部的下拉框切换不同的API分组
- 建议先选择 "笔记模块" 分组查看笔记相关接口

### 5. 搜索接口

- 使用浏览器的搜索功能（Ctrl+F / Cmd+F）
- 可以快速定位到需要的接口

## ⚙️ 配置说明

### application.yml

```yaml
# Swagger配置
swagger:
  # 是否开启swagger（生产环境建议关闭）
  enabled: true
  # 请求前缀
  pathMapping: /dev-api
```

### 关闭 Swagger

生产环境建议关闭 Swagger：

```yaml
swagger:
  enabled: false
```

## 🔧 故障排查

### 1. 无法访问 Swagger UI

**问题**：访问 `http://localhost:8080/swagger-ui/index.html` 返回 404

**解决方案**：
- 检查 `swagger.enabled` 配置是否为 `true`
- 确认项目已正确启动
- 清除浏览器缓存后重试
- 检查是否有权限访问（`tool:swagger:view`）

### 2. 接口无法调用（401错误）

**问题**：测试接口时返回 401 Unauthorized

**解决方案**：
- 确认已配置 Authorization Token
- Token 格式必须是：`Bearer <token>`
- 确认 Token 未过期（默认30分钟）
- 重新登录获取新 Token

### 3. 找不到笔记模块接口

**问题**：Swagger UI 中看不到笔记相关接口

**解决方案**：
- 切换到 "笔记模块" 分组
- 确认 Controller 已添加 `@ApiOperation` 注解
- 重启项目后再查看

### 4. 请求参数格式错误

**问题**：提交请求时提示参数格式错误

**解决方案**：
- 查看 "Model" 选项卡了解正确的数据结构
- 注意必填字段不能为空
- JSON 格式要正确（注意逗号、引号）
- 枚举值要在允许的范围内

## 📞 更多帮助

- Swagger 官方文档：https://swagger.io/docs/
- Springfox 文档：https://springfox.github.io/springfox/docs/current/
- xiaohe 框架文档：请配置为团队实际文档地址。

---

**更新时间**: 2025-01-07
**版本**: v1.0.0

