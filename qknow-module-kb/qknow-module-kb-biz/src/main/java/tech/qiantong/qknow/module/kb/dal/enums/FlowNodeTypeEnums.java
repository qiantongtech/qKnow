package tech.qiantong.qknow.module.kb.dal.enums;

import java.util.Objects;

/**
 * 流程节点类型枚举
 */
public enum FlowNodeTypeEnums {
    START(1, "start"),
    LLM(2, "llm"),
    REPLY(3, "reply"),
    CONDITION(4, "condition"),
    ;

    private final Integer code;
    private final String name;

    FlowNodeTypeEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据name获取枚举
     *
     * @param name 节点名字
     * @return 节点类型
     */
    public static FlowNodeTypeEnums getByName(String name) {
        for (FlowNodeTypeEnums value : FlowNodeTypeEnums.values()) {
            if (Objects.equals(value.name, name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据 code 获取枚举
     *
     * @param code 节点code
     * @return 节点类型
     */
    public static FlowNodeTypeEnums getByCode(Integer code) {
        for (FlowNodeTypeEnums value : FlowNodeTypeEnums.values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
