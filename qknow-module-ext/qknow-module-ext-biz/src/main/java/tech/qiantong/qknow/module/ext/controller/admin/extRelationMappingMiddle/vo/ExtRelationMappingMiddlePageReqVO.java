package tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 关系映射中间 Request VO 对象 ext_relation_mapping_middle
 *
 * @author qknow
 * @date 2025-12-16
 */
@Schema(description = "关系映射中间 Request VO")
@Data
public class ExtRelationMappingMiddlePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "关系表id", example = "")
    private Long relationId;

    @Schema(description = "中间表名称", example = "")
    private String tableName;

    @Schema(description = "关联源表字段", example = "")
    private String tableField;

    @Schema(description = "关联目标表字段", example = "")
    private String relationField;




}
