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

package tech.qiantong.qknow.module.system.convert.message;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageTemplateRespVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageTemplateSaveReqVO;
import tech.qiantong.qknow.module.system.dal.dataobject.message.MessageTemplateDO;

import java.util.List;

/**
 * 消息模板 Convert
 *
 * @author qknow
 * @date 2024-10-31
 */
@Mapper
public interface MessageTemplateConvert {
    MessageTemplateConvert INSTANCE = Mappers.getMapper(MessageTemplateConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param messageTemplatePageReqVO 请求参数
     * @return MessageTemplateDO
     */
     MessageTemplateDO convertToDO(MessageTemplatePageReqVO messageTemplatePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param messageTemplateSaveReqVO 保存请求参数
     * @return MessageTemplateDO
     */
     MessageTemplateDO convertToDO(MessageTemplateSaveReqVO messageTemplateSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param messageTemplateDO 实体对象
     * @return MessageTemplateRespVO
     */
     MessageTemplateRespVO convertToRespVO(MessageTemplateDO messageTemplateDO);

    /**
     * DOList 转换为 RespVOList
     * @param messageTemplateDOList 实体对象列表
     * @return List<MessageTemplateRespVO>
     */
     List<MessageTemplateRespVO> convertToRespVOList(List<MessageTemplateDO> messageTemplateDOList);
}
