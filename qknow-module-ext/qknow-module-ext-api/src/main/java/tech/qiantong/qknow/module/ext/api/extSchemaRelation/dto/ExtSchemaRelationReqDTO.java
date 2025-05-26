package tech.qiantong.qknow.module.ext.api.extSchemaRelation.dto;

import lombok.*;

/**
 * 关系配置 DTO 对象 ext_schema_relation
 *
 * @author qknow
 * @date 2025-02-18
 */
@Data
public class ExtSchemaRelationReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 起点概念id */
    private Long startSchemaId;

    /** 关系 */
    private String relation;

    /** 终点概念id */
    private Long endSchemaId;

    /** 是否可逆 */
    private Integer inverseFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
