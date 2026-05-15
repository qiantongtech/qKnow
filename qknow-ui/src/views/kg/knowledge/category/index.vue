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
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="kg/knowledge/category.list" />
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item label="分类名称" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入分类名称"
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
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['kg:knowledge:category:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              :disabled="multiple"
              @click="handleDelete"
              icon="Delete"
              v-hasPermi="['kg:knowledge:category:remove']"
              @mousedown="(e) => e.preventDefault()"
            >
              删除
            </el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button
              class="toggle-expand-all"
              type="primary"
              plain
              @click="toggleExpandAll"
            >
              <svg-icon v-if="isExpandAll" icon-class="toggle" />
              <svg-icon v-else icon-class="expand" />
              <span>{{ isExpandAll ? "折叠" : "展开" }}</span>
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
        v-if="refreshTable"
        v-loading="loading"
        :data="categoryList"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpandAll"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <!-- <el-table-column type="selection" width="55" align="center" /> -->
        <el-table-column
          v-if="getColumnVisibility(1)"
          label="分类名称"
          prop="name"
          width="250"
          :show-overflow-tooltip="true"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(2)"
          label="显示顺序"
          align="center"
          prop="orderNum"
          width="100"
        >
          <template #default="scope">
            {{ scope.row.orderNum !== null ? scope.row.orderNum : "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(3)"
          label="备注"
          width="300"
          align="left"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(4)"
          label="创建人"
          align="center"
          prop="createBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(5)"
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
        >
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(6)"
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
              icon="Plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['kg:knowledge:category:add']"
              >新增</el-button
            >
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['kg:knowledge:category:edit']"
              >修改</el-button
            >
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['kg:knowledge:category:remove']"
              >删除</el-button
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
    </div>

    <!-- 添加或修改知识分类对话框 -->
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
        </span>
      </template>
      <el-form
        ref="kgCategoryRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-row :gutter="20" v-if="result">
          <el-col :span="24">
            <el-form-item label="上级类型" prop="parentId">
              <el-tree-select
                v-model="form.parentId"
                :data="categoryOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="选择上级类型"
                check-strictly
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入分类名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number
                style="width: 100%"
                v-model="form.orderNum"
                placeholder="请输入显示顺序"
                controls-position="right"
                :min="0"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入备注"
                maxlength="512"
                show-word-limit
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
  </div>
</template>

<script setup name="Category">
import {
  getAllList,
  getCategory,
  delCategory,
  addCategory,
  updateCategory,
} from "@/api/kg/knowledge/category";
import { ref } from "vue";
import { listDocument } from "@/api/kg/knowledge/document.js";

const { proxy } = getCurrentInstance();

const categoryList = ref([]);
const graphList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "分类名称", visible: true },
  { key: 2, label: "显示顺序", visible: true },
  { key: 3, label: "备注", visible: true },
  { key: 4, label: "创建人", visible: true },
  { key: 5, label: "创建时间", visible: true },
  { key: 6, label: "操作", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const categoryOptions = ref([]);
const refreshTable = ref(true);
const isExpandAll = ref(true);
const result = ref(true);
const router = useRouter();
const route = useRoute();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    parentId: null,
    name: null,
    orderNum: null,
    ancestors: null,
    createTime: null,
    orderByColumn: "orderNum",
    isAsc: "ascending",
  },
  rules: {
    name: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询知识分类列表 */
function getList() {
  loading.value = true;
  getAllList(queryParams.value).then((response) => {
    categoryList.value = proxy.handleTree(response.data, "id");
    loading.value = false;
  });
}
// 判断是不是一级分类
const isValidData = (ancestors) => {
  if (ancestors && ancestors.includes(",")) {
    return true;
  }
  return false;
};

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
    parentId: null,
    name: null,
    orderNum: 0,
    ancestors: null,
    validFlag: "0",
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("kgCategoryRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 搜索按钮操作 */
function handleQueryAdd() {
  let param = {
    pageSize: 10,
    pageNum: 1,
  };
  getAllList(param).then((response) => {
    categoryOptions.value = proxy.handleTree(response.data, "id");
  });
  // 清空已选的上级信息
  form.value.parentId = null;
}

/** 排序触发事件 */
function handleSortChange(column) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
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

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  getAllList(queryParams.value).then((response) => {
    categoryOptions.value = proxy.handleTree(response.data, "id");
  });
  if (row != undefined) {
    form.value.parentId = row.id;
  }

  open.value = true;
  title.value = "新增知识分类";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getAllList(queryParams.value).then((response) => {
    categoryOptions.value = proxy.handleTree(response.data, "id");
  });
  const _id = row.id || ids.value;
  getCategory(_id).then((response) => {
    form.value = response.data;
    result.value = form.value.ancestors.includes(",") ? true : false;
    open.value = true;
    title.value = "修改知识分类";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["kgCategoryRef"].validate((valid) => {
    if (valid) {
      if (form.value.parentId == null) {
        form.value.parentId = 0;
      }
      if (form.value.id != null) {
        updateCategory(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        console.log(form.value);
        addCategory(form.value)
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
  const name = row.name;

  // 获取该分类下的文档列表
  listDocument({ ids: _ids }).then((res) => {
    console.log(res.data.total);

    // 判断该分类下是否存在文件
    if (res.data.total !== 0) {
      proxy.$modal.msgWarning("该分类下存有文件，不可删除");
      return;
    } else {
      // 获取该分类下的子分类
      getAllList({ parentId: row.id }).then((res) => {
        if (res.data.length !== 0) {
          proxy.$modal.msgWarning("该分类下存有子分类，不可删除");
          return;
        } else {
          // 提示确认删除
          proxy.$modal
            .confirm('是否确认删除知识分类名称为"' + name + '"的数据项？')
            .then(function () {
              return delCategory(_ids);
            })
            .then(() => {
              getList();
              proxy.$modal.msgSuccess("删除成功");
            })
            .catch(() => {});
        }
      });
    }
  });
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

getList();
</script>
<style scoped lang="scss">
/* 表格单元格悬浮提示样式 - 多行显示 */
::v-deep .el-table .cell.el-tooltip {
  display: -webkit-box !important;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical !important;
  white-space: normal;
}

::v-deep .el-popper.is-light {
    box-shadow: 0px 2px 8px 1px rgba(0, 0, 0, 0.15);
    max-width: 600px;
    font-size: 14px;
    padding: 16px;
    line-height: 22px;
}
</style>