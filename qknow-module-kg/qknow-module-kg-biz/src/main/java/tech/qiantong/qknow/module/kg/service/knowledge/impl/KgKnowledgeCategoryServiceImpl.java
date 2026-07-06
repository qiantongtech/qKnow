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

package tech.qiantong.qknow.module.kg.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategorySaveReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeCategoryDO;
import tech.qiantong.qknow.module.kg.dal.mapper.knowledge.KgKnowledgeCategoryMapper;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeCategoryService;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 知识分类Service业务层处理
 *
 * @author qknow
 * @date 2025-10-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KgKnowledgeCategoryServiceImpl  extends ServiceImpl<KgKnowledgeCategoryMapper,KgKnowledgeCategoryDO> implements IKgKnowledgeCategoryService {
    @Resource
    private KgKnowledgeCategoryMapper kgKnowledgeCategoryMapper;

    @Override
    public PageResult<KgKnowledgeCategoryDO> getKgKnowledgeCategoryPage(KgKnowledgeCategoryPageReqVO pageReqVO) {
        return kgKnowledgeCategoryMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKgKnowledgeCategory(KgKnowledgeCategorySaveReqVO createReqVO) {
        //判断祖级列表字段是否为空串
        if (StringUtils.isEmpty(createReqVO.getAncestors())) {
            createReqVO.setAncestors(String.valueOf(createReqVO.getParentId()));
        }
        //获取到父级id的详细数据
        KgKnowledgeCategoryDO categoryById = kgKnowledgeCategoryMapper.selectKgCategoryById(createReqVO.getParentId());
        //如果不为空，拼接已有的祖级列表字段 +  传入的父级id
        if (categoryById != null) {
            createReqVO.setAncestors(categoryById.getAncestors() + "," + createReqVO.getParentId());
        }
        KgKnowledgeCategoryDO dictType = BeanUtils.toBean(createReqVO, KgKnowledgeCategoryDO.class);
        kgKnowledgeCategoryMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKgKnowledgeCategory(KgKnowledgeCategorySaveReqVO updateReqVO) {
        // 相关校验

        // 获取当前要更新的分类原始数据
        KgKnowledgeCategoryDO originalCategory = kgKnowledgeCategoryMapper.selectById(updateReqVO.getId());
        if (originalCategory == null) {
            throw new ServiceException("要更新的知识分类不存在");
        }

        //获取到父级id的详细数据
        KgKnowledgeCategoryDO categoryById = kgKnowledgeCategoryMapper.selectKgCategoryById(updateReqVO.getParentId());
        //如果不为空，拼接已有的祖级列表字段 +  传入的父级id
        if (categoryById != null) {
            updateReqVO.setAncestors(categoryById.getAncestors() + "," + updateReqVO.getParentId());
        }
        // 更新知识分类
        KgKnowledgeCategoryDO updateObj = BeanUtils.toBean(updateReqVO, KgKnowledgeCategoryDO.class);
        return kgKnowledgeCategoryMapper.updateById(updateObj);
    }


    /**
     * 更新指定分类的所有子分类的graphId
     * @param parentId 父分类ID
     * @param knowledgeBaseId 新的知识库ID
     */
    private void updateChildrenGraphId(Long parentId, Long knowledgeBaseId) {
        // 获取所有子分类
        List<KgKnowledgeCategoryDO> children = kgKnowledgeCategoryMapper.selectChildrenCategoryById(parentId);
        if (StringUtils.isNotEmpty(children)) {
            kgKnowledgeCategoryMapper.updateBatch(children);
        }
    }

    @Override
    public int removeKgKnowledgeCategory(Collection<Long> idList) {
        // 批量删除知识分类
        return kgKnowledgeCategoryMapper.deleteBatchIds(idList);
    }

    @Override
    public KgKnowledgeCategoryDO getKgKnowledgeCategoryById(Long id) {
        return kgKnowledgeCategoryMapper.selectById(id);
    }

    @Override
    public List<KgKnowledgeCategoryDO> getKgKnowledgeCategoryList() {
        return kgKnowledgeCategoryMapper.selectList();
    }

    @Override
    public Map<Long, KgKnowledgeCategoryDO> getKgKnowledgeCategoryMap() {
        List<KgKnowledgeCategoryDO> kgKnowledgeCategoryList = kgKnowledgeCategoryMapper.selectList();
        return kgKnowledgeCategoryList.stream()
                .collect(Collectors.toMap(
                        KgKnowledgeCategoryDO::getId,
                        kgKnowledgeCategoryDO -> kgKnowledgeCategoryDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入知识分类数据
     *
     * @param importExcelList 知识分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importKgKnowledgeCategory(List<KgKnowledgeCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KgKnowledgeCategoryRespVO respVO : importExcelList) {
                try {
                    KgKnowledgeCategoryDO kgKnowledgeCategoryDO = BeanUtils.toBean(respVO, KgKnowledgeCategoryDO.class);
                    Long kgKnowledgeCategoryId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kgKnowledgeCategoryId != null) {
                            KgKnowledgeCategoryDO existingKgKnowledgeCategory = kgKnowledgeCategoryMapper.selectById(kgKnowledgeCategoryId);
                            if (existingKgKnowledgeCategory != null) {
                                kgKnowledgeCategoryMapper.updateById(kgKnowledgeCategoryDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kgKnowledgeCategoryId + " 的知识分类记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kgKnowledgeCategoryId + " 的知识分类记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KgKnowledgeCategoryDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kgKnowledgeCategoryId);
                        KgKnowledgeCategoryDO existingKgKnowledgeCategory = kgKnowledgeCategoryMapper.selectOne(queryWrapper);
                        if (existingKgKnowledgeCategory == null) {
                            kgKnowledgeCategoryMapper.insert(kgKnowledgeCategoryDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kgKnowledgeCategoryId + " 的知识分类记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kgKnowledgeCategoryId + " 的知识分类记录已存在。");
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
    public List<KgKnowledgeCategoryDO> getKgKnowledgeCategoryList(KgKnowledgeCategoryReqVO kgKnowledgeCategoryReqVO) {
        return kgKnowledgeCategoryMapper.getKgKnowledgeCategoryList(kgKnowledgeCategoryReqVO);
    }
}
