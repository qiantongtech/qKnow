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

package tech.qiantong.qknow.module.kmc.service.kmcDocument.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.utils.FileReader;
import tech.qiantong.qknow.common.utils.file.FileUtils;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.DocumentTrackEnum;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentTrackReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.enums.DocumentSyncStatus;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcDocumentLog.KmcDocumentLogDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.sync.KmcSyncDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.kmcDocument.KmcDocumentMapper;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.module.kmc.service.kmcDocumentLog.IKmcDocumentLogService;
import tech.qiantong.qknow.module.kmc.service.sync.IKmcSyncService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qknow.mybatis.core.query.QueryWrapperX;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 知识文件Service业务层处理
 *
 * @author qknow
 * @date 2025-02-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcDocumentServiceImpl  extends ServiceImpl<KmcDocumentMapper, KmcDocumentDO> implements IKmcDocumentService {
    @Resource
    private KmcDocumentMapper kmcDocumentMapper;
    @Resource
    private IKmcDocumentLogService kmcDocumentLogService;
    @Resource
    private IKmcSyncService kmcSyncService;
    @Resource
    private IKmcCategoryService kmcCategoryService;
    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String prefix;

    @Override
    public PageResult<KmcDocumentDO> getKmcDocumentPage(KmcDocumentPageReqVO pageReqVO) {
        return kmcDocumentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids) {
        return kmcDocumentMapper.getKmcDocumentListByIds(ids);
    }

    @Override
    public Integer createKmcDocument(KmcDocumentSaveReqVO createReqVO) {
        String[] names = createReqVO.getName().split(",");
        String[] paths = createReqVO.getPath().split(",");
        List<KmcDocumentDO> documentList = new ArrayList<>();
        // 使用本地前缀，取出多余的/
        String base = StringUtils.substring(prefix, 0, prefix.length() - 1);
        for (int i = 0; i < paths.length; i++) {
            KmcDocumentDO dictType = BeanUtils.toBean(createReqVO, KmcDocumentDO.class);
            dictType.setName(names[i]);
            dictType.setPath(paths[i]);

            // 获取文件
            File file =  new File(base + paths[i]);
            String s;
            try {
                s = FileReader.safeReadFile(file);
            } catch (TikaException | IOException e) {
                throw new ServiceException("请上传有效内容的文件");
            }
            if (StrUtil.isBlank(s)){
                throw new ServiceException("请上传有效内容的文件");
            }
            dictType.setSyncStatus(DocumentSyncStatus.SUCCESS.getCode());
            documentList.add(dictType);
        }
        //批量添加
        boolean success =  kmcDocumentMapper.insertBatch(documentList);
        //批量同步
        documentList.forEach(kmcSyncService::syncToCreate);
        return success ? documentList.size() : 0;
    }

    @Override
    public int updateKmcDocument(KmcDocumentSaveReqVO updateReqVO) {
        String[] names = updateReqVO.getName().split(",");
        String[] paths = updateReqVO.getPath().split(",");

        Pattern pattern = Pattern.compile("\n"); // 匹配换行符
        Matcher matcher = pattern.matcher(updateReqVO.getSeparator());
        String escaped = matcher.replaceAll("\\\\n"); // 使用replaceAll来替换所有匹配项
        updateReqVO.setSeparator(escaped);

        List<KmcDocumentDO> documentList = Lists.newArrayList();
        for (int i = 0; i < paths.length; i++) {
            KmcDocumentDO dictType = BeanUtils.toBean(updateReqVO, KmcDocumentDO.class);
            dictType.setName(names[i]);
            dictType.setPath(paths[i]);
            if (i != 0) {
                dictType.setId(null);
            }
            documentList.add(dictType);
        }

        // 第一条记录更新
        KmcDocumentDO kmcDocument = documentList.get(0);
        documentList.remove(0);
        kmcSyncService.syncToUpdate(kmcDocument);

        // 其他记录作为新增
        if (!documentList.isEmpty()) {
            //批量添加
            kmcDocumentMapper.insertBatch(documentList);
            //批量同步
            documentList.forEach(kmcSyncService::syncToCreate);
        }

        return kmcDocumentMapper.updateById(kmcDocument);
    }
    @Override
    public int removeKmcDocument(Collection<Long> idList) {
        List<KmcDocumentDO> kmcDocumentList= kmcDocumentMapper.selectByIds(idList);
        kmcDocumentList.forEach(kmcDocumentDO -> kmcSyncService.syncToRemove(kmcDocumentDO));
        // 批量删除知识文件
        return kmcDocumentMapper.deleteByIds(idList);
    }

    @Override
    public KmcDocumentDO getKmcDocumentById(Long id) {
        return kmcDocumentMapper.selectById(id);
    }

    @Override
    public KmcDocumentDO getKmcDocumentByIdJoinSync(Long id) {
        List<KmcDocumentDO> list = new MPJLambdaWrapper<>(KmcDocumentDO.class)
                .selectAll(KmcDocumentDO.class)
                .select(KmcSyncDO::getQmDocumentId,KmcSyncDO::getQmDatasetId)
                .leftJoin(KmcSyncDO.class, KmcSyncDO::getDocumentId, KmcDocumentDO::getId)
                .eq(KmcDocumentDO::getId, id)
                .eq(KmcSyncDO::getDelFlag, false)
                .list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<KmcDocumentDO> getKmcDocumentList() {
        return kmcDocumentMapper.selectList();
    }


    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importKmcDocument(List<KmcDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcDocumentRespVO respVO : importExcelList) {
            try {
                KmcDocumentDO kmcDocumentDO = BeanUtils.toBean(respVO, KmcDocumentDO.class);
                Long kmcDocumentId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcDocumentId != null) {
                        KmcDocumentDO existingKmcDocument = kmcDocumentMapper.selectById(kmcDocumentId);
                        if (existingKmcDocument != null) {
                            kmcDocumentMapper.updateById(kmcDocumentDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcDocumentId + " 的知识文件记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcDocumentId + " 的知识文件记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcDocumentDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcDocumentId);
                    KmcDocumentDO existingKmcDocument = kmcDocumentMapper.selectOne(queryWrapper);
                    if (existingKmcDocument == null) {
                        kmcDocumentMapper.insert(kmcDocumentDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcDocumentId + " 的知识文件记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcDocumentId + " 的知识文件记录已存在。");
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

    @Override
    public List<KmcDocumentDO> selectList(KmcDocumentPageReqVO kmcDocument) {
        QueryWrapper<KmcDocumentDO> queryWrapper = new QueryWrapper<>();

        // 如果传入的 orderByColumn 有值，则根据该字段进行排序
        if (kmcDocument.getOrderByColumn() != null && kmcDocument.getIsAsc() != null) {
            String orderByColumn = kmcDocument.getOrderByColumn();  // 获取排序字段
            boolean isAsc = "asc".equalsIgnoreCase(kmcDocument.getIsAsc());  // 判断是升序还是降序

            // 根据传入的字段来排序
            if ("downloadCount".equalsIgnoreCase(orderByColumn)) {
                queryWrapper.orderBy(true, isAsc, "download_count");
            } else if ("previewCount".equalsIgnoreCase(orderByColumn)) {
                queryWrapper.orderBy(true, isAsc, "preview_count");
            } else {
                queryWrapper.orderByDesc("COALESCE(preview_count, 0) + COALESCE(download_count, 0)")
                        .orderByDesc("create_time"); // 如果前者相等，则按 create_time 降序排列
            }
            // 始终按照 create_time 排序，依据传入的 isAsc 排序
            queryWrapper.orderBy(true, isAsc, "create_time"); // 排序依据 create_time
        } else {
            // 如果 orderByColumn 没有值，使用 COALESCE 排序
            queryWrapper.orderByDesc("COALESCE(preview_count, 0) + COALESCE(download_count, 0)")
                    .orderByDesc("create_time"); // 如果前者相等，则按 create_time 降序排列
        }

        return kmcDocumentMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getFileTypes(Long knowledgeBaseId) {
        // 1. 获取所有未删除的分类数据
        KmcCategoryDO kmcCategoryDO = new KmcCategoryDO();
        kmcCategoryDO.setDelFlag(false);
        kmcCategoryDO.setKnowledgeBaseId(knowledgeBaseId);
        List<KmcCategoryDO> allCategories = kmcCategoryService.getKmcCategoryAllList(kmcCategoryDO);

        // 2. 获取文档统计信息（按分类名称分组计数）
        List<Map<String, Object>> docStats = kmcDocumentMapper.getFileTypes();
        // 将统计结果转换为Map<分类名称, 文档数量>方便后续查询
        Map<Long, Integer> statsMap = docStats.stream()
                .collect(Collectors.toMap(
                        // 分类名称作为key
                        stat -> Long.parseLong(stat.get("category_id").toString()),
                        // 文档数量作为value
                        stat -> Integer.parseInt(stat.get("count").toString())
                ));

        // 3. 按父ID分组所有子分类（parentId ≠ 0的才是子分类）
        Map<Long, List<KmcCategoryDO>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != 0) // 过滤掉顶级分类（parentId=0的）
                .collect(Collectors.groupingBy(KmcCategoryDO::getParentId)); // 按parentId分组

        // 4. 准备最终返回的结果列表
        List<Map<String, Object>> result = Lists.newArrayList();

        // 5. 处理所有顶级分类（parentId=0的）
        allCategories.stream()
                .filter(c -> c.getParentId() == 0) // 筛选顶级分类
                .forEach(topCategory -> {
                    // 创建当前顶级分类的节点
                    Map<String, Object> categoryNode = Maps.newLinkedHashMap();
                    categoryNode.put("id", topCategory.getId()); // 分类ID
                    categoryNode.put("name", topCategory.getName()); // 分类名称

                    // 递归构建子分类树，并计算总文档数
                    // 使用数组来传递引用，便于在递归中修改值
                    int[] totalCount = new int[]{statsMap.getOrDefault(topCategory.getId(), 0)};
                    List<Map<String, Object>> children = buildCategoryTree(
                            topCategory.getId(), // 当前分类ID作为父ID
                            childrenMap, // 子分类分组数据
                            statsMap, // 分类文档统计
                            totalCount // 用于累计总文档数
                    );

                    // 设置子分类列表
                    categoryNode.put("children", children);
                    // 当前分类的直接文档数（不包含子分类）
                    categoryNode.put("count", statsMap.getOrDefault(topCategory.getId(), 0));
                    // 当前分类及其所有子分类的总文档数
                    categoryNode.put("totalCount", totalCount[0]);

                    // 将当前顶级分类添加到结果列表
                    result.add(categoryNode);
                });

        return result;
    }

    /**
     * 递归构建分类树结构并累计文档总数
     * @param parentId 当前父分类ID
     * @param childrenMap 所有子分类的分组映射（按parentId分组）
     * @param statsMap 分类文档统计信息（分类名称 -> 文档数）
     * @param parentTotalCount 父分类的总文档数数组（使用数组以便在递归中修改值）
     * @return 当前父分类下的子分类树结构
     */
    private List<Map<String, Object>> buildCategoryTree(
            Long parentId,
            Map<Long, List<KmcCategoryDO>> childrenMap,
            Map<Long, Integer> statsMap,
            int[] parentTotalCount
    ) {
        // 如果当前父ID没有子分类，返回空列表
        if (!childrenMap.containsKey(parentId)) {
            return Lists.newArrayList();
        }

        // 处理当前父ID下的所有子分类
        return childrenMap.get(parentId).stream()
                .map(child -> {
                    // 创建当前子分类节点
                    Map<String, Object> childNode = Maps.newHashMap();
                    childNode.put("id", child.getId()); // 分类ID
                    childNode.put("name", child.getName()); // 分类名称

                    // 获取当前分类的直接文档数
                    int currentCount = statsMap.getOrDefault(child.getId(), 0);
                    childNode.put("count", currentCount);

                    // 递归处理子分类的子分类（孙分类）
                    // 使用数组来累计当前分类及其所有子分类的总文档数
                    int[] childTotalCount = new int[]{currentCount};
                    List<Map<String, Object>> grandchildren = buildCategoryTree(
                            child.getId(), // 当前分类作为父分类
                            childrenMap,
                            statsMap,
                            childTotalCount // 用于累计当前分类树的总文档数
                    );

                    // 设置子分类列表
                    childNode.put("children", grandchildren);
                    // 设置当前分类及其所有子分类的总文档数
                    childNode.put("totalCount", childTotalCount[0]);

                    // 将当前分类树的总文档数累加到父分类的总数中
                    parentTotalCount[0] += childTotalCount[0];

                    return childNode;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean trackCount(List<KmcDocumentTrackReqVO> documentTrackList, Long userId, String userName) {
        Set<Long> idSet = documentTrackList.stream().map(KmcDocumentTrackReqVO::getDocumentId).collect(Collectors.toSet());
        List<KmcDocumentDO> kmcDocumentDOList = kmcDocumentMapper.selectBatchIds(idSet);
        Map<Long, KmcDocumentDO> documentMap = kmcDocumentDOList.stream()
                .collect(Collectors.toMap(KmcDocumentDO::getId, kmcDocumentDO -> kmcDocumentDO));

        List<KmcDocumentLogDO> documentLogList = Lists.newArrayList();
        documentTrackList.forEach(documentTrack -> {
            KmcDocumentDO kmcDocumentDO = documentMap.get(documentTrack.getDocumentId());
            KmcDocumentLogDO documentLog = new KmcDocumentLogDO();
            documentLog.setWorkspaceId(kmcDocumentDO.getWorkspaceId());
            documentLog.setUserId(userId);
            documentLog.setUserName(userName);
            documentLog.setDocumentId(kmcDocumentDO.getId());
            documentLog.setDocumentName(kmcDocumentDO.getName());
            documentLog.setType(DocumentTrackEnum.getCode(documentTrack.getType()));
            documentLogList.add(documentLog);

            if (DocumentTrackEnum.PREVIEW.eq(documentLog.getType())) {
                kmcDocumentDO.setPreviewCount(kmcDocumentDO.getPreviewCount() + 1);
            } else if (DocumentTrackEnum.DOWNLOAD.eq(documentLog.getType())) {
                kmcDocumentDO.setDownloadCount(kmcDocumentDO.getDownloadCount() + 1);
            }
        });
        kmcDocumentMapper.updateBatch(documentMap.values());
      return kmcDocumentLogService.saveBatch(documentLogList);
    }

    @Override
    public KmcDocumentDO getKnowledgeBaseOne(Long knowledgeBaseId) {
       return getOne(new LambdaQueryWrapperX<KmcDocumentDO>()
                .eqIfPresent(KmcDocumentDO::getKnowledgeBaseId, knowledgeBaseId)
               .last("limit 1"));
    }

    @Override
    public String checkFileName(Long id, String fileNames, Long knowledgeBaseId) {
        if (StringUtils.isEmpty(fileNames)) {
            return null;
        }
        String name = null;
        if (id != null) {
            KmcDocumentDO documentDO = this.getKmcDocumentById(id);
            name = documentDO.getName();
        }
        List<KmcDocumentDO> kmcDocumentDOList = kmcDocumentMapper.selectList(new LambdaQueryWrapperX<KmcDocumentDO>()
                .eqIfPresent(KmcDocumentDO::getKnowledgeBaseId, knowledgeBaseId)
                .neIfPresent(KmcDocumentDO::getName, name)
                .inIfPresent(KmcDocumentDO::getName, StringUtils.str2List(fileNames, ",", true, true)));
        return kmcDocumentDOList.stream().map(KmcDocumentDO::getName).collect(Collectors.joining(","));
    }

    @Override
    public Map<Long, Integer> getFileCount(List<Long> knowledgeBaseIdList) {
        if (CollUtil.isEmpty(knowledgeBaseIdList)) {
            return Maps.newHashMap();
        }

        List<Map<String, Object>> result = kmcDocumentMapper.selectMaps(new QueryWrapperX<KmcDocumentDO>()
                        .in("knowledge_base_id", knowledgeBaseIdList)
                        .select("knowledge_base_id", "COUNT(*) AS count")
                        .groupBy("knowledge_base_id")
        );
        if (CollUtil.isNotEmpty(result)) {
            return result.stream().collect(Collectors.toMap(
                    map -> Long.parseLong(map.get("knowledge_base_id").toString()),
                    map -> Integer.parseInt(map.get("count").toString())
            ));
        }
        return Maps.newHashMap();
    }
}
