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
  <div ref="appContainerRef" class="app-container workflow-editor">
    <div class="toolbar">
      <div class="toolbar-left">
        <span class="toolbar-title">
          <span class="title" style="">{{ flowName || "-" }}</span>
          <el-tag type="primary" v-if="workflowType !== null">
            {{
              (workflowType == 1 && "Chatflow") ||
              (workflowType == 0 && "工作流") ||
              "Agent"
            }}</el-tag
          >
          <span class="internally" v-if="flowBuiltinFlag == 1">
            <el-icon class="desc-icon">
              <WarningFilled />
            </el-icon>
            此Bot为系统内置，不可修改！如果需要修改，请复制新Bot后修改。</span
          >
        </span>
      </div>
      <div class="toolbar-actions">
        <el-button
          type="primary"
          plain
          @click="showCopyDialog()"
          icon="CopyDocument"
          @mousedown="(e) => e.preventDefault()"
          class="custom-btn-padding"
        >
          复制
        </el-button>
        <el-button type="primary" class="fhbtn" plain @click="routerView">
          <svg-icon :iconClass="'fhs'" />返回
        </el-button>
        <el-button type="primary" @click="openDebugRunPanel">
          <VideoPlay class="ds" />
          调试
        </el-button>
        <el-button
          type="primary"
          @click="exportFlow"
          :disabled="flowBuiltinFlag == 1"
        >
          <img src="../../../../assets/icons/svg/bc.svg" alt="" class="bcimg" />
          保存
        </el-button>
      </div>
    </div>

    <div class="editor-container" v-loading="loading">
      <div class="flow-wrapper">
        <VueFlow
          v-model:nodes="nodes"
          v-model:edges="edges"
          :fit-view-on-init="false"
          :snap-to-grid="true"
          :snap-grid="[20, 20]"
          :default-zoom="0.2"
          :min-zoom="0.1"
          :max-zoom="2"
          :default-edge-options="defaultEdgeOptions"
          :connection-line-type="ConnectionLineType.Bezier"
          @node-click="onNodeClick"
          @edge-click="onEdgeClick"
          @pane-click="onPaneClick"
          @connect="onConnect"
        >
          <template #node-start="props">
            <div
              class="custom-node start-node"
              :class="{ selected: selectedNode?.id === props.id }"
            >
              <div class="node-top">
                <span class="node-type-label">开始</span>
              </div>
              <div class="node-body">
                <div class="node-header">
                  <div class="node-icon-wrapper">
                    <svg
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      class="node-svg"
                    >
                      <path
                        d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 14.5v-9l6 4.5-6 4.5z"
                      />
                    </svg>
                  </div>
                  <div class="node-title">
                    {{ getNodeLabel(props.data, "开始") }}
                  </div>
                </div>
                <div
                  v-if="getStartFieldPreview(props.data).length"
                  class="start-node-fields"
                >
                  <div
                    v-for="field in getStartFieldPreview(props.data)"
                    :key="field.id"
                    class="start-node-field-row"
                  >
                    <span class="start-node-field-name">
                      {{ getStartFieldDisplayLabel(field) }}
                    </span>
                    <span class="start-node-field-type">
                      {{ getStartFieldValueTypeLabel(field.type) }}
                    </span>
                  </div>
                  <div
                    v-if="getStartFieldOverflowCount(props.data) > 0"
                    class="start-node-field-more"
                  >
                    +{{ getStartFieldOverflowCount(props.data) }} 个输入字段
                  </div>
                </div>
                <div v-else class="start-node-empty">
                  当前没有输入变量，请添加。
                </div>
              </div>
              <Handle
                type="target"
                :position="Position.Left"
                class="handle"
                :class="
                  dragHoverTargetId === props.id ? 'handle-drop-target' : ''
                "
              />
              <Handle
                type="source"
                :position="Position.Right"
                :connectable-start="false"
                class="handle node-source-handle"
              />
              <div
                class="node-add-btn"
                @click.stop.prevent
                @pointerdown.stop.prevent="
                  onSourceActionPointerDown($event, props.id)
                "
              >
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </template>

          <template #node-llm="props">
            <div
              class="custom-node llm-node"
              :class="{ selected: selectedNode?.id === props.id }"
            >
              <button
                type="button"
                class="node-delete-btn"
                @click.stop.prevent="handleOuterNodeDelete(props.id)"
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
                    {{ getNodeLabel(props.data, "LLM") }}
                  </div>
                </div>
                <div
                  class="node-subtitle"
                  v-if="getNodeConfigValue(props.data, 'model', '')"
                >
                  {{ getNodeConfigValue(props.data, "model", "") }}
                </div>
              </div>
              <Handle
                type="target"
                :position="Position.Left"
                class="handle"
                :class="
                  dragHoverTargetId === props.id ? 'handle-drop-target' : ''
                "
              />
              <Handle
                type="source"
                :position="Position.Right"
                :connectable-start="false"
                class="handle node-source-handle"
              />
              <div
                class="node-add-btn"
                @click.stop.prevent
                @pointerdown.stop.prevent="
                  onSourceActionPointerDown($event, props.id)
                "
              >
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </template>

          <template #node-reply="props">
            <div
              class="custom-node reply-node"
              :class="{ selected: selectedNode?.id === props.id }"
            >
              <button
                type="button"
                class="node-delete-btn"
                @click.stop.prevent="handleOuterNodeDelete(props.id)"
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
                    <svg
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      class="node-svg"
                    >
                      <path
                        d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"
                      />
                    </svg>
                  </div>
                  <div class="node-title">
                    {{ getReplyNodeLabel(props.data) }}
                  </div>
                </div>
                <div
                  v-if="
                    isChatflowWorkflow &&
                    getReplyNodePreviewSegments(props.data, props.id).length
                  "
                  class="node-preview node-preview--inline"
                >
                  <template
                    v-for="(
                      previewSegment, previewIndex
                    ) in getReplyNodePreviewSegments(props.data, props.id)"
                    :key="`reply-preview-segment-${props.id}-${previewIndex}`"
                  >
                    <span
                      v-if="previewSegment.type === 'variable'"
                      class="preview-tag"
                    >
                      {{ previewSegment.display }}
                    </span>
                    <span v-else class="preview-text">
                      {{ previewSegment.value }}
                    </span>
                  </template>
                </div>
                <div
                  class="node-preview"
                  v-else-if="getReplyNodePreviewList(props.data, 3).length"
                >
                  <span
                    v-for="(
                      previewItem, previewIndex
                    ) in getReplyNodePreviewList(props.data, 3)"
                    :key="`reply-preview-${props.id}-${previewIndex}`"
                    class="preview-tag"
                  >
                    {{ previewItem }}
                  </span>
                  <div
                    v-if="getReplyNodePreviewOverflowCount(props.data, 3) > 0"
                    class="reply-node-preview-more"
                  >
                    +{{ getReplyNodePreviewOverflowCount(props.data, 3) }}
                    个输出字段
                  </div>
                </div>
              </div>
              <Handle
                type="target"
                :position="Position.Left"
                class="handle"
                :class="
                  dragHoverTargetId === props.id ? 'handle-drop-target' : ''
                "
              />
              <Handle
                type="source"
                :position="Position.Right"
                :connectable-start="false"
                class="handle node-source-handle"
              />
              <div
                class="node-add-btn"
                @click.stop.prevent
                @pointerdown.stop.prevent="
                  onSourceActionPointerDown($event, props.id)
                "
              >
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </template>

          <template #node-tool="props">
            <div
              class="custom-node tool-node"
              :class="{ selected: selectedNode?.id === props.id }"
            >
              <button
                type="button"
                class="node-delete-btn"
                @click.stop.prevent="handleOuterNodeDelete(props.id)"
                @pointerdown.stop.prevent
              >
                <el-icon><Delete /></el-icon>
              </button>
              <div class="node-top">
                <span class="node-type-label">工具</span>
              </div>
              <div class="node-body">
                <div class="node-header">
                  <div class="node-icon-wrapper">
                    <svg
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      class="node-svg"
                    >
                      <path
                        d="M20.7 7.3a5.99 5.99 0 0 1-7.95 7.95l-5.02 5.02a1.5 1.5 0 1 1-2.12-2.12l5.02-5.02A6 6 0 0 1 16.7 3.3l-3.06 3.06 1 3 3 1 3.06-3.06z"
                      />
                    </svg>
                  </div>
                  <div class="node-title">
                    {{ getToolNodeLabel(props.data) }}
                  </div>
                </div>
                <div
                  class="node-subtitle"
                  v-if="getToolNodeSubtitle(props.data)"
                >
                  {{ getToolNodeSubtitle(props.data) }}
                </div>
                <div
                  class="node-preview"
                  v-if="getToolNodePreviewList(props.data, 3).length"
                >
                  <span
                    v-for="(
                      previewItem, previewIndex
                    ) in getToolNodePreviewList(props.data, 3)"
                    :key="`tool-preview-${props.id}-${previewIndex}`"
                    class="preview-tag"
                  >
                    {{ previewItem }}
                  </span>
                  <div
                    v-if="getToolNodePreviewOverflowCount(props.data, 3) > 0"
                    class="tool-node-preview-more"
                  >
                    +{{ getToolNodePreviewOverflowCount(props.data, 3) }}
                    个输出字段
                  </div>
                </div>
              </div>
              <Handle
                type="target"
                :position="Position.Left"
                class="handle"
                :class="
                  dragHoverTargetId === props.id ? 'handle-drop-target' : ''
                "
              />
              <Handle
                type="source"
                :position="Position.Right"
                :connectable-start="false"
                class="handle node-source-handle"
              />
              <div
                class="node-add-btn"
                @click.stop.prevent
                @pointerdown.stop.prevent="
                  onSourceActionPointerDown($event, props.id)
                "
              >
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </template>

          <template #node-condition="props">
            <div
              class="custom-node condition-node"
              :class="{ selected: selectedNode?.id === props.id }"
            >
              <button
                type="button"
                class="node-delete-btn"
                @click.stop.prevent="handleOuterNodeDelete(props.id)"
                @pointerdown.stop.prevent
              >
                <el-icon><Delete /></el-icon>
              </button>
              <div class="node-top">
                <span class="node-type-label">条件分支</span>
              </div>
              <div class="node-body">
                <div class="condition-summary">
                  <div class="node-icon-wrapper">
                    <svg
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      class="node-svg"
                    >
                      <path
                        d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 9h-2V5h2v7zM7 9h2v7H7V9zm10 7h-2V9h2v7z"
                      />
                    </svg>
                  </div>
                  <div class="condition-title-row">
                    <div class="node-title">
                      {{ getNodeLabel(props.data, "条件分支") }}
                    </div>
                    <span class="condition-count">{{
                      getConditionCases(props.data).length
                    }}</span>
                  </div>
                </div>

                <div class="condition-branch-list">
                  <div
                    v-for="(caseItem, index) in getConditionCases(props.data)"
                    :key="caseItem.id"
                    class="condition-branch-row"
                  >
                    <span class="condition-branch-name">
                      {{ getConditionCaseLabel(index) }}
                    </span>
                    <div class="condition-branch-output">
                      <span class="condition-branch-tag">
                        {{
                          getConditionBranchLabel(
                            index,
                            getConditionCases(props.data).length
                          )
                        }}
                      </span>
                      <div class="condition-branch-action">
                        <Handle
                          type="source"
                          :id="getConditionHandleId(caseItem.id)"
                          :position="Position.Right"
                          :connectable-start="false"
                          class="handle condition-branch-handle"
                        />
                        <div
                          class="condition-branch-add-btn"
                          @click.stop.prevent
                          @pointerdown.stop.prevent="
                            onSourceActionPointerDown(
                              $event,
                              props.id,
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
                class="handle"
                :class="
                  dragHoverTargetId === props.id ? 'handle-drop-target' : ''
                "
              />
            </div>
          </template>

          <template #node-loop="props">
            <div
              class="custom-node loop-node"
              :class="{ selected: selectedNode?.id === props.id }"
              :style="getLoopNodeStyle(props.data)"
            >
              <button
                type="button"
                class="node-delete-btn"
                @click.stop.prevent="handleOuterNodeDelete(props.id)"
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
                    {{ getNodeLabel(props.data, "循环") }}
                  </div>
                </div>

                <div
                  class="loop-editor-surface nodrag nopan"
                  :style="getLoopEditorSurfaceStyle(props.data)"
                  @pointerdown.stop
                  @click.stop
                >
                  <LoopWorkflowCanvas
                    :workflow-data="props.data"
                    :compact="true"
                    :loop-path="loopPath"
                    :selected-node-id="getSelectedLoopNodeId(loopPath)"
                    :default-llm-model-config="defaultLlmModelConfig"
                    @update:workflow-data="
                      updateLoopWorkflowData(props.id, $event)
                    "
                    @selectNode="handleInnerNodeSelect"
                    @clearSelection="clearInnerSelection"
                    @deleteNode="handleInnerNodeDelete"
                  />
                </div>
              </div>
              <Handle
                :id="LOOP_OUTER_TARGET_HANDLE_ID"
                type="target"
                :position="Position.Left"
                class="handle"
                :class="
                  dragHoverTargetId === props.id ? 'handle-drop-target' : ''
                "
              />
              <Handle
                :id="LOOP_OUTER_SOURCE_HANDLE_ID"
                type="source"
                :position="Position.Right"
                :connectable-start="false"
                class="handle node-source-handle"
              />
              <div
                class="node-add-btn"
                @click.stop.prevent
                @pointerdown.stop.prevent="
                  onSourceActionPointerDown(
                    $event,
                    props.id,
                    LOOP_OUTER_SOURCE_HANDLE_ID
                  )
                "
              >
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </template>

          <template #edge-default="edgeProps">
            <g class="edge-wrapper">
              <!-- 使用 BezierEdge 组件渲染基础边 -->
              <BezierEdge v-bind="edgeProps" />
              <!-- 中间添加按钮 - 20x20 稍微小一点更精致 -->
              <g
                v-if="edgeProps.sourceX && edgeProps.targetX"
                class="edge-add-btn"
                :transform="`translate(${
                  (edgeProps.sourceX + edgeProps.targetX) / 2 - 10
                }, ${(edgeProps.sourceY + edgeProps.targetY) / 2 - 10})`"
                @click.stop="showAddNodeMenuOnEdge(edgeProps, $event)"
              >
                <circle cx="10" cy="10" r="10" class="add-btn-bg" />
                <text x="10" y="10.5" text-anchor="middle" fill="white">+</text>
              </g>
            </g>
          </template>

          <Background pattern-color="#e5e7eb" :gap="20" />
          <Controls
            :show-zoom="true"
            :show-fit="true"
            :show-interactive="false"
          />
        </VueFlow>

        <!-- 添加节点菜单 -->
        <div
          v-if="addNodeMenuVisible"
          class="add-node-menu"
          :style="{
            left: addNodeMenuPosition.x + 'px',
            top: addNodeMenuPosition.y + 'px',
          }"
        >
          <div class="add-node-menu-header">
            <span>添加节点</span>
            <el-icon class="close-btn" @click="hideAddNodeMenu">
              <Close />
            </el-icon>
          </div>
          <div class="add-node-menu-tabs">
            <button
              v-for="tabItem in addNodeMenuTabs"
              :key="tabItem.key"
              type="button"
              class="add-node-menu-tab"
              :class="{ 'is-active': addNodeMenuActiveTab === tabItem.key }"
              @click="handleAddNodeMenuTabChange(tabItem.key)"
            >
              {{ tabItem.label }}
            </button>
          </div>
          <div class="add-node-menu-list">
            <div
              v-for="menuItem in currentAddMenuItems"
              :key="`${menuItem.menuType}-${
                menuItem.toolId ||
                menuItem.name ||
                menuItem.label ||
                menuItem.type
              }`"
              class="add-node-menu-item"
              @click="handleAddMenuItemClick(menuItem)"
            >
              <NodeTypeIcon
                class="node-icon"
                :type="menuItem.type"
                :fallback="menuItem.icon"
              />
              <div class="node-info">
                <div class="node-label">{{ menuItem.label }}</div>
                <div class="node-desc">{{ menuItem.description }}</div>
              </div>
            </div>
            <div v-if="!currentAddMenuItems.length" class="add-node-menu-empty">
              <span>{{ addNodeMenuEmptyText }}</span>
              <el-button
                v-if="addNodeMenuActiveTab === 'tools' && toolMenuLoadError"
                link
                type="primary"
                @click.stop="ensureToolMenuItemsLoaded(true)"
              >
                重新加载
              </el-button>
            </div>
          </div>
        </div>

        <div
          v-if="addNodeMenuVisible"
          class="add-node-menu-overlay"
          @click="hideAddNodeMenu()"
        ></div>
      </div>

      <div class="config-drawer" :class="{ open: drawerVisible }">
        <template v-if="drawerMode === 'node'">
          <div
            class="drawer-header"
            :class="{
              'is-start': ['start', 'llm', 'reply'].includes(
                selectedNode?.type
              ),
            }"
          >
            <div class="drawer-header-main">
              <div class="drawer-title">
                <div class="drawer-icon" :class="selectedNode?.type">
                  <svg
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    v-if="selectedNode?.type === 'start'"
                  >
                    <path
                      d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 14.5v-9l6 4.5-6 4.5z"
                    />
                  </svg>
                  <LlmNodeIcon
                    v-else-if="selectedNode?.type === 'llm'"
                    class="drawer-llm-icon"
                  />
                  <svg
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    v-else-if="selectedNode?.type === 'reply'"
                  >
                    <path
                      d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"
                    />
                  </svg>
                  <svg
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    v-else-if="selectedNode?.type === 'tool'"
                  >
                    <path
                      d="M20.7 7.3a5.99 5.99 0 0 1-7.95 7.95l-5.02 5.02a1.5 1.5 0 1 1-2.12-2.12l5.02-5.02A6 6 0 0 1 16.7 3.3l-3.06 3.06 1 3 3 1 3.06-3.06z"
                    />
                  </svg>
                  <svg
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    v-else-if="selectedNode?.type === 'condition'"
                  >
                    <path
                      d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 9h-2V5h2v7zM7 9h2v7H7V9zm10 7h-2V9h2v7z"
                    />
                  </svg>
                  <span
                    v-else-if="selectedNode?.type === 'loop'"
                    class="drawer-loop-glyph"
                    >&#8734;</span
                  >
                  <svg viewBox="0 0 24 24" fill="currentColor" v-else>
                    <path
                      d="M12 3l7 4v10l-7 4-7-4V7l7-4zm0 2.3L7 8.1v7.8l5 2.8 5-2.8V8.1l-5-2.8z"
                    />
                  </svg>
                </div>
                <el-input
                  v-if="selectedNode"
                  :model-value="getNodeLabel(selectedNode.data)"
                  class="node-config-header-name"
                  placeholder="输入节点名称"
                  @update:model-value="
                    handleSelectedNodeFieldUpdate({
                      field: 'label',
                      value: $event,
                    })
                  "
                />
              </div>
            </div>
            <el-icon class="close-icon" @click="closeSidebarPanel">
              <Close />
            </el-icon>
          </div>

          <div class="drawer-content">
            <StartNodeConfigForm
              v-if="selectedNode?.type === 'start'"
              :fields="selectedStartFieldCards"
              @addField="openStartFieldDialog()"
              @editField="openStartFieldDialog"
              @removeField="removeStartField"
            />

            <LlmNodeConfigForm
              v-else-if="selectedNode?.type === 'llm'"
              :node="selectedLlmNodeView"
              :model-options="llmModelOptions"
              :response-format-options="llmResponseFormatOptions"
              :available-context-variable-groups="
                selectedLlmContextVariableGroups
              "
              :has-upstream-context-sources="
                selectedLlmHasUpstreamContextSources
              "
              :dialog-append-to="appContainerRef || 'body'"
              @updateField="handleSelectedNodeFieldUpdate"
              @updateMessage="handleSelectedLlmMessageUpdate"
              @addMessage="addLlmMessage"
              @removeMessage="removeLlmMessage"
            />

            <ChatflowReplyNodeConfigForm
              v-else-if="selectedNode?.type === 'reply' && isChatflowWorkflow"
              :content="selectedReplyContent"
              :available-context-variable-groups="
                selectedReplyContextVariableGroups
              "
              :has-upstream-context-sources="
                selectedReplyHasUpstreamContextSources
              "
              @updateContent="handleSelectedReplyContentUpdate"
            />

            <ReplyNodeConfigForm
              v-else-if="selectedNode?.type === 'reply'"
              :outputs="selectedReplyOutputs"
              :available-context-variable-groups="
                selectedReplyContextVariableGroups
              "
              :has-upstream-context-sources="
                selectedReplyHasUpstreamContextSources
              "
              @addOutput="addReplyOutput"
              @updateOutput="handleSelectedReplyOutputUpdate"
              @removeOutput="removeReplyOutput"
            />

            <ToolNodeConfigForm
              v-else-if="selectedNode?.type === 'tool'"
              :tool-id="getNodeConfigValue(selectedNode?.data, 'toolId', '')"
              :tool-name="
                getNodeConfigValue(selectedNode?.data, 'toolName', '')
              "
              :tool-source="
                getNodeConfigValue(selectedNode?.data, 'toolSource', '')
              "
              :tool-description="
                getNodeConfigValue(selectedNode?.data, 'toolDescription', '')
              "
              :description="getNodeDescription(selectedNode?.data)"
              :outputs="selectedToolOutputs"
              @updateField="handleSelectedNodeFieldUpdate"
              @addOutput="addToolOutput"
              @updateOutput="handleSelectedToolOutputUpdate"
              @removeOutput="removeToolOutput"
            />

            <ConditionNodeConfigForm
              v-else-if="selectedNode?.type === 'condition'"
              :cases="selectedConditionCases"
              @updateCase="handleSelectedConditionCaseUpdate"
              @addCase="addConditionCase"
              @removeCase="removeConditionCase"
            />

            <LoopNodeConfigForm
              v-else-if="selectedNode?.type === 'loop'"
              :item-alias="
                getNodeConfigValue(selectedNode?.data, 'itemAlias', '')
              "
              :max-iterations="
                Number(
                  getNodeConfigValue(selectedNode?.data, 'maxIterations', 1)
                ) || 1
              "
              @updateField="handleSelectedNodeFieldUpdate"
            />
          </div>
        </template>

        <template v-else-if="drawerMode === 'debug'">
          <div class="drawer-header">
            <div class="drawer-header-main">
              <div class="drawer-title">
                <div class="drawer-icon debug">
                  <svg viewBox="0 0 24 24" fill="currentColor">
                    <path
                      d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 14.5v-9l6 4.5-6 4.5z"
                    />
                  </svg>
                </div>
                <span>调试</span>
              </div>
            </div>

            <div class="drawer-header-actions">
              <el-tooltip
                v-if="isChatflowWorkflow"
                content="用户输入字段"
                placement="top"
              >
                <el-button
                  text
                  class="drawer-toggle-btn"
                  :class="{
                    'is-active': chatflowDebugSectionsVisible,
                    'is-inactive': !chatflowDebugSectionsVisible,
                  }"
                  @click="toggleChatflowDebugSections"
                >
                  <el-icon><Operation /></el-icon>
                </el-button>
              </el-tooltip>
              <el-icon class="close-icon" @click="closeSidebarPanel">
                <Close />
              </el-icon>
            </div>
          </div>

          <div
            class="drawer-content drawer-content--debug"
            :class="{ 'drawer-content--chatflow': isChatflowWorkflow }"
          >
            <WorkflowDebugRunPanel
              v-if="!isChatflowWorkflow"
              :fields="debugRunStartFields"
              :workflow-data="debugRunWorkflowData"
              :before-run="validateFlowBeforeRun"
            />
            <ChatflowDebugRunPanel
              v-else
              v-model:show-sections="chatflowDebugSectionsVisible"
              :fields="debugRunStartFields"
              :workflow-data="debugRunWorkflowData"
              :before-run="validateFlowBeforeRun"
            />
          </div>
        </template>
      </div>
    </div>

    <el-dialog
      v-model="startFieldDialogVisible"
      :title="startFieldDialogTitle"
      width="560px"
      class="start-field-dialog"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="closeStartFieldDialog"
    >
      <el-form
        ref="startFieldDialogFormRef"
        :model="startFieldDraft"
        :rules="startFieldDialogRules"
        :validate-on-rule-change="false"
        label-width="80px"
        status-icon
      >
        <el-form-item prop="name" label="变量名称">
          <el-input v-model="startFieldDraft.name" placeholder="请输入" />
        </el-form-item>

        <el-form-item prop="label" label="显示名称">
          <el-input v-model="startFieldDraft.label" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="字段类型">
          <el-select
            v-model="startFieldDraft.type"
            popper-class="dydome-square-popper"
            @change="handleStartFieldTypeChange"
          >
            <el-option
              v-for="typeOption in startFieldTypeOptions"
              :key="typeOption.value"
              :label="typeOption.label"
              :value="typeOption.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          v-if="startFieldDraftTypeMeta.supportsMaxLength"
          prop="maxLength"
          label="最大长度"
        >
          <el-input-number
            v-model="startFieldDraft.maxLength"
            :min="1"
            :max="2000"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div>
          <el-button @click="closeStartFieldDialog">取消</el-button>
          <el-button type="primary" @click="saveStartField">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <div class="status-bar">
      <span>{{ nodes.length }} 个节点</span>
      <span>{{ edges.length }} 条连接</span>
    </div>

    <el-dialog :title="title" v-model="open" width="800px" draggable>
      <el-form ref="botRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="Bot名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入描述"
            maxlength="512"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注"
            maxlength="512"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">取 消</el-button>
          <el-button type="primary" size="mini" @click="submitForm"
            >确 定</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ProcessFlow">
import { ref, computed, nextTick, onBeforeUnmount, onMounted } from "vue";
import {
  VueFlow,
  useVueFlow,
  Handle,
  Position,
  ConnectionLineType,
  BezierEdge,
} from "@vue-flow/core";
import { Background } from "@vue-flow/background";
import { Controls } from "@vue-flow/controls";
import { Close, Delete, Operation, Plus } from "@element-plus/icons-vue";
import { getChatModelDict } from "@/api/ai/myModel/myModel.js";
import { getFlow, submitFlow } from "@/api/kb/bot/flow.js";
import { getBot, copyBot } from "@/api/kb/bot/bot";
import { listTool } from "@/api/kb/tool/tool.js";
import LoopWorkflowCanvas from "./LoopWorkflowCanvas.vue";
import LlmNodeIcon from "./components/LlmNodeIcon.vue";
import NodeTypeIcon from "./components/NodeTypeIcon.vue";
import WorkflowDebugRunPanel from "./components/WorkflowDebugRunPanel.vue";
import ChatflowDebugRunPanel from "./components/ChatflowDebugRunPanel.vue";
import StartNodeConfigForm from "./components/nodeConfig/StartNodeConfigForm.vue";
import LlmNodeConfigForm from "./components/nodeConfig/LlmNodeConfigForm.vue";
import ChatflowReplyNodeConfigForm from "./components/nodeConfig/ChatflowReplyNodeConfigForm.vue";
import ReplyNodeConfigForm from "./components/nodeConfig/ReplyNodeConfigForm.vue";
import ConditionNodeConfigForm from "./components/nodeConfig/ConditionNodeConfigForm.vue";
import LoopNodeConfigForm from "./components/nodeConfig/LoopNodeConfigForm.vue";
import ToolNodeConfigForm from "./components/nodeConfig/ToolNodeConfigForm.vue";
import "@vue-flow/core/dist/style.css";
import "@vue-flow/core/dist/theme-default.css";
import "@vue-flow/controls/dist/style.css";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const route = useRoute();
import {
  cloneNodeData,
  createStructuredNodeData,
  getNodeConfig,
  getNodeConfigValue,
  getNodeDescription,
  getNodeInput,
  getNodeLabel,
  getNodeOutput,
  mergeNodeConfig,
  normalizeTemplateReferenceSegment,
  normalizeWorkflowGraphIds,
  omitObjectKeys,
  setNodeConfigValue,
  setNodeInput,
  setNodeOutput,
} from "./utils/nodeData";
const router = useRouter();
const {
  addEdges,
  removeNodes,
  removeEdges,
  setNodes,
  setEdges,
  startConnection,
  updateConnection,
  endConnection,
  updateNodeInternals,
  vueFlowRef,
} = useVueFlow();

// 默认边配置 - 使用自定义边类型
const defaultEdgeOptions = {
  type: "default",
  style: {
    stroke: "#999",
    strokeWidth: 2,
  },
  markerEnd: {
    type: "arrowclosed",
    width: 12,
    height: 12,
    color: "#999",
  },
};
const loading = ref(false);

// 工作流类型
const workflowType = ref(null);
const flowBuiltinFlag = ref(null);
const nodes = ref([
  {
    id: "start_1",
    type: "start",
    position: { x: 100, y: 200 },
    data: normalizeStartNodeData({ label: "开始" }),
  },
]);

const edges = ref([
  // { id: "e1-2", source: "1", target: "2", type: "default" },
  // { id: "e2-3", source: "2", target: "3", type: "default" },
]);
//复制Bot
const open = ref(false);
const title = ref("");
const botDetail = ref({});
const form = ref({});
// 表单校验规则
const rules = reactive({
  name: [
    { required: true, message: "请输入Bot名称", trigger: "blur" },
    { max: 100, message: "Bot名称不能超过100个字符", trigger: "blur" },
  ],
});

// 工作流id
const botId = ref(null);
const flowName = ref(null);
let conditionCaseSeed = 0;
let edgeSeed = 0;
let nodeSeed = 0;
let loopStepSeed = 0;
let startFieldSeed = 0;
let llmMessageSeed = 0;
let replyOutputSeed = 0;
let toolOutputSeed = 0;
const startFieldDialogVisible = ref(false);
const startFieldDialogMode = ref("create");
const startFieldDialogFormRef = ref(null);
const startFieldDraft = ref({
  type: "text",
  name: "",
  label: "",
  maxLength: 48,
  defaultValue: "",
  required: true,
});
const debugRunPanelVisible = ref(false);
const chatflowDebugSectionsVisible = ref(true);
const appContainerRef = ref(null);
const selectedNode = ref(null);
const llmModelOptions = ref([]);

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

function getDefaultLlmModelOption() {
  return (
    llmModelOptions.value
      .flatMap((group) => (Array.isArray(group?.options) ? group.options : []))
      .find((option) => `${option?.model || option?.label || ""}`.trim()) ||
    null
  );
}

function getDefaultLlmModelConfig() {
  const option = getDefaultLlmModelOption();

  return {
    provider: normalizeModelProviderValue(option?.provider),
    model: `${option?.model || option?.label || ""}`.trim(),
  };
}

const loopPath = computed(() => [props.id]);
const defaultLlmModelConfig = computed(() => getDefaultLlmModelConfig());
const llmResponseFormatOptions = [
  { label: "文本", value: "text" },
  { label: "JSON", value: "json_object" },
  { label: "Markdown", value: "markdown" },
];

const addNodeMenuVisible = ref(false);
const addNodeMenuPosition = ref({ x: 0, y: 0 });
const addNodeMenuSourceId = ref(null);
const addNodeMenuSourceHandle = ref(null);
const addNodeMenuTargetId = ref(null);
const addNodeMenuEdgeId = ref(null);
const addNodeMenuActiveTab = ref("nodes");
const toolMenuItems = ref([]);
const toolMenuLoading = ref(false);
const toolMenuLoaded = ref(false);
const toolMenuLoadError = ref("");
const sourceActionDragThreshold = 6;
const dragHoverTargetId = ref(null);
const addMenuOpenAt = ref(0);
const addMenuDismissGuardMs = 220;
const LOOP_OUTER_TARGET_HANDLE_ID = "loop-node-target";
const LOOP_OUTER_SOURCE_HANDLE_ID = "loop-node-source";
const ADD_NODE_MENU_MARGIN = 16;
const ADD_NODE_MENU_VERTICAL_OFFSET = 120;
const ADD_NODE_MENU_ESTIMATED_WIDTH = 252;
const ADD_NODE_MENU_HEADER_HEIGHT = 48;
const ADD_NODE_MENU_TAB_HEIGHT = 44;
const ADD_NODE_MENU_ITEM_HEIGHT = 56;
const ADD_NODE_MENU_LIST_PADDING = 16;
const ENABLED_ADDABLE_NODE_TYPES = new Set(["llm", "reply"]);
const TOOL_NODE_TYPE = "tool";
const DEFAULT_TOOL_MENU_ICON = "🧰";

// 可添加的节点类型
const addableNodeTypes = [
  {
    type: "llm",
    label: "LLM",
    icon: "\uD83E\uDD16",
    description: "AI 对话处理",
  },
  {
    type: "reply",
    label: "输出",
    icon: "\uD83D\uDCAC",
    description: "返回结果",
  },
  {
    type: "condition",
    label: "条件分支",
    icon: "\uD83D\uDD00",
    description: "条件判断",
  },
  {
    type: "loop",
    label: "循环",
    icon: "\u221e",
    description: "循环执行子流程",
  },

  {
    type: "http-request",
    label: "HTTP请求",
    icon: "\uD83C\uDF10",
    description: "调用外部 HTTP 接口",
  },
  {
    type: "document-extractor",
    label: "文档提取器",
    icon: "\uD83D\uDCC4",
    description: "解析并提取文档内容",
  },
  {
    type: "parameter-extractor",
    label: "参数提取器",
    icon: "\uD83E\uDDE9",
    description: "从文本中提取参数",
  },
  {
    type: "code-executor",
    label: "代码执行",
    icon: "\uD83D\uDCBB",
    description: "执行自定义代码",
  },
  {
    type: "iteration",
    label: "迭代",
    icon: "\uD83D\uDD01",
    description: "对数据集合进行迭代处理",
  },
  {
    type: "knowledge-retrieval",
    label: "知识检索",
    icon: "\uD83D\uDCDA",
    description: "检索知识库内容",
  },
  {
    type: "question-classifier",
    label: "问题分类器",
    icon: "\uD83E\uDDE0",
    description: "对问题进行分类判断",
  },
];

const addNodeMenuTabs = [
  // { key: "nodes", label: "节点" },
  // { key: "tools", label: "工具" },
];

const currentAddMenuItems = computed(() =>
  addNodeMenuActiveTab.value === "tools"
    ? toolMenuItems.value
    : addableNodeTypes.map((item) => ({
        ...item,
        menuType: "node",
      }))
);

const addNodeMenuEmptyText = computed(() => {
  if (addNodeMenuActiveTab.value !== "tools") {
    return "";
  }

  if (toolMenuLoading.value) {
    return "工具加载中...";
  }

  if (toolMenuLoadError.value) {
    return toolMenuLoadError.value;
  }

  return "暂无可用工具";
});

function isAddableNodeTypeEnabled(type = "") {
  const normalizedType = `${type || ""}`.trim();

  if (normalizedType === TOOL_NODE_TYPE) {
    return true;
  }

  return ENABLED_ADDABLE_NODE_TYPES.has(normalizedType);
}

function normalizeMenuToolIcon(value = "") {
  const normalizedValue = `${value || ""}`.trim();

  if (!normalizedValue || normalizedValue.length > 2) {
    return DEFAULT_TOOL_MENU_ICON;
  }

  return normalizedValue;
}

function normalizeToolMenuOutput(output = {}) {
  return createToolOutput({
    id: output.id,
    variableKey: output.variableKey || output.path || output.name || "result",
    variableLabel:
      output.variableLabel || output.variableKey || output.path || output.name,
    valueType: output.valueType || "string",
    path: output.path || output.variableKey || output.name || "result",
  });
}

function normalizeToolMenuItem(item = {}) {
  const toolId = `${item?.id || item?.toolId || item?.code || ""}`.trim();
  const toolName = `${
    item?.name || item?.toolName || item?.label || item?.title || ""
  }`.trim();
  const toolSource = `${
    item?.source || item?.provider || item?.providerName || ""
  }`.trim();
  const toolDescription = `${
    item?.description || item?.remark || item?.summary || ""
  }`.trim();
  const rawOutputs = Array.isArray(item?.output)
    ? item.output
    : Array.isArray(item?.outputs)
    ? item.outputs
    : Array.isArray(item?.outputVariables)
    ? item.outputVariables
    : [];

  if (!toolId && !toolName) {
    return null;
  }

  return {
    menuType: "tool",
    type: TOOL_NODE_TYPE,
    toolId,
    name: toolName || "工具",
    label: toolName || "工具",
    description: toolDescription || toolSource || "工具能力",
    source: toolSource,
    icon: normalizeMenuToolIcon(item?.icon || item?.emoji || item?.glyph),
    outputs: rawOutputs
      .filter(Boolean)
      .map((output) => normalizeToolMenuOutput(output)),
  };
}

function normalizeToolMenuList(response = {}) {
  const responseData =
    response && typeof response === "object" ? response.data : null;
  const rawList = Array.isArray(responseData?.rows)
    ? responseData.rows
    : Array.isArray(response?.rows)
    ? response.rows
    : Array.isArray(responseData)
    ? responseData
    : Array.isArray(response)
    ? response
    : [];

  return rawList.map((item) => normalizeToolMenuItem(item)).filter(Boolean);
}

async function ensureToolMenuItemsLoaded(force = false) {
  if (toolMenuLoading.value) {
    return;
  }

  if (!force && toolMenuLoaded.value) {
    return;
  }

  toolMenuLoading.value = true;
  toolMenuLoadError.value = "";

  try {
    const response = await listTool({
      pageNum: 1,
      pageSize: 200,
    });
    toolMenuItems.value = normalizeToolMenuList(response);
    toolMenuLoaded.value = true;
  } catch (error) {
    toolMenuItems.value = [];
    toolMenuLoadError.value = "工具列表加载失败，请稍后重试";
  } finally {
    toolMenuLoading.value = false;
  }
}

function handleAddNodeMenuTabChange(tabKey = "nodes") {
  addNodeMenuActiveTab.value = tabKey;

  if (tabKey === "tools") {
    ensureToolMenuItemsLoaded();
  }
}

function buildToolNodeDataFromMenuItem(menuItem = {}) {
  return normalizeToolNodeData({
    label: `${menuItem.label || menuItem.name || "工具"}`.trim() || "工具",
    description: `${menuItem.description || ""}`.trim(),
    toolId: `${menuItem.toolId || ""}`.trim(),
    toolName: `${menuItem.name || menuItem.label || "工具"}`.trim() || "工具",
    toolDescription: `${menuItem.description || ""}`.trim(),
    toolSource: `${menuItem.source || ""}`.trim(),
    toolIcon: `${menuItem.icon || ""}`.trim(),
    output:
      Array.isArray(menuItem.outputs) && menuItem.outputs.length
        ? menuItem.outputs.map((item) => normalizeToolOutput(item))
        : buildDefaultToolOutputs(),
  });
}

function handleAddMenuItemClick(menuItem = {}) {
  if (menuItem?.menuType === "tool") {
    addNodeAndConnect(TOOL_NODE_TYPE, buildToolNodeDataFromMenuItem(menuItem));
    return;
  }

  addNodeAndConnect(menuItem?.type || "");
}

const startFieldTypeOptions = [
  {
    value: "text",
    label: "文本",
    valueType: "string",
    icon: "T",
    defaultMaxLength: 48,
    supportsMaxLength: true,
  },
  {
    value: "paragraph",
    label: "段落",
    valueType: "string",
    icon: "P",
    defaultMaxLength: 200,
    supportsMaxLength: true,
  },
];
function routerView() {
  // todo 根据类型进行判断
  if (workflowType.value == 0){// 工作流
    router.push("/kb/bot/workflow?botType=0");
  }else if (workflowType.value == 1) {// Chatflow
    router.push("/kb/bot/chatflow?botType=1");
  }
}

function toggleChatflowDebugSections() {
  chatflowDebugSectionsVisible.value = !chatflowDebugSectionsVisible.value;
}

//复制Bot
function showCopyDialog() {
  title.value = "复制Bot";
  reset();
  form.value = { ...botDetail.value };
  form.value.name = botDetail.value.name + "（副本）";
  open.value = true;
}
// 取消按钮
function cancel() {
  open.value = false;
  reset();
}
// 表单重置
function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    name: null,
    type: null,
    description: null,
    builtinFlag: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };

  proxy.resetForm("botRef");
}

// 确定按钮
function submitForm() {
  proxy.$refs["botRef"].validate((valid) => {
    if (valid) {
      copyBot(form.value)
        .then((response) => {
          proxy.$modal.msgSuccess("复制成功");
          open.value = false;
          const newId = response.data;
          router.push({
            path:
              form.value.type === 2
                ? "/kb/bot/agent"
                : form.value.type === 3
                ? "/system/bot/codeNative"
                : "/kb/bot/processflow",
            query: {
              id: newId,
            },
          });
        })
        .catch((error) => {});
    }
  });
}

function openDebugRunPanel() {
  hideAddNodeMenu();

  if (!validateDebugNodeConfigs()) {
    return;
  }

  selectedNode.value = null;
  chatflowDebugSectionsVisible.value = true;
  debugRunPanelVisible.value = true;
}

function closeSidebarPanel() {
  selectedNode.value = null;
  debugRunPanelVisible.value = false;
}

function validateFlowBeforeRun() {
  if (!validateDebugNodeConfigs()) {
    return false;
  }

  return validateFlow({ showSuccess: false });
}

function walkDebugValidationNodes(nodeList = [], visitor = () => true) {
  const normalizedNodeList = Array.isArray(nodeList) ? nodeList : [];

  for (const node of normalizedNodeList) {
    if (!node || !node.type) {
      continue;
    }

    if (visitor(node) === false) {
      return false;
    }

    if (node.type !== "loop") {
      continue;
    }

    const loopNodes = getLoopNodes(node.data).filter(
      (loopNode) =>
        loopNode &&
        loopNode.type &&
        loopNode.type !== "loop-start" &&
        loopNode.id !== "loop-entry"
    );

    if (!walkDebugValidationNodes(loopNodes, visitor)) {
      return false;
    }
  }

  return true;
}

function hasInvalidReplyOutputConfig(data = {}) {
  const outputs = getReplyOutputs(data);

  return (
    !outputs.length ||
    outputs.some((output) => {
      const outputName = `${output?.name || ""}`.trim();
      const outputValue = `${output?.variableKey || output?.path || ""}`.trim();

      return !outputName || !outputValue;
    })
  );
}

function validateDebugNodeConfigs() {
  const flowData = buildFlowData();
  const flowNodes = Array.isArray(flowData?.nodes) ? flowData.nodes : [];

  return walkDebugValidationNodes(flowNodes, (node) => {
    if (
      node.type === "llm" &&
      !`${getNodeConfigValue(node.data, "prompt", "") || ""}`.trim()
    ) {
      proxy.$modal.msgWarning("LLM节点未设置提示词，请设置！");
      return false;
    }

    if (node.type === "reply") {
      if (isChatflowWorkflow.value) {
        if (!`${getReplyContent(node.data) || ""}`.trim()) {
          proxy.$modal.msgWarning("输出节点未设置返回结果，请设置！");
          return false;
        }
      } else if (hasInvalidReplyOutputConfig(node.data)) {
        proxy.$modal.msgWarning("输出节点未设置输出变量，请设置！");
        return false;
      }
    }

    return true;
  });
}

function getModelList() {
  getChatModelDict().then((res) => {
    if (res?.code !== 200 || !Array.isArray(res?.data)) {
      llmModelOptions.value = [];
      return;
    }

    llmModelOptions.value = res.data
      .map((provider) => {
        const providerLabel =
          `${
            provider?.label?.zh_Hans ||
            provider?.label?.zh_Hant ||
            provider?.label?.en ||
            provider?.provider ||
            ""
          }`.trim() || "模型厂商";
        const options = Array.isArray(provider?.models)
          ? provider.models
              .filter((model) => `${model?.model || ""}`.trim())
              .map((model) => {
                const modelName = `${model.model || ""}`.trim();
                const providerValue = normalizeModelProviderValue(
                  provider?.provider
                );

                return {
                  label: modelName,
                  value:
                    providerValue === ""
                      ? modelName
                      : `${providerValue}::${modelName}`,
                  model: modelName,
                  provider: providerValue,
                  providerLabel,
                };
              })
          : [];

        return {
          label: providerLabel,
          options,
        };
      })
      .filter((group) => group.options.length);
  });
}

function getFlowData(id) {
  // loading.value = true;
  getFlow(id).then((res) => {
    const normalizedFlow = normalizeWorkflowGraphIds({
      nodes: res.data.nodes,
      edges: res.data.edges,
    });

    if (
      Array.isArray(normalizedFlow.nodes) &&
      normalizedFlow.nodes.length > 0
    ) {
      nodes.value = normalizedFlow.nodes.map((node) => ({
        ...node,
        data: normalizeNodeData(node?.type, cloneNodeData(node?.data || {})),
      }));
    }

    if (
      Array.isArray(normalizedFlow.edges) &&
      normalizedFlow.edges.length > 0
    ) {
      edges.value = normalizedFlow.edges;
    }
    // loading.value = false;
  });
}

function getBotDetail(id) {
  botDetail.value = {};
  workflowType.value = null;
  flowName.value = "";
  flowBuiltinFlag.value = 0;
  getBot(id).then((res) => {
    botDetail.value = res.data;
    workflowType.value = res.data.type;
    flowName.value = res.data.name;
    flowBuiltinFlag.value = res.data.builtinFlag;
  });
}

watch(
  () => route.query.id,
  (newId) => {
    botId.value = newId;
    if (!newId) {
      return;
    }
    getBotDetail(newId);
    getFlowData(newId);
  },
  { immediate: true }
);

onMounted(() => {
  nodes.value = nodes.value.map((node) => ({
    ...node,
    data: normalizeNodeData(node?.type, cloneNodeData(node?.data || {})),
  }));
  getModelList();
  window.addEventListener("keydown", handleSelectedNodeDeleteKeydown);
});

onBeforeUnmount(() => {
  window.removeEventListener("keydown", handleSelectedNodeDeleteKeydown);
});

const selectedStartFieldCards = computed(() =>
  selectedNode.value?.type === "start"
    ? getStartFields(selectedNode.value.data).map((field) => ({
        ...field,
        typeLabel: getStartFieldTypeMeta(field.type).label,
        valueTypeLabel: getStartFieldValueTypeLabel(field.type),
      }))
    : []
);

const selectedConditionCases = computed(() =>
  selectedNode.value?.type === "condition"
    ? getConditionCases(selectedNode.value.data)
    : []
);
const selectedReplyOutputs = computed(() =>
  selectedNode.value?.type === "reply"
    ? getReplyOutputs(selectedNode.value.data)
    : []
);
const selectedReplyContent = computed(() =>
  selectedNode.value?.type === "reply"
    ? getReplyContent(selectedNode.value.data)
    : ""
);
const selectedToolOutputs = computed(() =>
  selectedNode.value?.type === TOOL_NODE_TYPE
    ? getToolOutputs(selectedNode.value.data)
    : []
);
const selectedLlmNodeView = computed(() =>
  selectedNode.value?.type === "llm"
    ? {
        ...selectedNode.value,
        data: {
          ...getNodeConfig(selectedNode.value.data),
          messages: getNodeInput(selectedNode.value.data),
          output: getNodeOutput(selectedNode.value.data),
        },
      }
    : null
);
const selectedLlmContextState = computed(() => getSelectedLlmContextState());
const selectedLlmContextVariableGroups = computed(
  () => selectedLlmContextState.value.groups
);
const selectedLlmHasUpstreamContextSources = computed(
  () => selectedLlmContextState.value.hasUpstreamContextSources
);
const selectedReplyContextState = computed(() =>
  getSelectedReplyContextState()
);
const selectedReplyContextVariableGroups = computed(
  () => selectedReplyContextState.value.groups
);
const selectedReplyHasUpstreamContextSources = computed(
  () => selectedReplyContextState.value.hasUpstreamContextSources
);
const drawerMode = computed(() =>
  debugRunPanelVisible.value ? "debug" : selectedNode.value ? "node" : ""
);
const drawerVisible = computed(() => drawerMode.value !== "");
function isChatflowWorkflowMode() {
  return Number(workflowType.value) === 1;
}

const isChatflowWorkflow = computed(() => isChatflowWorkflowMode());
const debugRunStartFields = computed(() => {
  const startNode = nodes.value.find((node) => node.type === "start");

  return startNode ? getStartFields(startNode.data) : [];
});
const debugRunWorkflowData = computed(() => buildFlowData());

const startFieldDialogTitle = computed(() =>
  startFieldDialogMode.value === "edit" ? "修改变量" : "新增变量"
);

const startFieldDraftTypeMeta = computed(() =>
  getStartFieldTypeMeta(startFieldDraft.value.type)
);
const START_FIELD_NAME_PATTERN = /^[A-Za-z_][A-Za-z0-9_.]*$/;
const CHATFLOW_DEFAULT_START_FIELD_ID = "chatflow-default-query-field";
const CHATFLOW_LEGACY_DEFAULT_START_FIELD_ID = "chatflow-default-user-field";

function findDuplicatedStartField(fieldName = "", currentFieldId = "") {
  if (selectedNode.value?.type !== "start") {
    return null;
  }

  const normalizedFieldName = `${fieldName || ""}`.trim().toLowerCase();

  if (!normalizedFieldName) {
    return null;
  }

  return getStartFields(selectedNode.value.data).find(
    (field) =>
      field.id !== currentFieldId &&
      field.name.trim().toLowerCase() === normalizedFieldName
  );
}

function validateStartFieldName(rule, value, callback) {
  const fieldName = `${value || ""}`.trim();

  if (!fieldName) {
    callback(new Error("请输入变量名称"));
    return;
  }

  if (!START_FIELD_NAME_PATTERN.test(fieldName)) {
    callback(
      new Error("变量名称需以字母或下划线开头，可包含数字、下划线和点号")
    );
    return;
  }

  if (findDuplicatedStartField(fieldName, startFieldDraft.value.id)) {
    callback(new Error("变量名称不能重复"));
    return;
  }

  callback();
}

function validateStartFieldLabel(rule, value, callback) {
  if (`${value || ""}`.trim()) {
    callback();
    return;
  }

  callback(new Error("请输入显示名称"));
}

function validateStartFieldMaxLength(rule, value, callback) {
  if (!startFieldDraftTypeMeta.value.supportsMaxLength) {
    callback();
    return;
  }

  const maxLength = Number(value);

  if (!Number.isInteger(maxLength) || maxLength <= 0) {
    callback(new Error("请输入大于 0 的最大长度"));
    return;
  }

  callback();
}

const startFieldDialogRules = {
  name: [
    {
      required: true,
      validator: validateStartFieldName,
      trigger: "blur",
    },
  ],
  label: [
    {
      required: true,
      validator: validateStartFieldLabel,
      trigger: "blur",
    },
  ],
  maxLength: [
    {
      validator: validateStartFieldMaxLength,
      trigger: "blur",
    },
  ],
};

function normalizeLoopPath(loopPath = []) {
  return Array.isArray(loopPath) ? loopPath.filter(Boolean) : [];
}

function areLoopPathsEqual(leftPath = [], rightPath = []) {
  const left = normalizeLoopPath(leftPath);
  const right = normalizeLoopPath(rightPath);

  if (left.length !== right.length) {
    return false;
  }

  return left.every((item, index) => item === right[index]);
}

function getSelectedLoopNodeId(loopPath = []) {
  if (selectedNode.value?.scope !== "loop-inner") {
    return null;
  }

  return areLoopPathsEqual(selectedNode.value.loopPath, loopPath)
    ? selectedNode.value.id
    : null;
}

function refreshNodeHandleLayout(nodeIds = []) {
  const nextIds = Array.isArray(nodeIds)
    ? [...new Set(nodeIds.filter(Boolean))]
    : [nodeIds].filter(Boolean);

  if (!nextIds.length) {
    return;
  }

  nextTick(() => {
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        updateNodeInternals(nextIds);
      });
    });
  });
}

function buildGeneratedId(prefix = "node", seed = 0) {
  return normalizeTemplateReferenceSegment(
    `${prefix}_${Date.now()}_${seed}`,
    prefix
  );
}

function buildNodeId(prefix = "node") {
  nodeSeed += 1;
  return buildGeneratedId(prefix, nodeSeed);
}

function buildCaseId(prefix = "case") {
  conditionCaseSeed += 1;
  return `${prefix}-${Date.now()}-${conditionCaseSeed}`;
}

function createConditionCase(isElse = false, overrides = {}) {
  return {
    id: buildCaseId(isElse ? "else" : "case"),
    expression: "",
    isElse,
    ...overrides,
  };
}

function normalizeConditionData(data = {}) {
  const rawCases = Array.isArray(getNodeConfigValue(data, "cases", null))
    ? getNodeConfigValue(data, "cases", [])
    : Array.isArray(data.cases)
    ? data.cases
    : [];
  const normalizedCases = rawCases.filter(Boolean).map((item) => ({
    id: item.id || buildCaseId(item.isElse ? "else" : "case"),
    expression: item.expression || "",
    isElse: Boolean(item.isElse),
  }));

  const branchCases = normalizedCases.filter((item) => !item.isElse);
  if (!branchCases.length) {
    branchCases.push(
      createConditionCase(false, { expression: data.expression || "" })
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

function buildStartFieldId(prefix = "start-field") {
  startFieldSeed += 1;
  return `${prefix}-${Date.now()}-${startFieldSeed}`;
}

function getStartFieldTypeMeta(type = "text") {
  return (
    startFieldTypeOptions.find((item) => item.value === type) ||
    startFieldTypeOptions[0]
  );
}

function createStartField(overrides = {}) {
  const meta = getStartFieldTypeMeta(overrides.type);
  const normalizedMaxLength = Number(overrides.maxLength);

  return {
    id: overrides.id || buildStartFieldId(meta.value),
    type: meta.value,
    name: overrides.name || "",
    label: overrides.label || "",
    maxLength: meta.supportsMaxLength
      ? Number.isFinite(normalizedMaxLength) && normalizedMaxLength > 0
        ? normalizedMaxLength
        : meta.defaultMaxLength
      : null,
    defaultValue:
      overrides.defaultValue === undefined || overrides.defaultValue === null
        ? ""
        : `${overrides.defaultValue}`,
    required:
      overrides.required === undefined ? true : Boolean(overrides.required),
    readonly: Boolean(overrides.readonly),
  };
}

function createChatflowDefaultStartField(overrides = {}) {
  return createStartField({
    ...overrides,
    id: CHATFLOW_DEFAULT_START_FIELD_ID,
    type: "text",
    name: "query",
    label: "用户",
    maxLength: 48,
    defaultValue: "",
    required: true,
    readonly: true,
  });
}

function isReadonlyStartField(field = {}) {
  return Boolean(field?.readonly);
}

function isChatflowDefaultStartField(field = {}) {
  const fieldId = `${field?.id || ""}`.trim();
  const fieldName = `${field?.name || ""}`.trim().toLowerCase();

  return (
    fieldId === CHATFLOW_DEFAULT_START_FIELD_ID ||
    fieldId === CHATFLOW_LEGACY_DEFAULT_START_FIELD_ID ||
    fieldName === "query" ||
    fieldName === "user"
  );
}

function normalizeStartField(field = {}) {
  const meta = getStartFieldTypeMeta(field.type);
  const nextField = createStartField(field);

  return {
    ...nextField,
    name: nextField.name.trim(),
    label: nextField.label.trim(),
    maxLength: meta.supportsMaxLength
      ? Number(nextField.maxLength) > 0
        ? Number(nextField.maxLength)
        : meta.defaultMaxLength
      : null,
  };
}

function normalizeStartFields(fields = []) {
  const normalizedFields = (Array.isArray(fields) ? fields : [])
    .filter(Boolean)
    .map((field) => normalizeStartField(field));

  if (!isChatflowWorkflowMode()) {
    return normalizedFields;
  }

  const customFields = [];
  let defaultFieldSource = null;

  normalizedFields.forEach((field) => {
    if (isChatflowDefaultStartField(field)) {
      if (!defaultFieldSource) {
        defaultFieldSource = field;
      }

      return;
    }

    customFields.push(field);
  });

  return [
    createChatflowDefaultStartField(defaultFieldSource || {}),
    ...customFields,
  ];
}

function normalizeStartNodeData(data = {}) {
  const structuredInput = getNodeInput(data);
  const rawFields = structuredInput.length
    ? structuredInput
    : Array.isArray(data.fields)
    ? data.fields
    : Array.isArray(data.inputFields)
    ? data.inputFields
    : [];
  const config = {
    ...omitObjectKeys(data, [
      "input",
      "config",
      "output",
      "fields",
      "inputFields",
    ]),
    ...getNodeConfig(data),
    label: getNodeLabel(data, "开始"),
    description: getNodeDescription(data),
  };

  return createStructuredNodeData({
    input: normalizeStartFields(rawFields),
    config,
    output: getNodeOutput(data),
  });
}

function getStartFields(data = {}) {
  return getNodeInput(normalizeStartNodeData(data));
}

function getStartFieldValueTypeLabel(type = "text") {
  return getStartFieldTypeMeta(type).valueType;
}

function getStartFieldDisplayLabel(field = {}) {
  return field.label || field.name || "未命名字段";
}

function buildContextSourceNodeId(nodeId = "", graphPath = []) {
  const normalizedNodeId = `${nodeId || ""}`.trim();

  if (!normalizedNodeId) {
    return "";
  }

  const segments = [...normalizeLoopPath(graphPath), normalizedNodeId].filter(
    Boolean
  );

  return segments.join("::");
}

function createGraphContext(
  nodeList = [],
  edgeList = [],
  graphPath = [],
  workflowData = null,
  containerNodeId = ""
) {
  return {
    nodeList: Array.isArray(nodeList) ? nodeList.filter(Boolean) : [],
    edgeList: Array.isArray(edgeList) ? edgeList.filter(Boolean) : [],
    graphPath: normalizeLoopPath(graphPath),
    workflowData: workflowData ? normalizeLoopData(workflowData) : null,
    containerNodeId: `${containerNodeId || ""}`.trim(),
  };
}

function getLoopWorkflowDataAtPath(loopPath = []) {
  const normalizedPath = normalizeLoopPath(loopPath);

  if (!normalizedPath.length) {
    return null;
  }

  let currentNodeList = nodes.value;
  let currentLoopData = null;

  for (const loopNodeId of normalizedPath) {
    const loopNode = currentNodeList.find(
      (node) => node?.id === loopNodeId && node.type === "loop"
    );

    if (!loopNode) {
      return null;
    }

    currentLoopData = normalizeLoopData(loopNode.data || {});
    currentNodeList = getLoopNodes(currentLoopData);
  }

  return currentLoopData;
}

function getGraphContextByLoopPath(loopPath = []) {
  const normalizedPath = normalizeLoopPath(loopPath);

  if (!normalizedPath.length) {
    return createGraphContext(nodes.value, edges.value);
  }

  const workflowData = getLoopWorkflowDataAtPath(normalizedPath);

  if (!workflowData) {
    return null;
  }

  return createGraphContext(
    getLoopNodes(workflowData),
    getLoopEdges(workflowData),
    normalizedPath,
    workflowData
  );
}

function getAncestorGraphContexts(loopPath = []) {
  const contexts = [];
  let currentLoopPath = normalizeLoopPath(loopPath);

  while (currentLoopPath.length) {
    const containerNodeId = currentLoopPath[currentLoopPath.length - 1];
    const parentLoopPath = currentLoopPath.slice(0, -1);

    if (!parentLoopPath.length) {
      contexts.push(
        createGraphContext(nodes.value, edges.value, [], null, containerNodeId)
      );
      break;
    }

    const workflowData = getLoopWorkflowDataAtPath(parentLoopPath);

    if (!workflowData) {
      break;
    }

    contexts.push(
      createGraphContext(
        getLoopNodes(workflowData),
        getLoopEdges(workflowData),
        parentLoopPath,
        workflowData,
        containerNodeId
      )
    );
    currentLoopPath = parentLoopPath;
  }

  return contexts;
}

function collectUpstreamContextSources(targetNodeId = "", graphContext = null) {
  const normalizedTargetNodeId = `${targetNodeId || ""}`.trim();

  if (!normalizedTargetNodeId || !graphContext) {
    return [];
  }

  const nodeList = Array.isArray(graphContext.nodeList)
    ? graphContext.nodeList
    : [];
  const edgeList = Array.isArray(graphContext.edgeList)
    ? graphContext.edgeList
    : [];
  const queue = [normalizedTargetNodeId];
  const visitedSourceIds = new Set([normalizedTargetNodeId]);
  const sources = [];

  while (queue.length) {
    const currentTargetId = queue.shift();
    const incomingEdges = edgeList.filter(
      (edge) =>
        `${edge?.target || ""}`.trim() === currentTargetId &&
        `${edge?.source || ""}`.trim()
    );

    incomingEdges.forEach((edge) => {
      const sourceNodeId = `${edge.source || ""}`.trim();

      if (!sourceNodeId || visitedSourceIds.has(sourceNodeId)) {
        return;
      }

      visitedSourceIds.add(sourceNodeId);

      const sourceNode = nodeList.find((node) => node?.id === sourceNodeId);

      if (!sourceNode) {
        return;
      }

      sources.push({
        node: sourceNode,
        graphPath: graphContext.graphPath,
        workflowData: graphContext.workflowData,
      });
      queue.push(sourceNodeId);
    });
  }

  return sources;
}

function mergeContextSources(...sourceLists) {
  const sourceMap = new Map();

  sourceLists
    .flat()
    .filter(Boolean)
    .forEach((source) => {
      const sourceKey = buildContextSourceNodeId(
        source?.node?.id,
        source?.graphPath
      );

      if (!sourceKey || sourceMap.has(sourceKey)) {
        return;
      }

      sourceMap.set(sourceKey, source);
    });

  return [...sourceMap.values()];
}

function getLoopEntryContextVariables(workflowData = null) {
  const itemAlias =
    `${
      getNodeConfigValue(workflowData, "itemAlias", "item") || "item"
    }`.trim() || "item";

  return [
    {
      variableKey: itemAlias,
      valueType: "item",
      path: itemAlias,
    },
  ];
}

function getContextVariablesForSource(source = {}) {
  const node = source?.node;

  if (!node) {
    return [];
  }

  if (node.type === "start") {
    return getStartFields(node.data)
      .filter((field) => `${field?.name || ""}`.trim())
      .map((field) => ({
        variableKey: `${field.name || ""}`.trim(),
        variableLabel: `${field.label || field.name || ""}`.trim(),
        valueType: getStartFieldValueTypeLabel(field.type),
        path: `${field.name || ""}`.trim(),
      }));
  }

  if (node.type === "loop-start" || node.id === "loop-entry") {
    return getLoopEntryContextVariables(source.workflowData).map((item) => ({
      ...item,
      variableLabel: `${item.variableLabel || item.variableKey || ""}`.trim(),
    }));
  }

  if (node.type === "llm") {
    return getLlmOutputs(node.data).map((item) => ({
      variableKey: `${item.variableKey || ""}`.trim(),
      variableLabel: `${item.variableLabel || item.variableKey || ""}`.trim(),
      valueType: `${item.valueType || ""}`.trim(),
      path: `${item.path || item.variableKey || ""}`.trim(),
    }));
  }

  if (node.type === TOOL_NODE_TYPE) {
    return getToolContextVariables(node.data);
  }

  if (node.type === "condition") {
    return [
      {
        variableKey: "branch_index",
        variableLabel: "命中分支索引",
        valueType: "number",
        path: "branch_index",
      },
      {
        variableKey: "branch_label",
        variableLabel: "命中分支名称",
        valueType: "string",
        path: "branch_label",
      },
    ];
  }

  if (node.type === "reply") {
    const replyContent = `${getReplyContent(node.data) || ""}`.trim();

    if (isChatflowWorkflow.value) {
      return replyContent
        ? [
            {
              variableKey: "text",
              variableLabel: "回复内容",
              valueType: "string",
              path: "text",
            },
          ]
        : [];
    }

    const replyOutputs = getReplyContextVariables(node.data);

    if (replyOutputs.length) {
      return replyOutputs;
    }

    return replyContent
      ? [
          {
            variableKey: "text",
            variableLabel: "回复内容",
            valueType: "string",
            path: "text",
          },
        ]
      : [];
  }

  return [];
}

function getContextSourceLabel(source = {}) {
  const node = source?.node;

  if (!node) {
    return "上游节点";
  }

  return getNodeLabel(node?.data, getNodeTypeLabel(node.type || ""));
}

function buildContextVariableGroup(source = {}) {
  const node = source?.node;

  if (!node?.id) {
    return null;
  }

  const normalizedNodeType =
    node.type === "loop-start" || node.id === "loop-entry"
      ? "loop-entry"
      : `${node.type || ""}`.trim();
  const sourceNodeId = buildContextSourceNodeId(node.id, source.graphPath);
  const sourceNodeLabel = getContextSourceLabel(source);
  const variables = getContextVariablesForSource(source)
    .map((item) => {
      const variableKey = `${item.variableKey || ""}`.trim();
      const path = `${item.path || variableKey}`.trim();

      if (!variableKey || !path) {
        return null;
      }

      return {
        sourceNodeId,
        sourceNodeLabel,
        sourceNodeType: normalizedNodeType,
        variableKey,
        variableLabel: `${item.variableLabel || variableKey}`.trim(),
        valueType: `${item.valueType || ""}`.trim(),
        path,
      };
    })
    .filter(Boolean);

  if (!variables.length) {
    return null;
  }

  return {
    nodeId: sourceNodeId,
    nodeLabel: sourceNodeLabel,
    nodeType: normalizedNodeType,
    variables,
  };
}

function getSelectedNodeContextState(allowedNodeTypes = []) {
  if (
    !selectedNode.value?.id ||
    (allowedNodeTypes.length &&
      !allowedNodeTypes.includes(selectedNode.value?.type || ""))
  ) {
    return {
      groups: [],
      hasUpstreamContextSources: false,
    };
  }

  const currentGraphContext =
    selectedNode.value.scope === "loop-inner"
      ? getGraphContextByLoopPath(selectedNode.value.loopPath)
      : createGraphContext(nodes.value, edges.value);

  if (!currentGraphContext) {
    return {
      groups: [],
      hasUpstreamContextSources: false,
    };
  }

  const currentSources = collectUpstreamContextSources(
    selectedNode.value.id,
    currentGraphContext
  );
  const ancestorSources =
    selectedNode.value.scope === "loop-inner"
      ? getAncestorGraphContexts(selectedNode.value.loopPath).flatMap(
          (graphContext) =>
            graphContext?.containerNodeId
              ? collectUpstreamContextSources(
                  graphContext.containerNodeId,
                  graphContext
                )
              : []
        )
      : [];
  const upstreamSources = mergeContextSources(currentSources, ancestorSources);
  const groups = upstreamSources
    .map((source) => buildContextVariableGroup(source))
    .filter(Boolean);

  return {
    groups,
    hasUpstreamContextSources: upstreamSources.length > 0,
  };
}

function getSelectedLlmContextState() {
  return getSelectedNodeContextState(["llm"]);
}

function getSelectedReplyContextState() {
  return getSelectedNodeContextState(["reply"]);
}

function getStartFieldPreview(data = {}, limit = 3) {
  return getStartFields(data).slice(0, limit);
}

function getStartFieldOverflowCount(data = {}, limit = 3) {
  return Math.max(0, getStartFields(data).length - limit);
}

function buildReplyOutputId(prefix = "reply-output") {
  replyOutputSeed += 1;
  return `${prefix}-${Date.now()}-${replyOutputSeed}`;
}

function createReplyOutput(overrides = {}) {
  const binding =
    overrides.binding && typeof overrides.binding === "object"
      ? overrides.binding
      : {};

  return {
    id: overrides.id || buildReplyOutputId(),
    name: `${overrides.name || overrides.variableName || ""}`,
    sourceNodeId: `${overrides.sourceNodeId || binding.sourceNodeId || ""}`,
    sourceNodeLabel: `${
      overrides.sourceNodeLabel || binding.sourceNodeLabel || ""
    }`,
    sourceNodeType: `${
      overrides.sourceNodeType || binding.sourceNodeType || ""
    }`,
    variableKey: `${overrides.variableKey || binding.variableKey || ""}`,
    variableLabel: `${
      overrides.variableLabel ||
      binding.variableLabel ||
      overrides.variableKey ||
      binding.variableKey ||
      ""
    }`,
    valueType: `${overrides.valueType || binding.valueType || ""}`,
    path: `${
      overrides.path ||
      binding.path ||
      overrides.variableKey ||
      binding.variableKey ||
      ""
    }`,
  };
}

function normalizeReplyOutput(output = {}) {
  const nextOutput = createReplyOutput(output);

  return {
    ...nextOutput,
    name: nextOutput.name.trim(),
    sourceNodeId: nextOutput.sourceNodeId.trim(),
    sourceNodeLabel: nextOutput.sourceNodeLabel.trim(),
    sourceNodeType: nextOutput.sourceNodeType.trim(),
    variableKey: nextOutput.variableKey.trim(),
    variableLabel: (nextOutput.variableLabel || nextOutput.variableKey).trim(),
    valueType: nextOutput.valueType.trim(),
    path: (nextOutput.path || nextOutput.variableKey).trim(),
  };
}

function getReplyNodeLabel(data = {}, fallback = "输出") {
  const label = `${getNodeLabel(data, "") || ""}`.trim();

  if (!label || label === "直接回复" || label === "直接回复") {
    return fallback;
  }

  return label;
}

function buildReplyPreviewReferenceSegment(value = "", fallback = "node") {
  const normalized = `${value || ""}`
    .trim()
    .replace(/[^A-Za-z0-9_]+/g, "_")
    .replace(/^_+|_+$/g, "");
  const nextValue = normalized || fallback;

  return /^[0-9]/.test(nextValue) ? `node_${nextValue}` : nextValue;
}

function buildReplyPreviewVariableReferenceKey(
  sourceSegment = "",
  variablePath = ""
) {
  return `${sourceSegment || ""}::${variablePath || ""}`;
}

function getReplyPreviewVariableFallbackLabel(
  variablePath = "",
  sourceSegment = ""
) {
  const normalizedPath = `${variablePath || ""}`.trim();

  if (!normalizedPath) {
    return `${sourceSegment || ""}`.trim();
  }

  const pathSegments = normalizedPath.split(".").filter(Boolean);
  return pathSegments[pathSegments.length - 1] || normalizedPath;
}

function getReplyPreviewVariableOptionMap(nodeId = "") {
  const normalizedNodeId = `${nodeId || ""}`.trim();

  if (!normalizedNodeId) {
    return new Map();
  }

  const currentGraphContext = createGraphContext(nodes.value, edges.value);

  if (!currentGraphContext) {
    return new Map();
  }

  const groups = collectUpstreamContextSources(
    normalizedNodeId,
    currentGraphContext
  )
    .map((source) => buildContextVariableGroup(source))
    .filter(Boolean);
  const optionMap = new Map();

  groups.forEach((group) => {
    (group.variables || []).forEach((option) => {
      const sourceSegment = buildReplyPreviewReferenceSegment(
        option.sourceNodeId || option.sourceNodeType || "node",
        "node"
      );
      const variablePath = `${option.path || option.variableKey || ""}`.trim();

      if (!sourceSegment || !variablePath) {
        return;
      }

      optionMap.set(
        buildReplyPreviewVariableReferenceKey(sourceSegment, variablePath),
        option
      );
    });
  });

  return optionMap;
}

function getReplyNodePreviewSegments(data = {}, nodeId = "") {
  if (!isChatflowWorkflow.value) {
    return [];
  }

  const content = `${getReplyContent(data) || ""}`;

  if (!content.trim()) {
    return [];
  }

  const optionMap = getReplyPreviewVariableOptionMap(nodeId);
  const variablePattern = /\{\{\s*([A-Za-z0-9_]+)\.([A-Za-z0-9_.]+)\s*\}\}/g;
  const segments = [];
  let lastIndex = 0;
  let match = variablePattern.exec(content);

  while (match) {
    const [rawValue, sourceSegment, variablePath] = match;

    if (match.index > lastIndex) {
      segments.push({
        type: "text",
        value: content.slice(lastIndex, match.index),
      });
    }

    const binding = optionMap.get(
      buildReplyPreviewVariableReferenceKey(sourceSegment, variablePath)
    );

    segments.push({
      type: "variable",
      rawValue,
      display:
        binding?.variableLabel ||
        binding?.variableKey ||
        getReplyPreviewVariableFallbackLabel(variablePath, sourceSegment),
    });

    lastIndex = match.index + rawValue.length;
    match = variablePattern.exec(content);
  }

  if (lastIndex < content.length) {
    segments.push({
      type: "text",
      value: content.slice(lastIndex),
    });
  }

  return segments.length ? segments : [{ type: "text", value: content }];
}

function getReplyPreviewText(content = "", limit = 30) {
  const normalizedContent = `${content || ""}`.replace(/\s+/g, " ").trim();

  if (!normalizedContent) {
    return "";
  }

  return normalizedContent.length > limit
    ? `${normalizedContent.slice(0, limit)}...`
    : normalizedContent;
}

function normalizeReplyNodeData(data = {}) {
  const structuredOutput = getNodeOutput(data);
  const rawOutputs = structuredOutput.length
    ? structuredOutput
    : Array.isArray(data.outputs)
    ? data.outputs
    : Array.isArray(data.outputVariables)
    ? data.outputVariables
    : [];
  const config = {
    ...omitObjectKeys(data, [
      "input",
      "config",
      "output",
      "outputs",
      "outputVariables",
      "content",
      "description",
      "label",
    ]),
    ...getNodeConfig(data),
    label: getReplyNodeLabel(data),
    description: getNodeDescription(data),
    content:
      typeof getNodeConfigValue(data, "content", "") === "string"
        ? getNodeConfigValue(data, "content", "")
        : "",
  };

  return createStructuredNodeData({
    input: getNodeInput(data),
    config,
    output: rawOutputs
      .filter(Boolean)
      .map((output) => normalizeReplyOutput(output)),
  });
}

function getReplyContent(data = {}) {
  return `${
    getNodeConfigValue(normalizeReplyNodeData(data), "content", "") || ""
  }`;
}

function getReplyOutputs(data = {}) {
  return getNodeOutput(normalizeReplyNodeData(data));
}

function getReplyContextVariables(data = {}) {
  return getReplyOutputs(data)
    .map((output) => {
      const outputName = `${output.name || ""}`.trim();

      if (!outputName) {
        return null;
      }

      return {
        variableKey: outputName,
        variableLabel: outputName,
        valueType: `${output.valueType || ""}`.trim(),
        path: outputName,
      };
    })
    .filter(Boolean);
}

function getReplyNodePreviewItems(data = {}) {
  if (isChatflowWorkflow.value) {
    return getReplyPreviewText(getReplyContent(data));
  }

  const replyOutputs = getReplyOutputs(data);

  if (replyOutputs.length) {
    const firstOutput = replyOutputs[0];
    const variableKey = `${
      firstOutput.variableLabel || firstOutput.variableKey || ""
    }`.trim();

    if (variableKey) {
      return variableKey;
    }

    if (firstOutput.name) {
      return firstOutput.name;
    }
  }

  const legacyContent = `${getReplyContent(data) || ""}`.trim();
  return legacyContent ? `@${legacyContent}` : "";
}

function getReplyNodePreviewList(data = {}, limit = null) {
  let previewList = [];

  if (isChatflowWorkflow.value) {
    const replyPreviewText = getReplyPreviewText(getReplyContent(data));

    previewList = replyPreviewText ? [replyPreviewText] : [];
  } else {
    const replyOutputs = getReplyOutputs(data);

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
      const legacyPreviewText = `${
        getReplyNodePreviewItems(data) || ""
      }`.trim();
      previewList = legacyPreviewText ? [legacyPreviewText] : [];
    }
  }

  if (Number.isFinite(limit)) {
    return previewList.slice(0, Math.max(0, Math.floor(limit)));
  }

  return previewList;
}

function getReplyNodePreviewOverflowCount(data = {}, limit = 3) {
  return Math.max(0, getReplyNodePreviewList(data).length - limit);
}

function buildToolOutputId(prefix = "tool-output") {
  toolOutputSeed += 1;
  return `${prefix}-${Date.now()}-${toolOutputSeed}`;
}

function createToolOutput(overrides = {}) {
  const variableKey =
    `${overrides.variableKey || overrides.path || "result"}`.trim() || "result";

  return {
    id: overrides.id || buildToolOutputId(),
    variableKey,
    variableLabel:
      `${overrides.variableLabel || variableKey}`.trim() || variableKey,
    valueType: `${overrides.valueType || "string"}`.trim() || "string",
    path: `${overrides.path || variableKey}`.trim() || variableKey,
  };
}

function normalizeToolOutput(output = {}) {
  const nextOutput = createToolOutput(output);

  return {
    ...nextOutput,
    variableKey: nextOutput.variableKey.trim() || "result",
    variableLabel:
      (nextOutput.variableLabel || nextOutput.variableKey).trim() ||
      nextOutput.variableKey,
    valueType: nextOutput.valueType.trim() || "string",
    path:
      (nextOutput.path || nextOutput.variableKey).trim() ||
      nextOutput.variableKey,
  };
}

function buildDefaultToolOutputs() {
  return [
    normalizeToolOutput({
      variableKey: "result",
      variableLabel: "result",
      valueType: "string",
      path: "result",
    }),
  ];
}

function normalizeToolNodeData(data = {}) {
  const structuredOutput = getNodeOutput(data);
  const hasExplicitOutput =
    structuredOutput.length > 0 ||
    Array.isArray(data.output) ||
    Array.isArray(data.outputs) ||
    Array.isArray(data.outputVariables);
  const rawOutputs = structuredOutput.length
    ? structuredOutput
    : Array.isArray(data.output)
    ? data.output
    : Array.isArray(data.outputs)
    ? data.outputs
    : Array.isArray(data.outputVariables)
    ? data.outputVariables
    : [];
  const config = {
    ...omitObjectKeys(data, [
      "input",
      "config",
      "output",
      "outputs",
      "outputVariables",
      "label",
      "description",
      "toolId",
      "toolName",
      "toolDescription",
      "toolSource",
      "toolIcon",
    ]),
    ...getNodeConfig(data),
    label:
      getNodeLabel(data, getNodeConfigValue(data, "toolName", "工具")) ||
      "工具",
    description:
      getNodeDescription(data) ||
      `${getNodeConfigValue(data, "toolDescription", "") || ""}`.trim(),
    toolId: `${getNodeConfigValue(data, "toolId", "") || ""}`.trim(),
    toolName:
      `${
        getNodeConfigValue(data, "toolName", getNodeLabel(data, "工具")) || ""
      }`.trim() || "工具",
    toolDescription: `${
      getNodeConfigValue(data, "toolDescription", "") || ""
    }`.trim(),
    toolSource: `${getNodeConfigValue(data, "toolSource", "") || ""}`.trim(),
    toolIcon: `${getNodeConfigValue(data, "toolIcon", "") || ""}`.trim(),
  };

  return createStructuredNodeData({
    input: getNodeInput(data),
    config,
    output: hasExplicitOutput
      ? rawOutputs.filter(Boolean).map((item) => normalizeToolOutput(item))
      : buildDefaultToolOutputs(),
  });
}

function getToolOutputs(data = {}) {
  return getNodeOutput(normalizeToolNodeData(data));
}

function getToolContextVariables(data = {}) {
  return getToolOutputs(data)
    .map((output) => {
      const variableKey = `${output.variableKey || output.path || ""}`.trim();

      if (!variableKey) {
        return null;
      }

      return {
        variableKey,
        variableLabel: `${
          output.variableLabel || output.variableKey || variableKey
        }`.trim(),
        valueType: `${output.valueType || ""}`.trim() || "string",
        path: `${output.path || variableKey}`.trim() || variableKey,
      };
    })
    .filter(Boolean);
}

function getToolNodeLabel(data = {}, fallback = "工具") {
  return (
    `${
      getNodeLabel(data, getNodeConfigValue(data, "toolName", fallback)) || ""
    }`.trim() || fallback
  );
}

function getToolNodeSubtitle(data = {}) {
  const toolSource = `${
    getNodeConfigValue(data, "toolSource", "") || ""
  }`.trim();
  return toolSource;
}

function getToolNodePreviewList(data = {}, limit = null) {
  const previewList = getToolOutputs(data)
    .map((output) => {
      const variableLabel = `${
        output.variableLabel || output.variableKey || output.path || ""
      }`.trim();

      return variableLabel;
    })
    .filter(Boolean);

  if (Number.isFinite(limit)) {
    return previewList.slice(0, Math.max(0, Math.floor(limit)));
  }

  return previewList;
}

function getToolNodePreviewOverflowCount(data = {}, limit = 3) {
  return Math.max(0, getToolNodePreviewList(data).length - limit);
}

function buildLlmMessageId(prefix = "llm-message") {
  llmMessageSeed += 1;
  return `${prefix}-${Date.now()}-${llmMessageSeed}`;
}

function normalizeLlmMessageRole(role = "user") {
  const normalizedRole = String(role || "").toLowerCase();
  return normalizedRole === "assistant" ? "assistant" : "user";
}

function createLlmMessage(overrides = {}) {
  return {
    id: overrides.id || buildLlmMessageId(),
    role: normalizeLlmMessageRole(overrides.role),
    content: overrides.content || "",
  };
}

function normalizeLlmOutputItem(output = {}) {
  return {
    variableKey:
      `${output.variableKey || output.path || "text"}`.trim() || "text",
    variableLabel:
      `${
        output.variableLabel || output.variableKey || output.path || "text"
      }`.trim() || "text",
    valueType: `${output.valueType || ""}`.trim(),
    path: `${output.path || output.variableKey || "text"}`.trim() || "text",
  };
}

function buildLlmOutputItems(config = {}) {
  const outputs = [
    {
      variableKey: "text",
      variableLabel: "text",
      valueType: "string",
      path: "text",
    },
  ];

  if (config.reasoningTagEnabled) {
    outputs.push({
      variableKey: "reasoning_content",
      variableLabel: "reasoning_content",
      valueType: "string",
      path: "reasoning_content",
    });
  }

  if (
    Boolean(config.structuredOutputEnabled) ||
    config.responseFormat === "json_object"
  ) {
    outputs.push({
      variableKey: "json",
      variableLabel: "json",
      valueType: "object",
      path: "json",
    });
  }

  return outputs.map((item) => normalizeLlmOutputItem(item));
}

function normalizeLlmNodeData(data = {}) {
  const structuredInput = getNodeInput(data);
  const rawMessages = structuredInput.length
    ? structuredInput
    : Array.isArray(data.messages)
    ? data.messages
    : [];
  const normalizeNumber = (value, fallback) => {
    const nextValue = Number(value);
    return Number.isFinite(nextValue) ? nextValue : fallback;
  };
  const defaultModelConfig = getDefaultLlmModelConfig();
  const configuredModel = `${
    getNodeConfigValue(data, "model", "") || ""
  }`.trim();
  const configuredProvider = normalizeModelProviderValue(
    getNodeConfigValue(data, "provider", "")
  );
  const config = {
    ...omitObjectKeys(data, [
      "input",
      "config",
      "output",
      "messages",
      "contextVariables",
      "label",
      "description",
      "provider",
      "model",
      "prompt",
      "temperature",
      "maxTokens",
      "topP",
      "logprobs",
      "topLogprobs",
      "frequencyPenalty",
      "responseFormat",
      "stopSequences",
      "memoryEnabled",
      "visionEnabled",
      "reasoningTagEnabled",
      "structuredOutputEnabled",
      "retryEnabled",
      "errorStrategy",
    ]),
    ...getNodeConfig(data),
    label: getNodeLabel(data, "LLM"),
    description: getNodeDescription(data),
    provider: configuredModel
      ? configuredProvider
      : normalizeModelProviderValue(defaultModelConfig.provider),
    model: configuredModel || defaultModelConfig.model,
    prompt: getNodeConfigValue(data, "prompt", ""),
    temperature: normalizeNumber(getNodeConfigValue(data, "temperature", 1), 1),
    maxTokens: Math.max(
      1,
      Math.round(
        normalizeNumber(getNodeConfigValue(data, "maxTokens", 4096), 4096)
      )
    ),
    topP: normalizeNumber(getNodeConfigValue(data, "topP", 1), 1),
    logprobs: Boolean(getNodeConfigValue(data, "logprobs", false)),
    topLogprobs: Math.max(
      0,
      Math.round(normalizeNumber(getNodeConfigValue(data, "topLogprobs", 0), 0))
    ),
    frequencyPenalty: normalizeNumber(
      getNodeConfigValue(data, "frequencyPenalty", 0),
      0
    ),
    responseFormat:
      typeof getNodeConfigValue(data, "responseFormat", "") === "string"
        ? getNodeConfigValue(data, "responseFormat", "")
        : "",
    stopSequences:
      typeof getNodeConfigValue(data, "stopSequences", "") === "string"
        ? getNodeConfigValue(data, "stopSequences", "")
        : "",
    memoryEnabled: Boolean(getNodeConfigValue(data, "memoryEnabled", false)),
    visionEnabled: Boolean(getNodeConfigValue(data, "visionEnabled", false)),
    reasoningTagEnabled: Boolean(
      getNodeConfigValue(data, "reasoningTagEnabled", false)
    ),
    structuredOutputEnabled: Boolean(
      getNodeConfigValue(data, "structuredOutputEnabled", false)
    ),
    retryEnabled: Boolean(getNodeConfigValue(data, "retryEnabled", false)),
    errorStrategy: getNodeConfigValue(data, "errorStrategy", "none") || "none",
  };
  const structuredOutput = getNodeOutput(data);

  return createStructuredNodeData({
    input: rawMessages
      .filter(Boolean)
      .map((message) => createLlmMessage(message)),
    config,
    output: structuredOutput.length
      ? structuredOutput
          .filter(Boolean)
          .map((item) => normalizeLlmOutputItem(item))
      : buildLlmOutputItems(config),
  });
}

function getLlmOutputs(data = {}) {
  return getNodeOutput(normalizeLlmNodeData(data));
}

function normalizeNodeData(type, data = {}) {
  if (type === "start") {
    return normalizeStartNodeData(data);
  }

  if (type === "llm") {
    return normalizeLlmNodeData(data);
  }

  if (type === TOOL_NODE_TYPE) {
    return normalizeToolNodeData(data);
  }

  if (type === "condition") {
    return normalizeConditionData(data);
  }

  if (type === "reply") {
    return normalizeReplyNodeData(data);
  }

  if (type === "loop") {
    return normalizeLoopData(data);
  }

  return data;
}

function getConditionCases(data = {}) {
  return getNodeConfigValue(normalizeConditionData(data), "cases", []);
}

function getConditionCaseLabel(index) {
  return `CASE ${index + 1}`;
}

function getConditionBranchLabel(index, total) {
  if (index === 0) return "IF";
  if (index === total - 1) return "ELSE";
  return "ELIF";
}

function getConditionHandleId(caseId) {
  return `condition-case-${caseId}`;
}

function getConditionDefaultSourceHandle(data = {}) {
  const firstCase = getConditionCases(data)[0];
  return firstCase ? getConditionHandleId(firstCase.id) : null;
}

function getNodeTypeLabel(nodeType) {
  const labels = {
    start: "开始",
    llm: "LLM",
    reply: "回复",
    tool: "工具",
    condition: "条件分支",
    loop: "循环",
  };

  return labels[nodeType] || "节点";
}

function buildLoopStepId(prefix = "loop-node") {
  loopStepSeed += 1;
  return buildGeneratedId(prefix, loopStepSeed);
}

function createLoopFlowEdge(connection) {
  return {
    ...connection,
    id:
      connection.id ||
      createEdgeId(
        connection.source,
        connection.target,
        connection.sourceHandle,
        connection.targetHandle
      ),
    type: "default",
  };
}

function createLoopEntryNode(overrides = {}) {
  return {
    id: "loop-entry",
    type: "loop-start",
    position: overrides.position || { x: 24, y: 46 },
    draggable: false,
    selectable: false,
    ...overrides,
    data: createStructuredNodeData({
      config: {
        label: getNodeLabel(overrides.data, "开始"),
      },
    }),
  };
}

function createLoopFlowNodeData(nodeType = "llm", index = 0, overrides = {}) {
  if (nodeType === "condition") {
    return normalizeConditionData({
      label: overrides.label || `条件分支 ${index + 1}`,
      ...overrides,
    });
  }

  if (nodeType === "reply") {
    return normalizeReplyNodeData({
      label: overrides.label || `回复 ${index + 1}`,
      output: [],
      ...overrides,
    });
  }

  if (nodeType === "loop") {
    return normalizeLoopData({
      label: overrides.label || `循环 ${index + 1}`,
      description: getNodeDescription(overrides),
      ...overrides,
    });
  }

  const defaultModelConfig = getDefaultLlmModelConfig();
  return normalizeLlmNodeData({
    label: overrides.label || `LLM ${index + 1}`,
    ...defaultModelConfig,
    description: getNodeDescription(overrides),
    prompt: "",
    ...overrides,
  });
}

function createLoopFlowNode(nodeType = "llm", index = 0, overrides = {}) {
  return {
    id: overrides.id || buildLoopStepId(nodeType),
    type: nodeType,
    position: overrides.position || { x: 124 + index * 188, y: 42 },
    data: createLoopFlowNodeData(nodeType, index, overrides.data || {}),
    ...overrides,
  };
}

function normalizeLoopFlowNode(node, index = 0) {
  if (!node || node.type === "loop-start" || node.id === "loop-entry") {
    return createLoopEntryNode(node || {});
  }

  const nextType = node.type || "llm";
  const nextNode = {
    id: node.id || buildLoopStepId(nextType),
    type: nextType,
    position: node.position || { x: 124 + index * 188, y: 42 },
    data: cloneNodeData(node.data || {}),
  };

  if (nextType === "condition") {
    nextNode.data = normalizeConditionData(nextNode.data);
    nextNode.data = setNodeConfigValue(
      nextNode.data,
      "label",
      getNodeLabel(nextNode.data, `条件分支 ${index + 1}`)
    );
    return nextNode;
  }

  if (nextType === "loop") {
    nextNode.data = normalizeLoopData(nextNode.data);
    nextNode.data = setNodeConfigValue(
      nextNode.data,
      "label",
      getNodeLabel(nextNode.data, `循环 ${index + 1}`)
    );
    return nextNode;
  }

  if (nextType === "reply") {
    nextNode.data = normalizeReplyNodeData({
      label: getReplyNodeLabel(nextNode.data, `输出 ${index + 1}`),
      ...nextNode.data,
    });
    return nextNode;
  }

  const defaultModelConfig = getDefaultLlmModelConfig();
  nextNode.data = normalizeLlmNodeData({
    label: getNodeLabel(nextNode.data, `LLM ${index + 1}`),
    provider: getNodeConfigValue(
      nextNode.data,
      "provider",
      defaultModelConfig.provider
    ),
    model: getNodeConfigValue(nextNode.data, "model", defaultModelConfig.model),
    description: getNodeDescription(nextNode.data),
    prompt: getNodeConfigValue(nextNode.data, "prompt", ""),
    ...nextNode.data,
  });
  return nextNode;
}

function buildLoopNodesFromLegacySteps(steps = []) {
  return [
    createLoopEntryNode(),
    ...steps.map((step, index) =>
      normalizeLoopFlowNode(
        createLoopFlowNode(step.type || "llm", index, {
          data: createLoopFlowNodeData(step.type || "llm", index, {
            label:
              step.label ||
              `${getNodeTypeLabel(step.type || "llm")} ${index + 1}`,
          }),
        }),
        index
      )
    ),
  ];
}

function buildLoopEdgesFromNodes(loopNodes = []) {
  const sequence = loopNodes.filter(Boolean);
  const nextEdges = [];

  for (let index = 0; index < sequence.length - 1; index += 1) {
    const currentNode = sequence[index];
    const targetNode = sequence[index + 1];
    const connection = {
      source: currentNode.id,
      target: targetNode.id,
    };

    if (currentNode.type === "condition") {
      connection.sourceHandle = getConditionDefaultSourceHandle(
        currentNode.data
      );
    }

    nextEdges.push(createLoopFlowEdge(connection));
  }

  return nextEdges;
}

function normalizeLoopData(data = {}) {
  const legacySteps = Array.isArray(data?.steps)
    ? data.steps
    : Array.isArray(getNodeConfigValue(data, "steps", null))
    ? getNodeConfigValue(data, "steps", [])
    : [];
  const rawLoopNodes = Array.isArray(
    getNodeConfigValue(data, "loopNodes", null)
  )
    ? getNodeConfigValue(data, "loopNodes", [])
    : Array.isArray(data.loopNodes)
    ? data.loopNodes
    : [];
  const rawLoopEdges = Array.isArray(
    getNodeConfigValue(data, "loopEdges", null)
  )
    ? getNodeConfigValue(data, "loopEdges", [])
    : Array.isArray(data.loopEdges)
    ? data.loopEdges
    : [];
  let nextLoopNodes = Array.isArray(rawLoopNodes)
    ? cloneNodeData(rawLoopNodes)
    : [];
  let nextLoopEdges = Array.isArray(rawLoopEdges)
    ? cloneNodeData(rawLoopEdges)
    : [];

  if (
    !nextLoopNodes.length &&
    Array.isArray(legacySteps) &&
    legacySteps.length
  ) {
    nextLoopNodes = buildLoopNodesFromLegacySteps(legacySteps);
    nextLoopEdges = buildLoopEdgesFromNodes(nextLoopNodes);
  }

  const entryNode = nextLoopNodes.find(
    (node) => node?.type === "loop-start" || node?.id === "loop-entry"
  );
  const bodyNodes = nextLoopNodes
    .filter((node) => node && node.id !== (entryNode?.id || "loop-entry"))
    .map((node, index) => normalizeLoopFlowNode(node, index));
  const normalizedLoopNodes = [
    normalizeLoopFlowNode(entryNode, 0),
    ...bodyNodes,
  ];
  const loopNodeIds = new Set(normalizedLoopNodes.map((node) => node.id));
  const normalizedLoopEdges = nextLoopEdges
    .filter(
      (edge) =>
        edge && loopNodeIds.has(edge.source) && loopNodeIds.has(edge.target)
    )
    .map((edge) => createLoopFlowEdge(edge));
  const maxIterations = Number(getNodeConfigValue(data, "maxIterations", 10));

  return createStructuredNodeData({
    input: getNodeInput(data),
    config: {
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
      loopNodes: normalizedLoopNodes,
      loopEdges: normalizedLoopEdges,
    },
    output: getNodeOutput(data),
  });
}

function getLoopNodes(data = {}) {
  return getNodeConfigValue(normalizeLoopData(data), "loopNodes", []);
}

function getLoopEdges(data = {}) {
  return getNodeConfigValue(normalizeLoopData(data), "loopEdges", []);
}

function getLoopCanvasLayout(compactMode = true) {
  return compactMode
    ? {
        minWidth: 292,
        minHeight: 118,
        paddingTop: 18,
        paddingRight: 26,
        paddingBottom: 18,
        paddingLeft: 18,
      }
    : {
        minWidth: 680,
        minHeight: 300,
        paddingTop: 30,
        paddingRight: 52,
        paddingBottom: 34,
        paddingLeft: 34,
      };
}

function getLoopCanvasNodeSize(node, compactMode = true) {
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
    return { width: 200, height: 112 };
  }

  if (node?.type === "loop") {
    const metrics = getLoopCanvasMetrics(node?.data || {}, true);
    return {
      width: metrics.width + 32,
      height: metrics.height + 60,
    };
  }

  return { width: 200, height: 112 };
}

function getLoopCanvasMetrics(data = {}, compactMode = true) {
  const layout = getLoopCanvasLayout(compactMode);
  const loopNodes = getLoopNodes(data);

  let maxRight = layout.paddingLeft;
  let maxBottom = layout.paddingTop;

  loopNodes.forEach((node) => {
    const size = getLoopCanvasNodeSize(node, compactMode);
    const positionX = Number(node?.position?.x) || 0;
    const positionY = Number(node?.position?.y) || 0;

    maxRight = Math.max(maxRight, positionX + size.width);
    maxBottom = Math.max(maxBottom, positionY + size.height);
  });

  return {
    width: Math.max(layout.minWidth, Math.ceil(maxRight + layout.paddingRight)),
    height: Math.max(
      layout.minHeight,
      Math.ceil(maxBottom + layout.paddingBottom)
    ),
  };
}

function getLoopNodeStyle(data = {}) {
  const metrics = getLoopCanvasMetrics(data, true);

  return {
    width: `${metrics.width + 32}px`,
    height: `${metrics.height + 60}px`,
  };
}

function getLoopEditorSurfaceStyle(data = {}) {
  const metrics = getLoopCanvasMetrics(data, true);

  return {
    width: `${metrics.width}px`,
    height: `${metrics.height}px`,
  };
}

function getDefaultSourceHandleId(node) {
  if (!node) {
    return undefined;
  }

  if (node.type === "condition") {
    return getConditionDefaultSourceHandle(node.data);
  }

  if (node.type === "loop") {
    return LOOP_OUTER_SOURCE_HANDLE_ID;
  }

  return undefined;
}

function getDefaultTargetHandleId(node) {
  if (!node) {
    return undefined;
  }

  if (node.type === "loop") {
    return LOOP_OUTER_TARGET_HANDLE_ID;
  }

  return undefined;
}

function normalizeLoopExternalEdges(nodeIds = []) {
  const loopNodeIds = new Set(
    [nodeIds]
      .flat()
      .filter(Boolean)
      .filter((nodeId) => {
        const node = nodes.value.find((item) => item.id === nodeId);
        return node?.type === "loop";
      })
  );

  if (!loopNodeIds.size) {
    return;
  }

  let changed = false;
  const nextEdges = edges.value.map((edge) => {
    let nextEdge = edge;

    if (
      loopNodeIds.has(edge.source) &&
      edge.sourceHandle !== LOOP_OUTER_SOURCE_HANDLE_ID
    ) {
      nextEdge = {
        ...nextEdge,
        sourceHandle: LOOP_OUTER_SOURCE_HANDLE_ID,
      };
      changed = true;
    }

    if (
      loopNodeIds.has(edge.target) &&
      edge.targetHandle !== LOOP_OUTER_TARGET_HANDLE_ID
    ) {
      nextEdge = {
        ...nextEdge,
        targetHandle: LOOP_OUTER_TARGET_HANDLE_ID,
      };
      changed = true;
    }

    return nextEdge;
  });

  if (changed) {
    setEdges(nextEdges);
  }
}

function getNodeTargetHandleElement(nodeElement, nodeId) {
  const targetNode = nodes.value.find((node) => node.id === nodeId);

  if (targetNode?.type === "loop") {
    return nodeElement?.querySelector?.(
      `.vue-flow__handle.target[data-handleid="${LOOP_OUTER_TARGET_HANDLE_ID}"]`
    );
  }

  return (
    nodeElement?.querySelector?.(":scope > .vue-flow__handle.target") ||
    nodeElement?.querySelector?.(".vue-flow__handle.target")
  );
}

function updateLoopWorkflowData(nodeId, workflowData) {
  if (!nodeId) {
    return;
  }

  updateNodeData(nodeId, normalizeLoopData(workflowData || {}));
  normalizeLoopExternalEdges(nodeId);
}

function updateLoopDataInPath(loopData, nestedPath = [], updater) {
  const normalizedLoopData = normalizeLoopData(loopData);

  if (!nestedPath.length) {
    const nextRawData =
      typeof updater === "function"
        ? updater(cloneNodeData(normalizedLoopData))
        : updater;

    return normalizeLoopData(nextRawData || normalizedLoopData);
  }

  const [currentLoopNodeId, ...restPath] = nestedPath;
  let changed = false;
  const nextLoopNodes = getLoopNodes(normalizedLoopData).map((node) => {
    if (node.id !== currentLoopNodeId || node.type !== "loop") {
      return node;
    }

    changed = true;
    return {
      ...node,
      data: updateLoopDataInPath(node.data, restPath, updater),
    };
  });

  return changed
    ? normalizeLoopData(
        mergeNodeConfig(normalizedLoopData, {
          loopNodes: nextLoopNodes,
        })
      )
    : normalizedLoopData;
}

function updateLoopWorkflowDataAtPath(loopPath = [], workflowData) {
  const normalizedPath = normalizeLoopPath(loopPath);
  if (!normalizedPath.length) {
    return;
  }

  const [outerLoopNodeId, ...nestedPath] = normalizedPath;

  updateNodeData(outerLoopNodeId, (currentData) =>
    updateLoopDataInPath(currentData, nestedPath, workflowData)
  );
  normalizeLoopExternalEdges(outerLoopNodeId);
}

function updateInnerNodeDataAtPath(loopPath = [], nodeId, nodeType, updater) {
  if (!nodeId) {
    return;
  }

  updateLoopWorkflowDataAtPath(loopPath, (currentLoopData) => {
    const normalizedLoopData = normalizeLoopData(currentLoopData);
    let changed = false;
    const nextLoopNodes = getLoopNodes(normalizedLoopData).map((node) => {
      if (node.id !== nodeId) {
        return node;
      }

      changed = true;
      const currentNodeData = normalizeNodeData(
        node.type,
        cloneNodeData(node.data)
      );
      const nextRawData =
        typeof updater === "function"
          ? updater(cloneNodeData(currentNodeData))
          : updater;
      const nextData = normalizeNodeData(
        nodeType || node.type,
        cloneNodeData(nextRawData || currentNodeData)
      );

      return {
        ...node,
        data: cloneNodeData(nextData),
      };
    });

    return changed
      ? mergeNodeConfig(normalizedLoopData, {
          loopNodes: nextLoopNodes,
        })
      : normalizedLoopData;
  });
}

function removeInnerNodeAtPath(loopPath = [], nodeId) {
  if (!nodeId) {
    return;
  }

  updateLoopWorkflowDataAtPath(loopPath, (currentLoopData) => {
    const normalizedLoopData = normalizeLoopData(currentLoopData);

    return mergeNodeConfig(normalizedLoopData, {
      loopNodes: getLoopNodes(normalizedLoopData).filter(
        (node) => node.id !== nodeId
      ),
      loopEdges: getLoopEdges(normalizedLoopData).filter(
        (edge) => edge.source !== nodeId && edge.target !== nodeId
      ),
    });
  });
}

function updateLoopEdgesAtPath(loopPath = [], updater) {
  updateLoopWorkflowDataAtPath(loopPath, (currentLoopData) => {
    const normalizedLoopData = normalizeLoopData(currentLoopData);
    const nextEdges =
      typeof updater === "function"
        ? updater(
            cloneNodeData(getLoopEdges(normalizedLoopData)),
            normalizedLoopData
          )
        : updater;

    return mergeNodeConfig(normalizedLoopData, {
      loopEdges: cloneNodeData(nextEdges || getLoopEdges(normalizedLoopData)),
    });
  });
}

function handleInnerNodeSelect(payload = {}) {
  if (!payload.id || !payload.type) {
    return;
  }

  hideAddNodeMenu();
  debugRunPanelVisible.value = false;
  const nextData = normalizeNodeData(
    payload.type,
    cloneNodeData(payload.data || {})
  );

  selectedNode.value = {
    id: payload.id,
    type: payload.type,
    data: cloneNodeData(nextData),
    scope: "loop-inner",
    loopPath: normalizeLoopPath(payload.loopPath),
  };
}

function clearInnerSelection(loopPath = []) {
  if (selectedNode.value?.scope !== "loop-inner") {
    return;
  }

  if (areLoopPathsEqual(selectedNode.value.loopPath, loopPath)) {
    selectedNode.value = null;
  }
}

function updateNodeData(nodeId, updater) {
  const targetNode = nodes.value.find((node) => node.id === nodeId);
  if (!targetNode) {
    return null;
  }

  const currentData = normalizeNodeData(
    targetNode.type,
    cloneNodeData(targetNode.data)
  );
  const nextRawData =
    typeof updater === "function"
      ? updater(cloneNodeData(currentData))
      : updater;
  const nextData = normalizeNodeData(
    targetNode.type,
    cloneNodeData(nextRawData || currentData)
  );

  setNodes(
    nodes.value.map((node) =>
      node.id === nodeId
        ? {
            ...node,
            data: cloneNodeData(nextData),
          }
        : node
    )
  );
  refreshNodeHandleLayout([nodeId]);

  if (selectedNode.value?.id === nodeId) {
    selectedNode.value = {
      ...selectedNode.value,
      type: targetNode.type,
      data: cloneNodeData(nextData),
    };
  }

  return nextData;
}

function createEdgeId(source, target, sourceHandle, targetHandle) {
  edgeSeed += 1;
  return [
    "e",
    source,
    sourceHandle || "source",
    target,
    targetHandle || "target",
    Date.now(),
    edgeSeed,
  ].join("-");
}

function createDefaultEdge(connection) {
  return {
    ...connection,
    id:
      connection.id ||
      createEdgeId(
        connection.source,
        connection.target,
        connection.sourceHandle,
        connection.targetHandle
      ),
    type: "default",
  };
}

function normalizeExternalConnection(connection = {}, nodeList = nodes.value) {
  if (!connection.source || !connection.target) {
    return connection;
  }

  const normalizedNodeList = Array.isArray(nodeList) ? nodeList : nodes.value;
  const sourceNode = normalizedNodeList.find(
    (node) => node.id === connection.source
  );
  const targetNode = normalizedNodeList.find(
    (node) => node.id === connection.target
  );

  return {
    ...connection,
    sourceHandle:
      connection.sourceHandle ||
      getDefaultSourceHandleId(sourceNode) ||
      undefined,
    targetHandle:
      connection.targetHandle ||
      getDefaultTargetHandleId(targetNode) ||
      undefined,
  };
}

function createNormalizedExternalEdge(connection = {}, nodeList = nodes.value) {
  return createDefaultEdge(normalizeExternalConnection(connection, nodeList));
}

function getOuterNodeApproxSize(nodeLike = {}) {
  const nodeType = nodeLike?.type || "llm";

  if (nodeType === "loop") {
    const metrics = getLoopCanvasMetrics(nodeLike?.data || {}, true);
    return {
      width: metrics.width + 32,
      height: metrics.height + 60,
    };
  }

  if (nodeType === "condition") {
    const caseCount = Math.max(2, getConditionCases(nodeLike?.data).length);
    return {
      width: 260,
      height: 96 + caseCount * 28,
    };
  }

  if (nodeType === "reply") {
    if (isChatflowWorkflow.value) {
      return { width: 220, height: 122 };
    }

    const previewCount = Math.min(
      3,
      getReplyNodePreviewList(nodeLike?.data).length
    );
    const overflowCount = getReplyNodePreviewOverflowCount(nodeLike?.data, 3);

    return {
      width: 220,
      height: Math.max(
        122,
        96 + previewCount * 20 + (overflowCount > 0 ? 20 : 0)
      ),
    };
  }

  if (nodeType === TOOL_NODE_TYPE) {
    const previewCount = Math.min(
      3,
      getToolNodePreviewList(nodeLike?.data).length
    );
    const overflowCount = getToolNodePreviewOverflowCount(nodeLike?.data, 3);
    const hasSubtitle = Boolean(getToolNodeSubtitle(nodeLike?.data));

    return {
      width: 220,
      height: Math.max(
        122,
        96 +
          (hasSubtitle ? 26 : 0) +
          previewCount * 20 +
          (overflowCount > 0 ? 20 : 0)
      ),
    };
  }

  if (nodeType === "start") {
    const previewCount = Math.min(3, getStartFields(nodeLike?.data).length);
    const overflowCount = getStartFieldOverflowCount(nodeLike?.data, 3);
    const extraHeight = previewCount * 26 + (overflowCount > 0 ? 22 : 0);

    return {
      width: 220,
      height: 118 + extraHeight + (previewCount === 0 ? 18 : 0),
    };
  }

  return { width: 220, height: 128 };
}

function getOuterNodeBox(
  nodeLike = {},
  position = nodeLike?.position || { x: 0, y: 0 }
) {
  const size = getOuterNodeApproxSize(nodeLike);

  return {
    left: Number(position?.x) || 0,
    top: Number(position?.y) || 0,
    width: size.width,
    height: size.height,
    right: (Number(position?.x) || 0) + size.width,
    bottom: (Number(position?.y) || 0) + size.height,
  };
}

function getOuterInsertSpacingX(sourceNode = {}) {
  if (sourceNode?.type === "start") {
    return 96;
  }

  if (sourceNode?.type === "condition") {
    return 84;
  }

  return 72;
}

function getOuterOutgoingTargetNodes(
  sourceNodeId,
  sourceHandleId = null,
  nodeList = nodes.value,
  edgeList = edges.value
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

function getOuterSiblingBasePosition(
  sourceNode,
  candidateNode,
  sourceHandleId = null,
  nodeList = nodes.value,
  edgeList = edges.value
) {
  const siblingTargets = getOuterOutgoingTargetNodes(
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

    return getOuterNodeBox(node).bottom > getOuterNodeBox(currentLowest).bottom
      ? node
      : currentLowest;
  }, null);
  const siblingGapY = 40;

  return {
    x: anchorNode.position.x,
    y: getOuterNodeBox(lowestNode).bottom + siblingGapY,
  };
}

function getOuterSequentialBasePosition(
  sourceNode,
  candidateNode,
  sourceHandleId = null
) {
  const sourceSize = getOuterNodeApproxSize(sourceNode);
  const candidateSize = getOuterNodeApproxSize(candidateNode);
  const alignTop =
    sourceHandleId || sourceNode.type === "condition"
      ? sourceNode.position.y
      : sourceNode.position.y +
        Math.round((sourceSize.height - candidateSize.height) / 2);

  return {
    x:
      sourceNode.position.x +
      sourceSize.width +
      getOuterInsertSpacingX(sourceNode),
    y: alignTop,
  };
}

function getOuterEdgeBasePosition(sourceNode, targetNode, candidateNode) {
  const sourceBox = getOuterNodeBox(sourceNode);
  const targetBox = getOuterNodeBox(targetNode);
  const candidateSize = getOuterNodeApproxSize(candidateNode);
  const gapX = getOuterInsertSpacingX(sourceNode);
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

function isOuterBoxOverlapping(boxA, boxB, gapX = 40, gapY = 32) {
  return (
    boxA.left < boxB.right + gapX &&
    boxA.right + gapX > boxB.left &&
    boxA.top < boxB.bottom + gapY &&
    boxA.bottom + gapY > boxB.top
  );
}

function collectDownstreamNodeIds(edgeList = edges.value, startNodeIds = []) {
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

function shiftOuterNodesHorizontally(
  nodeList = nodes.value,
  nodeIds = new Set(),
  deltaX = 0
) {
  if (!deltaX) {
    return cloneNodeData(nodeList);
  }

  return cloneNodeData(nodeList).map((node) =>
    nodeIds.has(node.id)
      ? {
          ...node,
          position: {
            ...node.position,
            x: node.position.x + deltaX,
          },
        }
      : node
  );
}

function makeOuterInsertionRoom(
  nodeList = nodes.value,
  edgeList = edges.value,
  candidateNode,
  candidatePosition,
  options = {}
) {
  let nextNodes = cloneNodeData(nodeList);
  const excludedIds = new Set((options.excludeIds || []).filter(Boolean));
  const candidateBox = () => getOuterNodeBox(candidateNode, candidatePosition);

  if (options.targetNodeId) {
    const targetNode = nextNodes.find(
      (node) => node.id === options.targetNodeId
    );
    if (targetNode) {
      const targetBox = getOuterNodeBox(targetNode);
      const requiredShift = Math.ceil(
        candidateBox().right + 40 - targetBox.left
      );

      if (requiredShift > 0) {
        const downstreamNodeIds = collectDownstreamNodeIds(edgeList, [
          targetNode.id,
        ]);
        downstreamNodeIds.add(targetNode.id);
        nextNodes = shiftOuterNodesHorizontally(
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
      if (!node || excludedIds.has(node.id)) {
        return false;
      }

      return isOuterBoxOverlapping(candidateBox(), getOuterNodeBox(node));
    });

    if (!blockingNode) {
      break;
    }

    const blockingBox = getOuterNodeBox(blockingNode);
    const requiredShift = Math.max(
      40,
      Math.ceil(candidateBox().right + 40 - blockingBox.left)
    );
    const downstreamNodeIds = collectDownstreamNodeIds(edgeList, [
      blockingNode.id,
    ]);
    downstreamNodeIds.add(blockingNode.id);
    nextNodes = shiftOuterNodesHorizontally(
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
  newNodeId,
  nodeType = "llm",
  nodeData = {},
  nodeList = nodes.value,
  excludeIds = []
) {
  const minGapX = 40;
  const minGapY = 32;
  const maxAttempts = 18;
  const excludedIdSet = new Set([newNodeId, ...excludeIds].filter(Boolean));
  const candidateNode = {
    id: newNodeId,
    type: nodeType,
    data: nodeData,
  };
  const candidateSize = getOuterNodeApproxSize(candidateNode);
  let adjustedPosition = {
    x: Number(position?.x) || 0,
    y: Number(position?.y) || 0,
  };
  let attemptCount = 0;

  while (attemptCount < maxAttempts) {
    const candidateBox = {
      left: adjustedPosition.x,
      top: adjustedPosition.y,
      right: adjustedPosition.x + candidateSize.width,
      bottom: adjustedPosition.y + candidateSize.height,
    };

    const overlappingNode = nodeList.find((node) => {
      if (!node || excludedIdSet.has(node.id)) {
        return false;
      }

      return isOuterBoxOverlapping(
        candidateBox,
        getOuterNodeBox(node),
        minGapX,
        minGapY
      );
    });

    if (!overlappingNode) {
      return adjustedPosition;
    }

    attemptCount += 1;
    adjustedPosition = {
      x:
        attemptCount % 4 === 0
          ? adjustedPosition.x + Math.round(minGapX * 1.5)
          : adjustedPosition.x,
      y: adjustedPosition.y + minGapY,
    };
  }

  return adjustedPosition;
}

function getDefaultNodeData(nodeType) {
  const defaults = {
    start: normalizeStartNodeData({
      label: "开始",
      description: "",
      fields: [],
    }),
    llm: normalizeLlmNodeData({
      label: "LLM",
      ...getDefaultLlmModelConfig(),
      description: "",
      prompt: "",
    }),
    tool: normalizeToolNodeData({
      label: "工具",
      description: "",
      toolName: "工具",
      toolDescription: "",
      toolSource: "",
    }),
    condition: normalizeConditionData({
      label: "条件分支",
      description: "",
    }),
    loop: normalizeLoopData({
      label: "循环",
      description: "",
    }),
    reply: normalizeReplyNodeData({
      label: "输出",
      description: "",
      outputs: [],
    }),
  };

  return cloneNodeData(defaults[nodeType] || { label: "新节点" });
}

function syncSelectedNodeData() {
  if (!selectedNode.value) return;

  const activeSelection = selectedNode.value;
  const normalizedLoopPath = normalizeLoopPath(activeSelection.loopPath);
  const nextData = normalizeNodeData(
    activeSelection.type,
    cloneNodeData(activeSelection.data)
  );

  activeSelection.data = cloneNodeData(nextData);
  if (!areLoopPathsEqual(activeSelection.loopPath, normalizedLoopPath)) {
    activeSelection.loopPath = normalizedLoopPath;
  }

  if (activeSelection.scope === "loop-inner") {
    updateInnerNodeDataAtPath(
      activeSelection.loopPath,
      activeSelection.id,
      activeSelection.type,
      nextData
    );
    return;
  }

  setNodes(
    nodes.value.map((node) =>
      node.id === activeSelection.id
        ? {
            ...node,
            data: cloneNodeData(nextData),
          }
        : node
    )
  );
  refreshNodeHandleLayout([activeSelection.id]);
}

function addLlmMessage() {
  if (selectedNode.value?.type !== "llm") {
    return;
  }

  const messages = getNodeInput(selectedNode.value.data);

  const nextRole = messages.length % 2 === 0 ? "user" : "assistant";

  selectedNode.value.data = setNodeInput(selectedNode.value.data, [
    ...messages,
    createLlmMessage({ role: nextRole }),
  ]);
  syncSelectedNodeData();
}

function removeLlmMessage(messageId) {
  if (selectedNode.value?.type !== "llm") {
    return;
  }

  selectedNode.value.data = setNodeInput(
    selectedNode.value.data,
    getNodeInput(selectedNode.value.data).filter(
      (message) => message.id !== messageId
    )
  );
  syncSelectedNodeData();
}

function handleStartFieldTypeChange(nextType) {
  const meta = getStartFieldTypeMeta(nextType);
  const currentMaxLength = Number(startFieldDraft.value.maxLength);

  startFieldDraft.value = {
    ...startFieldDraft.value,
    type: meta.value,
    maxLength: meta.supportsMaxLength
      ? Number.isFinite(currentMaxLength) && currentMaxLength > 0
        ? currentMaxLength
        : meta.defaultMaxLength
      : null,
  };

  nextTick(() => {
    startFieldDialogFormRef.value?.clearValidate(["maxLength"]);
  });
}

function openStartFieldDialog(field = null) {
  if (selectedNode.value?.type !== "start") {
    return;
  }

  if (field && isReadonlyStartField(field)) {
    return;
  }

  startFieldDialogMode.value = field ? "edit" : "create";
  startFieldDraft.value = createStartField(
    cloneNodeData(field || { type: "text", required: true })
  );
  startFieldDialogVisible.value = true;

  nextTick(() => {
    startFieldDialogFormRef.value?.clearValidate();
  });
}

function closeStartFieldDialog() {
  startFieldDialogFormRef.value?.clearValidate();
  startFieldDialogVisible.value = false;
  startFieldDraft.value = createStartField();
}

async function saveStartField() {
  if (selectedNode.value?.type !== "start") {
    return;
  }

  if (
    startFieldDialogMode.value === "edit" &&
    isReadonlyStartField(startFieldDraft.value)
  ) {
    return;
  }

  try {
    await startFieldDialogFormRef.value?.validate();
  } catch {
    return;
  }

  const nextField = normalizeStartField(startFieldDraft.value);
  const fieldName = nextField.name.trim();
  const displayLabel = nextField.label.trim();
  const currentFields = getStartFields(selectedNode.value.data);

  if (findDuplicatedStartField(fieldName, nextField.id)) {
    startFieldDialogFormRef.value?.validateField("name");
    return;
  }

  const normalizedField = {
    ...nextField,
    name: fieldName,
    label: displayLabel,
  };

  const nextFields =
    startFieldDialogMode.value === "edit"
      ? currentFields.map((field) =>
          field.id === normalizedField.id ? normalizedField : field
        )
      : [...currentFields, normalizedField];

  selectedNode.value.data = setNodeInput(selectedNode.value.data, nextFields);
  syncSelectedNodeData();
  closeStartFieldDialog();
}

function removeStartField(fieldId) {
  if (selectedNode.value?.type !== "start") {
    return;
  }

  const targetField = getStartFields(selectedNode.value.data).find(
    (field) => field.id === fieldId
  );

  if (isReadonlyStartField(targetField)) {
    return;
  }

  selectedNode.value.data = setNodeInput(
    selectedNode.value.data,
    getStartFields(selectedNode.value.data).filter(
      (field) => field.id !== fieldId
    )
  );
  syncSelectedNodeData();
}

function addConditionCase() {
  if (selectedNode.value?.type !== "condition") return;

  const cases = getConditionCases(selectedNode.value.data);
  const elseCase =
    cases.find((item) => item.isElse) || createConditionCase(true);
  const branchCases = cases.filter((item) => !item.isElse);

  selectedNode.value.data = setNodeConfigValue(
    selectedNode.value.data,
    "cases",
    [...branchCases, createConditionCase(false), elseCase]
  );

  syncSelectedNodeData();
}

function removeConditionCase(caseId) {
  if (selectedNode.value?.type !== "condition") return;

  const selectedEntry = {
    ...selectedNode.value,
    loopPath: normalizeLoopPath(selectedNode.value.loopPath),
  };
  const cases = getConditionCases(selectedEntry.data);
  const targetCase = cases.find((item) => item.id === caseId);

  if (!targetCase || targetCase.isElse) {
    return;
  }

  const branchCases = cases.filter((item) => !item.isElse);
  if (branchCases.length <= 1) {
    ElMessage.warning("条件分支至少需要保留一个 IF 分支");
    return;
  }

  selectedNode.value.data = setNodeConfigValue(
    selectedNode.value.data,
    "cases",
    cases.filter((item) => item.id !== caseId)
  );
  syncSelectedNodeData();

  const handleId = getConditionHandleId(caseId);
  if (selectedEntry.scope === "loop-inner") {
    updateLoopEdgesAtPath(selectedEntry.loopPath, (currentEdges) =>
      currentEdges.filter(
        (edge) =>
          !(edge.source === selectedEntry.id && edge.sourceHandle === handleId)
      )
    );
    return;
  }

  const relatedEdges = edges.value
    .filter(
      (edge) =>
        edge.source === selectedEntry.id && edge.sourceHandle === handleId
    )
    .map((edge) => edge.id);

  if (relatedEdges.length) {
    removeEdges(relatedEdges);
  }
}

function onConnect(connection) {
  if (!connection.source || !connection.target) {
    return;
  }

  const sourceNode = nodes.value.find((node) => node.id === connection.source);
  const normalizedConnection = normalizeExternalConnection(connection);

  if (sourceNode?.type === "condition") {
    if (!normalizedConnection.sourceHandle) {
      ElMessage.warning("条件分支需要从具体的分支出口发起连接");
      return;
    }
  }

  const duplicatedEdge = edges.value.find(
    (edge) =>
      edge.source === normalizedConnection.source &&
      edge.target === normalizedConnection.target &&
      (edge.sourceHandle || null) ===
        (normalizedConnection.sourceHandle || null) &&
      (edge.targetHandle || null) ===
        (normalizedConnection.targetHandle || null)
  );

  if (duplicatedEdge) {
    return;
  }

  addEdges([createDefaultEdge(normalizedConnection)]);
}

function getPointerPositionInFlow(clientX, clientY) {
  const bounds = vueFlowRef.value?.getBoundingClientRect();
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
  const nodeId = nodeElement?.getAttribute?.("data-id");

  if (!nodeId || nodeId === sourceNodeId) {
    return null;
  }

  const targetHandleElement = getNodeTargetHandleElement(nodeElement, nodeId);
  const targetBounds =
    targetHandleElement?.getBoundingClientRect?.() ??
    nodeElement?.getBoundingClientRect?.();

  if (!targetBounds) {
    return null;
  }

  const targetFlowPosition = getPointerPositionInFlow(
    targetBounds.left + targetBounds.width / 2,
    targetBounds.top + targetBounds.height / 2
  );

  if (!targetFlowPosition) {
    return null;
  }

  const targetHandleId =
    targetHandleElement?.getAttribute?.("data-handleid") || undefined;
  const targetHandlePosition =
    targetHandleElement?.getAttribute?.("data-handlepos") || Position.Left;

  return {
    nodeId,
    position: targetFlowPosition,
    handle: {
      nodeId,
      type: "target",
      ...(targetHandleId ? { id: targetHandleId } : {}),
      position: targetHandlePosition,
      ...targetFlowPosition,
    },
  };
}

function onSourceActionPointerDown(event, nodeId, sourceHandleId = null) {
  if (event.button !== 0) {
    return;
  }

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
        onConnect({
          source: nodeId,
          sourceHandle:
            sourceHandleId ||
            getDefaultSourceHandleId(
              nodes.value.find((node) => node.id === nodeId)
            ) ||
            undefined,
          target: targetPreview.nodeId,
          targetHandle: targetPreview.handle.id || undefined,
        });
      }

      endConnection(upEvent);
      return;
    }

    dragHoverTargetId.value = null;
    showAddNodeMenu(nodeId, upEvent, sourceHandleId);
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

function markAddMenuOpened() {
  addMenuOpenAt.value = Date.now();
}

function shouldIgnoreAddMenuDismiss() {
  return (
    addNodeMenuVisible.value &&
    Date.now() - addMenuOpenAt.value < addMenuDismissGuardMs
  );
}

function showAddNodeMenu(nodeId, event, sourceHandleId = null) {
  hideAddNodeMenu();
  addNodeMenuSourceId.value = nodeId;
  addNodeMenuSourceHandle.value = sourceHandleId || null;
  addNodeMenuPosition.value = getAddNodeMenuViewportPosition(
    event.clientX,
    event.clientY
  );
  addNodeMenuVisible.value = true;
  markAddMenuOpened();
}

// 隐藏添加节点菜单
function hideAddNodeMenu() {
  addNodeMenuVisible.value = false;
  addNodeMenuSourceId.value = null;
  addNodeMenuSourceHandle.value = null;
  addNodeMenuTargetId.value = null;
  addNodeMenuEdgeId.value = null;
}

function getAddNodeMenuViewportPosition(x, y) {
  const viewportWidth = window.innerWidth || 0;
  const viewportHeight = window.innerHeight || 0;
  const estimatedListLength = Math.max(
    addableNodeTypes.length,
    toolMenuItems.value.length || 6
  );
  const estimatedMenuHeight =
    ADD_NODE_MENU_HEADER_HEIGHT +
    ADD_NODE_MENU_TAB_HEIGHT +
    estimatedListLength * ADD_NODE_MENU_ITEM_HEIGHT +
    ADD_NODE_MENU_LIST_PADDING;
  const maxX = Math.max(
    ADD_NODE_MENU_MARGIN,
    viewportWidth - ADD_NODE_MENU_ESTIMATED_WIDTH - ADD_NODE_MENU_MARGIN
  );
  const maxY = Math.max(
    ADD_NODE_MENU_MARGIN,
    viewportHeight - estimatedMenuHeight - ADD_NODE_MENU_MARGIN
  );

  return {
    x: Math.min(Math.max(ADD_NODE_MENU_MARGIN, x), maxX),
    y: Math.min(
      Math.max(ADD_NODE_MENU_MARGIN, y - ADD_NODE_MENU_VERTICAL_OFFSET),
      maxY
    ),
  };
}

function addNodeAndConnect(nodeType, nodeDataOverride = null) {
  if (!addNodeMenuSourceId.value) {
    return;
  }

  if (!isAddableNodeTypeEnabled(nodeType)) {
    ElMessage({
      message: "正在开发中...",
      type: "warning",
    });
    return;
  }

  const isFromEdge = Boolean(
    addNodeMenuEdgeId.value && addNodeMenuTargetId.value
  );
  const sourceNode = nodes.value.find(
    (node) => node.id === addNodeMenuSourceId.value
  );

  if (!sourceNode) {
    hideAddNodeMenu();
    return;
  }

  const targetNode = isFromEdge
    ? nodes.value.find((node) => node.id === addNodeMenuTargetId.value)
    : null;
  const originalEdge = isFromEdge
    ? edges.value.find((edge) => edge.id === addNodeMenuEdgeId.value)
    : null;

  if (isFromEdge && !originalEdge) {
    hideAddNodeMenu();
    return;
  }

  const newNodeId = buildNodeId(nodeType);
  const newNodeData = nodeDataOverride
    ? normalizeNodeData(nodeType, cloneNodeData(nodeDataOverride))
    : getDefaultNodeData(nodeType);
  const candidateNode = {
    id: newNodeId,
    type: nodeType,
    data: newNodeData,
  };
  const newNodeDefaultSourceHandle =
    getDefaultSourceHandleId(candidateNode) || undefined;
  const newNodeDefaultTargetHandle =
    getDefaultTargetHandleId(candidateNode) || undefined;
  const hasExistingOutgoingTargets =
    !isFromEdge &&
    getOuterOutgoingTargetNodes(
      sourceNode.id,
      addNodeMenuSourceHandle.value,
      nodes.value,
      edges.value
    ).length > 0;
  const basePosition =
    isFromEdge && targetNode
      ? getOuterEdgeBasePosition(sourceNode, targetNode, candidateNode)
      : hasExistingOutgoingTargets
      ? getOuterSiblingBasePosition(
          sourceNode,
          candidateNode,
          addNodeMenuSourceHandle.value,
          nodes.value,
          edges.value
        ) ||
        getOuterSequentialBasePosition(
          sourceNode,
          candidateNode,
          addNodeMenuSourceHandle.value
        )
      : getOuterSequentialBasePosition(
          sourceNode,
          candidateNode,
          addNodeMenuSourceHandle.value
        );

  const shiftedNodes = isFromEdge
    ? makeOuterInsertionRoom(
        cloneNodeData(nodes.value),
        cloneNodeData(edges.value),
        candidateNode,
        basePosition,
        {
          excludeIds: [sourceNode.id],
          targetNodeId: targetNode?.id,
        }
      )
    : cloneNodeData(nodes.value);
  const finalPosition = adjustPositionToAvoidOverlap(
    basePosition,
    newNodeId,
    nodeType,
    newNodeData,
    shiftedNodes,
    [sourceNode.id]
  );
  const newNode = {
    ...candidateNode,
    position: finalPosition,
  };
  const nextNodes = [...shiftedNodes, newNode];
  let nextEdges = cloneNodeData(edges.value);

  if (isFromEdge && originalEdge) {
    nextEdges = nextEdges.filter((edge) => edge.id !== originalEdge.id);
    nextEdges.push(
      createNormalizedExternalEdge(
        {
          source: addNodeMenuSourceId.value,
          sourceHandle:
            originalEdge.sourceHandle ||
            addNodeMenuSourceHandle.value ||
            undefined,
          target: newNodeId,
          targetHandle: newNodeDefaultTargetHandle,
        },
        nextNodes
      )
    );

    const outgoingEdge = {
      source: newNodeId,
      sourceHandle: newNodeDefaultSourceHandle,
      target: addNodeMenuTargetId.value,
      targetHandle: originalEdge.targetHandle || undefined,
    };

    if (nodeType === "condition") {
      outgoingEdge.sourceHandle = getConditionDefaultSourceHandle(newNode.data);
    }

    nextEdges.push(createNormalizedExternalEdge(outgoingEdge, nextNodes));
  } else {
    nextEdges.push(
      createNormalizedExternalEdge(
        {
          source: addNodeMenuSourceId.value,
          sourceHandle:
            addNodeMenuSourceHandle.value ||
            getDefaultSourceHandleId(sourceNode) ||
            undefined,
          target: newNodeId,
          targetHandle: newNodeDefaultTargetHandle,
        },
        nextNodes
      )
    );
  }

  nodes.value = nextNodes;
  edges.value = nextEdges;
  nextTick(() => {
    refreshNodeHandleLayout([newNode.id, sourceNode.id]);
  });
  hideAddNodeMenu();
}

function onNodeClick({ event, node }) {
  if (shouldIgnoreAddMenuDismiss()) {
    return;
  }

  if (event?.target?.closest?.(".loop-workflow-canvas")) {
    return;
  }

  hideAddNodeMenu();
  if (!node) return;
  debugRunPanelVisible.value = false;

  const nextData = normalizeNodeData(node.type, node.data);
  if (node.type === "condition" && !getConditionCases(node.data).length) {
    setNodes(
      nodes.value.map((item) =>
        item.id === node.id
          ? {
              ...item,
              data: cloneNodeData(nextData),
            }
          : item
      )
    );
  }

  selectedNode.value = {
    id: node.id,
    type: node.type,
    data: cloneNodeData(nextData),
    scope: "outer",
    loopPath: [],
  };
}

function onEdgeClick() {
  if (shouldIgnoreAddMenuDismiss()) {
    return;
  }

  hideAddNodeMenu();
  selectedNode.value = null;
}

// 在边上显示添加节点菜单
function showAddNodeMenuOnEdge(edgeProps) {
  // 使用边中点定位，避免依赖 sourceX、sourceY、targetX、targetY 是否存在
  const centerX = (edgeProps.sourceX + edgeProps.targetX) / 2;
  const centerY = (edgeProps.sourceY + edgeProps.targetY) / 2;

  addNodeMenuPosition.value = getAddNodeMenuViewportPosition(
    centerX + 20,
    centerY
  );

  addNodeMenuSourceId.value = edgeProps.source;
  addNodeMenuSourceHandle.value = edgeProps.sourceHandle || null;
  addNodeMenuTargetId.value = edgeProps.target;
  addNodeMenuEdgeId.value = edgeProps.id;
  addNodeMenuVisible.value = true;
  markAddMenuOpened();
}

function onPaneClick() {
  if (shouldIgnoreAddMenuDismiss()) {
    return;
  }

  hideAddNodeMenu();
  selectedNode.value = null;
}

function handleSelectedNodeFieldUpdate(payload = {}) {
  const { field, value } = payload;

  if (!selectedNode.value || !field) {
    return;
  }

  selectedNode.value.data = setNodeConfigValue(
    selectedNode.value.data,
    field,
    value
  );
  syncSelectedNodeData();
}

function addToolOutput() {
  if (selectedNode.value?.type !== TOOL_NODE_TYPE) {
    return;
  }

  selectedNode.value.data = setNodeOutput(selectedNode.value.data, [
    ...getToolOutputs(selectedNode.value.data),
    createToolOutput(),
  ]);
  syncSelectedNodeData();
}

function removeToolOutput(outputId) {
  if (selectedNode.value?.type !== TOOL_NODE_TYPE || !outputId) {
    return;
  }

  selectedNode.value.data = setNodeOutput(
    selectedNode.value.data,
    getToolOutputs(selectedNode.value.data).filter(
      (output) => output.id !== outputId
    )
  );
  syncSelectedNodeData();
}

function handleSelectedToolOutputUpdate(payload = {}) {
  const { id, patch } = payload;

  if (selectedNode.value?.type !== TOOL_NODE_TYPE || !id) {
    return;
  }

  selectedNode.value.data = setNodeOutput(
    selectedNode.value.data,
    getToolOutputs(selectedNode.value.data).map((output) =>
      output.id === id
        ? normalizeToolOutput({
            ...output,
            ...(patch || {}),
          })
        : output
    )
  );
  syncSelectedNodeData();
}

function addReplyOutput() {
  if (selectedNode.value?.type !== "reply") {
    return;
  }

  selectedNode.value.data = setNodeOutput(selectedNode.value.data, [
    ...getReplyOutputs(selectedNode.value.data),
    createReplyOutput(),
  ]);
  syncSelectedNodeData();
}

function removeReplyOutput(outputId) {
  if (selectedNode.value?.type !== "reply" || !outputId) {
    return;
  }

  selectedNode.value.data = setNodeOutput(
    selectedNode.value.data,
    getReplyOutputs(selectedNode.value.data).filter(
      (output) => output.id !== outputId
    )
  );
  syncSelectedNodeData();
}

function handleSelectedReplyOutputUpdate(payload = {}) {
  const { id, patch } = payload;

  if (selectedNode.value?.type !== "reply" || !id) {
    return;
  }

  selectedNode.value.data = setNodeOutput(
    selectedNode.value.data,
    getReplyOutputs(selectedNode.value.data).map((output) =>
      output.id === id
        ? normalizeReplyOutput({
            ...output,
            ...(patch || {}),
          })
        : output
    )
  );
  syncSelectedNodeData();
}

function handleSelectedReplyContentUpdate(value = "") {
  if (selectedNode.value?.type !== "reply") {
    return;
  }

  selectedNode.value.data = setNodeConfigValue(
    selectedNode.value.data,
    "content",
    `${value || ""}`
  );
  syncSelectedNodeData();
}

function handleSelectedConditionCaseUpdate(payload = {}) {
  const { id, expression } = payload;

  if (selectedNode.value?.type !== "condition" || !id) {
    return;
  }

  selectedNode.value.data = setNodeConfigValue(
    selectedNode.value.data,
    "cases",
    getConditionCases(selectedNode.value.data).map((caseItem) =>
      caseItem.id === id
        ? {
            ...caseItem,
            expression: expression ?? "",
          }
        : caseItem
    )
  );
  syncSelectedNodeData();
}

function handleSelectedLlmMessageUpdate(payload = {}) {
  const { id, patch } = payload;

  if (selectedNode.value?.type !== "llm" || !id) {
    return;
  }

  selectedNode.value.data = setNodeInput(
    selectedNode.value.data,
    getNodeInput(selectedNode.value.data).map((message) =>
      message.id === id
        ? {
            ...message,
            ...(patch || {}),
          }
        : message
    )
  );
  syncSelectedNodeData();
}

function deleteNodeByTarget(nodeId, loopPath = []) {
  const normalizedNodeId = `${nodeId || ""}`.trim();
  const normalizedLoopPath = normalizeLoopPath(loopPath);

  if (!normalizedNodeId) {
    return;
  }

  hideAddNodeMenu();

  if (normalizedLoopPath.length) {
    removeInnerNodeAtPath(normalizedLoopPath, normalizedNodeId);

    if (
      selectedNode.value?.scope === "loop-inner" &&
      selectedNode.value?.id === normalizedNodeId &&
      areLoopPathsEqual(selectedNode.value.loopPath, normalizedLoopPath)
    ) {
      selectedNode.value = null;
    }

    return;
  }

  removeNodes([normalizedNodeId]);

  if (
    selectedNode.value?.scope !== "loop-inner" &&
    selectedNode.value?.id === normalizedNodeId
  ) {
    selectedNode.value = null;
  }
}

function isSelectedNodeDeletable(node = null) {
  if (!node?.id) {
    return false;
  }

  if (node.scope === "loop-inner") {
    return node.id !== "loop-entry" && node.type !== "loop-start";
  }

  return node.type !== "start";
}

function shouldIgnoreDeleteShortcutTarget(event) {
  const target = event?.target;

  if (!(target instanceof HTMLElement)) {
    return false;
  }

  return Boolean(
    target.closest(
      'input, textarea, select, [contenteditable="true"], [contenteditable=""], .el-input, .el-textarea, .el-select'
    )
  );
}

function handleSelectedNodeDeleteKeydown(event) {
  if (
    event?.key !== "Delete" ||
    event?.defaultPrevented ||
    event?.isComposing ||
    !selectedNode.value ||
    !isSelectedNodeDeletable(selectedNode.value) ||
    shouldIgnoreDeleteShortcutTarget(event)
  ) {
    return;
  }

  event.preventDefault();
  deleteNodeByTarget(selectedNode.value.id, selectedNode.value.loopPath);
}

function handleOuterNodeDelete(nodeId = "") {
  deleteNodeByTarget(nodeId);
}

function handleInnerNodeDelete(payload = {}) {
  deleteNodeByTarget(payload.id, payload.loopPath);
}

function validateFlow(options = {}) {
  const { showSuccess = true } = options;
  const startNodes = nodes.value.filter((n) => n.type === "start");
  const replyNodes = nodes.value.filter((n) => n.type === "reply");

  if (startNodes.length === 0) {
    ElMessage.warning("流程必须包含开始节点");
    return false;
  }

  if (replyNodes.length === 0) {
    ElMessage.warning("流程必须包含回复节点");
    return false;
  }

  const invalidReplyNode = replyNodes.find((node) =>
    isChatflowWorkflow.value
      ? !`${getReplyContent(node.data) || ""}`.trim()
      : !getReplyOutputs(node.data).length &&
        !`${getReplyContent(node.data) || ""}`.trim()
  );

  if (invalidReplyNode) {
    ElMessage.warning(
      isChatflowWorkflow.value
        ? `回复节点“${getNodeLabel(
            invalidReplyNode.data,
            "回复"
          )}”请填写回复内容`
        : `输出节点“${getNodeLabel(
            invalidReplyNode.data,
            "输出"
          )}”请至少配置一个输出变量`
    );
    return false;
  }

  if (showSuccess) {
    ElMessage.success("流程验证通过");
  }

  return true;
}

function buildFlowData() {
  return normalizeWorkflowGraphIds({
    botId: botId.value,
    nodes: nodes.value.map((node) => ({
      ...node,
      data: normalizeNodeData(node.type, cloneNodeData(node.data || {})),
    })),
    edges: cloneNodeData(edges.value),
  });
}

function exportFlow() {
  const flowData = buildFlowData();
  submitFlow(flowData).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("保存成功");
      return;
    } else {
      proxy.$modal.msgError(`保存异常! ${msg}`);
    }
  });

  // const dataStr = JSON.stringify(flowData, null, 2);
  // const dataBlob = new Blob([dataStr], { type: "application/json" });
  // const url = URL.createObjectURL(dataBlob);

  // const link = document.createElement("a");
  // link.href = url;
  // link.download = `workflow-${Date.now()}.json`;
  // link.click();

  // URL.revokeObjectURL(url);
  // ElMessage.success("流程已导出");
}
</script>

<style scoped lang="scss">
.workflow-editor {
  margin: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f9fafb;
  --el-border-radius-base: 0;
  --el-border-radius-small: 0;
  --el-border-radius-round: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background-color: #fff;
  border-bottom: 1px solid #e5e7eb;

  .toolbar-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .toolbar-title {
      display: flex;
      align-items: center;
      font-size: 20px;
      font-weight: 600;
      color: #111827;
      .title{
        max-width: 500px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .el-tag {
        margin-left: 10px;
      }
      .internally {
        display: flex;
        align-items: center;
        font-size: 12px;
        color: red;
        margin-left: 8px;
        font-weight: normal;
      }
    }
  }

  .toolbar-actions {
    display: flex;
    gap: 8px;
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

    .bcimg {
      width: 1em;
      height: 1em;
      color: #fff;
      margin-right: 3px;
    }
    .ds {
      width: 1em;
      height: 1em;
      margin-right: 3px;
    }
  }
}

.editor-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.flow-wrapper {
  flex: 1;
  position: relative;
  background-color: #f9fafb;
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

  &.selected {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  }

  .node-top {
    padding: 4px 12px;
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

      &.node-preview--inline {
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        gap: 4px;
        line-height: 1.6;
      }

      .preview-tag {
        display: inline-flex;
        align-items: center;
        max-width: 100%;
        background-color: #eff6ff;
        color: #3b82f6;
        padding: 1px 6px;
        border-radius: 4px;
        font-size: 12px;
        white-space: normal;
        word-break: break-word;
      }

      .preview-text {
        white-space: pre-wrap;
        word-break: break-word;
        color: #6b7280;
      }
    }
  }

  // 添加节点按钮
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
    top: 3px;
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

.start-node {
  .node-icon-wrapper {
    background-color: #dbeafe;
    color: #3b82f6;
  }

  .node-body {
    gap: 8px;
  }

  .start-node-fields {
    display: flex;
    flex-direction: column;
    gap: 6px;
  }

  .start-node-field-row {
    padding: 3px 8px;
    border-radius: 8px;
    background-color: #f8fafc;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
  }

  .start-node-field-name {
    min-width: 0;
    font-size: 12px;
    color: #3b82f6;
    font-weight: 500;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .start-node-field-type {
    flex-shrink: 0;
    font-size: 12px;
    color: #6b7280;
    background-color: #eef2ff;
    padding: 2px 6px;
    border-radius: 999px;
  }

  .start-node-field-more {
    font-size: 11px;
    color: #6b7280;
  }

  .start-node-empty {
    padding: 2px 8px;
    border: 1px dashed #cbd5e1;
    border-radius: 10px;
    font-size: 12px;
    color: #6b7280;
    background-color: #f8fafc;
  }
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

.tool-node {
  .node-icon-wrapper {
    background-color: #ede9fe;
    color: #7c3aed;
  }

  .tool-node-preview-more {
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
    letter-spacing: 0.4px;
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
    letter-spacing: 0.5px;
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

.node-source-handle {
  opacity: 0;
  pointer-events: none;
}

.handle {
  width: 10px;
  height: 10px;
  background-color: #3b82f6;
  border: 2px solid #fff;
  border-radius: 50%;
  transition: all 0.2s;

  &.handle-drop-target {
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

// 添加节点菜单样式
.add-node-menu {
  position: fixed;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  min-width: 220px;
  max-height: calc(100vh - 32px);
  overflow: hidden;

  .add-node-menu-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    border-bottom: 1px solid #f3f4f6;
    font-size: 14px;
    font-weight: 600;
    color: #111827;

    .close-btn {
      cursor: pointer;
      color: #9ca3af;
      transition: color 0.2s;

      &:hover {
        color: #6b7280;
      }
    }
  }

  .add-node-menu-tabs {
    display: flex;
    align-items: center;
    gap: 4px;
    padding: 8px 12px 0;
  }

  .add-node-menu-tab {
    flex: 1;
    height: 32px;
    border: none;
    border-radius: 8px;
    background-color: transparent;
    color: #6b7280;
    font-size: 13px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
      background-color: #f3f4f6;
      color: #374151;
    }

    &.is-active {
      background-color: #eff6ff;
      color: #2563eb;
    }
  }

  .add-node-menu-list {
    padding: 8px;
    max-height: calc(100vh - 140px);
    overflow-y: auto;
    overscroll-behavior: contain;
  }

  .add-node-menu-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 10px 12px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background-color: #f3f4f6;
    }

    .node-icon {
      font-size: 20px;
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f9fafb;
      border-radius: 8px;
    }

    .node-info {
      flex: 1;

      .node-label {
        font-size: 14px;
        font-weight: 500;
        color: #111827;
      }

      .node-desc {
        font-size: 12px;
        color: #6b7280;
        margin-top: 2px;
      }
    }
  }

  .add-node-menu-empty {
    min-height: 120px;
    padding: 16px 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 8px;
    text-align: center;
    font-size: 13px;
    color: #6b7280;
  }
}

.add-node-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
}

.config-drawer {
  width: 0;
  background-color: #fff;
  border-left: 1px solid #e5e7eb;
  transition: width 0.3s ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  &.open {
    width: 420px;
  }

  .drawer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    padding: 16px 20px;
    border-bottom: 1px solid #e5e7eb;

    &.is-start {
      align-items: flex-start;

      .close-icon {
        margin-top: 4px;
      }
    }

    .drawer-header-main {
      min-width: 0;
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .drawer-title {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 16px;
      font-weight: 600;
      color: #111827;

      .drawer-icon {
        width: 32px;
        height: 32px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;

        svg {
          width: 18px;
          height: 18px;
        }

        .drawer-llm-icon {
          width: 18px;
          height: 18px;
        }

        &.start {
          background-color: #dbeafe;
          color: #3b82f6;
        }

        &.llm {
          background-color: #dbeafe;
          color: #2563eb;
        }

        &.reply {
          background-color: #ffedd5;
          color: #f97316;
        }

        &.tool {
          background-color: #ede9fe;
          color: #7c3aed;
        }

        &.condition {
          background-color: #fef3c7;
          color: #f59e0b;
        }

        &.loop {
          background-color: #ccfbf1;
          color: #0f766e;
        }

        &.debug {
          background-color: #ecfeff;
          color: #0891b2;
        }
      }
    }

    .drawer-header-actions {
      display: inline-flex;
      align-items: center;
      gap: 12px;
      flex-shrink: 0;
    }

    .drawer-toggle-btn {
      height: 32px;
      width: 32px;
      min-width: 32px;
      padding: 0;
      border: none;
      border-radius: 0;
      background-color: transparent;
      font-size: 16px;
      line-height: 32px;
      transition: color 0.2s ease, background-color 0.2s ease;

      &.is-active {
        color: #2563eb;
      }

      &.is-inactive {
        color: #9ca3af;
      }

      &:hover {
        background-color: #f5f7fa;
      }
    }

    .close-icon {
      width: 24px;
      height: 24px;
      cursor: pointer;
      color: #6b7280;

      &:hover {
        color: #111827;
      }
    }
  }

  .drawer-content {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
  }

  .drawer-content--debug {
    padding-top: 16px;
  }

  .drawer-content--chatflow {
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }
}

:deep(.node-config-section-title) {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 400;
  color: #111827;
  line-height: 1.5;
}

:deep(.node-config-section-title::before) {
  content: none;
}

:deep(.start-field-dialog) {
  // border-radius: 24px;
  padding: 0 !important;
}

:deep(.start-field-dialog .el-dialog__header) {
  padding: 9px 20px !important;
  margin-right: 0 !important;
  background: #f8f8f8 !important;
  border-bottom: 1px solid #7072751a;
}

:deep(.start-field-dialog .el-dialog__title) {
  font-weight: 600;
  font-size: 16px;
  color: #3f3f3f;
  line-height: 1.5;
}

:deep(.start-field-dialog .el-dialog__headerbtn) {
  top: 0 !important;
  height: 42px;
}

:deep(.start-field-dialog .el-dialog__body) {
  padding: 20px 40px !important;
  overflow: visible;
}

:deep(.start-field-dialog .el-dialog__footer) {
  padding: 12px 24px 20px !important;
  border-top: 1px solid #7072751a;
}

.start-field-dialog-form {
  :deep(.el-form-item) {
    margin-bottom: 18px;
    align-items: flex-start;
  }

  :deep(.el-form-item:last-child) {
    margin-bottom: 0;
  }

  :deep(.el-form-item__label) {
    display: flex;
    align-items: center;
    min-height: 32px;
    padding: 0 16px 0 0;
    font-size: 14px;
    font-weight: 600;
    color: #111827;
    line-height: 20px;
    justify-content: flex-end;
  }

  :deep(.el-form-item__content) {
    min-width: 0;
    min-height: 32px;
    line-height: normal;
  }

  :deep(.el-form-item.start-field-form-item-radio) {
    align-items: center;
  }

  :deep(.el-form-item__error) {
    padding-top: 4px;
  }
}

.start-field-type-select {
  width: 100%;
}

.start-field-radio-group {
  display: flex;
  align-items: center;
  gap: 28px;
  min-height: 40px;
}

.start-field-radio-group :deep(.el-radio) {
  margin-right: 0;
}

.start-field-dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.loop-add-menu {
  z-index: 1001;
}

.node-config-header-name {
  flex: 1;
  min-width: 0;

  :deep(.el-input__wrapper) {
    padding: 0;
    border-radius: 0;
    background: transparent;
    box-shadow: none;
  }

  :deep(.el-input__inner) {
    height: auto;
    padding: 0;
    color: #111827;
    font-size: 18px;
    font-weight: 600;
    line-height: 1.4;
  }
}

.workflow-editor :deep(.el-input__wrapper),
.workflow-editor :deep(.el-select__wrapper),
.workflow-editor :deep(.el-textarea__inner),
.workflow-editor :deep(.el-input-number),
.workflow-editor :deep(.el-input-number__wrapper),
:deep(.start-field-dialog .el-input__wrapper),
:deep(.start-field-dialog .el-select__wrapper),
:deep(.start-field-dialog .el-textarea__inner),
:deep(.start-field-dialog .el-input-number),
:deep(.start-field-dialog .el-input-number__wrapper),
:deep(.llm-model-config-dialog .el-input__wrapper),
:deep(.llm-model-config-dialog .el-select__wrapper),
:deep(.llm-model-config-dialog .el-textarea__inner),
:deep(.llm-model-config-dialog .el-input-number),
:deep(.llm-model-config-dialog .el-input-number__wrapper) {
  border-radius: 0 !important;
}
.workflow-editor :deep(.el-input-number__increase),
.workflow-editor :deep(.el-input-number__decrease),
:deep(.start-field-dialog .el-input-number__increase),
:deep(.start-field-dialog .el-input-number__decrease),
:deep(.llm-model-config-dialog .el-input-number__increase),
:deep(.llm-model-config-dialog .el-input-number__decrease) {
  border-radius: 0 !important;
}
:deep(.dydome-square-popper.el-popper),
:deep(.dydome-square-popper.el-select__popper) {
  border-radius: 0 !important;
}
.drawer-loop-glyph {
  font-size: 18px;
  font-weight: 700;
  line-height: 1;
}

.loop-step-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.loop-step-editor-start,
.loop-step-editor-card {
  padding: 14px;
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  background-color: #f9fafb;
}

.loop-step-editor-start {
  display: flex;
  align-items: center;
  gap: 12px;
}

.loop-step-editor-lock {
  width: 40px;
  height: 40px;
  border-radius: 999px;
  background-color: #eff6ff;
  color: #2563eb;
  border: 1px solid #bfdbfe;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.loop-step-editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}

.loop-step-editor-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.loop-step-editor-title {
  font-size: 13px;
  font-weight: 600;
  color: #111827;
}

.loop-step-editor-index {
  font-size: 13px;
  font-weight: 600;
  color: #111827;
}

.loop-step-editor-type {
  padding: 2px 8px;
  border-radius: 999px;
  background-color: #eef2ff;
  color: #4338ca;
  font-size: 11px;
  font-weight: 700;
}

.loop-step-editor-type.type-condition {
  background-color: #fef3c7;
  color: #b45309;
}

.loop-step-editor-type.type-loop {
  background-color: #ccfbf1;
  color: #0f766e;
}

.loop-step-editor-type.type-reply {
  background-color: #ffedd5;
  color: #c2410c;
}

.loop-step-editor-tip {
  margin-top: 8px;
  font-size: 12px;
  line-height: 1.6;
  color: #6b7280;
}

.loop-empty-tip {
  padding: 12px 14px;
  border-radius: 12px;
  border: 1px dashed #cbd5e1;
  background-color: #fff;
  color: #6b7280;
  font-size: 12px;
  line-height: 1.6;
}

.status-bar {
  display: flex;
  gap: 16px;
  padding: 8px 20px;
  background-color: #fff;
  border-top: 1px solid #e5e7eb;
  font-size: 12px;
  color: #6b7280;
}

// Dify 风格的连接线样式
:deep(.vue-flow__edge-path) {
  stroke: #999;
  stroke-width: 2;
  transition: stroke 0.2s;
}

:deep(.vue-flow__edge:hover .vue-flow__edge-path) {
  stroke: #666;
  stroke-width: 2.5;
}

:deep(.vue-flow__edge.selected .vue-flow__edge-path) {
  stroke: #3b82f6;
  stroke-width: 2.5;
}

:deep(.vue-flow__edge-marker) {
  fill: #999;
}

:deep(.vue-flow__edge:hover .vue-flow__edge-marker) {
  fill: #666;
}

:deep(.vue-flow__edge.selected .vue-flow__edge-marker) {
  fill: #3b82f6;
}

// 连接时的预览线样式
:deep(.vue-flow__connection-path) {
  stroke: #3b82f6;
  stroke-width: 2;
  stroke-dasharray: 5 5;
}
:deep(.edge-add-btn) {
  cursor: pointer;
  transition: all 0.2s ease;
  opacity: 0;
  pointer-events: none;

  .add-btn-bg {
    fill: #3b82f6;
    transition: all 0.2s ease;
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
  }

  &:hover .add-btn-bg {
    fill: #2563eb;
    r: 13;
  }

  &:active .add-btn-bg {
    fill: #1d4ed8;
  }

  text {
    font-size: 14px;
    font-weight: 300;
    dominant-baseline: central;
  }
}
:deep(.edge-wrapper:hover .edge-add-btn),
:deep(.edge-add-btn:hover) {
  opacity: 1;
  pointer-events: all;
}
</style>
