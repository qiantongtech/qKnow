package tech.qiantong.qknow.module.app.controller.admin.appGraph.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.qiantong.qknow.common.core.page.PageParam;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppGraphPageReqVO extends PageParam {
    private Long entityId;//任务id

    private String name; // 名称

    private Integer entityType;//任务类型 1结构化 2非结构化 3故障
    private Integer releaseStatus;//发布状态 0未发布 1已发布
}
