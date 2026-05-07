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

