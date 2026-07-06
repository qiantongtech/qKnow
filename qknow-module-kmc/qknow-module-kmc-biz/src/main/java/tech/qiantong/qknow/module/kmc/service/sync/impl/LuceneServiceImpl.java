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
package tech.qiantong.qknow.module.kmc.service.sync.impl;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.ai.constant.WeaviateConstant;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment.KmcDocumentSegmentDO;
import tech.qiantong.qknow.module.kmc.service.sync.ILuceneService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author fabian
 */
@Service
public class LuceneServiceImpl implements ILuceneService {

    @Value("${lucene.indexPath}")
    private String indexPath;

    /**
     * 保存 文档片段
     *
     * @param documentDO 文档对象
     * @param segmentDO  分段对象
     */
    @Override
    public void saveSegment(KmcDocumentSegmentDO segmentDO, KmcDocumentDO documentDO) {
        org.apache.lucene.document.Document doc = segment2LuceneDocument(segmentDO, documentDO);

        Directory directory;
        IndexWriter writer = null;
        try {
            directory = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, config);
            writer.addDocument(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量报错文档片段
     *
     * @param documentDO  文档对象
     * @param segmentList 分段列表
     */
    @Override
    public void saveAiDocumentBatch(Map<String, Long> map, KmcDocumentDO documentDO, List<org.springframework.ai.document.Document> segmentList) {
        List<Document> saveList = new ArrayList<>(segmentList.size());
        for (org.springframework.ai.document.Document document : segmentList) {
            org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
            Map<String, Object> metadata = document.getMetadata();
            Long segmentId = map.get(document.getId());
            doc.add(new StringField(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID,
                    String.valueOf(documentDO.getKnowledgeBaseId()), Field.Store.YES));
            doc.add(new StringField(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, String.valueOf(documentDO.getId()), Field.Store.YES));
            doc.add(new StringField(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, String.valueOf(segmentId), Field.Store.YES));// 分段id
            doc.add(new StringField(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME, documentDO.getName(), Field.Store.YES));// 分段id
            doc.add(new TextField("content", document.getText(), Field.Store.YES));

            if (!Objects.isNull(metadata.get("answer"))) {
                doc.add(new StoredField("answer", String.valueOf(metadata.get("answer"))));
            }
            saveList.add(doc);
        }
        Directory directory;
        IndexWriter writer = null;
        try {
            directory = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, config);
            Term term = new Term(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, String.valueOf(documentDO.getId()));
            writer.deleteDocuments(term);// 先删除
            writer.addDocuments(saveList);// 后新增
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量删除文档片段
     *
     * @param segmentDOList 文档片段列表
     */
    @Override
    public void deleteSegmentList(List<KmcDocumentSegmentDO> segmentDOList) {
        Directory directory;
        IndexWriter writer = null;
        try {
            directory = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, config);
            for (KmcDocumentSegmentDO segmentDO : segmentDOList) {
                Term term = new Term(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, String.valueOf(segmentDO.getId()));
                writer.deleteDocuments(term);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文档id 删除索引
     *
     * @param documentId  文档id
     */
    @Override
    public void deleteByDocumentId(String documentId) {
        Directory directory;
        IndexWriter writer = null;
        try {
            directory = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, config);
            Term term = new Term(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, documentId);
            writer.deleteDocuments(term);// 删除
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新文档片段
     *
     * @param documentDO 文档对象
     * @param segmentDO  分段对象
     */
    @Override
    public void updateSegment(KmcDocumentSegmentDO segmentDO, KmcDocumentDO documentDO) {
        Document doc = segment2LuceneDocument(segmentDO, documentDO);
        Directory directory;
        IndexWriter writer = null;
        try {
            directory = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, config);
            Term term = new Term(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, String.valueOf(segmentDO.getId()));
            writer.updateDocument(term, doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文档片段 转换成 Lucene 文档
     *
     * @param segmentDO  文档片段
     * @param documentDO 文档对象
     * @return Lucene 文档
     */
    private Document segment2LuceneDocument(KmcDocumentSegmentDO segmentDO, KmcDocumentDO documentDO) {
        Document doc = new Document();
        doc.add(new StringField(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID,
                String.valueOf(documentDO.getKnowledgeBaseId()), Field.Store.YES));
        doc.add(new StringField(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, String.valueOf(documentDO.getId()), Field.Store.YES));
        doc.add(new StringField(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME, documentDO.getName(), Field.Store.YES));
        doc.add(new StringField(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, String.valueOf(segmentDO.getId()), Field.Store.YES));// 分段id
        doc.add(new TextField("content", segmentDO.getContent(), Field.Store.YES));

        if (!Objects.isNull(segmentDO.getAnswer())) {
            doc.add(new StoredField("answer", segmentDO.getAnswer()));
        }

        return doc;
    }
}
