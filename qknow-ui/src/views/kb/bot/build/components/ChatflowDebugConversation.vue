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
  <div ref="conversationRef" class="chatflow-debug-conversation">
    <div
      v-if="!normalizedMessages.length"
      class="chatflow-debug-conversation__empty"
    >
      <div class="chatflow-debug-conversation__empty-title">
        开始调试 Chatflow
      </div>
      <div class="chatflow-debug-conversation__empty-desc">
        发送第一条消息后，这里会展示完整的对话调试记录。
      </div>
    </div>

    <div v-else class="chatflow-debug-conversation__list">
      <div
        v-for="message in normalizedMessages"
        :key="message.id"
        class="chatflow-debug-conversation__item"
        :class="[`is-${message.role}`]"
      >
        <div
          class="chatflow-debug-conversation__avatar"
          :class="{ 'is-image': Boolean(getAvatarImage(message)) }"
        >
          <img
            v-if="getAvatarImage(message)"
            class="chatflow-debug-conversation__avatar-image"
            :class="{ 'is-cover': message.role === 'user' }"
            :src="getAvatarImage(message)"
            :alt="message.role === 'assistant' ? 'AI' : '用户'"
          />
          <template v-else>
            {{ getRoleAvatarText(message) }}
          </template>
        </div>

        <div class="chatflow-debug-conversation__body">
          <div
            class="chatflow-debug-conversation__meta"
            :class="{ 'is-placeholder': message.role !== 'assistant' }"
          >
            <!-- <span class="chatflow-debug-conversation__name">
              {{ message.role === "assistant" ? getRoleLabel(message) : "" }}
            </span> -->
            <span
              v-if="
                message.role === 'assistant' && message.status === 'streaming'
              "
              class="chatflow-debug-conversation__status"
            >
              生成中...
            </span>
          </div>

          <div class="chatflow-debug-conversation__bubble">
            <div
              v-if="
                message.role !== 'assistant' ||
                !`${message.content || ''}`.trim()
              "
              class="chatflow-debug-conversation__text"
            >
              {{
                message.role === "assistant" && message.status === "streaming"
                  ? "正在生成回复..."
                  : message.content || "暂无内容"
              }}
            </div>
            <MarkdownView
              v-else
              class="chatflow-debug-conversation__markdown"
              :content="message.content"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, ref, watch } from "vue";
import MarkdownView from "@/components/MarkdownView/index.vue";
import assistantAvatar from "@/assets/app/gpt-new.svg";
import userAvatar from "@/assets/system/images/profile.jpg";

const props = defineProps({
  messages: {
    type: Array,
    default: () => [],
  },
});

const conversationRef = ref(null);

const normalizedMessages = computed(() =>
  Array.isArray(props.messages)
    ? props.messages.filter((message) => message && message.id)
    : []
);

const lastMessageSignature = computed(() => {
  const lastMessage =
    normalizedMessages.value[normalizedMessages.value.length - 1] || null;

  if (!lastMessage) {
    return "";
  }

  return [
    lastMessage.id,
    lastMessage.role,
    lastMessage.status,
    `${lastMessage.content || ""}`,
  ].join("::");
});

async function scrollToBottom() {
  await nextTick();
  const container = conversationRef.value;

  if (!container) {
    return;
  }

  container.scrollTop = container.scrollHeight;
}

function getRoleLabel(message = {}) {
  return message.role === "assistant" ? "助手" : "你";
}

function getRoleAvatarText(message = {}) {
  return message.role === "assistant" ? "AI" : "你";
}

function getAvatarImage(message = {}) {
  if (message.role === "assistant") {
    return assistantAvatar;
  }

  if (message.role === "user") {
    return userAvatar;
  }

  return "";
}

watch(
  () => normalizedMessages.value.length,
  () => {
    scrollToBottom();
  }
);

watch(lastMessageSignature, () => {
  scrollToBottom();
});

defineExpose({
  scrollToBottom,
});
</script>

<style scoped lang="scss">
.chatflow-debug-conversation {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding-right: 4px;
}

.chatflow-debug-conversation__empty {
  min-height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 32px 20px;
  border: 1px dashed #dcdfe6;
  background-color: #fafafa;
  text-align: center;
}

.chatflow-debug-conversation__empty-badge {
  min-width: 48px;
  height: 32px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  color: #606266;
  font-size: 12px;
  font-weight: 600;
  line-height: 32px;
}

.chatflow-debug-conversation__empty-title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
}

.chatflow-debug-conversation__empty-desc {
  max-width: 260px;
  font-size: 13px;
  line-height: 1.7;
  color: #6b7280;
}

.chatflow-debug-conversation__list {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 4px 0;
}

.chatflow-debug-conversation__item {
  width: 100%;
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.chatflow-debug-conversation__item.is-user {
  justify-content: flex-end;
  flex-direction: row-reverse;
}

.chatflow-debug-conversation__avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid #dcdfe6;
  background: linear-gradient(135deg, #ffffff 0%, #f4f6fa 100%);
  color: #303133;
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  box-shadow: 0 4px 10px rgba(17, 24, 39, 0.08);
}

.chatflow-debug-conversation__avatar.is-image {
  border: none;
  background: transparent;
  box-shadow: none;
}

.chatflow-debug-conversation__avatar-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.chatflow-debug-conversation__avatar-image.is-cover {
  object-fit: cover;
  border-radius: 50%;
}

.chatflow-debug-conversation__item.is-assistant
  .chatflow-debug-conversation__avatar {
  color: #fff;
}

.chatflow-debug-conversation__item.is-user
  .chatflow-debug-conversation__avatar {
  border-color: #c7d2fe;
  background: linear-gradient(135deg, #312e81 0%, #1d4ed8 100%);
  color: #fff;
}

.chatflow-debug-conversation__body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.chatflow-debug-conversation__item.is-assistant
  .chatflow-debug-conversation__body {
  max-width: calc(100% - 44px);
}

.chatflow-debug-conversation__item.is-user .chatflow-debug-conversation__body {
  flex: 0 1 82%;
  align-items: flex-end;
}

.chatflow-debug-conversation__meta {
  min-height: 20px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #6b7280;
}

.chatflow-debug-conversation__meta.is-placeholder {
  visibility: hidden;
}

.chatflow-debug-conversation__name {
  color: #111827;
  font-weight: 500;
}

.chatflow-debug-conversation__status {
  color: #0f766e;
}

.chatflow-debug-conversation__bubble {
  width: fit-content;
  max-width: min(100%, 82%);
  padding: 14px 16px;
  border-radius: 4px;
  background-color: #f0f0f6;
  border: none;
  color: #707070;
  overflow: hidden;
  box-shadow: none;
}

.chatflow-debug-conversation__item.is-user
  .chatflow-debug-conversation__bubble {
  min-width: 72px;
  max-width: min(100%, 100%);
  border-radius: 4px;
  background: #257fff;
  border: none;
  color: #fff;
}

.chatflow-debug-conversation__text {
  font-size: 14px;
  line-height: 1.75;
  white-space: pre-wrap;
  word-break: break-word;
}

.chatflow-debug-conversation__markdown {
  width: 100%;
  color: inherit;

  :deep(.markdown-view) {
    color: inherit;
  }

  :deep(.markdown-body) {
    background-color: transparent;
    color: inherit;
    font-size: 14px;
    line-height: 1.75;
    word-break: break-word;
  }

  :deep(.markdown-body a) {
    color: inherit;
  }

  :deep(.markdown-body > :first-child) {
    margin-top: 0 !important;
  }

  :deep(.markdown-body > :last-child) {
    margin-bottom: 0 !important;
  }

  :deep(.markdown-body p) {
    margin: 0 0 10px;
  }

  :deep(.markdown-body p:last-child) {
    margin-bottom: 0;
  }

  :deep(pre) {
    max-width: 100%;
    overflow-x: auto;
  }
}
</style>
