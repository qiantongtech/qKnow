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
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    @click="handleAdd"
                    @mousedown="(e) => e.preventDefault()"
                >
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    icon="Delete"
                    plain
                    :disabled="multiple"
                    @click="handleDelete"
                    @mousedown="(e) => e.preventDefault()"
                >
                    删除
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar :search="false" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
    </div>
    <el-table
        stripe
        v-loading="loading"
        :data="methodList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
    >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
            v-if="getColumnVisibility(0)"
            label="编号"
            align="center"
            prop="id"
            width="85"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        />
        <el-table-column
            v-if="getColumnVisibility(3)"
            label="唯一标识"
            align="left"
            prop="code"
            min-width="120"
        >
            <template #default="scope">
                {{ scope.row.code || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(4)"
            label="名称"
            align="left"
            prop="name"
            min-width="150"
        >
            <template #default="scope">
                {{ scope.row.name || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(5)"
            label="描述"
            align="left"
            prop="description"
            width="250"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
            <template #default="scope">
                {{ scope.row.description || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(14)"
            label="备注"
            align="left"
            prop="remark"
            width="250"
            :show-overflow-tooltip="{ effect: 'light' }"
        >
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(8)"
            label="创建人"
            align="center"
            prop="createBy"
        >
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column
            v-if="getColumnVisibility(10)"
            label="创建时间"
            align="center"
            prop="createTime"
            width="180"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
        >
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            fixed="right"
            width="240"
            v-if="getColumnVisibility(16)"
        >
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                    >修改</el-button
                >
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                    >删除</el-button
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

    <!-- 添加或修改工具方法对话框 -->
    <el-dialog
        :title="title"
        v-model="open"
        width="800px"
        :append-to="$refs['app-container']"
        draggable
    >
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="methodRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="唯一标识" prop="code">
                        <el-input v-model="form.code" placeholder="请输入唯一标识" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="描述" prop="description">
                        <el-input
                            v-model="form.description"
                            placeholder="请输入描述"
                            type="textarea"
                            maxlength="512"
                            show-word-limit
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                        <el-input
                            v-model="form.remark"
                            type="textarea"
                            placeholder="请输入备注"
                            maxlength="512"
                            show-word-limit
                        />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">取 消</el-button>
                <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="Method">
    import {
        listMethod,
        getMethod,
        delMethod,
        addMethod,
        updateMethod
    } from '@/api/kb/tool/method';

    const { proxy } = getCurrentInstance();

    const methodList = ref([]);

    const props = defineProps({
        toolId: {
            type: Number,
            required: true
        }
    });

    // 列显隐信息
    const columns = ref([
        { key: 0, label: '编号', visible: true },
        { key: 3, label: '唯一标识', visible: true },
        { key: 4, label: '名称', visible: true },
        { key: 5, label: '描述', visible: true },
        { key: 14, label: '备注', visible: true },
        { key: 8, label: '创建人', visible: true },
        { key: 10, label: '创建时间', visible: true },
        { key: 16, label: '操作', visible: true }
    ]);

    const getColumnVisibility = (key) => {
        const column = columns.value.find((col) => col.key === key);
        // 如果没有找到对应列配置，默认显示
        if (!column) return true;
        // 如果找到对应列配置，根据visible属性来控制显示
        return column.visible;
    };

    const open = ref(false);
    const openDetail = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref('');
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            toolId: props.toolId,
            code: null,
            name: null,
            description: null,
            createTime: null,
            orderByColumn: 'createTime',
            isAsc: 'desc'
        },
        rules: {
            code: [{ required: true, message: '标识不能为空', trigger: 'blur' }],
            name: [{ required: true, message: '名称不能为空', trigger: 'blur' }]
            // description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询工具方法列表 */
    function getList() {
        loading.value = true;
        listMethod(queryParams.value).then((response) => {
            methodList.value = response.data.rows;
            total.value = response.data.total;
            loading.value = false;
        });
    }

    // 取消按钮
    function cancel() {
        open.value = false;
        openDetail.value = false;
        reset();
    }

    // 表单重置
    function reset() {
        form.value = {
            id: null,
            workspaceId: null,
            toolId: props.toolId,
            code: null,
            name: null,
            description: null,
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
        proxy.resetForm('methodRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    // 多选框选中数据
    function handleSelectionChange(selection) {
        ids.value = selection.map((item) => item.id);
        single.value = selection.length != 1;
        multiple.value = !selection.length;
    }

    /** 排序触发事件 */
    function handleSortChange(column, prop, order) {
        queryParams.value.orderByColumn = column.prop;
        queryParams.value.isAsc = column.order;
        getList();
    }

    /** 新增按钮操作 */
    function handleAdd() {
        reset();
        open.value = true;
        title.value = '新增工具方法';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getMethod(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改工具方法';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['methodRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateMethod(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addMethod(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('新增成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                }
            }
        });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除工具方法编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delMethod(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    getList();
</script>
