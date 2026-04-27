/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */


# 2025-10-31
ALTER TABLE ext_struct_task ADD update_type  tinyint DEFAULT 0  COMMENT '更新类型;0：全量更新，1：增量更新' AFTER datasource_id;
ALTER TABLE ext_struct_task ADD update_frequency varchar(256) DEFAULT NULL  COMMENT '更新频率' AFTER update_type;

# 2025-11-05
ALTER TABLE ext_struct_task ADD update_status tinyint  DEFAULT 1 COMMENT '定时更新状态（0正常 1暂停）' AFTER update_frequency;

# 2025-11-10
ALTER TABLE ext_schema_mapping ADD primary_key varchar(32)  COMMENT '主键' AFTER entity_name_field;
ALTER TABLE ext_schema_mapping ADD entity_time_field varchar(128)  COMMENT '增量依据字段' AFTER primary_key;
ALTER TABLE ext_schema_mapping ADD last_date_time DATETIME  COMMENT '最新数据时间' AFTER  entity_time_field;

# 2025-11-17
ALTER TABLE ext_schema_attribute ADD dict_type_id BIGINT(20)  COMMENT '关联字典类型id' AFTER data_type;

# 2025-12-16
DROP TABLE IF EXISTS ext_relation_mapping_middle;
CREATE TABLE ext_relation_mapping_middle(
                                            `id` BIGINT AUTO_INCREMENT COMMENT 'ID' ,
                                            `relation_id` BIGINT NOT NULL  COMMENT '关系表id' ,
                                            `table_name` VARCHAR(32)   COMMENT '中间表名称' ,
                                            `table_field` VARCHAR(128) NOT NULL  COMMENT '关联源表字段' ,
                                            `relation_field` VARCHAR(128) NOT NULL  COMMENT '关联目标表字段' ,
                                            `valid_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效' ,
                                            `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除' ,
                                            `create_by` VARCHAR(32)   COMMENT '创建人' ,
                                            `creator_id` BIGINT   COMMENT '创建人id' ,
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
                                            `update_by` VARCHAR(32)   COMMENT '更新人' ,
                                            `updater_id` BIGINT   COMMENT '更新人id' ,
                                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
                                            `remark` VARCHAR(512)   COMMENT '备注' ,
                                            PRIMARY KEY (id)
)  COMMENT = '关系映射中间表';

# 2025-12-25
insert into system_dict_type values (16, '导入表映射状态', 'ext_mapping_status', '0', '小桐', sysdate(), '', NULL, '导入表映射状态');
insert into system_dict_data values (50, 0, '未映射', '0', 'ext_mapping_status', NULL, 'warning', 'N', '0', '小桐', sysdate(), '小桐', sysdate(), NULL);
insert into system_dict_data values (51, 1, '已映射', '1', 'ext_mapping_status', NULL, 'success', 'N', '0', '小桐', sysdate(), '', NULL, NULL);


# 2026-01-29
DROP TABLE IF EXISTS ext_task_log;
CREATE TABLE ext_task_log(
                             `id` BIGINT AUTO_INCREMENT COMMENT 'ID' ,
                             `workspace_id` BIGINT NOT NULL  COMMENT '工作区id' ,
                             `task_id` BIGINT NOT NULL  COMMENT '任务id;' ,
                             `task_type` TINYINT(4) UNSIGNED NOT NULL  COMMENT '任务类型;0：结构化；1：非结构化' ,
                             `task_name` VARCHAR(128) NOT NULL  COMMENT '任务名称' ,
                             `status` TINYINT(4) UNSIGNED   COMMENT '状态;1成功，0失败' ,
                             `error_msg` VARCHAR(2000)   COMMENT '错误消息;' ,
                             `start_time` DATETIME   COMMENT '执行开始时间' ,
                             `end_time` DATETIME   COMMENT '执行结束时间' ,
                             `valid_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效' ,
                             `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除' ,
                             `create_by` VARCHAR(32)   COMMENT '创建人' ,
                             `creator_id` BIGINT   COMMENT '创建人id' ,
                             `create_time` DATETIME  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
                             `update_by` VARCHAR(32)   COMMENT '更新人' ,
                             `updater_id` BIGINT   COMMENT '更新人id' ,
                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
                             `remark` VARCHAR(512)   COMMENT '备注' ,
                             PRIMARY KEY (id)
)  COMMENT = '抽取任务执行日志';
DROP TABLE IF EXISTS ext_task_log_detail;
CREATE TABLE ext_task_log_detail(
                                    `id` BIGINT AUTO_INCREMENT COMMENT 'ID;' ,
                                    `workspace_id` BIGINT NOT NULL DEFAULT 1001 COMMENT '工作区id' ,
                                    `log_id` BIGINT NOT NULL  COMMENT '执行日志id' ,
                                    `step` VARCHAR(2000) NOT NULL  COMMENT '任务执行步骤' ,
                                    `valid_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效' ,
                                    `del_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除' ,
                                    `create_by` VARCHAR(32)   COMMENT '创建人' ,
                                    `creator_id` BIGINT   COMMENT '创建人id' ,
                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
                                    `update_by` VARCHAR(32)   COMMENT '更新人' ,
                                    `updater_id` BIGINT   COMMENT '更新人id' ,
                                    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间' ,
                                    `remark` VARCHAR(512)   COMMENT '备注' ,
                                    PRIMARY KEY (id)
)  COMMENT = '抽取任务执行日志详情';

insert into system_menu values (2054, '非结构化抽取任务日志', 2030, 8, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ext:extUnstructTask:unstructtask:taskLog', '#', '小桐', sysdate(), '小桐', sysdate(), '');
insert into system_menu values (2055, '抽取日志', 2015, 5, 'extTaskLog', 'ext/extTaskLog/index', NULL, '', 1, 0, 'C', '0', '0', 'ext:extTasklog:tasklog:list', '#', '小桐', sysdate(), '小桐', sysdate(), '');

insert into system_dict_type values (17, '知识抽取日志状态', 'ext_log_status', '0', '小桐', sysdate(), '', NULL, '知识抽取日志状态');
insert into system_dict_type values (18, '知识抽取任务类型', 'ext_task_type', '0', '小桐', sysdate(), '', NULL, '知识抽取任务类型');

insert into system_dict_data values (52, 1, '失败', '0', 'ext_log_status', NULL, 'danger', 'N', '0', '小桐', sysdate(), '', NULL, NULL);
insert into system_dict_data values (53, 2, '成功', '1', 'ext_log_status', NULL, 'success', 'N', '0', '小桐', sysdate(), '', NULL, NULL);
insert into system_dict_data values (54, 1, '结构化抽取', '0', 'ext_task_type', NULL, 'default', 'N', '0', '小桐', sysdate(), '', NULL, NULL);
insert into system_dict_data values (55, 2, '非结构化抽取', '1', 'ext_task_type', NULL, 'default', 'N', '0', '小桐', sysdate(), '', NULL, NULL);
insert into system_dict_data values (56, 1, '队列中', '4', 'ext_task_status', NULL, 'info', 'N', '0', '小桐', sysdate(), '', NULL, NULL);

insert into system_config values(7, '非结构化抽取线程数量', 'ext.thread.concurrency', '3', 'Y', '小桐', sysdate(), '', NULL, '最大50');

# 2026-04-27
INSERT INTO system_menu VALUES (2057, 'AI工作台', 0, 1, 'kb', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '系统工具', '小桐', '2026-04-20 17:22:18', '小桐', '2026-04-27 09:38:10', '');
INSERT INTO system_menu VALUES (2058, '知识图谱', 0, 3, 'kg', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '知识中心', '小桐', '2026-04-20 17:23:39', '', NULL, '');
INSERT INTO system_menu VALUES (2059, '知识库', 0, 4, 'kmc', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '知识抽取', '小桐', '2026-04-20 17:24:02', '', NULL, '');
INSERT INTO system_menu VALUES (2061, '看板', 0, 0, 'kd', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '首页', '小桐', '2026-04-21 11:19:30', '小桐', '2026-04-27 09:31:43', '');
INSERT INTO system_menu VALUES (2062, '综合看板', 2061, 0, 'integrated', 'system/index', NULL, '', 1, 0, 'C', '0', '0', '', 'chart', '小桐', '2026-04-21 11:20:52', '小桐', '2026-04-27 10:16:32', '');
INSERT INTO system_menu VALUES (2063, '应用中心', 0, 2, 'kac', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '知识应用', '小桐', '2026-04-27 09:35:56', '', NULL, '');
INSERT INTO system_menu VALUES (2064, '插件中心', 0, 5, 'plugin', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'tool_new_icon', '小桐', '2026-04-27 09:37:31', '', NULL, '');
INSERT INTO system_menu VALUES (2065, 'Bot 管理', 2057, 0, 'bot', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', '', '数据管理', '小桐', '2026-04-27 10:37:10', '小桐', '2026-04-27 10:40:45', '');
INSERT INTO system_menu VALUES (2066, '工具管理', 2057, 1, 'tools', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', '', 'tool_new_icon', '小桐', '2026-04-27 10:40:34', '小桐', '2026-04-27 10:44:23', '');
INSERT INTO system_menu VALUES (2067, '横向通用应用', 2063, 12, 'horizontal', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:apply:apply:list', 'book-3-fill', '小桐', '2026-04-07 13:48:40', '小桐', '2026-04-10 11:31:23', '');
INSERT INTO system_menu VALUES (2068, '纵向行业应用', 2063, 13, 'vertical', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:apply:apply:list', 'book-ai-fill', '小桐', '2026-04-08 13:39:12', '小桐', '2026-04-10 11:31:32', '');
INSERT INTO system_menu VALUES (2069, '我的解决方案', 2063, 14, 'mySolution', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:solution:solution:list', 'account-box-fill', '小桐', '2026-04-08 13:41:59', '小桐', '2026-04-22 15:34:09', '');
INSERT INTO system_menu VALUES (2070, '我的应用', 2063, 15, 'myApp', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:apply:apply:list', 'group-fill', '小桐', '2026-04-08 13:43:33', '小桐', '2026-04-10 11:32:01', '');
INSERT INTO system_menu VALUES (2071, '概览', 2063, 10, 'overview', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:solution:solution:list', 'ai-generate-3d-fill', '小桐', '2026-04-08 13:56:39', '吴同', '2026-04-22 10:40:47', '');
INSERT INTO system_menu VALUES (2072, '解决方案', 2063, 11, 'solution', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', 'kac:solution:solution:list', 'briefcase-4-fill', '小桐', '2026-04-08 13:57:05', '小桐', '2026-04-22 15:18:13', '');
INSERT INTO system_menu VALUES (2073, '知识库', 2059, 0, 'knowledgeBase', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-04-27 10:52:21', '', NULL, '');
INSERT INTO system_menu VALUES (2074, '知识分类', 2059, 1, ':kbId/kmcCategory', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-04-27 10:53:02', '小桐', '2026-04-27 10:54:25', '');
INSERT INTO system_menu VALUES (2075, '知识文件', 2059, 2, ':kbId/kmcDocument', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-04-27 10:53:30', '小桐', '2026-04-27 10:54:30', '');
INSERT INTO system_menu VALUES (2076, '召回测试', 2059, 3, ':kbId/recall', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-04-27 10:53:55', '小桐', '2026-04-27 10:54:35', '');
INSERT INTO system_menu VALUES (2077, '知识库设置', 2059, 4, ':kbId/knowledgeBase', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '#', '小桐', '2026-04-27 10:54:19', '小桐', '2026-04-27 10:54:39', '');
INSERT INTO system_menu VALUES (2078, '插件管理', 2064, 0, 'plugin', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-04-27 10:55:28', '', NULL, '');

UPDATE system_menu SET order_num = 6, update_by = '小桐', update_time = '2026-04-27 09:37:41' WHERE `menu_id` = 1;
UPDATE system_menu SET order_num = 7, update_by = '小桐', update_time = '2026-04-27 09:37:46' WHERE `menu_id` = 2;
UPDATE system_menu SET order_num = 8, update_by = '小桐', update_time = '2026-04-27 09:37:53' WHERE `menu_id` = 3;

