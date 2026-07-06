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
    <div class="app-container">
        <el-row :gutter="15">
            <el-col :span="6" :xs="24">
                <el-card class="box-card">
                    <template v-slot:header>
                        <div class="head-container">
                            <span class="head-title"></span>
                            <span>个人信息</span>
                        </div>
                    </template>
                    <div>
                        <div class="text-center">
                            <userAvatar />
                        </div>
                        <ul class="list-group list-group-striped">
                            <li class="list-group-item vertical-center">
                                <i class="iconfont icon-a-yonghuzhanghaoxianxing mr5"></i>
                                用户名称
                                <div class="pull-right label-text">{{ state.user.userName }}</div>
                            </li>
                            <li class="list-group-item vertical-center">
                                <i class="iconfont icon-a-shoujixianxing mr5"></i>
                                手机号码
                                <div class="pull-right label-text">
                                    {{ state.user.phonenumber }}
                                </div>
                            </li>
                            <li class="list-group-item vertical-center">
                                <i class="iconfont icon-a-yonghuyouxiangxianxing mr5"></i>
                                用户邮箱
                                <div class="pull-right label-text">{{ state.user.email }}</div>
                            </li>
                            <li class="list-group-item vertical-center">
                                <i class="iconfont icon-bumen margin-right-5 mr5"></i>
                                所属部门
                                <div class="pull-right label-text" v-if="state.user.dept">
                                    {{ state.user.dept.deptName }} /
                                    {{ state.postGroup }}
                                </div>
                            </li>
                            <li class="list-group-item vertical-center">
                                <i class="iconfont icon-a-suoshujiaosexianxing mr5"></i>
                                所属角色
                                <div class="pull-right label-text">{{ state.roleGroup }}</div>
                            </li>
                            <li class="list-group-item vertical-center">
                                <i class="iconfont icon-a-riqixianxing mr5"></i>
                                创建日期
                                <div class="pull-right label-text">{{ state.user.createTime }}</div>
                            </li>
                        </ul>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="18" :xs="24">
                <el-card>
                    <template v-slot:header>
                        <div class="head-container">
                            <span class="head-title"></span>
                            <span>基本资料</span>
                        </div>
                    </template>
                    <el-tabs v-model="activeTab">
                        <el-tab-pane label="基本资料" name="userinfo">
                            <userInfo :user="state.user" />
                        </el-tab-pane>
                        <el-tab-pane label="修改密码" name="resetPwd">
                            <resetPwd />
                        </el-tab-pane>
                    </el-tabs>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script setup name="Profile">
    import userAvatar from './userAvatar.vue';
    import userInfo from './userInfo.vue';
    import resetPwd from './resetPwd.vue';
    import { getUserProfile } from '@/api/system/system/user.js';

    const activeTab = ref('userinfo');
    const state = reactive({
        user: {},
        roleGroup: {},
        postGroup: {}
    });

    function getUser() {
        getUserProfile().then((response) => {
            state.user = response.data;
            state.roleGroup = response.roleGroup;
            state.postGroup = response.postGroup;
        });
    }

    getUser();
</script>
<style scoped lang="scss">
    .label-text {
        color: #888888;
        position: absolute;
        right: 10px;
    }

    :deep {
        .el-tabs__item.is-active {
            color: var(--el-color-primary);
        }

        .el-tabs__item:hover {
            background-color: transparent !important; /* 去掉背景色变化 */
            color: var(--el-color-primary); /* 字体颜色不变 */
        }

        .el-tabs__active-bar {
            background-color: var(--el-color-primary);
        }
        .el-card {
            .el-card__header {
                padding: 14px !important;
            }
        }
    }

    .box-card {
        min-width: 260px;
    }

    .vertical-center {
        display: flex;
        align-items: center;
        position: relative;
    }
    .head-container {
        display: flex;
        flex-direction: row;
        align-items: center;
    }
    .head-title {
        display: inline-block;
        content: '';
        width: 6px;
        height: 16px;
        border-radius: 2px;
        background: var(--el-color-primary);
        margin-right: 10px;
    }
</style>
