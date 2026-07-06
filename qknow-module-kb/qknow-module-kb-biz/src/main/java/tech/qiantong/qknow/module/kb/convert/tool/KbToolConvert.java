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

package tech.qiantong.qknow.module.kb.convert.tool;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;

/**
 * 工具管理 Convert
 *
 * @author qknow
 * @date 2026-03-19
 */
@Mapper
public interface KbToolConvert {
    KbToolConvert INSTANCE = Mappers.getMapper(KbToolConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kbToolPageReqVO 请求参数
     * @return KbToolDO
     */
     KbToolDO convertToDO(KbToolPageReqVO kbToolPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kbToolSaveReqVO 保存请求参数
     * @return KbToolDO
     */
     KbToolDO convertToDO(KbToolSaveReqVO kbToolSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kbToolDO 实体对象
     * @return KbToolRespVO
     */
     KbToolRespVO convertToRespVO(KbToolDO kbToolDO);

    /**
     * DOList 转换为 RespVOList
     * @param kbToolDOList 实体对象列表
     * @return List<KbToolRespVO>
     */
     List<KbToolRespVO> convertToRespVOList(List<KbToolDO> kbToolDOList);
}
