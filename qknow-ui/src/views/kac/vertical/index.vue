<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qKnow Knowledge Platform (Business Edition)
  Software Copyright Registration No. 15980140
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
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
            <Card :data="applyList" source="vertical" variant="overview" />
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
    import { getCurrentInstance, ref, reactive, toRefs } from 'vue';
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
            category: 1,
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
            `,
            rain: `
                <path d="M25 35a12 12 0 0 1 23-5 10 10 0 1 1 4 19H25a7 7 0 0 1 0-14Z" fill="white" opacity=".95"/>
                <path d="M27 58l-3 6M39 58l-3 6M51 58l-3 6" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
            `,
            checklist: `
                <rect x="18" y="17" width="44" height="46" rx="8" fill="white" opacity=".95"/>
                <path d="m27 31 4 4 8-9M27 47l4 4 8-9" stroke="${accent}" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M45 33h8M45 49h8" stroke="${accent}" stroke-width="4" stroke-linecap="round" opacity=".75"/>
            `,
            medicine: `
                <rect x="18" y="34" width="44" height="18" rx="9" fill="white" opacity=".95"/>
                <path d="M40 34v18" stroke="${accent}" stroke-width="4"/>
                <path d="M40 17v13M33 24h14" stroke="white" stroke-width="5" stroke-linecap="round"/>
            `,
            folder: `
                <path d="M16 25a7 7 0 0 1 7-7h12l6 7h16a7 7 0 0 1 7 7v24a7 7 0 0 1-7 7H23a7 7 0 0 1-7-7V25Z" fill="white" opacity=".95"/>
                <path d="M28 42h24M28 52h17" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
            `,
            water: `
                <path d="M40 15c10 13 18 24 18 34a18 18 0 1 1-36 0c0-10 8-21 18-34Z" fill="white" opacity=".95"/>
                <path d="M31 50c5 5 13 5 18 0" stroke="${accent}" stroke-width="4" stroke-linecap="round"/>
            `,
            image: `
                <rect x="17" y="19" width="46" height="42" rx="8" fill="white" opacity=".95"/>
                <circle cx="30" cy="32" r="5" fill="${accent}"/>
                <path d="m22 55 13-14 8 8 7-8 10 14" fill="${accent}" opacity=".82"/>
            `
        };
        const svg = `
            <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 80 80">
                <defs>
                    <linearGradient id="bg" x1="12" y1="10" x2="68" y2="70" gradientUnits="userSpaceOnUse">
                        <stop stop-color="${bgStart}"/>
                        <stop offset="1" stop-color="${bgEnd}"/>
                    </linearGradient>
                    <filter id="shadow" x="-20%" y="-20%" width="140%" height="140%">
                        <feDropShadow dx="0" dy="8" stdDeviation="6" flood-color="${bgEnd}" flood-opacity=".28"/>
                    </filter>
                </defs>
                <rect x="0" y="0" width="80" height="80" rx="16" fill="url(#bg)"/>
                <circle cx="66" cy="14" r="9" fill="white" opacity=".24"/>
                <circle cx="15" cy="66" r="12" fill="white" opacity=".14"/>
                ${symbols[symbol]}
            </svg>
        `;
        return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
    }

    const mockApplyList = [
        {
            id: 101,
            name: '故障快速排查与维修指导',
            category: 1,
            description:
                '面向设备运维场景，结合故障现象、报警代码和历史维修记录，快速定位可能原因并生成维修步骤建议。',
            status: 0,
            tags: '[{"name":"运维"},{"name":"维修"}]',
            icon: createIcon({
                bgStart: '#ff9f43',
                bgEnd: '#f45b69',
                accent: '#f97316',
                symbol: 'wrench'
            })
        },
        {
            id: 102,
            name: '设备健康诊断与报告编写',
            category: 1,
            description:
                '汇总巡检、运行、告警和检修数据，自动评估设备健康状态，输出结构化诊断结论和专业分析报告。',
            status: 0,
            tags: '[{"name":"设备"},{"name":"诊断"}]',
            icon: createIcon({
                bgStart: '#35d0ba',
                bgEnd: '#0ea5e9',
                accent: '#0ea5e9',
                symbol: 'health'
            })
        },
        {
            id: 103,
            name: '水雨情数据统计与简报编写',
            category: 1,
            description:
                '自动统计降雨、水位、流量等水雨情数据，生成趋势分析、异常提示和可直接发布的业务简报。',
            status: 0,
            tags: '[{"name":"水利"},{"name":"简报"}]',
            icon: createIcon({
                bgStart: '#60a5fa',
                bgEnd: '#2563eb',
                accent: '#2563eb',
                symbol: 'rain'
            })
        },
        {
            id: 104,
            name: '巡查规范答疑与隐患台账生成',
            category: 1,
            description:
                '基于巡查规范和隐患分类标准，解答现场检查问题，并将发现项自动整理为隐患台账。',
            status: 0,
            tags: '[{"name":"巡查"},{"name":"台账"}]',
            icon: createIcon({
                bgStart: '#a78bfa',
                bgEnd: '#7c3aed',
                accent: '#7c3aed',
                symbol: 'checklist'
            })
        },
        {
            id: 105,
            name: '智能用药咨询',
            category: 1,
            description:
                '围绕药品适应症、用法用量、禁忌和相互作用提供智能咨询，辅助形成安全、规范的用药建议。',
            status: 0,
            tags: '[{"name":"医疗"},{"name":"用药"}]',
            icon: createIcon({
                bgStart: '#34d399',
                bgEnd: '#059669',
                accent: '#059669',
                symbol: 'medicine'
            })
        },
        {
            id: 106,
            name: '慢病健康档案与随访报告编写',
            category: 1,
            description:
                '整合慢病患者基础信息、检查结果和随访记录，自动生成健康档案摘要与随访报告。',
            status: 0,
            tags: '[{"name":"慢病"},{"name":"随访"}]',
            icon: createIcon({
                bgStart: '#fbbf24',
                bgEnd: '#f97316',
                accent: '#f97316',
                symbol: 'folder'
            })
        },
        {
            id: 107,
            name: '水资源调度与取水许可问答',
            category: 1,
            description:
                '围绕水资源调度规则、取水许可流程和政策条款提供问答服务，辅助业务人员快速研判办理要求。',
            status: 0,
            tags: '[{"name":"水资源"},{"name":"问答"}]',
            icon: createIcon({
                bgStart: '#22d3ee',
                bgEnd: '#0284c7',
                accent: '#0284c7',
                symbol: 'water'
            })
        },
        {
            id: 108,
            name: '影像报告解读',
            category: 1,
            description:
                '结合医学影像报告文本，提取关键检查结论、异常描述和随访建议，生成通俗易懂的解读内容。',
            status: 0,
            tags: '[{"name":"影像"},{"name":"解读"}]',
            icon: createIcon({
                bgStart: '#fb7185',
                bgEnd: '#be123c',
                accent: '#e11d48',
                symbol: 'image'
            })
        }
    ].map((item) => ({
        workspaceId: 1001,
        pluginId: null,
        source: null,
        myApplyFlag: false,
        validFlag: true,
        delFlag: false,
        createTime: '2026-05-12 09:20:00',
        updateTime: '2026-05-12 09:20:00',
        kacApplyKnowledgeList: null,
        kacApplyGraphList: null,
        kacApplyBotList: null,
        ...item
    }));

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
        //     applyList.value = response.data.rows;
        //     total.value = response.data.total;
        //     loading.value = false;
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
