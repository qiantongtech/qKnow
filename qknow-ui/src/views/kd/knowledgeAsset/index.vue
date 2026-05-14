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
  <div class="app-container integrated-dashboard knowledge-asset-page">
    <div v-loading="pageLoading" class="knowledge-asset-shell">
      <section class="page-head">
        <div class="page-title">
          <h2>知识资产大盘与全生命周期监控</h2>
          <el-icon class="page-title__icon"><InfoFilled /></el-icon>
        </div>

        <div class="page-time">
          <el-icon><Clock /></el-icon>
          <span>数据更新时间: {{ dashboardData.updatedAt || "--" }}</span>
        </div>
      </section>

      <section class="summary-grid">
        <article
          v-for="card in summaryCards"
          :key="card.key"
          class="panel-card summary-card"
        >
          <div class="summary-card__head">
            <div>
              <div class="summary-card__title">{{ card.title }}</div>
              <div class="summary-card__value-row">
                <strong class="summary-card__value">
                  {{ formatDisplayValue(card.value) }}
                </strong>
                <span class="summary-card__unit">{{ card.unit }}</span>
              </div>
            </div>

            <div class="summary-card__icon" :class="`is-${card.color}`">
              <img
                :src="card.icon"
                :alt="card.title"
                class="summary-card__icon-image"
                :style="{
                  width: `${card.iconSize}px`,
                  height: `${card.iconSize}px`,
                }"
              />
            </div>
          </div>

          <div class="summary-card__meta">
            <div
              v-for="meta in card.meta"
              :key="meta.label"
              class="summary-card__meta-item"
            >
              <span>{{ meta.label }}</span>
              <strong>{{ formatDisplayValue(meta.value) }}</strong>
            </div>
          </div>
        </article>
      </section>

      <section class="insight-grid">
        <article class="panel-card feature-card">
          <div class="border-item-head">
            <span class="head-title">知识图谱构建与提炼</span>
          </div>

          <div class="feature-card__body">
            <div class="feature-card__main">
              <div class="feature-label">
                <span class="feature-dot is-blue"></span>
                {{ trendSection.legend }}
              </div>

              <div class="chart-shell">
                <div
                  v-if="showTrendChart"
                  ref="trendChartRef"
                  class="trend-chart"
                ></div>
                <div v-else class="chart-placeholder">接口预留中</div>
              </div>
            </div>

            <div class="feature-card__aside">
              <div
                v-for="item in trendSection.kpis"
                :key="item.label"
                class="kpi-item"
              >
                <span class="kpi-item__label">{{ item.label }}</span>
                <div class="kpi-item__metric">
                  <strong>{{ item.value }}</strong>
                  <span class="trend-indicator" :class="`is-${item.trend}`">
                    {{ getTrendSymbol(item.trend) }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </article>

        <article class="panel-card feature-card">
          <div class="border-item-head">
            <span class="head-title">知识库解析与检索评测</span>
          </div>

          <div class="feature-card__body">
            <div class="feature-card__main feature-card__main--pipeline">
              <div class="feature-label">
                <span class="feature-dot is-orange"></span>
                {{ pipelineSection.legend }}
              </div>

              <div class="pipeline-list">
                <div
                  v-for="item in pipelineSection.progressItems"
                  :key="item.label"
                  class="progress-item"
                >
                  <div class="progress-item__top">
                    <span>{{ item.label }}</span>
                    <strong>{{ formatPercent(item) }}</strong>
                  </div>

                  <el-progress
                    :percentage="item.value"
                    :stroke-width="8"
                    :show-text="false"
                    :color="item.color"
                  />
                </div>
              </div>
            </div>

            <div class="feature-card__aside">
              <div
                v-for="item in pipelineSection.kpis"
                :key="item.label"
                class="kpi-item"
              >
                <span class="kpi-item__label">{{ item.label }}</span>
                <div class="kpi-item__metric">
                  <strong>{{ item.value }}</strong>
                  <span class="trend-indicator" :class="`is-${item.trend}`">
                    {{ getTrendSymbol(item.trend) }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </article>
      </section>

      <section class="panel-card governance-card">
        <div class="border-item-head governance-head">
          <div class="governance-head__left">
            <span class="head-title">资产治理中心</span>
            <Tag
              class="governance-tag governance-tag--pending"
              :name="governancePendingTagText"
              type="danger"
              style-type="rect"
            />
          </div>

          <div class="governance-head__filters">
            <button
              v-for="item in governanceFilters"
              :key="item.value"
              type="button"
              class="filter-button"
              :class="{ active: activeGovernanceFilter === item.value }"
              @click="handleGovernanceFilter(item.value)"
            >
              {{ item.label }}
            </button>
          </div>
        </div>

        <div class="border-item-body governance-body">
          <el-table
            class="governance-table"
            :data="paginatedGovernanceRows"
            :empty-text="tableEmptyText"
          >
            <el-table-column label="诊断对象" min-width="200">
              <template #default="{ row }">
                <div class="asset-name">
                  <i class="asset-name__dot" :class="`is-${row.typeTone}`"></i>
                  <span>{{ row.name }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="资产类型" width="140" align="center">
              <template #default="{ row }">
                <Tag
                  class="governance-tag governance-tag--soft"
                  :name="row.typeLabel"
                  :type="getGovernanceTypeTagType(row.typeTone)"
                  style-type="rect"
                />
              </template>
            </el-table-column>

            <el-table-column
              prop="issue"
              label="诊断问题描述"
              min-width="340"
              show-overflow-tooltip
            />

            <el-table-column label="严重级别" width="140" align="center">
              <template #default="{ row }">
                <Tag
                  class="governance-tag governance-tag--soft"
                  :name="row.severityLabel"
                  :type="getGovernanceSeverityTagType(row.severityTone)"
                  style-type="rect"
                />
              </template>
            </el-table-column>

            <el-table-column
              prop="discoveredAt"
              label="发现时间"
              width="180"
              align="center"
            />

            <el-table-column label="建议操作" width="140" align="center">
              <template #default="{ row }">
                <button type="button" class="table-link">
                  {{ row.actionText }}
                </button>
              </template>
            </el-table-column>
          </el-table>

          <div class="governance-footer">
            <span>共 {{ filteredGovernanceRows.length }} 条</span>

            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              small
              background
              layout="prev, pager, next, sizes"
              :page-sizes="[10, 20, 50]"
              :total="filteredGovernanceRows.length"
            />
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup name="KnowledgeAsset">
import {
  computed,
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from "vue";
import * as echarts from "echarts";
import { Clock, InfoFilled } from "@element-plus/icons-vue";
import bubbleChartFillIcon from "@/assets/icons/svg/bubble-chart-fill.svg";
import bookOpenFillIcon from "@/assets/icons/svg/book-open-fill.svg";
import folderChartFillIcon from "@/assets/icons/svg/folder-chart-fill.svg";
import progressFillIcon from "@/assets/icons/svg/progress-6-fill.svg";

const isDemo = import.meta.env.VITE_APP_MODE === "demo";

const summaryCardConfigs = [
  {
    key: "graph",
    title: "知识图谱资源",
    unit: "核心图谱",
    icon: bubbleChartFillIcon,
    iconSize: 33,
    color: "blue",
    metaLabels: ["实体总数", "关系总数"],
  },
  {
    key: "library",
    title: "知识库规模",
    unit: "个领域库",
    icon: bookOpenFillIcon,
    iconSize: 33,
    color: "orange",
    metaLabels: ["总文档数", "解析切片"],
  },
  {
    key: "source",
    title: "数据源与文件接入",
    unit: "个活动连接",
    icon: folderChartFillIcon,
    iconSize: 33,
    color: "teal",
    metaLabels: ["结构化感知库", "非结构化文件"],
  },
  {
    key: "health",
    title: "资产全局健康度",
    unit: "分",
    icon: progressFillIcon,
    iconSize: 33,
    color: "purple",
    metaLabels: ["高质量资产占比", "告警项"],
  },
];

const governanceFilters = [
  { label: "全部诊断", value: "all" },
  { label: "高危异常", value: "critical" },
  { label: "质量优化", value: "quality" },
];

const pageLoading = ref(false);
const activeGovernanceFilter = ref("all");
const currentPage = ref(1);
const pageSize = ref(10);
const trendChartRef = ref(null);

const dashboardData = ref(createEmptyDashboardData());

let trendChartInstance;

const summaryCards = computed(() =>
  summaryCardConfigs.map((config) => {
    const current = dashboardData.value.summary[config.key] || {};

    return {
      ...config,
      value: current.value ?? "--",
      meta: config.metaLabels.map((label, index) => ({
        label,
        value: current.meta?.[index] ?? "--",
      })),
    };
  })
);

const trendSection = computed(() => dashboardData.value.trend);
const pipelineSection = computed(() => dashboardData.value.pipeline);
const governanceRows = computed(
  () => dashboardData.value.governance.rows || []
);
const governancePendingCount = computed(() => governanceRows.value.length);
const governancePendingTagText = computed(
  () => `待处理 ${governancePendingCount.value}`
);
const showTrendChart = computed(
  () => dashboardData.value.mode !== "placeholder"
);

const filteredGovernanceRows = computed(() => {
  if (activeGovernanceFilter.value === "critical") {
    return governanceRows.value.filter((item) => item.group === "critical");
  }

  if (activeGovernanceFilter.value === "quality") {
    return governanceRows.value.filter((item) => item.group === "quality");
  }

  return governanceRows.value;
});

const paginatedGovernanceRows = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredGovernanceRows.value.slice(start, start + pageSize.value);
});

const tableEmptyText = computed(() => {
  if (dashboardData.value.mode === "placeholder") {
    return "接口预留中";
  }

  if (!governanceRows.value.length) {
    return "暂无诊断数据";
  }

  return "当前筛选下暂无记录";
});

watch(
  () => [filteredGovernanceRows.value.length, pageSize.value],
  () => {
    const maxPage = Math.max(
      1,
      Math.ceil(filteredGovernanceRows.value.length / pageSize.value)
    );

    if (currentPage.value > maxPage) {
      currentPage.value = maxPage;
    }
  }
);

function createEmptyDashboardData() {
  return {
    mode: "placeholder",
    updatedAt: "",
    summary: {
      graph: { value: "--", meta: ["--", "--"] },
      library: { value: "--", meta: ["--", "--"] },
      source: { value: "--", meta: ["--", "--"] },
      health: { value: "--", meta: ["--", "--"] },
    },
    trend: {
      legend: "三元组持续积累趋势",
      labels: ["1月", "2月", "3月", "本月"],
      values: [0, 0, 0, 0],
      kpis: [
        { label: "知识抽取准确率", value: "--", trend: "up" },
        { label: "实体对齐完成度", value: "--", trend: "up" },
        { label: "孤立实体节点数", value: "--", trend: "down" },
      ],
    },
    pipeline: {
      legend: "文档解析流水线状态",
      progressItems: [
        {
          label: "解析成功率",
          value: 0,
          display: "--",
          color: "#ff9a3d",
        },
        {
          label: "多模态切片生成进度",
          value: 0,
          display: "--",
          color: "#3d7dff",
        },
        {
          label: "向量化 Embedding 完成",
          value: 0,
          display: "--",
          color: "#47d97b",
        },
      ],
      kpis: [
        { label: "Top-3 平均召回率", value: "--", trend: "up" },
        { label: "混合检索模式占比", value: "--", trend: "flat" },
        { label: "检索超时告警", value: "--", trend: "flat" },
      ],
    },
    governance: {
      rows: [],
    },
  };
}

function createDemoDashboardData() {
  return {
    mode: "demo",
    updatedAt: "2026-03-31 16:30:00",
    summary: {
      graph: { value: "3", meta: ["12.5k", "45.2k"] },
      library: { value: "18", meta: ["8,450", "125.4k"] },
      source: { value: "24", meta: ["6", "18"] },
      health: { value: "96", meta: ["92%", "3项"] },
    },
    trend: {
      legend: "三元组持续积累趋势",
      labels: ["1月", "2月", "3月", "本月"],
      values: [18, 37, 42, 58],
      kpis: [
        { label: "知识抽取准确率", value: "94.2%", trend: "up" },
        { label: "实体对齐完成度", value: "96.5%", trend: "up" },
        { label: "孤立实体节点数", value: "12", trend: "down" },
      ],
    },
    pipeline: {
      legend: "文档解析流水线状态",
      progressItems: [
        {
          label: "解析成功率 (8,310份)",
          value: 98.3,
          color: "#ff9a3d",
        },
        {
          label: "多模态切片生成进度",
          value: 85.0,
          color: "#3d7dff",
        },
        {
          label: "向量化 Embedding 完成",
          value: 100,
          color: "#47d97b",
        },
      ],
      kpis: [
        { label: "Top-3 平均召回率", value: "88.7%", trend: "up" },
        { label: "混合检索模式占比", value: "76.0%", trend: "flat" },
        { label: "检索超时告警", value: "0", trend: "flat" },
      ],
    },
    governance: {
      rows: [
        {
          name: "MySQL 业务库同步",
          typeLabel: "数据源连接",
          typeTone: "blue",
          issue: "数据库认证凭证已过期，数据抽取任务中断",
          severityLabel: "高危（阻断）",
          severityTone: "danger",
          discoveredAt: "2026-03-31 14:20",
          actionText: "更新凭证",
          group: "critical",
        },
        {
          name: "历史文献合集库",
          typeLabel: "知识库",
          typeTone: "orange",
          issue: "发现 14 个文档解析为“乱码切片”，影响召回质量",
          severityLabel: "中等（降质）",
          severityTone: "warning",
          discoveredAt: "2026-03-31 09:15",
          actionText: "查看失败切片",
          group: "quality",
        },
        {
          name: "医疗实体关系图谱",
          typeLabel: "知识图谱",
          typeTone: "teal",
          issue: "存在 12 个孤立实体节点，缺少关系边连接",
          severityLabel: "低优（优化）",
          severityTone: "notice",
          discoveredAt: "2026-03-30 11:00",
          actionText: "进入图谱编辑",
          group: "quality",
        },
      ],
    },
  };
}

async function fetchKnowledgeAssetDashboard() {
  // TODO: 后端接口就绪后，在这里接入真实数据并做格式归一化。
  // const { data } = await getKnowledgeAssetDashboard();
  // return normalizeKnowledgeAssetDashboard(data);
  return createEmptyDashboardData();
}

async function loadDashboardData() {
  pageLoading.value = true;

  try {
    dashboardData.value = isDemo
      ? createDemoDashboardData()
      : await fetchKnowledgeAssetDashboard();
  } finally {
    pageLoading.value = false;
    nextTick(() => {
      renderTrendChart();
    });
  }
}

function disposeChart(chart) {
  if (chart && !chart.isDisposed()) {
    chart.dispose();
  }
}

function createChart(target, current) {
  if (!target) {
    return null;
  }

  disposeChart(current);
  return echarts.init(target);
}

function getNiceAxisConfig(maxValue, splitCount = 4) {
  const safeMax = Math.max(Number(maxValue) || 0, 1);
  const rawStep = safeMax / splitCount;
  const magnitude = 10 ** Math.floor(Math.log10(rawStep));
  const normalized = rawStep / magnitude;

  let niceNormalized = 1;
  if (normalized <= 1) {
    niceNormalized = 1;
  } else if (normalized <= 2) {
    niceNormalized = 2;
  } else if (normalized <= 2.5) {
    niceNormalized = 2.5;
  } else if (normalized <= 5) {
    niceNormalized = 5;
  } else {
    niceNormalized = 10;
  }

  const interval = niceNormalized * magnitude;
  const max = Math.ceil(safeMax / interval) * interval;

  return {
    min: 0,
    max,
    interval,
  };
}

function renderTrendChart() {
  if (!showTrendChart.value || !trendChartRef.value) {
    disposeChart(trendChartInstance);
    trendChartInstance = null;
    return;
  }

  const { labels, values } = trendSection.value;
  const maxVal = Math.max(...values, 1);
  const axisConfig = getNiceAxisConfig(maxVal, 4);
  trendChartInstance = createChart(trendChartRef.value, trendChartInstance);

  if (!trendChartInstance) {
    return;
  }

  trendChartInstance.setOption({
    animationDuration: 900,
    animationEasing: "cubicOut",
    grid: {
      top: "15%",
      right: "3%",
      bottom: "3%",
      left: "3%",
      containLabel: true,
    },
    tooltip: {
      trigger: "axis",
      backgroundColor: "#ffffff",
      borderColor: "#d9e7f6",
      borderWidth: 1,
      textStyle: { color: "#607086", fontSize: 12 },
    },
    xAxis: {
      type: "category",
      data: labels,
      boundaryGap: false,
      axisLine: { lineStyle: { color: "#7f8da3" } },
      axisLabel: { color: "#6f7f95", fontSize: 12, margin: 12 },
      axisTick: { show: false },
    },
    yAxis: {
      type: "value",
      min: axisConfig.min,
      max: axisConfig.max,
      interval: axisConfig.interval,
      splitNumber: 4,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { show: true, color: "#6f7f95", fontSize: 12, margin: 8 },
      splitLine: { lineStyle: { color: "#dbe8f5", type: "solid" } },
    },
    series: [
      {
        type: "line",
        data: values,
        smooth: false,
        symbol: "circle",
        symbolSize: 8,
        lineStyle: { width: 2, color: "#52a8ff" },
        itemStyle: { color: "#fff", borderColor: "#52a8ff", borderWidth: 2 },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "rgba(82,168,255,0.22)" },
            { offset: 1, color: "rgba(82,168,255,0.02)" },
          ]),
        },
      },
    ],
  });
}

function resizeCharts() {
  trendChartInstance?.resize();
}

function handleGovernanceFilter(value) {
  activeGovernanceFilter.value = value;
  currentPage.value = 1;
}

function formatDisplayValue(value) {
  return value === 0 || value ? value : "--";
}

function formatPercent(item) {
  if (item.display) {
    return item.display;
  }

  if (typeof item.value !== "number") {
    return "--";
  }

  return `${item.value.toFixed(1).replace(/\.0$/, "")}%`;
}

function getGovernanceTypeTagType(tone) {
  const typeMap = {
    blue: "primary",
    orange: "warning",
    teal: "success",
  };

  return typeMap[tone] || "default";
}

function getGovernanceSeverityTagType(tone) {
  const typeMap = {
    danger: "danger",
    warning: "warning",
    notice: "info",
  };

  return typeMap[tone] || "default";
}

function getTrendSymbol(status) {
  if (status === "up") {
    return "↗";
  }

  if (status === "down") {
    return "↘";
  }

  return "-";
}

onMounted(() => {
  loadDashboardData();
  window.addEventListener("resize", resizeCharts);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeCharts);
  disposeChart(trendChartInstance);
});
</script>

<style lang="scss" scoped>
.integrated-dashboard {
  --bg-page: #f0f2f5;
  --bg-card: #ffffff;
  --line-soft: #e8e8e8;
  --text-main: #1f2d3d;
  --text-sub: #6f7f95;
  --text-mute: #9aa7bb;
  --brand-blue: #346dff;
  --brand-indigo: #606aff;
  --brand-purple: #8c58ff;
  --brand-cyan: #53c3ff;
  --brand-green: #47d97b;
  --brand-teal: #5bd8cf;
  --brand-orange: #ff8a3d;
  min-height: 100%;
  background: var(--bg-page);
}

.knowledge-asset-shell {
  display: grid;
  gap: 15px;
}

.panel-card {
  border-radius: 2px;
  background: var(--bg-card);
  box-shadow: none;
  overflow: hidden;
}

.border-item-head,
.page-head,
.governance-head,
.governance-head__left,
.governance-head__filters,
.page-title,
.page-time,
.card-head,
.progress-item__top,
.kpi-item__metric,
.summary-card__head,
.summary-card__value-row,
.asset-name,
.governance-footer {
  display: flex;
  align-items: center;
}

.page-head,
.border-item-head,
.governance-head,
.card-head,
.progress-item__top,
.kpi-item__metric,
.summary-card__head,
.governance-footer {
  justify-content: space-between;
}

.page-head {
  gap: 16px;
  min-height: 36px;
}

.border-item-head {
  height: 50px;
  padding: 0 20px;
  border-bottom: 1px solid #e8e8e8;
}

.border-item-body {
  display: block;
}

.page-title {
  gap: 8px;
  min-width: 0;
}

.page-title h2 {
  margin: 0;
  color: rgba(0, 0, 0, 0.88);
  font-size: 16px;
  font-weight: 700;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.page-title__icon,
.page-time {
  color: var(--text-sub);
  font-size: 13px;
}

.page-time {
  gap: 6px;
  min-width: 0;
  margin-left: auto;
}

.page-time span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
}

.summary-card {
  padding: 18px;
}

.summary-card__title {
  color: var(--text-main);
  font-size: 15px;
}

.summary-card__value-row {
  gap: 8px;
  margin-top: 30px;
}

.summary-card__value {
  color: #142b4d;
  font-size: 32px;
  line-height: 1;
  font-weight: 700;
}

.summary-card__unit {
  color: var(--text-sub);
  font-size: 14px;
}

.summary-card__icon {
  display: grid;
  place-items: center;
  flex-shrink: 0;
  position: relative;
  width: 48px;
  height: 48px;
  overflow: hidden;
  border-radius: 10px;
  box-shadow: 0 6px 14px rgba(61, 109, 255, 0.16),
    inset 0 1px 0 rgba(255, 255, 255, 0.22);
}

.summary-card__icon-image {
  position: relative;
  z-index: 1;
  display: block;
  object-fit: contain;
  filter: brightness(0) invert(1);
}

.summary-card__icon::before {
  content: "";
  position: absolute;
  inset: 1px;
  border-radius: 9px;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.18) 0%,
    rgba(255, 255, 255, 0.02) 58%,
    transparent 100%
  );
  pointer-events: none;
}

.summary-card__icon.is-blue {
  color: #fff;
  background: linear-gradient(135deg, #62b6ff 0%, #3a84ff 100%);
  box-shadow: 0 8px 18px rgba(58, 132, 255, 0.24);
}

.summary-card__icon.is-orange {
  color: #fff;
  background: linear-gradient(135deg, #ffb564 0%, #ff8a3d 100%);
  box-shadow: 0 8px 18px rgba(255, 138, 61, 0.24);
}

.summary-card__icon.is-teal {
  color: #fff;
  background: linear-gradient(135deg, #45d7cb 0%, #14b8a6 100%);
  box-shadow: 0 8px 18px rgba(20, 184, 166, 0.24);
}

.summary-card__icon.is-purple {
  color: #fff;
  background: linear-gradient(135deg, #bb87ff 0%, #925cff 100%);
  box-shadow: 0 8px 18px rgba(146, 92, 255, 0.24);
}

.summary-card__meta {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #edf1f7;
}

.summary-card__meta-item {
  display: grid;
  gap: 8px;
}

.summary-card__meta-item span {
  color: var(--text-sub);
  font-size: 13px;
}

.summary-card__meta-item strong {
  color: #334969;
  font-size: 20px;
  line-height: 1;
}

.insight-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(0, 1fr);
  gap: 15px;
}

.feature-card {
  display: flex;
  flex-direction: column;
  padding: 0;
}

.card-head {
  min-height: 50px;
  padding: 0 20px;
  border-bottom: 1px solid #e8e8e8;
}

.card-head--plain {
  min-height: auto;
  padding: 18px 20px 0;
  border-bottom: none;
}

.head-title {
  display: flex;
  align-items: center;
  color: rgba(0, 0, 0, 0.85);
  font-size: 16px;
  font-family: "PingFang SC", sans-serif;
  font-weight: 500;
}

.head-title::before {
  display: inline-block;
  content: "";
  width: 6px;
  height: 16px;
  margin-right: 10px;
  border-radius: 3px;
  background: var(--el-color-primary);
}

.card-title {
  position: relative;
  padding-left: 16px;
  color: rgba(0, 0, 0, 0.85);
  font-size: 16px;
  font-weight: 500;
}

.card-title::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  width: 6px;
  height: 16px;
  border-radius: 3px;
  transform: translateY(-50%);
  background: var(--brand-blue);
}

.feature-card__body {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 220px;
  flex: 1;
  gap: 22px;
  padding: 15px 20px 18px;
}

.feature-card__main {
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 0;
  padding-right: 20px;
  border-right: 1px solid #edf1f7;
}

.feature-card__main--pipeline {
  justify-content: space-between;
}

.feature-label {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: var(--text-sub);
  font-size: 13px;
}

.feature-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.feature-dot.is-blue {
  background: var(--brand-blue);
  box-shadow: 0 0 8px rgba(61, 125, 255, 0.28);
}

.feature-dot.is-orange {
  background: var(--brand-orange);
  box-shadow: 0 0 8px rgba(255, 138, 61, 0.28);
}

.chart-shell {
  display: flex;
  flex: 1;
  min-height: 212px;
  margin-top: 12px;
}

.trend-chart {
  flex: 1;
  width: 100%;
  height: 100%;
}

.chart-placeholder {
  display: grid;
  place-items: center;
  flex: 1;
  width: 100%;
  height: 100%;
  color: var(--text-mute);
  font-size: 14px;
  background: linear-gradient(180deg, #f8fbff 0%, #f3f7fd 100%);
  border: 1px dashed #d7e2f0;
  border-radius: 10px;
}

.feature-card__aside {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-height: 0;
  padding: 2px 0;
}

.kpi-item {
  display: grid;
  gap: 10px;
}

.kpi-item__label {
  color: var(--text-sub);
  font-size: 14px;
}

.kpi-item__metric strong {
  color: #142b4d;
  font-size: 24px;
  line-height: 1;
  font-weight: 700;
}

.trend-indicator {
  min-width: 18px;
  text-align: right;
  font-size: 13px;
  font-weight: 700;
}

.trend-indicator.is-up {
  color: var(--brand-green);
}

.trend-indicator.is-down {
  color: #ff5f56;
}

.trend-indicator.is-flat {
  color: var(--text-mute);
}

.pipeline-list {
  display: grid;
  flex: 1;
  align-content: space-between;
  gap: 16px;
  margin-top: 18px;
}

.progress-item {
  display: grid;
  gap: 8px;
}

.progress-item__top {
  gap: 12px;
  color: var(--text-sub);
  font-size: 14px;
}

.progress-item__top strong {
  flex-shrink: 0;
  color: #334969;
  font-size: 13px;
}

.governance-card {
  padding: 0;
}

.governance-head {
  gap: 16px;
}

.governance-head__left,
.governance-head__filters {
  gap: 12px;
}

.governance-body {
  position: relative;
  padding: 15px 34px;
}

.pending-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 24px;
  padding: 0 10px;
  border-radius: 999px;
  background: rgba(255, 95, 86, 0.12);
  color: #ff5f56;
  font-size: 12px;
}

.filter-button,
.table-link {
  padding: 0;
  border: none;
  background: transparent;
  cursor: pointer;
}

.filter-button {
  height: 32px;
  padding: 0 14px;
  border: 1px solid #dce6f3;
  border-radius: 4px;
  color: var(--text-sub);
  font-size: 13px;
  transition: all 0.2s ease;
}

.filter-button.active {
  color: var(--brand-orange);
  background: #fff6ef;
  border-color: rgba(255, 138, 61, 0.32);
}

.asset-name {
  gap: 8px;
  color: var(--text-main);
  font-weight: 500;
}

.asset-name__dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.asset-name__dot.is-blue {
  background: var(--brand-blue);
}

.asset-name__dot.is-orange {
  background: var(--brand-orange);
}

.asset-name__dot.is-teal {
  background: #1ea99d;
}

.type-badge,
.severity-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 24px;
  padding: 0 10px;
  border-radius: 999px;
  font-size: 12px;
  white-space: nowrap;
}

.type-badge.is-blue {
  background: rgba(61, 125, 255, 0.12);
  color: var(--brand-blue);
}

.type-badge.is-orange {
  background: rgba(255, 138, 61, 0.12);
  color: var(--brand-orange);
}

.type-badge.is-teal {
  background: rgba(91, 216, 207, 0.14);
  color: #1ea99d;
}

.severity-badge.is-danger {
  background: rgba(255, 95, 86, 0.12);
  color: #ff5f56;
}

.severity-badge.is-warning {
  background: rgba(255, 138, 61, 0.16);
  color: #ff8a3d;
}

.severity-badge.is-notice {
  background: rgba(255, 190, 61, 0.16);
  color: #c88400;
}

.table-link {
  color: var(--brand-blue);
  font-size: 14px;
}

.governance-footer {
  gap: 16px;
  padding: 14px 0;
  color: var(--text-sub);
  font-size: 13px;
  border-top: 1px solid var(--line-soft);
}

:deep(.pipeline-list .el-progress-bar__outer) {
  background: #edf1f7;
  border-radius: 999px;
}

:deep(.pipeline-list .el-progress-bar__inner) {
  border-radius: 999px;
}

:deep(.governance-table .el-table__inner-wrapper::before) {
  display: none;
}

:deep(.governance-table) {
  width: 100%;
}

:deep(.governance-table thead) {
  height: 53px;
}

:deep(.governance-table thead .el-table__cell.is-leaf) {
  background: rgba(19, 90, 251, 0.04) !important;
}

:deep(.governance-table th.el-table__cell) {
  padding: 12px 0;
  background: rgba(19, 90, 251, 0.04);
}

:deep(.governance-table th.el-table__cell > .cell) {
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
  font-weight: 500;
}

:deep(.governance-table td.el-table__cell) {
  padding: 14px 0;
}

:deep(.governance-table td.el-table__cell > .cell) {
  color: var(--text-main);
  font-size: 14px;
}

:deep(.governance-table .el-table__empty-block) {
  min-height: 180px;
}

:deep(.governance-footer .el-pagination) {
  margin-left: auto;
}

@media (max-width: 1400px) {
  .summary-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 1100px) {
  .insight-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 900px) {
  .feature-card__body,
  .page-head {
    grid-template-columns: 1fr;
  }

  .page-head {
    flex-direction: row;
    align-items: center;
  }

  .governance-head {
    flex-direction: row;
    align-items: center;
    flex-wrap: nowrap;
  }

  .governance-head__left {
    flex: 0 0 auto;
    flex-direction: row;
    align-items: center;
    min-width: 0;
  }

  .governance-head__filters {
    flex: 1 1 auto;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
    flex-wrap: nowrap;
    min-width: 0;
    margin-left: auto;
    overflow-x: auto;
    overflow-y: hidden;
  }

  .governance-head__filters .filter-button {
    flex-shrink: 0;
  }

  .feature-card__body {
    display: grid;
  }

  .feature-card__main {
    padding-right: 0;
    padding-bottom: 18px;
    border-right: none;
    border-bottom: 1px solid #edf1f7;
  }

  .feature-card__aside {
    padding-top: 4px;
  }
}

@media (max-width: 680px) {
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .summary-card__meta {
    grid-template-columns: 1fr;
  }

  .feature-card__body {
    grid-template-columns: 1fr;
  }

  .governance-footer {
    flex-direction: column;
    align-items: flex-start;
  }

  .governance-body {
    padding: 14px;
  }

  :deep(.governance-footer .el-pagination) {
    margin-left: 0;
  }
}
</style>
