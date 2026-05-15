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
  <el-table stripe height="58vh" v-loading="loading" :data="solutionApplyList" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column label="编号" align="center" width="80">
      <template #default="scope">
        <span>{{ scope.$index + 1 }}</span>
      </template>
    </el-table-column>
    <el-table-column label="应用名称" align="center" prop="applyId" show-overflow-tooltip>
      <template #default="scope">
        {{ getMyAppInfo(scope.row.applyId, 'name') || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="描述" align="left" prop="applyId" width="380" show-overflow-tooltip>
      <template #default="scope">
        {{ getMyAppInfo(scope.row.applyId, 'description') || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="创建人" align="center" prop="createBy"/>
    <el-table-column label="创建时间" align="center" prop="createTime" sortable="custom" :sort-orders="['descending', 'ascending']">
      <template #default="scope">
        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
      </template>
    </el-table-column>
    <el-table-column label="最后更新时间" align="center" prop="updateTime" sortable="custom" :sort-orders="['descending', 'ascending']">
      <template #default="scope">
        <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') }}</span>
      </template>
    </el-table-column>
    <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        width="240"
    >
      <template #default="scope">
        <el-button
            link
            type="primary"
            icon="VideoPlay"
            @click="handleUpdate(scope.row)"
        >立即体验
        </el-button>
        <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetail(scope.row)"
        >查看详情
        </el-button>
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
</template>

<script setup name="solutionApplyList">
import { ref, reactive, toRefs, watch } from 'vue';
import { listSolutionApply } from "@/api/kac/solution/solutionApply.js";

const router = useRouter();

const {proxy} = getCurrentInstance();
const loading = ref(true);
const total = ref(0);
const myAppList = ref([]);
const defaultSort = ref({ prop: "createTime", order: "desc" });

const props = defineProps({
  solutionId: {
    type: Number,
    required: true
  },
  myAppList: {
    type: Array,
    default: () => []
  },
});

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    solutionId: props.solutionId,
    createTime: null,
  },
});

const { queryParams } = toRefs(data);

const solutionApplyList = ref([]);

const getMyAppInfo = (id, field) => {
  if (!id) return '-';
  const target = myAppList.value.find(item => item.id == id);
  console.log(target, 'target');
  return target && target[field] != null && target[field] !== '' ? target[field] : '-';
};

// 监听父组件传递的知识库列表变化
watch(
    () => props.myAppList,
    (newList) => {
      if (newList && newList.length > 0) {
        myAppList.value = newList;
        getList();
      }
    },
    { immediate: true, deep: true }
)

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

function getList() {
  if (props.solutionId) {
    queryParams.value.solutionId = props.solutionId;
  }
  loading.value = true;
  listSolutionApply(queryParams.value).then(response => {
    solutionApplyList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

// 立即体验
function handleUpdate(row) {
  let appId = getMyAppInfo(row.applyId, 'id');
  let appName = getMyAppInfo(row.applyId, 'name');
  router.push({
    path: "/kac/myApp/pluginApply",
    query: {
      applyId: appId,
      title: appName
    }
  });
}

// 查看详情
function handleDetail(row) {
  let appId = getMyAppInfo(row.applyId, 'id');
  router.push({
    path: "/kac/myApp/myAppDetail",
    query: {
      id: appId
    }
  });
}
</script>

