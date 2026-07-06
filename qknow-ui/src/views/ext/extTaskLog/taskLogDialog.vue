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
    <el-dialog v-model="logIsShow">
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title"> 执行日志 </span>
        </template>
        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="getList"
                        :columns="columns"
                        :search="false"
                    ></right-toolbar>
                </div>
            </div>
            <el-table
                stripe
                v-loading="loading"
                :data="taskLogList"
                :default-sort="defaultSort"
                :tooltip-options="tooltipOptions"
                @selection-change="handleSelectionChange"
                @sort-change="handleSortChange"
            >
                <el-table-column
                    v-if="getColumnVisibility(0)"
                    label="编号"
                    align="center"
                    prop="id"
                />
                <el-table-column
                    v-if="getColumnVisibility(3)"
                    label="执行状态"
                    align="center"
                    prop="status"
                >
                    <template #default="scope">
                        <div>
                            <dict-tag :options="ext_log_status" :value="scope.row.status" />
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(5)"
                    label="执行开始时间"
                    align="center"
                    prop="startTime"
                    width="180"
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                >
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(6)"
                    label="执行结束时间"
                    align="center"
                    prop="endTime"
                    width="180"
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                >
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    class-name="small-padding fixed-width"
                    width="240"
                >
                    <template #default="scope">
                        <el-button
                            link
                            type="primary"
                            icon="view"
                            @click="handleDetail(scope.row)"
                            v-if="scope.row.errorMsg"
                            >查看错误日志
                        </el-button>

                        <el-button link type="primary" icon="view" @click="showLogDetail(scope.row)"
                            >查看具体步骤
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
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" size="mini" @click="logIsShow = false">关 闭</el-button>
            </div>
        </template>

        <!-- 查看具体步骤 -->
        <el-dialog v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title"> 操作步骤 </span>
            </template>
            <div class="justify-between mb15">
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="showLogDetail()"
                        :columns="detailColumns"
                        :search="false"
                    ></right-toolbar>
                </div>
            </div>

            <el-table
                stripe
                v-loading="detailPageLoading"
                :data="taskLogDetailList"
                @sort-change="handleLogDetailSortChange"
            >
                <el-table-column
                    v-if="getDetailColumnVisibility(10)"
                    label="执行步骤"
                    align="center"
                    prop="step"
                >
                    <template #default="scope">
                        <span>{{ scope.row.step || '-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getDetailColumnVisibility(11)"
                    label="执行时间"
                    align="center"
                    prop="createTime"
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                >
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}')
                        }}</span>
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
                v-show="detailPageTotal > 0"
                :total="detailPageTotal"
                v-model:page="logDetailParams.pageNum"
                v-model:limit="logDetailParams.pageSize"
                @pagination="showLogDetail()"
            />
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" size="mini" @click="cancel">关 闭</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 查看错误消息 -->
        <el-dialog v-model="centerDialogVisible" width="800px" destroy-on-close>
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title"> 错误日志 </span>
            </template>
            <div class="wrap-line">
                {{ errorMsg }}
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" size="mini" @click="centerDialogVisible = false"
                        >关 闭</el-button
                    >
                </div>
            </template>
        </el-dialog>
    </el-dialog>
</template>

<script setup name="ExtTaskLog">
    import { listTaskLog, getDetailPage } from '@/api/ext/extTaskLog/extTaskLog';

    const { proxy } = getCurrentInstance();

    const taskLogList = ref([]);
    const taskLogDetailList = ref([]);
    const { ext_task_type, ext_log_status } = proxy.useDict('ext_task_type', 'ext_log_status');

    // 列显隐信息
    const columns = ref([
        { key: 0, label: 'id', visible: true },
        { key: 3, label: '执行状态', visible: true },
        { key: 5, label: '执行开始时间', visible: true },
        { key: 6, label: '执行结束时间', visible: true }
    ]);
    const detailColumns = ref([
        { key: 10, label: '执行步骤', visible: true },
        { key: 11, label: '执行时间', visible: true }
    ]);

    const getColumnVisibility = (key) => {
        const column = columns.value.find((col) => col.key === key);
        // 如果没有找到对应列配置，默认显示
        if (!column) return true;
        // 如果找到对应列配置，根据visible属性来控制显示
        return column.visible;
    };

    const getDetailColumnVisibility = (key) => {
        const column = detailColumns.value.find((col) => col.key === key);
        if (!column) return true;
        return column.visible;
    };

    const open = ref(false);
    const centerDialogVisible = ref(false);
    const errorMsg = ref('');
    const loading = ref(true);
    const detailPageLoading = ref(true);
    const showSearch = ref(true);
    const total = ref(0);
    const detailPageTotal = ref(0);
    const logId = ref(0);
    const defaultSort = ref({ prop: 'startTime', order: 'desc' });
    const logDetailParams = ref({
        pageNum: 1,
        pageSize: 10,
        logId: null,
        orderByColumn: 'createTime',
        isAsc: 'descending'
    });
    const router = useRouter();
    const taskId = ref(0);
    const taskType = ref(0);
    const logIsShow = ref(false);
    const tooltipOptions = ref({ effect: 'light' });

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            logId: null,
            step: null,
            createTime: null,
            taskName: '',
            orderByColumn: 'startTime',
            isAsc: 'descending'
        }
    });

    const { queryParams, form } = toRefs(data);

    /** 查询抽取任务执行日志详情列表 */
    function getList() {
        loading.value = true;
        queryParams.value.taskId = taskId;
        queryParams.value.taskType = taskType;
        listTaskLog(queryParams.value).then((response) => {
            taskLogList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    function show(taskIds, taskTypes) {
        logIsShow.value = true;
        taskId.value = taskIds;
        taskType.value = taskTypes;
        getList();
    }

    /**
     * 打开日志详情页面弹窗
     * @param row
     */
    function showLogDetail(row) {
        if (row) {
            logId.value = row.id;
        }
        detailPageLoading.value = true;
        logDetailParams.value.logId = logId.value;
        getDetailPage(logDetailParams.value).then((response) => {
            taskLogDetailList.value = response.data.rows;
            detailPageTotal.value = response.data.total;
            detailPageLoading.value = false;
            open.value = true;
        });
    }

    // 取消按钮
    function cancel() {
        open.value = false;
        reset();
    }

    // 表单重置
    function reset() {
        form.value = {
            id: null,
            workspaceId: null,
            logId: null,
            step: null,
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
        proxy.resetForm('taskLogDetailRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 排序触发事件 */
    function handleSortChange(column, prop, order) {
        queryParams.value.orderByColumn = column.prop;
        queryParams.value.isAsc = column.order;
        getList();
    }

    /** 日志详情排序触发事件 */
    function handleLogDetailSortChange(column, prop, order) {
        logDetailParams.value.orderByColumn = column.prop;
        logDetailParams.value.isAsc = column.order;
        showLogDetail();
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        errorMsg.value = row.errorMsg;
        centerDialogVisible.value = true;
    }

    defineExpose({ show });
</script>

<style scoped>
    .wrap-line {
        white-space: pre-wrap;
    }
    .app-container .pagecont-bottom {
        margin: -10px 0px 0px 0px;
        padding: 0px;
    }
    .app-container .justify-between {
        margin-top: -10px;
        margin-bottom: 10px;
    }
</style>
