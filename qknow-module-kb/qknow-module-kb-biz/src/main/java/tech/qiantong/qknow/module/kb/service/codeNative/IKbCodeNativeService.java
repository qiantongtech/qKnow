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

package tech.qiantong.qknow.module.kb.service.codeNative;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.module.kb.controller.admin.codeNative.vo.KbCodeNativeSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.codeNative.KbCodeNativeDO;

import java.util.Collection;

/**
 * 白盒化开发Service接口
 *
 * @author qknow
 * @date 2026-04-09
 */
public interface IKbCodeNativeService extends IService<KbCodeNativeDO> {

    /**
     * 删除白盒化开发
     *
     * @param idList 白盒化开发编号
     */
    int removeKbCodeNative(Collection<Long> idList);

    /**
     * 获得白盒化开发详情
     *
     * @param botId 白盒化开发编号
     * @return 白盒化开发
     */
    KbCodeNativeDO getKbCodeNativeByBotId(Long botId);

    /**
     * 提交白盒化开发数据
     * @param kbCodeNative 白盒化开发数据
     * @return 操作是否成功
     */
    Boolean submit(KbCodeNativeSaveReqVO kbCodeNative);
}
