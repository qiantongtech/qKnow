<!--
  /*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */
-->

<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="genApiKey"
                    icon="Plus"
                    @mousedown="(e) => e.preventDefault()"
                    class="custom-btn-padding"
                >
                    创建密钥
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar :search="false" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>
    <el-table
        stripe
        height="58vh"
        v-loading="loading"
        :data="botApiKeyList"
        @sort-change="handleSortChange"
    >
        <el-table-column
            label="编号"
            prop="id"
            align="center"
            width="85"
            sortable="custom"
            :sort-orders="sortOrders"
        >
            <template #default="scope">
                <span>{{ scope.row.id }}</span>
            </template>
        </el-table-column>
        <el-table-column label="访问地址" align="center" prop="origin">
            <template #default="scope">
                {{ origin + '/v1' }}
            </template>
        </el-table-column>
        <el-table-column label="访问密钥" align="center" prop="status" width="350">
            <template #default="scope">
                {{ scope.row.apiKey || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            label="创建人"
            align="center"
            prop="createBy"
            width="200"
            sortable="custom"
            :sort-orders="sortOrders"
        >
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="250"
            sortable="custom"
            :sort-orders="sortOrders"
        >
            <template #default="scope">
                {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}
            </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            width="300"
            class-name="small-padding fixed-width"
        >
            <template #default="scope">
                <el-button link type="primary" icon="CopyDocument" @click="handleCopy(scope.row)"
                    >复制密钥</el-button
                >
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">
                    删除</el-button
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
        @pagination="queryApiKeyPage"
    />
</template>

<script setup name="botLogTable">
    import { apiKeyPage, delBotApiKey } from '@/api/kb/bot/botApikey.js';
    import { genBotApiKey } from '@/api/kb/bot/botApikey.js';

    const { proxy } = getCurrentInstance();
    const { bot_run_status } = proxy.useDict('bot_run_status');

    const botApiKeyList = ref([]);
    const sortOrders = ['descending', 'ascending'];
    const origin = window.location.origin;

    const loading = ref(true);
    const total = ref(0);
    const open = ref(false);
    const openDetail = ref(false);
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
                queryApiKeyPage();
            }
        },
        { immediate: true }
    );

    function queryApiKeyPage() {
        if (!props.botId) return;
        loading.value = true;
        queryParams.value.botId = props.botId;
        apiKeyPage(queryParams.value).then((response) => {
            botApiKeyList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    /** 排序触发事件 */
    function handleSortChange(column) {
        queryParams.value.orderByColumn = column.prop;
        queryParams.value.isAsc = column.order;
        queryApiKeyPage();
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id;
        proxy.$modal
            .confirm('是否确认删除编号为"' + _ids + '"的密钥？')
            .then(function () {
                return delBotApiKey(_ids);
            })
            .then(() => {
                queryApiKeyPage();
                proxy.$modal.msgSuccess('删除成功');
            });
    }

    /** 复制按钮操作 */
    function handleCopy(row) {
        copyText(row.apiKey);
    }

    /**
     * 复制文本
     */
    const copyText = async (copyContent) => {
        try {
            await navigator.clipboard.writeText(copyContent);
            proxy.$modal.msgSuccess('复制成功');
        } catch (err) {
            proxy.$modal.msgError('复制失败');
        }
    };

    /**
     * 生成访问密钥
     */
    function genApiKey() {
        proxy.$modal.confirm('确认生成新的访问密钥？').then(() => {
            genBotApiKey(props.botId)
                .then((response) => {
                    proxy.$modal.msgSuccess('生成成功');
                })
                .then(() => {
                    queryApiKeyPage();
                });
        });
    }

    defineExpose({
        queryApiKeyPage
    });
</script>
