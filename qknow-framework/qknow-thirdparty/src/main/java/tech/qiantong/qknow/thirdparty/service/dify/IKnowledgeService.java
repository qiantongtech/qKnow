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

package tech.qiantong.qknow.thirdparty.service.dify;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.*;

import java.util.List;

/**
 * 灵桐-知识库Service接口
 *
 * @author qknow
 * @date 2025-02-19
 */
public interface IKnowledgeService {
    /**
     * 上次文档
     * @param createFile
     * @return
     */
    public CreateFileResult createByFile(CreateFile createFile, String datasetId, String apiKey);

    /**
     * 获取文件状态
     * @param batch
     * @return
     */
    public String getFileStatus(String batch, String datasetId, String apiKey);

    /**
     * 删除文件状态
     * @param documentId
     * @return
     */
    public Boolean deleteFileStatus(String documentId, String datasetId, String apiKey);
    /**
     * 检索文件
     * @param retrieve
     * @return
     */
    public List<RetrieveResult> findFileRetrieve(Retrieve retrieve, String datasetId, String apiKey);

    /**
     * 获取片段
     * @param documentId
     * @param datasetId
     * @param apiKey
     * @return 所以片段详细数据
     */
    public List<Segments> segments(String documentId, String datasetId, String apiKey);

    /**
     * 创建知识库
     * @param datasets
     * @param apiKey
     * @return id
     */
    public String createDatasets(Datasets datasets, String apiKey);

    /**
     * 修改知识库
     * @param datasets
     * @param apiKey
     * @return id
     */
    public String updateDatasets(Datasets datasets, String apiKey);

    /**
     * 删除知识库
     * @param datasetId
     * @param apiKey
     */
    public void deleteDatasets(String datasetId, String apiKey);

    /**
     * 获取文本嵌入模型
     * @param apiKey
     * @return JSONArray 返回格式参考dify文档,格式比较复杂，所以通过json格式返回
     */
    public JSONArray getTextEmbedding(String apiKey);

    /**
     * 获取Rerank
     * 隐藏接口，dify文档暂无，但是接口可用
     * @param apiKey
     * @return JSONArray 同getTextEmbedding
     */
    public JSONArray getRerank(String apiKey);


    /**
     * 删除父节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @return 删除成功返回true
     */
    public boolean removeRootSegment(String segmentId, String datasetId,String documentId, String apiKey);


    /**
     * 删除子节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param parentId 父级id为空代表是顶节点
     * @return 删除成功返回true
     */
    public boolean removeChildSegment(String segmentId, String datasetId,String documentId,String parentId, String apiKey);

    /**
     * 新增父节点分段信息
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param params 请求参数
     * @return 分段信息
     */
    public JSONObject addRootSegment(String datasetId, String documentId, String apiKey,  JSONObject params);


    /**
     * 新增子节点分段信息
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param parentId 父级id为空代表是顶节点
     * @param params 请求参数
     * @return 分段信息 addRootSegment
     */
    public JSONObject addChildSegment(String datasetId, String documentId, String parentId, String apiKey,  JSONObject params);


    /**
     * 编辑父级节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @return 分段信息
     */
    public JSONObject editRootSegment(String segmentId, String datasetId, String documentId , String apiKey,  JSONObject params);


    /**
     * 编辑子级节点分段信息
     * @param segmentId 分段id
     * @param datasetId 知识库id
     * @param apiKey apiKey
     * @param documentId 文档id
     * @param parentId 父级id为空代表是顶节点
     * @return 分段信息
     */
    public JSONObject editChildSegment(String segmentId, String datasetId, String documentId, String parentId, String apiKey,  JSONObject params);




    /**
     * 获取分段信息
     * @param datasetId 知识库id
     * @param documentId 文档id
     * @param apiKey apiKey
     * @return JSONArray 同getTextEmbedding
     */
    public JSONArray getSegments(String datasetId,String documentId, String apiKey);

}
