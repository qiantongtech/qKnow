package tech.qiantong.qknow.module.app.controller.admin.appGraph.vo;

import lombok.Data;

@Data
public class AppGraphVO {
    private Long entityId;//任务id
    private Integer entityType;//任务类型 1结构化 2非结构化 3故障
    private Integer releaseStatus;//发布状态 0未发布 1已发布
}
