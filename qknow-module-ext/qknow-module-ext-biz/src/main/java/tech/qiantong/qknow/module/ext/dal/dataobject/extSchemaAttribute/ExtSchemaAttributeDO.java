package tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 概念属性 DO 对象 ext_schema_attribute
 *
 * @author qknow
 * @date 2025-02-17
 */
@Data
@TableName(value = "ext_schema_attribute")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ext_schema_attribute_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtSchemaAttributeDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 概念id */
    private Long schemaId;

    /** 概念名称 */
    private String schemaName;
    /** 属性名称（中文） */
    private String name;

    /** 属性名称代码 */
    private String nameCode;

    /** 是否必填 */
    private Integer requireFlag;

    /** 数据类型 */
    private Integer dataType;

    /** 单/多值 */
    private Integer multipleFlag;

    /** 校验方式 */
    private Integer validateType;

    /** 最小值（用于区间校验） */
    private Long minValue;

    /** 最大值（用于区间校验） */
    private Long maxValue;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
