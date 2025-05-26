<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
        <el-form-item label="告警名称" prop="name">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              placeholder="请输入告警名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="工况" prop="operateCond" >
          <el-select v-model="operateConds" multiple placeholder="请选择工况" style="width: 250px">
            <el-option label="调水" :value="0"></el-option>
            <el-option label="发电" :value="1"></el-option>
            <el-option label="其他" :value="2"></el-option>
          </el-select>
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dm:dmAlarmConfig:config:add']"
                       @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['dm:dmAlarmConfig:config:edit']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['dm:dmAlarmConfig:config:remove']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="info" plain  @click="handleImport" v-hasPermi="['dm:dmAlarmConfig:config:export']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="warning" plain @click="handleExport" v-hasPermi="['dm:dmAlarmConfig:config:export']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-download-line mr5"></i>导出-->
<!--            </el-button>-->
<!--          </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe height="590px" v-loading="loading" :data="configList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
<!--        <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="序号" align="center" width="80">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
<!--        <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
<!--        <el-table-column v-if="getColumnVisibility(1)" label="工作区id" align="center" prop="workspaceId">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.workspaceId || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column v-if="getColumnVisibility(2)" label="告警名称" align="center" prop="name" width="130px">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>
<!--        <el-table-column v-if="getColumnVisibility(3)" label="工况编码" align="center" prop="condCode" width="150px">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.condCode || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column v-if="getColumnVisibility(4)" label="工况" align="center" prop="operateCond" width="150px">
          <template #default="scope">
            <span v-if="scope.row.operateCond">
              {{ scope.row.operateCond.split(',').map(item => operateCondLabels[item]).join(', ') }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="告警类型" align="center" prop="type">
          <template #default="scope">
            {{ scope.row.type === 0 ? '水位告警' : scope.row.type === 1 ? '设备告警' : '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="阈值上限" align="center" prop="upper">
          <template #default="scope">
            {{ scope.row.upper || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="阈值下限" align="center" prop="lower">
          <template #default="scope">
            {{ scope.row.lower || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(8)" label="告警内容" align="center" prop="content">
          <template #default="scope">
            {{ scope.row.content || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(9)" label="预案" align="center" prop="plan">
          <template #default="scope">
            {{ scope.row.plan || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(10)" label="预案简称" align="center" prop="planShort">
          <template #default="scope">
            {{ scope.row.planShort || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(13)" label="创建人" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(15)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(19)" label="备注" align="center" prop="remark">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                       v-hasPermi="['dm:dmAlarmConfig:config:edit']">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                       v-hasPermi="['dm:dmAlarmConfig:config:remove']">删除</el-button>
            <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                       v-hasPermi="['dm:dmAlarmConfig:config:edit']">详情</el-button>
<!--            <el-button link type="primary" icon="view" @click="routeTo('/dm/dmAlarmConfig/configDetail',scope.row)"-->
<!--                       v-hasPermi="['dm:dmAlarmConfig:config:edit']">复杂详情</el-button>-->
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

    <!-- 添加或修改告警配置对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="configRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="告警名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入告警名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="告警类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择告警类型" clearable>
                <el-option label="水位告警" :value="0"></el-option>
                <el-option label="设备告警" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工况" prop="operateCond">
              <el-select v-model="form.operateCond" multiple placeholder="请选择工况">
                <el-option label="调水" :value="0"></el-option>
                <el-option label="发电" :value="1"></el-option>
                <el-option label="其他" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item label="工况编码" prop="condCode">-->
<!--              <el-input v-model="form.condCode" placeholder="请输入工况编码"  disabled />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="阈值上限" prop="upper">
              <el-input v-model="form.upper" placeholder="请输入阈值上限" @input="removeNonNumeric('upper')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值下限" prop="lower">
              <el-input v-model="form.lower" placeholder="请输入阈值下限" @input="removeNonNumeric('lower')"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预案简称" prop="planShort">
              <el-input v-model="form.planShort" placeholder="请输入预案简称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预案" prop="plan">
              <el-input v-model="form.plan" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="告警内容">
              <el-input v-model="form.content" type="textarea" placeholder="请输入内容" />
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

    <!-- 告警配置详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="configRef" :model="form"  label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="告警名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="告警类型" prop="type">
              <div>
                {{ form.type === 0 ? '水位告警' : form.type === 1 ? '设备告警' : '-' }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
<!--          <el-col :span="12">-->
<!--            <el-form-item label="工况编码" prop="condCode">-->
<!--              <div>-->
<!--                {{ form.condCode }}-->
<!--              </div>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
          <el-col :span="12">
            <el-form-item label="工况" prop="operateCond">
              <div>
                {{ form.operateCond.split(',').map(item => operateCondLabels[item]).join(', ') }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="阈值上限" prop="upper">
              <div>
                {{ form.upper }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值下限" prop="lower">
              <div>
                {{ form.lower }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预案简称" prop="planShort">
              <div>
                {{ form.planShort }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预案" prop="plan">
              <div>
                {{ form.plan }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="告警内容">
              <div v-html="form.content" ></div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的告警配置数据
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

<script setup name="Config">
import { listConfig, getConfig, delConfig, addConfig, updateConfig } from "@/api/dm/dmAlarmConfig/config";
import {getToken} from "@/utils/auth.js";

const { proxy } = getCurrentInstance();

const configList = ref([]);

const operateCondLabels = {
  0: '调水',
  1: '发电',
  2: '其他'
};

// 列显隐信息
const columns = ref([
  // { key: 1, label: "工作区id", visible: true },
  { key: 2, label: "告警名称", visible: true },
  // { key: 3, label: "工况编码", visible: true },
  { key: 4, label: "工况", visible: true },
  { key: 5, label: "告警类型", visible: true },
  { key: 6, label: "阈值上限", visible: true },
  { key: 7, label: "阈值下限", visible: true },
  { key: 8, label: "告警内容", visible: true },
  { key: 9, label: "预案", visible: true },
  { key: 10, label: "预案简称", visible: true },
  { key: 13, label: "创建人", visible: true },
  { key: 15, label: "创建时间", visible: true },
  { key: 19, label: "备注", visible: true }
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const removeNonNumeric = (field) => {
  // 1. 如果以小数点开头，则移除小数点
  if (form.value[field].startsWith('.')) {
    form.value[field] = form.value[field].slice(1);
  }
  // 2. 只保留数字和一个小数点
  form.value[field] = form.value[field].replace(/[^0-9.]/g, '');
  // 3. 确保只能有一个小数点
  form.value[field] = form.value[field].replace(/(\..*)\./g, '\$1');
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
const operateConds = ref([]);

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
  url: import.meta.env.VITE_APP_BASE_API + "/dm/config/importData"
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    name: null,
    condCode: null,
    operateCond: null,
    type: null,
    upper: null,
    lower: null,
    content: null,
    plan: null,
    planShort: null,
    createTime: null,
  },
  rules: {
    workspaceId: [{ required: true, message: "工作区id不能为空", trigger: "blur" }],
    name: [{ required: true, message: "告警名称不能为空", trigger: "blur" }],
    operateCond: [{ required: true, message: "工况不能为空", trigger: "blur" }],
    type: [{ required: true, message: "告警类型不能为空", trigger: "change" }],
    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询告警配置列表 */
function getList() {
  loading.value = true;
  listConfig(queryParams.value).then(response => {
    configList.value = response.data.rows;
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
    name: null,
    condCode: null,
    operateCond: null,
    type: null,
    upper: null,
    lower: null,
    content: "${告警名称}",
    plan: null,
    planShort: null,
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
  proxy.resetForm("configRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  if (operateConds.value){
    queryParams.value.operateCond = operateConds.value.join(',')
  }
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  operateConds.value = [];
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
  title.value = "添加告警配置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getConfig(_id).then(response => {
    form.value = response.data;
    form.value.operateCond = form.value.operateCond.split(',').map(Number);
    open.value = true;
    title.value = "修改告警配置";
  });
}


/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value
  getConfig(_id).then(response => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "告警配置详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["configRef"].validate(valid => {
    if (valid) {
      if (form.value.operateCond){
        form.value.operateCond = form.value.operateCond.join(',')
        form.value.condCode =  form.value.operateCond
      }
      if (form.value.id != null) {
        updateConfig(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addConfig(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除告警配置编号为"' + _ids + '"的数据项？').then(function() {
    return delConfig(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('dm/config/export', {
    ...queryParams.value
  }, `config_${new Date().getTime()}.xlsx`)
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "告警配置导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `config_template_${new Date().getTime()}.xlsx`)
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

getList();
</script>
