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

package tech.qiantong.qknow.module.kmc.service.knowledgeSegment.bo;

import lombok.Data;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment.KmcDocumentSegmentDO;

/**
 * JSON 风格 - Alpaca
 */
@Data
public class AlpacaBO implements JsonStyle {

    /**
     * 问题
     */
    private String instruction;
    private String input;

    /**
     * 回答
     */
    private String output;
    private String system;

    /**
     * 转换为 kmcDocumentSegmentDO
     *
     * @return kmcDocumentSegmentDO
     */
    @Override
    public KmcDocumentSegmentDO toKmcDocumentSegmentDO(int position) {
        KmcDocumentSegmentDO result = new KmcDocumentSegmentDO();
        result.setContent(this.getInstruction());
        result.setSignContent(this.getInstruction());
        result.setAnswer(this.getOutput());
        result.setPosition((long) position);
        return result;
    }
}
