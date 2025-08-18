package tech.qiantong.qknow.module.ext.api.unstructTaskRelation.dto;

import lombok.*;

/**
 * 任务文件关联 DTO 对象 ext_unstruct_task_relation
 *
 * @author qknow
 * @date 2025-04-03
 */
@Data
public class ExtUnstructTaskRelationReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 任务id */
    private Long taskId;

    /** 关系id */
    private Long relationId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
