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
