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

package tech.qiantong.qknow.module.kb.convert.runtime;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimePageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeDO;

import java.util.List;

/**
 * bot运行 Convert
 *
 * @author qknow
 * @date 2026-03-18
 */
@Mapper
public interface KbRuntimeConvert {
    KbRuntimeConvert INSTANCE = Mappers.getMapper(KbRuntimeConvert.class);

    /**
     * PageReqVO 转换为 DO
     *
     * @param kbRuntimePageReqVO 请求参数
     * @return KbRuntimeDO
     */
    KbRuntimeDO convertToDO(KbRuntimePageReqVO kbRuntimePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     *
     * @param kbRuntimeSaveReqVO 保存请求参数
     * @return KbRuntimeDO
     */
    KbRuntimeDO convertToDO(KbRuntimeSaveReqVO kbRuntimeSaveReqVO);

    /**
     * DO 转换为 RespVO
     *
     * @param kbRuntimeDO 实体对象
     * @return KbRuntimeRespVO
     */
    KbRuntimeRespVO convertToRespVO(KbRuntimeDO kbRuntimeDO);

    /**
     * DOList 转换为 RespVOList
     *
     * @param kbRuntimeDOList 实体对象列表
     * @return List<KbRuntimeRespVO>
     */
    List<KbRuntimeRespVO> convertToRespVOList(List<KbRuntimeDO> kbRuntimeDOList);
}
