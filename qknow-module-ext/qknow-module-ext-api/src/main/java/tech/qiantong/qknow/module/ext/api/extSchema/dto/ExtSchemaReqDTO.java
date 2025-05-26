package tech.qiantong.qknow.module.ext.api.extSchema.dto;

import lombok.*;

/**
 * 概念配置 DTO 对象 ext_schema
 *
 * @author qknow
 * @date 2025-02-17
 */
@Data
public class ExtSchemaReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 概念名称 */
    private String name;

    /** 概念描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
