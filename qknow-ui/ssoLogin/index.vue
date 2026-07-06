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
    <div style="height: 100%" class="app-container" ref="app-container">
        <sys-info-two ref="sysInfoTwo"> </sys-info-two>
    </div>
</template>

<script>
    import SysInfoTwo from './loginPages/loginPage.vue';

    export default {
        components: {
            SysInfoTwo
        },
        name: 'Login'
    };
</script>
<style lang="scss" scoped>
    .app-container {
        padding: 0px;
        margin: 0px;
        background-color: #ffffff;
        min-height: 100%;
    }
</style>
