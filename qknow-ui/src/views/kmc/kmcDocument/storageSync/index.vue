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
  <div class="app-container storage-sync-page">
    <div class="custom-card">
      <div class="steps-inner">
        <ul class="zl-step">
          <li
            v-for="(item, index) in stepsList"
            :key="item.id"
            :class="{
              statusEnd: activeStep === index,
              prevStep: index < activeStep,
              cur: index > activeStep,
            }"
          >
            <div
              class="step-circle"
              :class="{
                active: activeStep === index,
                prev: index < activeStep,
              }"
            >
              <span>{{ index + 1 }}</span>
            </div>
            <span class="step-name">{{ item.name }}</span>
          </li>
        </ul>
      </div>
    </div>

    <div class="page-content">
      <div class="content-main">
        <div v-show="activeStep === 0" class="connection-panel">
          <StorageConnectionStep
            ref="connectionStepRef"
            @change="handleConnectionChange"
          />
        </div>

        <StorageFileSelectStep
          v-show="activeStep === 1"
          ref="fileStepRef"
          :connection="connectionData"
          :supported-extensions="KG_SUPPORTED_EXTENSIONS"
        />

        <div v-show="activeStep === 2" class="document-panel">
          <div class="document-header">
            <div class="section-title">文件信息</div>
            <!-- <div class="document-tip">
              <el-icon class="document-tip-icon"><InfoFilled /></el-icon>
              <span
                >注：当所选文件中包含 JSON/JSONL
                的半结构化文件时，需要选择对应的数据风格。</span
              >
            </div> -->
          </div>
          <StorageDocumentStep
            ref="documentStepRef"
            :knowledge-base-id="route.params.kbId"
            :has-semi-structured="hasSemiStructured"
          />
        </div>
      </div>

      <div class="button-style">
        <el-button type="primary" @click="returnToList">返回列表</el-button>
        <el-button v-if="activeStep > 0" @click="goPrevious">上一步</el-button>
        <el-button
          v-if="activeStep === 0"
          type="primary"
          plain
          :loading="testingConnection"
          @click="handleTestConnection"
        >
          测试连接
        </el-button>
        <el-button
          v-if="activeStep === 0"
          :disabled="!connectionTestPassed"
          @click="goNext"
        >
          下一步
        </el-button>
        <el-button
          v-if="activeStep === 1"
          :loading="resolvingCandidates"
          @click="goNext"
        >
          下一步
        </el-button>
        <el-button
          v-if="activeStep === 2"
          type="primary"
          :loading="importingDocuments"
          @click="handleConfirm"
        >
          确定
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup name="KmcDocumentStorageSync">
import { nextTick, ref } from "vue";
import { ElMessage } from "element-plus";
import { InfoFilled } from "@element-plus/icons-vue";
import StorageConnectionStep from "@/components/ThirdStorage/StorageConnectionStep.vue";
import StorageFileSelectStep from "@/components/ThirdStorage/StorageFileSelectStep.vue";
import StorageDocumentStep from "@/components/ThirdStorage/StorageDocumentStep.vue";
import {
  importStorageDocuments,
  resolveStorageCandidates,
  testStorageConnection,
} from "@/api/kmc/storageSync/storageSync";

const route = useRoute();
const { proxy } = getCurrentInstance();
const KG_SUPPORTED_EXTENSIONS = [
  "txt",
  "pdf",
  "html",
  "xlsx",
  "xls",
  "docx",
  "csv",
  "md",
  "mdx",
  "htm",
  "markdown",
];
const stepsList = [
  { name: "数据连接", id: 0 },
  { name: "文件选择", id: 1 },
  { name: "文件信息", id: 2 },
];
const activeStep = ref(0);
const connectionStepRef = ref();
const fileStepRef = ref();
const documentStepRef = ref();
const connectionData = ref({});
const testingConnection = ref(false);
const connectionTestPassed = ref(false);
const testedConnectionFingerprint = ref("");
const selectedFiles = ref([]);
const candidateFiles = ref([]);
const hasSemiStructured = ref(false);
const resolvingCandidates = ref(false);
const importingDocuments = ref(false);

function getConnectionFingerprint(connection) {
  return JSON.stringify(connection || {});
}

function handleConnectionChange() {
  connectionTestPassed.value = false;
  testedConnectionFingerprint.value = "";
  fileStepRef.value?.reset();
  selectedFiles.value = [];
  candidateFiles.value = [];
  hasSemiStructured.value = false;
}

function closeAndReturnToList() {
  return proxy.$tab
    .closeOpenPage({ path: `/kmc/${route.params.kbId}/kmcDocument` })
    .then(() => proxy.$tab.refreshPage());
}

function returnToList() {
  proxy.$modal
    .confirm("返回列表后，当前填写及选择的数据将不会保留，是否确认返回？")
    .then(closeAndReturnToList)
    .catch(() => {});
}

function goPrevious() {
  if (activeStep.value > 0) activeStep.value -= 1;
}

async function goNext() {
  if (activeStep.value === 1) {
    await goToDocumentStep();
    return;
  }
  const currentConnection = connectionStepRef.value?.getFormData() || {};
  if (
    !connectionTestPassed.value ||
    testedConnectionFingerprint.value !==
      getConnectionFingerprint(currentConnection)
  ) {
    ElMessage.warning("请先完成连接测试");
    return;
  }

  try {
    await connectionStepRef.value?.validate();
  } catch {
    ElMessage.warning("请填写完整的数据连接信息");
    return;
  }

  connectionData.value = currentConnection;
  activeStep.value = 1;
  await nextTick();
  await fileStepRef.value?.reload();
}

async function goToDocumentStep() {
  try {
    await fileStepRef.value?.validate();
  } catch {
    ElMessage.warning("请至少选择一个文件或文件夹");
    return;
  }

  selectedFiles.value = fileStepRef.value?.getSelectedFiles() || [];
  resolvingCandidates.value = true;
  try {
    const response = await resolveStorageCandidates({
      connection: connectionData.value,
      selectedItems: selectedFiles.value.map(({ name, path, directory }) => ({
        name,
        path,
        directory,
      })),
    });
    const result = response?.data || response || {};
    candidateFiles.value = result.acceptedFiles || [];
    const rejectedFiles = result.rejectedFiles || [];
    hasSemiStructured.value = Boolean(result.hasSemiStructured);
    if (!candidateFiles.value.length) {
      ElMessage.warning("所选内容中没有系统支持的文件");
      return;
    }
    if (rejectedFiles.length) {
      ElMessage.warning(`已剔除 ${rejectedFiles.length} 个不支持格式的文件`);
    }
    activeStep.value = 2;
  } catch {
    // 请求层统一展示错误。
  } finally {
    resolvingCandidates.value = false;
  }
}

async function handleTestConnection() {
  if (testingConnection.value) return;
  try {
    await connectionStepRef.value?.validate();
  } catch {
    ElMessage.warning("请填写完整的数据连接信息");
    return;
  }

  connectionData.value = connectionStepRef.value?.getFormData() || {};
  const testingFingerprint = getConnectionFingerprint(connectionData.value);
  connectionTestPassed.value = false;
  testedConnectionFingerprint.value = "";
  testingConnection.value = true;
  try {
    const response = await testStorageConnection({
      connection: connectionData.value,
    });
    const currentConnection = connectionStepRef.value?.getFormData() || {};
    if (getConnectionFingerprint(currentConnection) === testingFingerprint) {
      connectionTestPassed.value = true;
      testedConnectionFingerprint.value = testingFingerprint;
      ElMessage.success(response?.msg || "连接测试成功");
    } else {
      ElMessage.warning("连接信息已变更，请重新测试");
    }
  } catch {
    // 请求层统一展示后端连接错误。
  } finally {
    testingConnection.value = false;
  }
}

async function handleConfirm() {
  try {
    await documentStepRef.value?.validate();
  } catch {
    ElMessage.warning("请填写完整的文件信息");
    return;
  }

  importingDocuments.value = true;
  try {
    const documentData = documentStepRef.value?.getFormData() || {};
    const response = await importStorageDocuments({
      connection: connectionData.value,
      selectedItems: selectedFiles.value.map(({ name, path, directory }) => ({
        name,
        path,
        directory,
      })),
      ...documentData,
    });
    ElMessage.success(
      response?.msg || `成功导入 ${candidateFiles.value.length} 个文件`
    );
    await closeAndReturnToList();
  } catch {
    // 请求层统一展示错误。
  } finally {
    importingDocuments.value = false;
  }
}
</script>

<style scoped lang="scss">
.storage-sync-page {
  height: 100%;
  min-height: 0;
  overflow: hidden;
  background: #f0f2f5;
}

.custom-card {
  width: 100%;
  height: 100px;
  padding: 34px 177px 26px 189px;
  margin-bottom: 15px;
  background: #fff;
  box-sizing: border-box;

  .steps-inner {
    display: flex;
    width: auto;
    padding: 0 10px 0 20px;
    color: #303133;
    transform: translateZ(0);
    transition: 0.3s;

    &::-webkit-scrollbar {
      height: 5px;
    }

    .zl-step {
      display: flex;
      width: 100%;
      height: 20px;
      align-items: flex-end;
      padding: 0;
      margin: 20px auto;
      list-style: none;
      cursor: pointer;

      li {
        position: relative;
        display: flex;
        height: 40px;
        flex: 1;
        align-items: center;
        justify-content: center;
        color: #666;
        background: #d7d8da;
        font-weight: 500;
        transition: background 0.3s;

        &:first-child {
          z-index: 2;
          clip-path: polygon(
            0 0,
            calc(100% - 20px) 0,
            100% 50%,
            calc(100% - 20px) 100%,
            0 100%
          );
        }

        &:not(:first-child):not(:last-child) {
          z-index: 1;
          margin-left: -10px;
          clip-path: polygon(
            0 0,
            calc(100% - 20px) 0,
            100% 50%,
            calc(100% - 20px) 100%,
            0 100%
          );

          &::before {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 2;
            width: 20px;
            height: 100%;
            background: #fff;
            clip-path: polygon(0 0, 100% 50%, 0 100%);
            content: "";
          }
        }

        &:last-child {
          z-index: 0;
          margin-left: -10px;
          clip-path: polygon(0 0, 100% 0, 100% 100%, 0 100%);

          &::before {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 2;
            width: 20px;
            height: 100%;
            background: #fff;
            clip-path: polygon(0 0, 100% 50%, 0 100%);
            content: "";
          }
        }

        &.statusEnd {
          color: #2666fb !important;
          background: linear-gradient(270deg, #e9effe 0%, #5589fa 100%);
        }

        &.prevStep {
          color: #2666fb !important;
          background: #e9effe !important;
          font-size: 16px !important;
          font-weight: normal;
        }

        &.cur {
          color: #404040;
          background: #f1f1f5;
          font-weight: 500;
        }
      }
    }

    .step-circle {
      display: inline-flex;
      width: 26px;
      height: 26px;
      flex-shrink: 0;
      align-items: center;
      justify-content: center;
      margin-right: 11px;
      color: #303133;
      background: #f1f1f5;
      border: 1px solid #b2b2b2;
      border-radius: 50%;
      font-size: 18px;
      font-weight: bold;
      transition: all 0.3s;

      &.active {
        color: #fff;
        background: #2666fb;
        border: 1px solid #fff;
      }

      &.prev {
        color: #2666fb !important;
        background: #f1f1f5 !important;
        border: 1px solid #2666fb !important;
      }
    }

    .step-name {
      font-family: "PingFang SC", sans-serif;
      font-size: 16px;
      font-weight: 500;
    }
  }
}

.page-content {
  display: flex;
  min-height: 0;
  flex: 1;
  flex-direction: column;
  padding: 0 !important;
  background: #fff;
}

.content-main {
  display: flex;
  min-height: 0;
  flex: 1;
  flex-direction: column;
}

.connection-panel {
  width: 100%;
  padding: 0 25px;
  margin: 0;
  background: #fff;
  box-sizing: border-box;
}

.document-panel {
  width: 100%;
  padding: 15px 25px 24px;
  margin: 0;
  background: #fff;
  box-sizing: border-box;
}

.section-title {
  display: flex;
  align-items: center;
  margin: 8px 0 8px 15px;
  color: rgba(0, 0, 0, 0.85);
  font-family: "PingFang SC", sans-serif;
  font-size: 16px;
  font-weight: 500;

  &::before {
    display: inline-block;
    width: 6px;
    height: 16px;
    margin-right: 8px;
    background: var(--el-color-primary);
    border-radius: 3px;
    content: "";
  }
}

.document-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.document-tip {
  display: flex;
  align-items: center;
  margin-left: 18px;
  color: #909399;
  font-size: 12px;
  line-height: 18px;

  .document-tip-icon {
    flex-shrink: 0;
    margin-right: 4px;
    font-size: 14px;
  }
}

.button-style {
  display: flex;
  height: 66px;
  flex-shrink: 0;
  align-items: flex-end;
  justify-content: flex-end;
  padding: 0 35px 25px 35px;
  background: #fff;
  box-sizing: border-box;
}
</style>
