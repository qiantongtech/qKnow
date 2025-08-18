package tech.qiantong.qknow.neo4j.wrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import tech.qiantong.qknow.neo4j.utils.LambdaUtils;

import java.util.*;

/**
 * 基础条件
 * @author wang
 * @date 2025/03/26 16:39
 **/
@Getter
public class AbstractWrapper<T> {
    private final Class<T> entityClass;
    private final List<String> conditions = Lists.newArrayList();
    private final Set<String> labels = Sets.newHashSet();
    private final Map<String, Object> params = Maps.newHashMap();
    private String orderBy;
    private int offset;
    private int limit;

    public AbstractWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // 处理多标签查询
    public AbstractWrapper<T> addLabels(String... labels) {
        this.labels.addAll(Arrays.asList(labels));
        return this;
    }

    // 公共条件构建方法
    private <R> AbstractWrapper<T> addCondition(String propertyName, String operator, R value) {
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);
        if ("id".equals(propertyName)) {
            conditions.add("id(n) " + operator + " $" + propertyName);
            params.put(propertyName, value);
        } else {
            conditions.add("n." + prefixName + propertyName + " " + operator + " $" + propertyName);
            params.put(propertyName, value);
        }
        return this;
    }

    // 处理like的特殊情况
    private <R> AbstractWrapper<T> addLikeCondition(String propertyName, R value) {
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);
        if (value instanceof Number) {
            // CONTAINS实现方式
            conditions.add("toString(n." + prefixName + propertyName + ") CONTAINS $" + propertyName);
            params.put(propertyName, String.valueOf(value));
            // 正则实现方式
            //conditions.add("toString(n." + prefixName + propertyName + ") =~ $" + propertyName);
            //params.put(propertyName, ".*" + value + ".*");
        } else {
            // CONTAINS实现方式
            conditions.add("n." + prefixName + propertyName + " CONTAINS $" + propertyName);
            params.put(propertyName, String.valueOf(value));
            // 正则实现方式
            //conditions.add("n." + prefixName + propertyName + " =~ $" + propertyName);
            //params.put(propertyName, ".*" + value + ".*");
        }
        return this;
    }

    // 处理between的特殊情况
    private <R extends Comparable<R>> AbstractWrapper<T> addBetweenCondition(String propertyName, R start, R end) {
        String prefixName = LambdaUtils.processCompositeProperties(this.entityClass);
        String startKey = propertyName + "Start";
        String endKey = propertyName + "End";
        conditions.add("n." + prefixName + propertyName + " >= $" + startKey + " AND n." + prefixName + " <= $" + endKey);
        params.put(startKey, start);
        params.put(endKey, end);
        return this;
    }

    // 以下为Lambda属性方法
    public <R> AbstractWrapper<T> eq(LambdaUtils.PropertyFunction<T, R> propertyFunction, R value) {
        return addCondition(LambdaUtils.getPropertyName(propertyFunction), "=", value);
    }

    public <R> AbstractWrapper<T> like(LambdaUtils.PropertyFunction<T, R> propertyFunction, R value) {
        return addLikeCondition(LambdaUtils.getPropertyName(propertyFunction), value);
    }

    public <R extends Comparable<R>> AbstractWrapper<T> gt(LambdaUtils.PropertyFunction<T, R> propertyFunction, R value) {
        return addCondition(LambdaUtils.getPropertyName(propertyFunction), ">", value);
    }

    public <R extends Comparable<R>> AbstractWrapper<T> lt(LambdaUtils.PropertyFunction<T, R> propertyFunction, R value) {
        return addCondition(LambdaUtils.getPropertyName(propertyFunction), "<", value);
    }

    public <R> AbstractWrapper<T> in(LambdaUtils.PropertyFunction<T, R> propertyFunction, List<R> values) {
        return addCondition(LambdaUtils.getPropertyName(propertyFunction), "IN", values);
    }

    public <R> AbstractWrapper<T> notIn(LambdaUtils.PropertyFunction<T, R> propertyFunction, List<R> values) {
        return addCondition(LambdaUtils.getPropertyName(propertyFunction), "NOT IN", values);
    }

    public <R extends Comparable<R>> AbstractWrapper<T> between(LambdaUtils.PropertyFunction<T, R> propertyFunction, R start, R end) {
        return addBetweenCondition(LambdaUtils.getPropertyName(propertyFunction), start, end);
    }

    // 以下为字符串属性名方法
    public <R> AbstractWrapper<T> eq(String propertyName, R value) {
        return addCondition(propertyName, "=", value);
    }

    public <R> AbstractWrapper<T> like(String propertyName, R value) {
        return addLikeCondition(propertyName, value);
    }

    public <R extends Comparable<R>> AbstractWrapper<T> gt(String propertyName, R value) {
        return addCondition(propertyName, ">", value);
    }

    public <R extends Comparable<R>> AbstractWrapper<T> lt(String propertyName, R value) {
        return addCondition(propertyName, "<", value);
    }

    public <R> AbstractWrapper<T> in(String propertyName, List<R> values) {
        return addCondition(propertyName, "IN", values);
    }

    public <R> AbstractWrapper<T> notIn(String propertyName, List<R> values) {
        return addCondition(propertyName, "NOT IN", values);
    }

    public <R extends Comparable<R>> AbstractWrapper<T> between(String propertyName, R start, R end) {
        return addBetweenCondition(propertyName, start, end);
    }

    public AbstractWrapper<T> orderByAsc(String propertyName) {
        this.orderBy = "n." + propertyName + " ASC";
        return this;
    }

    public AbstractWrapper<T> orderByDesc(String propertyName) {
        this.orderBy = "n." + propertyName + " DESC";
        return this;
    }

    /**
     * 设置分页参数
     * @param offset 当前页码（从1开始）
     * @param limit  每页数量
     */
    public AbstractWrapper<T> page(int offset, int limit) {
        this.offset = (offset - 1) * limit; // 转换为数据库跳过的行数
        this.limit = limit;
        return this;
    }

    protected String getLabel() {
        StringJoiner labelJoiner = new StringJoiner(":", entityClass.getSimpleName(), "");
        labels.forEach(labelJoiner::add);
        return labelJoiner.toString();
    }
}
