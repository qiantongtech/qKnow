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

package tech.qiantong.qknow.module.kb.service.flow.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.convert.flow.KbFlowNodeConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.mapper.flow.KbFlowNodeMapper;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowNodeService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * bot流程节点Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
public class KbFlowNodeServiceImpl extends ServiceImpl<KbFlowNodeMapper, KbFlowNodeDO> implements IKbFlowNodeService {

    /**
     * 根据 botId 查询流程节点
     *
     * @param botId botId
     * @return 流程节点列表
     */
    @Override
    public List<JSONObject> flowVOByBotId(Long botId) {
        LambdaQueryWrapper<KbFlowNodeDO> queryWrapper = Wrappers.<KbFlowNodeDO>lambdaQuery()
                .select(KbFlowNodeDO::getStyle)
                .eq(KbFlowNodeDO::getBotId, botId);
        List<KbFlowNodeDO> list = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        // 将 style 字段转为 JSONObject
        return list.stream()
                .map(item -> JSONObject.parseObject(item.getStyle()))
                .collect(Collectors.toList());
    }

    /**
     * 批量新增流程节点
     *
     * @param flowVO 流程对象
     * @return 操作是否成功
     */
    @Override
    public boolean submitBatch(KbFlowVO flowVO) {
        List<KbFlowNodeDO> saveList = KbFlowNodeConvert.toDOList(flowVO);
        // 先删除
        this.removeByBotId(flowVO.getBotId());
        return super.saveBatch(saveList);
    }

    /**
     * 根据 botId 删除流程节点
     *
     * @param botId botId
     */
    public void removeByBotId(Long botId) {
        baseMapper.delete(Wrappers.<KbFlowNodeDO>lambdaQuery().eq(KbFlowNodeDO::getBotId, botId));
    }

    /**
     * 根据 botId 查询流程节点
     *
     * @param botId botId
     * @return 流程节点列表
     */
    @Override
    public List<KbFlowNodeDO> listByBotId(Long botId) {
        LambdaQueryWrapper<KbFlowNodeDO> queryWrapper = Wrappers.<KbFlowNodeDO>lambdaQuery()
                .eq(KbFlowNodeDO::getBotId, botId);
        return super.list(queryWrapper);
    }
}
