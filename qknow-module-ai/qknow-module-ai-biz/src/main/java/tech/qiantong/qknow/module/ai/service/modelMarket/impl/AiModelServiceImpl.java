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

package tech.qiantong.qknow.module.ai.service.modelMarket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankOptions;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.ai.enums.model.AiModelTypeEnum;
import tech.qiantong.qknow.ai.enums.model.AiPlatformEnum;
import tech.qiantong.qknow.ai.service.IChatModelService;
import tech.qiantong.qknow.ai.service.IEmbeddingService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiModelPageReqVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiModelSaveReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiModelDO;
import tech.qiantong.qknow.module.ai.dal.enums.ApiKeyStatus;
import tech.qiantong.qknow.module.ai.dal.mapper.modelMarket.AiApiKeyMapper;
import tech.qiantong.qknow.module.ai.dal.mapper.modelMarket.AiModelMapper;
import tech.qiantong.qknow.module.ai.service.modelMarket.IAiModelService;
import tech.qiantong.qknow.redis.service.IRedisService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AI 模型Service业务层处理
 *
 * @author qknow
 * @date 2025-12-23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AiModelServiceImpl extends ServiceImpl<AiModelMapper, AiModelDO> implements IAiModelService {

    @Resource
    private AiModelMapper modelMapper;
    @Resource
    private AiApiKeyMapper apiKeyMapper;
    @Resource
    private IChatModelService chatModelService;
    @Resource
    private IEmbeddingService embeddingService;
    @Resource
    private IRedisService redisService;

    private static final Integer STATUS_ENABLE = 1;// 状态-启用
    private static final Integer STATUS_DISABLE = 0;// 状态-未启用

    /**
     * 添加数据
     *
     * @param createReqVO AI 模型信息
     * @return modelId
     */
    @Override
    public Long createAiModel(AiModelSaveReqVO createReqVO) {
        AiModelDO entity = BeanUtils.toBean(createReqVO, AiModelDO.class);
        AiApiKeyDO apiKeyDO = apiKeyMapper.selectById(entity.getKeyId());
        entity.setPlatform(apiKeyDO.getPlatform());
        entity.setWorkspaceId(apiKeyDO.getWorkspaceId());
        modelMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 删除AI 模型
     *
     * @param keyId     密钥id
     * @param modelName 模型名称
     */
    @Override
    public int removeAiModel(Long keyId, String modelName) {
        LambdaQueryWrapper<AiModelDO> queryWrapper = Wrappers.<AiModelDO>lambdaQuery()
                .eq(AiModelDO::getKeyId, keyId)
                .eq(AiModelDO::getName, modelName);
        // 批量删除AI 模型
        return modelMapper.delete(queryWrapper);
    }

    /**
     * 修改模型的状态，是否启用
     *
     * @param aiModel 根据keyId 和 模型名进行修改改
     * @return 操作是否成功
     */
    @Override
    public Boolean changeModelEnable(AiModelSaveReqVO aiModel) {
        LambdaUpdateWrapper<AiModelDO> updateWrapper = Wrappers.<AiModelDO>lambdaUpdate()
                .eq(AiModelDO::getName, aiModel.getName())
                .eq(AiModelDO::getKeyId, aiModel.getKeyId())
                .set(AiModelDO::getStatus, aiModel.getStatus());
        return modelMapper.update(updateWrapper) > 0;
    }

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
        if (Objects.equals(type, AiModelTypeEnum.RERANK.getType())) {
            queryWrapper.eq(AiModelDO::getPlatform, AiPlatformEnum.TONG_YI.getPlatform());
        }
        Map<Long, String> apiKeyMap = apiKeyMapper.selectList().stream()
                .collect(Collectors.toMap(AiApiKeyDO::getId, AiApiKeyDO::getName));
        List<AiModelDO> modelDOList = baseMapper.selectList(queryWrapper);
        JSONArray result = new JSONArray();
        Map<JSONObject, List<JSONObject>> resultMap = new HashMap<>();
        for (AiModelDO aiModelDO : modelDOList) {
            JSONObject key = new JSONObject();
            String label = apiKeyMap.get(aiModelDO.getKeyId());
            if (StrUtil.isBlank(label)) {
                continue;
            }
            key.put("label", Map.of("zh_Hans", label));
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

    /**
     * 获取模型分页列表
     * 首先先进行数据的同步，将平台中的所有模型都同步到表中，默认是启用的状态
     *
     * @param modelPage 模型分页参数
     * @return 模型分页结果
     */
    @Override
    public PageResult<AiModelDO> getModelPage(AiModelPageReqVO modelPage) {
        if (Objects.isNull(modelPage.getKeyId())) {
            return new PageResult<>(0L);
        }
        AiApiKeyDO aiApiKeyDO = apiKeyMapper.selectById(modelPage.getKeyId());
        // 需要同步数据
        if (ApiKeyStatus.CONFIG.getCode().equals(aiApiKeyDO.getStatus())) {
            // 首先进行数据同步
            Boolean syncResult = syncModel(aiApiKeyDO);
            if (!syncResult) {
                throw new ServiceException("同步数据失败");
            }
        }
        return baseMapper.selectPage(modelPage);
    }

    /**
     * 同步平台下的模型数据
     *
     * @param aiApiKeyDO apiKey 对象
     * @return 操作是否成功
     */
    private Boolean syncModel(AiApiKeyDO aiApiKeyDO) {
        String redisLock = redisService.get("redisLock");
        if (StrUtil.isNotBlank(redisLock)) {
            return true;
        }
        redisService.set("redisLock", "1");
        try {
            List<AiModelDO> aiModelDOList = this.queryListByKeyId(aiApiKeyDO.getId());
            Set<String> sqlSet = aiModelDOList.stream().map(AiModelDO::getName).collect(Collectors.toSet());

            AiPlatformEnum aiPlatformEnum = AiPlatformEnum.validatePlatform(aiApiKeyDO.getPlatform());
            JSONArray modelJsonArray = this.getModelsUrl(aiPlatformEnum, aiApiKeyDO);
            Set<String> modelSet = new HashSet<>();
            Set<String> tempModelSet = new HashSet<>();
            for (int i = 0; i < modelJsonArray.size(); i++) {
                JSONObject jsonObject1 = modelJsonArray.getJSONObject(i);
                String modeName = jsonObject1.getString("id");
                modelSet.add(modeName);
                tempModelSet.add(modeName);
            }

            modelSet.removeAll(sqlSet);// 需要添加的
            sqlSet.removeAll(tempModelSet);// 需要删除的

            this.saveBatch(aiApiKeyDO, modelSet);
            this.removeBatch(aiApiKeyDO, sqlSet);
            LambdaUpdateWrapper<AiApiKeyDO> updateWrapper = Wrappers.<AiApiKeyDO>lambdaUpdate()
                    .eq(AiApiKeyDO::getId, aiApiKeyDO.getId())
                    .set(AiApiKeyDO::getStatus, ApiKeyStatus.SYNC.getCode());
            apiKeyMapper.update(updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("同步模型数据失败");
        } finally {
            redisService.delete("redisLock");
        }

        return true;
    }

    /**
     * 根据 keyId 查询模型列表
     *
     * @param keyId 密钥 id
     * @return 模型列表
     */
    private List<AiModelDO> queryListByKeyId(Long keyId) {
        LambdaQueryWrapper<AiModelDO> queryWrapper = Wrappers.<AiModelDO>lambdaQuery()
                .eq(AiModelDO::getKeyId, keyId);
        return modelMapper.selectList(queryWrapper);
    }

    /**
     * 获取平台中的所有可用模型列表
     *
     * @param aiPlatform ai大模型平台
     * @param apiKeyDO   apiKey 对象
     * @return 可用模型列表
     * [
     * {
     * "id": "nomic-embed-text:latest",
     * "object": "model",
     * "created": 1765938001,
     * "owned_by": "library"
     * }
     * ]
     */
    private JSONArray getModelsUrl(AiPlatformEnum aiPlatform, AiApiKeyDO apiKeyDO) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=utf-8");
        header.put("Authorization", "Bearer " + apiKeyDO.getApiKey());

        String baseUrl;
        if (Objects.equals(aiPlatform, AiPlatformEnum.TONG_YI)) {
            return this.getTongYiModel(header);
        }
        switch (aiPlatform) {
            case OLLAMA, OPENAI -> baseUrl = apiKeyDO.getUrl() + "/v1";
            default -> baseUrl = aiPlatform.getOpenAiUrl();
        }
        HttpRequest get = HttpUtil.createGet(baseUrl + "/models");
        get.addHeaders(header);
        HttpResponse response;
        try {
            response = get.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取模型列表失败");
        }
        if (Objects.isNull(response) || !response.isOk()) {
            throw new ServiceException("获取模型列表失败");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        return jsonObject.getJSONArray("data");
    }

    /**
     * 查询模型类型
     *
     * @return 模型字典key
     */
    private Integer getModelType(String modelName) {
        modelName = modelName.toLowerCase();
        if (modelName.contains("tts")) {
            return AiModelTypeEnum.VOICE.getType();
        } else if (modelName.contains("embed")) {
            return AiModelTypeEnum.EMBEDDING.getType();
        } else if (modelName.contains("image")) {
            return AiModelTypeEnum.IMAGE.getType();
        } else if (modelName.contains("rerank")) {
            return AiModelTypeEnum.RERANK.getType();
        }
        return AiModelTypeEnum.CHAT.getType();
    }

    /**
     * 批量新增数据
     *
     * @param aiApiKeyDO    apiKey 对象
     * @param modelNameColl 需要新增的模型名称集合
     */
    private void saveBatch(AiApiKeyDO aiApiKeyDO, Collection<String> modelNameColl) {
        if (CollUtil.isEmpty(modelNameColl)) {
            return;
        }
        List<AiModelDO> saveList = new ArrayList<>(modelNameColl.size());
        for (String modelName : modelNameColl) {
            AiModelDO entity = new AiModelDO();
            entity.setWorkspaceId(aiApiKeyDO.getWorkspaceId());
            entity.setKeyId(aiApiKeyDO.getId());
            entity.setName(modelName);
//            entity.setModel(modelName);
            entity.setPlatform(aiApiKeyDO.getPlatform());
            entity.setType(this.getModelType(modelName));
            entity.setStatus(STATUS_ENABLE);// 默认是启用的

            saveList.add(entity);
        }
        super.saveBatch(saveList);

    }

    /**
     * 批量删除数据
     *
     * @param aiApiKeyDO    apiKey 对象
     * @param modelNameColl 需要删除的模型名称集合
     */
    private void removeBatch(AiApiKeyDO aiApiKeyDO, Collection<String> modelNameColl) {
        if (CollUtil.isEmpty(modelNameColl)) {
            return;
        }
        LambdaQueryWrapper<AiModelDO> queryWrapper = Wrappers.<AiModelDO>lambdaQuery()
                .eq(AiModelDO::getKeyId, aiApiKeyDO.getId())
                .in(AiModelDO::getName, modelNameColl);
        modelMapper.delete(queryWrapper);
    }

    /**
     * 获取统一千问的模型列表，需要分页获取
     *
     * @param header 请求头
     * @return 模型列表
     */
    private JSONArray getTongYiModel(Map<String, String> header) {
        String url = AiPlatformEnum.TONG_YI.getOpenAiUrl() + "/models?page_size={}&page_no={}";
        int pageNo = 1;
        Integer total = 0;
        Integer pageSize = 200;
        JSONArray modelArray;
        JSONArray result = new JSONArray();
        while (true) {
            HttpRequest get = HttpUtil.createGet(StrUtil.format(url, pageSize, pageNo));
            get.addHeaders(header);
            HttpResponse response = get.execute();
            if (response.isOk()) {
                JSONObject jsonObject = JSONObject.parseObject(response.body());
                JSONObject output = jsonObject.getJSONObject("output");
                total = output.getInteger("total");
                modelArray = output.getJSONArray("models");

                for (int i = 0; i < modelArray.size(); i++) {
                    JSONObject model = modelArray.getJSONObject(i);
                    String modelName = model.getString("model");
                    result.add(Map.of("id", modelName));
                }
            }
            if (total / pageSize < pageNo) {
                break;
            }
            pageNo++;
        }
        return result;
    }

}
