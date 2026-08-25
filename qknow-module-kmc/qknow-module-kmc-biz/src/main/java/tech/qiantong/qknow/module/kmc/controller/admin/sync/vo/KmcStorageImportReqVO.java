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

package tech.qiantong.qknow.module.kmc.controller.admin.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "第三方存储文件导入知识库请求")
public class KmcStorageImportReqVO extends KmcStorageSelectionReqVO {

    @NotNull(message = "知识库不能为空")
    private Long knowledgeBaseId;

    private String knowledgeBaseName;

    @NotNull(message = "所属分类不能为空")
    private Long categoryId;

    private String categoryName;

    @Size(max = 256, message = "文件描述长度不能超过256个字符")
    private String description;

    private String jsonStyle;
}

