package tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 关系映射 Request VO 对象 ext_relation_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "关系映射 Request VO")
@Data
public class ExtRelationMappingPageReqVO extends PageParam {

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

    @Schema(description = "字段名", example = "")
    private String fieldName;

    @Schema(description = "字段显示名称", example = "")
    private String fieldComment;

    @Schema(description = "关系", example = "")
    private String relation;

    @Schema(description = "关联表", example = "")
    private String relationTable;

    @Schema(description = "关联表名称", example = "")
    private String relationTableName;

    @Schema(description = "关联表字段", example = "")
    private String relationField;

    @Schema(description = "关联表实体字段", example = "")
    private String relationNameField;

}
