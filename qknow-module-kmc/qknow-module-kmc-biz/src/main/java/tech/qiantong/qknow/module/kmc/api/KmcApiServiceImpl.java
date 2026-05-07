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
