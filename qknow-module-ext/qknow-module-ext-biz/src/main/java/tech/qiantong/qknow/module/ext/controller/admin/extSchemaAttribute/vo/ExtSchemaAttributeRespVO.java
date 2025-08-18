package tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qknow.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * 概念属性 Response VO 对象 ext_schema_attribute
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "概念属性 Response VO")
@Data
public class ExtSchemaAttributeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "概念id")
    @Schema(description = "概念id", example = "")
    private Long schemaId;

    @Excel(name = "概念名称")
    @Schema(description = "概念名称", example = "")
    private String schemaName;

    @Excel(name = "属性名称")
    @Schema(description = "属性名称", example = "")
    private String name;

    @Excel(name = "属性名称代码")
    @Schema(description = "属性名称代码", example = "")
    private String nameCode;

    @Excel(name = "是否必填")
    @Schema(description = "是否必填", example = "")
    private Integer requireFlag;

    @Excel(name = "数据类型")
    @Schema(description = "数据类型", example = "")
    private Integer dataType;

    @Excel(name = "单/多值")
    @Schema(description = "单/多值", example = "")
    private Integer multipleFlag;

    @Excel(name = "校验方式")
    @Schema(description = "校验方式", example = "")
    private Integer validateType;

    @Excel(name = "最小值", readConverterExp = "用=于区间校验")
    @Schema(description = "最小值", example = "")
    private Long minValue;

    @Excel(name = "最大值", readConverterExp = "用=于区间校验")
    @Schema(description = "最大值", example = "")
    private Long maxValue;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
