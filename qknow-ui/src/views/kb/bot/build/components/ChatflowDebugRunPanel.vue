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
  <div class="chatflow-debug-run-panel">
    <div v-if="showSections" class="chatflow-debug-run-panel__input-block">
      <div class="chatflow-debug-run-panel__title">
        <span class="blue-bar"></span>输入参数
      </div>

      <el-form
        class="chatflow-debug-run-panel__form"
        @submit.prevent
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <template v-if="displayFields.length">
              <el-form-item
                v-for="field in displayFields"
                :key="getFieldKey(field)"
                :label="getFieldLabel(field)"
              >
                <template #label>
                  <DebugOverflowTooltipLabel :text="getFieldLabel(field)" />
                </template>
                <el-input
                  v-model="formData[getFieldKey(field)]"
                  :placeholder="`请输入${getFieldLabel(field)}`"
                />
              </el-form-item>
            </template>

            <div v-else class="chatflow-debug-run-panel__empty-hint">
              当前没有输入变量，请先添加。
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <section class="chatflow-debug-run-panel__conversation-shell">
      <div class="chatflow-debug-run-panel__conversation-header">
        <div class="chatflow-debug-run-panel__conversation-title">
          <span class="blue-bar"></span>对话调试
        </div>
      </div>

      <ChatflowDebugConversation ref="conversationRef" :messages="messages" />
    </section>

    <form
      class="chatflow-debug-run-panel__composer"
      @submit.prevent="handleSend"
    >
      <el-input
        v-model="prompt"
        class="chatflow-debug-run-panel__composer-input"
        type="textarea"
        resize="none"
        :autosize="{ minRows: 2, maxRows: 6 }"
        placeholder="问我任何问题...(Shift+Enter 换行，按 Enter 发送)"
        @keydown.enter="handlePromptKeydown"
      />

      <div class="chatflow-debug-run-panel__composer-actions">
        <div class="chatflow-debug-run-panel__composer-buttons">
          <el-button
            v-if="running"
            type="danger"
            plain
            native-type="button"
            @click="stopStream()"
          >
            停止
          </el-button>
          <el-button
            v-else
            type="primary"
            native-type="submit"
            class="chatflow-debug-run-panel__send-button"
            :disabled="!canSend"
          >
            发送
          </el-button>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, ref, watch } from "vue";
import { ProcessChstFlow } from "@/api/kb/bot/flow.js";
import ChatflowDebugConversation from "./ChatflowDebugConversation.vue";
import DebugOverflowTooltipLabel from "./DebugOverflowTooltipLabel.vue";

const props = defineProps({
  fields: {
    type: Array,
    default: () => [],
  },
  workflowData: {
    type: Object,
    default: () => ({
      nodes: [],
      edges: [],
    }),
  },
  beforeRun: {
    type: Function,
    default: null,
  },
  showSections: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(["run", "update:showSections"]);

const conversationRef = ref(null);
const formData = ref({});
const prompt = ref("");
const messages = ref([]);
const running = ref(false);
const conversationInAbortController = ref(null);
const streamStoppedManually = ref(false);
let messageSeed = 0;

const canSend = computed(
  () => !running.value && Boolean(`${prompt.value || ""}`.trim())
);

const chatPromptField = computed(() =>
  detectChatPromptField(Array.isArray(props.fields) ? props.fields : [])
);
const chatPromptFieldKey = computed(() => getFieldKey(chatPromptField.value));
const displayFields = computed(() =>
  (Array.isArray(props.fields) ? props.fields : []).filter(
    (field) => getFieldKey(field) !== chatPromptFieldKey.value
  )
);

watch(
  () => props.fields,
  (fields) => {
    const previousValues = formData.value || {};
    const nextValues = {};

    (Array.isArray(fields) ? fields : []).forEach((field) => {
      const fieldKey = getFieldKey(field);

      if (!fieldKey) {
        return;
      }

      nextValues[fieldKey] = Object.prototype.hasOwnProperty.call(
        previousValues,
        fieldKey
      )
        ? previousValues[fieldKey]
        : getInitialFieldValue(field);
    });

    formData.value = nextValues;
  },
  {
    immediate: true,
    deep: true,
  }
);

function getFieldKey(field = {}) {
  return `${field?.name || field?.id || ""}`.trim();
}

function getFieldLabel(field = {}) {
  return `${field?.label || field?.name || "未命名变量"}`.trim();
}

function getInitialFieldValue(field = {}) {
  return field?.defaultValue === undefined || field?.defaultValue === null
    ? ""
    : `${field.defaultValue}`;
}

function detectChatPromptField(fields = []) {
  const normalizedFields = Array.isArray(fields) ? fields : [];

  return (
    normalizedFields.find(
      (field) => getFieldKey(field).toLowerCase() === "userinput.query"
    ) ||
    normalizedFields.find((field) => {
      const fieldKey = getFieldKey(field).toLowerCase();

      return fieldKey === "query" || fieldKey.endsWith(".query");
    }) ||
    normalizedFields.find((field) => {
      const fieldKey = getFieldKey(field).toLowerCase();
      const fieldLabel = getFieldLabel(field).toLowerCase();

      return (
        fieldKey === "user" ||
        fieldLabel === "userinput" ||
        fieldLabel === "用户"
      );
    }) ||
    normalizedFields.find((field) => {
      const fieldKey = getFieldKey(field).toLowerCase();
      const fieldLabel = getFieldLabel(field).toLowerCase();

      return (
        fieldKey.includes("query") ||
        fieldKey.includes("question") ||
        fieldLabel.includes("问题") ||
        fieldLabel.includes("消息") ||
        fieldLabel.includes("对话")
      );
    }) ||
    null
  );
}

function buildBaseInputValues() {
  return (Array.isArray(props.fields) ? props.fields : []).reduce(
    (result, field) => {
      const fieldKey = getFieldKey(field);

      if (!fieldKey) {
        return result;
      }

      result[fieldKey] = formData.value[fieldKey] ?? "";
      return result;
    },
    {}
  );
}

function buildChatInputValues(messageText = "") {
  const inputValues = buildBaseInputValues();
  const nextMessageText = `${messageText || ""}`;

  if (chatPromptFieldKey.value) {
    inputValues[chatPromptFieldKey.value] = nextMessageText;
  }

  inputValues.query = nextMessageText;

  return inputValues;
}

function createMessage(role, content = "", overrides = {}) {
  messageSeed += 1;

  return {
    id: `chatflow-debug-message-${messageSeed}`,
    role,
    content,
    status: "done",
    ...overrides,
  };
}

function updateMessage(messageId, patch = {}) {
  messages.value = messages.value.map((message) =>
    message.id === messageId ? { ...message, ...patch } : message
  );
}

function buildConversationHistory(nextUserMessage = null) {
  const historyMessages = nextUserMessage
    ? [...messages.value, nextUserMessage]
    : [...messages.value];

  return historyMessages
    .filter(
      (message) =>
        message &&
        (message.role === "user" || message.role === "assistant") &&
        `${message.content || ""}`.trim()
    )
    .map((message) => ({
      role: message.role,
      context: message.content,
    }));
}

function normalizeDebugError(error) {
  if (streamStoppedManually.value || error?.name === "AbortError") {
    return "已停止本轮调试。";
  }

  return (
    `${error?.message || ""}`.trim() ||
    "Chatflow 调试请求失败，请检查接口接入实现。"
  );
}

function normalizeStreamError(error, fallback = "") {
  if (error instanceof Error) {
    return `${error.message || ""}`.trim() ? error : new Error(fallback);
  }

  return new Error(`${error || ""}`.trim() || fallback);
}

function createStreamChunkResult(mode = "ignore", content = "", options = {}) {
  return {
    mode,
    content,
    structured: false,
    ...options,
  };
}

function parsePlainStreamText(
  rawText = "",
  currentContent = "",
  hasStructuredTextChunk = false
) {
  const nextText = `${rawText ?? ""}`;

  if (!nextText.trim()) {
    return createStreamChunkResult();
  }

  if (!hasStructuredTextChunk) {
    return createStreamChunkResult("append", nextText);
  }

  if (!currentContent) {
    return createStreamChunkResult("append", nextText);
  }

  if (nextText === currentContent) {
    return createStreamChunkResult();
  }

  if (
    nextText.startsWith(currentContent) ||
    nextText.length >= currentContent.length
  ) {
    return createStreamChunkResult("replace", nextText);
  }

  return createStreamChunkResult();
}

function parseStreamChunk(
  rawData = "",
  currentContent = "",
  hasStructuredTextChunk = false
) {
  const normalizedRaw = `${rawData ?? ""}`.trim();

  if (!normalizedRaw || normalizedRaw === "[DONE]") {
    return createStreamChunkResult();
  }

  let outer;

  try {
    outer = JSON.parse(normalizedRaw.replace(/^data:\s*/, ""));
  } catch {
    throw new Error("Chatflow 调试响应解析失败");
  }

  if (Number(outer?.code ?? 200) !== 200) {
    throw new Error(
      `${outer?.msg || outer?.message || "Chatflow 调试请求失败"}`.trim()
    );
  }

  const rawInner = outer?.data;

  if (typeof rawInner !== "string") {
    return typeof rawInner?.text === "string"
      ? createStreamChunkResult("append", rawInner.text, {
          structured: true,
        })
      : createStreamChunkResult();
  }

  const normalizedInner = rawInner.trim();

  if (!normalizedInner || normalizedInner === "{}") {
    return createStreamChunkResult();
  }

  try {
    const inner = JSON.parse(normalizedInner);

    if (typeof inner?.text === "string") {
      return createStreamChunkResult("append", inner.text, {
        structured: true,
      });
    }

    if (typeof inner === "string") {
      return parsePlainStreamText(
        inner,
        currentContent,
        hasStructuredTextChunk
      );
    }

    return createStreamChunkResult();
  } catch {
    return parsePlainStreamText(
      rawInner,
      currentContent,
      hasStructuredTextChunk
    );
  }
}

function finishStream(controller = conversationInAbortController.value) {
  if (
    controller &&
    conversationInAbortController.value &&
    conversationInAbortController.value !== controller
  ) {
    return;
  }

  if (!controller || conversationInAbortController.value === controller) {
    conversationInAbortController.value = null;
    running.value = false;
  }
}

function stopStream(
  markAsManual = true,
  controller = conversationInAbortController.value
) {
  streamStoppedManually.value = Boolean(markAsManual);

  if (controller) {
    controller.abort();
  }

  finishStream(controller);
}

onBeforeUnmount(() => {
  stopStream(false);
});

function handlePromptKeydown(event) {
  if (event.shiftKey) {
    return;
  }

  event.preventDefault();
  handleSend();
}

async function handleSend() {
  if (!canSend.value) {
    return;
  }

  const messageText = `${prompt.value || ""}`.trim();

  const nextUserMessage = createMessage("user", messageText);
  const payload = {
    input: buildChatInputValues(messageText),
    flow: props.workflowData,
    prompt: messageText,
    history: buildConversationHistory(nextUserMessage),
  };

  if (typeof props.beforeRun === "function") {
    const canRun = await Promise.resolve(props.beforeRun(payload));

    if (canRun === false) {
      return;
    }
  }

  const assistantMessage = createMessage("assistant", "", {
    status: "streaming",
  });

  messages.value = [...messages.value, nextUserMessage, assistantMessage];
  prompt.value = "";
  emit("run", payload);

  if (props.showSections) {
    emit("update:showSections", false);
  }

  const controller = new AbortController();
  conversationInAbortController.value = controller;
  running.value = true;
  streamStoppedManually.value = false;

  let assistantContent = "";
  let hasStructuredTextChunk = false;
  let requestError = null;

  try {
    await ProcessChstFlow.executeChatflowDebugStream(
      payload.flow,
      payload.input,
      payload.history,
      controller,
      (res) => {
        const nextChunk = parseStreamChunk(
          res?.data,
          assistantContent,
          hasStructuredTextChunk
        );

        if (nextChunk.mode === "ignore") {
          return;
        }

        if (nextChunk.structured) {
          hasStructuredTextChunk = true;
        }

        assistantContent =
          nextChunk.mode === "replace"
            ? nextChunk.content
            : assistantContent + nextChunk.content;

        updateMessage(assistantMessage.id, {
          content: assistantContent,
        });
      },
      (error) => {
        requestError = normalizeStreamError(
          error,
          "Chatflow 调试请求失败，请检查接口接入实现。"
        );
        throw requestError;
      },
      () => {
        finishStream(controller);
      }
    );

    if (streamStoppedManually.value) {
      updateMessage(assistantMessage.id, {
        content: assistantContent || "已停止本轮调试。",
        status: "done",
      });
      return;
    }

    if (requestError) {
      throw requestError;
    }

    updateMessage(assistantMessage.id, {
      content: assistantContent || "调试完成，但接口暂未返回内容。",
      status: "done",
    });
  } catch (error) {
    const errorText = normalizeDebugError(error);

    updateMessage(assistantMessage.id, {
      content: errorText,
      status: "done",
    });
  } finally {
    finishStream(controller);
    conversationRef.value?.scrollToBottom?.();
  }
}
</script>

<style scoped lang="scss">
.chatflow-debug-run-panel {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
  gap: 20px;

  :deep(.el-input__wrapper) {
    border-radius: 0;
  }

  :deep(.el-textarea__inner) {
    border-radius: 0;
  }

  :deep(.el-button) {
    border-radius: 0;
  }
}

.chatflow-debug-run-panel__input-block {
  display: flex;
  flex-direction: column;
}

.chatflow-debug-run-panel__title {
  margin-bottom: 12px;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 400;
  color: #111827;

  .blue-bar {
    background-color: #2666fb;
    width: 6px;
    height: 16px;
    // margin-right: 10px;
    border-radius: 10px;
  }
}

.chatflow-debug-run-panel__empty-hint {
  padding: 12px 0;
  font-size: 13px;
  line-height: 1.7;
  color: #6b7280;
}

.chatflow-debug-run-panel__form {
  :deep(.el-form-item:last-child) {
    margin-bottom: 0;
  }

  :deep(.el-form-item__label) {
    min-width: 0;
    overflow: hidden;
    color: #111827;
    font-size: 14px;
    font-weight: 600;
  }
}

.chatflow-debug-run-panel__conversation-shell {
  flex: 1 1 0;
  min-height: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
  overflow: hidden;
}

.chatflow-debug-run-panel__conversation-header {
  display: flex;
  align-items: center;
}

.chatflow-debug-run-panel__conversation-title {
  font-size: 16px;
  font-weight: 400;
  color: #111827;
  display: flex;
  align-items: center;

  .blue-bar {
    display: inline-block;
    background-color: #2666fb;
    width: 6px;
    height: 16px;
    margin-right: 10px;
    border-radius: 10px;
  }
}

.chatflow-debug-run-panel__composer {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  background-color: #fff;
  border-radius: 4px;
}

.chatflow-debug-run-panel__composer-input {
  :deep(.el-textarea) {
    display: block;
  }

  :deep(.el-textarea__inner) {
    min-height: 48px !important;
    padding: 0;
    border: none;
    background-color: transparent;
    box-shadow: none;
  }

  :deep(.el-textarea__inner:focus) {
    box-shadow: none;
  }
}

.chatflow-debug-run-panel__composer-actions {
  margin-top: 8px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
}

.chatflow-debug-run-panel__composer-buttons {
  display: inline-flex;
  align-items: center;
  gap: 10px;

  :deep(.el-button) {
    min-width: 66px;
    height: 32px;
    padding: 0 18px;
    border-radius: 4px;
  }
}

:deep(.chatflow-debug-run-panel__send-button.is-disabled),
:deep(.chatflow-debug-run-panel__send-button.is-disabled:hover),
:deep(.chatflow-debug-run-panel__send-button.is-disabled:focus),
:deep(.chatflow-debug-run-panel__send-button.is-disabled:active) {
  color: #9ca3af;
  background-color: #e5e7eb;
  border-color: #e5e7eb;
  box-shadow: none;
}

@media (max-width: 420px) {
  .chatflow-debug-run-panel__composer-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .chatflow-debug-run-panel__composer-buttons {
    justify-content: flex-end;
  }
}
</style>
