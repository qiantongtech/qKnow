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
  源代码的"可访问性"均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
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
  <div class="app-container integrated-dashboard bot-operation-page">
    <div v-loading="pageLoading" class="bot-operation-shell">
      <section class="page-head">
        <div class="page-title">
          <h2>Bot 运营看板</h2>
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
        </article>
      </section>

      <section class="analytics-grid">
        <article class="panel-card chart-card">
          <div class="card-head">
            <div class="card-tools">
              <div class="tab-switch">
                <button
                  v-for="tab in trendRangeTabs"
                  :key="tab.value"
                  type="button"
                  :class="{ active: trendRange === tab.value }"
                  @click="trendRange = tab.value"
                >
                  {{ tab.label }}
                </button>
              </div>
            </div>
            <span class="card-title">调用趋势</span>
          </div>

          <div class="border-item-body chart-body">
            <div
              v-if="showTrendChart"
              ref="trendChartRef"
              class="trend-chart"
            ></div>
            <div v-else class="chart-placeholder">
              <el-icon><TrendCharts /></el-icon>
              <p>调用趋势折线图</p>
            </div>
          </div>
        </article>

        <article class="panel-card chart-card">
          <div class="card-head">
            <span class="card-title">Bot 类型分布</span>
          </div>

          <div class="border-item-body chart-body chart-body--type">
            <div v-if="showTypeChart" class="type-chart-layout">
              <div class="type-chart-panel">
                <div ref="typeChartRef" class="type-chart"></div>
                <div class="type-chart-center">
                  <strong>{{ typeTotal }}</strong>
                  <span>合计</span>
                </div>
              </div>

              <div class="type-legend">
                <div
                  v-for="item in typeLegendList"
                  :key="item.name"
                  class="type-legend__item"
                >
                  <span
                    class="type-legend__dot"
                    :style="{ backgroundColor: item.color }"
                  ></span>
                  <span class="type-legend__label">{{ item.name }}</span>
                  <span class="type-legend__value">{{ item.value }}个</span>
                </div>
              </div>
            </div>
            <div v-else class="chart-placeholder">
              <el-icon><PieChart /></el-icon>
              <p>问答/任务/插件 类型占比</p>
            </div>
          </div>
        </article>
      </section>

      <section class="bottom-grid">
        <article class="panel-card health-card">
          <div class="card-head">
            <span class="card-title">健康监控（异常对象）</span>
          </div>

          <div class="border-item-body health-body">
            <el-table
              class="health-table"
              :data="paginatedHealthList"
              :empty-text="tableEmptyText"
            >
              <el-table-column label="Bot名称" min-width="180">
                <template #default="{ row }">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>

              <el-table-column label="异常类型" width="140" align="center">
                <template #default="{ row }">
                  <span>{{ row.errorType }}</span>
                </template>
              </el-table-column>

              <el-table-column label="状态" width="100" align="center">
                <template #default="{ row }">
                  <Tag
                    :name="row.status"
                    :type="getHealthStatusTagType(row.statusTone, row.status)"
                    style-type="rect"
                  />
                </template>
              </el-table-column>

              <el-table-column label="严重级别" width="100" align="center">
                <template #default="{ row }">
                  <Tag
                    :name="row.severity"
                    :type="
                      getHealthSeverityTagType(row.severity, row.severityTone)
                    "
                    style-type="rect"
                  />
                </template>
              </el-table-column>
            </el-table>

            <div class="health-footer">
              <span>共 {{ healthList.length }} 条</span>

              <el-pagination
                v-model:current-page="healthPage"
                small
                background
                layout="prev, pager, next"
                :page-size="healthPageSize"
                :total="healthList.length"
              />
            </div>
          </div>
        </article>

        <article class="panel-card rank-card">
          <div class="card-head">
            <span class="card-title">热门 Bot 排行</span>
            <span class="rank-subtitle">调用量 TOP 5</span>
          </div>

          <div class="border-item-body rank-body">
            <div
              v-for="(item, index) in rankList"
              :key="item.name"
              class="rank-item"
            >
              <div class="rank-index" :class="{ 'is-top': index < 3 }">
                {{ index + 1 }}
              </div>
              <div class="rank-info">
                <span class="rank-name">{{ item.name }}</span>
              </div>
              <div class="rank-value">
                <strong>{{ formatNumber(item.calls) }}</strong>
                <span class="rank-unit">次</span>
              </div>
            </div>
          </div>
        </article>
      </section>
    </div>
  </div>
</template>

<script setup name="BotOperation">
import {
  computed,
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from "vue";
import * as echarts from "echarts";
import {
  Clock,
  Cpu,
  InfoFilled,
  Microphone,
  Monitor,
  TrendCharts,
  PieChart,
  Timer,
  Warning,
} from "@element-plus/icons-vue";
import articleFillIcon from "@/assets/icons/svg/article-fill.svg";
import alertTriangleFillIcon from "@/assets/icons/svg/alert-triangle-fill.svg";
import archiveStackFillIcon from "@/assets/icons/svg/archive-stack-fill.svg";
import chartIcon from "@/assets/icons/svg/chart.svg";
import progressFillIcon from "@/assets/icons/svg/progress-6-fill.svg";

const isDemo = import.meta.env.VITE_APP_MODE === "demo";

const pageLoading = ref(false);
const trendRange = ref("30d");
const trendRangeTabs = [
  { label: "\u8fd11\u5468", value: "7d" },
  { label: "\u8fd11\u6708", value: "30d" },
  { label: "\u8fd1\u534a\u5e74", value: "180d" },
];
const healthPage = ref(1);
const healthPageSize = ref(10);
const trendChartRef = ref(null);
const typeChartRef = ref(null);

const dashboardData = ref(createEmptyDashboardData());

let trendChartInstance;
let typeChartInstance;
const typeDistributionColors = ["#346dff", "#47d97b", "#ff8a3d"];

const summaryCardConfigs = [
  { key: "total", title: "Bot总数", icon: Monitor, color: "blue" },
  { key: "running", title: "运行中", icon: Cpu, color: "green" },
  { key: "error", title: "异常中", icon: Warning, color: "red" },
  { key: "pending", title: "待发布", icon: Timer, color: "orange" },
  { key: "calls", title: "总调用量", icon: Microphone, color: "purple" },
];

summaryCardConfigs[0].icon = archiveStackFillIcon;
summaryCardConfigs[0].iconSize = 33;
summaryCardConfigs[1].icon = progressFillIcon;
summaryCardConfigs[1].iconSize = 33;
summaryCardConfigs[2].icon = alertTriangleFillIcon;
summaryCardConfigs[2].iconSize = 33;
summaryCardConfigs[3].icon = articleFillIcon;
summaryCardConfigs[3].iconSize = 33;
summaryCardConfigs[4].icon = chartIcon;
summaryCardConfigs[4].iconSize = 28;

const summaryCards = computed(() =>
  summaryCardConfigs.map((config) => ({
    ...config,
    value: dashboardData.value.summary[config.key] ?? "--",
  }))
);

const healthList = computed(() => dashboardData.value.health || []);
const paginatedHealthList = computed(() => {
  const start = (healthPage.value - 1) * healthPageSize.value;
  return healthList.value.slice(start, start + healthPageSize.value);
});
const rankList = computed(() => dashboardData.value.rank || []);
const typeLegendList = computed(() =>
  (dashboardData.value.typeDistribution || []).map((item, index) => ({
    ...item,
    color: typeDistributionColors[index % typeDistributionColors.length],
  }))
);
const typeTotal = computed(() =>
  (dashboardData.value.typeDistribution || []).reduce(
    (sum, item) => sum + (Number(item.value) || 0),
    0
  )
);
const activeTrendData = computed(() => {
  const trend = dashboardData.value.trend || {};

  if (trend.datasets?.[trendRange.value]) {
    return trend.datasets[trendRange.value];
  }

  if (trend[trendRange.value]) {
    return trend[trendRange.value];
  }

  if (Array.isArray(trend.labels) && Array.isArray(trend.values)) {
    return trend;
  }

  return { labels: [], values: [] };
});

const showTrendChart = computed(
  () =>
    dashboardData.value.mode !== "placeholder" &&
    activeTrendData.value.labels.length > 0
);
const showTypeChart = computed(
  () => dashboardData.value.mode !== "placeholder"
);

const tableEmptyText = computed(() => {
  if (dashboardData.value.mode === "placeholder") return "接口预留中";
  if (!healthList.value.length) return "暂无异常数据";
  return "当前筛选下暂无记录";
});

watch(
  () => [healthList.value.length, healthPageSize.value],
  () => {
    const maxPage = Math.max(
      1,
      Math.ceil(healthList.value.length / healthPageSize.value)
    );

    if (healthPage.value > maxPage) {
      healthPage.value = maxPage;
    }
  }
);

watch(trendRange, () => {
  nextTick(() => {
    renderTrendChart();
  });
});

function createEmptyDashboardData() {
  return {
    mode: "placeholder",
    updatedAt: "",
    summary: {
      total: "--",
      running: "--",
      error: "--",
      pending: "--",
      calls: "--",
    },
    trend: {
      datasets: {
        "7d": { labels: [], values: [] },
        "30d": { labels: [], values: [] },
        "180d": { labels: [], values: [] },
      },
    },
    typeDistribution: [],
    health: [],
    rank: [],
  };
}

function createDemoDashboardData() {
  return {
    mode: "demo",
    updatedAt: "2026-03-31 16:30:00",
    summary: {
      total: 15,
      running: 12,
      error: 1,
      pending: 2,
      calls: 8920,
    },
    trend: {
      datasets: {
        "7d": {
          labels: [
            "03-25",
            "03-26",
            "03-27",
            "03-28",
            "03-29",
            "03-30",
            "03-31",
          ],
          values: [610, 645, 702, 688, 735, 792, 890],
        },
        "30d": {
          labels: [
            "03-01",
            "03-05",
            "03-10",
            "03-15",
            "03-20",
            "03-25",
            "03-31",
          ],
          values: [320, 450, 380, 520, 680, 750, 890],
        },
        "180d": {
          labels: ["10月", "11月", "12月", "01月", "02月", "03月"],
          values: [4820, 5310, 5660, 6240, 7120, 8920],
        },
      },
    },
    typeDistribution: [
      { name: "问答型 Bot", value: 8 },
      { name: "任务型 Bot", value: 5 },
      { name: "插件型 Bot", value: 2 },
    ],
    health: [
      {
        name: "IT运维服务助手",
        errorType: "接口超时",
        status: "告警",
        statusTone: "warning",
        severityTone: "danger",
        severity: "高",
      },
      {
        name: "财务报销指南Bot",
        errorType: "知识库无响应",
        status: "降级",
        statusTone: "danger",
        severityTone: "warning",
        severity: "中",
      },
    ],
    rank: [
      { name: "IT运维服务助手", calls: 4520 },
      { name: "内部HR问答机器人", calls: 2100 },
      { name: "法律合规审查Bot", calls: 980 },
      { name: "研发规范审查Bot", calls: 840 },
      { name: "新人入职指引", calls: 320 },
    ],
  };
}

async function fetchBotOperationDashboard() {
  // TODO: 后端接口就绪后，在这里接入真实数据并做格式归一化。
  // const { data } = await getBotOperationDashboard();
  // return normalizeBotOperationDashboard(data);
  return createEmptyDashboardData();
}

async function loadDashboardData() {
  pageLoading.value = true;

  try {
    dashboardData.value = isDemo
      ? createDemoDashboardData()
      : await fetchBotOperationDashboard();
  } finally {
    pageLoading.value = false;
    nextTick(() => {
      renderTrendChart();
      renderTypeChart();
    });
  }
}

function disposeChart(chart) {
  if (chart && !chart.isDisposed()) {
    chart.dispose();
  }
}

function createChart(target, current) {
  if (!target) return null;
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

  const { labels, values } = activeTrendData.value;
  const maxVal = Math.max(...values, 1);
  const axisConfig = getNiceAxisConfig(maxVal, 4);

  trendChartInstance = createChart(trendChartRef.value, trendChartInstance);
  if (!trendChartInstance) return;

  trendChartInstance.setOption({
    animationDuration: 900,
    animationEasing: "cubicOut",
    grid: {
      top: "15%",
      bottom: "3%",
      left: "3%",
      right: "3%",
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

function renderTypeChart() {
  if (!showTypeChart.value || !typeChartRef.value) {
    disposeChart(typeChartInstance);
    typeChartInstance = null;
    return;
  }

  const data = dashboardData.value.typeDistribution;

  typeChartInstance = createChart(typeChartRef.value, typeChartInstance);
  if (!typeChartInstance) return;

  typeChartInstance.setOption({
    animationDuration: 900,
    tooltip: {
      trigger: "item",
      confine: true,
      backgroundColor: "#ffffff",
      borderColor: "#346dff",
      borderWidth: 1,
      padding: [6, 8],
      textStyle: { color: "#5d6b80", fontSize: 11, lineHeight: 16 },
      extraCssText:
        "box-shadow: 0 6px 18px rgba(52, 109, 255, 0.14); border-radius: 4px;",
      formatter: ({ name, value, percent }) =>
        `${name}: ${value} (${percent}%)`,
    },
    series: [
      {
        type: "pie",
        radius: ["56%", "84%"],
        center: ["50%", "50%"],
        startAngle: 90,
        clockwise: true,
        avoidLabelOverlap: true,
        itemStyle: { borderRadius: 0, borderColor: "#fff", borderWidth: 4 },
        label: { show: false },
        emphasis: {
          scale: true,
          label: { show: false },
        },
        labelLine: { show: false },
        data: data.map((item, i) => ({
          ...item,
          itemStyle: {
            color: typeDistributionColors[i % typeDistributionColors.length],
          },
        })),
      },
    ],
  });
}

function resizeCharts() {
  trendChartInstance?.resize();
  typeChartInstance?.resize();
}

function formatDisplayValue(value) {
  return value === 0 || value ? value : "--";
}

function formatNumber(num) {
  if (typeof num !== "number") return "--";
  return num.toLocaleString("zh-CN");
}

function getHealthStatusTagType(tone, status) {
  const toneMap = {
    success: "success",
    primary: "primary",
    warning: "warning",
    danger: "danger",
    info: "info",
    notice: "info",
  };

  if (toneMap[tone]) {
    return toneMap[tone];
  }

  if (status === "正常") {
    return "success";
  }

  return "default";
}

function getHealthSeverityTagType(severity, tone) {
  const toneMap = {
    critical: "danger",
    danger: "danger",
    high: "danger",
    warning: "warning",
    medium: "warning",
    notice: "info",
    info: "info",
    low: "info",
  };

  if (toneMap[tone]) {
    return toneMap[tone];
  }

  const severityMap = {
    高: "danger",
    中: "warning",
    低: "info",
  };

  return severityMap[severity] || "default";
}

onMounted(() => {
  loadDashboardData();
  window.addEventListener("resize", resizeCharts);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeCharts);
  disposeChart(trendChartInstance);
  disposeChart(typeChartInstance);
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
  --brand-green: #47d97b;
  --brand-orange: #ff8a3d;
  --brand-red: #ff4d4f;
  --brand-purple: #8c58ff;
  min-height: 100%;
  background: var(--bg-page);
}

.bot-operation-shell {
  display: grid;
  gap: 15px;
}

.panel-card {
  border-radius: 2px;
  background: var(--bg-card);
  box-shadow: none;
  overflow: hidden;
}

.page-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-height: 36px;
}

.page-title {
  display: flex;
  align-items: center;
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
  display: flex;
  align-items: center;
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
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 15px;
}

.summary-card {
  padding: 18px 20px;
}

.summary-card__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.summary-card__title {
  color: var(--text-main);
  font-size: 14px;
  font-weight: 500;
}

.summary-card__value-row {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin-top: 12px;
}

.summary-card__value {
  color: #142b4d;
  font-size: 32px;
  line-height: 1;
  font-weight: 700;
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

  &::before {
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

  &.is-blue {
    color: #fff;
    background: linear-gradient(135deg, #62b6ff 0%, #3a84ff 100%);
    box-shadow: 0 8px 18px rgba(58, 132, 255, 0.24);
  }

  &.is-green {
    color: #fff;
    background: linear-gradient(135deg, #74de86 0%, #4acb69 100%);
    box-shadow: 0 8px 18px rgba(74, 203, 105, 0.24);
  }

  &.is-red {
    color: #fff;
    background: linear-gradient(135deg, #ff8d97 0%, #ff5c68 100%);
    box-shadow: 0 8px 18px rgba(255, 92, 104, 0.24);
  }

  &.is-orange {
    color: #fff;
    background: linear-gradient(135deg, #ffb564 0%, #ff8a3d 100%);
    box-shadow: 0 8px 18px rgba(255, 138, 61, 0.24);
  }

  &.is-purple {
    color: #fff;
    background: linear-gradient(135deg, #bb87ff 0%, #925cff 100%);
    box-shadow: 0 8px 18px rgba(146, 92, 255, 0.24);
  }
}

.summary-card__icon-image {
  position: relative;
  z-index: 1;
  display: block;
  object-fit: contain;
  filter: brightness(0) invert(1);
}

.analytics-grid {
  display: grid;
  grid-template-columns: minmax(0, 2.15fr) minmax(340px, 0.85fr);
  gap: 15px;
}

.chart-card {
  display: flex;
  flex-direction: column;
}

.card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-height: 50px;
  padding: 0 20px;
  border-bottom: 1px solid #e8e8e8;
}

.card-title {
  order: 0;
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

.card-tools {
  order: 1;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
}

.tab-switch {
  display: inline-flex;
  padding: 0;
  background: #f5f7fa;
}

.tab-switch button {
  min-width: 56px;
  height: 30px;
  padding: 0 12px;
  border: 1px solid #e8e8e8;
  background: transparent;
  color: var(--text-sub);
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-switch button + button {
  margin-left: -1px;
}

.tab-switch button.active {
  background: #fff;
  color: #172b5f;
  box-shadow: none;
}

.chart-body {
  //   padding: 16px 20px 20px;
  min-height: 280px;
}

.chart-body--type {
  display: flex;
  align-items: center;
  padding: 12px 16px 12px 8px;
}

.type-chart-layout {
  display: grid;
  grid-template-columns: minmax(0, 190px) minmax(0, 1fr);
  align-items: center;
  width: 100%;
  gap: 10px;
}

.type-chart-panel {
  position: relative;
  width: 190px;
}

.trend-chart,
.type-chart {
  width: 100%;
}

.trend-chart {
  height: 280px;
}

.type-chart {
  height: 190px;
}

.type-chart-center {
  position: absolute;
  inset: 44px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #fff;
  pointer-events: none;
  text-align: center;
  z-index: 1;
}

.type-chart-center strong {
  color: #1f1f1f;
  font-size: 32px;
  line-height: 1;
  font-weight: 700;
}

.type-chart-center span {
  margin-top: 6px;
  color: #6f7f95;
  font-size: 14px;
  line-height: 1;
}

.type-legend {
  display: grid;
  gap: 14px;
  align-content: center;
  min-width: 0;
}

.type-legend__item {
  display: grid;
  grid-template-columns: 10px minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
}

.type-legend__dot {
  width: 8px;
  height: 8px;
  border-radius: 2px;
}

.type-legend__label {
  color: var(--text-main);
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.type-legend__value {
  color: #5f6f86;
  font-size: 14px;
  white-space: nowrap;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 260px;
  color: var(--text-mute);

  .el-icon {
    font-size: 48px;
    margin-bottom: 12px;
    opacity: 0.35;
  }

  p {
    margin: 0;
    font-size: 14px;
  }
}

.bottom-grid {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 15px;
}

.health-body {
  padding: 16px 20px 0;
}

.health-table {
  :deep(.el-table__inner-wrapper::before) {
    display: none;
  }

  :deep(.el-table) {
    width: 100%;
  }

  :deep(thead) {
    height: 53px;
  }

  :deep(thead .el-table__cell.is-leaf) {
    background: rgba(19, 90, 251, 0.04) !important;
  }

  :deep(th.el-table__cell) {
    padding: 12px 0;
    background: rgba(19, 90, 251, 0.04);
  }

  :deep(th.el-table__cell > .cell) {
    color: rgba(0, 0, 0, 0.85);
    font-size: 14px;
    font-weight: 500;
  }

  :deep(td.el-table__cell) {
    padding: 14px 0;
  }

  :deep(td.el-table__cell > .cell) {
    color: var(--text-main);
    font-size: 14px;
  }

  :deep(.el-table__empty-block) {
    min-height: 180px;
  }
}

.health-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0 16px;
  border-top: 1px solid var(--line-soft);
  color: var(--text-mute);
  font-size: 13px;

  :deep(.el-pagination) {
    margin-left: auto;
  }
}

.rank-subtitle {
  color: var(--text-mute);
  font-size: 13px;
  font-weight: 400;
}

.rank-body {
  padding: 16px 20px 20px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;

  &:last-child {
    border-bottom: none;
  }
}

.rank-index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #f0f2f5;
  color: var(--text-sub);
  font-size: 12px;
  font-weight: 700;
  flex-shrink: 0;

  &.is-top {
    background: linear-gradient(135deg, #ff8a3d, #ff6b35);
    color: #fff;
  }
}

.rank-info {
  flex: 1;
  min-width: 0;
}

.rank-name {
  display: block;
  font-size: 14px;
  color: var(--text-main);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.rank-value {
  text-align: right;
  flex-shrink: 0;

  strong {
    font-size: 16px;
    font-weight: 700;
    color: var(--text-main);
  }

  .rank-unit {
    margin-left: 2px;
    font-size: 12px;
    color: var(--text-mute);
  }
}

@media (max-width: 1180px) {
  .summary-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .analytics-grid,
  .bottom-grid {
    grid-template-columns: 1fr;
  }

  .type-chart-layout {
    grid-template-columns: minmax(0, 220px) minmax(0, 1fr);
  }
}

@media (max-width: 768px) {
  .summary-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .analytics-grid .card-head {
    flex-wrap: nowrap;
  }

  .analytics-grid .card-tools {
    flex: 0 1 auto;
    width: fit-content;
    min-width: 0;
    max-width: 100%;
    margin-left: auto;
    justify-content: flex-end;
    overflow-x: auto;
    overflow-y: hidden;
  }

  .analytics-grid .tab-switch {
    flex: 0 0 auto;
    width: max-content;
    min-width: max-content;
  }

  .analytics-grid .tab-switch button {
    flex: 0 0 auto;
  }

  .analytics-grid .card-title {
    white-space: nowrap;
  }

  .chart-body--type {
    padding: 16px 20px 20px;
  }

  .type-chart-layout {
    grid-template-columns: 1fr;
    justify-items: center;
    gap: 18px;
  }

  .type-legend {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .summary-grid {
    grid-template-columns: 1fr;
  }
}
</style>
