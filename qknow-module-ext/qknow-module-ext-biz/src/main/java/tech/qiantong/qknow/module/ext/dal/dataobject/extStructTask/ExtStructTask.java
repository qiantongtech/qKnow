package tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask;

import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 结构化抽取
 */
@Data
public class ExtStructTask extends BaseEntity {

    private Long workspaceId;
    private Long taskId;// 任务id,修改的时候传
    private String taskName;// 任务名称
    private String remark;// 任务备注
    private Long dataSourceId;// 数据库id
    private List<TableData> tableData;// 数据映射表内容

    /**
     * 数据映射表内容
     */
    @Data
    public static class TableData {
        private String tableName;// 表名
        private String tableComment;// 表显示名称
        private String operate;// 对应概念
        private String status;// 状态 0:未映射 1:已映射
        private MappingData mappingData;// 映射数据
    }

    /**
     * 映射数据
     */
    @Data
    public static class MappingData {
        private Long dataSourceId;// 数据库id
        private Long concept;// 概念id
        private String conceptName;// 概念名称
        private String tableName;// 表名
        private String entityNameField;// 实体名称字段
        private List<Attribute> attributeList;// 属性映射
        private List<Relationship> relationshipList;// 关系映射
        private List<Custom> customList;// 自定义映射
    }

    /**
     * 属性映射
     */
    @Data
    public static class Attribute {
        private String field;// 列
        private String fieldDescription;// 列描述
        private Long conceptId;// 属性id
        private String conceptName;// 属性名称
    }

    /**
     * 关系映射
     */
    @Data
    public static class Relationship {
        private Long id;// 关系id
        private String field;//列
        private String relation;// 关系
        private String associationTable;// 关联表
        private String associationTableField;// 关联列
        private String associationTableEntityField;// 关联实体名称列
//        private String associationTableDetail;
    }

    /**
     * 自定义映射
     */
    @Data
    public static class Custom {
        private String field;// 列
        private String customSQL;// 自定义sql
    }

}
