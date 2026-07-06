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
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeBasePageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeBaseRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeBaseSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;

import java.util.List;

/**
 * 知识库 Convert
 *
 * @author qknow
 * @date 2025-07-22
 */
@Mapper
public interface KmcKnowledgeBaseConvert {
    KmcKnowledgeBaseConvert INSTANCE = Mappers.getMapper(KmcKnowledgeBaseConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kmcKnowledgeBasePageReqVO 请求参数
     * @return KmcKnowledgeBaseDO
     */
     KmcKnowledgeBaseDO convertToDO(KmcKnowledgeBasePageReqVO kmcKnowledgeBasePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kmcKnowledgeBaseSaveReqVO 保存请求参数
     * @return KmcKnowledgeBaseDO
     */
     KmcKnowledgeBaseDO convertToDO(KmcKnowledgeBaseSaveReqVO kmcKnowledgeBaseSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kmcKnowledgeBaseDO 实体对象
     * @return KmcKnowledgeBaseRespVO
     */
     KmcKnowledgeBaseRespVO convertToRespVO(KmcKnowledgeBaseDO kmcKnowledgeBaseDO);

    /**
     * DOList 转换为 RespVOList
     * @param kmcKnowledgeBaseDOList 实体对象列表
     * @return List<KmcKnowledgeBaseRespVO>
     */
     List<KmcKnowledgeBaseRespVO> convertToRespVOList(List<KmcKnowledgeBaseDO> kmcKnowledgeBaseDOList);
}
