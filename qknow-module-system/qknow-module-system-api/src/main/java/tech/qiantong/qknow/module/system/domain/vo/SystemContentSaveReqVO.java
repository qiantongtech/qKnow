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

package tech.qiantong.qknow.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 系统配置 创建/修改 Request VO system_content
 *
 * @author qknow
 * @date 2024-12-31
 */
@Schema(description = "系统配置 Response VO")
@Data
public class SystemContentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "系统名称", example = "")
    @Size(max = 256, message = "系统名称长度不能超过256个字符")
    private String sysName;

    @Schema(description = "loginLogo", example = "")
    @Size(max = 256, message = "loginLogo长度不能超过256个字符")
    private String loginLogo;

    @Schema(description = "logo", example = "")
    @Size(max = 256, message = "logo长度不能超过256个字符")
    private String logo;

    @Schema(description = "轮播图", example = "")
    @Size(max = 256, message = "轮播图长度不能超过256个字符")
    private String carouselImage;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "电子邮箱", example = "")
    @Size(max = 256, message = "电子邮箱长度不能超过256个字符")
    private String email;

    @Schema(description = "版权方", example = "")
    @Size(max = 256, message = "版权方长度不能超过256个字符")
    private String copyright;

    @Schema(description = "备案号", example = "")
    @Size(max = 256, message = "备案号长度不能超过256个字符")
    private String recordNumber;

    @Schema(description = "状态", example = "")
    private Integer status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remarks;


}
