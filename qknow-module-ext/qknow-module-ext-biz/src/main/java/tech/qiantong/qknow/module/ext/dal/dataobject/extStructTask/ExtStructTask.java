/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

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
    private Long graphId;
    private Integer updateType;// 更新类型
    private String updateFrequency; // 更新频率
    private Integer updateStatus; //定时更新状态
    private List<Relationship> relationshipList;// 数据映射表内容

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
        private String primaryKey;// 主键
        private String entityTimeField;// 实体时间字段
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
        //        private String field;//列
//        private String relation;// 关系
//        private String associationTable;// 关联表
//        private String associationTableField;// 关联列
//        private String associationTableEntityField;// 关联实体名称列
        private String tableName; //源表
        private String fieldName;//源表字段名
        private String relation;// 关系
        private String relationTable;// 关联表
        private String relationField;// 关联列
        private String relationNameField;// 关联实体名称列

        private List<RelationMappingMiddle> relationMappingMiddle;


    }

    /**
     * 关系中间表映射
     */
    @Data
    public static class RelationMappingMiddle {
        private Long relationId;// 关系表id
        private String tableName;// 中间表名称
        private String tableField; //关联源表字段
        private String relationField;//关联目标表字段



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
