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
    class="app-container app-detail-page"
    ref="appContainer"
    v-loading="loading"
  >
    <section class="detail-hero" :style="{ backgroundImage: `url(${heroBg})` }">
      <div class="hero-main">
        <div class="app-logo">
          <img :src="getAppIcon(applyDetail)" alt="" />
        </div>
        <div class="hero-info">
          <div class="hero-title-row">
            <h1>{{ applyDetail.name || "-" }}</h1>
            <span class="status-pill" :class="{ disabled: isDisabled }">
              <i></i>
              {{ statusText }}
            </span>
            <span class="category-text">{{ categoryLabel }}</span>
          </div>
          <p class="hero-desc">
            {{ applyDetail.description || "暂无应用描述" }}
          </p>
          <div class="hero-tags">
            <span v-for="tag in displayTags" :key="tag.name" class="hero-tag">
              {{ tag.name }}
            </span>
          </div>
        </div>
      </div>
      <div class="hero-actions">
        <el-button
          type="primary"
          plain
          icon="CopyDocument"
          :disabled="isDisabled"
          @click="showCopyDialog('copy')"
          @mousedown="(e) => e.preventDefault()"
        >
          复制应用
        </el-button>
        <el-button
          type="primary"
          icon="Promotion"
          :disabled="isDisabled && applyDetail.myApplyFlag === false"
          @click="handleUse(applyDetail)"
          @mousedown="(e) => e.preventDefault()"
        >
          立即体验
        </el-button>
      </div>
    </section>

    <div
      class="detail-content"
      :class="{
        'detail-content--single': !visibleMountedResourceGroups.length,
      }"
    >
      <main class="main-panel" ref="mainPanelRef">
        <section class="content-section">
          <div class="section-title">
            <span></span>
            核心价值
          </div>
          <div
            class="value-card"
            :style="{ backgroundImage: `url(${valueBg})` }"
          >
            <p>
              文章编写应用的核心价值在于大幅<span>降低创作门槛</span>并显著提升内容产出效率。它通过<span>智能辅助</span>与实时纠错，帮助创作者快速跨越构思与表达的障碍，将繁琐的文字组织工作自动化。最终，插件让创作者得以从重复性的劳动中解放出来，将更多<span>精力聚焦</span>于核心观点的创新与情感价值的传递。
            </p>
            <div class="value-metrics">
              <div v-for="item in coreMetrics" :key="item.title">
                <strong>{{ item.title }}</strong>
                <small>{{ item.desc }}</small>
              </div>
            </div>
          </div>
        </section>

        <section class="content-section">
          <div class="section-title">
            <span></span>
            核心能力优势
          </div>
          <div class="ability-grid">
            <article
              v-for="item in capabilityCards"
              :key="item.title"
              class="ability-card"
            >
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
            </article>
          </div>
        </section>

        <section class="content-section scene-section">
          <div class="section-title">
            <span></span>
            使用场景
          </div>
          <div class="scene-grid">
            <article
              v-for="item in scenarioCards"
              :key="item.title"
              class="scene-card"
            >
              <div class="scene-card-cover">
                <img :src="item.image" alt="" />
              </div>
              <div class="scene-card-content">
                <h3 class="scene-card-title">{{ item.title }}</h3>
                <div class="scene-card-tags">
                  <el-tag
                    v-for="tag in item.tags"
                    :key="tag"
                    class="scene-card-tag"
                  >
                    {{ tag }}
                  </el-tag>
                </div>
                <p class="scene-card-desc">{{ item.desc }}</p>
                <div class="scene-card-meta">
                  <span class="scene-card-heat">
                    <img
                      class="scene-card-heat-icon"
                      :src="heatFlameIcon"
                      alt=""
                    />
                    <span>{{ item.heatValue }}</span>
                  </span>
                  <span class="scene-card-date">
                    <el-icon class="scene-card-date-icon"><Clock /></el-icon>
                    <span>{{ item.createTime }}</span>
                  </span>
                </div>
              </div>
            </article>
          </div>
        </section>
      </main>

      <aside
        v-if="visibleMountedResourceGroups.length"
        class="resource-panel"
        :style="
          mainPanelHeight ? { maxHeight: `${mainPanelHeight}px` } : undefined
        "
      >
        <div class="section-title resource-title">
          <span></span>
          挂载资源
        </div>
        <section
          v-for="group in visibleMountedResourceGroups"
          :key="group.type"
          class="resource-group"
        >
          <div class="resource-group-title">
            <span class="resource-icon-wrap" :class="group.type">
              <img :src="group.icon" alt="" />
            </span>
            {{ group.title }}
          </div>
          <div class="resource-list">
            <article
              v-for="item in group.items"
              :key="`${group.type}-${item.relationId || item.id}`"
              class="resource-card"
              :class="group.type"
            >
              <span class="resource-card-icon" :class="group.type">
                <img :src="group.cardIcon" alt="" />
              </span>
              <div>
                <h3>{{ item.name }}</h3>
                <p>{{ item.description }}</p>
              </div>
              <el-button
                v-if="isMyAppSource"
                type="primary"
                plain
                icon="Switch"
                @click="openResourceDialog(group.type)"
              >
                切换
              </el-button>
            </article>
          </div>
        </section>
      </aside>
    </div>

    <el-dialog
      :title="resourceDialogTitle"
      v-model="resourceDialogOpen"
      width="1100px"
      append-to-body
      destroy-on-close
      class="resource-manager-dialog"
      @closed="handleResourceDialogClosed"
    >
      <Kmc
        v-if="resourceDialogOpen && resourceDialogType === 'knowledge'"
        :applyId="applyDetail.id"
        :knowledgeBaseList="knowledgeBaseList"
        :source="getSource()"
        :key="`knowledge-${applyDetail.id}`"
      />
      <Kg
        v-if="resourceDialogOpen && resourceDialogType === 'graph'"
        :applyId="applyDetail.id"
        :graphList="graphList"
        :source="getSource()"
        :key="`graph-${applyDetail.id}`"
      />
      <Bot
        v-if="resourceDialogOpen && resourceDialogType === 'bot'"
        :applyId="applyDetail.id"
        :botList="botList"
        :source="getSource()"
        :key="`bot-${applyDetail.id}`"
      />
    </el-dialog>

    <el-dialog :title="title" v-model="open" width="800px" draggable>
      <el-form ref="applyRef" :model="form" :rules="rules" label-width="80px">
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

<script setup name="Horizontal">
import { useRoute, useRouter } from "vue-router";
import {
  computed,
  getCurrentInstance,
  nextTick,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  toRefs,
  watch,
} from "vue";
import { Clock } from "@element-plus/icons-vue";
import Kmc from "@/views/kac/horizontal/detail/kmc.vue";
import Kg from "@/views/kac/horizontal/detail/kg.vue";
import Bot from "@/views/kac/horizontal/detail/bot.vue";
import {
  copy,
  getApply,
  getByApplyIdId,
  updateApply,
} from "@/api/kac/apply/apply.js";
import { listKnowledge as listKacKnowledge } from "@/api/kac/applyKnowledge/applyKnowledge.js";
import { listKacGraph } from "@/api/kac/applyGraph/applyGraph.js";
import { listKacBot } from "@/api/kac/applyBot/applyBot.js";
import { listKnowledgeBase } from "@/api/kmc/knowledgeBase/knowledgeBase.js";
// import { listSimple } from "@/api/kg/graph/graph.js";
import { listBot } from "@/api/kb/bot/bot.js";
import { ElMessage } from "element-plus";

import heroBg from "@/assets/kac/app-detail/hero-banner.png";
import valueBg from "@/assets/kac/app-detail/value-bg.png";
import appFeather from "@/assets/kac/app-detail/app-feather.png";
import sceneMedia from "@/assets/kac/app-detail/scene-media.png";
import sceneWriting from "@/assets/kac/app-detail/scene-writing.png";
import sceneTranslation from "@/assets/kac/app-detail/scene-translation.png";
import heatFlameIcon from "@/assets/kac/overview/heat-flame.svg";
import kbIcon from "@/assets/kac/app-detail/icon-kb.png";
import iconKbCard from "@/assets/kac/app-detail/icon-kb-card.png";
import graphIcon from "@/assets/kac/app-detail/icon-graph.png";
import botIcon from "@/assets/kac/app-detail/icon-bot.png";
import botCardIcon from "@/assets/kac/app-detail/icon-bot-card.png";

const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();

const id = ref(route.query.id || 1);
const loading = ref(false);
const open = ref(false);
const title = ref("");
const resourceDialogOpen = ref(false);
const resourceDialogType = ref("knowledge");
const appContainer = ref(null);
const mainPanelRef = ref(null);
const mainPanelHeight = ref(0);
let mainPanelResizeObserver = null;

const knowledgeBaseList = ref([]);
const graphList = ref([]);
const botList = ref([]);
const mountedKnowledgeList = ref([]);
const mountedGraphList = ref([]);
const mountedBotList = ref([]);

const rules = reactive({
  name: [
    { required: true, message: "请输入应用名称", trigger: "blur" },
    { max: 100, message: "应用名称不能超过100个字符", trigger: "blur" },
  ],
});

const data = reactive({
  applyDetail: {},
  form: {},
});
const { applyDetail, form } = toRefs(data);

const coreMetrics = [
  { title: "提升效率", desc: "智能生成，高效创作" },
  { title: "降低门槛", desc: "简化流程，轻松上手" },
  { title: "智能辅助", desc: "实时优化，质量提升" },
];

const capabilityCards = [
  {
    title: "知识库精准赋能",
    desc: "关联垂直领域知识库，为AI提供专业语料支撑，让生成内容言之有物。",
  },
  {
    title: "多Bot协同作业",
    desc: "集成多个功能型Bot分工协作，从选题构思到最终润色，一站式完成。",
  },
  {
    title: "多场景模板生成",
    desc: "内置新媒体、电商、办公等多种场景模板，一键生成各类文章。",
  },
  {
    title: "智能润色与纠错",
    desc: "实时识别语法错误并优化文笔，根据目标风格自动调整。",
  },
];

const scenarioCards = [
  {
    title: "新媒体内容创作",
    desc: "辅助生成标题与大纲，撰写公众号、小红书文案，缩短成稿周期。",
    image: sceneMedia,
    tags: ["写作", "内容"],
    heatValue: "1.2k",
    createTime: "2026.05.09",
  },
  {
    title: "文学与创意写作",
    desc: "提供情节与人物描写灵感，帮助创作者突破卡文瓶颈，激发创意。",
    image: sceneWriting,
    tags: ["创意", "润色"],
    heatValue: "980",
    createTime: "2026.05.09",
  },
  {
    title: "跨境电商与翻译",
    desc: "协助撰写地道外语详情页与广告语，纠正语法，符合目标市场习惯。",
    image: sceneTranslation,
    tags: ["翻译", "电商"],
    heatValue: "860",
    createTime: "2026.05.09",
  },
];

const isDisabled = computed(() => Number(applyDetail.value.status) === 0);
const statusText = computed(() => (isDisabled.value ? "停用" : "正常"));
const isMyAppSource = computed(() => getSource() === "myApp");
const categoryLabel = computed(() => {
  if (Number(applyDetail.value.category) === 1) {
    return "纵向行业应用";
  }
  if (Number(applyDetail.value.category) === 0) {
    return "横向通用应用";
  }
  return "-";
});
const displayTags = computed(() => {
  const tags = getTags(applyDetail.value);
  return tags.length ? tags : [];
});

function getAppIcon(row = {}) {
  // if (!row.icon) {
  //   return appFeather;
  // }
  return `${import.meta.env.VITE_APP_BASE_API}/profile${row.icon}`;
}

const resourceDialogTitle = computed(() => {
  const map = {
    knowledge: "切换关联知识库",
    graph: "切换关联知识图谱",
    bot: "切换关联 Bot",
  };
  return map[resourceDialogType.value] || "切换挂载资源";
});
const knowledgeResourceItems = computed(() =>
  normalizeResourceRows(
    getMountedRows(
      mountedKnowledgeList.value,
      applyDetail.value.kacApplyKnowledgeList
    ),
    knowledgeBaseList.value,
    ["knowledgeId", "knowledgeBaseId", "kbId", "id"],
    ["name", "knowledgeName", "knowledgeBaseName"],
    ["description", "remark"],
    "暂无挂载描述"
  )
);
const graphResourceItems = computed(() =>
  normalizeResourceRows(
    getMountedRows(mountedGraphList.value, applyDetail.value.kacApplyGraphList),
    graphList.value,
    ["graphId", "kgId", "id"],
    ["name", "graphName"],
    ["description", "remark"],
    "暂无挂载描述"
  )
);
const botResourceItems = computed(() =>
  normalizeResourceRows(
    getMountedRows(mountedBotList.value, applyDetail.value.kacApplyBotList),
    botList.value,
    ["botId", "id"],
    ["name", "botName"],
    ["description", "remark"],
    "暂无挂载描述"
  )
);
const mountedResourceGroups = computed(() => [
  {
    type: "knowledge",
    title: "知识库",
    icon: kbIcon,
    cardIcon: iconKbCard,
    items: knowledgeResourceItems.value,
  },
  {
    type: "graph",
    title: "知识图谱",
    icon: graphIcon,
    cardIcon: graphIcon,
    items: graphResourceItems.value,
  },
  {
    type: "bot",
    title: "Bot",
    icon: botIcon,
    cardIcon: botCardIcon,
    items: botResourceItems.value,
  },
]);
const visibleMountedResourceGroups = computed(() =>
  mountedResourceGroups.value.filter((group) => group.items.length > 0)
);

function getSource() {
  const routeName = route.name;
  if (routeName === "myAppDetail") {
    return "myApp";
  }
  if (routeName === "verticalDetail") {
    return "vertical";
  }
  return "horizontal";
}

function updateMainPanelHeight() {
  if (!mainPanelRef.value) {
    mainPanelHeight.value = 0;
    return;
  }
  mainPanelHeight.value = Math.ceil(mainPanelRef.value.offsetHeight);
}

onMounted(() => {
  nextTick(() => {
    updateMainPanelHeight();
    if (window.ResizeObserver && mainPanelRef.value) {
      mainPanelResizeObserver = new ResizeObserver(updateMainPanelHeight);
      mainPanelResizeObserver.observe(mainPanelRef.value);
    }
  });
});

onBeforeUnmount(() => {
  if (mainPanelResizeObserver) {
    mainPanelResizeObserver.disconnect();
    mainPanelResizeObserver = null;
  }
});

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

function showCopyDialog(action) {
  reset();
  const _id = applyDetail.value.id;
  getByApplyIdId(_id).then((res) => {
    form.value = res.data;
    if (action === "copy") {
      title.value = "复制应用";
      form.value.id = null;
      form.value.name = `${form.value.name || ""}（副本）`;
    }
    open.value = true;
  });
}

watch(
  () => route.query.id,
  (newId) => {
    id.value = newId || 1;
    getApplyDetailById();
  },
  { immediate: true }
);

async function getApplyDetailById() {
  loading.value = true;
  try {
    const response = await getApply(id.value);
    applyDetail.value = response.data || {};
    await loadMountedResources();
  } finally {
    loading.value = false;
  }
}

function loadMountedResources() {
  const params = { pageSize: 1000, pageNum: 1, applyId: id.value };
  return Promise.allSettled([
    listKacKnowledge(params).then((res) => {
      mountedKnowledgeList.value = res.data?.rows || [];
    }),
    listKacGraph(params).then((res) => {
      mountedGraphList.value = res.data?.rows || [];
    }),
    listKacBot(params).then((res) => {
      mountedBotList.value = res.data?.rows || [];
    }),
  ]);
}

function loadDropdownData() {
  listKnowledgeBase({ pageSize: 1000, pageNum: 1 }).then((res) => {
    knowledgeBaseList.value = res.data?.rows || [];
  });

  // listSimple({ pageSize: 1000, pageNum: 1 }).then((res) => {
  //   graphList.value = res.data?.rows || [];
  // });

  listBot({ pageSize: 1000, pageNum: 1 }).then((res) => {
    botList.value = res.data?.rows || [];
  });
}

function getTags(row) {
  if (!row?.tags) {
    return [];
  }
  if (Array.isArray(row.tags)) {
    return row.tags;
  }
  try {
    return JSON.parse(row.tags);
  } catch {
    return [];
  }
}

function getFirstValue(source, keys) {
  if (!source) {
    return "";
  }
  for (const key of keys) {
    const value = source[key];
    if (value !== undefined && value !== null && value !== "") {
      return value;
    }
  }
  return "";
}

function getMountedRows(primaryRows, fallbackRows) {
  if (Array.isArray(primaryRows) && primaryRows.length) {
    return primaryRows;
  }
  return Array.isArray(fallbackRows) ? fallbackRows : [];
}

function normalizeResourceRows(
  relationRows,
  allRows,
  idKeys,
  nameKeys,
  descKeys,
  fallbackDescription
) {
  return (relationRows || []).map((row) => {
    const resourceId = getFirstValue(row, idKeys);
    const matched = (allRows || []).find((item) => item.id == resourceId) || {};
    const name =
      getFirstValue(matched, nameKeys) || getFirstValue(row, nameKeys) || "-";
    const description = getFirstValue(row, descKeys) || fallbackDescription;
    return {
      relationId: row.id,
      id: resourceId || row.id,
      name,
      description,
    };
  });
}

function handleUse(row) {
  let path = "";
  const source = getSource();

  if (source === "vertical") {
    path = "/kac/vertical/pluginApply";
  } else if (source === "horizontal") {
    path = "/kac/horizontal/pluginApply";
  } else if (source === "myApp") {
    path = "/kac/myApp/pluginApply";
  } else if (row.pluginId != null) {
    path = "/kac/horizontal/pluginApply";
  } else {
    ElMessage({
      message: "功能正常开发中",
      type: "warning",
    });
    return;
  }
  router.push({
    path,
    query: {
      applyId: applyDetail.value.id,
      title: applyDetail.value.name,
    },
  });
}

function openResourceDialog(type) {
  resourceDialogType.value = type;
  resourceDialogOpen.value = true;
}

function handleResourceDialogClosed() {
  loadMountedResources();
}

function cancel() {
  open.value = false;
  reset();
}

function submitForm() {
  proxy.$refs["applyRef"].validate((valid) => {
    if (!valid) {
      return;
    }
    if (form.value.id != null) {
      updateApply(form.value).then(() => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getApplyDetailById();
      });
      return;
    }
    form.value.source = applyDetail.value.name;
    form.value.updateBy = null;
    form.value.updateTime = null;
    copy(form.value).then((response) => {
      proxy.$modal.msgSuccess("复制成功");
      open.value = false;
      const newId = response.data;
      proxy.$router.push({
        name: "myAppDetail",
        query: { id: newId },
      });
    });
  });
}

loadDropdownData();
</script>

<style scoped lang="scss">
.app-detail-page {
  background: #f0f2f5;
  color: #101828;
}

.detail-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 150px;
  padding: 20px 50px 20px 30px;
  background-color: #f7fbff;
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  border-radius: 0;
}

.hero-main {
  display: flex;
  align-items: center;
  min-width: 0;
}

.app-logo {
  width: 64px;
  height: 64px;
  flex: 0 0 64px;
  overflow: hidden;
  border-radius: 6px;
  // background: linear-gradient(135deg, #8eb6ff, #4c75ff);

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.hero-info {
  min-width: 0;
  margin-left: 30px;
}

.hero-title-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px 16px;

  h1 {
    margin: 0;
    color: #1f2937;
    font-size: 28px;
    font-weight: 600;
    line-height: 34px;
    font-size: 24px;
  }
}

.status-pill {
  display: inline-flex;
  align-items: center;
  height: 24px;
  padding: 0 10px;
  border: 1px solid #9ee4b0;
  border-radius: 12px;
  background: #eaffef;
  color: #21b352;
  font-size: 14px;

  i {
    width: 6px;
    height: 6px;
    margin-right: 6px;
    border-radius: 50%;
    background: currentColor;
  }

  &.disabled {
    border-color: #d8dce6;
    background: #f5f7fa;
    color: #909399;
  }
}

.category-text {
  color: #6b7280;
  font-size: 15px;
}

.hero-desc {
  max-width: 980px;
  margin: 12px 0 16px;
  color: #1f2937;
  font-size: 16px;
  line-height: 24px;
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.hero-tag {
  display: inline-flex;
  align-items: center;
  height: 30px;
  padding: 0 18px;
  border-radius: 2px;
  background: rgba(38, 102, 251, 0.08);
  color: #2666fb;
  font-size: 14px;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-left: 24px;

  :deep(.el-button) {
    height: 40px;
    padding: 0 20px;
    border-radius: 4px;
    font-size: 14px;
  }
}

.detail-content {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 540px;
  align-items: start;
  gap: 16px;
  margin-top: 16px;
}

.detail-content--single {
  grid-template-columns: minmax(0, 1fr);
}

.main-panel,
.resource-panel {
  background: #fff;
}

.main-panel {
  padding: 20px;
}

.resource-panel {
  padding: 20px;
  align-self: start;
  overflow-y: auto;
  scrollbar-gutter: stable;
}

.content-section + .content-section {
  margin-top: 18px;
  padding-top: 18px;
  border-top: 1px solid #edf0f5;
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;

  span {
    width: 6px;
    height: 16px;
    margin-right: 10px;
    border-radius: 3px;
    background: #2666fb;
  }
}

.value-card {
  min-height: 174px;
  padding: 20px;
  background-color: #f7fbff;
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  border-radius: 4px;

  p {
    // max-width: 920px;
    margin: 0;
    color: #374151;
    font-size: 16px;
    line-height: 32px;
  }

  span {
    color: #2666fb;
  }
}

.value-metrics {
  display: grid;
  grid-template-columns: repeat(3, 170px);
  gap: 20px;
  margin-top: 18px;

  div {
    height: 60px;
    padding: 9px 12px;
    border-radius: 4px;
    background: linear-gradient(
      180deg,
      rgba(255, 255, 255, 0.95),
      rgba(232, 241, 255, 0.95)
    );
    text-align: center;
  }

  strong,
  small {
    display: block;
  }

  strong {
    color: #1f2937;
    font-size: 14px;
    line-height: 22px;
  }

  small {
    color: #6b7280;
    font-size: 12px;
    line-height: 18px;
  }
}

.ability-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
}

.ability-card {
  min-height: 116px;
  padding: 20px;
  border: 1px solid #e5ebf5;
  border-radius: 4px;
  background: linear-gradient(180deg, #fff, #f8fbff);

  h3 {
    margin: 0 0 10px;
    color: #1f2937;
    font-size: 16px;
    font-weight: 600;
    line-height: 22px;
  }

  p {
    margin: 0;
    color: #4b5563;
    font-size: 14px;
    line-height: 24px;
  }
}

.scene-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
}

.scene-card {
  min-width: 0;
  overflow: hidden;
  border: 1px solid #e5ebf5;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
}

.scene-card-cover {
  height: 119px;
  border-bottom: 1px solid #eef1f6;
  background: linear-gradient(180deg, #f6f8fc 0%, #eef2f8 100%);
}

.scene-card-cover img {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.scene-card-content {
  padding: 16px 20px;
}

.scene-card-title {
  margin: 0;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  line-height: 24px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.scene-card-tags {
  min-height: 24px;
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  gap: 10px;
}

.scene-card-tag {
  height: 24px;
  margin: 0;
  padding: 0 14px;
  border: none !important;
  border-radius: 2px;
  background: #eaf3ff !important;
  color: #2b70f4 !important;
  font-size: 14px;
  font-weight: 400;
  line-height: 22px;
}

.scene-card-desc {
  min-height: 44px;
  margin: 12px 0 0;
  color: #4b5563;
  font-size: 14px;
  line-height: 22px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.scene-card-meta {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-top: 12px;
  color: #9aa3af;
  font-size: 14px;
  line-height: 20px;
}

.scene-card-heat,
.scene-card-date {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  height: 20px;
  line-height: 20px;

  span {
    display: block;
    height: 14px;
    line-height: 14px;
  }
}

.scene-card-heat-icon {
  width: 14px;
  height: 14px;
  display: block;
  flex-shrink: 0;
  object-fit: contain;
  margin-top: -3px;
}

.scene-card-date-icon {
  width: 14px;
  height: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: -1px;
  color: #9aa3af;
  font-size: 14px;
}

:deep(.scene-card-date-icon svg) {
  width: 14px;
  height: 14px;
}

.resource-title {
  margin-bottom: 28px;
}

.resource-group + .resource-group {
  margin-top: 22px;
}

.resource-group-title {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.resource-icon-wrap {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  margin-right: 12px;
  border-radius: 4px;

  &.knowledge {
    background: rgba(38, 102, 251, 0.08);
  }

  &.graph {
    background: rgba(248, 136, 37, 0.08);
  }

  &.bot {
    background: rgba(38, 202, 85, 0.08);
  }

  img {
    width: 20px;
    height: 20px;
    object-fit: contain;
  }
}

.resource-list {
  display: grid;
  gap: 20px;
}

.resource-card {
  display: grid;
  grid-template-columns: 54px minmax(0, 1fr) auto;
  align-items: center;
  column-gap: 20px;
  min-height: 100px;
  padding: 18px 18px 18px 20px;
  border: 1px solid #e5ebf5;
  border-radius: 4px;
  background: #fff;

  :deep(.el-button) {
    height: 30px;
    border-radius: 4px;
  }
}

.resource-card {
  &.knowledge {
    background: linear-gradient(180deg, #fff, #fbfdff);
  }

  &.graph {
    border-color: #f4e7d6;
    background: linear-gradient(180deg, #fff, #fffaf4);
  }

  &.bot {
    border-color: #dff3e6;
    background: linear-gradient(180deg, #fff, #f9fffb);
  }

  h3 {
    margin: 0 0 8px;
    overflow: hidden;
    color: #1f2937;
    font-size: 16px;
    font-weight: 600;
    line-height: 22px;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  p {
    margin: 0;
    overflow: hidden;
    color: #6b7280;
    font-size: 14px;
    line-height: 20px;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.resource-card-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 54px;
  height: 54px;
  overflow: hidden;
  border-radius: 8px;

  &.knowledge {
    img {
      width: 54px;
      height: 54px;
    }
  }

  &.graph {
    img {
      width: 54px;
      height: 54px;
    }
  }

  &.bot {
    img {
      width: 54px;
      height: 54px;
    }
  }

  img {
    display: block;
    object-fit: contain;
  }
}

:global(.resource-manager-dialog .el-dialog__body) {
  padding-top: 10px;
}

@media (max-width: 1440px) {
  .detail-content {
    grid-template-columns: minmax(0, 1fr) 420px;
  }

  .ability-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 1200px) {
  .detail-hero,
  .detail-content {
    display: block;
  }

  .hero-actions {
    justify-content: flex-end;
    margin-top: 20px;
    margin-left: 94px;
  }

  .resource-panel {
    margin-top: 16px;
    max-height: none !important;
    overflow: visible;
  }
}

@media (max-width: 768px) {
  .app-detail-page {
    padding: 10px;
  }

  .detail-hero,
  .main-panel,
  .resource-panel {
    padding: 18px 14px;
  }

  .hero-main {
    align-items: flex-start;
  }

  .hero-info {
    margin-left: 14px;
  }

  .hero-title-row h1 {
    font-size: 22px;
    line-height: 28px;
  }

  .hero-actions {
    margin-left: 0;
  }

  .value-metrics,
  .scene-grid,
  .ability-grid {
    grid-template-columns: 1fr;
  }
}
aside {
  margin-bottom: 0;
}
</style>
