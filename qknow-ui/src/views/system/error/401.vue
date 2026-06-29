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
    <div class="errPage-container">
        <el-button icon="arrow-left" class="pan-back-btn" @click="back"> 返回 </el-button>
        <el-row>
            <el-col :span="12">
                <h1 class="text-jumbo text-ginormous">401错误!</h1>
                <h2>您没有访问权限！</h2>
                <h6>对不起，您没有访问权限，请不要进行非法操作！您可以返回主页面</h6>
                <ul class="list-unstyled">
                    <li class="link-type">
                        <router-link to="/"> 回首页 </router-link>
                    </li>
                </ul>
            </el-col>
            <el-col :span="12">
                <img :src="errGif" width="313" height="428" alt="Girl has dropped her ice cream." />
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
    import errImage from '@/assets/system/401_images/401.gif';

    let { proxy } = getCurrentInstance();

    const errGif = ref(errImage + '?' + +new Date());

    function back() {
        if (proxy.$route.query.noGoBack) {
            proxy.$router.push({ path: '/' });
        } else {
            proxy.$router.go(-1);
        }
    }
</script>

<style lang="scss" scoped>
    .errPage-container {
        width: 800px;
        max-width: 100%;
        margin: 100px auto;
        .pan-back-btn {
            background: #008489;
            color: #fff;
            border: none !important;
        }
        .pan-gif {
            margin: 0 auto;
            display: block;
        }
        .pan-img {
            display: block;
            margin: 0 auto;
            width: 100%;
        }
        .text-jumbo {
            font-size: 60px;
            font-weight: 700;
            color: #484848;
        }
        .list-unstyled {
            font-size: 14px;
            li {
                padding-bottom: 5px;
            }
            a {
                color: #008489;
                text-decoration: none;
                &:hover {
                    text-decoration: underline;
                }
            }
        }
    }
</style>
