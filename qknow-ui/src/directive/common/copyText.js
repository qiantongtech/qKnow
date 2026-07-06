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

/**
 * v-copyText 复制文本内容
 * Copyright (c) 2022 qiantong
 */

export default {
    beforeMount(el, { value, arg }) {
        if (arg === 'callback') {
            el.$copyCallback = value;
        } else {
            el.$copyValue = value;
            const handler = () => {
                copyTextToClipboard(el.$copyValue);
                if (el.$copyCallback) {
                    el.$copyCallback(el.$copyValue);
                }
            };
            el.addEventListener('click', handler);
            el.$destroyCopy = () => el.removeEventListener('click', handler);
        }
    }
};

function copyTextToClipboard(input, { target = document.body } = {}) {
    const element = document.createElement('textarea');
    const previouslyFocusedElement = document.activeElement;

    element.value = input;

    // Prevent keyboard from showing on mobile
    element.setAttribute('readonly', '');

    element.style.contain = 'strict';
    element.style.position = 'absolute';
    element.style.left = '-9999px';
    element.style.fontSize = '12pt'; // Prevent zooming on iOS

    const selection = document.getSelection();
    const originalRange = selection.rangeCount > 0 && selection.getRangeAt(0);

    target.append(element);
    element.select();

    // Explicit selection workaround for iOS
    element.selectionStart = 0;
    element.selectionEnd = input.length;

    let isSuccess = false;
    try {
        isSuccess = document.execCommand('copy');
    } catch {}

    element.remove();

    if (originalRange) {
        selection.removeAllRanges();
        selection.addRange(originalRange);
    }

    // Get the focus back on the previously focused element, if any
    if (previouslyFocusedElement) {
        previouslyFocusedElement.focus();
    }

    return isSuccess;
}
