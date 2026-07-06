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
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    <el-tag class="id-tag">
                        {{ toolDetail.id }}
                    </el-tag>
                    <span style="margin-left: 8px">
                        {{ toolDetail.name || '-' }}
                    </span>
                    <el-row :gutter="15" class="btn-style" style="margin-left: auto">
                        <el-col :span="1.5">
                            <el-button
                                type="primary"
                                size="small"
                                class="fhbtn"
                                plain
                                @click="handleReturn"
                                @mousedown="(e) => e.preventDefault()"
                            >
                                <svg-icon
                                    style="width: 1em; height: 1em; margin-right: 3px"
                                    :iconClass="'fhs'"
                                />
                                返回
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
                <el-row :gutter="3" style="margin-bottom: 3px">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">标签</div>
                            <div class="infotop-row-value">
                                <div class="multiline-ellipsis">
                                    <el-tag
                                        v-for="(tag, tagIndex) in getTags(toolDetail)"
                                        :key="tagIndex"
                                        size="medium"
                                        class="card-tag"
                                    >
                                        {{ tag.name }}
                                    </el-tag>
                                    <span v-if="getTags(toolDetail).length <= 0">-</span>
                                </div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建人</div>
                            <div class="infotop-row-value">
                                {{ toolDetail.createBy || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(toolDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <el-row style="margin-bottom: 3px">
                    <el-col :span="24">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">描述</div>
                            <div class="infotop-row-value">
                                {{ toolDetail.description || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">备注</div>
                            <div class="infotop-row-value">
                                {{ toolDetail.remark || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

        <div class="pagecont-bottom">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="工具方法" name="1">
                    <Method :toolId="toolDetail.id" :key="toolDetail.id"></Method>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup name="Tool">
    import { getTool } from '@/api/kb/tool/tool';
    import { useRoute } from 'vue-router';
    import Method from '@/views/kb/tool/detail/method.vue';
    const { proxy } = getCurrentInstance();

    const activeName = ref('1');

    const handleClick = (tab, event) => {};

    const showSearch = ref(true);
    const route = useRoute();
    let id = route.query.id || 1;
    // 监听 id 变化
    watch(
        () => route.query.id,
        (newId) => {
            id = newId || 1; // 如果 id 为空，使用默认值 1
            getToolDetailById();
        },
        { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
    );
    const data = reactive({
        toolDetail: {},
        form: {}
    });

    const { toolDetail, rules } = toRefs(data);

    /** 复杂详情页面上方表单查询 */
    function getToolDetailById() {
        const _id = id;
        getTool(_id).then((response) => {
            toolDetail.value = response.data;
        });
    }

    getToolDetailById();

    function getTags(row) {
        if (!row.tags) {
            return [];
        }
        return JSON.parse(row.tags);
    }

    const handleReturn = () => {
        const obj = { path: `/kb/tool` };
        proxy.$tab.closeOpenPage(obj);
    };
</script>
<style scoped lang="scss">
    .id-tag {
        display: inline-flex;
        justify-content: center;
        align-items: center;
        padding: 2px;
        background-color: #2666fb;
        color: #fff;
        aspect-ratio: 1 / 1;
        width: 20px;
        height: 20px;
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
    .svg-icon {
        font-size: 12px;
        margin-right: 3px;
        vertical-align: middle;
        margin-top: -3px;
    }
    .fhbtn {
        .svg-icon {
            font-size: 12px;
            margin-right: 3px;
            vertical-align: middle;
            margin-top: -3px;
        }
        &:hover {
            .svg-icon {
                filter: brightness(0) invert(1) !important;
            }
        }
    }
</style>
