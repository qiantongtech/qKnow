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
  <div
    ref="canvasRef"
    class="loop-workflow-canvas"
    :class="{ compact, 'is-empty': isEmptyBody }"
    :style="canvasStyle"
    @pointerdown.stop
    @click.stop
  >
    <VueFlow
      :nodes="localNodes"
      :edges="localEdges"
      :id="flowInstanceId"
      class="loop-workflow-stage"
      :style="stageStyle"
      :fit-view-on-init="false"
      :min-zoom="1"
      :max-zoom="1"
      :default-zoom="1"
      :snap-to-grid="false"
      :snap-grid="[20, 20]"
      :pan-on-drag="false"
      :pan-on-scroll="false"
      :zoom-on-scroll="false"
      :zoom-on-double-click="false"
      :prevent-scrolling="false"
      :nodes-draggable="false"
      :node-drag-threshold="0"
      :no-drag-class-name="'loop-mini-no-drag'"
      :no-pan-class-name="'loop-mini-no-pan'"
      :default-edge-options="defaultEdgeOptions"
      :connection-line-type="ConnectionLineType.Bezier"
      @connect="onInnerConnect"
      @node-click="onInnerNodeClick"
      @pane-click="onPaneClick"
    >
      <template #node-loop-start="nodeProps">
        <div class="loop-mini-start">
          <div class="loop-mini-start-lock">
            <el-icon><Lock /></el-icon>
          </div>
          <div v-if="!compact" class="loop-mini-start-text">
            <div class="loop-mini-start-title">
              {{ getNodeLabel(nodeProps.data, "循环开始") }}
            </div>
            <div class="loop-mini-start-tip">寰幆鍏ュ彛鑺傜偣</div>
          </div>
          <Handle
            type="source"
            :position="Position.Right"
            :connectable-start="false"
            class="loop-mini-handle source loop-mini-source-handle"
          />
          <button
            class="loop-mini-add-btn loop-mini-no-drag loop-mini-no-pan"
            type="button"
            @click.stop.prevent="showAddNodeMenuFromNode(nodeProps.id, $event)"
            @pointerdown.stop.prevent="
              onInnerSourceActionPointerDown($event, nodeProps.id)
            "
          >
            <el-icon><Plus /></el-icon>
          </button>
        </div>
      </template>

      <template #node-llm="nodeProps">
        <div
          class="custom-node llm-node loop-inner-drag-handle"
          :class="{ selected: selectedNodeId === nodeProps.id }"
          @pointerdown.stop.prevent="
            onInnerNodePointerDown($event, nodeProps.id)
          "
        >
          <button
            type="button"
            class="node-delete-btn loop-mini-no-drag loop-mini-no-pan"
            @click.stop.prevent="emitDeleteNode(nodeProps.id)"
            @pointerdown.stop.prevent
          >
            <el-icon><Delete /></el-icon>
          </button>
          <div class="node-top">
            <span class="node-type-label">LLM</span>
          </div>
          <div class="node-body">
            <div class="node-header">
              <div class="node-icon-wrapper">
                <LlmNodeIcon class="node-svg llm-node-svg" />
              </div>
              <div class="node-title">
                {{ getNodeLabel(nodeProps.data, "LLM") }}
              </div>
            </div>
            <div
              class="node-subtitle"
              v-if="getNodeConfigValue(nodeProps.data, 'model', '')"
            >
              {{ getNodeConfigValue(nodeProps.data, "model", "") }}
            </div>
          </div>
          <Handle
            type="target"
            :position="Position.Left"
            :class="[
              'loop-inner-handle',
              {
                'loop-inner-handle-drop-target':
                  dragHoverTargetId === nodeProps.id,
              },
            ]"
          />
          <Handle
            type="source"
            :position="Position.Right"
            :connectable-start="false"
            class="loop-inner-handle loop-inner-source-handle"
          />
          <div
            class="node-add-btn loop-inner-node-add-btn"
            @click.stop.prevent
            @pointerdown.stop.prevent="
              onInnerSourceActionPointerDown($event, nodeProps.id)
            "
          >
            <el-icon><Plus /></el-icon>
          </div>
        </div>
      </template>

      <template #node-reply="nodeProps">
        <div
          class="custom-node reply-node loop-inner-drag-handle"
          :class="{ selected: selectedNodeId === nodeProps.id }"
          @pointerdown.stop.prevent="
            onInnerNodePointerDown($event, nodeProps.id)
          "
        >
          <button
            type="button"
            class="node-delete-btn loop-mini-no-drag loop-mini-no-pan"
            @click.stop.prevent="emitDeleteNode(nodeProps.id)"
            @pointerdown.stop.prevent
          >
            <el-icon><Delete /></el-icon>
          </button>
          <div class="node-top">
            <span class="node-type-label">输出</span>
          </div>
          <div class="node-body">
            <div class="node-header">
              <div class="node-icon-wrapper">
                <svg viewBox="0 0 24 24" fill="currentColor" class="node-svg">
                  <path
                    d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"
                  />
                </svg>
              </div>
              <div class="node-title">
                {{ getNodeLabel(nodeProps.data, "输出") }}
              </div>
            </div>
            <div
              class="node-preview"
              v-if="getLoopReplyPreviewList(nodeProps.data, 3).length"
            >
              <span
                v-for="(previewItem, previewIndex) in getLoopReplyPreviewList(
                  nodeProps.data,
                  3
                )"
                :key="`loop-reply-preview-${nodeProps.id}-${previewIndex}`"
                class="preview-tag"
              >
                {{ previewItem }}
              </span>
              <div
                v-if="getLoopReplyPreviewOverflowCount(nodeProps.data, 3) > 0"
                class="reply-node-preview-more"
              >
                +{{ getLoopReplyPreviewOverflowCount(nodeProps.data, 3) }}
                个输出字段
              </div>
            </div>
          </div>
          <Handle
            type="target"
            :position="Position.Left"
            :class="[
              'loop-inner-handle',
              {
                'loop-inner-handle-drop-target':
                  dragHoverTargetId === nodeProps.id,
              },
            ]"
          />
          <Handle
            type="source"
            :position="Position.Right"
            :connectable-start="false"
            class="loop-inner-handle loop-inner-source-handle"
          />
          <div
            class="node-add-btn loop-inner-node-add-btn"
            @click.stop.prevent
            @pointerdown.stop.prevent="
              onInnerSourceActionPointerDown($event, nodeProps.id)
            "
          >
            <el-icon><Plus /></el-icon>
          </div>
        </div>
      </template>

      <template #node-loop="nodeProps">
        <div
          class="custom-node loop-node loop-inner-drag-handle"
          :class="{ selected: selectedNodeId === nodeProps.id }"
          :style="getRegularLoopNodeStyle(nodeProps.data)"
          @pointerdown.stop.prevent="
            onInnerNodePointerDown($event, nodeProps.id)
          "
        >
          <button
            type="button"
            class="node-delete-btn loop-mini-no-drag loop-mini-no-pan"
            @click.stop.prevent="emitDeleteNode(nodeProps.id)"
            @pointerdown.stop.prevent
          >
            <el-icon><Delete /></el-icon>
          </button>
          <div class="loop-node-shell">
            <div class="loop-node-header">
              <div class="loop-node-icon">
                <span class="loop-icon-glyph">&#8734;</span>
              </div>
              <div class="loop-node-title">
                {{ getNodeLabel(nodeProps.data, "循环") }}
              </div>
            </div>

            <div
              class="loop-editor-surface loop-mini-no-drag loop-mini-no-pan"
              :style="getRegularLoopEditorSurfaceStyle(nodeProps.data)"
              @pointerdown.stop
              @click.stop
            >
              <LoopWorkflowCanvas
                :workflow-data="nodeProps.data"
                :compact="true"
                :loop-path="[...props.loopPath, nodeProps.id]"
                @update:workflow-data="
                  updateNestedLoopNodeData(nodeProps.id, $event)
                "
                @selectNode="emit('selectNode', $event)"
                @clearSelection="emit('clearSelection', $event)"
                @deleteNode="emit('deleteNode', $event)"
              />
            </div>
          </div>
          <Handle
            type="target"
            :position="Position.Left"
            :class="[
              'loop-inner-handle',
              {
                'loop-inner-handle-drop-target':
                  dragHoverTargetId === nodeProps.id,
              },
            ]"
          />
          <Handle
            type="source"
            :position="Position.Right"
            :connectable-start="false"
            class="loop-inner-handle loop-inner-source-handle"
          />
          <div
            class="node-add-btn loop-inner-node-add-btn"
            @click.stop.prevent
            @pointerdown.stop.prevent="
              onInnerSourceActionPointerDown($event, nodeProps.id)
            "
          >
            <el-icon><Plus /></el-icon>
          </div>
        </div>
      </template>

      <template #node-condition="nodeProps">
        <div
          class="custom-node condition-node loop-inner-drag-handle"
          :class="{ selected: selectedNodeId === nodeProps.id }"
          @pointerdown.stop.prevent="
            onInnerNodePointerDown($event, nodeProps.id)
          "
        >
          <button
            type="button"
            class="node-delete-btn loop-mini-no-drag loop-mini-no-pan"
            @click.stop.prevent="emitDeleteNode(nodeProps.id)"
            @pointerdown.stop.prevent
          >
            <el-icon><Delete /></el-icon>
          </button>
          <div class="node-top">
            <span class="node-type-label">鏉′欢鍒嗘敮</span>
          </div>
          <div class="node-body">
            <div class="condition-summary">
              <div class="node-icon-wrapper">
                <svg viewBox="0 0 24 24" fill="currentColor" class="node-svg">
                  <path
                    d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 9h-2V5h2v7zM7 9h2v7H7V9zm10 7h-2V9h2v7z"
                  />
                </svg>
              </div>
              <div class="condition-title-row">
                <div class="node-title">
                  {{ getNodeLabel(nodeProps.data, "鏉′欢鍒嗘敮") }}
                </div>
                <span class="condition-count">{{
                  getConditionCases(nodeProps.data).length
                }}</span>
              </div>
            </div>

            <div class="condition-branch-list">
              <div
                v-for="(caseItem, index) in getConditionCases(nodeProps.data)"
                :key="caseItem.id"
                class="condition-branch-row"
              >
                <span class="condition-branch-name"
                  >鍒嗘敮 {{ index + 1 }}</span
                >
                <div class="condition-branch-output">
                  <span class="condition-branch-tag">
                    {{
                      getConditionBranchLabel(
                        index,
                        getConditionCases(nodeProps.data).length
                      )
                    }}
                  </span>
                  <div class="condition-branch-action">
                    <Handle
                      type="source"
                      :id="getConditionHandleId(caseItem.id)"
                      :position="Position.Right"
                      :connectable-start="false"
                      class="loop-inner-handle condition-branch-handle loop-inner-source-handle"
                    />
                    <div
                      class="condition-branch-add-btn"
                      @click.stop.prevent
                      @pointerdown.stop.prevent="
                        onInnerSourceActionPointerDown(
                          $event,
                          nodeProps.id,
                          getConditionHandleId(caseItem.id)
                        )
                      "
                    >
                      <el-icon><Plus /></el-icon>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <Handle
            type="target"
            :position="Position.Left"
            :class="[
              'loop-inner-handle',
              {
                'loop-inner-handle-drop-target':
                  dragHoverTargetId === nodeProps.id,
              },
            ]"
          />
        </div>
      </template>

      <template #edge-default="edgeProps">
        <g class="loop-inner-edge-wrapper">
          <BezierEdge v-bind="edgeProps" />
          <foreignObject
            v-if="edgeProps.sourceX && edgeProps.targetX"
            class="loop-inner-edge-add"
            :x="(edgeProps.sourceX + edgeProps.targetX) / 2 - 12"
            :y="(edgeProps.sourceY + edgeProps.targetY) / 2 - 12"
            width="24"
            height="24"
          >
            <div
              xmlns="http://www.w3.org/1999/xhtml"
              class="loop-inner-edge-add-shell"
            >
              <button
                type="button"
                class="loop-mini-add-btn loop-inner-edge-add-btn loop-mini-no-drag loop-mini-no-pan"
                @click.stop="showAddNodeMenuOnEdge(edgeProps)"
              >
                <el-icon><Plus /></el-icon>
              </button>
            </div>
          </foreignObject>
        </g>
      </template>

      <Background
        :pattern-color="compact ? '#d7dee8' : '#dbe3ef'"
        :gap="compact ? 18 : 20"
      />
    </VueFlow>

    <div
      v-if="addNodeMenuVisible"
      class="loop-inner-add-menu"
      :style="{
        left: `${addNodeMenuPosition.x}px`,
        top: `${addNodeMenuPosition.y}px`,
      }"
    >
      <div class="loop-inner-add-header">
        <span>娣诲姞鑺傜偣</span>
        <button type="button" class="loop-inner-close" @click="hideAddNodeMenu">
          <el-icon><Close /></el-icon>
        </button>
      </div>
      <div class="loop-inner-add-list">
        <button
          v-for="nodeType in innerAddableNodeTypes"
          :key="nodeType.type"
          type="button"
          class="loop-inner-add-item"
          @click="addNodeAndConnect(nodeType.type)"
        >
          <NodeTypeIcon
            class="loop-inner-add-icon"
            :type="nodeType.type"
            :fallback="nodeType.icon"
          />
          <span class="loop-inner-add-text">
            <span class="loop-inner-add-label">{{ nodeType.label }}</span>
            <span class="loop-inner-add-desc">{{ nodeType.description }}</span>
          </span>
        </button>
      </div>
    </div>

    <div
      v-if="addNodeMenuVisible"
      class="loop-inner-add-overlay"
      @click="hideAddNodeMenu"
    ></div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import {
  VueFlow,
  Handle,
  Position,
  ConnectionLineType,
  BezierEdge,
  useVueFlow,
} from "@vue-flow/core";
import { Background } from "@vue-flow/background";
import { Close, Delete, Lock, Plus } from "@element-plus/icons-vue";
import LlmNodeIcon from "./components/LlmNodeIcon.vue";
import NodeTypeIcon from "./components/NodeTypeIcon.vue";
import {
  cloneNodeData as cloneData,
  createStructuredNodeData,
  getNodeConfig,
  getNodeConfigValue,
  getNodeDescription,
  getNodeInput,
  getNodeLabel,
  getNodeOutput,
  mergeNodeConfig,
  normalizeTemplateReferenceSegment,
  omitObjectKeys,
} from "./utils/nodeData";

const props = defineProps({
  workflowData: {
    type: Object,
    default: () => ({}),
  },
  defaultLlmModelConfig: {
    type: Object,
    default: () => ({
      provider: "",
      model: "",
    }),
  },
  compact: {
    type: Boolean,
    default: false,
  },
  loopPath: {
    type: Array,
    default: () => [],
  },
  selectedNodeId: {
    type: String,
    default: null,
  },
});

function normalizeModelProviderValue(value, fallback = "") {
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

const emit = defineEmits([
  "update:workflowData",
  "selectNode",
  "clearSelection",
  "deleteNode",
]);

const flowInstanceId = `loop-workflow-${Date.now()}-${Math.random()
  .toString(36)
  .slice(2, 8)}`;
const { startConnection, updateConnection, endConnection } = useVueFlow({
  id: flowInstanceId,
});

const innerAddableNodeTypes = [
  {
    type: "llm",
    label: "LLM",
    icon: "L",
    description: "AI 对话处理",
  },
  {
    type: "condition",
    label: "条件分支",
    icon: "C",
    description: "条件判断",
  },
  {
    type: "loop",
    label: "循环",
    icon: "O",
    description: "循环执行子流程",
  },
  {
    type: "reply",
    label: "输出",
    icon: "R",
    description: "返回结果",
  },
];

const defaultEdgeOptions = {
  type: "default",
  style: {
    stroke: "#94a3b8",
    strokeWidth: 2,
  },
  markerEnd: {
    type: "arrowclosed",
    width: 10,
    height: 10,
    color: "#94a3b8",
  },
};

const canvasRef = ref(null);
const localNodes = ref([]);
const localEdges = ref([]);
const pendingCommittedNodes = ref(null);
const lastCommittedAt = ref(0);
const committedPositionGuardMs = 1200;
const workspaceMetrics = ref({
  width: props.compact ? 292 : 680,
  height: props.compact ? 118 : 300,
});
const isDraggingInnerNode = ref(false);
const dragHoverTargetId = ref(null);
const addNodeMenuVisible = ref(false);
const addNodeMenuPosition = ref({ x: 0, y: 0 });
const addNodeMenuSourceId = ref(null);
const addNodeMenuSourceHandle = ref(null);
const addNodeMenuTargetId = ref(null);
const addNodeMenuEdgeId = ref(null);
const sourceActionDragThreshold = 6;
const addMenuOpenAt = ref(0);
const addMenuDismissGuardMs = 220;
const LOOP_ADD_MENU_WIDTH = 220;
const LOOP_ADD_MENU_OFFSET_X = 14;
const LOOP_ADD_MENU_OFFSET_Y = -10;
const LOOP_ADD_MENU_MARGIN = 12;
let removeOutsidePointerListener = null;

const LOOP_CANVAS_LAYOUTS = {
  compact: {
    minWidth: 292,
    minHeight: 118,
    paddingTop: 18,
    paddingRight: 26,
    paddingBottom: 18,
    paddingLeft: 18,
    dragInsetX: 12,
    dragInsetY: 12,
  },
  regular: {
    minWidth: 680,
    minHeight: 300,
    paddingTop: 30,
    paddingRight: 52,
    paddingBottom: 34,
    paddingLeft: 34,
    dragInsetX: 20,
    dragInsetY: 20,
  },
};

const layoutConfig = computed(() =>
  props.compact ? LOOP_CANVAS_LAYOUTS.compact : LOOP_CANVAS_LAYOUTS.regular
);

const canvasStyle = computed(() => ({
  width: `${workspaceMetrics.value.width}px`,
  height: `${workspaceMetrics.value.height}px`,
  minWidth: `${workspaceMetrics.value.width}px`,
  minHeight: `${workspaceMetrics.value.height}px`,
}));

const stageStyle = computed(() => ({
  width: `${workspaceMetrics.value.width}px`,
  height: `${workspaceMetrics.value.height}px`,
}));

const isEmptyBody = computed(
  () =>
    localNodes.value.filter((node) => node.type !== "loop-start").length === 0
);

let innerNodeSeed = 0;
let innerEdgeSeed = 0;
let innerConditionCaseSeed = 0;

function markAddMenuOpened() {
  addMenuOpenAt.value = Date.now();
}

function shouldIgnoreAddMenuDismiss() {
  return (
    addNodeMenuVisible.value &&
    Date.now() - addMenuOpenAt.value < addMenuDismissGuardMs
  );
}

function getLoopAddMenuHeight() {
  return 60 + innerAddableNodeTypes.length * 54;
}

function detachOutsidePointerListener() {
  if (removeOutsidePointerListener) {
    removeOutsidePointerListener();
    removeOutsidePointerListener = null;
  }
}

function handleOutsidePointerDown(event) {
  if (!addNodeMenuVisible.value) {
    return;
  }

  const target = event?.target;
  if (!(target instanceof Node)) {
    hideAddNodeMenu();
    return;
  }

  const menuElement = canvasRef.value?.querySelector?.(".loop-inner-add-menu");
  if (menuElement?.contains(target)) {
    return;
  }

  const triggerElement = target?.closest?.(
    ".loop-mini-add-btn, .loop-inner-edge-add"
  );
  if (triggerElement && canvasRef.value?.contains(triggerElement)) {
    return;
  }

  hideAddNodeMenu();
}

watch(
  () => props.workflowData,
  (value) => {
    if (isDraggingInnerNode.value) {
      return;
    }
    const normalized = normalizeLoopWorkflowData(cloneData(value || {}));
    const normalizedLoopNodes = getLoopNodeList(normalized);
    const normalizedLoopEdges = getLoopEdgeList(normalized);
    const protectedNodes = getProtectedCommittedNodes(normalizedLoopNodes);
    if (protectedNodes) {
      const protectedMetrics = syncWorkspaceMetrics(protectedNodes, {
        shrink: true,
      });
      localNodes.value = applyExtentsToNodes(
        cloneData(protectedNodes),
        protectedMetrics
      );
      localEdges.value = normalizedLoopEdges;
      return;
    }
    const nextMetrics = syncWorkspaceMetrics(normalizedLoopNodes, {
      shrink: true,
    });
    localNodes.value = applyExtentsToNodes(normalizedLoopNodes, nextMetrics);
    localEdges.value = normalizedLoopEdges;
  },
  { immediate: true, deep: true }
);

watch(
  () => props.compact,
  () => {
    const nextMetrics = syncWorkspaceMetrics(localNodes.value, {
      shrink: true,
    });
    localNodes.value = applyExtentsToNodes(localNodes.value, nextMetrics);
  }
);

watch(addNodeMenuVisible, (visible) => {
  detachOutsidePointerListener();

  if (!visible) {
    return;
  }

  const onPointerDown = (event) => {
    handleOutsidePointerDown(event);
  };

  document.addEventListener("pointerdown", onPointerDown);
  removeOutsidePointerListener = () => {
    document.removeEventListener("pointerdown", onPointerDown);
  };
});

onBeforeUnmount(() => {
  detachOutsidePointerListener();
});

function getCompactLoopPreviewMetrics(data = {}) {
  const normalized = normalizeLoopWorkflowData(cloneData(data || {}));
  return getCanvasMetricsForNodes(getLoopNodeList(normalized), true);
}

function getRegularLoopNodeStyle(data = {}) {
  const metrics = getCompactLoopPreviewMetrics(data);

  return {
    width: `${metrics.width + 32}px`,
    height: `${metrics.height + 60}px`,
  };
}

function getRegularLoopEditorSurfaceStyle(data = {}) {
  const metrics = getCompactLoopPreviewMetrics(data);

  return {
    width: `${metrics.width}px`,
    height: `${metrics.height}px`,
  };
}

function updateNestedLoopNodeData(nodeId, workflowData) {
  const nextNodes = localNodes.value.map((node) =>
    node.id === nodeId
      ? {
          ...node,
          data: normalizeLoopWorkflowData(cloneData(workflowData || {})),
        }
      : node
  );

  const nextWorkspaceMetrics = syncWorkspaceMetrics(nextNodes, {
    shrink: true,
  });
  localNodes.value = applyExtentsToNodes(nextNodes, nextWorkspaceMetrics);
  emitWorkflowState({ loopNodes: nextNodes });
}

function getInnerNodeSize(node, compactMode = props.compact) {
  if (node?.type === "loop-start" || node?.id === "loop-entry") {
    return compactMode ? { width: 36, height: 36 } : { width: 150, height: 56 };
  }

  if (node?.type === "condition") {
    const caseCount = Math.max(2, getConditionCases(node?.data).length);
    return {
      width: 260,
      height: 93 + caseCount * 32,
    };
  }

  if (node?.type === "reply") {
    const previewCount = Math.min(
      3,
      getLoopReplyPreviewList(node?.data).length
    );
    const overflowCount = getLoopReplyPreviewOverflowCount(node?.data, 3);

    return {
      width: 200,
      height: Math.max(
        112,
        88 + previewCount * 20 + (overflowCount > 0 ? 20 : 0)
      ),
    };
  }

  if (node?.type === "loop") {
    const metrics = getCompactLoopPreviewMetrics(node?.data || {});
    return {
      width: metrics.width + 32,
      height: metrics.height + 60,
    };
  }

  return { width: 200, height: 112 };
}
function getCanvasMetricsForNodes(nodes = [], compactMode = props.compact) {
  const layout = compactMode
    ? LOOP_CANVAS_LAYOUTS.compact
    : LOOP_CANVAS_LAYOUTS.regular;
  const safeNodes =
    Array.isArray(nodes) && nodes.length ? nodes : [createLoopStartNode()];

  let maxRight = layout.paddingLeft;
  let maxBottom = layout.paddingTop;

  safeNodes.forEach((node) => {
    const size = getInnerNodeSize(node, compactMode);
    const positionX = Number(node?.position?.x) || 0;
    const positionY = Number(node?.position?.y) || 0;

    maxRight = Math.max(maxRight, positionX + size.width);
    maxBottom = Math.max(maxBottom, positionY + size.height);
  });

  return {
    layout,
    width: Math.max(layout.minWidth, Math.ceil(maxRight + layout.paddingRight)),
    height: Math.max(
      layout.minHeight,
      Math.ceil(maxBottom + layout.paddingBottom)
    ),
  };
}

function mergeCanvasMetrics(currentMetrics, nextMetrics) {
  if (!currentMetrics) {
    return nextMetrics;
  }

  const layout =
    nextMetrics?.layout || currentMetrics.layout || layoutConfig.value;
  const layoutChanged =
    currentMetrics.layout?.minWidth !== layout.minWidth ||
    currentMetrics.layout?.minHeight !== layout.minHeight;

  if (layoutChanged) {
    return nextMetrics;
  }

  return {
    layout,
    width: Math.max(currentMetrics.width || layout.minWidth, nextMetrics.width),
    height: Math.max(
      currentMetrics.height || layout.minHeight,
      nextMetrics.height
    ),
  };
}

function syncWorkspaceMetrics(nodes = localNodes.value, options = {}) {
  const nextMetrics = getCanvasMetricsForNodes(nodes, props.compact);

  workspaceMetrics.value = options.shrink
    ? nextMetrics
    : mergeCanvasMetrics(workspaceMetrics.value, nextMetrics);

  return workspaceMetrics.value;
}

function applyExtentsToNodes(nodes = [], metrics = workspaceMetrics.value) {
  return (nodes || []).map((node) => {
    if (!node) {
      return node;
    }
    const cleanNode = { ...node };
    delete cleanNode.extent;
    if (cleanNode.type === "loop-start") {
      return cleanNode;
    }
    return {
      ...cleanNode,
      position: clampNodePositionToMetrics(
        cleanNode.position,
        cleanNode,
        metrics
      ),
    };
  });
}

function hasSameNodeStructure(leftNodes = [], rightNodes = []) {
  if (!Array.isArray(leftNodes) || !Array.isArray(rightNodes)) {
    return false;
  }

  if (leftNodes.length !== rightNodes.length) {
    return false;
  }

  return leftNodes.every((node, index) => {
    const otherNode = rightNodes[index];
    return node?.id === otherNode?.id && node?.type === otherNode?.type;
  });
}

function getProtectedCommittedNodes(normalizedNodes = []) {
  if (!pendingCommittedNodes.value) {
    return null;
  }

  if (Date.now() - lastCommittedAt.value > committedPositionGuardMs) {
    pendingCommittedNodes.value = null;
    return null;
  }

  return hasSameNodeStructure(normalizedNodes, pendingCommittedNodes.value)
    ? pendingCommittedNodes.value
    : null;
}

function clampValue(value, min, max) {
  if (!Number.isFinite(value)) {
    return min;
  }

  if (max < min) {
    return min;
  }

  return Math.min(Math.max(value, min), max);
}

function clampNodePositionToMetrics(
  position,
  nodeLike,
  metrics = workspaceMetrics.value
) {
  const layout = metrics?.layout || layoutConfig.value;
  const nodeSize = getInnerNodeSize(nodeLike, props.compact);

  return {
    x: clampValue(
      position?.x ?? layout.dragInsetX,
      layout.dragInsetX,
      metrics.width - layout.dragInsetX - nodeSize.width
    ),
    y: clampValue(
      position?.y ?? layout.dragInsetY,
      layout.dragInsetY,
      metrics.height - layout.dragInsetY - nodeSize.height
    ),
  };
}

function buildConditionCaseId(prefix = "case") {
  innerConditionCaseSeed += 1;
  return `${prefix}-${Date.now()}-${innerConditionCaseSeed}`;
}

function createConditionCase(isElse = false, overrides = {}) {
  return {
    id: buildConditionCaseId(isElse ? "else" : "case"),
    expression: "",
    isElse,
    ...overrides,
  };
}

function normalizeConditionNodeData(data = {}) {
  const rawCases = Array.isArray(getNodeConfigValue(data, "cases", null))
    ? getNodeConfigValue(data, "cases", [])
    : Array.isArray(data.cases)
    ? data.cases
    : [];
  const normalizedCases = rawCases.filter(Boolean).map((item) => ({
    id: item.id || buildConditionCaseId(item.isElse ? "else" : "case"),
    expression: item.expression || "",
    isElse: Boolean(item.isElse),
  }));

  const branchCases = normalizedCases.filter((item) => !item.isElse);
  if (!branchCases.length) {
    branchCases.push(
      createConditionCase(false, {
        expression: getNodeConfigValue(
          data,
          "expression",
          data.expression || ""
        ),
      })
    );
  }

  const elseCase =
    normalizedCases.find((item) => item.isElse) || createConditionCase(true);

  return createStructuredNodeData({
    input: getNodeInput(data),
    config: {
      ...omitObjectKeys(data, [
        "input",
        "config",
        "output",
        "cases",
        "expression",
        "label",
      ]),
      ...getNodeConfig(data),
      label: getNodeLabel(data, "条件分支"),
      cases: [...branchCases, elseCase],
    },
    output: getNodeOutput(data),
  });
}

function getConditionCases(data = {}) {
  return getNodeConfigValue(normalizeConditionNodeData(data), "cases", []);
}

function getConditionHandleId(caseId) {
  return `condition-case-${caseId}`;
}

function getConditionBranchLabel(index, total) {
  if (index === 0) return "濡傛灉";
  if (index === total - 1) return "鍚﹀垯";
  return "鍚﹀垯濡傛灉";
}

function getConditionDefaultSourceHandle(data = {}) {
  const firstCase = getConditionCases(data)[0];
  return firstCase ? getConditionHandleId(firstCase.id) : undefined;
}

function buildInnerNodeId(prefix = "loop-node") {
  innerNodeSeed += 1;
  return normalizeTemplateReferenceSegment(
    `${prefix}_${Date.now()}_${innerNodeSeed}`,
    prefix
  );
}

function buildInnerEdgeId(source, target, sourceHandle, targetHandle) {
  innerEdgeSeed += 1;
  return [
    "loop-edge",
    source,
    sourceHandle || "source",
    target,
    targetHandle || "target",
    Date.now(),
    innerEdgeSeed,
  ].join("-");
}

function getInnerNodeLabel(nodeType, index = 0) {
  const labels = {
    llm: "LLM",
    reply: "输出",
    condition: "鏉′欢鍒嗘敮",
    loop: "寰幆",
  };

  return `${labels[nodeType] || "鑺傜偣"} ${index + 1}`;
}
function createLoopStartNode(overrides = {}) {
  return {
    id: "loop-entry",
    type: "loop-start",
    position: { x: 18, y: 38 },
    draggable: false,
    selectable: false,
    data: createStructuredNodeData({
      config: {
        label: getNodeLabel(overrides.data, "循环开始"),
      },
    }),
    ...overrides,
  };
}
function createDefaultLoopData() {
  return createStructuredNodeData({
    config: {
      label: "循环",
      description: "",
      itemAlias: "item",
      maxIterations: 10,
      loopNodes: [createLoopStartNode()],
      loopEdges: [],
    },
  });
}
function createInnerNodeData(nodeType, index = 0, overrides = {}) {
  if (nodeType === "condition") {
    return normalizeConditionNodeData({
      label: "条件分支",
      ...overrides,
    });
  }

  if (nodeType === "reply") {
    return createStructuredNodeData({
      input: getNodeInput(overrides),
      config: {
        ...omitObjectKeys(overrides, [
          "input",
          "config",
          "output",
          "label",
          "content",
        ]),
        ...getNodeConfig(overrides),
        label: getNodeLabel(overrides, getInnerNodeLabel(nodeType, index)),
        content: getNodeConfigValue(overrides, "content", ""),
      },
      output: getNodeOutput(overrides),
    });
  }

  if (nodeType === "loop") {
    return normalizeLoopWorkflowData(
      mergeNodeConfig(createDefaultLoopData(), {
        ...omitObjectKeys(overrides, ["input", "config", "output", "label"]),
        ...getNodeConfig(overrides),
        label: getNodeLabel(overrides, getInnerNodeLabel(nodeType, index)),
      })
    );
  }

  const defaultModelConfig = props.defaultLlmModelConfig || {};
  return createStructuredNodeData({
    input: getNodeInput(overrides),
    config: {
      ...omitObjectKeys(overrides, [
        "input",
        "config",
        "output",
        "label",
        "provider",
        "model",
        "description",
        "prompt",
      ]),
      ...getNodeConfig(overrides),
      label: getNodeLabel(overrides, getInnerNodeLabel(nodeType, index)),
      provider: normalizeModelProviderValue(
        getNodeConfigValue(
          overrides,
          "provider",
          defaultModelConfig?.provider ?? ""
        ),
        defaultModelConfig?.provider ?? ""
      ),
      model: getNodeConfigValue(
        overrides,
        "model",
        `${defaultModelConfig?.model || ""}`.trim()
      ),
      description: getNodeDescription(overrides),
      prompt: getNodeConfigValue(overrides, "prompt", ""),
    },
    output: getNodeOutput(overrides),
  });
}
function normalizeInnerNode(node, index = 0) {
  if (!node || node.type === "loop-start" || node.id === "loop-entry") {
    return createLoopStartNode({
      ...node,
      id: "loop-entry",
      type: "loop-start",
      position: node?.position || { x: 18, y: 38 },
      data: createStructuredNodeData({
        config: {
          label: getNodeLabel(node?.data, "循环开始"),
        },
      }),
    });
  }

  const baseNode = {
    id: node.id || buildInnerNodeId(node.type || "llm"),
    type: node.type || "llm",
    position: node.position || { x: 112 + index * 192, y: 32 },
    draggable: true,
    selectable: true,
    data: node.data || {},
  };

  if (baseNode.type === "condition") {
    return {
      ...baseNode,
      data: normalizeConditionNodeData(baseNode.data),
    };
  }

  if (baseNode.type === "loop") {
    return {
      ...baseNode,
      data: normalizeLoopWorkflowData(baseNode.data),
    };
  }

  if (baseNode.type === "reply") {
    return {
      ...baseNode,
      data: createInnerNodeData("reply", index, baseNode.data),
    };
  }

  return {
    ...baseNode,
    data: createInnerNodeData("llm", index, baseNode.data),
  };
}
function createDefaultEdge(connection) {
  return {
    ...connection,
    id:
      connection.id ||
      buildInnerEdgeId(
        connection.source,
        connection.target,
        connection.sourceHandle,
        connection.targetHandle
      ),
    type: "default",
  };
}

function buildMigratedLoopNodes(steps = []) {
  return [
    createLoopStartNode(),
    ...steps.map((step, index) =>
      normalizeInnerNode(
        {
          id: buildInnerNodeId(step.type || "llm"),
          type: step.type || "llm",
          position: { x: 112 + index * 192, y: 32 },
          data: createInnerNodeData(step.type || "llm", index, {
            label: step.label || getInnerNodeLabel(step.type || "llm", index),
          }),
        },
        index
      )
    ),
  ];
}
function buildSequentialEdgesFromNodes(nodes) {
  const sequence = nodes.filter(Boolean);
  const nextEdges = [];

  for (let index = 0; index < sequence.length - 1; index += 1) {
    const currentNode = sequence[index];
    const nextNode = sequence[index + 1];
    const connection = {
      source: currentNode.id,
      target: nextNode.id,
    };

    if (currentNode.type === "condition") {
      connection.sourceHandle = getConditionDefaultSourceHandle(
        currentNode.data
      );
    }

    nextEdges.push(createDefaultEdge(connection));
  }

  return nextEdges;
}

function normalizeLoopWorkflowData(data = {}) {
  let nextNodes = Array.isArray(getNodeConfigValue(data, "loopNodes", null))
    ? cloneData(getNodeConfigValue(data, "loopNodes", []))
    : Array.isArray(data.loopNodes)
    ? cloneData(data.loopNodes)
    : [];
  let nextEdges = Array.isArray(getNodeConfigValue(data, "loopEdges", null))
    ? cloneData(getNodeConfigValue(data, "loopEdges", []))
    : Array.isArray(data.loopEdges)
    ? cloneData(data.loopEdges)
    : [];
  const legacySteps = Array.isArray(getNodeConfigValue(data, "steps", null))
    ? getNodeConfigValue(data, "steps", [])
    : Array.isArray(data.steps)
    ? data.steps
    : [];

  if (!nextNodes.length && legacySteps.length) {
    nextNodes = buildMigratedLoopNodes(legacySteps);
    nextEdges = buildSequentialEdgesFromNodes(nextNodes);
  }

  const entryNode = nextNodes.find(
    (node) => node?.type === "loop-start" || node?.id === "loop-entry"
  );
  const otherNodes = nextNodes
    .filter((node) => node && node.id !== (entryNode?.id || "loop-entry"))
    .map((node, index) => normalizeInnerNode(node, index));

  const normalizedNodes = [normalizeInnerNode(entryNode, 0), ...otherNodes];
  const validNodeIds = new Set(normalizedNodes.map((node) => node.id));
  const normalizedEdges = nextEdges
    .filter(
      (edge) =>
        edge && validNodeIds.has(edge.source) && validNodeIds.has(edge.target)
    )
    .map((edge) => createDefaultEdge(edge));
  const maxIterations = Number(getNodeConfigValue(data, "maxIterations", 10));

  return createStructuredNodeData({
    input: getNodeInput(data),
    config: {
      ...getNodeConfig(createDefaultLoopData()),
      ...omitObjectKeys(data, [
        "input",
        "config",
        "output",
        "steps",
        "loopNodes",
        "loopEdges",
        "label",
        "description",
        "itemAlias",
        "maxIterations",
      ]),
      ...getNodeConfig(data),
      label: getNodeLabel(data, "循环"),
      description: getNodeDescription(data),
      itemAlias: getNodeConfigValue(data, "itemAlias", "item") || "item",
      maxIterations:
        Number.isFinite(maxIterations) && maxIterations > 0
          ? maxIterations
          : 10,
      loopNodes: normalizedNodes,
      loopEdges: normalizedEdges,
    },
    output: getNodeOutput(data),
  });
}

function getLoopNodeList(data = {}) {
  return getNodeConfigValue(normalizeLoopWorkflowData(data), "loopNodes", []);
}

function getLoopEdgeList(data = {}) {
  return getNodeConfigValue(normalizeLoopWorkflowData(data), "loopEdges", []);
}

function emitWorkflowState(overrides = {}) {
  const normalized = normalizeLoopWorkflowData({
    ...cloneData(props.workflowData || {}),
    loopNodes: cloneData(localNodes.value),
    loopEdges: cloneData(localEdges.value),
    ...overrides,
  });

  emit("update:workflowData", normalized);
}

function emitInnerNodeSelection(node) {
  if (!node || node.type === "loop-start") {
    return;
  }

  emit("selectNode", {
    id: node.id,
    type: node.type,
    data: cloneData(node.data),
    loopPath: cloneData(props.loopPath),
  });
}

function emitDeleteNode(nodeId = "") {
  const normalizedNodeId = `${nodeId || ""}`.trim();

  if (!normalizedNodeId || normalizedNodeId === "loop-entry") {
    return;
  }

  emit("deleteNode", {
    id: normalizedNodeId,
    loopPath: cloneData(props.loopPath),
  });
}

function onInnerNodeClick({ node }) {
  hideAddNodeMenu();
  emitInnerNodeSelection(node);
}

function updateInnerNodePosition(nodeId, position) {
  localNodes.value = localNodes.value.map((item) => {
    if (item.id !== nodeId) {
      return item;
    }
    const cleanNode = { ...item };
    delete cleanNode.extent;
    return {
      ...cleanNode,
      position,
    };
  });
}

function getWorkspaceScale() {
  const bounds = getFlowBoundsElement()?.getBoundingClientRect?.();
  const width = workspaceMetrics.value?.width || 1;
  const height = workspaceMetrics.value?.height || 1;

  return {
    x: bounds?.width ? bounds.width / width : 1,
    y: bounds?.height ? bounds.height / height : 1,
  };
}

function onInnerNodePointerDown(event, nodeId) {
  if (event.button !== 0) {
    return;
  }

  const target = event.target;
  if (
    target instanceof Element &&
    target.closest(
      ".loop-mini-no-drag, .vue-flow__handle, .loop-mini-add-btn, .node-add-btn, .node-delete-btn, .condition-branch-add-btn"
    )
  ) {
    return;
  }

  const node = localNodes.value.find((item) => item.id === nodeId);
  if (!node || node.type === "loop-start") {
    return;
  }

  hideAddNodeMenu();
  isDraggingInnerNode.value = true;

  const startClientX = event.clientX;
  const startClientY = event.clientY;
  const startPosition = { ...node.position };
  const scale = getWorkspaceScale();
  const moveThreshold = 3;
  let hasMoved = false;

  const cleanup = () => {
    document.removeEventListener("pointermove", onPointerMove);
    document.removeEventListener("pointerup", onPointerUp);
    document.removeEventListener("pointercancel", onPointerCancel);
  };

  const onPointerMove = (moveEvent) => {
    const deltaX = (moveEvent.clientX - startClientX) / (scale.x || 1);
    const deltaY = (moveEvent.clientY - startClientY) / (scale.y || 1);

    if (!hasMoved) {
      const distance = Math.hypot(
        moveEvent.clientX - startClientX,
        moveEvent.clientY - startClientY
      );

      if (distance < moveThreshold) {
        return;
      }

      hasMoved = true;
    }

    const nextPosition = clampNodePositionToMetrics(
      {
        x: startPosition.x + deltaX,
        y: startPosition.y + deltaY,
      },
      node
    );

    updateInnerNodePosition(nodeId, nextPosition);
  };

  const onPointerUp = () => {
    cleanup();
    const latestNode =
      localNodes.value.find((item) => item.id === nodeId) || node;
    if (!hasMoved) {
      isDraggingInnerNode.value = false;
      emitInnerNodeSelection(latestNode);
      return;
    }
    const committedNodes = cloneData(localNodes.value);
    pendingCommittedNodes.value = cloneData(committedNodes);
    lastCommittedAt.value = Date.now();
    const committedMetrics = syncWorkspaceMetrics(committedNodes, {
      shrink: true,
    });
    localNodes.value = applyExtentsToNodes(
      cloneData(committedNodes),
      committedMetrics
    );
    isDraggingInnerNode.value = false;
    emitWorkflowState({ loopNodes: committedNodes });
  };

  const onPointerCancel = () => {
    cleanup();
    isDraggingInnerNode.value = false;
  };

  document.addEventListener("pointermove", onPointerMove);
  document.addEventListener("pointerup", onPointerUp);
  document.addEventListener("pointercancel", onPointerCancel);
}

function hideAddNodeMenu() {
  addNodeMenuVisible.value = false;
  addNodeMenuSourceId.value = null;
  addNodeMenuSourceHandle.value = null;
  addNodeMenuTargetId.value = null;
  addNodeMenuEdgeId.value = null;
}

function getLocalMenuPosition(clientX, clientY) {
  const rect = canvasRef.value?.getBoundingClientRect();
  if (!rect) {
    return { x: 0, y: 0 };
  }

  const menuHeight = getLoopAddMenuHeight();
  const localPreferredX = clientX - rect.left + LOOP_ADD_MENU_OFFSET_X;
  const localPreferredY = clientY - rect.top + LOOP_ADD_MENU_OFFSET_Y;
  const maxLocalX = Math.max(
    LOOP_ADD_MENU_MARGIN,
    rect.width - LOOP_ADD_MENU_WIDTH - LOOP_ADD_MENU_MARGIN
  );
  const maxLocalY = Math.max(
    LOOP_ADD_MENU_MARGIN,
    rect.height - menuHeight - LOOP_ADD_MENU_MARGIN
  );

  return {
    x: Math.round(clampValue(localPreferredX, LOOP_ADD_MENU_MARGIN, maxLocalX)),
    y: Math.round(clampValue(localPreferredY, LOOP_ADD_MENU_MARGIN, maxLocalY)),
  };
}

function getFlowBoundsElement() {
  return (
    canvasRef.value?.querySelector?.(".loop-workflow-stage") || canvasRef.value
  );
}

function getPointerPositionInFlow(clientX, clientY) {
  const bounds = getFlowBoundsElement()?.getBoundingClientRect?.();
  if (!bounds) {
    return null;
  }

  return {
    x: clientX - bounds.left,
    y: clientY - bounds.top,
  };
}

function getTargetHandlePreviewFromPointer(clientX, clientY, sourceNodeId) {
  const element = document.elementFromPoint(clientX, clientY);
  const nodeElement = element?.closest?.(".vue-flow__node");

  if (!nodeElement || !canvasRef.value?.contains(nodeElement)) {
    return null;
  }

  const nodeId = nodeElement.getAttribute("data-id");
  if (!nodeId || nodeId === sourceNodeId) {
    return null;
  }

  const targetHandleElement = nodeElement.querySelector(
    ".vue-flow__handle.target"
  );
  const targetBounds =
    targetHandleElement?.getBoundingClientRect?.() ||
    nodeElement.getBoundingClientRect?.();

  if (!targetBounds) {
    return null;
  }

  const targetPosition = getPointerPositionInFlow(
    targetBounds.left + targetBounds.width / 2,
    targetBounds.top + targetBounds.height / 2
  );

  if (!targetPosition) {
    return null;
  }

  const targetHandleId =
    targetHandleElement?.getAttribute?.("data-handleid") || undefined;
  const targetHandlePosition =
    targetHandleElement?.getAttribute?.("data-handlepos") || Position.Left;

  return {
    nodeId,
    position: targetPosition,
    handle: {
      nodeId,
      type: "target",
      ...(targetHandleId ? { id: targetHandleId } : {}),
      position: targetHandlePosition,
      ...targetPosition,
    },
  };
}

function onInnerSourceActionPointerDown(event, nodeId, sourceHandleId = null) {
  if (event.button !== 0) {
    return;
  }

  hideAddNodeMenu();
  const startPosition = getPointerPositionInFlow(event.clientX, event.clientY);
  if (!startPosition) {
    return;
  }

  const startHandle = {
    nodeId,
    type: "source",
    id: sourceHandleId || null,
    position: Position.Right,
    ...startPosition,
  };

  const startClientX = event.clientX;
  const startClientY = event.clientY;
  let hasMoved = false;

  const cleanup = () => {
    document.removeEventListener("pointermove", onPointerMove);
    document.removeEventListener("pointerup", onPointerUp);
    document.removeEventListener("pointercancel", onPointerCancel);
  };

  const onPointerMove = (moveEvent) => {
    const currentPosition = getPointerPositionInFlow(
      moveEvent.clientX,
      moveEvent.clientY
    );
    if (!currentPosition) {
      dragHoverTargetId.value = null;
      return;
    }

    if (!hasMoved) {
      const distance = Math.hypot(
        moveEvent.clientX - startClientX,
        moveEvent.clientY - startClientY
      );

      if (distance < sourceActionDragThreshold) {
        return;
      }

      hasMoved = true;
      startConnection(startHandle, startPosition);
    }

    const targetPreview = getTargetHandlePreviewFromPointer(
      moveEvent.clientX,
      moveEvent.clientY,
      nodeId
    );

    dragHoverTargetId.value = targetPreview?.nodeId || null;

    if (targetPreview) {
      updateConnection(targetPreview.position, targetPreview.handle, "valid");
      return;
    }

    updateConnection(currentPosition, null, null);
  };

  const onPointerUp = (upEvent) => {
    cleanup();

    if (hasMoved) {
      const targetPreview = getTargetHandlePreviewFromPointer(
        upEvent.clientX,
        upEvent.clientY,
        nodeId
      );

      dragHoverTargetId.value = null;

      if (targetPreview) {
        onInnerConnect({
          source: nodeId,
          sourceHandle: sourceHandleId || undefined,
          target: targetPreview.nodeId,
          targetHandle: targetPreview.handle.id || undefined,
        });
      }

      endConnection(upEvent);
      return;
    }

    dragHoverTargetId.value = null;
    showAddNodeMenuFromNode(nodeId, upEvent, sourceHandleId);
  };

  const onPointerCancel = () => {
    cleanup();
    dragHoverTargetId.value = null;

    if (hasMoved) {
      endConnection();
    }
  };

  document.addEventListener("pointermove", onPointerMove);
  document.addEventListener("pointerup", onPointerUp);
  document.addEventListener("pointercancel", onPointerCancel);
}

function showAddNodeMenuFromNode(nodeId, event, sourceHandleId = null) {
  hideAddNodeMenu();
  addNodeMenuSourceId.value = nodeId;
  addNodeMenuSourceHandle.value = sourceHandleId;
  addNodeMenuTargetId.value = null;
  addNodeMenuEdgeId.value = null;
  addNodeMenuPosition.value = getLocalMenuPosition(
    event.clientX,
    event.clientY
  );
  addNodeMenuVisible.value = true;
  markAddMenuOpened();
}

function showAddNodeMenuOnEdge(edgeProps) {
  hideAddNodeMenu();
  addNodeMenuSourceId.value = edgeProps.source;
  addNodeMenuSourceHandle.value = edgeProps.sourceHandle || null;
  addNodeMenuTargetId.value = edgeProps.target;
  addNodeMenuEdgeId.value = edgeProps.id;

  const canvasBounds = canvasRef.value?.getBoundingClientRect();
  if (canvasBounds) {
    const centerX =
      canvasBounds.left + (edgeProps.sourceX + edgeProps.targetX) / 2;
    const centerY =
      canvasBounds.top + (edgeProps.sourceY + edgeProps.targetY) / 2;
    addNodeMenuPosition.value = getLocalMenuPosition(centerX, centerY);
  } else {
    addNodeMenuPosition.value = {
      x: (edgeProps.sourceX + edgeProps.targetX) / 2 + 18,
      y: (edgeProps.sourceY + edgeProps.targetY) / 2,
    };
  }

  addNodeMenuVisible.value = true;
  markAddMenuOpened();
}

function onPaneClick() {
  if (shouldIgnoreAddMenuDismiss()) {
    return;
  }

  hideAddNodeMenu();
  emit("clearSelection", cloneData(props.loopPath));
}

function getInnerNodeBox(
  nodeLike = {},
  position = nodeLike?.position || { x: 0, y: 0 }
) {
  const size = getInnerNodeSize(nodeLike, props.compact);

  return {
    left: Number(position?.x) || 0,
    top: Number(position?.y) || 0,
    width: size.width,
    height: size.height,
    right: (Number(position?.x) || 0) + size.width,
    bottom: (Number(position?.y) || 0) + size.height,
  };
}

function getInnerInsertSpacingX(sourceNode = {}) {
  const isEntryNode =
    sourceNode.type === "loop-start" || sourceNode.id === "loop-entry";

  return props.compact ? (isEntryNode ? 110 : 72) : isEntryNode ? 136 : 76;
}

function getInnerEdgeBasePosition(sourceNode, targetNode, candidateNode) {
  const sourceBox = getInnerNodeBox(sourceNode);
  const targetBox = getInnerNodeBox(targetNode);
  const candidateSize = getInnerNodeSize(candidateNode, props.compact);
  const gapX = getInnerInsertSpacingX(sourceNode);
  const minX = sourceBox.right + gapX;
  const maxX = targetBox.left - candidateSize.width - gapX;
  const centeredX = Math.round(
    (sourceBox.right + targetBox.left - candidateSize.width) / 2
  );
  const sourceCenterY = sourceBox.top + sourceBox.height / 2;
  const targetCenterY = targetBox.top + targetBox.height / 2;

  return {
    x: maxX >= minX ? Math.max(minX, Math.min(centeredX, maxX)) : minX,
    y: Math.round(
      (sourceCenterY + targetCenterY) / 2 - candidateSize.height / 2
    ),
  };
}

function isInnerBoxOverlapping(boxA, boxB, gapX = 40, gapY = 32) {
  return (
    boxA.left < boxB.right + gapX &&
    boxA.right + gapX > boxB.left &&
    boxA.top < boxB.bottom + gapY &&
    boxA.bottom + gapY > boxB.top
  );
}

function collectInnerDownstreamNodeIds(
  edgeList = localEdges.value,
  startNodeIds = []
) {
  const visited = new Set();
  const queue = Array.isArray(startNodeIds)
    ? startNodeIds.filter(Boolean)
    : [startNodeIds].filter(Boolean);

  while (queue.length) {
    const currentId = queue.shift();
    if (!currentId || visited.has(currentId)) {
      continue;
    }

    visited.add(currentId);
    edgeList.forEach((edge) => {
      if (
        edge.source === currentId &&
        edge.target &&
        !visited.has(edge.target)
      ) {
        queue.push(edge.target);
      }
    });
  }

  return visited;
}

function shiftInnerNodesHorizontally(
  nodeList = localNodes.value,
  nodeIds = new Set(),
  deltaX = 0
) {
  if (!deltaX) {
    return cloneData(nodeList);
  }

  return cloneData(nodeList).map((node) => {
    if (!nodeIds.has(node.id) || node.type === "loop-start") {
      return node;
    }

    return {
      ...node,
      position: {
        ...node.position,
        x: node.position.x + deltaX,
      },
    };
  });
}

function makeInnerInsertionRoom(
  nodeList = localNodes.value,
  edgeList = localEdges.value,
  candidateNode,
  candidatePosition,
  options = {}
) {
  let nextNodes = cloneData(nodeList);
  const excludedIds = new Set((options.excludeIds || []).filter(Boolean));
  const candidateBox = () => getInnerNodeBox(candidateNode, candidatePosition);

  if (options.targetNodeId) {
    const targetNode = nextNodes.find(
      (node) => node.id === options.targetNodeId
    );
    if (targetNode) {
      const targetBox = getInnerNodeBox(targetNode);
      const requiredShift = Math.ceil(
        candidateBox().right + 40 - targetBox.left
      );

      if (requiredShift > 0) {
        const downstreamNodeIds = collectInnerDownstreamNodeIds(edgeList, [
          targetNode.id,
        ]);
        downstreamNodeIds.add(targetNode.id);
        nextNodes = shiftInnerNodesHorizontally(
          nextNodes,
          downstreamNodeIds,
          requiredShift
        );
      }
    }
  }

  let attempt = 0;
  while (attempt < 14) {
    const blockingNode = nextNodes.find((node) => {
      if (!node || node.type === "loop-start" || excludedIds.has(node.id)) {
        return false;
      }

      return isInnerBoxOverlapping(candidateBox(), getInnerNodeBox(node));
    });

    if (!blockingNode) {
      break;
    }

    const blockingBox = getInnerNodeBox(blockingNode);
    const requiredShift = Math.max(
      40,
      Math.ceil(candidateBox().right + 40 - blockingBox.left)
    );
    const downstreamNodeIds = collectInnerDownstreamNodeIds(edgeList, [
      blockingNode.id,
    ]);
    downstreamNodeIds.add(blockingNode.id);
    nextNodes = shiftInnerNodesHorizontally(
      nextNodes,
      downstreamNodeIds,
      requiredShift
    );
    attempt += 1;
  }

  return nextNodes;
}

function adjustPositionToAvoidOverlap(
  position,
  candidateNode,
  excludeId = null,
  metrics = workspaceMetrics.value,
  nodeList = localNodes.value,
  excludeIds = []
) {
  const candidateSize = getInnerNodeSize(candidateNode, props.compact);
  const excludedIdSet = new Set([excludeId, ...excludeIds].filter(Boolean));
  let nextPosition = clampNodePositionToMetrics(
    position,
    candidateNode,
    metrics
  );
  let attempt = 0;

  while (attempt < 14) {
    const candidateBox = {
      left: nextPosition.x,
      top: nextPosition.y,
      right: nextPosition.x + candidateSize.width,
      bottom: nextPosition.y + candidateSize.height,
    };
    const hasOverlap = nodeList.some((node) => {
      if (!node || node.type === "loop-start" || excludedIdSet.has(node.id)) {
        return false;
      }

      return isInnerBoxOverlapping(candidateBox, getInnerNodeBox(node));
    });

    if (!hasOverlap) {
      return nextPosition;
    }

    attempt += 1;
    nextPosition = clampNodePositionToMetrics(
      {
        x:
          attempt % 4 === 0
            ? nextPosition.x + Math.round((props.compact ? 40 : 52) * 1.5)
            : nextPosition.x,
        y: nextPosition.y + (props.compact ? 72 : 88),
      },
      candidateNode,
      metrics
    );
  }

  return nextPosition;
}

function getInnerOutgoingTargetNodes(
  sourceNodeId,
  sourceHandleId = null,
  nodeList = localNodes.value,
  edgeList = localEdges.value
) {
  const normalizedHandleId = sourceHandleId || null;

  return edgeList
    .filter(
      (edge) =>
        edge.source === sourceNodeId &&
        (edge.sourceHandle || null) === normalizedHandleId &&
        edge.target
    )
    .map((edge) => nodeList.find((node) => node.id === edge.target))
    .filter(Boolean);
}

function getInnerSiblingBasePosition(
  sourceNode,
  candidateNode,
  sourceHandleId = null,
  nodeList = localNodes.value,
  edgeList = localEdges.value
) {
  const siblingTargets = getInnerOutgoingTargetNodes(
    sourceNode.id,
    sourceHandleId,
    nodeList,
    edgeList
  );

  if (!siblingTargets.length) {
    return null;
  }

  const anchorNode = [...siblingTargets].sort(
    (left, right) =>
      left.position.x - right.position.x || left.position.y - right.position.y
  )[0];
  const lowestNode = siblingTargets.reduce((currentLowest, node) => {
    if (!currentLowest) {
      return node;
    }

    return getInnerNodeBox(node).bottom > getInnerNodeBox(currentLowest).bottom
      ? node
      : currentLowest;
  }, null);
  const siblingGapY = props.compact ? 72 : 88;

  return {
    x: anchorNode.position.x,
    y: getInnerNodeBox(lowestNode).bottom + siblingGapY,
  };
}

function getSequentialBasePosition(
  sourceNode,
  candidateNode,
  sourceHandleId = null
) {
  const sourceSize = getInnerNodeSize(sourceNode, props.compact);
  const candidateSize = getInnerNodeSize(candidateNode, props.compact);
  const alignTop =
    sourceHandleId || sourceNode.type === "condition"
      ? sourceNode.position.y
      : sourceNode.position.y +
        Math.round((sourceSize.height - candidateSize.height) / 2);

  return {
    x:
      sourceNode.position.x +
      sourceSize.width +
      getInnerInsertSpacingX(sourceNode),
    y: alignTop,
  };
}

function addNodeAndConnect(nodeType) {
  if (!addNodeMenuSourceId.value) {
    return;
  }

  const isFromEdge = Boolean(
    addNodeMenuEdgeId.value && addNodeMenuTargetId.value
  );
  const sourceNode = localNodes.value.find(
    (node) => node.id === addNodeMenuSourceId.value
  );
  if (!sourceNode) {
    hideAddNodeMenu();
    return;
  }

  const targetNode = isFromEdge
    ? localNodes.value.find((node) => node.id === addNodeMenuTargetId.value)
    : null;
  const bodyNodeCount = localNodes.value.filter(
    (node) => node.type !== "loop-start"
  ).length;
  const newNodeId = buildInnerNodeId(nodeType);

  let newNode = normalizeInnerNode(
    {
      id: newNodeId,
      type: nodeType,
      position: { x: 0, y: 0 },
      data: createInnerNodeData(nodeType, bodyNodeCount),
    },
    bodyNodeCount
  );

  const hasExistingOutgoingTargets =
    !isFromEdge &&
    getInnerOutgoingTargetNodes(
      sourceNode.id,
      addNodeMenuSourceHandle.value,
      localNodes.value,
      localEdges.value
    ).length > 0;
  const basePosition =
    isFromEdge && targetNode
      ? getInnerEdgeBasePosition(sourceNode, targetNode, newNode)
      : hasExistingOutgoingTargets
      ? getInnerSiblingBasePosition(
          sourceNode,
          newNode,
          addNodeMenuSourceHandle.value,
          localNodes.value,
          localEdges.value
        ) ||
        getSequentialBasePosition(
          sourceNode,
          newNode,
          addNodeMenuSourceHandle.value
        )
      : getSequentialBasePosition(
          sourceNode,
          newNode,
          addNodeMenuSourceHandle.value
        );
  const shiftedNodes = isFromEdge
    ? makeInnerInsertionRoom(
        cloneData(localNodes.value),
        cloneData(localEdges.value),
        newNode,
        basePosition,
        {
          excludeIds: [sourceNode.id],
          targetNodeId: targetNode?.id,
        }
      )
    : cloneData(localNodes.value);
  const placementMetrics = mergeCanvasMetrics(
    workspaceMetrics.value,
    getCanvasMetricsForNodes(
      [...shiftedNodes, { ...newNode, position: basePosition }],
      props.compact
    )
  );
  const nextPosition = adjustPositionToAvoidOverlap(
    basePosition,
    newNode,
    newNodeId,
    placementMetrics,
    shiftedNodes,
    [sourceNode.id]
  );

  newNode = {
    ...newNode,
    position: nextPosition,
  };

  const nextNodes = [...shiftedNodes, newNode];
  let nextEdges = [...localEdges.value];

  if (isFromEdge) {
    const originalEdge = nextEdges.find(
      (edge) => edge.id === addNodeMenuEdgeId.value
    );
    nextEdges = nextEdges.filter((edge) => edge.id !== addNodeMenuEdgeId.value);

    nextEdges.push(
      createDefaultEdge({
        source: addNodeMenuSourceId.value,
        sourceHandle:
          originalEdge?.sourceHandle ||
          addNodeMenuSourceHandle.value ||
          undefined,
        target: newNodeId,
      })
    );

    const outgoingEdge = {
      source: newNodeId,
      target: addNodeMenuTargetId.value,
      targetHandle: originalEdge?.targetHandle || undefined,
    };

    if (nodeType === "condition") {
      outgoingEdge.sourceHandle = getConditionDefaultSourceHandle(newNode.data);
    }

    nextEdges.push(createDefaultEdge(outgoingEdge));
  } else {
    nextEdges.push(
      createDefaultEdge({
        source: addNodeMenuSourceId.value,
        sourceHandle: addNodeMenuSourceHandle.value || undefined,
        target: newNodeId,
      })
    );
  }

  const nextWorkspaceMetrics = syncWorkspaceMetrics(nextNodes);
  localNodes.value = applyExtentsToNodes(nextNodes, nextWorkspaceMetrics);
  localEdges.value = nextEdges;
  emitWorkflowState();
  hideAddNodeMenu();
}

function onInnerConnect(connection) {
  if (!connection.source || !connection.target) {
    return;
  }

  const sourceNode = localNodes.value.find(
    (node) => node.id === connection.source
  );
  if (sourceNode?.type === "condition" && !connection.sourceHandle) {
    ElMessage.warning("条件分支需要从对应的分支连接点发起连线");
    return;
  }

  const duplicatedEdge = localEdges.value.find(
    (edge) =>
      edge.source === connection.source &&
      edge.target === connection.target &&
      (edge.sourceHandle || null) === (connection.sourceHandle || null) &&
      (edge.targetHandle || null) === (connection.targetHandle || null)
  );

  if (duplicatedEdge) {
    return;
  }

  localEdges.value = [...localEdges.value, createDefaultEdge(connection)];
  emitWorkflowState();
}

function getLoopReplyOutputs(data = {}) {
  const structuredOutput = getNodeOutput(data);

  if (structuredOutput.length) {
    return structuredOutput.filter(Boolean);
  }

  const rawOutputs = Array.isArray(data.outputs)
    ? data.outputs
    : Array.isArray(data.outputVariables)
    ? data.outputVariables
    : [];

  return rawOutputs.filter(Boolean);
}

function getLoopReplyPreviewList(data = {}, limit = null) {
  let previewList = [];
  const replyOutputs = getLoopReplyOutputs(data);

  if (replyOutputs.length) {
    previewList = replyOutputs
      .map((output) => {
        const variableKey = `${
          output.variableLabel || output.variableKey || ""
        }`.trim();

        if (variableKey) {
          return variableKey;
        }

        return `${output.name || ""}`.trim();
      })
      .filter(Boolean);
  } else {
    const legacyContent = `${
      getNodeConfigValue(data, "content", "") || ""
    }`.trim();
    previewList = legacyContent ? [`@${legacyContent}`] : [];
  }

  if (Number.isFinite(limit)) {
    return previewList.slice(0, Math.max(0, Math.floor(limit)));
  }

  return previewList;
}

function getLoopReplyPreviewOverflowCount(data = {}, limit = 3) {
  return Math.max(0, getLoopReplyPreviewList(data).length - limit);
}
</script>

<style scoped lang="scss">
.loop-workflow-canvas {
  position: relative;
  display: inline-block;
  box-sizing: border-box;
  border: 1px solid #e6edf6;
  border-radius: 16px;
  overflow: visible;
  background: linear-gradient(180deg, #f9fbfe 0%, #f5f8fc 100%);

  &.compact {
    border: none;
    border-radius: 12px;
    background: transparent;
  }

  &.is-empty .loop-mini-start .loop-mini-add-btn {
    opacity: 1;
  }
}

.loop-workflow-stage {
  position: relative;
  display: block;
  overflow: hidden;
  border-radius: inherit;
}

.custom-node {
  min-width: 200px;
  max-width: 280px;
  background-color: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
  position: relative;
  user-select: none;
  touch-action: none;

  &.selected {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  }

  .node-top {
    padding: 6px 12px;
    border-bottom: 1px solid #f3f4f6;

    .node-type-label {
      font-size: 11px;
      font-weight: 500;
      color: #6b7280;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
  }

  .node-body {
    padding: 12px;
    display: flex;
    flex-direction: column;
    gap: 8px;

    .node-icon-wrapper {
      width: 32px;
      height: 32px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 4px;

      .node-svg {
        width: 18px;
        height: 18px;
      }
    }

    .node-header {
      display: flex;
      align-items: center;
      gap: 10px;

      .node-icon-wrapper {
        margin-bottom: 0;
        flex-shrink: 0;
      }

      .node-title {
        min-width: 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .node-title {
      font-size: 14px;
      font-weight: 600;
      color: #111827;
    }

    .node-subtitle {
      font-size: 12px;
      color: #6b7280;
      background-color: #f3f4f6;
      padding: 4px 8px;
      border-radius: 4px;
      display: inline-block;
    }

    .node-preview {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: 4px;
      font-size: 12px;
      color: #6b7280;

      .preview-tag {
        display: inline-flex;
        max-width: 100%;
        background-color: #eff6ff;
        color: #3b82f6;
        padding: 2px 6px;
        border-radius: 4px;
        font-size: 11px;
      }
    }
  }

  .node-add-btn {
    position: absolute;
    right: -10px;
    top: 50%;
    transform: translateY(-50%);
    width: 20px;
    height: 20px;
    background-color: #3b82f6;
    color: #fff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    opacity: 0;
    transition: all 0.2s;
    z-index: 10;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    font-size: 14px;
    font-weight: 300;
    user-select: none;
    touch-action: none;

    &:hover {
      background-color: #2563eb;
      transform: translateY(-50%) scale(1.1);
    }
  }

  &:hover .node-add-btn {
    opacity: 1;
  }

  .node-delete-btn {
    position: absolute;
    top: 8px;
    right: 8px;
    width: 20px;
    height: 20px;
    padding: 0;
    border: none;
    background-color: transparent;
    color: #ef4444;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    opacity: 0;
    transition: opacity 0.2s ease;
    z-index: 12;

    &:hover {
      color: #ef4444;
      background-color: transparent;
    }
  }

  &:hover .node-delete-btn {
    opacity: 1;
  }
}

.loop-inner-drag-handle {
  cursor: grab;
}

.loop-inner-drag-handle:active {
  cursor: grabbing;
}

.llm-node {
  .node-icon-wrapper {
    background-color: #dbeafe;
  }

  .llm-node-svg {
    width: 20px;
    height: 20px;
  }
}

.reply-node {
  .node-icon-wrapper {
    background-color: #ffedd5;
    color: #f97316;
  }

  .reply-node-preview-more {
    font-size: 11px;
    color: #6b7280;
  }
}

.condition-node {
  min-width: 260px;

  .node-icon-wrapper {
    background-color: #fef3c7;
    color: #f59e0b;
    margin-bottom: 0;
  }

  .node-body {
    gap: 12px;
  }

  .condition-summary {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .condition-title-row {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .condition-count {
    min-width: 20px;
    height: 20px;
    padding: 0 6px;
    border-radius: 999px;
    background-color: #eff6ff;
    color: #2563eb;
    font-size: 12px;
    font-weight: 600;
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }

  .condition-branch-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .condition-branch-row {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
    min-height: 28px;
    overflow: visible;
  }

  .condition-branch-name {
    font-size: 12px;
    font-weight: 600;
    color: #4b5563;
  }

  .condition-branch-output {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 10px;
    margin-right: 14px;
  }

  .condition-branch-tag {
    font-size: 12px;
    font-weight: 700;
    color: #374151;
    letter-spacing: 0.3px;
  }

  .condition-branch-action {
    position: absolute;
    right: -10px;
    top: 50%;
    transform: translateY(-50%);
    width: 20px;
    height: 20px;
    flex-shrink: 0;
    z-index: 10;
    opacity: 0;
    transition: all 0.2s;
  }

  .condition-branch-handle {
    width: 20px;
    height: 20px;
    left: 0 !important;
    right: auto !important;
    top: 0 !important;
    transform: none;
    opacity: 0;
    pointer-events: none;
  }

  .condition-branch-add-btn {
    position: absolute;
    inset: 0;
    border-radius: 50%;
    background-color: #3b82f6;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    font-size: 14px;
    font-weight: 300;
    line-height: 1;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.2s;
    user-select: none;
    touch-action: none;

    &:hover {
      background-color: #2563eb;
      transform: scale(1.1);
    }
  }

  &:hover .condition-branch-action {
    opacity: 1;
  }
}

.loop-node {
  min-width: 0;
  max-width: none;
  width: fit-content;
  box-sizing: border-box;
  border-radius: 18px;
  border-color: #d8e6ff;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
  overflow: visible;
  isolation: isolate;

  &.selected {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.16),
      0 14px 28px rgba(59, 130, 246, 0.1);
  }

  .loop-node-shell {
    position: relative;
    z-index: 1;
    height: 100%;
    padding: 14px 16px 14px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    box-sizing: border-box;
    background-color: #fff;
    border-radius: inherit;
  }

  .loop-node-header {
    display: flex;
    align-items: center;
    gap: 10px;
    min-height: 22px;
  }

  .loop-node-icon {
    width: 22px;
    height: 22px;
    border-radius: 7px;
    background: linear-gradient(180deg, #14b8a6 0%, #06b6d4 100%);
    color: #fff;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 6px 14px rgba(6, 182, 212, 0.18);
    flex-shrink: 0;
  }

  .loop-icon-glyph {
    font-size: 14px;
    font-weight: 700;
    line-height: 1;
  }

  .loop-node-title {
    min-width: 0;
    font-size: 14px;
    font-weight: 600;
    color: #111827;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .loop-editor-surface {
    min-height: 0;
    flex: 1;
    border: 1px solid #eef2f7;
    border-radius: 14px;
    background: linear-gradient(180deg, #fbfcfe 0%, #f5f7fb 100%);
    overflow: visible;
    box-sizing: border-box;
  }
}

.loop-inner-source-handle {
  opacity: 0;
  pointer-events: none;
}

.loop-inner-handle {
  width: 10px;
  height: 10px;
  background-color: #3b82f6;
  border: 2px solid #fff;
  border-radius: 50%;
  transition: all 0.2s;

  &.loop-inner-handle-drop-target {
    background-color: #2563eb;
    border-color: #dbeafe;
    box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.2);
  }

  &:hover {
    background-color: #2563eb;
    transform: scale(1.3);
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
  }
}

.loop-mini-start {
  position: relative;
  box-sizing: border-box;
  background-color: #fff;
  border: 1px solid #dde6f0;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(15, 23, 42, 0.06);
  user-select: none;
  touch-action: none;
  min-width: 148px;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  gap: 8px;

  .loop-mini-start-lock {
    width: 30px;
    height: 30px;
    border-radius: 999px;
    background-color: #eff6ff;
    color: #2563eb;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    box-shadow: inset 0 0 0 1px rgba(96, 165, 250, 0.18);
  }

  .loop-mini-start-text {
    display: flex;
    flex-direction: column;
    gap: 2px;
  }

  .loop-mini-start-title {
    font-size: 13px;
    font-weight: 600;
    color: #111827;
  }

  .loop-mini-start-tip {
    font-size: 11px;
    color: #6b7280;
  }
}

.loop-workflow-canvas.compact .loop-mini-start {
  min-width: 36px;
  width: 36px;
  height: 36px;
  padding: 0;
  border-radius: 999px;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.06);

  .loop-mini-start-lock {
    width: 20px;
    height: 20px;
    font-size: 10px;
  }
}

.loop-workflow-canvas.compact .loop-mini-start .loop-mini-add-btn {
  right: -14px;
  opacity: 1;
}

.loop-mini-handle {
  width: 8px;
  height: 8px;
  background-color: #3b82f6;
  border: 2px solid #fff;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.12);
  transition: all 0.2s;

  &.loop-mini-handle-drop-target {
    background-color: #2563eb;
    border-color: #dbeafe;
    box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.22);
  }

  &.target {
    left: -5px !important;
  }

  &.source {
    right: -5px !important;
  }
}

.loop-mini-source-handle {
  opacity: 0;
  pointer-events: none;
}

.loop-mini-add-btn {
  position: absolute;
  width: 18px;
  height: 18px;
  border: none;
  border-radius: 50%;
  background-color: #3b82f6;
  color: #fff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  transition: all 0.2s;
  box-shadow: 0 3px 10px rgba(59, 130, 246, 0.24);
  user-select: none;
  touch-action: none;
  right: -13px;
  top: 50%;
  transform: translateY(-50%);

  &:hover {
    background-color: #2563eb;
    transform: translateY(-50%) scale(1.08);
  }
}

.loop-mini-start:hover .loop-mini-add-btn {
  opacity: 1;
}

.loop-inner-edge-add {
  overflow: visible;
  pointer-events: none;
}
.loop-inner-edge-add-shell {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: visible;
}
.loop-inner-edge-add-btn {
  position: static;
  width: 18px;
  height: 18px;
  opacity: 0;
  pointer-events: none;
  transform: none;
}
.loop-inner-edge-wrapper:hover .loop-inner-edge-add {
  pointer-events: auto;
}
.loop-inner-edge-wrapper:hover .loop-inner-edge-add-btn,
.loop-inner-edge-add-btn:hover {
  opacity: 1;
  pointer-events: auto;
}
.loop-inner-edge-add-btn:hover {
  transform: scale(1.08);
}

.loop-inner-add-menu {
  position: absolute;
  min-width: 220px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 60;
  overflow: hidden;
}

.loop-inner-add-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f3f4f6;
  font-size: 14px;
  font-weight: 600;
  color: #111827;
}

.loop-inner-close {
  width: 24px;
  height: 24px;
  border: none;
  background: transparent;
  color: #9ca3af;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s;
}

.loop-inner-close:hover {
  color: #6b7280;
}

.loop-inner-add-list {
  padding: 8px;
}

.loop-inner-add-item {
  width: 100%;
  padding: 10px 12px;
  border: none;
  border-radius: 8px;
  background-color: transparent;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  text-align: left;
  transition: all 0.2s;
}

.loop-inner-add-item:hover {
  background-color: #f3f4f6;
}

.loop-inner-add-icon {
  font-size: 20px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9fafb;
  border-radius: 8px;
  flex-shrink: 0;
}

.loop-inner-add-text {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.loop-inner-add-label {
  font-size: 14px;
  font-weight: 500;
  color: #111827;
}

.loop-inner-add-desc {
  font-size: 12px;
  color: #6b7280;
  margin-top: 2px;
}

.loop-inner-add-overlay {
  position: absolute;
  inset: 0;
  z-index: 20;
}

:deep(.loop-workflow-stage .vue-flow__renderer) {
  border-radius: inherit;
}

:deep(.loop-workflow-stage .vue-flow__edge-path) {
  stroke: #94a3b8;
  stroke-width: 2;
}

:deep(.loop-workflow-stage .vue-flow__node) {
  width: auto;
}

:deep(.loop-workflow-stage .vue-flow__attribution) {
  display: none;
}
</style>

