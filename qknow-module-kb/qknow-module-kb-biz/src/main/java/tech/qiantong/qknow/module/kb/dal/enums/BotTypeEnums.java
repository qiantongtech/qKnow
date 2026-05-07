package tech.qiantong.qknow.module.kb.dal.enums;

import java.util.Objects;

/**
 * bot 类型枚举
 */
public enum BotTypeEnums {
    WORK_FLOW(0),
    CHAT_FLOW(1),
    AGENT(2);

    private final Integer code;

    BotTypeEnums(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据code返回枚举
     *
     * @param code code
     * @return 枚举类型
     */
    public static BotTypeEnums get(Integer code) {
        for (BotTypeEnums v : values()) {
            if (Objects.equals(v.getCode(), code)) {
                return v;
            }
        }
        return null;
    }
}
