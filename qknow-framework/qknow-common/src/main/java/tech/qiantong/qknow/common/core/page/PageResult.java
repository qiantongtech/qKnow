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

package tech.qiantong.qknow.common.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页结果
 * @author Ming
 */

@Schema(description = "分页结果")
@Data
public final class PageResult<T> implements Serializable {

    @Schema(description = "数据")
    private List<?> rows;

    @Schema(description = "总量")
    private Long total;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        this.rows = list;
        this.total = total;
    }

    public PageResult(Long total) {
        this.rows = new ArrayList<>();
        this.total = total;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }


    /**
     * 获取当前页的数据列表，并将其转换为指定类型 T 的列表
     * @return 数据列表
     */
    @SuppressWarnings("unchecked")
    public List<T> getList() {
        if (this.rows == null) {
            return new ArrayList<>();
        }
        return (List<T>) this.rows;
    }
}
