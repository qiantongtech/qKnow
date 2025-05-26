package tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 知识分类 Request VO 对象 kmc_category
 *
 * @author qknow
 * @date 2025-02-13
 */
@Schema(description = "知识分类 Request VO")
@Data
public class KmcCategoryPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "父级id", example = "")
    private Long parentId;

    @Schema(description = "分类名称", example = "")
    private String name;

    @Schema(description = "显示顺序", example = "")
    private Long orderNum;

    @Schema(description = "祖级列表", example = "")
    private String ancestors;



    @Schema(description = "更新人id", example = "")
    private Long updaterId;


}
