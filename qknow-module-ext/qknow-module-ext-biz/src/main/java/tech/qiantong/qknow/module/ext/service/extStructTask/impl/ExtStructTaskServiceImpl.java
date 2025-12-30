package tech.qiantong.qknow.module.ext.service.extStructTask.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.constant.ScheduleConstants;
import tech.qiantong.qknow.common.core.domain.AjaxResult;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.database.DataSourceFactory;
import tech.qiantong.qknow.common.database.DbQuery;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.exception.job.TaskException;
import tech.qiantong.qknow.common.utils.PageUtil;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.app.enums.ReleaseStatus;
import tech.qiantong.qknow.module.dm.api.datasource.dto.DmDatasourceRespDTO;
import tech.qiantong.qknow.module.dm.api.service.asset.IDmDatasourceApiService;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMapping.ExtRelationMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTask;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extAttributeMapping.ExtAttributeMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extRelationMapping.ExtRelationMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extRelationMappingMiddle.ExtRelationMappingMiddleMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaAttribute.ExtSchemaAttributeMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaMapping.ExtSchemaMappingMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extStructTask.ExtStructTaskMapper;
import tech.qiantong.qknow.module.ext.extEnum.*;
import tech.qiantong.qknow.module.ext.service.extSchema.IExtSchemaService;
import tech.qiantong.qknow.module.ext.service.extSchemaRelation.IExtSchemaRelationService;
import tech.qiantong.qknow.module.ext.service.extStructTask.IExtStructTaskService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.domain.relationship.DynamicEntityRelationship;
import tech.qiantong.qknow.neo4j.enums.GraphLabelEnum;
import tech.qiantong.qknow.neo4j.enums.Neo4jLabelEnum;
import tech.qiantong.qknow.neo4j.repository.DynamicRepository;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jBuildWrapper;
import tech.qiantong.qknow.neo4j.wrapper.query.Neo4jQueryWrapper;
import tech.qiantong.qknow.quartz.domain.SysJob;
import tech.qiantong.qknow.quartz.util.CronUtils;
import tech.qiantong.qknow.quartz.util.ScheduleUtils;
import tech.qiantong.qknow.redis.service.IRedisService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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
    private ExtSchemaAttributeMapper extSchemaAttributeMapper;
    @Resource
    private ExtNeo4jService extNeo4jService;
    @Resource
    private IExtSchemaRelationService extSchemaRelationService;
    @Resource
    private IExtSchemaService extSchemaService;
    @Resource
    private IRedisService redisService;

    @Resource
    private Scheduler scheduler;
    @Resource
    private DataSourceFactory dataSourceFactory;
    @Resource
    private ExtRelationMappingMiddleMapper extRelationMappingMiddleMapper;

    @Resource
    private DynamicRepository dynamicRepository;


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Integer PAGE_SIZE = 50;




    /**
     * 执行抽取
     *
     * @param extStructTask
     * @return tech.qiantong.qknow.common.core.domain.AjaxResult
     * @author shaun
     * @date 2025/06/18 18:04
     */
    @Override
    public AjaxResult executeExtraction(ExtStructTaskPageReqVO extStructTask) {
        ExtStructTaskDO extStructTaskDO = extStructTaskMapper.selectById(extStructTask.getId());
        if (ReleaseStatus.PUBLISHED.getValue().equals(extStructTaskDO.getPublishStatus())) {
            throw new RuntimeException("已发布状态不能重新执行抽取");
        }
        extStructTaskDO.setStatus(ExtTaskStatus.INPROGRESS.getValue());
        extStructTaskMapper.updateById(extStructTaskDO);

        //放入redis队列中, 按顺序 一个一个执行
        redisService.leftPush("ext_struck", String.valueOf(extStructTask.getId()));
        return AjaxResult.success("操作成功,执行中");
    }

    /**
     * 定时任务，执行抽取
     *
     * @return void
     * @author shaun
     * @date 2025/06/18 18:07
     */
    public void consumeQueue() {
        // 取出队列中的任务id
        String taskId = redisService.leftPop("ext_struck");
        // 判断队列是否为空
        if (StringUtils.isEmpty(taskId)) {
            log.info("队列中没有等待抽取的任务");
            return;
        }

        // 开始抽取
        log.info("----------------结构化抽取任务开始-----------------------taskId:{}", taskId);

        ExtStructTaskDO extStructTaskDO = getExtStructTaskById(Long.valueOf(taskId));

        try {
            this.execStructTask(taskId);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常并打印日志
            log.error("结构化任务抽取处理失败: {}", e.getMessage());

            extStructTaskDO.setStatus(ExtTaskStatus.ERROR.getValue());
            extStructTaskMapper.updateById(extStructTaskDO);
        }
    }
    //全量更新
    private void execStructTask(String taskId) {
        execStructTaskInternal(taskId, false);
    }

    //增量更新
    private void execStructTaskIncrementUpdate(String taskId) {
        execStructTaskInternal(taskId, true);
    }

    /**
     * 统一的抽取任务执行方法
     */
    private void execStructTaskInternal(String taskId, boolean isIncrement) {
        // 1. 初始化
        ExtStructTaskDO task = extStructTaskMapper.selectById(Long.valueOf(taskId));
        if (task == null) throw new DataQueryException("抽取任务不存在");

        // 全量清理
        if (!isIncrement) {
            ExtExtractionDO extractionDO = new ExtExtractionDO();
            extractionDO.setTaskId(task.getId());
            extNeo4jService.deleteExtStruck(extractionDO);
        }

        // 加载现有实体（用于增量更新）
        Map<String, DynamicEntity> existingMap = isIncrement ? loadExistingEntities(task) : new HashMap<>();

        // 2. 数据库连接
        DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(task.getDatasourceId());
        if (datasource == null) throw new DataQueryException("未查到数据源信息");

        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                datasource.getDatasourceType(), datasource.getIp(),
                datasource.getPort(), datasource.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            dbQuery.close();
            throw new DataQueryException("数据库连接失败");
        }

        try {
            // 3. 加载配置
            List<ExtSchemaMappingDO> schemaMappings = loadSchemaMappings(task.getId());
            Map<String, List<ExtAttributeMappingDO>> attrMap = loadAttributeMappings(task.getId());
            Map<String, List<ExtRelationMappingDO>> relMap = loadRelationMappings(task.getId());
            Map<Long, List<ExtRelationMappingMiddleDO>> middleMap = loadMiddleMappings();

            // 4. 处理数据
            List<DynamicEntity> entities = new ArrayList<>();
            List<Map<String,Object>> relations = new ArrayList<>();

            for (ExtSchemaMappingDO schema : schemaMappings) {
                List<ExtRelationMappingDO> relMappings = relMap.get(schema.getTableName());

                if (CollectionUtils.isEmpty(relMappings)) {
                    // 单表处理 - 直接查询
                    processSingleTable(task, schema, dbQuery, dbQueryProperty, isIncrement,
                            existingMap, datasource, attrMap, entities);
                } else {
                    // 多表处理 - 关联查询
                    processMultiTablesWithComplexRelations(task, schema, dbQuery, dbQueryProperty, isIncrement,
                            existingMap, datasource, attrMap, relMappings, middleMap, entities, relations);
                }
            }

            Map<Long, DynamicEntity> map = entities
                    .stream().collect(Collectors.toMap(DynamicEntity::getId, e -> e, (e1, e2) -> e1));

            // 将关系中的节点指向，设置为正确的，避免丢失关系。
            entities.forEach(e -> {
                Map<String, List<DynamicEntityRelationship>> relationshipEntityMap = e.getRelationshipEntityMap();
                if (relationshipEntityMap != null) {
                    relationshipEntityMap.forEach((name, list) -> {
                        list.forEach(r -> {
                            DynamicEntity endNode = map.get(r.getEndNode().getId());
                            if (endNode == null) {
                                endNode = dynamicRepository.findById(r.getEndNode().getId()).orElse(new DynamicEntity());
                            }
                            r.setEndNode(endNode);
                        });
                    });
                }
                e.setRelationshipEntityMap(relationshipEntityMap);
            });
            // 5. 保存数据
            if (!entities.isEmpty())  dynamicRepository.saveAll(entities);
            Neo4jBuildWrapper<DynamicEntity> build = new Neo4jBuildWrapper<>(DynamicEntity.class);
            for (Map<String, Object> rel : relations) {
                dynamicRepository.mergeRelationship(
                        Neo4jLabelEnum.STRUCTURED.getLabel(), build,
                        (Map<String, Object>) rel.get("startNode"),
                        (Map<String, Object>) rel.get("endNode"),
                        (String) rel.get("rel"), (Map<String, Object>) rel.get("relMa"));
            }
//            for (Map<String, Object> rel : relations) {
//                dynamicRepository.mergeRelationship(
//                        (String) rel.get("rel"), (Map<String, Object>) rel.get("relMa"),
//                        (Neo4jBuildWrapper<DynamicEntity>) rel.get("startNode"),
//                        (Neo4jBuildWrapper<DynamicEntity>) rel.get("endNode"));
//            }


            // 6. 更新任务状态
            task.setStatus(ExtTaskStatus.EXECUTED.getValue());
            extStructTaskMapper.updateById(task);

        } finally {
            dbQuery.close();
        }
    }

    /**
     * 处理多表
     */
    private void processMultiTablesWithComplexRelations(ExtStructTaskDO task, ExtSchemaMappingDO schema,
                                                        DbQuery dbQuery, DbQueryProperty dbQueryProperty,
                                                        boolean isIncrement, Map<String, DynamicEntity> existingMap,
                                                        DmDatasourceRespDTO datasource,
                                                        Map<String, List<ExtAttributeMappingDO>> attrMap,
                                                        List<ExtRelationMappingDO> relMappings,
                                                        Map<Long, List<ExtRelationMappingMiddleDO>> middleMap,
                                                        List<DynamicEntity> entities,
                                                        List<Map<String, Object>> relations) {

        // 用于缓存已处理的实体，避免重复创建
        Map<String, DynamicEntity> processedEntities = new HashMap<>();
        // 用于缓存已处理的关系，避免重复创建
        Set<String> processedRelationKeys = new HashSet<>();

        // 首先，为每个关系映射创建独立的查询
        for (ExtRelationMappingDO rel : relMappings) {
            // 分页查询参数
            Integer pageNum = 1;
            boolean hasMoreData = true;

            // 循环分页查询直到没有数据
            while (hasMoreData) {
                PageUtil pageUtil = new PageUtil(pageNum, PAGE_SIZE);
                Integer offset = pageUtil.getOffset();
                // 构建关联查询SQL
                String sql = buildRelationSQL(schema, rel, dbQuery, dbQueryProperty, middleMap);
                // 使用dbQuery的分页方法
                tech.qiantong.qknow.common.database.core.PageResult<Map<String, Object>> rows = dbQuery.queryByPage(sql, offset, PAGE_SIZE);

                if (rows == null || rows.getData() == null || rows.getData().isEmpty()) {
                    hasMoreData = false;
                    continue;
                }

                if (CollectionUtils.isEmpty(rows.getData())) continue;

                // 处理查询结果中的每一行
                for (Map<String, Object> row : rows.getData()) {
                    // 1. 提取源表数据（a_开头的字段）
                    Map<String, Object> sourceData = extractTableData(row, "a_", schema.getTableName());

                    // 获取源表实体信息
                    String sourceEntityNameField = schema.getEntityNameField();
                    String sourcePrimaryKeyField = schema.getPrimaryKey();

                    Object sourceEntityName = sourceData.get(sourceEntityNameField);
                    Object sourcePrimaryKeyValue = sourceData.get(sourcePrimaryKeyField);

                    if (sourceEntityName == null || sourcePrimaryKeyValue == null) continue;

                    // 2. 提取目标表数据（b_开头的字段）
                    Map<String, Object> targetData = extractTableData(row, "b_", rel.getRelationTable());

                    // 获取目标表实体信息
                    String targetEntityNameField = rel.getRelationNameField();
                    String targetPrimaryKeyField = rel.getRelationField();

                    Object targetEntityName = targetData.get(targetEntityNameField);
                    Object targetPrimaryKeyValue = targetData.get(targetPrimaryKeyField);

                    if (targetEntityName == null || targetPrimaryKeyValue == null) continue;

                    // 3. 获取或创建源实体
                    DynamicEntity sourceEntity = getOrCreateEntity(task, datasource, schema, sourceData, attrMap,
                            existingMap, entities, processedEntities, isIncrement,
                            sourceEntityName, sourcePrimaryKeyValue);

                    if (sourceEntity == null) continue;

                    // 4. 获取或创建目标实体
                    DynamicEntity targetEntity = getOrCreateTargetEntity(task, datasource, rel, targetData,
                            existingMap, entities, processedEntities, isIncrement,
                            targetEntityName, targetPrimaryKeyValue);

                    if (targetEntity == null) continue;

                    // 5. 构建关系键（用于去重）
                    String relationKey = buildRelationKey(sourceEntity, targetEntity, rel.getRelation());

                    // 6. 如果关系不存在，则创建
                    if (!processedRelationKeys.contains(relationKey)) {
                        Map<String, Object> relation = buildRelation(task, sourceEntity, targetEntity, rel);
                        relations.add(relation);
                        processedRelationKeys.add(relationKey);
                    }
                }

                // 准备下一页
                pageNum++;

                // 如果当前页数据少于pageSize，说明是最后一页
                if (rows.getData().size() < PAGE_SIZE) {
                    hasMoreData = false;
                }
            }

        }
    }

    /**
     * 获取或创建实体
     */
    private DynamicEntity getOrCreateEntity(ExtStructTaskDO task, DmDatasourceRespDTO datasource,
                                          ExtSchemaMappingDO schema, Map<String, Object> data,
                                          Map<String, List<ExtAttributeMappingDO>> attrMap,
                                          Map<String, DynamicEntity> existingMap,
                                          List<DynamicEntity> entities,
                                          Map<String, DynamicEntity> processedEntities,
                                          boolean isIncrement,
                                          Object entityName, Object primaryKeyValue) {

        // 构建实体唯一键（表名 + 主键值）
        String entityKey = schema.getTableName() + "_" + primaryKeyValue;

        // 1. 检查是否已处理（当前批次缓存）
        if (processedEntities.containsKey(entityKey)) {
            return processedEntities.get(entityKey);
        }

        // 2. 检查是否已在当前实体列表中
        DynamicEntity existingEntity = findEntityInList(entities, schema.getTableName(), primaryKeyValue);
        if (existingEntity != null) {
            processedEntities.put(entityKey, existingEntity);
            return existingEntity;
        }

        // 3. 检查是否已存在（增量模式下的历史数据）
        if (isIncrement) {
            // 从existingMap中查找（现有实现使用表名+实体名，需要改进）
            for (DynamicEntity entity : existingMap.values()) {
                if (schema.getTableName().equals(entity.getDynamicProperties().get("table_name")) &&
                        primaryKeyValue.toString().equals(entity.getDynamicProperties().get("primary_key_data").toString())) {
                    processedEntities.put(entityKey, entity);
                    return entity;
                }
            }
        }

        // 4. 创建新实体
        DynamicEntity entity = createDynamicEntity(task, datasource, schema, data, attrMap,
                entityName, primaryKeyValue);
        entities.add(entity);
        processedEntities.put(entityKey, entity);

        return entity;
    }

    /**
     * 获取或创建目标实体
     */
    private DynamicEntity getOrCreateTargetEntity(ExtStructTaskDO task, DmDatasourceRespDTO datasource,
                                                ExtRelationMappingDO rel, Map<String, Object> data,
                                                Map<String, DynamicEntity> existingMap,
                                                List<DynamicEntity> entities,
                                                Map<String, DynamicEntity> processedEntities,
                                                boolean isIncrement,
                                                Object entityName, Object primaryKeyValue) {

        // 构建实体唯一键（表名 + 主键值）
        String entityKey = rel.getRelationTable() + "_" + primaryKeyValue;

        // 1. 检查是否已处理（当前批次缓存）
        if (processedEntities.containsKey(entityKey)) {
            return processedEntities.get(entityKey);
        }

        // 2. 检查是否已在当前实体列表中
        DynamicEntity existingEntity = findEntityInList(entities, rel.getRelationTable(), primaryKeyValue);
        if (existingEntity != null) {
            processedEntities.put(entityKey, existingEntity);
            return existingEntity;
        }

        // 3. 检查是否已存在（增量模式下的历史数据）
        if (isIncrement) {
            // 从existingMap中查找
            for (DynamicEntity entity : existingMap.values()) {
                if (rel.getRelationTable().equals(entity.getDynamicProperties().get("table_name")) &&
                        primaryKeyValue.toString().equals(entity.getDynamicProperties().get("primary_key_data").toString())) {
                    processedEntities.put(entityKey, entity);
                    return entity;
                }
            }
        }

        // 4. 创建新实体
        DynamicEntity entity = createTargetEntity(task, datasource, rel, data,
                entityName, primaryKeyValue);
        entities.add(entity);
        processedEntities.put(entityKey, entity);

        return entity;
    }

    /**
     * 在实体列表中查找实体（根据表名和主键值）
     */
    private DynamicEntity findEntityInList(List<DynamicEntity> entities, String tableName, Object primaryKeyValue) {
        for (DynamicEntity entity : entities) {
            if (tableName.equals(entity.getDynamicProperties().get("table_name")) &&
                    primaryKeyValue.toString().equals(entity.getDynamicProperties().get("primary_key_data").toString())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 构建关系键（用于去重）
     * 格式：源表:源主键->目标表:目标主键:关系类型
     */
    private String buildRelationKey(DynamicEntity sourceEntity, DynamicEntity targetEntity, String relationType) {
        String sourceKey = sourceEntity.getDynamicProperties().get("table_name") + ":" +
                sourceEntity.getDynamicProperties().get("primary_key_data");
        String targetKey = targetEntity.getDynamicProperties().get("table_name") + ":" +
                targetEntity.getDynamicProperties().get("primary_key_data");

        return sourceKey + "->" + targetKey + ":" + relationType;
    }

    /**
     * 从查询结果中提取指定表的字段数据
     */
    private Map<String, Object> extractTableData(Map<String, Object> row, String prefix, String tableName) {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : row.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.startsWith(prefix)) {
                // 去掉前缀，得到原始字段名
                String originalField = key.substring(prefix.length());
                result.put(originalField, value);
            } else if (prefix.equals("a_") && !key.startsWith("b_")) {
                // 对于源表，还包括那些没有前缀的字段（如name、primary_key_data等）
                // 这些字段可能是之前处理时添加的
                result.put(key, value);
            }
        }

        return result;
    }

    /**
     * 构建关联SQL
     */
    private String buildRelationSQL(ExtSchemaMappingDO schema, ExtRelationMappingDO rel,
                                    DbQuery dbQuery, DbQueryProperty dbQueryProperty,
                                    Map<Long, List<ExtRelationMappingMiddleDO>> middleMap) {

        // 获取列信息
        List<DbColumn> sourceCols = dbQuery.getTableColumns(dbQueryProperty, schema.getTableName());
        List<DbColumn> targetCols = dbQuery.getTableColumns(dbQueryProperty, rel.getRelationTable());

        StringBuilder sql = new StringBuilder("SELECT ");

        // 添加源表字段
        for (DbColumn col : sourceCols) {
            sql.append("t1.").append(col.getColName()).append(" AS a_").append(col.getColName()).append(", ");
        }

        // 添加目标表字段
        List<ExtRelationMappingMiddleDO> middleList = middleMap.get(rel.getId());
        int targetIndex = CollectionUtils.isEmpty(middleList) ? 2 : middleList.size() + 2;
        for (DbColumn col : targetCols) {
            sql.append("t").append(targetIndex).append(".").append(col.getColName())
                    .append(" AS b_").append(col.getColName()).append(", ");
        }

        // 移除末尾逗号
        sql.delete(sql.length() - 2, sql.length());

        // FROM和JOIN
        sql.append("\nFROM ").append(schema.getTableName()).append(" t1");

        if (CollectionUtils.isEmpty(middleList)) {
            sql.append("\nINNER JOIN ").append(rel.getRelationTable()).append(" t2")
                    .append("\n    ON t1.").append(rel.getFieldName())
                    .append(" = t2.").append(rel.getRelationField());
        } else {
            String prevTable = "t1";
            String prevField = rel.getFieldName();
            for (int i = 0; i < middleList.size(); i++) {
                ExtRelationMappingMiddleDO middle = middleList.get(i);
                String tableName = dbQuery.buildTableNameByDbType(middle.getTableName());
                String currTable = "t" + (i + 2);

                sql.append("\nINNER JOIN ").append(tableName).append(" ").append(currTable)
                        .append("\n    ON ").append(prevTable).append(".").append(prevField)
                        .append(" = ").append(currTable).append(".").append(middle.getTableField());

                prevTable = currTable;
                prevField = middle.getRelationField();
            }

            sql.append("\nINNER JOIN ").append(rel.getRelationTable()).append(" t").append(targetIndex)
                    .append("\n    ON ").append(prevTable).append(".").append(prevField)
                    .append(" = t").append(targetIndex).append(".").append(rel.getRelationField());
        }

        return sql.toString();
    }

    /**
     * 创建图实体
     */
    private DynamicEntity createDynamicEntity(ExtStructTaskDO task, DmDatasourceRespDTO datasource,
                                          ExtSchemaMappingDO schema, Map<String, Object> data,
                                          Map<String, List<ExtAttributeMappingDO>> attrMap,
                                          Object entityNameValue, Object primaryKeyValue) {

        ConcurrentHashMap<String, Object> props = new ConcurrentHashMap<>();

        // 基础属性
        props.put("task_id", task.getId());
        props.put("database_id", datasource.getId());
        props.put("table_name", schema.getTableName());
        props.put("schema_id", schema.getSchemaId());
        props.put("schema_name", schema.getSchemaName());
        props.put("entity_type", ExtractType.STRUCTURED.getValue());
        props.put("workspace_id", task.getWorkspaceId());
        props.put("primary_key", schema.getPrimaryKey());
        props.put("primary_key_data", primaryKeyValue);
        props.put("name", entityNameValue);
//        props.put("create_time", LocalDateTime.now().format(formatter));

        // 添加属性映射
        List<ExtAttributeMappingDO> attrs = attrMap.get(schema.getTableName());
        if (CollectionUtils.isNotEmpty(attrs)) {
            // 构建字段名到属性ID的映射
            Map<String, Long> fieldToAttrId = new HashMap<>();
            for (ExtAttributeMappingDO attr : attrs) {
                if (attr.getAttributeId() != null) {
                    fieldToAttrId.put(attr.getFieldName(), attr.getAttributeId());
                }
            }

            // 添加属性字段
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String fieldName = entry.getKey();
                Long attrId = fieldToAttrId.get(fieldName);
                if (attrId != null && entry.getValue() != null) {
                    props.put("attribute_id_" + attrId, entry.getValue());
                }
            }
        }

        // 创建实体对象
        DynamicEntity entity = new DynamicEntity();
        entity.setName(entityNameValue.toString());
        entity.setReleaseStatus(task.getPublishStatus());
        entity.setDynamicProperties(props);
        entity.addLabels(GraphLabelEnum.STRUCTURED.getLabel());
        entity.setCreateTime(new Date());

        return entity;
    }

    /**
     * 创建目标实体（用于多表关联）
     */
    private DynamicEntity createTargetEntity(ExtStructTaskDO task, DmDatasourceRespDTO datasource,
                                           ExtRelationMappingDO rel, Map<String, Object> data,
                                           Object entityNameValue, Object primaryKeyValue) {

        ConcurrentHashMap<String, Object> props = new ConcurrentHashMap<>();

        // 基础属性
        props.put("task_id", task.getId());
        props.put("database_id", datasource.getId());
        props.put("table_name", rel.getRelationTable());
        props.put("entity_type", ExtractType.STRUCTURED.getValue());
        props.put("workspace_id", task.getWorkspaceId());
        props.put("primary_key", rel.getRelationField());
        props.put("primary_key_data", primaryKeyValue);
        props.put("name", entityNameValue);
//        props.put("create_time", LocalDateTime.now().format(formatter));

        // 注意：目标实体的schema_id和schema_name可能为空，因为没有对应的schema配置
        // 如果需要，可以从其他途径获取

        // 创建实体对象
        DynamicEntity entity = new DynamicEntity();
        entity.setName(entityNameValue.toString());
        entity.setReleaseStatus(task.getPublishStatus());
        entity.setDynamicProperties(props);
        entity.addLabels(GraphLabelEnum.STRUCTURED.getLabel());
        entity.setCreateTime(new Date());

        return entity;
    }

    /**
     * 构建关系
     */
    private Map<String, Object> buildRelation(ExtStructTaskDO task,
                                              DynamicEntity sourceEntity,
                                              DynamicEntity targetEntity,
                                              ExtRelationMappingDO rel) {

        Map<String, Object> relation = new HashMap<>();

        // 关系类型
        relation.put("rel", rel.getRelation());

        // 关系属性
        ConcurrentHashMap<String, Object> relProps = new ConcurrentHashMap<>();
        relProps.put("task_id", task.getId());
        relProps.put("entity_type", ExtractType.STRUCTURED.getValue());
        relation.put("relMa", relProps);

        // 起点节点
        Neo4jQueryWrapper<DynamicEntity> startNode = createEntityUpsertWrapper(sourceEntity, task);
        relation.put("startNode", startNode.getParams());

        // 终点节点
        Neo4jQueryWrapper<DynamicEntity> endNode = createEntityUpsertWrapper(targetEntity, task);
        relation.put("endNode", endNode.getParams());

        return relation;
    }

    /**
     * 创建实体更新包装器
     */
    private Neo4jQueryWrapper<DynamicEntity> createEntityUpsertWrapper(DynamicEntity entity, ExtStructTaskDO task) {
        Neo4jQueryWrapper<DynamicEntity> wrapper = new Neo4jQueryWrapper<>(DynamicEntity.class);
        wrapper.addLabels(GraphLabelEnum.STRUCTURED.getLabel());

        // 使用多个条件确保唯一性
        wrapper.eq("table_name",
                entity.getDynamicProperties().get("table_name"));
        wrapper.eq("primary_key_data",
                entity.getDynamicProperties().get("primary_key_data"));
        wrapper.eq( "task_id", task.getId());

        return wrapper;
    }

    /**
     * 加载现有实体
     */
    private Map<String, DynamicEntity> loadExistingEntities(ExtStructTaskDO task) {
        List<DynamicEntity> graphEntities = dynamicRepository.findAll();

        return graphEntities.stream()
                .filter(e -> task.getId().equals(e.getDynamicProperties().get("task_id")))
                .collect(Collectors.toMap(
                        e -> e.getDynamicProperties().get("table_name") + "_" + e.getDynamicProperties().get("name"),
                        Function.identity(), (e1, e2) -> e1));
    }

    /**
     * 处理单表
     */
    private void processSingleTable(ExtStructTaskDO task, ExtSchemaMappingDO schema,
                                    DbQuery dbQuery, DbQueryProperty dbQueryProperty,
                                    boolean isIncrement, Map<String, DynamicEntity> existingMap,
                                    DmDatasourceRespDTO datasource,
                                    Map<String, List<ExtAttributeMappingDO>> attrMap,
                                    List<DynamicEntity> entities) {

        // 构建SQL
        String tableName = dbQuery.buildTableNameByDbType(schema.getTableName());
        if (dbQuery.generateCheckTableExistsSQL(dbQueryProperty, schema.getTableName()) == 0) {
            throw new DataQueryException("表不存在: " + schema.getTableName());
        }

        String sql = "SELECT * FROM " + tableName;
        if (isIncrement && schema.getLastDateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sql += " WHERE " + schema.getEntityTimeField() + " > '" + sdf.format(schema.getLastDateTime()) + "'";
        }

        // 分页查询参数
        Integer pageNum = 1;
        boolean hasMoreData = true;

        // 循环分页查询直到没有数据
        while (hasMoreData) {
            PageUtil pageUtil = new PageUtil(pageNum, PAGE_SIZE);
            Integer offset = pageUtil.getOffset();
            // 使用dbQuery的分页方法
            tech.qiantong.qknow.common.database.core.PageResult<Map<String, Object>> rows = dbQuery.queryByPage(sql, offset, PAGE_SIZE);

            if (rows == null || rows.getData() == null || rows.getData().isEmpty()) {
                hasMoreData = false;
                continue;
            }

            for (Map<String, Object> row : rows.getData()) {
                // 获取实体名称值
                String entityNameField = schema.getEntityNameField();
                String primaryKeyField = schema.getPrimaryKey();

                Object entityNameValue = row.get(entityNameField);
                Object primaryKeyValue = row.get(primaryKeyField);

                if (entityNameValue == null || primaryKeyValue == null) {
                    continue;
                }

                // 构建实体唯一键
                String key = schema.getTableName() + "_" + entityNameValue;

                // 检查是否已存在
                if (isIncrement && existingMap.containsKey(key)) continue;
                if (isEntityExists(entities, schema.getTableName(), entityNameValue)) continue;

                // 创建实体
                DynamicEntity entity = createDynamicEntity(task, datasource, schema, row, attrMap,
                        entityNameValue, primaryKeyValue);
                entities.add(entity);
            }

            // 准备下一页
            pageNum++;

            // 如果当前页数据少于pageSize，说明是最后一页
            if (rows.getData().size() < PAGE_SIZE) {
                hasMoreData = false;
            }
        }
    }

    /**
     * 检查实体是否已存在
     */
    private boolean isEntityExists(List<DynamicEntity> entities, String tableName, Object entityName) {
        return entities.stream().anyMatch(e ->
                tableName.equals(e.getDynamicProperties().get("table_name")) &&
                        entityName.equals(e.getDynamicProperties().get("name"))
        );
    }

    /**
     * 加载配置的简单实现
     */
    private List<ExtSchemaMappingDO> loadSchemaMappings(Long taskId) {
        ExtSchemaMappingPageReqVO req = new ExtSchemaMappingPageReqVO();
        req.setTaskId(taskId);
        PageResult<ExtSchemaMappingDO> result = extSchemaMappingMapper.selectPage(req);
        return BeanUtils.toBean(result.getRows(), ExtSchemaMappingDO.class);
    }

    private Map<String, List<ExtAttributeMappingDO>> loadAttributeMappings(Long taskId) {
        ExtAttributeMappingPageReqVO req = new ExtAttributeMappingPageReqVO();
        req.setTaskId(taskId);
        PageResult<ExtAttributeMappingDO> result = extAttributeMappingMapper.selectPage(req);
        List<ExtAttributeMappingDO> list = BeanUtils.toBean(result.getRows(), ExtAttributeMappingDO.class);
        return list.stream().collect(Collectors.groupingBy(ExtAttributeMappingDO::getTableName));
    }

    private Map<String, List<ExtRelationMappingDO>> loadRelationMappings(Long taskId) {
        ExtRelationMappingPageReqVO req = new ExtRelationMappingPageReqVO();
        req.setTaskId(taskId);
        PageResult<ExtRelationMappingDO> result = extRelationMappingMapper.selectPage(req);
        List<ExtRelationMappingDO> list = BeanUtils.toBean(result.getRows(), ExtRelationMappingDO.class);
        return list.stream().collect(Collectors.groupingBy(ExtRelationMappingDO::getTableName));
    }

    private Map<Long, List<ExtRelationMappingMiddleDO>> loadMiddleMappings() {
        List<ExtRelationMappingMiddleDO> list = extRelationMappingMiddleMapper.selectList();
        return list.stream().collect(Collectors.groupingBy(ExtRelationMappingMiddleDO::getRelationId));
    }


    @Override
    public AjaxResult getAttributeInformation(List<String> list) {
        List<ExtSchemaAttributeDO> attributeList = extSchemaAttributeMapper.selectList("id", list);
        log.info("------{}", attributeList);
        return AjaxResult.success("获取成功", attributeList);
    }

    /**
     * 获取抽取结果
     *
     * @param taskId
     * @param extractType
     * @return
     */
    @Override
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
    @Override
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
    @Override
    public AjaxResult cancelReleaseByTaskId(ExtStructTaskDO structTaskDO) {
        extStructTaskMapper.updateById(structTaskDO);
        extNeo4jService.updateByTaskIdAndExtractType(structTaskDO.getId(), ExtractType.STRUCTURED.getValue(), ReleaseStatus.UNPUBLISHED.getValue());
        return AjaxResult.success("取消发布成功");
    }

    /**
     * 修改结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    @Transactional
    @Override
    public AjaxResult updateDataMapping(ExtStructTask extStructTask) {
        Long taskId = extStructTask.getTaskId();
        //修改任务数据
        DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(extStructTask.getDataSourceId());
        ExtStructTaskDO extStructTaskDO = extStructTaskMapper.selectById(taskId);
//        extStructTaskDO.setStatus(ExtTaskStatus.UNEXECUTED.getValue());
        extStructTaskDO.setRemark(extStructTask.getRemark());
        extStructTaskDO.setName(extStructTask.getTaskName());
        extStructTaskDO.setUpdateType(extStructTask.getUpdateType());
        extStructTaskDO.setUpdateFrequency(extStructTask.getUpdateFrequency());
        extStructTaskDO.setDatasourceId(extStructTask.getDataSourceId());
        extStructTaskDO.setDatasourceName(datasource.getDatasourceName());
        extStructTaskDO.setUpdateBy(extStructTask.getUpdateBy());
        extStructTaskDO.setUpdaterId(extStructTask.getUpdaterId());
        extStructTaskDO.setUpdateTime(new Date());
        extStructTaskMapper.updateById(extStructTaskDO);
        //删除之前添加的映射关系
        extSchemaMappingMapper.delete("task_id", taskId.toString());//概念
        extAttributeMappingMapper.delete("task_id", taskId.toString());//属性
        //删除关系中间表数据
        List<ExtRelationMappingDO> relationMappingDOList =  extRelationMappingMapper.selectList("task_id",taskId.toString());
        if (StringUtils.isNotEmpty(relationMappingDOList)){
            for (ExtRelationMappingDO extRelationMappingDO : relationMappingDOList) {
                extRelationMappingMiddleMapper.delete("relation_id", extRelationMappingDO.getId().toString());
            }
        }
        extRelationMappingMapper.delete("task_id", taskId.toString());//关系

        return addMapping(extStructTask, extStructTaskDO);
    }

    /**
     * 添加结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    @Transactional
    @Override
    public AjaxResult addDataMapping(ExtStructTask extStructTask) {
        DmDatasourceRespDTO datasource = daDatasourceApiService.getDatasourceById(extStructTask.getDataSourceId());
        // 存储结构化抽取任务
        ExtStructTaskDO extStructTaskDO = new ExtStructTaskDO();
        extStructTaskDO.setWorkspaceId(extStructTask.getWorkspaceId());
        extStructTaskDO.setName(extStructTask.getTaskName());
        extStructTaskDO.setRemark(extStructTask.getRemark());
        extStructTaskDO.setUpdateType(extStructTask.getUpdateType());
        extStructTaskDO.setUpdateFrequency(extStructTask.getUpdateFrequency());
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
                schemaMappingDO.setPrimaryKey(mappingData.getPrimaryKey());
                schemaMappingDO.setEntityTimeField(mappingData.getEntityTimeField());
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
                //关系映射添加集合数据
                ArrayList<ExtRelationMappingDO> relationMappingDOS = new ArrayList<>();
                ArrayList<ExtRelationMappingMiddleDO> relationMappingMiddleDOS = new ArrayList<>();
                if (StringUtils.isNotEmpty(relationshipList)) {
                    relationshipList = relationshipList.stream()
                            .filter(e -> StringUtils.isNotBlank(e.getTableName())
                                    && StringUtils.isNotBlank(e.getFieldName())
                                    && StringUtils.isNotBlank(e.getRelation())
                                    && StringUtils.isNotBlank(e.getRelationTable())
                                    && StringUtils.isNotBlank(e.getRelationField())
                                    && StringUtils.isNotBlank(e.getRelationNameField())
                            ).collect(Collectors.toList());
                    relationshipList.forEach(e -> {
                        if (StringUtils.isBlank(e.getTableName()) || StringUtils.isBlank(e.getFieldName()) || StringUtils.isBlank(e.getRelation())
                                || StringUtils.isBlank(e.getRelationTable()) || StringUtils.isBlank(e.getRelationField())) {
                            return;
                        }
                        ExtRelationMappingDO relationMappingDO = new ExtRelationMappingDO();
                        relationMappingDO.setWorkspaceId(extStructTask.getWorkspaceId());
                        relationMappingDO.setTaskId(extStructTaskDO.getId());
                        relationMappingDO.setTableName(ext.getTableName());
                        relationMappingDO.setTableComment(ext.getTableComment());
                        //TODO fieldComment 这个值暂时没有
                        relationMappingDO.setFieldComment("");
                        relationMappingDO.setFieldName(e.getFieldName());
                        relationMappingDO.setRelation(e.getRelation());
                        relationMappingDO.setRelationTable(e.getRelationTable());
                        for (ExtStructTask.TableData tableDatum : tableData) {
                            if (tableDatum.getTableName().equals(e.getRelationTable())) {
                                relationMappingDO.setRelationTableName(tableDatum.getTableComment());
                            }
                        }
                        relationMappingDO.setRelationField(e.getRelationField());
                        relationMappingDO.setRelationNameField(e.getRelationNameField());
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
                }

                // 批量插入关系映射数据，并获取生成的ID
                if (relationMappingDOS.size() > 0) {
                    // 批量插入关系映射数据
                    extRelationMappingMapper.insertBatch(relationMappingDOS);

                    // 处理中间表数据
                    for (int i = 0; i < relationshipList.size(); i++) {
                        ExtStructTask.Relationship relationship = relationshipList.get(i);
                        ExtRelationMappingDO savedRelationMapping = relationMappingDOS.get(i);
                        Long relationId = savedRelationMapping.getId(); // 获取生成的ID

                        List<ExtStructTask.RelationMappingMiddle> relationMappingMiddles = relationship.getRelationMappingMiddle();
                        if (CollectionUtils.isNotEmpty(relationMappingMiddles)) {
                            relationMappingMiddles.forEach(r -> {
                                if (StringUtils.isNotBlank(r.getTableName())
                                        && StringUtils.isNotBlank(r.getTableField())
                                        && StringUtils.isNotBlank(r.getRelationField())) {

                                    ExtRelationMappingMiddleDO relationMappingMiddleDO = new ExtRelationMappingMiddleDO();
                                    relationMappingMiddleDO.setRelationId(relationId); // 设置关系ID
                                    relationMappingMiddleDO.setTableName(r.getTableName());
                                    relationMappingMiddleDO.setTableField(r.getTableField());
                                    relationMappingMiddleDO.setRelationField(r.getRelationField());
                                    relationMappingMiddleDO.setCreateTime(new Date());
                                    relationMappingMiddleDO.setUpdateTime(new Date());
                                    relationMappingMiddleDO.setCreateBy(extStructTask.getUpdateBy());
                                    relationMappingMiddleDO.setUpdateBy(extStructTask.getUpdateBy());
                                    relationMappingMiddleDOS.add(relationMappingMiddleDO);
                                }
                            });
                        }
                    }

                    // 批量插入中间表数据
                    if (relationMappingMiddleDOS.size() > 0) {
                        extRelationMappingMiddleMapper.insertBatch(relationMappingMiddleDOS);
                    }
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
    public int updateExtStructTask(ExtStructTaskSaveReqVO updateReqVO) throws SchedulerException {
        // 更新结构化抽取任务
        ExtStructTaskDO updateObj = BeanUtils.toBean(updateReqVO, ExtStructTaskDO.class);
        int result = extStructTaskMapper.updateById(updateObj);
        // 如果更新成功，重新查询数据
        if (result > 0) {
            // 查询更新后的数据
            ExtStructTaskDO updatedTask = extStructTaskMapper.selectById(updateObj.getId());

            // 判断如果定时更新状态为启用,则执行定时任务
            if(updatedTask.getUpdateStatus().equals(ExtTaskConcurrent.ALLOW.getValue())) {
                // 只处理已执行完成的任务，验证 Cron 表达式是否有效
                if (updatedTask.getStatus().equals(ExtTaskStatus.EXECUTED.getValue()) && CronUtils.isValid(updatedTask.getUpdateFrequency())) {
                    try {
                        scheduleTask(updatedTask); // 创建定时任务
                    } catch (SchedulerException | TaskException e) {
                        log.error("定时任务创建失败，任务ID: {}", updatedTask.getId(), e);
                        // 可考虑重试或标记任务为失败
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }else {
                // 检查定时任务是否已存在，。删除任务
                if (isScheduleJobExists(updatedTask.getId(),updatedTask.getUpdateFrequency())) {
                    this.deleteJob(updatedTask.getId());
                }
            }
        }
        return result;
    }

    @Override
    public int removeExtStructTask(Collection<Long> idList) throws SchedulerException {
        // 批量删除结构化抽取任务
        int result = extStructTaskMapper.deleteBatchIds(idList);

        //删除对应的定时任务
        for (Long id : idList) {
            this.deleteJob(id);
        }
        return result;
    }

    @Override
    public ExtStructTaskDO getExtStructTaskById(Long id) {
        return extStructTaskMapper.selectById(id);
    }

    @Override
    public AjaxResult getInfo(Long id) {
        ExtStructTaskDO structTaskDO = extStructTaskMapper.selectById(id);
        ExtStructTaskRespVO structTaskRespVO = BeanUtils.toBean(structTaskDO, ExtStructTaskRespVO.class);

        //获取任务中关联的概念
        ExtSchemaMappingPageReqVO mappingPageReqVO = new ExtSchemaMappingPageReqVO();
        mappingPageReqVO.setTaskId(id);
        PageResult<ExtSchemaMappingDO> schemaMappingDOPageResult = extSchemaMappingMapper.selectPage(mappingPageReqVO);
        List<ExtSchemaMappingRespVO> schemaMappingList = BeanUtils.toBean(schemaMappingDOPageResult.getRows(), ExtSchemaMappingRespVO.class);

        //获取任务中关联的属性映射
        ExtAttributeMappingPageReqVO attributeMappingPageReqVO = new ExtAttributeMappingPageReqVO();
        attributeMappingPageReqVO.setTaskId(id);
        PageResult<ExtAttributeMappingDO> attributeMappingDOPageResult = extAttributeMappingMapper.selectPage(attributeMappingPageReqVO);
        List<ExtAttributeMappingRespVO> attributeMappingList = BeanUtils.toBean(attributeMappingDOPageResult.getRows(), ExtAttributeMappingRespVO.class);

        //获取任务中关联的关系映射
        ExtRelationMappingPageReqVO relationMappingPageReqVO = new ExtRelationMappingPageReqVO();
        relationMappingPageReqVO.setTaskId(id);
        PageResult<ExtRelationMappingDO> relationMappingDOPageResult = extRelationMappingMapper.selectPage(relationMappingPageReqVO);
        List<ExtRelationMappingRespVO> relationMappingList = BeanUtils.toBean(relationMappingDOPageResult.getRows(), ExtRelationMappingRespVO.class);


        //获取所有的关系中间表数据
        List<ExtRelationMappingMiddleDO> relationMappingMiddleDOList =  extRelationMappingMiddleMapper.selectList();
        if(StringUtils.isNotEmpty(relationMappingMiddleDOList)){
            Map<Long, List<ExtRelationMappingMiddleDO>> relationMappingMiddleMap = relationMappingMiddleDOList.stream().collect(Collectors.groupingBy(ExtRelationMappingMiddleDO::getRelationId));
            for (ExtRelationMappingRespVO extRelationMappingRespVO : relationMappingList) {
                if(relationMappingMiddleMap.get(extRelationMappingRespVO.getId()) != null) {
                    extRelationMappingRespVO.setRelationMappingMiddle(relationMappingMiddleMap.get(extRelationMappingRespVO.getId()));
                }
            }
        }


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
            String entityTimeField = null;
            String primaryKey = null;
            String entityNameField = null;
            for (ExtSchemaMappingRespVO respVO : schemaMappingList) {
                if (tableName.equals(respVO.getTableName())) {
                    status = true;
                    schemaId = respVO.getSchemaId();
                    schemaName = respVO.getSchemaName();
                    entityTimeField = respVO.getEntityTimeField();
                    primaryKey = respVO.getPrimaryKey();
                    entityNameField = respVO.getEntityNameField();
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
            hashMap.put("entityTimeField",entityTimeField);
            hashMap.put("primaryKey",primaryKey);
            hashMap.put("entityNameField",entityNameField);

            tableMappingList.add(hashMap);
        }


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("structTask", structTaskRespVO);
        hashMap.put("tableMappingList", tableMappingList);//表
        hashMap.put("schemaMappingList", schemaMappingList);//概念
        hashMap.put("attributeMappingList", attributeMappingList);//属性
        hashMap.put("relationMappingList", relationMappingList);//关系
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



    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() throws Exception {
        ExtStructTaskPageReqVO pageReqVO = new ExtStructTaskPageReqVO();
        PageResult<ExtStructTaskDO> result = extStructTaskMapper.selectPage(pageReqVO);
        //遍历数据，查询定时任务
        for (ExtStructTaskDO structTaskDO : result.getList()) {
            // 如果 Cron 表达式为空，跳过当前任务
            if (structTaskDO.getUpdateFrequency() == null || !CronUtils.isValid(structTaskDO.getUpdateFrequency())) {
                continue; // 跳过这个任务，继续处理下一个
            }
            //任务状态是已完成并且定时更新状态的已开启的
            if (structTaskDO.getStatus().equals(ExtTaskStatus.EXECUTED.getValue()) && structTaskDO.getUpdateStatus().equals(ExtTaskConcurrent.ALLOW.getValue())) {
                this.scheduleTask(structTaskDO);
            }
        }
    }


    /**
     * 获取任务，执行定时数据更新
     *
     * @param extStructTaskDO
     * @return
     */
    public void scheduleTask(ExtStructTaskDO extStructTaskDO) throws Exception {
        // 检查定时任务是否已存在，并且cron表达式没有变
        if (isScheduleJobExists(extStructTaskDO.getId(),extStructTaskDO.getUpdateFrequency())) {
            log.info("定时任务已存在，任务ID: {}", extStructTaskDO.getId());
            return;
        }
        SysJob job = new SysJob();
        job.setJobId(extStructTaskDO.getId());
        job.setJobName(extStructTaskDO.getName() + "数据更新");
        //判断是全量更新还是增量更新
        if (extStructTaskDO.getUpdateType().equals(ExtTaskUpdateType.FULLUPDATE.getValue())) {
            job.setInvokeTarget("extStructTaskServiceImpl.getExtStructTaskFullUpdate(" + extStructTaskDO.getId() + ")");
        }
        if (extStructTaskDO.getUpdateType().equals(ExtTaskUpdateType.INCREMENTUDATE.getValue())) {
            job.setInvokeTarget("extStructTaskServiceImpl.getExtStructTaskIncrementUpdate(" + extStructTaskDO.getId() + ")");
        }
        job.setCronExpression(extStructTaskDO.getUpdateFrequency());
        job.setMisfirePolicy(ScheduleConstants.MISFIRE_IGNORE_MISFIRES);
        job.setConcurrent(ExtTaskConcurrent.FORBID.getValue().toString());
        job.setJobGroup("DEFAULT");
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        //创建定时任务
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 检查定时任务是否已存在
     */
    private boolean isScheduleJobExists(Long taskId,String cronExpression) {
        try {

            // 检查任务是否已存在
            JobKey jobKey = JobKey.jobKey("TASK_CLASS_NAME" + taskId.toString(), "DEFAULT");
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            // 创建 TriggerKey 来查找任务触发器
            TriggerKey triggerKey = TriggerKey.triggerKey("TASK_CLASS_NAME" + taskId.toString(), "DEFAULT");
            Trigger trigger = scheduler.getTrigger(triggerKey);

//            // 检查是否有对应的触发器
//            if (trigger != null) {
//                log.info("获取到任务的触发器，Cron表达式为: {}", ((CronTrigger) trigger).getCronExpression());
//            } else {
//                log.warn("未找到对应的触发器，任务ID: {}", taskId);
//            }

            //用于匹配cron表达式是否更新
            String cron = trigger != null ?  ((CronTrigger) trigger).getCronExpression() : null;

            if (jobDetail != null && trigger != null && cron.equals(cronExpression)) {
                log.info("获取到任务的 JobDetail: {}", jobDetail.getKey());
                return true; // 任务存在或这cron不一致
            } else {
                log.warn("未找到对应的 JobDetail，任务ID: {}", taskId);
                return false; // 任务不存在
            }
        } catch (SchedulerException e) {
            log.error("检查定时任务是否存在时发生错误", e);
            return false;
        }
    }


    //执行全量更新的方法
    public void getExtStructTaskFullUpdate(Integer taskId) {
        this.execStructTask(String.valueOf(taskId));
    }

    //执行增量更新的方法
    public void getExtStructTaskIncrementUpdate(Integer taskId){
        this.execStructTaskIncrementUpdate(String.valueOf(taskId));
    }

    //删除指定的触发器和任务
    public void deleteJob(Long taskId) throws SchedulerException {
        // 删除触发器
        TriggerKey triggerKey = TriggerKey.triggerKey("TASK_CLASS_NAME" + taskId.toString(), "DEFAULT");
        scheduler.unscheduleJob(triggerKey);

        // 删除任务
        JobKey jobKey = JobKey.jobKey("TASK_CLASS_NAME" + taskId.toString(), "DEFAULT");
        scheduler.deleteJob(jobKey);

    }

    /**
     * 定时任务立即执行一次
     *
     * @param extStructTask
     * @return
     */
    @Override
    public void  runStructTask(ExtStructTask extStructTask) {
        if (StringUtils.isNull(extStructTask.getTaskId())) {
            throw new IllegalArgumentException("任务ID不能为空");
        }
        ExtStructTaskDO extStructTaskDO = extStructTaskMapper.selectById(extStructTask.getTaskId());
        if (ReleaseStatus.PUBLISHED.getValue().equals(extStructTaskDO.getPublishStatus())) {
            throw new RuntimeException("已发布状态不能重新执行抽取");
        }
        extStructTaskDO.setStatus(ExtTaskStatus.INPROGRESS.getValue());
        extStructTaskMapper.updateById(extStructTaskDO);
        // ✅ 提交异步任务（不等待）
        CompletableFuture.runAsync(() -> {
            // 判断是全量更新还是增量更新
            if (extStructTask.getUpdateType().equals(ExtTaskUpdateType.FULLUPDATE.getValue())) {
                this.execStructTask(String.valueOf(extStructTask.getTaskId()));
            } else if (extStructTask.getUpdateType().equals(ExtTaskUpdateType.INCREMENTUDATE.getValue())) {
                this.execStructTaskIncrementUpdate(String.valueOf(extStructTask.getTaskId()));
            } else {
                throw new IllegalArgumentException("无效的更新类型");
            }
        });

    }

}
