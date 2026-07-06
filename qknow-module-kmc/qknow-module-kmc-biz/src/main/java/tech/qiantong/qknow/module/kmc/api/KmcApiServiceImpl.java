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

package tech.qiantong.qknow.module.kmc.api;

import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.domain.entity.SysRole;
import tech.qiantong.qknow.module.kmc.api.kmcDocument.dto.KmcDocumentRespDTO;
import tech.qiantong.qknow.module.kmc.api.kmcDocument.dto.TreeSelectsDTO;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.RetrieveResultRespVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRoleDO;
import tech.qiantong.qknow.module.kmc.dal.mapper.kmcDocument.KmcDocumentMapper;
import tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeBase.KmcKnowledgeBaseMapper;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeBaseService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRoleService;
import tech.qiantong.qknow.module.system.service.ISysRoleService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.RetrieveResult;

import java.util.*;
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
public class KmcApiServiceImpl implements IKmcApiService {

    @Resource
    private KmcDocumentMapper kmcDocumentMapper;
    @Resource
    private KmcKnowledgeBaseMapper kmcKnowledgeBaseMapper;
    @Resource
    private IKmcKnowledgeBaseService iKmcKnowledgeBaseService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private IKmcKnowledgeRoleService kmcKnowledgeRoleService;
    @Resource
    private IKmcCategoryService iKmcCategoryService;

    @Override
    public List<KmcDocumentRespDTO> getKmcDocumentList() {
        List<KmcDocumentDO> kmcDocumentDOList = kmcDocumentMapper.selectList();
        List<KmcDocumentRespDTO> kmcDocumentReqDTOList = new ArrayList<>();
        kmcDocumentDOList.forEach(item ->{
            KmcDocumentRespDTO kmcDocumentReqDTO = new KmcDocumentRespDTO();
            // 使用BeanUtils拷贝属性（推荐方式）
            BeanUtils.copyProperties(item, kmcDocumentReqDTO);
            kmcDocumentReqDTOList.add(kmcDocumentReqDTO);
        });

        return kmcDocumentReqDTOList;
    }

    @Override
    public List<KmcDocumentRespDTO> getKmcDocumentListByIds(List<Long> ids) {
        List<KmcDocumentDO> kmcDocumentDOList = kmcDocumentMapper.getKmcDocumentListByIds(ids);
        List<KmcDocumentRespDTO> kmcDocumentReqDTOList = new ArrayList<>();
        kmcDocumentDOList.forEach(item ->{
            KmcDocumentRespDTO kmcDocumentReqDTO = new KmcDocumentRespDTO();
            // 使用BeanUtils拷贝属性（推荐方式）
            BeanUtils.copyProperties(item, kmcDocumentReqDTO);
            kmcDocumentReqDTOList.add(kmcDocumentReqDTO);
        });
        return kmcDocumentReqDTOList;
    }

    @Override
    public KmcDocumentRespDTO getKmcDocumentById(Long id) {
        KmcDocumentDO kmcDocumentDO = kmcDocumentMapper.selectById(id);
        KmcDocumentRespDTO kmcDocumentReqDTO = new KmcDocumentRespDTO();
        // 使用BeanUtils拷贝属性（推荐方式）
        BeanUtils.copyProperties(kmcDocumentDO, kmcDocumentReqDTO);
        return kmcDocumentReqDTO;
    }
    @Override
    public List<TreeSelectsDTO> getCategoryTreeByKnowledgeList(Long knowledgeId) {
        KmcCategoryDO kmcCategoryDO = new KmcCategoryDO();
        kmcCategoryDO.setKnowledgeBaseId(knowledgeId);
        List<TreeSelects> treeSelectsList =   iKmcCategoryService.selectCategoryTreeList(kmcCategoryDO);
        List<TreeSelectsDTO> treeSelectsDTOList = new ArrayList<>();
        treeSelectsList.forEach(item ->{
            TreeSelectsDTO kmcDocumentReqDTO = new TreeSelectsDTO();
            // 使用BeanUtils拷贝属性（推荐方式）
            BeanUtils.copyProperties(item, kmcDocumentReqDTO);
            treeSelectsDTOList.add(kmcDocumentReqDTO);
        });
        return treeSelectsDTOList;
    }

    @Override
    public List<RetrieveResult> recallTest(Long knowledgeId, String query) {

        List<RetrieveResultRespVO> resultRespVOList =  iKmcKnowledgeBaseService.search(knowledgeId,query);
        List<RetrieveResult> retrieveResults = new ArrayList<>();
        for (RetrieveResultRespVO vo : resultRespVOList) {
            RetrieveResult result = new RetrieveResult();
            // 手动复制所有字段
            result.setId(vo.getId());
            result.setPosition(vo.getPosition());
            result.setDocumentId(vo.getDocumentId());
            result.setContent(vo.getContent());
            result.setAnswer(vo.getAnswer());
            result.setWordCount(vo.getWordCount());
            result.setTokens(vo.getTokens());
            result.setKeywords(vo.getKeywords());
            result.setIndexNodeId(vo.getIndexNodeId());
            result.setIndexNodeHash(vo.getIndexNodeHash());
            result.setHitCount(vo.getHitCount());
            result.setEnabled(vo.getEnabled());
            result.setDisabledAt(vo.getDisabledAt());
            result.setDisabledBy(vo.getDisabledBy());
            result.setStatus(vo.getStatus());
            result.setCreatedBy(vo.getCreatedBy());
            result.setCreatedAt(vo.getCreatedAt());
            result.setIndexingAt(vo.getIndexingAt());
            result.setCompletedAt(vo.getCompletedAt());
            result.setError(vo.getError());
            result.setStoppedAt(vo.getStoppedAt());
            result.setDocumentName(vo.getDocumentName());
            result.setScore(vo.getScore());

            retrieveResults.add(result);
        }
        return retrieveResults;
    }

    @Override
    public List<KmcKnowledgeBaseRespDTO> getKnowledgeBaseList() {
        List<KmcKnowledgeBaseDO> kmcKnowledgeBaseDOS = iKmcKnowledgeBaseService.getKmcKnowledgeBaseList();
        List<KmcKnowledgeBaseRespDTO> kmcDocumentReqDTOList = new ArrayList<>();
        kmcKnowledgeBaseDOS.forEach(item ->{
            KmcKnowledgeBaseRespDTO kmcKnowledgeBaseReqDTO = new KmcKnowledgeBaseRespDTO();
            // 使用BeanUtils拷贝属性（推荐方式）
            BeanUtils.copyProperties(item, kmcKnowledgeBaseReqDTO);
            kmcDocumentReqDTOList.add(kmcKnowledgeBaseReqDTO);
        });
        return kmcDocumentReqDTOList;
    }

    @Override
    public List<KmcKnowledgeBaseRespDTO> getKnowledgeBaseByIds(List<Long> ids) {
        List<KmcKnowledgeBaseDO> kmcKnowledgeBaseDOS = iKmcKnowledgeBaseService.listByIds(ids);
        List<KmcKnowledgeBaseRespDTO> kmcDocumentReqDTOList = new ArrayList<>();
        kmcKnowledgeBaseDOS.forEach(item ->{
            KmcKnowledgeBaseRespDTO kmcKnowledgeBaseReqDTO = new KmcKnowledgeBaseRespDTO();
            // 使用BeanUtils拷贝属性（推荐方式）
            BeanUtils.copyProperties(item, kmcKnowledgeBaseReqDTO);
            kmcDocumentReqDTOList.add(kmcKnowledgeBaseReqDTO);
        });
        return kmcDocumentReqDTOList;
    }

    @Override
    public List<KmcKnowledgeBaseRespDTO> getKmcKnowledgeBaseList(Long userId, Boolean isValid) {
        // 获取角色列表
        List<SysRole> sysRoles = sysRoleService.selectRoleByUserId(userId);
        List<Long> roleIds = sysRoles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        Set<Long> idList = Sets.newHashSet();
        if (!roleIds.isEmpty()) {
            // 获取知识库角色列表
            List<KmcKnowledgeRoleDO> roleDOList = kmcKnowledgeRoleService.list(
                    new LambdaQueryWrapperX<KmcKnowledgeRoleDO>()
                            .inIfPresent(KmcKnowledgeRoleDO::getRoleId, roleIds)
            );
            idList = roleDOList.stream().map(KmcKnowledgeRoleDO::getKnowledgeId).collect(Collectors.toSet());
        }

        // 是否为管理员标识符
        boolean isAdmin = sysRoles.stream()
                .anyMatch(role -> "admin".equals(role.getRoleKey()) || "system".equals(role.getRoleKey())  || "sales".equals(role.getRoleKey()));


        List<KmcKnowledgeBaseDO> kmcKnowledgeBaseDOList = kmcKnowledgeBaseMapper.getKmcKnowledgeBaseList(idList, userId, isAdmin, isValid);
        List<KmcKnowledgeBaseRespDTO> list = new ArrayList<>();
        kmcKnowledgeBaseDOList.forEach(item -> {
            KmcKnowledgeBaseRespDTO knowledgeBaseRespDTO = new KmcKnowledgeBaseRespDTO();
            // 使用BeanUtils拷贝属性
            BeanUtils.copyProperties(item, knowledgeBaseRespDTO);
            list.add(knowledgeBaseRespDTO);
        });
        return list;
    }

}
