package tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 结构化抽取任务 创建/修改 Request VO ext_struct_task
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "结构化抽取任务 Response VO")
@Data
public class ExtStructTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "任务名称", example = "")
    @NotBlank(message = "任务名称不能为空")
    @Size(max = 256, message = "任务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "任务状态", example = "")
    @NotNull(message = "任务状态不能为空")
    private Integer status;

    @Schema(description = "发布状态", example = "")
    @NotNull(message = "发布状态不能为空")
    private Integer publishStatus;

    @Schema(description = "发布时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @Schema(description = "发布人id", example = "")
    @NotNull(message = "发布人id不能为空")
    private Long publisherId;

    @Schema(description = "发布人", example = "")
    @Size(max = 256, message = "发布人长度不能超过256个字符")
    private String publishBy;

    @Schema(description = "数据源id", example = "")
    @NotNull(message = "数据源id不能为空")
    private Long datasourceId;

    @Schema(description = "数据源名称", example = "")
    @NotBlank(message = "数据源名称不能为空")
    @Size(max = 256, message = "数据源名称长度不能超过256个字符")
    private String datasourceName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
