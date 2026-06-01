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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.ai.constant.WeaviateConstant;
import tech.qiantong.qknow.ai.service.IVectorStoreService;
import tech.qiantong.qknow.ai.transformer.ContinuousWhitespaceEnricher;
import tech.qiantong.qknow.ai.transformer.GeneralSplitter;
import tech.qiantong.qknow.ai.transformer.QuestionAnswerEnricher;
import tech.qiantong.qknow.ai.transformer.RemoveUrlAndEmailEnricher;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.FileReader;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.enums.DocumentSyncStatus;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment.KmcDocumentSegmentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.sync.KmcSyncDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.sync.KmcSyncMapper;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeBaseService;
import tech.qiantong.qknow.module.kmc.service.knowledgeSegment.IKmcDocumentSegmentService;
import tech.qiantong.qknow.module.kmc.service.sync.IKmcSyncService;
import tech.qiantong.qknow.module.kmc.service.sync.ILuceneService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qknow.thirdparty.domain.dify.enums.DocFormEnum;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.CreateFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件同步Service业务层处理
 *
 * @author qknow
 * @date 2025-03-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcSyncServiceImpl extends ServiceImpl<KmcSyncMapper, KmcSyncDO> implements IKmcSyncService {
    @Resource
    private IKmcDocumentService kmcDocumentService;
    @Resource
    private IKmcKnowledgeBaseService kmcKnowledgeBaseService;
    @Resource
    private IKmcDocumentSegmentService iKmcDocumentSegmentService;
    @Resource
    private IAiModelApiService aiModelService;
    @Resource
    private IVectorStoreService vectorStoreService;
    @Resource
    private ILuceneService luceneService;

    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String prefix;

    @Override
    public PageResult<KmcSyncDO> getKmcSyncPage(KmcSyncPageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKmcSync(KmcSyncSaveReqVO createReqVO) {
        KmcSyncDO dictType = BeanUtils.toBean(createReqVO, KmcSyncDO.class);
        baseMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKmcSync(KmcSyncSaveReqVO updateReqVO) {
        KmcSyncDO updateObj = BeanUtils.toBean(updateReqVO, KmcSyncDO.class);
        return baseMapper.updateById(updateObj);
    }

    @Override
    public int removeKmcSync(Collection<Long> idList) {
        return baseMapper.deleteByIds(idList);
    }

    @Override
    public KmcSyncDO getKmcSyncById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 导入文件同步数据
     *
     * @param importExcelList 文件同步数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importKmcSync(List<KmcSyncRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcSyncRespVO respVO : importExcelList) {
            try {
                KmcSyncDO kmcSyncDO = BeanUtils.toBean(respVO, KmcSyncDO.class);
                Long kmcSyncId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcSyncId != null) {
                        KmcSyncDO existingKmcSync = baseMapper.selectById(kmcSyncId);
                        if (existingKmcSync != null) {
                            baseMapper.updateById(kmcSyncDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcSyncId + " 的文件同步记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcSyncId + " 的文件同步记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcSyncDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcSyncId);
                    KmcSyncDO existingKmcSync = baseMapper.selectOne(queryWrapper);
                    if (existingKmcSync == null) {
                        baseMapper.insert(kmcSyncDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcSyncId + " 的文件同步记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcSyncId + " 的文件同步记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }

    /**
     * 创建的数据同步信息
     *
     * @param kmcDocumentDO 文档对象
     * @return 操作是否成功
     */
    @Override
    public Boolean syncToCreate(KmcDocumentDO kmcDocumentDO) {
        if (kmcDocumentDO == null) {
            return false;
        }
        Path tempDir = null;
        try {
            KmcKnowledgeBaseDO knowledgeBase = kmcKnowledgeBaseService.getKmcKnowledgeBaseById(kmcDocumentDO.getKnowledgeBaseId());
            File file = getFile(kmcDocumentDO);

            // 1. 创建一个随机临时目录
            tempDir = Files.createTempDirectory("upload_");

            // 2. 在该目录下创建你想要名字的文件（名字不变！）
            Path targetPath = tempDir.resolve(kmcDocumentDO.getName());
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            File copiedFile = targetPath.toFile();

            // 知识库参数请求对象
            CreateFile createFile = BeanUtils.toBean(kmcDocumentDO, CreateFile.class);
            createFile.setFile(copiedFile);
            createFile.setIndexingTechnique(knowledgeBase.getIndexingTechnique());

            kmcDocumentDO.setSyncStatus(DocumentSyncStatus.IN.code);
            kmcDocumentService.updateById(kmcDocumentDO);
        } catch (Exception e) {
            log.error("同步文件失败：{}", e.getMessage());
            kmcDocumentDO.setSyncStatus(DocumentSyncStatus.ERROR.code);
            kmcDocumentService.updateById(kmcDocumentDO);
        } finally {
            if (tempDir != null) {
                FileUtils.deleteQuietly(tempDir.toFile());
            }
        }

        return true;
    }

    @Override
    public Boolean syncToUpdate(KmcDocumentDO kmcDocumentDO) {
        Path tempDir = null;
        try {
            KmcKnowledgeBaseDO knowledgeBase = kmcKnowledgeBaseService.getKmcKnowledgeBaseById(kmcDocumentDO.getKnowledgeBaseId());

            File file = this.getFile(kmcDocumentDO);

            // 1. 创建一个随机临时目录
            tempDir = Files.createTempDirectory("upload_");
            // 2. 在该目录下创建你想要名字的文件（名字不变！）
            Path targetPath = tempDir.resolve(kmcDocumentDO.getName());
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            File copiedFile = targetPath.toFile();

            // 知识库参数请求对象
            CreateFile createFile = BeanUtils.toBean(kmcDocumentDO, CreateFile.class);
            createFile.setFile(copiedFile);
            createFile.setIndexingTechnique(knowledgeBase.getIndexingTechnique());

            kmcDocumentDO.setSyncStatus(DocumentSyncStatus.IN.code);
            kmcDocumentService.updateById(kmcDocumentDO);
        } catch (Exception e) {
            log.error("同步文件失败：{}", e.getMessage());
            kmcDocumentDO.setSyncStatus(DocumentSyncStatus.ERROR.code);
            kmcDocumentService.updateById(kmcDocumentDO);
        } finally {
            if (tempDir != null) {
                FileUtils.deleteQuietly(tempDir.toFile());
            }
        }
        return true;
    }

    /**
     * 同步删除
     * @param kmcDocumentDO kmc文档对象
     * @return 操作是否成功
     */
    @Override
    public Boolean syncToRemove(KmcDocumentDO kmcDocumentDO) {
        KmcKnowledgeBaseDO knowledgeBase = kmcKnowledgeBaseService.getKmcKnowledgeBaseById(kmcDocumentDO.getKnowledgeBaseId());
        luceneService.deleteByDocumentId(String.valueOf(kmcDocumentDO.getId()));// 删除索引
        this.removeVectorStoreByDocument(knowledgeBase,kmcDocumentDO);// 删除向量数据库
        LambdaQueryWrapper<KmcDocumentSegmentDO> queryWrapper = Wrappers.<KmcDocumentSegmentDO>lambdaQuery()
                .eq(KmcDocumentSegmentDO::getDocumentId, kmcDocumentDO.getId());
        iKmcDocumentSegmentService.remove(queryWrapper);
        baseMapper.deleteById(kmcDocumentDO.getId());
        return true;
    }

    /**
     * 根据知识库id获取文档列表
     */
    @Override
    public List<KmcSyncDO> getSyncByDocumentIdList(Collection<? extends Serializable> idList, String datasetId) {
        return baseMapper.selectList(new LambdaQueryWrapperX<KmcSyncDO>()
                .eqIfPresent(KmcSyncDO::getQmDatasetId, datasetId)
                .inIfPresent(KmcSyncDO::getDocumentId, idList));
    }

    @Override
    public List<KmcSyncDO> getSyncList(List<String> idList, String datasetId) {
        return baseMapper.selectList(new LambdaQueryWrapperX<KmcSyncDO>()
                .eqIfPresent(KmcSyncDO::getQmDatasetId, datasetId)
                .inIfPresent(KmcSyncDO::getQmDocumentId, idList));
    }

    /**
     * 通多定时任务来进行文档的分块操作
     * 定时任务 - 进行文档分块
     */
    @SuppressWarnings("unused")
    public void updateResult() {
        List<KmcDocumentDO> documentDOList = kmcDocumentService.list(new LambdaQueryWrapperX<KmcDocumentDO>()
                .eq(KmcDocumentDO::getSyncStatus, DocumentSyncStatus.IN.code));
        if (documentDOList.isEmpty()) {
            return;
        }
        ContinuousWhitespaceEnricher continuousWhitespaceEnricher = new ContinuousWhitespaceEnricher();
        RemoveUrlAndEmailEnricher removeUrlAndEmailEnricher = new RemoveUrlAndEmailEnricher();

        log.debug("开始同步文档分块状态..............................");
        for (KmcDocumentDO kmcDocumentDO : documentDOList) {
            KmcKnowledgeBaseDO knowledgeBaseDO = kmcKnowledgeBaseService.getById(kmcDocumentDO.getKnowledgeBaseId());
            File file = getFile(kmcDocumentDO);
            GeneralSplitter generalSplitter = new GeneralSplitter(kmcDocumentDO.getSeparator(), kmcDocumentDO.getMaxTokens(),
                    Integer.parseInt(kmcDocumentDO.getChunkOverlap()));
            List<Document> documentList = this.readFile(file);

            // 文档预处理
            if (kmcDocumentDO.getRemoveExtraSpaces()) {
                documentList = continuousWhitespaceEnricher.apply(documentList);
            }
            if (kmcDocumentDO.getRemoveUrlsEmails()) {
                documentList = removeUrlAndEmailEnricher.apply(documentList);
            }

            List<Document> segmentList = generalSplitter.split(documentList);

            // qa 分块
            if (Objects.equals(kmcDocumentDO.getDocForm(), DocFormEnum.QA_MODEL.getType())) {
                // 首先获取chatModel
                ChatModel chatModel = aiModelService.getChatModel(Long.valueOf(kmcDocumentDO.getChatModelProvider()),
                        kmcDocumentDO.getChatModel());
                QuestionAnswerEnricher questionAnswerEnricher = new QuestionAnswerEnricher(chatModel, kmcDocumentDO.getDocLanguage());
                segmentList = questionAnswerEnricher.apply(segmentList);
            }
            // 保存分段
            this.saveSegment(knowledgeBaseDO, kmcDocumentDO, segmentList);

            kmcDocumentDO.setSyncStatus(DocumentSyncStatus.SUCCESS.code);
            kmcDocumentService.updateById(kmcDocumentDO);
            log.debug("结束同步文档分块状态..............................");
        }
    }

    /**
     * 读取文档
     *
     * @param file 文档的文件对象
     * @return 文档内容
     */
    private List<Document> readFile(File file) {
        String s;
        try {
            s = FileReader.safeReadFile(file);
        } catch (TikaException | IOException e) {
            throw new RuntimeException(e);
        }
        Document document = new Document(s);
        return List.of(document);
    }

    /**
     * 从 kmcDocumentDO 中获取具体的文档文件
     *
     * @param kmcDocumentDO kmcDocumentDO 对象
     * @return 具体的文档文件
     */
    private File getFile(KmcDocumentDO kmcDocumentDO) {
        // 使用本地前缀，取出多余的/
        String base = StringUtils.substring(prefix, 0, prefix.length() - 1);
        // 获取文件
        return new File(base + kmcDocumentDO.getPath());
    }

    /**
     * 保存分段信息
     */
    private void saveSegment(KmcKnowledgeBaseDO knowledgeBaseDO, KmcDocumentDO documentDO, List<Document> segmentList) {
        Map<String, Long> map = save2sql(documentDO, segmentList);
        luceneService.saveAiDocumentBatch(map, documentDO, segmentList);
        // 如果是高质量索引，需要往向量数据库中添加
        if (Objects.equals(knowledgeBaseDO.getIndexingTechnique(), "high_quality")) {
            save2VectorStore(map, knowledgeBaseDO, documentDO, segmentList);
        }
    }

    /**
     * 保存到数据库
     *
     * @param documentDO  文档对象
     * @param segmentList 分段列表
     * @return key:向量数据库id, value:文档分段id
     */
    public Map<String, Long> save2sql(KmcDocumentDO documentDO, List<Document> segmentList) {
        List<KmcDocumentSegmentDO> saveList = new ArrayList<>(segmentList.size());
        for (Document document : segmentList) {
            Map<String, Object> metadata = document.getMetadata();

            KmcDocumentSegmentDO segmentDO = new KmcDocumentSegmentDO();
            segmentDO.setWorkspaceId(documentDO.getWorkspaceId());
            segmentDO.setDocumentName(documentDO.getName());
            segmentDO.setDocumentId(documentDO.getId());
            segmentDO.setQmSegmentId(document.getId());
            segmentDO.setPosition(segmentList.indexOf(document) + 1L);
            segmentDO.setContent(document.getText());
            segmentDO.setSignContent(document.getText());
            segmentDO.setAnswer(metadata.get("answer") + "");

            saveList.add(segmentDO);
        }
        LambdaQueryWrapper<KmcDocumentSegmentDO> queryWrapper = Wrappers.<KmcDocumentSegmentDO>lambdaQuery()
                .eq(KmcDocumentSegmentDO::getDocumentId, documentDO.getId());

        iKmcDocumentSegmentService.remove(queryWrapper);// 先删除
        iKmcDocumentSegmentService.saveBatch(saveList);// 后新增
        return saveList.stream()
                .collect(Collectors.toMap(KmcDocumentSegmentDO::getQmSegmentId, KmcDocumentSegmentDO::getId));
    }

    /**
     * 保存到向量数据库
     *
     * @param knowledgeBaseDO 知识库对象
     * @param documentDO      文档对象
     * @param segmentList     分段列表
     */
    /**
     * 保存到向量数据库
     *
     * @param knowledgeBaseDO 知识库对象
     * @param documentDO      文档对象
     * @param segmentList     分段列表
     */
    public void save2VectorStore(Map<String, Long> map, KmcKnowledgeBaseDO knowledgeBaseDO, KmcDocumentDO documentDO,
                                 List<Document> segmentList) {
        EmbeddingModel embeddingModel = aiModelService.getEmbeddingModel(
                Long.valueOf(knowledgeBaseDO.getEmbeddingModelProvider()),
                knowledgeBaseDO.getEmbeddingModel());
        WeaviateVectorStore vectorStore = vectorStoreService.getVectorStore(embeddingModel);

        segmentList.forEach(document -> {
            Long segmentId = map.get(document.getId());
            Map<String, Object> metadata = document.getMetadata();
            metadata.put(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID, documentDO.getKnowledgeBaseId());
            metadata.put(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, documentDO.getId());
            metadata.put(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME, documentDO.getName());
            metadata.put(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, segmentId);
        });

        try {
            // ===================== 1. 先插入（自动创建Schema，解决 no graphql provider） =====================
            List<List<Document>> partitions = Lists.partition(segmentList, 5);
            for (List<Document> partition : partitions) {
                vectorStore.add(partition);
            }
            log.info("文档ID {} 向量数据插入成功", documentDO.getId());

            // ===================== 2. 再删除旧数据（安全删除，不存在不报错） =====================
            FilterExpressionBuilder b = new FilterExpressionBuilder();
            Filter.Expression expression = b.eq(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, documentDO.getId()).build();

            try {
                vectorStore.delete(expression);
                log.info("文档ID {} 旧向量数据已清理", documentDO.getId());
            } catch (Exception e) {
                // 无数据/无Schema 都只打警告，不中断流程
                log.warn("文档ID {} 旧向量数据不存在，无需删除", documentDO.getId());
            }

        } catch (Exception e) {
            log.error("文档ID {} 向量库写入失败：{}", documentDO.getId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据文档id 删除从向量数据库中删除
     *
     * @param knowledgeBaseDO 知识库对象
     * @param documentDO      文档对象
     */
    public void removeVectorStoreByDocument( KmcKnowledgeBaseDO knowledgeBaseDO, KmcDocumentDO documentDO) {
        EmbeddingModel embeddingModel = aiModelService.getEmbeddingModel(
                Long.valueOf(knowledgeBaseDO.getEmbeddingModelProvider()),
                knowledgeBaseDO.getEmbeddingModel());
        WeaviateVectorStore vectorStore = vectorStoreService.getVectorStore(embeddingModel);

        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.eq(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, documentDO.getId()).build();
        try {
            vectorStore.delete(expression);// 删除
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
