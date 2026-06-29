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

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentLogDO;
import tech.qiantong.qknow.module.kg.dal.mapper.knowledge.KgKnowledgeDocumentLogMapper;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeDocumentLogService;
/**
 * 文件操作日志Service业务层处理
 *
 * @author qknow
 * @date 2025-10-22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KgKnowledgeDocumentLogServiceImpl  extends ServiceImpl<KgKnowledgeDocumentLogMapper,KgKnowledgeDocumentLogDO> implements IKgKnowledgeDocumentLogService {
    @Resource
    private KgKnowledgeDocumentLogMapper kgKnowledgeDocumentLogMapper;

    @Override
    public PageResult<KgKnowledgeDocumentLogDO> getKgKnowledgeDocumentLogPage(KgKnowledgeDocumentLogPageReqVO pageReqVO) {
        return kgKnowledgeDocumentLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKgKnowledgeDocumentLog(KgKnowledgeDocumentLogSaveReqVO createReqVO) {
        KgKnowledgeDocumentLogDO dictType = BeanUtils.toBean(createReqVO, KgKnowledgeDocumentLogDO.class);
        kgKnowledgeDocumentLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKgKnowledgeDocumentLog(KgKnowledgeDocumentLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新文件操作日志
        KgKnowledgeDocumentLogDO updateObj = BeanUtils.toBean(updateReqVO, KgKnowledgeDocumentLogDO.class);
        return kgKnowledgeDocumentLogMapper.updateById(updateObj);
    }
    @Override
    public int removeKgKnowledgeDocumentLog(Collection<Long> idList) {
        // 批量删除文件操作日志
        return kgKnowledgeDocumentLogMapper.deleteBatchIds(idList);
    }

    @Override
    public KgKnowledgeDocumentLogDO getKgKnowledgeDocumentLogById(Long id) {
        return kgKnowledgeDocumentLogMapper.selectById(id);
    }

    @Override
    public List<KgKnowledgeDocumentLogDO> getKgKnowledgeDocumentLogList() {
        return kgKnowledgeDocumentLogMapper.selectList();
    }

    @Override
    public Map<Long, KgKnowledgeDocumentLogDO> getKgKnowledgeDocumentLogMap() {
        List<KgKnowledgeDocumentLogDO> kgKnowledgeDocumentLogList = kgKnowledgeDocumentLogMapper.selectList();
        return kgKnowledgeDocumentLogList.stream()
                .collect(Collectors.toMap(
                        KgKnowledgeDocumentLogDO::getId,
                        kgKnowledgeDocumentLogDO -> kgKnowledgeDocumentLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入文件操作日志数据
         *
         * @param importExcelList 文件操作日志数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKgKnowledgeDocumentLog(List<KgKnowledgeDocumentLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KgKnowledgeDocumentLogRespVO respVO : importExcelList) {
                try {
                    KgKnowledgeDocumentLogDO kgKnowledgeDocumentLogDO = BeanUtils.toBean(respVO, KgKnowledgeDocumentLogDO.class);
                    Long kgKnowledgeDocumentLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kgKnowledgeDocumentLogId != null) {
                            KgKnowledgeDocumentLogDO existingKgKnowledgeDocumentLog = kgKnowledgeDocumentLogMapper.selectById(kgKnowledgeDocumentLogId);
                            if (existingKgKnowledgeDocumentLog != null) {
                                kgKnowledgeDocumentLogMapper.updateById(kgKnowledgeDocumentLogDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kgKnowledgeDocumentLogId + " 的文件操作日志记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kgKnowledgeDocumentLogId + " 的文件操作日志记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KgKnowledgeDocumentLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kgKnowledgeDocumentLogId);
                        KgKnowledgeDocumentLogDO existingKgKnowledgeDocumentLog = kgKnowledgeDocumentLogMapper.selectOne(queryWrapper);
                        if (existingKgKnowledgeDocumentLog == null) {
                            kgKnowledgeDocumentLogMapper.insert(kgKnowledgeDocumentLogDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kgKnowledgeDocumentLogId + " 的文件操作日志记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kgKnowledgeDocumentLogId + " 的文件操作日志记录已存在。");
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
}
