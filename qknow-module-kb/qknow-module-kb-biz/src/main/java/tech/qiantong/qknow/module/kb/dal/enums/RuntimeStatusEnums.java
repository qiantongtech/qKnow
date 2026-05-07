package tech.qiantong.qknow.module.kb.dal.enums;

/**
 * 运行状态枚举
 */
public enum RuntimeStatusEnums {
    RUNNING(0),
    SUCCESS(1),
    ERROR(2);

    private final Integer code;

    RuntimeStatusEnums(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
