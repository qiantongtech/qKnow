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
        <div class="emptyBg">
            <div class="empty-content">
                <img
                    src="@/assets/system/images/no_data/noData.png"
                    alt="功能开发中"
                    class="empty-image"
                />
                <p class="empty-text">功能开发中</p>
            </div>
        </div>
    </div>
</template>

<script setup>
    // 无需逻辑，仅展示组件
</script>

<style scoped>
    .emptyBg {
        display: flex;
        justify-content: center;
        align-items: center;
        height: calc(100vh - 124px);
        padding: 40px 0;
        background-color: #fff;
    }

    .empty-content {
        text-align: center;
    }

    .empty-image {
        max-width: 300px;
        width: 100%;
        height: auto;
        margin-bottom: 16px;
    }

    .empty-text {
        font-size: 18px;
        color: #666;
    }
</style>
