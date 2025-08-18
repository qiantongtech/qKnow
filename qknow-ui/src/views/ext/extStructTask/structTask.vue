<template>
    <el-dialog :title="title" v-model="open" width="1200px" top="5vh" :append-to="$refs['app-container']" draggable>
        <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                  {{ title }}
                </span>
        </template>
        <el-form ref="extStructRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
            <div class="pagecont-top">
                <el-row :gutter="20">
                    <el-col :span="11">
                        <el-form-item label="任务名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入任务名称"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="9">
                        <el-form-item label="数据源" prop="dataSourceId">
                            <el-select @change="changeDataSource" :disabled="structTaskStatus && structTaskStatus != 0" v-model="form.dataSourceId" placeholder="请选择">
                                <el-option
                                        v-for="item in dataSourceList"
                                        :key="item.id"
                                        :label="item.datasourceName"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <div>
                            <el-button type="primary" plain @click="testConnection">测试连接</el-button>
                            <!-- 条件渲染绿色圆形对号图标 -->
                            <div v-if="connectionSuccess" class="success-icon">
                                <el-icon style="color: white;">
                                    <check/>
                                </el-icon>
                            </div>
                            <!-- 条件渲染红色圆形 X 图标 -->
                            <div v-if="connectionError" class="error-icon">
                                <el-icon style="color: white;">
                                    <close/>
                                </el-icon>
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                            <el-input  type="textarea" v-model="form.remark" :rows="2"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="module-header">
                            <span class="module-title">数据映射</span>
                            <div class="header-actions">
                                <el-button type="primary" plain :disabled="structTaskStatus && structTaskStatus != 0" @click="openImportTable">导入表</el-button>
                                <el-icon class="tip-icon" size="20" style="">
                                    <InfoFilled/>
                                </el-icon>
                                <span class="tip-text">注：导入表之前需确保数据库连接信息正确</span>
                            </div>
                        </div>
                        <el-table :data="tableData" max-height="410" >
                            <el-table-column
                                    prop="tableName"
                                    label="表名">
                            </el-table-column>
                            <el-table-column
                                    prop="tableComment"
                                    label="表显示名称">
                                <template #default="scope">
                                    {{ scope.row.tableComment ? scope.row.tableComment : '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="operate"
                                    label="对应概念">
                                <template #default="scope">
                                    {{ scope.row.operate ? scope.row.operate : '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="status"
                                    label="状态"
                                    align="center"
                                    width="180">
                                <template #default="scope">
                                    <div>
                                        <dict-tag :options="ext_mapping_status" :value="scope.row.status"/>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="operate"
                                    align="center"
                                    label="操作"
                                    width="180">
                                <template #default="scope">
                                    <div>
                                        <el-button link type="primary" icon="Edit"
                                                   :disabled="structTaskStatus && structTaskStatus != 0"
                                                   @click="mappingClick(scope.row)">映射
                                        </el-button>
                                        <el-button link type="danger" icon="Delete"
                                                   :disabled="structTaskStatus && structTaskStatus != 0"
                                                   @click="tableDataDeleteClick(scope.row)">
                                            删除
                                        </el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>

                </el-row>
            </div>
            <import-table class="custom-import" v-show="importShow" ref="importRef" @importDataTable="importDataTable"/>
            <Mapping class="custom-mapping" v-show="mappingShow" ref="MappingRef" @ok="mappingOk"/>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="open = false">取 消</el-button>
                <el-button type="primary" size="mini" @click="submitFileForm">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="structTask">
    import {ref} from "vue";
    import { listDaDatasource, clientsTest } from "@/api/da/datasource/daDatasource";
    import {getDaDatasourceTableList} from "@/api/da/datasource/daDatasource";
    import {addExtStructDataMapping,updateExtStructDataMapping, getExtStruct} from "@/api/ext/extStructTask/extStruct"
    //导入表页面
    import importTable from "./importTable.vue";
    //数据映射页面
    import Mapping from "./mapping.vue";

    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const {ext_mapping_status} = proxy.useDict('ext_mapping_status');
    const open = ref(false);
    const title = ref(null)
    const titleId = ref(null)
    const connectionSuccess = ref(false)
    const connectionError = ref(false)
    const taskVisible = ref(false);

    const dataSourceList = ref(null)
    const dataTableList = ref(null)
    // const tableData = ref(null)
    const tableData = ref([])
    const dbTableList = ref([])
    const data = reactive({
        form: {
            dataSourceId: null
        },
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
            datasourceId: null,
            datasourceName: null,
            createTime: null,
        },
        rules: {
            name: [{required: true, message: "任务名称不能为空", trigger: "blur"}],
            dataSourceId: [{required: true, message: "数据源不能为空", trigger: "blur"}],
        }
    });

    const {queryParams, form, rules} = toRefs(data);
    const structTaskStatus = ref(null)
    const queqryInfo = ref({
        id: null
    })

    const emit = defineEmits(["ok","close-dialog"]);

    onMounted(() => {
        getDataSourceList();
    });

    function getDataSourceList() {
        listDaDatasource().then(res => {
            dataSourceList.value = res.data.list
            // 默认选中第一个选项
            if (res.data.list.length > 0) {
                form.value.dataSourceId = res.data.list[0].id;
            }
            console.log('============>', res)
        })
    }

    const getExtStructInfo = async (taskId) => {
        form.value = {
            dataSourceId: null
        }

        await getExtStruct(taskId).then(async (res) => {
            let structTask = res.data.structTask
            structTaskStatus.value = structTask.status

            await getTableName(structTask.datasourceId);

            let formData = {
                id: taskId,
                name: structTask.name,
                dataSourceId: structTask.datasourceId,
                remark: structTask.remark
            }
            form.value = formData;

            //导入过的表
            let tableMappingList = res.data.tableMappingList

            let schemaMappingList = res.data.schemaMappingList
            let attributeMappingList = res.data.attributeMappingList
            let relationMappingList = res.data.relationMappingList
            let customMappingList = res.data.customMappingList

            // attributeMappingList = attributeMappingList.filter(item => item.attributeId != null)

            tableMappingList.forEach(e => {
                e.operate = e.schemaName
                if(!e.tableComment){
                    dbTableList.value.forEach(db => {
                        if (e.tableName == db.tableName) {
                            e.tableComment = db.tableComment
                        }
                    })
                }
                e.status = e.status == true ? 1 : 0

                let mappingData = {}
                // 对应概念
                schemaMappingList.forEach(sch => {
                    if (e.tableName == sch.tableName) {
                        mappingData.concept = sch.schemaId
                        mappingData.conceptName = sch.schemaName
                        mappingData.entityNameField = sch.entityNameField
                        return;
                    }
                })

                // 属性映射
                let attList = []
                let attributeMapping = attributeMappingList.filter(item => e.tableName == item.tableName && item.attributeId)
                attributeMapping.forEach(e => {
                    attList.push({
                        field: e.fieldName,
                        fieldDescription: e.fieldComment,
                        conceptId: e.attributeId,
                        conceptName: e.attributeName
                    })
                })
                mappingData.attributeList = attList

                //关系映射
                let relList = []
                let relationMapping = relationMappingList.filter(item => e.tableName == item.tableName)
                relationMapping.forEach(e => {
                    relList.push({
                        relation: e.relation,
                        field: e.fieldName,
                        associationTable: e.relationTable,
                        associationTableField: e.relationField,
                        associationTableEntityField: e.relationNameField,

                    })
                })
                mappingData.relationshipList = relList

                console.log('---data-----mappingData----', mappingData)
                e.mappingData = mappingData
            })
            console.log('--导入过的表---tableMappingList---', tableMappingList)
            tableData.value = tableMappingList
        })
    }

    const getTableName = async (datasourceId) => {
        await getDaDatasourceTableList(datasourceId).then(res => {
            dbTableList.value = res.data;
        })
    }

    /** 打开导入表弹窗 */
    function openImportTable() {
        proxy.$refs["importRef"].show({dataSourceId: form.value.dataSourceId});
        // if (connectionSuccess.value) {
        //     proxy.$refs["importRef"].show({dataSourceId: form.value.dataSourceId});
        // } else if (connectionError.value) {
        //     proxy.$modal.msgError("数据库连接失败");
        // } else {
        //     proxy.$modal.msgError("请先测试连接数据库");
        // }

    }

    function show(val){
        proxy.resetForm("extStructRef");
        structTaskStatus.value = null
        form.value = {}
        connectionSuccess.value = null
        connectionError.value = null
        tableData.value = []
        title.value = val.title
        if(val.id){
            getExtStructInfo(val.id);
        }else {
            form.value.dataSourceId = dataSourceList.value.length > 0 ? dataSourceList.value[0].id : null
        }
        open.value = true;
    }

    /**
     * 测试连接
     */
    function testConnection() {
        console.log('-------form-', form.value.dataSourceId)
        clientsTest(form.value.dataSourceId).then(res=>{
            if (res && res.code == 200) {
                connectionSuccess.value = true
            } else {
                connectionError.value = true
            }
        })
    }

    /**
     * 切换数据源
     */
    function changeDataSource() {
        connectionSuccess.value = false
        connectionError.value = false
    }

    /**
     * 取消
     */
    const handleBack = () => {
        router.push({
            path: "/ext/extStructTask"
        });
    };

    /**
     * 导入表
     * @param val
     */
    function importDataTable(val) {
        console.log('-------dataTableList---111------', val)
        val.forEach(e => {
            // 检查 tableData 中是否已经包含该 tableName
            const exists = tableData.value.some(item => item.tableName === e.tableName);
            if (!exists) {
                // 如果没有重复的表名，则添加新的表格数据
                tableData.value.push({
                    tableName: e.tableName,
                    tableComment: e.tableComment,
                    operate: null,
                    status: 0,
                    mappingData: null
                });
            }
        })
    }

    /**
     * 映射
     * @param row
     */
    function mappingClick(row) {
        console.log('--点击映射--mappingClick----row---', row)
        let tables = []
        tableData.value.forEach(e => {
            tables.push({
                tableName: e.tableName,
                tableComment: e.tableName + "(" + e.tableComment + ")"
            })
        })
        proxy.$refs["MappingRef"].show({
            title: `数据映射 - ${row.tableName} (${row.tableComment})`,
            dataSourceId: form.value.dataSourceId,
            tableName: row.tableName,
            tableComment: row.tableComment,
            status: row.status,
            tables: tables,
            mappingData: row.mappingData
        });
    }

    /**
     * 删除导入的表
     * @param row
     */
    function tableDataDeleteClick(row) {
        tableData.value = tableData.value.filter(e => e.tableName != row.tableName)
    }

    /**
     * 数据映射确定
     *
     * @param row
     */
    const mappingOk = (val) => {
        console.log('-----mappingData----', val)
        tableData.value.forEach(tableItem => {
            if (tableItem.tableName == val.tableName) {
                tableItem.operate = val.conceptName
                tableItem.status = 1
                tableItem.mappingData = val
            }
        })
    }

    /**
     * 提交结构化抽取任务
     * @param val
     */
    const submitFileForm = () => {
        proxy.$refs["extStructRef"].validate(valid => {
            if (valid) {
                //提交之前测试数据库连通性
                clientsTest(form.value.dataSourceId).then(res=>{
                    if(res && res.code == 200){
                        console.log('---提交结构化抽取任务--tableData----', tableData.value)
                        if(form.value.name.length >= 256){
                            proxy.$modal.msgError("任务名称不能超过256个字!");
                            return;
                        }else if(form.value.remark && form.value.remark.length >= 512) {
                            proxy.$modal.msgError("备注不能超过512个字!");
                            return;
                        }
                        if(form.value.id){
                            let data = tableData.value.filter(e => e.status == 1)
                            updateExtStructDataMapping({
                                taskId: form.value.id,
                                taskName: form.value.name,
                                remark: form.value.remark,
                                dataSourceId: form.value.dataSourceId,
                                tableData: data
                            }).then(res => {
                                if (res && res.code == 200) {
                                    emit("ok",'编辑成功')
                                    open.value = false
                                }
                            })
                        }else {
                            addExtStructDataMapping({
                                taskName: form.value.name,
                                dataSourceId: form.value.dataSourceId,
                                remark: form.value.remark,
                                tableData: tableData.value
                            }).then(res => {
                                if (res && res.code == 200) {
                                    emit("ok",'新增成功')
                                    open.value = false
                                }
                            })
                        }
                    }
                })
            }
        })
    }

    defineExpose({
        show,
    });
</script>

<style scoped>
    /*!* 新增样式 *!*/
    /*.module-block {*/
    /*    border: 1px solid #ebeef5;*/
    /*    border-radius: 4px;*/
    /*    background: #fff;*/
    /*    padding: 10px 20px;*/
    /*    margin-bottom: 20px;*/
    /*    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);*/
    /*}*/

    .module-header {
        /*border-bottom: 1px solid #ebeef5;*/
        padding-bottom: 8px;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .module-title {
        font-size: 16px;
        font-weight: 600;
        color: #212121;
        position: relative;
        padding-left: 12px;
    }

    .module-title::before {
        content: "";
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 6px;
        height: 16px;
        background: var(--el-color-primary);
        border-radius: 2px;
    }

    .header-actions {
        display: flex;
        align-items: center;
        margin: 0 660px 0 0;
    }

    .tip-icon {
        color: #909399;
        font-size: 16px;
        margin-left: 12px;
        margin-right: 2px;
    }

    .tip-text {
        color: #909399;
        font-size: 12px;
    }

    /*!* 调整原有样式 *!*/
    /*.div-title {*/
    /*    display: none; !* 隐藏原有标题 *!*/
    /*}*/

    /*.custom-line {*/
    /*    display: none; !* 隐藏原有分割线 *!*/
    /*}*/

    /*!* 调整表格高度 *!*/
    /*.el-table {*/
    /*    height: 490px !important;*/
    /*    margin-top: 15px;*/
    /*}*/

    /* 调整状态图标位置 */
    .success-icon, .error-icon {
        margin: -28.5px 0px 0px 102px;
        transform: translateY(2px);
        /*display: inline-block;*/
        width: 20px;
        height: 20px;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .success-icon {
        background-color: #0b930b; /* 成功时绿色背景 */
    }

    .error-icon {
        background-color: rgba(246, 2, 2, 0.97); /* 失败时红色背景 */
    }

    .success-icon el-icon, .error-icon el-icon {
        font-size: 16px;
    }

    /*!* 调整按钮容器位置 *!*/
    /*.dialog-footer {*/
    /*    margin-top: 25px;*/
    /*    padding-top: 20px;*/
    /*    !*border-top: 1px solid #ebeef5;*!*/
    /*    text-align: right;*/
    /*}*/

    ::v-deep .custom-import .el-dialog__body {
        height: 630px !important;
    }

    ::v-deep .custom-mapping .el-dialog__body {
        height: 700px !important;
    }
</style>
