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

package tech.qiantong.qknow.module.kb.service.bot;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.domain.model.LoginUser;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeyPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeySaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotApikeyDO;

import java.util.Collection;

/**
 * bot访问密钥Service接口
 *
 * @author qknow
 * @date 2026-04-24
 */
public interface IKbBotApikeyService extends IService<KbBotApikeyDO> {

    /**
     * 获得bot访问密钥分页列表
     *
     * @param pageReqVO 分页请求
     * @return bot访问密钥分页列表
     */
    PageResult<KbBotApikeyDO> getKbBotApikeyPage(KbBotApikeyPageReqVO pageReqVO);

    /**
     * 删除bot访问密钥
     *
     * @param idList bot访问密钥编号
     */
    int removeKbBotApikey(Collection<Long> idList);

    /**
     * 生成bot访问密钥
     *
     * @param kbBotApikey bot访问密钥信息
     * @param currentUser 当前用户
     * @return 生成的bot访问密钥
     */
    Boolean generate(KbBotApikeySaveReqVO kbBotApikey, LoginUser currentUser);
}
