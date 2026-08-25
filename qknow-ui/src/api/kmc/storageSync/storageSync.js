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

import request from '@/utils/request';

/**
 * 使用未落库的连接参数测试第三方存储连接。
 */
export function testStorageConnection(data) {
  return request({
    url: '/kmc/sync/testConnection',
    method: 'post',
    data
  });
}

/**
 * 按路径懒加载第三方存储目录。
 */
export function getStorageDirectoryTree(data) {
  return request({
    url: '/kmc/sync/fileTree',
    method: 'post',
    data
  });
}

/**
 * 查询第三方存储当前目录下的文件和文件夹。
 */
export function listStorageFiles(data) {
  return request({
    url: '/kmc/sync/fileList',
    method: 'post',
    data
  });
}

/**
 * 将第三方存储文件准备为系统统一预览地址。
 */
export function prepareStorageFilePreview(data) {
  return request({
    url: '/kmc/sync/filePreview',
    method: 'post',
    data
  });
}

/**
 * 下载第三方存储文件；目录由后端打包为 ZIP。
 */
export function downloadStorageFile(data) {
  return request({
    url: '/kmc/sync/fileDownload',
    method: 'post',
    data,
    responseType: 'blob'
  });
}

/** 展开选中的文件夹，并按知识文件支持格式过滤候选文件。 */
export function resolveStorageCandidates(data) {
  return request({
    url: '/kmc/sync/resolveCandidates',
    method: 'post',
    data
  });
}

/** 将过滤后的第三方存储文件导入当前知识库。 */
export function importStorageDocuments(data) {
  return request({
    url: '/kmc/sync/importDocuments',
    method: 'post',
    data
  });
}
