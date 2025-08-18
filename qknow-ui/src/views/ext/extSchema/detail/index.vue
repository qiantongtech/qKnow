<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="95px" v-show="showSearch" @submit.prevent>
<!--        <el-form-item label="概念名称" prop="schemaName">-->
<!--          <el-input-->
<!--              class="el-form-input-width"-->
<!--              v-model="queryParams.schemaName"-->
<!--              placeholder="请输入概念名称"-->
<!--              clearable-->
<!--              @keyup.enter="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="属性名" prop="name">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              placeholder="请输入属性名"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="属性代码" prop="nameCode">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.nameCode"
              placeholder="请输入属性代码"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
<!--        <el-form-item label="是否必填" prop="requireFlag">-->
<!--          <el-input-->
<!--              class="el-form-input-width"-->
<!--              v-model="queryParams.requireFlag"-->
<!--              placeholder="请输入是否必填"-->
<!--              clearable-->
<!--              @keyup.enter="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="单/多值" prop="multipleFlag">-->
<!--          <el-input-->
<!--              class="el-form-input-width"-->
<!--              v-model="queryParams.multipleFlag"-->
<!--              placeholder="请输入单/多值"-->
<!--              clearable-->
<!--              @keyup.enter="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="最小值" prop="minValue">-->
<!--          <el-input-->
<!--              class="el-form-input-width"-->
<!--              v-model="queryParams.minValue"-->
<!--              placeholder="请输入最小值"-->
<!--              clearable-->
<!--              @keyup.enter="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="最大值" prop="maxValue">-->
<!--          <el-input-->
<!--              class="el-form-input-width"-->
<!--              v-model="queryParams.maxValue"-->
<!--              placeholder="请输入最大值"-->
<!--              clearable-->
<!--              @keyup.enter="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="创建时间" prop="createTime">-->
<!--          <el-date-picker class="el-form-input-width"-->
<!--                          clearable-->
<!--                          v-model="queryParams.createTime"-->
<!--                          type="date"-->
<!--                          value-format="YYYY-MM-DD"-->
<!--                          placeholder="请选择创建时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

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
            <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
            <el-button icon="Back" @click="close()">返回</el-button>
          </el-col>
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['ext:extSchemaAttribute:attribute:edit']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['ext:extSchemaAttribute:attribute:remove']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="info" plain  @click="handleImport" v-hasPermi="['ext:extSchemaAttribute:attribute:export']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="warning" plain @click="handleExport" v-hasPermi="['ext:extSchemaAttribute:attribute:export']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-download-line mr5"></i>导出-->
<!--            </el-button>-->
<!--          </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe height="590px" v-loading="loading" :data="attributeList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
<!--        <el-table-column type="selection" width="55" align="center" />-->
<!--        <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
<!--        <el-table-column v-if="getColumnVisibility(1)" label="工作区id" align="center" prop="workspaceId">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.workspaceId || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column v-if="getColumnVisibility(2)" label="概念id" align="center" prop="schemaId">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.schemaId || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column v-if="getColumnVisibility(3)" label="概念名称" align="center" prop="schemaName">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.schemaName || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column v-if="getColumnVisibility(4)" label="属性名" align="center" prop="name">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="属性代码" align="center" prop="nameCode">
          <template #default="scope">
            {{ scope.row.nameCode || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="是否必填" align="center" prop="requireFlag">
          <template #default="scope">
            {{ scope.row.requireFlag === 1 ? '是' : (scope.row.requireFlag === 0 ? '否' : '-') }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="数据类型" align="center" prop="dataType">
          <template #default="scope">
            <dict-tag :options="ext_data_type" :value="scope.row.dataType"/>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(8)" label="单/多值" align="center" prop="multipleFlag">
          <template #default="scope">
            {{ scope.row.multipleFlag === 1 ? '多值' : (scope.row.multipleFlag === 0 ? '单值' : '-') }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(9)" label="校验方式" align="center" prop="validateType">
          <template #default="scope">
            <dict-tag :options="ext_data_check" :value="scope.row.validateType"/>
          </template>
        </el-table-column>
        <el-table-column label="校验规则" align="center" prop="id" v-if="getColumnVisibility(10)">
          <template #default="scope">
            <span v-if="scope.row.validateType === 0">不可重复</span>
            <span v-else-if="scope.row.validateType === 2">
              <template v-if="scope.row.minValue != null && scope.row.maxValue != null">
                {{ scope.row.minValue }} < value < {{ scope.row.maxValue }}
              </template>
              <template v-else-if="scope.row.minValue != null">
                value > {{ scope.row.minValue }}
              </template>
              <template v-else-if="scope.row.maxValue != null">
                value < {{ scope.row.maxValue }}
              </template>
            </span>
            <span v-else-if="scope.row.validateType === 1">
              <template v-if="scope.row.maxValue != null">
                长度 < {{ scope.row.maxValue }}
              </template>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(20)" label="备注" align="center" prop="remark">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 添加或修改概念属性对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="attributeRef" :model="form" :rules="rules" label-width="130px" label-position="left" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="属性名" prop="name">
              <el-input v-model="form.name" placeholder="请输入属性名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="属性代码" prop="nameCode">
              <el-input v-model="form.nameCode" placeholder="请输入属性代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否必填" prop="requireFlag">
              <el-radio-group v-model="form.requireFlag">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据类型" prop="dataType">
              <el-select v-model="form.dataType" placeholder="请选择数据类型" clearable>
                <el-option
                        v-for="dict in ext_data_type"
                        :key="dict.value"
                        :label="dict.label"
                        :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单/多值" prop="multipleFlag">
              <el-radio-group v-model="form.multipleFlag">
                <el-radio :label="0">单值</el-radio>
                <el-radio :label="1">多值</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="校验方式" prop="validateType">
              <el-select v-model="form.validateType" placeholder="请选择数据类型" clearable>
                <el-option :label="dict.label" :value="parseInt(dict.value)" v-for="dict in ext_data_check"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.validateType === 2">
          <el-col :span="12">
            <el-form-item label="最小值" prop="minValue">
              <el-input v-model="form.minValue" placeholder="请输入最小值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.validateType === 2 || form.validateType === 1">
          <el-col :span="12">
            <el-form-item label="最大值" prop="maxValue">
              <el-input v-model="form.maxValue" placeholder="请输入最大值" />
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

    <!-- 概念属性详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="attributeRef" :model="form"  label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工作区id" prop="workspaceId">
              <div>
                {{ form.workspaceId }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="概念id" prop="schemaId">
              <div>
                {{ form.schemaId }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="概念名称" prop="schemaName">
              <div>
                {{ form.schemaName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="属性名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="属性名称" prop="nameCode">
              <div>
                {{ form.nameCode }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否必填" prop="requireFlag">
              <div>
                {{ form.requireFlag }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据类型" prop="dataType">
              <div>
                {{ form.dataType }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单/多值" prop="multipleFlag">
              <div>
                {{ form.multipleFlag }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="校验方式" prop="validateType">
              <div>
                {{ form.validateType }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小值" prop="minValue">
              <div>
                {{ form.minValue }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大值" prop="maxValue">
              <div>
                {{ form.maxValue }}
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的概念属性数据
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

<script setup name="Attribute">
import { listAttribute, getAttribute, delAttribute, addAttribute, updateAttribute } from "@/api/ext/extSchemaAttribute/attribute";
import {getToken} from "@/utils/auth.js";
import { useRoute,useRouter } from 'vue-router';
import moment from 'moment';

const { proxy } = getCurrentInstance();
const {ext_data_type,ext_data_check} = proxy.useDict("ext_data_type","ext_data_check");
const attributeList = ref([]);

// 列显隐信息
const columns = ref([
  {key: 4, label: "属性名", visible: true },
  { key: 5, label: "属性代码", visible: true },
  { key: 6, label: "是否必填", visible: true },
  { key: 7, label: "数据类型", visible: true },
  { key: 8, label: "单/多值", visible: true },
  { key: 9, label: "校验方式", visible: true },
  { key: 10, label: "校验规则", visible: true },
  { key: 20, label: "备注", visible: true }
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
let receptionId = ref(0);
let receptionName = ref('');
let receptionPageNum = ref(1);

const route = useRoute();
const router = useRouter();
let id = route.query.id || 1;
let name = route.query.name
let pageNum = route.query.pageNum
watch(
    () => [id, name,pageNum],
    ([newId, newName,newPageNum]) => {
      id = newId || 1;
      name = newName || '';
      pageNum = newPageNum || 1;
      receptionId = id;
      receptionName = name;
      receptionPageNum = pageNum;
    },
    { immediate: true }
);

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
  url: import.meta.env.VITE_APP_BASE_API + "/ext/attribute/importData"
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    schemaId: null,
    schemaName: null,
    name: null,
    nameCode: null,
    requireFlag: null,
    dataType: null,
    multipleFlag: null,
    validateType: null,
    minValue: null,
    maxValue: null,
    createTime: null,
  },
  rules: {
    workspaceId: [{ required: true, message: "工作区id不能为空", trigger: "blur" }],
    schemaId: [{ required: true, message: "概念id不能为空", trigger: "blur" }],
    schemaName: [{ required: true, message: "概念名称不能为空", trigger: "blur" }],
    name: [{ required: true, message: "属性名称不能为空", trigger: "blur" }],
    nameCode: [{ required: true, message: "属性名称不能为空", trigger: "blur" }],
    requireFlag: [{ required: true, message: "是否必填不能为空", trigger: "blur" }],
    dataType: [{ required: true, message: "数据类型不能为空", trigger: "change" }],
    multipleFlag: [{ required: true, message: "单/多值不能为空", trigger: "blur" }],
    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
    // delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    // createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
    // updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    maxValue: [
      {
        validator: (rule, value, callback) => {
          if (form.value.minValue !== null && value !== null && value <= form.value.minValue) {
            callback(new Error('最大值必须大于最小值'));
          } else {
            callback();
          }
        },
        trigger: 'blur',
      },
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询概念属性列表 */
function getList() {
  loading.value = true;
  if (receptionId != null) {
    queryParams.value.schemaId = receptionId
  }
  listAttribute(queryParams.value).then(response => {
    attributeList.value = response.data.rows;
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
    schemaId: null,
    schemaName: null,
    name: null,
    nameCode: null,
    requireFlag: 0,
    dataType: null,
    multipleFlag: 0,
    validateType: null,
    minValue: null,
    maxValue: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("attributeRef");
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
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加概念属性";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getAttribute(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改概念属性";
  });
}


/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value
  getAttribute(_id).then(response => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "概念属性详情";
  });
}

/** 提交按钮 */
function submitForm() {
  if (form.value.validateType === 0 ) {
    form.value.maxValue = null
    form.value.minValue = null
  }
  if (form.value.validateType === 1 ) {
    form.value.minValue = null
  }
  if (form.value.validateType == null) {
    form.value.maxValue = null
    form.value.minValue = null
  }
  proxy.$refs["attributeRef"].validate(valid => {
    if (valid) {
      form.value.schemaId = receptionId
      form.value.schemaName = receptionName
      if (form.value.createTime != null) {
        form.value.createTime = moment(form.value.createTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.updateTime != null) {
        form.value.updateTime = moment(form.value.updateTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.id != null) {
        updateAttribute(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addAttribute(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除概念属性名为"' + name  + '"的数据项？').then(function() {
    return delAttribute(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ext/attribute/export', {
    ...queryParams.value
  }, `attribute_${new Date().getTime()}.xlsx`)
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "概念属性导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `attribute_template_${new Date().getTime()}.xlsx`)
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

function close() {
  const obj = { path: "/ext/schema", query: {pageNum: receptionPageNum } };
  proxy.$tab.closeOpenPage(obj);
}

getList();
</script>
