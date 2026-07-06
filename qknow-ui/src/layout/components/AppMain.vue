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

<!-- eslint-disable vue/require-toggle-inside-transition -->
<template>
    <section class="app-main">
        <router-view v-slot="{ Component, route }">
            <transition name="fade-transform" mode="out-in">
                <div class="app-main-inner">
                    <keep-alive :include="tagsViewStore.cachedViews">
                        <component v-if="!route.meta.link" :is="Component" :key="route.path" />
                    </keep-alive>
                </div>
            </transition>
        </router-view>
        <iframe-toggle />
    </section>
</template>

<script setup>
    import iframeToggle from './IframeToggle/index';
    import useTagsViewStore from '@/store/system/tagsView';

    const tagsViewStore = useTagsViewStore();
</script>

<style lang="scss" scoped>
    .app-main {
        background-color: #f0f2f5;
        width: 100%;
        height: 100%;
        overflow: hidden;
        .app-main-inner {
            position: relative;
            width: 100%;
            height: 100%;
            overflow: hidden auto;
        }
    }

    .fixed-header + .app-main {
        padding-top: 60px;
    }

    .hasTagsView {
        .app-main {
            /* 84 = navbar + tags-view = 60 + 34 */
            min-height: calc(100% - 94px);
        }

        .fixed-header + .app-main {
            padding-top: 94px;
        }
    }
</style>

<style lang="scss">
    // fix css style bug in open el-dialog
    .el-popup-parent--hidden {
        .fixed-header {
            padding-right: 6px;
        }
    }

    ::-webkit-scrollbar {
        width: 6px;
        height: 6px;
    }

    ::-webkit-scrollbar-track {
        background-color: #f1f1f1;
    }

    ::-webkit-scrollbar-thumb {
        background-color: #c0c0c0;
        border-radius: 3px;
    }
</style>
