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
  <div class="container" id="top">
    <p class="title">{{ notice.noticeTitle }}</p>
    <p class="time">
      时间：{{ notice.createTime }}<span class="separator">丨</span>作者：{{
        notice.createBy || "-"
      }}
    </p>
    <div class="content" id="notice-content">
      <div class="content-line">
        <template
          v-if="notice.noticeContent != null && notice.noticeContent !== ''"
        >
          <div v-html="notice.noticeContent"></div>
        </template>
        <div class="empty" v-else>
          <img
            src="@/assets/system/images/no_data/noData.png"
            alt="暂无公告内容"
          />
          <span>暂无公告内容</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="NoticeDetail">
import moment from "moment";
import { getNotice } from "@/api/system/system/notice.js";

const route = useRoute();
const notice = ref({
  noticeTitle: "",
  noticeContent: "",
  createTime: "",
  createBy: "",
});

watch(
  () => route.query.id,
  (noticeId) => {
    if (noticeId != null && noticeId !== "") {
      loadNotice(noticeId);
    }
  },
  { immediate: true }
);

function loadNotice(noticeId) {
  getNotice(noticeId).then((response) => {
    notice.value = response.data || {};
    if (notice.value.createTime) {
      notice.value.createTime = moment(notice.value.createTime).format(
        "YYYY-MM-DD"
      );
    }
  });
}
</script>

<style scoped lang="scss">
.container {
  min-height: calc(100vh - 94px) !important;
  margin: 15px;
  padding: 20px 0;
  scroll-behavior: smooth;
  background-color: #ffffff;
}

.header-line {
  width: 4px;
  height: 28px;
  margin: 0 auto 10px;
  border-radius: 2px;
  background-color: var(--el-color-primary);
}

.title {
  display: inline-block;
  width: 100%;
  margin: 10px 0;
  color: #333333;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  word-break: break-word;
}

.time {
  width: 100%;
  margin: 10px 0;
  color: #999999;
  font-size: 14px;
  text-align: center;
}

.separator {
  margin: 0 6px;
}

.content {
  max-width: 920px;
  margin: 0 auto;
  padding: 0 10px;
  overflow-wrap: break-word;
  color: #333333;
  font-size: 14px;
  line-height: 1.8;

  :deep(.content-line) {
    padding: 10px 0;
    border-top: 1px dashed #dddddd;
    border-bottom: 1px dashed #dddddd;
  }

  :deep(h1),
  :deep(h2),
  :deep(h3) {
    margin: 0.67em 0;
    font-weight: 600;
  }

  :deep(h1) {
    font-size: 2em;
  }

  :deep(h2) {
    margin: 0.83em 0;
    font-size: 1.5em;
  }

  :deep(h3) {
    font-size: 1.17em;
  }

  :deep(p) {
    margin: 1em 0;
  }

  :deep(ul),
  :deep(ol) {
    margin: 1em 0;
    padding-left: 2em;
  }

  :deep(img) {
    display: block;
    width: 100%;
    height: auto;
    margin: 1em 0;
    border-radius: 2px;
    text-align: center;
  }

  :deep(a) {
    color: var(--el-color-primary);
    text-decoration: underline;
  }

  :deep(pre) {
    padding: 12px;
    overflow-x: auto;
    color: #f8f8f2;
    background: #23241f;
    border-radius: 2px;
    white-space: pre-wrap;
    word-wrap: break-word;
  }

  :deep(blockquote) {
    margin: 5px 0;
    padding-left: 16px;
    border-left: 4px solid #cccccc;
  }
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #909399;
  background: #ffffff;

  img {
    width: 300px;
    height: 300px;
  }
}
</style>
