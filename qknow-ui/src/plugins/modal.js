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

import { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus';

let loadingInstance;

export default {
    // 消息提示
    msg(content) {
        ElMessage.info(content);
    },
    // 错误消息
    msgError(content) {
        ElMessage.error(content);
    },
    // 成功消息
    msgSuccess(content) {
        ElMessage.success(content);
    },
    // 警告消息
    msgWarning(content) {
        ElMessage.warning(content);
    },
    // 弹出提示
    alert(content) {
        ElMessageBox.alert(content, '系统提示');
    },
    // 错误提示
    alertError(content) {
        ElMessageBox.alert(content, '系统提示', { type: 'error' });
    },
    // 成功提示
    alertSuccess(content) {
        ElMessageBox.alert(content, '系统提示', { type: 'success' });
    },
    // 警告提示
    alertWarning(content) {
        ElMessageBox.alert(content, '系统提示', { type: 'warning' });
    },
    // 通知提示
    notify(content) {
        ElNotification.info(content);
    },
    // 错误通知
    notifyError(content) {
        ElNotification.error(content);
    },
    // 成功通知
    notifySuccess(content) {
        ElNotification.success(content);
    },
    // 警告通知
    notifyWarning(content) {
        ElNotification.warning(content);
    },
    // 确认窗体
    confirm(content) {
        return ElMessageBox.confirm(content, '系统提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        });
    },
    // 提交内容
    prompt(content) {
        return ElMessageBox.prompt(content, '系统提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        });
    },
    // 打开遮罩层
    loading(content) {
        loadingInstance = ElLoading.service({
            lock: true,
            text: content,
            background: 'rgba(0, 0, 0, 0.7)'
        });
    },
    // 关闭遮罩层
    closeLoading() {
        loadingInstance.close();
    }
};
