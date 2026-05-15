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
<!--  <div class="justify-between mb15">-->
<!--    <el-row :gutter="15" class="btn-style">-->
<!--    </el-row>-->
<!--    <div class="justify-end top-right-btn">-->
<!--      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>-->
<!--    </div>-->
<!--  </div>-->
  <el-table stripe height="58vh" v-loading="loading" :data="botList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column v-if="getColumnVisibility(1)" label="编号" align="center" width="55">
      <template #default="scope">
        <span>{{ scope.$index + 1 }}</span>
      </template>
    </el-table-column>
    <el-table-column v-if="getColumnVisibility(2)" label="名称" align="center" prop="botId" width="250">
      <template #default="scope">
        {{ getBotInfo(scope.row.botId, 'name') }}
      </template>
    </el-table-column>
    <el-table-column v-if="getColumnVisibility(3)" label="挂载说明" align="left" prop="description" width="350">
      <template #default="scope">
        {{ scope.row.description || '-' }}
      </template>
    </el-table-column>
    <el-table-column v-if="getColumnVisibility(5)" label="类型" align="center" prop="botId">
      <template #default="scope">
        <div>
          <dict-tag :options="kg_bot_type" :value="getBotInfo(scope.row.botId, 'type')"/>
        </div>
      </template>
    </el-table-column>
    <el-table-column v-if="getColumnVisibility(6)" label="是否内置" align="center" prop="botId">
      <template #default="scope">
        <div>
          <dict-tag :options="sys_is_or_not" :value="getBotInfo(scope.row.botId, 'builtinFlag')"/>
        </div>
      </template>
    </el-table-column>
    <el-table-column v-if="getColumnVisibility(7)" label="创建人" align="center" prop="createBy"/>
    <el-table-column v-if="getColumnVisibility(8)" label="创建时间" align="center" prop="createTime" sortable="custom" :sort-orders="['descending', 'ascending']">
      <template #default="scope">
        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
      </template>
    </el-table-column>
    <el-table-column v-if="getColumnVisibility(8)" label="最后更新时间" align="center" prop="updateTime" sortable="custom" :sort-orders="['descending', 'ascending']">
      <template #default="scope">
        <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="props.source === 'myApp'">
      <template #default="scope">
        <el-button link type="primary" icon="Switch" @click="handleUpdate(scope.row)"
          >替换</el-button>
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/system/images/no_data/noData.png" alt="" />
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
  />

  <!-- 选择BOT对话框 -->
  <el-dialog
    :title="botDialogTitle"
    v-model="botDialogOpen"
    width="1000px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ botDialogTitle }}
      </span>
    </template>
    <!--Bot数据-->
    <el-form
      class="btn-style"
      :model="queryParamsBot"
      ref="queryBotRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="Bot 名称" prop="name">
        <el-input
          class="el-form-input-width"
          v-model="queryParamsBot.name"
          placeholder="请输入 Bot 名称"
          clearable
          @keyup.enter="handleQueryBot"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select
            v-model="queryParamsBot.type"
            placeholder="请选择类型"
            clearable
            class="el-form-input-width"
        >
          <el-option
              v-for="dict in kg_bot_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          plain
          type="primary"
          @click="handleQueryBot"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button
          @click="resetQueryBot"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      stripe
      ref="botTableRef"
      v-loading="botLoading"
      :data="botSelectList"
      row-key="id"
      :row-class-name="getTableRowClassName"
      highlight-current-row
      @row-click="handleBotRowClick"
      style="width: 1000px"
    >
      <el-table-column
        label="Bot 名称"
        prop="name"
        align="center"
        :show-overflow-tooltip="true"
        width="200"
      />
      <el-table-column
        label="描述"
        prop="description"
        align="left"
        :show-overflow-tooltip="true"
        width="280"
      />
      <el-table-column
          label="类型"
          prop="type"
          align="center"
          :show-overflow-tooltip="true"
          width="100"
      >
        <template #default="scope">
          <div>
            <dict-tag :options="kg_bot_type" :value="scope.row.type"/>
          </div>
        </template>
      </el-table-column>
      <el-table-column
          label="是否内置"
          align="center"
          prop="type"
          width="100"
      >
        <template #default="scope">
          <div>
            <dict-tag
                :options="sys_is_or_not"
                :value="scope.row.builtinFlag"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column
          label="创建人"
          align="center"
          prop="createBy"
          width="100"
      >
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{
            scope.row.createTime
              ? parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
              : "-"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{
              scope.row.updateTime
                  ? parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}")
                  : "-"
            }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="botTotal > 0"
      :total="botTotal"
      v-model:page="queryParamsBot.pageNum"
      v-model:limit="queryParamsBot.pageSize"
      @pagination="getBotSelectList"
    />
    <template #footer>
      <div class="dialog-footer">
        <div class="check-status-wrapper">
          <div v-if="checking" class="check-status">
            <el-icon class="is-loading"><loading /></el-icon>
            <span style="color:#333333">正在进行Bot适配性检测...</span>
          </div>
          <div v-else-if="checkResult !== null" class="check-status">
            <el-icon v-if="checkResult.passed" style="color: #14a339">
              <circle-check />
            </el-icon>
            <el-icon v-else style="color: #f56c6c">
              <circle-close />
            </el-icon>
            <span :style="{ color: checkResult.passed ? '#14a339' : '#ec544d' }">
              {{ checkResult.passed ? '适配性检测通过' : '适配性检测未通过，请检查Bot配置。' }}
            </span>
          </div>
        </div>
        <div class="button-wrapper">
          <el-button size="mini" @click="handleCancel" :disabled="checking">取 消</el-button>
          <el-button
            type="primary"
            size="mini"
            :loading="checking"
            @click="submitBotForm"
          >
            确 定
          </el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="Method">
import { listKacBot, getKacBot, updateKacBot, addKacBot } from "@/api/kac/applyBot/applyBot.js";
import { listBot } from "@/api/kb/bot/bot.js";
import { Loading, CircleCheck, CircleClose } from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance();

const {kg_bot_type, sys_is_or_not} = proxy.useDict(
    "kg_bot_type",
    "sys_is_or_not"
);

const botList = ref([]);
const botAllList = ref([]);
const botName = ref('');

const botDialogTitle = ref('');
const botDialogOpen = ref(false);
const botLoading = ref(true);
const botSelectList = ref([]);
const botTotal = ref(0);
const selectedBot = ref(null);
// 【修复点 1】：组件 ref 需要初始化为 null，而不是 []
const botTableRef = ref(null);
const currentEditRow = ref(null);
const checking = ref(false);
const checkResult = ref(null);

const props = defineProps({
  applyId: {
    type: Number,
    required: true
  },
  botList: {
    type: Array,
    default: () => []
  },
  source: {
    type: String,
    default: 'horizontal',
    validator: (value) => ['horizontal', 'vertical', 'myApp'].includes(value)
  }
})

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "名称", visible: true },
  { key: 3, label: "挂载说明", visible: true },
  { key: 5, label: "类型", visible: true },
  { key: 6, label: "是否内置", visible: true },
  { key: 7, label: "创建人", visible: true },
  { key: 8, label: "创建时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const defaultSort = ref({ prop: "createTime", order: "desc" });
const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    applyId: null,
    botId: null,
    trenchKey: null,
    createTime: null,
  },
  queryParamsBot: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    type: null,
    builtinFlag: null
  },
  rules: {
  }
});

const { queryParams, queryParamsBot, form, rules } = toRefs(data);

const getBotInfo = (id, field) => {
  if (!id) return '-';
  const target = botAllList.value.find(item => item.id == id);
  console.log(target, 'target');
  return target && target[field] != null && target[field] !== '' ? target[field] : '-';
};

/** 查询应用关联Bot列表 */
function getList() {
  loading.value = true;
  queryParams.value.applyId = props.applyId;
  listKacBot(queryParams.value).then(response => {
    botList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

// 监听父组件传递的Bot列表变化
watch(
  () => props.botList,
  (newList) => {
    if (newList && newList.length > 0) {
      botAllList.value = newList;
      // 数据加载完成，结束loading
      loading.value = false;
    }
  },
  { immediate: true, deep: true }
)

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
      id: null,
      workspaceId: null,
      appId: null,
      botId: null,
      trenchKey: null,
      validFlag: null,
      delFlag: null,
      createBy: null,
      creatorId: null,
      createTime: null,
      updateBy: null,
      updaterId: null,
      updateTime: null,
      remark: null,
      description: null
  };
  proxy.resetForm("botRef");
}

/** 修改按钮操作 */
function handleUpdate(row) {
  currentEditRow.value = { ...row };
  reset();
  const _id = row.id || ids.value
  getKacBot(_id).then(response => {
    form.value = response.data;
    botName.value = getBotInfo(form.value.botId, 'name');
    botDialogTitle.value = "替换关联BOT";
    botLoading.value = true;
    queryParamsBot.value.pageNum = 1;
    queryParamsBot.value.name = null;
    selectedBot.value = form.value.botId ? { id: form.value.botId } : null;
    checking.value = false;
    checkResult.value = null;
    botDialogOpen.value = true;
    getBotSelectList();
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["botRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateKacBot(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addKacBot(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      }
    }
  });
}

function openBotDialog() {
  botDialogOpen.value = true;
  botDialogTitle.value = "选择关联BOT";
  botLoading.value = true;
  queryParamsBot.value.pageNum = 1;
  queryParamsBot.value.name = null;
  selectedBot.value = form.value.botId ? { id: form.value.botId } : null;
  getBotSelectList();
}

function getBotSelectList() {
  botLoading.value = true;
  listBot(queryParamsBot.value).then(response => {
    botLoading.value = false;
    botSelectList.value = response.data.rows;
    botTotal.value = response.data.total;
    nextTick(() => {
      // 【修复点 2】：确保 DOM 更新后通知 Element Plus 设置行高亮
      if (selectedBot.value && botTableRef.value) {
        const data = botSelectList.value.find(item => item.id == selectedBot.value.id);
        if (data) {
          selectedBot.value = data;
          botTableRef.value.setCurrentRow(data);
        } else {
          botTableRef.value.setCurrentRow();
        }
      }
    });
  });
}

function handleBotRowClick(row) {
  if (selectedBot.value && selectedBot.value.id == row.id) {
    selectedBot.value = null;
    // 【修复点 3】：反选时需要清除 el-table 的选中状态
    if (botTableRef.value) {
      botTableRef.value.setCurrentRow();
    }
  } else {
    selectedBot.value = row;
  }
  checking.value = false;
  checkResult.value = null;
}

function handleCancel() {
  botDialogOpen.value = false;
  checking.value = false;
  checkResult.value = null;
}

async function performAdaptabilityCheck() {
  checking.value = true;
  checkResult.value = null;

  await new Promise(resolve => setTimeout(resolve, 2000));

  const passed = Math.random() > 0.3;
  checkResult.value = { passed };
  checking.value = false;

  return passed;
}

async function submitBotForm() {
  if (!selectedBot.value) {
    proxy.$modal.msgWarning("请选择所要替换的BOT");
    return;
  }

  if (!checkResult.value) {
    const passed = await performAdaptabilityCheck();
    if (!passed) {
      return;
    }
  }

  if (!checkResult.value?.passed) {
    proxy.$modal.msgError("适配性检测未通过，请检查Bot配置。");
    return;
  }

  form.value.botId = selectedBot.value.id;
  botName.value = selectedBot.value.name;

  updateKacBot(form.value).then(response => {
    proxy.$modal.msgSuccess("替换成功");
    botDialogOpen.value = false;
    checking.value = false;
    checkResult.value = null;
    getList();
  }).catch(error => {
  });
}

function handleQueryBot() {
  queryParamsBot.value.pageNum = 1;
  getBotSelectList();
}

function resetQueryBot() {
  queryParamsBot.value.pageNum = 1;
  queryParamsBot.value.pageSize = 10;
  queryParamsBot.value.name = null;
  queryParamsBot.value.type = null;
  queryParamsBot.value.builtinFlag = null;
  getBotSelectList();
}

function getTableRowClassName({ row }) {
  if (selectedBot.value && selectedBot.value.id == row.id) {
    return 'selected-row';
  }
  return '';
}

getList();
</script>

<style scoped>
:deep(.el-table .selected-row > td.el-table__cell) {
  background-color: #e6f7ff !important;
}

.dialog-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.check-status-wrapper {
  display: flex;
  align-items: center;
}

.check-status {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
  font-size: 14px;
}

.check-status .el-icon {
  font-size: 18px;
}

.button-wrapper {
  display: flex;
  gap: 8px;
}
</style>
