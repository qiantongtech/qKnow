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
  <el-table stripe height="58vh"
            v-loading="loading"
            :data="botLogList"
            @sort-change="handleSortChange">
    <el-table-column label="编号" align="center" width="55">
      <template #default="scope">
        <span>{{ scope.row.id }}</span>
      </template>
    </el-table-column>
    <el-table-column
        label="开始时间"
        align="center"
        sortable="custom"
        :sort-orders="sortOrders"
        prop="createTime">
      <template #default="scope">
        {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
      </template>
    </el-table-column>
    <el-table-column label="执行状态" align="left" prop="status" width="200" sortable="custom"
                     :sort-orders="sortOrders">
      <template #default="scope">
        <div>
          <dict-tag :options="bot_run_status" :value="scope.row.status"/>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="运行时间" align="center" prop="runtime" width="250" sortable="custom"
                     :sort-orders="sortOrders">
      <template #default="scope">
        {{ scope.row.runtime + " ms" || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="创建人" align="center" prop="createBy" width="250" sortable="custom"
                     :sort-orders="sortOrders">
      <template #default="scope">
        <span>{{ scope.row.createBy || '-' }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
      <template #default="scope">
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)">详情</el-button>
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/system/images/no_data/noData.png" alt=""/>
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="queryBotLogPage"
  />

  <el-dialog
      :title="botLogDialogTitle"
      v-model="botLogDialogOpen"
      width="1000px"
      :append-to="$refs['app-container']"
      draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ botLogDialogTitle }}
      </span>
    </template>
    <el-form
        ref="botRef"
        :model="logDetail"
        label-width="80px"
        @submit.prevent
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="开始时间" prop="createTime">
            <el-input v-model="logDetail.createTime" placeholder="请输入 开始时间" readonly/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="执行状态" prop="status">
            <el-select
                v-model="logDetail.status"
                placeholder="请选择执行状态"
                class="el-form-input-width"
                style="width: 100%"
                disabled
            >
              <el-option
                  v-for="dict in bot_run_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="运行时间" prop="runtime">
            <el-input v-model="logDetail.runtimeStr" placeholder="请输入 Bot 名称" readonly/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="输入参数" prop="input">
            <el-input
                v-model="logDetail.input"
                type="textarea"
                readonly
                placeholder="请输入输入参数"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="输出结果" prop="output">
            <el-input
                v-model="logDetail.output"
                type="textarea"
                placeholder="请输入输出结果"
                readonly
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <div class="check-status-wrapper">
        </div>
        <div class="button-wrapper">
          <el-button size="mini" @click="botLogDialogOpen = false">关 闭</el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="botLogTable">
import {botLogPage} from "@/api/kb/bot/botLog.js";

const {proxy} = getCurrentInstance();
const {bot_run_status} = proxy.useDict("bot_run_status");

const botLogList = ref([]);
const botLogDialogTitle = ref('');
const botLogDialogOpen = ref(false);
const sortOrders = ['descending', 'ascending'];
const logDetail = ref(null);

const loading = ref(true);
const total = ref(0);
const title = ref("");
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  workspaceId: null,
  botId: null,
});

const props = defineProps({
  botId: {
    type: Number,
    required: true
  },
})

// 监听父组件传递的Bot列表变化
watch(
    () => props.botId,
    (newVal) => {
      if (newVal) {
        queryBotLogPage();
      }
    },
    {immediate: true}
);

function queryBotLogPage() {
  if (!props.botId) return;
  loading.value = true;
  queryParams.value.botId = props.botId;
  botLogPage(queryParams.value).then(response => {
    botLogList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 排序触发事件 */
function handleSortChange(column) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  queryBotLogPage();
}

function handleDetail(row) {
  botLogDialogTitle.value = "执行日志详情";
  botLogDialogOpen.value = true;
  logDetail.value = row;
  logDetail.value.status = row.status + ""
  logDetail.value.runtimeStr = row.runtime + " ms"
}
</script>
