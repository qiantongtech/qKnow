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

package tech.qiantong.qknow.thirdparty.domain.dify.knowledge;

import lombok.Data;

/**
 * 知识库创建后返回的实体结果（document对象）
 * @program: qknow
 * @author wang
 * @date 2025/02/19 16:11
 **/
@Data
public class CreateFileResult {
    /** 文件id */
    private String id;

    private Integer position;

    private String dataSourceType;

    private String datasetProcessRuleId;

    /** 文件名称 */
    private String name;

    private String createdFrom;

    private String createdBy;

    private Integer createdAt;

    private Integer tokens;

    private String indexingStatus;

    private String error;

    private Boolean enabled;

    private String disabledAt;

    private String disabledBy;

    private Boolean archived;

    private String displayStatus;

    private Integer wordCount;

    private Integer hitCount;

    private String docForm;

    /** 上传文档的批次号 */
    private String batch;
}
