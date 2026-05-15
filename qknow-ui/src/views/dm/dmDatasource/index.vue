<!--
  Copyright © 2026 Qiantong Technology Co., Ltd.
  qKnow Knowledge Platform
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qknow.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2026 江苏千桐科技有限公司
  qKnow 知识平台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qknow.qiantong.tech/business.html
-->

<template>
    <div class="app-container" ref="app-container">
        <GuideTip tip-id="kg/dm/dmDatasource.list" />
        <div class="pagecont-top" v-show="showSearch">
            <el-form
                class="btn-style"
                :model="queryParams"
                ref="queryRef"
                :inline="true"
                label-width="100px"
                v-show="showSearch"
                @submit.prevent
            >
                <el-form-item label="数据连接名称" prop="datasourceName">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.datasourceName"
                        placeholder="请输入数据连接名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="数据连接类型" prop="datasourceType">
                    <el-select
                        class="el-form-input-width"
                        v-model="queryParams.datasourceType"
                        placeholder="请选择数据连接类型"
                        clearable
                    >
                        <el-option
                            v-for="dict in datasource_type"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                        />
                    </el-select>
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
                            v-hasPermi="['dm:datasource:datasource:add']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <!--         <el-col :span="1.5">-->
                    <!--           <el-button type="primary" plain :disabled="single" @click="handleUpdate" v-hasPermi="['dm:datasource:datasource:edit']"-->
                    <!--                      @mousedown="(e) => e.preventDefault()">-->
                    <!--             <i class="iconfont-mini icon-xiugai&#45;&#45;copy mr5"></i>修改-->
                    <!--           </el-button>-->
                    <!--         </el-col>-->
                    <!--         <el-col :span="1.5">-->
                    <!--           <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['dm:datasource:datasource:remove']"-->
                    <!--                      @mousedown="(e) => e.preventDefault()">-->
                    <!--             <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除-->
                    <!--           </el-button>-->
                    <!--         </el-col>-->
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
                :data="daDatasourceList"
                @selection-change="handleSelectionChange"
                :default-sort="defaultSort"
                @sort-change="handleSortChange"
                :tooltip-options="{ effect: 'light' }"
            >
                <el-table-column
                    v-if="getColumnVisibility(1)"
                    label="编号"
                    align="center"
                    prop="id"
                    width="80"
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                    show-overflow-tooltip
                >
                    <template #default="scope">
                        {{ scope.row.id || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(2)"
                    label="数据连接名称"
                    align="left"
                    prop="datasourceName"
                    width="240"
                    show-overflow-tooltip
                >
                    <template #default="scope">
                        {{ scope.row.datasourceName || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(3)"
                    label="描述"
                    align="left"
                    prop="description"
                    width="240"
                    show-overflow-tooltip
                >
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(4)"
                    label="数据连接类型"
                    align="center"
                    prop="datasourceType"
                >
                    <template #default="scope">
                        <dict-tag :options="datasource_type" :value="scope.row.datasourceType" />
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(5)"
                    label="状态"
                    align="center"
                    prop="validFlag"
                >
                    <template #default="scope">
                        <el-switch
                            v-model="scope.row.validFlag"
                            :active-value="true"
                            :inactive-value="false"
                            @change="handleStatusChange(scope.row)"
                        />
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(6)"
                    label="备注"
                    align="left"
                    prop="remark"
                    width="240"
                    show-overflow-tooltip
                >
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(7)"
                    label="创建人"
                    align="center"
                    prop="description"
                    show-overflow-tooltip
                >
                  <template #default="scope">
                    {{ scope.row.createBy || '-' }}
                  </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(8)"
                    label="创建时间"
                    align="center"
                    prop="createTime"
                    width="160"
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                >
                    <template #default="scope">
                        <span>{{
                            parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}')
                        }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(9)"
                    label="操作"
                    align="center"
                    class-name="small-padding fixed-width"
                    fixed="right"
                    width="280"
                >
                    <template #default="scope">
                        <el-button
                            link
                            type="primary"
                            @click="handleTestConnection(scope.row,'table')"
                            v-hasPermi="['dm:datasource:datasource:edit']"
                        >
                            <template #icon><el-icon :size="14"><Connection /></el-icon></template>
                            测试连接
                        </el-button>
                        <el-button
                            link
                            type="primary"
                            @click="handleDetail(scope.row)"
                            v-hasPermi="['dm:datasource:datasource:edit']"
                        >
                            <template #icon><el-icon :size="14"><View /></el-icon></template>
                            详情
                        </el-button>
                        <el-popover placement="bottom" :width="150" trigger="click">
                            <template #reference>
                                <el-button type="primary" link @click.stop>
                                    <template #icon><el-icon :size="14"><ArrowDown /></el-icon></template>
                                    更多
                                </el-button>
                            </template>
                            <div class="card-button-group">
                                <el-button
                                    link
                                    type="primary"
                                    @click="handleUpdate(scope.row)"
                                    v-hasPermi="['dm:datasource:datasource:edit']"
                                    style="margin-left: 28px"
                                    :disabled="scope.row.validFlag"
                                >
                                    <template #icon><el-icon :size="14"><Edit /></el-icon></template>
                                    修改
                                </el-button>
                                <el-button
                                    link
                                    type="danger"
                                    @click="handleDelete(scope.row)"
                                    v-hasPermi="['dm:datasource:datasource:remove']"
                                    style="margin-left: 28px"
                                    :disabled="scope.row.validFlag"
                                >
                                    <template #icon><el-icon :size="14"><Delete /></el-icon></template>
                                    删除
                                </el-button>
                            </div>
                        </el-popover>
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

        <!-- 新增或修改数据源对话框 -->
        <el-dialog
            :title="title"
            v-model="open"
            width="1000px"
            :append-to="$refs['app-container']"
            draggable
        >
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form
                ref="daDatasourceRef"
                :model="form"
                :rules="rules"
                label-width="110px"
                @submit.prevent
            >
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据连接名称" prop="datasourceName">
                            <el-input
                                v-model="form.datasourceName"
                                placeholder="请输入数据连接名称"
                            />
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="数据连接类型" prop="datasourceType">
                            <el-select v-model="form.datasourceType" placeholder="请选择数据连接类型">
                                <el-option
                                    v-for="dict in datasource_type"
                                    :key="dict.value"
                                    :label="dict.label"
                                    :value="dict.value"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="IP" prop="ip">
                            <el-input v-model="form.ip" placeholder="请输入IP" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="端口号" prop="port">
                            <el-input v-model="form.port" placeholder="请输入端口号" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="username">
                            <el-input v-model="form.username" placeholder="请输入账号" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" prop="password">
                            <el-input
                                v-model="form.password"
                                placeholder="请输入密码"
                                v-if="title === '新增数据源'"
                            />
                            <el-input
                                type="password"
                                v-model="form.password"
                                placeholder="请输入密码"
                                v-if="title !== '新增数据源'"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12" v-if="form.datasourceType !== null">
                        <el-form-item label="数据库名称" prop="dbname">
                            <el-input v-model="form.dbname" placeholder="请输入数据库名称" />
                        </el-form-item>
                    </el-col>
                    <el-col
                        :span="12"
                        v-if="form.datasourceType !== null && form.datasourceType !== 'DM8' && form.datasourceType !== 'MySql'"
                    >
                        <el-form-item label="模式" prop="sid">
                            <el-input v-model="form.sid" placeholder="请输入模式" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="状态" prop="validFlag">
                            <el-radio-group v-model="form.validFlag">
                                <el-radio :label="true">启用</el-radio>
                                <el-radio :label="false">禁用</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="描述" prop="description">
                            <el-input
                                type="textarea"
                                :min-height="192"
                                v-model="form.description"
                                placeholder="请输入描述"
                                :maxlength="512"
                                show-word-limit
                            />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注">
                            <el-input
                                type="textarea"
                                v-model="form.remark"
                                placeholder="请输入备注"
                                :min-height="192"
                                :maxlength="512"
                                show-word-limit
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" size="mini" @click="handleTestConnection(form.value,'dialog')">测试连接</el-button>
                    <el-button size="mini" @click="cancel">取 消</el-button>
                    <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 数据源详情对话框 -->
        <el-dialog
            :title="title"
            v-model="openDetail"
            width="1000px"
            :append-to="$refs['app-container']"
            draggable
        >
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form ref="daDatasourceRef" :model="form" label-width="110px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数据连接名称" prop="datasourceName">
                            <el-input :value="form.datasourceName" disabled />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="数据源类型" prop="datasourceType">
                            <div style="display: flex; align-items: center; border: 1px solid var(--el-input-border-color, #e4e7ed); border-radius: 2px; padding: 1px 11px; background-color: var(--el-fill-color-light, #f5f7fa); height: 32px; width: 100%;">
                                <el-tag size="small">
                                    {{ getDatasourceLabel(form.datasourceType) }}
                                </el-tag>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="IP" prop="ip">
                            <el-input :value="form.ip" disabled />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="端口号" prop="port">
                            <el-input :value="form.port" disabled />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="账号" prop="username">
                            <el-input :value="form.username" disabled />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" prop="password">
                            <el-input :value="'***********'" disabled />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12" v-if="form.datasourceType !== null">
                        <el-form-item label="数据库名称" prop="dbname">
                            <el-input :value="form.dbname" disabled />
                        </el-form-item>
                    </el-col>
                    <el-col
                        :span="12"
                        v-if="form.datasourceType !== null && form.datasourceType !== 'DM8' && form.datasourceType !== 'MySql'"
                    >
                        <el-form-item label="模式" prop="sid">
                            <el-input :value="form.sid" disabled />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="状态" prop="validFlag">
                            <el-tag :type="form.validFlag ? 'primary' : 'danger'">
                                {{ form.validFlag ? '启用' : '禁用' }}
                            </el-tag>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="描述" prop="description">
                            <el-input type="textarea" :value="form.description || '-'" disabled class="no-resize-textarea" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                            <el-input type="textarea" :value="form.remark || '-'" disabled class="no-resize-textarea" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button size="mini" @click="cancel">关 闭</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 用户导入对话框 -->
        <el-dialog
            :title="upload.title"
            v-model="upload.open"
            width="800px"
            :append-to="$refs['app-container']"
            draggable
            destroy-on-close
        >
            <el-upload
                ref="uploadRef"
                :limit="1"
                accept=".xlsx, .xls"
                :headers="upload.headers"
                :action="upload.url + '?updateSupport=' + upload.updateSupport"
                :disabled="upload.isUploading"
                :on-progress="handleFileUploadProgress"
                :on-success="handleFileSuccess"
                :auto-upload="false"
                drag
            >
                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <div class="el-upload__tip">
                            <el-checkbox
                                v-model="upload.updateSupport"
                            />是否更新已经存在的数据源数据
                        </div>
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link
                            type="primary"
                            :underline="false"
                            style="font-size: 12px; vertical-align: baseline"
                            @click="importTemplate"
                            >下载模板</el-link
                        >
                    </div>
                </template>
            </el-upload>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="upload.open = false">取 消</el-button>
                    <el-button type="primary" @click="submitFileForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="DaDatasource">
    import {
        listDaDatasource,
        getDaDatasource,
        clientsTest,
        clientsTestWithForm,
        delDaDatasource,
        addDaDatasource,
        updateDaDatasource
    } from '@/api/da/datasource/daDatasource';
    import { getToken } from '@/utils/auth.js';

    const { proxy } = getCurrentInstance();
    const { datasource_type } = proxy.useDict('datasource_type');
    const daDatasourceList = ref([]);

    const getDatasourceLabel = (value) => {
        const dict = datasource_type.value.find(item => item.value === value);
        return dict ? dict.label : value;
    };

    // 列显隐信息
    const columns = ref([
        { key: 1, label: '编号', visible: true },
        { key: 2, label: '数据连接名称', visible: true },
        { key: 3, label: '描述', visible: true },
        { key: 4, label: '数据连接类型', visible: true },
        { key: 5, label: '状态', visible: true },
        { key: 6, label: '备注', visible: true },
        { key: 7, label: '创建人', visible: true },
        { key: 8, label: '创建时间', visible: true },
        { key: 9, label: '操作', visible: true }
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

    /*** 用户导入参数 */
    const upload = reactive({
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: import.meta.env.VITE_APP_BASE_API + '/da/daDatasource/importData'
    });

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            datasourceName: null,
            datasourceType: null,
            datasourceConfig: null,
            ip: null,
            port: null,
            listCount: null,
            syncCount: null,
            dataSize: null,
            description: null,
            createTime: null,
            orderByColumn: "createTime",
            isAsc: "desc",
        },
        rules: {
            datasourceName: [{ required: true, message: '数据连接名称不能为空', trigger: 'blur' }],
            datasourceType: [{ required: true, message: '数据连接类型不能为空', trigger: 'change' }],
            datasourceConfig: [
                { required: true, message: '数据源配置(json字符串)不能为空', trigger: 'blur' }
            ],
            ip: [{ required: true, message: 'IP不能为空', trigger: 'blur' }],
            port: [{ required: true, message: '端口号不能为空', trigger: 'blur' }],
            username: [{ required: true, message: '账号不能为空', trigger: 'blur' }],
            password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
            dbname: [{ required: true, message: '数据库名称不能为空', trigger: 'blur' }],
            validFlag: [{ required: true, message: '模式不能为空', trigger: 'blur' }]
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询数据源列表 */
    function getList() {
        loading.value = true;
        listDaDatasource(queryParams.value).then((response) => {
            daDatasourceList.value = response.data.rows;
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
            datasourceName: null,
            datasourceType: null,
            datasourceConfig: null,
            ip: null,
            port: null,
            listCount: null,
            syncCount: null,
            dataSize: null,
            description: null,
            validFlag: false,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null
        };
        proxy.resetForm('daDatasourceRef');
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
        title.value = '新增数据源';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getDaDatasource(_id).then((response) => {
            form.value = response.data;
            // 拆解 datasourceConfig
            if (form.value.datasourceConfig) {
                const config = JSON.parse(form.value.datasourceConfig);
                console.log(config, 'config');
                form.value.username = config.username;
                form.value.password = config.password;
                form.value.dbname = config.dbname;
                form.value.sid = config.sid;
            }
            open.value = true;
            title.value = '修改数据源';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getDaDatasource(_id).then((response) => {
            form.value = response.data;
            if (form.value.datasourceConfig) {
                const config = JSON.parse(form.value.datasourceConfig);
                form.value.username = config.username;
                form.value.password = config.password;
                form.value.dbname = config.dbname;
                form.value.sid = config.sid;
            }
            openDetail.value = true;
            title.value = '数据源详情';
        });
    }

    /** 测试连接按钮操作 */
    function handleTestConnection(row,type) {
        if (type == 'dialog') {
            proxy.$refs['daDatasourceRef'].validate((valid) => {
                if (valid) {
                    form.value.datasourceConfig = JSON.stringify({
                        username: form.value.username,
                        password: form.value.password,
                        dbname: form.value.dbname,
                        sid: form.value.sid
                    });
                    clientsTestWithForm(form.value).then((response) => {
                        console.log(response);
                        proxy.$modal.msgSuccess(response.msg);
                    });
                }
            });
        } else {
            reset();
            const _id = row.id || ids.value;
            clientsTest(_id).then((response) => {
                console.log(response);
                proxy.$modal.msgSuccess(response.msg);
            });
        }
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['daDatasourceRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    form.value.datasourceConfig = JSON.stringify({
                        username: form.value.username,
                        password: form.value.password,
                        dbname: form.value.dbname,
                        sid: form.value.sid
                    });
                    updateDaDatasource(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    form.value.datasourceConfig = JSON.stringify({
                        username: form.value.username,
                        password: form.value.password,
                        dbname: form.value.dbname,
                        sid: form.value.sid
                    });
                    addDaDatasource(form.value)
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

    /** 状态切换操作 */
    function handleStatusChange(row) {
        console.log(row,'rowcjyyyyy');
        let text = row.validFlag === true ? "启用" : "禁用";
        row.validFlag = row.validFlag === true ? true : false;
        proxy.$modal
            .confirm('确认要' + text + '"' + row.datasourceName + '"数据连接吗？')
            .then(function () {
                return updateDaDatasource(row);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess(text + '成功');
            })
            .catch(() => {});
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除数据连接名称为"' + row.datasourceName + '"的数据项？')
            .then(function () {
                return delDaDatasource(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    /** 导出按钮操作 */
    function handleExport() {
        proxy.download(
            'da/daDatasource/export',
            {
                ...queryParams.value
            },
            `daDatasource_${new Date().getTime()}.xlsx`
        );
    }

    /** ---------------- 导入相关操作 -----------------**/
    /** 导入按钮操作 */
    function handleImport() {
        upload.title = '数据源导入';
        upload.open = true;
    }

    /** 下载模板操作 */
    function importTemplate() {
        proxy.download(
            'system/user/importTemplate',
            {},
            `daDatasource_template_${new Date().getTime()}.xlsx`
        );
    }

    /** 提交上传文件 */
    function submitFileForm() {
        proxy.$refs['uploadRef'].submit();
    }

    /**文件上传中处理 */
    const handleFileUploadProgress = (event, file, fileList) => {
        upload.isUploading = true;
    };

    /** 文件上传成功处理 */
    const handleFileSuccess = (response, file, fileList) => {
        upload.open = false;
        upload.isUploading = false;
        proxy.$refs['uploadRef'].handleRemove(file);
        proxy.$alert(
            "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
                response.msg +
                '</div>',
            '导入结果',
            { dangerouslyUseHTMLString: true }
        );
        getList();
    };
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

    queryParams.value.orderByColumn = defaultSort.value.prop;
    queryParams.value.isAsc = defaultSort.value.order;
    getList();
</script>
<style scoped lang="scss">
.card-button-group {
  width: 100px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

::v-deep .el-dialog__body {
  height: auto !important;
}

::v-deep .no-resize-textarea textarea {
  resize: none !important;
}

/* 表格单元格悬浮提示样式 - 多行显示 */
::v-deep .el-table .cell.el-tooltip {
  display: -webkit-box !important;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical !important;
  white-space: normal;
}

::v-deep .el-popper.is-light {
    box-shadow: 0px 2px 8px 1px rgba(0, 0, 0, 0.15);
    max-width: 600px;
    font-size: 14px;
    padding: 16px;
    line-height: 22px;
}
</style>
