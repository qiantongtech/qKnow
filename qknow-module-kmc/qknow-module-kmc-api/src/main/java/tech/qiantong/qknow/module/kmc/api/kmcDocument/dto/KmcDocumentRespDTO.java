package tech.qiantong.qknow.module.kmc.api.kmcDocument.dto;

import lombok.Data;

/**
 * 知识文件 DTO 对象 kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Data
public class KmcDocumentRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 知识分类id */
    private Long categoryId;

    /** 知识分类名称 */
    private String categoryName;

    /** 文件名称 */
    private String name;

    /** 文件路径 */
    private String path;

    /** 文件描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 更新人id */
    private Long updaterId;


}
