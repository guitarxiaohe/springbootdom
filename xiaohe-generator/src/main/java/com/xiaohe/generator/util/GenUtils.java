package com.xiaohe.generator.util;

import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.RegExUtils;
import com.xiaohe.common.constant.GenConstants;
import com.xiaohe.common.utils.StringUtils;
import com.xiaohe.generator.config.GenConfig;
import com.xiaohe.generator.domain.GenTable;
import com.xiaohe.generator.domain.GenTableColumn;
import com.xiaohe.system.domain.FieldConfig;

/**
 * 代码生成器 工具类
 * 
 * @author xiaohe
 */
public class GenUtils
{
    /**
     * 初始化表信息
     */
    public static void initTable(GenTable genTable, String operName)
    {
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setPackageName(GenConfig.getPackageName());
        genTable.setModuleName(getModuleName(GenConfig.getPackageName()));
        genTable.setBusinessName(getBusinessName(genTable.getTableName()));
        genTable.setFunctionName(replaceText(genTable.getTableComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        genTable.setCreateBy(operName);
    }

    /**
     * 初始化列属性字段（对齐 field_config 字段类型体系）
     * @param column 列信息
     * @param table 表信息
     * @param fieldConfigKnowledge field_config 知识库（field_key → 最佳配置），优先于硬编码推断
     */
    public static void initColumnField(GenTableColumn column, GenTable table, Map<String, FieldConfig> fieldConfigKnowledge)
    {
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        // 查找 field_config 知识库中该列名的已有配置
        FieldConfig knownConfig = fieldConfigKnowledge != null ? fieldConfigKnowledge.get(columnName) : null;
        column.setTableId(table.getTableId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名（驼峰）
        column.setJavaField(StringUtils.toCamelCase(columnName));
        // 设置默认类型
        column.setJavaType(GenConstants.TYPE_STRING);

        // ======== 字段类型判定（字段配置体系：input/number/textarea/select/dict/date/datetime/switch/file/by/user） ========

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType))
        {
            Integer columnLength = getColumnLength(column.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            column.setHtmlType(htmlType);
        }
        else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType))
        {
            column.setJavaType(GenConstants.TYPE_DATE);
            // 区分 date 与 datetime
            column.setHtmlType("date".equalsIgnoreCase(dataType) ? GenConstants.HTML_DATE : GenConstants.HTML_DATETIME);
        }
        else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType))
        {
            column.setHtmlType(GenConstants.HTML_NUMBER);

            String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0)
            {
                column.setJavaType(GenConstants.TYPE_BIGDECIMAL);
            }
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10)
            {
                column.setJavaType(GenConstants.TYPE_INTEGER);
            }
            else
            {
                column.setJavaType(GenConstants.TYPE_LONG);
            }
        }

        // ======== 字段配置推断：优先 field_config 知识库，fallback 到列名语义 ========

        // 优先：使用 field_config 中该列名的已有配置
        if (knownConfig != null)
        {
            if (StringUtils.isNotEmpty(knownConfig.getFieldType()))
            {
                column.setHtmlType(knownConfig.getFieldType());
            }
            if (StringUtils.isNotEmpty(knownConfig.getFieldRole()))
            {
                column.setFieldRole(knownConfig.getFieldRole());
            }
            if (StringUtils.isNotEmpty(knownConfig.getDictCode()))
            {
                column.setDictType(knownConfig.getDictCode());
            }
            if (StringUtils.isNotEmpty(knownConfig.getSelectEntityKey()))
            {
                column.setSelectEntityKey(knownConfig.getSelectEntityKey());
            }

            // field_role = createUser/updateUser → 不允许编辑
            if ("createUser".equals(knownConfig.getFieldRole())
                    || "updateUser".equals(knownConfig.getFieldRole()))
            {
                column.setIsEdit("0");
            }
        }
        else
        {
            // Fallback：列名语义推断（仅知识库中找不到时使用）
            if (isStatusColumn(columnName))
            {
                column.setHtmlType(GenConstants.HTML_DICT);
                column.setDictType(getDictCodeForColumn(columnName));
            }
            else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                    || StringUtils.endsWithIgnoreCase(columnName, "sex"))
            {
                column.setHtmlType(GenConstants.HTML_DICT);
                column.setDictType(getDictCodeForColumn(columnName));
            }
            else if (arraysContains(GenConstants.USER_SELECT_COLUMNS, columnName))
            {
                column.setHtmlType(GenConstants.HTML_USER);
            }
            else if (arraysContains(GenConstants.FILE_COLUMNS, columnName)
                    || StringUtils.endsWithIgnoreCase(columnName, "avatar")
                    || StringUtils.endsWithIgnoreCase(columnName, "_file_id"))
            {
                column.setHtmlType(GenConstants.HTML_FILE);
            }
            else if (StringUtils.endsWithIgnoreCase(columnName, "content")
                    || StringUtils.endsWithIgnoreCase(columnName, "remark")
                    || StringUtils.endsWithIgnoreCase(columnName, "description")
                    || StringUtils.endsWithIgnoreCase(columnName, "message"))
            {
                column.setHtmlType(GenConstants.HTML_TEXTAREA);
            }
        }

        // ======== 插入/编辑/列表/查询标记 ========

        column.setIsInsert(GenConstants.REQUIRE);

        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPk())
        {
            column.setIsEdit(GenConstants.REQUIRE);
        }
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPk())
        {
            column.setIsList(GenConstants.REQUIRE);
        }
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk())
        {
            column.setIsQuery(GenConstants.REQUIRE);
        }

        // ======== 查询方式 ========

        if (StringUtils.endsWithIgnoreCase(columnName, "name")
                || StringUtils.endsWithIgnoreCase(columnName, "title")
                || StringUtils.endsWithIgnoreCase(columnName, "key"))
        {
            column.setQueryType(GenConstants.QUERY_LIKE);
        }
        // 日期字段 → 查询用 BETWEEN
        if (GenConstants.HTML_DATE.equals(column.getHtmlType())
                || GenConstants.HTML_DATETIME.equals(column.getHtmlType()))
        {
            column.setQueryType("BETWEEN");
        }

        // ======== 字段角色（优先使用知识库配置，仅 file 类型补充推断） ========

        // 文件关联字段 → field_role = fileInfo（知识库无此字段时补充）
        if (GenConstants.HTML_FILE.equals(column.getHtmlType())
                && StringUtils.isEmpty(column.getFieldRole()))
        {
            column.setFieldRole("fileInfo");
        }

        // ======== 关联实体 key（优先使用知识库配置，仅 select 型补充推断） ========

        // 已由知识库设置了 selectEntityKey → 跳过
        // 未设置时，对常见 FK 列名做 fallback 推断
        if (StringUtils.isEmpty(column.getSelectEntityKey()))
        {
            if ("parent_id".equalsIgnoreCase(columnName) && !"dept".equalsIgnoreCase(table.getTableName()))
            {
                column.setSelectEntityKey("dept");
            }
            else if ("dept_id".equalsIgnoreCase(columnName) && !"dept".equalsIgnoreCase(table.getTableName()))
            {
                column.setSelectEntityKey("dept");
            }
        }
    }

    // 判断是否为状态字段
    private static boolean isStatusColumn(String columnName)
    {
        for (String col : GenConstants.STATUS_COLUMNS)
        {
            if (StringUtils.endsWithIgnoreCase(columnName, col))
            {
                return true;
            }
        }
        return false;
    }

    // 获取字段对应的字典代码
    private static String getDictCodeForColumn(String columnName)
    {
        for (String[] pair : GenConstants.DICT_COLUMN_MAP)
        {
            if (columnName.equalsIgnoreCase(pair[0]))
            {
                return pair[1];
            }
        }
        // 默认兜底
        return "sys_normal_disable";
    }

    /**
     * 校验数组是否包含指定值
     * 
     * @param arr 数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue)
    {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取模块名
     * 
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName)
    {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        String moduleName = StringUtils.substring(packageName, lastIndex + 1, nameLength);
        return moduleName;
    }

    /**
     * 获取业务名
     * 
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName)
    {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        String businessName = StringUtils.substring(tableName, lastIndex + 1, nameLength);
        return businessName;
    }

    /**
     * 表名转换成Java类名
     * 
     * @param tableName 表名称
     * @return 类名
     */
    public static String convertClassName(String tableName)
    {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix))
        {
            String[] searchList = StringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase(tableName);
    }

    /**
     * 批量替换前缀
     * 
     * @param replacementm 替换值
     * @param searchList 替换列表
     * @return
     */
    public static String replaceFirst(String replacementm, String[] searchList)
    {
        String text = replacementm;
        for (String searchString : searchList)
        {
            if (replacementm.startsWith(searchString))
            {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    /**
     * 关键字替换
     * 
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text)
    {
        return RegExUtils.replaceAll(text, "(?:表|xiaohe)", "");
    }

    /**
     * 获取数据库类型字段
     * 
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            return StringUtils.substringBefore(columnType, "(");
        }
        else
        {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     * 
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        }
        else
        {
            return 0;
        }
    }
}
