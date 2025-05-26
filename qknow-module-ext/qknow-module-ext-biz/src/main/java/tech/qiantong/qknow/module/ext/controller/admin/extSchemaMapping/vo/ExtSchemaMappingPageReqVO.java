package tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 概念映射 Request VO 对象 ext_schema_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "概念映射 Request VO")
@Data
public class ExtSchemaMappingPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "表名", example = "")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    private String tableComment;

    @Schema(description = "概念id", example = "")
    private Long schemaId;

    @Schema(description = "概念名称", example = "")
    private String schemaName;




}
