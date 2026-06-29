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
                label-width="75px"
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

        <div class="card-list-panel">
            <Card :data="applyList" source="horizontal" variant="overview" />
        </div>

        <div class="pagecont-bottom">
            <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            />
        </div>
    </div>
</template>

<script setup name="Horizontal">
    import Card from '@/views/kac/horizontal/components/card.vue';
    import { ref, reactive, toRefs } from 'vue';
    import { listApply } from '@/api/kac/apply/apply.js';

    const { proxy } = getCurrentInstance();

    const loading = ref(true);
    const showSearch = ref(true);
    const total = ref(0);

    const data = reactive({
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            workspaceId: null,
            pluginId: null,
            name: null,
            category: 0,
            description: null,
            status: null,
            source: null,
            tags: null,
            useScene: null,
            useCount: null,
            createTime: null,
            myApplyFlag: 0,
            orderByColumn: 'createTime',
            isAsc: 'desc'
        }
    });

    const { queryParams } = toRefs(data);

    const applyList = ref([]);
    const mockApplyList = [
        {
            id: 20,
            workspaceId: 1001,
            pluginId: null,
            name: '文章编写',
            category: 0,
            description:
                '文章编写插件是一类旨在辅助用户更高效、更高质量地完成文本创作任务的软件工具或扩展程序。',
            status: 1,
            source: null,
            tags: '[{"name":"写作"},{"name":"文章"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01a88de4b0d389f4f52e8e.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-23 19:54:44',
            updateTime: '2026-04-23 19:54:44',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 8,
            workspaceId: 1001,
            pluginId: null,
            name: '批量检索',
            category: 0,
            description:
                '支持一次性上传多个查询条件并行处理，汇总输出结果。大幅提升效率，适用于多项目数据对比或大规模文献调研。',
            status: 1,
            source: null,
            tags: '[{"name":"效率"},{"name":"工具"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01a9a0e4b0d389f4f52e90.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:41:23',
            updateTime: '2026-04-21 18:41:23',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 7,
            workspaceId: 1001,
            pluginId: null,
            name: '精确检索',
            category: 0,
            description: '严格字符匹配，精准查找代码、条款或参数，无模糊干扰。',
            status: 1,
            source: null,
            tags: '[{"name":"搜索"},{"name":"工具"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01a9d8e4b0d389f4f52e91.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:41:10',
            updateTime: '2026-04-21 18:41:10',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 6,
            workspaceId: 1001,
            pluginId: null,
            name: '实体关系检索',
            category: 0,
            description: '智能识别实体与深层关系，助力知识图谱与情报分析。',
            status: 1,
            source: null,
            tags: '[{"name":"分析"},{"name":"数据"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01a9e9e4b0d389f4f52e92.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:41:07',
            updateTime: '2026-04-21 18:41:07',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 5,
            workspaceId: 1001,
            pluginId: null,
            name: '语义检索',
            category: 0,
            description:
                '利用深度学习理解查询意图与上下文，突破关键词匹配限制。即使词汇不完全一致，也能通过语义关联精准定位内容。',
            status: 1,
            source: null,
            tags: '[{"name":"搜索"},{"name":"AI"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01a9f9e4b0d389f4f52e93.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:40:54',
            updateTime: '2026-04-21 18:40:54',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 4,
            workspaceId: 1001,
            pluginId: null,
            name: '知识问答',
            category: 0,
            description:
                '基于海量数据理解并回答各类事实性或解释性问题，提供准确简洁的答案，满足即时信息获取需求。',
            status: 1,
            source: null,
            tags: '[{"name":"问答"},{"name":"知识"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01aa0ae4b0d389f4f52e94.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:39:46',
            updateTime: '2026-04-21 18:39:46',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 3,
            workspaceId: 1001,
            pluginId: null,
            name: '模板报告生成',
            category: 0,
            description: '提供多场景标准模板，引导填充并自动排版，确保企业级文档专业规范。',
            status: 1,
            source: null,
            tags: '[{"name":"模板"},{"name":"文档"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01aa17e4b0d389f4f52e95.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:37:08',
            updateTime: '2026-04-21 18:37:08',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        },
        {
            id: 2,
            workspaceId: 1001,
            pluginId: null,
            name: '日报/周报/月报文章编写',
            category: 0,
            description:
                '简化周期性工作汇报撰写。输入关键事项，系统自动扩展为结构完整、语气专业的报告，智能识别成果与计划。',
            status: 1,
            source: null,
            tags: '[{"name":"写作"},{"name":"办公"}]',
            myApplyFlag: false,
            icon: '/2026/05/11/6a01a8f6e4b0d389f4f52e8f.png',
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:36:52',
            updateTime: '2026-04-21 18:36:52',
            kacApplyKnowledgeList: null,
            kacApplyGraphList: null,
            kacApplyBotList: null
        }
    ];

    function getMockList() {
        const keyword = queryParams.value.name?.trim().toLowerCase();
        const pageNum = Number(queryParams.value.pageNum) || 1;
        const pageSize = Number(queryParams.value.pageSize) || 10;
        const filteredList = keyword
            ? mockApplyList.filter((item) =>
                  [item.name, item.description, item.tags]
                      .filter(Boolean)
                      .some((value) => String(value).toLowerCase().includes(keyword))
              )
            : mockApplyList;
        const start = (pageNum - 1) * pageSize;

        applyList.value = filteredList.slice(start, start + pageSize);
        total.value = filteredList.length;
    }

    /** 查询应用列表 */
    function getList() {
        loading.value = true;
        getMockList();
        loading.value = false;
        // 接口暂未迁移，先保留调用代码，后续后端接口接入后恢复。
        // listApply(queryParams.value).then((response) => {
        //   applyList.value = response.data.rows;
        //   total.value = response.data.total;
        //   loading.value = false;
        // });
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

    getList();
</script>
<style lang="scss" scoped>
    .app-container {
        box-sizing: border-box;
        padding-bottom: 45px;
    }

    .card-list-panel {
        margin-top: 15px;
        padding: 15px;
        background: #ffffff;
        border-radius: 2px;
    }

    .multiline-ellipsis {
        display: -webkit-box;
        -webkit-line-clamp: 2; /* 限制为2行 */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .card-tag {
        margin: 2px;
    }
    .pagecont-bottom {
        position: fixed;
        bottom: 0;
        width: 100%;
        left: 0;
        height: 60px;
        background: #ffffff;
        border-radius: 2px 2px 2px 2px;
        line-height: 60px;
        margin: 0;
        padding: 0 18px 0 0;
        flex: none;
        .pagination-container {
            margin-top: 0;
        }
    }
    .pagecont-top {
        ::v-deep .el-form-item:first-child .el-form-item__label {
            width: 41px !important;
        }
    }
</style>
