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

package tech.qiantong.qknow.module.kb.dal.dataobject.skills;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * skills DO 对象 kb_skills
 *
 * @author qknow
 * @date 2026-06-17
 */
@Data
@TableName(value = "kb_skills")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("kb_skills_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KbSkillsDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 指令 */
    private String prompt;

    /** 文件路径 */
    private String filePath;

    /** 状态 */
    private Integer status;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
