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

package tech.qiantong.qknow.module.kb.convert.agent;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.KbAgentConfigPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.KbAgentConfigRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.KbAgentConfigSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.agent.KbAgentConfigDO;

import java.util.List;

/**
 * agent配置 Convert
 *
 * @author qknow
 * @date 2026-03-19
 */
@Mapper
public interface KbAgentConfigConvert {
    KbAgentConfigConvert INSTANCE = Mappers.getMapper(KbAgentConfigConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kbAgentConfigPageReqVO 请求参数
     * @return KbAgentConfigDO
     */
     KbAgentConfigDO convertToDO(KbAgentConfigPageReqVO kbAgentConfigPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kbAgentConfigSaveReqVO 保存请求参数
     * @return KbAgentConfigDO
     */
     KbAgentConfigDO convertToDO(KbAgentConfigSaveReqVO kbAgentConfigSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kbAgentConfigDO 实体对象
     * @return KbAgentConfigRespVO
     */
     KbAgentConfigRespVO convertToRespVO(KbAgentConfigDO kbAgentConfigDO);

    /**
     * DOList 转换为 RespVOList
     * @param kbAgentConfigDOList 实体对象列表
     * @return List<KbAgentConfigRespVO>
     */
     List<KbAgentConfigRespVO> convertToRespVOList(List<KbAgentConfigDO> kbAgentConfigDOList);
}
