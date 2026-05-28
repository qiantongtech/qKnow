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
  <div class="app-container integrated-dashboard">
    <div v-loading="pageLoading" class="app-operation-shell">
      <section class="page-head">
        <div class="page-title">
          <h2>应用运营看板</h2>
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
              <div class="summary-card__title-row">
                <span class="summary-card__title">{{ card.title }}</span>
                <span v-if="card.tag" class="summary-card__tag">{{
                  card.tag
                }}</span>
              </div>

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
            <span class="card-title">访问与活跃趋势</span>
            <span class="card-extra">近30日</span>
          </div>

          <div class="chart-body">
            <div
              v-if="showTrendChart"
              ref="trendChartRef"
              class="trend-chart"
            ></div>
            <div v-else class="chart-placeholder">
              <el-icon><TrendCharts /></el-icon>
              <p>访问量 / 活跃用户 复合走势图</p>
            </div>
          </div>
        </article>

        <article class="panel-card chart-card">
          <div class="card-head">
            <span class="card-title">行业分类分布</span>
          </div>

          <div class="chart-body chart-body--compact chart-body--industry">
            <div v-if="showIndustryChart" class="industry-chart-layout">
              <div class="industry-chart-panel">
                <div ref="industryChartRef" class="industry-chart"></div>
                <div class="industry-chart-center">
                  <strong>{{ industryTotal }}</strong>
                  <span>合计</span>
                </div>
              </div>

              <div class="industry-legend">
                <div
                  v-for="item in industryLegendList"
                  :key="item.name"
                  class="industry-legend__item"
                >
                  <span
                    class="industry-legend__dot"
                    :style="{ backgroundColor: item.color }"
                  ></span>
                  <span class="industry-legend__label">{{ item.name }}</span>
                  <span class="industry-legend__value">{{ item.value }}个</span>
                </div>
              </div>
            </div>
            <div v-else class="chart-placeholder">
              <el-icon><PieChart /></el-icon>
              <p>应用行业分类环图/饼图</p>
            </div>
          </div>
        </article>
      </section>

      <section class="table-grid">
        <article class="panel-card table-card">
          <div class="card-head">
            <span class="card-title">低活跃应用清单</span>
          </div>

          <div class="table-body">
            <el-table
              class="operation-table"
              :data="paginatedLowActivityList"
              :empty-text="lowActivityEmptyText"
            >
              <el-table-column prop="name" label="应用名称" min-width="180" />

              <el-table-column
                prop="visits7d"
                label="近7日访问"
                width="120"
                align="center"
              />

              <el-table-column label="状态" width="120" align="center">
                <template #default="{ row }">
                  <Tag
                    :name="row.status"
                    :type="
                      getLowActivityStatusTagType(row.statusTone, row.status)
                    "
                    style-type="rect"
                  />
                </template>
              </el-table-column>

              <el-table-column
                prop="publishedAt"
                label="发布时间"
                width="140"
                align="center"
              />
            </el-table>

            <div class="table-footer">
              <span>共 {{ lowActivityList.length }} 条</span>

              <el-pagination
                v-model:current-page="lowActivityPage"
                v-model:page-size="lowActivityPageSize"
                small
                background
                layout="prev, pager, next, sizes"
                :page-sizes="[10, 20, 50]"
                :total="lowActivityList.length"
              />
            </div>
          </div>
        </article>

        <article class="panel-card table-card">
          <div class="card-head">
            <span class="card-title">新发布应用</span>
          </div>

          <div class="table-body">
            <el-table
              class="operation-table"
              :data="paginatedNewReleaseList"
              :empty-text="newReleaseEmptyText"
            >
              <el-table-column prop="name" label="应用名称" min-width="180" />

              <el-table-column
                prop="category"
                label="分类"
                width="120"
                align="center"
              />

              <el-table-column
                prop="creator"
                label="创建人"
                width="120"
                align="center"
              />

              <el-table-column
                prop="publishedAt"
                label="发布时间"
                width="140"
                align="center"
              />
            </el-table>

            <div class="table-footer">
              <span>共 {{ newReleaseList.length }} 条</span>

              <el-pagination
                v-model:current-page="newReleasePage"
                v-model:page-size="newReleasePageSize"
                small
                background
                layout="prev, pager, next, sizes"
                :page-sizes="[10, 20, 50]"
                :total="newReleaseList.length"
              />
            </div>
          </div>
        </article>
      </section>
    </div>
  </div>
</template>

<script setup name="AppOperation">
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
  InfoFilled,
  PieChart,
  TrendCharts,
} from "@element-plus/icons-vue";
import appsAiFillIcon from "@/assets/icons/svg/apps-ai-fill.svg";
import archiveStackFillIcon from "@/assets/icons/svg/archive-stack-fill.svg";
import briefcaseFillIcon from "@/assets/icons/svg/briefcase-4-fill.svg";
import progressFillIcon from "@/assets/icons/svg/progress-6-fill.svg";

const isDemo = import.meta.env.VITE_APP_MODE === "demo";

const pageLoading = ref(false);
const trendRange = ref("30d");
const trendRangeTabs = [
  { label: "\u8fd11\u5468", value: "7d" },
  { label: "\u8fd11\u6708", value: "30d" },
  { label: "\u8fd1\u534a\u5e74", value: "180d" },
];
const trendChartRef = ref(null);
const industryChartRef = ref(null);
const lowActivityPage = ref(1);
const lowActivityPageSize = ref(10);
const newReleasePage = ref(1);
const newReleasePageSize = ref(10);

const dashboardData = ref(createEmptyDashboardData());

let trendChartInstance;
let industryChartInstance;
const industryDistributionColors = ["#3f72ff", "#41cbc3", "#f5c446", "#8c58ff"];

const trendChartGrid = {
  top: "15%",
  right: "3%",
  bottom: "3%",
  left: "3%",
  containLabel: true,
};

const chartTextStyle = {
  color: "#6f7f95",
  fontSize: 12,
};

const summaryCardConfigs = [
  {
    key: "total",
    title: "应用总数",
    icon: archiveStackFillIcon,
    iconSize: 33,
    color: "blue",
  },
  {
    key: "active",
    title: "活跃应用数",
    tag: "近7日",
    icon: progressFillIcon,
    iconSize: 33,
    color: "green",
  },
  {
    key: "common",
    title: "横向通用应用",
    icon: appsAiFillIcon,
    iconSize: 33,
    color: "indigo",
  },
  {
    key: "industry",
    title: "纵向行业应用",
    icon: briefcaseFillIcon,
    iconSize: 33,
    color: "purple",
  },
];

const summaryCards = computed(() =>
  summaryCardConfigs.map((config) => ({
    ...config,
    value: dashboardData.value.summary[config.key] ?? "--",
  }))
);

const lowActivityList = computed(() => dashboardData.value.lowActivity || []);
const newReleaseList = computed(() => dashboardData.value.newRelease || []);
const industryLegendList = computed(() =>
  (dashboardData.value.industryDistribution || []).map((item, index) => ({
    ...item,
    color:
      industryDistributionColors[index % industryDistributionColors.length],
  }))
);
const industryTotal = computed(() =>
  (dashboardData.value.industryDistribution || []).reduce(
    (sum, item) => sum + (Number(item.value) || 0),
    0
  )
);
const activeTrendData = computed(() => {
  const trend = dashboardData.value.trend || {};

  if (trend.datasets?.[trendRange.value]) {
    return trend.datasets[trendRange.value];
  }

  if (
    Array.isArray(trend.labels) &&
    Array.isArray(trend.visits) &&
    Array.isArray(trend.activeUsers)
  ) {
    return trend;
  }

  return { labels: [], visits: [], activeUsers: [] };
});

const paginatedLowActivityList = computed(() =>
  paginate(
    lowActivityList.value,
    lowActivityPage.value,
    lowActivityPageSize.value
  )
);
const paginatedNewReleaseList = computed(() =>
  paginate(newReleaseList.value, newReleasePage.value, newReleasePageSize.value)
);

const showTrendChart = computed(
  () =>
    dashboardData.value.mode !== "placeholder" &&
    activeTrendData.value.labels.length > 0
);
const showIndustryChart = computed(
  () =>
    dashboardData.value.mode !== "placeholder" &&
    dashboardData.value.industryDistribution.length > 0
);

const lowActivityEmptyText = computed(() =>
  getTableEmptyText(lowActivityList.value, "暂无低活跃应用")
);
const newReleaseEmptyText = computed(() =>
  getTableEmptyText(newReleaseList.value, "暂无新发布应用")
);

function createEmptyDashboardData() {
  return {
    mode: "placeholder",
    updatedAt: "",
    summary: {
      total: "--",
      active: "--",
      common: "--",
      industry: "--",
    },
    trend: {
      datasets: {
        "7d": { labels: [], visits: [], activeUsers: [] },
        "30d": { labels: [], visits: [], activeUsers: [] },
        "180d": { labels: [], visits: [], activeUsers: [] },
      },
    },
    industryDistribution: [],
    lowActivity: [],
    newRelease: [],
  };
}

watch(trendRange, () => {
  nextTick(() => {
    renderTrendChart();
  });
});

function createDemoDashboardData() {
  return {
    mode: "demo",
    updatedAt: "2026-03-31 16:30:00",
    summary: {
      total: 8,
      active: 5,
      common: 3,
      industry: 5,
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
          visits: [196, 248, 226, 241, 254, 268, 286],
          activeUsers: [48, 59, 54, 57, 60, 62, 67],
        },
        "30d": {
          labels: [
            "03-02",
            "03-06",
            "03-10",
            "03-14",
            "03-18",
            "03-22",
            "03-26",
            "03-31",
          ],
          visits: [108, 152, 136, 188, 164, 212, 248, 286],
          activeUsers: [26, 38, 34, 46, 42, 54, 59, 67],
        },
        "180d": {
          labels: ["10月", "11月", "12月", "01月", "02月", "03月"],
          visits: [2380, 2650, 2980, 3320, 3840, 4520],
          activeUsers: [480, 530, 612, 688, 752, 836],
        },
      },
    },
    industryDistribution: [
      { name: "垂直行业", value: 5 },
      { name: "职能辅助", value: 2 },
      { name: "通用效率", value: 1 },
    ],
    lowActivity: [
      {
        name: "会议纪要生成器",
        visits7d: 0,
        status: "已发布",
        statusTone: "success",
        publishedAt: "2026-01-15",
      },
      {
        name: "代码规范检查助手",
        visits7d: 2,
        status: "已发布",
        statusTone: "success",
        publishedAt: "2026-02-10",
      },
    ],
    newRelease: [
      {
        name: "水利工程助手V2",
        category: "垂直行业",
        creator: "张工",
        publishedAt: "2026-03-30",
      },
      {
        name: "通用文案撰写",
        category: "通用效率",
        creator: "李运",
        publishedAt: "2026-03-28",
      },
      {
        name: "财务报表解读",
        category: "职能辅助",
        creator: "王财",
        publishedAt: "2026-03-25",
      },
    ],
  };
}

async function fetchAppOperationDashboard() {
  // TODO: 后端接口就绪后，在这里接入真实数据。
  // const { data } = await getAppOperationDashboard();
  // return {
  //   mode: "live",
  //   updatedAt: data.updatedAt || "",
  //   summary: data.summary || {},
  //   trend: data.trend || { labels: [], visits: [], activeUsers: [] },
  //   industryDistribution: data.industryDistribution || [],
  //   lowActivity: data.lowActivity || [],
  //   newRelease: data.newRelease || [],
  // };
  return createEmptyDashboardData();
}

async function loadDashboardData() {
  pageLoading.value = true;

  try {
    dashboardData.value = isDemo
      ? createDemoDashboardData()
      : await fetchAppOperationDashboard();
  } finally {
    pageLoading.value = false;
    await nextTick();
    renderTrendChart();
    renderIndustryChart();
  }
}

function paginate(list, page, pageSize) {
  const start = (page - 1) * pageSize;
  return list.slice(start, start + pageSize);
}

function getTableEmptyText(list, fallback) {
  if (dashboardData.value.mode === "placeholder") return "接口预留中";
  if (!list.length) return fallback;
  return "当前筛选下暂无记录";
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

  const { labels, visits, activeUsers } = activeTrendData.value;
  const maxVal = Math.max(...visits, ...activeUsers, 1);
  const axisConfig = getNiceAxisConfig(maxVal, 4);

  trendChartInstance = createChart(trendChartRef.value, trendChartInstance);
  if (!trendChartInstance) return;

  trendChartInstance.setOption({
    animationDuration: 900,
    animationEasing: "cubicOut",
    color: ["#52a8ff", "#47d97b"],
    tooltip: {
      trigger: "axis",
      backgroundColor: "#ffffff",
      borderColor: "#d9e7f6",
      borderWidth: 1,
      textStyle: { color: "#607086", fontSize: 12 },
    },
    // legend: {
    //   top: 2,
    //   left: 8,
    //   itemWidth: 10,
    //   itemHeight: 10,
    //   itemGap: 18,
    //   icon: "circle",
    //   textStyle: chartTextStyle,
    //   data: ["访问量", "活跃用户"],
    // },
    grid: trendChartGrid,
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: labels,
      axisLine: { lineStyle: { color: "#7f8da3" } },
      axisTick: { show: false },
      axisLabel: {
        ...chartTextStyle,
        margin: 12,
      },
    },
    yAxis: {
      type: "value",
      min: axisConfig.min,
      max: axisConfig.max,
      interval: axisConfig.interval,
      splitNumber: 4,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        color: "#6f7f95",
        fontSize: 12,
        margin: 8,
      },
      splitLine: { lineStyle: { color: "#dbe8f5", type: "solid" } },
    },
    series: [
      {
        name: "访问量",
        type: "line",
        smooth: false,
        symbol: "circle",
        symbolSize: 8,
        data: visits,
        lineStyle: { width: 2, color: "#52a8ff" },
        itemStyle: { color: "#fff", borderColor: "#52a8ff", borderWidth: 2 },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "rgba(82, 168, 255, 0.22)" },
            { offset: 1, color: "rgba(82, 168, 255, 0.02)" },
          ]),
        },
      },
      {
        name: "活跃用户",
        type: "line",
        smooth: false,
        symbol: "circle",
        symbolSize: 8,
        data: activeUsers,
        lineStyle: { width: 2, color: "#47d97b" },
        itemStyle: { color: "#fff", borderColor: "#47d97b", borderWidth: 2 },
      },
    ],
  });
}

function renderIndustryChart() {
  if (!showIndustryChart.value || !industryChartRef.value) {
    disposeChart(industryChartInstance);
    industryChartInstance = null;
    return;
  }

  const data = dashboardData.value.industryDistribution;

  industryChartInstance = createChart(
    industryChartRef.value,
    industryChartInstance
  );
  if (!industryChartInstance) return;

  industryChartInstance.setOption({
    animationDuration: 900,
    tooltip: {
      trigger: "item",
      formatter: "{b}: {c} ({d}%)",
    },
    series: [
      {
        type: "pie",
        radius: ["56%", "84%"],
        center: ["50%", "50%"],
        startAngle: 90,
        clockwise: true,
        avoidLabelOverlap: false,
        label: { show: false },
        labelLine: { show: false },
        itemStyle: {
          borderColor: "#fff",
          borderWidth: 4,
          borderRadius: 0,
        },
        data: data.map((item, index) => ({
          ...item,
          itemStyle: {
            color:
              industryDistributionColors[
                index % industryDistributionColors.length
              ],
          },
        })),
      },
    ],
  });
}

function resizeCharts() {
  trendChartInstance?.resize();
  industryChartInstance?.resize();
}

function formatDisplayValue(value) {
  return value === 0 || value ? value : "--";
}

function getLowActivityStatusTagType(tone, status) {
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

  if (status === "已发布") {
    return "success";
  }

  return "default";
}

onMounted(() => {
  loadDashboardData();
  window.addEventListener("resize", resizeCharts);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeCharts);
  disposeChart(trendChartInstance);
  disposeChart(industryChartInstance);
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
  --brand-indigo: #4f7dff;
  --brand-orange: #ff8a3d;
  --brand-purple: #8c58ff;
  min-height: 100%;
  background: var(--bg-page);
}

.app-operation-shell {
  display: grid;
  gap: 15px;
  // margin-top: 15px;
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
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
}

.summary-card {
  padding: 18px 20px;
}

.summary-card__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
}

.summary-card__title-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.summary-card__title {
  color: var(--text-main);
  font-size: 14px;
  font-weight: 500;
}

.summary-card__tag {
  display: inline-flex;
  align-items: center;
  height: 20px;
  padding: 0 6px;
  border-radius: 2px;
  background: #f5f7fb;
  color: var(--text-mute);
  font-size: 12px;
}

.summary-card__value-row {
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

.summary-card__icon.is-green {
  color: #fff;
  background: linear-gradient(135deg, #74de86 0%, #4acb69 100%);
  box-shadow: 0 8px 18px rgba(74, 203, 105, 0.24);
}

.summary-card__icon.is-indigo {
  color: #fff;
  background: linear-gradient(135deg, #8794ff 0%, #5b67ff 100%);
  box-shadow: 0 8px 18px rgba(91, 103, 255, 0.24);
}

.summary-card__icon.is-purple {
  color: #fff;
  background: linear-gradient(135deg, #bb87ff 0%, #925cff 100%);
  box-shadow: 0 8px 18px rgba(146, 92, 255, 0.24);
}

.analytics-grid {
  display: grid;
  grid-template-columns: minmax(0, 2.2fr) minmax(440px, 0.95fr);
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

.card-extra {
  display: none;
}

.chart-body {
  // padding: 16px 20px 20px;
  min-height: 280px;
}

.chart-body--compact {
  min-height: 280px;
}

.chart-body--industry {
  display: flex;
  align-items: center;
  padding: 12px 18px 12px 8px;
}

.industry-chart-layout {
  display: grid;
  grid-template-columns: minmax(0, 190px) minmax(0, 1fr);
  align-items: center;
  width: 100%;
  gap: 10px;
}

.industry-chart-panel {
  position: relative;
  width: 190px;
}

.trend-chart,
.industry-chart {
  width: 100%;
}

.trend-chart {
  height: 280px;
}

.industry-chart {
  height: 190px;
}

.industry-chart-center {
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

.industry-chart-center strong {
  color: #1f1f1f;
  font-size: 32px;
  line-height: 1;
  font-weight: 700;
}

.industry-chart-center span {
  margin-top: 6px;
  color: #6f7f95;
  font-size: 14px;
  line-height: 1;
}

.industry-legend {
  display: grid;
  gap: 16px;
  align-content: center;
  min-width: 0;
}

.industry-legend__item {
  display: grid;
  grid-template-columns: 10px minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
}

.industry-legend__dot {
  width: 8px;
  height: 8px;
  border-radius: 2px;
}

.industry-legend__label {
  color: var(--text-main);
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.industry-legend__value {
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
  background: #fbfcfe;
  border: 1px solid #eef2f7;
  border-radius: 2px;
}

.chart-placeholder .el-icon {
  margin-bottom: 12px;
  font-size: 44px;
  opacity: 0.35;
}

.chart-placeholder p {
  margin: 0;
  font-size: 14px;
}

.table-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 15px;
}

.table-body {
  padding: 16px 20px 0;
}

.table-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 14px 0 16px;
  border-top: 1px solid var(--line-soft);
  color: var(--text-mute);
  font-size: 13px;
}

:deep(.operation-table) {
  width: 100%;
}

:deep(.operation-table .el-table__inner-wrapper::before) {
  display: none;
}

:deep(.operation-table thead) {
  height: 53px;
}

:deep(.operation-table thead .el-table__cell.is-leaf) {
  background: rgba(19, 90, 251, 0.04) !important;
}

:deep(.operation-table th.el-table__cell) {
  padding: 12px 0;
  background: rgba(19, 90, 251, 0.04);
}

:deep(.operation-table th.el-table__cell > .cell) {
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
  font-weight: 500;
}

:deep(.operation-table td.el-table__cell) {
  padding: 14px 0;
}

:deep(.operation-table td.el-table__cell > .cell) {
  color: var(--text-main);
  font-size: 14px;
}

:deep(.operation-table .el-table__empty-block) {
  min-height: 180px;
}

:deep(.table-footer .el-pagination) {
  margin-left: auto;
}

@media (max-width: 1200px) {
  .summary-grid,
  .table-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .analytics-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .summary-grid,
  .table-grid {
    grid-template-columns: 1fr;
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

  .chart-body--industry {
    padding: 16px 20px 20px;
  }

  .industry-chart-layout {
    grid-template-columns: 1fr;
    justify-items: center;
    gap: 18px;
  }

  .industry-legend {
    width: 100%;
  }
}
</style>
