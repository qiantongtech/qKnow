package tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 概念属性 创建/修改 Request VO ext_schema_attribute
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "概念属性 Response VO")
@Data
public class ExtSchemaAttributeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
//    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "概念id", example = "")
    @NotNull(message = "概念id不能为空")
    private Long schemaId;

    @Schema(description = "概念名称", example = "")
    @NotBlank(message = "概念名称不能为空")
    @Size(max = 256, message = "概念名称长度不能超过256个字符")
    private String schemaName;

    @Schema(description = "属性名称", example = "")
    @NotBlank(message = "属性名称不能为空")
    @Size(max = 256, message = "属性名称长度不能超过256个字符")
    private String name;

    @Schema(description = "属性名称代码", example = "")
    @NotBlank(message = "属性名称代码不能为空")
    @Size(max = 256, message = "属性名称代码长度不能超过256个字符")
    private String nameCode;

    @Schema(description = "是否必填", example = "")
    @NotNull(message = "是否必填不能为空")
    private Integer requireFlag;

    @Schema(description = "数据类型", example = "")
    @NotNull(message = "数据类型不能为空")
    private Integer dataType;

    @Schema(description = "单/多值", example = "")
    @NotNull(message = "单/多值不能为空")
    private Integer multipleFlag;

    @Schema(description = "校验方式", example = "")
    private Integer validateType;

    @Schema(description = "最小值", example = "")
    private Long minValue;

    @Schema(description = "最大值", example = "")
    private Long maxValue;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
