<!--
  Copyright © 2026 Qiantong Technology Co., Ltd.
  qKnow Knowledge Platform
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qknow.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2026 江苏千桐科技有限公司
  qKnow 知识平台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qknow.qiantong.tech/business.html
-->

<template>
  <div class="app-container" ref="app-container">
    <el-container>
      <!-- 左侧可调整的部分 -->
      <el-aside
          :style="{ width: `${leftWidth}px` }"
          class="left-pane"
      >
        <div class="left-tree">
          <div class="head-container">
            <el-input
                v-model="kcName"
                placeholder="请输入分类名称"
                clearable
                prefix-icon="Search"
                style="margin-bottom: 20px"
            />
          </div>
          <div class="head-container">
            <el-tree
                :data="KcOptions"
                :props="{ label: 'label', children: 'children' }"
                :filter-node-method="filterNode"
                ref="kcTreeRef"
                node-key="id"
                highlight-current
                default-expand-all
                :current-node-key="currentLivingId"
                @node-click="handleNodeClick"
                @current-change="handleCurrentChange"
            >
              <template #default="{ node,data }">
                     <span class="custom-tree-node">
                        <!-- 第一级 -->
                        <el-icon class="iconimg colorxz" v-if="node.expanded && node.level === 1"><FolderOpened/></el-icon>
                        <el-icon class="iconimg colorxz" v-if="!node.expanded && node.level === 1"><Folder/></el-icon>
                       <!-- 第二级 -->
                        <el-icon class="iconimg colorxz"
                                 v-if="node.expanded &&node.childNodes.length && node.level == 2"><FolderOpened/></el-icon>
                        <el-icon class="iconimg colorxz"
                                 v-if="!node.expanded &&node.childNodes.length && node.level == 2"><Folder/></el-icon>
                       <!-- 子级 -->
                        <el-icon class="zjiconimg colorwxz" v-show="!node.isCurrent && node.level == 3"><Tickets/></el-icon>
                        <el-icon class="zjiconimg colorxz" v-show="node.isCurrent && node.level == 3"><Tickets/></el-icon>

                        <span class="treelable" @click="getNode(node)">{{ node.label}}</span>
                     </span>
              </template>
            </el-tree>
          </div>
        </div>
      </el-aside>
      <div class="resize-bar" @mousedown="startResize">
        <div class="resize-handle-sx">
          <span class="zjsx"></span>
        </div>
      </div>
      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
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
              <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
              </el-button>
              <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div  class="pagecont-bottom">
          <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
              <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" v-hasPermi="['kmcDocument:kmcDocument:document:add']"
                           @mousedown="(e) => e.preventDefault()">
                  <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
              </el-col>
            </el-row>
            <div class="justify-end top-right-btn">
              <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
            </div>
          </div>
          <el-table stripe height="590px" v-loading="loading" :data="documentList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column v-if="getColumnVisibility(4)" label="文件名称" prop="name">
              <template #default="scope">
                {{ scope.row.name || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(6)" label="文件描述" align="left" prop="description">
              <template #default="scope">
                {{ scope.row.description || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(9)" label="创建人" align="center" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="getColumnVisibility(11)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                           v-hasPermi="['kmcDocument:kmcDocument:document:edit']">修改</el-button>
                <el-button link type="primary" icon="view" @click="previewRefactoring(scope.row)">预览</el-button>
                <el-button link type="primary" icon="download" @click="handleDownload(scope.row)">下载</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                           v-hasPermi="['kmcDocument:kmcDocument:document:remove']">删除</el-button>
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
        </div>
      </el-main>
    </el-container>

    <!-- 预览文件弹窗 -->
    <el-dialog class="fileDialog" v-model="dialogVisible" style="height: 90%; width: 85%;" :title="'预览'">
      <div class="filecont">
        <vue-office-pdf v-if="fileType === 'pdf'" :src="fileUrl" :style="{ height: '94%' }"  />
        <vue-office-excel v-if="fileType === 'xls'" :src="fileUrl"  :style="{ height: '94%' }" />
        <vue-office-docx v-if="fileType === 'docx'" :src="fileUrl"  :style="{ height: '94%' }" />
      </div>
    </el-dialog>

    <!-- 添加或修改知识文件对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="documentRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="所属分类" prop="categoryId">
              <el-tree-select
                  v-model="form.categoryId"
                  :data="KcOptions"
                  :props="{ value: 'id', label: 'label', children: 'children' }"
                  value-key="id"
                  placeholder="请选择文件分类"
                  check-strictly
                  @change="handleTypeChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入文件名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="文件路径" prop="path">
              <FileUpload v-model="form.path" :fileName="form.name" :limit="1" :fileSize="15" :platForm="platForm"  @update:fileName="updateFormFileName"></FileUpload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="文件描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="800px"  :append-to="$refs['app-container']" draggable destroy-on-close>
      <el-upload
          ref="uploadRef"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的知识文件数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">取 消</el-button>
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="Document">
import {
  listDocument,
  getDocument,
  delDocument,
  addDocument,
  updateDocument,
  getPdfPreview, updateDownloadCount, updatePreviewCount, selectList
} from "@/api/kmc/kmcDocument/kmcDocument.js";
import {getToken} from "@/utils/auth.js";
import {ref} from "vue";
import {kmcCategoryTree} from "@/api/kmc/kmcCategory/kmcCategory.js";
import moment from "moment/moment.js";
//引入VueOfficeDocx组件
import VueOfficeDocx from "@vue-office/docx";
//引入相关样式
import "@vue-office/docx/lib/index.css";
//引入VueOfficeExcel组件
import VueOfficeExcel from "@vue-office/excel";
//引入相关样式
import "@vue-office/excel/lib/index.css";
//引入VueOfficePdf组件
import VueOfficePdf from '@vue-office/pdf'
import FileUpload from "@/components/FileUpload2/index.vue";

const { proxy } = getCurrentInstance();

const documentList = ref([]);
const platForm = ref("aliyun-oss-qt");
const env = import.meta.env.VITE_APP_ENV;

const defaultSort = ref({ prop: "createTime", order: "desc" });

// 列显隐信息
const columns = ref([
  { key: 4, label: "文件名称", visible: true },
  { key: 6, label: "文件描述", visible: true },
  { key: 9, label: "创建人", visible: true },
  { key: 11, label: "创建时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const router = useRouter();
const route = useRoute();

const leftWidth = ref(300);  // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0;  // 鼠标按下时的初始位置
const kcName = ref("");
const KcOptions = ref(undefined);
const dialogVisible = ref(false);
const fileType = ref('');
const fileUrl = ref('');
const selectedNodeId = ref(null);
const selectedNodeName = ref(null);
const currentLivingId = ref(null);

onMounted(async() => {
  // await getList();
  // await getKmcCategoryTree();
  treeQuery();
});

/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/kmcDocument/document/importData"
});

const data = reactive({
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
    updaterId: null,
    ids: null
  },
  rules: {
    workspaceId: [{ required: true, message: "工作区id不能为空", trigger: "blur" }],
    categoryId: [{ required: true, message: "知识分类id不能为空", trigger: "blur" }],
    name: [{ required: true, message: "文件名称不能为空", trigger: "blur" }],
    path: [{ required: true, message: "文件路径不能为空", trigger: "blur" }],
    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

async function treeQuery(){
  const id = route.query.id || '';
  if (id) {
    queryParams.value.ids = id
    currentLivingId.value = id
  }
  await getList();
  await getKmcCategoryTree();
};

/** 查询部门下拉树结构 */
function getKmcCategoryTree() {
  kmcCategoryTree().then(response => {
    KcOptions.value = response.data;

    nextTick(() => {
      proxy.$refs["kcTreeRef"].setCurrentKey(currentLivingId.value)
    })
  })
};

/** 查询知识文件列表 */
function getList() {
  loading.value = true;
  listDocument(queryParams.value).then(response => {
    documentList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 处理选择变化的方法 */
const handleTypeChange = (value) => {
  const selectedNode = findNodeById(KcOptions.value, value);
  if (selectedNode) {
    form.value.categoryName = selectedNode.label;
  }
};

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

const updateFormFileName = (newValue) => {
  form.value.name = newValue;
};

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 根据名称筛选部门树 */
watch(kcName, val => {
  console.log(val,'val')
  console.log(proxy.$refs["kcTreeRef"], 'kcTreeRef');
  proxy.$refs["kcTreeRef"].filter(val);
});

/** 节点单击事件 */
function handleNodeClick(data) {
  let ids = [];

  collectIds(data, ids);
  queryParams.value.ids = ids

  handleQuery();
}

//监听当前点击的树形
function handleCurrentChange(node) {
  console.log(node,'node')
  if (node != null) {
    selectedNodeId.value = node.id
    selectedNodeName.value = node.label
  } else {
    selectedNodeId.value = null
    selectedNodeName.value = null
  }
}

function collectIds(node, ids) {
  ids.push(node.id);
  if (node.children && Array.isArray(node.children)) {
    node.children.forEach(child => collectIds(child, ids));
  }
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
    previewCount: null,
    downloadCount: null,
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
  selectedNodeId.value = null;
  selectedNodeName.value = null
  form.value.categoryId = null;
  queryParams.value.categoryId = undefined;
  queryParams.value.ids = undefined;
  proxy.$refs.kcTreeRef.setCurrentKey(null);
  kcName.value = "";
  handleQuery();
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加知识文件";
  if (title.value === "添加知识文件") {
    if (selectedNodeId.value != null) {
      form.value.categoryId = selectedNodeId.value
      form.value.categoryName = selectedNodeName.value
    }
  }
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getDocument(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改知识文件";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["documentRef"].validate(valid => {
    if (valid) {
      if (form.value.createTime != null) {
        form.value.createTime = moment(form.value.createTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.updateTime != null) {
        form.value.updateTime = moment(form.value.updateTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.id != null) {
        updateDocument(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addDocument(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  const name = row.name
  proxy.$modal.confirm('是否确认删除知识文件名称为"' + name + '"的数据项？').then(function() {
    return delDocument(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('kmcDocument/document/export', {
    ...queryParams.value
  }, `document_${new Date().getTime()}.xlsx`)
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "知识文件导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `document_template_${new Date().getTime()}.xlsx`)
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
};

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return
  }
  if (link !== "") {
    if(link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id:row.id
        }
      });
    }
  }
};

const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  // 使用 requestAnimationFrame 减少重绘频率
  document.addEventListener('mousemove', updateResize);
  document.addEventListener('mouseup', stopResize);
};

const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => {

    });
  }
};

const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener('mousemove', updateResize);
  document.removeEventListener('mouseup', stopResize);
};

function handleDownload(row) {
  if (row.path === '') {
    proxy.$modal.msgError("没有文件");
  } else {
    let path = import.meta.env.VITE_APP_BASE_API + '/profile' + row.path;
    fetch(path) // 使用 fetch 获取文件数据
        .then(response => response.blob()) // 将文件数据转换为 Blob
        .then(blob => {
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
          proxy.$modal.msgError('文件下载失败');
        });
  }
};

function previewRefactoring(row) {
  const fileName = row.name;
  let fileExt = fileName.split('.').pop().toLowerCase();
  fileExt = fileExt.trim();
  const allowedExtensions = ['pdf','docx', 'xls','xlsx','csv'];
  let path = import.meta.env.VITE_APP_BASE_API + '/profile' + row.path;
  if (!allowedExtensions.includes(fileExt)) {
    const supportedFormats = allowedExtensions.join('、');
    proxy.$modal.msgWarning(`当前仅支持以下格式文件预览：${supportedFormats}`);
    return;
  }
  if (['pdf'].includes(fileExt)) {
    fileType.value = 'pdf';
  } else if (['docx'].includes(fileExt)) {
    fileType.value = 'docx';
  } else if (['xls', 'xlsx','csv'].includes(fileExt)) {
    fileType.value = 'xls';
  }
  fileUrl.value = path;
  dialogVisible.value = true;
}

// content是base64格式
function viewPdf(content,name) {
  console.log("content", content);
  const blob = base64ToBlob(content);
  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
    window.navigator.msSaveOrOpenBlob(blob);
  } else {
    // 获取屏幕尺寸
    const screenWidth = window.screen.width;
    const screenHeight = window.screen.height;

    // 设置窗口尺寸为屏幕尺寸的一部分，例如60%
    const width = screenWidth * 0.7;
    const height = screenHeight * 0.7;

    // 计算窗口居中时的左上角位置
    const left = (screenWidth - width) / 2;
    const top = (screenHeight - height) / 2;
    const fileURL = URL.createObjectURL(blob);
    const win = window.open(fileURL,
        '预览',
        `scrollbars=yes, width=${width}, height=${height}, top=${top}, left=${left}`); //打开ppf文件
    setTimeout(() => {
      win.document.title = name;
    }, 800);
  }
}

function base64ToBlob(code) {
  code = code.replace(/[\n\r]/g, ""); // 检查base64字符串是否符合base64编码
  // atob() 方法用于解码使用 base-64 编码的字符串。
  const raw = window.atob(code);
  const rawLength = raw.length;
  const uInt8Array = new Uint8Array(rawLength);
  for (let i = 0; i < rawLength; ++i) {
    // 将解码后的逐个字符转换成Unicode序号，放入Unit8Array数组
    uInt8Array[i] = raw.charCodeAt(i);
  }
  // 通过Blob将Uint8Array数组转换成pdf类型的文件对象
  return new Blob([uInt8Array], { type: "application/pdf" });
}
</script>
<style scoped lang="scss">
::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #F3F8FF !important;
    border: 0px solid #6BA7FF !important;
    color: #2666fb !important;
  }
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

.zjsx {
  display: inline-block;
  width: 5px;
  height: 50px;
  border-left: 1px solid #ccc;
  border-right: 1px solid #ccc;
}

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
::v-deep {
  // .el-upload-list{
  //    display: flex;
  // }
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}

.filecont {
  height: 100%;
}
::v-deep .el-dialog .el-dialog__body {
  height: 100% !important;
  //padding: 10px !important;
}
</style>
