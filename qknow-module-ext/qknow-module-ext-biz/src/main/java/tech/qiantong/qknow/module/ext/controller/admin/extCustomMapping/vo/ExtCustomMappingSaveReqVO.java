package tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 自定义映射 创建/修改 Request VO ext_custom_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "自定义映射 Response VO")
@Data
public class ExtCustomMappingSaveReqVO extends BaseEntity {

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

    @Schema(description = "sql语句", example = "")
    @Size(max = 256, message = "sql语句长度不能超过256个字符")
    private String sqlStatement;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
