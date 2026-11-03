/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

ALTER TABLE `kb_agent_config`
    ADD COLUMN `history_message_count` int(11) NOT NULL DEFAULT 1 COMMENT '历史记录条数' AFTER `pre_prompt`,
    ADD COLUMN `long_term_memory_enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用长期记忆;0否1是' AFTER `history_message_count`,
    ADD COLUMN `show_thinking_process` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否展示思考过程;0否1是' AFTER `long_term_memory_enabled`,
    ADD COLUMN `mcp_config_ids` varchar(128) NULL DEFAULT NULL COMMENT 'mcp配置 ids' AFTER `skill_ids`,
    ADD COLUMN `callable_bot_ids` varchar(128) NULL DEFAULT NULL COMMENT 'Agent可调用的Agent、工作流Bot ids' AFTER `mcp_config_ids`;
