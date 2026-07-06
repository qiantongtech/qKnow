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
    <div class="app-container" ref="app-container">
        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    权限设置
                </div>
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="getList"
                        :columns="columns"
                    ></right-toolbar>
                </div>
            </div>
            <!-- 表格数据 -->
            <el-table stripe v-loading="loading" :data="roleList" :default-sort="defaultSort">
                <!--        <el-table-column type="selection" width="55" align="center" />-->
                <el-table-column
                    label="编号"
                    prop="roleId"
                    align="center"
                    sortable="custom"
                    width="80"
                    v-if="getColumnVisibility(1)"
                />
                <el-table-column
                    v-if="getColumnVisibility(2)"
                    label="角色名称"
                    prop="roleName"
                    align="left"
                    width="200"
                    :show-overflow-tooltip="{ effect: 'light' }"
                />
                <el-table-column
                    v-if="getColumnVisibility(3)"
                    label="权限字符"
                    prop="roleKey"
                    align="center"
                    :show-overflow-tooltip="{ effect: 'light' }"
                />
                <el-table-column
                    label="显示顺序"
                    prop="roleSort"
                    align="center"
                    sortable="custom"
                    v-if="getColumnVisibility(4)"
                />
                <el-table-column label="状态" align="center" v-if="getColumnVisibility(5)">
                    <template #default="scope">
                        <el-switch
                            :model-value="selectedRoleIds.includes(scope.row.roleId) ? '0' : '1'"
                            @update:model-value="handleSwitchUpdate(scope.row.roleId, $event)"
                            active-value="0"
                            inactive-value="1"
                        ></el-switch>
                    </template>
                </el-table-column>

                <el-table-column
                    v-if="getColumnVisibility(6)"
                    label="创建人"
                    align="center"
                    prop="createBy"
                >
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="创建时间"
                    align="center"
                    prop="createTime"
                    width="160"
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                    v-if="getColumnVisibility(7)"
                >
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script setup name="RoleTable">
    import {
        addKnowledgeBase,
        getRerank,
        getTextEmbedding,
        updateKnowledgeBase,
        delKnowledgeBase,
        getKnowledgeBase,
        getRole,
        updateKnowledgeBaseRole,
        changeKnowledgeValid
    } from '@/api/kmc/knowledgeBase/knowledgeBase.js';
    const route = useRoute();
    const router = useRouter();
    const { proxy } = getCurrentInstance();
    const { sys_normal_disable } = proxy.useDict('sys_normal_disable');

    const roleList = ref([]);
    const loading = ref(true);
    const total = ref(0);
    const dateRange = ref([]);
    const kmcId = route.params.kbId;
    const selectedRoleIds = ref([]);
    const isInitializing = ref(true); // 新增：初始化标志位
    const defaultSort = ref({ prop: 'createTime', order: 'descending' });

    // 列显隐信息
    const columns = ref([
        { key: 1, label: '编号', visible: true },
        { key: 2, label: '角色名称', visible: true },
        { key: 3, label: '权限字符', visible: true },
        { key: 4, label: '显示顺序', visible: true },
        { key: 5, label: '状态', visible: true },
        { key: 6, label: '创建人', visible: true },
        { key: 7, label: '创建时间', visible: true }
    ]);
    const getColumnVisibility = (key) => {
        const column = columns.value.find((col) => col.key === key);
        // 如果没有找到对应列配置，默认显示
        if (!column) return true;
        // 如果找到对应列配置，根据visible属性来控制显示
        return column.visible;
    };

    const handleSwitchUpdate = (roleId, value) => {
        if (isInitializing.value) return;

        console.log('switch 更新:', roleId, value);

        if (value === '0') {
            // 开启
            if (!selectedRoleIds.value.includes(roleId)) {
                selectedRoleIds.value.push(roleId);
            }
        } else {
            // 关闭
            selectedRoleIds.value = selectedRoleIds.value.filter((id) => id !== roleId);
        }
        handleStatusChange();
    };

    /** 查询图谱角色列表 */
    function getList() {
        loading.value = true;
        getRole(kmcId).then((response) => {
            loading.value = false;
            roleList.value = response.data.roleList;
            // 开始初始化
            isInitializing.value = true;
            // 直接初始化 selectedRoleIds
            selectedRoleIds.value = response.data.knowledgeRoleList.map((item) => item.roleId);
            nextTick(() => {
                isInitializing.value = false;
            });
        });
    }

    /** 角色状态修改 */
    function handleStatusChange() {
        updateKnowledgeBaseRole({
            knowledgeId: kmcId,
            roleIds: selectedRoleIds.value.join(',')
        });
    }
    getList();
</script>
<style scoped lang="scss">
    .app-container {
        .pagecont-bottom {
            .header-left {
                display: flex;
                align-items: center;
                .blue-bar {
                    background-color: #2666fb;
                    width: 6px;
                    height: 16px;
                    margin-right: 10px;
                    border-radius: 3px;
                }
            }
            height: 100%;
        }
    }
</style>
