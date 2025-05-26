package tech.qiantong.qknow.module.ext.api.extRelationMapping.dto;

import lombok.*;

/**
 * 关系映射 DTO 对象 ext_relation_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
public class ExtRelationMappingRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 任务id */
    private Long taskId;

    /** 表名 */
    private String tableName;

    /** 表显示名称 */
    private String tableComment;

    /** 字段名 */
    private String fieldName;

    /** 字段显示名称 */
    private String fieldComment;

    /** 关系 */
    private String relation;

    /** 关联表 */
    private String relationTable;

    /** 关联表字段 */
    private String relationField;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
