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
    <div class="pagecont-top" v-show="showSearch">
      <el-form
          class="btn-style"
          :model="queryParams"
          ref="queryRef"
          :inline="true"
          v-show="showSearch"
          @submit.prevent
      >
        <el-form-item label="名称" prop="name">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              placeholder="请输入名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="是否内置" prop="builtinFlag">
          <el-select
              v-model="queryParams.builtinFlag"
              placeholder="请选择是否内置"
              clearable
              class="el-form-input-width"
          >
            <el-option
                v-for="dict in sys_is_or_not"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
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
                v-hasPermi="['kb:bot:bot:add']"
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
                v-hasPermi="['kb:bot:bot:remove']"
                @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
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
          :data="botList"
          :default-sort="defaultSort"
          @sort-change="handleSortChange"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column
            v-if="getColumnVisibility(0)"
            label="编号"
            align="center"
            prop="id"
            width="85"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        />
        <el-table-column
            v-if="getColumnVisibility(1)"
            label="名称"
            align="left"
            prop="name"
            width="250"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(2)"
            label="描述"
            align="left"
            prop="description"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(3)"
            label="类型"
            align="center"
            prop="type"
            width="100"
        >
          <template #default="scope">
            <div>
              <dict-tag :options="kg_bot_type" :value="scope.row.type"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(4)"
            label="是否内置"
            align="center"
            prop="type"
            width="80"
        >
          <template #default="scope">
            <div>
              <dict-tag
                  :options="sys_is_or_not"
                  :value="scope.row.builtinFlag"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column label="节点数" v-if="getColumnVisibility(9) && (botType===1|| botType ===0)"
            align="center"
            prop="createBy"
            width="80"
        >
          <template #default="scope">
            {{ scope.row.nodeNum || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="工具数" v-if="getColumnVisibility(9) && (botType===2)"
                         align="center"
                         prop="createBy"
                         width="80"
        >
          <template #default="scope">
            {{ scope.row.nodeNum || "-" }}
          </template>
        </el-table-column>
        <el-table-column label="密钥数" v-if="getColumnVisibility(10)"
                         align="center"
                         prop="createBy"
                         width="80"
        >
          <template #default="scope">
            {{ scope.row.apiKeyNum || "-" }}
          </template>
        </el-table-column>

        <el-table-column
            v-if="getColumnVisibility(5)"
            label="创建人"
            align="center"
            prop="createBy"
            width="80"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(6)"
            label="创建时间"
            align="center"
            prop="createTime"
            width="150"
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
            v-if="getColumnVisibility(7)"
            label="最后更新时间"
            align="center"
            prop="updateTime"
            width="150"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        >
          <template #default="scope">
            <span>{{
                parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}")
              }}</span>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            v-if="getColumnVisibility(8)"
            class-name="small-padding fixed-width"
            fixed="right"
            width="180"
        >
          <template #default="scope">
            <el-button
                link
                type="primary"
                icon="view"
                @click="handleDetail(scope.row)"
                v-hasPermi="['kb:bot:bot:query']"
            >详情
            </el-button>
            <el-button
                link
                type="primary"
                icon="Operation"
                @click="handleBuild(scope.row)"
                v-hasPermi="['kb:bot:bot:query']"
            >构建
            </el-button>

            <el-popover placement="bottom" :width="100" trigger="click">
              <template #reference>
                <el-button type="primary" icon="ArrowDown"  link @click.stop>更多</el-button>
              </template>
              <div class="card-button-group" >
                <el-button
                    link
                    type="primary"
                    icon="Edit"
                    @click="handleUpdate(scope.row)"
                    :disabled="scope.row.builtinFlag === 1"
                    v-hasPermi="['kb:bot:bot:edit']"
                >修改
                </el-button>
                <el-button
                    link
                    type="danger"
                    icon="Delete"
                    @click="handleDelete(scope.row)"
                    :disabled="scope.row.builtinFlag === 1"
                    v-hasPermi="['kb:bot:bot:remove']"
                >删除
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
          v-show="total >= 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <!-- 添加或修改bot 管理对话框 -->
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
          ref="botRef"
          :model="form"
          :rules="rules"
          label-width="80px"
          @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input
                  v-model="form.description"
                  type="textarea"
                  placeholder="请输入描述"
                  maxlength="256"
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
          >确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Bot">
import {listBot, getBot, delBot, addBot, updateBot} from "@/api/kb/bot/bot";
import {getToken} from "@/utils/auth.js";
import {useRoute, useRouter} from "vue-router";

const {proxy} = getCurrentInstance();
const {kg_bot_type, sys_is_or_not} = proxy.useDict(
    "kg_bot_type",
    "sys_is_or_not"
);
const router = useRouter();
const botList = ref([]);

// 列显隐信息
const columns = ref([
  {key: 0, label: "编号", visible: true},
  {key: 1, label: "名称", visible: true},
  {key: 2, label: "描述", visible: true},
  {key: 3, label: "类型", visible: true},
  {key: 4, label: "是否内置", visible: true},
  {key: 9, label: "节点数", visible: true},
  {key: 10, label: "密钥数", visible: true},
  {key: 5, label: "创建人", visible: true},
  {key: 6, label: "创建时间", visible: true},
  {key: 7, label: "最后更新时间", visible: true},
  {key: 8, label: "操作", visible: true}
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
const defaultSort = ref({prop: "createTime", order: "desc"});
const botType = ref(0);
const botTypeName = ref("");


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
  headers: {Authorization: "Bearer " + getToken()},
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/kb/bot/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    name: null,
    type: null,
    description: null,
    builtinFlag: null,
    orderByColumn: "createTime",
    isAsc: "descending",
  },
  rules: {
    name: [{required: true, message: "名称不能为空", trigger: "blur"}],
    type: [{required: true, message: "类型不能为空", trigger: "change"}],
  },
});

const {queryParams, form, rules} = toRefs(data);
const route = useRoute()

watch(() => route.fullPath,
    () => {
      // 获取 id
      botType.value = Number(route.query.botType)
      let path = '';
      if (botType.value === 0) {
        botTypeName.value = '工作流'
        path = '/kb/bot/workflow'
      } else if (botType.value === 1) {
        botTypeName.value = 'chatflow'
        path = '/kb/bot/chatflow'
      } else if (botType.value === 3) {
        botTypeName.value = 'agent'
        path = '/kb/bot/agent'
      }else {
        return
      }
      router.push({
        path: path,
        query: {
          botType: botType.value,
        },
      });
    }, {immediate: true})

/** 查询bot 管理列表 */
function getList() {
  queryParams.value.type = botType.value
  loading.value = true;
  listBot(queryParams.value).then((response) => {
    botList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  router.push({
    path: route.path+"/detail",
    query: {
      botId: row.id,
      name: row.name,
    },
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
    type: null,
    description: null,
    builtinFlag: null,
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
  proxy.resetForm("botRef");
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
  title.value = "新增" + botTypeName.value;
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getBot(_id).then((response) => {
    form.value = response.data;
    form.value.type = String(form.value.type);
    open.value = true;
    title.value = "修改" + botTypeName.value;
  });
}

/** 详情按钮操作 */
function handleBuild(row) {
  let path = '';
  let title = '';
  let activeMenu = '';

  if (row.type === 0) {
    path = '/kb/bot/processflow';
    title = '构建工作流';
    activeMenu = '/kb/bot/workflow';
  } else if (row.type === 1) {
    path = '/kb/bot/processflow';
    title = '构建chatFlow';
    activeMenu = '/kb/bot/chatflow';
  } else {
    path = '/kb/bot/agent/build';
  }

  router.push({
    path: path,
    query: {
      id: row.id,
    },
  });

  router.afterEach((to) => {
    if (to.path === '/kb/bot/processflow') {
      to.meta.title = title
      to.meta.activeMenu = activeMenu
    }
  })
}

/** 提交按钮 */
function submitForm() {
  form.value.type = botType.value;
  proxy.$refs["botRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updateBot(form.value)
            .then((response) => {
              proxy.$modal.msgSuccess("修改成功");
              open.value = false;
              getList();
            })
            .catch((error) => {
            });
      } else {
        addBot(form.value)
            .then((response) => {
              proxy.$modal.msgSuccess("新增成功");
              open.value = false;
              getList();
            })
            .catch((error) => {
            });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
      .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
      .then(function () {
        return delBot(_ids);
      })
      .then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
      })
      .catch(() => {
      });
}

getList();
</script>

<style lang="scss" scoped>
.default-wrap {
  width: 100%;
  position: relative;

  .tip-content {
    display: flex;
    gap: 2px;
    color: #888;
    font-size: 12px;
    line-height: 1.5;
    padding-top: 4px;
  }

  .el-icon {
    margin-top: 3px;
  }
}

.label-wrap {
  display: flex;
  align-items: center;
  gap: 2px;

  .el-icon {
    color: #888;
  }
}

::v-deep(.el-form-item__error) {
  padding-top: 6px;
}

.el-form-item.is-error {
  padding-bottom: 16px;
}

.card-button-group {
  display: flex;
  flex-direction: column;
  button{
    margin-left: 0;
  }
}
</style>
