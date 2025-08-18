<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
        <el-form-item label="测点名称" prop="name">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              placeholder="请输入测点名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.deviceName"
              placeholder="请输入设备名称"
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dm:dmMeasurePoint:point:add']"
                       @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
<!--          <el-col :span="1.5">-->
<!--            <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['dm:dmMeasurePoint:point:edit']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['dm:dmMeasurePoint:point:remove']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="info" plain  @click="handleImport" v-hasPermi="['dm:dmMeasurePoint:point:export']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入-->
<!--            </el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button type="warning" plain @click="handleExport" v-hasPermi="['dm:dmMeasurePoint:point:export']"-->
<!--                       @mousedown="(e) => e.preventDefault()">-->
<!--              <i class="iconfont-mini icon-download-line mr5"></i>导出-->
<!--            </el-button>-->
<!--          </el-col>-->
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe height="590px" v-loading="loading" :data="pointList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
<!--        <el-table-column type="selection" width="55" align="center" />-->
<!--        <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
<!--        <el-table-column v-if="getColumnVisibility(1)" label="工作区id" align="center" prop="workspaceId">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.workspaceId || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="序号" align="center" width="80">
          <template #default="{ $index }">
            {{ $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="测点名称" align="center" prop="name">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" label="测点号" align="center" prop="code">
          <template #default="scope">
            {{ scope.row.code || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="设备名称" align="center" prop="deviceName">
          <template #default="scope">
            {{ scope.row.deviceName || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="测点类型" align="center" prop="type">
          <template #default="scope">
            {{ scope.row.type === 0 ? '开关量' : scope.row.type === 1 ? '数值量' : '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="设备key" align="center" prop="deviceKey">
          <template #default="scope">
            {{ scope.row.deviceKey || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="前缀" align="center" prop="prefix">
          <template #default="scope">
            {{ scope.row.prefix || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(8)" label="是否实时获取" align="center" prop="realtimeFlag">
          <template #default="scope">
            {{ scope.row.realtimeFlag === 0 ? '否' : scope.row.realtimeFlag === 1 ? '是' : '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(9)" label="同步频率(分钟)" align="center" prop="frequency" width="130">
          <template #default="scope">
            {{ scope.row.frequency || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(10)" label="单位" align="center" prop="unit">
          <template #default="scope">
            {{ scope.row.unit || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(11)" label="是否为故障诊断" align="center" prop="failureFlag" width="130">
          <template #default="scope">
            {{ scope.row.failureFlag === 0 ? '否' : scope.row.failureFlag === 1 ? '是' : '-' }}
          </template>
        </el-table-column>
<!--        <el-table-column v-if="getColumnVisibility(14)" label="创建人" align="center" prop="createBy">-->
<!--          <template #default="scope">-->
<!--            {{ scope.row.createBy || '-' }}-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column v-if="getColumnVisibility(16)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">-->
<!--          <template #default="scope">-->
<!--            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column v-if="getColumnVisibility(20)" label="备注" align="center" prop="remark">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                       v-hasPermi="['dm:dmMeasurePoint:point:edit']">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                       v-hasPermi="['dm:dmMeasurePoint:point:remove']">删除</el-button>
            <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                       v-hasPermi="['dm:dmMeasurePoint:point:edit']">详情</el-button>
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

    <!-- 添加或修改物联网测点对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable class="custom-dialog">
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="pointRef" :model="form" :rules="rules" label-width="110px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测点名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入测点名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测点号" prop="code">
              <el-input v-model="form.code" placeholder="请输入测点号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测点类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio :value="0">开关量</el-radio>
                <el-radio :value="1">数值量</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备key" prop="deviceKey">
              <el-input v-model="form.deviceKey" placeholder="请输入设备key" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="前缀" prop="prefix">
              <el-input v-model="form.prefix" placeholder="请输入前缀" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同步频率" prop="frequency">
              <el-input v-model="form.frequency" placeholder="请输入同步频率">
                <template #suffix>分钟</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否实时获取" prop="realtimeFlag">
              <el-radio-group v-model="form.realtimeFlag">
                <el-radio :value="0">否</el-radio>
                <el-radio :value="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否为故障诊断" prop="failureFlag">
              <el-radio-group v-model="form.failureFlag">
                <el-radio :value="0">否</el-radio>
                <el-radio :value="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
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

    <!-- 物联网测点详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="pointRef" :model="form"  label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测点名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="测点号" prop="code">
              <div>
                {{ form.code }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <div>
                {{ form.deviceName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备key" prop="deviceKey">
              <div>
                {{ form.deviceKey }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测点类型" prop="type">
              <div>
                {{ form.type === 0 ? '开关量' : form.type === 1 ? '数值量' : '-' }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="前缀" prop="prefix">
              <div>
                {{ form.prefix }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否实时获取" prop="realtimeFlag">
              <div>
                {{ form.realtimeFlag === 0 ? '否' : form.realtimeFlag === 1 ? '是' : '-' }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同步频率" prop="frequency">
              <div>
                {{ form.frequency }}分钟
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <div>
                {{ form.unit }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否为故障诊断" prop="failureFlag">
              <div>
                {{ form.failureFlag === 0 ? '否' : form.failureFlag === 1 ? '是' : '-' }}
              </div>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的物联网测点数据
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

<script setup name="Point">
import { listPoint, getPoint, delPoint, addPoint, updatePoint } from "@/api/dm/dmMeasurePoint/point";
import {getToken} from "@/utils/auth.js";
import moment from "moment/moment.js";

const { proxy } = getCurrentInstance();

const pointList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "工作区id", visible: true },
  { key: 2, label: "测点名称", visible: true },
  { key: 3, label: "测点号", visible: true },
  { key: 4, label: "设备名称", visible: true },
  { key: 5, label: "测点类型", visible: true },
  { key: 6, label: "设备key", visible: true },
  { key: 7, label: "前缀", visible: true },
  { key: 8, label: "是否实时获取", visible: true },
  { key: 9, label: "同步频率（分钟）", visible: true },
  { key: 10, label: "单位", visible: true },
  { key: 11, label: "是否为故障诊断", visible: true },
  { key: 14, label: "创建人", visible: true },
  { key: 16, label: "创建时间", visible: true },
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
const router = useRouter();

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
  url: import.meta.env.VITE_APP_BASE_API + "/dm/point/importData"
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    name: null,
    code: null,
    deviceName: null,
    type: null,
    deviceKey: null,
    prefix: null,
    realtimeFlag: null,
    frequency: null,
    unit: null,
    failureFlag: null,
    createTime: null,
  },
  rules: {
    workspaceId: [{ required: true, message: "工作区id不能为空", trigger: "blur" }],
    name: [{ required: true, message: "测点名称不能为空", trigger: "blur" }],
    code: [{ required: true, message: "测点号不能为空", trigger: "blur" }],
    deviceName: [{ required: true, message: "设备名称不能为空", trigger: "blur" }],
    type: [{ required: true, message: "测点类型不能为空", trigger: "change" }],
    deviceKey: [{ required: true, message: "设备key不能为空", trigger: "blur" }],
    validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
    prefix: [{ required: true, message: "前缀不能为空", trigger: "blur" }],
    unit: [{ required: true, message: "单位不能为空", trigger: "blur" }],
    frequency: [{ required: true, message: "同步频率不能为空", trigger: "blur" }],
    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
    updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询物联网测点列表 */
function getList() {
  loading.value = true;
  listPoint(queryParams.value).then(response => {
    pointList.value = response.data.rows;
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
    code: null,
    deviceName: null,
    type: 0,
    deviceKey: null,
    prefix: null,
    realtimeFlag: 0,
    frequency: null,
    unit: null,
    failureFlag: 0,
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
  proxy.resetForm("pointRef");
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
  title.value = "添加物联网测点";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getPoint(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改物联网测点";
  });
}


/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value
  getPoint(_id).then(response => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "物联网测点详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pointRef"].validate(valid => {
    if (valid) {
      if (form.value.createTime != null) {
        form.value.createTime = moment(form.value.createTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.updateTime != null) {
        form.value.updateTime = moment(form.value.updateTime).format('YYYY-MM-DD HH:mm:ss');
      }
      if (form.value.id != null) {
        updatePoint(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addPoint(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除物联网测点编号为"' + _ids + '"的数据项？').then(function() {
    return delPoint(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('dm/point/export', {
    ...queryParams.value
  }, `point_${new Date().getTime()}.xlsx`)
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "物联网测点导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {
  }, `point_template_${new Date().getTime()}.xlsx`)
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
<style scoped lang="scss">
::v-deep(.custom-dialog) {
  .el-dialog__body {
    padding: 20px 60px 20px 20px !important;
  }
}
</style>
