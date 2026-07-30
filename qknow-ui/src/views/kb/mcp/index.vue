<!--
Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).

qKnow is licensed under Apache License 2.0 with additional qKnow terms.
You may use qKnow for commercial purposes, but you may not remove, hide,
modify, or replace the qKnow logo, copyright notices, license notices,
or attribution information without a separate commercial license.

White-label use, OEM distribution, rebranding, or presenting qKnow as
another product requires separate commercial authorization from
Jiangsu Qiantong Technology Co., Ltd.

Business License: https://community.qknow.ai/business/policy.html
See the LICENSE file in the project root for full license information.
-->

<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
               v-show="showSearch" @submit.prevent>
        <el-form-item label="名称" prop="name">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              placeholder="请输入名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
              v-model="queryParams.status"
              placeholder="状态"
              clearable
              class="el-form-input-width"
          >
            <el-option
                v-for="dict in statusDict"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
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

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['kb:mcp:mcpconfig:add']"
                       @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
                       icon="Delete" v-hasPermi="['kb:mcp:mcpconfig:remove']"
                       @mousedown="(e) => e.preventDefault()">删除
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="mcpConfigList" @selection-change="handleSelectionChange"
                :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" width="80"
                         sortable="custom" :sort-orders="['descending', 'ascending']"/>
        <el-table-column v-if="getColumnVisibility(1)" label="名称" width="200" align="left" prop="name">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="描述" align="left" prop="description">
          <template #default="scope">
            {{ scope.row.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" label="类型" width="80" align="center" prop="type">
          <template #default="scope">
            <div>
              <dict-tag :options="bot_mcp_type" :value="scope.row.type"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="工具数量" width="80" align="center" prop="toolNum">
          <template #default="scope">
            {{ scope.row.toolNum || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="状态" width="100" align="center" prop="status">
          <template #default="scope">
            <div>
              <el-switch
                  v-model="scope.row.status"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleValidFlagChange(scope.row)"
              ></el-switch>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="创建人" width="100" align="center" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="创建时间" align="center" prop="createTime" width="180"
                         sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(8)" label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="view" v-hasPermi="['kb:mcp:mcpconfig:query']"
                       @click="routeTo('/kb/mcp/mcpConfigDetail',scope.row)">详情
            </el-button>
            <el-button link type="primary" icon="Refresh" v-hasPermi="['kb:mcp:mcpconfig:edit']"
                       @click="handleSync(scope.row)">同步
            </el-button>
            <el-popover placement="bottom" :width="100" trigger="click">
              <template #reference>
                <el-button type="primary" icon="ArrowDown" link @click.stop>更多</el-button>
              </template>
              <div class="card-button-group">
                <el-button link type="primary" icon="Edit" v-hasPermi="['kb:mcp:mcpconfig:edit']"
                           :disabled="scope.row.status === 1" @click="handleUpdate(scope.row)">修改
                </el-button>
                <el-button link type="danger" icon="Delete" v-hasPermi="['kb:mcp:mcpconfig:remove']"
                           :disabled="scope.row.status === 1" @click="handleDelete(scope.row)">删除
                </el-button>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt=""/>
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

    <!-- 添加或修改mcp 配置对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-scrollbar max-height="500px" class="only-y-scroll" :noresize="true">
      <el-form ref="mcpConfigRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入描述" maxlength="500个字符"
                        show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类型" prop="type">
              <el-select
                  v-model="form.type"
                  :filterable="true"
                  style="width: 100%"
                  placeholder="请选择类型"
              >
                <el-option
                    v-for="dict in bot_mcp_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                    :disabled="dict.value !== '1'"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="url" prop="url">
              <el-input v-model="form.url" placeholder="请输入 url"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="url 请求头" prop="url">
              <el-button type="primary" style="margin-bottom: 15px" @click="addItem" plain>
                <i class="iconfont-mini icon-xinzeng mr5"></i>新增</el-button>
              <el-table :data="form.urlHeaderList" style="width: 100%">
                <el-table-column label="键" align="center" prop="name">
                  <template #default="scope">
                    <el-input v-model="scope.row.key" type="text" placeholder="请输入键"/>
                  </template>
                </el-table-column>
                <el-table-column label="值" align="center" prop="url">
                  <template #default="scope">
                    <el-input v-model="scope.row.val" type="text" placeholder="请输入值"/>
                  </template>
                </el-table-column>
                <!-- 操作列 -->
                <el-table-column align="center" width="110px" label="操作">
                  <template #default="scope">
                    <el-button link icon="Delete" size="mini" type="danger" @click="deleteItem(scope.$index, scope.row)" plain>删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="display: none">
          <el-col :span="24">
            <el-form-item label="命令" prop="command">
              <el-input v-model="form.command" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      </el-scrollbar>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="McpConfig">
import {
  listMcpConfig,
  getMcpConfig,
  delMcpConfig,
  addMcpConfig,
  updateMcpConfig,
  updateMcpStatus,
  syncMcpTool
} from "@/api/kb/mcp/mcpConfig";

const {proxy} = getCurrentInstance();

const mcpConfigList = ref([]);
const {bot_mcp_type, bot_mcp_status} = proxy.useDict("bot_mcp_type", "bot_mcp_status");

// 列显隐信息
const columns = ref([
  {key: 0, label: "编号", visible: true},
  {key: 1, label: "名称", visible: true},
  {key: 2, label: "描述", visible: true},
  {key: 3, label: "类型", visible: true},
  {key: 4, label: "工具数量", visible: true},
  {key: 5, label: "状态", visible: true},
  {key: 6, label: "创建人", visible: true},
  {key: 7, label: "创建时间", visible: true},
  {key: 8, label: "操作", visible: true}
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
const defaultSort = ref({prop: "createTime", order: "desc"});
const router = useRouter();
const statusDict = [
  {
    "label": "正常",
    "value": "1",
  },
  {
    "remark": "停用状态",
    "label": "停用",
    "value": "0",
  }
]

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    description: null,
    type: null,
    toolNum: null,
    status: null,
    url: null,
    urlHeader: null,
    command: null,
    createTime: null,
    orderByColumn: 'createTime',
    isAsc: 'descending'
  },
  rules: {
    name: [{required: true, message: "名称不能为空", trigger: "blur"}],
    description: [{required: true, message: "描述不能为空", trigger: "blur"}],
    type: [{required: true, message: "类型不能为空", trigger: "change"}],
    status: [{required: true, message: "状态不能为空", trigger: "change"}],
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询mcp 配置列表 */
function getList() {
  loading.value = true;
  listMcpConfig(queryParams.value).then(response => {
    mcpConfigList.value = response.data.rows;
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
    name: null,
    description: null,
    type: null,
    toolNum: null,
    status: null,
    url: null,
    urlHeader: null,
    urlHeaderList: [],
    command: null,
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
  proxy.resetForm("mcpConfigRef");
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
  title.value = "新增 MCP";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getMcpConfig(_id).then(response => {
    form.value = response.data;
    form.value.type = form.value.type.toString();
    mapStr2list(form.value);
    open.value = true;
    title.value = "修改 MCP";
  });
}

/** 提交按钮 */
function submitForm() {
  list2mapStr(form.value);
  proxy.$refs["mcpConfigRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateMcpConfig(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addMcpConfig(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除 mcp 配置编号为"' + _ids + '"的数据项？').then(function () {
    return delMcpConfig(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 同步按钮操作 */
function handleSync(row) {
  const _ids = row.id || ids.value;
  const mcpId = row.id
  proxy.$modal.confirm('是否确认重新同步"' + mcpId + '"工具列表？')
      .then(function () {
        return syncMcpTool(_ids);
      }).then(() => {
    getList();
    proxy.$modal.msgSuccess("同步成功");
  }).catch(() => {
  });
}

function handleValidFlagChange(row) {
  let newStatus = row.status;
  let text = newStatus === 0 ? "停用" : "启用";
  const mcpId = row.id
  proxy.$modal.confirm('确认要' + text + '"' + row.name + '" MCP 吗?')
      .then(function () {
        return updateMcpStatus(mcpId, newStatus);
      })
      .then(() => {
        proxy.$modal.msgSuccess(text + "成功");
        handleQuery();
      })
      .catch(function () {
        row.status = newStatus === 0 ? 1 : 0;
      });
}

function addItem() {
  form.value.urlHeaderList.push(itemData());
}

const itemData = () => {
  return {
    key: "",
    val: ""
  };
};

// 删除操作
function deleteItem(index, row) {
  if (row.id) {
    proxy.$modal
        .confirm(`是否确认删除该条数据？`)
        .then(() => {
          // 根据索引删除数据
          form.value.urlHeaderList.splice(index, 1);
          ids.value.push(row.id);
          // 最后一条清空不删掉
          form.value.urlHeaderList.length === 0 && form.value.urlHeaderList.push(itemData());
        })
        .catch(() => {
        });
  } else {
    // 根据索引删除数据
    form.value.urlHeaderList.splice(index, 1);
    // 最后一条清空不删掉
    form.value.tableData.length === 0 && form.value.tableData.push(itemData());
  }
}

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id
        }
      });
    }
  }
}

function mapStr2list(form) {
  if (!form.urlHeader || form.urlHeader === "") {
    form.urlHeaderList = [];
    return;
  }
  let mapStr = form.urlHeader;
  const obj = JSON.parse(mapStr);
  form.urlHeaderList = Object.entries(obj).map(([key, val]) => {
    return {key, val};
  });
}

function list2mapStr(form) {
  if (!form.urlHeaderList || form.urlHeaderList.length === 0) {
    form.urlHeader = '';
    return;
  }
  try {
    const obj = {};
    const arr = form.urlHeaderList;
    arr.forEach(item => {
      const {key, val} = item;
      obj[key] = val;
    });
    form.urlHeader = JSON.stringify(obj);
  } catch (err) {
    form.urlHeader = '';
  }
}

getList();
</script>

<style lang="scss" scoped>
.card-button-group {
  display: flex;
  flex-direction: column;

  button {
    margin-left: 0;
  }
}
/* 外层容器限制横向溢出隐藏 */
.only-y-scroll :deep(.el-scrollbar__wrap) {
  overflow-x: hidden !important;
  overflow-y: auto;
}
/* 隐藏横向滚动条DOM节点 */
.only-y-scroll :deep(.el-scrollbar__bar.is-horizontal) {
  display: none !important;
}
.only-y-scroll :deep(.el-scrollbar__bar.is-vertical) {
  right: -6px !important;
  border-radius: 3px;
  background: rgba(0,0,0,0.05);
}
</style>
