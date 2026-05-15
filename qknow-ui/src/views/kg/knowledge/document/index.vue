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
    <GuideTip tip-id="kg/knowledge/document.list" />
    <el-container>
      <!-- 左侧可调整的部分 -->
      <DeptTree
        :deptOptions="KcOptions"
        :leftWidth="leftWidth"
        :placeholder="'请输入分类名称'"
        @node-click="handleNodeClick"
        ref="deptTreeRef"
      />
      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            @submit.prevent
          >
            <el-form-item label="文件名称" prop="name">
              <el-input
                class="el-form-input-width"
                v-model="queryParams.name"
                placeholder="请输入文件名称"
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
              <el-button
                @click="resetQuery"
                @mousedown="(e) => e.preventDefault()"
              >
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
                  v-hasPermi="['kg:knowledge:document:add']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  plain
                  :disabled="multiple"
                  @click="handleDelete"
                  icon="Delete"
                  v-hasPermi="['kg:knowledge:document:remove']"
                  @mousedown="(e) => e.preventDefault()"
                >
                  删除
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
            v-loading="loading"
            :data="documentList"
            @selection-change="handleSelectionChange"
            :default-sort="defaultSort"
            @sort-change="handleSortChange"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column
              v-if="getColumnVisibility(1)"
              label="编号"
              align="center"
              prop="id"
              width="80"
              sortable="custom"
            />
            <el-table-column 
              v-if="getColumnVisibility(2)"
              label="文件名称"
              prop="name"
              align="left"
              width="200px"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                <div class="file-name-cell">
                  <img
                    v-if="getFileType(scope.row.name)"
                    :src="getFileType(scope.row.name)"
                    class="file-type-icon"
                  />
                  <span class="file-name-text">{{
                    scope.row.name || "-"
                  }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(3)"
              label="文件描述"
              align="left"
              prop="description"
              width="300px"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                {{ scope.row.description || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(4)"
              label="分类"
              align="left"
              prop="categoryName"
              width="120px"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                {{ scope.row.categoryName || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(5)"
              label="文件大小"
              align="center"
              prop="fileSize"
              width="100"
            >
              <template #default="scope">
                {{ scope.row.fileSize || '0.00' }} MB
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(6)"
              label="备注"
              align="left"
              prop="remark"
              width="280px"
              :show-overflow-tooltip="{ effect: 'light' }"
            >
              <template #default="scope">
                {{ scope.row.remark || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(7)"
              label="创建人"
              align="center"
              prop="createBy"
            >
              <template #default="scope">
                {{ scope.row.createBy || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(8)"
              label="创建时间"
              align="center"
              prop="createTime"
              width="180"
              sortable="custom"
              :sort-orders="['descending', 'ascending']"
            >
              <template #default="scope">
                <span>{{
                  parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              v-if="getColumnVisibility(9)"
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
              width="240"
            >
              <template #default="scope">
                <el-button
                  v-track="{
                    type: 'preview',
                    documentId: scope.row.id,
                    module: 'kg',
                  }"
                  link
                  type="primary"
                  icon="view"
                  @click="previewRefactoring(scope.row)"
                  >预览</el-button
                >
                <el-button
                  v-track="{
                    type: 'download',
                    documentId: scope.row.id,
                    module: 'kg',
                  }"
                  link
                  type="primary"
                  icon="download"
                  @click="handleDownload(scope.row)"
                  >下载</el-button
                >
                <el-popover placement="bottom" :width="150" trigger="click">
                  <template #reference>
                    <el-button type="primary" link @click.stop>
                      <template #icon><el-icon :size="14"><ArrowDown /></el-icon></template>
                      更多
                    </el-button>
                  </template>
                  <div class="card-button-group">
                    <el-button
                      link
                      type="primary"
                      @click="handleUpdate(scope.row)"
                      v-hasPermi="['kg:knowledge:document:edit']"
                    >
                      <template #icon><el-icon :size="14"><Edit /></el-icon></template>
                      修改
                    </el-button>
                    <el-button
                      link
                      type="danger"
                      @click="handleDelete(scope.row)"
                      v-hasPermi="['kg:knowledge:document:remove']"
                    >
                      <template #icon><el-icon :size="14"><Delete /></el-icon></template>
                      删除
                    </el-button>
                  </div>
                </el-popover>
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
      </el-main>
    </el-container>

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
        </span>
      </template>
      <el-form
        ref="documentRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="知识分类" prop="categoryId">
              <el-tree-select
                v-model="form.categoryId"
                :data="KcOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择知识分类"
                check-strictly
                @change="handleTypeChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <!--            <el-col :span="12">-->
          <!--              <el-form-item label="文件名称" prop="name">-->
          <!--                <el-input v-model="form.name" placeholder="请输入文件名称" />-->
          <!--              </el-form-item>-->
          <!--            </el-col>-->
          <el-col :span="24">
            <el-form-item label="文件路径" prop="path">
              <FileUpload
                v-model="form.path"
                :fileName="form.name"
                :fileSize="15"
                :fileType="[
                  'txt',
                  'pdf',
                  'html',
                  'xlsx',
                  'xls',
                  'docx',
                  'csv',
                  'md',
                  'mdx',
                  'htm',
                  'markdown',
                ]"
                @update:fileName="updateFormFileName"
                @delete:index="handleDeleteFile"
              ></FileUpload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入文件描述"
                maxlength="1024"
                show-word-limit
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

<script setup name="Document">
import DeptTree from "@/components/DeptTree";
import {
  getFileTypes,
  listDocument,
  getDocument,
  delDocument,
  addDocument,
  updateDocument,
} from "@/api/kg/knowledge/document";
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { ArrowDown, Edit, Delete } from "@element-plus/icons-vue";
import { filePreview } from "@/utils/kkFileView.js";
import { getFileFormat } from "@/utils/app/chat/chat.js";
import FileUpload from "@/components/FileUpload2/index.vue";
import word from "@/assets/app/office/WORD.png";
import excel from "@/assets/app/office/ECEL.png";
import pdf from "@/assets/app/office/PDF.png";
import ppt from "@/assets/app/office/PPT.png";
import tet from "@/assets/app/office/TET.png";

const { proxy } = getCurrentInstance();

const deptTreeRef = ref(null);
const documentList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "文件名称", visible: true },
  { key: 3, label: "文件描述", visible: true },
  { key: 4, label: "分类", visible: true },
  { key: 5, label: "文件大小", visible: true },
  { key: 6, label: "备注", visible: true },
  { key: 7, label: "创建人", visible: true },
  { key: 8, label: "创建时间", visible: true },
  { key: 9, label: "操作", visible: true },
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
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();
const route = useRoute();

const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置
const kcName = ref("");
const KcOptions = ref(undefined);
const selectedNodeId = ref(null);
const selectedNodeName = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    categoryId: null,
    categoryName: null,
    id: null,
    name: null,
    orderByColumn: "createTime",
    isAsc: "descending",
  },
  rules: {
    categoryId: [
      { required: true, message: "知识分类id不能为空", trigger: "blur" },
    ],
    name: [{ required: true, message: "文件名称不能为空", trigger: "blur" }],
    path: [{ required: true, message: "文件路径不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询知识文件列表 */
function getList() {
  loading.value = true;
  const fileSizes = ['0.52', '1.34', '2.68', '0.89', '3.12', '4.56', '1.78', '2.34', '0.95', '3.78'];
  listDocument(queryParams.value).then((response) => {
    documentList.value = response.data.rows.map((row, index) => ({
      ...row,
      fileSize: fileSizes[index % fileSizes.length]
    }));
    total.value = response.data.total;
    loading.value = false;
  });
}
/** 查询部门下拉树结构 */
function getCategoryTree() {
  getFileTypes().then((response) => {
    KcOptions.value = [{
      id: 0,
      name: '知识分类',
      children: response.data,
      count: response.data.length,
      totalCount: response.data.reduce((sum, item) => sum + item.totalCount, 0)
    }];
  });
}

/** 递归查找选中的节点 */
const findNodeById = (nodes, id) => {
  for (let node of nodes) {
    if (node.id === id) {
      return node;
    }
    if (node.children) {
      const found = findNodeById(node.children, id);
      if (found) return found;
    }
  }
  return null;
};

/** 节点单击事件 */
function handleNodeClick(data) {
  let ids = [];
  collectIds(data, ids);
  queryParams.value.ids = ids;
  handleQuery();
}

function collectIds(node, ids) {
  ids.push(node.id);
  if (node.children && Array.isArray(node.children)) {
    node.children.forEach((child) => collectIds(child, ids));
  }
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
    name: [],
    // name: null,
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
  //请求树形数据
  // getCategoryTree();
}

/** 重置按钮操作 */
function resetQuery() {
    console.log('1-----',deptTreeRef.value);
    if(deptTreeRef.value){
      deptTreeRef.value?.resetTree();
    }
  proxy.resetForm("queryRef");
  //不重置知识库id
  selectedNodeId.value = null;
  selectedNodeName.value = null;
  form.value.categoryId = null;
  queryParams.value.categoryId = undefined;
  queryParams.value.ids = undefined;
  kcName.value = "";
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
  title.value = "新增知识文件";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getDocument(_id).then((response) => {
    form.value = response.data;
    form.value.name = response.data.name.split(",");
    open.value = true;
    title.value = "修改知识文件";
  });
}

// 处理文件删除
const handleDeleteFile = (index) => {
  // 从form.name数组中删除对应索引的文件名
  if (Array.isArray(form.value.name)) {
    form.value.name.splice(index, 1);
  }
};

/** 提交按钮 */
function submitForm() {
  proxy.$refs["documentRef"].validate((valid) => {
    if (valid) {
      form.value.name = form.value.name.join(",");
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
      getCategoryTree();
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

const handleTypeChange = (value) => {
  const selectedNode = findNodeById(KcOptions.value, value);
  if (selectedNode) {
    form.value.categoryName = selectedNode.name;
  }
};

const updateFormFileName = (newValue) => {
  form.value.name.push(newValue);
};

const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  // 使用 requestAnimationFrame 减少重绘频率
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};

const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => {});
  }
};

const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};

// 文件类型图标映射
const fileImg = {
  doc: word,
  docx: word,
  xlsx: excel,
  xls: excel,
  ppt: ppt,
  pptx: ppt,
  pdf: pdf,
  txt: tet,
};

// 获取文件图标
const getFileType = (name) => {
  return fileImg[getFileFormat(name)];
};


function handleDownload(row) {
  if (row.path === "") {
    proxy.$modal.msgError("没有文件");
  } else {
    let path = import.meta.env.VITE_APP_BASE_API + "/profile" + row.path;
    fetch(path) // 使用 fetch 获取文件数据
      .then((response) => response.blob()) // 将文件数据转换为 Blob
      .then((blob) => {
        const link = document.createElement("a");
        const url = URL.createObjectURL(blob); // 创建 Blob URL
        link.href = url;
        link.setAttribute("download", row.name || "下载文件"); // 设置文件名
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url); // 释放 Blob URL
      })
      .catch(() => {
        proxy.$modal.msgError("文件下载失败");
      });
  }
}

function previewRefactoring(row) {
  let path = import.meta.env.VITE_APP_BASE_API + "/profile" + row.path;
  filePreview(path);
}

getList();
getCategoryTree();
</script>
<style scoped lang="scss">
:deep(.selectlist .el-tag.el-tag--info) {
  background: #f3f8ff !important;
  border: 0px solid #6ba7ff !important;
  color: #2666fb !important;
}

.left-pane {
  background-color: #ffffff;
  overflow: hidden;
  transition: width 0s; /* 可以根据需要调整过渡时间 */
}

.resize-bar {
  cursor: ew-resize;
  background-color: #f0f2f5;
  height: 86vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-handle-sx {
  width: 15px;
  text-align: center;
  //   cursor:pointer
}

//.zjsx {
//  display: inline-block;
//  width: 5px;
//  height: 50px;
//  border-left: 1px solid #ccc;
//  border-right: 1px solid #ccc;
//}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

.el-aside {
  padding: 2px 0px;
  margin-bottom: 0px;
  background-color: #f0f2f5;
}

.custom-tree-node {
  display: flex;
  align-items: center;
}

.treelable {
  margin-left: 5px;
}

.zjiconimg {
  font-size: 12px;
}

.colorxz {
  color: #358cf3;
}

.colorwxz {
  color: #afd1fa;
}

.iconimg {
  font-size: 15px;
}

//上传附件样式调整
// .el-upload-list{
//    display: flex;
// }
:deep(.el-upload-list__item) {
  width: 100%;
  height: 25px;
}

.filecont {
  height: 100%;
}
:deep(.el-dialog .el-dialog__body) {
  height: 100% !important;
  //padding: 10px !important;
}
.mr0 {
  margin-right: 0px !important;
}
.card-button-group {
  display: flex;
  flex-direction: column;
  button {
    margin-left: 0;
  }
}
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

.file-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;

  .file-type-icon {
    width: 20px;
    height: 20px;
    flex-shrink: 0;
  }

  .file-name-text {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    word-break: break-all;
    line-height: 1.5em;
    max-height: 3em;
  }
}
</style>
