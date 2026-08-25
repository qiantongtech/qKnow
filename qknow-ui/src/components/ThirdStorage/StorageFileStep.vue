<!--
 Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 
 This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 
 qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 You may use qKnow for commercial purposes, but you may not remove, hide,
 modify, or replace the qKnow logo, copyright notices, license notices,
 or attribution information without a separate commercial license.
 
 White-label use, OEM distribution, rebranding, or presenting qKnow as
 another product requires separate commercial authorization from
 Jiangsu Qiantong Technology Co., Ltd.
 
 Business License: https://community.qknow.ai/business/policy.html
 See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="storage-file-step" :class="{ 'storage-file-step--page': pageMode }">
    <aside
      class="directory-pane"
      :class="{ 'directory-pane--collapsed': leftPaneWidth === 0 }"
      :style="{ width: `${leftPaneWidth}px` }"
    >
      <el-input
        v-model="treeKeyword"
        class="filter-tree"
        size="large"
        placeholder="请输入目录名称"
        clearable
        :prefix-icon="Search"
      />
      <div class="directory-tree" v-loading="treeLoading">
        <el-tree
          ref="treeRef"
          class="dept-tree"
          :key="treeKey"
          node-key="path"
          lazy
          highlight-current
          :load="loadTreeNode"
          :props="treeProps"
          :filter-node-method="filterTreeNode"
          :default-expanded-keys="['/']"
          @node-click="handleTreeNodeClick"
        >
          <template #default="{ data }">
            <span class="custom-tree-node">
              <img :src="folderIcon" alt="" class="node-icon" />
              <span class="treelable" :title="data.name">{{ data.name }}</span>
            </span>
          </template>
        </el-tree>
      </div>
    </aside>

    <div class="resize-bar" @mousedown="startResize">
      <div class="resize-handle-sx">
        <el-icon class="collapse-icon" @mousedown.stop @click.stop="toggleCollapse">
          <ArrowRight v-if="leftPaneWidth === 0" />
          <ArrowLeft v-else />
        </el-icon>
      </div>
    </div>

    <section class="file-pane">
      <div v-show="showSearch" class="file-search-panel">
      
        <el-form class="btn-style file-search-form" :inline="true" label-width="68px" @submit.prevent>
          <el-form-item label="文件名称">
            <el-input
              v-model="fileKeyword"
              class="el-form-input-width"
              placeholder="请输入文件名称"
              clearable
              @keyup.enter="handleSearch"
              @clear="handleSearch"
            />
          </el-form-item>
          <el-form-item>
            <el-button
                plain
                type="primary"
                :loading="fileLoading"
                @click="handleQuery"
                @mousedown="(e) => e.preventDefault()"
            >
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
            </el-button>
            <el-button :loading="fileLoading" @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="file-list-panel">
        <el-table
          ref="tableRef"
          v-loading="fileLoading"
          :data="fileRows"
          row-key="path"
          height="100%"
          stripe
          @selection-change="handleSelectionChange"
          @row-dblclick="handleRowDblclick"
        >
          <el-table-column type="selection" width="46" :selectable="isSelectable" />
          <el-table-column
            v-if="getColumnVisibility(1)"
            type="index"
            label="编号"
            width="62"
            align="center"
          />
          <el-table-column
            v-if="getColumnVisibility(2)"
            label="文件名"
            prop="name"
            min-width="185"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <button
                type="button"
                :class="['file-name', row.directory ? 'is-link' : 'is-text']"
                @click="handleNameClick(row)"
              >
                <span>{{ row.name }}</span>
              </button>
            </template>
          </el-table-column>
          <el-table-column v-if="getColumnVisibility(3)" label="文件类型" width="95" align="center">
            <template #default="{ row }">
              <el-tag :type="row.directory ? 'warning' : 'primary'" effect="light">
                {{ row.directory ? '文件夹' : row.fileType || '文件' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            v-if="getColumnVisibility(6)"
            label="所在路径"
            prop="path"
            min-width="210"
            show-overflow-tooltip
          />
          <el-table-column v-if="getColumnVisibility(4)" label="文件大小" width="90" >
            <template #default="{ row }">{{ row.directory ? '-' : formatSize(row.size) }}</template>
          </el-table-column>
          <el-table-column
            v-if="getColumnVisibility(5)"
            label="更新时间"
            width="155"
            align="center"
          >
            <template #default="{ row }">{{ formatLastModified(row.lastModified) }}</template>
          </el-table-column>
          <!-- <el-table-column
            v-if="getColumnVisibility(7)"
            label="操作"
            width="145"
            fixed="right"
            align="center"
          >
            <template #default="{ row }">
              <el-button link type="primary" :icon="View" @click.stop="handlePreview(row)">
                预览
              </el-button>
              <el-button link type="primary" :icon="Download" @click.stop="handleDownload(row)">
                下载
              </el-button>
            </template>
          </el-table-column> -->
          <template #empty>
            <el-empty description="当前目录暂无文件" :image-size="80" />
          </template>
        </el-table>

        <pagination
          v-show="total > 0"
          v-model:page="pageNum"
          v-model:limit="pageSize"
          :total="total"
          :auto-scroll="false"
          @pagination="fetchFiles"
        />
      </div>
    </section>
  </div>
</template>

<script setup name="StorageFileStep">
import { computed, nextTick, onBeforeUnmount, ref, watch } from 'vue';
import { ArrowLeft, ArrowRight, Download, Search, View } from '@element-plus/icons-vue';
import { saveAs } from 'file-saver';
import {
  downloadStorageFile,
  getStorageDirectoryTree,
  listStorageFiles,
  prepareStorageFilePreview
} from '@/api/kmc/storageSync/storageSync';
import { filePreview } from '@/utils/kkFileView.js';
import { parseTime } from '@/utils/anivia.js';
import folderIcon from '@/assets/da/asset/folder.svg';

const props = defineProps({
  connection: {
    type: Object,
    default: () => ({})
  },
  pageMode: {
    type: Boolean,
    default: false
  },
  loadTreeApi: {
    type: Function,
    default: null
  },
  listFilesApi: {
    type: Function,
    default: null
  },
  previewFileApi: {
    type: Function,
    default: null
  },
  downloadFileApi: {
    type: Function,
    default: null
  },
  supportedExtensions: {
    type: Array,
    default: () => [
      'txt', 'pdf', 'html', 'xlsx', 'xls', 'docx', 'csv', 'md', 'mdx', 'htm', 'markdown',
      'json', 'jsonl'
    ]
  }
});

const treeProps = { label: 'name', children: 'children', isLeaf: 'leaf' };
const supportedExtensionSet = computed(() => new Set(props.supportedExtensions));
const treeRef = ref();
const tableRef = ref();
const treeKey = ref(0);
const treeKeyword = ref('');
const fileKeyword = ref('');
const currentPath = ref('/');
const treeLoading = ref(false);
const fileLoading = ref(false);
const fileRows = ref([]);
const selectedRows = ref([]);
const selectedFileMap = ref(new Map());
const restoringSelection = ref(false);
const leftPaneWidth = ref(300);
const previousLeftPaneWidth = ref(300);
const resizing = ref(false);
let resizeStartX = 0;
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const showSearch = ref(true);
const loadedItemCounts = ref(new Map());
const columns = ref([
  { key: 1, label: '编号', visible: true },
  { key: 2, label: '文件名', visible: true },
  { key: 3, label: '文件类型', visible: true },
  { key: 4, label: '文件大小', visible: true },
  { key: 5, label: '更新时间', visible: true },
  { key: 6, label: '所在路径', visible: true },
  { key: 7, label: '操作', visible: true }
]);

function getColumnVisibility(key) {
  return columns.value.find((item) => item.key === key)?.visible !== false;
}

watch(treeKeyword, (value) => treeRef.value?.filter(value));

function startResize(event) {
  resizing.value = true;
  resizeStartX = event.clientX;
  document.addEventListener('mousemove', handleResize);
  document.addEventListener('mouseup', stopResize);
}

function handleResize(event) {
  if (!resizing.value) return;
  const delta = event.clientX - resizeStartX;
  const baseWidth = leftPaneWidth.value === 0 ? 220 : leftPaneWidth.value;
  leftPaneWidth.value = Math.min(450, Math.max(220, baseWidth + delta));
  previousLeftPaneWidth.value = leftPaneWidth.value;
  resizeStartX = event.clientX;
}

function stopResize() {
  resizing.value = false;
  document.removeEventListener('mousemove', handleResize);
  document.removeEventListener('mouseup', stopResize);
}

function toggleCollapse() {
  if (leftPaneWidth.value === 0) {
    leftPaneWidth.value = previousLeftPaneWidth.value || 300;
  } else {
    previousLeftPaneWidth.value = leftPaneWidth.value;
    leftPaneWidth.value = 0;
  }
}

onBeforeUnmount(stopResize);

function normalizePath(path) {
  let target = String(path || '/').replace(/\\/g, '/').replace(/\/+/g, '/');
  if (!target.startsWith('/')) target = `/${target}`;
  if (target.length > 1 && target.endsWith('/')) target = target.slice(0, -1);
  return target || '/';
}

function joinPath(parentPath, name) {
  const parent = normalizePath(parentPath);
  const child = String(name || '').replace(/^\/+|\/+$/g, '');
  return parent === '/' ? `/${child}` : `${parent}/${child}`;
}

function getRows(response) {
  const data = response?.data ?? response;
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.rows)) return data.rows;
  if (Array.isArray(data?.list)) return data.list;
  return [];
}

function getTotal(response, rows) {
  const data = response?.data ?? response;
  return Number(data?.total ?? rows.length);
}

function getFileExtension(name) {
  const value = String(name || '');
  const index = value.lastIndexOf('.');
  return index > -1 && index < value.length - 1
    ? value.substring(index + 1).toLowerCase()
    : '';
}

function isSupportedFile(name) {
  return supportedExtensionSet.value.has(getFileExtension(name));
}

function isSelectable(row) {
  return row.directory || isSupportedFile(row.name);
}

function normalizeRow(item, parentPath = currentPath.value) {
  const path = normalizePath(item.path || joinPath(parentPath, item.name));
  const directory =
    item.directory === true ||
    item.directory === 1 ||
    item.isDirectory === true ||
    item.type === 'directory' ||
    item.fileType === '文件夹';
  return {
    ...item,
    path,
    name: item.name || path.split('/').filter(Boolean).at(-1) || '/',
    directory,
    supported: directory || isSupportedFile(item.name || path),
    lastModified: item.lastModified || item.updateTime || '-'
  };
}

function buildRequestData(extra = {}) {
  return {
    connection: { ...props.connection },
    ...extra
  };
}

async function loadTreeNode(node, resolve) {
  if (node.level === 0) {
    resolve([{ name: '/', path: '/', directory: true, leaf: false }]);
    return;
  }

  // 第一步尚未完成时不允许树组件提前访问后端。
  if (!props.connection?.datasourceType) {
    resolve([]);
    return;
  }

  treeLoading.value = true;
  const path = normalizePath(node.data.path);
  try {
    const response = await (props.loadTreeApi || getStorageDirectoryTree)(buildRequestData({ path }));
    const directories = getRows(response)
      .map((item) => normalizeRow(item, path))
      .filter((item) => item.directory)
      .map((item) => ({ ...item, leaf: item.hasChildren === false }));
    resolve(directories);
  } catch {
    resolve([]);
  } finally {
    treeLoading.value = false;

  }
}

function filterTreeNode(value, data) {
  return !value || String(data.name || '').includes(value);
}

function handleTreeNodeClick(data) {
  openDirectory(data.path);
}

function handleNameClick(row) {
  if (row.directory) openDirectory(row.path);
}

function handleRowDblclick(row) {
  if (row.directory) openDirectory(row.path);
}

async function handlePreview(row) {
  if (row.directory) {
    await openDirectory(row.path);
    return;
  }
  const response = await (props.previewFileApi || prepareStorageFilePreview)(
    buildRequestData({ path: row.path })
  );
  const previewUrl = response?.data?.fileUrl || response?.fileUrl;
  if (!previewUrl) throw new Error('未获取到文件预览地址');
  filePreview(previewUrl);
}

async function handleDownload(row) {
  const blob = await (props.downloadFileApi || downloadStorageFile)(
    buildRequestData({ path: row.path })
  );
  const fileName = row.directory ? `${row.name || '目录'}.zip` : row.name;
  saveAs(new Blob([blob]), fileName);
}

async function openDirectory(path) {
  currentPath.value = normalizePath(path);
  pageNum.value = 1;
  await fetchFiles();
  await nextTick();
  treeRef.value?.setCurrentKey(currentPath.value);
}

async function fetchFiles() {
  fileLoading.value = true;
  try {
    const response = await (props.listFilesApi || listStorageFiles)(
      buildRequestData({
        path: currentPath.value,
        fileName: fileKeyword.value.trim(),
        pageNum: pageNum.value,
        pageSize: pageSize.value
      })
    );
    const rows = getRows(response).map((item) => normalizeRow(item));
    fileRows.value = rows;
    total.value = getTotal(response, rows);
    loadedItemCounts.value.set(currentPath.value, rows.length);
    await restoreCurrentPageSelection();
  } catch {
    fileRows.value = [];
    total.value = 0;
  } finally {
    fileLoading.value = false;
  }
}

function handleSearch() {
  pageNum.value = 1;
  fetchFiles();
}

function handleQuery() {
  if (fileLoading.value) return;
  handleSearch();
}

function handleResetSearch() {
  if (fileLoading.value) return;
  fileKeyword.value = '';
  pageNum.value = 1;
  fetchFiles();
}

function resetQuery() {
  handleResetSearch();
}

function handleSelectionChange(rows) {
  if (restoringSelection.value) return;
  const deselectedDirs = [];
  fileRows.value.forEach((row) => {
    if (row.directory && isRowImplicitlySelected(row)) {
      const isNowSelected = rows.some((r) => r.path === row.path);
      if (!isNowSelected) {
        deselectedDirs.push(row);
      }
    }
  });
  const nextSelection = new Map(selectedFileMap.value);
  fileRows.value.forEach((row) => nextSelection.delete(row.path));
  rows.filter((row) => isSelectable(row)).forEach((row) => nextSelection.set(row.path, { ...row }));
  deselectedDirs.forEach((row) => {
    const prefix = row.path === '/' ? '/' : (row.path.endsWith('/') ? row.path : row.path + '/');
    for (const key of Array.from(nextSelection.keys())) {
      if (key.startsWith(prefix)) {
        nextSelection.delete(key);
      }
    }
  });
  selectedFileMap.value = nextSelection;
  selectedRows.value = Array.from(nextSelection.values());
}

function isRowImplicitlySelected(row) {
  if (!row || !row.path) return false;
  const normalized = normalizePath(row.path);
  if (selectedFileMap.value.has(normalized)) return true;
  const segments = normalized.split('/').filter(Boolean);
  let ancestor = '';
  for (let i = 0; i < segments.length - 1; i++) {
    ancestor += '/' + segments[i];
    if (selectedFileMap.value.has(ancestor)) return true;
  }
  if (row.directory) {
    const loadedCount = loadedItemCounts.value.get(normalized);
    if (loadedCount != null && loadedCount > 0) {
      const prefix = normalized === '/' ? '/' : (normalized.endsWith('/') ? normalized : normalized + '/');
      const selectedCount = Array.from(selectedFileMap.value.keys())
        .filter((p) => p.startsWith(prefix) && p !== normalized).length;
      if (selectedCount >= loadedCount) return true;
    }
  }
  return false;
}

async function restoreCurrentPageSelection() {
  restoringSelection.value = true;
  await nextTick();
  if (!tableRef.value) {
    restoringSelection.value = false;
    return;
  }
  tableRef.value.clearSelection();
  fileRows.value.forEach((row) => {
    if (isRowImplicitlySelected(row) && isSelectable(row)) {
      tableRef.value.toggleRowSelection(row, true);
    }
  });
  await nextTick();
  restoringSelection.value = false;
}

function refresh() {
  treeKey.value += 1;
  return fetchFiles();
}

function formatSize(size) {
  const value = Number(size);
  if (!Number.isFinite(value) || value < 0) return '-';
  if (value === 0) return '0 B';
  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  const index = Math.min(Math.floor(Math.log(value) / Math.log(1024)), units.length - 1);
  const result = value / 1024 ** index;
  return `${result >= 10 || index === 0 ? result.toFixed(0) : result.toFixed(1)} ${units[index]}`;
}

function formatLastModified(value) {
  if (!value || value === '-') return '-';
  return parseTime(value, '{y}-{m}-{d} {h}:{i}') || value;
}

async function reload() {
  treeKeyword.value = '';
  fileKeyword.value = '';
  currentPath.value = '/';
  pageNum.value = 1;
  selectedRows.value = [];
  selectedFileMap.value = new Map();
  loadedItemCounts.value = new Map();
  treeKey.value += 1;
  await fetchFiles();
}

function reset() {
  currentPath.value = '/';
  fileRows.value = [];
  selectedRows.value = [];
  selectedFileMap.value = new Map();
  loadedItemCounts.value = new Map();
  total.value = 0;
  pageNum.value = 1;
  treeKeyword.value = '';
  fileKeyword.value = '';
}

async function setSelectedFiles(files = []) {
  const nextSelection = new Map(
    files
      .filter((item) => item?.path)
      .filter((item) => item.directory || isSupportedFile(item.name))
      .map((item) => [normalizePath(item.path), { ...item, path: normalizePath(item.path) }])
  );
  selectedFileMap.value = nextSelection;
  selectedRows.value = Array.from(nextSelection.values());
  await restoreCurrentPageSelection();
}

function validate() {
  return selectedRows.value.length > 0
    ? Promise.resolve(true)
    : Promise.reject(new Error('请至少选择一个文件或文件夹'));
}

defineExpose({
  reload,
  refresh,
  reset,
  setSelectedFiles,
  validate,
  getSelectedFiles: () => selectedRows.value.map((item) => ({ ...item }))
});
</script>

<style scoped lang="scss">
.storage-file-step {
  display: flex;
  height: 500px;
  overflow: hidden;
  background: #f0f2f5;
}

.storage-file-step--page {
  height: 100%;
  min-height: 0;
  background: #fff;
}

.directory-pane {
  display: flex;
  flex-shrink: 0;
  flex-direction: column;
  padding: 15px;
  overflow: hidden;
  background: #fff;
  box-sizing: border-box;

  &--collapsed {
    padding-right: 0;
    padding-left: 0;
  }
}

.filter-tree {
  margin-bottom: 16px;

  :deep(.el-input__wrapper) {
    border: 1px solid var(--el-color-primary);
  }

  :deep(.el-input__prefix) {
    color: var(--el-color-primary);
  }
}

.directory-tree {
  min-height: 0;
  flex: 1;
  overflow: auto;
  scrollbar-width: thin;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 3px;
  }
}

.custom-tree-node {
  display: flex;
  width: 100%;
  min-width: 0;
  align-items: center;
  padding: 0 36px 0 12px;
  box-sizing: border-box;
}

.node-icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}

.treelable {
  min-width: 0;
  flex: 1;
  margin-left: 10px;
  overflow: hidden;
  color: rgba(0, 0, 0, 0.85);
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  font-size: 14px;
  font-weight: 400;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.dept-tree) {
  &.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
    background: rgba(51, 103, 252, 0.06) !important;
    border: none;

    .treelable {
      color: var(--el-color-primary);
    }
  }

  .el-tree-node__content {
    position: relative;

    .el-tree-node__expand-icon {
      position: absolute;
      right: 10px;
      width: 11px;
      height: 11px;
      color: transparent;
      font-size: 11px;

      & > svg {
        background: url('@/assets/da/asset/arrow.svg') no-repeat;
        background-size: 100% 100%;
        transform: rotate(-90deg);
      }
    }
  }
}

.resize-bar {
  display: flex;
  width: 12px;
  height: 100%;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  cursor: ew-resize;
}

.resize-handle-sx {
  position: relative;
  width: 12px;
  text-align: center;
}

.collapse-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  z-index: 10;
  padding: 5px 0;
  color: #aaa;
  cursor: pointer;
  font-size: 28px;
  transform: translate(-50%, -50%);
}

.file-pane {
  display: flex;
  min-width: 0;
  flex: 1;
  flex-direction: column;
  overflow: hidden;
  background: #f0f2f5;
  gap: 12px;

  :deep(.el-table) {
    min-height: 0;
    flex: 1;
  }
}

.file-search-panel {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  height: 62px;
  padding: 0 15px;
  background: #fff;

  .file-search-form {
    display: flex;
    align-items: center;
  }

  :deep(.el-form-item) {
    margin-bottom: 0;
  }
}

.file-list-panel {
  display: flex;
  min-height: 0;
  flex: 1;
  flex-direction: column;
  padding: 15px;
  background: #fff;
}

.file-list-toolbar {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: flex-end;
  margin-bottom: 12px;
}

.file-name {
  display: inline-flex;
  max-width: 100%;
  align-items: center;
  padding: 0;
  background: transparent;
  border: 0;

  &.is-link {
    color: #2666fb;
    cursor: pointer;
  }

  &.is-text {
    color: inherit;
    cursor: default;
  }

  &__icon {
    width: 16px;
    height: 16px;
    flex-shrink: 0;
    margin-right: 8px;
  }

  span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>

