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

package tech.qiantong.qknow.module.ext.convert.extRelationMappingMiddle;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;

import java.util.List;

/**
 * 关系映射中间 Convert
 *
 * @author qknow
 * @date 2025-12-16
 */
@Mapper
public interface ExtRelationMappingMiddleConvert {
    ExtRelationMappingMiddleConvert INSTANCE = Mappers.getMapper(ExtRelationMappingMiddleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extRelationMappingMiddlePageReqVO 请求参数
     * @return ExtRelationMappingMiddleDO
     */
     ExtRelationMappingMiddleDO convertToDO(ExtRelationMappingMiddlePageReqVO extRelationMappingMiddlePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extRelationMappingMiddleSaveReqVO 保存请求参数
     * @return ExtRelationMappingMiddleDO
     */
     ExtRelationMappingMiddleDO convertToDO(ExtRelationMappingMiddleSaveReqVO extRelationMappingMiddleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extRelationMappingMiddleDO 实体对象
     * @return ExtRelationMappingMiddleRespVO
     */
     ExtRelationMappingMiddleRespVO convertToRespVO(ExtRelationMappingMiddleDO extRelationMappingMiddleDO);

    /**
     * DOList 转换为 RespVOList
     * @param extRelationMappingMiddleDOList 实体对象列表
     * @return List<ExtRelationMappingMiddleRespVO>
     */
     List<ExtRelationMappingMiddleRespVO> convertToRespVOList(List<ExtRelationMappingMiddleDO> extRelationMappingMiddleDOList);
}
