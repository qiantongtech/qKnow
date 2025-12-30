package tech.qiantong.qknow.module.ext.extEnum;

public enum ExtTaskConcurrent {
    ALLOW(0),//允许
    FORBID(1); //禁止

    private final Integer type;

    // 构造方法
    private ExtTaskConcurrent(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return this.type;
    }
}
