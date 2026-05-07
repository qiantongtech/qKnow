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

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.*;
import tech.qiantong.qknow.thirdparty.service.dify.IKnowledgeService;

import java.util.List;
import java.util.Map;

/**
 * ai聊天消息Service业务层处理
 *
 * @author qknow
 * @date 2025-02-17
 */
@Slf4j
@Service
public class KnowledgeServiceImpl implements IKnowledgeService {
    @Value("${app.model.url}")
    private String url;

    /**
     * 上次文档
     * @param createFile
     * @return
     */
    @Override
    public CreateFileResult createByFile(CreateFile createFile, String datasetId, String apiKey) {
        String data = createFile.toString();
        String modelURL = url + "/datasets/" + datasetId + "/document/create-by-file";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("file", createFile.getFile());
        paramMap.put("data", data);
        String response = HttpRequest.post(modelURL).form(paramMap)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject parseObject = JSONObject.parseObject(response);
        if (parseObject != null) {
            String batch = parseObject.getString("batch");
            JSONObject document = parseObject.getJSONObject("document");
            CreateFileResult createFileResult = document.to(CreateFileResult.class);
            createFileResult.setBatch(batch);
            return createFileResult;
        }
        return null;
    }

    /**
     * 获取文件状态
     * @param batch
     * @return
     */
    @Override
    public String getFileStatus(String batch, String datasetId, String apiKey) {
        String modelURL = url + "/datasets/" + datasetId + "/documents/" + batch + "/indexing-status";
        String response = HttpRequest.get(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray == null || jsonArray.isEmpty()) {
            log.error("查无文档，文档状态获取失败！");
            throw new RuntimeException("查无文档，文档状态获取失败！");
        }
        //String indexingStatus = jsonArray.getJSONObject(0).getString("indexing_status");
        return jsonArray.getJSONObject(0).getString("indexing_status");
    }

    /**
     * 删除文件状态
     * @param documentId
     * @return
     */
    @Override
    public Boolean deleteFileStatus(String documentId, String datasetId, String apiKey) {
        String modelURL = url + "/datasets/" + datasetId + "/documents/" + documentId;
        String response = HttpRequest.delete(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();
        return "204\n".equals(response);
    }

    /**
     * 检索文件
     * @param retrieve
     * @return
     */
    @Override
    public List<RetrieveResult> findFileRetrieve(Retrieve retrieve, String datasetId, String apiKey) {
        String modelURL = url + "/datasets/" + datasetId + "/retrieve";
        String response = HttpRequest.post(modelURL).contentType("application/json").body(retrieve.toJSONString())
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONArray records = result.getJSONArray("records");
        List<RetrieveResult> retrieveResults = Lists.newArrayList();
        if (records != null) {
            for (int i = 0; i < records.size(); i++) {
                JSONObject record = records.getJSONObject(i);
                // 详细数据
                JSONObject segment = record.getJSONObject("segment");
                // 相似度
                Double score = record.getDouble("score");
                // 文档信息
                JSONObject document = segment.getJSONObject("document");
                RetrieveResult retrieveResult = segment.to(RetrieveResult.class, JSONReader.Feature.SupportSmartMatch);
                retrieveResult.setScore(score);
                retrieveResult.setDocumentName(document.getString("name"));
                retrieveResult.setDocumentId(document.getString("id"));
                retrieveResult.setDataSourceType(segment.getString("data_source_type"));
                retrieveResult.setDocMetadata(segment.getString("doc_metadata"));
                retrieveResults.add(retrieveResult);
            }
        }
        return retrieveResults;
    }

    /**
     * 获取片段
     * @param documentId
     * @param datasetId
     * @param apiKey
     * @return
     */
    @Override
    public List<Segments> segments(String documentId, String datasetId, String apiKey) {
        List<Segments> segmentsList = Lists.newArrayList();

        // 循环遍历取出所有文档分段
        int page = 1;
        while (true) {
            String modelURL = url + "/datasets/" + datasetId + "/documents/" + documentId + "/segments?limit=100&page=" + page;
            String response = HttpRequest.get(modelURL)
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(HttpGlobalConfig.getTimeout()).execute().body();
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            // 如果没有数据则跳出循环
            if (jsonArray.isEmpty()) {
                break;
            }

            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject segment = jsonArray.getJSONObject(i);
                Segments segments = segment.to(Segments.class, JSONReader.Feature.SupportSmartMatch);
                segmentsList.add(segments);
            }
            page++;
        }
        return segmentsList;
    }

    /**
     * 创建知识库
     * @param datasets
     * @param apiKey
     * @return id
     */
    @Override
    public String createDatasets(Datasets datasets, String apiKey) {
        String modelURL = url + "/datasets";
        log.info("创建知识库参数：{}", datasets.toJSONString());
        String response = HttpRequest.post(modelURL).body(datasets.toJSONString())
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();

        log.info("创建知识库结果：{}", response);
        JSONObject result = JSONObject.parseObject(response);
        return result.getString("id");
    }

    /**
     * 修改知识库
     * @param datasets
     * @param apiKey
     * @return id
     */
    @Override
    public String updateDatasets(Datasets datasets, String apiKey) {
        String modelURL = url + "/datasets/" + datasets.getId();
        String response = HttpRequest.patch(modelURL).body(datasets.toJSONString())
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();

        JSONObject result = JSONObject.parseObject(response);
        return result.getString("id");
    }

    /**
     * 删除知识库
     * @param datasetId
     * @param apiKey
     */
    @Override
    public void deleteDatasets(String datasetId, String apiKey) {
        String modelURL = url + "/datasets/" + datasetId;
        HttpRequest.delete(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute();
    }

    /**
     * 获取文本嵌入模型
     * @param apiKey
     * @return JSONArray 返回格式参考dify文档,格式比较复杂，所以通过json格式返回
     */
    @Override
    public JSONArray getTextEmbedding(String apiKey) {
        String modelURL = url + "/workspaces/current/models/model-types/text-embedding";
        String response = HttpRequest.get(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();

        JSONObject jsonObject = JSONObject.parseObject(response);
        return jsonObject.getJSONArray("data");
    }

    /**
     * 获取Rerank
     * 隐藏接口，dify文档暂无，但是接口可用
     * @param apiKey
     * @return JSONArray 同getTextEmbedding
     */
    @Override
    public JSONArray getRerank(String apiKey) {
        String modelURL = url + "/workspaces/current/models/model-types/rerank";
        String response = HttpRequest.get(modelURL)
                .header("Authorization", "Bearer " + apiKey)
                .timeout(HttpGlobalConfig.getTimeout()).execute().body();

        JSONObject jsonObject = JSONObject.parseObject(response);
        return jsonObject.getJSONArray("data");
    }

    /**
     * 删除根分段（顶级 chunk）
     */
    @Override
    public boolean removeRootSegment(String segmentId, String datasetId, String documentId, String apiKey) {
        String url = getSegmentUrl(datasetId, documentId) + "/" + segmentId;
        return executeDelete(url, apiKey);
    }

    /**
     * 删除子分段（child chunk）
     */
    @Override
    public boolean  removeChildSegment(String segmentId, String datasetId, String documentId, String parentId, String apiKey) {
        String url = getSegmentUrl(datasetId, documentId) +
                "/" + parentId +
                "/child_chunks/" + segmentId;
        return executeDelete(url, apiKey);
    }

    // 公共删除执行逻辑
    private boolean executeDelete(String url, String apiKey) {
        try {
            HttpResponse response = HttpRequest.delete(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(HttpGlobalConfig.getTimeout())
                    .execute();

            if("204\n".equals(response.body())){
                return true;
            } else {
                log.error("删除分段失败，URL: {}, 状态码: {}, 响应: {}", url, response.getStatus(), response.body());
                return false;
            }
        } catch (Exception e) {
            log.error("删除分段异常，URL: " + url, e);
            return false;
        }
    }

    @Override
    public JSONArray getSegments(String datasetId, String documentId, String apiKey) {
        JSONArray  result = new JSONArray();
        // 循环遍历取出所有文档分段
        int page = 1;
        while (true) {
            String segmentUrl = getSegmentUrl(datasetId, documentId);
            segmentUrl += "?limit=100&page=" + page;
            String response = HttpRequest.get(segmentUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(HttpGlobalConfig.getTimeout()).execute().body();
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            result.addAll(jsonArray);
            // 如果没有数据则跳出循环
            if (jsonArray.isEmpty()) {
                break;
            }
            page++;
        }
        return result;
    }

    /**
     * 添加父级节点（顶级 chunk）
     */
    @Override
    public JSONObject addRootSegment(String datasetId, String documentId, String apiKey, JSONObject params) {
        String segmentUrl = getSegmentUrl(datasetId, documentId);
        return executeAddSegment(segmentUrl, apiKey, params);
    }

    /**
     * 添加子节点（child chunk）
     */
    @Override
    public JSONObject addChildSegment(String datasetId, String documentId, String parentId, String apiKey, JSONObject params) {
        String segmentUrl = getSegmentUrl(datasetId, documentId) + "/" + parentId + "/child_chunks";
        return executeAddSegment(segmentUrl, apiKey, params);
    }

    // 公共添加执行逻辑
    private JSONObject executeAddSegment(String url, String apiKey, JSONObject params) {
        String response = null;
        try {
            response = HttpRequest.post(url)
                    .body(params.toJSONString())
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .timeout(HttpGlobalConfig.getTimeout())
                    .execute().body();
        } catch (Exception e) {
            log.error("添加分段异常, 请求地址: {},返回信息: {}", url, response, e);
        }
        return JSONObject.parseObject(response);
    }

    /**
     * 编辑根分段（顶级 chunk）
     */
    @Override
    public JSONObject editRootSegment(String segmentId, String datasetId, String documentId, String apiKey, JSONObject params) {
        String segmentUrl = getSegmentUrl(datasetId, documentId) + "/" + segmentId;
        return executeEditSegment(segmentUrl, apiKey, params,Method.POST);
    }

    /**
     * 编辑子分段（child chunk）
     */
    @Override
    public JSONObject editChildSegment(String segmentId, String datasetId, String documentId, String parentId, String apiKey, JSONObject params) {
        String segmentUrl = getSegmentUrl(datasetId, documentId) + "/" + parentId + "/child_chunks/" + segmentId;
        return executeEditSegment(segmentUrl, apiKey, params, Method.PATCH);
    }

    // 公共编辑执行逻辑
    private JSONObject executeEditSegment(String url, String apiKey, JSONObject params,Method  method) {
        String response = null;
        try {
            HttpRequest httpRequest = new HttpRequest(UrlBuilder.ofHttp(url));
            httpRequest.header("Authorization", "Bearer " + apiKey);
            httpRequest.header("Content-Type", "application/json");
            httpRequest.body(params.toJSONString());
            httpRequest.setMethod(method);
            response = httpRequest.execute().body();
        } catch (Exception e) {
            log.error("修改分段异常, 请求地址: {},请求参数: {}", url, params.toJSONString(), e);
        }
        return JSONObject.parseObject(response);
    }

    //组装分段URL
    public String getSegmentUrl(String datasetId, String documentId){
        //获取知识库信息
        return url + "/datasets/" + datasetId +
                "/documents/" + documentId
                + "/segments";
    }
}
