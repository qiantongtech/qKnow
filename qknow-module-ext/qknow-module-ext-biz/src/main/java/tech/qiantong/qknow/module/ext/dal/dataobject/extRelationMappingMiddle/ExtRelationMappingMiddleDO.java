package tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 关系映射中间 DO 对象 ext_relation_mapping_middle
 *
 * @author qknow
 * @date 2025-12-16
 */
@Data
@TableName(value = "ext_relation_mapping_middle")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ext_relation_mapping_middle_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtRelationMappingMiddleDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关系表id */
    private Long relationId;

    /** 中间表名称 */
    private String tableName;

    /** 关联源表字段 */
    private String tableField;

    /** 关联目标表字段 */
    private String relationField;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
