package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

import java.util.Date;

/**
 * 非结构化抽取任务 Request VO 对象 ext_unstruct_task
 *
 * @author qknow
 * @date 2025-02-18
 */
@Schema(description = "非结构化抽取任务 Request VO")
@Data
public class ExtUnstructTaskPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "任务名称", example = "")
    private String name;

    @Schema(description = "任务状态", example = "")
    private Integer status;

    @Schema(description = "发布状态", example = "")
    private Integer publishStatus;

    @Schema(description = "发布时间", example = "")
    private Date publishTime;

    @Schema(description = "发布人id", example = "")
    private Long publisherId;

    @Schema(description = "发布人", example = "")
    private String publishBy;


}
