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
        <el-form-item label="分段内容" prop="content">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.content"
            placeholder="请输入分段内容"
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

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              @click="handleAdd"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:add']"
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
              icon="Delete"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:remove']"
              @mousedown="(e) => e.preventDefault()"
            >
              删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="" @click="back">
              <i class="iconfont-mini icon-fanhui-baise mr5"></i>返回
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
        v-if="model === 'text_model' || model === 'qa_model'"
        :data="knowledgeSegmentList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          v-if="getColumnVisibility(1) && model !== 'qa_model'"
          label="编号"
          align="center"
          prop="id"
          width="80"
          sortable="custom"
        />
        <el-table-column
          v-if="getColumnVisibility(2) && model !== 'qa_model'"
          label="分段内容"
          align="left"
          prop="content"
          width="600px"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.content || "-" }}
          </template>
        </el-table-column>

        <el-table-column
          v-if="getColumnVisibility(3) && model === 'qa_model'"
          label="问题"
          align="left"
          prop="content"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.content || "-" }}
          </template>
        </el-table-column>

        <el-table-column
          v-if="getColumnVisibility(4) && model === 'qa_model'"
          label="答案"
          align="left"
          prop="answer"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.answer || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(5)"
          label="备注"
          width="200"
          align="left"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(6)"
          label="创建人"
          align="center"
          prop="createBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(7)"
          label="创建时间"
          align="center"
          prop="createTime"
          width="160"
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
          width="240"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="Plus"
              v-if="!scope.row.parentId && model === 'hierarchical_model'"
              @click="handleAdd(scope.row)"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:query']"
              >新增
            </el-button>
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:edit']"
              >修改
            </el-button>
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:remove']"
              >删除
            </el-button>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt="" />
            <p>暂无记录</p>
          </div>
        </template>
      </el-table>
      <el-table
        v-if="model === 'hierarchical_model'"
        v-loading="loading"
        :data="knowledgeSegmentList"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpandAll"
      >
        <el-table-column
          v-if="getColumnVisibility(1)"
          label="编号"
          align="center"
          prop="id"
          width="80"
          sortable="custom"
        />
        <el-table-column
          v-if="getColumnVisibility(2) && model !== 'qa_model'"
          label="分段内容"
          align="left"
          prop="content"
          width="400px"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.content || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(5)"
          label="备注"
          width="200"
          align="left"
          prop="remark"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(6)"
          label="创建人"
          align="center"
          prop="createBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(7)"
          label="创建时间"
          align="center"
          prop="createTime"
          width="160"
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
          label="操作"
          align="left"
          class-name="small-padding fixed-width"
          fixed="right"
          width="240"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="Plus"
              v-if="!scope.row.parentId && model === 'hierarchical_model'"
              @click="handleAdd(scope.row)"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:query']"
              >新增
            </el-button>
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:edit']"
              >修改
            </el-button>
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['kmc:knowledgeSegment:knowledgesegment:remove']"
              >删除
            </el-button>
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

    <!-- 添加或修改文件分段对话框 -->
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
        ref="knowledgeSegmentRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent
      >
        <el-row :gutter="20" v-if="model === 'hierarchical_model'">
          <el-col :span="24">
            <el-form-item label="上级分段" prop="parentId">
              <el-select
                v-model="form.parentId"
                placeholder="请选择上级分段"
                style="width: 350px"
              >
                <el-option
                  v-for="item in allLevelNodesList"
                  :key="item.qmSegmentId"
                  :label="item.content"
                  :value="item.qmSegmentId"
                  style="width: 350px"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item
              label="分段内容"
              prop="content"
              v-if="model !== 'qa_model'"
            >
              <el-input
                v-model="form.content"
                type="textarea"
                placeholder="请输入分段内容"
                :autosize="{ minRows: 8 }"
                maxlength="3000个字符"
                show-word-limit
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item
              label="问题"
              prop="content"
              v-if="model === 'qa_model'"
            >
              <el-input
                v-model="form.content"
                type="textarea"
                placeholder="请输入内容"
                :autosize="{ minRows: 8 }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="model === 'qa_model'">
            <el-form-item label="答案" prop="answer">
              <el-input
                v-model="form.answer"
                type="textarea"
                placeholder="请输入答案"
              />
            </el-form-item>
          </el-col>

          <!--                    <el-col :span="24" v-if="model !== 'hierarchical_model'">-->
          <!--                        <el-form-item label="关键词">-->
          <!--                            <div class="keyword-list">-->
          <!--                                <div-->
          <!--                                    v-for="(keyword, index) in form.keywordList"-->
          <!--                                    :key="index"-->
          <!--                                    class="keyword-item"-->
          <!--                                >-->
          <!--                                    <el-input-->
          <!--                                        v-model="form.keywordList[index]"-->
          <!--                                        placeholder="请输入关键词"-->
          <!--                                        @change="handleKeywordChange(form.keywordList)"-->
          <!--                                        class="keyword-input"-->
          <!--                                        style="width: 500px; margin-right: 10px; margin-bottom: 5px"-->
          <!--                                    />-->
          <!--                                    <el-button-->
          <!--                                        icon="Delete"-->
          <!--                                        circle-->
          <!--                                        @click="removeKeyword(index, form.keywordList)"-->
          <!--                                        :disabled="form.keywordList.length <= 1"-->
          <!--                                        class="keyword-action"-->
          <!--                                        size="small"-->
          <!--                                    />-->
          <!--                                    <el-button-->
          <!--                                        type="primary"-->
          <!--                                        icon="Plus"-->
          <!--                                        circle-->
          <!--                                        @click="addKeyword(form.keywordList)"-->
          <!--                                        v-if="index === form.keywordList.length - 1"-->
          <!--                                        class="keyword-action"-->
          <!--                                        size="small"-->
          <!--                                    />-->
          <!--                                </div>-->
          <!--                            </div>-->
          <!--                        </el-form-item>-->
          <!--                    </el-col>-->
        </el-row>
        <!--                <el-row :gutter="20">-->
        <!--                    <el-col :span="24">-->
        <!--                        <el-form-item label="备注" prop="remark">-->
        <!--                            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>-->
        <!--                        </el-form-item>-->
        <!--                    </el-col>-->
        <!--                </el-row>-->
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
    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
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
        <el-icon class="el-icon--upload">
          <upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />
              是否更新已经存在的文件分段数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >下载模板
            </el-link>
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

<script setup name="KnowledgeSegment">
import {
  listKnowledgeSegment,
  listKnowledgeSegmentTree,
  getAllLevelNodes,
  getKnowledgeSegment,
  delKnowledgeSegment,
  addKnowledgeSegment,
  updateKnowledgeSegment,
} from "@/api/kmc/knowledgeSegment/knowledgeSegment";
import { getToken } from "@/utils/auth.js";
const route = useRoute();
const { proxy } = getCurrentInstance();
const { sync_status, segment_type } = proxy.useDict(
  "sync_status",
  "segment_type"
);

const knowledgeSegmentList = ref([]);

const keywordList = ref([""]);
const allLevelNodesList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "分段内容", visible: true },
  { key: 3, label: "问题", visible: true },
  { key: 4, label: "答案", visible: true },
  { key: 5, label: "备注", visible: true },
  { key: 6, label: "创建人", visible: true },
  { key: 7, label: "创建时间", visible: true },
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
const model = ref(proxy.$route.query.mode);

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
  url: import.meta.env.VITE_APP_BASE_API + "/kmc/knowledgeSegment/importData",
});

const data = reactive({
  form: {
    keywords: [""],
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    content: null,
    syncStatus: null,
    orderByColumn: "createTime",
    isAsc: "descending",
  },
  rules: {
    content: [{ required: true, message: "文件名称不能为空", trigger: "blur" }],
    answer: [{ required: true, message: "答案不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

function addKeyword(list) {
  list.push("");
}

function removeKeyword(index, list) {
  if (list.length > 1) {
    list.splice(index, 1);
  }
}

function handleKeywordChange() {
  // 可以在这里添加关键词变化时的处理逻辑
}

function setColumnsByMode() {
  if (model.value === "qa_model") {
    columns.value = [
      { key: 1, label: "编号", visible: true },
      { key: 3, label: "问题", visible: true },
      { key: 4, label: "答案", visible: true },
      { key: 5, label: "备注", visible: true },
      { key: 6, label: "创建人", visible: true },
      { key: 7, label: "创建时间", visible: true },
    ];
  } else if (model.value === "hierarchical_model") {
    columns.value = [
      { key: 1, label: "编号", visible: true },
      { key: 2, label: "分段内容", visible: true },
      { key: 5, label: "备注", visible: true },
      { key: 6, label: "创建人", visible: true },
      { key: 7, label: "创建时间", visible: true },
    ];
  } else {
    columns.value = [
      { key: 1, label: "编号", visible: true },
      { key: 2, label: "分段内容", visible: true },
      { key: 5, label: "备注", visible: true },
      { key: 6, label: "创建人", visible: true },
      { key: 7, label: "创建时间", visible: true },
    ];
  }
}
setColumnsByMode();

/** 查询文件分段列表 */
function getList() {
  loading.value = true;
  queryParams.value.documentId = Number(proxy.$route.query.id);
  if (proxy.$route.query.mode === "hierarchical_model") {
    //判断是否是分层的数据
    queryParams.value.parent = true;
    listKnowledgeSegmentTree(queryParams.value).then((response) => {
      knowledgeSegmentList.value = proxy.handleTree(
        response.data.rows,
        "qmSegmentId"
      );
      total.value = response.data.total;
      loading.value = false;
    });
  } else {
    queryParams.value.parent = false;
    listKnowledgeSegment(queryParams.value).then((response) => {
      knowledgeSegmentList.value = response.data.rows;
      if (
        knowledgeSegmentList.value != null &&
        knowledgeSegmentList.value.length > 0
      ) {
        knowledgeSegmentList.value.forEach((item) => {
          item.keywordList = JSON.parse(item.keywords);
        });
      }
      total.value = response.data.total;
      loading.value = false;
    });
  }
}

function allLevelNodes() {
  if (proxy.$route.query.mode === "hierarchical_model") {
    //判断是否是分层的数据
    getAllLevelNodes(queryParams.value).then((response) => {
      allLevelNodesList.value = response.data;
    });
  }
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
    documentName: null,
    documentId: null,
    qmSegmentId: null,
    position: null,
    qmDocumentId: null,
    content: null,
    signContent: null,
    answer: null,
    wordCount: null,
    tokens: null,
    keywords: "",
    keywordList: [""],
    indexNodeId: null,
    indexNodeHash: null,
    hitCount: null,
    enabled: null,
    status: null,
    completedAt: null,
    error: null,
    childChunks: null,
    syncStatus: null,
    segmentType: null,
    parentId: null,
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
  proxy.resetForm("knowledgeSegmentRef");
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
function handleAdd(row) {
  reset();
  open.value = true;
  title.value = "新增文件分段";
  if (row) {
    nextTick(() => {
      form.value.parentId = row.qmSegmentId;
    });
  }
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getKnowledgeSegment(_id).then((response) => {
    form.value = response.data;
    if (form.value.keywords) {
      form.value.keywordList = JSON.parse(form.value.keywords);
    }
    open.value = true;
    title.value = "修改文件分段";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getKnowledgeSegment(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "文件分段详情";
  });
}

/** 提交按钮 */
function submitForm() {
  form.value.documentId = proxy.$route.query.id;
  proxy.$refs["knowledgeSegmentRef"].validate((valid) => {
    //如果只有一个元素，并且是空字符串，跳过
    if (form.value.keywordList && form.value.keywordList.length > 0) {
      form.value.keywords = JSON.stringify(form.value.keywordList);
    }
    if (
      form.value.keywordList &&
      form.value.keywordList.length === 1 &&
      form.value.keywordList[0] === ""
    ) {
      delete form.value.keywords;
    }

    if (valid) {
      if (form.value.id != null) {
        updateKnowledgeSegment(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
            allLevelNodes();
          })
          .catch((error) => {});
      } else {
        addKnowledgeSegment(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
            allLevelNodes();
          })
          .catch((error) => {});
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除分段编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delKnowledgeSegment(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/**返回上一页 */
function back() {
  const obj = {
    path: `/kmc/${route.params.kbId}/kmcDocument`,
  };
  proxy.$tab.closeOpenPage(obj);
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "kmc/knowledgeSegment/export",
    {
      ...queryParams.value,
    },
    `knowledgeSegment_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "文件分段导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `knowledgeSegment_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    "导入结果",
    { dangerouslyUseHTMLString: true }
  );
  getList();
};

/** ---------------------------------**/

function routeTo(link, row) {
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
          id: row.id,
        },
      });
    }
  }
}

getList();
allLevelNodes();
</script>
<style scoped lang="scss">
.twoline-content {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2; /* 限制显示2行 */
  overflow: hidden; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 溢出时显示省略号 */
  white-space: normal; /* 允许换行 */
}
</style>
