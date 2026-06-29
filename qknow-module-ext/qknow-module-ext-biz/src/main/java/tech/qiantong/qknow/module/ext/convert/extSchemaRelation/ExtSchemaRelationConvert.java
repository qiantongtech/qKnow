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

package tech.qiantong.qknow.module.ext.convert.extSchemaRelation;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;

/**
 * 关系配置 Convert
 *
 * @author qknow
 * @date 2025-02-18
 */
@Mapper
public interface ExtSchemaRelationConvert {
    ExtSchemaRelationConvert INSTANCE = Mappers.getMapper(ExtSchemaRelationConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extSchemaRelationPageReqVO 请求参数
     * @return ExtSchemaRelationDO
     */
    ExtSchemaRelationDO convertToDO(ExtSchemaRelationPageReqVO extSchemaRelationPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extSchemaRelationSaveReqVO 保存请求参数
     * @return ExtSchemaRelationDO
     */
    ExtSchemaRelationDO convertToDO(ExtSchemaRelationSaveReqVO extSchemaRelationSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extSchemaRelationDO 实体对象
     * @return ExtSchemaRelationRespVO
     */
    ExtSchemaRelationRespVO convertToRespVO(ExtSchemaRelationDO extSchemaRelationDO);

    /**
     * DOList 转换为 RespVOList
     * @param extSchemaRelationDOList 实体对象列表
     * @return List<ExtSchemaRelationRespVO>
     */
    List<ExtSchemaRelationRespVO> convertToRespVOList(List<ExtSchemaRelationDO> extSchemaRelationDOList);
}
