<template>
  <div class="chatflow-reply-config-panel">
    <div class="chatflow-reply-config-section">
      <div class="chatflow-reply-config-title node-config-section-title">
        <span class="blue-bar"></span> 输出
      </div>

      <div class="chatflow-reply-config-card">
        <div class="chatflow-reply-editor-shell">
          <div class="chatflow-reply-toolbar">
            <div class="chatflow-reply-toolbar-title">返回结果</div>
            <el-popover
              :visible="variablePopoverVisible"
              @update:visible="handleVariablePopoverVisibleChange"
              placement="bottom-end"
              trigger="click"
              :teleported="true"
              :width="260"
              popper-class="chatflow-reply-context-picker-popper dydome-square-popper"
            >
              <template #reference>
                <button
                  type="button"
                  class="chatflow-reply-toolbar-chip chatflow-reply-toolbar-chip-button"
                  @mousedown.prevent
                  @click.stop
                >
                  插入
                </button>
              </template>

              <div class="chatflow-reply-context-picker">
                <template v-if="getContextVariableGroups().length">
                  <div
                    v-for="group in getContextVariableGroups()"
                    :key="group.nodeId"
                    class="chatflow-reply-context-group"
                  >
                    <div class="chatflow-reply-context-group-title">
                      {{ group.nodeLabel || "上游节点" }}
                    </div>

                    <button
                      v-for="option in group.variables"
                      :key="`${option.sourceNodeId}-${option.variableKey}`"
                      type="button"
                      class="chatflow-reply-context-option"
                      @mousedown.prevent
                      @click="insertVariable(option)"
                    >
                      <el-tag
                        disable-transitions
                        effect="light"
                        size="small"
                        class="chatflow-reply-context-option-tag"
                        :title="getContextVariableOptionLabel(option)"
                      >
                        {{ getContextVariableOptionLabel(option) }}
                      </el-tag>
                    </button>
                  </div>
                </template>

                <div v-else class="chatflow-reply-context-empty">
                  {{ getContextVariableEmptyText() }}
                </div>
              </div>
            </el-popover>
          </div>

          <div
            ref="editorRef"
            contenteditable="true"
            spellcheck="false"
            class="chatflow-reply-editor"
            :class="{ 'is-empty': !content && !editorComposing }"
            data-placeholder="请输入"
            @focus="handleEditorFocus"
            @blur="handleEditorBlur"
            @compositionstart="handleEditorCompositionStart"
            @compositionend="handleEditorCompositionEnd"
            @click="handleEditorSelectionChange"
            @keyup="handleEditorSelectionChange"
            @mouseup="handleEditorSelectionChange"
            @beforeinput="handleEditorBeforeInput($event)"
            @input="handleEditorInput($event)"
            @keydown="handleEditorKeydown($event)"
            @paste="handleEditorPaste($event)"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { nextTick, ref, watch } from "vue";

const props = defineProps({
  content: {
    type: String,
    default: "",
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

const emit = defineEmits(["updateContent"]);

const editorRef = ref(null);
const variablePopoverVisible = ref(false);
const editorSelection = ref({
  start: 0,
  end: 0,
});
const editorComposing = ref(false);
const slashVariableMenuVisible = ref(false);

function setContent(value = "") {
  emit("updateContent", value);
}

function getEditorContent() {
  return `${props.content || ""}`;
}

function getContextVariableGroups() {
  return Array.isArray(props.availableContextVariableGroups)
    ? props.availableContextVariableGroups.filter(
        (group) => Array.isArray(group?.variables) && group.variables.length
      )
    : [];
}

function getContextVariableEmptyText() {
  return props.hasUpstreamContextSources
    ? "当前上游节点暂无可用输出变量"
    : "请先连接上游节点";
}

function getContextVariableOptionLabel(binding = {}) {
  return binding.variableLabel || binding.variableKey || "";
}

function getContextVariableDisplay(binding = {}) {
  return getContextVariableOptionLabel(binding);
}

function closeVariablePopover() {
  variablePopoverVisible.value = false;
}

function closeSlashVariableMenu() {
  slashVariableMenuVisible.value = false;
}

function handleVariablePopoverVisibleChange(visible = false) {
  if (!visible) {
    if (variablePopoverVisible.value) {
      variablePopoverVisible.value = false;
    }
    return;
  }

  closeSlashVariableMenu();
  variablePopoverVisible.value = true;
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

function serializeEditorContent() {
  const editorElement = editorRef.value;

  if (!editorElement) {
    return getEditorContent();
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
  wrapper.className = "chatflow-reply-context-chip chatflow-reply-editor-chip";
  wrapper.contentEditable = "false";
  wrapper.dataset.templateExpression = segment.value || "";

  const tag = document.createElement("span");
  tag.className =
    "el-tag el-tag--light el-tag--small chatflow-reply-context-chip-tag";

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

function markRenderedContent(content = "") {
  const editorElement = editorRef.value;

  if (!editorElement) {
    return;
  }

  editorElement.dataset.renderedContent = `${content || ""}`;
}

function shouldRenderEditorContent() {
  const editorElement = editorRef.value;

  if (!editorElement) {
    return false;
  }

  const content = getEditorContent();
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

function saveSelection({ preserveExisting = false } = {}) {
  const editorElement = editorRef.value;
  const contentLength = getEditorContent().length;

  if (!editorElement) {
    if (preserveExisting && editorSelection.value) {
      return;
    }

    editorSelection.value = {
      start: contentLength,
      end: contentLength,
    };
    return;
  }

  const selection = window.getSelection();

  if (!selection || !selection.rangeCount) {
    if (preserveExisting && editorSelection.value) {
      return;
    }

    editorSelection.value = {
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
    if (preserveExisting && editorSelection.value) {
      return;
    }

    editorSelection.value = {
      start: contentLength,
      end: contentLength,
    };
    return;
  }

  editorSelection.value = {
    start: getSelectionOffset(
      editorElement,
      range.startContainer,
      range.startOffset
    ),
    end: getSelectionOffset(editorElement, range.endContainer, range.endOffset),
  };
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

function restoreSelection() {
  const editorElement = editorRef.value;

  if (!editorElement || !editorSelection.value) {
    return;
  }

  const selection = window.getSelection();

  if (!selection) {
    return;
  }

  const startPosition = locateEditorPosition(
    editorElement,
    editorSelection.value.start
  );
  const endPosition = locateEditorPosition(
    editorElement,
    editorSelection.value.end
  );
  const range = document.createRange();

  range.setStart(startPosition.container, startPosition.offset);
  range.setEnd(endPosition.container, endPosition.offset);
  selection.removeAllRanges();
  selection.addRange(range);
}

function focusEditor() {
  const editorElement = editorRef.value;

  if (!editorElement) {
    return;
  }

  editorElement.focus();
  restoreSelection();
}

function renderEditorContent({ preserveSelection = true, force = false } = {}) {
  const editorElement = editorRef.value;

  if (!editorElement || (!force && !shouldRenderEditorContent())) {
    return;
  }

  const content = getEditorContent();
  const isFocused = document.activeElement === editorElement;

  if (preserveSelection && isFocused) {
    saveSelection({ preserveExisting: true });
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
  markRenderedContent(content);

  if (preserveSelection && isFocused) {
    restoreSelection();
  }
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

function insertTextIntoEditor(insertedText = "") {
  if (!insertedText) {
    return;
  }

  const { value, caret } = insertTextAtSelection(
    getEditorContent(),
    insertedText,
    editorSelection.value || {}
  );

  editorSelection.value = {
    start: caret,
    end: caret,
  };
  setContent(value);

  nextTick(() => {
    focusEditor();
    syncSlashVariableMenu();
  });
}

function deleteTextFromEditor(direction = "backward") {
  const { value, caret } = removeTextAtSelection(
    getEditorContent(),
    editorSelection.value || {},
    direction
  );

  editorSelection.value = {
    start: caret,
    end: caret,
  };
  setContent(value);

  nextTick(() => {
    focusEditor();
    syncSlashVariableMenu();
  });
}

function handleEditorBeforeInput(event) {
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
    insertTextIntoEditor("\n");
    return;
  }

  if (inputType === "deleteContentBackward") {
    event.preventDefault();
    deleteTextFromEditor("backward");
    return;
  }

  if (inputType === "deleteContentForward") {
    event.preventDefault();
    deleteTextFromEditor("forward");
  }
}

function syncContentFromDom() {
  saveSelection();
  const nextValue = serializeEditorContent();

  setContent(nextValue);

  const editorElement = editorRef.value;

  if (
    editorElement &&
    (!hasTemplateExpression(nextValue) ||
      editorElement.querySelector("[data-template-expression]"))
  ) {
    markRenderedContent(nextValue);
  }

  nextTick(() => {
    focusEditor();
    syncSlashVariableMenu();
  });
}

function handleEditorInput(event) {
  if (event?.isComposing || editorComposing.value) {
    return;
  }

  syncContentFromDom();
}

function handleEditorCompositionStart() {
  editorComposing.value = true;
  hideVariablePopover();
}

function handleEditorCompositionEnd() {
  editorComposing.value = false;
  syncContentFromDom();
}

function handleEditorFocus() {
  saveSelection();
  syncSlashVariableMenu();
}

function handleEditorBlur() {
  saveSelection({ preserveExisting: true });

  if (slashVariableMenuVisible.value) {
    hideVariablePopover();
  }
}

function handleEditorSelectionChange() {
  saveSelection();
  syncSlashVariableMenu();
}

function handleEditorKeydown(event) {
  if (event.key === "Escape" && slashVariableMenuVisible.value) {
    event.preventDefault();
    hideVariablePopover();
    return;
  }

  if (event.key !== "Enter") {
    return;
  }

  event.preventDefault();
  insertTextIntoEditor("\n");
}

function handleEditorPaste(event) {
  const pastedText = event.clipboardData?.getData("text/plain");

  if (!pastedText) {
    return;
  }

  event.preventDefault();
  insertTextIntoEditor(pastedText.replace(/\r\n?/g, "\n"));
}

function shouldOpenSlashVariableMenu() {
  if (
    !getContextVariableGroups().length ||
    !editorSelection.value ||
    !Number.isInteger(editorSelection.value.start) ||
    editorSelection.value.start !== editorSelection.value.end
  ) {
    return false;
  }

  const content = getEditorContent();
  const caret = Math.max(
    0,
    Math.min(editorSelection.value.start, content.length)
  );

  return caret > 0 && content.slice(caret - 1, caret) === "/";
}

function syncSlashVariableMenu() {
  if (shouldOpenSlashVariableMenu()) {
    slashVariableMenuVisible.value = true;
    variablePopoverVisible.value = true;
    return;
  }

  if (slashVariableMenuVisible.value) {
    hideVariablePopover();
  }
}

function hideVariablePopover() {
  closeSlashVariableMenu();
  closeVariablePopover();
}

function insertVariable(option = {}) {
  const expression = buildEditorVariableExpression(option);

  if (!expression) {
    return;
  }

  let nextSelection = editorSelection.value || {};

  if (
    slashVariableMenuVisible.value &&
    Number.isInteger(nextSelection.start) &&
    nextSelection.start === nextSelection.end
  ) {
    const caret = nextSelection.start;
    const content = getEditorContent();

    if (caret > 0 && content.slice(caret - 1, caret) === "/") {
      nextSelection = {
        start: caret - 1,
        end: caret,
      };
    }
  }

  const { value, caret } = insertTextAtSelection(
    getEditorContent(),
    expression,
    nextSelection
  );

  editorSelection.value = {
    start: caret,
    end: caret,
  };
  setContent(value);

  nextTick(() => {
    hideVariablePopover();
    focusEditor();
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
    path: `${option.path || variableKey}`.trim(),
  };
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

watch(
  () => [props.content || "", getContextVariableRenderSignature()],
  () => {
    nextTick(() => {
      renderEditorContent();
    });
  },
  {
    immediate: true,
  }
);
</script>

<style scoped lang="scss">
.chatflow-reply-config-panel {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.chatflow-reply-config-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chatflow-reply-config-title {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 400;
  color: #111827;
}

.chatflow-reply-config-card {
  padding: 14px;
  border: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chatflow-reply-editor-shell {
  display: flex;
  flex-direction: column;
}

.chatflow-reply-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 8px;
}

.chatflow-reply-toolbar-title {
  font-size: 14px;
  font-weight: 500;
  line-height: 1.5;
  color: #374151;
}

.chatflow-reply-toolbar-chip {
  color: #2563eb;
  font-size: 12px;
  line-height: 20px;
}

.chatflow-reply-toolbar-chip-button {
  border: none;
  padding: 0;
  background: transparent;
  display: inline-flex;
  align-items: center;
  cursor: pointer;
}

.chatflow-reply-toolbar-chip-button:hover,
.chatflow-reply-toolbar-chip-button:focus-visible {
  color: #1d4ed8;
  outline: none;
}

.chatflow-reply-context-picker {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 280px;
  overflow: auto;
}

:deep(.chatflow-reply-context-picker-popper.el-popover) {
  padding: 8px 10px;
}

.chatflow-reply-context-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.chatflow-reply-context-group-title {
  font-size: 12px;
  font-weight: 700;
  line-height: 1.5;
  color: #6b7280;
}

.chatflow-reply-context-option {
  width: 100%;
  padding: 3px 0;
  border: none;
  background-color: transparent;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  cursor: pointer;
  text-align: left;
  transition: background-color 0.2s ease;
}

.chatflow-reply-context-option:hover {
  background-color: #f8fafc;
}

:deep(.chatflow-reply-context-option-tag) {
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

:deep(.chatflow-reply-context-option-tag .el-tag__content) {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.chatflow-reply-context-empty {
  padding: 12px;
  border: 1px dashed #d7dee8;
  background-color: #f8fafc;
  color: #6b7280;
  font-size: 12px;
  line-height: 1.6;
}

.chatflow-reply-editor {
  min-height: 84px;
  color: #374151;
  font-size: 14px;
  line-height: 1.7;
  white-space: pre-wrap;
  word-break: break-word;
  outline: none;
  cursor: text;
}

.chatflow-reply-editor.is-empty::before {
  content: attr(data-placeholder);
  color: #b8c0cc;
}

:deep(.chatflow-reply-context-chip) {
  display: inline-flex;
  align-items: center;
  min-width: 0;
  max-width: 100%;
}

:deep(.chatflow-reply-editor-chip) {
  display: inline-flex;
  vertical-align: baseline;
  margin: 0 2px;
  white-space: nowrap;
}

:deep(.chatflow-reply-context-chip-tag) {
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

:deep(.chatflow-reply-context-chip-tag .el-tag__content) {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.blue-bar {
  background-color: #2666fb;
  width: 6px;
  height: 16px;
  // margin-right: 10px;
  border-radius: 10px;
}
</style>
