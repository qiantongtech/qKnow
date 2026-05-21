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
