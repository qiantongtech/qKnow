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
  <div class="overview-page">
    <div class="overview-top">
      <section
        class="overview-hero"
        :style="{ backgroundImage: `url(${bannerImage})` }"
      >
        <div class="hero-copy">
          <h1>应用中心</h1>
          <h2>汇聚行业解决方案，赋能业务智能升级</h2>
          <p>
            支持一次性上传多个查询条件并行处理，汇总输出结果。大幅提升效率，适用于多项目数据对比或大规模文献调研。
          </p>
        </div>
      </section>

      <aside class="quick-panel">
        <div class="panel-title">
          <span></span>
          快捷入口
        </div>
        <div class="quick-grid">
          <button
            v-for="item in quickEntries"
            :key="item.label"
            class="quick-entry"
            type="button"
            @click="goToPage(item.path)"
          >
            <img :src="item.icon" alt="" />
            <span>{{ item.label }}</span>
          </button>
        </div>
        <div class="stat-grid">
          <button
            v-for="item in quickStats"
            :key="item.label"
            class="stat-item"
            type="button"
            @click="goToPage(item.path)"
          >
            <strong>{{ item.value }}</strong>
            <span>{{ item.label }}</span>
          </button>
        </div>
      </aside>
    </div>

    <!-- <section class="overview-section">
      <div class="section-head">
        <div class="panel-title">
          <span></span>
          精选解决方案
        </div>
        <button
          class="section-more"
          type="button"
          @click="goToPage('/kac/solution')"
        >
          查看更多
          <el-icon><ArrowRight /></el-icon>
        </button>
      </div>

      <div class="section-content" v-loading="solutionLoading">
        <SolutionCard
          v-if="solutionList.length"
          :data="solutionList"
          source="solution"
          variant="overview"
        />
        <el-empty v-else description="暂无解决方案" />
      </div>
    </section> -->

    <section class="overview-section">
      <div class="section-head">
        <div class="panel-title">
          <span></span>
          热门应用推荐
        </div>
        <button
          class="section-more"
          type="button"
          @click="goToPage('/kac/horizontal')"
        >
          查看更多
          <el-icon><ArrowRight /></el-icon>
        </button>
      </div>

      <div class="section-content" v-loading="applyLoading">
        <Card
          v-if="applyList.length"
          :data="applyList"
          source="horizontal"
          variant="overview"
        />
        <el-empty v-else description="暂无应用推荐" />
      </div>
    </section>
  </div>
</template>

<script setup name="Overview">
import { computed, onMounted, reactive, ref } from "vue";
import { ArrowRight } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import Card from "@/views/kac/horizontal/components/card.vue";
import SolutionCard from "@/views/kac/mySolution/components/solutionCard.vue";
// import { listApply } from "@/api/kac/apply/apply.js";
// import { listSolution } from "@/api/kac/solution/solution";
import useUserStore from "@/store/system/user.js";
import bannerImage from "@/assets/kac/overview/banner.png";
import quickSolutionIcon from "@/assets/kac/overview/quick-solution.png";
import quickHorizontalIcon from "@/assets/kac/overview/quick-horizontal.png";
import quickVerticalIcon from "@/assets/kac/overview/quick-vertical.png";
import quickMySolutionIcon from "@/assets/kac/overview/quick-my-solution.png";
import quickMyAppIcon from "@/assets/kac/overview/quick-my-app.png";

const router = useRouter();
const userStore = useUserStore();

const solutionLoading = ref(false);
const applyLoading = ref(false);
const solutionList = ref([
  {
    id: 7,
    workspaceId: 1001,
    name: "变电站预测性维护方案",
    description:
      "基于变电站主设备的在线监测数据（如油色谱、局放、红外热像），构建设备全生命周期健康画像。系统自动评估设备健康指数，预测绝缘老化与机械故障风险，将传统的定期检修转变为基于状态的预测性维护，降低运维成本。",
    coverImage: "/2026/04/24/69ead145e4b077552f280c97.jpg",
    tags: '[{"name":"能源电力"}, {"name":"预测性维护"}]',
    status: null,
    mySolutionFlag: false,
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-22 09:05:17",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-22 09:05:17",
    remark: null,
    heatValue: "6357",
  },
  {
    id: 6,
    workspaceId: 1001,
    name: "大坝安全监测数据分析",
    description:
      "汇聚大坝变形、渗流、应力应变等海量监测数据，采用统计模型与机器学习混合算法剔除环境干扰（如水位、温度影响）。精准识别大坝结构性态的异常变化趋势，评估大坝长期服役安全性，为除险加固提供科学依据。",
    coverImage: "/2026/04/24/69ead16ee4b077552f280c99.jpg",
    tags: '[{"name":"水利"}, {"name":"安全监测"}]',
    status: null,
    mySolutionFlag: false,
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-22 09:04:58",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-22 09:04:58",
    remark: null,
    heatValue: "9303",
  },
  {
    id: 5,
    workspaceId: 1001,
    name: "源网荷储AI协同调度方案",
    description:
      "针对新型电力系统，利用人工智能技术协调电源、电网、负荷与储能侧的实时平衡。通过超短期新能源发电预测与负荷预测，智能调度储能充放电策略与柔性负荷响应，平抑新能源波动，提升电网消纳能力与运行经济性。",
    coverImage: "/2026/04/24/69ead0f3e4b077552f280c96.png",
    tags: '[{"name":"能源电力"}, {"name":"协同调度"}]',
    status: null,
    mySolutionFlag: false,
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-22 09:04:44",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-22 09:04:44",
    remark: null,
    heatValue: "7654",
  },
  {
    id: 4,
    workspaceId: 1001,
    name: "水库综合治理解决方案",
    description:
      "构建集雨水情监测、防洪调度、水质保护与库区安防于一体的综合管理平台。利用水文模型预测入库流量，结合多目标优化算法制定蓄泄方案，同时通过视频AI识别非法入侵与水面漂浮物，实现水库运行的数字化与生态化管理。",
    coverImage: "/2026/04/24/69ead156e4b077552f280c98.jpg",
    tags: '[{"name":"水利"}, {"name":"综合治理"}]',
    status: null,
    mySolutionFlag: false,
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-22 09:04:02",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-22 09:04:02",
    remark: null,
    heatValue: "1381",
  },
]);
const applyList = ref([
  {
    id: 20,
    workspaceId: 1001,
    pluginId: null,
    name: "文章编写",
    category: 0,
    description:
      "文章编写插件是一类旨在辅助用户更高效、更高质量地完成文本创作任务的软件工具或扩展程序。",
    status: 0,
    source: null,
    tags: '[{"name":"写作"},{"name":"文章"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01a88de4b0d389f4f52e8e.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-23 19:54:44",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-23 19:54:44",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 8,
    workspaceId: 1001,
    pluginId: null,
    name: "批量检索",
    category: 0,
    description:
      "支持一次性上传多个查询条件并行处理，汇总输出结果。大幅提升效率，适用于多项目数据对比或大规模文献调研。",
    status: 0,
    source: null,
    tags: '[{"name":"效率"}, {"name":"工具"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01a9a0e4b0d389f4f52e90.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:41:23",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:41:23",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 7,
    workspaceId: 1001,
    pluginId: null,
    name: "精确检索",
    category: 0,
    description: "严格字符匹配，精准查找代码、条款或参数，无模糊干扰。",
    status: 0,
    source: null,
    tags: '[{"name":"搜索"}, {"name":"工具"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01a9d8e4b0d389f4f52e91.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:41:10",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:41:10",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 6,
    workspaceId: 1001,
    pluginId: null,
    name: "实体关系检索",
    category: 0,
    description: "智能识别实体与深层关系，助力知识图谱与情报分析。\r\n\r\n",
    status: 0,
    source: null,
    tags: '[{"name":"分析"}, {"name":"数据"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01a9e9e4b0d389f4f52e92.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:41:07",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:41:07",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 5,
    workspaceId: 1001,
    pluginId: null,
    name: "语义检索",
    category: 0,
    description:
      "利用深度学习理解查询意图与上下文，突破关键词匹配限制。即使词汇不完全一致，也能通过语义关联精准定位内容。",
    status: 0,
    source: null,
    tags: '[{"name":"搜索"}, {"name":"AI"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01a9f9e4b0d389f4f52e93.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:40:54",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:40:54",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 4,
    workspaceId: 1001,
    pluginId: null,
    name: "知识问答",
    category: 0,
    description:
      "基于海量数据理解并回答各类事实性或解释性问题，提供准确简洁的答案，充当智能百科全书，满足即时信息获取需求。",
    status: 0,
    source: null,
    tags: '[{"name":"问答"}, {"name":"知识"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01aa0ae4b0d389f4f52e94.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:39:46",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:39:46",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 3,
    workspaceId: 1001,
    pluginId: null,
    name: "模板报告生成",
    category: 0,
    description:
      "提供多场景标准模板，引导填充并自动排版，确保企业级文档专业规范。",
    status: 0,
    source: null,
    tags: '[{"name":"模板"}, {"name":"文档"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01aa17e4b0d389f4f52e95.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:37:08",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:37:08",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
  {
    id: 2,
    workspaceId: 1001,
    pluginId: null,
    name: "日报/周报/月报文章编写",
    category: 0,
    description:
      "简化周期性工作汇报撰写。输入关键事项，系统自动扩展为结构完整、语气专业的报告，智能识别成果与计划，节省写作时间。",
    status: 0,
    source: null,
    tags: '[{"name":"写作"}, {"name":"办公"}]',
    myApplyFlag: false,
    useScene: null,
    useCount: null,
    icon: "/2026/05/11/6a01a8f6e4b0d389f4f52e8f.png",
    validFlag: true,
    delFlag: false,
    createBy: null,
    creatorId: null,
    createTime: "2026-04-21 18:36:52",
    updateBy: null,
    updaterId: null,
    updateTime: "2026-04-21 18:36:52",
    remark: null,
    kacApplyKnowledgeList: null,
    kacApplyGraphList: null,
    kacApplyBotList: null,
  },
]);
const overviewStats = reactive({
  solutionTotal: 7,
  applyTotal: 10,
  mySolutionTotal: 2,
  myApplyTotal: 2,
});
const quickEntries = [
  // {
  //   label: "解决方案",
  //   icon: quickSolutionIcon,
  //   path: "/kac/solution",
  // },
  {
    label: "通用应用",
    icon: quickHorizontalIcon,
    path: "/kac/horizontal",
  },
  {
    label: "行业应用",
    icon: quickVerticalIcon,
    path: "/kac/vertical",
  },
  // {
  //   label: "我的方案",
  //   icon: quickMySolutionIcon,
  //   path: "/kac/mySolution",
  // },
  {
    label: "我的应用",
    icon: quickMyAppIcon,
    path: "/kac/myApp",
  },
];
const quickStats = computed(() => [
  // {
  //   label: "解决方案",
  //   value: overviewStats.solutionTotal,
  //   path: "/kac/solution",
  // },
  {
    label: "所有应用",
    value: overviewStats.applyTotal,
    path: "/kac/horizontal",
  },
  // {
  //   label: "我的方案",
  //   value: overviewStats.mySolutionTotal,
  //   path: "/kac/mySolution",
  // },
  {
    label: "我的应用",
    value: overviewStats.myApplyTotal,
    path: "/kac/myApp",
  },
]);

const solutionQueryParams = reactive({
  pageNum: 1,
  pageSize: 4,
  name: null,
  mySolutionFlag: 0,
  orderByColumn: "createTime",
  isAsc: "desc",
});

const applyQueryParams = reactive({
  pageNum: 1,
  pageSize: 8,
  workspaceId: null,
  pluginId: null,
  name: null,
  category: 0,
  description: null,
  status: null,
  source: null,
  tags: null,
  useScene: null,
  useCount: null,
  createTime: null,
  myApplyFlag: 0,
  orderByColumn: "createTime",
  isAsc: "desc",
});

async function getSolutionList() {
  solutionLoading.value = true;
  try {
    const response = await listSolution(solutionQueryParams);
    solutionList.value = response.data.rows || [];
    overviewStats.solutionTotal = response.data.total || 0;
  } catch {
    solutionList.value = [];
  } finally {
    solutionLoading.value = false;
  }
}

async function getApplyList() {
  applyLoading.value = true;
  try {
    const response = await listApply(applyQueryParams);
    applyList.value = response.data.rows || [];
    overviewStats.applyTotal = response.data.total || 0;
  } catch {
    applyList.value = [];
  } finally {
    applyLoading.value = false;
  }
}

async function getOverviewStats() {
  const [mySolutionRes, myApplyRes, allApplyRes] = await Promise.allSettled([
    listSolution({
      ...solutionQueryParams,
      pageNum: 1,
      pageSize: 1,
      userId: userStore.id,
      mySolutionFlag: 1,
    }),
    listApply({
      ...applyQueryParams,
      pageNum: 1,
      pageSize: 1,
      category: null,
      userId: userStore.id,
      myApplyFlag: 1,
    }),
    listApply({
      ...applyQueryParams,
      pageNum: 1,
      pageSize: 1,
      category: null,
      myApplyFlag: 0,
    }),
  ]);

  if (mySolutionRes.status === "fulfilled") {
    overviewStats.mySolutionTotal = mySolutionRes.value.data?.total || 0;
  }
  if (myApplyRes.status === "fulfilled") {
    overviewStats.myApplyTotal = myApplyRes.value.data?.total || 0;
  }
  if (allApplyRes.status === "fulfilled") {
    overviewStats.applyTotal =
      allApplyRes.value.data?.total || overviewStats.applyTotal;
  }
}

function goToPage(path) {
  if (path === "/kac/solution" || path === "/kac/mySolution") {
    ElMessage({
      message: "功能正在开发中",
      type: "warning",
    });
    return;
  }
  router.push(path);
}

// onMounted(() => {
//   getSolutionList();
//   getApplyList();
//   getOverviewStats();
// });
</script>

<style scoped lang="scss">
.overview-page {
  padding: 16px;
  background: #f0f2f5;
  color: #1f2937;
  display: flex;
  flex-direction: column;
  gap: 0;
}

.overview-top {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 500px;
  gap: 16px;
}

.overview-hero,
.quick-panel,
.overview-section {
  background: #fff;
}

.overview-hero {
  height: 234px;
  padding: 46px 72px;
  background-color: #eaf3ff;
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.hero-copy {
  max-width: 520px;

  h1,
  h2,
  p {
    margin: 0;
  }

  h1 {
    color: #1f2937;
    font-size: 32px;
    font-weight: 600;
    line-height: 44px;
  }

  h2 {
    margin-top: 14px;
    color: #1f2937;
    font-size: 18px;
    font-weight: 400;
    line-height: 28px;
  }

  p {
    margin-top: 14px;
    color: #4b5563;
    font-size: 14px;
    line-height: 26px;
  }
}

.quick-panel {
  height: 234px;
  padding: 20px;
}

.panel-title {
  display: flex;
  align-items: center;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  line-height: 24px;

  span {
    width: 6px;
    height: 16px;
    margin-right: 10px;
    border-radius: 3px;
    background: #2666fb;
  }
}

.quick-grid {
  display: grid;
  // grid-template-columns: repeat(5, minmax(0, 1fr));
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
  margin-top: 18px;
}

.quick-entry,
.stat-item,
.section-more {
  border: 0;
  background: transparent;
  cursor: pointer;
}

.quick-entry {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 0;
  padding: 0;
  color: #1f2937;
  font-size: 14px;
  line-height: 20px;

  img {
    width: 52px;
    height: 52px;
    border-radius: 8px;
    object-fit: cover;
  }

  span {
    margin-top: 8px;
    white-space: nowrap;
  }
}

.stat-grid {
  display: grid;
  // grid-template-columns: repeat(4, minmax(0, 1fr));
  grid-template-columns: repeat(2, minmax(0, 1fr));
  height: 60px;
  margin-top: 16px;
  border: 1px solid #e5ebf5;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.02),
    rgba(38, 102, 251, 0.04)
  );
}

.stat-item {
  min-width: 0;
  height: 58px;
  padding: 7px 6px;
  border-right: 1px solid #e5ebf5;
  color: #6b7280;

  &:last-child {
    border-right: 0;
  }

  strong,
  span {
    display: block;
  }

  strong {
    color: #1f6eea;
    font-size: 16px;
    font-weight: 600;
    line-height: 22px;
  }

  span {
    margin-top: 4px;
    font-size: 14px;
    line-height: 18px;
  }
}

.overview-section {
  margin-top: 0;
  padding: 20px;
}

.overview-top + .overview-section,
.overview-section + .overview-section {
  margin-top: 16px;
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-more {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 0;
  color: #1f6eea;
  font-size: 14px;
  line-height: 22px;
}

.section-content {
  min-height: 120px;
}

.section-content :deep(.el-empty) {
  padding: 48px 0 24px;
}

@media (max-width: 1440px) {
  .overview-top {
    grid-template-columns: minmax(0, 1fr) 500px;
  }

  .overview-hero {
    padding: 42px 54px;
  }
}

@media (max-width: 1200px) {
  .overview-top {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 767px) {
  .overview-hero,
  .quick-panel,
  .overview-section {
    padding: 16px;
  }

  .overview-page {
    padding: 10px;
  }

  .overview-hero {
    min-height: 220px;
  }

  .quick-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .stat-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .stat-item:nth-child(2) {
    border-right: 0;
  }
}
aside {
  margin-bottom: 0;
}
</style>
