package tech.qiantong.qknow.module.kmc.api.kmcCategory.dto;

import lombok.Data;

/**
 * 知识分类 DTO 对象 kmc_category
 *
 * @author qknow
 * @date 2025-02-13
 */
@Data
public class KmcCategoryReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 父级id */
    private Long parentId;

    /** 分类名称 */
    private String name;

    /** 显示顺序 */
    private Long orderNum;

    /** 祖级列表 */
    private String ancestors;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 更新人id */
    private Long updaterId;


}
