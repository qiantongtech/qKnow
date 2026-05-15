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
  <el-dialog
      title="导入关联应用"
      v-model="dialogVisible"
      width="1200px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
      @close="handleClose"
  >
    <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
    >
      <el-form-item label="应用名称" prop="name">
        <el-input
            style="width:240px"
            v-model="queryParams.name"
            placeholder="请输入应用名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
        ref="multipletableRef"
        stripe
        height="420px"
        v-loading="loading"
        :data="dataList"
        reserve-selection
        row-key="id"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="应用名称" align="center"  prop="name">
        <template #default="scope">
          {{ scope.row.name || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="left" prop="description" width="300">
        <template #default="scope">
          {{ scope.row.description || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="category">
        <template #default="scope">
          {{ scope.row.category == 1 ? '纵向行业应用' : (scope.row.category == 0 ? '横向通用应用' : '-') }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" width="120">
        <template #default="scope">
          {{ scope.row.createBy || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagecont-bottom">
      <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="SelectMyApp">
import { ref, reactive, toRefs, watch, nextTick } from 'vue';
import { listApply } from "@/api/kac/apply/apply.js";
import useUserStore from "@/store/system/user.js";

const { proxy } = getCurrentInstance();
const userStore = useUserStore();

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  // 已选择的id列表，用于回显
  selectedIds: {
    type: [String, Array],
    default: ''
  }
});

const emit = defineEmits(['update:visible', 'confirm']);

const dialogVisible = ref(false);
const multipletableRef = ref(null);
const dataList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const selectedList = ref([]);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    myApplyFlag: 1,
    userId: userStore.id,
    orderByColumn: 'createTime',
    isAsc: 'desc'
  },
});

const { queryParams } = toRefs(data);

function getList() {
  loading.value = true;
  listApply(queryParams.value).then(response => {
    dataList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
    nextTick(() => {
      if (props.selectedIds && multipletableRef.value) {
        let idsArray = [];
        if (typeof props.selectedIds === 'string') {
          idsArray = props.selectedIds.split(',').map(id => Number(id)).filter(id => id);
        } else if (Array.isArray(props.selectedIds)) {
          idsArray = props.selectedIds.map(id => Number(id)).filter(id => id);
        }

        dataList.value.forEach(row => {
          if (idsArray.includes(row.id)) {
            multipletableRef.value.toggleRowSelection(row, true);
          }
        });
      }
    });
  });
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleSelectionChange(selection) {
  selectedList.value = selection;
}

function handleRowClick(row) {
  multipletableRef.value.toggleRowSelection(row);
}

function handleConfirm() {
  if (selectedList.value.length === 0) {
    proxy.$modal.msgWarning("请至少选择一个关联应用");
    return;
  }
  emit('confirm', selectedList.value);
  handleClose();
}

function handleClose() {
  dialogVisible.value = false;
  emit('update:visible', false);
  selectedList.value = [];
}

watch(() => props.visible, (val) => {
  dialogVisible.value = val;
  if (val) {
    getList();
  }
});
</script>

<style lang="scss" scoped>
.pagecont-bottom {
  margin-top: 15px;
}
</style>

