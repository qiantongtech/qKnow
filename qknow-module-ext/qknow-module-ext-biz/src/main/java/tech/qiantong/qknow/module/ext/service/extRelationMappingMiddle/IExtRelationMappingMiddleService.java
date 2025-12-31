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

package tech.qiantong.qknow.module.ext.service.extRelationMappingMiddle;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 关系映射中间Service接口
 *
 * @author qknow
 * @date 2025-12-16
 */
public interface IExtRelationMappingMiddleService extends IService<ExtRelationMappingMiddleDO> {

    /**
     * 获得关系映射中间分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系映射中间分页列表
     */
    PageResult<ExtRelationMappingMiddleDO> getExtRelationMappingMiddlePage(ExtRelationMappingMiddlePageReqVO pageReqVO);

    /**
     * 创建关系映射中间
     *
     * @param createReqVO 关系映射中间信息
     * @return 关系映射中间编号
     */
    Long createExtRelationMappingMiddle(ExtRelationMappingMiddleSaveReqVO createReqVO);

    /**
     * 更新关系映射中间
     *
     * @param updateReqVO 关系映射中间信息
     */
    int updateExtRelationMappingMiddle(ExtRelationMappingMiddleSaveReqVO updateReqVO);

    /**
     * 删除关系映射中间
     *
     * @param idList 关系映射中间编号
     */
    int removeExtRelationMappingMiddle(Collection<Long> idList);

    /**
     * 获得关系映射中间详情
     *
     * @param id 关系映射中间编号
     * @return 关系映射中间
     */
    ExtRelationMappingMiddleDO getExtRelationMappingMiddleById(Long id);

    /**
     * 获得全部关系映射中间列表
     *
     * @return 关系映射中间列表
     */
    List<ExtRelationMappingMiddleDO> getExtRelationMappingMiddleList();

    /**
     * 获得全部关系映射中间 Map
     *
     * @return 关系映射中间 Map
     */
    Map<Long, ExtRelationMappingMiddleDO> getExtRelationMappingMiddleMap();


    /**
     * 导入关系映射中间数据
     *
     * @param importExcelList 关系映射中间数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtRelationMappingMiddle(List<ExtRelationMappingMiddleRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
