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
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRolePageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRoleRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRoleSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRoleDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeBase.KmcKnowledgeRoleMapper;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRoleService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 知识库角色关联Service业务层处理
 *
 * @author qknow
 * @date 2025-07-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KmcKnowledgeRoleServiceImpl  extends ServiceImpl<KmcKnowledgeRoleMapper,KmcKnowledgeRoleDO> implements IKmcKnowledgeRoleService {
    @Resource
    private KmcKnowledgeRoleMapper kmcKnowledgeRoleMapper;

    @Override
    public PageResult<KmcKnowledgeRoleDO> getKmcKnowledgeRolePage(KmcKnowledgeRolePageReqVO pageReqVO) {
        return kmcKnowledgeRoleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKmcKnowledgeRole(KmcKnowledgeRoleSaveReqVO createReqVO) {
        KmcKnowledgeRoleDO dictType = BeanUtils.toBean(createReqVO, KmcKnowledgeRoleDO.class);
        kmcKnowledgeRoleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKmcKnowledgeRole(KmcKnowledgeRoleSaveReqVO updateReqVO) {
        // 相关校验

        // 更新知识库角色关联
        KmcKnowledgeRoleDO updateObj = BeanUtils.toBean(updateReqVO, KmcKnowledgeRoleDO.class);
        return kmcKnowledgeRoleMapper.updateById(updateObj);
    }
    @Override
    public int removeKmcKnowledgeRole(Collection<Long> idList) {
        // 批量删除知识库角色关联
        return kmcKnowledgeRoleMapper.deleteBatchIds(idList);
    }

    @Override
    public KmcKnowledgeRoleDO getKmcKnowledgeRoleById(Long id) {
        return kmcKnowledgeRoleMapper.selectById(id);
    }

    @Override
    public List<KmcKnowledgeRoleDO> getKmcKnowledgeRoleList() {
        return kmcKnowledgeRoleMapper.selectList();
    }

    @Override
    public Map<Long, KmcKnowledgeRoleDO> getKmcKnowledgeRoleMap() {
        List<KmcKnowledgeRoleDO> kmcKnowledgeRoleList = kmcKnowledgeRoleMapper.selectList();
        return kmcKnowledgeRoleList.stream()
                .collect(Collectors.toMap(
                        KmcKnowledgeRoleDO::getId,
                        kmcKnowledgeRoleDO -> kmcKnowledgeRoleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入知识库角色关联数据
         *
         * @param importExcelList 知识库角色关联数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKmcKnowledgeRole(List<KmcKnowledgeRoleRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KmcKnowledgeRoleRespVO respVO : importExcelList) {
                try {
                    KmcKnowledgeRoleDO kmcKnowledgeRoleDO = BeanUtils.toBean(respVO, KmcKnowledgeRoleDO.class);
                    Long kmcKnowledgeRoleId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kmcKnowledgeRoleId != null) {
                            KmcKnowledgeRoleDO existingKmcKnowledgeRole = kmcKnowledgeRoleMapper.selectById(kmcKnowledgeRoleId);
                            if (existingKmcKnowledgeRole != null) {
                                kmcKnowledgeRoleMapper.updateById(kmcKnowledgeRoleDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kmcKnowledgeRoleId + " 的知识库角色关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kmcKnowledgeRoleId + " 的知识库角色关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KmcKnowledgeRoleDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kmcKnowledgeRoleId);
                        KmcKnowledgeRoleDO existingKmcKnowledgeRole = kmcKnowledgeRoleMapper.selectOne(queryWrapper);
                        if (existingKmcKnowledgeRole == null) {
                            kmcKnowledgeRoleMapper.insert(kmcKnowledgeRoleDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kmcKnowledgeRoleId + " 的知识库角色关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kmcKnowledgeRoleId + " 的知识库角色关联记录已存在。");
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
        public List<KmcKnowledgeRoleDO> findByKnowledgeBaseId(Long knowledgeBaseId) {
            return this.list(new LambdaQueryWrapperX<KmcKnowledgeRoleDO>()
                    .eqIfPresent(KmcKnowledgeRoleDO::getKnowledgeId, knowledgeBaseId)
            );
        }
}
