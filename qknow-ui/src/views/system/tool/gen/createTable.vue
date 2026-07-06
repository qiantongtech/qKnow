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
    <!-- 创建表 -->
    <el-dialog
        title="创建表"
        v-model="visible"
        width="800px"
        top="5vh"
        :append-to="$refs['app-container']"
        draggable
        destroy-on-close
    >
        <span>创建表语句(支持多个建表语句)：</span>
        <el-input type="textarea" :rows="10" placeholder="请输入文本" v-model="content"></el-input>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false">取 消</el-button>
                <el-button type="primary" @click="handleImportTable">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
    import { createTable } from '@/api/system/tool/gen.js';

    const visible = ref(false);
    const content = ref('');
    const { proxy } = getCurrentInstance();
    const emit = defineEmits(['ok']);

    /** 显示弹框 */
    function show() {
        visible.value = true;
    }

    /** 导入按钮操作 */
    function handleImportTable() {
        if (content.value === '') {
            proxy.$modal.msgError('请输入建表语句');
            return;
        }
        createTable({ sql: content.value }).then((res) => {
            proxy.$modal.msgSuccess(res.msg);
            if (res.code === 200) {
                visible.value = false;
                emit('ok');
            }
        });
    }

    defineExpose({
        show
    });
</script>
