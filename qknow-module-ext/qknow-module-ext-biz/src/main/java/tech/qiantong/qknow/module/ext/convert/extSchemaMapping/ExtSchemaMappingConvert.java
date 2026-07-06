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

package tech.qiantong.qknow.module.ext.convert.extSchemaMapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;

/**
 * 概念映射 Convert
 *
 * @author qknow
 * @date 2025-02-25
 */
@Mapper
public interface ExtSchemaMappingConvert {
    ExtSchemaMappingConvert INSTANCE = Mappers.getMapper(ExtSchemaMappingConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extSchemaMappingPageReqVO 请求参数
     * @return ExtSchemaMappingDO
     */
     ExtSchemaMappingDO convertToDO(ExtSchemaMappingPageReqVO extSchemaMappingPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extSchemaMappingSaveReqVO 保存请求参数
     * @return ExtSchemaMappingDO
     */
     ExtSchemaMappingDO convertToDO(ExtSchemaMappingSaveReqVO extSchemaMappingSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extSchemaMappingDO 实体对象
     * @return ExtSchemaMappingRespVO
     */
     ExtSchemaMappingRespVO convertToRespVO(ExtSchemaMappingDO extSchemaMappingDO);

    /**
     * DOList 转换为 RespVOList
     * @param extSchemaMappingDOList 实体对象列表
     * @return List<ExtSchemaMappingRespVO>
     */
     List<ExtSchemaMappingRespVO> convertToRespVOList(List<ExtSchemaMappingDO> extSchemaMappingDOList);
}
