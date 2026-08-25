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
  <div  class="app-container" ref="app-container">
    <div class="selected-file-panel">
      <div class="section-heading">文件选择</div>

      <div class="selected-file-toolbar">
        <el-button type="primary" plain @click="openFileDialog">选择文件</el-button>
        <el-icon class="toolbar-tip-icon"><InfoFilled /></el-icon>
        <span class="toolbar-tip-text">{{ fileTypeTip }}</span>
      </div>

      <el-table
        :data="pagedSelectedFiles"
        row-key="path"
        max-height="410"
        v-loading="expanding"
        element-loading-text="正在展开文件夹，请稍候..."
        element-loading-background="rgba(255, 255, 255, 0.6)"
      >
        <el-table-column type="index" label="编号" width="70" align="center">
          <template #default="scope">
            {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="文件名" prop="name" min-width="220" show-overflow-tooltip />
        <el-table-column label="文件类型" width="110" align="center">
          <template #default="{ row }">
            <el-tag type="primary" effect="light">
              {{ row.directory ? '文件夹' : row.fileType || '文件' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="所在路径"
          prop="path"
          min-width="260"
          show-overflow-tooltip
        />
        <el-table-column label="文件大小" width="110">
          <template #default="{ row }">{{ row.directory ? '-' : formatSize(row.size) }}</template>
        </el-table-column>
        <el-table-column label="更新时间" width="165" align="center">
          <template #default="{ row }">{{ formatLastModified(row.lastModified) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
                link
                type="danger"
                icon="Delete"
                @click="removeSelectedFile(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="totalSelectedCount > 0"
        v-model:page="pageNum"
        v-model:limit="pageSize"
        :total="totalSelectedCount"
        :auto-scroll="false"
      />
    </div>

    <el-dialog
      v-model="fileDialogVisible"
      title="选择文件"
      width="86%"
      append-to-body
      draggable
      :append-to="$refs['app-container']"
      class="storage-file-select-dialog"
    >
      <div class="file-browser-container">
        <StorageFileStep
          ref="browserRef"
          :connection="connection"
          page-mode
          :load-tree-api="loadTreeApi"
          :list-files-api="listFilesApi"
          :preview-file-api="previewFileApi"
          :download-file-api="downloadFileApi"
          :supported-extensions="supportedExtensions"
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="fileDialogVisible = false">取 消</el-button>
          <el-button type="primary" size="mini" @click="confirmSelection"> 确 定 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="StorageFileSelectStep">
import { computed, nextTick, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { InfoFilled } from '@element-plus/icons-vue';
import StorageFileStep from './StorageFileStep.vue';
import { resolveStorageCandidates } from '@/api/kmc/storageSync/storageSync';
import { parseTime } from '@/utils/anivia.js';

const props = defineProps({
  connection: {
    type: Object,
    default: () => ({})
  },
  resolveCandidatesApi: {
    type: Function,
    default: null
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

const fileTypeTip = computed(() => {
  const extensions = props.supportedExtensions || [];
  return `注：请选择需要同步的文件或文件夹，仅支持 ${extensions.map((ext) => ext.toUpperCase()).join('、')} 格式`;
});

const browserRef = ref();
const fileDialogVisible = ref(false);
// 用户原始选择（含文件夹），用于回显到弹窗和提交给下一步
const selectedFiles = ref([]);
// 展开后的文件列表，用于右侧表格展示
const expandedFiles = ref([]);
// 文件夹内被手动剔除的文件路径集合（保留跨次选择，直到 reset）
const excludedPaths = ref(new Set());
const expanding = ref(false);
const pageNum = ref(1);
const pageSize = ref(10);

const pagedSelectedFiles = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value;
  return expandedFiles.value.slice(start, start + pageSize.value);
});

const totalSelectedCount = computed(() => expandedFiles.value.length);

function isPathInsideFolder(filePath, folderPath) {
  if (!folderPath || !filePath) return false;
  const prefix = folderPath === '/'
    ? '/'
    : (folderPath.endsWith('/') ? folderPath : folderPath + '/');
  return filePath.startsWith(prefix);
}

function findFolderForPath(filePath) {
  return selectedFiles.value.find(
    (item) => item.directory && isPathInsideFolder(filePath, item.path)
  );
}

async function refreshExpandedFiles() {
  if (!selectedFiles.value.length) {
    expandedFiles.value = [];
    return;
  }
  expanding.value = true;
  try {
    const response = await (props.resolveCandidatesApi || resolveStorageCandidates)({
      connection: { ...props.connection },
      selectedItems: selectedFiles.value.map(({ name, path, directory }) => ({
        name,
        path,
        directory
      }))
    });
    const result = response?.data || response || {};
    const accepted = Array.isArray(result.acceptedFiles) ? result.acceptedFiles : [];
    // 弹窗里已获取到的文件信息（size/lastModified 等）不应被后端展开结果覆盖丢失
    const selectedMap = new Map(selectedFiles.value.map((item) => [item.path, item]));
    expandedFiles.value = accepted
      .filter((item) => !excludedPaths.value.has(item.path))
      .map((item) => {
        const original = selectedMap.get(item.path);
        return {
          ...original,
          ...item,
          size: item.size || item.size === 0 ? item.size : original?.size,
          lastModified: item.lastModified || original?.lastModified || '-'
        };
      });
  } catch {
    expandedFiles.value = selectedFiles.value
      .filter((item) => !item.directory)
      .filter((item) => !excludedPaths.value.has(item.path));
    ElMessage.warning('展开文件夹失败，请稍后重试');
  } finally {
    expanding.value = false;
  }
}

async function openFileDialog() {
  fileDialogVisible.value = true;
  await nextTick();
  await browserRef.value?.reload();
  await browserRef.value?.setSelectedFiles(selectedFiles.value);
}

async function confirmSelection() {
  try {
    await browserRef.value?.validate();
  } catch {
    ElMessage.warning('请至少选择一个文件或文件夹');
    return;
  }
  // 保留用户原始选择（含文件夹），便于弹窗回显与下一步服务端展开
  selectedFiles.value = (browserRef.value?.getSelectedFiles() || []).map((item) => ({ ...item }));
  pageNum.value = 1;
  fileDialogVisible.value = false;
  await refreshExpandedFiles();
}

async function removeSelectedFile(row) {
  const folder = findFolderForPath(row.path);
  if (folder) {
    // 文件夹内的文件：把文件夹拆分为剩余文件，避免弹窗里因父文件夹仍被勾选而继续隐式选中该文件
    const folderPath = folder.path;
    const folderPrefix = folderPath === '/' ? '/' : (folderPath.endsWith('/') ? folderPath : folderPath + '/');
    const remainingInFolder = expandedFiles.value.filter(
      (item) => item.path !== row.path && item.path.startsWith(folderPrefix)
    );
    // 清理该文件夹相关的排除记录，改用精确文件列表维护选择
    excludedPaths.value = new Set(
      Array.from(excludedPaths.value).filter((p) => !p.startsWith(folderPrefix))
    );
    selectedFiles.value = selectedFiles.value
      .filter((item) => item.path !== folderPath)
      .concat(remainingInFolder);
    await refreshExpandedFiles();
  } else {
    // 文件夹本身或独立选择的文件：从原始选择中移除
    selectedFiles.value = selectedFiles.value.filter((item) => item.path !== row.path);
    if (row.directory) {
      const prefix = row.path === '/' ? '/' : (row.path.endsWith('/') ? row.path : row.path + '/');
      excludedPaths.value = new Set(
        Array.from(excludedPaths.value).filter((p) => !p.startsWith(prefix))
      );
    }
    await refreshExpandedFiles();
  }
  const maxPage = Math.max(1, Math.ceil(expandedFiles.value.length / pageSize.value));
  pageNum.value = Math.min(pageNum.value, maxPage);
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

function reload() {
  browserRef.value?.reset();
}

function reset() {
  selectedFiles.value = [];
  expandedFiles.value = [];
  excludedPaths.value = new Set();
  pageNum.value = 1;
  browserRef.value?.reset();
  fileDialogVisible.value = false;
}

function validate() {
  return selectedFiles.value.length > 0
    ? Promise.resolve(true)
    : Promise.reject(new Error('请至少选择一个文件或文件夹'));
}

defineExpose({
  reload,
  reset,
  validate,
  getSelectedFiles: () => selectedFiles.value.map((item) => ({ ...item }))
});
</script>

<style scoped lang="scss">
.app-container {
  display: flex;
  min-height: 0;
  flex: 1;
  padding: 0px !important;
  background: #fff;
  box-sizing: border-box;

}
.app-container :deep(.el-dialog .el-dialog__body) {
  padding: 0px !important;
}

.selected-file-panel {
  min-height: 0;
  flex: 1;
  padding: 15px;
  background: #fff;
}

.section-heading {
  display: flex;
  align-items: center;
  margin: 8px 0;
  color: rgb(0 0 0 / 85%);
  font-size: 16px;
  font-weight: 500;

  &::before {
    display: inline-block;
    width: 6px;
    height: 16px;
    margin-right: 8px;
    background: var(--el-color-primary);
    border-radius: 3px;
    content: '';
  }
}

.selected-file-toolbar {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  margin-bottom: 15px;
}

.toolbar-tip-icon {
  margin-right: 2px;
  margin-left: 12px;
  color: #909399;
  font-size: 16px;
}

.toolbar-tip-text {
  color: #909399;
  font-size: 12px;
}
</style>

<style lang="scss">
.storage-file-select-dialog {
  .el-dialog__body {
    height: calc(88vh - 150px);
    min-height: 500px;
    padding: 0 !important;
    overflow: hidden !important;
    background: #f0f2f5;
  }

  .file-browser-container {
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    padding: 12px;
    overflow: hidden;
    background: #f0f2f5;

    .storage-file-step--page {
      height: 100%;
    }
  }
}
</style>

