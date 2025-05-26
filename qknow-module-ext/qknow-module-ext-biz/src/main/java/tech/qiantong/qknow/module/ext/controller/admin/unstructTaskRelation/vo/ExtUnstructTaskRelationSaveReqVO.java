package tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 任务文件关联 创建/修改 Request VO ext_unstruct_task_relation
 *
 * @author qknow
 * @date 2025-04-03
 */
@Schema(description = "任务文件关联 Response VO")
@Data
public class ExtUnstructTaskRelationSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "关系id", example = "")
    @NotNull(message = "关系id不能为空")
    private Long relationId;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
