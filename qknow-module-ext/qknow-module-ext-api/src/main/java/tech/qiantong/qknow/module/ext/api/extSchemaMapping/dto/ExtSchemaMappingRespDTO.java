package tech.qiantong.qknow.module.ext.api.extSchemaMapping.dto;

import lombok.*;

/**
 * 概念映射 DTO 对象 ext_schema_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
public class ExtSchemaMappingRespDTO {

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

    /** 概念id */
    private Long schemaId;

    /** 概念名称 */
    private String schemaName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
