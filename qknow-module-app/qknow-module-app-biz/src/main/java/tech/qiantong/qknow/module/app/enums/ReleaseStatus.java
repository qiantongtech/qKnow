package tech.qiantong.qknow.module.app.enums;

/**
 * 发布状态
 */
public enum ReleaseStatus {
    UNPUBLISHED(0),//未发布
    PUBLISHED(1);//已发布

    private final Integer status;

    // 构造方法
    private ReleaseStatus(Integer status) {
        this.status = status;
    }

    public Integer getValue() {
        return this.status;
    }
}
