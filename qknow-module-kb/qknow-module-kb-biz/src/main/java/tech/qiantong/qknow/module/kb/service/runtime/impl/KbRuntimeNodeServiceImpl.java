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

package tech.qiantong.qknow.module.kb.service.runtime.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeNodeRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeNodeSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeNodeDO;
import tech.qiantong.qknow.module.kb.dal.mapper.runtime.KbRuntimeNodeMapper;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeNodeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * bot节点运行Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
public class KbRuntimeNodeServiceImpl extends ServiceImpl<KbRuntimeNodeMapper, KbRuntimeNodeDO> implements IKbRuntimeNodeService {
    @Resource
    private KbRuntimeNodeMapper kbRuntimeNodeMapper;

    @Override
    public Long createKbRuntimeNode(KbRuntimeNodeSaveReqVO createReqVO) {
        KbRuntimeNodeDO dictType = BeanUtils.toBean(createReqVO, KbRuntimeNodeDO.class);
        kbRuntimeNodeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKbRuntimeNode(KbRuntimeNodeSaveReqVO updateReqVO) {
        // 相关校验

        // 更新bot节点运行
        KbRuntimeNodeDO updateObj = BeanUtils.toBean(updateReqVO, KbRuntimeNodeDO.class);
        return kbRuntimeNodeMapper.updateById(updateObj);
    }

    @Override
    public int removeKbRuntimeNode(Collection<Long> idList) {
        // 批量删除bot节点运行
        return kbRuntimeNodeMapper.deleteBatchIds(idList);
    }

    @Override
    public KbRuntimeNodeDO getKbRuntimeNodeById(Long id) {
        return kbRuntimeNodeMapper.selectById(id);
    }

    @Override
    public List<KbRuntimeNodeDO> getKbRuntimeNodeList() {
        return kbRuntimeNodeMapper.selectList();
    }

    @Override
    public Map<Long, KbRuntimeNodeDO> getKbRuntimeNodeMap() {
        List<KbRuntimeNodeDO> kbRuntimeNodeList = kbRuntimeNodeMapper.selectList();
        return kbRuntimeNodeList.stream()
                .collect(Collectors.toMap(
                        KbRuntimeNodeDO::getId,
                        kbRuntimeNodeDO -> kbRuntimeNodeDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入bot节点运行数据
     *
     * @param importExcelList bot节点运行数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importKbRuntimeNode(List<KbRuntimeNodeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (KbRuntimeNodeRespVO respVO : importExcelList) {
            try {
                KbRuntimeNodeDO kbRuntimeNodeDO = BeanUtils.toBean(respVO, KbRuntimeNodeDO.class);
                Long kbRuntimeNodeId = respVO.getId();
                if (isUpdateSupport) {
                    if (kbRuntimeNodeId != null) {
                        KbRuntimeNodeDO existingKbRuntimeNode = kbRuntimeNodeMapper.selectById(kbRuntimeNodeId);
                        if (existingKbRuntimeNode != null) {
                            kbRuntimeNodeMapper.updateById(kbRuntimeNodeDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kbRuntimeNodeId + " 的bot节点运行记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kbRuntimeNodeId + " 的bot节点运行记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<KbRuntimeNodeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", kbRuntimeNodeId);
                    KbRuntimeNodeDO existingKbRuntimeNode = kbRuntimeNodeMapper.selectOne(queryWrapper);
                    if (existingKbRuntimeNode == null) {
                        kbRuntimeNodeMapper.insert(kbRuntimeNodeDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + kbRuntimeNodeId + " 的bot节点运行记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + kbRuntimeNodeId + " 的bot节点运行记录已存在。");
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
