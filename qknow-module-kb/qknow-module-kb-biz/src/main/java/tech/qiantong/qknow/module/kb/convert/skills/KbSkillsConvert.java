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

package tech.qiantong.qknow.module.kb.convert.skills;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;

import java.util.List;

/**
 * skills Convert
 *
 * @author qknow
 * @date 2026-06-17
 */
@Mapper
public interface KbSkillsConvert {
    KbSkillsConvert INSTANCE = Mappers.getMapper(KbSkillsConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kbSkillsPageReqVO 请求参数
     * @return KbSkillsDO
     */
     KbSkillsDO convertToDO(KbSkillsPageReqVO kbSkillsPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kbSkillsSaveReqVO 保存请求参数
     * @return KbSkillsDO
     */
     KbSkillsDO convertToDO(KbSkillsSaveReqVO kbSkillsSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kbSkillsDO 实体对象
     * @return KbSkillsRespVO
     */
     KbSkillsRespVO convertToRespVO(KbSkillsDO kbSkillsDO);

    /**
     * DOList 转换为 RespVOList
     * @param kbSkillsDOList 实体对象列表
     * @return List<KbSkillsRespVO>
     */
     List<KbSkillsRespVO> convertToRespVOList(List<KbSkillsDO> kbSkillsDOList);
}
