-- 实体配置表
CREATE TABLE IF NOT EXISTS `entity_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entity_key` varchar(100) NOT NULL COMMENT '实体标识',
    `entity_name` varchar(100) NOT NULL COMMENT '实体名称',
    `table_name` varchar(100) NOT NULL COMMENT '关联表名',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `sort` int DEFAULT '0' COMMENT '排序',
    `is_visible` tinyint DEFAULT '1' COMMENT '是否显示: 0-否,1-是',
    `created_by` bigint DEFAULT NULL COMMENT '创建人',
    `created_time` bigint DEFAULT NULL COMMENT '创建时间戳',
    `updated_by` bigint DEFAULT NULL COMMENT '修改人',
    `updated_time` bigint DEFAULT NULL COMMENT '修改时间戳',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_entity_key` (`entity_key`),
    UNIQUE KEY `uk_table_name` (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='实体配置表';

-- 字段配置表
CREATE TABLE IF NOT EXISTS `field_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `entity_key` varchar(100) NOT NULL COMMENT '实体标识',
    `field_key` varchar(100) NOT NULL COMMENT '字段标识',
    `field_name` varchar(100) NOT NULL COMMENT '字段名称',
    `dict_code` varchar(100) DEFAULT NULL COMMENT '字典编码',
    `select_entity_key` varchar(100) DEFAULT NULL COMMENT '下拉实体key',
    `sort` int DEFAULT '0' COMMENT '排序',
    `is_fuzzy_search` tinyint DEFAULT '0' COMMENT '是否模糊查询: 0-否,1-是',
    `is_visible` tinyint DEFAULT '1' COMMENT '是否显示: 0-否,1-是',
    `created_by` bigint DEFAULT NULL COMMENT '创建人',
    `created_time` bigint DEFAULT NULL COMMENT '创建时间戳',
    `updated_by` bigint DEFAULT NULL COMMENT '修改人',
    `updated_time` bigint DEFAULT NULL COMMENT '修改时间戳',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_entity_field` (`entity_key`,`field_key`),
    KEY `idx_entity_key` (`entity_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字段配置表';

-- 插入实体配置数据（手动配置有意义的 entity_key）
INSERT INTO entity_config (entity_key, entity_name, table_name, description, sort, created_time) VALUES
('user', '用户', 'sys_user', '系统用户管理', 1, UNIX_TIMESTAMP() * 1000),
('dept', '部门', 'sys_dept', '部门管理', 2, UNIX_TIMESTAMP() * 1000),
('role', '角色', 'sys_role', '角色管理', 3, UNIX_TIMESTAMP() * 1000),
('menu', '菜单', 'sys_menu', '菜单管理', 4, UNIX_TIMESTAMP() * 1000),
('dict', '字典', 'sys_dict_type', '字典管理', 5, UNIX_TIMESTAMP() * 1000),
('dictData', '字典数据', 'sys_dict_data', '字典数据管理', 6, UNIX_TIMESTAMP() * 1000),
('config', '参数配置', 'sys_config', '系统参数配置', 7, UNIX_TIMESTAMP() * 1000),
('operLog', '操作日志', 'sys_oper_log', '操作日志记录', 8, UNIX_TIMESTAMP() * 1000),
('loginLog', '登录日志', 'sys_logininfor', '登录日志记录', 9, UNIX_TIMESTAMP() * 1000),
('job', '定时任务', 'sys_job', '定时任务管理', 10, UNIX_TIMESTAMP() * 1000),
('jobLog', '任务日志', 'sys_job_log', '定时任务日志', 11, UNIX_TIMESTAMP() * 1000),
('notice', '通知公告', 'sys_notice', '通知公告管理', 12, UNIX_TIMESTAMP() * 1000),
('post', '岗位', 'sys_post', '岗位管理', 13, UNIX_TIMESTAMP() * 1000),
('noteCategory', '笔记分类', 'sys_note_category', '笔记分类管理', 14, UNIX_TIMESTAMP() * 1000),
('note', '笔记', 'sys_note', '笔记管理', 15, UNIX_TIMESTAMP() * 1000),
('project', '项目', 'project_info', '项目管理', 16, UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE entity_name = VALUES(entity_name);

-- 使用存储过程批量插入字段配置
DELIMITER //

DROP PROCEDURE IF EXISTS generate_field_config//

CREATE PROCEDURE generate_field_config(IN db_name VARCHAR(100))
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE tbl_name VARCHAR(100);
    DECLARE ent_key VARCHAR(100);
    DECLARE fld_name VARCHAR(100);
    DECLARE fld_comment VARCHAR(500);
    DECLARE fld_order INT;
    DECLARE cur CURSOR FOR
        SELECT
            ec.table_name,
            ec.entity_key,
            c.COLUMN_NAME,
            c.COLUMN_COMMENT,
            c.ORDINAL_POSITION
        FROM entity_config ec
        LEFT JOIN information_schema.COLUMNS c
            ON c.TABLE_SCHEMA = db_name
            AND c.TABLE_NAME = ec.table_name
        WHERE c.COLUMN_NAME IS NOT NULL
        ORDER BY ec.sort, c.ORDINAL_POSITION;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO tbl_name, ent_key, fld_name, fld_comment, fld_order;
        IF done THEN
            LEAVE read_loop;
        END IF;

        INSERT IGNORE INTO field_config (
            entity_key,
            field_key,
            field_name,
            dict_code,
            select_entity_key,
            sort,
            is_fuzzy_search,
            is_visible,
            created_time
        ) VALUES (
            ent_key,
            fld_name,
            IFNULL(NULLIF(fld_comment, ''), fld_name),
            NULL,
            NULL,
            fld_order,
            0,
            1,
            UNIX_TIMESTAMP() * 1000
        );
    END LOOP;

    CLOSE cur;

    SELECT CONCAT('已成功插入字段配置记录') AS result;
END//

DELIMITER ;

-- 执行存储过程
CALL generate_field_config('xiaohe');

-- 删除存储过程
DROP PROCEDURE IF EXISTS generate_field_config;
