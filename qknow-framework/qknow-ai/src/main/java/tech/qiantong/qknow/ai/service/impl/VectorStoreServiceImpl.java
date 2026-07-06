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

package tech.qiantong.qknow.ai.service.impl;

import io.weaviate.client.WeaviateClient;
import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.ai.constant.WeaviateConstant;
import tech.qiantong.qknow.ai.service.IVectorStoreService;

import java.util.ArrayList;
import java.util.List;

/**
 * vectorStore 是向量数据库的连接对象
 *
 * @author fabian
 */
@Service
public class VectorStoreServiceImpl implements IVectorStoreService {

    @Resource
    private WeaviateClient weaviateClient;

    @Value("${weaviate.className:SpringAiWeaviate}")
    private String className;

    /**
     * 获取 VectorStore 数据库连接
     *
     * @param embeddingModel 文本向量模型
     * @return 数据库连接
     */
    @Override
    public WeaviateVectorStore getVectorStore(EmbeddingModel embeddingModel) {
        WeaviateVectorStoreOptions options = new WeaviateVectorStoreOptions();
        options.setObjectClass(className);

        // 可在过滤器中使用的字段
        List<WeaviateVectorStore.MetadataField> filterMetadataFields = new ArrayList<>();
        filterMetadataFields.add(WeaviateVectorStore.MetadataField.number(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID));
        filterMetadataFields.add(WeaviateVectorStore.MetadataField.number(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID));
        filterMetadataFields.add(WeaviateVectorStore.MetadataField.number(WeaviateConstant.METADATA_FIELD_SEGMENT_ID));

        return WeaviateVectorStore.builder(weaviateClient, embeddingModel)
                .consistencyLevel(WeaviateVectorStore.ConsistentLevel.QUORUM)
                .filterMetadataFields(filterMetadataFields)
                .build();
    }
}
