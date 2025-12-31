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

package tech.qiantong.qknow.module.system.controller.admin.system.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 消息 创建/修改 Request VO message
 *
 * @author qknow
 * @date 2024-10-31
 */
@Schema(description = "消息 Response VO")
@Data
public class MessageSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发送人", example = "")
    private Long senderId;


    @Schema(description = "接收人", example = "")
    private Long receiverId;

    @Schema(description = "消息模块", example = "")
    @NotNull(message = "消息模块不能为空")
    private Integer module;


    @Schema(description = "实体类型", example = "")
    private Integer entityType;


    @Schema(description = "实体id", example = "")
    private Long entityId;

    @Schema(description = "消息链接", example = "")
    @NotBlank(message = "消息链接不能为空")
    @Size(max = 256, message = "消息链接长度不能超过256个字符")
    private String entityUrl;

    private Integer delFlag;
    private String id;
    private Integer hasRead;

}
