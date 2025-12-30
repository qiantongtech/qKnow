<template>
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="kg/ext/structTask.list" />
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="75px"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item label="任务名称" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入任务名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="任务状态" prop="status">
          <el-select
            v-model="queryParams.status"
            style="width: 200px"
            placeholder="任务状态"
            clearable
          >
            <el-option
              v-for="dict in ext_task_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="发布状态" prop="publishStatus">
          <el-select
            v-model="queryParams.publishStatus"
            style="width: 200px"
            placeholder="发布状态"
            clearable
          >
            <el-option
              v-for="dict in publish_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="创建时间" prop="reportTime">
          <el-date-picker
            class="el-form-input-width"
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :clearable="false"
          ></el-date-picker>
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
              @click="routeTo(`/ext/addStructTask`)"
              v-hasPermi="['ext:extStructTask:struct:add']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
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
        height="590px"
        v-loading="loading"
        :data="extStructList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column
          v-if="getColumnVisibility(1)"
          label="ID"
          align="center"
          prop="id"
        />
        <el-table-column
          v-if="getColumnVisibility(2)"
          label="任务名称"
          prop="name"
          width="200"
          show-overflow-tooltip
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(3)"
          label="更新类型"
          align="center"
          prop="updateType"
        >
          <template #default="scope">
            <div>
              <dict-tag
                :options="ext_update_type"
                :value="scope.row.updateType"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(4)"
          label="更新频率"
          align="center"
          width="120"
          prop="updateFrequency"
        >
          <template #default="scope">
            {{ scope.row.updateFrequency || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(5)"
          label="任务状态"
          align="center"
          prop="status"
        >
          <template #default="scope">
            <div>
              <dict-tag :options="ext_task_status" :value="scope.row.status" />
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(6)"
          label="发布状态"
          align="center"
          prop="publishStatus"
        >
          <template #default="scope">
            <div>
              <dict-tag
                :options="publish_status"
                :value="scope.row.publishStatus"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(7)"
          label="更新状态"
          align="center"
          width="100"
          prop="updateStatus"
        >
          <template #default="scope">
            <el-switch
              v-model="scope.row.updateStatus"
              :active-value="0"
              :inactive-value="1"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(8)"
          label="发布人"
          align="center"
          prop="publishBy"
        >
          <template #default="scope">
            {{ scope.row.publishBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(9)"
          label="发布时间"
          align="center"
          prop="publishTime"
          sortable="custom"
          :sort-orders="['descending', 'ascending']"
          width="180"
        >
          <template #default="scope">
            <span>{{
              scope.row.publishTime == null
                ? "-"
                : parseTime(scope.row.publishTime, "{y}-{m}-{d} {h}:{i}:{s}")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(18)"
          label="备注"
          align="center"
          prop="remark"
          show-overflow-tooltip
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(12)"
          label="创建人"
          align="center"
          prop="createBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(14)"
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          sortable="custom"
          :sort-orders="['descending', 'ascending']"
        >
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}")
            }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          width="280"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              v-if="scope.row.publishStatus != 1"
              icon="Edit"
              @click="extraction(scope.row)"
              v-hasPermi="['ext:extStructTask:struct:edit']"
              >执行
            </el-button>
            <el-button
              link
              type="primary"
              v-if="scope.row.status == 2"
              icon="view"
              @click="handleResult(scope.row)"
              >抽取结果
            </el-button>
            <template
              v-if="scope.row.publishStatus != 1 && scope.row.status == 2"
            >
              <el-popover
                placement="bottom"
                :width="150"
                trigger="click"
                v-if="scope.row.publishStatus != 1"
              >
                <template #reference>
                  <el-button type="primary" icon="ArrowDown" link @click.stop
                    >更多</el-button
                  >
                </template>
                <div class="card-button-group">
                  <el-button
                    link
                    type="primary"
                    style="margin-left: 12px"
                    v-if="scope.row.status == 2"
                    icon="VideoPlay"
                    @click="handleRun(scope.row)"
                    >执行一次
                  </el-button>
                  <el-button
                    link
                    type="primary"
                    v-if="scope.row.publishStatus != 1"
                    icon="Edit"
                    @click="routeTo(`/ext/editStructTask`, scope.row)"
                    v-hasPermi="['ext:extStructTask:struct:edit']"
                    >编辑
                  </el-button>
                  <el-button
                    link
                    type="danger"
                    v-if="scope.row.publishStatus != 1"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['ext:extStructTask:struct:remove']"
                    >删除
                  </el-button>
                </div>
              </el-popover>
            </template>
            <template v-else>
              <el-button
                link
                type="primary"
                style="margin-left: 12px"
                v-if="scope.row.status == 2"
                icon="VideoPlay"
                @click="handleRun(scope.row)"
                >执行一次
              </el-button>
              <el-button
                link
                type="primary"
                v-if="scope.row.publishStatus != 1"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['ext:extStructTask:struct:edit']"
                >编辑
              </el-button>
              <el-button
                link
                type="danger"
                v-if="scope.row.publishStatus != 1"
                icon="Delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['ext:extStructTask:struct:remove']"
                >删除
              </el-button>
            </template>
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
    <!-- 引入子组件 -->
    <StructTask class="custom-struck" ref="extStructRef" @ok="struckOk" />
  </div>
</template>
  
  <script setup name="ExtStruct">
import {
  listExtStruct,
  getExtStruct,
  delExtStruct,
  addExtStruct,
  updateExtStruct,
  extExecuteExtraction,
  runStructTask,
} from "@/api/ext/extStructTask/extStruct";
import { getToken } from "@/utils/auth.js";
//新增和修改任务页面
import StructTask from "./structTask.vue";
import { ref, watch } from "vue";
const { proxy } = getCurrentInstance();

const { ext_task_status, publish_status, ext_update_type } = proxy.useDict(
  "ext_task_status",
  "publish_status",
  "ext_update_type"
);
const extStructList = ref([]);
const structTaskShow = ref(true);
const taskVisible = ref(true);

// 列显隐信息
const columns = ref([
  { key: 1, label: "ID", visible: true },
  { key: 2, label: "任务名称", visible: true },
  { key: 3, label: "更新类型", visible: true },
  { key: 4, label: "更新频率", visible: true },
  { key: 5, label: "任务状态", visible: true },
  { key: 6, label: "发布状态", visible: true },
  { key: 7, label: "定时更新状态", visible: true },
  { key: 8, label: "发布人", visible: true },
  // {key: 6, label: "发布人id", visible: true},
  { key: 9, label: "发布时间", visible: true },
  // {key: 8, label: "数据源id", visible: true},
  // {key: 9, label: "数据源", visible: true},
  { key: 12, label: "创建人", visible: true },
  { key: 14, label: "创建时间", visible: true },
  { key: 18, label: "备注", visible: true },
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
  url: import.meta.env.VITE_APP_BASE_API + "/ext/extStruct/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    name: null,
    status: null,
    publishStatus: null,
    publishTime: null,
    publisherId: null,
    publishBy: null,
    updateType: null,
    updateFrequency: null,
    updateStatus: null,
    datasourceId: null,
    datasourceName: null,
    createTime: null,
    params: {
      beginCreateTime: null,
      endCreateTime: null,
    },
  },
  rules: {
    workspaceId: [
      { required: true, message: "工作区id不能为空", trigger: "blur" },
    ],
    name: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
    status: [
      { required: true, message: "任务状态不能为空", trigger: "change" },
    ],
    publishStatus: [
      { required: true, message: "发布状态不能为空", trigger: "change" },
    ],
    publishTime: [
      { required: true, message: "发布时间不能为空", trigger: "blur" },
    ],
    publisherId: [
      { required: true, message: "发布人id不能为空", trigger: "blur" },
    ],
    updateType: [
      { required: true, message: "更新类型不能为空", trigger: "blur" },
    ],
    updateFrequency: [
      { required: true, message: "更新频率不能为空", trigger: "blur" },
    ],
    datasourceId: [
      { required: true, message: "数据源id不能为空", trigger: "blur" },
    ],
    datasourceName: [
      { required: true, message: "数据源不能为空", trigger: "blur" },
    ],
    validFlag: [
      { required: true, message: "是否有效不能为空", trigger: "blur" },
    ],
    delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
    createTime: [
      { required: true, message: "创建时间不能为空", trigger: "blur" },
    ],
    updateTime: [
      { required: true, message: "更新时间不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

const daterangeCreateTime = ref([]);

/** 查询结构化抽取任务列表 */
function getList() {
  loading.value = true;

  if (
    null != daterangeCreateTime.value &&
    daterangeCreateTime.value.length == 2
  ) {
    const date = daterangeCreateTime.value;
    const endDate = new Date(date[1]);
    endDate.setDate(endDate.getDate() + 1);
    queryParams.value.params.beginCreateTime = date[0];
    queryParams.value.params.endCreateTime = endDate
      .toISOString()
      .split("T")[0];
    console.log("------queryParams.params-----", queryParams.value);
  }

  listExtStruct(queryParams.value).then((response) => {
    extStructList.value = response.data.rows;
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
    status: null,
    publishStatus: null,
    publishTime: null,
    publisherId: null,
    publishBy: null,
    updateType: 0,
    updateFrequency: null,
    updateStatus: null,
    datasourceId: null,
    datasourceName: null,
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
  proxy.resetForm("extStructRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  daterangeCreateTime.value = [];
  queryParams.value.params = {};
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

// 任务状态修改
function handleStatusChange(row) {
  // 保存原始状态
  const original = row.updateStatus === 0 ? 1 : 0;
  proxy.$modal.msgWarning("当前可用资源不足，暂不支持操作");
  // 恢复原状态
  row.updateStatus = original;

  // let text = row.updateStatus === 0 ? "启用" : "停用";
  // proxy.$modal.confirm('确认要' + text + '"' + row.name + '"的定时更新任务吗?').then(function () {
  //     return updateExtStruct(row);
  // }).then(() => {
  //     proxy.$modal.msgSuccess(text + "成功");
  //     getList();
  // }).catch(function () {
  //     row.updateStatus = row.updateStatus === 0 ? 1 : 0;
  // });
}

/* 立即执行一次 */
function handleRun(row) {
  proxy.$modal
    .confirm('确认要立即执行一次"' + row.name + '"任务吗?')
    .then(function () {
      return runStructTask(row.id, row.updateType);
    })
    .then(() => {
      proxy.$modal.msgWarning("任务执行中");
      getList();
    })
    .catch(() => {});
}

/** 新增按钮操作 */
function handleAdd() {
  proxy.$refs["extStructRef"].show({
    title: "新增结构化抽取任务",
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  if (row.status == 1) {
    proxy.$modal.msgWarning("任务执行中");
    return;
  }
  proxy.$refs["extStructRef"].show({
    id: row.id,
    title: "编辑结构化抽取任务",
  });
  // router.push({
  //     path: '/ext/editStructTask',
  //     query: {
  //         id: row.id
  //     }
  // });
}

//执行抽取
const extraction = (row) => {
  if (row.status == 1) {
    proxy.$modal.msgWarning("任务执行中");
    return;
  }
  proxy.$modal
    .confirm('是否确认执行任务名称为"' + row.name + '"的数据项？')
    .then(function () {})
    .then(() => {
      extExecuteExtraction({ id: row.id }).then((res) => {
        if (res && res.code == 200) {
          proxy.$modal.msgSuccess("操作成功,执行中");
          getList();
        }
      });
    })
    .catch(() => {});
};

//新增或者编辑成功
const struckOk = (val) => {
  console.log("--------aval--", val);
  proxy.$modal.msgSuccess(val);
  getList();
};

/** 删除按钮操作 */
function handleDelete(row) {
  if (row.status == 1) {
    proxy.$modal.msgWarning("任务执行中");
    return;
  }
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除任务名称为"' + row.name + '"的数据项？')
    .then(function () {
      return delExtStruct(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

// /** 详情按钮操作 */
// function handleDetail(row) {
//     reset();
//     const _id = row.id || ids.value
//     getExtStruct(_id).then(response => {
//         form.value = response.data;
//         openDetail.value = true;
//         title.value = "结构化抽取任务详情";
//     });
// }
//
// /** 提交按钮 */
// function submitForm() {
//     proxy.$refs["extStructRef"].validate(valid => {
//         if (valid) {
//             if (form.value.id != null) {
//                 updateExtStruct(form.value).then(response => {
//                     proxy.$modal.msgSuccess("修改成功");
//                     open.value = false;
//                     getList();
//                 }).catch(error => {
//                 });
//             } else {
//                 addExtStruct(form.value).then(response => {
//                     proxy.$modal.msgSuccess("新增成功");
//                     open.value = false;
//                     getList();
//                 }).catch(error => {
//                 });
//             }
//         }
//     });
// }
// /** 导出按钮操作 */
// function handleExport() {
//     proxy.download('ext/extStruct/export', {
//         ...queryParams.value
//     }, `extStruct_${new Date().getTime()}.xlsx`)
// }
//
// /** ---------------- 导入相关操作 -----------------**/
// /** 导入按钮操作 */
// function handleImport() {
//     upload.title = "结构化抽取任务导入";
//     upload.open = true;
// }
//
// /** 下载模板操作 */
// function importTemplate() {
//     proxy.download("system/user/importTemplate", {}, `extStruct_template_${new Date().getTime()}.xlsx`)
// }
//
// /** 提交上传文件 */
// function submitFileForm() {
//     proxy.$refs["uploadRef"].submit();
// };
//
// /**文件上传中处理 */
// const handleFileUploadProgress = (event, file, fileList) => {
//     upload.isUploading = true;
// };
//
// /** 文件上传成功处理 */
// const handleFileSuccess = (response, file, fileList) => {
//     upload.open = false;
//     upload.isUploading = false;
//     proxy.$refs["uploadRef"].handleRemove(file);
//     proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
//     getList();
// };
//
// /** ---------------------------------**/
//
// function routeTo(link, row) {
//     if (link !== "" && link.indexOf("http") !== -1) {
//         window.location.href = link;
//         return
//     }
//     if (link !== "") {
//         if (link === router.currentRoute.value.path) {
//             window.location.reload();
//         } else {
//             router.push({
//                 path: link,
//                 query: {
//                     id: row.id
//                 }
//             });
//         }
//     }
// }

// 抽取结果
function handleResult(row) {
  router.push({
    path: `/ext/structuredResult`,
    query: {
      id: row.id,
      name: row.name,
      pageType: "1",
      pageNum: queryParams.value.pageNum,
      pageSize: queryParams.value.pageSize,
    },
  });
}

function routeTo(link, row) {
  console.log("--------aval--", link);
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      if (row) {
        router.push({
          path: link,
          query: {
            taskId: row.id,
          },
        });
      } else {
        router.push({
          path: link,
          query: {},
        });
      }
    }
  }
}

getList();
</script>
  <style scoped lang="scss">
:deep(.custom-struck .el-dialog__body) {
  height: 700px;
}
.card-button-group {
  width: 100px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
  