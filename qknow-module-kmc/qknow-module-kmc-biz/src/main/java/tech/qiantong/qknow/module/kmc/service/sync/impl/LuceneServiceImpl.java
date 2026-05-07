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
