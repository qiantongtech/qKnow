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
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          @click="handleAdd"
          v-hasPermi="['knowledge:document:add']"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          @click="handleExport"
          v-hasPermi="['knowledge:document:export']"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
        :columns="columns"
      ></right-toolbar>
    </div>
  </div>
  <el-table
    stripe
    height="374px"
    v-loading="loading"
    :data="documentList"
    @selection-change="handleSelectionChange"
    :default-sort="defaultSort"
    @sort-change="handleSortChange"
  >
    <el-table-column type="selection" width="55" align="center" />
    <el-table-column
      v-if="columns[0].visible"
      label="ID"
      align="center"
      prop="id"
    />
    <el-table-column
      v-if="columns[1].visible"
      label="工作区id"
      align="center"
      prop="workspaceId"
    >
      <template #default="scope">
        {{ scope.row.workspaceId || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[3].visible"
      label="知识分类id"
      align="center"
      prop="categoryId"
    >
      <template #default="scope">
        {{ scope.row.categoryId || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[4].visible"
      label="知识分类名称"
      align="center"
      prop="categoryName"
    >
      <template #default="scope">
        {{ scope.row.categoryName || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[5].visible"
      label="文件名称"
      align="center"
      prop="name"
    >
      <template #default="scope">
        {{ scope.row.name || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[6].visible"
      label="文件路径"
      align="center"
      prop="path"
    >
      <template #default="scope">
        {{ scope.row.path || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[7].visible"
      label="文件描述"
      align="center"
      prop="description"
    >
      <template #default="scope">
        {{ scope.row.description || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[12].visible"
      label="创建人"
      align="center"
      prop="createBy"
    >
      <template #default="scope">
        {{ scope.row.createBy || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[14].visible"
      label="创建时间"
      align="center"
      prop="createTime"
      width="180"
      sortable="custom"
      :sort-orders="['descending', 'ascending']"
    >
      <template #default="scope">
        <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
      </template>
    </el-table-column>
    <el-table-column
      v-if="columns[18].visible"
      label="备注"
      align="center"
      prop="remark"
    >
      <template #default="scope">
        {{ scope.row.remark || "-" }}
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
          icon="Edit"
          @click="handleUpdate(scope.row)"
          v-hasPermi="['knowledge:document:edit']"
          >修改</el-button
        >
        <el-button
          link
          type="danger"
          icon="Delete"
          @click="handleDelete(scope.row)"
          v-hasPermi="['knowledge:document:remove']"
          >删除</el-button
        >
        <el-button
          link
          type="primary"
          icon="view"
          @click="handleDetail(scope.row)"
          v-hasPermi="['knowledge:document:edit']"
          >详情</el-button
        >
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
    v-show="total > 0"
    :total="total"
    v-model:page="queryParams.pageNum"
    v-model:limit="queryParams.pageSize"
    @pagination="getList"
  />

  <!-- 添加或修改知识文件对话框 -->
  <el-dialog
    :title="title"
    v-model="open"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
        <el-icon size="20" style="color: #909399; font-size: 16px">
          <InfoFilled />
        </el-icon>
      </span>
      <button
        aria-label="el.dialog.close"
        class="el-dialog__headerbtn"
        type="button"
      >
        <i class="el-icon el-dialog__close"
          ><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
            <path
              fill="currentColor"
              d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"
            ></path></svg
        ></i>
      </button>
    </template>
    <el-form ref="documentRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工作区id" prop="workspaceId">
            <el-input v-model="form.workspaceId" placeholder="请输入工作区id" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="知识分类id" prop="categoryId">
            <el-input
              v-model="form.categoryId"
              placeholder="请输入知识分类id"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="知识分类名称" prop="categoryName">
            <el-input
              v-model="form.categoryName"
              placeholder="请输入知识分类名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文件名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入文件名称" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文件路径" prop="path">
            <el-input
              v-model="form.path"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="文件描述" prop="description">
            <el-input
              v-model="form.description"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="submitForm"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>

  <!-- 知识文件详情对话框 -->
  <el-dialog
    :title="title"
    v-model="openDetail"
    width="800px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
        <el-icon size="20" style="color: #909399; font-size: 16px">
          <InfoFilled />
        </el-icon>
      </span>
      <button
        aria-label="el.dialog.close"
        class="el-dialog__headerbtn"
        type="button"
      >
        <i class="el-icon el-dialog__close"
          ><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
            <path
              fill="currentColor"
              d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"
            ></path></svg
        ></i>
      </button>
    </template>
    <el-form ref="documentRef" :model="form" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工作区id" prop="workspaceId">
            <div>
              {{ form.workspaceId }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="知识分类id" prop="categoryId">
            <div>
              {{ form.categoryId }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="知识分类名称" prop="categoryName">
            <div>
              {{ form.categoryName }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文件名称" prop="name">
            <div>
              {{ form.name }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文件路径" prop="path">
            <div>
              {{ form.path }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="文件描述" prop="description">
            <div>
              {{ form.description }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <div>
              {{ form.remark }}
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
import {
  listDocument,
  getDocument,
  delDocument,
  addDocument,
  updateDocument,
} from "@/api/kg/knowledge/document";

const { proxy } = getCurrentInstance();

const documentList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "ID", visible: true },
  { key: 1, label: "工作区id", visible: true },
  { key: 2, label: "图谱id", visible: true },
  { key: 3, label: "知识分类id", visible: true },
  { key: 4, label: "知识分类名称", visible: true },
  { key: 5, label: "文件名称", visible: true },
  { key: 6, label: "文件路径", visible: true },
  { key: 7, label: "文件描述", visible: true },
  { key: 8, label: "预览次数", visible: true },
  { key: 9, label: "下载次数", visible: true },
  { key: 10, label: "是否有效", visible: true },
  { key: 11, label: "删除标志", visible: true },
  { key: 12, label: "创建人", visible: true },
  { key: 13, label: "创建人id", visible: true },
  { key: 14, label: "创建时间", visible: true },
  { key: 15, label: "更新人", visible: true },
  { key: 16, label: "更新人id", visible: true },
  { key: 17, label: "更新时间", visible: true },
  { key: 18, label: "备注", visible: true },
]);

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });

const data = reactive({
  documentDetail: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    categoryId: null,
    categoryName: null,
    name: null,
    path: null,
    description: null,
    createTime: null,
  },
  rules: {
    workspaceId: [
      { required: true, message: "工作区id不能为空", trigger: "blur" },
    ],
    categoryId: [
      { required: true, message: "知识分类id不能为空", trigger: "blur" },
    ],
    name: [{ required: true, message: "文件名称不能为空", trigger: "blur" }],
    path: [{ required: true, message: "文件路径不能为空", trigger: "blur" }],
    validFlag: [
      { required: true, message: "是否有效不能为空", trigger: "blur" },
    ],
    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    updateTime: [
      { required: true, message: "更新时间不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, documentDetail, rules } = toRefs(data);

/** 查询知识文件列表 */
function getList() {
  loading.value = true;
  listDocument(queryParams.value).then((response) => {
    documentList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
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
    categoryId: null,
    categoryName: null,
    name: null,
    path: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("documentRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加知识文件";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDocument(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改知识文件";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDocument(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "知识文件详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["documentRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateDocument(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        addDocument(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除知识文件编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delDocument(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "kg/document/export",
    {
      ...queryParams.value,
    },
    `document_${new Date().getTime()}.xlsx`
  );
}

getList();
</script>
