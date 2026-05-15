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
        label-width="75px"
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
        <el-form-item style="float: right; margin-right: auto">
          <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                @click="handleAdd"
                v-hasPermi="['kac:solution:solution:add']"
              >
                <i class="iconfont-mini icon-xinzeng mr5"></i>
                新增</el-button
              >
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>

    <div class="card-list-panel">
      <SolutionCard
        :data="solutionList"
        source="myApp"
        variant="overview"
        @refresh="getList"
        @edit="handleEdit"
      />
    </div>

    <div class="pagecont-bottom">
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改解决方案对话框 -->
    <el-dialog
      :title="title"
      v-model="open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form
        ref="solutionRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="关联应用" prop="myAppIds">
              <div>
                <el-button
                  :disabled="form.status && form.status != 0"
                  v-on:click="selectApp"
                  type="primary"
                  plain
                >
                  <i class="iconfont icon-upload-cloud-line mr5"></i
                  >导入关联应用
                </el-button>
              </div>
            </el-form-item>
            <div style="margin: 0px 0 10px 80px">
              <el-table
                stripe
                height="300px"
                v-loading="loading"
                :data="appTitles"
              >
                <el-table-column
                  width="450"
                  label="应用名称"
                  prop="status"
                  show-overflow-tooltip
                >
                  <template #default="scope">
                    {{ scope.row.name || "-" }}
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="center"
                  class-name="small-padding fixed-width"
                >
                  <template #default="scope">
                    <el-button
                      link
                      type="danger"
                      icon="Delete"
                      :disabled="form.status && form.status != 0"
                      @click="removeSelectApp(scope.row)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>

                <template #empty>
                  <div class="emptyBg">
                    <img
                      src="@/assets/system/images/no_data/noData.png"
                      alt=""
                      style="width: 100px; height: 100px"
                    />
                    <p>暂无记录</p>
                  </div>
                </template>
              </el-table>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item
              label="封面图"
              prop="coverImage"
              class="image-form-item"
            >
              <image-upload
                v-model="form.coverImage"
                :limit="1"
                :fileSize="10"
                :isShowTip="true"
                :platForm="platForm"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="标签" prop="tags">
              <div class="flex gap-2">
                <el-tag
                  v-for="tag in form.items.row"
                  :key="tag"
                  closable
                  :disable-transitions="false"
                  @close="handleClose(tag)"
                >
                  {{ tag.name }}
                </el-tag>
                <el-input
                  v-if="inputVisible"
                  ref="InputRef"
                  v-model="inputValue"
                  class="w-20"
                  size="small"
                  @keyup.enter="handleInputConfirm"
                  @blur="handleInputConfirm"
                />
                <el-button
                  v-else
                  class="button-new-tag"
                  size="small"
                  @click="showInput"
                >
                  + 添加标签
                </el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                maxlength="512"
                placeholder="请输入描述"
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
                maxlength="512"
                placeholder="请输入备注"
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

    <SelectMyApp
      v-model:visible="selectAppVisible"
      :selectedIds="form.myAppIds"
      @confirm="selectConfirmData"
    ></SelectMyApp>
  </div>
</template>

<script setup name="Horizontal">
import SolutionCard from "./components/solutionCard.vue";
import SelectMyApp from "./components/selectMyApp.vue";
import { getCurrentInstance, nextTick, ref, reactive, toRefs } from "vue";
import {
  addSolution,
  listSolution,
  updateSolution,
  getSolution,
} from "@/api/kac/solution/solution";
import { listSolutionApply } from "@/api/kac/solution/solutionApply.js";
import { listApply } from "@/api/kac/apply/apply.js";
import useUserStore from "@/store/system/user.js";

const { proxy } = getCurrentInstance();
const userStore = useUserStore();

const open = ref(false);
const selectAppVisible = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const title = ref("");
const platForm = ref('local');
const appTitles = ref([]);
const openDetail = ref(false);
const allApplyList = ref([]); // 存储所有应用列表

const inputValue = ref("");
const inputVisible = ref(false);
const InputRef = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    userId: userStore.id,
    mySolutionFlag: 1,
    orderByColumn: 'createTime',
    isAsc: 'desc'
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
    myAppIds: [
      { required: true, message: "关联应用不能为空", trigger: "blur" },
    ],
    tags: [
      {
        required: true,
        validator: (rule, value, callback) => {
          if (!form.value.items.row || form.value.items.row.length === 0) {
            callback(new Error('请至少添加一个标签'));
          } else {
            callback();
          }
        },
        trigger: 'change'
      }
    ]
  },
});

const { queryParams, form, rules } = toRefs(data);

const solutionList = ref([]);

// 处理输入确认（回车或失去焦点）
const handleInputConfirm = () => {
  const value = inputValue.value.trim();
  if (value) {
    form.value.items.row.push({ name: value });
  }
  inputVisible.value = false;
  inputValue.value = "";
};

// 显示输入框并自动聚焦
const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    if (InputRef.value && InputRef.value.input) {
      InputRef.value.input.focus();
    }
  });
};

/** 查询解决方案列表 */
function getList() {
  loading.value = true;
  listSolution(queryParams.value).then((response) => {
    solutionList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
  listApply({ pageNum: 1, pageSize: 999 }).then((response) => {
    allApplyList.value = response.data.rows || [];
  });
}

function selectApp() {
  selectAppVisible.value = true;
}

function selectConfirmData(data) {
  appTitles.value = data;
  form.value.myAppIds = data.map((e) => e.id).join(",");
}

function removeSelectApp(row) {
  const index = appTitles.value.findIndex((item) => item.id === row.id);
  if (index > -1) {
    appTitles.value.splice(index, 1);
    form.value.myAppIds = appTitles.value.map((e) => e.id).join(",");
  }
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
    description: null,
    coverImage: null,
    tags: null,
    items: {
      row: [],
    },
    status: null,
    mySolutionFlag: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    myAppIds: null,
  };
  appTitles.value = [];
  proxy.resetForm("solutionRef");
}

// 删除指定的 tag
const handleClose = (tag) => {
  const index = form.value.items.row.findIndex((t) => t.name === tag.name);
  if (index !== -1) {
    form.value.items.row.splice(index, 1);
  }
};

/** 新增按钮操作 */
function handleAdd() {
  reset();
  appTitles.value = [];
  form.value.myAppIds = null;
  form.value.items = { row: [] };
  open.value = true;
  title.value = "新增我的解决方案";
}

/** 编辑按钮操作 */
function handleEdit(row) {
  reset();
  // 调用接口查询明细
  getSolution(row.id).then(response => {
    const data = response.data;
    // 复制行数据到表单
    form.value = { ...data };
    // 解析 tags
    if (data.tags) {
      try {
        const parsedTags = JSON.parse(data.tags);
        form.value.items = { row: Array.isArray(parsedTags) ? parsedTags : [] };
      } catch {
        form.value.items = { row: [] };
      }
    } else {
      form.value.items = { row: [] };
    }
    // 加载关联应用数据
    if (data.id) {
      // 调用接口查询解决方案关联应用列表
      listSolutionApply({ solutionId: data.id, pageNum: 1, pageSize: 999 }).then(response => {
        const solutionApplyList = response.data.rows || [];
        // 获取 apply_id 列表
        const applyIds = solutionApplyList.map(item => item.applyId).filter(id => id);
        if (applyIds.length > 0 && allApplyList.value.length > 0) {
          // 从缓存的应用列表中匹配名称
          const applyMap = new Map(allApplyList.value.map(item => [item.id, item]));
          appTitles.value = applyIds.map(id => {
            const apply = applyMap.get(Number(id));
            return {
              id: id,
              name: apply?.name || '-'
            };
          });
        } else {
          appTitles.value = [];
        }
      }).catch(() => {
        appTitles.value = [];
      });
    } else {
      appTitles.value = [];
    }
    open.value = true;
    title.value = "修改我的解决方案";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["solutionRef"].validate(valid => {
    if (valid) {
      if (form.value.myAppIds == null || form.value.myAppIds == "") {
        proxy.$modal.msgWarning(`关联应用不能为空`);
        return;
      }
      if (form.value.items.row) {
        form.value.tags = JSON.stringify(form.value.items.row);
      }
      if (form.value.id != null) {
        updateSolution(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        form.value.status = 0;
        form.value.mySolutionFlag = 1;
        addSolution(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      }
    }
  });
}

getList();
</script>
<style lang="scss" scoped>
::v-deep .el-dialog__body {
  max-height: 700px;
  overflow-y: auto;
  padding-right: 10px;
}
.app-container {
  box-sizing: border-box;
  padding-bottom: 45px;
}

.card-list-panel {
  margin-top: 15px;
  padding: 15px;
  background: #ffffff;
  border-radius: 2px;
}

.multiline-ellipsis {
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 限制为2行 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
.gap-2 {
  gap: 0.5rem;
}
.flex {
  display: flex;
}
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
  padding: 0 18px 0 0;
  flex: none;
  .pagination-container {
    margin-top: 0;
  }
}
.pagecont-top {
  ::v-deep .el-form-item:first-child .el-form-item__label {
    width: 41px !important;
  }
}
</style>

