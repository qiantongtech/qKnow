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

package tech.qiantong.qknow.module.kg.api.knowledge;

import tech.qiantong.qknow.module.kg.api.knowledge.dto.KgKnowledgeDocumentRespDTO;

import java.util.List;

public interface IKgKnowledgeApiService {

    /**
     * 获得知识文件列表
     * @author jinwang
     * @date 2025/06/10 17:14
     * @param ids
     */
    public List<KgKnowledgeDocumentRespDTO> getKgDocumentListByIds(List<Long> ids);

    /**
     * 获得知识文件列表
     * @author jinwang
     * @date 2025/06/10 17:14
     * @param id
     */
    public KgKnowledgeDocumentRespDTO getKgDocumentById(Long id);
}
