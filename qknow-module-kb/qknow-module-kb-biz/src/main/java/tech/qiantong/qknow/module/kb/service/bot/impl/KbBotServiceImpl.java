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

package tech.qiantong.qknow.module.kb.service.bot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotSaveReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotTypeEnums;
import tech.qiantong.qknow.module.kb.dal.mapper.bot.KbBotMapper;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowEdgeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowNodeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowService;

import java.util.Collection;
import java.util.Objects;

/**
 * bot 管理Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbBotServiceImpl extends ServiceImpl<KbBotMapper, KbBotDO> implements IKbBotService {

    @Resource
    private IKbFlowService flowService;

    @Resource
    private IKbFlowNodeService flowNodeService;
    @Resource
    private IKbFlowEdgeService flowEdgeService;

    /**
     * 获得bot 管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return bot 管理分页列表
     */
    @Override
    public PageResult<KbBotDO> getKbBotPage(KbBotPageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    /**
     * 创建bot 管理
     *
     * @param createReqVO bot 管理信息
     * @return bot 管理编号
     */
    @Override
    public Long createKbBot(KbBotSaveReqVO createReqVO) {
        KbBotDO dictType = BeanUtils.toBean(createReqVO, KbBotDO.class);
        baseMapper.insert(dictType);
        return dictType.getId();
    }

    /**
     * 更新bot 管理
     *
     * @param updateReqVO bot 管理信息
     */
    @Override
    public int updateKbBot(KbBotSaveReqVO updateReqVO) {
        // 相关校验

        // 更新bot 管理
        KbBotDO updateObj = BeanUtils.toBean(updateReqVO, KbBotDO.class);
        return baseMapper.updateById(updateObj);
    }

    /**
     * 删除bot
     *
     * @param idList botId 集合
     */
    @Override
    public int removeKbBot(Collection<Long> idList) {
        int result = 0;
        // 批量删除bot 管理
        for (Long id : idList) {
            result += removeKbBot(id);
        }
        return result;
    }

    /**
     * 获得 bot
     *
     * @param id botId
     * @return bot
     */
    @Override
    public KbBotDO getKbBotById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 删除 bot
     *
     * @param id botId
     */
    private int removeKbBot(Long id) {
        // 首先根据id 获取 bot
        KbBotDO kbBotDO = baseMapper.selectById(id);
        if (kbBotDO.getBuiltinFlag().equals(1)){
            throw new ServiceException("内置 Bot，不允许删除");
        }
        // 如果是 chatFlow 或者 workflow 需要进一步删除流程数据
        if (Objects.equals(kbBotDO.getType(), BotTypeEnums.WORK_FLOW.getCode())
                || Objects.equals(kbBotDO.getType(), BotTypeEnums.CHAT_FLOW.getCode())) {
            flowService.deleteByBotId(kbBotDO.getId());
        }
        return baseMapper.deleteById(id);
    }

    /**
     * 复制bot 管理
     *
     * @param createReqVO bot 管理信息
     * @return bot 管理编号
     */
    @Override
    public Long copyKbBot(KbBotSaveReqVO createReqVO) {
        // 1. 获取【源Bot ID】
        Long sourceBotId = createReqVO.getId();

        // 2. 构建新Bot
        KbBotDO newBot = BeanUtils.toBean(createReqVO, KbBotDO.class);
        newBot.setId(null); // 关键：新Bot必须清空ID，让数据库自增
        newBot.setBuiltinFlag(0);
        baseMapper.insert(newBot);
        Long newBotId = newBot.getId(); // 拿到新生成的ID

        // 3. 查询【源Bot】的流程节点、连线
        KbFlowVO flowVO = new KbFlowVO();
        flowVO.setNodes(flowNodeService.flowVOByBotId(sourceBotId));
        flowVO.setEdges(flowEdgeService.flowVOByBotId(sourceBotId));

        // 4. 把流程绑定到【新Bot】
        flowVO.setBotId(newBotId);
        flowVO.setWorkspaceId(newBot.getWorkspaceId());
        // 5. 批量保存新流程
        flowNodeService.submitBatch(flowVO);
        flowEdgeService.submitBatch(flowVO);

        return newBotId;
    }


}
