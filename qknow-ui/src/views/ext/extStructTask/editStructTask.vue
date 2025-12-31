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
                    <el-table :data="tableData" >
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
                                    <el-button link type="danger" icon="Delete" @click="tableDataDeleteClick(scope.row)">
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
    // import {listDatasource, getTestConnection} from "@/api/ext/extDatasource/datasource"
    import { listDaDatasource, clientsTest } from "@/api/da/datasource/daDatasource";
    import {updateExtStructDataMapping, getExtStruct} from "@/api/ext/extStructTask/extStruct"
    // import {getTableList} from "@/api/ext/extDatasource/datasource";
    import {getDaDatasourceTableList} from "@/api/da/datasource/daDatasource";
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

    const dataSourceList = ref(null)
    const dataTableList = ref(null)
    // const tableData = ref(null)
    const tableData = ref([])
    const data = reactive({
        form: {
            dataSourceId: null,
            name: null
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
            // workspaceId: [{required: true, message: "工作区id不能为空", trigger: "blur"}],
            // status: [{required: true, message: "任务状态不能为空", trigger: "change"}],
            // publishStatus: [{required: true, message: "发布状态不能为空", trigger: "change"}],
            // publishTime: [{required: true, message: "发布时间不能为空", trigger: "blur"}],
            // publisherId: [{required: true, message: "发布人id不能为空", trigger: "blur"}],
            // datasourceName: [{required: true, message: "数据源不能为空", trigger: "blur"}],
            // validFlag: [{required: true, message: "是否有效不能为空", trigger: "blur"}],
            // delFlag: [{required: true, message: "删除标志不能为空", trigger: "blur"}],
            // createTime: [{required: true, message: "创建时间不能为空", trigger: "blur"}],
            // updateTime: [{required: true, message: "更新时间不能为空", trigger: "blur"}],
        }
    });

    const queqryInfo = ref({
        id: null
    })
    watch(
        () => router.currentRoute.value.query.id,
        (val) => {
            if (val) {
                queqryInfo.value = router.currentRoute.value.query;
            }
        },
        {immediate: true}
    );

    const {queryParams, form, rules} = toRefs(data);

    onMounted(() => {
        getDataSourceList();
        getExtStructInfo();
    });

    const dbTableList = ref([])
    const getExtStructInfo = async () => {
        form.value = {
            dataSourceId: null
        }

        await getExtStruct(queqryInfo.value.id).then(async (res) => {
            let structTask = res.data.structTask

            await getTableName(structTask.datasourceId);

            let formData = {
                name: structTask.name,
                dataSourceId: structTask.datasourceId,
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

                //自定义映射
                let cusList = []
                let customMapping = customMappingList.filter(item => e.tableName == item.tableName)
                customMapping.forEach(e => {
                    cusList.push({
                        field: e.fieldName,
                        customSQL: e.sqlStatement
                    })
                })
                mappingData.customList = cusList

                console.log('---data-----mappingData----', mappingData)
                e.mappingData = mappingData
            })
            console.log('--导入过的表---tableMappingList---', tableMappingList)
            tableData.value = tableMappingList
        })
    }

    const getTableName = async (datasourceId) => {
        // await getTableList({
        //     tableId: datasourceId
        // }).then(res => {
        //     dbTableList.value = res.data.list;
        // })
        await getDaDatasourceTableList(datasourceId).then(res => {
            dbTableList.value = res.data;
        })
    }

    function getDataSourceList() {
        // listDatasource().then(res => {
        //     dataSourceList.value = res.data.list
        //     console.log('============>', res)
        // })
        listDaDatasource().then(res => {
            dataSourceList.value = res.data.list
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
        // getTestConnection(form.value.dataSourceId).then(res => {
        //     if (res && res.code == 200) {
        //         connectionSuccess.value = true
        //     } else {
        //         connectionError.value = true
        //     }
        // }).catch(res => {
        //     connectionError.value = true
        // })
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
        if (connectionSuccess.value) {
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
        } else if (connectionError.value) {
            proxy.$modal.msgError("数据库连接失败");
        } else {
            proxy.$modal.msgError("请先测试连接数据库");
        }
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
                let data = tableData.value.filter(e => e.status == 1)
                updateExtStructDataMapping({
                    taskName: form.value.name,
                    taskId: queqryInfo.value.id,
                    dataSourceId: form.value.dataSourceId,
                    tableData: data
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