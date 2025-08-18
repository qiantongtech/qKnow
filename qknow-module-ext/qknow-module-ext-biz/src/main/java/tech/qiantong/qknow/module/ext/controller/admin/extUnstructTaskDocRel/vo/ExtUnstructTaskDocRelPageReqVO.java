package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 任务文件关联 Request VO 对象 ext_unstruct_task_doc_rel
 *
 * @author qknow
 * @date 2025-02-19
 */
@Schema(description = "任务文件关联 Request VO")
@Data
public class ExtUnstructTaskDocRelPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "文件id", example = "")
    private Long docId;

    @Schema(description = "文件名", example = "")
    private String docName;

    private Boolean delFlag;




}
