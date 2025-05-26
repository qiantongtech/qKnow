package tech.qiantong.qknow.neo4j.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.neo4j.driver.Value;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 图谱转换器
 * @author wang
 * @date 2025/03/11 17:50
 **/
public class Convert {
    public static JSONObject toJSONObject(List<?> entityList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray entities = new JSONArray(); // 实体
            JSONArray relationships = new JSONArray(); // 关系
            for (Object entity : entityList) {
                // 处理实体信息
                JSONObject entityJson = new JSONObject();
                Field idField = getField(entity.getClass(), "id");
                assert idField != null;
                idField.setAccessible(true);
                Object id = idField.get(entity);
                entityJson.put("id", id);

                Field nameField = getField(entity.getClass(), "name");
                if (nameField != null) {
                    nameField.setAccessible(true);
                    Object name = nameField.get(entity);
                    entityJson.put("name", name);
                }
                entities.add(entityJson);

                // 处理关系信息
                Field relationshipMapField = getField(entity.getClass(), "relationshipEntityMap");
                if (relationshipMapField != null) {
                    relationshipMapField.setAccessible(true);
                    Map<String, List<?>> relationshipMap = (Map<String, List<?>>) relationshipMapField.get(entity);
                    if (relationshipMap != null) {
                        for (Map.Entry<String, List<?>> entry : relationshipMap.entrySet()) {
                            String relationshipName = entry.getKey();
                            for (Object relObject : entry.getValue()) {
                                // 直接处理GraphEntityRelationship对象
                                Field endNodeField = getField(relObject.getClass(), "endNode");
                                if (endNodeField != null) {
                                    endNodeField.setAccessible(true);
                                    Object endNode = endNodeField.get(relObject);
                                    Field relationshipId = getField(relObject.getClass(), "id");
                                    assert relationshipId != null;
                                    relationshipId.setAccessible(true);
                                    JSONObject relationshipJson = new JSONObject();
                                    relationshipJson.put("id", relationshipId.get(relObject));
                                    relationshipJson.put("startId", id);
                                    if (nameField != null) {
                                        relationshipJson.put("startName", nameField.get(entity));
                                    }

                                    if (endNode != null) {
                                        Field endIdField = getField(endNode.getClass(), "id");
                                        endIdField.setAccessible(true);
                                        Long endId = (Long) endIdField.get(endNode);
                                        relationshipJson.put("endId", endId);

                                        Field endNameField = getField(endNode.getClass(), "name");
                                        if (endNameField != null) {
                                            endNameField.setAccessible(true);
                                            relationshipJson.put("endName", endNameField.get(endNode));
                                        }
                                    }
                                    relationshipJson.put("relationType", relationshipName);
                                    relationships.add(relationshipJson);
                                }
                            }
                        }
                    }
                }

            }
            jsonObject.put("entities", entities);
            jsonObject.put("relationships", relationships);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public static JSONObject toDynamicEntityJSONObject(List<?> entityList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray entities = new JSONArray(); // 实体
            JSONArray relationships = new JSONArray(); // 关系
            for (Object entity : entityList) {
                // 处理实体信息
                JSONObject entityJson = new JSONObject();
                Field idField = getField(entity.getClass(), "id");
                assert idField != null;
                idField.setAccessible(true);
                Object id = idField.get(entity);
                entityJson.put("id", id);

                Field dynamicPropertiesField = getField(entity.getClass(), "dynamicProperties");
                if (dynamicPropertiesField != null) {
                    dynamicPropertiesField.setAccessible(true);
                    // 对 dynamicProperties 中的键进行转换并添加到 entityJson 中
                    Map<String, Object> dynamicProperties = (Map<String, Object>) dynamicPropertiesField.get(entity);
                    for (Map.Entry<String, Object> entry : dynamicProperties.entrySet()) {
                        String snakeKey = entry.getKey();
                        String camelKey = snakeToCamel(snakeKey);
                        Value value = (Value) entry.getValue();
                        entityJson.put(camelKey, value.asObject());
                    }
                }
                entities.add(entityJson);

                // 处理关系信息
                Field relationshipMapField = getField(entity.getClass(), "relationshipEntityMap");
                if (relationshipMapField != null) {
                    relationshipMapField.setAccessible(true);
                    Map<String, List<?>> relationshipMap = (Map<String, List<?>>) relationshipMapField.get(entity);
                    if (relationshipMap != null) {
                        for (Map.Entry<String, List<?>> entry : relationshipMap.entrySet()) {
                            String relationshipName = entry.getKey();
                            for (Object relObject : entry.getValue()) {
                                // 直接处理GraphEntityRelationship对象
                                Field endNodeField = getField(relObject.getClass(), "endNode");
                                if (endNodeField != null) {
                                    endNodeField.setAccessible(true);
                                    Object endNode = endNodeField.get(relObject);
                                    Field relationshipId = getField(relObject.getClass(), "id");
                                    assert relationshipId != null;
                                    relationshipId.setAccessible(true);
                                    JSONObject relationshipJson = new JSONObject();
                                    relationshipJson.put("id", relationshipId.get(relObject));
                                    relationshipJson.put("startId", id);
                                    relationshipJson.put("startName", entityJson.get("name"));

                                    if (endNode != null) {
                                        Field endIdField = getField(endNode.getClass(), "id");
                                        endIdField.setAccessible(true);
                                        Long endId = (Long) endIdField.get(endNode);
                                        relationshipJson.put("endId", endId);

                                        Field endNameField = getField(endNode.getClass(), "dynamicProperties");
                                        if (endNameField != null) {
                                            endNameField.setAccessible(true);
                                            Map<String, Object> map = (Map<String, Object>) endNameField.get(endNode);
                                            Value name = (Value) map.get("name");
                                            relationshipJson.put("endName", name.asObject());
                                        }
                                    }
                                    relationshipJson.put("relationType", relationshipName);
                                    relationships.add(relationshipJson);
                                }
                            }
                        }
                    }
                }

            }
            jsonObject.put("entities", entities);
            jsonObject.put("relationships", relationships);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    // 将驼峰式命名转换为下划线分隔的命名
    public static String camelToSnake(String camelStr) {
        StringBuilder snakeStr = new StringBuilder();
        for (int i = 0; i < camelStr.length(); i++) {
            char c = camelStr.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                snakeStr.append("_");
            }
            snakeStr.append(Character.toLowerCase(c));
        }
        return snakeStr.toString();
    }

    // 将下划线分隔的命名转换为驼峰式命名
    public static String snakeToCamel(String snakeStr) {
        String[] components = snakeStr.split("_");
        StringBuilder camelCase = new StringBuilder(components[0]);
        for (int i = 1; i < components.length; i++) {
            String component = components[i];
            camelCase.append(Character.toUpperCase(component.charAt(0)));
            camelCase.append(component.substring(1));
        }
        return camelCase.toString();
    }
}
