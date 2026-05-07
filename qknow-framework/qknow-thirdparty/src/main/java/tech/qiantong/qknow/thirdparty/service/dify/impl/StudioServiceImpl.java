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

package tech.qiantong.qknow.thirdparty.service.dify.impl;

import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.annotations.EverythingIsNonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.thirdparty.api.dify.ChatCallback;
import tech.qiantong.qknow.thirdparty.constant.HttpClientConstants;
import tech.qiantong.qknow.thirdparty.domain.dify.studio.ChatFile;
import tech.qiantong.qknow.thirdparty.domain.dify.studio.ChatMessage;
import tech.qiantong.qknow.thirdparty.domain.dify.studio.ChatParameter;
import tech.qiantong.qknow.thirdparty.domain.dify.studio.ChatWorkflows;
import tech.qiantong.qknow.thirdparty.service.dify.IStudioService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ai聊天消息Service业务层处理
 *
 * @author qknow
 * @date 2025-02-17
 */
@Slf4j
@Service
public class StudioServiceImpl implements IStudioService {
//    @Value("${app.model.url}")
//    private String url;

    // 配置连接池
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(HttpClientConstants.DEFAULT_CONNECT_TIMEOUT,TimeUnit.MILLISECONDS)
            .readTimeout(HttpClientConstants.DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(HttpClientConstants.DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .build();

    /**
     * 构建会话请求
     * @param chatMessage
     * @return
     */
    private Request buildChatMessageRequest(ChatMessage chatMessage) {
        JSONObject inputs = new JSONObject();
        inputs.putAll(chatMessage.getMap());
        // 构建请求体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("inputs", inputs);
        jsonObject.put("response_mode", "streaming");
        jsonObject.put("conversation_id", chatMessage.getConversationId());
        jsonObject.put("query", chatMessage.getQuery());
        jsonObject.put("user", chatMessage.getUser());

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonObject.toJSONString()
        );

        return new Request.Builder()
                .url(chatMessage.getUrl() + "/chat-messages")
                .post(body)
                .addHeader("Authorization", "Bearer " + chatMessage.getApiKey())
                .build();
    }

    /**
     * 发送聊天消息
     * @param chatMessage
     * @param callback
     * @return
     */
    @Override
    public Call sendChatMessage(ChatMessage chatMessage, ChatCallback callback) {
        // 构建请求
        Request request = this.buildChatMessageRequest(chatMessage);
        // 执行异步请求
        Call call = CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                // 调用外部回调
                callback.onFailure(e);
            }

            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                // 调用外部回调
                callback.onSuccess(response);
            }
        });

        return call; // 返回Call对象以便管理请求（如取消）
    }

    /**
     * 构建工作流请求
     * @param chatWorkflows
     * @return
     */
    private Request buildChatWorkflowsRequest(ChatWorkflows chatWorkflows) {
        // 构建请求体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("inputs", chatWorkflows.getJsonObject());
        jsonObject.put("response_mode", "streaming");
        jsonObject.put("user", chatWorkflows.getUser());

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonObject.toJSONString() // 确保使用正确的JSON序列化方法（如JSON.toJSONString）
        );

        return new Request.Builder()
                .url(chatWorkflows.getUrl() + "/workflows/run")
                .post(body)
                .addHeader("Authorization", "Bearer " + chatWorkflows.getApiKey())
                .build();
    }

    /**
     * 发送工作流请求
     * @param chatWorkflows
     * @param callback
     * @return
     */
    @Override
    public Call sendChatWorkflow(ChatWorkflows chatWorkflows, ChatCallback callback) {
        // 构建请求
        Request request = this.buildChatWorkflowsRequest(chatWorkflows);
        // 执行异步请求
        Call call = CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                // 调用外部回调
                callback.onFailure(e);
            }

            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                // 调用外部回调
                callback.onSuccess(response);
            }
        });

        return call; // 返回Call对象以便管理请求（如取消）
    }

    @Override
    public List<ChatParameter> getParameters(String url ,String apiKey) {
        String modelURL = url + "/parameters";
        String response = HttpRequest.get(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject parseObject = JSONObject.parseObject(response);
        JSONArray jsonArray = parseObject.getJSONArray("user_input_form");
        List<ChatParameter> parameters = Lists.newArrayList();

        if (StringUtils.isNull(jsonArray) || jsonArray.size() == 0) {
            return parameters;
        }
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // 获取类型字段（如paragraph/file等）
            String type = jsonObject.keySet().iterator().next();
            JSONObject typeData = jsonObject.getJSONObject(type);

            ChatParameter param = new ChatParameter();
            param.setType(type);
            param.setLabel(typeData.getString("label"));
            param.setFieldName(typeData.getString("variable"));
            param.setRequired(typeData.getBoolean("required"));
            param.setDefaultValue(typeData.getString("default"));
            param.setMaxLength(typeData.getInteger("max_length"));
            param.setFileTypes(typeData.getJSONArray("allowed_file_types"));
            param.setFileExtensions(typeData.getJSONArray("allowed_file_extensions"));

            parameters.add(param);
        }
        return parameters;
    }

    @Override
    public ChatFile uploadFile(String url,File file, String userId, String apiKey) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("file", file);
        paramMap.put("user", userId);
        String parseObject = HttpRequest.post(url + "/files/upload")
                .form(paramMap)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(parseObject);
        return jsonObject.to(ChatFile.class, JSONReader.Feature.SupportSmartMatch);
    }

    @Override
    public List<String> getSuggested(String url,String messageId, String userId, String apiKey) {
        String modelURL = url + "/messages/" + messageId + "/suggested?user=" + userId;
        String response = HttpRequest.get(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        if ("success".equals(jsonObject.getString("result"))) {
            return jsonObject.getList("data", String.class);
        } else {
            throw new RuntimeException("获取建议失败");
        }
    }

    @Override
    public Boolean stopChatMessage(String url,String taskId, String userId, String apiKey) {
        String modelURL = url + "/chat-messages/" + taskId + "/stop";
        JSONObject body = new JSONObject();
        body.put("user", userId);

        String response = HttpRequest.post(modelURL)
                .body(body.toJSONString())
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        if ("success".equals(jsonObject.getString("result"))) {
            return true;
        } else {
            throw new RuntimeException("停止任务失败");
        }
    }
}
