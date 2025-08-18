package tech.qiantong.qknow.module.ext.extEnum;

import java.util.Objects;

public enum UnstructTypeEnums {
    DEEPKE("deepke", "deepke"),
    MODEL("model", "模型");
    public final String code;
    public final String info;

    UnstructTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static UnstructTypeEnums get(String code) {
        for (UnstructTypeEnums v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    // 根据code返回县市名称
    public static UnstructTypeEnums getName(String info) {
        for (UnstructTypeEnums v : values()) {
            if (v.like(info)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(String code) {
        return this.code.equals(code);
    }

    public boolean like(String info) {
        return this.info.equals(info);
    }

    public static String getInfo(String code) {
        return Objects.requireNonNull(UnstructTypeEnums.get(code)).getInfo();
    }

    public static String getCode(String info) {
        return Objects.requireNonNull(UnstructTypeEnums.getName(info)).getCode();
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
