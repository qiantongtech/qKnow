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
  <div class="justify-between mb15">
    <el-row :gutter="10" class="btn-style">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Sort"
            @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
    </el-row>
    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
  </div>

  <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="detailsList"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
  >
    <el-table-column label="ID" align="center" prop="parentId" >
      <template #default="scope">
        {{ scope.row.id || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="标题" prop="title" />
    <el-table-column label="内容" align="center" prop="content" >
      <template #default="scope">
        {{ scope.row.content || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="创建人" align="center" prop="createBy" >
      <template #default="scope">
        {{ scope.row.createBy || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="创建时间" align="center" prop="createTime" width="180">
      <template #default="scope">
        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
      </template>
    </el-table-column>
    <el-table-column label="备注" align="center" prop="remark" >
      <template #default="scope">
        {{ scope.row.remark || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
      <template #default="scope">
        <el-button link type="primary" icon="Edit"  >修改</el-button>
        <el-button link type="primary" icon="Plus"  >新增</el-button>
        <el-button link type="danger" icon="Delete" >删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 添加或修改详情对话框 -->
</template>

<script setup name="ComponentTwo">

  const { proxy } = getCurrentInstance();

  const detailsList = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const title = ref("");
  const isExpandAll = ref(true);
  const refreshTable = ref(true);

  const props = defineProps(['bidId'])

  const data = reactive({
    form: {},
    queryParams: {
      parentId: null,
      bidId: null,
      title: null,
      content: null,
      createTime: null,
    },
    rules: {
      parentId: [
        { required: true, message: "节点不能为空", trigger: "blur" }
      ],
      title: [
        { required: true, message: "标题不能为空", trigger: "blur" }
      ],
      validFlag: [
        { required: true, message: "是否有效不能为空", trigger: "blur" }
      ],
      delFlag: [
        { required: true, message: "删除标志不能为空", trigger: "blur" }
      ],
      createTime: [
        { required: true, message: "创建时间不能为空", trigger: "blur" }
      ],
      updateTime: [
        { required: true, message: "更新时间不能为空", trigger: "blur" }
      ],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询详情列表 */
  function getList() {
    loading.value = true;
    let responseData = [
      {
        "id": 1,
        "parentId": 0,
        "bidId": 13,
        "title": "测试",
        "content": "11",
        "validFlag": true,
        "delFlag": false,
        "createBy": "admin",
        "creatorId": 1,
        "createTime": "2024-12-16 12:08:41",
        "updateBy": null,
        "updatorId": null,
        "updateTime": "2024-12-16 12:08:41",
        "remark": "11"
      },
      {
        "id": 2,
        "parentId": 1,
        "bidId": 13,
        "title": "测试2",
        "content": "2",
        "validFlag": true,
        "delFlag": false,
        "createBy": "admin",
        "creatorId": 1,
        "createTime": "2024-12-16 12:09:50",
        "updateBy": null,
        "updatorId": null,
        "updateTime": "2024-12-16 12:09:50",
        "remark": "2"
      }
    ];
    detailsList.value = proxy.handleTree(responseData, "id", "parentId");
    loading.value = false;
  }

  /** 查询详情下拉树结构 */
  function getTreeselect() {

  }

  // 取消按钮
  function cancel() {
    open.value = false;
    reset();
  }

  // 表单重置
  function reset() {
    form.value = {
      id: null,
      parentId: null,
      bidId: null,
      title: null,
      content: null,
      validFlag: null,
      delFlag: null,
      createBy: null,
      creatorId: null,
      createTime: null,
      updateBy: null,
      updatorId: null,
      updateTime: null,
      remark: null
    };
    proxy.resetForm("bidDetailsRef");
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
  }

  /** 新增按钮操作 */
  function handleAdd(row) {
    reset();
    getTreeselect();
    if (row != null && row.id) {
      form.value.parentId = row.id;
    } else {
      form.value.parentId = 0;
    }
    open.value = true;
    title.value = "添加内容";
  }

  /** 展开/折叠操作 */
  function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
      refreshTable.value = true;
    });
  }

  /** 修改按钮操作 */
  async function handleUpdate(row) {
    reset();
    await getTreeselect();
    if (row != null) {
      form.value.parentId = row.parentId;
    }
    // (row.id).then(response => {
    //   form.value = response.data;
    //   open.value = true;
    //   title.value = "修改详情";
    // });
  }

  getList();
</script>
