/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
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
