-- ----------------------------
-- 笔记管理系统数据库脚本
-- ----------------------------

-- ----------------------------
-- 1. 笔记分类表
-- ----------------------------
DROP TABLE IF EXISTS `sys_note_category`;
CREATE TABLE `sys_note_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) NOT NULL COMMENT '分类编码',
  `category_icon` varchar(100) DEFAULT NULL COMMENT '分类图标',
  `category_desc` varchar(500) DEFAULT NULL COMMENT '分类描述',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uk_category_code` (`category_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='笔记分类表';

-- ----------------------------
-- 初始化笔记分类数据
-- ----------------------------
INSERT INTO `sys_note_category` VALUES (1, 'React', 'react', '⚛️', 'React框架相关笔记', 1, '0', '0', 'admin', NOW(), NULL, NULL, 'React前端框架学习笔记');
INSERT INTO `sys_note_category` VALUES (2, 'Vue', 'vue', '🖖', 'Vue框架相关笔记', 2, '0', '0', 'admin', NOW(), NULL, NULL, 'Vue前端框架学习笔记');
INSERT INTO `sys_note_category` VALUES (3, 'UniApp', 'uniapp', '🦄', 'UniApp跨平台开发笔记', 3, '0', '0', 'admin', NOW(), NULL, NULL, 'UniApp跨平台应用开发笔记');

-- ----------------------------
-- 2. 笔记表
-- ----------------------------
DROP TABLE IF EXISTS `sys_note`;
CREATE TABLE `sys_note` (
  `note_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父笔记ID',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `note_title` varchar(200) NOT NULL COMMENT '笔记标题',
  `note_content` longtext COMMENT '笔记内容',
  `note_type` char(1) DEFAULT '2' COMMENT '笔记类型（1目录 2笔记）',
  `note_tags` varchar(500) DEFAULT NULL COMMENT '笔记标签（多个用逗号分隔）',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `is_public` char(1) DEFAULT '0' COMMENT '是否公开（0私有 1公开）',
  `read_count` bigint(20) DEFAULT '0' COMMENT '阅读次数',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`note_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_note_type` (`note_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='笔记表';

-- ----------------------------
-- 初始化笔记示例数据 - React分类
-- ----------------------------
INSERT INTO `sys_note` VALUES (1, 0, 1, 'React基础', NULL, '1', NULL, 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, 'React基础知识目录');
INSERT INTO `sys_note` VALUES (2, 1, 1, 'React简介', '# React简介\n\nReact 是一个用于构建用户界面的 JavaScript 库。\n\n## 核心特性\n\n1. **声明式**：以声明式编写UI，更加可预测，更容易调试\n2. **组件化**：构建管理自身状态的封装组件，然后组合它们构成复杂的UI\n3. **一次学习，随处编写**：既可以用于Web开发，也可以用于移动端开发（React Native）\n\n## 安装\n\n```bash\nnpx create-react-app my-app\ncd my-app\nnpm start\n```', '2', 'React,入门,基础', 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `sys_note` VALUES (3, 1, 1, 'JSX语法', '# JSX语法\n\nJSX 是 JavaScript 的语法扩展。\n\n## 基本使用\n\n```jsx\nconst element = <h1>Hello, world!</h1>;\n```\n\n## JSX表达式\n\n```jsx\nconst name = ''张三'';\nconst element = <h1>你好, {name}</h1>;\n```\n\n## JSX属性\n\n```jsx\nconst element = <img src={user.avatarUrl} alt={user.name} />;\n```', '2', 'React,JSX,语法', 2, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `sys_note` VALUES (4, 0, 1, 'React Hooks', NULL, '1', NULL, 2, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, 'React Hooks目录');
INSERT INTO `sys_note` VALUES (5, 4, 1, 'useState Hook', '# useState Hook\n\nuseState 是最常用的 Hook，用于在函数组件中添加状态。\n\n## 基本用法\n\n```jsx\nimport { useState } from ''react'';\n\nfunction Counter() {\n  const [count, setCount] = useState(0);\n\n  return (\n    <div>\n      <p>点击了 {count} 次</p>\n      <button onClick={() => setCount(count + 1)}>\n        点击\n      </button>\n    </div>\n  );\n}\n```', '2', 'React,Hooks,useState', 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `sys_note` VALUES (6, 4, 1, 'useEffect Hook', '# useEffect Hook\n\nuseEffect 用于在函数组件中执行副作用操作。\n\n## 基本用法\n\n```jsx\nimport { useState, useEffect } from ''react'';\n\nfunction Example() {\n  const [count, setCount] = useState(0);\n\n  useEffect(() => {\n    document.title = `点击了 ${count} 次`;\n  }, [count]);\n\n  return (\n    <div>\n      <p>你点击了 {count} 次</p>\n      <button onClick={() => setCount(count + 1)}>\n        点击我\n      </button>\n    </div>\n  );\n}\n```', '2', 'React,Hooks,useEffect', 2, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);

-- ----------------------------
-- 初始化笔记示例数据 - Vue分类
-- ----------------------------
INSERT INTO `sys_note` VALUES (7, 0, 2, 'Vue基础', NULL, '1', NULL, 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, 'Vue基础知识目录');
INSERT INTO `sys_note` VALUES (8, 7, 2, 'Vue简介', '# Vue简介\n\nVue 是一套用于构建用户界面的渐进式框架。\n\n## 核心特性\n\n1. **响应式数据绑定**：自动追踪依赖关系\n2. **组件系统**：可复用的组件\n3. **虚拟DOM**：高效的更新机制\n\n## 安装\n\n```bash\nnpm create vue@latest\ncd vue-project\nnpm install\nnpm run dev\n```', '2', 'Vue,入门,基础', 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `sys_note` VALUES (9, 7, 2, '模板语法', '# Vue模板语法\n\n## 文本插值\n\n```vue\n<span>消息: {{ msg }}</span>\n```\n\n## 指令\n\n```vue\n<div v-if="seen">现在你看到我了</div>\n<button v-on:click="doSomething">点击</button>\n<input v-model="message">\n```', '2', 'Vue,模板,指令', 2, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);

-- ----------------------------
-- 初始化笔记示例数据 - UniApp分类
-- ----------------------------
INSERT INTO `sys_note` VALUES (10, 0, 3, 'UniApp基础', NULL, '1', NULL, 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, 'UniApp基础知识目录');
INSERT INTO `sys_note` VALUES (11, 10, 3, 'UniApp简介', '# UniApp简介\n\nuni-app 是一个使用 Vue.js 开发所有前端应用的框架。\n\n## 特点\n\n1. **一套代码，多端运行**\n2. **完整的Vue生态**\n3. **丰富的组件和API**\n\n## 创建项目\n\n```bash\nnpx degit dcloudio/uni-preset-vue my-project\ncd my-project\nnpm install\nnpm run dev:mp-weixin\n```', '2', 'UniApp,入门,跨平台', 1, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);
INSERT INTO `sys_note` VALUES (12, 10, 3, '页面配置', '# UniApp页面配置\n\n## pages.json\n\n```json\n{\n  "pages": [\n    {\n      "path": "pages/index/index",\n      "style": {\n        "navigationBarTitleText": "首页"\n      }\n    }\n  ],\n  "globalStyle": {\n    "navigationBarTextStyle": "black",\n    "navigationBarTitleText": "uni-app",\n    "navigationBarBackgroundColor": "#F8F8F8",\n    "backgroundColor": "#F8F8F8"\n  }\n}\n```', '2', 'UniApp,配置,pages', 2, '0', '1', 0, '0', 'admin', NOW(), NULL, NULL, NULL);

-- ----------------------------
-- 菜单权限SQL
-- ----------------------------
-- 笔记管理菜单
INSERT INTO sys_menu VALUES (2000, '笔记管理', 0, 6, 'note', NULL, NULL, 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '笔记管理菜单');

-- 笔记分类管理
INSERT INTO sys_menu VALUES (2001, '笔记分类', 2000, 1, 'category', 'note/category/index', NULL, 1, 0, 'C', '0', '0', 'note:category:list', 'tree', 'admin', NOW(), '', NULL, '笔记分类菜单');
INSERT INTO sys_menu VALUES (2002, '笔记分类查询', 2001, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:category:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2003, '笔记分类新增', 2001, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:category:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2004, '笔记分类修改', 2001, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:category:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2005, '笔记分类删除', 2001, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:category:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2006, '笔记分类导出', 2001, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:category:export', '#', 'admin', NOW(), '', NULL, '');

-- 笔记管理
INSERT INTO sys_menu VALUES (2007, '笔记管理', 2000, 2, 'manage', 'note/manage/index', NULL, 1, 0, 'C', '0', '0', 'note:manage:list', 'documentation', 'admin', NOW(), '', NULL, '笔记菜单');
INSERT INTO sys_menu VALUES (2008, '笔记查询', 2007, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:manage:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2009, '笔记新增', 2007, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:manage:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2010, '笔记修改', 2007, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:manage:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2011, '笔记删除', 2007, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:manage:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES (2012, '笔记导出', 2007, 5, '', NULL, NULL, 1, 0, 'F', '0', '0', 'note:manage:export', '#', 'admin', NOW(), '', NULL, '');

