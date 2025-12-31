<!--
  Copyright © 2026 Qiantong Technology Co., Ltd.
  qKnow Knowledge Platform
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qknow.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2026 江苏千桐科技有限公司
  qKnow 知识平台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qknow.qiantong.tech/business.html
-->

<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
     <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px" v-show="showSearch" @submit.prevent>
      <el-form-item label="连接名称" prop="name">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            placeholder="请输入连接名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="host">
        <el-input
            class="el-form-input-width"
            v-model="queryParams.host"
            placeholder="请输入地址"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="端口号" prop="port">-->
<!--        <el-input-->
<!--            class="el-form-input-width"-->
<!--            v-model="queryParams.port"-->
<!--            placeholder="请输入端口号"-->
<!--            clearable-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="用户名" prop="username">-->
<!--        <el-input-->
<!--            class="el-form-input-width"-->
<!--            v-model="queryParams.username"-->
<!--            placeholder="请输入用户名"-->
<!--            clearable-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="密码" prop="password">-->
<!--        <el-input-->
<!--            class="el-form-input-width"-->
<!--            v-model="queryParams.password"-->
<!--            placeholder="请输入密码"-->
<!--            clearable-->
<!--            @keyup.enter="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
           <el-button type="primary" plain @click="handleAdd" v-hasPermi="['ext:extDatasource:datasource:add']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xinzeng mr5"></i>新增
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['ext:extDatasource:datasource:edit']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
           </el-button>
         </el-col>
         <el-col :span="1.5">
           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['ext:extDatasource:datasource:remove']"
                      @mousedown="(e) => e.preventDefault()">
             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
           </el-button>
         </el-col>
<!--         <el-col :span="1.5">-->
<!--           <el-button type="info" plain  @click="handleImport" v-hasPermi="['ext:extDatasource:datasource:export']"-->
<!--                      @mousedown="(e) => e.preventDefault()">-->
<!--             <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入-->
<!--           </el-button>-->
<!--         </el-col>-->
<!--         <el-col :span="1.5">-->
<!--           <el-button type="warning" plain @click="handleExport" v-hasPermi="['ext:extDatasource:datasource:export']"-->
<!--                      @mousedown="(e) => e.preventDefault()">-->
<!--             <i class="iconfont-mini icon-download-line mr5"></i>导出-->
<!--           </el-button>-->
<!--         </el-col>-->
       </el-row>
       <div class="justify-end top-right-btn">
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
       </div>
     </div>
     <el-table stripe height="590px" v-loading="loading" :data="datasourceList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
       <el-table-column type="selection" width="55" align="center" />
<!--       <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
       <el-table-column v-if="getColumnVisibility(1)" label="连接名称" align="center" prop="name">
         <template #default="scope">
           {{ scope.row.name || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(2)" label="数据库类型" align="center" prop="type">
         <template #default="scope">
<!--           {{ scope.row.type || '-' }}-->
           <dict-tag :options="ext_data_source_type" :value="scope.row.type"/>
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(3)" label="地址" align="center" width="120px" prop="host">
         <template #default="scope">
           {{ scope.row.host || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(4)" label="端口号" align="center" prop="port">
         <template #default="scope">
           {{ scope.row.port || '-' }}
         </template>
       </el-table-column>
         <el-table-column v-if="getColumnVisibility(5)" label="数据库名称" align="center" prop="databaseName">
             <template #default="scope">
                 {{ scope.row.databaseName || '-' }}
             </template>
         </el-table-column>
       <el-table-column v-if="getColumnVisibility(5)" label="用户名" align="center" prop="username">
         <template #default="scope">
           {{ scope.row.username || '-' }}
         </template>
       </el-table-column>
<!--       <el-table-column v-if="getColumnVisibility(6)" label="密码" align="center" prop="password">-->
<!--         <template #default="scope">-->
<!--           {{ scope.row.password || '-' }}-->
<!--         </template>-->
<!--       </el-table-column>-->
       <el-table-column v-if="getColumnVisibility(7)" label="连接状态" align="center" prop="status">
         <template #default="scope">
<!--           {{ scope.row.status || '-' }}-->
           <dict-tag :options="ext_data_source_connection_status" :value="scope.row.status"/>
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(10)" label="创建人" align="center" prop="createBy">
         <template #default="scope">
           {{ scope.row.createBy || '-' }}
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(12)" label="创建时间" align="center" prop="createTime" width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
         <template #default="scope">
           <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
         </template>
       </el-table-column>
       <el-table-column v-if="getColumnVisibility(16)" label="备注" align="center" prop="remark">
         <template #default="scope">
           {{ scope.row.remark || '-' }}
         </template>
       </el-table-column>
       <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
         <template #default="scope">
           <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                      v-hasPermi="['ext:extDatasource:datasource:edit']">修改</el-button>
           <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                      v-hasPermi="['ext:extDatasource:datasource:remove']">删除</el-button>
           <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                      v-hasPermi="['ext:extDatasource:datasource:edit']">详情</el-button>
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

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="datasourceRef" :model="form" :rules="rules" label-width="90px" @submit.prevent>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="连接名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入连接名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="类型" prop="name">
                <el-select v-model="form.type" placeholder="请选择" filterable>
                  <el-option
                          v-for="dict in ext_data_source_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="地址" prop="host">
                <el-input v-model="form.host" placeholder="请输入地址" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口号" prop="port">
                <el-input v-model="form.port" placeholder="请输入端口号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" placeholder="请输入密码" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
              <el-col :span="12">
                  <el-form-item label="数据库名称" prop="databaseName">
                      <el-input v-model="form.databaseName" placeholder="请输入数据库名称" />
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

    <!-- 数据源详情对话框 -->
    <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="datasourceRef" :model="form"  label-width="80px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="连接名称" prop="name">
                <div>
                  {{ form.name }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="数据库类型" prop="type">
                <div>
                  <dict-tag :options="ext_data_source_type" :value="form.type"/>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="地址" prop="host">
                <div>
                  {{ form.host }}
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="端口号" prop="port">
                <div>
                  {{ form.port }}
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <div>
                  {{ form.username }}
                </div>
              </el-form-item>
            </el-col>
<!--            <el-col :span="12">-->
<!--              <el-form-item label="密码" prop="password">-->
<!--                <div>-->
<!--                  {{ form.password }}-->
<!--                </div>-->
<!--              </el-form-item>-->
<!--            </el-col>-->
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="连接状态" prop="status">
                <div>
                  {{ form.status }}
                </div>
              </el-form-item>
            </el-col>
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
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据源数据
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

<script setup name="Datasource">
  import { listDatasource, getDatasource, delDatasource, addDatasource, updateDatasource } from "@/api/ext/extDatasource/datasource";
  import {getToken} from "@/utils/auth.js";

  const { proxy } = getCurrentInstance();
  const {ext_data_source_type,ext_data_source_connection_status} = proxy.useDict("ext_data_source_type","ext_data_source_connection_status");

  const datasourceList = ref([]);

  // 列显隐信息
  const columns = ref([
            { key: 1, label: "连接名称", visible: true },
            { key: 2, label: "数据库类型", visible: true },
            { key: 3, label: "地址", visible: true },
            { key: 4, label: "端口号", visible: true },
            { key: 5, label: "用户名", visible: true },
            // { key: 6, label: "密码", visible: true },
            { key: 7, label: "连接状态", visible: true },
            { key: 10, label: "创建人", visible: true },
            { key: 12, label: "创建时间", visible: true },
            { key: 16, label: "备注", visible: true }
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
    url: import.meta.env.VITE_APP_BASE_API + "/ext/datasource/importData"
  });

  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
        name: null,
        type: null,
        host: null,
        port: null,
        username: null,
        password: null,
        status: null,
        createTime: null,
    },
    rules: {
        name: [{ required: true, message: "连接名称不能为空", trigger: "blur" }],
        type: [{ required: true, message: "数据库类型不能为空", trigger: "change" }],
        host: [{ required: true, message: "地址不能为空", trigger: "blur" }],
        port: [{ required: true, message: "端口号不能为空", trigger: "blur" }],
        status: [{ required: true, message: "连接状态不能为空", trigger: "change" }],
        validFlag: [{ required: true, message: "是否有效不能为空", trigger: "blur" }],
        delFlag: [{ required: true, message: "删除标志不能为空", trigger: "blur" }],
        createTime: [{ required: true, message: "创建时间不能为空", trigger: "blur" }],
        updateTime: [{ required: true, message: "更新时间不能为空", trigger: "blur" }],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询数据源列表 */
  function getList() {
    loading.value = true;
    listDatasource(queryParams.value).then(response => {
            datasourceList.value = response.data.rows;
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
        type: null,
        host: null,
        port: null,
        username: null,
        password: null,
        databaseName: null,
        status: null,
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
    proxy.resetForm("datasourceRef");
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
    title.value = "添加数据源";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getDatasource(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改数据源";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getDatasource(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "数据源详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["datasourceRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateDatasource(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addDatasource(form.value).then(response => {
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
    proxy.$modal.confirm('是否确认删除数据源编号为"' + _ids + '"的数据项？').then(function() {
      return delDatasource(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('ext/datasource/export', {
      ...queryParams.value
    }, `datasource_${new Date().getTime()}.xlsx`)
  }

  /** ---------------- 导入相关操作 -----------------**/
  /** 导入按钮操作 */
  function handleImport() {
    upload.title = "数据源导入";
    upload.open = true;
  }

  /** 下载模板操作 */
  function importTemplate() {
    proxy.download("system/user/importTemplate", {
    }, `datasource_template_${new Date().getTime()}.xlsx`)
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
