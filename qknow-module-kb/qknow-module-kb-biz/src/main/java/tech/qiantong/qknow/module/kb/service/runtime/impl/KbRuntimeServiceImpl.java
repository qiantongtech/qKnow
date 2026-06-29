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

package tech.qiantong.qknow.module.kb.service.runtime.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.RuntimeStatusEnums;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimePageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeDO;
import tech.qiantong.qknow.module.kb.dal.mapper.runtime.KbRuntimeMapper;
import tech.qiantong.qknow.module.kb.service.flow.bo.KbFlowBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.NodeRunResultBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.RuntimeContextBO;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeNodeService;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeService;

import java.util.Collection;

/**
 * bot运行Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
public class KbRuntimeServiceImpl extends ServiceImpl<KbRuntimeMapper, KbRuntimeDO> implements IKbRuntimeService {
    @Resource
    private IKbRuntimeNodeService nodeService;

    @Override
    public PageResult<KbRuntimeDO> getKbRuntimePage(KbRuntimePageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    @Override
    public int removeKbRuntime(Collection<Long> idList) {
        // 批量删除bot运行
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public KbRuntimeDO getKbRuntimeById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 创建运行时上下文
     *
     * @param flowBO 流程的 bo 对象
     * @param input  输入参数
     * @return 运行时上下文
     */
    @Override
    public RuntimeContextBO createRuntimeContext(KbFlowBO flowBO, JSONObject input) {
        KbRuntimeDO runtimeDO = new KbRuntimeDO();
        runtimeDO.setWorkspaceId(flowBO.getWorkspaceId());
        runtimeDO.setBotId(flowBO.getBotId());
        runtimeDO.setInput(input.toJSONString());
        runtimeDO.setStatus(RuntimeStatusEnums.RUNNING.getCode());

        return new RuntimeContextBO(runtimeDO, input);
    }

    /**
     * 创建并保存运行时上下文
     *
     * @param flowBO 流程的 bo 对象
     * @param input  输入参数
     * @return 运行时上下文
     */
    @Override
    public RuntimeContextBO createSaveRuntimeContext(KbFlowBO flowBO, JSONObject input) {
        KbRuntimeDO runtimeDO = new KbRuntimeDO();
        runtimeDO.setWorkspaceId(flowBO.getWorkspaceId());
        runtimeDO.setBotId(flowBO.getBotId());
        runtimeDO.setInput(input.toJSONString());
        runtimeDO.setStatus(RuntimeStatusEnums.RUNNING.getCode());
        baseMapper.insert(runtimeDO);

        return new RuntimeContextBO(runtimeDO, input);
    }

    /**
     * 保存运行成功的记录
     *
     * @param runtimeDO 运行时记录
     */
    @Override
    public void saveRunSuccess(KbRuntimeDO runtimeDO) {
        runtimeDO.setStatus(RuntimeStatusEnums.SUCCESS.getCode());
        // 将创建时间转成毫秒的时间戳
        long time = runtimeDO.getCreateTime().getTime();
        runtimeDO.setRuntime(System.currentTimeMillis() - time);
        baseMapper.saveRunSuccess(runtimeDO);
    }

    /**
     * 保存运行失败的记录
     *
     * @param runtimeDO 运行时记录
     */
    @Override
    public void saveRunError(KbRuntimeDO runtimeDO) {
        runtimeDO.setStatus(RuntimeStatusEnums.ERROR.getCode());
        // 将创建时间转成毫秒的时间戳
        long time = runtimeDO.getCreateTime().getTime();
        runtimeDO.setRuntime(System.currentTimeMillis() - time);
        baseMapper.updateById(runtimeDO);
    }

    /**
     * 保存节点运行状态
     *
     * @param nodeResult     节点执行结果
     * @param runtimeContext 流程的运行上下文
     */
    @Override
    public void saveRuntimeNode(NodeRunResultBO nodeResult, RuntimeContextBO runtimeContext) {
        KbRuntimeNodeDO runtimeNodeDO = new KbRuntimeNodeDO();
        runtimeNodeDO.setWorkspaceId(runtimeContext.getRuntimeDO().getWorkspaceId());
        runtimeNodeDO.setRuntimeId(runtimeContext.getRuntimeDO().getId());
        runtimeNodeDO.setNodeUuid(nodeResult.getNodeUuid());
        runtimeNodeDO.setStep(nodeResult.getStep());
        runtimeNodeDO.setInput(JSONObject.toJSONString(nodeResult.getInput()));
        runtimeNodeDO.setOutput(JSONObject.toJSONString(nodeResult.getOutput()));
        runtimeNodeDO.setStatus(nodeResult.getStatus());
        nodeService.save(runtimeNodeDO);
    }
}
