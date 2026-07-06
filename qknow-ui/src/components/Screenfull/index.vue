<!--
 /*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */
-->

<template>
    <div>
        <!--    <svg-icon :icon-class="isFullscreen ? 'exit-fullscreen-new' : 'fullscreen_new_icon'" @click="toggle" :style="{width:'20px',height:'20px',paddingTop:'2px'}"/>-->

        <i
            :class="
                isFullscreen
                    ? 'iconfont icon-fullscreen-exit-line'
                    : 'iconfont icon-a-quanpingxianxing'
            "
            style="font-size: 20px"
            @click="toggle"
        ></i>
    </div>
</template>

<script setup>
    import { useFullscreen } from '@vueuse/core';

    const { isFullscreen, enter, exit, toggle } = useFullscreen();
</script>

<style lang="scss" scoped>
    .screenfull-svg {
        display: inline-block;
        cursor: pointer;
        fill: #5a5e66;
        width: 20px;
        height: 20px;
        vertical-align: 10px;
    }
</style>
