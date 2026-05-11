/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.ext.service.extUnstructTask.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.exception.TikaException;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.DateUtils;
import tech.qiantong.qknow.common.utils.FileReader;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.api.enums.AiWorkflowIdEnums;
import tech.qiantong.qknow.module.app.enums.ReleaseStatus;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTask.ExtUnstructTaskMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTaskText.ExtUnstructTaskTextMapper;
import tech.qiantong.qknow.module.ext.extEnum.*;
import tech.qiantong.qknow.module.ext.service.deepke.DeepkeExtractionService;
import tech.qiantong.qknow.module.ext.service.extSchema.IExtSchemaService;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;
import tech.qiantong.qknow.module.ext.service.extSchemaRelation.IExtSchemaRelationService;
import tech.qiantong.qknow.module.ext.service.extTaskLog.IExtTaskLogService;
import tech.qiantong.qknow.module.ext.service.extUnstructTask.IExtUnstructTaskService;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel.IExtUnstructTaskDocRelService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;
import tech.qiantong.qknow.module.ext.service.unstructTaskRelation.IExtUnstructTaskRelationService;
import tech.qiantong.qknow.module.kb.api.flow.dto.KbRuntimeRespDTO;
import tech.qiantong.qknow.module.kb.api.flow.service.IBotApiService;
import tech.qiantong.qknow.module.kg.api.knowledge.IKgKnowledgeApiService;
import tech.qiantong.qknow.module.kg.api.knowledge.dto.KgKnowledgeDocumentRespDTO;
import tech.qiantong.qknow.module.system.service.ISysConfigService;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.domain.relationship.DynamicEntityRelationship;
import tech.qiantong.qknow.neo4j.enums.GraphLabelEnum;
import tech.qiantong.qknow.redis.service.IRedisService;

import jakarta.annotation.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 非结构化抽取任务Service业务层处理
 *
 * @author qknow
 * @date 2025-02-18
 */
@Slf4j
@Service
public class ExtUnstructTaskServiceImpl extends ServiceImpl<ExtUnstructTaskMapper, ExtUnstructTaskDO> implements IExtUnstructTaskService {
    @Resource
    private ExtUnstructTaskMapper extUnstructTaskMapper;
    @Resource
    private IKgKnowledgeApiService kgKnowledgeApiService;
    @Resource
    private IExtUnstructTaskDocRelService extUnstructTaskDocRelService;
    @Resource
    private IExtUnstructTaskRelationService extUnstructTaskRelationService;
    @Resource
    private DeepkeExtractionService deepkeExtractionService;
    @Resource
    private ExtUnstructTaskTextMapper extUnstructTaskTextMapper;
    @Resource
    private IExtSchemaRelationService extSchemaRelationService;
    @Resource
    private IExtSchemaService extSchemaService;
    @Resource
    private ExtNeo4jService extNeo4jService;
    @Resource
    private IRedisService redisService;
    @Resource
    private IExtTaskLogService extTaskLogService;
    @Resource
    private ISysConfigService configService;
    @Resource
    private IExtSchemaAttributeService extSchemaAttributeService;
    @Resource
    private IBotApiService botApiService;

    @Value("${unstruct.type}")
    private String unstructType;
    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String prefix;

    private ExecutorService executor = null;
    // 锁对象：防止并发创建线程池
    private final Object executorLock = new Object();

    /**
     * 执行任务抽取，先放入redis队列中
     *
     * @param createReqVO
     * @return tech.qiantong.qknow.common.core.domain.AjaxResult
     * @author shaun
     * @date 2025/05/26 15:48
     */
    @Override
    public AjaxResult executeExtraction(ExtUnstructTaskSaveReqVO createReqVO) {
        // 任务id
        Long taskId = createReqVO.getId();

        // 根据任务id获取非结构化抽取任务
        ExtUnstructTaskDO unstructTaskDO = extUnstructTaskMapper.selectById(taskId);

        // 校验任务状态
        if (ReleaseStatus.PUBLISHED.getValue().equals(unstructTaskDO.getPublishStatus())) {
            throw new RuntimeException("已发布状态不能重新执行抽取");
        }
        if (ExtTaskStatus.INPROGRESS.getValue().equals(unstructTaskDO.getStatus())) {
            throw new RuntimeException("执行中不能重新执行抽取");
        }
        if (ExtTaskStatus.QUEUE.getValue().equals(unstructTaskDO.getStatus())) {
            throw new RuntimeException("队列中不能重新执行抽取");
        }

        // 校验任务是否关联文件
        ExtUnstructTaskDocRelPageReqVO docRelPageReqVO = new ExtUnstructTaskDocRelPageReqVO();
        docRelPageReqVO.setTaskId(taskId);
        docRelPageReqVO.setDelFlag(false);
        PageResult<ExtUnstructTaskDocRelDO> docRelPage = extUnstructTaskDocRelService.getExtUnstructTaskDocRelPage(docRelPageReqVO);
        if (docRelPage.getRows().size() == 0) {
            return AjaxResult.error("当前任务没有关联需要抽取的文件");
        }

        // 变更任务执行状态
        unstructTaskDO.setStatus(ExtTaskStatus.QUEUE.getValue());
        extUnstructTaskMapper.updateExtUnstructTask(unstructTaskDO);

        // 放入redis队列中, 按顺序 一个一个执行
        redisService.leftPush("ext_unstruck", String.valueOf(taskId));
        return AjaxResult.success("操作成功，已放入队列中，当前队列排队数：" + redisService.getListSize("ext_unstruck"));
    }

    /**
     * 定时任务，消费队列任务, 执行任务抽取
     *
     * @author shaun
     */
    @Override
    public void consumeQueue() {
        int totalThreadNum = getCurrentConcurrentCount();
        // 获取配置的线程数
        String config = configService.selectConfigByKey("ext.thread.concurrency");
        int threadNum = StringUtils.isEmpty(config) ? 1 : Integer.parseInt(config);
        // 限制1~50
        threadNum = Math.max(1, Math.min(50, threadNum));

        while (totalThreadNum < threadNum) {
            // 取出队列中的任务id
            String taskId = redisService.leftPop("ext_unstruck");

            // 判断队列是否为空
            if (StringUtils.isEmpty(taskId)) {
                log.info("队列中没有等待抽取的任务");
                break;
            }
            executeTask(taskId, getOrRebuildExecutor(threadNum));
            totalThreadNum = getCurrentConcurrentCount();
        }
    }

    /**
     * 调用deepke工具，执行抽取任务
     *
     * @param unstructTaskDO
     * @param taskDocRelDOList
     * @return void
     * @author shaun
     * @date 2025/05/27 10:22
     */
    private void execTaskByDeepKe(ExtUnstructTaskDO unstructTaskDO, List<ExtUnstructTaskDocRelDO> taskDocRelDOList, Long logId) throws Exception {
        List<Long> idList = taskDocRelDOList.stream().map(e -> e.getDocId()).collect(Collectors.toList());
        // 获取所有的文件，存放至map中
        List<KgKnowledgeDocumentRespDTO> kgDocumentList = kgKnowledgeApiService.getKgDocumentListByIds(idList);
        Map<Long, KgKnowledgeDocumentRespDTO> documentMap = kgDocumentList.stream()
                .collect(Collectors.toMap(KgKnowledgeDocumentRespDTO::getId, e -> e));

        // 遍历任务关联的文件
        for (ExtUnstructTaskDocRelDO extUnstructTaskDocRelDO : taskDocRelDOList) {
            KgKnowledgeDocumentRespDTO kgDocument = documentMap.get(extUnstructTaskDocRelDO.getDocId());
//
//            // 拼接文件地址
//            String fileUrl = "http://127.0.0.1:8090/profile" + kgDocument.getPath();

            // 删除neo4j中之前抽取相关的数据, 如果有的话
            ExtExtractionDO extractionDO = new ExtExtractionDO();
            extractionDO.setTaskId(unstructTaskDO.getId());
            extNeo4jService.deleteExtUnStruck(extractionDO);
            // 删除mysql中之前抽取的段落相关的数据, 如果有的话
            extUnstructTaskTextMapper.deleteByTaskId(unstructTaskDO.getId());
//
//            // 创建 URL 对象
//            URL url = new URL(fileUrl);
//            // 打开连接
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            // 设置请求方法
//            connection.setRequestMethod("GET");
//            // 获取输入流
//            InputStream inputStream = connection.getInputStream();
//            // 使用 XWPFDocument 解析 docx 文件
//            XWPFDocument document = new XWPFDocument(inputStream);
//            // 获取文档中的段落
//            List<XWPFParagraph> paragraphs = document.getParagraphs();


            // 使用本地前缀，取出多余的/
            String base = StringUtils.substring(prefix, 0, prefix.length() - 1);
            // 获取文件内容
            String fileText = FileReader.safeReadFile(base + kgDocument.getPath());
            // 获取片段
            List<String> paragraphs = FileReader.splitText2(fileText, 5000, "\n\n");

            int i = 0;
            // 输出每个段落的文本内容
            for (String text : paragraphs) {
                i++;
                if (StringUtils.isBlank(text)) {
                    continue;
                }

                text = text.replace("“", "").replace("”", "").replace("'", "").replace("\"", "");
                log.info("============>抽取文本: {}", text);

                log.info("============调用DeepKE工具开始抽取============");
                AjaxResult ajaxResult = deepkeExtractionService.deepkeExtraction(text);
                log.info("============调用DeepKE工具完成============");

                if (ajaxResult.isSuccess()) {
                    log.info("============>抽取文本成功：{}", text);
                    String result = (String) ajaxResult.get("data");
                    String entity = result.substring(result.indexOf("抽取到的实体====>") + 11, result.indexOf("<====抽取到的实体"));
                    entity = entity.replace("'", "\"");
                    log.info("============>抽取到的实体：{}", entity);
                    String triplet = result.substring(result.indexOf("抽取到的三元组====>") + 12, result.indexOf("<====抽取到的三元组"));
                    triplet = triplet.replace("'", "\"");
                    log.info("============>抽取到的三元组：{}", JSONArray.parseArray(triplet));
                    List<ExtExtractionDO> extractionList = JSON.parseArray(triplet, ExtExtractionDO.class);
                    if (extractionList.size() > 0) {
                        for (ExtExtractionDO e : extractionList) {
                            e.setTaskId(unstructTaskDO.getId());
                            e.setDocId(extUnstructTaskDocRelDO.getDocId().intValue());
                            e.setParagraphIndex(i);
                        }

                        //把抽取出来的数据存储到neo4j数据库
                        extNeo4jService.insertExtractionList(extractionList);

                        //把文字信息存储到数据库
                        ExtUnstructTaskTextDO taskTextDO = new ExtUnstructTaskTextDO();
                        taskTextDO.setValidFlag(false);
                        taskTextDO.setDelFlag(false);
                        taskTextDO.setWorkspaceId(unstructTaskDO.getWorkspaceId());
                        taskTextDO.setDocId(extUnstructTaskDocRelDO.getDocId());
                        taskTextDO.setTaskId(unstructTaskDO.getId());
                        taskTextDO.setParagraphIndex(i);
                        taskTextDO.setText(text);
                        taskTextDO.setCreateBy(unstructTaskDO.getUpdateBy());
                        taskTextDO.setUpdateBy(unstructTaskDO.getUpdateBy());
                        taskTextDO.setCreatorId(unstructTaskDO.getUpdaterId());
                        taskTextDO.setUpdaterId(unstructTaskDO.getUpdaterId());
                        taskTextDO.setCreateTime(new Date());
                        taskTextDO.setUpdateTime(new Date());
                        log.info("============>把段落数据添加到数据库:{}", JSONObject.toJSONString(taskTextDO));
                        extUnstructTaskTextMapper.insert(taskTextDO);
                    }
                } else {
                    log.error("============>抽取任务失败: {}", ajaxResult);
                    unstructTaskDO.setStatus(ExtTaskStatus.ERROR.getValue());
                    extUnstructTaskMapper.updateExtUnstructTask(unstructTaskDO);
                }
            }
        }

        // 处理任务完成后的状态更新
        unstructTaskDO.setStatus(ExtTaskStatus.EXECUTED.getValue());
        extUnstructTaskMapper.updateExtUnstructTask(unstructTaskDO);
        log.info("---------- 执行抽取任务结束 -------------");
    }

    /**
     * 调用大模型，执行抽取任务
     *
     * @param unStructTaskDO
     * @param taskDocRelDOList
     * @author shaun
     * @date 2025/06/10 16:25
     */
    @SuppressWarnings("unchecked")
    private void execTaskByModel(ExtUnstructTaskDO unStructTaskDO, List<ExtUnstructTaskDocRelDO> taskDocRelDOList, Long logId, ExecutorService executor) throws TikaException, IOException {
        // 获取任务关联的所有文件
        List<Long> docIdList = taskDocRelDOList.stream().map(ExtUnstructTaskDocRelDO::getDocId).collect(Collectors.toList());
        List<KgKnowledgeDocumentRespDTO> documentList = kgKnowledgeApiService.getKgDocumentListByIds(docIdList);
        // 获取任务关联的所有关系
        List<ExtUnstructTaskRelationDO> relationList = extUnstructTaskRelationService.findByTaskId(unStructTaskDO.getId());
        List<Long> relationIdList = relationList.stream().map(ExtUnstructTaskRelationDO::getRelationId).collect(Collectors.toList());
        List<ExtSchemaRelationDO> schemaRelationList = extSchemaRelationService.listByIds(relationIdList);

        //  动态实体集合
        List<DynamicEntity> graphEntityAll = Lists.newArrayList();
        // 段落文本集合
        Map<String, List<ExtUnstructTaskTextDO>> textMap = Maps.newHashMap();
        // 提示词类型集合
        Map<String, String> typeMap = Maps.newHashMap();
        // 生成提示词
        Map<String, String> callWord = this.callWord(schemaRelationList);
        // 使用本地前缀，取出多余的/
        String base = StringUtils.substring(prefix, 0, prefix.length() - 1);

        List<CompletableFuture<Void>> futures = Lists.newArrayList();
        // 创建任务取消标志
        AtomicBoolean taskCancelled = new AtomicBoolean(false);
        try {
            for (KgKnowledgeDocumentRespDTO documentDO : documentList) {
                List<DynamicEntity> list = Lists.newArrayList();
                int fileIndex = documentList.indexOf(documentDO);
                // 获取文件内容
                String fileText = FileReader.safeReadFile(base + documentDO.getPath());
                // 获取片段
                List<String> segments = FileReader.splitText2(fileText, 5000, "\n\n");

                for (String segment : segments) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        if (taskCancelled.get()) {
                            String count = String.valueOf(getCurrentConcurrentCount() - 1);
                            redisService.set("ext_run_model_count", count);
                            return; // 如果任务已被取消，则直接返回
                        }
                        ServiceException serviceException = new ServiceException();
                        int partIndex = segments.indexOf(segment);
                        String logText = "开始抽取第" + (fileIndex + 1) + "个文件的第" + (partIndex + 1) + "个分段";
                        try {
                            extTaskLogService.recordStep(logId, logText);
                            Map<String, String> entityMap = new HashMap<>();
                            entityMap.put("entityCallWord", callWord.get("entity"));
                            entityMap.put("relationCallWord", callWord.get("relation"));
                            entityMap.put("context", String.join("", segment));
                            entityMap.put("entityAttribute", callWord.get("entityAttributeDesc"));
                            KbRuntimeRespDTO executeFlow = botApiService.executeFlow(AiWorkflowIdEnums.TRIPLE_EXTRACTION.getId()
                                    , unStructTaskDO.getWorkspaceId()
                                    , entityMap);
                            String output = executeFlow.getOutput();
                            Map<String, Object> map = resultUnstruct2(output, unStructTaskDO, documentDO.getId(), callWord);
                            //实体数据
                            if (map.get("entityList") != null) {
                                list.addAll((List<DynamicEntity>) map.get("entityList"));
                            }
                            //实体对应片段
                            if (map.get("textMap") != null) {
                                textMap.putAll((Map<String, List<ExtUnstructTaskTextDO>>) map.get("textMap"));
                            }
                            //实体对应类型
                            if (map.get("typeMap") != null) {
                                typeMap.putAll((Map<String, String>) map.get("typeMap"));
                            }
                        } catch (Exception e) {
                            log.error("结果解析异常:", e.getMessage());
                            serviceException.setMessage("结果解析异常：" + e.getMessage());
                        }

                        if (StringUtils.isNotEmpty(serviceException.getMessage())) {
                            taskCancelled.set(true);
                            String count = String.valueOf(getCurrentConcurrentCount() - 1);
                            redisService.set("ext_run_model_count", count);
                            throw serviceException;
                        }

                        extTaskLogService.recordStep(logId, logText + ",调用大模型抽取完成");
                        // 合并多分段相同name的实体
                        List<DynamicEntity> graphEntityList = mergeEntitiesByName(list, unStructTaskDO.getId());
                        graphEntityAll.addAll(graphEntityList);
                        String count = String.valueOf(getCurrentConcurrentCount() - 1);
                        redisService.set("ext_run_model_count", count);
                    }, executor);
                    futures.add(future);
                }
            }
        } finally {
            // 线程数量
            String count = String.valueOf(getCurrentConcurrentCount() + futures.size());
            redisService.set("ext_run_model_count", count);
            if (getCurrentConcurrentCount() <= 0) {
                executor.shutdown();
            }
        }

        CompletableFuture.runAsync(() -> {
            try {
                // 等待所有片段处理完成
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                processingAndSave(textMap, schemaRelationList, graphEntityAll, unStructTaskDO, typeMap);
                extTaskLogService.endInvoke(logId, ExtLogStatusEnum.SUCCESS, "");
            } catch (Exception e) {
                unStructTaskDO.setStatus(ExtTaskStatus.ERROR.getValue());
                extUnstructTaskMapper.updateExtUnstructTask(unStructTaskDO);
                extTaskLogService.recordStep(logId, e.getMessage());
                extTaskLogService.endInvoke(logId, ExtLogStatusEnum.FAIL, e.getMessage());
            } finally {
                if (getCurrentConcurrentCount() <= 0) {
                    executor.shutdown();
                }
            }
        });
    }


    /**
     * 处理并保存数据
     *
     * @param textMap            存储所有分段文本
     * @param schemaRelationList 存储所有关系
     * @param graphEntityAll     存储所有实体
     * @param unStructTaskDO     unstructTaskDO
     * @param typeMap            提示词类型集合
     */
    private void processingAndSave(Map<String, List<ExtUnstructTaskTextDO>> textMap,
                                   List<ExtSchemaRelationDO> schemaRelationList,
                                   List<DynamicEntity> graphEntityAll,
                                   ExtUnstructTaskDO unStructTaskDO,
                                   Map<String, String> typeMap) {
        // 1. 合并所有 textList 为一个大列表
        List<ExtUnstructTaskTextDO> textAll = new ArrayList<>();
        textMap.values().forEach(textAll::addAll);
        // 2. 关键：给每条数据设置 paragraphIndex（从1开始递增）
        int index = 1;
        for (ExtUnstructTaskTextDO textDO : textAll) {
            textDO.setParagraphIndex(index++);
        }
        // 3. 批量插入数据库
        if (textAll != null && !textAll.isEmpty()) {
            extUnstructTaskTextMapper.insertBatch(textAll);
        }

        // 所有的概念id
        Map<String, ExtSchemaDO> schemaMap = new HashMap<>();
        if (schemaRelationList != null && !schemaRelationList.isEmpty()) {
            List<Long> schemaIdList = schemaRelationList.stream()
                    .flatMap(relation -> java.util.stream.Stream.of(relation.getStartSchemaId(), relation.getEndSchemaId()))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            // 获取所有概念
            List<ExtSchemaDO> extSchemaList = extSchemaService.listByIds(schemaIdList);
            schemaMap = extSchemaList.stream().collect(Collectors.toMap(ExtSchemaDO::getName, Function.identity(), (key1, key2) -> key1));
        }

        // 4. 处理实体关系抽取（修复对象复用BUG + 空安全 + 性能优化）
        List<ExtExtractionDO> extractionDOList = new ArrayList<>();
        if (graphEntityAll != null && !graphEntityAll.isEmpty()) {
            Long taskId = unStructTaskDO.getId();
            for (DynamicEntity entity : graphEntityAll) {
                Map<String, Object> properties = entity.getDynamicProperties();
                // 安全获取 docId
                Integer docId = null;
                Object docIdObj = properties.get("doc_id");
                if (docIdObj != null) {
                    try {
                        docId = Integer.valueOf(docIdObj.toString());
                    } catch (Exception ignored) {}
                }
                // 安全获取 workspaceId
                String workspaceId = "";
                Object workspaceIdObj = properties.get("workspace_id");
                if (workspaceIdObj != null) {
                    workspaceId = workspaceIdObj.toString();
                }

                // 基础实体（每次循环都新建，彻底修复数据覆盖BUG）
                ExtExtractionDO baseDO = new ExtExtractionDO();
                baseDO.setHead(entity.getName());
                baseDO.setTaskId(taskId);
                baseDO.setDocId(docId);
                baseDO.setParagraphIndex(docId);
                baseDO.setWorkspaceId(workspaceId);
                baseDO.setConfidence("");
                if (entity.getName() != null) {
                    // 概念
                    String schemaName = typeMap.get(entity.getName());
                    if (StringUtils.isNotEmpty(schemaName) && schemaMap.get(schemaName) != null) {
                        baseDO.setStartSchemaId(schemaMap.get(schemaName).getId());
                        // 片段关联
                        List<ExtUnstructTaskTextDO> unstructTaskTextList = textMap.get(entity.getName());
                        List<String> textList = unstructTaskTextList.stream().map(ExtUnstructTaskTextDO::getId).map(Object::toString).collect(Collectors.toList());
                        baseDO.setStartTextIds(String.join(",", textList));
                    }
                }

                Map<String, List<DynamicEntityRelationship>> relationshipEntityMap = entity.getRelationshipEntityMap();
                boolean hasMatchRelation = false;

                // 遍历关系配置
                if (schemaRelationList != null && !schemaRelationList.isEmpty() && relationshipEntityMap != null) {
                    for (ExtSchemaRelationDO schemaRelationDO : schemaRelationList) {
                        List<DynamicEntityRelationship> relationList = relationshipEntityMap.get(schemaRelationDO.getRelation());
                        if (relationList == null || relationList.isEmpty()) {
                            continue;
                        }
                        hasMatchRelation = true;
                        // 每条关系新建一个DO，避免复用覆盖
                        for (DynamicEntityRelationship rel : relationList) {
                            ExtExtractionDO copyDO = new ExtExtractionDO();
                            // 手动复制属性（无任何工具类依赖）
                            copyDO.setHead(baseDO.getHead());
                            copyDO.setTaskId(baseDO.getTaskId());
                            copyDO.setDocId(baseDO.getDocId());
                            copyDO.setParagraphIndex(baseDO.getParagraphIndex());
                            copyDO.setWorkspaceId(baseDO.getWorkspaceId());
                            copyDO.setConfidence(baseDO.getConfidence());
                            copyDO.setTail(rel.getEndNode().getName());
                            copyDO.setRelation(schemaRelationDO.getRelation());

                            copyDO.setStartSchemaId(baseDO.getStartSchemaId());
                            copyDO.setStartTextIds(baseDO.getStartTextIds());
                            if (entity.getName() != null) {
                                // 概念
                                String schemaName = typeMap.get(rel.getEndNode().getName());
                                if (StringUtils.isNotEmpty(schemaName) && schemaMap.get(schemaName) != null) {
                                    copyDO.setEndSchemaId(schemaMap.get(schemaName).getId());
                                    // 片段关联
                                    List<ExtUnstructTaskTextDO> unstructTaskTextList = textMap.get(rel.getEndNode().getName());
                                    List<String> textList = unstructTaskTextList.stream().map(ExtUnstructTaskTextDO::getId).map(Object::toString).collect(Collectors.toList());
                                    copyDO.setEndTextIds(String.join(",", textList));
                                }
                            }
                            extractionDOList.add(copyDO);
                        }
                    }
                }
                // 无匹配关系时，添加基础DO
                if (!hasMatchRelation) {
                    extractionDOList.add(baseDO);
                }
            }
        }

        // 批量插入Neo4j
        if (!extractionDOList.isEmpty()) {
            extNeo4jService.insertExtractionList(extractionDOList);
        }

        // 5. 更新任务状态
        unStructTaskDO.setStatus(ExtTaskStatus.EXECUTED.getValue());
        extUnstructTaskMapper.updateExtUnstructTask(unStructTaskDO);
    }


    /**
     * 根据概念关系，生成提示词
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author shaun
     * @date 2025/06/10 17:21
     */
    private Map<String, String> callWord(List<ExtSchemaRelationDO> schemaRelationDOList) {
        // 所有的概念id
        List<Long> idList = schemaRelationDOList.stream()
                .flatMap(relation -> Stream.of(relation.getStartSchemaId(), relation.getEndSchemaId()))
                .collect(Collectors.toList());
        // 获取所有概念
        List<ExtSchemaDO> extSchemaList = extSchemaService.listByIds(idList);
        Map<Long, ExtSchemaDO> schemaMap = extSchemaList.stream().collect(Collectors.toMap(ExtSchemaDO::getId, Function.identity()));

        Set<String> entity = Sets.newHashSet();
        Set<String> relation = Sets.newHashSet();

        for (ExtSchemaRelationDO schemaRelationDO : schemaRelationDOList) {
            // 获取概念
            ExtSchemaDO startExtSchemaDO = schemaMap.get(schemaRelationDO.getStartSchemaId());
            ExtSchemaDO endExtSchemaDO = schemaMap.get(schemaRelationDO.getEndSchemaId());
            // 添加实体和关系
            entity.add(startExtSchemaDO.getName());
            entity.add(endExtSchemaDO.getName());
            relation.add(schemaRelationDO.getRelation());
        }
        // 拼接实体和关系
        String entityStr = String.join("、", entity);
        String relationStr = String.join("、", relation);
        Map<String, String> attributeMap = attributeCallWord(idList);
        Map<String, String> map = Maps.newHashMap();
        map.put("entity", entityStr);
        map.put("relation", relationStr);

        map.putAll(attributeMap);
        return map;
    }

    /**
     * 结果处理
     *
     * @param result
     * @param unstructTaskDO
     * @param docId
     * @param callWord       提示词内容
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author shaun
     * @date 2025/06/10 17:43
     */
    private Map<String, Object> resultUnstruct2(String result, ExtUnstructTaskDO unstructTaskDO, Long docId, Map<String, String> callWord) {
        List<DynamicEntity> list = Lists.newArrayList();
        Map<String, List<ExtUnstructTaskTextDO>> textMap = Maps.newHashMap();
        Map<String, String> typeMap = Maps.newHashMap();
        Map<String, DynamicEntity> entityMap = Maps.newHashMap();
        BeanOutputConverter<JSONObject> beanOutputConverter = new BeanOutputConverter<>(JSONObject.class);
        result = JSONObject.parseObject(result).getString("text");
        JSONObject jsonObject = beanOutputConverter.convert(result);
        // 实体
        JSONArray entities = jsonObject.getJSONArray("entities");
        for (JSONObject entity : entities.toList(JSONObject.class)) {
            String entityName = entity.getString("实体文本");
            // 概念
            String entityType = entity.getString("实体类型");
            typeMap.put(entityName, entityType);
            // 片段
            String segments = entity.getString("片段");
            ExtUnstructTaskTextDO unstructTaskTextDO = this.getExtUnstructTaskTextDO(unstructTaskDO, docId, segments);
            if (textMap.get(entityName) != null) {
                textMap.get(entityName).add(unstructTaskTextDO);
            } else {
                textMap.put(entityName, Lists.newArrayList(unstructTaskTextDO));
            }
            // 属性
            Map<String, Object> properties = this.cover2GraphProperties(entity, callWord);

            // 实体
            DynamicEntity graphEntity = new DynamicEntity();
            System.out.println(GraphLabelEnum.UNSTRUCTURED.getLabel());
            graphEntity.addLabels(GraphLabelEnum.UNSTRUCTURED.getLabel());

            graphEntity.setDynamicProperties(properties);
            graphEntity.putDynamicProperties("name", entityName);
            graphEntity.putDynamicProperties("schemaName", entityType);
            graphEntity.putDynamicProperties("table_name", "ext_unStruct_test");
            graphEntity.setName(entityName);
            graphEntity.putDynamicProperties("doc_id", docId);
            graphEntity.putDynamicProperties("workspace_id", unstructTaskDO.getWorkspaceId());

            list.add(graphEntity);
            entityMap.put(entityName, graphEntity);
        }
        // 关系
        JSONArray relations = jsonObject.getJSONArray("relations");
        for (JSONObject relation : relations.toList(JSONObject.class)) {
            String startEntityName = relation.getString("头实体");
            String relationName = relation.getString("关系");
            String endEntityName = relation.getString("尾实体");
            // 实体和实体的关系
            DynamicEntity startEntity = entityMap.get(startEntityName);
            DynamicEntity endEntity = entityMap.get(endEntityName);
            if (startEntity != null && endEntity != null) {
                startEntity.addRelationship(relationName, endEntity);
            }
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("entityList", list);
        map.put("textMap", textMap);
        map.put("typeMap", typeMap);

        return map;
    }

    /**
     * 合并多分段相同name的实体
     *
     * @param entityList
     * @param taskId
     * @author shaun
     * @date 2025/06/10 17:46
     */
    private List<DynamicEntity> mergeEntitiesByName(List<DynamicEntity> entityList, Long taskId) {
        Map<String, DynamicEntity> mergedMap = Maps.newHashMap();
        Map<DynamicEntity, DynamicEntity> oldToNewMap = new HashMap<>();

        // 第一步：合并实体
        for (DynamicEntity entity : entityList) {
            String name = entity.getName();
            if (mergedMap.containsKey(name)) {
                DynamicEntity existingEntity = mergedMap.get(name);
                existingEntity.getDynamicProperties().putAll(entity.getDynamicProperties());
                if (entity.getRelationshipEntityMap() != null) {
                    existingEntity.mergeRelationshipEntityMap(entity.getRelationshipEntityMap());
                }
                oldToNewMap.put(entity, existingEntity);
            } else {
                DynamicEntity newEntity = new DynamicEntity();
                newEntity.setLabels(entity.getLabels());
                newEntity.setName(name);
                newEntity.putDynamicProperties("task_id", taskId);
                newEntity.setReleaseStatus(ReleaseStatus.UNPUBLISHED.getValue());
                newEntity.putDynamicProperties("entity_type", ExtractType.UNSTRUCTURED.getValue());
                newEntity.getDynamicProperties().putAll(entity.getDynamicProperties());
                if (entity.getRelationshipEntityMap() != null) {
                    newEntity.setRelationshipEntityMap(entity.getRelationshipEntityMap());
                }
                mergedMap.put(name, newEntity);
                oldToNewMap.put(entity, newEntity);
            }

        }
//
//        // 第二步：更新 relationshipEntityMap 中的实体引用
//        for (DynamicEntity newEntity : mergedMap.values()) {
//            Map<String, List<DynamicEntityRelationship>> relatedRelationships = newEntity.getRelationshipEntityMap();
//            for (GraphRelationship relationship : relatedRelationships.get) {
//                GraphEntity oldRelatedEntity = relationship.getEndNode();
//                GraphEntity newRelatedEntity = oldToNewMap.get(oldRelatedEntity);
//                if (newRelatedEntity != null) {
//                    relationship.setEndNode(newRelatedEntity);
//                }
//            }
//        }

        return Lists.newArrayList(mergedMap.values());
    }

    private ExtUnstructTaskTextDO getExtUnstructTaskTextDO(ExtUnstructTaskDO unstructTaskDO, Long docId, String segments) {
        ExtUnstructTaskTextDO unstructTaskTextDO = new ExtUnstructTaskTextDO();
        unstructTaskTextDO.setWorkspaceId(unstructTaskDO.getWorkspaceId());
        unstructTaskTextDO.setDocId(docId);
        unstructTaskTextDO.setTaskId(unstructTaskDO.getId());
        unstructTaskTextDO.setCreateBy(unstructTaskDO.getUpdateBy());
        unstructTaskTextDO.setUpdateBy(unstructTaskDO.getUpdateBy());
        unstructTaskTextDO.setCreatorId(unstructTaskDO.getUpdaterId());
        unstructTaskTextDO.setUpdaterId(unstructTaskDO.getUpdaterId());
        unstructTaskTextDO.setCreateTime(DateUtils.getNowDate());
        unstructTaskTextDO.setUpdateTime(DateUtils.getNowDate());
        unstructTaskTextDO.setText(segments);
        return unstructTaskTextDO;
    }



//
//
//    /**
//     * 执行抽取任务
//     *
//     * @param unStructTaskDO   非结构化抽取任务对象
//     * @param taskDocRelDOList 任务文档对象
//     * @return void
//     * @author shaun
//     * @date 2025/05/27 10:22
//     */
//    private void execExtTask(ExtUnstructTaskDO unStructTaskDO, List<ExtUnstructTaskDocRelDO> taskDocRelDOList,
//                             ExecutorService executor, Long logId) throws Exception {
//        // 获取所有的文件，存放至map中
//        Map<Long, KmcDocumentDO> documentMap = kmcDocumentService.getKmcDocumentMap();
//        List<CompletableFuture<Void>> futures = Lists.newArrayList();
//        // 创建任务取消标志
//        AtomicBoolean taskCancelled = new AtomicBoolean(false);
//        try {
//            // 遍历任务关联的文件
//            for (ExtUnstructTaskDocRelDO extUnstructTaskDocRelDO : taskDocRelDOList) {
//                KmcDocumentDO kmcDocument = documentMap.get(extUnstructTaskDocRelDO.getDocId());
//                int fileIndex = taskDocRelDOList.indexOf(extUnstructTaskDocRelDO);
//                // 拼接文件地址
//                String fileUrl = "http://127.0.0.1:8090/profile" + kmcDocument.getPath();
//
//                // 删除neo4j中之前抽取相关的数据, 如果有的话
//                ExtExtractionDO extractionDO = new ExtExtractionDO();
//                extractionDO.setTaskId(unStructTaskDO.getId());
//                extNeo4jService.deleteExtUnStruck(extractionDO);
//                // 删除mysql中之前抽取的段落相关的数据, 如果有的话
//                extUnstructTaskTextMapper.deleteByTaskId(unStructTaskDO.getId());
//
//                // 创建 URL 对象
//                URL url = new URL(fileUrl);
//                // 打开连接
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                // 设置请求方法
//                connection.setRequestMethod("GET");
//                // 获取输入流
//                InputStream inputStream = connection.getInputStream();
//                // 使用 XWPFDocument 解析 docx 文件
//                XWPFDocument document = new XWPFDocument(inputStream);
//                // 获取文档中的段落
//                List<XWPFParagraph> paragraphs = document.getParagraphs();
//
//                // 输出每个段落的文本内容
//                for (XWPFParagraph paragraph : paragraphs) {
//                    String text = paragraph.getText();
//                    int partIndex = paragraphs.indexOf(paragraph);
//                    if (taskCancelled.get()) {
//                        subCurrentConcurrentCount();
//                        return; // 如果任务已被取消，则直接返回
//                    }
//                    if (StringUtils.isBlank(text)) {
//                        continue;
//                    }
//                    log.info("============>抽取文本: {}", text);
//                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                        log.info("============调用DeepKE工具开始抽取============");
//                        AjaxResult ajaxResult = new AjaxResult();
//                        ServiceException serviceException = new ServiceException();
//                        String logText = "开始抽取第" + (fileIndex + 1) + "个文件的第" + (partIndex + 1) + "个分段";
//                        extTaskLogService.recordStep(logId, logText);
//                        try {
//                            ajaxResult = deepkeExtractionService.deepkeExtraction(text);
//                        } catch (Exception e) {
//                            serviceException.setMessage("结果解析异常" + e.getMessage());
//                        }
//                        log.info("============调用DeepKE工具完成============");
//                        if (StringUtils.isNotEmpty(serviceException.getMessage()) || !ajaxResult.isSuccess()) {
//                            taskCancelled.set(true);
//                            extTaskLogService.recordStep(logId, StrUtil.format("抽取任务失败：{}", ajaxResult));
//                            unStructTaskDO.setStatus(ExtTaskStatus.ERROR.getValue());
//                            extUnstructTaskMapper.updateExtUnstructTask(unStructTaskDO);
//                            subCurrentConcurrentCount();
//                            throw serviceException;
//                        }
//
//                        log.info("============>抽取文本成功：{}", text);
//                        String result = (String) ajaxResult.get("data");
//                        String entity = result.substring(result.indexOf("抽取到的实体====>") + 11, result.indexOf("<====抽取到的实体"));
//                        entity = entity.replace("'", "\"");
//                        log.info("============>抽取到的实体：{}", entity);
//                        String triplet = result.substring(result.indexOf("抽取到的三元组====>") + 12, result.indexOf("<====抽取到的三元组"));
//                        triplet = triplet.replace("'", "\"");
//                        log.info("============>抽取到的三元组：{}", JSONArray.parseArray(triplet));
//                        List<ExtExtractionDO> extractionList = JSON.parseArray(triplet, ExtExtractionDO.class);
//                        if (extractionList.size() > 0) {
//                            for (ExtExtractionDO e : extractionList) {
//                                e.setTaskId(unStructTaskDO.getId());
//                                e.setDocId(extUnstructTaskDocRelDO.getDocId().intValue());
//                                e.setParagraphIndex(partIndex);
//                            }
//
//                            //把抽取出来的数据存储到neo4j数据库
//                            extNeo4jService.insertExtractionList(extractionList);
//
//                            //把文字信息存储到数据库
//                            ExtUnstructTaskTextDO taskTextDO = new ExtUnstructTaskTextDO();
//                            taskTextDO.setValidFlag(false);
//                            taskTextDO.setDelFlag(false);
//                            taskTextDO.setWorkspaceId(unStructTaskDO.getWorkspaceId());
//                            taskTextDO.setDocId(extUnstructTaskDocRelDO.getDocId());
//                            taskTextDO.setTaskId(unStructTaskDO.getId());
//                            taskTextDO.setParagraphIndex(partIndex);
//                            taskTextDO.setText(text);
//                            taskTextDO.setCreateBy(unStructTaskDO.getUpdateBy());
//                            taskTextDO.setUpdateBy(unStructTaskDO.getUpdateBy());
//                            taskTextDO.setCreatorId(unStructTaskDO.getUpdaterId());
//                            taskTextDO.setUpdaterId(unStructTaskDO.getUpdaterId());
//                            taskTextDO.setCreateTime(new Date());
//                            taskTextDO.setUpdateTime(new Date());
//                            log.info("============>把段落数据添加到数据库:{}", JSONObject.toJSONString(taskTextDO));
//                            extUnstructTaskTextMapper.insert(taskTextDO);
//                        }
//                        subCurrentConcurrentCount();
//                    }, executor);
//                    futures.add(future);
//                }
//            }
//        } finally {
//            // 线程数量
//            String count = String.valueOf(getCurrentConcurrentCount() + futures.size());
//            redisService.set("ext_run_model_count", count);
//            if (getCurrentConcurrentCount() <= 0) {
//                executor.shutdown();
//            }
//        }
//
//        CompletableFuture.runAsync(() -> {
//            try {
//                // 等待所有片段处理完成
//                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//                // 处理任务完成后的状态更新
//                unStructTaskDO.setStatus(ExtTaskStatus.EXECUTED.getValue());
//                extUnstructTaskMapper.updateExtUnstructTask(unStructTaskDO);
//                extTaskLogService.endInvoke(logId, ExtLogStatusEnum.SUCCESS, "");
//            } catch (Exception e) {
//                unStructTaskDO.setStatus(ExtTaskStatus.ERROR.getValue());
//                extUnstructTaskMapper.updateExtUnstructTask(unStructTaskDO);
//                extTaskLogService.recordStep(logId, e.getMessage());
//                extTaskLogService.endInvoke(logId, ExtLogStatusEnum.FAIL, e.getMessage());
//            } finally {
//                if (getCurrentConcurrentCount() <= 0) {
//                    executor.shutdown();
//                }
//            }
//        });
//        log.info("---------- 执行抽取任务结束 -------------");
//    }

    @Override
    public AjaxResult getExtExtraction(ExtExtractionDO extExtractionDO) {
        ExtUnstructTaskDO unstructTaskDO = extUnstructTaskMapper.selectById(extExtractionDO.getTaskId());
        AjaxResult ajaxResult = extNeo4jService.getExtExtraction(extExtractionDO);
        if (ajaxResult.isSuccess()) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) ajaxResult.get("data");
            hashMap.put("releaseStatus", unstructTaskDO.getPublishStatus());
            return AjaxResult.success("", hashMap);
        }
        return ajaxResult;
    }

    @Override
    public PageResult<ExtUnstructTaskDO> getExtUnstructTaskPage(ExtUnstructTaskPageReqVO pageReqVO) {
        return extUnstructTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtUnstructTask(ExtUnstructTaskSaveReqVO createReqVO) {
        ExtUnstructTaskDO dictType = BeanUtils.toBean(createReqVO, ExtUnstructTaskDO.class);
        extUnstructTaskMapper.insert(dictType);

        Map<String, Object> params = createReqVO.getParams();
        String docIds = (String) params.get("docIds");
        createReqVO.setId(dictType.getId());
        saveUnstructDocRel(createReqVO, docIds);

        String relationIds = (String) params.get("relationIds");
        this.saveExtUnstructTaskRelation(dictType, relationIds);
        return dictType.getId();
    }

    private void saveExtUnstructTaskRelation(ExtUnstructTaskDO createReqVO, String relationIds) {
        String[] split = StringUtils.split(relationIds, ",");
        for (String relationId : split) {
            ExtUnstructTaskRelationDO extUnstructTaskRelationDO = new ExtUnstructTaskRelationDO();
            extUnstructTaskRelationDO.setTaskId(createReqVO.getId());
            extUnstructTaskRelationDO.setRelationId(Long.parseLong(relationId));
            extUnstructTaskRelationDO.setWorkspaceId(createReqVO.getWorkspaceId());
            extUnstructTaskRelationService.save(extUnstructTaskRelationDO);
        }
    }

    private void saveUnstructDocRel(ExtUnstructTaskSaveReqVO createReqVO, String docIds) {
        String[] split = docIds.split(",");
        List<Long> ids = Arrays.stream(split)
                .map(Long::parseLong) // 将每个字符串转换为 Long
                .collect(Collectors.toList()); // 收集成一个 List<Long>
        if (ids.size() > 0) {
            List<KgKnowledgeDocumentRespDTO> documentListByIds = kgKnowledgeApiService.getKgDocumentListByIds(ids);

            documentListByIds.forEach(e -> {
                ExtUnstructTaskDocRelSaveReqVO docRelSaveReqVO = new ExtUnstructTaskDocRelSaveReqVO();
                docRelSaveReqVO.setWorkspaceId(createReqVO.getWorkspaceId());
                docRelSaveReqVO.setTaskId(createReqVO.getId());
                docRelSaveReqVO.setDocId(e.getId());
                docRelSaveReqVO.setDocName(e.getName());
                docRelSaveReqVO.setCreateBy(createReqVO.getCreateBy());
                docRelSaveReqVO.setCreatorId(createReqVO.getCreatorId());
                docRelSaveReqVO.setCreateTime(createReqVO.getCreateTime());
                docRelSaveReqVO.setUpdateBy(createReqVO.getUpdateBy());
                docRelSaveReqVO.setUpdaterId(createReqVO.getCreatorId());
                docRelSaveReqVO.setUpdateTime(createReqVO.getUpdateTime());
                extUnstructTaskDocRelService.createExtUnstructTaskDocRel(docRelSaveReqVO);
            });
        }
    }

    @Override
    public int updateExtUnstructTask(ExtUnstructTaskSaveReqVO updateReqVO) {
        // 更新非结构化抽取任务
        ExtUnstructTaskDO updateObj = BeanUtils.toBean(updateReqVO, ExtUnstructTaskDO.class);

        Map<String, Object> params = updateReqVO.getParams();
        //删除之前关联的任务文件信息
        String oldDocRelIds = (String) params.get("oldDocRelIds");
        if (StringUtils.isNotBlank(oldDocRelIds)) {
            String[] split = oldDocRelIds.split(",");
            // 转换为 long 类型的数组
            Long[] longArray = Arrays.stream(split)
                    .map(Long::parseLong)
                    .toArray(Long[]::new);
            List<Long> longs = Arrays.asList(longArray);
            extUnstructTaskDocRelService.removeByIds(longs);
        }

        String docIds = (String) params.get("docIds");
        //重新存关联文件
        saveUnstructDocRel(updateReqVO, docIds);
        List<ExtUnstructTaskRelationDO> relationDOList = extUnstructTaskRelationService.findByTaskId(updateObj.getId());
        extUnstructTaskRelationService.removeByIds(relationDOList);
        String relationIds = (String) params.get("relationIds");
        this.saveExtUnstructTaskRelation(updateObj, relationIds);
        return extUnstructTaskMapper.updateById(updateObj);
    }

    @Override
    public int removeExtUnstructTask(Collection<Long> idList) {
        // 批量删除非结构化抽取任务
        return extUnstructTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtUnstructTaskDO getExtUnstructTaskById(Long id) {
        return extUnstructTaskMapper.selectById(id);
    }

    /**
     * 发布
     *
     * @param unstructTaskDO
     * @return
     */
    @Override
    public AjaxResult releaseByTaskId(ExtUnstructTaskDO unstructTaskDO) {
        extUnstructTaskMapper.updateById(unstructTaskDO);
        extNeo4jService.updateByTaskIdAndExtractType(unstructTaskDO.getId(), ExtractType.UNSTRUCTURED.getValue(), ReleaseStatus.PUBLISHED.getValue());
        return AjaxResult.success("发布成功");
    }

    /**
     * 取消发布
     *
     * @param unstructTaskDO
     * @return
     */
    @Override
    public AjaxResult cancelReleaseByTaskId(ExtUnstructTaskDO unstructTaskDO) {
        extUnstructTaskMapper.updateById(unstructTaskDO);
        extNeo4jService.updateByTaskIdAndExtractType(unstructTaskDO.getId(), ExtractType.UNSTRUCTURED.getValue(), ReleaseStatus.UNPUBLISHED.getValue());
        return AjaxResult.success("取消发布成功");
    }

    /**
     * 导入非结构化抽取任务数据
     *
     * @param importExcelList 非结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importExtUnstructTask(List<ExtUnstructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (ExtUnstructTaskRespVO respVO : importExcelList) {
            try {
                ExtUnstructTaskDO extUnstructTaskDO = BeanUtils.toBean(respVO, ExtUnstructTaskDO.class);
                Long extUnstructTaskId = respVO.getId();
                if (isUpdateSupport) {
                    if (extUnstructTaskId != null) {
                        ExtUnstructTaskDO existingExtUnstructTask = extUnstructTaskMapper.selectById(extUnstructTaskId);
                        if (existingExtUnstructTask != null) {
                            extUnstructTaskMapper.updateById(extUnstructTaskDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + extUnstructTaskId + " 的非结构化抽取任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + extUnstructTaskId + " 的非结构化抽取任务记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<ExtUnstructTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", extUnstructTaskId);
                    ExtUnstructTaskDO existingExtUnstructTask = extUnstructTaskMapper.selectOne(queryWrapper);
                    if (existingExtUnstructTask == null) {
                        extUnstructTaskMapper.insert(extUnstructTaskDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + extUnstructTaskId + " 的非结构化抽取任务记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + extUnstructTaskId + " 的非结构化抽取任务记录已存在。");
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
     * 执行单个任务
     *
     * @param taskId 任务ID
     * @return void
     */
    private void executeTask(String taskId, ExecutorService executor) {
        // 获取抽取任务
        ExtUnstructTaskDO unstructTaskDO = extUnstructTaskMapper.selectById(taskId);
        // 更新任务状态
        unstructTaskDO.setStatus(ExtTaskStatus.INPROGRESS.getValue());
        baseMapper.updateById(unstructTaskDO);
        // 开始抽取
        Long logId = extTaskLogService.startInvoke(unstructTaskDO.getWorkspaceId(),
                unstructTaskDO.getId(), unstructTaskDO.getName(), ExtTaskTypeEnum.UN_STRUCT);
        extTaskLogService.recordStep(logId, "非结构化抽取任务开始");

        try {
            // 获取任务关联的文件
            ExtUnstructTaskDocRelPageReqVO docRelPageReqVO = new ExtUnstructTaskDocRelPageReqVO();
            docRelPageReqVO.setTaskId(Long.valueOf(taskId));
            PageResult<ExtUnstructTaskDocRelDO> docRelPage = extUnstructTaskDocRelService.getExtUnstructTaskDocRelPage(docRelPageReqVO);
            List<ExtUnstructTaskDocRelDO> taskDocRelDOList = BeanUtils.toBean(docRelPage.getRows(), ExtUnstructTaskDocRelDO.class);
            // 根据配置文件判断使用哪种方式进行抽取
            if (UnstructTypeEnums.MODEL.eq(unstructType)) {
                // 使用大模型进行抽取
                this.execTaskByModel(unstructTaskDO, taskDocRelDOList, logId, executor);
            } else {
                // 使用DeepKE进行抽取
                this.execTaskByDeepKe(unstructTaskDO, taskDocRelDOList, logId);
            }
        } catch (Exception e) {
            unstructTaskDO.setStatus(ExtTaskStatus.ERROR.getValue());
            extUnstructTaskMapper.updateExtUnstructTask(unstructTaskDO);
            extTaskLogService.recordStep(logId, e.getMessage());
            extTaskLogService.endInvoke(logId, ExtLogStatusEnum.FAIL, e.getMessage());
        }
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
                                public Thread newThread(Runnable r) {
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

    /**
     * 获取当前并发数
     *
     * @author wamg
     */
    private int getCurrentConcurrentCount() {
        String countStr = redisService.get("ext_run_model_count");
        return StringUtils.isEmpty(countStr) ? 0 : Integer.parseInt(countStr);
    }

    /**
     * 当前并发数减一
     */
    private void subCurrentConcurrentCount() {
        int currentConcurrentCount = getCurrentConcurrentCount();
        if (currentConcurrentCount < 1) {
            return;
        }
        String count = String.valueOf(currentConcurrentCount - 1);
        redisService.set("ext_run_model_count", count);
    }



    /**
     * 获取实体属性的提示词
     *
     * @param schemaIdList 实体id 列表
     */
    private Map<String, String> attributeCallWord(List<Long> schemaIdList) {
        JSONObject attributeDesc = new JSONObject();
        JSONObject attributeList = new JSONObject();
        LambdaQueryWrapper<ExtSchemaAttributeDO> queryWrapper = Wrappers.<ExtSchemaAttributeDO>lambdaQuery()
                .in(ExtSchemaAttributeDO::getSchemaId, schemaIdList);
        List<ExtSchemaAttributeDO> attributeDOList = extSchemaAttributeService.list(queryWrapper);
        if (CollUtil.isEmpty(attributeDOList)) {
            return new HashMap<>();
        }
        Map<String, String> result = new HashMap<>(2);
        for (ExtSchemaAttributeDO attributeDO : attributeDOList) {
            JSONArray descArray = attributeDesc.getJSONArray(attributeDO.getSchemaName());
            JSONArray attributeArray = attributeList.getJSONArray(attributeDO.getSchemaName());
            if (Objects.isNull(descArray)) {
                descArray = new JSONArray();
            }
            if (Objects.isNull(attributeArray)) {
                attributeArray = new JSONArray();
            }
            JSONObject obj = new JSONObject();
            obj.put("属性名", attributeDO.getName());
            obj.put("是否必须", Objects.equals(attributeDO.getRequireFlag(), 1) ? "是" : "否");
            obj.put("数据类型", AttributeDataTypeEnums.getCode(attributeDO.getDataType()));
            descArray.add(obj);
            attributeArray.add(JSONObject.from(attributeDO));
            attributeDesc.put(attributeDO.getSchemaName(), descArray);
            attributeList.put(attributeDO.getSchemaName(), attributeArray);
        }
        result.put("entityAttributeDesc", JSONObject.toJSONString(attributeDesc));
        result.put("entityAttributeList", JSONObject.toJSONString(attributeList));
        return result;
    }

    /**
     * 从大模型回答的 json 实体中提取出属性Map
     *
     * @param entity 大模型回答中的实体对象
     * @return DynamicEntity 的 Properties
     */
    private Map<String, Object> cover2GraphProperties(JSONObject entity, Map<String, String> callWord) {
        JSONObject attributeObj = entity.getJSONObject("属性");
        Map<String, Object> result = new HashMap<>();

        JSONObject attribute = JSONObject.parseObject(callWord.get("entityAttributeList"));
        JSONObject kgDictMap = JSONObject.parseObject(callWord.get("kgDictMap"));
        if (Objects.isNull(attribute) || Objects.isNull(kgDictMap)) {
            return new HashMap<>(0);
        }
        JSONArray attributeArray = attribute.getJSONArray(entity.getString("实体类型"));
        if (Objects.isNull(attributeArray) || attributeArray.size() < 1) {
            return new HashMap<>(0);
        }

        return result;
    }







}
