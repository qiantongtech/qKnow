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
