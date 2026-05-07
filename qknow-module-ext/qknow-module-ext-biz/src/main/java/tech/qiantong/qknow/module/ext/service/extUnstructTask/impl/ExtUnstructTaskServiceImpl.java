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

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.app.enums.ReleaseStatus;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTask.ExtUnstructTaskMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTaskText.ExtUnstructTaskTextMapper;
import tech.qiantong.qknow.module.ext.extEnum.*;
import tech.qiantong.qknow.module.ext.service.deepke.DeepkeExtractionService;
import tech.qiantong.qknow.module.ext.service.extTaskLog.IExtTaskLogService;
import tech.qiantong.qknow.module.ext.service.extUnstructTask.IExtUnstructTaskService;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel.IExtUnstructTaskDocRelService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;
import tech.qiantong.qknow.module.ext.service.unstructTaskRelation.IExtUnstructTaskRelationService;
import tech.qiantong.qknow.module.kg.api.knowledge.IKgKnowledgeApiService;
import tech.qiantong.qknow.module.kg.api.knowledge.dto.KgKnowledgeDocumentRespDTO;
import tech.qiantong.qknow.module.system.service.ISysConfigService;
import tech.qiantong.qknow.redis.service.IRedisService;

import jakarta.annotation.Resource;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private ExtNeo4jService extNeo4jService;
    @Resource
    private IRedisService redisService;
    @Value("${unstruct.type}")
    private String unstructType;
    @Resource
    private IExtTaskLogService extTaskLogService;
    @Resource
    private ISysConfigService configService;

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

            // 拼接文件地址
            String fileUrl = "http://127.0.0.1:8095/profile" + kgDocument.getPath();

            // 删除neo4j中之前抽取相关的数据, 如果有的话
            ExtExtractionDO extractionDO = new ExtExtractionDO();
            extractionDO.setTaskId(unstructTaskDO.getId());
            extNeo4jService.deleteExtUnStruck(extractionDO);
            // 删除mysql中之前抽取的段落相关的数据, 如果有的话
            extUnstructTaskTextMapper.deleteByTaskId(unstructTaskDO.getId());

            // 创建 URL 对象
            URL url = new URL(fileUrl);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法
            connection.setRequestMethod("GET");
            // 获取输入流
            InputStream inputStream = connection.getInputStream();
            // 使用 XWPFDocument 解析 docx 文件
            XWPFDocument document = new XWPFDocument(inputStream);
            // 获取文档中的段落
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            int i = 0;
            // 输出每个段落的文本内容
            for (XWPFParagraph paragraph : paragraphs) {
                i++;
                String text = paragraph.getText();
                if (StringUtils.isBlank(text)) {
                    continue;
                }
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
                // TODO 使用大模型抽取，开源版本暂未支持，如有需要请咨询相关管理人员
                log.warn("大模型抽取暂未支持～");
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
}
