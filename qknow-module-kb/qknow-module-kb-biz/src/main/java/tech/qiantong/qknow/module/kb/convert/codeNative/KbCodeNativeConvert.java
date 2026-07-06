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

package tech.qiantong.qknow.module.kb.convert.codeNative;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kb.controller.admin.codeNative.vo.KbCodeNativePageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.codeNative.vo.KbCodeNativeRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.codeNative.vo.KbCodeNativeSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.codeNative.KbCodeNativeDO;

/**
 * 白盒化开发 Convert
 *
 * @author qknow
 * @date 2026-04-09
 */
@Mapper
public interface KbCodeNativeConvert {
    KbCodeNativeConvert INSTANCE = Mappers.getMapper(KbCodeNativeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kbCodeNativePageReqVO 请求参数
     * @return KbCodeNativeDO
     */
     KbCodeNativeDO convertToDO(KbCodeNativePageReqVO kbCodeNativePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kbCodeNativeSaveReqVO 保存请求参数
     * @return KbCodeNativeDO
     */
     KbCodeNativeDO convertToDO(KbCodeNativeSaveReqVO kbCodeNativeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kbCodeNativeDO 实体对象
     * @return KbCodeNativeRespVO
     */
     KbCodeNativeRespVO convertToRespVO(KbCodeNativeDO kbCodeNativeDO);

    /**
     * DOList 转换为 RespVOList
     * @param kbCodeNativeDOList 实体对象列表
     * @return List<KbCodeNativeRespVO>
     */
     List<KbCodeNativeRespVO> convertToRespVOList(List<KbCodeNativeDO> kbCodeNativeDOList);
}
