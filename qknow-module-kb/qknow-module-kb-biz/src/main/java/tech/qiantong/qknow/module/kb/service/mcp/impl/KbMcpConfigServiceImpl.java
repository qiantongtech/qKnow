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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpConfigDO;
import tech.qiantong.qknow.module.kb.dal.mapper.mcp.KbMcpConfigMapper;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpConfigService;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpToolService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mcp 配置Service业务层处理
 *
 * @author qknow
 * @date 2026-06-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbMcpConfigServiceImpl extends ServiceImpl<KbMcpConfigMapper, KbMcpConfigDO> implements IKbMcpConfigService {

    @Resource
    private IKbMcpToolService mcpToolService;

    /**
     * 获得mcp 配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return mcp 配置分页列表
     */
    @Override
    public PageResult<KbMcpConfigDO> getKbMcpConfigPage(KbMcpConfigPageReqVO pageReqVO) {

        return baseMapper.selectPage(pageReqVO);
    }

    /**
     * 创建mcp 配置
     *
     * @param createReqVO mcp 配置信息
     * @return mcp 配置编号
     */
    @Override
    public Long createKbMcpConfig(KbMcpConfigSaveReqVO createReqVO) {
        KbMcpConfigDO dictType = BeanUtils.toBean(createReqVO, KbMcpConfigDO.class);
        baseMapper.insert(dictType);
        return dictType.getId();
    }

    /**
     * 更新mcp 配置
     *
     * @param updateReqVO mcp 配置信息
     */
    @Override
    public int updateKbMcpConfig(KbMcpConfigSaveReqVO updateReqVO) {
        // 相关校验

        // 更新mcp 配置
        KbMcpConfigDO kbMcpConfigDO = BeanUtils.toBean(updateReqVO, KbMcpConfigDO.class);
//        if (!this.syncMcpTool(kbMcpConfigDO)) {
//            return 0;
//        }
        return baseMapper.updateById(kbMcpConfigDO);
    }

    @Override
    public int removeKbMcpConfig(Collection<Long> idList) {
        // 批量删除mcp 配置
        // 判断状态是否是已启动状态
        LambdaQueryWrapper<KbMcpConfigDO> queryWrapper = Wrappers.<KbMcpConfigDO>lambdaQuery()
                .in(KbMcpConfigDO::getId, idList)
                .eq(KbMcpConfigDO::getStatus, 1);
        if (count(queryWrapper) > 0){
            throw new ServiceException("请先停止 MCP 配置");
        }
        return baseMapper.deleteByIds(idList);
    }

    /**
     * 删除mcp 配置
     *
     * @param id mcp 配置编号
     */
    @Override
    public KbMcpConfigDO getKbMcpConfigById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 更新mcp状态
     *
     * @param mcpId     mcpId
     * @param newStatus 新状态
     * @return 操作是否成功
     */
    @Override
    public Boolean updateMcpStatus(Long mcpId, Integer newStatus) {
        LambdaUpdateWrapper<KbMcpConfigDO> updateWrapper = Wrappers.<KbMcpConfigDO>lambdaUpdate()
                .eq(KbMcpConfigDO::getId, mcpId)
                .set(KbMcpConfigDO::getStatus, newStatus);
        return super.update(updateWrapper);
    }

    /**
     * 同步mcp工具列表
     *
     * @param mcpId mcpId
     * @return 操作是否成功
     */
    @Override
    public Boolean syncMcpTool(Long mcpId) {
        KbMcpConfigDO kbMcpConfigDO = baseMapper.selectById(mcpId);
        return this.syncMcpTool(kbMcpConfigDO);
    }

    /**
     * 同步mcp工具列表
     *
     * @param kbMcpConfigDO 配置实体
     * @return 操作是否成功
     */
    private Boolean syncMcpTool(KbMcpConfigDO kbMcpConfigDO) {
        McpClientTransport httpTransport = this.getMcpClientTransport(kbMcpConfigDO);
        McpSyncClient mcpSyncClient = McpClient.sync(httpTransport).build();
        mcpSyncClient.initialize();
        McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
        // 修改工具数量字段
        updateToolNum(kbMcpConfigDO.getId(), listToolsResult.tools().size());
        return mcpToolService.syncTool(kbMcpConfigDO.getId(), listToolsResult);
    }

    /**
     * 获取mcp回调提供者(异步，流式接口使用)
     *
     * @param mcpIds mcpIds, 多个用逗号分隔
     * @return mcp回调提供者
     */
    @Override
    public ToolCallbackProvider getMcpAsyncCallbackProvider(String mcpIds) {
        if (StrUtil.isBlank(mcpIds)) {
            return AsyncMcpToolCallbackProvider.builder().mcpClients(new ArrayList<>(0)).build();
        }
        List<Long> mcpIdList = Arrays.stream(mcpIds.split(",")).map(Long::parseLong).toList();
        LambdaQueryWrapper<KbMcpConfigDO> queryWrapper = Wrappers.<KbMcpConfigDO>lambdaQuery()
                .in(KbMcpConfigDO::getId, mcpIdList);
        List<KbMcpConfigDO> list = super.list(queryWrapper);
        List<McpAsyncClient> mcpAsyncClientList = new ArrayList<>();
        for (KbMcpConfigDO kbMcpConfigDO : list) {
            McpClientTransport transport = this.getMcpClientTransport(kbMcpConfigDO);
            McpAsyncClient mcpAsyncClient = McpClient.async(transport).build();
            mcpAsyncClientList.add(mcpAsyncClient);
        }
        return AsyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpAsyncClientList)
                .build();
    }

    /**
     * 获取mcp客户端传输
     *
     * @param kbMcpConfigDO mcp 配置信息
     * @return McpClientTransport
     */
    private McpClientTransport getMcpClientTransport(KbMcpConfigDO kbMcpConfigDO) {
        if (kbMcpConfigDO.getType() == 3) {
            return new StdioClientTransport(null, null);
        } else {
            Pattern pattern = Pattern.compile("^(https?://[^/]+)(/.*)?$");
            Matcher matcher = pattern.matcher(kbMcpConfigDO.getUrl());
            String baseUrl = kbMcpConfigDO.getUrl();
            String endpoint = null;
            if (matcher.matches()) {
                baseUrl = matcher.group(1);
                endpoint = matcher.group(2);
            }
            HttpClientStreamableHttpTransport.Builder builder = HttpClientStreamableHttpTransport
                    .builder(baseUrl);
            if (StrUtil.isNotBlank(endpoint)) {
                builder.endpoint(endpoint);
            }
            return builder.build();
        }

    }

    /**
     * 修改工具数量
     *
     * @param mcpId   mcpId
     * @param toolNum 工具数量
     */
    private void updateToolNum(Long mcpId, int toolNum) {
        LambdaUpdateWrapper<KbMcpConfigDO> updateWrapper = Wrappers.<KbMcpConfigDO>lambdaUpdate()
                .set(KbMcpConfigDO::getToolNum, toolNum)
                .eq(KbMcpConfigDO::getId, mcpId);
        super.update(updateWrapper);
    }
}
