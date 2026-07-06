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

package tech.qiantong.qknow.module.kmc.service.kmcDocumentLog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcDocumentLog.KmcDocumentLogDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.kmcDocumentLog.KmcDocumentLogMapper;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.module.kmc.service.kmcDocumentLog.IKmcDocumentLogService;

import java.util.*;
import java.util.stream.Collectors;
/**
 * 文件操作日志Service业务层处理
 *
 * @author qknow
 * @date 2025-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcDocumentLogServiceImpl  extends ServiceImpl<KmcDocumentLogMapper,KmcDocumentLogDO> implements IKmcDocumentLogService {
    @Resource
    private KmcDocumentLogMapper kmcDocumentLogMapper;

    @Resource
    public IKmcDocumentService kmcDocumentService;

    @Override
    public PageResult<KmcDocumentLogDO> getKmcDocumentLogPage(KmcDocumentLogPageReqVO pageReqVO) {
        return kmcDocumentLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKmcDocumentLog(KmcDocumentLogSaveReqVO createReqVO) {
        KmcDocumentLogDO dictType = BeanUtils.toBean(createReqVO, KmcDocumentLogDO.class);
        kmcDocumentLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKmcDocumentLog(KmcDocumentLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新文件操作日志
        KmcDocumentLogDO updateObj = BeanUtils.toBean(updateReqVO, KmcDocumentLogDO.class);
        return kmcDocumentLogMapper.updateById(updateObj);
    }
    @Override
    public int removeKmcDocumentLog(Collection<Long> idList) {
        // 批量删除文件操作日志
        return kmcDocumentLogMapper.deleteBatchIds(idList);
    }

    @Override
    public KmcDocumentLogDO getKmcDocumentLogById(Long id) {
        return kmcDocumentLogMapper.selectById(id);
    }

    @Override
    public List<KmcDocumentLogDO> getKmcDocumentLogList() {
        return kmcDocumentLogMapper.selectList();
    }

    @Override
    public Map<Long, KmcDocumentLogDO> getKmcDocumentLogMap() {
        List<KmcDocumentLogDO> kmcDocumentLogList = kmcDocumentLogMapper.selectList();
        return kmcDocumentLogList.stream()
                .collect(Collectors.toMap(
                        KmcDocumentLogDO::getId,
                        kmcDocumentLogDO -> kmcDocumentLogDO,
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
    public String importKmcDocumentLog(List<KmcDocumentLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KmcDocumentLogRespVO respVO : importExcelList) {
            try {
                KmcDocumentLogDO kmcDocumentLogDO = BeanUtils.toBean(respVO, KmcDocumentLogDO.class);
                Long kmcDocumentLogId = respVO.getId();
                if (isUpdateSupport) {
                    if (kmcDocumentLogId != null) {
                        KmcDocumentLogDO existingKmcDocumentLog = kmcDocumentLogMapper.selectById(kmcDocumentLogId);
                        if (existingKmcDocumentLog != null) {
                            kmcDocumentLogMapper.updateById(kmcDocumentLogDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kmcDocumentLogId + " 的文件操作日志记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kmcDocumentLogId + " 的文件操作日志记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KmcDocumentLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kmcDocumentLogId);
                    KmcDocumentLogDO existingKmcDocumentLog = kmcDocumentLogMapper.selectOne(queryWrapper);
                    if (existingKmcDocumentLog == null) {
                        kmcDocumentLogMapper.insert(kmcDocumentLogDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kmcDocumentLogId + " 的文件操作日志记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kmcDocumentLogId + " 的文件操作日志记录已存在。");
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
    public List<KmcDocumentDO> seletAllDocumentLogList(KmcDocumentLogDO kmcDocumentLog) {
        List<Map<String, Object>> result = kmcDocumentLogMapper.selectMaps( new QueryWrapper<KmcDocumentLogDO>()
                .eq("user_id", kmcDocumentLog.getUserId())
                .select("document_id", "COUNT(*) AS count")
                .groupBy("document_id")
                .orderByDesc("count")
                .last("LIMIT 10")
        );

        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> documentIdList = result.stream()
                .map(map -> Long.parseLong(map.get("document_id").toString()))
                .collect(Collectors.toList());
        List<KmcDocumentDO> kmcDocumentList = kmcDocumentService.listByIds(documentIdList);

        // 创建 Map 用于快速查找
        Map<Long, KmcDocumentDO> documentMap = kmcDocumentList.stream()
                .collect(Collectors.toMap(KmcDocumentDO::getId, doc -> doc));

        return documentIdList.stream()
                .map(documentMap::get)
                .collect(Collectors.toList());
    }
}
