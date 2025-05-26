package tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 属性映射 DO 对象 ext_attribute_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
@TableName(value = "ext_attribute_mapping")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ext_attribute_mapping_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtAttributeMappingDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 任务id */
    private Long taskId;

    /** 表名 */
    private String tableName;

    /** 表显示名称 */
    private String tableComment;

    /** 字段名 */
    private String fieldName;

    /** 字段显示名称 */
    private String fieldComment;

    /** 属性id */
    private Long attributeId;

    /** 属性名称 */
    private String attributeName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
