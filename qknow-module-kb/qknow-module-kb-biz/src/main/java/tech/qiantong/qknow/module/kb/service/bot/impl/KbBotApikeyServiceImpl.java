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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.domain.model.LoginUser;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeyPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeySaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotApikeyDO;
import tech.qiantong.qknow.module.kb.dal.mapper.bot.KbBotApikeyMapper;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotApikeyService;

import java.util.Collection;
import java.util.UUID;

/**
 * bot访问密钥Service业务层处理
 *
 * @author qknow
 * @date 2026-04-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbBotApikeyServiceImpl extends ServiceImpl<KbBotApikeyMapper, KbBotApikeyDO> implements IKbBotApikeyService {
    @Resource
    private KbBotApikeyMapper kbBotApikeyMapper;

    private static final String API_KEY_FORMAT = "bot-{}";

    @Override
    public PageResult<KbBotApikeyDO> getKbBotApikeyPage(KbBotApikeyPageReqVO pageReqVO) {
        return kbBotApikeyMapper.selectPage(pageReqVO);
    }

    @Override
    public int removeKbBotApikey(Collection<Long> idList) {
        // 批量删除bot访问密钥
        return kbBotApikeyMapper.deleteByIds(idList);
    }

    /**
     * 生成bot访问密钥
     *
     * @param kbBotApikey bot访问密钥信息
     * @param currentUser 当前用户
     * @return 生成的bot访问密钥
     */
    @Override
    public Boolean generate(KbBotApikeySaveReqVO kbBotApikey, LoginUser currentUser) {
        KbBotApikeyDO kbBotApikeyDO = BeanUtils.toBean(kbBotApikey, KbBotApikeyDO.class);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        kbBotApikeyDO.setApiKey(StrUtil.format(API_KEY_FORMAT,uuid));
        return baseMapper.insert(kbBotApikeyDO) > 0;
    }

}
