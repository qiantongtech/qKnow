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

package tech.qiantong.qknow.module.kb.service.flow;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;

import java.util.List;

/**
 * bot流程关系Service接口
 *
 * @author qknow
 * @date 2026-03-18
 */
public interface IKbFlowEdgeService extends IService<KbFlowEdgeDO> {

    /**
     * 根据 botId 查询流程关系
     *
     * @param botId botId
     * @return 流程关系列表
     */
    List<JSONObject> flowVOByBotId(Long botId);

    /**
     * 批量新增流程关系
     *
     * @param flowVO 流程对象
     * @return 操作是否成功
     */
    boolean submitBatch(KbFlowVO flowVO);

    /**
     * 根据 botId 删除流程关系
     *
     * @param botId botId
     */
    void removeByBotId(Long botId);

    /**
     * 根据 botId 查询流程关系
     * @param botId botId
     * @return 流程关系列表
     */
    List<KbFlowEdgeDO> listByBotId(Long botId);
}
