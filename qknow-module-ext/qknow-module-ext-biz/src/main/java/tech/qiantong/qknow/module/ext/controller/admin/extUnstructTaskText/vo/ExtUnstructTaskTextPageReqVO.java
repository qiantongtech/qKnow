package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 任务文件段落关联 Request VO 对象 ext_unstruct_task_text
 *
 * @author qknow
 * @date 2025-02-21
 */
@Schema(description = "任务文件段落关联 Request VO")
@Data
public class ExtUnstructTaskTextPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "文件id", example = "")
    private Long docId;

    @Schema(description = "段落标识", example = "")
    private Long paragraphIndex;

    @Schema(description = "文字内容", example = "")
    private String text;




}
