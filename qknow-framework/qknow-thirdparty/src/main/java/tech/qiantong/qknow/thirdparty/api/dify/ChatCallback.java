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

package tech.qiantong.qknow.thirdparty.api.dify;

import okhttp3.Response;

import java.io.IOException;

/**
 * 大模型回调封装
 * @author wang
 * @date 2025/03/18 15:41
 **/
public interface ChatCallback {
    /**
     * 成功回调
     * @param response
     */
    void onSuccess(Response response) throws IOException;

    /**
     * 失败回调
     * @param e
     */
    void onFailure(IOException e);
}
