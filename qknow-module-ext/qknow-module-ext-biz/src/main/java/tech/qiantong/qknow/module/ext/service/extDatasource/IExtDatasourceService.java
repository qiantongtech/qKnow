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

package tech.qiantong.qknow.module.ext.service.extDatasource;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDataSourceTable;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDatasource;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDatasourceDO;

/**
 * 数据源Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtDatasourceService extends IService<ExtDatasourceDO> {

    public AjaxResult getTableDataByDataId(ExtDataSourceTable sourceTable);

//    public AjaxResult getTableList(ExtDatasource extDatasource);
//
//    public AjaxResult getTableData(ExtDataSourceTable sourceTable);

//    public AjaxResult testConnection(Long id);
//
//    /**
//     * 获得数据源分页列表
//     *
//     * @param pageReqVO 分页请求
//     * @return 数据源分页列表
//     */
//    PageResult<ExtDatasourceDO> getExtDatasourcePage(ExtDatasourcePageReqVO pageReqVO);
//
//    /**
//     * 创建数据源
//     *
//     * @param createReqVO 数据源信息
//     * @return 数据源编号
//     */
//    Long createExtDatasource(ExtDatasourceSaveReqVO createReqVO);
//
//    /**
//     * 更新数据源
//     *
//     * @param updateReqVO 数据源信息
//     */
//    int updateExtDatasource(ExtDatasourceSaveReqVO updateReqVO);
//
//    /**
//     * 删除数据源
//     *
//     * @param idList 数据源编号
//     */
//    int removeExtDatasource(Collection<Long> idList);
//
//    /**
//     * 获得数据源详情
//     *
//     * @param id 数据源编号
//     * @return 数据源
//     */
//    ExtDatasourceDO getExtDatasourceById(Long id);
//
//    /**
//     * 获得全部数据源列表
//     *
//     * @return 数据源列表
//     */
//    List<ExtDatasourceDO> getExtDatasourceList();
//
//    /**
//     * 获得全部数据源 Map
//     *
//     * @return 数据源 Map
//     */
//    Map<Long, ExtDatasourceDO> getExtDatasourceMap();
//
//
//    /**
//     * 导入数据源数据
//     *
//     * @param importExcelList 数据源数据列表
//     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
//     * @param operName        操作用户
//     * @return 结果
//     */
//    String importExtDatasource(List<ExtDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
