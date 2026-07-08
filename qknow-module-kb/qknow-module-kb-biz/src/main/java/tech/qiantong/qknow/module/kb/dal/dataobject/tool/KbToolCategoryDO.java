/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.module.kb.dal.dataobject.tool;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 知识分类 DO 对象 kmc_category
 *
 * @author qknow
 * @date 2025-02-13
 */
@Data
@TableName(value = "kb_tool_category")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("kmc_category_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KbToolCategoryDO {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作区id
     */
    private Long workspaceId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 显示顺序
     */
    private Long orderNum;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 是否有效
     */
    private Integer validFlag;

    /**
     * 删除标志
     */
    @TableLogic
    private Boolean delFlag;

    /**
     * 更新人id
     */
    private Long updaterId;

    private Long creatorId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;

    @TableField(exist = false)
    private List<KbToolCategoryDO> children = new ArrayList<KbToolCategoryDO>();
}
