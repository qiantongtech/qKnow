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

package tech.qiantong.qknow.module.ext.service.extTaskLog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDetailDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extTaskLog.ExtTaskLogDetailMapper;
import tech.qiantong.qknow.module.ext.service.extTaskLog.IExtTaskLogDetailService;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抽取任务执行日志详情Service业务层处理
 *
 * @author qknow
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtTaskLogDetailServiceImpl extends ServiceImpl<ExtTaskLogDetailMapper, ExtTaskLogDetailDO> implements IExtTaskLogDetailService {
    @Resource
    private ExtTaskLogDetailMapper extTaskLogDetailMapper;

    @Override
    public PageResult<ExtTaskLogDetailDO> getExtTaskLogDetailPage(ExtTaskLogDetailPageReqVO pageReqVO) {
        return extTaskLogDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtTaskLogDetail(ExtTaskLogDetailSaveReqVO createReqVO) {
        ExtTaskLogDetailDO dictType = BeanUtils.toBean(createReqVO, ExtTaskLogDetailDO.class);
        extTaskLogDetailMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtTaskLogDetail(ExtTaskLogDetailSaveReqVO updateReqVO) {
        // 相关校验

        // 更新抽取任务执行日志详情
        ExtTaskLogDetailDO updateObj = BeanUtils.toBean(updateReqVO, ExtTaskLogDetailDO.class);
        return extTaskLogDetailMapper.updateById(updateObj);
    }

    @Override
    public int removeExtTaskLogDetail(Collection<Long> idList) {
        // 批量删除抽取任务执行日志详情
        return extTaskLogDetailMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtTaskLogDetailDO getExtTaskLogDetailById(Long id) {
        return extTaskLogDetailMapper.selectById(id);
    }

    @Override
    public List<ExtTaskLogDetailDO> getExtTaskLogDetailList() {
        return extTaskLogDetailMapper.selectList();
    }

    @Override
    public Map<Long, ExtTaskLogDetailDO> getExtTaskLogDetailMap() {
        List<ExtTaskLogDetailDO> extTaskLogDetailList = extTaskLogDetailMapper.selectList();
        return extTaskLogDetailList.stream()
                .collect(Collectors.toMap(
                        ExtTaskLogDetailDO::getId,
                        extTaskLogDetailDO -> extTaskLogDetailDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入抽取任务执行日志详情数据
     *
     * @param importExcelList 抽取任务执行日志详情数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importExtTaskLogDetail(List<ExtTaskLogDetailRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (ExtTaskLogDetailRespVO respVO : importExcelList) {
            try {
                ExtTaskLogDetailDO extTaskLogDetailDO = BeanUtils.toBean(respVO, ExtTaskLogDetailDO.class);
                Long extTaskLogDetailId = respVO.getId();
                if (isUpdateSupport) {
                    if (extTaskLogDetailId != null) {
                        ExtTaskLogDetailDO existingExtTaskLogDetail = extTaskLogDetailMapper.selectById(extTaskLogDetailId);
                        if (existingExtTaskLogDetail != null) {
                            extTaskLogDetailMapper.updateById(extTaskLogDetailDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + extTaskLogDetailId + " 的抽取任务执行日志详情记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + extTaskLogDetailId + " 的抽取任务执行日志详情记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<ExtTaskLogDetailDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", extTaskLogDetailId);
                    ExtTaskLogDetailDO existingExtTaskLogDetail = extTaskLogDetailMapper.selectOne(queryWrapper);
                    if (existingExtTaskLogDetail == null) {
                        extTaskLogDetailMapper.insert(extTaskLogDetailDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + extTaskLogDetailId + " 的抽取任务执行日志详情记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + extTaskLogDetailId + " 的抽取任务执行日志详情记录已存在。");
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
