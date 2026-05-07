package tech.qiantong.qknow.module.kmc.service.knowledgeBase.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import com.alibaba.cloud.ai.document.DocumentWithScore;
import com.alibaba.cloud.ai.model.RerankRequest;
import com.alibaba.cloud.ai.model.RerankResponse;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.ai.constant.WeaviateConstant;
import tech.qiantong.qknow.ai.enums.model.AiModelTypeEnum;
import tech.qiantong.qknow.ai.service.IVectorStoreService;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;

import tech.qiantong.qknow.common.core.domain.entity.SysRole;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.*;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRecallLogDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRoleDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeBase.KmcKnowledgeBaseMapper;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeBaseService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRecallLogService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRoleService;
import tech.qiantong.qknow.module.system.service.ISysRoleService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.Datasets;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 知识库Service业务层处理
 *
 * @author qknow
 * @date 2025-07-22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcKnowledgeBaseServiceImpl extends ServiceImpl<KmcKnowledgeBaseMapper, KmcKnowledgeBaseDO> implements IKmcKnowledgeBaseService {

    @Resource
    private KmcKnowledgeBaseMapper kmcKnowledgeBaseMapper;
    @Resource
    private IKmcKnowledgeRoleService kmcKnowledgeRoleService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private IKmcDocumentService kmcDocumentService;
    @Resource
    private IAiModelApiService aiModelService;
    @Resource
    private IVectorStoreService vectorStoreService;
    @Resource
    private IKmcKnowledgeRecallLogService kmcKnowledgeRecallLogService;

    private ExecutorService executor = null;
    // 锁对象：防止并发创建线程池
    private final Object executorLock = new Object();

    @Value("${lucene.indexPath}")
    private String indexPath;

    @Override
    public PageResult<KmcKnowledgeBaseDO> getKmcKnowledgeBasePage(KmcKnowledgeBasePageReqVO pageReqVO, Long userId) {
        // 获取角色列表
        List<SysRole> sysRoles = sysRoleService.selectRoleByUserId(userId);
        List<Long> roleIds = sysRoles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        Set<Long> idList = Sets.newHashSet();
        if (!roleIds.isEmpty()) {
            // 获取知识库角色列表
            List<KmcKnowledgeRoleDO> roleDOList = kmcKnowledgeRoleService.list(
                    new LambdaQueryWrapperX<KmcKnowledgeRoleDO>()
                            .inIfPresent(KmcKnowledgeRoleDO::getRoleId, roleIds)
            );
            idList = roleDOList.stream().map(KmcKnowledgeRoleDO::getKnowledgeId).collect(Collectors.toSet());
        }

        // 是否为管理员标识符
        boolean isAdmin = sysRoles.stream()
                .anyMatch(role -> "admin".equals(role.getRoleKey()) || "system".equals(role.getRoleKey())  || "sales".equals(role.getRoleKey()));
        return kmcKnowledgeBaseMapper.selectPage(pageReqVO, idList, userId, isAdmin);
    }

    @Override
    public Long createKmcKnowledgeBase(KmcKnowledgeBaseSaveReqVO createReqVO) {
        validate(createReqVO);

        Datasets datasets = BeanUtils.toBean(createReqVO, Datasets.class);
        // 默认只对当前用户可见,其他成员权限暂不支持
        datasets.setPermission("only_me");
        KmcKnowledgeBaseDO dictType = BeanUtils.toBean(createReqVO, KmcKnowledgeBaseDO.class);
        kmcKnowledgeBaseMapper.insert(dictType);
        return dictType.getId();
    }

    /**
     * 更新知识库
     *
     * @param updateReqVO 知识库信息
     * @return 更新数据条数
     */
    @Override
    public int updateKmcKnowledgeBase(KmcKnowledgeBaseSaveReqVO updateReqVO) {
        validate(updateReqVO);

        Datasets datasets = BeanUtils.toBean(updateReqVO, Datasets.class);
        datasets.setId(updateReqVO.getQmDatasetId());
        // 更新知识库
        KmcKnowledgeBaseDO updateObj = BeanUtils.toBean(updateReqVO, KmcKnowledgeBaseDO.class);
        return kmcKnowledgeBaseMapper.updateById(updateObj);
    }

    /**
     * 批量删除知识库
     *
     * @param idList 知识库编号
     * @return 删除数据条数
     */
    @Override
    public int removeKmcKnowledgeBase(Collection<Long> idList) {
        return kmcKnowledgeBaseMapper.deleteByIds(idList);
    }

    @Override
    public KmcKnowledgeBaseDO getKmcKnowledgeBaseById(Long id) {
        return kmcKnowledgeBaseMapper.selectById(id);
    }

    @Override
    public List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseByIds(List<Long> ids) {
        return kmcKnowledgeBaseMapper.selectByIds(ids);
    }



    @Override
    public List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseList() {
        return kmcKnowledgeBaseMapper.selectList();
    }

    @Override
    public Map<Long, KmcKnowledgeBaseDO> getKmcKnowledgeBaseMap() {
        List<KmcKnowledgeBaseDO> kmcKnowledgeBaseList = kmcKnowledgeBaseMapper.selectList();
        return kmcKnowledgeBaseList.stream()
                .collect(Collectors.toMap(
                        KmcKnowledgeBaseDO::getId,
                        kmcKnowledgeBaseDO -> kmcKnowledgeBaseDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 导入知识库数据
     *
     * @param importExcelList 知识库数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importKmcKnowledgeBase(List<KmcKnowledgeBaseRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcKnowledgeBaseRespVO respVO : importExcelList) {
            try {
                KmcKnowledgeBaseDO kmcKnowledgeBaseDO = BeanUtils.toBean(respVO, KmcKnowledgeBaseDO.class);
                Long kmcKnowledgeBaseId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcKnowledgeBaseId != null) {
                        KmcKnowledgeBaseDO existingKmcKnowledgeBase = kmcKnowledgeBaseMapper.selectById(kmcKnowledgeBaseId);
                        if (existingKmcKnowledgeBase != null) {
                            kmcKnowledgeBaseMapper.updateById(kmcKnowledgeBaseDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcKnowledgeBaseId + " 的知识库记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcKnowledgeBaseId + " 的知识库记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcKnowledgeBaseDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcKnowledgeBaseId);
                    KmcKnowledgeBaseDO existingKmcKnowledgeBase = kmcKnowledgeBaseMapper.selectOne(queryWrapper);
                    if (existingKmcKnowledgeBase == null) {
                        kmcKnowledgeBaseMapper.insert(kmcKnowledgeBaseDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcKnowledgeBaseId + " 的知识库记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcKnowledgeBaseId + " 的知识库记录已存在。");
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
     * 获取向量化模型字典
     *
     * @return 向量化模型字典
     */
    @Override
    public JSONArray getTextEmbedding() {
        return aiModelService.getModelDict(AiModelTypeEnum.EMBEDDING.getType());
    }

    /**
     * 获取 rerank 模型字典
     *
     * @return rerank 模型字典
     */
    @Override
    public JSONArray getRerank() {
        return aiModelService.getModelDict(AiModelTypeEnum.RERANK.getType());
    }

    /**
     * 召回测试
     */
    @Override
    public List<RetrieveResultRespVO> recallTest(RetrieveResultReqVO retrieveResultReqVO) {
        // 添加进检索记录
        KmcKnowledgeRecallLogDO kmcKnowledgeRecallLogDO = new KmcKnowledgeRecallLogDO();
        kmcKnowledgeRecallLogDO.setWorkspaceId(retrieveResultReqVO.getWorkspaceId());
        kmcKnowledgeRecallLogDO.setKnowledgeId(retrieveResultReqVO.getId());
        kmcKnowledgeRecallLogDO.setQuery(retrieveResultReqVO.getQuery());
        kmcKnowledgeRecallLogService.save(kmcKnowledgeRecallLogDO);

        // 混合检索
        if (Objects.equals(retrieveResultReqVO.getSearchMethod(), "hybrid_search")) {
            return hybridSearch(retrieveResultReqVO);
        }

        List<Document> documentList = null;
        if (Objects.equals(retrieveResultReqVO.getSearchMethod(), "full_text_search")) {
            // 全文检索
            documentList = fullTextSearch(retrieveResultReqVO);
        } else if (Objects.equals(retrieveResultReqVO.getSearchMethod(), "semantic_search")) {
            // 向量检索
            documentList = semanticSearch(retrieveResultReqVO);
        }

        assert documentList != null;

        // 使用 reRank 模型进行重排序
        if (retrieveResultReqVO.getRerankingEnable()) {
            documentList = reRank(retrieveResultReqVO, documentList);
        }
        return aiDocument2vo(documentList);
    }

    @Override
    public Boolean updateKnowledgeBaseRole(KmcKnowledgeRoleSaveReqVO kmcKnowledgeRoleSaveReqVO) {
        Long knowledgeId = kmcKnowledgeRoleSaveReqVO.getKnowledgeId();
        String roleIds = kmcKnowledgeRoleSaveReqVO.getRoleIds();
        String[] split = new String[]{};
        if (StringUtils.isNotEmpty(roleIds)) {
            split = StringUtils.split(roleIds, ",");
        }
        kmcKnowledgeRoleService.remove(new LambdaQueryWrapperX<KmcKnowledgeRoleDO>()
                .eqIfPresent(KmcKnowledgeRoleDO::getKnowledgeId, knowledgeId)
                .notInIfPresent(KmcKnowledgeRoleDO::getRoleId, split)
        );
        // 查询数据库中已存在的角色关联
        List<KmcKnowledgeRoleDO> existingRoles = kmcKnowledgeRoleService.list(new LambdaQueryWrapperX<KmcKnowledgeRoleDO>()
                .eqIfPresent(KmcKnowledgeRoleDO::getKnowledgeId, knowledgeId));

        // 构造已存在角色ID的Set，方便快速查找
        Set<Long> existingRoleIds = existingRoles.stream()
                .map(KmcKnowledgeRoleDO::getRoleId)
                .collect(Collectors.toSet());

        // 新增请求中有但数据库中没有的角色关联
        List<KmcKnowledgeRoleDO> newRoles = Lists.newArrayList();
        for (String roleId : split) {
            Long roleIdLong = Long.valueOf(roleId);
            if (!existingRoleIds.contains(roleIdLong)) {
                KmcKnowledgeRoleDO kmcKnowledgeRoleDO = new KmcKnowledgeRoleDO();
                kmcKnowledgeRoleDO.setKnowledgeId(knowledgeId);
                kmcKnowledgeRoleDO.setRoleId(roleIdLong);
                kmcKnowledgeRoleDO.setWorkspaceId(kmcKnowledgeRoleSaveReqVO.getWorkspaceId());
                newRoles.add(kmcKnowledgeRoleDO);
            }
        }
        return kmcKnowledgeRoleService.saveBatch(newRoles);
    }

    @Override
    public List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseList(Long userId, Boolean isValid) {
        // 获取角色列表
        List<SysRole> sysRoles = sysRoleService.selectRoleByUserId(userId);
        List<Long> roleIds = sysRoles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        Set<Long> idList = Sets.newHashSet();
        if (!roleIds.isEmpty()) {
            // 获取知识库角色列表
            List<KmcKnowledgeRoleDO> roleDOList = kmcKnowledgeRoleService.list(
                    new LambdaQueryWrapperX<KmcKnowledgeRoleDO>()
                            .inIfPresent(KmcKnowledgeRoleDO::getRoleId, roleIds)
            );
            idList = roleDOList.stream().map(KmcKnowledgeRoleDO::getKnowledgeId).collect(Collectors.toSet());
        }

        // 是否为管理员标识符
        boolean isAdmin = sysRoles.stream()
                .anyMatch(role -> "admin".equals(role.getRoleKey()) || "system".equals(role.getRoleKey())  || "sales".equals(role.getRoleKey()));
        return kmcKnowledgeBaseMapper.getKmcKnowledgeBaseList(idList, userId, isAdmin, isValid);
    }

    @Override
    public Integer changeKnowledgeValid(KmcKnowledgeBaseSaveReqVO kmcKnowledgeSaveReqVO) {
        // 更新知识库
        KmcKnowledgeBaseDO updateObj = BeanUtils.toBean(kmcKnowledgeSaveReqVO, KmcKnowledgeBaseDO.class);
        return kmcKnowledgeBaseMapper.updateById(updateObj);
    }

    @Override
    public PageResult<KmcKnowledgeBaseRespVO> getKmcKnowledgeBasePageWithFileCount(KmcKnowledgeBasePageReqVO pageReqVO, Long userId) {
        PageResult<KmcKnowledgeBaseDO> page = this.getKmcKnowledgeBasePage(pageReqVO, userId);
        List<Long> idList = page.getList().stream().map(KmcKnowledgeBaseDO::getId).collect(Collectors.toList());
        Map<Long, Integer> fileCount = kmcDocumentService.getFileCount(idList);
        PageResult<KmcKnowledgeBaseRespVO> bean = BeanUtils.toBean(page, KmcKnowledgeBaseRespVO.class);
        bean.getList().forEach(item -> {
            Integer count = fileCount.get(item.getId());
            item.setFileCount(count == null ? 0 : count);
        });
        return bean;
    }

    /**
     * 知识库内容检索
     *
     * @param knowledgeBaseId 知识库id
     * @param query           查询内容
     * @return 知识库检索结果列表
     */
    @Override
    public List<RetrieveResultRespVO> search(Long knowledgeBaseId, String query) {
        KmcKnowledgeBaseDO knowledgeBaseDO = baseMapper.selectById(knowledgeBaseId);
        RetrieveResultReqVO reqVO = new RetrieveResultReqVO();
        reqVO.setId(knowledgeBaseId);
        reqVO.setQuery(query);
        reqVO.setSearchMethod(knowledgeBaseDO.getSearchMethod());
        reqVO.setEmbeddingModel(knowledgeBaseDO.getEmbeddingModel());
        reqVO.setEmbeddingModelProvider(knowledgeBaseDO.getEmbeddingModelProvider());
        reqVO.setRerankingEnable(knowledgeBaseDO.getRerankingEnable());
        reqVO.setRerankingMode(knowledgeBaseDO.getRerankingMode());
        reqVO.setRerankingModelName(knowledgeBaseDO.getRerankingModelName());
        reqVO.setRerankingProviderName(knowledgeBaseDO.getRerankingProviderName());
        reqVO.setKeywordWeight(knowledgeBaseDO.getKeywordWeight());
        reqVO.setVectorWeight(knowledgeBaseDO.getVectorWeight());
        reqVO.setTopK(BigDecimal.valueOf(knowledgeBaseDO.getTopK()));
        reqVO.setScoreThresholdEnabled(knowledgeBaseDO.getScoreThresholdEnabled());
        reqVO.setScoreThreshold(knowledgeBaseDO.getScoreThreshold());
        reqVO.setWorkspaceId(knowledgeBaseDO.getWorkspaceId());

        List<RetrieveResultRespVO> list = new ArrayList<>();
        try {
            list = recallTest(reqVO);
        } catch (Exception e) {
            return list;
        }
        return list;
    }


    /**
     * 校验数据
     *
     * @param kmcKnowledgeBaseDO 校验的数据
     */
    private void validate(KmcKnowledgeBaseSaveReqVO kmcKnowledgeBaseDO) {
        long count = this.count(new LambdaQueryWrapperX<KmcKnowledgeBaseDO>()
                .neIfPresent(KmcKnowledgeBaseDO::getId, kmcKnowledgeBaseDO.getId())
                .eqIfPresent(KmcKnowledgeBaseDO::getName, kmcKnowledgeBaseDO.getName()));
        if (count > 0) {
            throw new ServiceException("名称重复");
        }
    }

    /**
     * 向量检索
     *
     * @param reqVO 检索条件
     * @return 检索结果，会统一封装成为 aiDocument 的形式
     */
    private List<Document> semanticSearch(RetrieveResultReqVO reqVO) {
        int topK = reqVO.getTopK().intValue();
        if (reqVO.getRerankingEnable()) {
            topK = 200;
        }
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.eq(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID, reqVO.getId()).build();
        EmbeddingModel embeddingModel = aiModelService.getEmbeddingModel(
                Long.valueOf(reqVO.getEmbeddingModelProvider()), reqVO.getEmbeddingModel());
        WeaviateVectorStore vectorStore = vectorStoreService.getVectorStore(embeddingModel);
        SearchRequest searchRequest;
        if (reqVO.getScoreThresholdEnabled()) {
            searchRequest = SearchRequest.builder()
                    .filterExpression(expression)
                    .topK(topK)
                    .similarityThreshold(reqVO.getScoreThreshold().doubleValue())
                    .query(reqVO.getQuery())
                    .build();
        } else {
            searchRequest = SearchRequest.builder()
                    .filterExpression(expression)
                    .topK(topK)
                    .query(reqVO.getQuery())
                    .build();
        }

        List<Document> documentList = vectorStore.similaritySearch(searchRequest);
        if (CollUtil.isEmpty(documentList)) {
            return new ArrayList<>();
        }
        return documentList;
    }

    /**
     * 全文检索
     *
     * @param reqVO 检索条件
     * @return 检索结果，会统一封装成为 aiDocument 的形式
     */
    private List<Document> fullTextSearch(RetrieveResultReqVO reqVO) {
        DirectoryReader reader = null;
        List<Document> result = null;
        int topK = reqVO.getTopK().intValue();
        if (reqVO.getRerankingEnable()) {
            topK = 200;
        }
        try {
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            String[] fields = {"content"};
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            // 条件1，搜索关键词
            MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
            Query query = multiFieldQueryParser.parse(reqVO.getQuery());
            // 条件2，数据库id 限制
            Term term = new Term(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID, String.valueOf(reqVO.getId()));
            TermQuery termQuery = new TermQuery(term);

            builder.add(query, BooleanClause.Occur.MUST);
            builder.add(termQuery, BooleanClause.Occur.MUST);

            TopDocs results = searcher.search(builder.build(), topK);
            result = new ArrayList<>();
            ScoreDoc[] scoreDocs = results.scoreDocs;

            for (ScoreDoc scoreDoc : scoreDocs) {
                org.apache.lucene.document.Document searchDocument = searcher.doc(scoreDoc.doc);
                float score = scoreDoc.score;
                if (!reqVO.getScoreThresholdEnabled()) {
                    Document document = luceneDocument2ai(searchDocument, score);
                    result.add(document);
                } else {
                    if (score > reqVO.getScoreThreshold().floatValue()) {
                        Document document = luceneDocument2ai(searchDocument, score);
                        result.add(document);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!Objects.isNull(reader)) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 混合检索
     *
     * @param reqVO 检索条件
     * @return 检索结果列表
     */
    private List<RetrieveResultRespVO> hybridSearch(RetrieveResultReqVO reqVO) {
        ExecutorService executorService = getOrRebuildExecutor(3);
        Future<List<Document>> semanticSubmit = executorService.submit(() -> semanticSearch(reqVO));
        Future<List<Document>> fullTextSubmit = executorService.submit(() -> fullTextSearch(reqVO));
        List<Document> semanticList = null;// 向量检索结果
        List<Document> fullTextList = null;// 全文检索结果
        try {
            semanticList = semanticSubmit.get();
            fullTextList = fullTextSubmit.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Objects.equals(reqVO.getRerankingMode(), "weighted_score")) {// 权重设置
            List<RetrieveResultRespVO> result = new ArrayList<>();
            // 首先进行得分的计算，得分值*权重
            if (CollUtil.isNotEmpty(semanticList)) {
                semanticList.forEach(document -> {
                    RetrieveResultRespVO respVO = aiDocument2vo(document);
                    respVO.setScore(reqVO.getVectorWeight().multiply(BigDecimal.valueOf(respVO.getScore())).doubleValue());
                    result.add(respVO);
                });
            }
            if (CollUtil.isNotEmpty(fullTextList)) {
                fullTextList.forEach((document -> {
                    RetrieveResultRespVO respVO = aiDocument2vo(document);
                    BigDecimal bigDecimal = BigDecimal.valueOf((Float) document.getMetadata().get("score"));
                    respVO.setScore(bigDecimal.doubleValue());
                    respVO.setScore(reqVO.getKeywordWeight().multiply(BigDecimal.valueOf(respVO.getScore())).doubleValue());
                    result.add(respVO);
                }));
            }
            // 去重，然后获取前 n 个数据
            return deWeightAndSort(result, reqVO);
        } else if (Objects.equals(reqVO.getRerankingMode(), "reranking_model")) {// rerank 设置
            List<Document> result = new ArrayList<>();
            if (CollUtil.isNotEmpty(semanticList)) {
                result.addAll(semanticList);
            }
            if (CollUtil.isNotEmpty(fullTextList)) {
                result.addAll(fullTextList);
            }
            List<Document> documentList = reRank(reqVO, result);
            return aiDocument2vo(documentList);
        }
        return new ArrayList<>();
    }

    /**
     * 将 document 转换为 请求结果对象
     *
     * @param document ai 文档
     * @return 请求结果对象
     */
    private RetrieveResultRespVO aiDocument2vo(Document document) {
        RetrieveResultRespVO result = new RetrieveResultRespVO();
        Map<String, Object> metadata = document.getMetadata();
        result.setId(String.valueOf(metadata.get(WeaviateConstant.METADATA_FIELD_SEGMENT_ID)));
        result.setDocumentId(String.valueOf(metadata.get(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID)));
        result.setContent(document.getText());
        result.setAnswer(String.valueOf(metadata.get("answer")));
        result.setWordCount(document.getText().length());
        result.setDocumentName(String.valueOf(metadata.get(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME)));
        Double score = document.getScore();
        result.setScore(score);
        return result;
    }

    /**
     * 将 document 转换为 请求结果列表
     *
     * @param documentList 文档列表
     * @return 请求结果的列表
     */
    private List<RetrieveResultRespVO> aiDocument2vo(List<Document> documentList) {
        if (CollUtil.isEmpty(documentList)) {
            return new ArrayList<>(0);
        }

        List<RetrieveResultRespVO> result = new ArrayList<>(documentList.size());
        for (Document document : documentList) {
            result.add(aiDocument2vo(document));
        }
        return result;
    }

    /**
     * 将 lucene 的 document 对象转为 springai 的 document 对象
     *
     * @param document lucene 的document对象
     * @param score    得分数值
     * @return springai 的 document 对象
     */
    private Document luceneDocument2ai(org.apache.lucene.document.Document document, float score) {
        Map<String, Object> mateData = new HashMap<>();
        mateData.put("score", score);

        mateData.put(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID, document.get(WeaviateConstant.METADATA_FIELD_KNOWLEDGE_BASE_ID));
        mateData.put(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID, document.get(WeaviateConstant.METADATA_FIELD_DOCUMENT_ID));
        mateData.put(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME, document.get(WeaviateConstant.METADATA_FIELD_DOCUMENT_NAME));
        mateData.put(WeaviateConstant.METADATA_FIELD_SEGMENT_ID, document.get(WeaviateConstant.METADATA_FIELD_SEGMENT_ID));
        String answer = document.get("answer");
        if (StrUtil.isNotBlank(answer)) {
            mateData.put("answer", answer);
        }
        return Document.builder()
                .text(document.get("content"))
                .score(BigDecimal.valueOf(score).doubleValue()).metadata(mateData)
                .build();
    }

    /**
     * 使用 rerank 模型重排序
     *
     * @param reqVO        封装的查询条件
     * @param documentList 排序前的文档列表
     * @return 排序后的文档列表
     */
    private List<Document> reRank(RetrieveResultReqVO reqVO, List<Document> documentList) {
        if (CollUtil.isEmpty(documentList)) {
            return new ArrayList<>(0);
        }
        List<Document> result = new ArrayList<>();
        DashScopeRerankModel rerankModel = aiModelService.getRerankModel(
                Long.valueOf(reqVO.getRerankingProviderName()),
                reqVO.getRerankingModelName());
        RerankRequest rerankRequest = new RerankRequest(reqVO.getQuery(), documentList);
        RerankResponse rerankResponse = rerankModel.call(rerankRequest);

        List<DocumentWithScore> reranResultList = rerankResponse.getResults();
        if (reqVO.getScoreThresholdEnabled()) {
            for (DocumentWithScore documentWithScore : reranResultList) {
                if (documentWithScore.getScore() > reqVO.getScoreThreshold().doubleValue()) {
                    Document output = documentWithScore.getOutput();
                    Document build = Document.builder()
                            .id(output.getId())
                            .text(output.getText())
                            .score(documentWithScore.getScore())
                            .metadata(output.getMetadata()).build();
                    result.add(build);
                }
            }
        } else {
            for (DocumentWithScore documentWithScore : reranResultList) {
                Document output = documentWithScore.getOutput();
                Document build = Document.builder()
                        .id(output.getId())
                        .text(output.getText())
                        .score(documentWithScore.getScore())
                        .metadata(output.getMetadata()).build();
                result.add(build);
            }
        }
        return result.stream()
                .sorted(Comparator.comparingDouble(Document::getScore).reversed())
                .limit(reqVO.getTopK().intValue())
                .toList();
    }

    /**
     * 去重、排序，并获取前 n 个值
     *
     * @param list  列表对象
     * @param reqVO 查询条件
     * @return 排序后的前 n 个值
     */
    private List<RetrieveResultRespVO> deWeightAndSort(List<RetrieveResultRespVO> list, RetrieveResultReqVO reqVO) {
        Map<String, RetrieveResultRespVO> map = new HashMap<>();
        for (RetrieveResultRespVO vo : list) {
            RetrieveResultRespVO mapValue = map.get(vo.getId());
            if (!Objects.isNull(mapValue)) {// 包含
                vo.setScore(mapValue.getScore() + vo.getScore());
            }
            map.put(vo.getId(), vo);
        }
        if (reqVO.getScoreThresholdEnabled()) {
            return map.values().stream()
                    .filter(respVO -> reqVO.getScoreThreshold().compareTo(BigDecimal.valueOf(respVO.getScore())) <= 0)
                    .sorted(Comparator.comparingDouble(RetrieveResultRespVO::getScore).reversed())
                    .limit(reqVO.getTopK().intValue())
                    .toList();
        }
        return map.values().stream()
                .sorted(Comparator.comparingDouble(RetrieveResultRespVO::getScore).reversed())
                .limit(reqVO.getTopK().intValue())
                .toList();

    }

    /**
     * 获取或重建线程池（核心逻辑：存在且存活则复用，否则重建）
     *
     * @param threadNum 配置的线程数
     * @return 可用的线程池
     */
    private ExecutorService getOrRebuildExecutor(int threadNum) {
        // 双重检查锁：避免并发创建
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            synchronized (executorLock) {
                if (executor == null || executor.isShutdown() || executor.isTerminated()) {
                    executor = new ThreadPoolExecutor(
                            threadNum,  // 核心线程数=配置数
                            threadNum,  // 固定线程池，最大=核心
                            60L, TimeUnit.SECONDS,  // 空闲线程存活时间（固定线程池可设长一点）
                            new LinkedBlockingQueue<>(1000),  // 有界队列，避免OOM
                            new ThreadFactory() {  // 自定义线程名，便于排查
                                private final AtomicInteger count = new AtomicInteger(1);

                                @Override
                                public Thread newThread(@NotNull Runnable r) {
                                    return new Thread(r, "ext-task-thread-" + count.getAndIncrement());
                                }
                            },
                            new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略：调用方执行，避免任务丢失
                    );
                }
            }
        }
        return executor;
    }
}
