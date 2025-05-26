package tech.qiantong.qknow.module.ext.extEnum;

/**
 * 抽取任务发布状态
 */
public enum ExtReleaseStatus {
    UNPUBLISHED(0),
    PUBLISHED(1);

    private final Integer status;

    // 构造方法
    private ExtReleaseStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }
}
