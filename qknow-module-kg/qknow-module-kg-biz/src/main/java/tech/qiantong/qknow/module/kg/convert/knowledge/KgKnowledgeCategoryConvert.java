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
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategorySaveReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeCategoryDO;

/**
 * 知识分类 Convert
 *
 * @author qknow
 * @date 2025-10-20
 */
@Mapper
public interface KgKnowledgeCategoryConvert {
    KgKnowledgeCategoryConvert INSTANCE = Mappers.getMapper(KgKnowledgeCategoryConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kgKnowledgeCategoryPageReqVO 请求参数
     * @return KgKnowledgeCategoryDO
     */
     KgKnowledgeCategoryDO convertToDO(KgKnowledgeCategoryPageReqVO kgKnowledgeCategoryPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kgKnowledgeCategorySaveReqVO 保存请求参数
     * @return KgKnowledgeCategoryDO
     */
     KgKnowledgeCategoryDO convertToDO(KgKnowledgeCategorySaveReqVO kgKnowledgeCategorySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kgKnowledgeCategoryDO 实体对象
     * @return KgKnowledgeCategoryRespVO
     */
     KgKnowledgeCategoryRespVO convertToRespVO(KgKnowledgeCategoryDO kgKnowledgeCategoryDO);

    /**
     * DOList 转换为 RespVOList
     * @param kgKnowledgeCategoryDOList 实体对象列表
     * @return List<KgKnowledgeCategoryRespVO>
     */
     List<KgKnowledgeCategoryRespVO> convertToRespVOList(List<KgKnowledgeCategoryDO> kgKnowledgeCategoryDOList);
}
