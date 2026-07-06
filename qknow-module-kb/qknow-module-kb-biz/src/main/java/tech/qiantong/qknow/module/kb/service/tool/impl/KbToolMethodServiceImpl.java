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

package tech.qiantong.qknow.module.kb.service.tool.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.dal.mapper.tool.KbToolMethodMapper;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;
/**
 * 工具方法Service业务层处理
 *
 * @author qknow
 * @date 2026-03-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbToolMethodServiceImpl  extends ServiceImpl<KbToolMethodMapper,KbToolMethodDO> implements IKbToolMethodService {
    @Resource
    private KbToolMethodMapper kbToolMethodMapper;

    @Override
    public PageResult<KbToolMethodDO> getKbToolMethodPage(KbToolMethodPageReqVO pageReqVO) {
        return kbToolMethodMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKbToolMethod(KbToolMethodSaveReqVO createReqVO) {
        KbToolMethodDO dictType = BeanUtils.toBean(createReqVO, KbToolMethodDO.class);
        kbToolMethodMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKbToolMethod(KbToolMethodSaveReqVO updateReqVO) {
        // 相关校验

        // 更新工具方法
        KbToolMethodDO updateObj = BeanUtils.toBean(updateReqVO, KbToolMethodDO.class);
        return kbToolMethodMapper.updateById(updateObj);
    }
    @Override
    public int removeKbToolMethod(Collection<Long> idList) {
        // 批量删除工具方法
        return kbToolMethodMapper.deleteBatchIds(idList);
    }

    @Override
    public KbToolMethodDO getKbToolMethodById(Long id) {
        return kbToolMethodMapper.selectById(id);
    }

    @Override
    public List<KbToolMethodDO> getKbToolMethodList() {
        return kbToolMethodMapper.selectList();
    }

    @Override
    public Map<Long, KbToolMethodDO> getKbToolMethodMap() {
        List<KbToolMethodDO> kbToolMethodList = kbToolMethodMapper.selectList();
        return kbToolMethodList.stream()
                .collect(Collectors.toMap(
                        KbToolMethodDO::getId,
                        kbToolMethodDO -> kbToolMethodDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入工具方法数据
         *
         * @param importExcelList 工具方法数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKbToolMethod(List<KbToolMethodRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KbToolMethodRespVO respVO : importExcelList) {
                try {
                    KbToolMethodDO kbToolMethodDO = BeanUtils.toBean(respVO, KbToolMethodDO.class);
                    Long kbToolMethodId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kbToolMethodId != null) {
                            KbToolMethodDO existingKbToolMethod = kbToolMethodMapper.selectById(kbToolMethodId);
                            if (existingKbToolMethod != null) {
                                kbToolMethodMapper.updateById(kbToolMethodDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kbToolMethodId + " 的工具方法记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kbToolMethodId + " 的工具方法记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KbToolMethodDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kbToolMethodId);
                        KbToolMethodDO existingKbToolMethod = kbToolMethodMapper.selectOne(queryWrapper);
                        if (existingKbToolMethod == null) {
                            kbToolMethodMapper.insert(kbToolMethodDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kbToolMethodId + " 的工具方法记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kbToolMethodId + " 的工具方法记录已存在。");
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
