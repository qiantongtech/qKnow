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

package tech.qiantong.qknow.module.kb.service.tool.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.StaticToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.convert.tool.KbToolConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;
import tech.qiantong.qknow.module.kb.dal.mapper.tool.KbToolMapper;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolService;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;
import java.util.function.Function;

/**
 * 工具管理Service业务层处理
 *
 * @author qknow
 * @date 2026-03-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbToolServiceImpl extends ServiceImpl<KbToolMapper, KbToolDO> implements IKbToolService {

    @Override
    public PageResult<KbToolDO> getKbToolPage(KbToolPageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKbTool(KbToolSaveReqVO createReqVO) {
        KbToolDO kbToolDO = KbToolConvert.INSTANCE.convertToDO(createReqVO);
        baseMapper.insert(kbToolDO);
        return kbToolDO.getId();
    }

    @Override
    public int updateKbTool(KbToolSaveReqVO updateReqVO) {
        // 相关校验

        // 更新工具管理
        KbToolDO updateObj = KbToolConvert.INSTANCE.convertToDO(updateReqVO);
        return baseMapper.updateById(updateObj);
    }

    @Override
    public int removeKbTool(Collection<Long> idList) {
        // 批量删除工具管理
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public KbToolDO getKbToolById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 获取工具回调提供者
     *
     * @param toolIds 工具ID
     * @return 工具回调提供者
     */
    @Override
    public ToolCallbackProvider getToolCallbackProvider(String toolIds) {
        if (StrUtil.isBlank(toolIds)) {
            return new StaticToolCallbackProvider(new ArrayList<>(0));
        }
        List<Long> toolIdList = Arrays.stream(toolIds.split(",")).map(Long::parseLong).toList();
        List<KbToolDO> kbToolDOList = super.listByIds(toolIdList);

        List<ToolCallback> toolList = new ArrayList<>(kbToolDOList.size());
        for (KbToolDO kbToolDO : kbToolDOList) {
            // 单独定义标准Function，类型清晰
            Function<Map<String, Object>, String> codeFunc = params -> {
                try {
                    Object result = runCode(kbToolDO.getContent(), params);
                    return "结果：" + result;
                } catch (Exception e) {
                    return "异常：" + e.getMessage();
                }
            };
            // 手动构建动态代码工具回调
            FunctionToolCallback<Map<String, Object>, String> codeTool = FunctionToolCallback.builder(kbToolDO.getName(), codeFunc).description(kbToolDO.getDescription()).inputSchema(kbToolDO.getParamSchema()).inputType(Map.class).build();
            toolList.add(codeTool);
        }

        // 使用 StaticToolCallbackProvider 装载自定义工具
        return new StaticToolCallbackProvider(toolList);
    }

    /**
     * 更新工具状态
     *
     * @param kbTool 工具信息
     * @return 更新结果
     */
    @Override
    public Boolean updateStatus(KbToolSaveReqVO kbTool) {
        LambdaUpdateWrapper<KbToolDO> updateWrapper = Wrappers.<KbToolDO>lambdaUpdate().set(KbToolDO::getStatus, kbTool.getStatus()).eq(KbToolDO::getId, kbTool.getId());
        return super.update(updateWrapper);
    }

    /**
     * 脚本引擎执行代码
     *
     * @param code   脚本
     * @param params 参数
     * @return 执行结果
     * @throws Exception 脚本执行异常
     */
    private Object runCode(String code, Map<String, Object> params) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
        engine.put("params", params);
        return engine.eval(code);
    }
}
