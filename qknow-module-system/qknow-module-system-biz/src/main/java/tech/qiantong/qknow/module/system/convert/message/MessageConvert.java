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

import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageRespVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qknow.module.system.dal.dataobject.message.MessageDO;

import java.util.List;

/**
 * 消息 Convert
 *
 * @author qknow
 * @date 2024-10-31
 */
public final class MessageConvert {
    public static final MessageConvert INSTANCE = new MessageConvert();

    private MessageConvert() {
    }

    /**
     * PageReqVO 转换为 DO
     * @param messagePageReqVO 请求参数
     * @return MessageDO
     */
    public MessageDO convertToDO(MessagePageReqVO messagePageReqVO) {
        if (messagePageReqVO == null) {
            return null;
        }
        return MessageDO.builder()
                .id(messagePageReqVO.getId())
                .senderId(messagePageReqVO.getSenderId())
                .receiverId(messagePageReqVO.getReceiverId())
                .title(messagePageReqVO.getTitle())
                .content(messagePageReqVO.getContent())
                .category(messagePageReqVO.getCategory())
                .msgLevel(messagePageReqVO.getMsgLevel())
                .module(messagePageReqVO.getModule())
                .entityType(messagePageReqVO.getEntityType())
                .entityId(messagePageReqVO.getEntityId())
                .entityUrl(messagePageReqVO.getEntityUrl())
                .hasRead(messagePageReqVO.getHasRead())
                .hasRetraction(messagePageReqVO.getHasRetraction())
                .validFlag(messagePageReqVO.getValidFlag())
                .delFlag(messagePageReqVO.getDelFlag())
                .build();
    }

    /**
     * SaveReqVO 转换为 DO
     * @param messageSaveReqVO 保存请求参数
     * @return MessageDO
     */
    public MessageDO convertToDO(MessageSaveReqVO messageSaveReqVO) {
        if (messageSaveReqVO == null) {
            return null;
        }
        return MessageDO.builder()
                .id(messageSaveReqVO.getId() == null ? null : Long.parseLong(messageSaveReqVO.getId()))
                .senderId(messageSaveReqVO.getSenderId())
                .receiverId(messageSaveReqVO.getReceiverId())
                .module(messageSaveReqVO.getModule())
                .entityType(messageSaveReqVO.getEntityType())
                .entityId(messageSaveReqVO.getEntityId())
                .entityUrl(messageSaveReqVO.getEntityUrl())
                .hasRead(messageSaveReqVO.getHasRead())
                .delFlag(messageSaveReqVO.getDelFlag())
                .build();
    }

    /**
     * DO 转换为 RespVO
     * @param messageDO 实体对象
     * @return MessageRespVO
     */
    public MessageRespVO convertToRespVO(MessageDO messageDO) {
        if (messageDO == null) {
            return null;
        }
        MessageRespVO response = new MessageRespVO();
        response.setId(messageDO.getId());
        response.setSenderId(messageDO.getSenderId());
        response.setReceiverId(messageDO.getReceiverId());
        response.setTitle(messageDO.getTitle());
        response.setContent(messageDO.getContent());
        response.setCategory(messageDO.getCategory());
        response.setMsgLevel(messageDO.getMsgLevel());
        response.setModule(messageDO.getModule());
        response.setEntityType(messageDO.getEntityType());
        response.setEntityId(messageDO.getEntityId());
        response.setEntityUrl(messageDO.getEntityUrl());
        response.setHasRead(messageDO.getHasRead());
        response.setHasRetraction(messageDO.getHasRetraction());
        response.setValidFlag(messageDO.getValidFlag());
        response.setDelFlag(messageDO.getDelFlag());
        response.setCreateBy(messageDO.getCreateBy());
        response.setCreatorId(messageDO.getCreatorId());
        response.setCreateTime(messageDO.getCreateTime());
        response.setUpdateBy(messageDO.getUpdateBy());
        response.setUpdateTime(messageDO.getUpdateTime());
        response.setRemark(messageDO.getRemark());
        return response;
    }

    /**
     * DOList 转换为 RespVOList
     * @param messageDOList 实体对象列表
     * @return List<MessageRespVO>
     */
    public List<MessageRespVO> convertToRespVOList(List<MessageDO> messageDOList) {
        if (messageDOList == null) {
            return null;
        }
        return messageDOList.stream().map(this::convertToRespVO).toList();
    }
}
