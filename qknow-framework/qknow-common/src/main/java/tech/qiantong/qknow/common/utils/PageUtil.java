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

package tech.qiantong.qknow.common.utils;

import java.io.Serializable;

public class PageUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Integer DEFAULT_MAX_COUNT = 5000;

    // 当前页码
    private Integer pageNum = 1;
    // 分页条数
    private Integer pageSize = 20;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if (this.pageSize > 0) {
            this.pageSize = this.pageSize > DEFAULT_MAX_COUNT ? DEFAULT_MAX_COUNT : this.pageSize;
        } else {
            this.pageSize = 20;
        }
    }

    public PageUtil(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        if (this.pageSize > 0) {
            this.pageSize = this.pageSize > DEFAULT_MAX_COUNT ? DEFAULT_MAX_COUNT : this.pageSize;
        } else {
            this.pageSize = 20;
        }
    }

    public Integer getOffset() {
        pageSize = pageSize == null ? 20 : pageSize;
        pageNum = pageNum == null ? 1 : pageNum;
        int offset = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        return offset;
    }
}
