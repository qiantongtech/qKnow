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

package tech.qiantong.qknow.neo4j.wrapper;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tech.qiantong.qknow.neo4j.utils.LambdaUtils;

import java.util.*;

public class Neo4jBuildWrapper<T> {
    @Getter
    private final Class<T> entityClass;

    public Neo4jBuildWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * 根据条件修改属性
     *
     * @param paramMap
     * @param updateMap
     * @return
     */
    public String updateQuery(String label, Map<String, Object> paramMap, Map<String, Object> updateMap) {
        ArrayList<String> arrayList = getUpdateListByMap("n",paramMap);
        StringBuilder query = new StringBuilder("MATCH (n:" + label + ")");
        if(paramMap.get("id") != null){
            query.append(" WHERE ID(n) = ").append(paramMap.get("id"));
        }else if (paramMap.size() > 0) {
            query.append(" WHERE ").append(String.join(" AND ", arrayList));
        }
        if (updateMap.size() > 0) {
            ArrayList<String> listByMap = getUpdateListByMap("n",updateMap);
            query.append(" SET ").append(String.join(",", listByMap));
        }
//        query.append(" RETURN n");
        return query.toString();
    }

    /**
     * 如果节点存在, 更新此节点的属性, 如果节点不存在, 创建节点和属性
     * MERGE 语法用于确保某个节点或关系存在。如果它已经存在，则返回该节点或关系；如果不存在，则创建新的节点或关系
     *
     * @return
     */
    public String mergeCreateNode(String label, Map<String, Object> mergeMap, Map<String, Object> attributeMap) {
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);

        StringBuilder query = new StringBuilder();
        if(!mergeMap.isEmpty()){
            ArrayList<String> arrayList = mergeAttByMap(mergeMap);
            query = new StringBuilder("MERGE (n:" + label + "{ " + String.join(",", arrayList) + "})");
        }else {
            query = new StringBuilder("MATCH (n:" + label + ")");
        }
        if (attributeMap.size() > 0) {
            query.append(" SET n += {");

            // 遍历 attributeMap，构建属性列表
            StringBuilder attributes = new StringBuilder();
            for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
                // 获取键和值
                String key = entry.getKey();
                Object value = entry.getValue();
                // 用于存储格式化后的值
                String valueString;
                // 判断值的类型并格式化
                if (value instanceof String) {
                    // 对于 String 类型，添加单引号
                    valueString = "'" + value + "'";
                } else if (value instanceof Number || value instanceof Boolean) {
                    // 对于 Number 和 Boolean 类型，直接使用其 toString() 结果，不需要引号
                    valueString = value.toString();
                } else if (value == null) {
                    // 对于 null 值，处理成 "null"
                    valueString = "null";
                } else {
                    // 对于其他类型，调用 toString() 获取其字符串表示
                    valueString = "'" + value + "'";;
                }
                // 为每个属性对生成格式化的 key: 'value' 字符串，并加入到 attributes 中
                if (attributes.length() > 0) {
                    attributes.append(", ");  // 每个属性对之间添加逗号
                }
                // 拼接属性字符串
                attributes.append(prefixName + key + ": " + valueString);
            }

            query.append(attributes);
            query.append("}");
        }
        return query.toString();
    }

    /**
     * 根据起点和结点创建关系 如果a和b不存在, 会创建节点
     *
     * @param startNodeMap
     * @param endNodeMap
     * @param rel
     * @param relMap
     * @return
     */
    public String createRelationship(String label, Map<String, Object> startNodeMap, Map<String, Object> endNodeMap, String rel, Map<String, Object> relMap) {
        ArrayList<String> aList = mergeAttByMap(startNodeMap);
        ArrayList<String> bList = mergeAttByMap(endNodeMap);
        ArrayList<String> rList = getUpdateListByMap("r",relMap);
        StringBuilder aquery = new StringBuilder("MERGE (a:" + label + "{ " + String.join(",", aList) + "})\n");
        StringBuilder bquery = new StringBuilder("MERGE (b:" + label + "{ " + String.join(",", bList) + "})\n");
        StringBuilder query = aquery.append(bquery).append("MERGE (a)-[r:" + rel + "]->(b) \n");
        if(!relMap.isEmpty()){
            query.append("ON CREATE SET " + String.join(",", rList));
        }
        return query.toString();
    }

    /**
     * 根据起点id和结点id创建关系
     * @param label
     * @param startNodeId
     * @param endNodeId
     * @param rel
     * @param relMap
     * @return
     */
    public String createRelationship(String label, Long startNodeId, Long endNodeId, String rel, Map<String, Object> relMap) {
        ArrayList<String> rList = getUpdateListByMap("r",relMap);
        StringBuilder aquery = new StringBuilder("MATCH (a:" + label + ") WHERE id(a) = + " + startNodeId  + "\n");
        StringBuilder bquery = new StringBuilder("MATCH (b:" + label + ") WHERE id(b) = + " + endNodeId  + "\n");
        StringBuilder query = aquery.append(bquery).append("MERGE (a)-[r:" + rel + "]->(b) \n");
        if(!relMap.isEmpty()){
            query.append("ON CREATE SET " + String.join(",", rList));
        }
        return query.toString();
    }

    @NotNull
    private ArrayList<String> mergeAttByMap(Map<String, Object> mergeMap) {
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);

        ArrayList<String> arrayList = new ArrayList<>();
        for (Map.Entry<String, Object> objectEntry : mergeMap.entrySet()) {
            String key = objectEntry.getKey();
            Object value = objectEntry.getValue();

            String valueString;
            // 根据不同的类型处理值
            if (value instanceof String) {
                // 对于字符串，添加引号
                valueString = "'" + value + "'";
            } else if (value instanceof Number || value instanceof Boolean) {
                // 对于数字和布尔值，直接使用它们
                valueString = value.toString();
            } else if (value == null) {
                // 对于null值，根据需要处理
                valueString = "null";
            } else {
                // 其他类型的值，可以根据需求处理（例如日期、集合等）
                valueString = value.toString();
            }

            // 拼接字符串
            arrayList.add(prefixName + key + ": " + valueString);
        }
        return arrayList;
    }

    @NotNull
    private ArrayList<String> getUpdateListByMap(String as,Map<String, Object> relMap) {
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);
        ArrayList<String> rList = new ArrayList<>();
        for (Map.Entry<String, Object> objectEntry : relMap.entrySet()) {
            String key = objectEntry.getKey();
            Object value = objectEntry.getValue();

            String valueString;
            // 判断值的类型并格式化
            if (value instanceof String) {
                // 对于String类型，添加单引号
                valueString = "'" + value + "'";
            } else if (value instanceof Number || value instanceof Boolean) {
                // 对于Number和Boolean类型，直接使用它们的toString()结果
                valueString = value.toString();
            } else if (value == null) {
                // 对于null值，可以选择处理成 null 或者其他值
                valueString = "null";
            } else {
                // 其他类型，调用toString()来获取字符串表示
                valueString = value.toString();
            }
            // 拼接字符串并添加到列表
            rList.add(as + "." + prefixName + key + "= " + valueString);
        }
        return rList;
    }

    public String deleteNodeAttribute(Long nodeId,String attributeKey){
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);

        return "MATCH (n) WHERE id(n) = " + nodeId +
                " REMOVE n." + prefixName + attributeKey + " " +
                " RETURN n";
    }


    //TODO 还在优化
    public static Map.Entry<String, Map<String, Object>> mergeCreateNode(
            Collection<String> labels,
            Map<String, Collection<?>> mergeFields,
            Map<String, Object> properties
    ) {
        // 参数映射
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        // 动态构建 WITH 和 UNWIND 子句
        List<String> mergeConditions = new ArrayList<>();
        if (mergeFields != null && !mergeFields.isEmpty()) {
            for (Map.Entry<String, Collection<?>> entry : mergeFields.entrySet()) {
                String field = entry.getKey();
                Collection<?> values = entry.getValue();

                if (values != null && !values.isEmpty()) {
                    String paramName = field + "Values";
                    query.append("WITH $").append(paramName).append(" AS ").append(field).append("Values\n");
                    query.append("UNWIND ").append(field).append("Values AS ").append(field).append("\n");
                    params.put(paramName, new ArrayList<>(values));
                    mergeConditions.add(field + ": " + field);
                }
            }
        }

        // 构建 MERGE 子句
        query.append("MERGE (n:");
        if (labels != null && !labels.isEmpty()) {
            query.append(String.join(":", labels)); // 多个标签用冒号分隔
        }
        if (!mergeConditions.isEmpty()) {
            query.append(" {").append(String.join(", ", mergeConditions)).append("}");
        }
        query.append(")\n");

        // 构建 SET 子句
        if (properties != null && !properties.isEmpty()) {
            query.append("SET n += $properties\n");
            params.put("properties", properties);
        }

        return new AbstractMap.SimpleEntry<>(query.toString(), params);
    }
}
