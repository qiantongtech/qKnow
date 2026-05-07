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

package tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeBase;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeBasePageReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 知识库Mapper接口
 *
 * @author qknow
 * @date 2025-07-22
 */
public interface KmcKnowledgeBaseMapper extends BaseMapperX<KmcKnowledgeBaseDO> {

    default PageResult<KmcKnowledgeBaseDO> selectPage(KmcKnowledgeBasePageReqVO reqVO, Set<Long> idList, Long userId, Boolean isAdmin) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        LambdaQueryWrapperX<KmcKnowledgeBaseDO> queryWrapper = new LambdaQueryWrapperX<KmcKnowledgeBaseDO>()
                .eqIfPresent(KmcKnowledgeBaseDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(KmcKnowledgeBaseDO::getQmDatasetId, reqVO.getQmDatasetId())
                .likeIfPresent(KmcKnowledgeBaseDO::getName, reqVO.getName())
                .eqIfPresent(KmcKnowledgeBaseDO::getDescription, reqVO.getDescription())
                .eqIfPresent(KmcKnowledgeBaseDO::getIndexingTechnique, reqVO.getIndexingTechnique())
                .eqIfPresent(KmcKnowledgeBaseDO::getPermission, reqVO.getPermission())
                .eqIfPresent(KmcKnowledgeBaseDO::getEmbeddingModel, reqVO.getEmbeddingModel())
                .eqIfPresent(KmcKnowledgeBaseDO::getEmbeddingModelProvider, reqVO.getEmbeddingModelProvider())
                .eqIfPresent(KmcKnowledgeBaseDO::getSearchMethod, reqVO.getSearchMethod())
                .eqIfPresent(KmcKnowledgeBaseDO::getRerankingEnable, reqVO.getRerankingEnable())
                .likeIfPresent(KmcKnowledgeBaseDO::getRerankingProviderName, reqVO.getRerankingProviderName())
                .likeIfPresent(KmcKnowledgeBaseDO::getRerankingModelName, reqVO.getRerankingModelName())
                .eqIfPresent(KmcKnowledgeBaseDO::getTopK, reqVO.getTopK())
                .eqIfPresent(KmcKnowledgeBaseDO::getScoreThresholdEnabled, reqVO.getScoreThresholdEnabled())
                .eqIfPresent(KmcKnowledgeBaseDO::getScoreThreshold, reqVO.getScoreThreshold())
                .eqIfPresent(KmcKnowledgeBaseDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(KmcKnowledgeBaseDO::getValidFlag, reqVO.getValidFlag());

        // 添加权限条件：用户有权限的知识库 或者 用户自己创建的知识库
        if (idList != null && !idList.isEmpty() && !isAdmin) {
            queryWrapper.and(wrapper ->
                    wrapper.in(KmcKnowledgeBaseDO::getId, idList)
                            .or()
                            .eq(KmcKnowledgeBaseDO::getCreatorId, userId)
            );
        } else if (!isAdmin) {
            // 如果没有角色权限，则只显示用户自己创建的知识库
            queryWrapper.eqIfPresent(KmcKnowledgeBaseDO::getCreatorId, userId);
        }
        queryWrapper.orderByDesc(KmcKnowledgeBaseDO::getValidFlag);
        queryWrapper.orderByDesc(KmcKnowledgeBaseDO::getCreateTime);
        // 构造动态查询条件
        return selectPage(reqVO, queryWrapper
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(KmcKnowledgeBaseDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    default List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseList(Set<Long> idList, Long userId, Boolean isAdmin, Boolean isValid) {
        LambdaQueryWrapperX<KmcKnowledgeBaseDO> queryWrapper = new LambdaQueryWrapperX<KmcKnowledgeBaseDO>();

        queryWrapper.eqIfPresent(KmcKnowledgeBaseDO::getValidFlag, isValid);
        queryWrapper.orderByDesc(KmcKnowledgeBaseDO::getCreateTime);

        // 添加权限条件：用户有权限的知识库 或者 用户自己创建的知识库
        if (!idList.isEmpty() && !isAdmin) {
            queryWrapper.and(wrapper ->
                    wrapper.in(KmcKnowledgeBaseDO::getId, idList)
                            .or()
                            .eq(KmcKnowledgeBaseDO::getCreatorId, userId)
            );
        } else if (!isAdmin) {
            // 如果没有角色权限，则只显示用户自己创建的知识库
            queryWrapper.eqIfPresent(KmcKnowledgeBaseDO::getCreatorId, userId);
        }
        return selectList(queryWrapper);
    }
}
