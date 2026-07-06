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

package tech.qiantong.qknow.module.ext.service.extSchema;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaPageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
/**
 * 概念配置Service接口
 *
 * @author qknow
 * @date 2025-02-17
 */
public interface IExtSchemaService extends IService<ExtSchemaDO> {

    /**
     * 获得概念配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念配置分页列表
     */
    PageResult<ExtSchemaDO> getExtSchemaPage(ExtSchemaPageReqVO pageReqVO);

    /**
     * 创建概念配置
     *
     * @param createReqVO 概念配置信息
     * @return 概念配置编号
     */
    Long createExtSchema(ExtSchemaSaveReqVO createReqVO);

    /**
     * 更新概念配置
     *
     * @param updateReqVO 概念配置信息
     */
    int updateExtSchema(ExtSchemaSaveReqVO updateReqVO);

    /**
     * 删除概念配置
     *
     * @param idList 概念配置编号
     */
    int removeExtSchema(Collection<Long> idList);

    /**
     * 获得概念配置详情
     *
     * @param id 概念配置编号
     * @return 概念配置
     */
    ExtSchemaDO getExtSchemaById(Long id);

    /**
     * 获得全部概念配置列表
     *
     * @return 概念配置列表
     */
    List<ExtSchemaDO> getExtSchemaList();

    /**
     * 获得全部概念配置 Map
     *
     * @return 概念配置 Map
     */
    Map<Long, ExtSchemaDO> getExtSchemaMap();


    /**
     * 导入概念配置数据
     *
     * @param importExcelList 概念配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtSchema(List<ExtSchemaRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获得全部概念配置数据
     *
     * @return 全部概念配置数据
     */
    List<ExtSchemaDO> getExtSchemaAllList(ExtSchemaDO extSchemaDO);
}
