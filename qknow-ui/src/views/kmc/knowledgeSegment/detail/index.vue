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
        path: '/kmc/knowledgeSegment',
        component: Layout,
        redirect: 'knowledgeSegment',
        hidden: true,
        children: [
            {
                path: 'knowledgeSegmentDetail',
                component: () => import('@/views/kmc/knowledgeSegment/detail/index.vue'),
                name: 'tree',
                meta: { title: '文件分段详情', activeMenu: '/kmc/knowledgeSegment'  }
            }
        ]
    }
 -->

<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    {{ knowledgeSegmentDetail.id }}
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">ID</div>
                            <div class="infotop-row-value">{{ knowledgeSegmentDetail.id }}</div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">分段内容文本</div>
                            <div class="infotop-row-value">
                                {{ knowledgeSegmentDetail.content || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">答案内容(如果有)</div>
                            <div class="infotop-row-value">
                                {{ knowledgeSegmentDetail.answer || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">内容长度</div>
                            <div class="infotop-row-value">
                                {{ knowledgeSegmentDetail.wordCount || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">关键词</div>
                            <div class="infotop-row-value">
                                {{ knowledgeSegmentDetail.keywords || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">访问次数</div>
                            <div class="infotop-row-value">
                                {{ knowledgeSegmentDetail.hitCount || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">分段添加dify状态</div>
                            <div class="infotop-row-value">
                                <dict-tag
                                    :options="sync_status"
                                    :value="knowledgeSegmentDetail.syncStatus"
                                />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(knowledgeSegmentDetail.createTime, '{y}-{m}-{d}') }}
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

<script setup name="KnowledgeSegment">
    import { getKnowledgeSegment } from '@/api/kmc/knowledgeSegment/knowledgeSegment';
    import { useRoute } from 'vue-router';
    import ComponentOne from '@/views/kmc/knowledgeSegment/detail/componentOne.vue';
    import ComponentTwo from '@/views/kmc/knowledgeSegment/detail/componentTwo.vue';

    const { proxy } = getCurrentInstance();
    const { sync_status, segment_type } = proxy.useDict('sync_status', 'segment_type');

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
            getKnowledgeSegmentDetailById();
        },
        { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
    );
    const data = reactive({
        knowledgeSegmentDetail: {},
        form: {}
    });

    const { knowledgeSegmentDetail, rules } = toRefs(data);

    /** 复杂详情页面上方表单查询 */
    function getKnowledgeSegmentDetailById() {
        const _id = id;
        getKnowledgeSegment(_id).then((response) => {
            knowledgeSegmentDetail.value = response.data;
        });
    }

    getKnowledgeSegmentDetailById();
</script>
