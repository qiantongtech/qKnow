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

package tech.qiantong.qknow.common.core.page;

import cn.hutool.core.lang.Opt;
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
