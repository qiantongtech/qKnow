package tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 关系配置 DO 对象 ext_schema_relation
 *
 * @author qknow
 * @date 2025-02-18
 */
@Data
@TableName(value = "ext_schema_relation")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ext_schema_relation_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtSchemaRelationDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Boolean delFlag;


}
