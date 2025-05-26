package tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 概念属性 Request VO 对象 ext_schema_attribute
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "概念属性 Request VO")
@Data
public class ExtSchemaAttributePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "概念id", example = "")
    private Long schemaId;

    @Schema(description = "概念名称", example = "")
    private String schemaName;

    @Schema(description = "属性名称", example = "")
    private String name;

    @Schema(description = "属性名称代码", example = "")
    private String nameCode;

    @Schema(description = "是否必填", example = "")
    private Integer requireFlag;

    @Schema(description = "数据类型", example = "")
    private Integer dataType;

    @Schema(description = "单/多值", example = "")
    private Integer multipleFlag;

    @Schema(description = "校验方式", example = "")
    private Integer validateType;

    @Schema(description = "最小值", example = "")
    private Long minValue;

    @Schema(description = "最大值", example = "")
    private Long maxValue;




}
