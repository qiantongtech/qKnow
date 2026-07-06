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
        path: '/kg/knowledge',
        component: Layout,
        redirect: 'knowledge',
        hidden: true,
        children: [
            {
                path: 'categoryDetail',
                component: () => import('@/views/kg/knowledge/detail/index.vue'),
                name: 'tree',
                meta: { title: '知识分类详情', activeMenu: '/kg/category'  }
            }
        ]
    }
 -->

<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    {{ categoryDetail.id }}
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">ID</div>
                            <div class="infotop-row-value">{{ categoryDetail.id }}</div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">工作区id</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.workspaceId || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">父级id</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.parentId || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">分类名称</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.name || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">显示顺序</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.orderNum || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">祖级列表</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.ancestors || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建人</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.createBy || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(categoryDetail.createTime, '{y}-{m}-{d}') }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">备注</div>
                            <div class="infotop-row-value">
                                {{ categoryDetail.remark || '-' }}
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

<script setup name="Category">
    import { getCategory } from '@/api/kg/knowledge/category';
    import { useRoute } from 'vue-router';
    import ComponentOne from '@/views/kg/knowledge/document/detail/componentOne.vue';
    import ComponentTwo from '@/views/kg/knowledge/document/detail/componentTwo.vue';

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
            getCategoryDetailById();
        },
        { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
    );
    const data = reactive({
        categoryDetail: {},
        form: {}
    });

    const { categoryDetail, rules } = toRefs(data);

    /** 复杂详情页面上方表单查询 */
    function getCategoryDetailById() {
        const _id = id;
        getCategory(_id).then((response) => {
            categoryDetail.value = response.data;
        });
    }

    getCategoryDetailById();
</script>
