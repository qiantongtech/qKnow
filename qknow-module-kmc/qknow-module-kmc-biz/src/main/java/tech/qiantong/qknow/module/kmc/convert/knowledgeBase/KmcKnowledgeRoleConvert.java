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
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRolePageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRoleRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRoleSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRoleDO;

import java.util.List;

/**
 * 知识库角色关联 Convert
 *
 * @author qknow
 * @date 2025-07-24
 */
@Mapper
public interface KmcKnowledgeRoleConvert {
    KmcKnowledgeRoleConvert INSTANCE = Mappers.getMapper(KmcKnowledgeRoleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kmcKnowledgeRolePageReqVO 请求参数
     * @return KmcKnowledgeRoleDO
     */
     KmcKnowledgeRoleDO convertToDO(KmcKnowledgeRolePageReqVO kmcKnowledgeRolePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kmcKnowledgeRoleSaveReqVO 保存请求参数
     * @return KmcKnowledgeRoleDO
     */
     KmcKnowledgeRoleDO convertToDO(KmcKnowledgeRoleSaveReqVO kmcKnowledgeRoleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kmcKnowledgeRoleDO 实体对象
     * @return KmcKnowledgeRoleRespVO
     */
     KmcKnowledgeRoleRespVO convertToRespVO(KmcKnowledgeRoleDO kmcKnowledgeRoleDO);

    /**
     * DOList 转换为 RespVOList
     * @param kmcKnowledgeRoleDOList 实体对象列表
     * @return List<KmcKnowledgeRoleRespVO>
     */
     List<KmcKnowledgeRoleRespVO> convertToRespVOList(List<KmcKnowledgeRoleDO> kmcKnowledgeRoleDOList);
}
