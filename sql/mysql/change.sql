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