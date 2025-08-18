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
