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
        :data="solutionApplyList"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column label="编号" align="center" width="80">
            <template #default="scope">
                <span>{{ scope.$index + 1 }}</span>
            </template>
        </el-table-column>
        <el-table-column label="应用名称" align="center" prop="applyId" show-overflow-tooltip>
            <template #default="scope">
                {{ getMyAppInfo(scope.row.applyId, 'name') || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="描述" align="left" prop="applyId" width="380" show-overflow-tooltip>
            <template #default="scope">
                {{ getMyAppInfo(scope.row.applyId, 'description') || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createBy" />
        <el-table-column
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
            fixed="right"
            width="240"
        >
            <template #default="scope">
                <el-button link type="primary" icon="VideoPlay" @click="handleUpdate(scope.row)"
                    >立即体验
                </el-button>
                <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                    >查看详情
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
</template>

<script setup name="solutionApplyList">
    import { ref, reactive, toRefs, watch } from 'vue';
    import { listSolutionApply } from '@/api/kac/solution/solutionApply.js';

    const router = useRouter();

    const { proxy } = getCurrentInstance();
    const loading = ref(true);
    const total = ref(0);
    const myAppList = ref([]);
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });

    const props = defineProps({
        solutionId: {
            type: Number,
            required: true
        },
        myAppList: {
            type: Array,
            default: () => []
        }
    });

    const data = reactive({
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            solutionId: props.solutionId,
            createTime: null
        }
    });

    const { queryParams } = toRefs(data);

    const solutionApplyList = ref([]);

    const getMyAppInfo = (id, field) => {
        if (!id) return '-';
        const target = myAppList.value.find((item) => item.id == id);
        console.log(target, 'target');
        return target && target[field] != null && target[field] !== '' ? target[field] : '-';
    };

    // 监听父组件传递的知识库列表变化
    watch(
        () => props.myAppList,
        (newList) => {
            if (newList && newList.length > 0) {
                myAppList.value = newList;
                getList();
            }
        },
        { immediate: true, deep: true }
    );

    /** 排序触发事件 */
    function handleSortChange(column, prop, order) {
        queryParams.value.orderByColumn = column.prop;
        queryParams.value.isAsc = column.order;
        getList();
    }

    function getList() {
        if (props.solutionId) {
            queryParams.value.solutionId = props.solutionId;
        }
        loading.value = true;
        listSolutionApply(queryParams.value).then((response) => {
            solutionApplyList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    // 立即体验
    function handleUpdate(row) {
        let appId = getMyAppInfo(row.applyId, 'id');
        let appName = getMyAppInfo(row.applyId, 'name');
        router.push({
            path: '/kac/myApp/pluginApply',
            query: {
                applyId: appId,
                title: appName
            }
        });
    }

    // 查看详情
    function handleDetail(row) {
        let appId = getMyAppInfo(row.applyId, 'id');
        router.push({
            path: '/kac/myApp/myAppDetail',
            query: {
                id: appId
            }
        });
    }
</script>
