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
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;

import java.util.Collection;

/**
 * bot 管理Service接口
 *
 * @author qknow
 * @date 2026-03-18
 */
public interface IKbBotService extends IService<KbBotDO> {

    /**
     * 获得bot 管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return bot 管理分页列表
     */
    PageResult<KbBotDO> getKbBotPage(KbBotPageReqVO pageReqVO);

    /**
     * 创建bot 管理
     *
     * @param createReqVO bot 管理信息
     * @return bot 管理编号
     */
    Long createKbBot(KbBotSaveReqVO createReqVO);

    /**
     * 更新bot 管理
     *
     * @param updateReqVO bot 管理信息
     */
    int updateKbBot(KbBotSaveReqVO updateReqVO);

    /**
     * 删除bot 管理
     *
     * @param idList bot 管理编号
     */
    int removeKbBot(Collection<Long> idList);

    /**
     * 获得bot 管理详情
     *
     * @param id bot 管理编号
     * @return bot 管理
     */
    KbBotDO getKbBotById(Long id);


    /**
     * 复制bot 管理
     *
     * @param createReqVO bot 管理信息
     * @return bot 管理编号
     */
    Long copyKbBot(KbBotSaveReqVO createReqVO);
}
