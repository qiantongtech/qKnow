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

package tech.qiantong.qknow.module.kg.convert.knowledge;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentSaveReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentDO;

/**
 * 知识文件 Convert
 *
 * @author qknow
 * @date 2025-10-20
 */
@Mapper
public interface KgKnowledgeDocumentConvert {
    KgKnowledgeDocumentConvert INSTANCE = Mappers.getMapper(KgKnowledgeDocumentConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kgKnowledgeDocumentPageReqVO 请求参数
     * @return KgKnowledgeDocumentDO
     */
     KgKnowledgeDocumentDO convertToDO(KgKnowledgeDocumentPageReqVO kgKnowledgeDocumentPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kgKnowledgeDocumentSaveReqVO 保存请求参数
     * @return KgKnowledgeDocumentDO
     */
     KgKnowledgeDocumentDO convertToDO(KgKnowledgeDocumentSaveReqVO kgKnowledgeDocumentSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kgKnowledgeDocumentDO 实体对象
     * @return KgKnowledgeDocumentRespVO
     */
     KgKnowledgeDocumentRespVO convertToRespVO(KgKnowledgeDocumentDO kgKnowledgeDocumentDO);

    /**
     * DOList 转换为 RespVOList
     * @param kgKnowledgeDocumentDOList 实体对象列表
     * @return List<KgKnowledgeDocumentRespVO>
     */
     List<KgKnowledgeDocumentRespVO> convertToRespVOList(List<KgKnowledgeDocumentDO> kgKnowledgeDocumentDOList);
}
