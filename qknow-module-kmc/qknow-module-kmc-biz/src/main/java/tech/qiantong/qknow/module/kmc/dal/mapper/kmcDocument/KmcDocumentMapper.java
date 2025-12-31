/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.kmc.dal.mapper.kmcDocument;

import java.util.*;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.qiantong.qknow.common.core.page.PageResult;

import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 知识文件Mapper接口
 *
 * @author qknow
 * @date 2025-02-14
 */
public interface KmcDocumentMapper extends BaseMapperX<KmcDocumentDO> {

    default PageResult<KmcDocumentDO> selectPage(KmcDocumentPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<KmcDocumentDO>()
                .eqIfPresent(KmcDocumentDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(KmcDocumentDO::getCategoryId, reqVO.getCategoryId())
                .likeIfPresent(KmcDocumentDO::getCategoryName, reqVO.getCategoryName())
                .likeIfPresent(KmcDocumentDO::getName, reqVO.getName())
                .eqIfPresent(KmcDocumentDO::getPath, reqVO.getPath())
                .eqIfPresent(KmcDocumentDO::getDescription, reqVO.getDescription())
                .eqIfPresent(KmcDocumentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(KmcDocumentDO::getUpdaterId, reqVO.getUpdaterId())
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

    @Select("SELECT category_name, COUNT(*) AS count FROM kmc_document WHERE del_flag = 0 GROUP BY category_name")
    List<Map<String, Object>> getFileTypes();
}
