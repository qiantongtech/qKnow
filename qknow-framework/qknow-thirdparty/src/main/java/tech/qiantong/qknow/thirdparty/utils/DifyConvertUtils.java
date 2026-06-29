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

package tech.qiantong.qknow.thirdparty.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.RetrieveResult;

import java.util.List;

/**
 * dify转换工具
 * @author wang
 * @date 2025/08/13 10:00
 **/
public class DifyConvertUtils {

    /**
     * 将RetrieveResult转换为编排流程中的知识库格式
     * @param result
     * @return
     */
    public static JSONObject convertToRecall(RetrieveResult result, String datasetId, String datasetName) {
        JSONObject metadata = new JSONObject();
        metadata.put("_source", "knowledge");
        metadata.put("dataset_id", datasetId);
        metadata.put("dataset_name", datasetName);
        metadata.put("document_id", result.getDocumentId());
        metadata.put("document_name", result.getDocumentName());
        metadata.put("data_source_type", result.getDataSourceType());
        metadata.put("segment_id", result.getId());
        //TODO: retriever_from目前写死，暂不清楚作用
        metadata.put("retriever_from", "workflow");
        metadata.put("score", result.getScore());
        metadata.put("segment_hit_count", result.getHitCount());
        metadata.put("segment_word_count", result.getWordCount());
        metadata.put("segment_position", result.getPosition());
        metadata.put("segment_index_node_hash", result.getIndexNodeHash());
        metadata.put("doc_metadata", result.getDocMetadata());
        metadata.put("position", result.getPosition());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("metadata", metadata);
        jsonObject.put("title", result.getDocumentName());
        jsonObject.put("content", result.getContent());
        return jsonObject;
    }

    /**
     * 批量转换
     * @param resultList
     * @return
     */
    public static JSONArray convertToRecallList(List<RetrieveResult> resultList, String datasetId, String datasetName) {
        JSONArray jsonArray = new JSONArray();
        for (RetrieveResult result : resultList) {
            JSONObject jsonObject = convertToRecall(result, datasetId, datasetName);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
