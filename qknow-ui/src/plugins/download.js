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

import axios from 'axios';
import { ElLoading, ElMessage } from 'element-plus';
import { saveAs } from 'file-saver';
import { getToken } from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import { blobValidate } from '@/utils/anivia.js';

const baseURL = import.meta.env.VITE_APP_BASE_API;
let downloadLoadingInstance;

export default {
    name(name, isDelete = true) {
        var url =
            baseURL +
            '/common/download?fileName=' +
            encodeURIComponent(name) +
            '&delete=' +
            isDelete;
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: { Authorization: 'Bearer ' + getToken() }
        }).then((res) => {
            const isBlob = blobValidate(res.data);
            if (isBlob) {
                const blob = new Blob([res.data]);
                this.saveAs(blob, decodeURIComponent(res.headers['download-filename']));
            } else {
                this.printErrMsg(res.data);
            }
        });
    },
    resource(resource) {
        var url = baseURL + '/common/download/resource?resource=' + encodeURIComponent(resource);
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: { Authorization: 'Bearer ' + getToken() }
        }).then((res) => {
            const isBlob = blobValidate(res.data);
            if (isBlob) {
                const blob = new Blob([res.data]);
                this.saveAs(blob, decodeURIComponent(res.headers['download-filename']));
            } else {
                this.printErrMsg(res.data);
            }
        });
    },
    zip(url, name) {
        var url = baseURL + url;
        downloadLoadingInstance = ElLoading.service({
            text: '正在下载数据，请稍候',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: { Authorization: 'Bearer ' + getToken() }
        })
            .then((res) => {
                const isBlob = blobValidate(res.data);
                if (isBlob) {
                    const blob = new Blob([res.data], { type: 'application/zip' });
                    this.saveAs(blob, name);
                } else {
                    this.printErrMsg(res.data);
                }
                downloadLoadingInstance.close();
            })
            .catch((r) => {
                console.error(r);
                ElMessage.error('下载文件出现错误，请联系管理员！');
                downloadLoadingInstance.close();
            });
    },
    saveAs(text, name, opts) {
        saveAs(text, name, opts);
    },
    async printErrMsg(data) {
        const resText = await data.text();
        const rspObj = JSON.parse(resText);
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
        ElMessage.error(errMsg);
    }
};
