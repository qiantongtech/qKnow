/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.neo4j.wrapper.query;

import tech.qiantong.qknow.neo4j.wrapper.AbstractWrapper;

/**
 * 动态查询，方便neo4j查询方式
 * @author wang
 * @date 2025/02/27 17:44
 **/
public class Neo4jQueryWrapper<T> extends AbstractWrapper<T> {

    public Neo4jQueryWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    /**
     * 根据条件生成基本查询语句
     * @return 查询实体关系语句
     */
    public String getCypherQuery() {
        StringBuilder query = new StringBuilder("MATCH (n:").append(getLabel()).append(")");
        if (!getConditions().isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", getConditions()));
        }
        // 添加 OPTIONAL MATCH 捕获所有关系
        query.append(" OPTIONAL MATCH (n)-[r]->(related)");
        query.append(" RETURN n, collect(r), collect(related)");
        if (getOrderBy() != null) {
            query.append(" ORDER BY ").append(getOrderBy());
        }
        if (getLimit() > 0) {
            query.append(" SKIP ").append(getOffset()).append(" LIMIT ").append(getLimit());
        }
        return query.toString();
    }

    /**
     * 根据条件生成count语句
     * @return 查询语句
     */
    public String getCypherCountQuery() {
        StringBuilder query = new StringBuilder("MATCH (n:").append(getLabel()).append(")");
        if (!getConditions().isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", getConditions()));
        }
        query.append(" RETURN count(n)");
        return query.toString();
    }

}
