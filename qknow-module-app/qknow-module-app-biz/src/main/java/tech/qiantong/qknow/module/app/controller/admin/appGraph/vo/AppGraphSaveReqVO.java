package tech.qiantong.qknow.module.app.controller.admin.appGraph.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * 概念配置 创建/修改 Request VO ext_schema
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "故障树配置 Response VO")
@Data
public class AppGraphSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "图谱id", example = "")
    private Long entityId;

    @Schema(description = "实体名称", example = "")
    @Size(max = 256, message = "实体名称长度不能超过256个字符")
    private String name;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
