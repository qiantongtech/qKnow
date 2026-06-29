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
        title="知识库-单选"
        v-model="visible"
        width="1200px"
        :append-to="$refs['app-container']"
        draggable
        destroy-on-close
        @close="cancel"
    >
        <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            label-width="68px"
        >
            <el-form-item label="工作区id" prop="workspaceId">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.workspaceId"
                    placeholder="请输入工作区id"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="灵桐知识库id" prop="qmDatasetId">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.qmDatasetId"
                    placeholder="请输入灵桐知识库id"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="名称" prop="name">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.name"
                    placeholder="请输入名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="描述" prop="description">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.description"
                    placeholder="请输入描述"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="索引方式" prop="indexingTechnique">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.indexingTechnique"
                    placeholder="请输入索引方式"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="权限" prop="permission">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.permission"
                    placeholder="请输入权限"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="Embedding 模型名称" prop="embeddingModel">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.embeddingModel"
                    placeholder="请输入Embedding 模型名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="Embedding 模型供应商" prop="embeddingModelProvider">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.embeddingModelProvider"
                    placeholder="请输入Embedding 模型供应商"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="检索方法" prop="searchMethod">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.searchMethod"
                    placeholder="请输入检索方法"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="是否开启 rerank" prop="rerankingEnable">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.rerankingEnable"
                    placeholder="请输入是否开启 rerank"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="Rerank 模型的提供商" prop="rerankingProviderName">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.rerankingProviderName"
                    placeholder="请输入Rerank 模型的提供商"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="Rerank 模型的名称" prop="rerankingModelName">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.rerankingModelName"
                    placeholder="请输入Rerank 模型的名称"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="召回条数" prop="topK">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.topK"
                    placeholder="请输入召回条数"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="是否开启召回分数限制" prop="scoreThresholdEnabled">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.scoreThresholdEnabled"
                    placeholder="请输入是否开启召回分数限制"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="召回分数限制" prop="scoreThreshold">
                <el-input
                    style="width: 240px"
                    v-model="queryParams.scoreThreshold"
                    placeholder="请输入召回分数限制"
                    clearable
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="创建时间" prop="createTime">
                <el-date-picker
                    style="width: 240px"
                    clearable
                    v-model="queryParams.createTime"
                    type="date"
                    value-format="YYYY-MM-DD"
                    placeholder="请选择创建时间"
                >
                </el-date-picker>
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
            ref="tableRef"
            stripe
            height="300px"
            v-loading="loading"
            :data="dataList"
            highlight-current-row
            row-key="id"
            @current-change="handleCurrentChange"
        >
            <el-table-column label="ID" align="center" prop="id" />
            <el-table-column label="工作区id" align="center" prop="workspaceId">
                <template #default="scope">
                    {{ scope.row.workspaceId || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="灵桐知识库id" align="center" prop="qmDatasetId">
                <template #default="scope">
                    {{ scope.row.qmDatasetId || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="名称" align="center" prop="name">
                <template #default="scope">
                    {{ scope.row.name || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="描述" align="center" prop="description">
                <template #default="scope">
                    {{ scope.row.description || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="索引方式" align="center" prop="indexingTechnique">
                <template #default="scope">
                    {{ scope.row.indexingTechnique || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="权限" align="center" prop="permission">
                <template #default="scope">
                    {{ scope.row.permission || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="Embedding 模型名称" align="center" prop="embeddingModel">
                <template #default="scope">
                    {{ scope.row.embeddingModel || '-' }}
                </template>
            </el-table-column>
            <el-table-column
                label="Embedding 模型供应商"
                align="center"
                prop="embeddingModelProvider"
            >
                <template #default="scope">
                    {{ scope.row.embeddingModelProvider || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="检索方法" align="center" prop="searchMethod">
                <template #default="scope">
                    {{ scope.row.searchMethod || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="是否开启 rerank" align="center" prop="rerankingEnable">
                <template #default="scope">
                    {{ scope.row.rerankingEnable || '-' }}
                </template>
            </el-table-column>
            <el-table-column
                label="Rerank 模型的提供商"
                align="center"
                prop="rerankingProviderName"
            >
                <template #default="scope">
                    {{ scope.row.rerankingProviderName || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="Rerank 模型的名称" align="center" prop="rerankingModelName">
                <template #default="scope">
                    {{ scope.row.rerankingModelName || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="召回条数" align="center" prop="topK">
                <template #default="scope">
                    {{ scope.row.topK || '-' }}
                </template>
            </el-table-column>
            <el-table-column
                label="是否开启召回分数限制"
                align="center"
                prop="scoreThresholdEnabled"
            >
                <template #default="scope">
                    {{ scope.row.scoreThresholdEnabled || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="召回分数限制" align="center" prop="scoreThreshold">
                <template #default="scope">
                    {{ scope.row.scoreThreshold || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="创建人" align="center" prop="createBy">
                <template #default="scope">
                    {{ scope.row.createBy || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark">
                <template #default="scope">
                    {{ scope.row.remark || '-' }}
                </template>
            </el-table-column>
        </el-table>

        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />

        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">取 消</el-button>
                <el-button type="primary" size="mini" @click="confirm"> 确 定 </el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="KnowledgeBaseSingle">
    import { listKnowledgeBase } from '@/api/kmc/knowledgeBase/knowledgeBase';
    import { ref } from 'vue';
    const { proxy } = getCurrentInstance();

    const dataList = ref([]);
    const loading = ref(true);
    const showSearch = ref(true);
    const total = ref(0);
    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            qmDatasetId: null,
            name: null,
            description: null,
            indexingTechnique: null,
            permission: null,
            embeddingModel: null,
            embeddingModelProvider: null,
            searchMethod: null,
            rerankingEnable: null,
            rerankingProviderName: null,
            rerankingModelName: null,
            topK: null,
            scoreThresholdEnabled: null,
            scoreThreshold: null,
            createTime: null
        }
    });
    const { queryParams, form } = toRefs(data);

    // -------------------------------------------
    const visible = ref(false);
    // 定义单选数据
    const single = ref();
    // 当前界面table
    const tableRef = ref();

    const emit = defineEmits(['open', 'confirm', 'cancel']);

    /** 单选选中事件 */
    function handleCurrentChange(selection) {
        if (selection) {
            single.value = selection;
        }
    }

    /**
     * 设置当前行
     * @param {Object} row 行对象
     * @returns 更改选中对象
     */
    function setCurrentRow(row) {
        if (row) {
            let data = dataList.value.filter((item) => item.id == row.id);
            tableRef.value?.setCurrentRow(data[0]);
        }
    }

    /**
     * 打开选择框
     * @param {Array} val 选中的对象数组
     */
    function open(val) {
        visible.value = true;
        single.value = val;
        resetQuery();
        getList();
    }

    /**
     * 取消按钮
     * @description 取消按钮时，重置所有状态
     */
    function cancel() {
        queryParams.value.pageNum = 1;
        proxy.resetForm('queryRef');
        visible.value = false;
    }

    /**
     * 确定按钮
     * @description 确定按钮时，emit confirm 事件，以便父组件接收到选中的数据
     */
    function confirm() {
        if (!single.value) {
            proxy.$modal.msgWarning('请选择数据！');
            return;
        }
        emit('confirm', single.value);
        visible.value = false;
    }

    /** 查询字典类型列表 */
    function getList() {
        loading.value = true;
        listKnowledgeBase(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
            async (response) => {
                dataList.value = response.data.rows;
                total.value = response.data.total;
                loading.value = false;
                // 初始化及分页切换选中逻辑
                await nextTick();
                setCurrentRow(single.value);
            }
        );
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        queryParams.value.pageNum = 1;
        handleQuery();
    }

    defineExpose({ open });
</script>
