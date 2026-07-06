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

package tech.qiantong.qknow.module.kb.service.codeNative.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.codeNative.vo.KbCodeNativeSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.codeNative.KbCodeNativeDO;
import tech.qiantong.qknow.module.kb.dal.mapper.codeNative.KbCodeNativeMapper;
import tech.qiantong.qknow.module.kb.service.codeNative.IKbCodeNativeService;

import java.util.Collection;
import java.util.List;

/**
 * 白盒化开发Service业务层处理
 *
 * @author qknow
 * @date 2026-04-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbCodeNativeServiceImpl  extends ServiceImpl<KbCodeNativeMapper,KbCodeNativeDO> implements IKbCodeNativeService {

    @Override
    public int removeKbCodeNative(Collection<Long> idList) {
        // 批量删除白盒化开发
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public KbCodeNativeDO getKbCodeNativeByBotId(Long botId) {
        LambdaQueryWrapper<KbCodeNativeDO> queryWrapper = Wrappers.<KbCodeNativeDO>lambdaQuery()
                .eq(KbCodeNativeDO::getBotId, botId);
        List<KbCodeNativeDO> doList = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(doList)){
            return new KbCodeNativeDO();
        }
        return doList.get(0);
    }

    /**
     * 提交白盒化开发数据
     *
     * @param kbCodeNative 白盒化开发数据
     * @return 操作是否成功
     */
    @Override
    public Boolean submit(KbCodeNativeSaveReqVO kbCodeNative) {
        LambdaQueryWrapper<KbCodeNativeDO> queryWrapper = Wrappers.<KbCodeNativeDO>lambdaQuery()
                .eq(KbCodeNativeDO::getBotId, kbCodeNative.getBotId());
        List<KbCodeNativeDO> doList = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(doList)){
            // 新增
            KbCodeNativeDO kbCodeNativeDO = BeanUtils.toBean(kbCodeNative, KbCodeNativeDO.class);
            return baseMapper.insert(kbCodeNativeDO) > 0;
        }
        KbCodeNativeDO kbCodeNativeDO = doList.get(0);
        kbCodeNativeDO.setCode(kbCodeNative.getCode());
        return baseMapper.updateById(kbCodeNativeDO) > 0;
    }
}
