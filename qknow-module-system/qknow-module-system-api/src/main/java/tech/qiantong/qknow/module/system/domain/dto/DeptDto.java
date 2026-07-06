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

package tech.qiantong.qknow.module.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeptDto {
    /**
     * 部门id 不能为空
     */


    private String id;
    /**
     * 部门名字 不能为空
     */


    private String name;
    /**
     * 部门上级id 不能为空 若为顶级 则是0
     */


    private String parentId;
    /**
     * 部门主管的userId 可以为空
     */


    private List<String> leaderUserIdList;
    /**
     * 部门状态 0 禁用 1启用
     */


    private Integer status;
    /**
     * 排序
     */


    private Integer sort;

}
