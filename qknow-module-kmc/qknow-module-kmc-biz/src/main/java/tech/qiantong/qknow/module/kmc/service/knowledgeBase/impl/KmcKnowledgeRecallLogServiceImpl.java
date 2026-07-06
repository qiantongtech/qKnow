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

package tech.qiantong.qknow.module.kmc.service.knowledgeBase.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRecallLogDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeBase.KmcKnowledgeRecallLogMapper;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRecallLogService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 召回记录Service业务层处理
 *
 * @author qknow
 * @date 2025-07-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcKnowledgeRecallLogServiceImpl  extends ServiceImpl<KmcKnowledgeRecallLogMapper,KmcKnowledgeRecallLogDO> implements IKmcKnowledgeRecallLogService {
    @Resource
    private KmcKnowledgeRecallLogMapper kmcKnowledgeRecallLogMapper;

    @Override
    public PageResult<KmcKnowledgeRecallLogDO> getKmcKnowledgeRecallLogPage(KmcKnowledgeRecallLogPageReqVO pageReqVO) {
        return kmcKnowledgeRecallLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKmcKnowledgeRecallLog(KmcKnowledgeRecallLogSaveReqVO createReqVO) {
        KmcKnowledgeRecallLogDO dictType = BeanUtils.toBean(createReqVO, KmcKnowledgeRecallLogDO.class);
        kmcKnowledgeRecallLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKmcKnowledgeRecallLog(KmcKnowledgeRecallLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新召回记录
        KmcKnowledgeRecallLogDO updateObj = BeanUtils.toBean(updateReqVO, KmcKnowledgeRecallLogDO.class);
        return kmcKnowledgeRecallLogMapper.updateById(updateObj);
    }
    @Override
    public int removeKmcKnowledgeRecallLog(Collection<Long> idList) {
        // 批量删除召回记录
        return kmcKnowledgeRecallLogMapper.deleteBatchIds(idList);
    }

    @Override
    public KmcKnowledgeRecallLogDO getKmcKnowledgeRecallLogById(Long id) {
        return kmcKnowledgeRecallLogMapper.selectById(id);
    }

    @Override
    public List<KmcKnowledgeRecallLogDO> getKmcKnowledgeRecallLogList() {
        return kmcKnowledgeRecallLogMapper.selectList();
    }

    @Override
    public Map<Long, KmcKnowledgeRecallLogDO> getKmcKnowledgeRecallLogMap() {
        List<KmcKnowledgeRecallLogDO> kmcKnowledgeRecallLogList = kmcKnowledgeRecallLogMapper.selectList();
        return kmcKnowledgeRecallLogList.stream()
                .collect(Collectors.toMap(
                        KmcKnowledgeRecallLogDO::getId,
                        kmcKnowledgeRecallLogDO -> kmcKnowledgeRecallLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入召回记录数据
         *
         * @param importExcelList 召回记录数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKmcKnowledgeRecallLog(List<KmcKnowledgeRecallLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KmcKnowledgeRecallLogRespVO respVO : importExcelList) {
                try {
                    KmcKnowledgeRecallLogDO kmcKnowledgeRecallLogDO = BeanUtils.toBean(respVO, KmcKnowledgeRecallLogDO.class);
                    Long kmcKnowledgeRecallLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kmcKnowledgeRecallLogId != null) {
                            KmcKnowledgeRecallLogDO existingKmcKnowledgeRecallLog = kmcKnowledgeRecallLogMapper.selectById(kmcKnowledgeRecallLogId);
                            if (existingKmcKnowledgeRecallLog != null) {
                                kmcKnowledgeRecallLogMapper.updateById(kmcKnowledgeRecallLogDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kmcKnowledgeRecallLogId + " 的召回记录记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kmcKnowledgeRecallLogId + " 的召回记录记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KmcKnowledgeRecallLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kmcKnowledgeRecallLogId);
                        KmcKnowledgeRecallLogDO existingKmcKnowledgeRecallLog = kmcKnowledgeRecallLogMapper.selectOne(queryWrapper);
                        if (existingKmcKnowledgeRecallLog == null) {
                            kmcKnowledgeRecallLogMapper.insert(kmcKnowledgeRecallLogDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kmcKnowledgeRecallLogId + " 的召回记录记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kmcKnowledgeRecallLogId + " 的召回记录记录已存在。");
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
    public List<KmcKnowledgeRecallLogDO> findByKnowledgeBaseId(Long knowledgeBaseId) {
        return this.list(new LambdaQueryWrapperX<KmcKnowledgeRecallLogDO>()
                .eqIfPresent(KmcKnowledgeRecallLogDO::getKnowledgeId, knowledgeBaseId)
                .orderByDesc(KmcKnowledgeRecallLogDO::getCreateTime)
        );
    }
}
