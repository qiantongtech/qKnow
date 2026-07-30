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

package tech.qiantong.qknow.module.kb.service.mcp.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpToolPageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpToolDO;
import tech.qiantong.qknow.module.kb.dal.mapper.mcp.KbMcpToolMapper;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpToolService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * mcp 工具Service业务层处理
 *
 * @author qknow
 * @date 2026-06-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbMcpToolServiceImpl extends ServiceImpl<KbMcpToolMapper, KbMcpToolDO> implements IKbMcpToolService {

    @Override
    public PageResult<KbMcpToolDO> getKbMcpToolPage(KbMcpToolPageReqVO pageReqVO) {
        if (Objects.isNull(pageReqVO.getMcpId())) {
            return new PageResult<>();
        }
        return baseMapper.selectPage(pageReqVO);
    }

    @Override
    public KbMcpToolDO getKbMcpToolById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 同步工具
     *
     * @param mcpId           mcpId
     * @param listToolsResult 工具列表
     * @return Boolean
     */
    @Override
    public Boolean syncTool(Long mcpId, McpSchema.ListToolsResult listToolsResult) {
        List<McpSchema.Tool> toolList = listToolsResult.tools();
        if (CollUtil.isEmpty(toolList)) {
            return true;
        }
        super.remove(Wrappers.<KbMcpToolDO>lambdaQuery().eq(KbMcpToolDO::getMcpId, mcpId));
        List<KbMcpToolDO> kbMcpToolDOList = new ArrayList<>(toolList.size());
        for (McpSchema.Tool tool : toolList) {
            KbMcpToolDO kbMcpToolDO = new KbMcpToolDO();
            kbMcpToolDO.setMcpId(mcpId);
            kbMcpToolDO.setName(tool.name());
            kbMcpToolDO.setDescription(tool.description());
            kbMcpToolDOList.add(kbMcpToolDO);
        }
        return super.saveBatch(kbMcpToolDOList);
    }
}
