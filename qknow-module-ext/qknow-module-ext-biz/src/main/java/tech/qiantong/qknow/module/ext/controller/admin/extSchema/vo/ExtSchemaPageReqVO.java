package tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 概念配置 Request VO 对象 ext_schema
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "概念配置 Request VO")
@Data
public class ExtSchemaPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "概念名称", example = "")
    private String name;

    @Schema(description = "概念颜色", example = "")
    private String color;

    @Schema(description = "概念描述", example = "")
    private String description;

    /** 用于精确查询 */
    @TableField(exist = false)
    private String exactName;




}
