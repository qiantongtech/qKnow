package tech.qiantong.qknow.neo4j.enums;

import lombok.Getter;

@Getter
public enum Neo4jLabelEnum {
    DYNAMICENTITY( "DynamicEntity", 0, ""), //公共标签
    STRUCTURED( "ExtStruck", 1, "task_id"), // 结构化
    UNSTRUCTURED("ExtUnStruck", 2, "task_id"), // 非结构化
    GRAPHENTITY("GraphEntity", 3, "graph_id"); // 故障

    @Getter
    private final String label;
    private final Integer code;
    private final String entityIdName;

    // 构造方法
    private Neo4jLabelEnum(String label, Integer code, String entityIdName) {
        this.label = label;
        this.code = code;
        this.entityIdName = entityIdName;
    }

    public static Neo4jLabelEnum get(Integer code) {
        for (Neo4jLabelEnum v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(Integer code) {
        return this.code.equals(code);
    }

}
