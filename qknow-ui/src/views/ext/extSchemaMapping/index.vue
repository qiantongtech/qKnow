<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
     <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
      <el-form-item label="工作区id" prop="workspaceId">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.workspaceId"
            placeholder="请输入工作区id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务id" prop="taskId">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.taskId"
            placeholder="请输入任务id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.tableName"
            placeholder="请输入表名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表显示名称" prop="tableComment">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.tableComment"
            placeholder="请输入表显示名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="概念id" prop="schemaId">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.schemaId"
            placeholder="请输入概念id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="概念名称" prop="schemaName">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.schemaName"
            placeholder="请输入概念名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker class="el-form-input-width"
            clearable
            v-model="queryParams.createTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
        </el-date-picker>
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
           <el-button type="primary" plain @click="handleAdd" v-hasPermi="['ext:extSchemaMapping:schema:add']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xinzeng mr5"></i>新增
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['ext:extSchemaMapping:schema:edit']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['ext:extSchemaMapping:schema:remove']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="info" plain  @click="handleImport" v-hasPermi="['ext:extSchemaMapping:schema:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="warning" plain @click="handleExport" v-hasPermi="['ext:extSchemaMapping:schema:export']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-download-line mr5"></i>导出
           </el-button>
         </el-col>
       </el-row>
       <div class="justify-end top-right-btn">
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
       </div>
     </div>
     <el-table stripe height="590px" v-loading="loading" :data="schemaList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
       <el-table-column type="selection" width="55" align="center" />
       <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />
       <el-table-column v-if="getColumnVisibility(1)" label="工作区id" align="center" prop="workspaceId">
         <template #default="scope">
           {{ scope.row.workspaceId || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(2)" label="任务id" align="center" prop="taskId">
         <template #default="scope">
           {{ scope.row.taskId || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(3)" label="表名" align="center" prop="tableName">
         <template #default="scope">
           {{ scope.row.tableName || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(4)" label="表显示名称" align="center" prop="tableComment">
         <template #default="scope">
           {{ scope.row.tableComment || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(5)" label="概念id" align="center" prop="schemaId">
         <template #default="scope">
           {{ scope.row.schemaId || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(6)" label="概念名称" align="center" prop="schemaName">
         <template #default="scope">
           {{ scope.row.schemaName || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(9)" label="创建人" align="center" prop="createBy">
         <template #default="scope">
           {{ scope.row.createBy || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(11)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
         <template #default="scope">
           <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(15)" label="备注" align="center" prop="remark">
         <template #default="scope">
           {{ scope.row.remark || '-' }}
         </template>
       </el-table-column>
       <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
         <template #default="scope">
           <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                      v-hasPermi="['ext:extSchemaMapping:schema:edit']">修改</el-button>
           <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                      v-hasPermi="['ext:extSchemaMapping:schema:remove']">删除</el-button>
           <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                      v-hasPermi="['ext:extSchemaMapping:schema:edit']">详情</el-button>
           <el-button link type="primary" icon="view" @click="routeTo('/ext/extSchemaMapping/schemaDetail',scope.row)"
                      v-hasPermi="['ext:extSchemaMapping:schema:edit']">复杂详情</el-button>
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

    <!-- 添加或修改概念映射对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="schemaRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="工作区id" prop="workspaceId">
                <el-input v-model="form.workspaceId" placeholder="请输入工作区id" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="任务id" prop="taskId">
                <el-input v-model="form.taskId" placeholder="请输入任务id" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="表名" prop="tableName">
                <el-input v-model="form.tableName" placeholder="请输入表名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="表显示名称" prop="tableComment">
                <el-input v-model="form.tableComment" placeholder="请输入表显示名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="概念id" prop="schemaId">
                <el-input v-model="form.schemaId" placeholder="请输入概念id" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="概念名称" prop="schemaName">
                <el-input v-model="form.schemaName" placeholder="请输入概念名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
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

    <!-- 概念映射详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="schemaRef" :model="form"  label-width="80px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="工作区id" prop="workspaceId">
                <div>
                  {{ form.workspaceId }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="任务id" prop="taskId">
                <div>
                  {{ form.taskId }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="表名" prop="tableName">
                <div>
                  {{ form.tableName }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="表显示名称" prop="tableComment">
                <div>
                  {{ form.tableComment }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="概念id" prop="schemaId">
                <div>
                  {{ form.schemaId }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="概念名称" prop="schemaName">
                <div>
                  {{ form.schemaName }}
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的概念映射数据
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

<script setup name="Schema">
  import { listSchema, getSchema, delSchema, addSchema, updateSchema } from "@/api/ext/extSchemaMapping/schema";
  import {getToken} from "@/utils/auth.js";

  const { proxy } = getCurrentInstance();

  const schemaList = ref([]);

  // 列显隐信息
  const columns = ref([
            { key: 1, label: "工作区id", visible: true },
            { key: 2, label: "任务id", visible: true },
            { key: 3, label: "表名", visible: true },
            { key: 4, label: "表显示名称", visible: true },
            { key: 5, label: "概念id", visible: true },
            { key: 6, label: "概念名称", visible: true },
            { key: 9, label: "创建人", visible: true },
            { key: 11, label: "创建时间", visible: true },
            { key: 15, label: "备注", visible: true }
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
    url: import.meta.env.VITE_APP_BASE_API + "/ext/schema/importData"
  });

  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
        workspaceId: null,
        taskId: null,
        tableName: null,
        tableComment: null,
        schemaId: null,
        schemaName: null,
        createTime: null,
    },
    rules: {
        workspaceId: [{ required: true, message: "工作区id不能为空", trigger: "blur" }],
        taskId: [{ required: true, message: "任务id不能为空", trigger: "blur" }],
        tableName: [{ required: true, message: "表名不能为空", trigger: "blur" }],
        validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
        delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
        createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
        updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询概念映射列表 */
  function getList() {
    loading.value = true;
    listSchema(queryParams.value).then(response => {
            schemaList.value = response.data.rows;
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
        taskId: null,
        tableName: null,
        tableComment: null,
        schemaId: null,
        schemaName: null,
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
    proxy.resetForm("schemaRef");
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
    title.value = "添加概念映射";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getSchema(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改概念映射";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getSchema(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "概念映射详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["schemaRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateSchema(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addSchema(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除概念映射编号为"' + _ids + '"的数据项？').then(function() {
      return delSchema(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('ext/schema/export', {
      ...queryParams.value
    }, `schema_${new Date().getTime()}.xlsx`)
  }

  /** ---------------- 导入相关操作 -----------------**/
  /** 导入按钮操作 */
  function handleImport() {
    upload.title = "概念映射导入";
    upload.open = true;
  }

  /** 下载模板操作 */
  function importTemplate() {
    proxy.download("system/user/importTemplate", {
    }, `schema_template_${new Date().getTime()}.xlsx`)
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
