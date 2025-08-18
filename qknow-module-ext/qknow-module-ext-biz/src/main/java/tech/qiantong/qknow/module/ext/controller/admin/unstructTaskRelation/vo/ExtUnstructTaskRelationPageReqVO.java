package tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 任务文件关联 Request VO 对象 ext_unstruct_task_relation
 *
 * @author qknow
 * @date 2025-04-03
 */
@Schema(description = "任务文件关联 Request VO")
@Data
public class ExtUnstructTaskRelationPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "关系id", example = "")
    private Long relationId;




}
