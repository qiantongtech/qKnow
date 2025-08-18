<template>
    <!-- 仅修改模板结构部分 -->
    <div class="app-container" ref="app-container">
        <div class="pagecont-top">
            <el-form ref="extStructRef" :model="form" :rules="rules" style="width: 75%;" label-width="100px">
                <!-- 结构化抽取任务区块 -->
                <div class="module-block">
                    <div class="module-header">
                        <span class="module-title">结构化抽取任务</span>
                    </div>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="任务名称" prop="name">
                                <el-input v-model="form.name" placeholder="请输入任务名称"/>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-form-item label="数据源" prop="dataSourceId">
                                <el-select @change="changeDataSource" v-model="form.dataSourceId" placeholder="请选择">
                                    <el-option
                                            v-for="item in dataSourceList"
                                            :key="item.id"
                                            :label="item.datasourceName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>

                        <el-col :span="12">
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
                </div>

                <!-- 数据映射区块 -->
                <div class="module-block" style="margin-top: 15px;">
                    <div class="module-header">
                        <span class="module-title">数据映射</span>
                        <div class="header-actions">
                            <el-button type="primary" plain @click="openImportTable">导入表</el-button>
                            <el-icon class="tip-icon" size="20" style="">
                                <InfoFilled/>
                            </el-icon>
                            <span class="tip-text">注：导入表之前需确保数据库连接信息正确</span>
                        </div>
                    </div>
                    <el-table :data="tableData">
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
                                    <el-button link type="primary" icon="Edit" @click="mappingClick(scope.row)">映射
                                    </el-button>
                                    <el-button link type="danger" icon="Delete"
                                               @click="tableDataDeleteClick(scope.row)">
                                        删除
                                    </el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>

                <!-- 底部按钮保持原位 -->
                <div class="dialog-footer" style="width: 30%;float: right;margin: -75px -163px 0px 0px;">
                    <el-button @click="handleBack">取 消</el-button>
                    <el-button type="primary" @click="submitFileForm">确 定</el-button>
                </div>
            </el-form>
        </div>
        <import-table v-show="importShow" ref="importRef" @importDataTable="importDataTable"/>
        <Mapping class="custom-mapping" v-show="mappingShow" ref="MappingRef" @ok="mappingOk"/>
    </div>
</template>

<script setup name="addStructTask">
    import {ref} from "vue";
    import {listDatasource, getTestConnection} from "@/api/ext/extDatasource/datasource"
    import { listDaDatasource, clientsTest } from "@/api/da/datasource/daDatasource";
    import {addExtStructDataMapping} from "@/api/ext/extStructTask/extStruct"
    //导入表页面
    import importTable from "./importTable.vue";
    //数据映射页面
    import Mapping from "./mapping.vue";

    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const {ext_mapping_status} = proxy.useDict('ext_mapping_status');
    const open = ref(true);
    const title = ref('temp')
    const titleId = ref(null)
    const connectionSuccess = ref(false)
    const connectionError = ref(false)
    const importShow = ref(false)
    const mappingShow = ref(false)

    const dataSourceList = ref(null)
    const dataTableList = ref(null)
    // const tableData = ref(null)
    const tableData = ref([])
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

    onMounted(() => {
        getDataSourceList();
    });

    function getDataSourceList() {
        // listDatasource().then(res => {
        //     dataSourceList.value = res.data.list
        //     // 默认选中第一个选项
        //     if (res.data.list.length > 0) {
        //         form.value.dataSourceId = res.data.list[0].id;
        //     }
        //     console.log('============>', res)
        // })
        listDaDatasource().then(res => {
            dataSourceList.value = res.data.list
            // 默认选中第一个选项
            if (res.data.list.length > 0) {
                form.value.dataSourceId = res.data.list[0].id;
            }
            console.log('============>', res)
        })
    }

    /** 打开导入表弹窗 */
    function openImportTable() {
        if (connectionSuccess.value) {
            proxy.$refs["importRef"].show({dataSourceId: form.value.dataSourceId});
        } else if (connectionError.value) {
            proxy.$modal.msgError("数据库连接失败");
        } else {
            proxy.$modal.msgError("请先测试连接数据库");
        }

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
                console.log('---提交结构化抽取任务--tableData----', tableData.value)
                // let data = tableData.value.filter(e => e.status == 1)
                addExtStructDataMapping({
                    taskName: form.value.name,
                    dataSourceId: form.value.dataSourceId,
                    tableData: tableData.value
                }).then(res => {
                    if (res && res.code == 200) {
                        //添加结构化抽取任务成功
                        router.push({
                            path: "/ext/extStructTask"
                        });
                    }
                })
            }
        })
    }
</script>

<style scoped>
    /* 新增样式 */
    .module-block {
        border: 1px solid #ebeef5;
        border-radius: 4px;
        background: #fff;
        padding: 10px 20px;
        margin-bottom: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }

    .module-header {
        border-bottom: 1px solid #ebeef5;
        padding-bottom: 8px;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .module-title {
        font-size: 16px;
        font-weight: 600;
        color: var(--el-color-primary);
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

    /* 调整原有样式 */
    .div-title {
        display: none; /* 隐藏原有标题 */
    }

    .custom-line {
        display: none; /* 隐藏原有分割线 */
    }

    /* 调整表格高度 */
    .el-table {
        height: 490px !important;
        margin-top: 15px;
    }

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

    /* 调整按钮容器位置 */
    .dialog-footer {
        margin-top: 25px;
        padding-top: 20px;
        /*border-top: 1px solid #ebeef5;*/
        text-align: right;
    }

    ::v-deep .custom-mapping .el-dialog__body {
        height: 700px !important;
    }
</style>