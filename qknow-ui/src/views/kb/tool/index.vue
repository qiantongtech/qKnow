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
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch">
            <el-form
                class="btn-style"
                :model="queryParams"
                ref="queryRef"
                :inline="true"
                v-show="showSearch"
                @submit.prevent
            >
                <el-form-item label="名称" prop="name">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.name"
                        placeholder="请输入名称"
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
        </div>

        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            @click="handleAdd"
                            v-hasPermi="['kb:tool:tool:add']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="danger"
                            plain
                            :disabled="multiple"
                            @click="handleDelete"
                            v-hasPermi="['kb:tool:tool:remove']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i> 删除
                        </el-button>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="getList"
                        :columns="columns"
                    ></right-toolbar>
                </div>
            </div>
            <el-table
                stripe
                v-loading="loading"
                :data="toolList"
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
                    v-if="getColumnVisibility(2)"
                    label="名称"
                    align="left"
                    prop="name"
                >
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(3)"
                    label="描述"
                    align="left"
                    prop="description"
                    width="400px"
                    :show-overflow-tooltip="{ effect: 'light' }"
                >
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(4)"
                    label="标签"
                    align="left"
                    prop="tags"
                    min-width="120"
                >
                    <template #default="scope">
                        <div class="multiline-ellipsis">
                            <el-tag
                                v-for="(tag, tagIndex) in getTags(scope.row)"
                                :key="tagIndex"
                                size="medium"
                                class="card-tag"
                            >
                                {{ tag.name }}
                            </el-tag>
                            <span v-if="getTags(scope.row).length <= 0">-</span>
                        </div>
                    </template>
                </el-table-column>
                <!--       <el-table-column v-if="getColumnVisibility(15)" label="备注" align="left" prop="remark" width="250"-->
                <!--                        :show-overflow-tooltip="{ effect: 'light' }">-->
                <!--         <template #default="scope">-->
                <!--           {{ scope.row.remark || '-' }}-->
                <!--         </template>-->
                <!--       </el-table-column>-->
                <el-table-column
                    v-if="getColumnVisibility(9)"
                    label="工具方法数"
                    align="center"
                    prop="methodNum"
                >
                    <template #default="scope">
                        {{ scope.row.methodNum || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(9)"
                    label="创建人"
                    align="center"
                    prop="createBy"
                >
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(11)"
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
                    v-if="getColumnVisibility(16)"
                    label="操作"
                    align="center"
                    class-name="small-padding fixed-width"
                    fixed="right"
                    width="240"
                >
                    <template #default="scope">
                        <el-button
                            link
                            type="primary"
                            icon="view"
                            @click="routeTo('/kb/tool/toolDetail', scope.row)"
                            v-hasPermi="['kb:tool:tool:query']"
                            >详情</el-button
                        >
                        <el-button
                            link
                            type="primary"
                            icon="Edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['kb:tool:tool:edit']"
                            >修改</el-button
                        >
                        <el-button
                            link
                            type="danger"
                            icon="Delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['kb:tool:tool:remove']"
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
        </div>

        <!-- 添加或修改工具管理对话框 -->
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
            <el-form ref="toolRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="名称" prop="name">
                            <el-input v-model="form.name" placeholder="请输入名称" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="标签" prop="tags">
                            <div class="flex gap-2">
                                <el-tag
                                    v-for="tag in form.items.row"
                                    :key="tag"
                                    closable
                                    :disable-transitions="false"
                                    @close="handleClose(tag)"
                                >
                                    {{ tag.name }}
                                </el-tag>
                                <el-input
                                    v-if="inputVisible"
                                    ref="InputRef"
                                    v-model="inputValue"
                                    class="w-20"
                                    size="small"
                                    @keyup.enter="handleInputConfirm"
                                    @blur="handleInputConfirm"
                                />
                                <el-button
                                    v-else
                                    class="button-new-tag"
                                    size="small"
                                    @click="showInput"
                                >
                                    + 添加标签
                                </el-button>
                            </div>
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
                </el-row>
                <el-row :gutter="20">
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
    </div>
</template>

<script setup name="KbTool">
    import { listTool, getTool, delTool, addTool, updateTool } from '@/api/kb/tool/tool';

    const { proxy } = getCurrentInstance();

    const toolList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 2, label: '名称', visible: true },
        { key: 3, label: '描述', visible: true },
        { key: 4, label: '标签', visible: true },
        // { key: 6, label: "来源", visible: true },
        { key: 15, label: '备注', visible: true },
        { key: 9, label: '创建人', visible: true },
        { key: 11, label: '创建时间', visible: true },
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
    const router = useRouter();

    const inputValue = ref('');
    const inputVisible = ref(false);
    const InputRef = ref(null);

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            name: null,
            description: null,
            tags: null,
            type: null,
            source: null,
            createTime: null,
            orderByColumn: 'createTime',
            isAsc: 'desc'
        },
        rules: {
            name: [{ required: true, message: '名称不能为空', trigger: 'blur' }]
            // description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    function getTags(row) {
        if (!row.tags) {
            return [];
        }
        return JSON.parse(row.tags);
    }

    // 删除指定的 tag
    const handleClose = (tag) => {
        const index = form.value.items.row.findIndex((t) => t.name === tag.name);
        if (index !== -1) {
            form.value.items.row.splice(index, 1);
        }
    };

    // 显示输入框并自动聚焦
    const showInput = () => {
        inputVisible.value = true;
        nextTick(() => {
            if (InputRef.value && InputRef.value.input) {
                InputRef.value.input.focus();
            }
        });
    };

    // 处理输入确认（回车或失去焦点）
    const handleInputConfirm = () => {
        const value = inputValue.value.trim();
        if (value) {
            form.value.items.row.push({ name: value });
        }
        inputVisible.value = false;
        inputValue.value = '';
    };

    /** 查询工具管理列表 */
    function getList() {
        loading.value = true;
        listTool(queryParams.value).then((response) => {
            toolList.value = response.data.rows;
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
            name: null,
            description: null,
            tags: null,
            items: {
                row: []
            },
            type: null,
            source: null,
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
        proxy.resetForm('toolRef');
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
        title.value = '新增工具';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getTool(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改工具';

            if (form.value.tags) {
                form.value.items = {
                    row: JSON.parse(form.value.tags)
                };
            } else {
                form.value.items = {
                    row: []
                };
            }
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['toolRef'].validate((valid) => {
            if (valid) {
                if (form.value.items.row) {
                    form.value.tags = JSON.stringify(form.value.items.row);
                }
                if (form.value.id != null) {
                    updateTool(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addTool(form.value)
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
            .confirm('是否确认删除工具管理编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delTool(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    /** ---------------------------------**/

    function routeTo(link, row) {
        if (link !== '' && link.indexOf('http') !== -1) {
            window.location.href = link;
            return;
        }
        if (link !== '') {
            if (link === router.currentRoute.value.path) {
                window.location.reload();
            } else {
                router.push({
                    path: link,
                    query: {
                        id: row.id
                    }
                });
            }
        }
    }

    getList();
</script>

<style lang="scss" scoped>
    .gap-2 {
        gap: 0.5rem;
    }
    .flex {
        display: flex;
    }
    .card-tag {
        margin: 2px;
    }
    .multiline-ellipsis {
        display: -webkit-box;
        -webkit-line-clamp: 2; /* 限制为2行 */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
    }
</style>
