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


-- v2.0.0版本change.sql
# 2026-04-27
INSERT INTO system_menu VALUES (2057, 'AI工作台', 0, 1, 'kb', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '系统工具', '小桐', '2026-04-20 17:22:18', '小桐', '2026-04-27 09:38:10', '');
INSERT INTO system_menu VALUES (2058, '知识图谱', 0, 3, 'kg', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '知识中心', '小桐', '2026-04-20 17:23:39', '', NULL, '');
INSERT INTO system_menu VALUES (2059, '知识库', 0, 4, 'kmc', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '知识抽取', '小桐', '2026-04-20 17:24:02', '', NULL, '');
INSERT INTO system_menu VALUES (2061, '首页', 0, 0, 'kd', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '首页', '小桐', '2026-04-21 11:19:30', '小桐', '2026-04-27 09:31:43', '');
INSERT INTO system_menu VALUES (2062, '首页', 2061, 0, 'integrated', 'system/index', NULL, '', 1, 0, 'C', '0', '0', '', '首页', '小桐', '2026-04-21 11:20:52', '小桐', '2026-04-27 14:48:08', '');
INSERT INTO system_menu VALUES (2063, '应用中心', 0, 2, 'kac', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '知识应用', '小桐', '2026-04-27 09:35:56', '', NULL, '');
INSERT INTO system_menu VALUES (2064, '插件中心', 0, 5, 'plugin', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'tool_new_icon', '小桐', '2026-04-27 09:37:31', '', NULL, '');
INSERT INTO system_menu VALUES (2079, '产品动态', 2061, 1, 'dynamic', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'education', '小桐', '2026-04-27 15:43:52', '', NULL, '');

UPDATE system_menu SET order_num = 6, update_by = '小桐', update_time = '2026-04-27 09:37:41' WHERE `menu_id` = 1;
UPDATE system_menu SET order_num = 7, update_by = '小桐', update_time = '2026-04-27 09:37:46' WHERE `menu_id` = 2;
UPDATE system_menu SET order_num = 8, update_by = '小桐', update_time = '2026-04-27 09:37:53' WHERE `menu_id` = 3;

UPDATE system_menu SET `menu_name` = '知识中心', `parent_id` = 2058, `order_num` = 1, `path` = 'kmc', `component` = NULL, `query` = NULL, `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '知识中心', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-04-20 17:24:16', `remark` = '' WHERE `menu_id` = 2000;
UPDATE system_menu SET `menu_name` = '知识抽取', `parent_id` = 2058, `order_num` = 2, `path` = 'ext', `component` = NULL, `query` = NULL, `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '知识抽取', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-04-20 17:24:26', `remark` = '' WHERE `menu_id` = 2015;
UPDATE system_menu SET `menu_name` = '知识应用', `parent_id` = 2058, `order_num` = 3, `path` = 'app', `component` = NULL, `query` = NULL, `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '知识应用', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-04-20 17:24:35', `remark` = '' WHERE `menu_id` = 2044;
UPDATE system_menu SET `menu_name` = '数据管理', `parent_id` = 2058, `order_num` = 4, `path` = 'dm', `component` = NULL, `query` = NULL, `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '数据管理', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-04-20 17:24:59', `remark` = '' WHERE `menu_id` = 2046;


-- v2.0.1版本change.sql
# 2026-04-28
INSERT INTO system_menu VALUES (2080, '模型市场', 1, 11, 'ai', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'book-marked-fill', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-27 13:51:16', '');
INSERT INTO system_menu VALUES (2322, '模型市场', 2080, 1, 'modelMarket', 'ai/modelMarket/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:modelMarket:key:list', 'book-ai-fill', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO system_menu VALUES (2323, '我的模型', 2080, 2, 'myModel', 'ai/myModel/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:modelMarket:key:list', 'book-read-fill', '吴同', '2026-04-21 13:57:04', '吴同', '2026-04-21 13:57:04', '');
INSERT INTO system_menu VALUES (2324, 'api密钥配置', 2080, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ai:modelMarket:key:edit', '#', '吴同', '2026-04-21 13:57:04', '', NULL, '');
INSERT INTO system_menu VALUES (2325, 'api密钥查看', 2080, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'ai:modelMarket:key:query', '#', '吴同', '2026-04-21 13:57:04', '', NULL, '');

# 2026-04-29
-- 创建 AI 模型表
DROP TABLE IF EXISTS ai_model;
CREATE TABLE ai_model
(
    `id`           BIGINT AUTO_INCREMENT COMMENT 'ID',
    `workspace_id` BIGINT   NOT NULL COMMENT '工作区id',
    `key_id`       BIGINT COMMENT '秘钥id',
    `name`         VARCHAR(128) COMMENT '模型名称',
    `model`        VARCHAR(32) COMMENT '模型标志',
    `platform`     VARCHAR(32) COMMENT '平台',
    `type`         TINYINT(4) UNSIGNED COMMENT '类型',
    `sort`         INT COMMENT '排序值',
    `status`       TINYINT(4) UNSIGNED COMMENT '状态',
    `valid_flag`   TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
    `del_flag`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
    `create_by`    VARCHAR(32) COMMENT '创建人',
    `creator_id`   BIGINT COMMENT '创建人id',
    `create_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    VARCHAR(32) COMMENT '更新人',
    `updater_id`   BIGINT COMMENT '更新人id',
    `update_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`       VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (id)
) COMMENT = 'AI 模型';

-- 创建 API秘钥表
DROP TABLE IF EXISTS ai_api_key;
CREATE TABLE ai_api_key
(
    `id`           BIGINT AUTO_INCREMENT COMMENT 'ID',
    `workspace_id` BIGINT   NOT NULL COMMENT '工作区id',
    `name`         VARCHAR(128) COMMENT '名称',
    `api_key`      VARCHAR(128) COMMENT '秘钥',
    `platform`     VARCHAR(32) COMMENT '平台',
    `url`          VARCHAR(256) COMMENT 'API地址',
    `platform_tag` VARCHAR(256) COMMENT '平台标签',
    `description`  VARCHAR(1024) COMMENT '描述',
    `deploy_type`  VARCHAR(32) COMMENT '部署方式',
    `status`       TINYINT(4) UNSIGNED COMMENT '状态',
    `valid_flag`   TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
    `del_flag`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
    `create_by`    VARCHAR(32) COMMENT '创建人',
    `creator_id`   BIGINT COMMENT '创建人id',
    `create_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    VARCHAR(32) COMMENT '更新人',
    `updater_id`   BIGINT COMMENT '更新人id',
    `update_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`       VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (id)
) COMMENT = 'API秘钥';

insert into ai_api_key values (1, 1001, '通义千问', NULL, 'TongYi', NULL, '1,2,3,4,5,6', '通义千问提供的模型。', '2', 0, 1, 0, NULL, NULL, sysdate(), '吴同', NULL, sysdate(), NULL);
insert into ai_api_key values (2, 1001, 'DeepSeek', NULL, 'DeepSeek', NULL, '1,2,3', '深度求索提供的模型，例如 deepseek-chat、deepseek-coder 。', '2', 0, 1, 0, NULL, NULL, sysdate(), '吴同', NULL, sysdate(), NULL);
insert into ai_api_key values (3, 1001, 'Ollama', NULL, 'Ollama', NULL, '1,2,3', 'ollama', '1', 0, 1, 0, NULL, NULL, sysdate(), '吴同', NULL, sysdate(), NULL);
INSERT INTO ai_api_key VALUES (4, 1001, 'OpenAI', NULL, 'OpenAI', NULL, '1,2,3,4,5,6', '符合 openai 规范的模型', '1', 0, 1, 0, NULL, NULL, '2026-04-21 13:57:13', '吴同', NULL, '2026-04-21 16:33:47', NULL);

INSERT INTO `system_dict_type` VALUES (51, 'ai apikey状态', 'ai_apikey_status', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (52, 'ai模型提供平台', 'ai_model_platform', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (53, 'ai平台部署方式', 'ai_deploy_type', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (54, 'ai模型标签', 'ai_model_tag', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);

INSERT INTO `system_dict_data` VALUES (151, 1, '本地部署', '1', 'ai_deploy_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (152, 2, 'API接入（开放平台）', '2', 'ai_deploy_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (153, 1, 'DeepSeek', 'DeepSeek', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (154, 1, 'CHAT', '1', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (155, 2, 'IMAGE', '2', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (156, 3, 'VOICE', '3', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (157, 4, 'VIDEO', '4', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (158, 5, 'EMBEDDING', '5', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (159, 6, 'RERANK', '6', 'ai_model_tag', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (160, 1, '未配置', '0', 'ai_apikey_status', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (161, 2, '已配置', '1', 'ai_apikey_status', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (162, 3, '通义千问', 'TongYi', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-04-24 10:01:38', NULL);
INSERT INTO `system_dict_data` VALUES (163, 2, 'Ollama', 'Ollama', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-04-24 10:01:38', NULL);
INSERT INTO `system_dict_data` VALUES (164, 3, '已配置', '2', 'ai_apikey_status', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '吴同', '2026-04-24 10:01:38', NULL);
INSERT INTO `system_dict_data` VALUES (172, 4, 'OpenAI', 'OpenAI', 'ai_model_platform', NULL, 'default', 'N', '0', '吴同', '2026-04-21 13:57:05', '吴同', '2026-04-21 13:57:05', NULL);


-- v2.0.2版本change.sql

-- ----------------------------
-- Table structure for kg_knowledge_category
-- ----------------------------
DROP TABLE IF EXISTS `kg_knowledge_category`;
CREATE TABLE `kg_knowledge_category`  (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                          `parent_id` bigint(20) NOT NULL COMMENT '父级id',
                                          `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
                                          `order_num` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
                                          `ancestors` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '祖级列表',
                                          `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                          `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                          `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                          `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                          `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of kg_knowledge_category
-- ----------------------------
INSERT INTO `kg_knowledge_category` VALUES (1, 1001, 0, '疾病与诊断', 1, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (2, 1001, 0, '治疗与干预', 2, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (3, 1001, 0, '药物与器械', 3, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (4, 1001, 0, '人体与功能', 4, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (5, 1001, 0, '人员与机构', 5, '0', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (6, 1001, 1, '疾病实体', 1, '0,1', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (7, 1001, 1, '诊断实体', 2, '0,1', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (8, 1001, 2, '治疗方法', 1, '0,2', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (9, 1001, 2, '治疗方案', 2, '0,2', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (10, 1001, 3, '药品实体', 1, '0,3', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (11, 1001, 3, '医疗器械', 2, '0,3', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (12, 1001, 4, '解剖结构', 1, '0,4', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (13, 1001, 4, '生理功能', 2, '0,4', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (14, 1001, 5, '医疗人员', 1, '0,5', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);
INSERT INTO `kg_knowledge_category` VALUES (15, 1001, 5, '医疗机构', 2, '0,5', 1, 0, '吴同', 2, '2026-04-24 10:01:43', '吴同', NULL, '2026-04-24 10:01:43', NULL);

-- ----------------------------
-- Table structure for kg_knowledge_document
-- ----------------------------
DROP TABLE IF EXISTS `kg_knowledge_document`;
CREATE TABLE `kg_knowledge_document`  (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                          `category_id` bigint(20) NOT NULL COMMENT '知识分类id',
                                          `category_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '知识分类名称',
                                          `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
                                          `path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
                                          `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件描述',
                                          `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                          `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                          `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                          `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                          `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                          `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识文件表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for kg_knowledge_document_log
-- ----------------------------
DROP TABLE IF EXISTS `kg_knowledge_document_log`;
CREATE TABLE `kg_knowledge_document_log`  (
                                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                              `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                              `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
                                              `user_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
                                              `document_id` int(11) NULL DEFAULT NULL COMMENT '文件id',
                                              `document_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名',
                                              `type` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '操作类型;0：预览，1：下载',
                                              `valid_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
                                              `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
                                              `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                              `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
                                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                              `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                              `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
                                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                              `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件操作日志' ROW_FORMAT = DYNAMIC;

INSERT INTO system_menu VALUES (2223, '知识中心', 2132, 2, ':kgId/knowledge', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'book-open-fill', '吴同', '2025-10-20 09:43:47', '吴同', '2025-10-22 18:36:23', '');
INSERT INTO system_menu VALUES (2224, '知识分类', 2223, 1, 'category', 'kg/knowledge/category/index', NULL, '', 1, 0, 'C', '0', '0', 'kg:knowledge:category:list', '#', 'admin', '2025-10-20 09:50:56', '吴同', '2025-10-20 09:56:13', '知识分类菜单');
INSERT INTO system_menu VALUES (2225, '知识分类查询', 2224, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:query', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2226, '知识分类新增', 2224, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:add', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2227, '知识分类修改', 2224, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:edit', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2228, '知识分类删除', 2224, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:remove', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2229, '知识分类导出', 2224, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:export', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2230, '知识分类导入', 2224, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:category:import', '#', 'admin', '2025-10-20 09:50:56', '', NULL, '');
INSERT INTO system_menu VALUES (2231, '知识文件', 2223, 1, 'document', 'kg/knowledge/document/index', NULL, '', 1, 0, 'C', '0', '0', 'kg:knowledge:document:list', '#', 'admin', '2025-10-20 09:51:05', '吴同', '2025-10-20 09:56:20', '知识文件菜单');
INSERT INTO system_menu VALUES (2232, '知识文件查询', 2231, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:query', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2233, '知识文件新增', 2231, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:add', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2234, '知识文件修改', 2231, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:edit', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2235, '知识文件删除', 2231, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:remove', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2236, '知识文件导出', 2231, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:export', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');
INSERT INTO system_menu VALUES (2237, '知识文件导入', 2231, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kg:knowledge:document:import', '#', 'admin', '2025-10-20 09:51:05', '', NULL, '');

UPDATE system_menu SET `menu_name` = '知识库', `parent_id` = 0, `order_num` = 4, `path` = 'kmc', `component` = NULL, `query` = NULL, `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '知识中心', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-04-29 16:38:55', `remark` = '' WHERE `menu_id` = 2000;
UPDATE system_menu SET `menu_name` = '知识分类', `parent_id` = 2000, `order_num` = 1, `path` = ':kbId/kmcCategory', `component` = 'kmc/kmcCategory/index', `query` = '', `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'kmc:kmcCategory:kmcCategory:list', `icon` = '#', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-05-06 10:01:38', `remark` = '' WHERE `menu_id` = 2001;
UPDATE system_menu SET `menu_name` = '知识文件', `parent_id` = 2000, `order_num` = 2, `path` = ':kbId/kmcDocument', `component` = 'kmc/kmcDocument/index', `query` = '', `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'kmcDocument:kmcDocument:document:list', `icon` = '#', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-05-06 10:02:30', `remark` = '' WHERE `menu_id` = 2008;
UPDATE system_menu SET `menu_name` = '知识文件导出', `parent_id` = 2008, `order_num` = 1, `path` = '', `component` = NULL, `query` = NULL, `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'F', `visible` = '0', `status` = '0', `perms` = 'kmcDocument:kmcDocument:document:export', `icon` = '#', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '', `update_time` = NULL, `remark` = '' WHERE `menu_id` = 2009;
UPDATE system_menu SET `menu_name` = '知识中心', `parent_id` = 2058, `order_num` = 1, `path` = 'knowledge', `component` = NULL, `query` = NULL, `route_name` = '', `is_frame` = 1, `is_cache` = 0, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = 'book-open-fill', `create_by` = '吴同', `create_time` = '2025-10-20 09:43:47', `update_by` = '小桐', `update_time` = '2026-04-29 17:43:32', `remark` = '' WHERE `menu_id` = 2223;
INSERT INTO system_dict_type VALUES (32, '知识库启用状态', 'kmc_know_valid', '0', '吴同', '2026-04-21 13:57:05', '', NULL, '知识库启用状态');
INSERT INTO system_dict_data VALUES (99, 0, '禁用', 'false', 'kmc_know_valid', 'disabled', 'default', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (173, 1, '启用', 'true', 'kmc_know_valid', 'enabled', 'default', 'N', '0', '吴同', '2026-04-21 13:57:05', 'user001', '2026-05-07 14:14:41', NULL);
INSERT INTO system_dict_type VALUES (30, '文档同步状态', 'document_sync_status', '0', '吴同', '2026-04-21 13:57:05', '', NULL, '文档预览下载埋点类型');
INSERT INTO system_dict_data VALUES (93, 0, '待解析', '0', 'document_sync_status', NULL, 'info', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (94, 0, '解析中', '1', 'document_sync_status', NULL, 'warning', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (95, 0, '解析成功', '2', 'document_sync_status', NULL, 'success', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (96, 0, '解析失败', '3', 'document_sync_status', NULL, 'danger', 'N', '0', '吴同', '2026-04-21 13:57:05', '', NULL, NULL);

INSERT INTO system_dict_type VALUES (39, '结构化抽取更新类型', 'ext_update_type', '0', '吴同', '2026-04-24 10:01:38', '', NULL, '结构化抽取更新类型列表');
INSERT INTO system_dict_data VALUES (122, 2, '全量更新', '0', 'ext_update_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (123, 2, '增量更新', '1', 'ext_update_type', NULL, 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);



----------------------------------------页面标准化以后更新的数据-----------------------------------------------------
# 2026-05-13
INSERT INTO system_menu VALUES (2402, '知识资产看板', 2061, 3, 'knowledgeAsset', 'kd/knowledgeAsset/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 10:34:41', '', NULL, '');
INSERT INTO system_menu VALUES (2403, 'Bot运营看板', 2061, 4, 'botOperation', 'kd/botOperation/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 10:43:41', '', NULL, '');
INSERT INTO system_menu VALUES (2404, '应用运营看板', 2061, 5, 'appOperations', 'kd/appOperations/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 10:44:06', '', NULL, '');
INSERT INTO system_menu VALUES (2405, '概览', 2063, 0, 'overview', 'kac/overview/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 13:42:03', '', NULL, '');
INSERT INTO system_menu VALUES (2407, '横向通用应用', 2063, 2, 'horizontal', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-05-13 13:42:58', '小桐', '2026-05-13 13:43:31', '');
INSERT INTO system_menu VALUES (2408, '纵向行业应用', 2063, 3, 'vertical', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 13:43:26', '', NULL, '');
INSERT INTO system_menu VALUES (2410, '我的应用', 2063, 5, 'myApp', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 13:44:26', '', NULL, '');
INSERT INTO system_menu VALUES (2411, '插件管理', 2064, 0, 'plugin', 'system/developing/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', '小桐', '2026-05-13 13:45:32', '', NULL, '');

UPDATE system_menu SET `menu_name` = '产品动态', `parent_id` = 2061, `order_num` = 1, `path` = 'dynamic', `component` = 'system/developing/index', `query` = NULL, `route_name` = '', `is_frame` = 1, `is_cache` = 0, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = 'education', `create_by` = '小桐', `create_time` = '2026-04-27 15:43:52', `update_by` = '小桐', `update_time` = '2026-05-13 11:47:42', `remark` = '' WHERE `menu_id` = 2079;
UPDATE system_menu SET `menu_name` = '知识文件', `parent_id` = 2223, `order_num` = 0, `path` = 'document', `component` = 'kg/knowledge/document/index', `query` = NULL, `route_name` = '', `is_frame` = 1, `is_cache` = 0, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'kg:knowledge:document:list', `icon` = '#', `create_by` = 'admin', `create_time` = '2025-10-20 09:51:05', `update_by` = '小桐', `update_time` = '2026-05-13 13:47:33', `remark` = '知识文件菜单' WHERE `menu_id` = 2231;
UPDATE system_menu SET `menu_name` = '知识分类', `parent_id` = 2000, `order_num` = 2, `path` = ':kbId/kmcCategory', `component` = 'kmc/kmcCategory/index', `query` = '', `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'kmc:kmcCategory:kmcCategory:list', `icon` = '#', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-05-13 17:32:20', `remark` = '' WHERE `menu_id` = 2001;
UPDATE system_menu SET `menu_name` = '知识文件', `parent_id` = 2000, `order_num` = 1, `path` = ':kbId/kmcDocument', `component` = 'kmc/kmcDocument/index', `query` = '', `route_name` = NULL, `is_frame` = 1, `is_cache` = 0, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'kmcDocument:kmcDocument:document:list', `icon` = '#', `create_by` = '小桐', `create_time` = '2026-04-20 09:32:54', `update_by` = '小桐', `update_time` = '2026-05-13 17:32:26', `remark` = '' WHERE `menu_id` = 2008;

INSERT INTO kmc_category VALUES (1, 1001, 0, 1, '疾病与症状', 1, '0', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '顶级分类：疾病与症状');
INSERT INTO kmc_category VALUES (2, 1001, 0, 1, '药物与治疗', 2, '0', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '顶级分类：药物与治疗');
INSERT INTO kmc_category VALUES (3, 1001, 0, 1, '检查与诊断', 3, '0', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '顶级分类：检查与诊断');
INSERT INTO kmc_category VALUES (4, 1001, 0, 1, '人体结构与功能', 4, '0', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '顶级分类：人体结构与功能');
INSERT INTO kmc_category VALUES (5, 1001, 0, 1, '健康管理与预防', 5, '0', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '顶级分类：健康管理与预防');
INSERT INTO kmc_category VALUES (6, 1001, 1, 1, '常见疾病', 1, '0,1', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '常见疾病的分类，如感冒、高血压等');
INSERT INTO kmc_category VALUES (7, 1001, 1, 1, '典型症状', 2, '0,1', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '疾病相关的典型表现，如发热、疼痛等');
INSERT INTO kmc_category VALUES (8, 1001, 2, 1, '常用药物', 1, '0,2', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '临床常用药品及OTC药物');
INSERT INTO kmc_category VALUES (9, 1001, 2, 1, '治疗方案', 2, '0,2', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '各类疾病的治疗方法和方案');
INSERT INTO kmc_category VALUES (10, 1001, 3, 1, '影像检查', 1, '0,3', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '放射、超声、CT、MRI等影像学检查');
INSERT INTO kmc_category VALUES (11, 1001, 3, 1, '实验室检查', 2, '0,3', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '血液、尿液、病理等检验项目');
INSERT INTO kmc_category VALUES (12, 1001, 4, 1, '器官系统', 1, '0,4', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '心、肝、肺、肾等主要器官及相关系统');
INSERT INTO kmc_category VALUES (13, 1001, 4, 1, '生理功能', 2, '0,4', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '机体正常的生理过程和机能');
INSERT INTO kmc_category VALUES (14, 1001, 5, 1, '健康生活方式', 1, '0,5', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '饮食、运动、作息等健康习惯指导');
INSERT INTO kmc_category VALUES (15, 1001, 5, 1, '疾病预防', 2, '0,5', 1, 0, '小桐', 1, '2026-04-21 13:57:08', '小桐', 1, '2026-04-21 13:57:08', '疫苗接种、筛查、风险评估等预防措施');

INSERT INTO kmc_document VALUES (9, 1001, 2, 1, NULL, '药物与治疗', '心血管介入治疗器械.txt', '/2026/05/14/6a053715dcf17570f4d68768.txt', '心血管介入治疗器械', 0, 0, 2, 'custom', 'paragraph', 1, 0, '50', 1024, '\n\n', 512, '\n', 'text_model', 'Chinese', NULL, NULL, 1, 0, '小桐', 1, '2026-05-14 10:44:42', '小桐', NULL, '2026-05-14 10:54:21', NULL);
INSERT INTO kmc_document VALUES (10, 1001, 2, 1, NULL, '药物与治疗', '常见药物.txt', '/2026/05/14/6a05472ddcf17570f4d6876c.txt', '常见药物', 0, 0, 2, 'custom', 'paragraph', 1, 0, '50', 1024, '\n\n', 512, '\n', 'text_model', 'Chinese', NULL, NULL, 1, 0, '小桐', 1, '2026-05-14 11:53:20', '小桐', NULL, '2026-05-14 11:53:45', NULL);


-- 2026.0514 bot 管理拆分
UPDATE system_menu SET menu_name='Bot 管理', parent_id=2057, order_num=1, `path`='bot', component='kb/bot/index', query='', route_name='', is_frame=1, is_cache=0, menu_type='M', visible='0', status='0', perms='kb:bot:bot:list', icon='ai-generate-3d-fill', create_by='吴同', create_time='2026-04-13 10:38:02', update_by='小桐', update_time='2026-05-13 17:20:53', remark='' WHERE menu_id=2335;

INSERT INTO system_menu VALUES(2412, '工作流', 2335, 1, 'workflow', 'kb/bot/index', '{"botType":0}', '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-05-13 17:22:28', '小桐', '2026-05-14 14:10:12', '');
INSERT INTO system_menu VALUES(2413, 'chatflow', 2335, 2, 'chatflow', 'kb/bot/index', '{"botType":1}', '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-05-14 09:22:25', '小桐', '2026-05-14 09:26:26', '');
INSERT INTO system_menu VALUES(2414, 'agent', 2335, 3, 'agent', 'kb/bot/index', '{"botType":2}', '', 1, 0, 'C', '0', '0', '', '#', '小桐', '2026-05-14 09:22:48', '小桐', '2026-05-14 09:31:26', '');



INSERT INTO system_dict_type VALUES (100, '抽取日志类型', 'ext_task_log_type', '0', '小桐', '2026-05-14 11:08:02', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (178, 0, '结构化', '0', 'ext_task_log_type', NULL, 'primary', 'N', '0', '小桐', '2026-05-14 11:08:45', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (179, 1, '非结构化', '1', 'ext_task_log_type', NULL, 'primary', 'N', '0', '小桐', '2026-05-14 11:08:55', '', NULL, NULL);

UPDATE system_menu SET `menu_name` = '数据连接' WHERE `menu_id` = 2047;
INSERT INTO kg_knowledge_document VALUES (1, 1001, 1, '疾病与诊断', '疾病与诊断：医学实践的核心要素.docx', '/2026/05/14/6a0544aab3541c9296b057dc.docx', NULL, 1, 0, '小桐', 1, '2026-05-14 11:42:38', '小桐', NULL, '2026-05-14 11:42:38', NULL);
INSERT INTO kg_knowledge_document VALUES (2, 1001, 2, '治疗与干预', '疾病治疗与干预综合指南.docx', '/2026/05/14/6a0544e9b3541c9296b057de.docx', NULL, 1, 0, '小桐', 1, '2026-05-14 11:43:39', '小桐', NULL, '2026-05-14 11:43:39', NULL);
INSERT INTO kg_knowledge_document VALUES (3, 1001, 3, '药物与器械', '药物与医疗器械应用指南.docx', '/2026/05/14/6a0544f3b3541c9296b057df.docx', NULL, 1, 0, '小桐', 1, '2026-05-14 11:43:48', '小桐', NULL, '2026-05-14 11:43:48', NULL);
INSERT INTO kg_knowledge_document VALUES (4, 1001, 5, '人员与机构', '医疗体系中的人力资源与机构组织.docx', '/2026/05/14/6a054501b3541c9296b057e0.docx', NULL, 1, 0, '小桐', 1, '2026-05-14 11:44:03', '小桐', NULL, '2026-05-14 11:44:03', NULL);
INSERT INTO kg_knowledge_document VALUES (5, 1001, 4, '人体与功能', '医学与健康科学综合文档.docx', '/2026/05/14/6a05451ab3541c9296b057e1.docx', NULL, 1, 0, '小桐', 1, '2026-05-14 11:44:27', '小桐', NULL, '2026-05-14 11:44:27', NULL);

# 2025-05-15
INSERT INTO system_dict_type VALUES (57, '应用插件状态', 'kac_horizontal_status', '0', '吴同', '2026-04-23 19:35:19', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (null, 0, '停用', '0', 'kac_horizontal_status', NULL, 'warning', 'N', '0', '吴同', '2026-04-23 19:35:48', '吴同', '2026-04-23 19:36:17', NULL);
INSERT INTO system_dict_data VALUES (null, 1, '正常', '1', 'kac_horizontal_status', NULL, 'primary', 'N', '0', '吴同', '2026-04-23 19:36:08', '', NULL, NULL);


-- 修改菜单名
UPDATE system_menu SET menu_name='Chatflow' WHERE menu_id=2413;
UPDATE system_menu SET menu_name='Agent' WHERE menu_id=2414;

-- 添加工具方法数
ALTER TABLE qknow_dev.kb_tool ADD method_num INT NULL COMMENT '工具所用方法数';
ALTER TABLE qknow_dev.kb_tool CHANGE method_num method_num INT NULL COMMENT '工具所用方法数' AFTER source;

-- 演示数据
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(1, 1001, '获取城市天气', '输入城市名称，快速获取当地实时气温、湿度、风力', '[{"name":"生活查询"},{"name":"气象"},{"name":"便民"}]', NULL, NULL, 11, 1, 0, '小桐', 1, '2026-05-07 16:24:53', '小桐', 1, '2026-05-15 15:03:50', '支持全国省市县区级精准查询，无地域限制');
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(2, 1001, '获取当前时间', '快速获取当前系统标准北京时间、对应星期及时区信息，支持自定义时间格式输出', '[{"name":"时间工具"},{"name":"办公"},{"name":"系统"}]', NULL, NULL, 1, 1, 0, '小桐', 1, '2026-05-07 15:45:46', '小桐', 1, '2026-05-15 15:03:04', '');
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(3, 1001, '代码运行调试', '在线执行 Python、Java、JS 等常用代码，返回运行结果与报错信息', '[{"name":"编程开发"},{"name":"调试"},{"name":"运维"}]', NULL, NULL, 2, 1, 0, '小桐', 1, '2026-05-06 14:55:10', '小桐', 1, '2026-05-15 15:04:34', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(4, 1001, '金额单位换算', '元、万元、亿元、美元、欧元等多币种及大额数字快速换算', '[{"name":"金融计算"},{"name":"数值"},{"name":"办公"}]', NULL, NULL, 3, 1, 0, '小桐', 1, '2026-05-06 14:55:10', '小桐', 1, '2026-05-15 15:04:58', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(5, 1001, '文本摘要提取', '对长文案、文章、对话内容精简，提炼核心，压缩篇幅', '[{"name":"文本处理"},{"name":"文案"},{"name":"总结"}]', NULL, NULL, 5, 1, 0, '小桐', 1, '2026-05-06 14:52:46', '小桐', 1, '2026-05-15 15:04:10', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(6, 1001, '数学公式计算', '解决几何、代数、函数、方程等各类数理运算题', '[{"name":"学习解题"},{"name":"数理"},{"name":"计算"}]', NULL, NULL, 3, 1, 0, '小桐', 1, '2026-05-04 14:55:10', '小桐', 1, '2026-05-15 15:05:24', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(7, 1001, '文案改写润色', '优化语句通顺度，调整文风，改写正式 / 口语 / 文艺风格', '[{"name":"文案创作"},{"name":"写作"},{"name":"优化"}]', NULL, NULL, 9, 1, 0, '小桐', 1, '2026-05-04 14:55:10', '小桐', 1, '2026-05-15 15:05:48', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(8, 1001, '网络资讯搜索', '检索全网公开行业资讯、热点事件、行业基础知识', '[{"name":"信息检索"},{"name":"调研"},{"name":"资讯"}]', NULL, NULL, 4, 1, 0, '小桐', 1, '2026-05-03 14:55:10', '小桐', 1, '2026-05-15 15:06:22', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(9, 1001, '行程路线规划', '输入起止地点，规划自驾、公交、步行最优出行路线', '[{"name":"出行规划"},{"name":"交通"},{"name":"旅游"}]', NULL, NULL, 6, 1, 0, '小桐', 1, '2026-05-02 14:55:10', '小桐', 1, '2026-05-15 15:06:40', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(10, 1001, '单词翻译互译', '支持中英日韩多语种单词、短句实时双向翻译', '[{"name":"语言学习"},{"name":"翻译"},{"name":"外语"}]', NULL, NULL, 6, 1, 0, '小桐', 1, '2026-05-02 14:55:10', '小桐', 1, '2026-05-15 15:06:56', NULL);
INSERT INTO kb_tool (id, workspace_id, name, description, tags, `type`, source, method_num, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(11, 1001, '表格数据整理', '杂乱文本一键规整为标准行列表格，方便统计使用', '[{"name":"数据整理"},{"name":"办公"},{"name":"统计"}]', NULL, NULL, 5, 1, 0, '小桐', 1, '2026-05-01 14:55:10', '小桐', 1, '2026-05-15 15:07:16', NULL);

INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(16, 1001, 'qKnow-智能写作-智能缩写', 1, '精简冗余语句，压缩文本篇幅，保留核心原意，缩短整体内容', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-05-15 15:55:35', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(17, 1001, 'qKnow-智能写作-模板生成', 1, '根据使用场景自动生成标准写作模板，直接填空即可快速成文知识问答(图谱)和知识问答(知识库+知识图谱)有什么区别？智能写作-智能扩写和智能写作-智能缩写的应用场景分别有哪些？除了这些，还有哪些常见的智能写作功能？', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-05-15 15:55:35', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(21, 1001, 'qKnow-智能写作-大纲内容优化', 2, '调整大纲逻辑结构，完善层级要点，梳理行文脉络，让框架更清晰合理', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-03-26 18:26:27', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(22, 1001, 'qKnow-智能写作-文章内容优化', 2, '修正语句语病，丰富内容细节，理顺行文逻辑，全面提升文章整体质量', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-03-26 18:26:50', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(23, 1001, 'qKnow-智能写作-日报生成', 2, '汇总当日工作事项，梳理完成进度与工作计划，快速生成简洁工作日报', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-03-26 19:08:29', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(24, 1001, 'qKnow-智能写作-周报生成', 2, '整合一周工作内容，总结成果问题，规划下周安排，自动生成规范周报', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-03-26 19:08:49', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(25, 1001, 'qKnow-智能写作-月报生成', 2, '梳理月度工作实绩，复盘工作情况，制定下月规划，高效撰写正式月报', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:35', '吴同', NULL, '2026-03-26 19:09:09', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(13, 1001, 'qKnow-智能写作-智能续写', 1, '承接已有文案内容，顺着行文逻辑自然续写，完善整篇文案内容', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:34', '吴同', NULL, '2026-05-15 15:55:34', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(14, 1001, 'qKnow-智能写作-智能扩写', 1, '在原有短句基础上扩充内容，丰富细节语句，让文案内容更加饱满', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:34', '吴同', NULL, '2026-05-15 15:55:34', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(15, 1001, 'qKnow-智能写作-智能润色', 1, '优化语句措辞，理顺行文逻辑，提升文案流畅度与整体文笔质感', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:34', '吴同', NULL, '2026-05-15 15:55:34', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(1, 1001, 'qKnow-知识问答(图谱)', 1, '依托知识图谱数据，依托实体关联关系精准作答专业领域问答内容', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:28', '吴同', NULL, '2026-05-15 15:55:28', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(2, 1001, 'qKnow-知识问答(知识库+知识图谱)', 1, '融合知识库与图谱数据，整合结构化信息，给出全面精准的问答答案', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:28', '吴同', NULL, '2026-05-15 15:55:28', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(5, 1001, 'qKnow-知识检索', 1, '快速检索海量业务知识内容，精准定位所需资料，高效调取参考信息', 1, 1, 0, '小桐', 1, '2026-05-15 15:55:28', '吴同', NULL, '2026-05-15 15:55:28', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(7, 1001, 'qKnow-三元组抽取', 0, '对实体关系三元组进行抽取，并进行校验去重，适配多行业知识图谱搭建场景', 1, 1, 0, '小桐', 1, '2026-05-15 10:08:59', '吴同', NULL, '2026-04-24 10:08:59', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(3, 1001, 'qKnow-意图检索', 0, '精准识别用户查询真实意图，匹配对应业务场景，快速筛选贴合需求的相关信息', 0, 1, 0, '小桐', 1, '2026-05-14 15:55:28', '吴同', NULL, '2026-05-15 15:55:28', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(4, 1001, 'qKnow-图谱语义检索', 0, '依托知识图谱关联关系，突破关键词匹配，从语义层面深度检索关联内容', 0, 1, 0, '小桐', 1, '2026-05-14 15:55:28', '吴同', NULL, '2026-05-15 15:55:28', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(6, 1001, 'qKnow-图谱模型提取 ', 0, '从海量文本中抽取实体、关系与属性，结构化输出数据，搭建标准化知识图谱', 0, 1, 0, '小桐', 1, '2026-05-13 15:55:29', '吴同', NULL, '2026-05-15 15:55:29', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(11, 1001, 'qKnow-智能写作-大纲内容提取', 0, '从完整文稿中快速梳理拆解，提炼核心框架与分段要点，整理成清晰内容大纲', 1, 1, 0, '小桐', 1, '2026-05-11 15:55:34', '吴同', NULL, '2026-05-15 15:55:34', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(12, 1001, 'qKnow-智能写作-生成文章', 0, '根据指定主题与要求，自动创作逻辑通顺、内容完整的完整成文内容', 1, 1, 0, '小桐', 1, '2026-05-11 15:55:34', '吴同', NULL, '2026-05-15 15:55:34', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(8, 1001, 'qKnow-合规性检查', 0, '合规性检查', 0, 1, 0, '小桐', 1, '2026-05-11 15:55:33', '吴同', NULL, '2026-05-15 15:55:33', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(9, 1001, 'qKnow-问答建议', 0, '结合对话上下文与知识库，自动生成贴合场景的优质问答话术与参考答案', 1, 1, 0, '小桐', 1, '2026-05-11 15:55:33', '吴同', NULL, '2026-05-15 15:55:33', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(18, 1001, 'qKnow-智能写作-生成摘要', 0, '浓缩长文核心信息，精简篇幅提炼主旨，产出简洁凝练的内容摘要', 1, 1, 0, '小桐', 1, '2026-05-10 15:55:35', '吴同', NULL, '2026-05-15 15:55:35', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(19, 1001, 'qKnow-智能写作-生成大纲', 0, '依据写作主题，自动搭建文章结构，划分段落层次，输出完整写作框架', 1, 1, 0, '小桐', 1, '2026-05-10 15:55:35', '吴同', NULL, '2026-05-15 15:55:35', NULL);
INSERT INTO kb_bot (id, workspace_id, name, `type`, description, builtin_flag, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(20, 1001, 'qKnow-智能写作-标题优化', 0, '润色改写原有标题，优化句式风格，提升吸引力与适配度，适配各类发文场景', 0, 1, 0, '小桐', 1, '2026-05-10 15:55:35', '吴同', NULL, '2026-05-15 15:55:35', NULL);

INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(1, 1001, 1, 'weather_query', '获取城市天气', '获取城市天气', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-07 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(2, 1001, 1, 'weather_max_airTemperature', '获取城市最高温度', '获取城市最高温度', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-06 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(3, 1001, 1, 'weather_min_airTemperature', '获取城市最低温度', '获取城市最低温度', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-06 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(4, 1001, 1, 'weather_time_query', '获取城市某个时间点的温度', '获取城市某个时间点的温度', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-06 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(5, 1001, 1, 'weather_max_humidity', '获取城市最大湿度', '获取城市最大湿度', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-06 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(6, 1001, 1, 'weather_min_humidity', '获取城市最小湿度', '获取城市最小湿度', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-03 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(7, 1001, 1, 'weather_max_wind', '获取城市最大风力', '获取城市最大风力', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-03 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(8, 1001, 1, 'weather_min_wind', '获取城市最小风力', '获取城市最小风力', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-02 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(9, 1001, 1, 'weather_max_pressure', '获取城市最高气压', '获取城市最高气压', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-02 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(10, 1001, 1, 'weather_min_pressure', '获取城市最低气压', '获取城市最低气压', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-01 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(11, 1001, 1, 'weather_n_weat', '获取城市未来n天的预报', '获取城市未来n天的预报', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-01 16:25:22', NULL);
INSERT INTO kb_tool_method (id, workspace_id, tool_id, code, name, description, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(12, 1001, 2, 'get_current_time', '获取当前时间', '获取当前时间', 1, 0, '小桐', 1, '2026-05-07 15:49:28', '小桐', 1, '2026-05-07 16:24:43', NULL);

INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"title":"预防糖尿病"}', '{"text":"[\\"糖尿病预防策略：科学饮食与生活方式调整\\",\\"基于医学研究的糖尿病前期干预方案\\",\\"有效控制血糖：糖尿病风险因素识别与管理\\",\\"糖尿病预防中的运动疗法应用及效果评估\\",\\"综合健康管理视角下的糖尿病早期预防措施\\"]"}', 1, 5155, 1, 0, '小桐', 1, '2026-04-21 17:39:36', '超级管理员', NULL, '2026-04-21 17:39:36', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"title":"预防糖尿病"}', '{"text":"[\\"糖尿病预防策略：科学饮食与生活方式调整\\",\\"构建有效糖尿病预防体系的关键步骤\\",\\"早期干预：识别并管理糖尿病风险因素\\",\\"基于循证医学的糖尿病综合预防方案\\",\\"优化生活习惯：降低糖尿病发病几率的实践方法\\"]"}', 1, 5348, 1, 0, '小桐', 1, '2026-04-21 18:18:53', '超级管理员', NULL, '2026-04-21 18:18:53', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"title":"预防糖尿病"}', '{"text":"[\\"糖尿病预防策略：科学饮食与生活方式调整\\",\\"构建全面的糖尿病预防体系：从早期筛查到长期管理\\",\\"基于循证医学的糖尿病预防指南：关键措施与实践建议\\",\\"糖尿病高危人群的识别与干预方法综述\\",\\"有效控制血糖水平：糖尿病前期患者的预防性治疗方案\\"]"}', 1, 9141, 1, 0, '小桐', 2, '2026-04-23 19:55:18', '吴同', NULL, '2026-04-23 19:55:18', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"title":"知识平台的内容"}', '{"text":"[\\"知识平台内容构建策略与实践方法\\",\\"优化知识平台内容质量的系统性方案\\",\\"知识平台内容管理的有效路径与技巧\\",\\"提升知识平台用户参与度的内容设计思路\\",\\"知识平台内容生态建设的实施步骤\\"]"}', 1, 3909, 1, 0, '小桐', 2, '2026-04-23 21:09:57', '吴同', NULL, '2026-04-23 21:09:57', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"workContent":"应用中心开发","position":"软件工程师","workPlan":"AI工作台开发"}', '{"text":"\\n","name":"tetx"}', 1, 10256, 1, 0, '小桐', NULL, '2026-04-23 21:10:55', NULL, NULL, '2026-04-23 21:10:55', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"title":"知识平台"}', '{"text":"[\\"构建高效知识平台的策略与实践\\",\\"知识平台的内容管理与优化方法论\\",\\"企业级知识平台的设计原则及应用案例分析\\",\\"知识平台用户互动机制的探索与实施\\",\\"基于云技术的知识平台搭建与运维指南\\"]"}', 1, 5556, 1, 0, '小桐', 2, '2026-04-24 09:04:43', '吴同', NULL, '2026-04-24 09:04:43', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"query":"","summaryCount":"1","title":"预防糖尿病"}', '{"text":"[\\"预防糖尿病的关键在于健康的生活方式，包括均衡饮食、规律运动、保持健康体重以及定期监测血糖水平。建议减少高糖和高脂肪食物的摄入，增加蔬菜、水果和全谷物的比例，并且每周至少进行150分钟的中等强度有氧运动。此外，戒烟限酒、避免过度压力也是预防糖尿病的重要措施。\\"]"}', 1, 4691, 1, 0, '小桐', 2, '2026-04-24 09:20:31', '吴同', NULL, '2026-04-24 09:20:31', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"word_count":"1000","query":"预防糖尿病的关键在于健康的生活方式，包括均衡饮食、规律运动、保持健康体重以及定期监测血糖水平。建议减少高糖和高脂肪食物的摄入，增加蔬菜、水果和全谷物的比例，并且每周至少进行150分钟的中等强度有氧运动。此外，戒烟限酒、避免过度压力也是预防糖尿病的重要措施。","title":"预防糖尿病"}', '{"text":"\\n","name":"text"}', 1, 13987, 1, 0, '小桐', NULL, '2026-04-24 09:20:39', NULL, NULL, '2026-04-24 09:20:39', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"query":"","summaryCount":"1","title":"预防糖尿病"}', '{"text":"[\\"预防糖尿病的关键在于健康的生活方式，包括均衡饮食、定期运动和保持正常体重。应减少高糖、高脂肪食物的摄入，增加蔬菜和全谷物的比例。定期监测血糖水平，并遵循医生建议进行必要的药物治疗。此外，戒烟限酒也是预防糖尿病的重要措施。\\"]"}', 1, 8768, 1, 0, '小桐', 1, '2026-04-24 09:31:07', '超级管理员', NULL, '2026-04-24 09:31:07', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"word_count":"1000","query":"预防糖尿病的关键在于健康的生活方式，包括均衡饮食、定期运动和保持正常体重。应减少高糖、高脂肪食物的摄入，增加蔬菜和全谷物的比例。定期监测血糖水平，并遵循医生建议进行必要的药物治疗。此外，戒烟限酒也是预防糖尿病的重要措施。","title":"预防糖尿病"}', '{"text":"\\n","name":"text"}', 1, 15648, 1, 0, '小桐', NULL, '2026-04-24 09:31:21', NULL, NULL, '2026-04-24 09:31:21', NULL);
INSERT INTO kb_runtime (id, workspace_id, bot_id, `input`, `output`, status, runtime, valid_flag, del_flag, create_by, creator_id, create_time, update_by, updater_id, update_time, remark) VALUES(null, 1001, 7, '{"title":"预防糖尿病"}', '{"text":"[\\"糖尿病预防策略：科学饮食与生活方式调整\\",\\"基于最新研究的糖尿病早期预防措施\\",\\"糖尿病高风险人群的有效管理方案\\",\\"实用指南：通过运动和监测血糖预防糖尿病\\",\\"综合方法探讨：如何有效降低糖尿病发病风险\\"]"}', 1, 8157, 1, 0, '小桐', 2, '2026-04-24 15:58:49', '吴同', NULL, '2026-04-24 15:58:49', NULL);
