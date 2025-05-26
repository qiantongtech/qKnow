package tech.qiantong.qknow.module.ext.api.extUnstructTaskDocRel.dto;

import lombok.*;

/**
 * 任务文件关联 DTO 对象 ext_unstruct_task_doc_rel
 *
 * @author qknow
 * @date 2025-02-19
 */
@Data
public class ExtUnstructTaskDocRelReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 任务id */
    private Long taskId;

    /** 文件id */
    private Long docId;

    /** 文件名 */
    private String docName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
