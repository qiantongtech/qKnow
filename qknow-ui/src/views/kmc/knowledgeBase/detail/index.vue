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

<!-- 复杂详情路由模板
    {
        path: '/kmc/knowledgeBase',
        component: Layout,
        redirect: 'knowledgeBase',
        hidden: true,
        children: [
            {
                path: 'knowledgeBaseDetail',
                component: () => import('@/views/kmc/knowledgeBase/detail/index.vue'),
                name: 'tree',
                meta: { title: '知识库详情', activeMenu: '/kmc/knowledgeBase'  }
            }
        ]
    }
 -->

<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    {{ knowledgeBaseDetail.id }}
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">ID</div>
                            <div class="infotop-row-value">{{ knowledgeBaseDetail.id }}</div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">工作区id</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.workspaceId || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">灵桐知识库id</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.qmDatasetId || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">名称</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.name || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">描述</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.description || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">索引方式</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.indexingTechnique || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">权限</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.permission || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">Embedding 模型名称</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.embeddingModel || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">Embedding 模型供应商</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.embeddingModelProvider || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">检索方法</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.searchMethod || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">是否开启 rerank</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.rerankingEnable || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">Rerank 模型的提供商</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.rerankingProviderName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">Rerank 模型的名称</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.rerankingModelName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">召回条数</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.topK || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">是否开启召回分数限制</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.scoreThresholdEnabled || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">召回分数限制</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.scoreThreshold || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建人</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.createBy || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(knowledgeBaseDetail.createTime, '{y}-{m}-{d}') }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">备注</div>
                            <div class="infotop-row-value">
                                {{ knowledgeBaseDetail.remark || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

        <div class="pagecont-bottom">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="组件一" name="1">
                    <component-one></component-one>
                </el-tab-pane>
                <el-tab-pane label="组件二" name="2">
                    <component-two></component-two>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup name="KnowledgeBase">
    import { getKnowledgeBase } from '@/api/kmc/knowledgeBase/knowledgeBase';
    import { useRoute } from 'vue-router';
    import ComponentOne from '@/views/kmc/knowledgeBase/detail/componentOne.vue';
    import ComponentTwo from '@/views/kmc/knowledgeBase/detail/componentTwo.vue';

    const { proxy } = getCurrentInstance();

    const activeName = ref('1');

    const handleClick = (tab, event) => {
        console.log(tab, event);
    };

    const showSearch = ref(true);
    const route = useRoute();
    let id = route.query.id || 1;
    // 监听 id 变化
    watch(
        () => route.query.id,
        (newId) => {
            id = newId || 1; // 如果 id 为空，使用默认值 1
            getKnowledgeBaseDetailById();
        },
        { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
    );
    const data = reactive({
        knowledgeBaseDetail: {},
        form: {}
    });

    const { knowledgeBaseDetail, rules } = toRefs(data);

    /** 复杂详情页面上方表单查询 */
    function getKnowledgeBaseDetailById() {
        const _id = id;
        getKnowledgeBase(_id).then((response) => {
            knowledgeBaseDetail.value = response.data;
        });
    }

    getKnowledgeBaseDetailById();
</script>
