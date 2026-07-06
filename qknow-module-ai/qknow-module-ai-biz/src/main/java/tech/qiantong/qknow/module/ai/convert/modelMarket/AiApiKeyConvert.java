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

package tech.qiantong.qknow.module.ai.convert.modelMarket;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyPageReqVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyRespVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeySaveReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;

/**
 * API秘钥 Convert
 *
 * @author qknow
 * @date 2025-12-23
 */
@Mapper
public interface AiApiKeyConvert {
    AiApiKeyConvert INSTANCE = Mappers.getMapper(AiApiKeyConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param aiApiKeyPageReqVO 请求参数
     * @return AiApiKeyDO
     */
     AiApiKeyDO convertToDO(AiApiKeyPageReqVO aiApiKeyPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param aiApiKeySaveReqVO 保存请求参数
     * @return AiApiKeyDO
     */
     AiApiKeyDO convertToDO(AiApiKeySaveReqVO aiApiKeySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param aiApiKeyDO 实体对象
     * @return AiApiKeyRespVO
     */
     AiApiKeyRespVO convertToRespVO(AiApiKeyDO aiApiKeyDO);

    /**
     * DOList 转换为 RespVOList
     * @param aiApiKeyDOList 实体对象列表
     * @return List<AiApiKeyRespVO>
     */
     List<AiApiKeyRespVO> convertToRespVOList(List<AiApiKeyDO> aiApiKeyDOList);
}
