package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识文件 Response VO 对象 kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KmcDocumentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "知识分类id")
    @Schema(description = "知识分类id", example = "")
    private Long categoryId;

    @Excel(name = "知识分类名称")
    @Schema(description = "知识分类名称", example = "")
    private String categoryName;

    @Excel(name = "文件名称")
    @Schema(description = "文件名称", example = "")
    private String name;

    @Schema(description = "文件标签", example = "")
    private String tagIds;

    @Excel(name = "文件路径")
    @Schema(description = "文件路径", example = "")
    private String path;

    @Excel(name = "文件描述")
    @Schema(description = "文件描述", example = "")
    private String description;

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

    @Excel(name = "预览次数")
    @Schema(description = "预览次数", example = "")
    private Long previewCount;

    @Excel(name = "下载次数")
    @Schema(description = "下载次数", example = "")
    private Long downloadCount;

}
