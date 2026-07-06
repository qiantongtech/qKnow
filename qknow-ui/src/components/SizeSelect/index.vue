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
        <el-dropdown trigger="click" @command="handleSetSize">
            <div class="size-icon--style">
                <svg-icon class-name="size-icon" icon-class="size" />
            </div>
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item
                        v-for="item of sizeOptions"
                        :key="item.value"
                        :disabled="size === item.value"
                        :command="item.value"
                    >
                        {{ item.label }}
                    </el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </div>
</template>

<script setup>
    import useAppStore from '@/store/system/app';

    const appStore = useAppStore();
    const size = computed(() => appStore.size);
    const route = useRoute();
    const router = useRouter();
    const { proxy } = getCurrentInstance();
    const sizeOptions = ref([
        { label: '较大', value: 'large' },
        { label: '默认', value: 'default' },
        { label: '稍小', value: 'small' }
    ]);

    function handleSetSize(size) {
        proxy.$modal.loading('正在设置布局大小，请稍候...');
        appStore.setSize(size);
        setTimeout('window.location.reload()', 1000);
    }
</script>

<style lang="scss" scoped>
    .size-icon--style {
        font-size: 18px;
        line-height: 50px;
        padding-right: 7px;
    }
</style>
