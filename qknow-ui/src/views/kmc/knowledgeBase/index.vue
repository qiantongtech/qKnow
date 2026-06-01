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
    <GuideTip tip-id="kmc/knowledgeBase.list" />
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
        <el-form-item label="名称" prop="name" label-width="44px">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item label="启用状态" prop="validFlag">
          <el-select
            class="el-form-input-width"
            v-model="queryParams.validFlag"
            placeholder="请选择启用状态"
            clearable
          >
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
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
        <el-form-item style="float: right; margin-right: auto">
          <el-button
            type="primary"
            @click="handleAdd"
            plain
            v-hasPermi="['kmc:knowledgeBase:knowledgebase:add']"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng"></i>新增知识库
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <Card
      :data="knowledgeBaseList"
      @handleDataScope="handleDataScope"
      @handleDelete="handleDelete"
      @handleValidChange="handleValidChange"
    />
    <div class="pagecont-bottom">
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
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
        ref="knowledgeBaseRef"
        :model="form"
        :rules="rules"
        label-width="auto"
        @submit.prevent
      >
        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item label="名称" prop="name" class="name-form-item">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 索引模式 -->
        <el-row :gutter="24">
          <el-col :span="24">
            <qt-form-item
              label="索引方式"
              prop="indexingTechnique"
              class="index-form-item"
              :tip="
                form.indexingTechnique === 'high_quality'
                  ? '调用高级模型来处理文本以实现更精确的检索，可以相对大语言模型生成高质量的回答。'
                  : '每个块使用 10 个关键词进行检索，不消耗tokens，但会降低检索准确性。'
              "
            >
              <el-select
                v-model="form.indexingTechnique"
                placeholder="请选择索引方式"
                :disabled="
                  isDisabled && form.indexingTechnique === 'high_quality'
                "
              >
                <el-option label="高质量 (推荐)" value="high_quality" />
                <el-option label="经济" value="economy" />
              </el-select>
            </qt-form-item>
          </el-col>
        </el-row>

        <!-- Embedding 模型 -->
        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item
              label="Embedding模型"
              prop="embeddingModel"
              class="emdedding-form-item"
            >
              <el-select
                v-model="form.embeddingModel"
                placeholder="请选择Embedding模型"
              >
                <el-option-group
                  v-for="group in embeddingModel"
                  :key="group.label.zh_Hans"
                  :label="group.label.zh_Hans"
                >
                  <el-option
                    v-for="item in group.models"
                    :key="item.model"
                    :label="item.model"
                    :value="item.model"
                  />
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
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

        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item label="标签" prop="tags" class="tag-form-item">
              <div class="flex gap-2">
                <el-tag
                  v-for="(tag, index) in form.items.row"
                  :key="index"
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

        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item
              class="status-form-item"
              label="状态"
              prop="validFlag"
              style="display: flex; align-items: center"
            >
              <el-radio-group v-model="form.validFlag">
                <el-radio
                  v-for="item in kmc_know_valid"
                  :key="item.value"
                  :label="item.value === 'true'"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item
              label="描述"
              prop="description"
              class="desc-form-item"
            >
              <el-input
                type="textarea"
                v-model="form.description"
                placeholder="请输入描述"
                maxlength="256"
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
            >确 定</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="KnowledgeBase">
import { nextTick, ref, reactive, toRefs } from "vue";
import {
  listKnowledgeBase,
  getRole,
  addKnowledgeBase,
  updateKnowledgeBase,
  updateKnowledgeBaseRole,
  changeKnowledgeValid,
  getTextEmbedding,
  delKnowledgeBase,
} from "@/api/kmc/knowledgeBase/knowledgeBase";
import Card from "./components/card.vue";
import { Trophy } from "@element-plus/icons-vue";

const { proxy } = getCurrentInstance();
const { kmc_know_valid } = proxy.useDict("kmc_know_valid");

const knowledgeBaseList = ref([]);
const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const title = ref("");
const roleList = ref([]);
const roleLoading = ref(false);
const roleIds = ref();
const knowledgeBaseId = ref(null);
const roleTableRef = ref(null);
const router = useRouter();

// 响应式变量
const isDisabled = ref(false);
const embeddingModel = ref([]);
const platForm = ref("local");
const inputValue = ref("");
const inputVisible = ref(false);
const InputRef = ref(null);
const isAdd = ref(true); // 默认为新增模式

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    description: null,
    indexingTechnique: null,
    permission: null,
    embeddingModel: null,
    embeddingModelProvider: null,
    searchMethod: null,
    rerankingEnable: null,
    rerankingProviderName: null,
    rerankingModelName: null,
    topK: null,
    scoreThresholdEnabled: null,
    scoreThreshold: null,
    createTime: null,
    orderByColumn: "createTime",
    isAsc: "descending",
    validFlag: null,
  },
  form: {
    items: { row: [] }, // 标签数组容器
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    validFlag: [{ required: true, message: "状态不能为空", trigger: "blur" }],
    indexingTechnique: [
      { required: true, message: "索引方式不能为空", trigger: "blur" },
    ],
    searchMethod: [
      { required: true, message: "检索方式不能为空", trigger: "blur" },
    ],
    embeddingModel: [
      { required: true, message: "请选择一个模型", trigger: "change" },
    ],
  },
});

const vectorData = ref({
  rerankingEnable: true,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: false,
  scoreThreshold: 0,
});
const fullTextData = ref({
  rerankingEnable: true,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: false,
  scoreThreshold: 0,
});
const mixData = ref({
  rerankingEnable: true,
  rerankingModelName: null,
  topK: 1,
  scoreThresholdEnabled: false,
  scoreThreshold: 0,
  keywordWeight: 0.5,
  vectorWeight: 0.5,
  rerankingMode: "weighted_score",
});

const { queryParams, form, rules } = toRefs(data);

/** 查询知识库列表 */
function getList() {
  loading.value = true;
  listKnowledgeBase(queryParams.value).then((response) => {
    knowledgeBaseList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
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

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增知识库";
  // router.push({
  //   path: "/kmc/knowledgeBase/add",
  //   query: {},
  // });
}

function handleValidChange(row) {
  let text = row.validFlag ? "启用" : "停用";
  proxy.$modal
    .confirm('确认要"' + text + '""' + row.name + '"知识库吗?')
    .then(function () {
      return changeKnowledgeValid(row.id, row.validFlag);
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功");
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除知识库编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delKnowledgeBase(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/** 提交按钮 */
function submitForm() {
  // 同步检索参数到表单
  if (form.value.searchMethod === "semantic_search") {
    form.value.rerankingEnable = vectorData.value.rerankingEnable;
    form.value.rerankingModelName = vectorData.value.rerankingModelName;
    form.value.topK = vectorData.value.topK;
    form.value.scoreThresholdEnabled = vectorData.value.scoreThresholdEnabled;
    form.value.scoreThreshold = vectorData.value.scoreThreshold;
  } else if (form.value.searchMethod === "full_text_search") {
    form.value.rerankingEnable = fullTextData.value.rerankingEnable;
    form.value.rerankingModelName = fullTextData.value.rerankingModelName;
    form.value.topK = fullTextData.value.topK;
    form.value.scoreThresholdEnabled = fullTextData.value.scoreThresholdEnabled;
    form.value.scoreThreshold = fullTextData.value.scoreThreshold;
  } else if (form.value.searchMethod === "hybrid_search") {
    form.value.rerankingEnable = mixData.value.rerankingEnable;
    form.value.rerankingModelName = mixData.value.rerankingModelName;
    form.value.topK = mixData.value.topK;
    form.value.scoreThresholdEnabled = mixData.value.scoreThresholdEnabled;
    form.value.scoreThreshold = mixData.value.scoreThreshold;
    form.value.keywordWeight = mixData.value.keywordWeight;
    form.value.vectorWeight = mixData.value.vectorWeight;
    form.value.rerankingMode = mixData.value.rerankingMode;
  }
  const matchedItem = embeddingModel.value.find((item) =>
    item.models.some((modelObj) => modelObj.model === form.value.embeddingModel)
  );
  form.value.rerankingProviderName = matchedItem ? matchedItem.provider : null;

  // 表单验证与提交
  proxy.$refs["knowledgeBaseRef"].validate((valid) => {
    if (valid) {
      // 区分修改和新增场景
      if (form.value.id) {
        // 修改场景：已有ID，先更新基础信息，再更新权限
        updateKnowledgeBase(form.value).then(() => {
          updateKnowledgeBaseRole({
            knowledgeId: form.value.id,
            roleIds: roleIds.value,
          }).then(() => {
            proxy.$modal.msgSuccess("保存成功");
            open.value = false;
            getList();
          });
        });
      } else {
        // 新增场景：先新增知识库，获取ID后再绑定权限
        addKnowledgeBase(form.value).then((response) => {
          const newKnowledgeId = response.data;
          if (newKnowledgeId) {
            updateKnowledgeBaseRole({
              knowledgeId: newKnowledgeId,
              roleIds: roleIds.value,
            }).then(() => {
              proxy.$modal.msgSuccess("新增成功");
              const obj = { path: "/kmc/knowledgeBase" };
              proxy.$tab.closeOpenPage(obj).then(() => {
                proxy.$tab.refreshPage();
              });
              open.value = false;
              getList();
            });
          } else {
            proxy.$modal.msgError("新增失败，未获取到知识库ID");
          }
        });
      }
    }
  });
}

// 多选框选中数据
function handleSelectionChange(selection) {
  roleIds.value = selection.map((item) => item.roleId).join(",");
}

function handleDataScope(row) {
  open.value = true;
  title.value = "分配数据权限";
  roleLoading.value = true;
  knowledgeBaseId.value = row.id;
  getRole(row.id).then((response) => {
    roleLoading.value = false;
    roleList.value = response.data.roleList;
    const selectedRoleIds = response.data.knowledgeRoleList.map(
      (item) => item.roleId
    );
    roleIds.value = selectedRoleIds.join(",");
    nextTick(() => {
      roleList.value.forEach((item) => {
        if (selectedRoleIds.includes(item.roleId)) {
          roleTableRef.value.toggleRowSelection(item, true);
        }
      });
    });
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 标签操作
const handleClose = (tag) => {
  const index = form.value.items.row.findIndex((t) => t.name === tag.name);
  if (index !== -1) {
    form.value.items.row.splice(index, 1);
    form.value.tags = JSON.stringify(form.value.items.row);
  }
};

const handleInputConfirm = () => {
  const value = inputValue.value.trim();
  if (value) {
    form.value.items.row.push({ name: value });
    form.value.tags = JSON.stringify(form.value.items.row);
  }
  inputVisible.value = false;
  inputValue.value = "";
};

const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    if (InputRef.value && InputRef.value.input) {
      InputRef.value.input.focus();
    }
  });
};

// 表单重置
function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    qmDatasetId: null,
    name: null,
    items: { row: [] },
    tags: null,
    description: null,
    indexingTechnique: "high_quality",
    permission: null,
    embeddingModel: null,
    embeddingModelProvider: null,
    searchMethod: "hybrid_search",
    rerankingEnable: false,
    rerankingProviderName: null,
    rerankingModelName: null,
    topK: 1,
    scoreThresholdEnabled: false,
    scoreThreshold: null,
    validFlag: true,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    keywordWeight: null,
    vectorWeight: null,
    rerankingMode: null,
  };
  proxy.resetForm("knowledgeBaseRef");
}

// 初始化模型数据
function init() {
  getTextEmbedding().then((response) => {
    embeddingModel.value = response.data;
  });
}

// 初始化
init();

getList();
proxy.$tab.closeAllPage();
</script>

<style scoped lang="scss">
.app-container {
}
.pagecont-bottom {
  //display: flex;
  //position: absolute;
  //bottom: 8px;
  //width: calc(100% - 30px);
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
// 基础样式
::v-deep .el-tag {
  margin-right: 7px;
}

.flex {
  display: flex;
}

// 基础下划线样式
.underline-base {
  border-bottom: 2px solid #f4f4f4;
  margin-bottom: 13px;
}

// 新增场景样式
.underline-add {
  border-bottom: 2px solid #f4f4f4;
  margin-top: 210px;
}

// 修改场景样式
.underline-edit {
  border-bottom: 2px solid #f4f4f4;
  margin-top: 166px;
}

// 索引方式样式
.indexing-container {
  display: flex;
  gap: 15px;
  width: 100%;
}
.indexing-item {
  flex: 1;
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #f9fafb;

  &.act {
    background-color: #f0f7ff;
    border-color: #2666fb;
    .indexing-title {
      color: #2666fb;
      font-weight: 500;
    }
  }
  &.disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}
.indexing-title {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #333;
  font-size: 14px;

  .icon {
    margin-right: 8px;
    font-size: 18px;
    color: #666;
  }
  img {
    width: 18px;
    height: 18px;
    margin-right: 8px;
  }
}
.indexing-desc {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
}
.recommend-tag {
  margin-left: auto;
  height: 20px;
  line-height: 20px;
  padding: 0 6px;
  font-size: 12px;
}

:deep(.tag-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}

:deep(.index-form-item .el-form-item__label) {
  color: #333333;
  font-size: 14px;
  font-family: PingFangSC-Regular-;
}
</style>
