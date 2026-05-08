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

-- v2.1.0版本change.sql


-- ----------------------------
-- Table structure for kb_agent_config
-- ----------------------------
DROP TABLE IF EXISTS `kb_agent_config`;
CREATE TABLE `kb_agent_config`  (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                    `bot_id` bigint(20) NULL DEFAULT NULL COMMENT 'botid',
                                    `model_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '大模型配置',
                                    `pre_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '提示词',
                                    `parameters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '参数配置',
                                    `knowledge_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '知识库ids',
                                    `graph_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '知识图谱ids',
                                    `tool_method_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工具ids',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'agent配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kb_agent_config
-- ----------------------------

-- ----------------------------
-- Table structure for kb_bot
-- ----------------------------
DROP TABLE IF EXISTS `kb_bot`;
CREATE TABLE `kb_bot`  (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                           `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
                           `type` tinyint(4) UNSIGNED NOT NULL COMMENT '类型;0：工作流、1：chatflow、2：agent',
                           `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
                           `builtin_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否内置;0：否，1：是',
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
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bot 管理表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for kb_flow_edge
-- ----------------------------
DROP TABLE IF EXISTS `kb_flow_edge`;
CREATE TABLE `kb_flow_edge`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                 `bot_id` bigint(20) NOT NULL COMMENT 'botId',
                                 `source_node_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '开始节点唯一标识',
                                 `target_node_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '结束节点唯一标识',
                                 `source_handle` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开始节点锚点',
                                 `style` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '连线样式',
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
) ENGINE = InnoDB AUTO_INCREMENT = 221 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bot流程关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kb_flow_node
-- ----------------------------
DROP TABLE IF EXISTS `kb_flow_node`;
CREATE TABLE `kb_flow_node`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                 `bot_id` bigint(20) NOT NULL COMMENT 'botId',
                                 `uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点唯一标识',
                                 `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '节点名',
                                 `type` tinyint(4) UNSIGNED NOT NULL COMMENT '节点类型;0：结束，1：开始，2：llm，3：判断...',
                                 `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '节点配置',
                                 `style` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点样式',
                                 `input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '输入参数',
                                 `output` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '输出参数',
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
) ENGINE = InnoDB AUTO_INCREMENT = 332 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bot流程节点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kb_runtime
-- ----------------------------
DROP TABLE IF EXISTS `kb_runtime`;
CREATE TABLE `kb_runtime`  (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                               `bot_id` bigint(20) NOT NULL COMMENT 'botId',
                               `input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '输入问题',
                               `output` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '输出结果',
                               `status` tinyint(4) UNSIGNED NULL DEFAULT NULL COMMENT '运行状态;0:执行中，1:成功，2: 失败',
                               `runtime` int(11) NULL DEFAULT NULL COMMENT '运行时间(毫秒)',
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bot运行表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kb_runtime_node
-- ----------------------------
DROP TABLE IF EXISTS `kb_runtime_node`;
CREATE TABLE `kb_runtime_node`  (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                    `runtime_id` bigint(20) NOT NULL COMMENT '运行时id',
                                    `node_uuid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点唯一标识',
                                    `step` int(11) NOT NULL COMMENT '步骤号',
                                    `status` tinyint(4) UNSIGNED NULL DEFAULT NULL COMMENT '运行状态;0:执行中，1:成功，2: 失败',
                                    `input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '输入数据',
                                    `output` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '输出数据',
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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bot节点运行表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kb_tool
-- ----------------------------
DROP TABLE IF EXISTS `kb_tool`;
CREATE TABLE `kb_tool`  (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                            `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
                            `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
                            `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签',
                            `type` tinyint(4) UNSIGNED NULL DEFAULT NULL COMMENT '类型',
                            `source` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '工具管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of kb_tool
-- ----------------------------

-- ----------------------------
-- Table structure for kb_tool_method
-- ----------------------------
DROP TABLE IF EXISTS `kb_tool_method`;
CREATE TABLE `kb_tool_method`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `workspace_id` bigint(20) NOT NULL COMMENT '工作区id',
                                   `tool_id` bigint(20) NULL DEFAULT NULL COMMENT '工具id',
                                   `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标识',
                                   `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
                                   `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '工具方法' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for kb_bot_apikey
-- ----------------------------
DROP TABLE IF EXISTS `kb_bot_apikey`;
CREATE TABLE `kb_bot_apikey`  (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `workspace_id` bigint(20) NULL DEFAULT NULL COMMENT '工作区id',
                                  `bot_id` bigint(20) NOT NULL COMMENT 'bot_id',
                                  `api_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '访问密钥',
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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'bot 访问密钥' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for kb_code_native
-- ----------------------------
DROP TABLE IF EXISTS `kb_code_native`;
CREATE TABLE `kb_code_native`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `workspace_id` bigint(20) NULL DEFAULT NULL COMMENT '工作区id',
                                   `bot_id` bigint(20) NOT NULL COMMENT 'botId',
                                   `class_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类名',
                                   `param_schema` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '参数定义',
                                   `return_schema` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '结果定义',
                                   `code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '代码',
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '白盒化开发' ROW_FORMAT = Dynamic;

INSERT INTO system_menu VALUES (2335, 'Bot 管理', 2057, 11, 'bot', 'kb/bot/index', NULL, '', 1, 0, 'C', '0', '0', 'kb:bot:bot:list', 'ai-generate-3d-fill', '吴同', '2026-04-13 10:38:02', '超级管理员', '2026-04-21 14:15:51', '');
INSERT INTO system_menu VALUES (2336, '新增 Bot', 2335, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:add', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO system_menu VALUES (2337, '修改 Bot', 2335, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:edit', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO system_menu VALUES (2338, '删除 Bot', 2335, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:remove', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO system_menu VALUES (2339, '获取 Bot', 2335, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'kb:bot:bot:query', '#', '吴同', '2026-04-21 13:58:59', '', NULL, '');
INSERT INTO system_menu VALUES (2395, '工具管理', 2057, 12, 'tool', 'kb/tool/index', NULL, 'kbTool', 1, 0, 'C', '0', '0', 'kb:tool:tool:list', 'briefcase-4-fill', 'admin', '2026-04-21 16:15:27', '超级管理员', '2026-04-21 16:23:12', '工具管理菜单');
INSERT INTO system_menu VALUES (2396, '工具管理查询', 2395, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:query', '#', 'admin', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO system_menu VALUES (2397, '工具管理新增', 2395, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:add', '#', 'admin', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO system_menu VALUES (2398, '工具管理修改', 2395, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:edit', '#', 'admin', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO system_menu VALUES (2399, '工具管理删除', 2395, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:remove', '#', 'admin', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO system_menu VALUES (2400, '工具管理导出', 2395, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:export', '#', 'admin', '2026-04-21 16:15:27', '', NULL, '');
INSERT INTO system_menu VALUES (2401, '工具管理导入', 2395, 6, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'kb:tool:tool:import', '#', 'admin', '2026-04-21 16:15:27', '', NULL, '');

INSERT INTO system_dict_type VALUES(56, 'Bot 类型', 'kg_bot_type', '0', '吴同', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data VALUES(175, 1, '工作流', '0', 'kg_bot_type', NULL, 'primary', 'N', '0', '吴同', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data VALUES(176, 2, 'Chatflow', '1', 'kg_bot_type', NULL, 'primary', 'N', '0', '吴同', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_data VALUES(177, 3, 'Agent', '2', 'kg_bot_type', NULL, 'primary', 'N', '0', '吴同', sysdate(), '', NULL, NULL);
INSERT INTO system_dict_type VALUES (37, '是否类型', 'sys_is_or_not', '0', '吴同', '2026-04-24 10:01:38', '', NULL, '是否选择列表');
INSERT INTO system_dict_data VALUES (117, 1, '否', '0', 'sys_is_or_not', NULL, 'info', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (118, 0, '是', '1', 'sys_is_or_not', NULL, 'primary', 'Y', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO system_dict_type VALUES (31, '索引方式', 'kmc_know_index', '0', '吴同', '2026-04-24 10:01:38', '', NULL, '索引方式列表');
INSERT INTO system_dict_data VALUES (97, 0, '高质量', 'high_quality', 'kmc_know_index', 'high_quality', 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO system_dict_data VALUES (98, 1, '经济', 'economy', 'kmc_know_index', 'economy', 'default', 'N', '0', '吴同', '2026-04-24 10:01:38', '', NULL, NULL);
INSERT INTO kb_tool VALUES (1, 1001, '获取当前时间', '', '[]', NULL, NULL, 1, 0, '小桐', 1, '2026-05-07 15:45:46', '小桐', 1, '2026-05-07 16:24:34', '');
INSERT INTO kb_tool VALUES (2, 1001, '获取城市天气', NULL, '[]', NULL, NULL, 1, 0, '小桐', 1, '2026-05-07 16:24:53', '小桐', NULL, '2026-05-07 16:24:53', NULL);
INSERT INTO kb_tool_method VALUES (1, 1001, 1, 'get_current_time', '获取当前时间', '获取当前时间', 1, 0, '小桐', 1, '2026-05-07 15:49:28', '小桐', 1, '2026-05-07 16:24:43', NULL);
INSERT INTO kb_tool_method VALUES (2, 1001, 2, 'weather_query', '获取城市天气', '获取城市天气', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-07 16:25:22', NULL);

INSERT INTO kb_tool_method VALUES (1, 1001, 1, 'get_current_time', '获取当前时间', '获取当前时间', 1, 0, '小桐', 1, '2026-05-07 15:49:28', '小桐', 1, '2026-05-07 16:24:43', NULL);
INSERT INTO kb_tool_method VALUES (2, 1001, 2, 'weather_query', '获取城市天气', '获取城市天气', 1, 0, '小桐', 1, '2026-05-07 16:25:22', '小桐', NULL, '2026-05-07 16:25:22', NULL);
