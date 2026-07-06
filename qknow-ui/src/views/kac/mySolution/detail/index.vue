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
    <div class="app-container" ref="app-container" :key="pageKey">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    <el-tag size="medium">
                        {{ solutionDetail.id }}
                    </el-tag>
                    <span style="margin-left: 8px">
                        {{ solutionDetail.name || '-' }}
                    </span>
                    <!--          <el-row :gutter="15" class="btn-style" style="margin-left: auto">-->
                    <!--            <el-col :span="1.5">-->
                    <!--              <el-button-->
                    <!--                  type="primary"-->
                    <!--                  plain-->
                    <!--                  @click="handlePublish(solutionDetail)"-->
                    <!--                  @mousedown="(e) => e.preventDefault()"-->
                    <!--                  class="custom-btn-padding"-->
                    <!--              >-->
                    <!--                {{ solutionDetail.status === 1 ? '取消发布' : '发布' }}-->
                    <!--              </el-button>-->
                    <!--            </el-col>-->
                    <!--          </el-row>-->
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">标签</div>
                            <div class="infotop-row-value">
                                <div class="multiline-ellipsis">
                                    <el-tag
                                        v-for="(tag, tagIndex) in getTags(solutionDetail)"
                                        :key="tagIndex"
                                        class="card-tag"
                                    >
                                        {{ tag.name }}
                                    </el-tag>
                                    <span v-if="getTags(solutionDetail).length <= 0">-</span>
                                </div>
                            </div>
                        </div>
                    </el-col>
                    <!--          <el-col :span="8">-->
                    <!--            <div class="infotop-row border-top">-->
                    <!--              <div class="infotop-row-lable">发布状态</div>-->
                    <!--              <div class="infotop-row-value">-->
                    <!--                <dict-tag-->
                    <!--                    :options="publish_status"-->
                    <!--                    :value="solutionDetail.status"-->
                    <!--                />-->
                    <!--              </div>-->
                    <!--            </div>-->
                    <!--          </el-col>-->
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建人</div>
                            <div class="infotop-row-value">
                                {{ solutionDetail.createBy || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">创建时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(solutionDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">最后更新时间</div>
                            <div class="infotop-row-value">
                                {{ parseTime(solutionDetail.updateTime, '{y}-{m}-{d} {h}:{i}') }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">描述</div>
                            <div class="infotop-row-value">
                                <el-tooltip :content="solutionDetail.description" placement="top">
                                    <span class="text-ellipsis">
                                        {{ solutionDetail.description || '-' }}
                                    </span>
                                </el-tooltip>
                            </div>
                        </div>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">备注</div>
                            <div class="infotop-row-value">
                                {{ solutionDetail.remark || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

        <div class="pagecont-bottom">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="关联应用" name="1">
                    <MyAppList
                        v-if="activeName === '1'"
                        :solutionId="solutionDetail.id"
                        :key="activeName"
                        :myAppList="myAppList"
                    ></MyAppList>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup name="SolutionDetail">
    import { useRoute } from 'vue-router';
    import { ref, reactive, toRefs, watch } from 'vue';
    import MyAppList from './myAppList.vue';
    import { getSolution } from '@/api/kac/solution/solution';
    import { listApply } from '@/api/kac/apply/apply.js';
    import { updateSolution } from '@/api/kac/solution/solution.js';

    const activeName = ref('1');
    const pageKey = ref(0); // 用于强制刷新页面

    const handleClick = (tab, event) => {
        console.log(tab, event);
    };

    const showSearch = ref(true);
    const route = useRoute();
    let id = route.query.id || 1;

    const data = reactive({
        solutionDetail: {}
    });

    const { proxy } = getCurrentInstance();
    const { publish_status } = proxy.useDict('publish_status');
    const { solutionDetail } = toRefs(data);

    // 应用列表
    const myAppList = ref([]);

    watch(
        () => route.query.id,
        (newId) => {
            id = newId || 1;
            getSolutionDetailById();
        },
        { immediate: true }
    );

    function getTags(row) {
        if (!row.tags) {
            return [];
        }
        return JSON.parse(row.tags);
    }

    /** 获取解决方案详情 */
    function getSolutionDetailById() {
        const _id = id;
        getSolution(_id).then((response) => {
            solutionDetail.value = response.data;
        });
    }

    // 加载下拉框数据
    function loadDropdownData() {
        // 获取应用列表
        listApply({ pageSize: 1000, pageNum: 1 }).then((response) => {
            myAppList.value = response.data.rows;
        });
    }

    /** 发布/取消发布操作 */
    const handlePublish = (row) => {
        const targetStatus = 1 - row.status;
        const actionText = targetStatus === 1 ? '发布' : '取消发布';
        const confirmMsg = `是否确认${actionText}编号为 "${row.id}" 的数据项？`;
        proxy.$modal
            .confirm(confirmMsg)
            .then(function () {
                row.status = targetStatus;
                return updateSolution(row);
            })
            .then(() => {
                proxy.$modal.msgSuccess(`${actionText}成功`);
                // 强制刷新整个页面（类似重新进入页面）
                pageKey.value++;
            })
            .catch(() => {});
    };

    // 加载所有下拉框数据
    loadDropdownData();
</script>
<style scoped lang="scss">
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
    .text-ellipsis {
        display: inline-block;
        max-width: 100%;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
</style>
