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

package tech.qiantong.qknow.module.kmc.service.knowledgeSegment.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import tech.qiantong.qknow.module.kmc.service.knowledgeSegment.bo.*;
import tech.qiantong.qknow.module.kmc.service.sync.ILuceneService;
import tech.qiantong.qknow.redis.service.IRedisService;
import tech.qiantong.qknow.thirdparty.domain.dify.enums.DocFormEnum;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
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
    @Resource
    private IRedisService redisService;

    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String prefix;


    private final String CHUNK_FILE_PREFIX = "chunk_";
    // 缓冲区 8MB，可按需调整
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;

    @Override
    public PageResult<KmcDocumentSegmentDO> getKmcDocumentSegmentPage(KmcDocumentSegmentPageReqVO pageReqVO) {
        return kmcDocumentSegmentMapper.selectPage(pageReqVO);
    }

    /**
     * 获取下载分页列表
     *
     * @param page     分页数据
     * @param configBO 下载配置
     * @return 文件分段分页列表
     */
    @Override
    public IPage<JSONObject> queryDownloadPage(IPage<JSONObject> page, DownloadJsonConfigBO configBO) {
        LambdaQueryWrapper<KmcDocumentSegmentDO> queryWrapper = Wrappers.lambdaQuery(KmcDocumentSegmentDO.class);
        if(Objects.equals(configBO.getIdType(), "document")){
            queryWrapper.in(KmcDocumentSegmentDO::getDocumentId, configBO.getDocumentIdList());
        }else {
            queryWrapper.in(KmcDocumentSegmentDO::getId, configBO.getDocumentIdList());
        }

        IPage<KmcDocumentSegmentDO> DOPage = new Page<>(page.getCurrent(), page.getSize());
        IPage<KmcDocumentSegmentDO> DOResultPage = super.page(DOPage, queryWrapper);
        if (CollUtil.isEmpty(DOResultPage.getRecords())) {
            page.setRecords(null);
            return page;
        }

        List<JSONObject> jsonObjectList;
        if (Objects.equals(configBO.getJsonStyle(), "Alpaca")) {
            jsonObjectList = toAlpacaJsonList(DOResultPage.getRecords());
        } else if (Objects.equals(configBO.getJsonStyle(), "ShareGPT")) {
            jsonObjectList = toShareGPTJsonList(DOResultPage.getRecords());
        } else {
            jsonObjectList = toMultilingualThinkingJsonList(DOResultPage.getRecords());
        }

        page.setRecords(jsonObjectList);
        return page;
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
     * 创建文段分段
     *
     * @param vectorStore 向量数据库
     * @param knowledgeBaseDO 知识库
     * @param kmcDocument 文件信息
     * @param segmentDO 文件分段信息
     * @return 分段信息id
     */
    @Override
    public Long createKmcDocumentSegment(WeaviateVectorStore vectorStore,
                                         KmcKnowledgeBaseDO knowledgeBaseDO,
                                         KmcDocumentDO kmcDocument,
                                         KmcDocumentSegmentDO segmentDO) {
        segmentDO.setQmDocumentId(kmcDocument.getQmDocumentId());
        segmentDO.setDocumentName(kmcDocument.getName());
        segmentDO.setWorkspaceId(kmcDocument.getWorkspaceId());
        segmentDO.setDocumentId(kmcDocument.getId());
        kmcDocumentSegmentMapper.insert(segmentDO);// 保存到数据库
        if (Objects.equals(knowledgeBaseDO.getIndexingTechnique(), "high_quality")) {
            save2VectorStore(vectorStore, segmentDO, kmcDocument);// 保存到向量数据库
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

    /**
     * 获取已上传的索引
     *
     * @param fileMd5 文件md5
     * @return 已上传的索引
     */
    @Override
    public List<Integer> getUploadedIndex(String fileMd5) {
        Path chunkDir = Paths.get(getChunkDir(), fileMd5);
        if (!Files.exists(chunkDir)) {
            return new ArrayList<>(0);
        }
        File file = chunkDir.toFile();
        File[] files = file.listFiles();
        if (Objects.isNull(files)) {
            return new ArrayList<>(0);
        }
        List<Integer> indexList = new ArrayList<>(files.length);
        for (File chunkFile : files) {
            String fileName = chunkFile.getName();
            if (fileName.startsWith(CHUNK_FILE_PREFIX)) {
                String indexStr = fileName.replace(CHUNK_FILE_PREFIX, "");
                indexList.add(Integer.parseInt(indexStr));
            }
        }
        return indexList;
    }

    /**
     * 保存分片
     *
     * @param chunk      分片
     * @param fileMd5    文件md5
     * @param chunkIndex 分片索引
     * @return 是否保存成功
     */
    @Override
    public Boolean saveChunk(MultipartFile chunk, String fileMd5, Integer chunkIndex) throws IOException {
        // 分片保存路径
        Path chunkDir = Paths.get(getChunkDir(), fileMd5);
        if (!Files.exists(chunkDir)) {
            Files.createDirectories(chunkDir);
        }
        Path chunkPath = chunkDir.resolve(CHUNK_FILE_PREFIX + chunkIndex);
        // 写入分片文件
        chunk.transferTo(chunkPath);
        return true;
    }

    /**
     * 合并分片
     *
     * @param md5      文件md5
     * @param fileName 文件名
     * @param total    分片总数
     * @return 是否合并成功
     */
    @Override
    public String mergeChunk(String md5, String fileName, Integer total) throws IOException {
        Path chunkDir = Paths.get(getChunkDir(), md5);
        String baseDir = getChunkDir();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/");

        String targetPath = baseDir.concat("/").concat(formatter.format(new Date()));
        String targetName = md5 + getFileSuffix(fileName);
        // 构造路径
        Path targetFile = Paths.get(targetPath, targetName);

        // 创建父目录，不存在则新建
        Path parent = targetFile.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent); // 递归创建多级目录
        }
        // NIO零拷贝合并所有分片
        try (FileChannel out = FileChannel.open(targetFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            for (int i = 0; i < total; i++) {
                Path chunkFile = chunkDir.resolve(CHUNK_FILE_PREFIX + i);
                try (FileChannel in = FileChannel.open(chunkFile, StandardOpenOption.READ)) {
                    in.transferTo(0, in.size(), out);
                }
                Files.delete(chunkFile); // 合并后删除分片
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Files.delete(chunkDir);
        // 校验文件
        String fileMD5 = getFileMD5(targetFile);
        if (!Objects.equals(md5, fileMD5)) {
            throw new ServiceException("");
        }
        Path baseDirPath = Paths.get(baseDir);
        String s = targetFile.toString();
        return s.replace(baseDirPath.toString(), "");
    }

    /**
     * 生成json文件
     *
     * @param configBO 配置信息
     * @return json文件
     */
    @Override
    public String genJsonFile(DownloadJsonConfigBO configBO) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String key = StrUtil.format(DOWNLOAD_FILE_ID_FORMAT, uuid);
        redisService.set(key, JSONObject.toJSONString(configBO), DOWNLOAD_FILE_TIME_OUT);
        return uuid;
    }

    /**
     * 获取分段数量
     *
     * @param documentId 文件id
     * @return 分段数量
     */
    @Override
    public Long getSegmentCount(Long documentId) {
        LambdaQueryWrapper<KmcDocumentSegmentDO> queryWrapper = Wrappers.lambdaQuery(KmcDocumentSegmentDO.class)
                .eq(KmcDocumentSegmentDO::getDocumentId, documentId);
        return super.count(queryWrapper);
    }

    /**
     * 转换为Alpaca 风格的数据
     *
     * @param segmentDOList 文件分段列表
     * @return Alpaca格式列表
     */
    private List<JSONObject> toAlpacaJsonList(List<KmcDocumentSegmentDO> segmentDOList) {
        List<JSONObject> resultList = new ArrayList<>(segmentDOList.size());
        for (KmcDocumentSegmentDO segmentDO : segmentDOList) {
            AlpacaBO alpacaBO = new AlpacaBO();
            if(StrUtil.isBlank(segmentDO.getAnswer()) || Objects.equals(segmentDO.getAnswer(), "null")){
                alpacaBO.setInstruction("");
                alpacaBO.setOutput(segmentDO.getContent());
            }else {
                alpacaBO.setInstruction(segmentDO.getContent());
                alpacaBO.setOutput(segmentDO.getAnswer());
            }

            alpacaBO.setSystem("");
            alpacaBO.setInput("");
            resultList.add(JSONObject.from(alpacaBO, JSONWriter.Feature.WriteNonStringValueAsString));
        }
        return resultList;
    }

    /**
     * 转换为ShareGPT风格数据
     *
     * @param segmentDOList 文件分段列表
     * @return ShareGPT格式列表
     */
    private List<JSONObject> toShareGPTJsonList(List<KmcDocumentSegmentDO> segmentDOList) {
        List<JSONObject> resultList = new ArrayList<>(segmentDOList.size());
        for (KmcDocumentSegmentDO segmentDO : segmentDOList) {
            ShareGPTBO shareGPTBO = new ShareGPTBO();
            ShareGPTMessageBO userMessageBO = new ShareGPTMessageBO();
            ShareGPTMessageBO assistantMessageBO = new ShareGPTMessageBO();

            userMessageBO.setRole("user");
            if(StrUtil.isBlank(segmentDO.getAnswer()) || Objects.equals(segmentDO.getAnswer(), "null")){
                userMessageBO.setContent("");
                assistantMessageBO.setContent(segmentDO.getContent());
            }else {
                userMessageBO.setContent(segmentDO.getContent());
                assistantMessageBO.setContent(segmentDO.getAnswer());
            }
            assistantMessageBO.setRole("assistant");
            shareGPTBO.setMessages(Arrays.asList(userMessageBO, assistantMessageBO));
            resultList.add(JSONObject.from(shareGPTBO));
        }
        return resultList;
    }

    /**
     * 转换为 MultilingualThinking 风格数据
     *
     * @param segmentDOList 文件分段列表
     * @return 多语言思考格式列表
     */
    private List<JSONObject> toMultilingualThinkingJsonList(List<KmcDocumentSegmentDO> segmentDOList) {
        List<JSONObject> resultList = new ArrayList<>(segmentDOList.size());
        for (KmcDocumentSegmentDO segmentDO : segmentDOList) {
            MultilingualThinkingBO multilingualThinkingBO = new MultilingualThinkingBO();
            MultilingualThinkingMessageBO userMessageBO = new MultilingualThinkingMessageBO();
            MultilingualThinkingMessageBO assistantMessageBO = new MultilingualThinkingMessageBO();

            userMessageBO.setRole("user");
            assistantMessageBO.setRole("assistant");

            if(StrUtil.isBlank(segmentDO.getAnswer()) || Objects.equals(segmentDO.getAnswer(), "null")){
                userMessageBO.setContent("");
                assistantMessageBO.setContent(segmentDO.getContent());
            }else {
                userMessageBO.setContent(segmentDO.getContent());
                assistantMessageBO.setContent(segmentDO.getAnswer());
                assistantMessageBO.setThinking(segmentDO.getThinking());
            }

            multilingualThinkingBO.setReasoning_language("");
            multilingualThinkingBO.setDeveloper("");
            multilingualThinkingBO.setUser(segmentDO.getContent());
            multilingualThinkingBO.setAnalysis(segmentDO.getThinking());
            multilingualThinkingBO.setMessages(List.of(userMessageBO, assistantMessageBO));
            JSONObject jsonObject = JSONObject.from(multilingualThinkingBO);
            jsonObject.put("final", segmentDO.getAnswer());// final 是关键字，只能在 JSONObject 中使用

            resultList.add(jsonObject);
        }
        return resultList;
    }

    /**
     * 获取文件MD5字符串（小写32位）
     */
    public static String getFileMD5(Path filePath) throws IOException {
        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            throw new IOException("文件不存在或为目录：" + filePath);
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try (FileChannel channel = FileChannel.open(filePath)) {
            while (channel.read(buffer) != -1) {
                buffer.flip();
                md5.update(buffer);
                buffer.clear();
            }
        }
        return bytesToHex(md5.digest());
    }

    /**
     * byte数组转32位小写MD5字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
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
    public void save2VectorStore(WeaviateVectorStore vectorStore,
                                 KmcDocumentSegmentDO segmentDO,
                                 KmcDocumentDO documentDO) {
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

    /**
     * 获取文件后缀（带点 .zip .txt）
     * 无后缀返回空字符串
     */
    public static String getFileSuffix(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return "";
    }

    private String getChunkDir() {
//    private final String CHUNK_DIR = "data/upload/temp/";
        return StringUtils.substring(prefix, 0, prefix.length() - 1);
    }
}
