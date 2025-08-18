package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 任务文件段落关联 创建/修改 Request VO ext_unstruct_task_text
 *
 * @author qknow
 * @date 2025-02-21
 */
@Schema(description = "任务文件段落关联 Response VO")
@Data
public class ExtUnstructTaskTextSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "文件id", example = "")
    @NotNull(message = "文件id不能为空")
    private Long docId;

    @Schema(description = "段落标识", example = "")
    @NotNull(message = "段落标识不能为空")
    private Long paragraphIndex;

    @Schema(description = "文字内容", example = "")
    @NotBlank(message = "文字内容不能为空")
    @Size(max = 256, message = "文字内容长度不能超过256个字符")
    private String text;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
