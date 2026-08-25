import request from '@/utils/request';

/**
 * 使用未落库的连接参数测试第三方存储连接。
 */
export function testStorageConnection(data) {
  return request({
    url: '/kg/sync/testConnection',
    method: 'post',
    data
  });
}

/**
 * 按路径懒加载第三方存储目录。
 */
export function getStorageDirectoryTree(data) {
  return request({
    url: '/kg/sync/fileTree',
    method: 'post',
    data
  });
}

/**
 * 查询第三方存储当前目录下的文件和文件夹。
 */
export function listStorageFiles(data) {
  return request({
    url: '/kg/sync/fileList',
    method: 'post',
    data
  });
}

/**
 * 将第三方存储文件准备为系统统一预览地址。
 */
export function prepareStorageFilePreview(data) {
  return request({
    url: '/kg/sync/filePreview',
    method: 'post',
    data
  });
}

/**
 * 下载第三方存储文件；目录由后端打包为 ZIP。
 */
export function downloadStorageFile(data) {
  return request({
    url: '/kg/sync/fileDownload',
    method: 'post',
    data,
    responseType: 'blob'
  });
}

/** 展开选中的文件夹，并按知识文件支持格式过滤候选文件。 */
export function resolveStorageCandidates(data) {
  return request({
    url: '/kg/sync/resolveCandidates',
    method: 'post',
    data
  });
}

/** 将过滤后的第三方存储文件导入当前知识图谱。 */
export function importStorageDocuments(data) {
  return request({
    url: '/kg/sync/importDocuments',
    method: 'post',
    data
  });
}
