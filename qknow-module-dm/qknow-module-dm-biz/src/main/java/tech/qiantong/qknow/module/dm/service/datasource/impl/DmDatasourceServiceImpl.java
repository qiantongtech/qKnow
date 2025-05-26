package tech.qiantong.qknow.module.dm.service.datasource.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.database.DataSourceFactory;
import tech.qiantong.qknow.common.database.DbQuery;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import tech.qiantong.qknow.common.enums.KingbaseColumnTypeEnum;
import tech.qiantong.qknow.common.enums.MySqlColumnTypeEnum;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.JSONUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;

import tech.qiantong.qknow.module.dm.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qknow.module.dm.api.datasource.dto.DmDatasourceRespDTO;
import tech.qiantong.qknow.module.dm.api.model.dto.DmModelColumnReqDTO;
import tech.qiantong.qknow.module.dm.api.model.dto.DmModelColumnRespDTO;
import tech.qiantong.qknow.module.dm.api.service.asset.IDmDatasourceApiService;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourcePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.datasource.DmDatasourceDO;
import tech.qiantong.qknow.module.dm.dal.mapper.datasource.DmDatasourceMapper;
import tech.qiantong.qknow.module.dm.service.datasource.IDmDatasourceService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qknow.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据源Service业务层处理
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmDatasourceServiceImpl extends ServiceImpl<DmDatasourceMapper, DmDatasourceDO> implements IDmDatasourceService, IDmDatasourceApiService {
    @Resource
    private DmDatasourceMapper dmDatasourceMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;


    @Autowired
    private IRedisService redisService;


    // 本地文件路径前缀
    private static String prefixUrl;
    private static String tempDataSource;


    /**
     * 查询数据资产的数据源连接信息
     * @param daAsset
     * @return
     */
    @Override
    public List<DmDatasourceDO> getDataSourceByAsset(DmDatasourceRespVO daAsset) {
        return dmDatasourceMapper.getDataSourceByAsset();
    }

    @Override
    public PageResult<DmDatasourceDO> getDmDatasourcePage(DmDatasourcePageReqVO pageReqVO) {
        return dmDatasourceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DmDatasourceDO> getDaDatasourceList(DmDatasourcePageReqVO reqVO) {
        LambdaQueryWrapperX<DmDatasourceDO> daDatasourceDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        daDatasourceDOLambdaQueryWrapperX.likeIfPresent(DmDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .eqIfPresent(DmDatasourceDO::getDatasourceType, reqVO.getDatasourceType())
                .eqIfPresent(DmDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eqIfPresent(DmDatasourceDO::getIp, reqVO.getIp())
                .eqIfPresent(DmDatasourceDO::getPort, reqVO.getPort())
                .eqIfPresent(DmDatasourceDO::getListCount, reqVO.getListCount())
                .eqIfPresent(DmDatasourceDO::getSyncCount, reqVO.getSyncCount())
                .eqIfPresent(DmDatasourceDO::getDataSize, reqVO.getDataSize())
                .eqIfPresent(DmDatasourceDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmDatasourceDO::getCreateTime, reqVO.getCreateTime());

        return dmDatasourceMapper.selectList(daDatasourceDOLambdaQueryWrapperX);
    }

    @Override
    public Long createDaDatasource(DmDatasourceSaveReqVO createReqVO) {
        DmDatasourceDO dictType = BeanUtils.toBean(createReqVO, DmDatasourceDO.class);
        dmDatasourceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaDatasource(DmDatasourceSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据源
        DmDatasourceDO updateObj = BeanUtils.toBean(updateReqVO, DmDatasourceDO.class);
        return dmDatasourceMapper.updateById(updateObj);
    }
    @Override
    public int removeDaDatasource(Collection<Long> idList) {
        // 批量删除数据源
        return dmDatasourceMapper.deleteBatchIds(idList);
    }

    @Override
    public DmDatasourceDO getDaDatasourceById(Long id) {
        return dmDatasourceMapper.selectById(id);
    }

    @Override
    public List<DmDatasourceDO> getDaDatasourceList() {
        return dmDatasourceMapper.selectList();
    }

    @Override
    public Map<Long, DmDatasourceDO> getDaDatasourceMap() {
        List<DmDatasourceDO> daDatasourceList = dmDatasourceMapper.selectList();
        return daDatasourceList.stream()
                .collect(Collectors.toMap(
                        DmDatasourceDO::getId,
                        daDatasourceDO -> daDatasourceDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据源数据
     *
     * @param importExcelList 数据源数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaDatasource(List<DmDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmDatasourceRespVO respVO : importExcelList) {
            try {
                DmDatasourceDO daDatasourceDO = BeanUtils.toBean(respVO, DmDatasourceDO.class);
                Long daDatasourceId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDatasourceId != null) {
                        DmDatasourceDO existingDaDatasource = dmDatasourceMapper.selectById(daDatasourceId);
                        if (existingDaDatasource != null) {
                            dmDatasourceMapper.updateById(daDatasourceDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daDatasourceId + " 的数据源记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daDatasourceId + " 的数据源记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmDatasourceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceId);
                    DmDatasourceDO existingDaDatasource = dmDatasourceMapper.selectOne(queryWrapper);
                    if (existingDaDatasource == null) {
                        dmDatasourceMapper.insert(daDatasourceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daDatasourceId + " 的数据源记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daDatasourceId + " 的数据源记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }


    @Override
    public AjaxResult clientsTest(Long id) {
        DbQuery dbQuery = this.buildDbQuery(id);
        if (dbQuery.valid()) {
            return AjaxResult.success("数据库连接成功");
        }
        return AjaxResult.error("数据库连接失败");

    }

    public DbQuery buildDbQuery(Long id) {
        DmDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceBy.getDatasourceType(),
                daDatasourceBy.getIp(),
                daDatasourceBy.getPort(),
                daDatasourceBy.getDatasourceConfig()
        );
        return dataSourceFactory.createDbQuery(dbQueryProperty);
    }

    /**
     * @param id 数据源id
     * @return
     */
    @Override
    public List<DbTable> getDbTables(Long id) {
        DbQuery dbQuery = null;
        try {
            DmDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
            if (daDatasourceBy == null) {
                throw new DataQueryException("数据源详情信息查询失败");
            }

            DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                    , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
            dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("数据库连接失败");
            }
            List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
            return tables;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        } finally {
            dbQuery.close();
        }
    }

    /**
     * @param id        数据源id
     * @param tableName 表名称
     * @return
     */
    @Override
    public List<DbColumn> getDbTableColumns(Long id, String tableName) {
        DbQuery dbQuery = null;
        try {
            if (StringUtils.isEmpty(tableName)) {
                throw new DataQueryException("表名不能为空");
            }

            DmDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
            if (daDatasourceBy == null) {
                throw new DataQueryException("数据源详情信息查询失败");
            }

            DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                    , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
             dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("数据库连接失败");
            }

            List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, tableName);
            return tableColumns;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        } finally {
            dbQuery.close();
        }
    }

    /**
     * 获取数据表里面的数据字段
     *
     * @param jsonObject 数据源id和数据表
     * @return
     */
    @Override
    public List<DmModelColumnReqDTO> getColumnsList(JSONObject jsonObject) {
        List<DmModelColumnRespDTO> modelIdColumnList = new ArrayList<>();
        Boolean isOld = jsonObject.getStr("isOld") == null ? null : Boolean.valueOf(jsonObject.getStr("isOld"));
        if (isOld != null && !isOld && jsonObject.getStr("modelId") != null){
//            modelIdColumnList = dpModelApiService.getModelIdColumnList(Long.valueOf(jsonObject.getStr("modelId")));
        }
        if (modelIdColumnList.size() > 0){
            List<DmModelColumnReqDTO> columnReqDTOList = BeanUtils.toBean(modelIdColumnList, DmModelColumnReqDTO.class);
            return columnReqDTOList;
        }
        List<DbColumn> columnList = this.getDbTableColumns(Long.valueOf(jsonObject.getStr("id")), jsonObject.getStr("tableName"));
        List<DmModelColumnReqDTO> columnReqDTOList = new ArrayList<>();
        for (DbColumn column : columnList) {
            String dataType = column.getDataType();
            switch (jsonObject.getStr("type")) {
                case "DM8":
                case "MYSQL":
                    column.setDataType(MySqlColumnTypeEnum.convertToDmType(dataType));
                    break;
                case "Kingbase8":
                    column.setDataType(KingbaseColumnTypeEnum.convertToDmType(dataType));
                    break;
            }
            DmModelColumnReqDTO dpModelColumnReqDTO = new DmModelColumnReqDTO(column);
            columnReqDTOList.add(dpModelColumnReqDTO);
        }
        return columnReqDTOList;
    }

    @Override
    public boolean creaDatasourceTeTable(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO) {
        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceCreaTeTableReqDTO.getDatasourceType()
                ,datasourceCreaTeTableReqDTO.getIp(),datasourceCreaTeTableReqDTO.getPort(),datasourceCreaTeTableReqDTO.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }

        int tableStatus = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName());
        if (tableStatus > 0) {
            return false;
        }

        List<String> tableSQLList = dbQuery.generateCreateTableSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName(), datasourceCreaTeTableReqDTO.getTableComment(), datasourceCreaTeTableReqDTO.getColumnsList());

        for (String sql : tableSQLList) {
            dbQuery.execute(sql);
        }
        return true;
    }


    @Override
    public boolean creaDatasourceTeTable(DbQuery dbQuery,DbQueryProperty dbQueryProperty,DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO) {

        int tableStatus = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName());
        if (tableStatus > 0) {
            return false;
        }

        List<String> tableSQLList = dbQuery.generateCreateTableSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName(), datasourceCreaTeTableReqDTO.getTableComment(), datasourceCreaTeTableReqDTO.getColumnsList());

        for (String sql : tableSQLList) {
            dbQuery.execute(sql);
        }
        return true;
    }


    @Override
    public DatasourceCreaTeTableReqDTO createTaskTempTable(Map<String, Object> taskParams) {

        DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO = buildJobDatasource(taskParams);

        creaDatasourceTeTable(datasourceCreaTeTableReqDTO);
        datasourceCreaTeTableReqDTO.setColumnsList(null);
        return datasourceCreaTeTableReqDTO;
    }

    /**
     * 创建对象
     * @param taskParams
     * @return
     */
    private static DatasourceCreaTeTableReqDTO buildJobDatasource(Map<String, Object> taskParams) {
        Map<String, Object> datasource = JSONUtils.convertTaskDefinitionJsonMap(tempDataSource);

//        Map<String, Object> datasource = (Map<String, Object>) MapUtils.getObject(taskParams, "readerDatasource");
        DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO = new DatasourceCreaTeTableReqDTO();
        String ip = MapUtils.getString(datasource, "ip");
        long port = MapUtils.getLong(datasource, "port");
        String datasourceConfig = MapUtils.getString(datasource, "datasourceConfig");
        String datasourceType = MapUtils.getString(datasource, "datasourceType");

        datasourceCreaTeTableReqDTO.setDatasourceType(datasourceType);
        datasourceCreaTeTableReqDTO.setDatasourceConfig(datasourceConfig);
        datasourceCreaTeTableReqDTO.setIp(ip);
        datasourceCreaTeTableReqDTO.setPort(port);

        //
//        String table_name = "task_"+StringUtils.generateRandomString();
        String table_name = "task_";


        datasourceCreaTeTableReqDTO.setTableName(table_name);

        List<Map<String, Object>> tableColumns = (List<Map<String, Object>>) MapUtils.getObject(taskParams, "tableFields");
        List<DbColumn> columnsList = convertToDbColumnList(tableColumns);
        datasourceCreaTeTableReqDTO.setColumnsList(columnsList);


        return datasourceCreaTeTableReqDTO;
    }


    public static List<DbColumn> convertToDbColumnList(List<Map<String, Object>> tableColumns) {
        return tableColumns.stream().map(columnMap -> {
            DbColumn dbColumn = new DbColumn();


            // 遍历 columnMap，将其映射到 DbColumn
            dbColumn.setColName(MapUtils.getString(columnMap,"columnName"));
            dbColumn.setDataType(MapUtils.getString(columnMap,"columnType"));
            dbColumn.setDataLength(MapUtils.getString(columnMap,"columnLength"));
            dbColumn.setDataPrecision(MapUtils.getString(columnMap,"columnPrecision"));
            dbColumn.setDataScale(MapUtils.getString(columnMap,"columnScale"));

            // 转换 pkFlag 和 nullableFlag 字段
            dbColumn.setColKey("1".equals(columnMap.get("pkFlag")));  // "1" 是主键，"0" 不是
            dbColumn.setNullable("1".equals(columnMap.get("nullableFlag")));  // "1" 是可空，"0" 不是

            dbColumn.setColPosition((Integer) columnMap.get("colPosition"));
            dbColumn.setDataDefault(MapUtils.getString(columnMap,"defaultValue"));
            dbColumn.setColComment(MapUtils.getString(columnMap,"columnComment"));

            return dbColumn;
        }).collect(Collectors.toList());
    }

    @Override
    public DmDatasourceRespDTO getDatasourceById(Long id) {
        DmDatasourceRespDTO dto = new DmDatasourceRespDTO();
        DmDatasourceDO daDatasourceDO = dmDatasourceMapper.selectById(id);
        org.springframework.beans.BeanUtils.copyProperties(daDatasourceDO, dto);
        return dto;
    }
}
