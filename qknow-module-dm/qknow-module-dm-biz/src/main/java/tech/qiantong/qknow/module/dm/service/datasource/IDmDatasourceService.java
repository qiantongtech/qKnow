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

package tech.qiantong.qknow.module.dm.service.datasource;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.database.DbQuery;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.module.dm.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qknow.module.dm.api.model.dto.DmModelColumnReqDTO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourcePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.datasource.DmDatasourceDO;


import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据源Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDmDatasourceService extends IService<DmDatasourceDO> {

    /**
     * 获得数据源分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据源分页列表
     */
    PageResult<DmDatasourceDO> getDmDatasourcePage(DmDatasourcePageReqVO pageReqVO);
    List<DmDatasourceDO> getDaDatasourceList(DmDatasourcePageReqVO pageReqVO);


    /**
     * 查询数据资产的数据源连接信息
     * @param daAsset
     * @return
     */
    List<DmDatasourceDO> getDataSourceByAsset(DmDatasourceRespVO daAsset);




    /**
     * 创建数据源
     *
     * @param createReqVO 数据源信息
     * @return 数据源编号
     */
    Long createDaDatasource(DmDatasourceSaveReqVO createReqVO);

    /**
     * 更新数据源
     *
     * @param updateReqVO 数据源信息
     */
    int updateDaDatasource(DmDatasourceSaveReqVO updateReqVO);

    /**
     * 删除数据源
     *
     * @param idList 数据源编号
     */
    int removeDaDatasource(Collection<Long> idList);

    /**
     * 获得数据源详情
     *
     * @param id 数据源编号
     * @return 数据源
     */
    DmDatasourceDO getDaDatasourceById(Long id);

    /**
     * 获得全部数据源列表
     *
     * @return 数据源列表
     */
    List<DmDatasourceDO> getDaDatasourceList();

    /**
     * 获得全部数据源 Map
     *
     * @return 数据源 Map
     */
    Map<Long, DmDatasourceDO> getDaDatasourceMap();


    /**
     * 导入数据源数据
     *
     * @param importExcelList 数据源数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaDatasource(List<DmDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName);


    AjaxResult clientsTest(Long id);

    /**
     * 获取数据库表信息
     * @param id  数据源id
     * @return
     */
    List<DbTable> getDbTables(Long id);

    /**
     * 获取数据库
     * 表的字段信息
     * @param id  数据源id
     * @param tableName   表名称
     * @return
     */
    List<DbColumn> getDbTableColumns(Long id, String tableName);


    /**
     * 获取数据表里面的数据字段
     * @param jsonObject 数据源id和数据表
     * @return
     */
    List<DmModelColumnReqDTO> getColumnsList(JSONObject jsonObject);

    /**
     * 建表工具方法
     * @param datasourceCreaTeTableReqDTO  单表
     * @return
     */
    boolean creaDatasourceTeTable(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);
    boolean creaDatasourceTeTable(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO);


    DatasourceCreaTeTableReqDTO createTaskTempTable(Map<String, Object> map);
}
