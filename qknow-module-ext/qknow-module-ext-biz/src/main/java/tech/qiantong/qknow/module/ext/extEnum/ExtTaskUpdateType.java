package tech.qiantong.qknow.module.ext.extEnum;

public enum ExtTaskUpdateType {
    FULLUPDATE(0),//全量更新
    INCREMENTUDATE(1); //增量更新

    private final Integer type;

    // 构造方法
    private ExtTaskUpdateType(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return this.type;
    }
}
