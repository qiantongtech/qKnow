<template>
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="ai/key.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="90px"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item label="模型名称" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入模型名称"
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

    <Card :data="keyList" @handleUpdate="handleUpdate" @routeTo="routeTo" />

    <div class="pagecont-bottom">
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 地址密钥列表 -->
    <el-dialog
      class="key-config-dialog"
      :title="keyTableTitle"
      v-model="showKeyOpen"
      width="800px"
      :append-to="$refs['app-container']"
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ keyTableTitle }}
        </span>
      </template>

      <el-form
        ref="handleRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-button
              type="primary"
              style="margin-bottom: 15px"
              @click="addItem"
              plain
              >新增密钥</el-button
            >
            <el-table :data="form.platformKeyList" style="width: 100%">
              <el-table-column label="名称" align="center" prop="name">
                <template #default="scope">
                  <el-input
                    v-model="scope.row.name"
                    type="text"
                    placeholder="请输入名称"
                  />
                </template>
              </el-table-column>

              <el-table-column
                v-if="getColumnVisibility(1)"
                label="API地址"
                align="center"
                prop="url"
              >
                <template #default="scope">
                  <el-input
                    v-model="scope.row.url"
                    type="text"
                    placeholder="请填写API地址"
                  />
                </template>
              </el-table-column>
              <el-table-column
                v-if="getColumnVisibility(2)"
                label="密钥"
                align="center"
                prop="apiKey"
              >
                <template #default="scope">
                  <el-input
                    v-model="scope.row.apiKey"
                    type="text"
                    placeholder="请填写密钥"
                  />
                </template>
              </el-table-column>
              <!-- 操作列 -->
              <el-table-column align="center" width="110px" label="操作">
                <template #default="scope">
                  <el-button
                    size="mini"
                    type="danger"
                    @click="deleteItem(scope.$index, scope.row)"
                    plain
                    >删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" size="mini" @click="submitForm"
            >提交</el-button
          >
          <el-button size="mini" @click="handleClose">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Key">
import {
  getKey,
  listByPlatform,
  listKey,
  removeKey,
  submitBatch,
} from "@/api/ai/modelMarket/key";
import Card from "@/views/ai/modelMarket/card.vue";

const { proxy } = getCurrentInstance();
const { ai_apikey_status, ai_model_platform } = proxy.useDict(
  "ai_apikey_status",
  "ai_model_platform"
);

const urlList = ref([
  {
    key: "TongYi",
    keyUrl: "https://help.aliyun.com/zh/model-studio/get-api-key",
    desc: "从阿里云百炼获取 API KEY",
  },
  {
    key: "DeepSeek",
    keyUrl: "https://platform.deepseek.com/api_keys",
    desc: "从DeepSeek获取 API KEY",
  },
  {
    key: "Ollama",
    keyUrl: "",
    desc: "123",
  },
]);
// 列显隐信息
const columns = ref([
  { key: 1, label: "API 地址", visible: true },
  { key: 2, label: "密钥", visible: true },
]);
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const showKeyOpen = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const title = ref("");
const keyTableTitle = ref("");
const currentPlatform = ref("");
const keyList = ref([]);
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();
const data = reactive({
  form: {
    platformKeyList: [],
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    name: null,
    apiKey: null,
    platform: null,
    url: null,
    status: null,
    createTime: null,
  },
  rules: {
    apiKey: [{ required: true, message: "密钥不能为空", trigger: "blur" }],
    url: [{ required: true, message: "API地址不能为空", trigger: "blur" }],
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
  },
  modelList: [],
});

const { queryParams, form, rules } = toRefs(data);

/** 查询API密钥列表 */
function getList() {
  loading.value = true;
  listKey(queryParams.value).then((response) => {
    keyList.value = response.data.rows;
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
    apiKey: null,
    platform: null,
    url: null,
    status: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    keyUrl: "",
    desc: "",
  };
  proxy.resetForm("keyRef");
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

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  if (row.platform === "Ollama") {
    columns.value[0].visible = true;
    columns.value[1].visible = false;
    rules.url = [
      { required: true, message: "API地址不能为空", trigger: "blur" },
    ];
  } else if (row.platform === "OpenAI") {
    columns.value[0].visible = true;
    columns.value[1].visible = true;
    rules.url = [
      { required: true, message: "API地址不能为空", trigger: "blur" },
    ];
  } else {
    columns.value[0].visible = false;
    columns.value[1].visible = true;
    delete rules.url;
  }
  keyTableTitle.value = "密钥设置";
  currentPlatform.value = row.platform;
  console.log("form.value", form.value);
  listByPlatform(row.platform).then((response) => {
    form.value.platformKeyList = response.data;
    showKeyOpen.value = true;
  });
}

const addItem = () => {
  form.value.platformKeyList.push(itemData());
};

const itemData = () => {
  return {
    platform: currentPlatform.value,
    name: "",
    apiKey: "",
    url: "",
  };
};

function handleClose() {
  reset();
  showKeyOpen.value = false;
}

// 删除操作
function deleteItem(index, row) {
  if (row.id) {
    proxy.$modal
      .confirm(`是否确认删除名称为【${row.name}】的密钥？`)
      .then(() => {
        // 根据索引删除数据
        form.value.platformKeyList.splice(index, 1);
        ids.value.push(row.id);
        // 最后一条清空不删掉
        form.value.platformKeyList.length === 0 &&
          form.value.platformKeyList.push(itemData());
      })
      .catch(() => {});
  } else {
    // 根据索引删除数据
    form.value.platformKeyList.splice(index, 1);
    // 最后一条清空不删掉
    form.value.tableData.length === 0 && form.value.tableData.push(itemData());
  }
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["handleRef"].validate(async (valid) => {
    if (valid) {
      const data = {
        platform: currentPlatform.value,
        keyList: form.value.platformKeyList,
      };
      submitBatch(data).then((response) => {
        proxy.$modal.msgSuccess("设置成功");
        showKeyOpen.value = false;
        getList();
      });
    }
  });
}

/**
 * 移除 apiKey
 */
function handleRemove() {
  removeKey(form.value.id).then(() => {
    getList();
  });
  open.value = false;
}

function routeTo(link, row) {
  link = "/system/ai/modelMarket/detail";
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          platform: row.platform,
        },
      });
    }
  }
}

getList();
</script>

<style scoped lang="scss">
.app-container {
  padding-bottom: 45px;
  overflow-x: hidden;

  .pagecont-bottom {
    position: fixed;
    bottom: 0;
    width: 100%;
    left: 0;
    height: 60px;
    background: #ffffff;
    border-radius: 2px 2px 2px 2px;
    line-height: 60px;
    margin: 0;
    padding: 0 18px 0px 0;
    flex: none;

    .pagination-container {
      margin-top: 0;
    }
  }
}

:deep(.no-change-link) {
  color: var(--el-color-primary) !important; // 固定蓝色（Element主色）
  text-decoration: none !important; // 强制无下划线
  &:hover,
  &:active,
  &:visited {
    color: var(
      --el-color-primary
    ) !important; // 悬浮/点击/已访问 都保持蓝色不变
    text-decoration: none !important; // 任何状态都无下划线
  }

  cursor: pointer; // 保留鼠标手型，提示可点击
}

:deep(.key-config-dialog .el-dialog__body) {
  overflow: auto;
  padding: 20px 40px 5px 40px !important;
}

:deep(.card-container) {
  padding: 15px 0px;
}
</style>
