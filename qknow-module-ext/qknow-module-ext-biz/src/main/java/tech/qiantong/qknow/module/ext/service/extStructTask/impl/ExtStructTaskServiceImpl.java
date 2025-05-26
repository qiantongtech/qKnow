package tech.qiantong.qknow.module.ext.service.extStructTask.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.extEnum.ExtReleaseStatus;
import tech.qiantong.qknow.module.ext.extEnum.ExtTaskStatus;
import tech.qiantong.qknow.module.app.enums.ReleaseStatus;
import tech.qiantong.qknow.module.dm.api.datasource.dto.DmDatasourceRespDTO;
import tech.qiantong.qknow.module.dm.api.service.asset.IDmDatasourceApiService;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extCustomMapping.ExtCustomMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDataSourceTable;
import tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.ExtExtractionMergeDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMapping.ExtRelationMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTask;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extAttributeMapping.ExtAttributeMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extCustomMapping.ExtCustomMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extRelationMapping.ExtRelationMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaAttribute.ExtSchemaAttributeMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaMapping.ExtSchemaMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extStructTask.ExtStructTaskMapper;
import tech.qiantong.qknow.module.ext.extEnum.ExtractType;
import tech.qiantong.qknow.module.ext.service.extDatasource.ExtDatasourceQueryService;
import tech.qiantong.qknow.module.ext.service.extStructTask.IExtStructTaskService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.enums.Neo4jLabelEnum;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jBuildWrapper;
import tech.qiantong.qknow.neo4j.repository.DynamicRepository;
import tech.qiantong.qknow.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 结构化抽取任务Service业务层处理
 *
 * @author qknow
 * @date 2025-02-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtStructTaskServiceImpl extends ServiceImpl<ExtStructTaskMapper, ExtStructTaskDO> implements IExtStructTaskService {
    @Resource
    private ExtStructTaskMapper extStructTaskMapper;
    @Resource
    private IDmDatasourceApiService daDatasourceApiService;
    @Resource
    private ExtSchemaMappingMapper extSchemaMappingMapper;
    @Resource
    private ExtAttributeMappingMapper extAttributeMappingMapper;
    @Resource
    private ExtRelationMappingMapper extRelationMappingMapper;
    @Resource
    private ExtCustomMappingMapper extCustomMappingMapper;
    @Resource
    private ExtDatasourceQueryService extDatasourceQueryService;
    @Resource
    private ExtSchemaAttributeMapper extSchemaAttributeMapper;
    @Resource
    private ExtNeo4jService extNeo4jService;
    @Resource
    private Neo4jClient neo4jClient;
    @Resource
    private DynamicRepository dynamicRepository;
    @Resource
    private IRedisService redisService;

    // 消费队列任务, 执行任务抽取
    public void consumeQueue() {
        while (true) {
            String taskId = redisService.leftPop("ext_struck");
            // 如果任务ID为空，休眠 1000 毫秒后再继续轮询
            if (taskId == null || taskId.isEmpty()) {
                try {
                    Thread.sleep(1000);  // 每次轮询之间休眠1000ms，减少CPU占用
                } catch (InterruptedException e) {
                    log.error("Sleep interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            ExtStructTaskDO extStructTaskDO = null;
            Long id = Long.valueOf(taskId);
            try {
                extStructTaskDO = getExtStructTaskById(id);
            } catch (Exception e) {
                redisService.leftPush("ext_struck", taskId);
                log.info("连接数据库异常:{}", e.getMessage());
                continue;
            }

            CompletableFuture<Void> future = null;
            log.info("----------------结构化抽取任务开始-----------------------taskId:{}", taskId);
            try {
                //异步执行抽取任务
                ExtStructTaskDO finalExtStructTaskDO = extStructTaskDO;
                future = this.getCompletableFuture(id)
                        .thenRun(() -> {
                            // 处理任务完成后的状态更新
                            finalExtStructTaskDO.setStatus(2);
                            extStructTaskMapper.updateById(finalExtStructTaskDO);
                        })
                        .exceptionally(exception -> {
                            // 处理异常情况
                            log.error("抽取线程执行异常: {}", exception.getMessage());
                            finalExtStructTaskDO.setStatus(3);
                            extStructTaskMapper.updateById(finalExtStructTaskDO);
                            return null;  // 这里返回 null 表示异常已处理
                        });
                // 等待当前任务完成再进行下一个任务
                future.join(); // 这里调用 join() 确保当前任务完成后才进行下一次循环
                log.info("----------------结构化抽取任务结束-----------------------taskId:{}", taskId);
            } catch (Exception e) {
                extStructTaskDO.setStatus(3);
                extStructTaskMapper.updateById(extStructTaskDO);
                // 捕获异常并打印日志
                log.error("非结构化任务抽取处理失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 执行抽取
     *
     * @param extStructTask
     * @return
     */
    public AjaxResult executeExtraction(ExtStructTaskPageReqVO extStructTask) {
        ExtStructTaskDO extStructTaskDO = extStructTaskMapper.selectById(extStructTask.getId());
        if (ReleaseStatus.PUBLISHED.getValue().equals(extStructTaskDO.getPublishStatus())) {
            throw new RuntimeException("已发布状态不能重新执行抽取");
        }
//        if (ExtTaskStatus.INPROGRESS.getValue().equals(extStructTaskDO.getStatus())) {
//            throw new RuntimeException("执行中不能重新执行抽取");
//        }
        extStructTaskDO.setStatus(ExtTaskStatus.INPROGRESS.getValue());
        extStructTaskMapper.updateById(extStructTaskDO);

        //放入redis队列中, 按顺序 一个一个执行
        redisService.leftPush("ext_struck", String.valueOf(extStructTask.getId()));
        return AjaxResult.success("操作成功,执行中");

//        CompletableFuture<AjaxResult> future = null;
//        try {
//            //异步执行抽取任务
//            future = this.getCompletableFuture(extStructTask.getId());
//        } finally {
//            // 获取抽取任务执行的结果
//            future.whenComplete((result, exception) -> {
//                if (exception != null) {
//                    log.info("结构化抽取--抽取线程执行异常: {}", exception.getMessage());
//                } else {
//                    AjaxResult ajaxResult = result;
//                    if (ajaxResult.isSuccess()) {
//                        extStructTaskDO.setStatus(2);
//                        extStructTaskMapper.updateById(extStructTaskDO);
//                        return;
//                    }
//                    log.info("结构化抽取--抽取线程执行成功: {}", result);  // 处理结果
//                }
//                extStructTaskDO.setStatus(3);
//                extStructTaskMapper.updateById(extStructTaskDO);
//            });
//        }
//        return AjaxResult.success("操作成功,执行中");
    }

    public AjaxResult getAttributeInformation(List<String> list) {
        List<ExtSchemaAttributeDO> attributeDOS = extSchemaAttributeMapper.selectList("id", list);
        log.info("------{}", attributeDOS);
        return AjaxResult.success("获取成功", attributeDOS);
    }

    /**
     * 获取抽取结果
     *
     * @param taskId
     * @param extractType
     * @return
     */
    public AjaxResult selectByTaskId(Long taskId, Integer extractType) {
        ExtStructTaskDO taskDO = extStructTaskMapper.selectById(taskId);
        AjaxResult ajaxResult = extNeo4jService.selectByTaskId(taskId, ExtractType.STRUCTURED.getValue());
        if (ajaxResult.isSuccess()) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) ajaxResult.get("data");
            hashMap.put("releaseStatus", taskDO.getPublishStatus());
            return AjaxResult.success("", hashMap);
        }
        return ajaxResult;
    }

    /**
     * 发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult releaseByTaskId(ExtStructTaskDO structTaskDO) {
        extStructTaskMapper.updateById(structTaskDO);
        extNeo4jService.updateByTaskIdAndExtractType(structTaskDO.getId(), ExtractType.STRUCTURED.getValue(), ReleaseStatus.PUBLISHED.getValue());
        return AjaxResult.success("发布成功");
    }

    /**
     * 取消发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult cancelReleaseByTaskId(ExtStructTaskDO structTaskDO) {
        extStructTaskMapper.updateById(structTaskDO);
        extNeo4jService.updateByTaskIdAndExtractType(structTaskDO.getId(), ExtractType.STRUCTURED.getValue(), ReleaseStatus.UNPUBLISHED.getValue());
        return AjaxResult.success("取消发布成功");
    }

    /**
     * 结构化抽取 异步抽取
     *
     * @param extStructTask
     * @return
     */
    private CompletableFuture<AjaxResult> getCompletableFuture(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ExtStructTaskDO extStructTaskDO = extStructTaskMapper.selectById(id);
                //删除neo4j中之前抽取相关的数据, 如果有的话
                ExtExtractionDO extractionDO = new ExtExtractionDO();
                extractionDO.setTaskId(extStructTaskDO.getId());
                extNeo4jService.deleteExtStruck(extractionDO);
                //数据源信息
                DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(extStructTaskDO.getDatasourceId());
                JSONObject jsonObject = JSONObject.parseObject(datasource.getDatasourceConfig());
                //根据任务id获取概念
                ExtSchemaMappingPageReqVO mappingPageReqVO = new ExtSchemaMappingPageReqVO();
                mappingPageReqVO.setTaskId(extStructTaskDO.getId());
                PageResult<ExtSchemaMappingDO> schemaMappingDOPageResult = extSchemaMappingMapper.selectPage(mappingPageReqVO);
                List<ExtSchemaMappingDO> schemaMappingDOS = BeanUtils.toBean(schemaMappingDOPageResult.getRows(), ExtSchemaMappingDO.class);
                //根据每个映射的概念抽取对应内容
                for (ExtSchemaMappingDO schemaMappingDO : schemaMappingDOS) {
                    //先获取表
                    String tableName = schemaMappingDO.getTableName();

                    //查询此表所有数据
                    ExtDataSourceTable.GetTableData getTableData = new ExtDataSourceTable.GetTableData();
                    getTableData.setDbType(datasource.getDatasourceType());
                    getTableData.setUrl("jdbc:mysql://" + datasource.getIp() + ": "
                            + datasource.getPort() + "/" + jsonObject.getString("dbname"));//数据库名称
                    getTableData.setUsername(jsonObject.getString("username"));
                    getTableData.setPassword(jsonObject.getString("password"));

                    ExtDataSourceTable.GetTableData getTableData1 = BeanUtils.toBean(getTableData, ExtDataSourceTable.GetTableData.class);
                    getTableData1.setQuery("SELECT * FROM " + tableName);
                    List<ConcurrentHashMap<String, Object>> mapList1 = extDatasourceQueryService.getTableData(getTableData1);

                    //查询所有相关属性映射
                    ExtAttributeMappingPageReqVO attributeMappingPageReqVO = new ExtAttributeMappingPageReqVO();
                    attributeMappingPageReqVO.setTaskId(extStructTaskDO.getId());
                    attributeMappingPageReqVO.setTableName(tableName);
                    PageResult<ExtAttributeMappingDO> attributeMappingDOPageResult = extAttributeMappingMapper.selectPage(attributeMappingPageReqVO);
                    List<ExtAttributeMappingDO> attributeMappingDOS = BeanUtils.toBean(attributeMappingDOPageResult.getRows(), ExtAttributeMappingDO.class);

                    ExtCustomMappingPageReqVO customMappingPageReqVO = new ExtCustomMappingPageReqVO();
                    customMappingPageReqVO.setTaskId(extStructTaskDO.getId());
                    customMappingPageReqVO.setTableName(tableName);
                    PageResult<ExtCustomMappingDO> customMappingDOPageResult = extCustomMappingMapper.selectPage(customMappingPageReqVO);
                    List<ExtCustomMappingDO> customMappingDOS = BeanUtils.toBean(customMappingDOPageResult.getRows(), ExtCustomMappingDO.class);

                    //自定义映射
                    HashMap<String, List<ConcurrentHashMap<String, Object>>> listHashMap = new HashMap<>();
                    for (ExtCustomMappingDO customMappingDO : customMappingDOS) {
                        ExtDataSourceTable.GetTableData getTableData0 = BeanUtils.toBean(getTableData, ExtDataSourceTable.GetTableData.class);
                        // 内连接连表, 查询自定义属性映射
//                    String query0 = "SELECT a.*,b.* FROM " + tableName + " a INNER JOIN " + relationTable + " b ON a." + fieldName + " = b." + relationField + ";";
                        String query0 = customMappingDO.getSqlStatement();
//                        String query0 = "SELECT a.id,b.name FROM test_aa a INNER JOIN test_bb b ON a.id = b.id;";
                        log.info("查询sql:{}", query0);
                        getTableData0.setQuery(query0);
                        getTableData0.setAfieldNum(1);
                        try {
                            List<ConcurrentHashMap<String, Object>> mapList0 = extDatasourceQueryService.getTableData2(getTableData0);
                            listHashMap.put(customMappingDO.getFieldName(), mapList0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //将属性和表字段对应上, 存成一个数组
                    ArrayList<ConcurrentHashMap<String, Object>> maps = new ArrayList<>();
                    for (ConcurrentHashMap<String, Object> objectMap : mapList1) {
                        //创建属性map  以属性id为key,字段值为value
                        ConcurrentHashMap<String, Object> nodes = new ConcurrentHashMap<>();
                        nodes.put("task_id", extStructTaskDO.getId());
                        nodes.put("database_id", datasource.getId());
                        nodes.put("table_name", tableName);
                        nodes.put("schema_id", schemaMappingDO.getSchemaId());
                        nodes.put("schema_nmae", schemaMappingDO.getSchemaName());
                        nodes.put("entity_type", ExtractType.STRUCTURED.getValue());//1: 结构化  2:非结构化
                        nodes.put("release_status", ExtReleaseStatus.UNPUBLISHED.getStatus());//0未发布 1已发布
                        nodes.put("workspace_id", extStructTaskDO.getWorkspaceId());
                        for (ConcurrentHashMap.Entry<String, Object> entry : objectMap.entrySet()) {
                            String columnName = entry.getKey();   // 获取字段名
                            Object columnValue = entry.getValue(); // 获取字段值
                            //如果这个字段是实体名称, 这个字段在前端选择
                            if (columnName.equals(schemaMappingDO.getEntityNameField())) {
                                nodes.put("name", columnValue);
                            }
                            //每个表必须要有id字段
                            if ("id".equals(columnName)) {
                                nodes.put("data_id", columnValue);
                            }
                            for (ExtAttributeMappingDO attributeMappingDO : attributeMappingDOS) {
                                //如果这个字段映射过属性, 就把属性放到节点map中
                                if (attributeMappingDO.getAttributeId() != null && columnName.equals(attributeMappingDO.getFieldName())) {
                                    //TODO 如果这个字段有自定义过属性
                                    for (Map.Entry<String, List<ConcurrentHashMap<String, Object>>> stringListEntry : listHashMap.entrySet()) {
                                        String key = stringListEntry.getKey();
                                        if (key.equals(columnName)) {
                                            List<ConcurrentHashMap<String, Object>> value = stringListEntry.getValue();
                                            for (ConcurrentHashMap<String, Object> concurrentHashMap : value) {
                                                for (Map.Entry<String, Object> objectEntry : concurrentHashMap.entrySet()) {
                                                    String key1 = objectEntry.getKey();
                                                    if (key1.contains("b_")) {
                                                        columnValue = objectEntry.getValue();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //把所有添加属性映射的区分出来, 方便展示
                                    nodes.put("attribute_id_" + attributeMappingDO.getAttributeId().toString(), columnValue);
                                }
                            }
                        }
                        maps.add(nodes);
                    }
                    log.info("-----节点maps---:{}", maps);

                    //存储所有抽取出来的节点
                    for (ConcurrentHashMap<String, Object> map : maps) {
                        // 创建头部节点并动态添加属性
                        ConcurrentHashMap<String, Object> startMap = map;
                        Neo4jBuildWrapper<DynamicEntity> wrapper = new Neo4jBuildWrapper<>(DynamicEntity.class);
                        ExtExtractionMergeDO.Node extractionMergeDO = new ExtExtractionMergeDO.Node();
                        extractionMergeDO.setName(startMap.get("name").toString());
                        extractionMergeDO.setTask_id(extStructTaskDO.getId());
                        extractionMergeDO.setTable_name(tableName);
                        extractionMergeDO.setData_id(Long.valueOf(startMap.get("data_id").toString()));
                        extractionMergeDO.setEntity_type(ExtractType.STRUCTURED.getType());
                        extractionMergeDO.setDatabase_id(Long.valueOf(datasource.getId().toString()));
                        ConcurrentHashMap<String, Object> mergeMap = new ObjectMapper().readValue(JSONObject.toJSONString(extractionMergeDO), ConcurrentHashMap.class);
                        String label = Neo4jLabelEnum.DYNAMICENTITY.getLabel() + ":" + Neo4jLabelEnum.STRUCTURED.getLabel();
                        dynamicRepository.mergeCreateNode(label, wrapper, mergeMap, startMap);
                    }

                    //查询映射过的关系
                    ExtRelationMappingPageReqVO relationMappingPageReqVO = new ExtRelationMappingPageReqVO();
                    relationMappingPageReqVO.setTaskId(extStructTaskDO.getId());
                    relationMappingPageReqVO.setTableName(tableName);
                    PageResult<ExtRelationMappingDO> mappingDOPageResult = extRelationMappingMapper.selectPage(relationMappingPageReqVO);
                    List<ExtRelationMappingDO> relationMappingDOS = BeanUtils.toBean(mappingDOPageResult.getRows(), ExtRelationMappingDO.class);
                    //循环映射的关系
                    for (ExtRelationMappingDO relationMappingDO : relationMappingDOS) {
                        String fieldName = relationMappingDO.getFieldName();//字段名
                        String relationTable = relationMappingDO.getRelationTable();//关联表
                        String relationField = relationMappingDO.getRelationField();//关联表字段

                        ExtDataSourceTable.GetTableData getTableData2 = BeanUtils.toBean(getTableData, ExtDataSourceTable.GetTableData.class);
                        // 内连接连表, 查询关系映射
                        String query2 = "SELECT a.*,b.* FROM " + tableName + " a INNER JOIN " + relationTable + " b ON a." + fieldName + " = b." + relationField + ";";
                        log.info("查询sql:{}", query2);
                        getTableData2.setQuery(query2);
                        getTableData2.setTableA(tableName);
                        getTableData2.setTableB(relationTable);
                        List<ConcurrentHashMap<String, Object>> mapList2 = extDatasourceQueryService.getTableData2(getTableData2);
                        log.info("----关系-------:{}", mapList2);

                        //存储节点和关系, 没有关系的节点也存储到neo4j
                        for (ConcurrentHashMap<String, Object> map : maps) {
                            // 创建头部节点并动态添加属性
                            ConcurrentHashMap<String, Object> startMap = map;

                            List<ConcurrentHashMap<String, Object>> filteredList = mapList2.stream()
                                    .filter(m -> m.containsKey("a_id") && m.get("a_id").equals(map.get("data_id")))
                                    .collect(Collectors.toList());
                            for (ConcurrentHashMap<String, Object> relMap : filteredList) {
                                boolean status = false;

                                //实体的属性 循环匹配是否有对应的关系 //尾部实体
                                ConcurrentHashMap<String, Object> endMap = new ConcurrentHashMap<>();

                                Iterator<ConcurrentHashMap.Entry<String, Object>> iterator = relMap.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    ConcurrentHashMap.Entry<String, Object> relEntry = iterator.next();
                                    // 修改或者删除元素
                                    if (relEntry.getKey().startsWith("a_")) {
                                        iterator.remove();  // 使用 iterator 的 remove() 方法
                                    }
                                    //如果是实体id字段, 匹配节点map中的值, 判断是否是同一个, 如果是同一个的话, 判断是否映射的有关系
                                    //根据节点map中的节点data_id和关系映射中的a_id匹配
                                    if ("a_id".equals(relEntry.getKey())) {
                                        //如果是同一个实体 并且b_表的数据不为空, 说明有对应关系存在, 需要存储关系
                                        if (map.get("data_id").equals(relEntry.getValue())) {
                                            relMap.keySet().removeIf(key -> key.startsWith("a_"));
                                            // 修改以 "b_" 开头的键，去掉 "b_" 前缀
                                            Set<String> keysToModify = new HashSet<>();
                                            for (String key : relMap.keySet()) {
                                                if (key.startsWith("b_")) {
                                                    keysToModify.add(key);
                                                }
                                            }
                                            // 更新键，去掉 "b_" 前缀
                                            for (String key : keysToModify) {
                                                Object value = relMap.remove(key);
                                                relMap.put(key.substring(2), value);  // 去掉 "b_" 前缀
                                            }
                                            endMap = relMap;
                                            status = true;
                                        }
                                    }
                                }

                                //如果有映射的关系, 添加尾部节点和关系
                                if (status) {
                                    // 创建尾部节点并动态添加属性
                                    endMap.put("task_id", extStructTaskDO.getId());
                                    endMap.put("table_name", relationTable);
                                    endMap.put("data_id", endMap.get("id"));
                                    endMap.put("database_id", datasource.getId());
                                    endMap.put("entity_type", ExtractType.STRUCTURED.getValue());//1: 结构化  2:非结构化
                                    endMap.put("release_status", ReleaseStatus.UNPUBLISHED.getValue());//0未发布 1已发布
                                    endMap.remove("id");
                                    Neo4jBuildWrapper<DynamicEntity> build = new Neo4jBuildWrapper<>(DynamicEntity.class);
                                    ExtExtractionMergeDO.Node extractionMergeDO = new ExtExtractionMergeDO.Node();
                                    extractionMergeDO.setName(endMap.get("name").toString());
                                    extractionMergeDO.setTask_id(Long.valueOf(extStructTaskDO.getId().toString()));
                                    extractionMergeDO.setTable_name(relationTable);
                                    extractionMergeDO.setData_id(Long.valueOf(endMap.get("data_id").toString()));
                                    extractionMergeDO.setEntity_type(ExtractType.STRUCTURED.getValue());
                                    extractionMergeDO.setDatabase_id(Long.valueOf(datasource.getId().toString()));
                                    ConcurrentHashMap<String, Object> endMergeMap = new ObjectMapper().readValue(JSONObject.toJSONString(extractionMergeDO), ConcurrentHashMap.class);
                                    String label = Neo4jLabelEnum.DYNAMICENTITY.getLabel() + ":" + Neo4jLabelEnum.STRUCTURED.getLabel();//TODO 用枚举
                                    dynamicRepository.mergeCreateNode(label, build, endMergeMap, endMap);

                                    //创建关系
                                    //起点
                                    ExtExtractionMergeDO.CreateRelationshipNode startNode = new ExtExtractionMergeDO.CreateRelationshipNode();
                                    startNode.setData_id(Long.valueOf(startMap.get("data_id").toString()));
                                    startNode.setTask_id(Long.valueOf(extStructTaskDO.getId().toString()));
                                    startNode.setTable_name(tableName);
                                    ConcurrentHashMap<String, Object> startNodeMap = new ObjectMapper().readValue(JSONObject.toJSONString(startNode), ConcurrentHashMap.class);
                                    //结点
                                    ExtExtractionMergeDO.CreateRelationshipNode endNode = new ExtExtractionMergeDO.CreateRelationshipNode();
                                    endNode.setData_id(Long.valueOf(endMap.get("data_id").toString()));
                                    endNode.setTask_id(Long.valueOf(extStructTaskDO.getId().toString()));
                                    endNode.setTable_name(relationTable);
                                    ConcurrentHashMap<String, Object> endNodeMap = new ObjectMapper().readValue(JSONObject.toJSONString(endNode), ConcurrentHashMap.class);
                                    String rel = relationMappingDO.getRelation();
                                    //关系
                                    ConcurrentHashMap<String, Object> relMa = new ConcurrentHashMap<>();
                                    relMa.put("task_id", extStructTaskDO.getId());
                                    relMa.put("entity_type", ExtractType.STRUCTURED.getValue());
                                    dynamicRepository.mergeRelationship(Neo4jLabelEnum.STRUCTURED.getLabel(), build, startNodeMap, endNodeMap, rel, relMa);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("执行异常");
            }
            return AjaxResult.success("执行成功");
        });
    }

    /**
     * 修改结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    @Transactional
    public AjaxResult updateDataMapping(ExtStructTask extStructTask) {
        Long taskId = extStructTask.getTaskId();
        //修改任务数据
        DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(extStructTask.getDataSourceId());
        ExtStructTaskDO extStructTaskDO = extStructTaskMapper.selectById(taskId);
//        extStructTaskDO.setStatus(ExtTaskStatus.UNEXECUTED.getValue());
        extStructTaskDO.setRemark(extStructTask.getRemark());
        extStructTaskDO.setName(extStructTask.getTaskName());
        extStructTaskDO.setDatasourceId(extStructTask.getDataSourceId());
        extStructTaskDO.setDatasourceName(datasource.getDatasourceName());
        extStructTaskDO.setUpdateBy(extStructTask.getUpdateBy());
        extStructTaskDO.setUpdaterId(extStructTask.getUpdaterId());
        extStructTaskDO.setUpdateTime(new Date());
        extStructTaskMapper.updateById(extStructTaskDO);
        //删除之前添加的映射关系
        extSchemaMappingMapper.delete("task_id", taskId.toString());//概念
        extAttributeMappingMapper.delete("task_id", taskId.toString());//属性
        extRelationMappingMapper.delete("task_id", taskId.toString());//关系
        extCustomMappingMapper.delete("task_id", taskId.toString());//自定义
        return addMapping(extStructTask, extStructTaskDO);
    }

    /**
     * 添加结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    @Transactional
    public AjaxResult addDataMapping(ExtStructTask extStructTask) {
        DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(extStructTask.getDataSourceId());
        // 存储结构化抽取任务
        ExtStructTaskDO extStructTaskDO = new ExtStructTaskDO();
        extStructTaskDO.setWorkspaceId(extStructTask.getWorkspaceId());
        extStructTaskDO.setName(extStructTask.getTaskName());
        extStructTaskDO.setRemark(extStructTask.getRemark());
        extStructTaskDO.setDatasourceId(extStructTask.getDataSourceId());
        extStructTaskDO.setDatasourceName(datasource.getDatasourceName());
        extStructTaskDO.setStatus(ExtTaskStatus.UNEXECUTED.getValue());
        extStructTaskDO.setPublishStatus(ExtReleaseStatus.UNPUBLISHED.getStatus());
        extStructTaskDO.setValidFlag(false);
        extStructTaskDO.setDelFlag(false);
        extStructTaskDO.setCreateBy(extStructTask.getUpdateBy());
        extStructTaskDO.setUpdateBy(extStructTask.getUpdateBy());
        extStructTaskDO.setCreatorId(extStructTask.getUpdaterId());
        extStructTaskDO.setUpdaterId(extStructTask.getUpdaterId());
//        extStructTaskDO.setPublisherId(extStructTask.getUpdaterId());
//        extStructTaskDO.setPublishTime(new Date());
        extStructTaskDO.setCreateTime(new Date());
        extStructTaskDO.setUpdateTime(new Date());
        extStructTaskMapper.insert(extStructTaskDO);
        return addMapping(extStructTask, extStructTaskDO);
    }

    /**
     * 添加映射关系
     *
     * @param extStructTask
     * @param extStructTaskDO
     * @return
     */
    private AjaxResult addMapping(ExtStructTask extStructTask, ExtStructTaskDO extStructTaskDO) {
        try {
            List<ExtStructTask.TableData> tableData = extStructTask.getTableData();
            tableData.forEach(ext -> {
                //如果未映射, 不处理
                if (!"1".equals(ext.getStatus())) {
                    return;
                }
                ExtStructTask.MappingData mappingData = ext.getMappingData();

                //概念映射
                ExtSchemaMappingDO schemaMappingDO = new ExtSchemaMappingDO();
                schemaMappingDO.setWorkspaceId(extStructTask.getWorkspaceId());
                schemaMappingDO.setTaskId(extStructTaskDO.getId());
                schemaMappingDO.setTableName(ext.getTableName());
                schemaMappingDO.setTableComment(ext.getTableComment());
                schemaMappingDO.setEntityNameField(mappingData.getEntityNameField());
                schemaMappingDO.setSchemaId(mappingData.getConcept());
                schemaMappingDO.setSchemaName(mappingData.getConceptName());
                schemaMappingDO.setValidFlag(false);
                schemaMappingDO.setDelFlag(false);
                schemaMappingDO.setCreateBy(extStructTask.getUpdateBy());
                schemaMappingDO.setUpdateBy(extStructTask.getUpdateBy());
                schemaMappingDO.setCreatorId(extStructTask.getUpdaterId());
                schemaMappingDO.setUpdaterId(extStructTask.getUpdaterId());
                schemaMappingDO.setCreateTime(new Date());
                schemaMappingDO.setUpdateTime(new Date());
                extSchemaMappingMapper.insert(schemaMappingDO);

                //属性映射
                List<ExtStructTask.Attribute> attributeList = mappingData.getAttributeList();
                attributeList = attributeList.stream()
                        .filter(e -> e.getConceptId() != null)
                        .collect(Collectors.toList());
                ArrayList<ExtAttributeMappingDO> attributeMappingDOS = new ArrayList<>();
                attributeList.forEach(e -> {
                    ExtAttributeMappingDO extAttributeMappingDO = new ExtAttributeMappingDO();
                    extAttributeMappingDO.setWorkspaceId(extStructTask.getWorkspaceId());
                    extAttributeMappingDO.setTaskId(extStructTaskDO.getId());
                    extAttributeMappingDO.setTableName(ext.getTableName());
                    extAttributeMappingDO.setTableComment(ext.getTableComment());//TODO
                    extAttributeMappingDO.setFieldName(e.getField());
                    extAttributeMappingDO.setFieldComment(e.getFieldDescription());
                    extAttributeMappingDO.setAttributeId(e.getConceptId());
                    extAttributeMappingDO.setAttributeName(e.getConceptName());
                    extAttributeMappingDO.setAttributeName(e.getConceptName());
                    extAttributeMappingDO.setValidFlag(false);
                    extAttributeMappingDO.setDelFlag(false);
                    extAttributeMappingDO.setCreateBy(extStructTask.getUpdateBy());
                    extAttributeMappingDO.setUpdateBy(extStructTask.getUpdateBy());
                    extAttributeMappingDO.setCreatorId(extStructTask.getUpdaterId());
                    extAttributeMappingDO.setUpdaterId(extStructTask.getUpdaterId());
                    extAttributeMappingDO.setCreateTime(new Date());
                    extAttributeMappingDO.setUpdateTime(new Date());
                    attributeMappingDOS.add(extAttributeMappingDO);
                });
                if (attributeMappingDOS.size() > 0) {
                    extAttributeMappingMapper.insertBatch(attributeMappingDOS);
                }

                //关系映射
                List<ExtStructTask.Relationship> relationshipList = mappingData.getRelationshipList();
                ArrayList<ExtRelationMappingDO> relationMappingDOS = new ArrayList<>();
                relationshipList = relationshipList.stream()
                        .filter(e -> StringUtils.isNotBlank(e.getField())
                                && StringUtils.isNotBlank(e.getRelation())
                                && StringUtils.isNotBlank(e.getAssociationTable())
                                && StringUtils.isNotBlank(e.getAssociationTableField())
                                && StringUtils.isNotBlank(e.getAssociationTableEntityField())
                        ).collect(Collectors.toList());
                relationshipList.forEach(e -> {
                    if (StringUtils.isBlank(e.getField()) || StringUtils.isBlank(e.getRelation())
                            || StringUtils.isBlank(e.getAssociationTable()) || StringUtils.isBlank(e.getAssociationTableField())) {
                        return;
                    }
                    ExtRelationMappingDO relationMappingDO = new ExtRelationMappingDO();
                    relationMappingDO.setWorkspaceId(extStructTask.getWorkspaceId());
                    relationMappingDO.setTaskId(extStructTaskDO.getId());
                    relationMappingDO.setTableName(ext.getTableName());
                    relationMappingDO.setTableComment(ext.getTableComment());
                    //TODO fieldComment 这个值暂时没有
                    relationMappingDO.setFieldComment("");
                    relationMappingDO.setFieldName(e.getField());
                    relationMappingDO.setRelation(e.getRelation());
                    relationMappingDO.setRelationTable(e.getAssociationTable());
                    for (ExtStructTask.TableData tableDatum : tableData) {
                        if (tableDatum.getTableName().equals(e.getAssociationTable())) {
                            relationMappingDO.setRelationTableName(tableDatum.getTableComment());
                        }
                    }
                    relationMappingDO.setRelationField(e.getAssociationTableField());
                    relationMappingDO.setRelationNameField(e.getAssociationTableEntityField());
                    relationMappingDO.setValidFlag(false);
                    relationMappingDO.setDelFlag(false);
                    relationMappingDO.setCreateBy(extStructTask.getUpdateBy());
                    relationMappingDO.setUpdateBy(extStructTask.getUpdateBy());
                    relationMappingDO.setCreatorId(extStructTask.getUpdaterId());
                    relationMappingDO.setUpdaterId(extStructTask.getUpdaterId());
                    relationMappingDO.setCreateTime(new Date());
                    relationMappingDO.setUpdateTime(new Date());
                    relationMappingDOS.add(relationMappingDO);
                });
                if (relationMappingDOS.size() > 0) {
                    extRelationMappingMapper.insertBatch(relationMappingDOS);
                }

                ArrayList<ExtCustomMappingDO> customMappingDOS = new ArrayList<>();
                //自定义映射
                List<ExtStructTask.Custom> customList = mappingData.getCustomList();
                customList = customList.stream()
                        .filter(e -> StringUtils.isNotBlank(e.getField())
                                && StringUtils.isNotBlank(e.getCustomSQL())
                        ).collect(Collectors.toList());
                customList.forEach(e -> {
                    ExtCustomMappingDO customMappingDO = new ExtCustomMappingDO();
                    customMappingDO.setWorkspaceId(extStructTask.getWorkspaceId());
                    customMappingDO.setTaskId(extStructTaskDO.getId());
                    customMappingDO.setTableName(ext.getTableName());
                    customMappingDO.setTableComment(ext.getTableComment());
                    //TODO fieldComment 这个值暂时没有
                    customMappingDO.setFieldComment("");
                    customMappingDO.setFieldName(e.getField());
                    customMappingDO.setSqlStatement(e.getCustomSQL());
                    customMappingDO.setValidFlag(false);
                    customMappingDO.setDelFlag(false);
                    customMappingDO.setCreateBy(extStructTask.getUpdateBy());
                    customMappingDO.setUpdateBy(extStructTask.getUpdateBy());
                    customMappingDO.setCreatorId(extStructTask.getUpdaterId());
                    customMappingDO.setUpdaterId(extStructTask.getUpdaterId());
                    customMappingDO.setCreateTime(new Date());
                    customMappingDO.setUpdateTime(new Date());
                    customMappingDOS.add(customMappingDO);
                });
                if (customMappingDOS.size() > 0) {
                    extCustomMappingMapper.insertBatch(customMappingDOS);
                }
            });
            return AjaxResult.success();
        } catch (Exception e) {
            log.info("添加结构化抽取任务异常:{}", e);
            return AjaxResult.error("添加失败: " + e.getMessage());
        }
    }

    @Override
    public PageResult<ExtStructTaskDO> getExtStructTaskPage(ExtStructTaskPageReqVO pageReqVO) {
        return extStructTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtStructTask(ExtStructTaskSaveReqVO createReqVO) {
        ExtStructTaskDO dictType = BeanUtils.toBean(createReqVO, ExtStructTaskDO.class);
        extStructTaskMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtStructTask(ExtStructTaskSaveReqVO updateReqVO) {
        // 相关校验

        // 更新结构化抽取任务
        ExtStructTaskDO updateObj = BeanUtils.toBean(updateReqVO, ExtStructTaskDO.class);
        return extStructTaskMapper.updateById(updateObj);
    }

    @Override
    public int removeExtStructTask(Collection<Long> idList) {
        // 批量删除结构化抽取任务
        return extStructTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtStructTaskDO getExtStructTaskById(Long id) {
        return extStructTaskMapper.selectById(id);
    }

    @Override
    public AjaxResult getInfo(Long id) {
        ExtStructTaskDO structTaskDO = extStructTaskMapper.selectById(id);
        ExtStructTaskRespVO structTaskRespVO = BeanUtils.toBean(structTaskDO, ExtStructTaskRespVO.class);

        //获取关联的概念
        ExtSchemaMappingPageReqVO mappingPageReqVO = new ExtSchemaMappingPageReqVO();
        mappingPageReqVO.setTaskId(id);
        PageResult<ExtSchemaMappingDO> schemaMappingDOPageResult = extSchemaMappingMapper.selectPage(mappingPageReqVO);
        List<ExtSchemaMappingRespVO> schemaMappingList = BeanUtils.toBean(schemaMappingDOPageResult.getRows(), ExtSchemaMappingRespVO.class);

        //获取关联的属性映射
        ExtAttributeMappingPageReqVO attributeMappingPageReqVO = new ExtAttributeMappingPageReqVO();
        attributeMappingPageReqVO.setTaskId(id);
        PageResult<ExtAttributeMappingDO> attributeMappingDOPageResult = extAttributeMappingMapper.selectPage(attributeMappingPageReqVO);
        List<ExtAttributeMappingRespVO> attributeMappingList = BeanUtils.toBean(attributeMappingDOPageResult.getRows(), ExtAttributeMappingRespVO.class);

        //获取关联的关系映射
        ExtRelationMappingPageReqVO relationMappingPageReqVO = new ExtRelationMappingPageReqVO();
        relationMappingPageReqVO.setTaskId(id);
        PageResult<ExtRelationMappingDO> relationMappingDOPageResult = extRelationMappingMapper.selectPage(relationMappingPageReqVO);
        List<ExtRelationMappingRespVO> relationMappingList = BeanUtils.toBean(relationMappingDOPageResult.getRows(), ExtRelationMappingRespVO.class);

        //获取关联的自定义映射
        ExtCustomMappingPageReqVO customMappingPageReqVO = new ExtCustomMappingPageReqVO();
        customMappingPageReqVO.setTaskId(id);
        PageResult<ExtCustomMappingDO> customMappingDOPageResult = extCustomMappingMapper.selectPage(customMappingPageReqVO);
        List<ExtCustomMappingRespVO> customMappingList = BeanUtils.toBean(customMappingDOPageResult.getRows(), ExtCustomMappingRespVO.class);

        //获取导入的表
        List<String> stringList = Stream.concat(
                schemaMappingList.stream().map(ExtSchemaMappingRespVO::getTableName).filter(Objects::nonNull),
                relationMappingList.stream().map(ExtRelationMappingRespVO::getRelationTable).filter(Objects::nonNull)
        )
                .distinct()  // 去重
                .collect(Collectors.toList());  // 收集成 List


        //判断这些表是否有对应概念
        ArrayList<Object> tableMappingList = new ArrayList<>();
        for (String tableName : stringList) {
            boolean status = false;
            Long schemaId = null;
            String schemaName = null;
            String tableComment = null;
            for (ExtSchemaMappingRespVO respVO : schemaMappingList) {
                if (tableName.equals(respVO.getTableName())) {
                    status = true;
                    schemaId = respVO.getSchemaId();
                    schemaName = respVO.getSchemaName();
                    break;
                }
            }
            for (ExtRelationMappingRespVO relationMappingRespVO : relationMappingList) {
                if (tableName.equals(relationMappingRespVO.getTableName())) {
                    tableComment = relationMappingRespVO.getTableComment();
                    break;
                } else if (tableName.equals(relationMappingRespVO.getRelationTable())) {
                    tableComment = relationMappingRespVO.getRelationTableName();
                    break;
                }
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("tableName", tableName);
            hashMap.put("tableComment", tableComment);
            hashMap.put("status", status);
            hashMap.put("schemaId", schemaId);
            hashMap.put("schemaName", schemaName);
            tableMappingList.add(hashMap);
        }


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("structTask", structTaskRespVO);
        hashMap.put("tableMappingList", tableMappingList);//表
        hashMap.put("schemaMappingList", schemaMappingList);//概念
        hashMap.put("attributeMappingList", attributeMappingList);//属性
        hashMap.put("relationMappingList", relationMappingList);//关系
        hashMap.put("customMappingList", customMappingList);//自定义
        return AjaxResult.success("", hashMap);
    }

    @Override
    public List<ExtStructTaskDO> getExtStructTaskList() {
        return extStructTaskMapper.selectList();
    }

    @Override
    public ConcurrentHashMap<Long, ExtStructTaskDO> getExtStructTaskMap() {
        List<ExtStructTaskDO> extStructTaskList = extStructTaskMapper.selectList();
        return extStructTaskList.stream()
                .collect(Collectors.toMap(
                        ExtStructTaskDO::getId,
                        extStructTaskDO -> extStructTaskDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing,
                        ConcurrentHashMap::new  // 指定返回的 Map 实现是 ConcurrentHashMap
                ));
    }


    /**
     * 导入结构化抽取任务数据
     *
     * @param importExcelList 结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importExtStructTask(List<ExtStructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (ExtStructTaskRespVO respVO : importExcelList) {
            try {
                ExtStructTaskDO extStructTaskDO = BeanUtils.toBean(respVO, ExtStructTaskDO.class);
                Long extStructTaskId = respVO.getId();
                if (isUpdateSupport) {
                    if (extStructTaskId != null) {
                        ExtStructTaskDO existingExtStructTask = extStructTaskMapper.selectById(extStructTaskId);
                        if (existingExtStructTask != null) {
                            extStructTaskMapper.updateById(extStructTaskDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + extStructTaskId + " 的结构化抽取任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + extStructTaskId + " 的结构化抽取任务记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<ExtStructTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", extStructTaskId);
                    ExtStructTaskDO existingExtStructTask = extStructTaskMapper.selectOne(queryWrapper);
                    if (existingExtStructTask == null) {
                        extStructTaskMapper.insert(extStructTaskDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + extStructTaskId + " 的结构化抽取任务记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + extStructTaskId + " 的结构化抽取任务记录已存在。");
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
}
