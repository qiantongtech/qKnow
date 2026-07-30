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
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          <el-tag size="medium" class="id-tag">
            {{ mcpConfigDetail.id }}
          </el-tag>
          <span style="margin-left: 8px">
            {{ mcpConfigDetail.name || "-" }}
          </span>
          <el-row :gutter="15" class="btn-style" style="margin-left: auto">
            <el-col :span="1.5">
              <el-button
                  type="primary"
                  size="small"
                  class="fhbtn"
                  plain
                  @click="handleReturn"
                  @mousedown="(e) => e.preventDefault()"
              >
                <svg-icon style="width: 1em;height: 1em; margin-right: 3px;" :iconClass="'fhs'"/>
                返回
              </el-button>
            </el-col>
          </el-row>
        </div>
        <el-row :gutter="3" style="margin-bottom: 3px;">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">名称</div>
              <div class="infotop-row-value">
                {{ mcpConfigDetail.name || '-' }}
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">类型</div>
              <div class="infotop-row-value">
                <!--                {{ mcpConfigDetail.type || '-' }}-->
                <dict-tag :options="bot_mcp_type" :value="mcpConfigDetail.type"/>
              </div>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">状态</div>
              <div class="infotop-row-value">
                <dict-tag :options="bot_mcp_status" :value="mcpConfigDetail.status"/>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="3" style="margin-bottom: 3px;">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">url</div>
              <div class="infotop-row-value">
                {{ mcpConfigDetail.url || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">命令</div>
              <div class="infotop-row-value">
                {{ mcpConfigDetail.command || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建时间</div>
              <div class="infotop-row-value">{{ parseTime(mcpConfigDetail.createTime, '{y}-{m}-{d} {h}:{i}') }}</div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="3">
          <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ mcpConfigDetail.description || '-' }}
              </div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs">
        <el-tab-pane label="工具" name="1">
          <mcp-tool-list :mcpId="id"></mcp-tool-list>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script setup name="McpConfig">
import {getMcpConfig} from "@/api/kb/mcp/mcpConfig";
import {useRoute,useRouter} from 'vue-router';
import McpToolList from "@/views/kb/mcp/detail/mcpToolList.vue";

const {proxy} = getCurrentInstance();

const activeName = ref('1')
const {bot_mcp_type, bot_mcp_status} = proxy.useDict("bot_mcp_type", "bot_mcp_status");

const showSearch = ref(true);
const route = useRoute();
const router = useRouter();
let id = route.query.id || 1;
// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
      id = newId || 1;  // 如果 id 为空，使用默认值 1
      getMcpConfigDetailById();
    },
    {immediate: true}  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  mcpConfigDetail: {},
  form: {},
});

const {mcpConfigDetail, rules} = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getMcpConfigDetailById() {
  const _id = id;
  getMcpConfig(_id).then(response => {
    mcpConfigDetail.value = response.data;
  });
}

const handleReturn = () => {
  let path = "/kb/mcp";
  router.push({
    path: path,
  });
};

getMcpConfigDetailById();

</script>

<style scoped lang="scss">
.fhbtn {
  .svg-icon {
    font-size: 12px;
    margin-right: 3px;
    vertical-align: middle;
    margin-top: -3px;
  }

  &:hover {
    .svg-icon {
      filter: brightness(0) invert(1) !important;
    }
  }
}
</style>
