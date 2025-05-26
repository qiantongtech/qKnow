package tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 自定义映射 Request VO 对象 ext_custom_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "自定义映射 Request VO")
@Data
public class ExtCustomMappingPageReqVO extends PageParam {

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

    @Schema(description = "sql语句", example = "")
    private String sqlStatement;




}
