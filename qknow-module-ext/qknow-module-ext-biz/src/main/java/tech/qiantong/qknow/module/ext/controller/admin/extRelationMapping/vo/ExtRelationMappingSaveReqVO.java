package tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 关系映射 创建/修改 Request VO ext_relation_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "关系映射 Response VO")
@Data
public class ExtRelationMappingSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "表名", example = "")
    @NotBlank(message = "表名不能为空")
    @Size(max = 256, message = "表名长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    @Size(max = 256, message = "表显示名称长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "字段名", example = "")
    @NotBlank(message = "字段名不能为空")
    @Size(max = 256, message = "字段名长度不能超过256个字符")
    private String fieldName;

    @Schema(description = "字段显示名称", example = "")
    @Size(max = 256, message = "字段显示名称长度不能超过256个字符")
    private String fieldComment;

    @Schema(description = "关系", example = "")
    @NotBlank(message = "关系不能为空")
    @Size(max = 256, message = "关系长度不能超过256个字符")
    private String relation;

    @Schema(description = "关联表", example = "")
    @Size(max = 256, message = "关联表长度不能超过256个字符")
    private String relationTable;

    @Size(max = 256, message = "关联表名称长度不能超过256个字符")
    @Schema(description = "关联表名称", example = "")
    private String relationTableName;

    @Schema(description = "关联表字段", example = "")
    @Size(max = 256, message = "关联表字段长度不能超过256个字符")
    private String relationField;

    @Schema(description = "关联表实体字段", example = "")
    @Size(max = 256, message = "关联表字段长度不能超过256个字符")
    private String relationNameField;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
