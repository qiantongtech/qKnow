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

package tech.qiantong.qknow.module.ai.api.modelMarket;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankOptions;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.ai.enums.model.AiPlatformEnum;
import tech.qiantong.qknow.ai.service.IChatModelService;
import tech.qiantong.qknow.ai.service.IEmbeddingService;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiModelDO;
import tech.qiantong.qknow.module.ai.dal.mapper.modelMarket.AiApiKeyMapper;
import tech.qiantong.qknow.module.ai.dal.mapper.modelMarket.AiModelMapper;

import java.util.*;

/**
 * AI 模型Service业务层处理
 *
 * @author qknow
 * @date 2025-12-23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AiModelApiServiceImpl extends ServiceImpl<AiModelMapper, AiModelDO>  implements IAiModelApiService {

    @Resource
    private AiApiKeyMapper apiKeyMapper;
    @Resource
    private IEmbeddingService embeddingService;
    @Resource
    private IChatModelService chatModelService;

    /**
     * 获取具体的聊天模型
     *
     * @param keyId     模型平台id
     * @param modelName 模型名称
     * @return chatModel 对象
     */
    @Override
    public ChatModel getChatModel(Long keyId, String modelName) {
        AiApiKeyDO aiApiKeyDO = apiKeyMapper.selectById(keyId);
        return chatModelService.getChatModel(aiApiKeyDO.getPlatform(),
                aiApiKeyDO.getUrl(),
                aiApiKeyDO.getApiKey(),
                modelName);
    }

    /**
     * 获取具体的 embedding 模型
     *
     * @param keyId     模型平台id
     * @param modelName 模型名称
     * @return EmbeddingModel 对象
     */
    @Override
    public EmbeddingModel getEmbeddingModel(Long keyId, String modelName) {
        AiApiKeyDO aiApiKeyDO = apiKeyMapper.selectById(keyId);
        return embeddingService.getEmbeddingModel(aiApiKeyDO.getPlatform(),
                aiApiKeyDO.getUrl(),
                aiApiKeyDO.getApiKey(),
                modelName);
    }

    /**
     * 获取模型字典
     *
     * @param type 模型类型
     */
    @Override
    public JSONArray getModelDict(Integer type) {
        LambdaQueryWrapper<AiModelDO> queryWrapper = Wrappers.<AiModelDO>lambdaQuery()
                .eq(AiModelDO::getType, type);
        List<AiModelDO> modelDOList = baseMapper.selectList(queryWrapper);
        JSONArray result = new JSONArray();
        Map<JSONObject, List<JSONObject>> resultMap = new HashMap<>();
        for (AiModelDO aiModelDO : modelDOList) {
            JSONObject key = new JSONObject();
            AiPlatformEnum platform = AiPlatformEnum.validatePlatform(aiModelDO.getPlatform());
            key.put("label", Map.of("zh_Hans", platform.getName()));
            key.put("provider", aiModelDO.getKeyId());
            List<JSONObject> valueList = resultMap.get(key);
            if (CollUtil.isEmpty(valueList)) {
                valueList = new ArrayList<>();
            }
            JSONObject valObj = new JSONObject();
            valObj.put("model", aiModelDO.getName());
            valueList.add(valObj);
            resultMap.put(key, valueList);
        }

        for (Map.Entry<JSONObject, List<JSONObject>> entry : resultMap.entrySet()) {
            JSONObject key = entry.getKey();
            key.put("models", entry.getValue());
            result.add(key);
        }
        return result;
    }

    /**
     * 获取重排序模型，暂时支持通义平台的
     *
     * @param keyId     模型平台id
     * @param modelName 模型名称
     * @return 重排序模型
     */
    @Override
    public DashScopeRerankModel getRerankModel(Long keyId, String modelName) {
        AiApiKeyDO aiApiKeyDO = apiKeyMapper.selectById(keyId);
        return new DashScopeRerankModel(
                DashScopeApi.builder().apiKey(aiApiKeyDO.getApiKey()).build(),
                DashScopeRerankOptions.builder().model(modelName).build()
        );
    }

}
