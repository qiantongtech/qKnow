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

