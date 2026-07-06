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

package tech.qiantong.qknow.thirdparty.domain.dify.studio;

import lombok.Data;

/**
 * 参数类型
 * @author wang
 * @date 2025/04/23 16:42
 **/
@Data
public class ChatFile {

    /** id */
    private String id;

    /** 名称 */
    private String name;

    /** 文件大小 */
    private Integer size;

    /** 后缀 */
    private String extension;

    /** 文件类型 */
    private String mimeType;
}
