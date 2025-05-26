package tech.qiantong.qknow.module.ext.service.extDatasource.impl;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.sql.DataSource;

import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.DictUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.generator.domain.GenTable;
import tech.qiantong.qknow.module.dm.api.datasource.dto.DmDatasourceRespDTO;
import tech.qiantong.qknow.module.dm.api.service.asset.IDmDatasourceApiService;
import tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo.ExtDatasourcePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo.ExtDatasourceRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo.ExtDatasourceSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDataSourceTable;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDatasource;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDatasourceDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extDatasource.ExtDatasourceMapper;
import tech.qiantong.qknow.module.ext.service.extDatasource.ExtDatasourceQueryService;
import tech.qiantong.qknow.module.ext.service.extDatasource.IExtDatasourceService;

/**
 * 数据源Service业务层处理
 *
 * @author qknow
 * @date 2025-02-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtDatasourceServiceImpl extends ServiceImpl<ExtDatasourceMapper, ExtDatasourceDO> implements IExtDatasourceService {
    @Resource
    private ExtDatasourceMapper extDatasourceMapper;
    @Resource
    private ExtDatasourceQueryService dynamicDatabaseQuery;
    @Resource
    private IDmDatasourceApiService daDatasourceApiService;

    /**
     * 根据数据源id, 数据id和表名获取行数据
     *
     * @param sourceTable
     * @return
     */
    public AjaxResult getTableDataByDataId(ExtDataSourceTable sourceTable) {
        DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(sourceTable.getId());
        JSONObject jsonObject = JSONObject.parseObject(datasource.getDatasourceConfig());

        //查询行数据
        ExtDataSourceTable.GetTableData getTableData = new ExtDataSourceTable.GetTableData();
        getTableData.setDbType(datasource.getDatasourceType());
        getTableData.setUrl("jdbc:mysql://" + datasource.getIp() + ": "
                + datasource.getPort() + "/" + jsonObject.getString("dbname"));//数据库名称
        getTableData.setUsername(jsonObject.getString("username"));
        getTableData.setPassword(jsonObject.getString("password"));
        getTableData.setQuery("SELECT * FROM " + sourceTable.getTableName() + " WHERE ID = " + sourceTable.getDataId());
        List<ConcurrentHashMap<String, Object>> tableData = dynamicDatabaseQuery.getTableData(getTableData);

        //查询表中每个字段是什么意思
        AjaxResult queryTableData = dynamicDatabaseQuery.getTableData(datasource.getDatasourceType(),
                jsonObject.getString("dbname"),
                getTableData.getUrl(),
                getTableData.getUsername(),
                getTableData.getPassword(),
                sourceTable.getTableName());
        JSONArray data = null;
        if (queryTableData.isSuccess()) {
            data = JSONArray.parseArray(queryTableData.get("data").toString());
        }

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        ConcurrentHashMap<String, Object> concurrentHashMap = tableData.get(0);
        for (Map.Entry<String, Object> objectEntry : concurrentHashMap.entrySet()) {
            String key = objectEntry.getKey();
            for (Object datum : data) {
                JSONObject parseObject = JSONObject.parseObject(datum.toString());
                if (parseObject.getString("columnName").equals(key)) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("key", key);
                    hashMap.put("value", objectEntry.getValue());
                    hashMap.put("description", parseObject.getString("description"));
                    hashMap.put("field", key + "(" + parseObject.getString("description") + ")");
                    arrayList.add(hashMap);
                }
            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("type", getTableData.getDbType());
        map.put("host", datasource.getIp());
        map.put("databaseName", jsonObject.getString("dbname"));
        map.put("tableName", sourceTable.getTableName());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("database", map);
        hashMap.put("tableData", arrayList);
        return AjaxResult.success(hashMap);
    }

//    /**
//     * 获取数据库表列表
//     *
//     * @param extDatasource
//     * @return
//     */
//    public AjaxResult getTableList(ExtDatasource extDatasource) {
//        ExtDatasourceDO datasourceById = getById(extDatasource.getTableId());
//        String url = ""; // 数据库连接参数
//        String user = datasourceById.getUsername(); // 替换成你的数据库用户名
//        String password = datasourceById.getPassword(); // 替换成你的数据库密码
//        String dbType = DictUtils.getDictLabel("ext_data_source_type", datasourceById.getType().toString(), "");
//        switch (datasourceById.getType()) {
//            // 加载 MySQL 驱动
//            case 0:
//                url = "jdbc:mysql://" + datasourceById.getHost() + ": "
//                        + datasourceById.getPort() + "/" + datasourceById.getDatabaseName(); // 替换成你的数据库名称
//                break;
//            // 加载 其他数据库 驱动
//            case 1:
//                break;
//        }
//        AjaxResult tableListRes = dynamicDatabaseQuery.getTableList(dbType, url, user, password);
//        if (tableListRes.isSuccess()) {
//            List<GenTable> dataList = (List<GenTable>) tableListRes.get("data");
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("total", dataList.size());
//            if (extDatasource.getPageNum() != null && extDatasource.getPageSize() != null) {
//                Integer pageNum = extDatasource.getPageNum();
//                Integer pageSize = extDatasource.getPageSize();
//                // 计算起始位置
//                int startIndex = (pageNum - 1) * pageSize;
//                // 计算结束位置
//                int endIndex = Math.min(startIndex + pageSize, dataList.size());
//                hashMap.put("list", dataList.subList(startIndex, endIndex));
//            } else {
//                hashMap.put("list", dataList);
//            }
//
//            return AjaxResult.success("查询成功", hashMap);
//        }
//        return tableListRes;
//    }
//
//    /**
//     * 获取表详细信息
//     *
//     * @param sourceTable
//     * @return
//     */
//    public AjaxResult getTableData(ExtDataSourceTable sourceTable) {
//        ExtDatasourceDO datasourceById = getById(sourceTable.getId());
//        String dbType = "";
//        String url = "";
//        String databaseName = datasourceById.getDatabaseName();
//        switch (datasourceById.getType()) {
//            // 加载 MySQL 驱动
//            case 0:
//                dbType = "mysql";
//                url = "jdbc:mysql://" + datasourceById.getHost() + ": "
//                        + datasourceById.getPort() + "/" + datasourceById.getDatabaseName(); // 替换成你的数据库名称
//                break;
//            // 加载 其他数据库 驱动
//            case 1:
//                break;
//        }
//        return dynamicDatabaseQuery.getTableData(dbType, databaseName, url, datasourceById.getUsername(), datasourceById.getPassword(), sourceTable.getTableName());
//    }
//    public AjaxResult testConnection(Long id) {
//        ExtDatasourceDO datasourceById = getById(id);
//        Connection connection = null;
//
//        try {
//            String url = ""; // 数据库连接参数
//            String user = datasourceById.getUsername(); // 替换成你的数据库用户名
//            String password = datasourceById.getPassword(); // 替换成你的数据库密码
//            switch (datasourceById.getType()) {
//                // 加载 MySQL 驱动
//                case 0:
////                    Class.forName("com.mysql.cj.jdbc.Driver");
//                    url = "jdbc:mysql://" + datasourceById.getHost() + ": "
//                            + datasourceById.getPort() + "/" + datasourceById.getDatabaseName();
//                    break;
//                // 加载 其他数据库 驱动
//                case 1:
//                    break;
//            }
//
//            // 获取连接
//            connection = DriverManager.getConnection(url, user, password);
//            if (connection != null) {
//                return AjaxResult.success("连接成功");
//            } else {
//                return AjaxResult.error("连接失败");
//            }
//        } catch (SQLException e) {
//            log.info("数据库连接失败！{}", e);
//            return AjaxResult.error("数据库连接失败");
//        } finally {
//            // 关闭连接
//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//
//    @Override
//    public PageResult<ExtDatasourceDO> getExtDatasourcePage(ExtDatasourcePageReqVO pageReqVO) {
//        return extDatasourceMapper.selectPage(pageReqVO);
//    }
//
//    @Override
//    public Long createExtDatasource(ExtDatasourceSaveReqVO createReqVO) {
//        ExtDatasourceDO dictType = BeanUtils.toBean(createReqVO, ExtDatasourceDO.class);
//        extDatasourceMapper.insert(dictType);
//        return dictType.getId();
//    }
//
//    @Override
//    public int updateExtDatasource(ExtDatasourceSaveReqVO updateReqVO) {
//        // 相关校验
//
//        // 更新数据源
//        ExtDatasourceDO updateObj = BeanUtils.toBean(updateReqVO, ExtDatasourceDO.class);
//        return extDatasourceMapper.updateById(updateObj);
//    }
//
//    @Override
//    public int removeExtDatasource(Collection<Long> idList) {
//        // 批量删除数据源
//        return extDatasourceMapper.deleteBatchIds(idList);
//    }
//
//    @Override
//    public ExtDatasourceDO getExtDatasourceById(Long id) {
//        return extDatasourceMapper.selectById(id);
//    }
//
//    @Override
//    public List<ExtDatasourceDO> getExtDatasourceList() {
//        return extDatasourceMapper.selectList();
//    }
//
//    @Override
//    public Map<Long, ExtDatasourceDO> getExtDatasourceMap() {
//        List<ExtDatasourceDO> extDatasourceList = extDatasourceMapper.selectList();
//        return extDatasourceList.stream()
//                .collect(Collectors.toMap(
//                        ExtDatasourceDO::getId,
//                        extDatasourceDO -> extDatasourceDO,
//                        // 保留已存在的值
//                        (existing, replacement) -> existing
//                ));
//    }
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
//    @Override
//    public String importExtDatasource(List<ExtDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
//        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
//            throw new ServiceException("导入数据不能为空！");
//        }
//
//        int successNum = 0;
//        int failureNum = 0;
//        List<String> successMessages = new ArrayList<>();
//        List<String> failureMessages = new ArrayList<>();
//
//        for (ExtDatasourceRespVO respVO : importExcelList) {
//            try {
//                ExtDatasourceDO extDatasourceDO = BeanUtils.toBean(respVO, ExtDatasourceDO.class);
//                Long extDatasourceId = respVO.getId();
//                if (isUpdateSupport) {
//                    if (extDatasourceId != null) {
//                        ExtDatasourceDO existingExtDatasource = extDatasourceMapper.selectById(extDatasourceId);
//                        if (existingExtDatasource != null) {
//                            extDatasourceMapper.updateById(extDatasourceDO);
//                            successNum++;
//                            successMessages.add("数据更新成功，ID为 " + extDatasourceId + " 的数据源记录。");
//                        } else {
//                            failureNum++;
//                            failureMessages.add("数据更新失败，ID为 " + extDatasourceId + " 的数据源记录不存在。");
//                        }
//                    } else {
//                        failureNum++;
//                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
//                    }
//                } else {
//                    QueryWrapper<ExtDatasourceDO> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("id", extDatasourceId);
//                    ExtDatasourceDO existingExtDatasource = extDatasourceMapper.selectOne(queryWrapper);
//                    if (existingExtDatasource == null) {
//                        extDatasourceMapper.insert(extDatasourceDO);
//                        successNum++;
//                        successMessages.add("数据插入成功，ID为 " + extDatasourceId + " 的数据源记录。");
//                    } else {
//                        failureNum++;
//                        failureMessages.add("数据插入失败，ID为 " + extDatasourceId + " 的数据源记录已存在。");
//                    }
//                }
//            } catch (Exception e) {
//                failureNum++;
//                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
//                failureMessages.add(errorMsg);
//                log.error(errorMsg, e);
//            }
//        }
//        StringBuilder resultMsg = new StringBuilder();
//        if (failureNum > 0) {
//            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
//            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
//            throw new ServiceException(resultMsg.toString());
//        } else {
//            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
//        }
//        return resultMsg.toString();
//    }
}
