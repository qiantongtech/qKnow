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

package tech.qiantong.qknow.module.kb.controller.admin.tool.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识分类 Response VO 对象 kmc_category
 *
 * @author qknow
 * @date 2025-02-13
 */
@Schema(description = "知识分类 Response VO")
@Data
public class KbToolCategoryRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "父级id")
    @Schema(description = "父级id", example = "")
    private Long parentId;

    @Excel(name = "分类名称")
    @Schema(description = "分类名称", example = "")
    private String name;

    @Excel(name = "显示顺序")
    @Schema(description = "显示顺序", example = "")
    private Long orderNum;

    @Excel(name = "祖级列表")
    @Schema(description = "祖级列表", example = "")
    private String ancestors;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private String validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
