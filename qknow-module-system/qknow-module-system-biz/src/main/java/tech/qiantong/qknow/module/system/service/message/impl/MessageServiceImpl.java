/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.system.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.exception.base.BaseException;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.websocket.WebSocketMessageServer;
import tech.qiantong.qknow.module.system.convert.message.MessageConvert;
import tech.qiantong.qknow.module.system.dal.dataobject.message.MessageDO;
import tech.qiantong.qknow.module.system.dal.dataobject.message.MessageTemplateDO;
import tech.qiantong.qknow.module.system.dal.dataobject.message.enums.MessageHasReadEnums;
import tech.qiantong.qknow.module.system.dal.mapper.message.MessageMapper;
import tech.qiantong.qknow.module.system.dal.mapper.message.MessageTemplateMapper;
import tech.qiantong.qknow.module.system.service.message.IMessageService;

import javax.annotation.Resource;
import java.util.Map;

import static tech.qiantong.qknow.common.utils.SecurityUtils.getLoginUser;

/**
 * 消息Service业务层处理
 *
 * @author qknow
 * @date 2024-10-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageServiceImpl  extends ServiceImpl<MessageMapper, MessageDO> implements IMessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private MessageTemplateMapper messageTemplateMapper;

    /**
     * 通过模版向某一个用户发送消息
     * @param templateId 模版id
     * @param messageSaveReqVO 消息创建
     * @param entity 实体对象
     * @return 是否发送成功
     */
    @Override
    public Boolean send(Long templateId, MessageSaveReqVO messageSaveReqVO, Object entity) {
        MessageTemplateDO messageTemplateDO = messageTemplateMapper.selectById(templateId); // 获取对应的模版
        if (messageTemplateDO == null) {
            throw new BaseException("消息模版不存在");
        }
        Map<?, ?> map = BeanUtils.toBean(entity, Map.class); // 将实体转换为键值对
        // 消息模版更新数值
        String message = messageTemplateDO.getContent();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            message = message.replace("${" + entry.getKey().toString() + "}", entry.getValue().toString());
        }
        MessageDO messageDO = BeanUtils.toBean(messageSaveReqVO, MessageDO.class);
        // 设置模版基本数据
        messageDO.setCategory(messageTemplateDO.getCategory());
        messageDO.setMsgLevel(messageTemplateDO.getMsgLevel());
        messageDO.setTitle(messageTemplateDO.getTitle());
        // 实际消息
        messageDO.setContent(message);

        messageDO.setCreatorId(getLoginUser().getUserId());
        messageDO.setCreateBy(getLoginUser().getUser().getNickName());
        boolean save = this.save(messageDO);
        // 更新消息
        this.getReceiverWDNum(messageSaveReqVO.getReceiverId());
        return save;
    }

    /**
     * 查询消息数量
     * @param message 查询条件
     * @return 数量
     */
    @Override
    public Long getNum(MessagePageReqVO message) {
        message.setDelFlag(1);
        QueryWrapper<MessageDO> queryWrapper = new QueryWrapper<>(MessageConvert.INSTANCE.convertToDO(message));
        Long count = this.count(queryWrapper);
        WebSocketMessageServer.sendMessage(message.getReceiverId().toString(), count.toString()); // 消息更新
        return count;
    }

    /**
     * 设置已读
     * @param id 消息id
     * @return 是否成功
     */
    @Override
    public Boolean read(Long id) {
        MessageDO messageDO = new MessageDO();
        messageDO.setId(id);
        messageDO.setHasRead(MessageHasReadEnums.YD.code);
        boolean b = this.updateById(messageDO);
        // 更新消息
        this.getReceiverWDNum(getLoginUser().getUserId());
        return b;
    }

    /**
     * 全部已读
     * @param receiverId 接收人id
     * @param category 消息类型
     * @param module 消息模块
     * @return 是否成功
     */
    @Override
    public Boolean readAll(Long receiverId, Integer category, Integer module) {
        LambdaUpdateWrapper<MessageDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MessageDO::getReceiverId, receiverId);
        if (category != null) {
            updateWrapper.eq(MessageDO::getCategory, category);
        }
        if (module != null) {
            updateWrapper.eq(MessageDO::getModule, module);
        }
        updateWrapper.set(MessageDO::getHasRead, MessageHasReadEnums.YD.code);
        boolean update = this.update(updateWrapper);
        // 更新消息
        this.getReceiverWDNum(getLoginUser().getUserId());
        return update;
    }

    /**
     * 更新接收人未读消息
     *
     * @param receiverId 接收人id
     */
    @Override
    public void getReceiverWDNum(Long receiverId) {
        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
        messagePageReqVO.setHasRead(MessageHasReadEnums.WD.code);
        messagePageReqVO.setReceiverId(receiverId);
        this.getNum(messagePageReqVO);
    }
}
