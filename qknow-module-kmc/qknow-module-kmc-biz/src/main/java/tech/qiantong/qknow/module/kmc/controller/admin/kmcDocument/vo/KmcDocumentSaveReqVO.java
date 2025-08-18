package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 知识文件 创建/修改 Request VO kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KmcDocumentSaveReqVO extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "知识分类id", example = "")
    @NotNull(message = "知识分类id不能为空")
    private Long categoryId;

    @Schema(description = "知识分类名称", example = "")
    @Size(max = 256, message = "知识分类名称长度不能超过256个字符")
    private String categoryName;

    @Schema(description = "文件名称", example = "")
    @NotBlank(message = "文件名称不能为空")
    @Size(max = 256, message = "文件名称长度不能超过256个字符")
    private String name;

    @Schema(description = "文件路径", example = "")
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 256, message = "文件路径长度不能超过256个字符")
    private String path;

    @Schema(description = "文件描述", example = "")
    @Size(max = 256, message = "文件描述长度不能超过256个字符")
    private String description;

    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;
}
