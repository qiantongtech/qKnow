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
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['kmc:kmcCategory:kmcCategory:add']"
                       @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table v-if="refreshTable"
                height="645px"
                v-loading="loading"
                :data="kmcCategoryList"
                row-key="id"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                :default-expand-all="isExpandAll"
      >
        <el-table-column v-if="getColumnVisibility(3)" label="分类名称" prop="name" :show-overflow-tooltip="true">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="显示顺序" align="center" prop="orderNum">
          <template #default="scope">
            {{ scope.row.orderNum !== null ? scope.row.orderNum : '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(10)" label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(14)" label="备注" align="center" prop="remark">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                       v-hasPermi="['kmc:kmcCategory:kmcCategory:add']">新增</el-button>
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                       v-hasPermi="['kmc:kmcCategory:kmcCategory:edit']">修改</el-button>
            <el-button v-if="isValidData(scope.row.ancestors)" link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                       v-hasPermi="['kmc:kmcCategory:kmcCategory:remove']">删除</el-button>
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

    <!-- 添加或修改知识分类对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="kmcCategoryRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20" v-if="result">
          <el-col :span="24">
            <el-form-item label="上级类型" prop="parentId" >
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
          <el-col :span="12">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入分类名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <el-input-number style="width:100%"
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
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

    <!-- 知识分类详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="kmcCategoryRef" :model="form"  label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工作区id" prop="workspaceId">
              <div>
                {{ form.workspaceId }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="父级id" prop="parentId">
              <div>
                {{ form.parentId }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="orderNum">
              <div>
                {{ form.orderNum }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="更新人id" prop="updaterId">
              <div>
                {{ form.updaterId }}
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的知识分类数据
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

<script setup name="kmcCategory">
import {
  listKmcCategory,
  getKmcCategory,
  delKmcCategory,
  addKmcCategory,
  updateKmcCategory,
  getAllList
} from "@/api/kmc/kmcCategory/kmcCategory";
import {getToken} from "@/utils/auth.js";
import moment from "moment/moment.js";
import {listDocument} from "@/api/kmc/kmcDocument/kmcDocument.js";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const kmcCategoryList = ref([]);

// 列显隐信息
const columns = ref([
  // { key: 1, label: "工作区id", visible: true },
  // { key: 2, label: "父级id", visible: true },
  { key: 3, label: "分类名称", visible: true },
  { key: 4, label: "显示顺序", visible: true },
  // { key: 5, label: "祖级列表", visible: true },
  // { key: 8, label: "创建人", visible: true },
  { key: 10, label: "创建时间", visible: true },
  // { key: 12, label: "更新人id", visible: true },
  { key: 14, label: "备注", visible: true }
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
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
const categoryOptions = ref([]);
const refreshTable = ref(true);
const isExpandAll = ref(true);
const result = ref(true);

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
  url: import.meta.env.VITE_APP_BASE_API + "/kmc/kmcCategory/importData"
});

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
    updaterId: null,
    validFlag: undefined,
  },
  rules: {
    workspaceId: [{ required: true, message: "工作区id不能为空", trigger: "blur" }],
    // parentId: [{ required: true, message: "父级id不能为空", trigger: "blur" }],
    name: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询知识分类列表 */
function getList() {
  loading.value = true;
  getAllList(queryParams.value).then(response => {
    kmcCategoryList.value = proxy.handleTree(response.data, "id");
    loading.value = false;
  })
}
// 判断是不是一级分类
const isValidData = (ancestors) => {
  if (ancestors && ancestors.includes(',')) {
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
    remark: null
  };
  proxy.resetForm("kmcCategoryRef");
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
function handleAdd(row) {
  reset();
  getAllList().then(response => {
    categoryOptions.value = proxy.handleTree(response.data, "id");
  })
  if (row != undefined) {
    form.value.parentId = row.id;
  }

  open.value = true;
  title.value = "添加知识分类";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getAllList().then(response => {
    categoryOptions.value = proxy.handleTree(response.data, "id");
  })
  const _id = row.id || ids.value
  getKmcCategory(_id).then(response => {
    form.value = response.data;
    result.value = form.value.ancestors.includes(',') ? true : false;
    open.value = true;
    title.value = "修改知识分类";
  });
}


/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value
  getKmcCategory(_id).then(response => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "知识分类详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["kmcCategoryRef"].validate(valid => {
    if (valid) {
      if (form.value.createTime != null) {
        form.value.createTime = moment(form.value.createTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.updateTime != null) {
        form.value.updateTime = moment(form.value.updateTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.parentId == null) {
        form.value.parentId = 0
      }
      if (form.value.id != null) {
        updateKmcCategory(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        console.log(form.value)
        addKmcCategory(form.value).then(response => {
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
  const name = row.name;

  // 获取该分类下的文档列表
  listDocument({categoryId: row.id}).then(res => {
    console.log(res.data.total);

    // 判断该分类下是否存在文件
    if (res.data.total !== 0) {
      proxy.$modal.msgWarning('该分类下存有文件，不可删除');
      return;
    } else {
      // 获取该分类下的子分类
      getAllList({parentId: row.id}).then(res => {
        if (res.data.length !== 0) {
          proxy.$modal.msgWarning('该分类下存有子分类，不可删除');
          return;
        } else {
          // 提示确认删除
          proxy.$modal.confirm('是否确认删除知识分类名称为"' + name + '"的数据项？').then(function() {
            return delKmcCategory(_ids);
          }).then(() => {
            getList();
            proxy.$modal.msgSuccess("删除成功");
          }).catch(() => {});
        }
      });
    }
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('kmc/kmcCategory/export', {
    ...queryParams.value
  }, `kmcCategory_${new Date().getTime()}.xlsx`)
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "知识分类导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `kmcCategory_template_${new Date().getTime()}.xlsx`)
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
