<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                     v-show="showSearch" @submit.prevent>
                <el-form-item label="任务名称" prop="name">
                    <el-input
                            class="el-form-input-width"
                            v-model="queryParams.name"
                            placeholder="请输入任务名称"
                            clearable
                            @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="任务状态" prop="status">
                    <el-select v-model="queryParams.status" style="width: 200px;" placeholder="任务状态" clearable>
                        <el-option
                                v-for="dict in ext_task_status"
                                :key="dict.value"
                                :label="dict.label"
                                :value="parseInt(dict.value)"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发布状态" prop="publishStatus">
                    <el-select v-model="queryParams.publishStatus" style="width: 200px;" placeholder="发布状态" clearable>
                        <el-option
                                v-for="dict in publish_status"
                                :key="dict.value"
                                :label="dict.label"
                                :value="parseInt(dict.value)"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="创建时间" prop="reportTime">
                    <el-date-picker
                            class="el-form-input-width"
                            v-model="daterangeCreateTime"
                            value-format="YYYY-MM-DD"
                            type="daterange"
                            range-separator="-"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            :clearable=false
                    ></el-date-picker>
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
                        <el-button type="primary" plain @click="handleAdd"
                                   v-hasPermi="['ext:extUnstructTask:unstructtask:add']"
                                   @mousedown="(e) => e.preventDefault()">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                                   :columns="columns"></right-toolbar>
                </div>
            </div>
            <el-table stripe height="590px" v-loading="loading" :data="unstructTaskList"
                      @selection-change="handleSelectionChange" :default-sort="defaultSort"
                      @sort-change="handleSortChange">
                <el-table-column v-if="getColumnVisibility(1)" label="ID" align="center" prop="id" />
                <el-table-column v-if="getColumnVisibility(2)" label="任务名称" prop="name" width="300" show-overflow-tooltip>
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" label="任务状态" align="center" prop="status">
                    <template #default="scope">
                        <div>
                            <dict-tag :options="ext_task_status" :value="scope.row.status"/>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" label="发布状态" align="center" prop="publishStatus">
                    <template #default="scope">
                        <div>
                            <dict-tag :options="publish_status" :value="scope.row.publishStatus"/>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(7)" label="发布人" align="center" prop="publishBy">
                    <template #default="scope">
                        {{ scope.row.publishBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" label="发布时间" align="center" prop="publishTime"
                                 sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
                    <template #default="scope">
                        <span>{{ scope.row.publishTime == null ? '-' : parseTime(scope.row.publishTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(10)" label="创建人" align="center" prop="createBy">
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(12)" label="创建时间" align="center" prop="createTime"
                                 width="180" sortable="custom" :sort-orders="['descending', 'ascending']">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(16)" label="备注" align="center" prop="remark">
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                                 width="280">
                    <template #default="scope">
                        <el-button link type="primary" v-if="scope.row.publishStatus != 1" icon="Edit" @click="extraction(scope.row)"
                                   v-hasPermi="['ext:extUnstructTask:unstructtask:edit']">执行
                        </el-button>
                        <el-button link type="primary" v-if="scope.row.publishStatus != 1" icon="Edit" @click="handleUpdate(scope.row)"
                                   v-hasPermi="['ext:extUnstructTask:unstructtask:edit']">编辑
                        </el-button>
                        <el-button link type="primary" v-if="scope.row.status != 1 && scope.row.status == 2" icon="view" @click="handleResult(scope.row)"
                                   v-hasPermi="['ext:extUnstructTask:unstructtask:extractResults']">抽取结果
                        </el-button>
                        <el-button link type="danger" v-if="scope.row.publishStatus != 1" icon="Delete" @click="handleDelete(scope.row)"
                                   v-hasPermi="['ext:extUnstructTask:unstructtask:remove']">删除
                        </el-button>
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

        <!-- 添加或修改非结构化抽取任务对话框 -->
        <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                  {{ title }}
                </span>
            </template>
            <el-form ref="unstructTaskRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="任务名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入任务名称"/>
                        </el-form-item>
                    </el-col>
                    <!--              v-if="title != '修改非结构化抽取任务'"-->
                    <el-col :span="24">
                        <el-form-item label="添加文件" prop="docIds">
                            <div>
                                <el-button :disabled="form.status && form.status != 0" v-on:click="selectDoc" type="primary" plain>
                                  <i class="iconfont icon-upload-cloud-line mr5"></i>导入知识中心文件
                                </el-button>
                            </div>
                        </el-form-item>
                        <div style="margin: 0px 0 10px 80px;">
                          <el-table stripe height="300px" v-loading="loading" :data="docTitles">
                            <el-table-column  width="450" label="文件名称" prop="status" show-overflow-tooltip>
                              <template #default="scope">
                                {{ scope.row.name || '-' }}
                              </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                              <template #default="scope">
                                <el-button link type="danger" icon="Delete" :disabled="form.status && form.status != 0" @click="removeSelectDoc(scope.row)">删除
                                </el-button>
                              </template>
                            </el-table-column>

                            <template #empty>
                              <div class="emptyBg">
                                <img src="@/assets/system/images/no_data/noData.png" alt="" style="width: 100px; height: 100px"/>
                                <p>暂无记录</p>
                              </div>
                            </template>
                          </el-table>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="24">
                    <el-form-item label="三元组" prop="relationIds">
                      <el-button type="primary" @click="addItem" plain>导入三元组</el-button>
                    </el-form-item>
                    <div style="margin: 0px 0 10px 80px;">
                      <el-table style="width: 100%;" :data="form.tableData">
                        <el-table-column label="起点" min-width="180">
                          <template #default="scope">
                            {{ getLabelByValue(scope.row.startSchemaId) || '-' }}
                          </template>
                        </el-table-column>
                        <el-table-column label="关系" min-width="180">
                          <template #default="scope">
                            {{ scope.row.relation || '-' }}
                          </template>
                        </el-table-column>
                        <el-table-column label="终点" min-width="180">
                          <template #default="scope">
                            {{ getLabelByValue(scope.row.endSchemaId) || '-' }}
                          </template>
                        </el-table-column>
                        <!-- 操作列 -->
                        <el-table-column label="操作" min-width="70">
                          <template #default="scope">
                            <el-button size="mini" type="danger" @click="deleteItem(scope.$index, scope.row)" plain>删除 </el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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

        <SelectDoc v-model:visible="visible"
                   @close-dialog="handleChildClose"
                   @confirm="selectConfirmData"
        ></SelectDoc>
        <RelationMultiple ref="relationMultiple" @confirm="relationConfirm"></RelationMultiple>
    </div>
</template>

<script setup name="UnstructTask">
    import {
        listUnstructTask,
        getUnstructTask,
        delUnstructTask,
        addUnstructTask,
        updateUnstructTask,
        executeExtraction
    } from "@/api/ext/extUnstructTask/unstructTask";
    import { getExtSchemaAllList } from "@/api/ext/extSchema/schema";
    import {getToken} from "@/utils/auth.js";
    import moment from 'moment';
    //选择文件 多选
    import SelectDoc from "@/views/kmc/kmcDocument/selection/DocumentMultiple.vue";
    import RelationMultiple from "@/views/ext/extSchemaRelation/selection/relationMultiple.vue";
    import {ref} from "vue";
    import {listByIds} from "../../../api/ext/extSchemaRelation/relation.js";

    const {proxy} = getCurrentInstance();
    const {ext_task_status, publish_status} = proxy.useDict('ext_task_status', 'publish_status');

    const unstructTaskList = ref([]);
    const visible = ref(false);
    const selectOptions = ref([]);

    // 列显隐信息
    const columns = ref([
        {key: 1, label: "ID", visible: true},
        {key: 2, label: "任务名称", visible: true},
        {key: 3, label: "任务状态", visible: true},
        {key: 4, label: "发布状态", visible: true},
        {key: 5, label: "发布时间", visible: true},
        {key: 6, label: "发布人id", visible: true},
        {key: 7, label: "发布人", visible: true},
        {key: 10, label: "创建人", visible: true},
        {key: 12, label: "创建时间", visible: true},
        {key: 16, label: "备注", visible: true}
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
    const docList = ref(null)
    const docTitles = ref(null)
    const docIds = ref(null)

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
        url: import.meta.env.VITE_APP_BASE_API + "/ext/unstructTask/importData"
    });

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            name: null,
            status: null,
            publishStatus: null,
            publishTime: null,
            publisherId: null,
            publishBy: null,
            createTime: null,
            params:{
                beginCreateTime: null,
                endCreateTime: null
            }
        },
        rules: {
            workspaceId: [{required: true, message: "工作区id不能为空", trigger: "blur"}],
            name: [{required: true, message: "任务名称不能为空", trigger: "blur"}],
            docIds: [{ required: true, message: "文件不能为空", trigger: "blur" }],
            relationIds: [{ required: true, message: "三元组不能为空", trigger: "blur" }],
            status: [{required: true, message: "任务状态不能为空", trigger: "change"}],
            publishStatus: [{required: true, message: "发布状态不能为空", trigger: "change"}],
            publishTime: [{required: true, message: "发布时间不能为空", trigger: "blur"}],
            publisherId: [{required: true, message: "发布人id不能为空", trigger: "blur"}],
            validFlag: [{required: true, message: "是否有效不能为空", trigger: "blur"}],
            delFlag: [{required: true, message: "删除标志不能为空", trigger: "blur"}],
            createTime: [{required: true, message: "创建时间不能为空", trigger: "blur"}],
            updateTime: [{required: true, message: "更新时间不能为空", trigger: "blur"}],
        }
    });

    const {queryParams, form, rules} = toRefs(data);

    // 父组件接收子组件发出的事件
    const handleChildClose = () => {
        visible.value = false
    };

    const daterangeCreateTime = ref([])

    //选择文件数据
    const selectConfirmData = (data) => {
        console.log('----选择文件数据-------', data)
        // let titles = data.map(e => e.name).join(',');
        let ids = data.map(e => e.id).join(',');
        docList.value = data
        docTitles.value = data
        form.value.docIds = ids
        console.log('-----docTitles------', docTitles)
        console.log('-----docIds------', docIds)
    };

    watch(
        () => router.currentRoute.value,
        (val) => {
            if (val.query.pageNum) {
                queryParams.value = Object.assign(queryParams.value, {
                    pageNum: val.query.pageNum,
                    pageSize: val.query.pageSize
                });
                getList();
            }
        }
    );

    onMounted(() => {
        queryParams.value.orderByColumn = defaultSort.value.prop;
        queryParams.value.isAsc = defaultSort.value.order;
        getList();
        getAllList();
    });

    function addItem() {
      proxy.$refs["relationMultiple"].open(form.value.tableData);
    }

    function relationConfirm(val) {
      form.value.tableData = val;
      form.value.relationIds = val.map(e=>e.id).join(",")
    }

    /** 查询概念全部数据 */
    function getAllList() {
      getExtSchemaAllList().then(response => {
        selectOptions.value = response.data.map(item => ({
          value: item.id,
          label: item.name,
        }));
      });
    }

    const getLabelByValue = (value) => {
      const selectedOption = selectOptions.value.find(item => item.value === value);
      return selectedOption ? selectedOption.label : '';
    };

    /** 查询非结构化抽取任务列表 */
    function getList() {
        loading.value = true;

        if (null != daterangeCreateTime.value && daterangeCreateTime.value.length == 2) {
            const date = daterangeCreateTime.value
            const endDate = new Date(date[1]);
            endDate.setDate(endDate.getDate() + 1);
            queryParams.value.params.beginCreateTime = date[0];
            queryParams.value.params.endCreateTime = endDate.toISOString().split('T')[0];
            console.log('------queryParams.params-----',queryParams.value)
        }

        listUnstructTask(queryParams.value).then(response => {
            unstructTaskList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    //删除选中的文件
    function removeSelectDoc(item) {
        let data = docTitles.value.filter(doc => doc.id !== item.id);
        docTitles.value = data;
        form.value.docIds = data.map(e => e.id).join(',');
    }

    function selectDoc() {
        console.log('------------选择文件---------------')
        visible.value = !visible.value
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
            status: null,
            publishStatus: null,
            publishTime: null,
            publisherId: null,
            publishBy: null,
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null,
            docIds: null,
          relationIds: null,
            tableData:[]
        };
        proxy.resetForm("unstructTaskRef");
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm("queryRef");
        daterangeCreateTime.value = []
        queryParams.value.params = {}
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
        docTitles.value = null
        form.value.docIds = null
        docList.value = null
        title.value = "添加非结构化抽取任务";
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        if(row.status == 1){
            proxy.$modal.msgError("任务执行中");
            return
        }
        docTitles.value = null
        form.value.docIds = null
        form.value.relationIds = null
        reset();
        const _id = row.id || ids.value
        getUnstructTask(_id).then(response => {
            form.value = response.data;
            let relPageReqVOS = response.data.relPageReqVOS;

            let ids = relPageReqVOS.map(e => e.docId).join(',');
            let data = []
            relPageReqVOS.forEach(e => {
                data.push({
                    id: e.docId,
                    name: e.docName
                })
            })
            docTitles.value = data
            form.value.docIds = ids
            if (form.value.relRelationDOList) {
              listByIds(form.value.relRelationDOList.map(e => e.relationId)).then(rel => {
                form.value.tableData = rel.data;
              })
            } else {
              form.value.tableData = [];
            }
            console.log('---------relPageReqVOS--------', relPageReqVOS)
            open.value = true;
            title.value = "修改非结构化抽取任务";
        });
    }

    // 抽取结果
    function handleResult(row) {
        router.push({
            path: '/ext/extractResults',
            query: {
                id: row.id,
                name: row.name,
                pageType: "2",
                pageNum: queryParams.value.pageNum,
                pageSize: queryParams.value.pageSize
            }
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value
        getUnstructTask(_id).then(response => {
            form.value = response.data;
            openDetail.value = true;
            title.value = "非结构化抽取任务详情";
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs["unstructTaskRef"].validate(valid => {
            if (valid) {
                if (form.value.docIds == null || form.value.docIds == '') {
                    proxy.$modal.msgWarning(`文件不能为空`);
                    return;
                }
                let req = {
                    ...form.value,
                    params: {
                        docIds: form.value.docIds,
                      relationIds: form.value.tableData.map(e=>e.id).join(",")
                    }
                }
                if (form.value.id != null) {
                    req.createTime = formatDate(req.createTime)
                    req.updateTime = formatDate(req.updateTime)
                    req.publishTime = formatDate(req.publishTime)
                    req.params.oldDocRelIds = req.relPageReqVOS.map(e => e.id).join(',');
                    req.relPageReqVOS = null
                    updateUnstructTask(req).then(response => {
                        proxy.$modal.msgSuccess("修改成功");
                        open.value = false;
                        getList();
                    }).catch(error => {
                    });
                } else {
                    addUnstructTask(req).then(response => {
                        proxy.$modal.msgSuccess("新增成功");
                        open.value = false;
                        getList();
                    }).catch(error => {
                    });
                }
            }
        });
    }

    function formatDate(inputDate) {
        if (inputDate) {
            return moment(inputDate).format('YYYY-MM-DD HH:mm:ss');
        }
        return inputDate;
    }

    //执行抽取
    function extraction(row) {
        if(row.status == 1){
            proxy.$modal.msgError("任务执行中");
            return
        }
        proxy.$modal.confirm('是否确认执行任务名称为"' + row.name + '"的数据项？').then(function () {
        }).then(() => {
            executeExtraction({id: row.id}).then(response => {
                console.log('---执行抽取-------', response)
                proxy.$modal.msgSuccess("操作成功,执行中");
                getList();
            }).catch(error => {
            });
        }).catch(() => {
        });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        if(row.status == 1){
            proxy.$modal.msgError("任务执行中");
            return
        }
        const _ids = row.id || ids.value;
        proxy.$modal.confirm('是否确认删除任务名称为"' + row.name + '"的数据项？').then(function () {
            return delUnstructTask(_ids);
        }).then(() => {
            getList();
            proxy.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
    }

    /** 导出按钮操作 */
    function handleExport() {
        proxy.download('ext/unstructTask/export', {
            ...queryParams.value
        }, `unstructTask_${new Date().getTime()}.xlsx`)
    }

    /** ---------------- 导入相关操作 -----------------**/
    /** 导入按钮操作 */
    function handleImport() {
        upload.title = "非结构化抽取任务导入";
        upload.open = true;
    }

    /** 下载模板操作 */
    function importTemplate() {
        proxy.download("system/user/importTemplate", {}, `unstructTask_template_${new Date().getTime()}.xlsx`)
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

    /** ---------------------------------**/

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

</script>
