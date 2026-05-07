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

package tech.qiantong.qknow.module.kmc.service.knowledgeSegment.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.ai.constant.WeaviateConstant;
import tech.qiantong.qknow.ai.service.IVectorStoreService;
import tech.qiantong.qknow.common.enums.DataConstant;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment.KmcDocumentSegmentDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeSegment.KmcDocumentSegmentMapper;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeBaseService;
import tech.qiantong.qknow.module.kmc.service.knowledgeSegment.IKmcDocumentSegmentService;
import tech.qiantong.qknow.module.kmc.service.sync.ILuceneService;
import tech.qiantong.qknow.thirdparty.domain.dify.enums.DocFormEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件分段Service业务层处理
 *
 * @author qknow
 * @date 2025-08-28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcDocumentSegmentServiceImpl extends ServiceImpl<KmcDocumentSegmentMapper, KmcDocumentSegmentDO> implements IKmcDocumentSegmentService {
    @Resource
    private KmcDocumentSegmentMapper kmcDocumentSegmentMapper;
    @Resource
    private IKmcDocumentService kmcDocumentService;
    @Resource
    private IAiModelApiService aiModelService;
    @Resource
    private IVectorStoreService vectorStoreService;
    @Resource
    private ILuceneService luceneService;
    @Resource
    private IKmcKnowledgeBaseService iKmcKnowledgeBaseService;

    @Override
    public PageResult<KmcDocumentSegmentDO> getKmcDocumentSegmentPage(KmcDocumentSegmentPageReqVO pageReqVO) {
        return kmcDocumentSegmentMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<KmcDocumentSegmentDO> getKmcDocumentSegmentTreePage(KmcDocumentSegmentPageReqVO pageReqVO) {
        PageResult<KmcDocumentSegmentDO> result = kmcDocumentSegmentMapper.selectPage(pageReqVO);
        List<KmcDocumentSegmentDO> list = result.getList();
        Set<String> collect = list.stream().map(KmcDocumentSegmentDO::getQmSegmentId).collect(Collectors.toSet());
        //获取所有的子节点，组装树形结构
        if (StringUtils.isNotEmpty(collect)) {
            List<KmcDocumentSegmentDO> childList = this.lambdaQuery()
                    .in(KmcDocumentSegmentDO::getParentId, collect)
                    .eq(KmcDocumentSegmentDO::getDelFlag, false)
                    .list();
            list.addAll(childList);
        }
        return result;
    }

    @Override
    public List<KmcDocumentSegmentDO> getAllLevelNodes(Long documentId) {
        return this.lambdaQuery()
                .eq(KmcDocumentSegmentDO::getDocumentId, documentId)
                .eq(KmcDocumentSegmentDO::getDelFlag, false)
                .isNull(KmcDocumentSegmentDO::getParentId)
                .list();
    }

    /**
     * 创建文段分段
     *
     * @param createReqVO 文件分段信息
     * @return 分段信息id
     */
    @Override
    public Long createKmcDocumentSegment(KmcDocumentSegmentSaveReqVO createReqVO) {
        KmcDocumentSegmentDO segmentDO = BeanUtils.toBean(createReqVO, KmcDocumentSegmentDO.class);
        KmcDocumentDO kmcDocument = kmcDocumentService.getById(createReqVO.getDocumentId());
        if (StringUtils.isNull(kmcDocument)) {
            throw new ServiceException("未找到对应的文件信息");
        }
        segmentDO.setQmDocumentId(kmcDocument.getQmDocumentId());
        segmentDO.setDocumentName(kmcDocument.getName());

        kmcDocumentSegmentMapper.insert(segmentDO);// 保存到数据库
        // 高质量模式需要向向量数据库中添加
        KmcKnowledgeBaseDO knowledgeBaseDO = iKmcKnowledgeBaseService.getById(kmcDocument.getKnowledgeBaseId());
        if (Objects.equals(knowledgeBaseDO.getIndexingTechnique(), "high_quality")) {
            save2VectorStore(knowledgeBaseDO, segmentDO, kmcDocument);// 保存到向量数据库
        }
        luceneService.saveSegment(segmentDO, kmcDocument);// 保存到 lucene
        return segmentDO.getId();
    }

    /**
     * 更新分段信息
     *
     * @param updateReqVO 文件分段信息
     * @return 修改的数据条数
     */
    @Override
    public int updateKmcDocumentSegment(KmcDocumentSegmentSaveReqVO updateReqVO) {
        // 更新文件分段
        KmcDocumentSegmentDO updateObj = BeanUtils.toBean(updateReqVO, KmcDocumentSegmentDO.class);
        KmcDocumentSegmentDO kmcDocumentSegment = this.getById(updateObj.getId());
        if (StringUtils.isNull(kmcDocumentSegment)) {
            throw new ServiceException("未找到对应的分段信息");
        }
        KmcDocumentDO kmcDocument = kmcDocumentService.getById(updateReqVO.getDocumentId());
        if (StringUtils.isNull(kmcDocument)) {
            throw new ServiceException("未找到对应的文件信息");
        }
        updateObj.setQmDocumentId(kmcDocument.getQmDocumentId());
        int result = kmcDocumentSegmentMapper.updateById(updateObj);
        // 高质量模式需要向向量数据库中添加
        KmcKnowledgeBaseDO knowledgeBaseDO = iKmcKnowledgeBaseService.getById(kmcDocument.getKnowledgeBaseId());
        if (Objects.equals(knowledgeBaseDO.getIndexingTechnique(), "high_quality")) {
            update2VectorStore(knowledgeBaseDO, updateObj, kmcDocument);// 修改向量数据库
        }
        luceneService.updateSegment(updateObj, kmcDocument);// 修改 lucene
        return result;
    }

    /**
     * 删除文段(并从向量数据库中进行删除)
     *
     * @param idList 文件分段编号
     * @return 删除数据条数
     */
    @Override
    public int removeKmcDocumentSegment(Collection<Long> idList) {
        List<KmcDocumentSegmentDO> kmcDocumentSegmentDOS = baseMapper.selectByIds(idList);
        delete4VectorStore(kmcDocumentSegmentDOS);// 从向量数据库删除片段
        luceneService.deleteSegmentList(kmcDocumentSegmentDOS);// 从 lucene 中删除片段索引
        return kmcDocumentSegmentMapper.deleteByIds(idList);
    }

    @Override
    public KmcDocumentSegmentDO getKmcDocumentSegmentById(Long id) {
        return kmcDocumentSegmentMapper.selectById(id);
    }

    @Override
    public List<KmcDocumentSegmentDO> getKmcDocumentSegmentList() {
        return kmcDocumentSegmentMapper.selectList();
    }

    @Override
    public Map<Long, KmcDocumentSegmentDO> getKmcDocumentSegmentMap() {
        List<KmcDocumentSegmentDO> kmcDocumentSegmentList = kmcDocumentSegmentMapper.selectList();
        return kmcDocumentSegmentList.stream()
                .collect(Collectors.toMap(
                        KmcDocumentSegmentDO::getId,
                        kmcDocumentSegmentDO -> kmcDocumentSegmentDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 导入文件分段数据
     *
     * @param importExcelList 文件分段数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importKmcDocumentSegment(List<KmcDocumentSegmentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcDocumentSegmentRespVO respVO : importExcelList) {
            try {
                KmcDocumentSegmentDO kmcDocumentSegmentDO = BeanUtils.toBean(respVO, KmcDocumentSegmentDO.class);
                Long kmcDocumentSegmentId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcDocumentSegmentId != null) {
                        KmcDocumentSegmentDO existingKmcDocumentSegment = kmcDocumentSegmentMapper.selectById(kmcDocumentSegmentId);
                        if (existingKmcDocumentSegment != null) {
                            kmcDocumentSegmentMapper.updateById(kmcDocumentSegmentDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcDocumentSegmentId + " 的文件分段记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcDocumentSegmentId + " 的文件分段记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcDocumentSegmentDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcDocumentSegmentId);
                    KmcDocumentSegmentDO existingKmcDocumentSegment = kmcDocumentSegmentMapper.selectOne(queryWrapper);
                    if (existingKmcDocumentSegment == null) {
                        kmcDocumentSegmentMapper.insert(kmcDocumentSegmentDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcDocumentSegmentId + " 的文件分段记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcDocumentSegmentId + " 的文件分段记录已存在。");
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
     * 处理分段数据及其子分段
     *
     * @param segmentObject 分段对象
     * @param kmcDocumentDO 文档信息
     * @param segments      结果集合
     * @param parentId      父节点ID
     */
    private void processSegmentRecursively(JSONObject segmentObject, KmcDocumentDO kmcDocumentDO,
                                           List<KmcDocumentSegmentDO> segments, String parentId, String qmDocumentId) {
        // 转换当前节点字段
        convertSegmentFields(segmentObject, kmcDocumentDO, parentId, qmDocumentId);
        segments.add(segmentObject.to(KmcDocumentSegmentDO.class));
        // 递归处理子节点
        Object child = segmentObject.get("child_chunks");
        if (StringUtils.isNotNull(child)) {
            JSONArray childList = segmentObject.getJSONArray("child_chunks");
            for (Object ch : childList) {
                JSONObject childObject = (JSONObject) ch;
                processSegmentRecursively(childObject, kmcDocumentDO, segments,
                        segmentObject.getString("qmSegmentId"), String.valueOf(segmentObject.get("document_id")));
            }
        }
    }

    /**
     * 同步分段数据到数据库
     *
     * @param segments 灵桐获取到的分段信息
     */
    private void syncSegmentsToDatabase(List<KmcDocumentSegmentDO> segments) {
        try {
            Set<String> allSegmentsId = segments.stream()
                    .filter(Objects::nonNull)
                    .map(KmcDocumentSegmentDO::getQmSegmentId)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toSet());

            if (allSegmentsId.isEmpty()) {
                return;
            }
            // 查询现有数据
            List<KmcDocumentSegmentDO> existingList = lambdaQuery()
                    .select(KmcDocumentSegmentDO::getId, KmcDocumentSegmentDO::getQmSegmentId)
                    .in(KmcDocumentSegmentDO::getQmSegmentId, allSegmentsId)
                    .eq(KmcDocumentSegmentDO::getDelFlag, DataConstant.TrueOrFalse.FALSE.getVal())
                    .list();

            if (existingList == null) {
                existingList = Collections.emptyList();
            }

            // 修改为Map<String, Long> 用qmSegmentId作为key，Id作为value
            Map<String, Long> existMap = existingList.stream()
                    .filter(Objects::nonNull)
                    .filter(segment -> StringUtils.isNotEmpty(segment.getQmSegmentId()))
                    .collect(Collectors.toMap(
                            KmcDocumentSegmentDO::getQmSegmentId,
                            KmcDocumentSegmentDO::getId,
                            // 处理重复key的情况，保留第一个
                            (existing, replacement) -> existing
                    ));

            // 分类处理
            List<KmcDocumentSegmentDO> update = new ArrayList<>();
            List<KmcDocumentSegmentDO> insert = new ArrayList<>();

            for (KmcDocumentSegmentDO segment : segments) {
                if (segment == null || StringUtils.isEmpty(segment.getQmSegmentId())) {
                    continue;
                }
                if (existMap.containsKey(segment.getQmSegmentId())) {
                    //设置ID用于更新操作
                    segment.setId(existMap.get(segment.getQmSegmentId()));
                    update.add(segment);
                } else {
                    insert.add(segment);
                }
            }
            // 批量操作
            if (!insert.isEmpty()) {
                baseMapper.insertBatch(insert);
            }
            if (!update.isEmpty()) {
                baseMapper.updateBatch(update);
            }
        } catch (Exception e) {
            log.error("批量同步分段数据到数据库失败", e);
        }
    }

    /**
     * 转换分段对象字段
     *
     * @param segmentObject 分段JSON对象
     * @param kmcDocumentDO 文档信息
     * @param parentId      父节点ID
     * @param qmDocumentId  分段文档ID
     */
    private void convertSegmentFields(JSONObject segmentObject, KmcDocumentDO kmcDocumentDO,
                                      String parentId, String qmDocumentId) {
        // 设置分段ID
        segmentObject.put("qmSegmentId", segmentObject.get("id"));

        // 设置文档ID
        if (StringUtils.isNotNull(segmentObject.get("document_id"))) {
            segmentObject.put("qmDocumentId", segmentObject.get("document_id"));
        } else {
            segmentObject.put("qmDocumentId", qmDocumentId);
        }
        // 移除原始字段
        segmentObject.remove("id");
        segmentObject.remove("documentId");
        // 添加文档相关信息
        segmentObject.put("workspaceId", kmcDocumentDO.getWorkspaceId());
        segmentObject.put("documentName", kmcDocumentDO.getName());
        segmentObject.put("documentId", kmcDocumentDO.getId());
        segmentObject.put("syncStatus", 1);

        // 设置父节点ID
        if (StringUtils.isNotEmpty(parentId)) {
            segmentObject.put("parentId", parentId);
        }
    }

    /**
     * 从向量数据库中批量删除数据
     *
     * @param segmentDOList 文档片段列表
     */
    private void delete4VectorStore(List<KmcDocumentSegmentDO> segmentDOList) {
        KmcDocumentDO kmcDocument = kmcDocumentService.getById(segmentDOList.get(0).getDocumentId());
        KmcKnowledgeBaseDO knowledgeBaseDO = iKmcKnowledgeBaseService.getById(kmcDocument.getKnowledgeBaseId());
        if (!Objects.equals(knowledgeBaseDO.getIndexingTechnique(), "high_quality")) {
            return;
        }
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        KmcDocumentSegmentDO segmentDO = segmentDOList.get(0);
        Filter.Expression expression = b.eq(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, segmentDO.getId())
                .build();
        WeaviateVectorStore vectorStore = this.getVectorStore(knowledgeBaseDO);
        vectorStore.delete(expression);
    }

    /**
     * 获取向量数据库的操作对象
     *
     * @param knowledgeBaseDO 数据对象
     * @return 向量数据库的操作对象
     */
    private WeaviateVectorStore getVectorStore(KmcKnowledgeBaseDO knowledgeBaseDO) {
        EmbeddingModel embeddingModel = aiModelService.getEmbeddingModel(
                Long.valueOf(knowledgeBaseDO.getEmbeddingModelProvider()),
                knowledgeBaseDO.getEmbeddingModel());
        return vectorStoreService.getVectorStore(embeddingModel);
    }

    /**
     * 保存到向量数据库
     *
     * @param segmentDO  片段对象
     * @param documentDO 文档对象
     */
    public void save2VectorStore(KmcKnowledgeBaseDO knowledgeBaseDO,
                                 KmcDocumentSegmentDO segmentDO, KmcDocumentDO documentDO) {
        WeaviateVectorStore vectorStore = getVectorStore(knowledgeBaseDO);
        Document document = segment2AiDocument(segmentDO, documentDO);
        List<Document> documentList = Collections.singletonList(document);
        vectorStore.add(documentList);
    }

    /**
     * 保存到向量数据库
     *
     * @param segmentDO  片段对象
     * @param documentDO 文档对象
     */
    public void update2VectorStore(KmcKnowledgeBaseDO knowledgeBaseDO,
                                   KmcDocumentSegmentDO segmentDO,
                                   KmcDocumentDO documentDO) {
        WeaviateVectorStore vectorStore = getVectorStore(knowledgeBaseDO);
        Document document = segment2AiDocument(segmentDO, documentDO);

        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.eq(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, segmentDO.getId())
                .build();
        vectorStore.delete(expression);

        vectorStore.add(Collections.singletonList(document));
    }

    private Document segment2AiDocument(KmcDocumentSegmentDO segmentDO, KmcDocumentDO documentDO) {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID, documentDO.getKnowledgeBaseId());
        metaData.put(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, documentDO.getId());
        metaData.put(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME, documentDO.getName());
        metaData.put(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, segmentDO.getId());
        if (Objects.equals(documentDO.getDocForm(), DocFormEnum.QA_MODEL.getType())) {
            metaData.put("answer", segmentDO.getAnswer());
        }
        return new Document(segmentDO.getContent(), metaData);
    }
}
