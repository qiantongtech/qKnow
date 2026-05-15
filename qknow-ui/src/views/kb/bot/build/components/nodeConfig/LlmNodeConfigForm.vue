<template>
  <div class="llm-config-panel">
    <div class="llm-config-section">
      <div class="llm-config-label node-config-section-title">
        <span class="blue-bar"></span>模型
      </div>
      <div class="llm-model-config-anchor">
        <el-select
          :model-value="getSelectedModelBindingValue()"
          class="llm-model-simple-select"
          popper-class="dydome-square-popper"
          placeholder="请选择模型"
          @update:model-value="handleModelChange"
        >
          <el-option-group
            v-for="group in modelOptions"
            :key="group.label"
            :label="group.label"
          >
            <el-option
              v-for="option in group.options || []"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-option-group>
        </el-select>
      </div>
    </div>
    <div class="llm-config-section">
      <div class="llm-prompt-section-header">
        <div class="llm-prompt-title node-config-section-title">
          <span class="blue-bar"></span> 提示词
        </div>
        <el-button
          type="primary"
          plain
          class="llm-add-message-trigger"
          @click="emit('addMessage')"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </div>
      <div class="llm-message-card">
        <div class="llm-message-editor-shell">
          <span class="llm-prompt-editor-label">SYSTEM</span>
          <div class="llm-message-toolbar llm-prompt-toolbar">
            <el-popover
              :visible="promptVariablePopoverVisible"
              @update:visible="handlePromptVariablePopoverVisibleChange"
              placement="bottom-end"
              trigger="click"
              :teleported="true"
              popper-class="llm-context-picker-popper dydome-square-popper"
            >
              <template #reference>
                <button
                  type="button"
                  class="llm-message-toolbar-chip llm-message-toolbar-chip-button llm-variable-popover-trigger"
                  @mousedown.prevent
                  @click.stop
                >
                  插入
                </button>
              </template>

              <div class="llm-context-picker">
                <template v-if="getContextVariableGroups().length">
                  <div
                    v-for="group in getContextVariableGroups()"
                    :key="`prompt-${group.nodeId}`"
                    class="llm-context-group"
                  >
                    <div class="llm-context-group-title">
                      {{ group.nodeLabel || "上游节点" }}
                    </div>
                    <button
                      v-for="option in group.variables"
                      :key="`prompt-${option.sourceNodeId}-${option.variableKey}`"
                      type="button"
                      class="llm-context-option"
                      @mousedown.prevent
                      @click="insertVariableIntoPrompt(option)"
                    >
                      <el-tag
                        disable-transitions
                        effect="light"
                        size="small"
                        class="llm-context-option-tag"
                        :title="getContextVariableOptionLabel(option)"
                      >
                        {{ getContextVariableOptionLabel(option) }}
                      </el-tag>
                    </button>
                  </div>
                </template>
                <div v-else class="llm-context-empty">
                  {{ getContextVariableEmptyText() }}
                </div>
              </div>
            </el-popover>
          </div>
          <div
            ref="promptInputRef"
            contenteditable="true"
            spellcheck="false"
            class="llm-message-editor is-prompt"
            :class="{
              'is-empty':
                !node.data.prompt && !isEditorComposing({ type: 'prompt' }),
            }"
            data-placeholder="请输入提示词"
            @focus="handleEditorFocus({ type: 'prompt' })"
            @blur="handleEditorBlur({ type: 'prompt' })"
            @compositionstart="handleEditorCompositionStart({ type: 'prompt' })"
            @compositionend="handleEditorCompositionEnd({ type: 'prompt' })"
            @click="handleEditorSelectionChange({ type: 'prompt' })"
            @keyup="handleEditorSelectionChange({ type: 'prompt' })"
            @mouseup="handleEditorSelectionChange({ type: 'prompt' })"
            @beforeinput="handleEditorBeforeInput({ type: 'prompt' }, $event)"
            @input="handleEditorInput({ type: 'prompt' }, $event)"
            @keydown="handleEditorKeydown({ type: 'prompt' }, $event)"
            @paste="handleEditorPaste({ type: 'prompt' }, $event)"
          ></div>
        </div>
      </div>

      <div
        v-for="message in node.data.messages || []"
        :key="message.id"
        class="llm-message-card is-secondary"
      >
        <div class="llm-message-card-header">
          <div class="llm-message-role-row">
            <el-select
              :model-value="message.role || 'user'"
              :class="[
                'llm-message-role-select',
                message.role === 'assistant' ? 'is-assistant' : 'is-user',
              ]"
              popper-class="dydome-square-popper"
              placeholder="USER"
              @update:model-value="updateMessage(message.id, { role: $event })"
            >
              <el-option
                v-for="option in messageRoleOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </div>
          <div class="llm-message-header-actions">
            <div class="llm-message-toolbar">
              <el-popover
                :visible="isMessageVariablePopoverVisible(message.id)"
                @update:visible="
                  handleMessageVariablePopoverVisibleChange(message.id, $event)
                "
                placement="bottom-end"
                trigger="click"
                :teleported="true"
                :width="260"
                popper-class="llm-context-picker-popper dydome-square-popper"
              >
                <template #reference>
                  <button
                    type="button"
                    class="llm-message-toolbar-chip llm-message-toolbar-chip-button llm-variable-popover-trigger"
                    @mousedown.prevent
                    @click.stop
                  >
                    插入
                  </button>
                </template>

                <div class="llm-context-picker">
                  <template v-if="getContextVariableGroups().length">
                    <div
                      v-for="group in getContextVariableGroups()"
                      :key="`${message.id}-${group.nodeId}`"
                      class="llm-context-group"
                    >
                      <div class="llm-context-group-title">
                        {{ group.nodeLabel || "上游节点" }}
                      </div>
                      <button
                        v-for="option in group.variables"
                        :key="`${message.id}-${option.sourceNodeId}-${option.variableKey}`"
                        type="button"
                        class="llm-context-option"
                        @mousedown.prevent
                        @click="insertVariableIntoMessage(message.id, option)"
                      >
                        <el-tag
                          disable-transitions
                          effect="light"
                          size="small"
                          class="llm-context-option-tag"
                          :title="getContextVariableOptionLabel(option)"
                        >
                          {{ getContextVariableOptionLabel(option) }}
                        </el-tag>
                      </button>
                    </div>
                  </template>
                  <div v-else class="llm-context-empty">
                    {{ getContextVariableEmptyText() }}
                  </div>
                </div>
              </el-popover>
            </div>
            <button
              type="button"
              class="llm-message-remove"
              @click="emit('removeMessage', message.id)"
            >
              {{ removeMessageLabel }}
            </button>
          </div>
        </div>
        <div class="llm-message-editor-shell">
          <div
            :ref="(el) => setMessageInputRef(message.id, el)"
            contenteditable="true"
            spellcheck="false"
            class="llm-message-editor is-message"
            :class="{
              'is-empty':
                !message.content &&
                !isEditorComposing({ type: 'message', id: message.id }),
            }"
            :data-placeholder="getMessagePlaceholder(message.role)"
            @focus="handleEditorFocus({ type: 'message', id: message.id })"
            @blur="handleEditorBlur({ type: 'message', id: message.id })"
            @compositionstart="
              handleEditorCompositionStart({ type: 'message', id: message.id })
            "
            @compositionend="
              handleEditorCompositionEnd({ type: 'message', id: message.id })
            "
            @click="
              handleEditorSelectionChange({ type: 'message', id: message.id })
            "
            @keyup="
              handleEditorSelectionChange({ type: 'message', id: message.id })
            "
            @mouseup="
              handleEditorSelectionChange({ type: 'message', id: message.id })
            "
            @beforeinput="
              handleEditorBeforeInput(
                { type: 'message', id: message.id },
                $event
              )
            "
            @input="
              handleEditorInput({ type: 'message', id: message.id }, $event)
            "
            @keydown="
              handleEditorKeydown({ type: 'message', id: message.id }, $event)
            "
            @paste="
              handleEditorPaste({ type: 'message', id: message.id }, $event)
            "
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { nextTick, ref, watch } from "vue";

const props = defineProps({
  node: {
    type: Object,
    required: true,
  },
  modelOptions: {
    type: Array,
    default: () => [],
  },
  responseFormatOptions: {
    type: Array,
    default: () => [],
  },
  availableContextVariableGroups: {
    type: Array,
    default: () => [],
  },
  hasUpstreamContextSources: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits([
  "updateField",
  "updateMessage",
  "addMessage",
  "removeMessage",
]);

const messageRoleOptions = [
  { label: "USER", value: "user" },
  { label: "ASSISTANT", value: "assistant" },
];

function setField(field, value) {
  emit("updateField", { field, value });
}

function updateMessage(id, patch) {
  emit("updateMessage", { id, patch });
}

function normalizeProviderValue(value, fallback = "") {
  const parseProviderValue = (targetValue) => {
    if (targetValue === undefined || targetValue === null) {
      return null;
    }

    if (typeof targetValue === "string" && targetValue.trim() === "") {
      return null;
    }

    const nextValue = Number(targetValue);
    return Number.isFinite(nextValue) ? nextValue : null;
  };

  return parseProviderValue(value) ?? parseProviderValue(fallback) ?? "";
}

function getFlatModelOptions() {
  return (props.modelOptions || []).flatMap((group) =>
    Array.isArray(group?.options) ? group.options : []
  );
}

function findModelOptionByBindingValue(bindingValue = "") {
  return (
    getFlatModelOptions().find(
      (option) =>
        `${option?.value || ""}`.trim() === `${bindingValue || ""}`.trim()
    ) || null
  );
}

function findModelOptionByModelName(modelName = "") {
  return (
    getFlatModelOptions().find(
      (option) =>
        `${option?.model || option?.label || ""}`.trim() ===
        `${modelName || ""}`.trim()
    ) || null
  );
}

function getSelectedModelBindingValue() {
  const modelName = `${props.node?.data?.model || ""}`.trim();
  const provider = `${props.node?.data?.provider ?? ""}`.trim();

  if (!modelName) {
    return "";
  }

  if (provider) {
    return `${provider}::${modelName}`;
  }

  return findModelOptionByModelName(modelName)?.value || modelName;
}

function handleModelChange(bindingValue = "") {
  const option = findModelOptionByBindingValue(bindingValue);

  if (!option) {
    setField("provider", "");
    setField("model", "");
    return;
  }

  setField("provider", normalizeProviderValue(option.provider));
  setField("model", `${option.model || option.label || ""}`.trim());
}

const removeMessageLabel = "删除";
const userMessagePlaceholder = "请输入提示词";
const assistantMessagePlaceholder = "请输入提示词";

function getMessagePlaceholder(role) {
  return role === "assistant"
    ? assistantMessagePlaceholder
    : userMessagePlaceholder;
}

const promptInputRef = ref(null);
const promptVariablePopoverVisible = ref(false);
const messageInputRefs = ref({});
const messageVariablePopoverVisibleMap = ref({});
const editorSelections = ref({});
const editorCompositionState = ref({});
const slashVariableMenuState = ref({
  targetKey: "",
});

function closePromptVariablePopover() {
  promptVariablePopoverVisible.value = false;
}

function closeAllMessageVariablePopovers() {
  messageVariablePopoverVisibleMap.value = {};
}

function closeSlashVariableMenu(target = null) {
  if (!target) {
    slashVariableMenuState.value = {
      targetKey: "",
    };
    return;
  }

  if (slashVariableMenuState.value.targetKey === getEditorTargetKey(target)) {
    slashVariableMenuState.value = {
      targetKey: "",
    };
  }
}

function isMessageVariablePopoverVisible(messageId = "") {
  return Boolean(messageVariablePopoverVisibleMap.value[messageId]);
}

function handlePromptVariablePopoverVisibleChange(visible = false) {
  if (visible) {
    closeSlashVariableMenu();
    closeAllMessageVariablePopovers();
  }

  promptVariablePopoverVisible.value = Boolean(visible);
}

function handleMessageVariablePopoverVisibleChange(
  messageId = "",
  visible = false
) {
  if (!messageId) {
    return;
  }

  if (!visible) {
    if (isMessageVariablePopoverVisible(messageId)) {
      messageVariablePopoverVisibleMap.value = {};
    }
    return;
  }

  closeSlashVariableMenu();
  closePromptVariablePopover();
  messageVariablePopoverVisibleMap.value = { [messageId]: true };
}

function getEditorTargetKey(target = {}) {
  return target.type === "message" ? `message:${target.id || ""}` : "prompt";
}

function setMessageInputRef(id, element) {
  if (!id) {
    return;
  }

  if (element) {
    messageInputRefs.value[id] = element;
    nextTick(() => {
      renderEditorContent({ type: "message", id }, { force: true });
    });
    return;
  }

  delete messageInputRefs.value[id];
}

function getEditorElement(target = {}) {
  return target.type === "message"
    ? messageInputRefs.value[target.id] || null
    : promptInputRef.value;
}

function getEditorContent(target = {}) {
  if (target.type === "message") {
    return (
      (props.node?.data?.messages || []).find(
        (message) => message.id === target.id
      )?.content || ""
    );
  }

  return props.node?.data?.prompt || "";
}

function applyEditorValue(target = {}, value = "") {
  if (target.type === "message") {
    updateMessage(target.id, { content: value });
    return;
  }

  setField("prompt", value);
}

function hideEditorVariablePopover(target = {}) {
  closeSlashVariableMenu(target);

  if (target.type === "message") {
    closeAllMessageVariablePopovers();
  } else {
    closePromptVariablePopover();
  }
}

function isEditorComposing(target = {}) {
  return Boolean(editorCompositionState.value[getEditorTargetKey(target)]);
}

function setEditorComposing(target = {}, composing = false) {
  const targetKey = getEditorTargetKey(target);

  if (!targetKey) {
    return;
  }

  const nextState = {
    ...editorCompositionState.value,
  };

  if (composing) {
    nextState[targetKey] = true;
  } else {
    delete nextState[targetKey];
  }

  editorCompositionState.value = nextState;
}

function getTemplateNodeLength(node) {
  if (!node) {
    return 0;
  }

  if (node.nodeType === 3) {
    return node.textContent?.length || 0;
  }

  if (node.nodeType !== 1) {
    return 0;
  }

  if (node.dataset?.templateExpression) {
    return node.dataset.templateExpression.length;
  }

  if (node.tagName === "BR") {
    return 1;
  }

  return Array.from(node.childNodes || []).reduce(
    (total, childNode) => total + getTemplateNodeLength(childNode),
    0
  );
}

function serializeTemplateNode(node) {
  if (!node) {
    return "";
  }

  if (node.nodeType === 3) {
    return node.textContent || "";
  }

  if (node.nodeType !== 1) {
    return "";
  }

  if (node.dataset?.templateExpression) {
    return node.dataset.templateExpression || "";
  }

  if (node.tagName === "BR") {
    return "\n";
  }

  return Array.from(node.childNodes || [])
    .map((childNode) => serializeTemplateNode(childNode))
    .join("");
}

function serializeEditorContent(target = {}) {
  const editorElement = getEditorElement(target);

  if (!editorElement) {
    return getEditorContent(target);
  }

  return Array.from(editorElement.childNodes || [])
    .map((childNode) => serializeTemplateNode(childNode))
    .join("")
    .replace(/\u00a0/g, " ");
}

function hasTemplateExpression(content = "") {
  return /\{\{\s*[A-Za-z0-9_]+\.[A-Za-z0-9_.]+\s*\}\}/.test(`${content || ""}`);
}

function createEditorVariableChip(segment = {}) {
  const wrapper = document.createElement("span");
  wrapper.className = "llm-context-chip llm-message-editor-chip";
  wrapper.contentEditable = "false";
  wrapper.dataset.templateExpression = segment.value || "";

  const tag = document.createElement("span");
  tag.className = "el-tag el-tag--light el-tag--small llm-context-chip-tag";

  if (segment.display) {
    tag.title = segment.display;
  }

  const content = document.createElement("span");
  content.className = "el-tag__content";
  content.textContent = segment.display || "";
  tag.appendChild(content);
  wrapper.appendChild(tag);

  return wrapper;
}

function markEditorRenderedContent(target = {}, content = "") {
  const editorElement = getEditorElement(target);

  if (!editorElement) {
    return;
  }

  editorElement.dataset.renderedContent = `${content || ""}`;
}

function shouldRenderEditorContent(target = {}) {
  const editorElement = getEditorElement(target);

  if (!editorElement) {
    return false;
  }

  const content = `${getEditorContent(target) || ""}`;
  const renderedContent = `${editorElement.dataset.renderedContent || ""}`;

  if (renderedContent !== content) {
    return true;
  }

  if (!content) {
    return editorElement.childNodes.length > 0;
  }

  return (
    hasTemplateExpression(content) &&
    !editorElement.querySelector("[data-template-expression]")
  );
}

function renderEditorContent(
  target = {},
  { preserveSelection = true, force = false } = {}
) {
  const editorElement = getEditorElement(target);

  if (!editorElement || (!force && !shouldRenderEditorContent(target))) {
    return;
  }

  const content = `${getEditorContent(target) || ""}`;
  const isFocused = document.activeElement === editorElement;

  if (preserveSelection && isFocused) {
    saveEditorSelection(target, { preserveExisting: true });
  }

  const fragment = document.createDocumentFragment();

  getTemplatePreviewSegments(content).forEach((segment) => {
    if (segment.type === "variable") {
      fragment.appendChild(createEditorVariableChip(segment));
      return;
    }

    if (segment.value) {
      fragment.appendChild(document.createTextNode(segment.value));
    }
  });

  editorElement.replaceChildren(fragment);
  markEditorRenderedContent(target, content);

  if (preserveSelection && isFocused) {
    restoreEditorSelection(target);
  }
}

function getSelectionOffset(root, container, offset) {
  if (!root) {
    return 0;
  }

  if (container === root) {
    let total = 0;
    const childNodes = Array.from(root.childNodes || []);
    const boundary = Math.min(offset, childNodes.length);

    for (let index = 0; index < boundary; index += 1) {
      total += getTemplateNodeLength(childNodes[index]);
    }

    return total;
  }

  function walk(currentNode, accumulated) {
    if (currentNode === container) {
      if (currentNode.nodeType === 3) {
        return {
          found: true,
          offset:
            accumulated +
            Math.min(offset, currentNode.textContent?.length || 0),
        };
      }

      if (currentNode.nodeType === 1) {
        let total = accumulated;
        const childNodes = Array.from(currentNode.childNodes || []);
        const boundary = Math.min(offset, childNodes.length);

        for (let index = 0; index < boundary; index += 1) {
          total += getTemplateNodeLength(childNodes[index]);
        }

        return {
          found: true,
          offset: total,
        };
      }
    }

    if (currentNode.nodeType === 1 && currentNode.dataset?.templateExpression) {
      return {
        found: false,
        offset: accumulated + getTemplateNodeLength(currentNode),
      };
    }

    const childNodes = Array.from(currentNode.childNodes || []);

    if (!childNodes.length) {
      return {
        found: false,
        offset: accumulated + getTemplateNodeLength(currentNode),
      };
    }

    let total = accumulated;

    for (const childNode of childNodes) {
      const result = walk(childNode, total);

      if (result.found) {
        return result;
      }

      total = result.offset;
    }

    return {
      found: false,
      offset: total,
    };
  }

  return walk(root, 0).offset;
}

function saveEditorSelection(target = {}, options = {}) {
  const { preserveExisting = false } = options;
  const targetKey = getEditorTargetKey(target);
  const editorElement = getEditorElement(target);
  const contentLength = getEditorContent(target).length;

  if (!editorElement) {
    if (preserveExisting && editorSelections.value[targetKey]) {
      return;
    }

    editorSelections.value[targetKey] = {
      start: contentLength,
      end: contentLength,
    };
    return;
  }

  const selection = window.getSelection();

  if (!selection || !selection.rangeCount) {
    if (preserveExisting && editorSelections.value[targetKey]) {
      return;
    }

    editorSelections.value[targetKey] = {
      start: contentLength,
      end: contentLength,
    };
    return;
  }

  const range = selection.getRangeAt(0);

  if (
    !editorElement.contains(range.startContainer) ||
    !editorElement.contains(range.endContainer)
  ) {
    if (preserveExisting && editorSelections.value[targetKey]) {
      return;
    }

    editorSelections.value[targetKey] = {
      start: contentLength,
      end: contentLength,
    };
    return;
  }

  editorSelections.value[targetKey] = {
    start: getSelectionOffset(
      editorElement,
      range.startContainer,
      range.startOffset
    ),
    end: getSelectionOffset(editorElement, range.endContainer, range.endOffset),
  };
}

function handleEditorBlur(target = {}) {
  saveEditorSelection(target, { preserveExisting: true });

  if (isSlashVariableMenuVisible(target)) {
    hideEditorVariablePopover(target);
  }
}

function handleEditorFocus(target = {}) {
  saveEditorSelection(target);
  syncSlashVariableMenu(target);
}

function handleEditorSelectionChange(target = {}) {
  saveEditorSelection(target);
  syncSlashVariableMenu(target);
}

function locateEditorPosition(root, rawOffset) {
  const totalLength = getTemplateNodeLength(root);
  let remaining = Math.max(0, Math.min(rawOffset, totalLength));

  function walk(currentNode) {
    if (currentNode.nodeType === 3) {
      const textLength = currentNode.textContent?.length || 0;

      return {
        container: currentNode,
        offset: Math.min(remaining, textLength),
      };
    }

    if (currentNode.nodeType === 1 && currentNode.dataset?.templateExpression) {
      const parentNode = currentNode.parentNode || root;
      const childIndex = Array.from(parentNode.childNodes || []).indexOf(
        currentNode
      );

      return {
        container: parentNode,
        offset: remaining <= 0 ? childIndex : childIndex + 1,
      };
    }

    const childNodes = Array.from(currentNode.childNodes || []);

    if (!childNodes.length) {
      return {
        container: currentNode,
        offset: 0,
      };
    }

    for (let index = 0; index < childNodes.length; index += 1) {
      const childNode = childNodes[index];
      const childLength = getTemplateNodeLength(childNode);

      if (remaining <= childLength) {
        return walk(childNode);
      }

      remaining -= childLength;
    }

    return {
      container: currentNode,
      offset: childNodes.length,
    };
  }

  return walk(root);
}

function restoreEditorSelection(target = {}) {
  const targetKey = getEditorTargetKey(target);
  const editorElement = getEditorElement(target);
  const selectionOffsets = editorSelections.value[targetKey];

  if (!editorElement || !selectionOffsets) {
    return;
  }

  const selection = window.getSelection();

  if (!selection) {
    return;
  }

  const startPosition = locateEditorPosition(
    editorElement,
    selectionOffsets.start
  );
  const endPosition = locateEditorPosition(editorElement, selectionOffsets.end);
  const range = document.createRange();

  range.setStart(startPosition.container, startPosition.offset);
  range.setEnd(endPosition.container, endPosition.offset);
  selection.removeAllRanges();
  selection.addRange(range);
}

function focusEditor(target = {}) {
  const editorElement = getEditorElement(target);

  if (!editorElement) {
    return;
  }

  editorElement.focus();
  restoreEditorSelection(target);
}

function buildTemplateReferenceSegment(value = "", fallback = "node") {
  const normalized = `${value || ""}`
    .trim()
    .replace(/[^A-Za-z0-9_]+/g, "_")
    .replace(/^_+|_+$/g, "");
  const nextValue = normalized || fallback;

  return /^[0-9]/.test(nextValue) ? `node_${nextValue}` : nextValue;
}

function buildEditorVariableExpression(option = {}) {
  const sourceSegment = buildTemplateReferenceSegment(
    option.sourceNodeId || option.sourceNodeType || "node",
    "node"
  );
  const variablePath = `${option.path || option.variableKey || ""}`.trim();

  if (!sourceSegment || !variablePath) {
    return "";
  }

  return `{{ ${sourceSegment}.${variablePath} }}`;
}

function insertTextAtSelection(
  content = "",
  insertedText = "",
  selection = {}
) {
  const currentContent = `${content || ""}`;
  const start = Number.isInteger(selection.start)
    ? Math.max(0, Math.min(selection.start, currentContent.length))
    : currentContent.length;
  const end = Number.isInteger(selection.end)
    ? Math.max(start, Math.min(selection.end, currentContent.length))
    : start;
  const nextValue =
    currentContent.slice(0, start) + insertedText + currentContent.slice(end);

  return {
    value: nextValue,
    caret: start + insertedText.length,
  };
}

function getTemplateExpressionRanges(content = "") {
  const currentContent = `${content || ""}`;
  const variablePattern = /\{\{\s*[A-Za-z0-9_]+\.[A-Za-z0-9_.]+\s*\}\}/g;
  const ranges = [];
  let match = variablePattern.exec(currentContent);

  while (match) {
    ranges.push({
      start: match.index,
      end: match.index + match[0].length,
    });
    match = variablePattern.exec(currentContent);
  }

  return ranges;
}

function removeTextAtSelection(
  content = "",
  selection = {},
  direction = "backward"
) {
  const currentContent = `${content || ""}`;
  let start = Number.isInteger(selection.start)
    ? Math.max(0, Math.min(selection.start, currentContent.length))
    : currentContent.length;
  let end = Number.isInteger(selection.end)
    ? Math.max(start, Math.min(selection.end, currentContent.length))
    : start;
  const ranges = getTemplateExpressionRanges(currentContent);

  if (start !== end) {
    ranges.forEach((range) => {
      if (start < range.end && end > range.start) {
        start = Math.min(start, range.start);
        end = Math.max(end, range.end);
      }
    });
  } else if (direction === "backward") {
    const adjacentRange = ranges.find(
      (range) => start > range.start && start <= range.end
    );

    if (adjacentRange) {
      start = adjacentRange.start;
      end = adjacentRange.end;
    } else {
      start = Math.max(0, start - 1);
    }
  } else {
    const adjacentRange = ranges.find(
      (range) => start >= range.start && start < range.end
    );

    if (adjacentRange) {
      start = adjacentRange.start;
      end = adjacentRange.end;
    } else {
      end = Math.min(currentContent.length, end + 1);
    }
  }

  return {
    value: currentContent.slice(0, start) + currentContent.slice(end),
    caret: start,
  };
}

function insertTextIntoEditor(target = {}, insertedText = "") {
  if (!insertedText) {
    return;
  }

  const targetKey = getEditorTargetKey(target);
  const selection = editorSelections.value[targetKey] || {};
  const { value, caret } = insertTextAtSelection(
    getEditorContent(target),
    insertedText,
    selection
  );

  editorSelections.value[targetKey] = {
    start: caret,
    end: caret,
  };
  applyEditorValue(target, value);

  nextTick(() => {
    focusEditor(target);
    syncSlashVariableMenu(target);
  });
}

function deleteTextFromEditor(target = {}, direction = "backward") {
  const targetKey = getEditorTargetKey(target);
  const selection = editorSelections.value[targetKey] || {};
  const { value, caret } = removeTextAtSelection(
    getEditorContent(target),
    selection,
    direction
  );

  editorSelections.value[targetKey] = {
    start: caret,
    end: caret,
  };
  applyEditorValue(target, value);

  nextTick(() => {
    focusEditor(target);
    syncSlashVariableMenu(target);
  });
}

function handleEditorBeforeInput(target = {}, event) {
  if (!event) {
    return;
  }

  const inputType = event.inputType || "";

  if (
    event.isComposing ||
    inputType === "insertCompositionText" ||
    inputType === "deleteCompositionText" ||
    inputType === "insertFromComposition"
  ) {
    return;
  }

  if (inputType === "insertParagraph" || inputType === "insertLineBreak") {
    event.preventDefault();
    insertTextIntoEditor(target, "\n");
    return;
  }

  if (inputType === "deleteContentBackward") {
    event.preventDefault();
    deleteTextFromEditor(target, "backward");
    return;
  }

  if (inputType === "deleteContentForward") {
    event.preventDefault();
    deleteTextFromEditor(target, "forward");
    return;
  }
}

function syncEditorContentFromDom(target = {}) {
  saveEditorSelection(target);
  const nextValue = serializeEditorContent(target);

  applyEditorValue(target, nextValue);

  const editorElement = getEditorElement(target);

  if (
    editorElement &&
    (!hasTemplateExpression(nextValue) ||
      editorElement.querySelector("[data-template-expression]"))
  ) {
    markEditorRenderedContent(target, nextValue);
  }

  nextTick(() => {
    focusEditor(target);
    syncSlashVariableMenu(target);
  });
}

function handleEditorInput(target = {}, event) {
  if (event?.isComposing || isEditorComposing(target)) {
    return;
  }

  syncEditorContentFromDom(target);
}

function handleEditorCompositionStart(target = {}) {
  setEditorComposing(target, true);
  hideEditorVariablePopover(target);
}

function handleEditorCompositionEnd(target = {}) {
  setEditorComposing(target, false);
  syncEditorContentFromDom(target);
}

function handleEditorKeydown(target = {}, event) {
  if (event.key === "Escape" && isSlashVariableMenuVisible(target)) {
    event.preventDefault();
    hideEditorVariablePopover(target);
    return;
  }

  if (event.key !== "Enter") {
    return;
  }

  event.preventDefault();
  insertTextIntoEditor(target, "\n");
}

function handleEditorPaste(target = {}, event) {
  const pastedText = event.clipboardData?.getData("text/plain");

  if (!pastedText) {
    return;
  }

  event.preventDefault();
  insertTextIntoEditor(target, pastedText.replace(/\r\n?/g, "\n"));
}

function insertVariableIntoEditor(
  target = {},
  option = {},
  { replaceSlashTrigger = false } = {}
) {
  const expression = buildEditorVariableExpression(option);

  if (!expression) {
    return;
  }

  const targetKey = getEditorTargetKey(target);
  const currentSelection = editorSelections.value[targetKey] || {};
  let nextSelection = currentSelection;

  if (
    replaceSlashTrigger &&
    Number.isInteger(currentSelection.start) &&
    currentSelection.start === currentSelection.end
  ) {
    const caret = currentSelection.start;
    const content = getEditorContent(target);

    if (caret > 0 && content.slice(caret - 1, caret) === "/") {
      nextSelection = {
        start: caret - 1,
        end: caret,
      };
    }
  }

  const { value, caret } = insertTextAtSelection(
    getEditorContent(target),
    expression,
    nextSelection
  );

  editorSelections.value[targetKey] = {
    start: caret,
    end: caret,
  };
  applyEditorValue(target, value);

  nextTick(() => {
    hideEditorVariablePopover(target);
    focusEditor(target);
  });
}

function insertVariableIntoPrompt(option = {}) {
  insertVariableIntoEditor({ type: "prompt" }, option, {
    replaceSlashTrigger: isSlashVariableMenuVisible({ type: "prompt" }),
  });
}

function insertVariableIntoMessage(messageId, option = {}) {
  insertVariableIntoEditor({ type: "message", id: messageId }, option, {
    replaceSlashTrigger: isSlashVariableMenuVisible({
      type: "message",
      id: messageId,
    }),
  });
}

function buildTemplateVariableReferenceKey(
  sourceSegment = "",
  variablePath = ""
) {
  return `${sourceSegment || ""}::${variablePath || ""}`;
}

function getTemplateVariableOptionMap() {
  const optionMap = new Map();

  getContextVariableGroups().forEach((group) => {
    (group.variables || []).forEach((option) => {
      const normalizedBinding = createContextBinding({
        ...option,
        sourceNodeLabel: option.sourceNodeLabel || group.nodeLabel || "",
        sourceNodeType: option.sourceNodeType || group.nodeType || "",
      });

      if (!normalizedBinding) {
        return;
      }

      const sourceSegment = buildTemplateReferenceSegment(
        normalizedBinding.sourceNodeId ||
          normalizedBinding.sourceNodeType ||
          "node",
        "node"
      );
      const variablePath = `${
        normalizedBinding.path || normalizedBinding.variableKey || ""
      }`.trim();

      if (!sourceSegment || !variablePath) {
        return;
      }

      optionMap.set(
        buildTemplateVariableReferenceKey(sourceSegment, variablePath),
        normalizedBinding
      );
    });
  });

  return optionMap;
}

function getTemplatePreviewSegments(content = "") {
  const currentContent = `${content || ""}`;

  if (!currentContent) {
    return [];
  }

  const optionMap = getTemplateVariableOptionMap();
  const variablePattern = /\{\{\s*([A-Za-z0-9_]+)\.([A-Za-z0-9_.]+)\s*\}\}/g;
  const segments = [];
  let lastIndex = 0;
  let match = variablePattern.exec(currentContent);

  while (match) {
    const [rawValue, sourceSegment, variablePath] = match;

    if (match.index > lastIndex) {
      segments.push({
        type: "text",
        value: currentContent.slice(lastIndex, match.index),
      });
    }

    const binding = optionMap.get(
      buildTemplateVariableReferenceKey(sourceSegment, variablePath)
    );

    segments.push({
      type: "variable",
      value: rawValue,
      display: binding
        ? getContextVariableDisplay(binding)
        : `${sourceSegment}.${variablePath}`,
    });

    lastIndex = match.index + rawValue.length;
    match = variablePattern.exec(currentContent);
  }

  if (lastIndex < currentContent.length) {
    segments.push({
      type: "text",
      value: currentContent.slice(lastIndex),
    });
  }

  return segments.length ? segments : [{ type: "text", value: currentContent }];
}

function getContextVariableGroups() {
  return Array.isArray(props.availableContextVariableGroups)
    ? props.availableContextVariableGroups.filter(
        (group) => Array.isArray(group?.variables) && group.variables.length
      )
    : [];
}

function isSlashVariableMenuVisible(target = {}) {
  return slashVariableMenuState.value.targetKey === getEditorTargetKey(target);
}

function shouldOpenSlashVariableMenu(target = {}) {
  const targetKey = getEditorTargetKey(target);
  const selection = editorSelections.value[targetKey];

  if (
    !getContextVariableGroups().length ||
    !selection ||
    !Number.isInteger(selection.start) ||
    selection.start !== selection.end
  ) {
    return false;
  }

  const content = getEditorContent(target);
  const caret = Math.max(0, Math.min(selection.start, content.length));

  return caret > 0 && content.slice(caret - 1, caret) === "/";
}

function syncSlashVariableMenu(target = {}) {
  if (shouldOpenSlashVariableMenu(target)) {
    slashVariableMenuState.value = {
      targetKey: getEditorTargetKey(target),
    };

    if (target.type === "message") {
      closePromptVariablePopover();
      messageVariablePopoverVisibleMap.value = target.id
        ? { [target.id]: true }
        : {};
    } else {
      closeAllMessageVariablePopovers();
      promptVariablePopoverVisible.value = true;
    }

    return;
  }

  if (isSlashVariableMenuVisible(target)) {
    hideEditorVariablePopover(target);
  }
}

function createContextBinding(option = {}) {
  const sourceNodeId = `${option.sourceNodeId || ""}`.trim();
  const variableKey = `${option.variableKey || ""}`.trim();

  if (!sourceNodeId || !variableKey) {
    return null;
  }

  return {
    sourceNodeId,
    sourceNodeLabel: `${option.sourceNodeLabel || ""}`.trim(),
    sourceNodeType: `${option.sourceNodeType || ""}`.trim(),
    variableKey,
    variableLabel: `${option.variableLabel || variableKey}`.trim(),
    valueType: `${option.valueType || ""}`.trim(),
    path: `${option.path || ""}`.trim(),
  };
}

function getContextVariableName(binding = {}) {
  return binding.variableLabel || binding.variableKey || "";
}

function getContextVariableOptionLabel(binding = {}) {
  return binding.variableLabel || binding.variableKey || "";
}

function getContextVariableDisplay(binding = {}) {
  const variableLabel = getContextVariableName(binding);

  return variableLabel || "";
}

function getContextVariableEmptyText() {
  return props.hasUpstreamContextSources
    ? "当前上游节点暂无可用输出变量"
    : "请先连接上游节点";
}

function getMessageRenderSignature() {
  return (props.node?.data?.messages || [])
    .map((message) => `${message.id || ""}::${message.content || ""}`)
    .join("||");
}

function getContextVariableRenderSignature() {
  return getContextVariableGroups()
    .map((group) => {
      const variables = (group.variables || [])
        .map((option) =>
          [
            option.sourceNodeId || "",
            option.variableKey || "",
            option.variableLabel || "",
            option.path || "",
          ].join(":")
        )
        .join(",");

      return `${group.nodeId || ""}:${group.nodeLabel || ""}:${variables}`;
    })
    .join("||");
}

function syncAllEditorContent() {
  renderEditorContent({ type: "prompt" });

  (props.node?.data?.messages || []).forEach((message) => {
    renderEditorContent({ type: "message", id: message.id });
  });
}

watch(
  () => [
    props.node?.data?.prompt || "",
    getMessageRenderSignature(),
    getContextVariableRenderSignature(),
  ],
  () => {
    nextTick(() => {
      syncAllEditorContent();
    });
  },
  {
    immediate: true,
  }
);
</script>

<style scoped lang="scss">
.llm-config-panel {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.llm-config-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.llm-config-label,
.llm-switch-label,
.llm-output-title {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 400;
  color: #111827;
}
.llm-help-badge {
  width: 16px;
  height: 16px;
  border-radius: 999px;
  border: 1px solid #d7dee8;
  color: #9ca3af;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  line-height: 1;
}
.llm-model-config-anchor {
  width: 100%;
}
.llm-model-simple-select {
  width: 100%;
}
.llm-model-trigger {
  width: 100%;
  padding: 11px 12px;
  border: 1px solid #e5e7eb;
  // border-radius: 12px;
  background-color: #f7f8fb;
  color: #111827;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  cursor: pointer;
  transition: border-color 0.2s ease, box-shadow 0.2s ease,
    background-color 0.2s ease;
}
.llm-model-trigger:hover {
  border-color: #cbd5e1;
  background-color: #f3f6fb;
}
.llm-model-trigger:focus-visible {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.14);
}
.llm-model-trigger-main,
.llm-model-trigger-copy,
.llm-model-trigger-actions {
  display: flex;
  align-items: center;
}
.llm-model-trigger-main {
  min-width: 0;
  gap: 10px;
}
.llm-model-trigger-copy {
  min-width: 0;
  gap: 8px;
}
.llm-model-trigger-actions {
  gap: 10px;
  color: #6b7280;
  flex-shrink: 0;
}
.llm-model-name {
  min-width: 0;
  font-size: 14px;
  font-weight: 500;
  color: #111827;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.llm-model-icon {
  width: 20px;
  height: 20px;
  border-radius: 999px;
  background-color: #eef2ff;
  color: #6366f1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.llm-model-icon svg {
  width: 12px;
  height: 12px;
}
.llm-model-mode {
  padding: 2px 8px;
  border-radius: 999px;
  background-color: #eef2f7;
  color: #6b7280;
  font-size: 11px;
  font-weight: 600;
  line-height: 1.4;
  flex-shrink: 0;
}
.llm-model-gear {
  width: 18px;
  height: 18px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.llm-model-gear svg,
.llm-model-chevron {
  width: 16px;
  height: 16px;
}
:deep(.llm-model-config-dialog .el-dialog) {
  margin: 8vh auto 0;
}
:deep(.llm-model-config-dialog .el-dialog__header) {
  padding: 18px 20px;
  margin-right: 0;
  border-bottom: 1px solid #ebeef5;
}
:deep(.llm-model-config-dialog .el-dialog__title) {
  font-size: 16px;
  font-weight: 700;
  color: #111827;
}
:deep(.llm-model-config-dialog .el-dialog__body) {
  padding: 20px 40px;
  overflow: visible;
}
:deep(.llm-model-config-dialog .el-dialog__footer) {
  padding: 12px 24px 20px;
  border-top: 1px solid #7072751a;
}
.llm-model-config-sheet.is-dialog {
  display: flex;
  flex-direction: column;
  gap: 18px;
  max-height: none;
  overflow: visible;
}
.llm-model-config-form {
  width: 100%;
}
.llm-model-form-item {
  margin-bottom: 16px;
}
.llm-model-form-item:last-child {
  margin-bottom: 0;
}
.llm-model-form-item :deep(.el-form-item__label) {
  min-height: 36px;
  padding: 0 12px 0 0;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  font-size: 14px;
  font-weight: 600;
  line-height: 20px;
  color: #111827;
  white-space: nowrap;
}
.llm-model-form-item :deep(.el-form-item__content) {
  flex: 1;
  width: 100%;
  min-width: 0;
  line-height: normal;
}
.llm-parameter-control {
  width: 100%;
  min-width: 0;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 96px;
  align-items: center;
  gap: 12px;
}
.llm-parameter-control.is-field {
  grid-template-columns: minmax(0, 1fr);
}
.llm-parameter-number,
.llm-parameter-select,
.llm-parameter-input {
  width: 100%;
}
.llm-model-config-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
.llm-boolean-radio-group {
  display: flex;
  align-items: center;
  gap: 24px;
  min-height: 36px;
}
.llm-boolean-radio-group :deep(.el-radio) {
  margin-right: 0;
}
:deep(.llm-context-chip) {
  display: inline-flex;
  align-items: center;
  min-width: 0;
  max-width: 100%;
}
.llm-context-picker {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 280px;
  overflow: auto;
}
:deep(.llm-context-picker-popper.el-popover) {
  padding: 8px 10px;
}
.llm-context-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.llm-context-group-title {
  font-size: 12px;
  font-weight: 700;
  line-height: 1.5;
  color: #6b7280;
}
.llm-context-option {
  width: 100%;
  padding: 3px 0;
  border: none;
  background-color: transparent;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0;
  cursor: pointer;
  text-align: left;
  transition: background-color 0.2s ease;
}
.llm-context-option:hover {
  background-color: #f8fafc;
}
:deep(.llm-context-option-tag) {
  --el-tag-bg-color: #eff4ff;
  --el-tag-border-color: transparent;
  --el-tag-text-color: #2563eb;
  max-width: 100%;
  height: 24px;
  line-height: 22px;
  padding: 0 10px;
  border-radius: 4px;
  border-color: transparent;
  font-size: 12px;
  font-weight: 500;
}

:deep(.llm-context-option-tag .el-tag__content) {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.llm-context-empty {
  padding: 12px;
  border: 1px dashed #d7dee8;
  background-color: #f8fafc;
  color: #6b7280;
  font-size: 12px;
  line-height: 1.6;
}
.llm-message-card {
  padding: 14px;
  border: 1px solid #e5e7eb;
  // border-radius: 14px;
  // background-color: #f7f8fb;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.llm-message-card.is-secondary {
  background-color: #ffffff;
}
.llm-message-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
.llm-prompt-section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
.llm-prompt-title {
  font-size: 14px;
  font-weight: 400;
  color: #111827;
  line-height: 1.5;
}
.llm-message-header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}
.llm-message-role {
  font-size: 13px;
  font-weight: 700;
  color: #374151;
  letter-spacing: 0.3px;
}
.llm-message-role.is-user {
  color: #2563eb;
}
.llm-message-role-row {
  display: flex;
  align-items: center;
  min-width: 0;
}
.llm-message-role-select {
  width: 136px;
}
.llm-message-role-select :deep(.el-select__wrapper) {
  min-height: 32px;
  padding: 0 10px 0 12px;
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 0 0 1px #d7dee8 inset;
}
.llm-message-role-select :deep(.el-select__wrapper:hover) {
  box-shadow: 0 0 0 1px #cbd5e1 inset;
}
.llm-message-role-select :deep(.el-select__wrapper.is-focused) {
  box-shadow: 0 0 0 1px #2563eb inset;
}
.llm-message-role-select :deep(.el-select__selected-item) {
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.3px;
}
.llm-message-role-select.is-user :deep(.el-select__selected-item) {
  color: #111827;
}
.llm-message-role-select.is-assistant :deep(.el-select__selected-item) {
  color: #111827;
}
.llm-message-role-select :deep(.el-select__caret) {
  color: #94a3b8;
}
.llm-message-toolbar {
  display: flex;
  align-items: center;
  min-width: 0;
}
.llm-message-toolbar-chip {
  color: #2563eb;
  font-size: 12px;
  line-height: 20px;
}
.llm-message-toolbar-chip-button {
  border: none;
  padding: 0;
  background: transparent;
  display: inline-flex;
  align-items: center;
  cursor: pointer;
}
.llm-message-toolbar-chip-button:hover {
  color: #1d4ed8;
}
.llm-message-toolbar-chip-button:focus-visible {
  outline: none;
  color: #1d4ed8;
}
.llm-variable-popover-trigger {
  position: relative;
  z-index: 1;
}
.llm-add-message-trigger {
  min-width: 74px;
  height: 32px;
  padding: 0 14px;
  flex-shrink: 0;
}
.llm-message-editor-shell {
  position: relative;
}
.llm-prompt-editor-label {
  position: absolute;
  top: 2px;
  left: 0;
  z-index: 1;
  font-size: 13px;
  font-weight: 700;
  color: #374151;
  letter-spacing: 0.3px;
  line-height: 24px;
}
.llm-prompt-toolbar {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 1;
}

.llm-message-editor {
  min-height: 72px;
  color: #374151;
  font-size: 14px;
  line-height: 1.7;
  white-space: pre-wrap;
  word-break: break-word;
  outline: none;
  cursor: text;
}

.llm-message-editor.is-prompt {
  min-height: 96px;
  padding-top: 30px;
}

.llm-message-editor.is-empty::before {
  content: attr(data-placeholder);
  color: #b8c0cc;
}

:deep(.llm-message-editor-text) {
  color: #374151;
}

:deep(.llm-message-editor-chip) {
  display: inline-flex;
  vertical-align: baseline;
  margin: 0 2px;
  white-space: nowrap;
}
:deep(.llm-context-chip-tag) {
  --el-tag-bg-color: #eff4ff;
  --el-tag-border-color: transparent;
  --el-tag-text-color: #2563eb;
  max-width: 100%;
  height: 24px;
  line-height: 22px;
  padding: 0 10px;
  border-radius: 4px;
  border-color: transparent;
  font-size: 12px;
  font-weight: 500;
  vertical-align: baseline;
}

:deep(.llm-context-chip-tag .el-tag__content) {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.llm-message-remove {
  border: none;
  padding: 0;
  background: transparent;
  color: #ef4444;
  font-size: 12px;
  line-height: 20px;
  display: inline-flex;
  align-items: center;
  cursor: pointer;
}
.llm-message-remove:hover {
  color: #dc2626;
}
.blue-bar {
  background-color: #2666fb;
  width: 6px;
  height: 16px;
  // margin-right: 10px;
  border-radius: 10px;
}
</style>

