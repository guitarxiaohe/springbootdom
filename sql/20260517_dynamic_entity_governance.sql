-- 动态实体治理增量：权限菜单 + 字典元数据对齐

-- 定时任务计划执行错误策略字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
SELECT '计划执行错误策略', 'sys_job_misfire_policy', '0', 'admin', NOW(), '定时任务计划执行错误策略'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'sys_job_misfire_policy');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark, tag_color)
SELECT 1, '立即执行', '1', 'sys_job_misfire_policy', 'primary', 'N', '0', 'admin', NOW(), '错过后立即执行', '#409eff'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'sys_job_misfire_policy' AND dict_value = '1');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark, tag_color)
SELECT 2, '执行一次', '2', 'sys_job_misfire_policy', 'warning', 'N', '0', 'admin', NOW(), '错过后执行一次', '#e6a23c'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'sys_job_misfire_policy' AND dict_value = '2');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark, tag_color)
SELECT 3, '放弃执行', '3', 'sys_job_misfire_policy', 'danger', 'Y', '0', 'admin', NOW(), '错过后放弃执行', '#f56c6c'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'sys_job_misfire_policy' AND dict_value = '3');

-- 修复已有 field_config 字典字段配置
UPDATE field_config
SET dict_code = 'sys_job_misfire_policy', updated_time = UNIX_TIMESTAMP() * 1000
WHERE entity_key = 'job' AND field_key = 'misfire_policy';

UPDATE field_config
SET dict_code = 'sys_oper_type', updated_time = UNIX_TIMESTAMP() * 1000
WHERE entity_key = 'operLog' AND field_key = 'business_type';

-- 动态实体导入导出统一权限菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2005, '动态实体导出', 3, 13, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dynamic:entity:export', '#', 'admin', NOW(), '', NULL, '动态实体导出权限'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2005);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2006, '动态实体导入', 3, 14, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dynamic:entity:import', '#', 'admin', NOW(), '', NULL, '动态实体导入权限'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2006);
