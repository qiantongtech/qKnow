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
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentLogDO;

/**
 * 文件操作日志 Convert
 *
 * @author qknow
 * @date 2025-10-22
 */
@Mapper
public interface KgKnowledgeDocumentLogConvert {
    KgKnowledgeDocumentLogConvert INSTANCE = Mappers.getMapper(KgKnowledgeDocumentLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kgKnowledgeDocumentLogPageReqVO 请求参数
     * @return KgKnowledgeDocumentLogDO
     */
     KgKnowledgeDocumentLogDO convertToDO(KgKnowledgeDocumentLogPageReqVO kgKnowledgeDocumentLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kgKnowledgeDocumentLogSaveReqVO 保存请求参数
     * @return KgKnowledgeDocumentLogDO
     */
     KgKnowledgeDocumentLogDO convertToDO(KgKnowledgeDocumentLogSaveReqVO kgKnowledgeDocumentLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kgKnowledgeDocumentLogDO 实体对象
     * @return KgKnowledgeDocumentLogRespVO
     */
     KgKnowledgeDocumentLogRespVO convertToRespVO(KgKnowledgeDocumentLogDO kgKnowledgeDocumentLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param kgKnowledgeDocumentLogDOList 实体对象列表
     * @return List<KgKnowledgeDocumentLogRespVO>
     */
     List<KgKnowledgeDocumentLogRespVO> convertToRespVOList(List<KgKnowledgeDocumentLogDO> kgKnowledgeDocumentLogDOList);
}
