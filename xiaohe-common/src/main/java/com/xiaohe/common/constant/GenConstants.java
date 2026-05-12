package com.xiaohe.common.constant;

/**
 * 代码生成通用常量
 * 
 * @author xiaohe
 */
public class GenConstants
{
    /** 单表（增删改查） */
    public static final String TPL_CRUD = "crud";

    /** 树表（增删改查） */
    public static final String TPL_TREE = "tree";

    /** 主子表（增删改查） */
    public static final String TPL_SUB = "sub";

    /** 树编码字段 */
    public static final String TREE_CODE = "treeCode";

    /** 树父编码字段 */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /** 树名称字段 */
    public static final String TREE_NAME = "treeName";

    /** 上级菜单ID字段 */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /** 上级菜单名称字段 */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /** 数据库字符串类型 */
    public static final String[] COLUMNTYPE_STR = { "char", "varchar", "nvarchar", "varchar2" };

    /** 数据库文本类型 */
    public static final String[] COLUMNTYPE_TEXT = { "tinytext", "text", "mediumtext", "longtext" };

    /** 数据库时间类型 */
    public static final String[] COLUMNTYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** 数据库数字类型 */
    public static final String[] COLUMNTYPE_NUMBER = { "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal" };

    /** 页面不需要编辑字段 */
    public static final String[] COLUMNNAME_NOT_EDIT = { "id", "create_by", "created_by", "create_time", "created_time", "del_flag" };

    /** 页面不需要显示的列表字段 */
    public static final String[] COLUMNNAME_NOT_LIST = { "id", "create_by", "created_by", "create_time", "created_time", "del_flag", "update_by",
            "updated_by", "update_time", "updated_time" };

    /** 页面不需要查询字段 */
    public static final String[] COLUMNNAME_NOT_QUERY = { "id", "create_by", "created_by", "create_time", "created_time", "del_flag", "update_by",
            "updated_by", "update_time", "updated_time", "remark" };

    /** 状态字段 → 字典 type */
    public static final String[] STATUS_COLUMNS = { "status", "del_flag", "visible" };

    /** 字典类型映射：column_name → dict_code */
    public static final String[][] DICT_COLUMN_MAP = {
        { "status", "sys_normal_disable" },
        { "sex", "sys_user_sex" },
        { "del_flag", "sys_del_flag" },
        { "visible", "sys_show_hide" },
        { "notice_type", "sys_notice_type" },
        { "job_group", "sys_job_group" },
        { "job_status", "sys_job_status" },
        { "dict_class", "sys_dict_class" },
    };

    /** 用户选择器字段 */
    public static final String[] USER_SELECT_COLUMNS = { "user_id", "assignee", "operator" };

    /** 文件关联字段 */
    public static final String[] FILE_COLUMNS = { "avatar", "file_id" };

    /** Entity基类字段 */
    public static final String[] BASE_ENTITY = { "createBy", "createTime", "updateBy", "updateTime", "remark" };

    /** Tree基类字段 */
    public static final String[] TREE_ENTITY = { "parentName", "parentId", "orderNum", "ancestors", "children" };

    /** 文本框 */
    public static final String HTML_INPUT = "input";

    /** 文本域 */
    public static final String HTML_TEXTAREA = "textarea";

    /** 下拉框 */
    public static final String HTML_SELECT = "select";

    /** 字典下拉框 */
    public static final String HTML_DICT = "dict";

    /** 日期控件 */
    public static final String HTML_DATE = "date";

    /** 日期时间控件 */
    public static final String HTML_DATETIME = "datetime";

    /** 数字控件 */
    public static final String HTML_NUMBER = "number";

    /** 开关控件 */
    public static final String HTML_SWITCH = "switch";

    /** 文件上传控件（关联 SysFileInfo） */
    public static final String HTML_FILE = "file";

    /** 审计人控件 */
    public static final String HTML_BY = "by";

    /** 用户选择控件 */
    public static final String HTML_USER = "user";

    /** 字符串类型 */
    public static final String TYPE_STRING = "String";

    /** 整型 */
    public static final String TYPE_INTEGER = "Integer";

    /** 长整型 */
    public static final String TYPE_LONG = "Long";

    /** 浮点型 */
    public static final String TYPE_DOUBLE = "Double";

    /** 高精度计算类型 */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** 时间类型 */
    public static final String TYPE_DATE = "Date";

    /** 模糊查询 */
    public static final String QUERY_LIKE = "LIKE";

    /** 需要 */
    public static final String REQUIRE = "1";
}
