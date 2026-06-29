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
            <Card :data="applyList" source="myApp" variant="overview" @refresh="getList" />
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
    // import { listApply } from "@/api/kac/apply/apply.js";
    import useUserStore from '@/store/system/user.js';

    const { proxy } = getCurrentInstance();
    const userStore = useUserStore();

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
            category: null,
            description: null,
            status: null,
            source: null,
            tags: null,
            useScene: null,
            useCount: null,
            createTime: null,
            myApplyFlag: 1,
            userId: userStore.id,
            orderByColumn: 'createTime',
            isAsc: 'desc'
        }
    });

    const { queryParams } = toRefs(data);

    const applyList = ref([]);

    function createIcon({ bgStart, bgEnd, accent, symbol }) {
        const symbols = {
            wrench: `
      <path d="M42 18a13 13 0 0 0 15 18L36 57a8 8 0 0 1-11-11l21-21a13 13 0 0 0-4-7Z" fill="white" opacity=".96"/>
      <path d="M25 51l7 7" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
    `,
            health: `
      <rect x="18" y="24" width="44" height="32" rx="8" fill="white" opacity=".95"/>
      <path d="M40 31v18M31 40h18" stroke="${accent}" stroke-width="5" stroke-linecap="round"/>
      <path d="M22 20c8-9 28-9 36 0" stroke="white" stroke-width="5" stroke-linecap="round" opacity=".75"/>
    `
        };
        const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 80 80">
      <defs>
        <linearGradient id="bg" x1="12" y1="10" x2="68" y2="70" gradientUnits="userSpaceOnUse">
          <stop stop-color="${bgStart}"/>
          <stop offset="1" stop-color="${bgEnd}"/>
        </linearGradient>
      </defs>
      <rect width="80" height="80" rx="16" fill="url(#bg)"/>
      <circle cx="66" cy="14" r="9" fill="white" opacity=".24"/>
      <circle cx="15" cy="66" r="12" fill="white" opacity=".14"/>
      ${symbols[symbol]}
    </svg>
  `;
        return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
    }

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
            source: '横向通用应用',
            tags: '[{"name":"写作"},{"name":"文章"}]',
            useScene: null,
            useCount: 156,
            myApplyFlag: true,
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-23 19:54:44',
            updateTime: '2026-04-23 19:54:44',
            kacApplyKnowledgeList: [],
            kacApplyGraphList: [],
            kacApplyBotList: [],
            icon: '/2026/05/11/6a01a88de4b0d389f4f52e8e.png'
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
            source: '横向通用应用',
            tags: '[{"name":"效率"},{"name":"工具"}]',
            useScene: null,
            useCount: 143,
            myApplyFlag: true,
            validFlag: true,
            delFlag: false,
            createTime: '2026-04-21 18:41:23',
            updateTime: '2026-04-21 18:41:23',
            kacApplyKnowledgeList: [],
            kacApplyGraphList: [],
            kacApplyBotList: [],
            icon: '/2026/05/11/6a01a9a0e4b0d389f4f52e90.png'
        },
        {
            id: 101,
            workspaceId: 1001,
            pluginId: null,
            name: '故障快速排查与维修指导',
            category: 1,
            description:
                '面向设备运维场景，结合故障现象、报警代码和历史维修记录，快速定位可能原因并生成维修步骤建议。',
            status: 1,
            source: '纵向行业应用',
            tags: '[{"name":"运维"},{"name":"维修"}]',
            useScene: null,
            useCount: 128,
            myApplyFlag: true,
            validFlag: true,
            delFlag: false,
            createTime: '2026-05-12 09:20:00',
            updateTime: '2026-05-12 09:20:00',
            kacApplyKnowledgeList: [],
            kacApplyGraphList: [],
            kacApplyBotList: [],
            icon: createIcon({
                bgStart: '#ff9f43',
                bgEnd: '#f45b69',
                accent: '#f97316',
                symbol: 'wrench'
            })
        },
        {
            id: 102,
            workspaceId: 1001,
            pluginId: null,
            name: '设备健康诊断与报告编写',
            category: 1,
            description:
                '汇总巡检、运行、告警和检修数据，自动评估设备健康状态，输出结构化诊断结论和专业分析报告。',
            status: 1,
            source: '纵向行业应用',
            tags: '[{"name":"设备"},{"name":"诊断"}]',
            useScene: null,
            useCount: 96,
            myApplyFlag: true,
            validFlag: true,
            delFlag: false,
            createTime: '2026-05-12 09:20:00',
            updateTime: '2026-05-12 09:20:00',
            kacApplyKnowledgeList: [],
            kacApplyGraphList: [],
            kacApplyBotList: [],
            icon: createIcon({
                bgStart: '#35d0ba',
                bgEnd: '#0ea5e9',
                accent: '#0ea5e9',
                symbol: 'health'
            })
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
        // 后续需要恢复接口时打开下面代码即可。
        // listApply(queryParams.value).then(response => {
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
        padding-bottom: 76px;
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
