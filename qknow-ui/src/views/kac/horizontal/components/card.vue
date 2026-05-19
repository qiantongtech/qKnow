<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qKnow Knowledge Platform (Business Edition)
  Software Copyright Registration No. 15980140
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <div
    class="card-container"
    :class="{ 'card-container--overview': props.variant === 'overview' }"
  >
    <div v-for="(item, index) in data" :key="item.id || index" class="card">
      <div class="card-inner">
        <div class="card-title-row">
          <div class="card-title-info">
            <span class="card-title-icon">
              <img :src="getImage(item)" alt="应用图标" />
            </span>
            <div class="card-title-text">
              <div class="card-title">{{ item.name || "-" }}</div>
              <div
                class="card-tags card-tags--title"
                v-if="props.variant === 'overview' && getTags(item).length > 0"
              >
                <el-tag
                  v-for="(tag, tagIndex) in getTags(item)"
                  :key="tagIndex"
                  class="card-tag"
                >
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>
          </div>

          <div class="card-title-extra">
            <span
              v-if="props.source !== 'myApp'"
              class="status-pill"
              :class="getStatusClass(item.status)"
            >
              {{ getStatusLabel(item.status) }}
            </span>

            <div class="card-top" v-if="props.source === 'myApp'">
              <div class="card-title-button">
                <el-popover
                  placement="bottom"
                  trigger="click"
                  :width="96"
                  popper-class="card-more-popper"
                >
                  <template #reference>
                    <el-button
                      type="primary"
                      link
                      @click.stop
                      :class="[
                        'custom-more-button',
                        {
                          'custom-more-button--plain': props.source === 'myApp',
                        },
                      ]"
                    >
                      <el-icon class="more-icon"><More /></el-icon>
                    </el-button>
                  </template>
                  <div class="card-button-group">
                    <el-button
                      text
                      type="primary"
                      class="card-menu-btn"
                      @click.stop="handleUpdate(item)"
                      v-hasPermi="['kac:apply:apply:edit']"
                    >
                      <el-icon class="card-menu-icon"><EditPen /></el-icon>
                      修改
                    </el-button>
                    <el-button
                      text
                      type="danger"
                      class="card-menu-btn"
                      @click.stop="handleDelete(item)"
                      v-hasPermi="['kac:apply:apply:remove']"
                    >
                      <el-icon class="card-menu-icon"><Delete /></el-icon>
                      删除
                    </el-button>
                  </div>
                </el-popover>
              </div>
            </div>
          </div>
        </div>

        <el-divider class="card-divider" />

        <div class="card-body">
          <div class="card-body-main">
            <div class="card-description">
              <el-text
                :ref="(el) => setDescriptionRef(index, el)"
                class="description-text"
                :line-clamp="props.variant === 'overview' ? 2 : 3"
              >
                {{ item.description || "-" }}
              </el-text>
            </div>

            <div
              class="card-tags"
              v-if="props.variant !== 'overview' && getTags(item).length > 0"
            >
              <el-tag
                v-for="(tag, tagIndex) in getTags(item)"
                :key="tagIndex"
                class="card-tag"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </div>
        </div>

        <div class="card-actions">
          <el-button
            type="primary"
            class="card-action-btn"
            @click.stop="handleExperience(item)"
          >
            <el-icon class="card-action-icon"><VideoPlay /></el-icon>
            立即体验
          </el-button>
          <el-button
            type="primary"
            class="card-action-btn card-action-btn--detail"
            @click.stop="handleDetail(item)"
          >
            <el-icon class="card-action-icon"><View /></el-icon>
            查看详情
          </el-button>
        </div>
      </div>
    </div>

    <el-dialog :title="title" v-model="open" width="800px">
      <el-form ref="applyRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="来源应用" prop="source">
          <el-input
            v-model="form.source"
            placeholder="请输入来源应用名称"
            disabled
          />
        </el-form-item>
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入描述"
            maxlength="512"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注"
            maxlength="512"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
  getCurrentInstance,
  onBeforeUnmount,
  onMounted,
  nextTick,
  reactive,
  ref,
  toRefs,
  watch,
} from "vue";
import { ElMessage } from "element-plus";
import {
  Delete,
  EditPen,
  More,
  View,
  VideoPlay,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

import GraphCover from "@/assets/kac/wzbx.png";
// import {
//   delApply,
//   getByApplyIdId,
//   updateApply,
// } from "@/api/kac/apply/apply.js";
// import { listBot } from "@/api/kb/bot/bot.js";
// import { listKnowledgeBase } from "@/api/kmc/knowledgeBase/knowledgeBase.js";

const { proxy } = getCurrentInstance();
const router = useRouter();

const { kac_horizontal_status } = proxy.useDict("kac_horizontal_status");

const props = defineProps({
  data: {
    type: Array,
    required: true,
  },
  source: {
    type: String,
    default: "horizontal",
    validator: (value) => ["horizontal", "vertical", "myApp"].includes(value),
  },
  variant: {
    type: String,
    default: "overview",
    validator: (value) => ["default", "overview"].includes(value),
  },
});

const emit = defineEmits(["refresh"]);

const rules = reactive({
  name: [
    { required: true, message: "请输入应用名称", trigger: "blur" },
    { max: 100, message: "应用名称不能超过100个字符", trigger: "blur" },
  ],
});

const open = ref(false);
const title = ref("");
const dataS = reactive({
  form: {},
});
const knowledgeBaseList = ref([]);
const graphList = ref([]);
const botList = ref([]);
const dropdownLoaded = ref(false);

const { form } = toRefs(dataS);
const descriptionRefs = ref([]);
const overflowStates = ref({});

function setDescriptionRef(index, el) {
  const target = el?.$el ?? el;
  if (target) {
    descriptionRefs.value[index] = target;
  }
}

function checkOverflow() {
  nextTick(() => {
    descriptionRefs.value.forEach((el, index) => {
      if (el) {
        overflowStates.value[index] = el.scrollHeight > el.clientHeight;
      }
    });
  });
}

function getImage(row) {
  if (!row.icon) {
    return GraphCover;
  }
  return `${import.meta.env.VITE_APP_BASE_API}/profile${row.icon}`;
}

function getStatusOption(status) {
  const options = Array.isArray(kac_horizontal_status.value)
    ? kac_horizontal_status.value
    : [];
  return options.find((item) => String(item.value) === String(status));
}

function getStatusLabel(status) {
  return getStatusOption(status)?.label || "--";
}

function getStatusClass(status) {
  const option = getStatusOption(status);
  const label = option?.label || "";
  const tagType = option?.elTagType || "";

  if (
    ["warning", "danger", "info"].includes(tagType) ||
    label.includes("停") ||
    label.includes("禁")
  ) {
    return "status-pill--warning";
  }

  return "status-pill--primary";
}

function getTags(row) {
  if (!row.tags) {
    return [];
  }
  try {
    return JSON.parse(row.tags);
  } catch {
    return [];
  }
}

function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    pluginId: null,
    name: null,
    category: null,
    description: null,
    status: null,
    source: null,
    tags: null,
    useScene: null,
    useCount: null,
    kacApplyKnowledgeList: [],
    kacApplyGraphList: [],
    kacApplyBotList: [],
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("applyRef");
}

async function ensureDropdownData() {
  if (dropdownLoaded.value) {
    return;
  }

  const [knowledgeBaseRes, graphRes, botRes] = await Promise.all([
    listKnowledgeBase({ pageSize: 1000, pageNum: 1 }),
    listSimple({ pageSize: 1000, pageNum: 1 }),
    listBot({ pageSize: 1000, pageNum: 1 }),
  ]);

  knowledgeBaseList.value = knowledgeBaseRes.data.rows;
  graphList.value = graphRes.data.rows;
  botList.value = botRes.data.rows;
  dropdownLoaded.value = true;
}

function handleDelete(row) {
  const _ids = row.id;
  proxy.$modal
    .confirm(`是否确认删除当前应用编号为"${_ids}"的数据项？`)
    .then(() => delApply(_ids))
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
      emit("refresh");
    })
    .catch(() => {});
}

async function handleUpdate(row) {
  reset();
  await ensureDropdownData();
  const res = await getByApplyIdId(row.id);
  form.value = res.data;
  title.value = "修改应用";
  open.value = true;
}

// id 与页面类型的映射关系
const pageTypeMap = {
  2: "sunWeekMoonReports",
  3: "templateReports",
  4: "chat",
  5: "semanticSearch",
  6: "entityRelationSearch",
  7: "preciseSearch",
  8: "batchSearch",
};

function handleExperience(row) {
  ElMessage({
    message: "功能正在开发中",
    type: "warning",
  });
  return;
  // vertical 页面时，直接提示
  if (props.source === "vertical") {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }

  const id = Number(row.id);

  // myApp 时直接跳转
  if (props.source === "myApp") {
    const path = "/kac/myApp/pluginApply";
    router.push({
      path: path,
      query: {
        applyId: row.id,
        title: row.name,
      },
    });
    return;
  }

  // status 为 0 时，提示开发中
  if (String(row.status) === "0") {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }

  // id = 1: 正常跳转
  if (row.pluginId != null) {
    const path = "/kac/horizontal/pluginApply";
    router.push({
      path: path,
      query: {
        applyId: row.id,
        title: row.name,
      },
    });
    return;
  }

  // id = 2, 3: 使用动态路由跳转
  if (pageTypeMap[id]) {
    const pageType = pageTypeMap[id];
    const path = `/kac/horizontal/page/${pageType}`;
    router.push({
      path: path,
      query: {
        applyId: row.id,
        title: row.name,
      },
    });
    return;
  }

  // id >= 10 或其他情况，提示开发中
  ElMessage({
    message: "功能正在开发中",
    type: "warning",
  });
}

function handleDetail(row) {
  // vertical 页面时，直接提示
  if (props.source === "vertical") {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }

  const id = Number(row.id);

  // myApp 时直接跳转
  if (props.source === "myApp") {
    let path = "/kac/myApp/myAppDetail";
    router.push({
      path,
      query: {
        id: row.id,
      },
    });
    return;
  }

  // status 为 0 时，提示开发中
  if (String(row.status) === "0") {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }

  // id = 1: 正常跳转
  if (row.pluginId != null) {
    const path = "/kac/horizontal/horizontalDetail";
    router.push({
      path,
      query: {
        id: row.id,
      },
    });
    return;
  }

  // id = 2, 3: 提示暂无详情
  if (
    id === 2 ||
    id === 3 ||
    id === 4 ||
    id === 5 ||
    id === 6 ||
    id === 7 ||
    id === 8
  ) {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }

  // id >= 10，提示开发中
  if (id >= 10) {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }

  // 其他情况正常跳转
  const path = "/kac/horizontal/horizontalDetail";
  router.push({
    path,
    query: {
      id: row.id,
    },
  });
}

function submitForm() {
  proxy.$refs.applyRef.validate((valid) => {
    if (valid && form.value.id != null) {
      updateApply(form.value)
        .then(() => {
          proxy.$modal.msgSuccess("修改成功");
          emit("refresh");
          open.value = false;
        })
        .catch(() => {});
    }
  });
}

function cancel() {
  open.value = false;
  reset();
}

onMounted(() => {
  checkOverflow();
  window.addEventListener("resize", checkOverflow);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", checkOverflow);
});

watch(
  () => props.data,
  () => {
    descriptionRefs.value = [];
    overflowStates.value = {};
    checkOverflow();
  },
  { deep: true, immediate: true }
);
</script>

<style scoped lang="scss">
.card-container {
  display: grid;
  grid-template-columns: repeat(1, minmax(0, 1fr));
  gap: 14px;
  padding: 14px 0;
  width: 100%;
  box-sizing: border-box;
}

.card {
  min-width: 0;
  min-height: 220px;
  height: 100%;
  background: #ffffff;
  border-radius: 2px;
  cursor: default;
}

.card-inner {
  min-height: 220px;
  height: 100%;
  padding: 14px;
  display: flex;
  flex-direction: column;
}

.card-title-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: flex-start;
  gap: 12px;
}

.card-title-info {
  min-width: 0;
  width: 100%;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-title-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  overflow: hidden;
  background: #f4f6fb;
  border: 1px solid #eef1f6;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.card-title-text {
  min-width: 0;
}

.card-title {
  font-family: PingFang SC;
  font-weight: 800;
  font-size: 16px;
  line-height: 22px;
  color: #333333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-title-extra {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  min-width: max-content;
  justify-self: end;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  line-height: 18px;
  white-space: nowrap;

  &::before {
    content: "";
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: currentColor;
  }
}

.status-pill--primary {
  background: #edf3ff;
  color: #2b70f4;
}

.status-pill--warning {
  background: #f3f5f7;
  color: #7d8797;
}

.card-top {
  position: relative;
  z-index: 2;
}

.card-title-button {
  display: flex;
  align-items: center;
}

.card-button-group {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  width: 100%;
  gap: 2px;
}

.card-menu-btn {
  min-height: 34px;
  font-size: 14px;
  margin-left: 0px;
}

.card-menu-icon {
  margin-right: 8px;
  width: 14px;
  height: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  line-height: 1;
  flex-shrink: 0;
}

.custom-more-button {
  width: 28px;
  height: 28px;
  padding: 0;
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  background: rgba(31, 35, 41, 0.58);
}

.custom-more-button--plain {
  color: #8b95a6;
  background: transparent;
}

.more-icon {
  font-size: 16px;
  line-height: 1;
}

:deep(.custom-more-button:hover) {
  color: #ffffff;
  background: rgba(43, 112, 244, 0.92);
}

:deep(.custom-more-button--plain:hover) {
  color: #5f6b7c;
  background: transparent;
}

:deep(.card-more-popper.el-popover) {
  min-width: 96px !important;
  padding: 6px;
}

:deep(.card-more-popper .el-button) {
  width: 100%;
  margin-left: 0 !important;
  justify-content: flex-start;
  padding: 8px 10px;
  line-height: 20px;
  display: flex;
  align-items: center;
}

:deep(.card-more-popper .el-button > span) {
  width: 100%;
  display: inline-flex;
  align-items: center;
  line-height: 20px;
}

:deep(.card-more-popper .el-button + .el-button) {
  margin-top: 0;
}

.card-divider {
  margin: 8px 0;
}

.card-body {
  flex: 1;
  min-width: 0;
}

.card-body-main {
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.card-description {
  height: 60px;
}

.description-text {
  width: 100%;
  line-height: 19px;
  font-family: PingFang SC;
  font-weight: 700;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.85);
  word-break: break-all;
}

.card-tags {
  min-height: 24px;
  margin-top: 6px;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  gap: 6px;
}

.card-tag {
  margin: 0;
  border: none !important;
  background: #eaf3ff !important;
  color: #2b70f4 !important;
  font-family: PingFang SC;
  font-weight: 700;
}

.card-actions {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #e8edf5;
  display: flex;
  gap: 10px;
}

.card-action-btn {
  flex: 1;
  min-width: 0;
  height: 30px;
  padding: 0 12px;
  border-radius: 2px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-family: PingFang SC;
  font-weight: 400;
  font-size: 12px;
}

.card-action-icon {
  margin-right: 6px;
  font-size: 14px;
}

:deep(.card-action-btn.el-button--primary) {
  border: none;
  background: linear-gradient(90deg, #3d83ff 0%, #266cf4 100%);
}

:deep(.card-action-btn--detail.el-button--primary) {
  color: #2b70f4;
  border: 1px solid #5185fc;
  background: #ffffff;
  box-shadow: none;
}

:deep(.card-action-btn--detail.el-button--primary:hover) {
  color: #ffffff;
  border-color: #5185fc;
  background: #5185fc;
}

:deep(.card-action-btn--detail.el-button--primary:focus) {
  color: #2b70f4;
  border-color: #5185fc;
  background: #ffffff;
}

:deep(.card-actions .el-button + .el-button) {
  margin-left: 0;
}

:deep(.card-action-btn.is-disabled) {
  opacity: 0.65;
}

.card-container--overview {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
  padding: 0;

  .card {
    min-height: 196px;
    border: 1px solid #e5ebf5;
    border-radius: 4px;
    box-shadow: none;
  }

  .card-inner {
    min-height: 196px;
    padding: 20px;
  }

  .card-title-row {
    align-items: flex-start;
    gap: 12px;
  }

  .card-title-icon {
    width: 58px;
    height: 58px;
    border: 0;
    border-radius: 8px;
  }

  .card-title {
    font-size: 16px;
    font-weight: 600;
    line-height: 24px;
    color: #111827;
  }

  .status-pill {
    height: 24px;
    padding: 0 10px;
    border: 1px solid #a8e8b7;
    background: #eafbf0;
    color: #20b759;
    font-size: 14px;
    font-weight: 400;
  }

  .status-pill--warning {
    border-color: #d8dce6;
    background: #f5f7fa;
    color: #909399;
  }

  .card-tags--title {
    min-height: 24px;
    margin-top: 8px;
  }

  .card-tag {
    height: 24px;
    padding: 0 14px;
    border-radius: 2px;
    font-size: 14px;
    font-weight: 400;
    line-height: 22px;
  }

  .card-divider {
    display: none;
  }

  .card-body {
    margin-top: 12px;
  }

  .card-description {
    height: 44px;
  }

  .description-text {
    font-size: 14px;
    font-weight: 400;
    line-height: 22px;
    color: #4b5563;
    display: -webkit-box;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
  }

  .card-actions {
    margin-top: 14px;
    padding-top: 0;
    border-top: 0;
    gap: 20px;
  }

  .card-action-btn {
    height: 30px;
    border-radius: 4px;
    font-size: 14px;
  }

  .card-action-icon {
    display: none;
  }
}

@media (min-width: 768px) {
  .card-container {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .card-container--overview {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (min-width: 1280px) {
  .card-container {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }

  .card-container--overview {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
}

@media (max-width: 767px) {
  .card-inner {
    min-height: 206px;
    padding: 14px;
  }

  .card-title-row {
    grid-template-columns: minmax(0, 1fr);
  }

  .card-title-extra {
    width: 100%;
    min-width: 0;
    justify-content: space-between;
    justify-self: stretch;
  }

  .card-actions {
    flex-direction: column;
  }
}
</style>
