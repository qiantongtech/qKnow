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

package tech.qiantong.qknow.module.ext.convert.extRelationMapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMapping.ExtRelationMappingDO;

/**
 * 关系映射 Convert
 *
 * @author qknow
 * @date 2025-02-25
 */
@Mapper
public interface ExtRelationMappingConvert {
    ExtRelationMappingConvert INSTANCE = Mappers.getMapper(ExtRelationMappingConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extRelationMappingPageReqVO 请求参数
     * @return ExtRelationMappingDO
     */
     ExtRelationMappingDO convertToDO(ExtRelationMappingPageReqVO extRelationMappingPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extRelationMappingSaveReqVO 保存请求参数
     * @return ExtRelationMappingDO
     */
     ExtRelationMappingDO convertToDO(ExtRelationMappingSaveReqVO extRelationMappingSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extRelationMappingDO 实体对象
     * @return ExtRelationMappingRespVO
     */
     ExtRelationMappingRespVO convertToRespVO(ExtRelationMappingDO extRelationMappingDO);

    /**
     * DOList 转换为 RespVOList
     * @param extRelationMappingDOList 实体对象列表
     * @return List<ExtRelationMappingRespVO>
     */
     List<ExtRelationMappingRespVO> convertToRespVOList(List<ExtRelationMappingDO> extRelationMappingDOList);
}
