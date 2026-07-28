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
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" @submit.prevent>
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
                v-for="dict in sys_normal_disable"
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
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['kb:skills:skills:add']"
                       @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="danger"
                icon="Delete"
                plain
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['kb:skills:skills:remove']"
                @mousedown="(e) => e.preventDefault()"
            >
              删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain @click="handleBatchImport" v-hasPermi="['kb:skills:skills:add']"
                       @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="skillsList" @selection-change="handleSelectionChange"
                :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" width="80" sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            {{ scope.row.id || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(1)" label="名称" align="left" prop="name" width="200">
          <template #default="scope">
            {{ scope.row.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="描述" align="left" prop="description"
                         :show-overflow-tooltip="{ effect: 'light' }">
          <template #default="scope">
            {{ scope.row.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(3)"
            label="状态"
            align="center"
            prop="status"
            width="100"
        >
          <template #default="scope">
            <el-switch
                v-model="scope.row.status"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="创建人" align="center" prop="createBy" width="150">
          <template #default="scope">
            {{ scope.row.createBy || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="创建时间" align="center" prop="createTime" width="150"
                         sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="280"
                         v-if="getColumnVisibility(6)">
          <template #default="scope">
            <el-button link type="primary" icon="View" @click="handlePreview(scope.row)"
                       v-hasPermi="['kb:skills:skills:query']">预览
            </el-button>
            <el-button link type="primary" icon="Download" @click="handleDownload(scope.row)"
                       v-hasPermi="['kb:skills:skills:query']">下载
            </el-button>
            <el-popover placement="bottom" :width="100" trigger="click">
              <template #reference>
                <el-button type="primary" icon="ArrowDown" link @click.stop
                >更多</el-button
                >
              </template>
              <div class="card-button-group">
                <el-button link type="primary" icon="Edit" :disabled="scope.row.status === 0"
                           @click="handleUpdate(scope.row)"
                           v-hasPermi="['kb:skills:skills:edit']">修改
                </el-button>
                <el-button link type="danger" icon="Delete" :disabled="scope.row.status === 0"
                           @click="handleDelete(scope.row)"
                           v-hasPermi="['kb:skills:skills:remove']">删除
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

    <!-- 添加或修改skills对话框 -->
    <el-dialog class="skills-dialog" :title="title" v-model="open" width="800px" max-height="700px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-scrollbar max-height="600px" class="only-y-scroll" :noresize="true">
        <el-form ref="skillsRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="技能名称" prop="name">
              <div class="default-wrap">
                <el-input v-model="form.name" placeholder="请输入技能名称"
                          @input="form.name = form.name.toLowerCase().replace(/[^a-z-]/g, '')"/>
                <div class="tip-content">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  <span>
                      只能包含小写英文和连字符，这是技能的唯一代码标识。
                  </span>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <div class="default-wrap">
                <el-input v-model="form.description" type="textarea"
                          placeholder="请输入描述" maxlength="1024个字符"
                          show-word-limit/>
                <div class="tip-content">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  <span>
                      用一句话说明该技能的用途和触发场景，这决定了 AI 能否准确调用它。
                  </span>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col class="cu_html_text" :span="24">
            <el-form-item label="指令" prop="prompt">
              <el-input
                  v-model="form.prompt"
                  type="textarea"
                  :rows="8"
                  :placeholder="promptPlaceholder"
                  show-word-limit
              />
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

    <!-- skills详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="skillsRef" :model="form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="指令" prop="prompt">
              <div style="white-space: pre-wrap;">
                {{ form.prompt }}
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

    <!-- 导入 skills 对话框 -->
    <el-dialog :title="batchImportTitle" v-model="batchImportOpen" width="800px" :append-to="$refs['app-container']"
               draggable destroy-on-close>
      <el-form ref="batchImportRef" :model="batchImportForm" :rules="batchImportRules"
               @submit.prevent>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item prop="filePath">
              <FileUpload
                  v-model="batchImportForm.filePath"
                  :fileName="batchImportFileName"
                  :fileSize="200"
                  :limit="1"
                  :fileType="['zip']"
                  :platForm="platForm"
                  @update:fileName="updateBatchFileName"
              ></FileUpload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button
              size="mini"
              @click="cancelBatchImport"
              :loading="batchImportLoading"
          >取 消
          </el-button>
          <el-button
              type="primary"
              size="mini"
              @click="submitBatchImport"
              :loading="batchImportLoading"
          >确 定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 预览 SKILL.md 对话框 -->
    <el-dialog :title="previewTitle" v-model="previewOpen" width="1200px" :append-to="$refs['app-container']" draggable destroy-on-close>
      <div class="skill-preview-content" v-loading="previewLoading">
        <pre>{{ previewContent }}</pre>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="previewOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="Skills">
import {listSkills, getSkills, delSkills, addSkills, updateSkills, batchImportSkills, previewSkillMd, downloadSkill} from "@/api/kb/skills/skills";
import {getToken} from "@/utils/auth.js";
import FileUpload from "@/components/FileUploadSkills/index.vue";
import {ElMessageBox} from "element-plus";

const { proxy } = getCurrentInstance();
const platForm = ref("aliyun-oss-qt");
const skillsList = ref([]);
const { sys_normal_disable } = proxy.useDict(
    'sys_normal_disable'
);
// 列显隐信息
const columns = ref([
  {key: 0, label: "编号", visible: true},
  {key: 1, label: "名称", visible: true},
  {key: 2, label: "描述", visible: true},
  {key: 3, label: "状态", visible: true},
  {key: 4, label: "创建人", visible: true},
  {key: 5, label: "创建时间", visible: true},
  {key: 6, label: "操作", visible: true},
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

// 预览相关状态
const previewOpen = ref(false);
const previewTitle = ref("预览 skills");
const previewContent = ref("");
const previewLoading = ref(false);

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
  url: import.meta.env.VITE_APP_BASE_API + "/kb/skills/importData"
});

// 批量导入相关状态
const batchImportOpen = ref(false);
const batchImportTitle = ref("导入 skills");
const batchImportLoading = ref(false);
const batchImportForm = reactive({
  filePath: ""
});
const batchImportFileName = ref("");
const batchImportRules = {
  filePath: [{required: true, message: "请上传 skills 压缩包", trigger: "change"}]
};

const promptPlaceholder = [
  "定义该技能激活时，AI 应该如何行为。参考示例：",
  "",
  "【英文翻译与润色助手】",
  "",
  "角色设定：",
  "你是一位精通中英双语的资深翻译家，擅长将中式英语转化为地道的母语表达。",
  "",
  "执行指令：",
  "1. 检查用户输入的英文，指出语法错误和中式英语表达。",
  "2. 提供 2-3 种不同语境（正式/非正式）的地道改写版本。",
  "3. 解释为什么这样修改更好。",
  "",
  "输出要求：",
  "请保持排版清晰，分段输出，重点修改的部分用【】或引号标出。"
].join('\n');

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    name: null,
    description: null,
    filePath: null,
    createTime: null,
    status: null,
    orderByColumn: "createTime",
    isAsc: "desc"
  },
  rules: {
    name: [
      {required: true, message: "技能名称不能为空", trigger: "blur"},
      {pattern: /^[a-z-]+$/, message: "只能包含小写英文和连字符", trigger: ["blur", "change"]}
    ],
    description: [{required: true, message: "描述不能为空", trigger: "blur"}],
    prompt: [{required: true, message: "指令不能为空", trigger: "blur"}],
  }
});

const {queryParams, form, rules} = toRefs(data);
const formFileName = ref("");

/** 查询skills列表 */
function getList() {
  loading.value = true;
  listSkills(queryParams.value).then(response => {
    skillsList.value = response.data.rows;
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
    description: null,
    prompt: null,
    status: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  formFileName.value = "";
  proxy.resetForm("skillsRef");
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
  title.value = "新增 skills";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getSkills(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改 skills";
  });
}

function handleStatusChange(row) {
  let text = row.status === 1 ? "停用" : "启用";

  proxy.$modal
      .confirm('确认要"' + text + '""' + row.name + '"skills吗?')
      .then(function () {
        return updateSkills(row);
      })
      .then(() => {
        proxy.$modal.msgSuccess(text + "成功");
        handleQuery();
      })
      .catch(function () {
        row.status = row.status === 0 ? 1 : 0;
      });
}

/** 预览按钮操作 */
function handlePreview(row) {
  previewLoading.value = true;
  previewOpen.value = true;
  previewTitle.value = "预览：" + row.name;
  previewSkillMd(row.id).then(response => {
    previewContent.value = response.data || "";
    previewLoading.value = false;
  }).catch(() => {
    previewLoading.value = false;
  });
}

/** 下载按钮操作 */
function handleDownload(row) {
  proxy.$modal.loading("正在下载 skill，请稍候...");
  downloadSkill(row.id).then(blob => {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${row.name}.zip`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
    proxy.$modal.closeLoading();
  }).catch(error => {
    proxy.$modal.closeLoading();
    proxy.$modal.msgError(error.message || '下载失败');
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["skillsRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateSkills(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        }).catch(error => {
        });
      } else {
        addSkills(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除skills编号为"' + _ids + '"的数据项？').then(function () {
    return delSkills(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("system/user/importTemplate", {}, `skills_template_${new Date().getTime()}.xlsx`)
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
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
  getList();
};

/** 导入按钮操作 */
function handleBatchImport() {
  batchImportOpen.value = true;
  batchImportTitle.value = "导入 skills";
  batchImportForm.filePath = "";
  batchImportFileName.value = "";
}

/** 批量导入文件名回显 */
function updateBatchFileName(newValue) {
  batchImportFileName.value = newValue;
}

/** 取消批量导入 */
function cancelBatchImport() {
  batchImportOpen.value = false;
  batchImportFileName.value = "";
}

/** 提交批量导入 */
function submitBatchImport() {
  proxy.$refs["batchImportRef"].validate(async (valid) => {
    if (!valid) return;

    batchImportLoading.value = true;
    try {
      const res = await batchImportSkills({
        fileUrl: batchImportForm.filePath,
        originalFilename: batchImportFileName.value,
        platForm: platForm.value
      });
      const result = res.data || {};
      const successCount = result.successCount || 0;
      const skipCount = result.skipCount || 0;
      const totalCount = result.totalCount || 0;
      const skipInfos = result.skipInfos || [];
      const groupedSkips = {};
      skipInfos.forEach(info => {
        const reason = info.reason || "未知原因";
        if (!groupedSkips[reason]) {
          groupedSkips[reason] = [];
        }
        groupedSkips[reason].push(info.name);
      });
      getList();
      batchImportOpen.value = false;
      const successNames = result.successNames || [];
      let innerContent = `<div style="padding: 10px 20px 0;">导入完成：成功 <b>${successCount}</b> 个，跳过 <b>${skipCount}</b> 个（共 <b>${totalCount}</b> 个）</div>`;
      if (successNames.length > 0) {
        innerContent += `
          <div style="margin-top: 12px; margin-left: 20px;">
            <div style="font-weight: bold;">成功导入：</div>
            <ul style="margin: 4px 0 0; padding-left: 20px; color: #606266;">
              ${successNames.map(name => `<li>${name}</li>`).join('')}
            </ul>
          </div>`;
      }
      const skipReasonKeys = Object.keys(groupedSkips);
      if (skipReasonKeys.length > 0) {
        innerContent += `<div style="margin-top: 12px; font-weight: bold; padding-left: 20px;">跳过原因：</div>`;
        skipReasonKeys.forEach(reason => {
          innerContent += `
            <div style="margin-top: 8px; padding-left: 20px;">
              <div style="font-weight: bold;">${reason}</div>
              <ul style="margin: 4px 0 0; padding-left: 20px; color: #606266;">
                ${groupedSkips[reason].map(name => `<li>${name}</li>`).join('')}
              </ul>
            </div>`;
        });
      }
      const finalMessage = `<div style="overflow: auto; overflow-x: hidden; max-height: 70vh; padding-right: 5px;">${innerContent}</div>`;
      ElMessageBox.alert(finalMessage, "导入结果", {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确定",
        customClass: "batch-import-result-box",
        customStyle: {
          'width': '500px',
          'max-width': '90vw'
        }
      });

    } catch (error) {
      proxy.$modal.msgError(error.message || "批量导入失败");
    } finally {
      batchImportLoading.value = false;
    }
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
  button {
    margin-left: 0;
  }
}
.hint-style {
  display: flex;
  align-items: center;
  .icon {
    color: #efbd47;
    cursor: pointer;
  }
  .text {
    color: #efbd47;
    cursor: pointer;
    font-size: 12px;
  }
}
</style>
