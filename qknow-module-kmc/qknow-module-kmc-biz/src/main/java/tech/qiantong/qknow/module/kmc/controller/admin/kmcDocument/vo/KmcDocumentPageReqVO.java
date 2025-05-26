package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

import java.util.List;

/**
 * 知识文件 Request VO 对象 kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Schema(description = "知识文件 Request VO")
@Data
public class KmcDocumentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "知识分类id", example = "")
    private Long categoryId;

    @Schema(description = "知识分类名称", example = "")
    private String categoryName;

    @Schema(description = "文件名称", example = "")
    private String name;

    @Schema(description = "文件标签", example = "")
    private String tagIds;

    @Schema(description = "文件路径", example = "")
    private String path;

    @Schema(description = "文件描述", example = "")
    private String description;

    @Schema(description = "预览次数", example = "")
    private Long previewCount;

    @Schema(description = "下载次数", example = "")
    private Long downloadCount;

    @TableField(exist = false)
    private List<Long> ids ;


}
