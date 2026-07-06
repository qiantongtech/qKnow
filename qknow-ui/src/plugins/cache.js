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

const sessionCache = {
    set(key, value) {
        if (!sessionStorage) {
            return;
        }
        if (key != null && value != null) {
            sessionStorage.setItem(key, value);
        }
    },
    get(key) {
        if (!sessionStorage) {
            return null;
        }
        if (key == null) {
            return null;
        }
        return sessionStorage.getItem(key);
    },
    setJSON(key, jsonValue) {
        if (jsonValue != null) {
            this.set(key, JSON.stringify(jsonValue));
        }
    },
    getJSON(key) {
        const value = this.get(key);
        if (value != null) {
            return JSON.parse(value);
        }
    },
    remove(key) {
        sessionStorage.removeItem(key);
    }
};
const localCache = {
    set(key, value) {
        if (!localStorage) {
            return;
        }
        if (key != null && value != null) {
            localStorage.setItem(key, value);
        }
    },
    get(key) {
        if (!localStorage) {
            return null;
        }
        if (key == null) {
            return null;
        }
        return localStorage.getItem(key);
    },
    setJSON(key, jsonValue) {
        if (jsonValue != null) {
            this.set(key, JSON.stringify(jsonValue));
        }
    },
    getJSON(key) {
        const value = this.get(key);
        if (value != null) {
            return JSON.parse(value);
        }
    },
    remove(key) {
        localStorage.removeItem(key);
    }
};

export default {
    /**
     * 会话级缓存
     */
    session: sessionCache,
    /**
     * 本地缓存
     */
    local: localCache
};
