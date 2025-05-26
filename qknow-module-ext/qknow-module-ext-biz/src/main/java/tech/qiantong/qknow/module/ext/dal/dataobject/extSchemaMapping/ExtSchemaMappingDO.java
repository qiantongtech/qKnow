package tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 概念映射 DO 对象 ext_schema_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
@TableName(value = "ext_schema_mapping")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("ext_schema_mapping_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtSchemaMappingDO extends BaseEntity {
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

    /** 实体名称字段 */
    private String entityNameField;

    /** 概念id */
    private Long schemaId;

    /** 概念名称 */
    private String schemaName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
