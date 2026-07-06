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
    <el-dialog
        title="导入关联应用"
        v-model="dialogVisible"
        width="1200px"
        :append-to="$refs['app-container']"
        draggable
        destroy-on-close
        @close="handleClose"
    >
        <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
        >
            <el-form-item label="应用名称" prop="name">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.name"
                    placeholder="请输入应用名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button
                    plain
                    type="primary"
                    @click="handleQuery"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                </el-button>
                <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                </el-button>
            </el-form-item>
        </el-form>

        <el-table
            ref="multipletableRef"
            stripe
            height="420px"
            v-loading="loading"
            :data="dataList"
            reserve-selection
            row-key="id"
            @selection-change="handleSelectionChange"
            @row-click="handleRowClick"
        >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="应用名称" align="center" prop="name">
                <template #default="scope">
                    {{ scope.row.name || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="描述" align="left" prop="description" width="300">
                <template #default="scope">
                    {{ scope.row.description || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="分类" align="center" prop="category">
                <template #default="scope">
                    {{
                        scope.row.category == 1
                            ? '纵向行业应用'
                            : scope.row.category == 0
                              ? '横向通用应用'
                              : '-'
                    }}
                </template>
            </el-table-column>
            <el-table-column label="创建人" align="center" prop="createBy" width="120">
                <template #default="scope">
                    {{ scope.row.createBy || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="120">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagecont-bottom">
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
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="handleConfirm">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="SelectMyApp">
    import { ref, reactive, toRefs, watch, nextTick } from 'vue';
    import { listApply } from '@/api/kac/apply/apply.js';
    import useUserStore from '@/store/system/user.js';

    const { proxy } = getCurrentInstance();
    const userStore = useUserStore();

    const props = defineProps({
        visible: {
            type: Boolean,
            default: false
        },
        // 已选择的id列表，用于回显
        selectedIds: {
            type: [String, Array],
            default: ''
        }
    });

    const emit = defineEmits(['update:visible', 'confirm']);

    const dialogVisible = ref(false);
    const multipletableRef = ref(null);
    const dataList = ref([]);
    const loading = ref(true);
    const showSearch = ref(true);
    const total = ref(0);
    const selectedList = ref([]);

    const data = reactive({
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            name: null,
            myApplyFlag: 1,
            userId: userStore.id,
            orderByColumn: 'createTime',
            isAsc: 'desc'
        }
    });

    const { queryParams } = toRefs(data);

    function getList() {
        loading.value = true;
        listApply(queryParams.value).then((response) => {
            dataList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
            nextTick(() => {
                if (props.selectedIds && multipletableRef.value) {
                    let idsArray = [];
                    if (typeof props.selectedIds === 'string') {
                        idsArray = props.selectedIds
                            .split(',')
                            .map((id) => Number(id))
                            .filter((id) => id);
                    } else if (Array.isArray(props.selectedIds)) {
                        idsArray = props.selectedIds.map((id) => Number(id)).filter((id) => id);
                    }

                    dataList.value.forEach((row) => {
                        if (idsArray.includes(row.id)) {
                            multipletableRef.value.toggleRowSelection(row, true);
                        }
                    });
                }
            });
        });
    }

    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    function handleSelectionChange(selection) {
        selectedList.value = selection;
    }

    function handleRowClick(row) {
        multipletableRef.value.toggleRowSelection(row);
    }

    function handleConfirm() {
        if (selectedList.value.length === 0) {
            proxy.$modal.msgWarning('请至少选择一个关联应用');
            return;
        }
        emit('confirm', selectedList.value);
        handleClose();
    }

    function handleClose() {
        dialogVisible.value = false;
        emit('update:visible', false);
        selectedList.value = [];
    }

    watch(
        () => props.visible,
        (val) => {
            dialogVisible.value = val;
            if (val) {
                getList();
            }
        }
    );
</script>

<style lang="scss" scoped>
    .pagecont-bottom {
        margin-top: 15px;
    }
</style>
