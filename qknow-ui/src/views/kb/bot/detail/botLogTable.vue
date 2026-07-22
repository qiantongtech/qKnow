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
    <el-table
        stripe
        height="58vh"
        v-loading="loading"
        :data="botLogList"
        @sort-change="handleSortChange"
    >
        <el-table-column label="编号" align="center" width="55">
            <template #default="scope">
                <span>{{ scope.row.id }}</span>
            </template>
        </el-table-column>
        <el-table-column
            label="开始时间"
            align="center"
            sortable="custom"
            :sort-orders="sortOrders"
            prop="createTime"
        >
            <template #default="scope">
                {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
            </template>
        </el-table-column>
        <el-table-column
            label="执行状态"
            align="left"
            prop="status"
            width="200"
            sortable="custom"
            :sort-orders="sortOrders"
        >
            <template #default="scope">
                <div>
                    <dict-tag :options="bot_run_status" :value="scope.row.status" />
                </div>
            </template>
        </el-table-column>
        <el-table-column
            label="运行时间"
            align="center"
            prop="runtime"
            width="250"
            sortable="custom"
            :sort-orders="sortOrders"
        >
            <template #default="scope">
                {{ scope.row.runtime + ' ms' || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            label="创建人"
            align="center"
            prop="createBy"
            width="250"
            sortable="custom"
            :sort-orders="sortOrders"
        >
            <template #default="scope">
                <span>{{ scope.row.createBy || '-' }}</span>
            </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            width="300"
            class-name="small-padding fixed-width"
        >
            <template #default="scope">
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                    >详情</el-button
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
        @pagination="queryBotLogPage"
    />

    <el-dialog
        :title="botLogDialogTitle"
        v-model="botLogDialogOpen"
        width="1000px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ botLogDialogTitle }}
            </span>
        </template>
        <el-form ref="botRef" :model="logDetail" label-width="80px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="开始时间" prop="createTime">
                        <el-input
                            v-model="logDetail.createTime"
                            placeholder="请输入 开始时间"
                            readonly
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="执行状态" prop="status">
                        <el-select
                            v-model="logDetail.status"
                            placeholder="请选择执行状态"
                            class="el-form-input-width"
                            style="width: 100%"
                            disabled
                        >
                            <el-option
                                v-for="dict in bot_run_status"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="运行时间" prop="runtime">
                        <el-input
                            v-model="logDetail.runtimeStr"
                            placeholder="请输入 Bot 名称"
                            readonly
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="输入参数" prop="input">
                        <el-input
                            v-model="logDetail.input"
                            type="textarea"
                            readonly
                            placeholder="请输入输入参数"
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="输出结果" prop="output">
                        <el-input
                            v-model="logDetail.output"
                            type="textarea"
                            placeholder="请输入输出结果"
                            readonly
                        />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <div class="check-status-wrapper"></div>
                <div class="button-wrapper">
                    <el-button size="mini" @click="botLogDialogOpen = false">关 闭</el-button>
                </div>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="botLogTable">
    import { botLogPage } from '@/api/kb/bot/botLog.js';

    const { proxy } = getCurrentInstance();
    const { bot_run_status } = proxy.useDict('bot_run_status');

    const botLogList = ref([]);
    const botLogDialogTitle = ref('');
    const botLogDialogOpen = ref(false);
    const sortOrders = ['descending', 'ascending'];
    const logDetail = ref(null);

    const loading = ref(true);
    const total = ref(0);
    const title = ref('');
    const queryParams = ref({
        pageNum: 1,
        pageSize: 10,
        workspaceId: null,
        botId: null
    });

    const props = defineProps({
        botId: {
            type: Number,
            required: true
        }
    });

    // 监听父组件传递的Bot列表变化
    watch(
        () => props.botId,
        (newVal) => {
            if (newVal) {
                queryBotLogPage();
            }
        },
        { immediate: true }
    );

    function queryBotLogPage() {
        if (!props.botId) return;
        loading.value = true;
        queryParams.value.botId = props.botId;
        botLogPage(queryParams.value).then((response) => {
            botLogList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    /** 排序触发事件 */
    function handleSortChange(column) {
        queryParams.value.orderByColumn = column.prop;
        queryParams.value.isAsc = column.order;
        queryBotLogPage();
    }

    function handleDetail(row) {
        botLogDialogTitle.value = '执行日志详情';
        botLogDialogOpen.value = true;
        logDetail.value = row;
        logDetail.value.status = row.status + '';
        logDetail.value.runtimeStr = row.runtime + ' ms';
    }
</script>
