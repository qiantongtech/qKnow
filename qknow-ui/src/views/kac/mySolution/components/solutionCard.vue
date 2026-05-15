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
    <div
      v-for="(item, index) in data"
      :key="item.id || index"
      class="card"
      @click="handleDetail(item)"
    >
      <div class="card-cover">
        <img
          :src="getImage(item)"
          alt="解决方案封面"
          class="card-cover-image"
        />

        <div class="card-top" v-if="props.source === 'myApp'">
          <div class="card-title-button">
            <el-popover placement="bottom" trigger="click">
              <template #reference>
                <el-button
                  type="primary"
                  link
                  @click.stop
                  class="custom-more-button"
                >
                  <el-icon class="more-icon"><More /></el-icon>
                </el-button>
              </template>
              <div class="card-button-group">
                <el-button
                  style="margin-left: 12px"
                  text
                  type="primary"
                  @click.stop="handleUpdate(item)"
                  v-hasPermi="['kac:solution:solution:edit']"
                >
                  <i
                    class="iconfont-mini icon-a-xiugaixianxing mr5"
                    style="margin-right: 8px"
                  ></i>
                  修改
                </el-button>
                <el-button
                  text
                  type="danger"
                  icon="Delete"
                  style="margin-left: 12px"
                  @click.stop="handleDelete(item)"
                  v-hasPermi="['kac:solution:solution:remove']"
                >
                  删除
                </el-button>
              </div>
            </el-popover>
          </div>
        </div>
      </div>

      <div class="card-content">
        <div class="card-title-row">
          <div class="card-title">{{ item.name || "" }}</div>
          <div
            class="card-date"
            v-if="
              props.variant !== 'overview' && getDisplayDate(item.createTime)
            "
          >
            <el-icon class="card-date-icon"><Clock /></el-icon>
            <span>{{ getDisplayDate(item.createTime) }}</span>
          </div>
        </div>

        <div class="card-tags">
          <el-tag
            v-for="(tag, tagIndex) in getTags(item)"
            :key="tagIndex"
            class="card-tag"
          >
            {{ tag.name }}
          </el-tag>
        </div>

        <div class="card-description">
          <el-text
            :ref="(el) => setDescriptionRef(index, el)"
            class="description-text"
            :line-clamp="2"
          >
            {{ item.description || "" }}
          </el-text>
        </div>

        <div class="card-meta" v-if="props.variant === 'overview'">
          <span class="card-heat">
            <img class="card-heat-icon" :src="heatFlameIcon" alt="" />
            <span>{{ formatCount(getHeatValue(item)) }}</span>
          </span>
          <span class="card-date">
            <el-icon class="card-date-icon"><Clock /></el-icon>
            <span>{{ getDisplayDate(item.createTime) || "-" }}</span>
          </span>
        </div>
      </div>
    </div>

    <el-dialog :title="title" v-model="open" width="800px">
      <el-form
        ref="solutionRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
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
  nextTick,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  toRefs,
  watch,
} from "vue";
import { Clock, More } from "@element-plus/icons-vue";
import GraphCover from "@/assets/kac/moren.jpg";
import heatFlameIcon from "@/assets/kac/overview/heat-flame.svg";
import { useRouter } from "vue-router";
// import { delSolution, updateSolution } from "@/api/kac/solution/solution";

const { proxy } = getCurrentInstance();
const router = useRouter();

const props = defineProps({
  data: {
    type: Array,
    required: true,
  },
  source: {
    type: String,
    default: "myApp",
    validator: (value) =>
      ["horizontal", "vertical", "myApp", "solution"].includes(value),
  },
  variant: {
    type: String,
    default: "overview",
    validator: (value) => ["default", "overview"].includes(value),
  },
});

const rules = reactive({
  name: [
    { required: true, message: "请输入名称", trigger: "blur" },
    { max: 128, message: "名称不能超过128个字符", trigger: "blur" },
  ],
  description: [
    { max: 512, message: "描述不能超过512个字符", trigger: "blur" },
  ],
  remark: [{ max: 512, message: "备注不能超过512个字符", trigger: "blur" }],
});

const open = ref(false);
const title = ref("");
const dataS = reactive({
  form: {},
});
const emit = defineEmits(["refresh"]);

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
  if (!row.coverImage) {
    return GraphCover;
  }
  return `${import.meta.env.VITE_APP_BASE_API}/profile${row.coverImage}`;
}

function getTags(row) {
  if (!row.tags) {
    return [];
  }
  try {
    const parsed = JSON.parse(row.tags);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function getDisplayDate(value) {
  return value ? String(value).slice(0, 10) : "";
}

function formatCount(value) {
  const count = Number(value || 0);
  if (count >= 10000) {
    return `${(count / 10000).toFixed(1).replace(/\.0$/, "")}w`;
  }
  if (count >= 1000) {
    return `${(count / 1000).toFixed(1).replace(/\.0$/, "")}k`;
  }
  return String(count);
}

function getHeatValue(row) {
  return row.heatValue ?? 0;
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

function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    name: null,
    description: null,
    coverImage: null,
    tags: null,
    status: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    myAppIds: null,
  };
  proxy.resetForm("solutionRef");
}

function handleDelete(row) {
  const _ids = row.id;
  proxy.$modal
    .confirm(`是否确认删除当前编号为"${_ids}"的数据项？`)
    .then(() => delSolution(_ids))
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
      emit("refresh");
    })
    .catch(() => {});
}

function handleUpdate(row) {
  // 通过事件通知父组件打开修改弹窗
  emit("edit", row);
}

function handleDetail(row) {
  if (props.source === "solution") {
    proxy.$modal.msgWarning("功能正在开发中");
    return;
  }

  const path = "/kac/mySolution/mySolutionDetail";
  router.push({
    path,
    query: {
      id: row.id,
    },
  });
}

function submitForm() {
  proxy.$refs.solutionRef.validate((valid) => {
    if (valid && form.value.id != null) {
      updateSolution(form.value)
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
  background: #ffffff;
  border: 1px solid #e8edf5;
  border-radius: 2px;
  overflow: hidden;
  cursor: pointer;
}

.card-cover {
  position: relative;
  height: 197px;
  background: linear-gradient(180deg, #f6f8fc 0%, #eef2f8 100%);
  border-bottom: 1px solid #eef1f6;
}

.card-cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-cover-empty {
  width: 100%;
  height: 100%;
}

.card-top {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 2;

  .card-title-button {
    .custom-more-button {
      width: 28px;
      height: 28px;
      padding: 0;
      border-radius: 50%;
      background: rgba(31, 35, 41, 0.58);
      color: #ffffff;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }
  }
}

.more-icon {
  font-size: 16px;
  line-height: 1;
}

:deep(.custom-more-button:hover) {
  color: #ffffff;
  background: rgba(43, 112, 244, 0.92);
}

.card-content {
  padding: 12px;
}

.card-title-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.card-title {
  flex: 1;
  min-width: 0;
  font-family: PingFang SC;
  font-weight: 700;
  font-size: 15px;
  line-height: 21px;
  color: #1f2329;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-date {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  line-height: 20px;
  color: #7d8797;
  white-space: nowrap;
  padding-top: 2px;
}

.card-date-icon {
  font-size: 13px;
}

.card-tags {
  min-height: 28px;
  margin-top: 8px;
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

.card-description {
  min-height: 44px;
  margin-top: 8px;
}

.description-text {
  width: 100%;
  line-height: 22px;
  font-family: PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: rgba(0, 0, 0, 0.85);
  word-break: break-all;
}

.card-meta {
  display: none;
}

.card-container--overview {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
  padding: 0;

  .card {
    border-color: #e5ebf5;
    border-radius: 4px;
    cursor: pointer;
  }

  .card-cover {
    height: 119px;
    border-bottom: 1px solid #eef1f6;
  }

  .card-content {
    padding: 16px 20px;
  }

  .card-title-row {
    display: block;
  }

  .card-title {
    display: block;
    color: #1f2937;
    font-size: 16px;
    font-weight: 600;
    line-height: 24px;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .card-tags {
    min-height: 24px;
    margin-top: 10px;
    gap: 10px;
  }

  .card-tag {
    height: 24px;
    padding: 0 14px;
    border-radius: 2px;
    font-size: 14px;
    font-weight: 400;
    line-height: 22px;
  }

  .card-description {
    min-height: 44px;
    margin-top: 12px;
  }

  .description-text {
    color: #4b5563;
    font-size: 14px;
    line-height: 22px;
  }

  .card-meta {
    display: flex;
    align-items: center;
    gap: 18px;
    margin-top: 12px;
    color: #9aa3af;
    font-size: 14px;
    line-height: 20px;
  }

  .card-heat,
  .card-date {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    height: 20px;
    padding-top: 0;
    line-height: 20px;

    span {
      display: block;
      height: 14px;
      line-height: 14px;
    }
  }

  .card-heat-icon {
    width: 14px;
    height: 14px;
    display: block;
    flex-shrink: 0;
    object-fit: contain;
    margin-top: -3px;
  }

  .card-date-icon {
    width: 14px;
    height: 14px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    font-size: 14px;
    line-height: 1;
  }

  :deep(.card-date-icon svg) {
    display: block;
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
  .card-cover {
    height: 136px;
  }

  .card-content {
    padding: 12px;
  }

  .card-title-row {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

