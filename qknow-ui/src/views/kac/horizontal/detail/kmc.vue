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
    <!--  <div class="justify-between mb15">-->
    <!--    <el-row :gutter="15" class="btn-style">-->
    <!--    </el-row>-->
    <!--    <div class="justify-end top-right-btn">-->
    <!--      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>-->
    <!--    </div>-->
    <!--  </div>-->
    <el-table
        stripe
        height="58vh"
        v-loading="loading"
        :data="knowledgeList"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column v-if="getColumnVisibility(1)" label="编号" align="center" width="55">
            <template #default="scope">
                <span>{{ scope.$index + 1 }}</span>
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(2)"
            label="名称"
            align="center"
            prop="knowledgeId"
            width="250"
        >
            <template #default="scope">
                {{ getKnowledgeInfo(scope.row.knowledgeId, 'name') }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(3)"
            label="挂载说明"
            align="left"
            prop="description"
            width="350"
        >
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(4)"
            label="标签"
            align="center"
            prop="knowledgeId"
        >
            <template #default="scope">
                <div class="multiline-ellipsis">
                    <el-tag
                        v-for="(tag, tagIndex) in getTags(
                            getKnowledgeInfo(scope.row.knowledgeId, 'tags')
                        )"
                        :key="tagIndex"
                        class="card-tag"
                    >
                        {{ tag.name }}
                    </el-tag>
                    <span
                        v-if="getTags(getKnowledgeInfo(scope.row.knowledgeId, 'tags')).length <= 0"
                        >-</span
                    >
                </div>
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(7)"
            label="创建人"
            align="center"
            prop="createBy"
        />
        <el-table-column
            v-if="getColumnVisibility(8)"
            label="创建时间"
            align="center"
            prop="createTime"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(8)"
            label="最后更新时间"
            align="center"
            prop="updateTime"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            v-if="props.source === 'myApp'"
        >
            <template #default="scope">
                <el-button link type="primary" icon="Switch" @click="handleUpdate(scope.row)"
                    >替换</el-button
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
        @pagination="getList"
    />

    <!-- 选择知识库对话框 -->
    <el-dialog
        :title="knowledgeDialogTitle"
        v-model="knowledgeDialogOpen"
        width="1000px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ knowledgeDialogTitle }}
            </span>
        </template>
        <!--知识库数据-->
        <el-form
            class="btn-style"
            :model="queryParamsKnowledge"
            ref="queryKnowledgeRef"
            :inline="true"
        >
            <el-form-item label="知识库名称" prop="name">
                <el-input
                    class="el-form-input-width"
                    v-model="queryParamsKnowledge.name"
                    placeholder="请输入知识库名称"
                    clearable
                    @keyup.enter="handleQueryKnowledge"
                />
            </el-form-item>
            <el-form-item>
                <el-button
                    plain
                    type="primary"
                    @click="handleQueryKnowledge"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                </el-button>
                <el-button @click="resetQueryKnowledge" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                </el-button>
            </el-form-item>
        </el-form>
        <el-table
            stripe
            ref="knowledgeTableRef"
            v-loading="knowledgeLoading"
            :data="knowledgeSelectList"
            row-key="id"
            :row-class-name="getTableRowClassName"
            highlight-current-row
            @row-click="handleKnowledgeRowClick"
            style="width: 1000px"
        >
            <el-table-column
                label="知识库名称"
                prop="name"
                align="center"
                :show-overflow-tooltip="true"
                width="200"
            />
            <el-table-column
                label="描述"
                prop="description"
                align="left"
                :show-overflow-tooltip="true"
                width="280"
            />
            <el-table-column
                label="标签"
                prop="tags"
                align="center"
                :show-overflow-tooltip="true"
                width="180"
            >
                <template #default="scope">
                    <el-tag
                        v-for="(tag, index) in getTags(scope.row.tags)"
                        :key="index"
                        class="card-tag"
                    >
                        {{ tag.name }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column
                label="创建人"
                prop="createBy"
                align="center"
                :show-overflow-tooltip="true"
                width="100"
            />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template #default="scope">
                    <span>{{
                        scope.row.createTime
                            ? parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}')
                            : '-'
                    }}</span>
                </template>
            </el-table-column>
            <el-table-column label="最后更新时间" align="center" prop="updateTime" width="180">
                <template #default="scope">
                    <span>{{
                        scope.row.updateTime
                            ? parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}')
                            : '-'
                    }}</span>
                </template>
            </el-table-column>
        </el-table>
        <pagination
            v-show="knowledgeTotal > 0"
            :total="knowledgeTotal"
            v-model:page="queryParamsKnowledge.pageNum"
            v-model:limit="queryParamsKnowledge.pageSize"
            @pagination="getKnowledgeSelectList"
        />
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="knowledgeDialogOpen = false">取 消</el-button>
                <el-button type="primary" size="mini" @click="submitKnowledgeForm">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="Method">
    import {
        listKnowledge,
        getKnowledge,
        updateKnowledge
    } from '@/api/kac/applyKnowledge/applyKnowledge.js';
    import { listKnowledgeBase } from '@/api/kmc/knowledgeBase/knowledgeBase.js';

    const { proxy } = getCurrentInstance();

    const knowledgeList = ref([]);
    const knowledgeBaseList = ref([]);
    const knowledgeName = ref('');

    const knowledgeDialogTitle = ref('');
    const knowledgeDialogOpen = ref(false);
    const knowledgeLoading = ref(true);
    const knowledgeSelectList = ref([]);
    const knowledgeTotal = ref(0);
    const selectedKnowledge = ref(null);
    const knowledgeTableRef = ref(null);
    const currentEditRow = ref(null);

    const props = defineProps({
        applyId: {
            type: Number,
            required: true
        },
        knowledgeBaseList: {
            type: Array,
            default: () => []
        },
        source: {
            type: String,
            default: 'horizontal',
            validator: (value) => ['horizontal', 'vertical', 'myApp'].includes(value)
        }
    });

    // 列显隐信息
    const columns = ref([
        { key: 1, label: '编号', visible: true },
        { key: 2, label: '名称', visible: true },
        { key: 3, label: '描述', visible: true },
        { key: 4, label: '创建人', visible: true },
        { key: 5, label: '创建时间', visible: true }
    ]);

    const getColumnVisibility = (key) => {
        const column = columns.value.find((col) => col.key === key);
        if (!column) return true;
        return column.visible;
    };

    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });
    const open = ref(false);
    const openDetail = ref(false);
    const title = ref('');
    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            applyId: null,
            knowledgeId: null,
            trenchKey: null,
            createTime: null
        },
        queryParamsKnowledge: {
            pageNum: 1,
            pageSize: 10,
            name: null
        },
        rules: {}
    });

    const { queryParams, queryParamsKnowledge, form, rules } = toRefs(data);

    const getKnowledgeInfo = (id, field) => {
        if (!id) return '-';
        const target = knowledgeBaseList.value.find((item) => item.id == id);
        return target && target[field] != null && target[field] !== '' ? target[field] : '-';
    };

    /** 查询应用关联知识库列表 */
    function getList() {
        loading.value = true;
        queryParams.value.applyId = props.applyId;
        listKnowledge(queryParams.value).then((response) => {
            knowledgeList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    // 监听父组件传递的知识库列表变化
    watch(
        () => props.knowledgeBaseList,
        (newList) => {
            if (newList && newList.length > 0) {
                console.log('newList', newList);
                knowledgeBaseList.value = newList;
                getList();
            }
        },
        { immediate: true, deep: true }
    );

    function getTags(data) {
        if (!data) {
            return [];
        }
        if (typeof data === 'string') {
            try {
                return JSON.parse(data);
            } catch (e) {
                return [];
            }
        }
        if (Array.isArray(data)) {
            return data;
        }
        return [];
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

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    // 表单重置
    function reset() {
        form.value = {
            id: null,
            workspaceId: null,
            appId: null,
            knowledgeId: null,
            trenchKey: null,
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null,
            description: null
        };
        proxy.resetForm('knowledgeRef');
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        currentEditRow.value = { ...row };
        reset();
        const _id = row.id || ids.value;
        getKnowledge(_id).then((response) => {
            form.value = response.data;
            knowledgeName.value = getKnowledgeInfo(form.value.knowledgeId, 'name');
            knowledgeDialogTitle.value = '替换关联知识库';
            knowledgeLoading.value = true;
            queryParamsKnowledge.value.pageNum = 1;
            queryParamsKnowledge.value.name = null;
            selectedKnowledge.value = form.value.knowledgeId
                ? { id: form.value.knowledgeId }
                : null;
            knowledgeDialogOpen.value = true;
            getKnowledgeSelectList();
        });
    }

    function getKnowledgeSelectList() {
        knowledgeLoading.value = true;
        listKnowledgeBase(queryParamsKnowledge.value).then((response) => {
            knowledgeLoading.value = false;
            knowledgeSelectList.value = response.data.rows;
            knowledgeTotal.value = response.data.total;
            nextTick(() => {
                if (selectedKnowledge.value && knowledgeTableRef.value) {
                    const data = knowledgeSelectList.value.find(
                        (item) => item.id == selectedKnowledge.value.id
                    );
                    if (data) {
                        selectedKnowledge.value = data;
                        knowledgeTableRef.value.setCurrentRow(data);
                    } else {
                        knowledgeTableRef.value.setCurrentRow();
                    }
                }
            });
        });
    }

    function handleKnowledgeRowClick(row) {
        if (selectedKnowledge.value && selectedKnowledge.value.id == row.id) {
            selectedKnowledge.value = null;
            if (knowledgeTableRef.value) {
                knowledgeTableRef.value.setCurrentRow();
            }
        } else {
            selectedKnowledge.value = row;
        }
    }

    function submitKnowledgeForm() {
        if (!selectedKnowledge.value) {
            proxy.$modal.msgError('请选择一个知识库');
            return;
        }
        form.value.knowledgeId = selectedKnowledge.value.id;
        knowledgeName.value = selectedKnowledge.value.name;

        updateKnowledge(form.value)
            .then((response) => {
                proxy.$modal.msgSuccess('替换成功');
                knowledgeDialogOpen.value = false;
                getList();
            })
            .catch((error) => {});
    }

    function handleQueryKnowledge() {
        queryParamsKnowledge.value.pageNum = 1;
        getKnowledgeSelectList();
    }

    function resetQueryKnowledge() {
        queryParamsKnowledge.value.pageNum = 1;
        queryParamsKnowledge.value.pageSize = 10;
        queryParamsKnowledge.value.name = null;
        getKnowledgeSelectList();
    }

    function getTableRowClassName({ row }) {
        if (selectedKnowledge.value && selectedKnowledge.value.id == row.id) {
            return 'selected-row';
        }
        return '';
    }
</script>

<style lang="scss" scoped>
    .card-tag {
        margin: 2px;
    }
    :deep(.el-table .selected-row > td.el-table__cell) {
        background-color: #e6f7ff !important;
    }
</style>
