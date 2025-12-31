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

package tech.qiantong.qknow.neo4j.wrapper.query;

import org.springframework.data.neo4j.core.schema.Relationship;
import tech.qiantong.qknow.neo4j.wrapper.AbstractWrapper;

/**
 * 动态关系链查询，方便neo4j查询方式
 * @author wang
 * @date 2025/02/27 17:44
 **/
public class Neo4jRelationChainCypherWrapper<T> extends AbstractWrapper<T> {

    public Neo4jRelationChainCypherWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    private int startNode = 0; // 起始节点
    private int endNode = 5; // 结束节点
    private Relationship.Direction direction; // 结束节点

    public AbstractWrapper<T> pathStartNode(Integer startNode) {
        this.startNode = startNode;
        return this;
    }

    public AbstractWrapper<T> pathEndNode(Integer endNode) {
        this.endNode = endNode;
        return this;
    }

    public AbstractWrapper<T> pathDirection(Relationship.Direction direction) {
        this.direction = direction;
        return this;
    }

    /**
     * 根据条件生成关系链查询语句
     * @return 查询语句
     */
    public String getCypherQuery() {
        String startDirection = "";
        String endDirection = "";
        StringBuilder query = new StringBuilder("MATCH (n:").append(getLabel()).append(")");
        if (!getConditions().isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", getConditions()));
        }
        if (Relationship.Direction.OUTGOING.equals(direction)) {
            endDirection = ">";
        }

        if (Relationship.Direction.INCOMING.equals(direction)) {
            startDirection = "<";
        }

        query.append(" OPTIONAL MATCH pathPattern = (n)")
                .append(startDirection).append("-[")
                .append(this.startNode)
                .append(".. ")
                .append(this.endNode)
                .append("]-")
                .append(endDirection)
                .append("(pathEndNode)")
                .append(" WITH pathPattern")
                .append(" UNWIND nodes(pathPattern) AS currentNode")
                .append(" OPTIONAL MATCH (currentNode)-[r]->(related)")
                .append(" WITH currentNode, r, related")
                .append(" RETURN currentNode, collect(r), collect(related)");
        return query.toString();
    }
}
