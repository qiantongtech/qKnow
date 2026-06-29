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

const baseUrl = import.meta.env.VITE_APP_FILE_VIEW + '/onlinePreview?url=';

// 获取屏幕尺寸
const screenWidth = window.screen.width;
const screenHeight = window.screen.height;

// 设置窗口尺寸为屏幕尺寸的一部分，例如60%
const width = screenWidth * 0.7;
const height = screenHeight * 0.7;

// 计算窗口居中时的左上角位置
const left = (screenWidth - width) / 2;
const top = (screenHeight - height) / 2;

export const filePreview = (fileUrl) => {
    // 打开新窗口并居中
    const newWindow = window.open(
        baseUrl + base64Encode(getBaseURL() + fileUrl),
        '',
        `scrollbars=yes, width=${width}, height=${height}, top=${top}, left=${left}`
    );
    if (window.focus) {
        newWindow.focus();
    }
};

export const filePreviewUrl = (fileUrl) => {
    return baseUrl + base64Encode(getBaseURL() + fileUrl);
};

function base64Encode(str) {
    // 首先将字符串转换为Uint8Array
    let uint8Array = new TextEncoder().encode(str);
    // 然后使用btoa进行Base64编码，注意这里使用了Uint8Array的reduce结合atob进行转换
    return btoa(String.fromCharCode.apply(null, uint8Array));
}

function getBaseURL() {
    const { protocol, hostname, port } = window.location;
    return `${protocol}//${hostname}${port ? ':' + port : ''}`;
    // return "http://192.168.0.115:80"
}
