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

import CryptoJS from 'crypto-js';

const aesKey = import.meta.env.VITE_APP_AES_KEY;
const authType = import.meta.env.VITE_APP_AUTH_TYPE;
console.log(aesKey, 'aesKey');
// 解密
export function decrypt(data) {
    const key = CryptoJS.enc.Utf8.parse(aesKey);
    const iv = CryptoJS.enc.Utf8.parse(aesKey);
    // 解密数据
    const decryptedBytes = CryptoJS.AES.decrypt(
        {
            ciphertext: CryptoJS.enc.Base64.parse(data)
        },
        key,
        {
            iv: iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.ZeroPadding
        }
    );
    const decryptedData = decryptedBytes.toString(CryptoJS.enc.Utf8);
    return decryptedData;
}

// 加密
export function encrypt(data) {
    const key = CryptoJS.enc.Utf8.parse(aesKey);
    const iv = CryptoJS.enc.Utf8.parse(aesKey);
    // 加密数据
    const encryptedBytes = CryptoJS.AES.encrypt(data, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.ZeroPadding
    });
    const encryptedData = encryptedBytes.ciphertext.toString(CryptoJS.enc.Base64);
    return encryptedData;
}
