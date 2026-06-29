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
    <div>
        <template v-for="(item, index) in options">
            <template v-if="values.includes(item.value)">
                <span
                    v-if="
                        (item.elTagType == 'default' || item.elTagType == '') &&
                        (item.elTagClass == '' || item.elTagClass == null)
                    "
                    :key="item.value"
                    :index="index"
                    :class="item.elTagClass"
                    >{{ item.label + ' ' }}</span
                >
                <el-tag
                    v-else
                    :disable-transitions="true"
                    :key="item.value + ''"
                    :index="index"
                    :type="item.elTagType"
                    :class="item.elTagClass"
                    >{{ item.label + ' ' }}</el-tag
                >
                <!-- item.elTagType === 'primary' ? '' : item.elTagType 不知道为什么改？ -->
            </template>
        </template>
        <template v-if="unmatch && showValue">
            {{ unmatchArray | handleArray }}
        </template>
    </div>
</template>

<script setup>
    // 记录未匹配的项
    const unmatchArray = ref([]);

    const props = defineProps({
        // 数据
        options: {
            type: Array,
            default: null
        },
        // 当前的值
        value: [Number, String, Array],
        // 当未找到匹配的数据时，显示value
        showValue: {
            type: Boolean,
            default: true
        },
        separator: {
            type: String,
            default: ','
        }
    });

    const values = computed(() => {
        if (props.value === null || typeof props.value === 'undefined' || props.value === '')
            return [];
        return Array.isArray(props.value)
            ? props.value.map((item) => '' + item)
            : String(props.value).split(props.separator);
    });

    const unmatch = computed(() => {
        unmatchArray.value = [];
        // 没有value不显示
        if (
            props.value === null ||
            typeof props.value === 'undefined' ||
            props.value === '' ||
            props.options.length === 0
        )
            return false;
        // 传入值为数组
        let unmatch = false; // 添加一个标志来判断是否有未匹配项
        values.value.forEach((item) => {
            if (!props.options.some((v) => v.value === item)) {
                unmatchArray.value.push(item);
                unmatch = true; // 如果有未匹配项，将标志设置为true
            }
        });
        return unmatch; // 返回标志的值
    });

    function handleArray(array) {
        if (array.length === 0) return '';
        return array.reduce((pre, cur) => {
            return pre + ' ' + cur;
        });
    }
</script>

<style scoped>
    .el-tag + .el-tag {
        margin-left: 10px;
    }
</style>
