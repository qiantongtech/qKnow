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


ALTER TABLE kmc_document ADD segment_num int NULL COMMENT '分段数量';
ALTER TABLE kmc_document CHANGE segment_num segment_num int NULL COMMENT '分段数量' AFTER chat_model_provider;
ALTER TABLE kmc_document
    ADD COLUMN file_type varchar(32) NULL COMMENT '文件类型;text：普通文件，json：JSON文件' AFTER `segment_num`,
    ADD COLUMN json_style varchar(64) NULL COMMENT 'json 风格' AFTER `file_type`;
ALTER TABLE kmc_document_segment
    ADD COLUMN `thinking` text NULL COMMENT '思考过程' AFTER `sync_status`;
