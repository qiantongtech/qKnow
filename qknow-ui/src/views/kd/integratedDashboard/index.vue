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
  <div class="app-container integrated-dashboard">
    <GuideTip v-if="isDemo" tip-id="index" />

    <section class="hero-panel" :class="{ collapsed: isHeroCollapsed }">
      <div class="hero-panel__head">
        <div class="hero-brand">
          <span class="hero-brand__badge">qKnow 专业版</span>
          <span class="hero-brand__text">解锁企业级 AI 核心生产力</span>
        </div>
        <button
          type="button"
          class="hero-collapse"
          @click="isHeroCollapsed = !isHeroCollapsed"
        >
          <!-- <span>{{ isHeroCollapsed ? "展开面板" : "收起面板" }}</span> -->
          <el-icon
            class="hero-collapse__icon"
            :class="{ 'is-collapsed': isHeroCollapsed }"
          >
            <ArrowUp />
          </el-icon>
        </button>
      </div>

      <div
        class="hero-panel__body-shell"
        :class="{ 'is-collapsed': isHeroCollapsed }"
        :aria-hidden="isHeroCollapsed"
      >
        <div class="hero-panel__body-clip">
          <div class="hero-panel__body">
            <div class="hero-copy">
              <h1>从知识支撑到解决方案输出，打通企业智能化全链路</h1>
              <p>
                qKnow
                基于知识图谱、知识库、Bot构建与应用体系，构建从底层能力到上层场景落地的完整平台机制，帮助企业把知识能力转化为业务能力，把
                AI 能力转化为行业解决方案。
              </p>

              <div class="hero-tags">
                <span
                  v-for="tag in heroHighlights"
                  :key="tag.label"
                  class="hero-tag"
                >
                  <img
                    :src="tag.icon"
                    :alt="tag.label"
                    class="hero-tag__icon"
                  />
                  {{ tag.label }}
                </span>
              </div>

              <el-button
                class="hero-action"
                type="primary"
                @click="routeTo('https://qiantong.tech/')"
              >
                了解更多企业级特性
              </el-button>
            </div>

            <HeroArchFlow class="hero-flow" />
            <div v-if="false" class="hero-flow">
              <div class="hero-flow__backdrop" aria-hidden="true">
                <span class="hero-flow__shape hero-flow__shape--upper"></span>
                <span class="hero-flow__shape hero-flow__shape--lower"></span>
                <span class="hero-flow__shape hero-flow__shape--right"></span>
              </div>

              <svg
                class="hero-flow__svg"
                viewBox="0 0 520 248"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <defs>
                  <linearGradient
                    id="flow-brace-left"
                    x1="148"
                    y1="82"
                    x2="148"
                    y2="188"
                  >
                    <stop offset="0%" stop-color="rgba(106, 255, 179, 0.88)" />
                    <stop
                      offset="100%"
                      stop-color="rgba(123, 145, 255, 0.88)"
                    />
                  </linearGradient>
                  <linearGradient
                    id="flow-brace-right"
                    x1="350"
                    y1="82"
                    x2="350"
                    y2="188"
                  >
                    <stop offset="0%" stop-color="rgba(214, 162, 255, 0.76)" />
                    <stop
                      offset="100%"
                      stop-color="rgba(240, 171, 255, 0.92)"
                    />
                  </linearGradient>
                </defs>

                <path
                  d="M 148 82 C 134 86, 114 98, 114 110 C 114 118, 120 122, 128 124 C 120 126, 114 132, 114 146 C 114 160, 134 174, 148 188"
                  stroke="url(#flow-brace-left)"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-dasharray="4 9"
                />
                <path
                  d="M 350 82 C 364 86, 384 98, 384 110 C 384 118, 378 122, 370 124 C 378 126, 384 132, 384 146 C 384 160, 364 174, 350 188"
                  stroke="url(#flow-brace-right)"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-dasharray="4 9"
                />

                <circle class="hero-flow__dot hero-flow__dot--green" r="4">
                  <animateMotion
                    dur="5.2s"
                    repeatCount="indefinite"
                    path="M 148 82 C 134 86, 114 98, 114 110 C 114 118, 120 122, 128 124 C 120 126, 114 132, 114 146 C 114 160, 134 174, 148 188"
                  />
                </circle>
                <circle class="hero-flow__dot hero-flow__dot--blue" r="4">
                  <animateMotion
                    dur="5.2s"
                    repeatCount="indefinite"
                    begin="2.1s"
                    path="M 148 82 C 134 86, 114 98, 114 110 C 114 118, 120 122, 128 124 C 120 126, 114 132, 114 146 C 114 160, 134 174, 148 188"
                  />
                </circle>
                <circle class="hero-flow__dot hero-flow__dot--violet" r="4">
                  <animateMotion
                    dur="5.2s"
                    repeatCount="indefinite"
                    begin="0.9s"
                    path="M 350 82 C 364 86, 384 98, 384 110 C 384 118, 378 122, 370 124 C 378 126, 384 132, 384 146 C 384 160, 364 174, 350 188"
                  />
                </circle>
                <circle class="hero-flow__dot hero-flow__dot--pink" r="4">
                  <animateMotion
                    dur="5.2s"
                    repeatCount="indefinite"
                    begin="2.8s"
                    path="M 350 82 C 364 86, 384 98, 384 110 C 384 118, 378 122, 370 124 C 378 126, 384 132, 384 146 C 384 160, 364 174, 350 188"
                  />
                </circle>
              </svg>

              <div class="hero-node hero-node--left">
                <div class="hero-node__icon">
                  <svg
                    class="hero-node__svg"
                    viewBox="0 0 24 24"
                    aria-hidden="true"
                  >
                    <rect x="7" y="7" width="10" height="10" rx="2.5"></rect>
                    <path
                      d="M12 4.8v2.1M12 17.1v2.1M4.8 12h2.1M17.1 12h2.1M6.7 6.7l1.5 1.5M15.8 15.8l1.5 1.5M17.3 6.7l-1.5 1.5M8.2 15.8l-1.5 1.5"
                    ></path>
                  </svg>
                </div>
                <span>知识资产 &amp; 模型</span>
              </div>

              <div class="hero-stage hero-stage--top">
                <div class="hero-stage__icon is-green">
                  <svg
                    class="hero-stage__svg"
                    viewBox="0 0 24 24"
                    aria-hidden="true"
                  >
                    <path d="M8 8V7a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v1"></path>
                    <path
                      d="M5.5 9.5h13a1 1 0 0 1 1 1v6.8a1.2 1.2 0 0 1-1.2 1.2H5.7a1.2 1.2 0 0 1-1.2-1.2v-6.8a1 1 0 0 1 1-1Z"
                    ></path>
                    <path d="M4.5 12.2h15"></path>
                  </svg>
                </div>
                <div>
                  <strong>Bot 构建层</strong>
                  <span>智能体技能封装</span>
                </div>
              </div>

              <div class="hero-stage hero-stage--bottom">
                <div class="hero-stage__icon is-blue">
                  <svg
                    class="hero-stage__svg"
                    viewBox="0 0 24 24"
                    aria-hidden="true"
                  >
                    <path d="M12 4.8 17.5 8 12 11.2 6.5 8 12 4.8Z"></path>
                    <path d="M6.5 8v8l5.5 3.2V11.2"></path>
                    <path d="M17.5 8v8L12 19.2"></path>
                  </svg>
                </div>
                <div>
                  <strong>场景应用层</strong>
                  <span>场景编排与部署</span>
                </div>
              </div>

              <div class="hero-node hero-node--right">
                <div class="hero-node__icon hero-node__icon--diamond is-purple">
                  <svg
                    class="hero-node__svg hero-node__svg--stack"
                    viewBox="0 0 24 24"
                    aria-hidden="true"
                  >
                    <path d="M6.5 8.5 12 5.5l5.5 3-5.5 3-5.5-3Z"></path>
                    <path d="M8 12.1 12 14.3l4-2.2"></path>
                    <path d="M9.2 15.3 12 16.8l2.8-1.5"></path>
                  </svg>
                </div>
                <span>业务解决方案</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <div class="dashboard-grid">
      <div ref="dashboardMainRef" class="dashboard-main">
        <section
          class="welcome-card panel-card"
          :style="{ backgroundImage: `url(${welcomeBanner})` }"
        >
          <div class="info-main">
            <div class="welcome-avatar">
              <img v-if="userAvatar" :src="userAvatar" alt="" />
              <el-icon v-else><User /></el-icon>
            </div>
            <div class="info-con">
              <div class="info-con-name">
                {{ greetingInfo.title }}，{{ displayName }}，{{
                  greetingInfo.tail
                }}
              </div>
              <div class="info-con-desc">
                <span class="welcome-role">{{
                  userStore.roles[0]
                    ? getRoleName(userStore.roles[0], userRole)
                    : "-"
                }}</span>
                <el-divider
                  direction="vertical"
                  class="custom-vertical-divider"
                />
                {{ dailyQuote }}
              </div>
            </div>

            <div class="info-btns">
              <el-button
                type="primary"
                size="large"
                class="info-btn-dft welcome-button"
                @click="goProfile"
              >
                <img
                  :src="profileIcon"
                  alt=""
                  class="welcome-button__icon img-u"
                />
                个人中心
              </el-button>
              <el-button
                type="primary"
                plain
                class="info-btn-dft welcome-button welcome-button--plain"
                @click="handleLogout"
              >
                <img :src="logoutIcon" alt="" class="welcome-button__icon" />
                退出登录
              </el-button>
            </div>
          </div>
        </section>

        <section class="overview-grid">
          <article
            v-for="item in overviewCards"
            :key="item.name"
            class="overview-card"
          >
            <div class="overview-card__head">
              <div>
                <div class="overview-card__title">{{ item.name }}</div>
                <div class="overview-card__value">
                  {{ item.value }}
                  <span>个</span>
                </div>
              </div>
              <div class="overview-card__icon" :class="`is-${item.color}`">
                <img
                  :src="item.icon"
                  :alt="item.name"
                  class="overview-card__icon-image"
                  :style="{
                    width: `${item.iconSize}px`,
                    height: `${item.iconSize}px`,
                  }"
                />
              </div>
            </div>

            <div class="overview-card__meta">
              <div
                v-for="meta in item.meta"
                :key="meta.label"
                class="overview-card__meta-item"
              >
                <span>{{ meta.label }}</span>
                <strong>{{ meta.value }}</strong>
              </div>
            </div>
          </article>
        </section>

        <div class="analytics-row">
          <section class="chart-card panel-card">
            <div class="card-head">
              <div class="card-title">Bot 数量统计</div>
              <div class="card-tools">
                <div class="tab-switch">
                  <button
                    v-for="tab in rangeTabs"
                    :key="tab.value"
                    type="button"
                    :class="{ active: botRange === tab.value }"
                    @click="botRange = tab.value"
                  >
                    {{ tab.label }}
                  </button>
                </div>
                <button
                  type="button"
                  class="link-button"
                  @click="routeTo('/system/bot')"
                >
                  查看更多
                </button>
              </div>
            </div>

            <div class="legend-row">
              <span class="legend-item">
                <i class="legend-dot is-blue"></i>
                内置
              </span>
              <span class="legend-item">
                <i class="legend-dot is-teal"></i>
                外部
              </span>
            </div>

            <div class="bot-chart">
              <div
                ref="botChartRef"
                class="echart-panel bot-chart__canvas"
              ></div>
            </div>
          </section>

          <section class="chart-card panel-card">
            <div class="card-head">
              <div class="card-title">应用统计</div>
              <div class="card-tools">
                <div class="tab-switch">
                  <button
                    v-for="tab in rangeTabs"
                    :key="tab.value"
                    type="button"
                    :class="{ active: appRange === tab.value }"
                    @click="appRange = tab.value"
                  >
                    {{ tab.label }}
                  </button>
                </div>
                <button
                  type="button"
                  class="link-button"
                  @click="routeTo('/app/chatflow/conversation')"
                >
                  查看更多
                </button>
              </div>
            </div>

            <div class="app-summary">
              <article class="app-summary__card">
                <span>现有应用</span>
                <strong>{{ activeAppData.current }}<em>个</em></strong>
              </article>
              <article class="app-summary__card">
                <span>应用实例</span>
                <strong>{{ activeAppData.examples }}<em>个</em></strong>
              </article>
            </div>

            <div class="legend-row">
              <span class="legend-item">
                <i class="legend-dot is-indigo"></i>
                实例增长趋势
              </span>
            </div>

            <div ref="appChartRef" class="line-chart echart-panel"></div>
          </section>
        </div>

        <section class="chart-card panel-card asset-card">
          <div class="card-head">
            <div class="card-title">知识资产统计</div>
            <div class="legend-row is-right">
              <span class="legend-item">
                <i class="legend-dot is-blue"></i>
                图谱实体数
              </span>
              <span class="legend-item">
                <i class="legend-dot is-orange"></i>
                知识库文档数
              </span>
            </div>
          </div>

          <div
            ref="assetChartRef"
            class="line-chart line-chart--wide echart-panel"
          ></div>
        </section>
      </div>

      <aside ref="dashboardSideRef" class="dashboard-side qxbox">
        <section class="weather-card module-2">
          <Weather />
        </section>

        <section class="side-card panel-card notice-card">
          <div class="card-head">
            <div class="card-title">新闻公告</div>
            <button
              type="button"
              class="link-button"
              @click="routeTo('/system/notice')"
            >
              查看更多
            </button>
          </div>
          <div class="notice-list">
            <div
              class="module-item"
              v-for="(item, index) in notices"
              :key="index"
              @click="goxinwen(item)"
            >
              <!--              <dict-tag :options="sys_notice_type" :value="item.noticeType" />-->
              <template v-if="item.topFlag == 1">
                <img src="@/assets/system/images/notice/new1.svg" alt="" />
              </template>
              <template v-else>
                <img
                  v-if="item.noticeType == 1"
                  src="@/assets/system/images/notice/new2.svg"
                  alt=""
                />
                <img
                  v-if="item.noticeType == 2"
                  src="@/assets/system/images/notice/new4.svg"
                  alt=""
                />
              </template>
              <div class="value" :title="item.noticeTitle">
                {{ item.noticeTitle }}
              </div>
              <!--              <div class="time">{{ parseTime(item.createTime, "{y}-{m}-{d}") }}</div>-->
              <div class="time">{{ timeAgo(item.createTime) }}</div>
            </div>
          </div>
        </section>

        <section class="side-card panel-card quick-entry-card">
          <div class="card-head">
            <div class="card-title">快捷功能入口</div>
          </div>

          <div class="quick-grid">
            <button
              v-for="item in quickLinks"
              :key="item.name"
              type="button"
              class="quick-card"
              :class="`is-${item.color}`"
              @click="routeTo(item.path)"
            >
              <div class="quick-card__icon">
                <img
                  :src="item.icon"
                  :alt="item.name"
                  class="quick-card__icon-image"
                  :style="{
                    width: `${item.iconSize}px`,
                    height: `${item.iconSize}px`,
                  }"
                />
              </div>
              <span>{{ item.name }}</span>
            </button>
          </div>
        </section>

        <section
          class="side-card panel-card solution-card"
          :style="{ marginTop: `${solutionCardOffset}px` }"
        >
          <div class="card-head">
            <div class="card-title">解决方案</div>
            <button
              type="button"
              class="link-button"
              @click="routeTo('/app/agent')"
            >
              查看更多
            </button>
          </div>

          <div class="solution-card__body">
            <div class="solution-ring">
              <div
                ref="solutionChartRef"
                class="solution-ring__chart echart-panel"
              ></div>
              <div class="solution-ring__inner">
                <strong>{{ solutionTotal }}</strong>
                <span>总方案</span>
              </div>
            </div>

            <div class="solution-legend">
              <div
                v-for="item in solutions"
                :key="item.name"
                class="solution-legend__item"
              >
                <span
                  class="legend-dot"
                  :style="{ background: item.color }"
                ></span>
                <span>{{ item.name }}</span>
                <strong>{{ item.value }}%</strong>
              </div>
            </div>
          </div>
        </section>
      </aside>
    </div>
  </div>
</template>

<script setup name="IntegratedDashboard">
import {
  computed,
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from "vue";
import { useRouter } from "vue-router";
import { ElMessageBox } from "element-plus";
import * as echarts from "echarts";
import { ArrowUp, User } from "@element-plus/icons-vue";
import Weather from "@/components/Weather/index.vue";
import { loginOut } from "@/api/system/sso-auth.js";
import useUserStore from "@/store/system/user";
import { listRole } from "@/api/system/system/role.js";
import { sortListNotice } from "@/api/system/system/notice.js";
import HeroArchFlow from "./components/HeroArchFlow.vue";
import welcomeBanner from "@/assets/system/images/index/banner.png";
import profileIcon from "@/assets/home/user.png";
import logoutIcon from "@/assets/home/logout.png";
import kxjyIcon from "@/assets/kd/kxjy.svg";
import nlgjIcon from "@/assets/kd/nlgj.svg";
import faldIcon from "@/assets/kd/fald.svg";
import bubbleChartFillIcon from "@/assets/icons/svg/bubble-chart-fill.svg";
import bookOpenFillIcon from "@/assets/icons/svg/book-open-fill.svg";
import messageAiFillIcon from "@/assets/icons/svg/message-ai-3-fill.svg";
import appsAiFillIcon from "@/assets/icons/svg/apps-ai-fill.svg";
import briefcaseFillIcon from "@/assets/icons/svg/briefcase-4-fill.svg";
import boxFillIcon from "@/assets/icons/svg/box-3-fill.svg";
import accountBoxFillIcon from "@/assets/icons/svg/account-box-fill.svg";
import aiGenerateFillIcon from "@/assets/icons/svg/ai-generate-2-fill.svg";
import { timeAgo } from "@/utils/time";

const router = useRouter();
const userStore = useUserStore();
let { proxy } = getCurrentInstance();

const authType = import.meta.env.VITE_APP_AUTH_TYPE;
const isDemo = import.meta.env.VITE_APP_MODE === "demo";

const isHeroCollapsed = ref(false);
const botRange = ref("month");
const appRange = ref("month");

const rangeTabs = [
  { label: "近1周", value: "week" },
  { label: "近1月", value: "month" },
  { label: "近半年", value: "halfYear" },
];

const heroHighlights = [
  { label: "开箱即用", icon: kxjyIcon },
  { label: "能力构建", icon: nlgjIcon },
  { label: "方案落地", icon: faldIcon },
];

const overviewCards = [
  {
    name: "知识图谱",
    value: "3",
    icon: bubbleChartFillIcon,
    iconSize: 33,
    color: "pink",
    meta: [
      { label: "实体总数", value: "65" },
      { label: "关系总数", value: "140" },
    ],
  },
  {
    name: "知识库",
    value: "3",
    icon: bookOpenFillIcon,
    iconSize: 33,
    color: "blue",
    meta: [
      { label: "文档总数", value: "1,245" },
      { label: "切片总数", value: "12,450" },
    ],
  },
  {
    name: "Bot",
    value: "15",
    icon: messageAiFillIcon,
    iconSize: 33,
    color: "green",
    meta: [
      { label: "运行中", value: "12" },
      { label: "近7日调用", value: "8,920" },
    ],
  },
  {
    name: "应用",
    value: "8",
    icon: appsAiFillIcon,
    iconSize: 33,
    color: "violet",
    meta: [
      { label: "活跃应用", value: "5" },
      { label: "行业应用", value: "5" },
    ],
  },
  {
    name: "解决方案",
    value: "5",
    icon: briefcaseFillIcon,
    iconSize: 33,
    color: "purple",
    meta: [
      { label: "已上架", value: "3" },
      { label: "企业内部", value: "2" },
    ],
  },
];

const botDatasets = {
  week: [
    { label: "第1周", inner: 38, outer: 18 },
    { label: "第2周", inner: 44, outer: 26 },
    { label: "第3周", inner: 51, outer: 38 },
    { label: "本周", inner: 58, outer: 47 },
  ],
  month: [
    { label: "第1周", inner: 39, outer: 20 },
    { label: "第2周", inner: 45, outer: 28 },
    { label: "第3周", inner: 52, outer: 39 },
    { label: "本周", inner: 58, outer: 48 },
  ],
  halfYear: [
    { label: "Q1", inner: 26, outer: 14 },
    { label: "Q2", inner: 38, outer: 24 },
    { label: "Q3", inner: 49, outer: 35 },
    { label: "Q4", inner: 63, outer: 54 },
  ],
};

const appDatasets = {
  week: {
    current: 6,
    examples: 20,
    labels: ["第1周", "第2周", "第3周", "本周"],
    trend: [6, 14, 17, 26],
  },
  month: {
    current: 8,
    examples: 34,
    labels: ["第1周", "第2周", "第3周", "本周"],
    trend: [8, 17, 18, 30],
  },
  halfYear: {
    current: 14,
    examples: 62,
    labels: ["Q1", "Q2", "Q3", "Q4"],
    trend: [12, 21, 34, 48],
  },
};

const assetStats = {
  labels: ["10月", "11月", "12月", "1月", "2月", "3月", "本月"],
  entities: [18, 29, 36, 50, 52, 61, 66],
  documents: [22, 30, 34, 45, 44, 54, 57],
};

const notices = ref([
  {
    date: "2016-05-05",
    title: "置顶",
    value: "XXXXXxxx科技有限公司",
    type: "danger",
  },
  {
    date: "2016-05-03",
    title: "新闻",
    value: "XXXXXxxx科技有限公司",
    type: "success",
  },
  {
    date: "2016-05-02",
    title: "其他",
    value: "XXXXXxxx科技有限公司",
    type: "primary",
  },
  {
    date: "2016-05-04",
    title: "其他",
    value: "XXXXXxxx科技有限公司",
    type: "info",
  },
  {
    date: "2016-05-06",
    title: "公告",
    value: "XXXXXxxx科技有限公司",
    type: "warning",
  },
]);

const quickLinks = [
  {
    name: "知识图谱",
    path: "/kg/graph",
    icon: bubbleChartFillIcon,
    iconSize: 29,
    color: "pink",
  },
  {
    name: "知识库",
    path: "/kmc/knowledgeBase",
    icon: bookOpenFillIcon,
    iconSize: 29,
    color: "blue",
  },
  {
    name: "AI工作台",
    path: "/app/agent",
    icon: aiGenerateFillIcon,
    iconSize: 29,
    color: "teal",
  },
  {
    name: "应用中心",
    path: "/app/chatflow/conversation",
    icon: boxFillIcon,
    iconSize: 29,
    color: "orange",
  },
  {
    name: "解决方案",
    path: "/app/agent",
    icon: briefcaseFillIcon,
    iconSize: 29,
    color: "indigo",
  },
  {
    name: "系统管理",
    path: "/system/user",
    icon: accountBoxFillIcon,
    iconSize: 29,
    color: "slate",
  },
].map((item) => {
  const targetName = item.name === "应用中心" ? "应用" : item.name;
  const matchedCard = overviewCards.find((card) => card.name === targetName);

  if (!matchedCard) {
    return item;
  }

  return {
    ...item,
    icon: matchedCard.icon,
    color: matchedCard.color,
  };
});

const solutions = [
  { name: "知识检索", value: 40, color: "#3d7dff" },
  { name: "智能客服", value: 30, color: "#22c55e" },
  { name: "代码审查", value: 20, color: "#6366f1" },
  { name: "内部制度", value: 10, color: "#a855f7" },
];

const botChartRef = ref(null);
const appChartRef = ref(null);
const assetChartRef = ref(null);
const solutionChartRef = ref(null);
const dashboardMainRef = ref(null);
const dashboardSideRef = ref(null);
const solutionCardOffset = ref(0);

let botChartInstance;
let appChartInstance;
let assetChartInstance;
let solutionChartInstance;
let layoutResizeObserver;
const SIDE_STACK_BREAKPOINT = 1360;

const quotes = [
  "在这里，知识不再孤立，而是相互生长的森林。",
  "把海量信息，变成你的专属知识地图。",
  "俯瞰知识全貌，看见认知的新边界。",
  "从零散碎片到完整图谱，让智慧有迹可循。",
];

const displayName = computed(
  () => userStore.nickName || userStore.name || "伙伴"
);
const userAvatar = computed(() => userStore.avatar || "");

const greetingInfo = computed(() => {
  const hour = new Date().getHours();

  if (hour < 6) {
    return { title: "夜深了", tail: "注意休息，也别忘了灵感会在清晨继续生长" };
  }
  if (hour < 12) {
    return { title: "上午好", tail: "今天也在稳步推进目标" };
  }
  if (hour < 18) {
    return { title: "下午好", tail: "保持专注，继续加油" };
  }
  return { title: "晚上好", tail: "收拢成果，也为明天蓄力" };
});

const dailyQuote = computed(() => quotes[new Date().getDate() % quotes.length]);
const solutionTotal = computed(() => solutions.length);

// const roleLabel = computed(() => {
//   const role = userStore.roles?.[0];

//   if (!role || role === "ROLE_DEFAULT") {
//     return "平台用户";
//   }
//   if (String(role).toLowerCase().includes("admin")) {
//     return "系统管理员";
//   }
//   return String(role)
//     .replace(/^ROLE_/, "")
//     .replace(/_/g, " ");
// });

function getNotices() {
  let query = {
    pageNum: 1,
    pageSize: 5,
  };
  // listNotice(query).then((response) => {
  //   module6.value = response.rows;
  // });
  sortListNotice(query).then((response) => {
    notices.value = response.rows;
  });
}

const userRole = ref([]);

function getUserRole() {
  listRole().then((res) => {
    userRole.value = res.rows;
  });
}

function getRoleName(value, dict) {
  // 如果数组不存在或 value 为空，直接返回默认值
  if (
    !Array.isArray(dict) ||
    value === undefined ||
    value === null ||
    value === ""
  ) {
    return "-";
  }
  const item = dict.find((item) => item.roleKey == value);
  return item ? item.roleName : "-";
}

const activeAppData = computed(() => appDatasets[appRange.value]);

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

const chartTextStyle = {
  color: "#6f7f95",
  fontSize: 12,
};

const chartGrid = {
  top: "15%",
  right: "3%",
  bottom: "3%",
  left: "3%",
  containLabel: true,
};

const axisTooltip = {
  trigger: "axis",
  backgroundColor: "#ffffff",
  borderColor: "#d9e7f6",
  borderWidth: 1,
  textStyle: { color: "#607086", fontSize: 12 },
};

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

function getCommonCategoryAxis(labels, extra = {}) {
  const { boundaryGap = false, ...rest } = extra;

  return {
    type: "category",
    data: labels,
    boundaryGap,
    axisTick: { show: false },
    axisLine: { lineStyle: { color: "#7f8da3" } },
    axisLabel: { ...chartTextStyle, margin: 12 },
    ...rest,
  };
}

function getCommonValueAxis(values, extra = {}) {
  const { splitCount = 4, ...rest } = extra;
  const axisConfig = getNiceAxisConfig(Math.max(...values, 1), splitCount);

  return {
    type: "value",
    min: axisConfig.min,
    max: axisConfig.max,
    interval: axisConfig.interval,
    splitNumber: splitCount,
    axisTick: { show: false },
    axisLine: { show: false },
    axisLabel: { show: true, ...chartTextStyle, margin: 8 },
    splitLine: {
      show: true,
      lineStyle: {
        color: "#dbe8f5",
        type: "solid",
      },
    },
    ...rest,
  };
}

function renderBotChart() {
  if (!botChartRef.value) {
    return;
  }

  const rows = botDatasets[botRange.value] || [];
  botChartInstance = createChart(botChartRef.value, botChartInstance);

  if (!botChartInstance) {
    return;
  }

  botChartInstance.setOption({
    animationDuration: 900,
    animationEasing: "cubicOut",
    grid: chartGrid,
    tooltip: {
      ...axisTooltip,
      axisPointer: {
        type: "shadow",
        shadowStyle: {
          color: "rgba(52, 109, 255, 0.06)",
        },
      },
    },
    xAxis: getCommonCategoryAxis(
      rows.map((item) => item.label),
      {
        boundaryGap: true,
      }
    ),
    yAxis: {
      ...getCommonValueAxis(
        rows.flatMap((item) => [item.inner, item.outer]),
        {
          splitCount: 6,
        }
      ),
    },
    series: [
      {
        name: "内置",
        type: "bar",
        data: rows.map((item) => item.inner),
        barWidth: 16,
        barGap: "32%",
        itemStyle: {
          borderRadius: 0,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "#6f95ff" },
            { offset: 1, color: "#4f7dff" },
          ]),
        },
      },
      {
        name: "外部",
        type: "bar",
        data: rows.map((item) => item.outer),
        barWidth: 16,
        itemStyle: {
          borderRadius: 0,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "#9fe4e2" },
            { offset: 1, color: "#7fd7d4" },
          ]),
        },
      },
    ],
  });
}

function renderAppChart() {
  if (!appChartRef.value) {
    return;
  }

  const { labels, trend } = activeAppData.value;
  appChartInstance = createChart(appChartRef.value, appChartInstance);

  if (!appChartInstance) {
    return;
  }

  appChartInstance.setOption({
    animationDuration: 900,
    animationEasing: "cubicOut",
    grid: chartGrid,
    tooltip: axisTooltip,
    xAxis: getCommonCategoryAxis(labels),
    yAxis: getCommonValueAxis(trend, {
      splitCount: 4,
    }),
    series: [
      {
        name: "实例增长趋势",
        type: "line",
        data: trend,
        smooth: false,
        symbol: "circle",
        symbolSize: 8,
        lineStyle: {
          width: 2,
          color: "#52a8ff",
        },
        itemStyle: {
          color: "#fff",
          borderColor: "#52a8ff",
          borderWidth: 2,
        },
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

function renderAssetChart() {
  if (!assetChartRef.value) {
    return;
  }

  const allValues = [...assetStats.entities, ...assetStats.documents];
  assetChartInstance = createChart(assetChartRef.value, assetChartInstance);

  if (!assetChartInstance) {
    return;
  }

  assetChartInstance.setOption({
    animationDuration: 900,
    animationEasing: "cubicOut",
    grid: chartGrid,
    tooltip: axisTooltip,
    xAxis: getCommonCategoryAxis(assetStats.labels),
    yAxis: getCommonValueAxis(allValues, {
      splitCount: 4,
    }),
    series: [
      {
        name: "图谱实体数",
        type: "line",
        data: assetStats.entities,
        smooth: false,
        symbol: "circle",
        symbolSize: 8,
        lineStyle: {
          width: 2,
          color: "#52a8ff",
        },
        itemStyle: {
          color: "#fff",
          borderColor: "#52a8ff",
          borderWidth: 2,
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "rgba(82,168,255,0.22)" },
            { offset: 1, color: "rgba(82,168,255,0.02)" },
          ]),
        },
      },
      {
        name: "知识库文档数",
        type: "line",
        data: assetStats.documents,
        smooth: false,
        symbol: "circle",
        symbolSize: 8,
        lineStyle: {
          width: 2,
          color: "#ff8a3d",
        },
        itemStyle: {
          color: "#fff",
          borderColor: "#ff8a3d",
          borderWidth: 2,
        },
      },
    ],
  });
}

function renderSolutionChart() {
  if (!solutionChartRef.value) {
    return;
  }

  solutionChartInstance = createChart(
    solutionChartRef.value,
    solutionChartInstance
  );

  if (!solutionChartInstance) {
    return;
  }

  solutionChartInstance.setOption({
    animationDuration: 850,
    animationEasing: "cubicOut",
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
        radius: ["48%", "90%"],
        center: ["50%", "50%"],
        startAngle: 90,
        clockwise: true,
        avoidLabelOverlap: true,
        label: {
          show: false,
        },
        labelLine: {
          show: false,
        },
        itemStyle: {
          borderRadius: 0,
          borderColor: "#ffffff",
          borderWidth: 3,
        },
        data: solutions.map((item) => ({
          ...item,
          itemStyle: {
            color: item.color,
          },
        })),
      },
    ],
  });
}

function syncSolutionCardOffset() {
  if (
    typeof window === "undefined" ||
    window.innerWidth <= SIDE_STACK_BREAKPOINT
  ) {
    solutionCardOffset.value = 0;
    return;
  }

  const mainEl = dashboardMainRef.value;
  const sideEl = dashboardSideRef.value;

  if (!mainEl || !sideEl) {
    solutionCardOffset.value = 0;
    return;
  }

  const solutionEl = sideEl.querySelector(".solution-card");

  if (!solutionEl) {
    solutionCardOffset.value = 0;
    return;
  }

  const currentOffset =
    Number.parseFloat(window.getComputedStyle(solutionEl).marginTop) || 0;
  const mainHeight = mainEl.getBoundingClientRect().height;
  const sideHeight = sideEl.getBoundingClientRect().height - currentOffset;

  solutionCardOffset.value = Math.max(Math.round(mainHeight - sideHeight), 0);
}

function resizeCharts() {
  [
    botChartInstance,
    appChartInstance,
    assetChartInstance,
    solutionChartInstance,
  ].forEach((chart) => chart?.resize());

  syncSolutionCardOffset();
}

function renderAllCharts() {
  renderBotChart();
  renderAppChart();
  renderAssetChart();
  renderSolutionChart();
  syncSolutionCardOffset();
}

function setupLayoutResizeObserver() {
  if (typeof ResizeObserver === "undefined") {
    return;
  }

  layoutResizeObserver?.disconnect();
  layoutResizeObserver = new ResizeObserver(() => {
    syncSolutionCardOffset();
  });

  [dashboardMainRef.value, dashboardSideRef.value].forEach((element) => {
    if (element) {
      layoutResizeObserver.observe(element);
    }
  });
}

async function routeTo(path, query = {}) {
  if (!path) {
    return;
  }

  if (path.startsWith("http")) {
    window.open(path, "_blank");
    return;
  }

  try {
    await router.push({ path, query });
  } catch (error) {
    if (path !== router.currentRoute.value.path) {
      console.error(error);
    }
  }
}

function goProfile() {
  routeTo("/user/profile");
}

function openNotice() {
  routeTo("/system/notice");
}

function goxinwen(row) {
  if (row == "list") {
    proxy.$router.push("/system/notice");
  } else {
    proxy.$router.push({
      path: "/system/system/notice/detail",
      query: { id: row.noticeId },
    });
  }
}

function handleLogout() {
  ElMessageBox.confirm("确定注销并退出系统吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => userStore.logOut())
    .then(() => {
      if (authType === "sso") {
        return loginOut(userStore.id).finally(() => {
          window.location.href = "/index";
        });
      }

      window.location.href = "/index";
      return null;
    })
    .catch(() => {});
}

watch(botRange, () => {
  nextTick(() => {
    renderBotChart();
    syncSolutionCardOffset();
  });
});

watch(appRange, () => {
  nextTick(() => {
    renderAppChart();
    syncSolutionCardOffset();
  });
});

onMounted(() => {
  getUserRole();
  getNotices();

  if (!userStore.nickName) {
    userStore.getInfo().catch(() => {});
  }

  nextTick(() => {
    renderAllCharts();
    setupLayoutResizeObserver();
    window.addEventListener("resize", resizeCharts);
  });
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeCharts);
  layoutResizeObserver?.disconnect();
  disposeChart(botChartInstance);
  disposeChart(appChartInstance);
  disposeChart(assetChartInstance);
  disposeChart(solutionChartInstance);
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
  --shadow-card: 0 10px 24px rgba(41, 72, 152, 0.06);
  min-height: auto;
  margin-bottom: 0;
  background: var(--bg-page);
}

.panel-card {
  border-radius: 2px;
  box-shadow: none;
  overflow: hidden;
}

.weather-card,
.module-2 {
  border-radius: 2px;
  box-shadow: none;
}

.hero-panel {
  box-shadow: var(--shadow-card);
  position: relative;
  overflow: hidden;
  padding: 20px 24px 12px;
  margin-top: 16px;
  background: url("@/assets/kd/yxmb.png") no-repeat;
  background-size: 100% 100%;

  color: #fff;
  transition: padding 0.36s cubic-bezier(0.22, 1, 0.36, 1), box-shadow 0.3s ease;
}

.hero-panel::before {
  content: "";
  position: absolute;
  inset: 52px auto auto -40px;
  width: 228px;
  height: 228px;
  border-radius: 50%;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.08) 0%,
    transparent 64%
  );
  pointer-events: none;
}

.hero-panel.collapsed {
  padding-bottom: 18px;
}

.hero-panel__head,
.card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.hero-panel__head {
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

.hero-brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.hero-brand__badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 92px;
  height: 30px;
  padding: 0 14px;
  background: rgba(255, 255, 255, 0.16);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.14);
  font-size: 14px;
  font-weight: 600;
}

.hero-brand__text {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.01em;
}

.hero-collapse,
.link-button,
.notice-item,
.quick-card {
  border: none;
  outline: none;
  background: transparent;
  cursor: pointer;
  font: inherit;
}

.hero-collapse {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.88);
  transition: opacity 0.2s ease, color 0.24s ease;
}

.hero-collapse:hover {
  opacity: 0.8;
}

.hero-collapse__icon {
  transition: transform 0.32s cubic-bezier(0.22, 1, 0.36, 1);
}

.hero-collapse__icon.is-collapsed {
  transform: rotate(180deg);
}

.hero-panel__body-shell {
  display: grid;
  grid-template-rows: 1fr;
  transition: grid-template-rows 0.42s cubic-bezier(0.22, 1, 0.36, 1);
  will-change: grid-template-rows;
}

.hero-panel__body-shell.is-collapsed {
  grid-template-rows: 0fr;
}

.hero-panel__body-clip {
  min-height: 0;
  overflow: hidden;
}

.hero-panel__body {
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(420px, 0.9fr);
  align-items: center;
  gap: 28px;
  padding-top: 5px;
  transform-origin: top center;
  transition: opacity 0.26s ease, transform 0.42s cubic-bezier(0.22, 1, 0.36, 1),
    padding-top 0.42s cubic-bezier(0.22, 1, 0.36, 1);
  will-change: transform, opacity, padding-top;
  backface-visibility: hidden;
  transform: translateZ(0);
}

.hero-panel__body-shell.is-collapsed .hero-panel__body {
  opacity: 0;
  transform: translate3d(0, -10px, 0);
  padding-top: 0;
  pointer-events: none;
}

.hero-copy h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  line-height: 1.45;
}

.hero-copy p {
  max-width: 680px;
  margin: 14px 0 0;
  font-size: 15px;
  line-height: 1.9;
  color: rgba(255, 255, 255, 0.84);
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 22px;
  margin-top: 24px;
}

.hero-tag {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.95);
}

.hero-tag__icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  object-fit: contain;
}

.hero-action {
  height: 42px;
  padding: 0 22px;
  margin-top: 28px;
  font-weight: 600;
  background: #fff;
  color: var(--brand-blue);
  box-shadow: 0 12px 28px rgba(12, 32, 112, 0.18);
}

.hero-action:hover {
  color: var(--brand-blue);
  background: rgba(255, 255, 255, 0.92);
}

.hero-flow {
  position: relative;
  width: 520px;
  max-width: 100%;
  min-height: 248px;
  margin-left: auto;
  overflow: hidden;
  isolation: isolate;
}

.hero-flow__svg {
  position: absolute;
  left: 0;
  top: 0;
  width: 520px;
  height: 248px;
  z-index: 1;
}

.hero-flow__backdrop {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
}

.hero-flow__shape {
  position: absolute;
  display: block;
}

.hero-flow__shape--upper {
  left: 126px;
  top: -156px;
  width: 304px;
  height: 304px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.11);
  opacity: 0.28;
}

.hero-flow__shape--lower {
  left: 194px;
  top: -4px;
  width: 166px;
  height: 332px;

  background: linear-gradient(
    180deg,
    rgba(60, 72, 187, 0.26) 0%,
    rgba(104, 103, 235, 0.08) 100%
  );
  opacity: 0.42;
  transform: rotate(11deg);
}

.hero-flow__shape--right {
  right: -96px;
  top: -20px;
  width: 292px;
  height: 292px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.09);
  opacity: 0.22;
}

.hero-flow__dot {
  filter: drop-shadow(0 0 8px currentColor);
}

.hero-flow__dot--green {
  fill: #68ffb3;
  color: #68ffb3;
}

.hero-flow__dot--blue {
  fill: #7c9bff;
  color: #7c9bff;
}

.hero-flow__dot--violet {
  fill: #d093ff;
  color: #d093ff;
}

.hero-flow__dot--pink {
  fill: #ff9bf0;
  color: #ff9bf0;
}

.hero-node,
.hero-stage {
  position: absolute;
  z-index: 2;
}

.hero-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  color: rgba(255, 255, 255, 0.78);
  font-size: 13px;
  text-align: center;
}

.hero-node span {
  white-space: nowrap;
}

.hero-node--left {
  left: 16px;
  top: 98px;
}

.hero-node--right {
  right: 12px;
  top: 94px;
}

.hero-node__icon {
  display: grid;
  place-items: center;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: rgba(44, 108, 239, 0.14);
  box-shadow: inset 0 0 0 1px rgba(99, 178, 255, 0.3),
    0 0 0 10px rgba(76, 138, 255, 0.08), 0 0 22px rgba(66, 170, 255, 0.22);
  color: #9ad7ff;
  font-size: 22px;
}

.hero-node__icon.is-purple {
  background: linear-gradient(
    180deg,
    rgba(123, 74, 228, 0.48) 0%,
    rgba(127, 63, 207, 0.3) 100%
  );
  box-shadow: inset 0 0 0 1px rgba(201, 155, 255, 0.12),
    0 0 24px rgba(162, 104, 255, 0.22);
  color: #e7c6ff;
}

.hero-node__icon--diamond {
  width: 58px;
  height: 58px;
  transform: rotate(45deg);
}

.hero-node__svg {
  width: 24px;
  height: 24px;
  stroke: currentColor;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
  fill: none;
}

.hero-node__icon--diamond .hero-node__svg {
  transform: rotate(-45deg);
}

.hero-node__svg--stack {
  width: 23px;
  height: 23px;
  stroke-width: 1.6;
}

.hero-stage {
  display: flex;
  align-items: center;
  gap: 16px;
  min-width: 184px;
  padding: 13px 18px;
  border-radius: 15px;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.16) 0%,
    rgba(255, 255, 255, 0.1) 100%
  );
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.2),
    0 14px 24px rgba(22, 29, 82, 0.16);
  backdrop-filter: blur(12px);
}

.hero-stage--top {
  left: 126px;
  top: 26px;
}

.hero-stage--bottom {
  left: 132px;
  top: 136px;
}

.hero-stage__icon {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  color: #fff;
  font-size: 18px;
  border-radius: 15px;
}

.hero-stage__svg {
  width: 22px;
  height: 22px;
  stroke: currentColor;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
  fill: none;
}

.hero-stage__icon.is-green {
  background: linear-gradient(135deg, #41df8b 0%, #1bc0aa 100%);
}

.hero-stage__icon.is-blue {
  background: linear-gradient(135deg, #7d9cff 0%, #63b5ff 100%);
}

.hero-stage strong,
.hero-stage span {
  display: block;
}

.hero-stage strong {
  font-size: 15px;
  font-weight: 700;
}

.hero-stage span {
  margin-top: 4px;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.74);
}

.dashboard-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 340px;
  align-items: start;
  gap: 15px;
  margin-top: 15px;
}

.dashboard-main,
.dashboard-side {
  display: grid;
  align-content: start;
  gap: 15px;
  min-width: 0;
}
.qxbox {
  line-height: 20px;
}

.panel-card {
  background: var(--bg-card);
}

.welcome-card {
  height: 150px;
  padding: 35px 60px 0 32px;
  background-color: #edf4ff;
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
}

.welcome-card .info-main {
  display: flex;
  position: relative;
  min-width: 100%;
  flex-shrink: 0;
}

.welcome-avatar {
  display: grid;
  place-items: center;
  flex-shrink: 0;
  width: 72px;
  height: 72px;
  margin-right: 24px;
  border-radius: 50%;
  overflow: hidden;
  background: rgba(112, 181, 255, 0.12);
  box-shadow: 0 8px 20px rgba(91, 131, 216, 0.12);
  color: var(--brand-blue);
  font-size: 28px;
}

.welcome-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.welcome-card .info-con {
  height: 72px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.welcome-card .info-con-name {
  margin: 0;
  color: rgba(0, 0, 0, 0.85);
  font-size: 20px;
  line-height: 28px;
  font-weight: 600;
}

.welcome-card .info-con-desc {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  margin: 8px 0 0;
  color: #888;
  font-size: 14px;
  line-height: 22px;
}

.welcome-role {
  color: var(--brand-blue);
}

.welcome-card .info-btns {
  position: absolute;
  top: 18px;
  right: 0;
  font-size: 14px;
  font-weight: 500;
}

.welcome-card .info-btns .info-btn-dft {
  width: 100px;
  height: 40px;
  border-radius: 4px !important;
  margin-left: 20px;
}

.welcome-button__icon {
  margin-right: 4px;
  object-fit: contain;
}
// 去除按钮鼠标悬浮效果
.welcome-card .info-btns .info-btn-dft {
  &:hover {
    opacity: 1 !important;
    border-color: var(--el-color-primary) !important;
    background-color: var(--el-color-primary) !important;
    color: white !important;
  }

  &.is-plain:hover {
    background-color: #e9effe !important;
    border-color: #92b2fd !important;
    color: var(--el-color-primary) !important;
  }
}
.img-u {
  margin-top: 5px;
  margin-right: 3px;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 14px;
}

.overview-card {
  padding: 20px;
  background: #fff;
  border-radius: 2px;
  box-shadow: none;
}

.overview-card__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.overview-card__title {
  color: var(--text-main);
  font-size: 15px;
}

.overview-card__value {
  margin-top: 14px;
  color: #000;
  font-size: 28px;
  font-weight: 700;
}

.overview-card__value span {
  margin-left: 3px;
  font-size: 14px;
  font-weight: 500;
}

.overview-card__icon {
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

.overview-card__icon-image {
  position: relative;
  z-index: 1;
  display: block;
  object-fit: contain;
  filter: brightness(0) invert(1);
}

.overview-card__icon::before {
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

.overview-card__icon.is-pink {
  color: #fff;
  background: linear-gradient(135deg, #ff9dc0 0%, #f26193 100%);
  box-shadow: 0 8px 18px rgba(242, 97, 147, 0.24);
}

.overview-card__icon.is-blue {
  color: #fff;
  background: linear-gradient(135deg, #62b6ff 0%, #3a84ff 100%);
  box-shadow: 0 8px 18px rgba(58, 132, 255, 0.24);
}

.overview-card__icon.is-cyan {
  color: #fff;
  background: linear-gradient(135deg, #62b6ff 0%, #3a84ff 100%);
  box-shadow: 0 8px 18px rgba(58, 132, 255, 0.24);
}

.overview-card__icon.is-orange {
  color: #fff;
  background: linear-gradient(135deg, #ffb564 0%, #ff8a3d 100%);
  box-shadow: 0 8px 18px rgba(255, 138, 61, 0.24);
}

.overview-card__icon.is-green {
  color: #fff;
  background: linear-gradient(135deg, #74de86 0%, #4acb69 100%);
  box-shadow: 0 8px 18px rgba(74, 203, 105, 0.24);
}

.overview-card__icon.is-violet {
  color: #fff;
  background: linear-gradient(135deg, #8a97ff 0%, #6276ff 100%);
  box-shadow: 0 8px 18px rgba(98, 118, 255, 0.24);
}

.overview-card__icon.is-purple {
  color: #fff;
  background: linear-gradient(135deg, #bb87ff 0%, #925cff 100%);
  box-shadow: 0 8px 18px rgba(146, 92, 255, 0.24);
}

.overview-card__meta {
  display: grid;
  gap: 8px;
  margin-top: 18px;
  padding-top: 12px;
  border-top: 1px solid #e2e2e2;
}

.overview-card__meta-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  color: var(--text-sub);
  font-size: 13px;
}

.overview-card__meta-item strong {
  color: #334969;
  font-size: 14px;
}

.analytics-row {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 15px;
}

.chart-card,
.side-card {
  display: flex;
  flex-direction: column;
  padding: 0;
  background: #fff;
  min-width: 0;
  min-height: 242px;
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

.card-head {
  min-height: 50px;
  padding: 0 20px;
  border-bottom: 1px solid #e8e8e8;
}

.card-tools {
  display: flex;
  align-items: center;
  gap: 12px;
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

.link-button {
  color: #0f62ff;
  font-size: 14px;
  white-space: nowrap;
}

.legend-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 14px;
  padding: 0 20px;
  color: var(--text-sub);
  font-size: 13px;
}

.legend-row.is-right {
  margin-top: 0;
  padding: 0;
}

.legend-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.legend-dot {
  width: 9px;
  height: 9px;
  border-radius: 50%;
}

.legend-dot.is-green {
  background: linear-gradient(135deg, #47d97b 0%, #27c963 100%);
}

.legend-dot.is-teal {
  background: linear-gradient(135deg, #79e7e0 0%, #4fd0ca 100%);
}

.legend-dot.is-indigo {
  background: #6366f1;
}

.legend-dot.is-blue {
  background: #3d7dff;
}

.legend-dot.is-orange {
  background: #ff8a3d;
}

.bot-chart {
  flex: none;
  height: 252px;
  margin: 8px 20px 16px;
}

.echart-panel,
.bot-chart__canvas {
  width: 100%;
  height: 100%;
}

.bot-chart__bars {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 15px;
  height: 100%;
  padding: 18px 6px 4px;
  background: linear-gradient(
      to top,
      rgba(231, 237, 247, 0.8) 1px,
      transparent 1px
    ),
    linear-gradient(to top, rgba(231, 237, 247, 0.8) 1px, transparent 1px),
    linear-gradient(to top, rgba(231, 237, 247, 0.8) 1px, transparent 1px);
  background-size: 100% 33.333%;
  background-position: left 20px bottom 22px;
  background-repeat: no-repeat;
}

.bot-group {
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  height: 100%;
}

.bot-group__columns {
  display: flex;
  align-items: flex-end;
  justify-content: center;
  gap: 12px;
  width: 100%;
  height: calc(100% - 30px);
}

.bot-bar {
  width: 32px;
  height: var(--value);
  min-height: 16px;
  border-radius: 12px 12px 4px 4px;
  box-shadow: 0 10px 22px rgba(58, 124, 255, 0.14);
}

.bot-bar--green {
  background: linear-gradient(180deg, #4ee28a 0%, #3bcb71 100%);
}

.bot-bar--teal {
  background: linear-gradient(180deg, #7ae7e0 0%, #54d3cb 100%);
}

.bot-group__label {
  color: var(--text-sub);
  font-size: 13px;
}

.app-summary {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin: 12px 20px 0;
}

.app-summary__card {
  padding: 10px 14px;
  background: linear-gradient(90deg, #ffffff 0%, #f3f7fe 100%);
  border: 1px solid #edf1f7;
  border-radius: 2px;
}

.app-summary__card span,
.app-summary__card strong,
.app-summary__card em {
  display: block;
}

.app-summary__card span {
  color: var(--text-sub);
  font-size: 14px;
}

.app-summary__card strong {
  margin-top: 6px;
  color: var(--brand-blue);
  font-size: 16px;
}

.app-summary__card em {
  display: inline;
  margin-left: 4px;
  font-style: normal;
  font-size: 14px;
  font-weight: 500;
}

.line-chart {
  width: auto;
  height: 176px;
  margin: 8px 20px 16px;
  flex: none;
}

.asset-card > .line-chart--wide {
  height: 299px;
  margin: 12px 20px 18px;
}

.line-chart__grid {
  stroke: #e8eef8;
  stroke-width: 1;
  stroke-dasharray: 4 5;
}

.line-chart__path {
  fill: none;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-width: 5;
}

.line-chart__path--app {
  stroke: #606aff;
}

.line-chart__path--blue {
  stroke: #3d7dff;
}

.line-chart__path--orange {
  stroke: #ff8a3d;
}

.line-chart__marker-outer {
  fill: #fff;
  stroke-width: 4;
}

.line-chart__marker-outer--app {
  stroke: #606aff;
}

.line-chart__marker-outer--blue {
  stroke: #3d7dff;
}

.line-chart__marker-outer--orange {
  stroke: #ff8a3d;
}

.line-chart__marker-inner--app {
  fill: #606aff;
}

.line-chart__marker-inner--blue {
  fill: #3d7dff;
}

.line-chart__marker-inner--orange {
  fill: #ff8a3d;
}

.line-chart__label {
  fill: var(--text-sub);
  font-size: 12px;
}

.weather-card {
  height: 150px;
  text-align: center;
}

.weather-card :deep(.weather) {
  height: 100%;
}

.notice-list {
  display: block;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 12px 0px;
  .module-item {
    display: flex;
    align-items: center;
    padding: 6px 20px;
    cursor: pointer;
  }
  .value {
    margin: 0 0 0 10px;
    font-size: 14px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    flex: 1;
    font-size: 14px;
    color: rgba(0, 0, 0, 0.85);
    font-weight: 400;
  }
  .time {
    width: 50px;
    height: 18px;
    line-height: 18px;
    text-align: center;
    font-size: 14px;
    font-weight: 400;
    font-size: 12px;
    color: #ffffff;
    background: #dadada;
    border-radius: 2px;
  }
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px 6px;
  margin-top: 0;
  padding: 16px 14px 18px;
}

.quick-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 12px 8px 8px;
  color: #5a5e66;
  transition: none;
}

.quick-card:hover {
  transform: none;
  box-shadow: none;
}

.quick-card__icon {
  display: grid;
  place-items: center;
  position: relative;
  width: 44px;
  height: 44px;
  overflow: hidden;
  border-radius: 10px;
  box-shadow: 0 6px 14px rgba(61, 109, 255, 0.16),
    inset 0 1px 0 rgba(255, 255, 255, 0.22);
}

.quick-card__icon-image {
  position: relative;
  z-index: 1;
  display: block;
  object-fit: contain;
  filter: brightness(0) invert(1);
}

.quick-card__icon::before {
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

.quick-card span {
  font-size: 14px;
}

.quick-card.is-pink {
  background: transparent;
}

.quick-card.is-pink .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #ff9dc0 0%, #f26193 100%);
}

.quick-card.is-blue {
  background: transparent;
}

.quick-card.is-blue .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #5aa0ff 0%, #2f75ff 100%);
}

.quick-card.is-teal {
  background: transparent;
}

.quick-card.is-teal .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #45d7cb 0%, #14b8a6 100%);
}

.quick-card.is-orange {
  background: transparent;
}

.quick-card.is-orange .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #ffb15f 0%, #ff8a3d 100%);
}

.quick-card.is-green {
  background: transparent;
}

.quick-card.is-green .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #72d35d 0%, #4cc576 100%);
}

.quick-card.is-violet {
  background: transparent;
}

.quick-card.is-violet .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #7b8dff 0%, #5d6dff 100%);
}

.quick-card.is-purple {
  background: transparent;
}

.quick-card.is-purple .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #b07aff 0%, #8f55ff 100%);
}

.quick-card.is-indigo {
  background: transparent;
}

.quick-card.is-indigo .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #8794ff 0%, #5b67ff 100%);
}

.quick-card.is-slate {
  background: transparent;
}

.quick-card.is-slate .quick-card__icon {
  color: #fff;
  background: linear-gradient(135deg, #8e9bb0 0%, #64748b 100%);
}

.solution-card {
  min-height: 382px;
}

.solution-card__body {
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 16px;
  padding: 18px 20px 18px;
}

.solution-ring {
  position: relative;
  width: 238px;
  height: 238px;
  border-radius: 50%;
}

.solution-ring__chart {
  position: absolute;
  inset: 0;
}

.solution-ring__inner {
  position: absolute;
  inset: 48px;
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

.solution-ring__inner strong {
  color: #1f1f1f;
  font-size: 32px;
  line-height: 1;
  font-weight: 700;
}

.solution-ring__inner span {
  margin-top: 6px;
  color: #6f7f95;
  font-size: 14px;
  line-height: 1;
}

.solution-legend {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px 12px;
}

.solution-legend .legend-dot {
  width: 8px;
  height: 8px;
  border-radius: 2px;
}

.solution-legend__item {
  display: grid;
  grid-template-columns: 10px minmax(0, 1fr) auto;
  align-items: center;
  gap: 6px;
  color: var(--text-main);
  font-size: 14px;
}

.solution-legend__item strong {
  color: #5f6f86;
  font-weight: 600;
}

@media (max-width: 1600px) {
  .dashboard-grid {
    grid-template-columns: minmax(0, 1fr) 320px;
  }

  .hero-panel__body {
    grid-template-columns: minmax(0, 1fr) minmax(360px, 0.86fr);
    gap: 22px;
  }

  .hero-copy h1 {
    font-size: 22px;
  }

  .hero-copy p {
    font-size: 14px;
    line-height: 1.8;
  }

  .hero-flow {
    transform: scale(0.94);
    transform-origin: right center;
  }

  .overview-grid {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }

  .welcome-card {
    padding: 28px 32px 0 24px;
  }

  .welcome-card .info-btns .info-btn-dft {
    width: 92px;
    margin-left: 12px;
  }
}

@media (max-width: 1360px) {
  .dashboard-grid {
    grid-template-columns: minmax(0, 1fr) 300px;
  }

  .hero-panel__body {
    grid-template-columns: minmax(0, 1fr);
  }

  .hero-copy {
    max-width: 100%;
  }

  .hero-flow {
    width: 520px;
    min-height: 286px;
    margin: 0 auto;
    transform: scale(0.94);
    transform-origin: center top;
  }
}

@media (max-width: 1360px) and (min-width: 1201px) {
  .overview-grid {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    gap: 14px;
  }

  .overview-card {
    flex: 0 0 calc((100% - 28px) / 3);
    max-width: calc((100% - 28px) / 3);
  }

  .dashboard-side {
    align-self: stretch;
    height: 100%;
    grid-template-rows: 150px minmax(0, 1fr) minmax(0, 1fr) auto;
  }

  .notice-list,
  .quick-grid {
    flex: 1;
    align-content: space-evenly;
  }
}

@media (max-width: 1200px) {
  .hero-panel {
    padding: 18px 18px 12px;
  }

  .dashboard-grid {
    grid-template-columns: minmax(0, 1fr);
  }

  .hero-panel__head,
  .card-head {
    flex-wrap: wrap;
  }

  .welcome-card {
    height: auto;
    padding: 24px;
  }

  .welcome-card .info-main {
    flex-wrap: wrap;
  }

  .welcome-card .info-con {
    min-width: 0;
    height: auto;
  }

  .welcome-card .info-btns {
    position: static;
    display: flex;
    width: 100%;
    margin-top: 18px;
  }

  .welcome-card .info-btns .info-btn-dft {
    width: auto;
    min-width: 100px;
    margin-left: 0;
    margin-right: 12px;
  }

  .card-tools {
    width: 100%;
    justify-content: space-between;
  }

  .overview-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .analytics-row,
  .dashboard-side {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .solution-card {
    grid-column: 1 / -1;
    min-height: 0;
  }

  .app-summary {
    grid-template-columns: 1fr;
  }

  .asset-card > .line-chart--wide {
    height: 260px;
  }
}

@media (max-width: 992px) {
  .hero-panel {
    margin-top: 12px;
  }

  .hero-panel__head {
    gap: 12px;
  }

  .hero-brand {
    flex-wrap: wrap;
  }

  .hero-brand__text {
    font-size: 16px;
  }

  .hero-copy h1 {
    font-size: 20px;
  }

  .hero-copy p {
    line-height: 1.75;
  }

  .hero-tags {
    gap: 12px 16px;
    margin-top: 18px;
  }

  .hero-flow {
    min-height: 248px;
    transform: scale(0.88);
  }

  .welcome-card {
    padding: 18px;
  }

  .welcome-avatar {
    width: 56px;
    height: 56px;
    margin-right: 16px;
  }

  .welcome-card .info-con-name {
    font-size: 17px;
    line-height: 25px;
  }

  .welcome-card .info-con-desc {
    font-size: 12px;
    line-height: 20px;
  }

  .overview-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .overview-grid > .overview-card:last-child:nth-child(odd) {
    grid-column: 1 / -1;
  }

  .dashboard-side {
    grid-template-columns: 1fr;
  }

  .solution-card {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .hero-panel {
    padding: 16px;
  }

  .hero-panel__head,
  .hero-brand {
    flex-direction: column;
    align-items: flex-start;
  }

  .analytics-row .card-head {
    flex-wrap: nowrap;
  }

  .analytics-row .card-tools {
    flex: 0 1 auto;
    width: fit-content;
    min-width: 0;
    max-width: 100%;
    margin-left: auto;
    justify-content: flex-end;
    overflow-x: auto;
    overflow-y: hidden;
  }

  .analytics-row .tab-switch {
    flex: 0 0 auto;
    width: max-content;
    min-width: max-content;
  }

  .analytics-row .tab-switch button {
    flex: 0 0 auto;
  }

  .analytics-row .link-button {
    flex-shrink: 0;
  }

  .asset-card > .card-head {
    flex-wrap: nowrap;
  }

  .asset-card .legend-row.is-right {
    flex: 1 1 auto;
    width: auto;
    min-width: 0;
    margin-left: auto;
    justify-content: flex-end;
    flex-wrap: nowrap;
    overflow-x: auto;
    overflow-y: hidden;
    padding: 0;
  }

  .asset-card .legend-item {
    flex-shrink: 0;
  }

  .welcome-card {
    padding: 18px 16px;
  }

  .welcome-card .info-main {
    align-items: flex-start;
  }

  .welcome-card .info-btns {
    gap: 10px;
  }

  .welcome-card .info-btns .info-btn-dft {
    min-width: 112px;
    margin-right: 0;
  }

  .overview-grid,
  .analytics-row,
  .app-summary,
  .solution-legend {
    grid-template-columns: 1fr;
  }

  .overview-grid > .overview-card:last-child {
    grid-column: auto;
  }

  .legend-row {
    flex-wrap: wrap;
    gap: 10px;
    padding: 0 16px;
  }

  .legend-row.is-right {
    width: 100%;
  }

  .bot-chart,
  .line-chart {
    margin: 8px 16px 16px;
  }

  .asset-card > .line-chart--wide {
    height: 220px;
    margin: 10px 16px 18px;
  }

  .app-summary {
    margin: 10px 16px 0;
  }

  .notice-item {
    padding: 6px 16px;
  }

  .quick-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    padding: 14px 12px 16px;
  }

  .solution-card__body {
    padding: 16px;
    gap: 14px;
  }

  .solution-ring {
    width: 204px;
    height: 204px;
  }

  .solution-ring__inner {
    inset: 40px;
  }
}

@media (max-width: 560px) {
  .hero-brand__badge {
    min-width: 0;
    padding: 0 12px;
  }

  .hero-brand__text {
    font-size: 14px;
  }

  .hero-copy h1 {
    font-size: 18px;
    line-height: 1.5;
  }

  .hero-copy p {
    font-size: 13px;
    line-height: 1.6;
  }

  .hero-flow {
    min-height: 214px;
    transform: scale(0.76);
  }

  .welcome-card .info-main {
    flex-direction: column;
  }

  .welcome-avatar {
    margin-right: 0;
    margin-bottom: 12px;
  }

  .welcome-card .info-btns {
    flex-direction: column;
    align-items: stretch;
  }

  .welcome-card .info-btns .info-btn-dft {
    width: 100%;
  }

  .notice-item {
    grid-template-columns: 12px minmax(0, 1fr);
  }

  .notice-item__time {
    grid-column: 2;
    width: fit-content;
    padding: 0 6px;
    margin-left: 0;
  }

  .solution-legend {
    grid-template-columns: 1fr;
  }
}
:deep(aside) {
  padding: 0;
}
:deep(.custom-vertical-divider) {
  border-left-color: #888888 !important;
  border-left-width: 1px !important;
  height: 16px !important;
}
</style>
