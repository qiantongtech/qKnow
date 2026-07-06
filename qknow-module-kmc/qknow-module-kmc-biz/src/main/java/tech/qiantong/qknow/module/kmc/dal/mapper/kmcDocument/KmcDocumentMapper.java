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

package tech.qiantong.qknow.module.kmc.dal.mapper.kmcDocument;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.*;

/**
 * 知识文件Mapper接口
 *
 * @author qknow
 * @date 2025-02-14
 */
public interface KmcDocumentMapper extends BaseMapperX<KmcDocumentDO> {

    default PageResult<KmcDocumentDO> selectPage(KmcDocumentPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time", "(preview_count + download_count)"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<KmcDocumentDO>()
                .eqIfPresent(KmcDocumentDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(KmcDocumentDO::getCategoryId, reqVO.getCategoryId())
                .likeIfPresent(KmcDocumentDO::getCategoryName, reqVO.getCategoryName())
                .likeIfPresent(KmcDocumentDO::getName, reqVO.getName())
                .eqIfPresent(KmcDocumentDO::getPath, reqVO.getPath())
                .eqIfPresent(KmcDocumentDO::getDescription, reqVO.getDescription())
                .eqIfPresent(KmcDocumentDO::getSyncStatus, reqVO.getSyncStatus())
                .eqIfPresent(KmcDocumentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(KmcDocumentDO::getUpdaterId, reqVO.getUpdaterId())
                .eqIfPresent(KmcDocumentDO::getKnowledgeBaseId, reqVO.getKnowledgeBaseId())
                .inIfPresent(KmcDocumentDO::getCategoryId, reqVO.getIds())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(KmcDocumentDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    @Select("<script>" +
            "SELECT * FROM kmc_document WHERE id IN " +
            "<foreach item='item' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<KmcDocumentDO> getKmcDocumentListByIds(@Param("ids") List<Long> ids);

    @Select("SELECT category_id, COUNT(category_id) AS count FROM kmc_document WHERE del_flag = 0 GROUP BY category_id")
    List<Map<String, Object>> getFileTypes();
}
