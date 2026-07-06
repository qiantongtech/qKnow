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

package tech.qiantong.qknow.module.kmc.convert.knowledgeBase;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRecallLogDO;

import java.util.List;

/**
 * 召回记录 Convert
 *
 * @author qknow
 * @date 2025-07-24
 */
@Mapper
public interface KmcKnowledgeRecallLogConvert {
    KmcKnowledgeRecallLogConvert INSTANCE = Mappers.getMapper(KmcKnowledgeRecallLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kmcKnowledgeRecallLogPageReqVO 请求参数
     * @return KmcKnowledgeRecallLogDO
     */
     KmcKnowledgeRecallLogDO convertToDO(KmcKnowledgeRecallLogPageReqVO kmcKnowledgeRecallLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kmcKnowledgeRecallLogSaveReqVO 保存请求参数
     * @return KmcKnowledgeRecallLogDO
     */
     KmcKnowledgeRecallLogDO convertToDO(KmcKnowledgeRecallLogSaveReqVO kmcKnowledgeRecallLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kmcKnowledgeRecallLogDO 实体对象
     * @return KmcKnowledgeRecallLogRespVO
     */
     KmcKnowledgeRecallLogRespVO convertToRespVO(KmcKnowledgeRecallLogDO kmcKnowledgeRecallLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param kmcKnowledgeRecallLogDOList 实体对象列表
     * @return List<KmcKnowledgeRecallLogRespVO>
     */
     List<KmcKnowledgeRecallLogRespVO> convertToRespVOList(List<KmcKnowledgeRecallLogDO> kmcKnowledgeRecallLogDOList);
}
