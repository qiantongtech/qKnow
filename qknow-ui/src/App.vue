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
    <!-- 页面内容 -->
    <el-watermark
        class="watermark"
        v-if="watermarkText"
        :font="config.font"
        :content="watermarkText"
        :gap="[200, 150]"
        :width="120"
        :height="80"
    >
        <router-view />
    </el-watermark>
    <router-view v-else />
</template>

<script setup>
    import useSettingsStore from '@/store/system/settings';
    import { handleThemeStyle } from '@/utils/theme';
    import { useRoute } from 'vue-router'; // 引入 useRoute 钩子
    import useUserStore from '@/store/system/user';
    import { alertEffects } from 'element-plus';
    onMounted(() => {
        nextTick(() => {
            // 初始化ico
            createIco();
            // 初始化主题样式
            handleThemeStyle(useSettingsStore().theme);
        });
    });
    // 使用 useRoute 钩子获取当前路由对象
    const route = useRoute();
    const storedUser = useUserStore();
    // 计算水印文本，动态获取当前路由的名称
    const watermarkText = computed(() => {
        if (localStorage.getItem('username')) {
            if (route.path != '/login' && route.path != '/sso/login') {
                return localStorage.getItem('username') || '默认水印'; //需要水印赋值不需要给空
            } else {
                return '';
            }
        }
    });
    const config = reactive({
        content: 'Element Plus',
        font: {
            fontSize: 16,
            color: 'rgba(0, 0, 0, 0.15)',
            textBaseline: 'top'
        }
    });
    const createIco = () => {
        let link = document.querySelector("link[rel*='icon']") || document.createElement('link');
        link.type = 'image/x-icon';
        link.rel = 'shortcut icon';
        link.href = '/favicon.ico';
        document.getElementsByTagName('head')[0].appendChild(link);
    };
</script>

<style scoped>
    .watermark {
        position: relative;
        height: 100%;
        width: 100%;
        overflow: hidden;
    }
</style>
