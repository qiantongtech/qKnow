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

import java.util.List;

/**
 * 知识库检索结果
 *
 * @author qknow
 * @date 2025-03-17
 */
@Data
public class Segments {

    /** */
    private String id;

    /** */
    private Integer position;

    /** 文档id */
    private String documentId;

    /** 匹配的文档内容 */
    private String content;

    /** */
    private String answer;

    /** */
    private Integer wordCount;

    /** */
    private Integer tokens;

    /** 关键字 */
    private List<String> keywords;

    /** */
    private String indexNodeId;

    /** */
    private String indexNodeHash;

    /** */
    private Integer hitCount;

    /** */
    private Boolean enabled;

    /** */
    private Long disabledAt;

    /** */
    private String disabledBy;

    /** 状态 */
    private String status;

    /** */
    private String createdBy;

    /** */
    private Long createdAt;

    /** */
    private Long indexingAt;

    /** */
    private Long completedAt;

    /** */
    private String error;

    /** */
    private Long stoppedAt;
}
