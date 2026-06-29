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
    <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
        <div
            v-if="device === 'mobile' && sidebar.opened && !isSpecialPath"
            class="drawer-bg"
            @click="handleClickOutside"
        />
        <sidebar v-if="!sidebarHide" class="sidebar-container" />
        <div
            :class="{ hasTagsView: needTagsView, sidebarHide: sidebarHide }"
            class="main-container"
        >
            <div :class="{ 'fixed-header': fixedHeader }">
                <navbar @setLayout="setLayout" />
                <tags-view v-if="needTagsView" />
            </div>
            <app-main />
            <settings ref="settingRef" />
        </div>
    </div>
</template>

<script setup>
    import { useWindowSize } from '@vueuse/core';
    import Sidebar from './components/Sidebar/index.vue';
    import { AppMain, Navbar, Settings, TagsView } from './components';
    import defaultSettings from '@/settings';

    import useAppStore from '@/store/system/app';
    import useSettingsStore from '@/store/system/settings';
    import usePermissionStore from '@/store/system/permission';
    import { nextTick, computed, watch, watchEffect, ref } from 'vue';

    const settingsStore = useSettingsStore();
    const permissionStore = usePermissionStore();
    const route = useRoute();
    const theme = computed(() => settingsStore.theme);
    const sideTheme = computed(() => settingsStore.sideTheme);
    const sidebar = computed(() => useAppStore().sidebar);
    const device = computed(() => useAppStore().device);
    const needTagsView = computed(() => settingsStore.tagsView);
    const fixedHeader = computed(() => settingsStore.fixedHeader);

    // 是否隐藏侧边栏：防止首次加载闪烁
    const sidebarHide = computed(() => {
        const path = route.path;
        if (route.meta?.sidebar === false || sidebar.value.hide) return true;
        // 1. 如果是明确不需要侧边栏的页面（如配置中的 Logo 路由），直接隐藏
        const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
        if (navbarLogoRoutes.some((p) => path.startsWith(p))) return true;
        // 2. 如果已经有菜单数据了，按数据来
        if (permissionStore.sidebarRouters.length > 0) return false;
        // 3. 如果当前路由不是首页且有二级匹配，先假设有侧边栏，防止初始渲染时 v-if 销毁组件
        if (route.matched.length > 1) return false;

        return false;
    });

    const { width, height } = useWindowSize();
    const WIDTH = 992;

    const specialPaths = ['/kmc/knowledgeBase', '/user/profile'];

    const isSpecialPath = computed(() => {
        console.log('isSpecialPath', specialPaths.includes(route.path));
        return specialPaths.includes(route.path);
    });

    const classObj = computed(() => ({
        hideSidebar: !sidebar.value.opened,
        openSidebar: sidebar.value.opened,
        withoutAnimation: sidebar.value.withoutAnimation,
        mobile: device.value === 'mobile',
        'big-logo': isSpecialPath.value
    }));

    watch(
        () => device.value,
        () => {
            // 使用 isSpecialPath.value
            if (device.value === 'mobile' && sidebar.value.opened && !isSpecialPath.value) {
                useAppStore().closeSideBar({ withoutAnimation: false });
            }
        }
    );

    watch(
        () => device.value,
        () => {
            if (device.value === 'mobile' && sidebar.value.opened && !isSpecialPath.value) {
                console.log('1-->closeSideBar');
                useAppStore().closeSideBar({ withoutAnimation: false });
            } else {
                if (isSpecialPath.value) {
                    console.log('openSideBar');
                    useAppStore().openSideBar({ withoutAnimation: false });
                }
            }
        }
    );

    // 监听路由自定义参数
    watch(
        () => route,
        () => {
            // 路由动态控制tagsView的显隐
            const tagsView = route.meta.tagsView == null ? true : route.meta.tagsView;
            settingsStore.changeSetting({
                key: 'tagsView',
                value: tagsView
            });
        },
        { immediate: true, deep: true }
    );

    watchEffect(() => {
        if (width.value - 1 < WIDTH) {
            useAppStore().toggleDevice('mobile');
            nextTick(() => {
                // 使用 isSpecialPath.value
                if (!isSpecialPath.value) {
                    useAppStore().closeSideBar({ withoutAnimation: true });
                }
            });
        } else {
            useAppStore().toggleDevice('desktop');
        }
    });

    function handleClickOutside() {
        useAppStore().closeSideBar({ withoutAnimation: false });
    }

    const settingRef = ref(null);
    function setLayout() {
        settingRef.value.openSetting();
    }
</script>

<style lang="scss" scoped>
    @import '@/assets/system/styles/mixin.scss';
    @import '@/assets/system/styles/variables.module.scss';

    .app-wrapper {
        @include clearfix;
        position: relative;
        height: 100%;
        width: 100%;

        &.mobile.openSidebar {
            position: fixed;
            top: 0;
        }
    }

    .drawer-bg {
        background: #000;
        opacity: 0.3;
        width: 100%;
        top: 0;
        height: 100%;
        position: absolute;
        z-index: 999;
    }

    .fixed-header {
        position: fixed;
        top: 0;
        right: 0;
        z-index: 9;
        width: calc(100% - #{$base-sidebar-width});
        transition: width 0.28s;
    }

    .hideSidebar .fixed-header {
        width: calc(100% - 60px);
    }

    .sidebarHide .fixed-header {
        width: 100%;
    }

    .mobile .fixed-header {
        width: 100%;
    }
</style>
