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
              v-hasPermi="['kac:plugin:plugin:add']"
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
              v-hasPermi="['kac:plugin:plugin:remove']"
              @mousedown="(e) => e.preventDefault()"
            >
              <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <div class="search-desc">
              <svg-icon iconClass="remind" class="desc-icon" />
              上传插件后，插件自启动，系统会短暂加载，期间无法正常使用，请谨慎操作！
            </div>
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
        height="58vh"
        v-loading="loading"
        :data="pluginList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          v-if="getColumnVisibility(1)"
          label="编号"
          align="center"
          width="80"
          prop="id"
          sortable="custom"
        >
          <template #default="scope">
            {{ scope.row.id || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(2)"
          label="名称"
          align="left"
          prop="name"
          width="200px"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(3)"
          label="描述"
          align="left"
          prop="description"
          width="280px"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(2)"
          label="作者"
          align="left"
          prop="name"
          width="60px"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope"> 小桐 </template>
        </el-table-column>
        <el-table-column
          label="文件名称"
          align="center"
          prop="path"
          width="250"
        >
          <template #default="scope">
            {{ scope.row.path.replaceAll("/", "") || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          label="文件大小"
          align="center"
          prop="path"
          width="100"
        >
          <template #default="scope"> 5.6MB </template>
        </el-table-column>
        <el-table-column
          label="图标"
          align="center"
          prop="coverImage"
          width="80"
        >
          <template #default="scope">
            <img
              :src="getImage(scope.row)"
              alt="应用图标"
              :width="50"
              :height="50"
            />
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(4)"
          label="状态"
          align="center"
          prop="status"
          width="80"
        >
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(5)"
          label="创建人"
          align="center"
          prop="createBy"
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
          sortable="custom"
          width="180"
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
          width="`40"
        >
          <template #default="scope">
            <!--            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
            <!--                       v-hasPermi="['kac:plugin:plugin:edit']">修改</el-button>-->
            <el-button
              link
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['kac:plugin:plugin:remove']"
              >删除</el-button
            >
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

    <!-- 添加或修改插件管理对话框 -->
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
        ref="pluginRef"
        :model="form"
        :rules="rules"
        @submit.prevent
        label-width="87px"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="插件路径" prop="path">
              <FileUpload
                v-model="form.path"
                :fileName="form.fileName"
                :fileSize="10"
                :limit="1"
                :fileType="['jar']"
                :platForm="platForm"
                @update:fileName="updateFormFileName"
              ></FileUpload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item
              label="图标"
              prop="coverImage"
              class="image-form-item"
            >
              <image-upload
                v-model="form.coverImage"
                :limit="1"
                :fileSize="300"
                :isShowTip="true"
                :platForm="platImageForm"
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
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__tip">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox
                v-model="upload.updateSupport"
              />是否更新已经存在的插件管理数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >下载模板</el-link
            >
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

<script setup name="Plugin">
// import {
//   listPlugin,
//   getPlugin,
//   delPlugin,
//   addPlugin,
//   updatePlugin,
// } from "@/api/kac/plugin/plugin";
import { getToken } from "@/utils/auth.js";
import FileUpload from "@/components/FileUploadPlugin/index.vue";
import GraphCover from "@/assets/kac/wzbx.png";
import { WarningFilled } from "@element-plus/icons-vue"; // 修正导入

const { proxy } = getCurrentInstance();
const platForm = ref("aliyun-oss-qt");
const platImageForm = ref("local");
const pluginList = ref([
  {
    id: 4,
    workspaceId: 1001,
    pluginKey: "writing-plugin",
    name: "文章编写",
    description:
      "文章编写插件是一类旨在辅助用户更高效、更高质量地完成文本创作任务的软件工具或扩展程序。",
    path: "/69ea0859e4b077552f280c95.jar",
    category: "0",
    fileName: "qknow-writing-2.7.1.jar",
    version: "v1.0.0",
    status: 1,
    coverImage: "/2026/05/11/6a01a88de4b0d389f4f52e8e.png",
    validFlag: true,
    delFlag: false,
    createBy: "吴同",
    creatorId: 2,
    createTime: "2026-04-23 19:54:04",
    updateBy: "吴同",
    updaterId: null,
    updateTime: "2026-04-23 19:54:04",
    remark: null,
  },
]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "名称", visible: true },
  { key: 3, label: "描述", visible: true },
  { key: 4, label: "状态", visible: true },
  { key: 5, label: "创建人", visible: true },
  { key: 6, label: "创建时间", visible: true },
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
const loading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "createTime", order: "desc" });
const router = useRouter();

const updateFormFileName = (newValue) => {
  form.value.fileName = newValue;
};

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
  url: import.meta.env.VITE_APP_BASE_API + "/kac/plugin/importData",
});

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    pluginKey: null,
    name: null,
    description: null,
    fileName: null,
    path: null,
    version: null,
    status: null,
    createTime: null,
    orderByColumn: "createTime",
    isAsc: "desc",
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    pluginKey: [
      { required: true, message: "插件标识不能为空", trigger: "blur" },
    ],
    version: [{ required: true, message: "版本不能为空", trigger: "blur" }],
    status: [{ required: true, message: "状态不能为空", trigger: "blur" }],
    path: [{ required: true, message: "插件路径不能为空", trigger: "blur" }],
    // coverImage: [{ required: true, message: "图标不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询插件管理列表 */
function getList() {
  loading.value = true;
  listPlugin(queryParams.value).then((response) => {
    pluginList.value = response.data.rows;
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
    pluginKey: null,
    name: null,
    description: null,
    fileName: null,
    path: null,
    version: null,
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
  };
  proxy.resetForm("pluginRef");
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
  title.value = "新增插件";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getPlugin(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = "修改插件管理";
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getPlugin(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = "插件管理详情";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["pluginRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        updatePlugin(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          })
          .catch((error) => {});
      } else {
        if (!form.value.path) {
          proxy.$modal.msgWarning("请先上传 jar 文件");
          return;
        }
        // 默认启用
        form.value.status = 1;
        // 默认横向应用
        form.value.category = 0;
        addPlugin(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          })
          .catch((error) => {});
      }
    }
  });
}

function getImage(row) {
  if (!row.coverImage) {
    return GraphCover;
  }
  return `${import.meta.env.VITE_APP_BASE_API}/profile${row.coverImage}`;
}

/** 状态修改  */
function handleStatusChange(row) {
  let text = row.status === 0 ? "停用" : "启用";
  proxy.$modal
    .confirm('确认要"' + text + '""' + row.name + '"插件吗?')
    .then(function () {
      return updatePlugin(row);
    })
    .then(() => {
      proxy.$modal.msgSuccess(text + "成功");
      handleQuery();
    })
    .catch(function () {
      row.status = row.status === 0 ? 1 : 0;
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除插件管理编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delPlugin(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "kac/plugin/export",
    {
      ...queryParams.value,
    },
    `plugin_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = "插件管理导入";
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `plugin_template_${new Date().getTime()}.xlsx`
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

// getList();
</script>

<style scoped lang="scss">
// 检索方式描述（复用提示文本样式）
.search-desc {
  display: flex;
  align-items: center;
  font-size: 14px;
  line-height: 22px;
  font-family: Microsoft YaHei-Regular;
  color: #e6a23c;

  .desc-icon {
    margin-right: 3px;
  }
  margin-top: 3px;
}
</style>
