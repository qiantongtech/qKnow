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
  5. Tampering, circumventing, or destroying copyright notices, license
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
  软件名称：qKnow 知识平台（商业版） | 软著登字第 15980140 号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的"可访问性"均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
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
  <div class="app-container" ref="app-container">
    <el-container>
      <el-aside class="left-aside">
        <div class="border-item-head">
          <div class="head-title">编排</div>
        </div>
        <el-form
          class="agent-form"
          ref="agentFormRef"
          :model="form"
          :rules="formRules"
          label-width="80px"
          @submit.prevent
        >
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="模型" prop="modelId">
                <el-select
                  v-model="form.modelName"
                  placeholder="请选择模型"
                  filterable
                  style="width: 100%"
                >
                  <el-option-group
                    v-for="group in modelOptions"
                    :key="group.label.zh_Hans"
                    :label="group.label.zh_Hans"
                  >
                    <el-option
                      v-for="option in group.models || []"
                      :key="option.model"
                      :label="option.model"
                      :value="option.model"
                    />
                  </el-option-group>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="提示词" prop="prePrompt">
                <el-input
                  v-model="form.prePrompt"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入提示词"
                  resize="vertical"
                  show-word-limit
                  maxlength="5000个字符"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="memory-settings">
            <el-col :span="24">
              <el-form-item label="历史记录">
                <el-input-number
                  v-model="form.historyMessageCount"
                  :min="0"
                  :max="20"
                  :step="1"
                  step-strictly
                  controls-position="right"
                  style="width: 100%"
                />
              </el-form-item>
              <el-form-item label="长期记忆">
                <el-switch v-model="form.longTermMemoryEnabled" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="thinking-settings">
            <el-col :span="24">
              <el-form-item label="思考过程">
                <el-switch
                  v-model="thinkingEnabled"
                  :disabled="!thinkingSupported"
                  :title="thinkingSupported ? '控制模型是否启用思考过程' : '当前模型不支持思考过程控制'"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 变量 -->
          <!--          <el-row :gutter="20">-->
          <!--            <el-col :span="24">-->
          <!--              <el-form-item label="变量">-->
          <!--                <el-button type="primary" size="small" @click="addItem" plain>新增变量</el-button>-->
          <!--              </el-form-item>-->
          <!--              <div style="margin: 0 0 10px 80px">-->
          <!--                <el-table :data="form.variables" border style="width: 100%">-->
          <!--                  <el-table-column prop="name" label="变量名" width="150">-->
          <!--                    <template #default="{ row, $index }">-->
          <!--                      <el-input v-model="row.name" placeholder="请输入变量名" size="small" />-->
          <!--                    </template>-->
          <!--                  </el-table-column>-->
          <!--                  <el-table-column prop="description" label="显示名称" >-->
          <!--                    <template #default="{ row }">-->
          <!--                      <el-input v-model="row.description" placeholder="请输入描述" size="small" />-->
          <!--                    </template>-->
          <!--                  </el-table-column>-->
          <!--                  <el-table-column label="操作" width="80" align="center">-->
          <!--                    <template #default="{ $index }">-->
          <!--                      <el-button type="danger" size="small" @click="handleDeleteVariable($index)">删除</el-button>-->
          <!--                    </template>-->
          <!--                  </el-table-column>-->
          <!--                </el-table>-->
          <!--              </div>-->
          <!--            </el-col>-->
          <!--          </el-row>-->
          <el-row :gutter="20">
            <el-col :span="24">
              <!-- 知识库 -->
              <el-form-item label="知识库">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleAddKnowledge"
                  plain
                >
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i
                  >导入知识库</el-button
                >
              </el-form-item>
              <div style="margin: 0 0 10px 80px">
                <el-table :data="form.knowledges" border style="width: 100%">
                  <el-table-column prop="name" label="知识库名称">
                    <template #default="{ row }">
                      {{ row.name }}
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template #default="{ $index }">
                      <el-button
                        type="danger"
                        size="small"
                        @click="handleDeleteKnowledge($index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <!-- 可调用 Agent / 工作流 -->
              <el-form-item label="调用能力">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleAddCallableBot"
                  plain
                >
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i>
                  导入 Agent / 工作流
                </el-button>
              </el-form-item>
              <div style="margin: 0 0 10px 80px">
                <el-table :data="form.callableBots" border style="width: 100%">
                  <el-table-column prop="name" label="名称" />
                  <el-table-column label="类型" width="100" align="center">
                    <template #default="scope">
                      <div>
                        <dict-tag
                          :options="kg_bot_type"
                          :value="scope.row.type"
                        />
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template #default="{ $index }">
                      <el-button
                        type="danger"
                        size="small"
                        @click="handleDeleteCallableBot($index)"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <!-- 工具 -->
              <el-form-item label="工具">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleAddTool"
                  plain
                >
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i
                  >导入工具</el-button
                >
              </el-form-item>
              <div style="margin: 0 0 10px 80px">
                <el-table :data="form.tools" border style="width: 100%">
                  <el-table-column prop="name" label="工具名称">
                    <template #default="{ row }">
                      {{ row.name }}
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template #default="{ $index }">
                      <el-button
                        type="danger"
                        size="small"
                        @click="handleDeleteTool($index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <!-- mcp -->
              <el-form-item label="MCP">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleAddMcp"
                  plain
                >
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i
                  >导入MCP</el-button
                >
              </el-form-item>
              <div style="margin: 0 0 10px 80px">
                <el-table :data="form.mcp" border style="width: 100%">
                  <el-table-column prop="name" label="MCP名称">
                    <template #default="{ row }">
                      {{ row.name }}
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template #default="{ $index }">
                      <el-button
                        type="danger"
                        size="small"
                        @click="handleDeleteMcp($index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <!-- Skills -->
              <el-form-item label="Skills">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleAddSkill"
                  plain
                >
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i
                  >导入Skills</el-button
                >
              </el-form-item>
              <div style="margin: 0 0 10px 80px">
                <el-table :data="form.skills" border style="width: 100%">
                  <el-table-column prop="name" label="Skills 名称">
                    <template #default="{ row }">
                      {{ row.name }}
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template #default="{ $index }">
                      <el-button
                        type="danger"
                        size="small"
                        @click="handleDeleteSkill($index)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
        </el-form>
        <div class="dialog-footer">
          <!--          <el-button size="mini" @click="handleReset">清 空</el-button>-->
          <el-button
            type="primary"
            size="mini"
            :loading="loading"
            @click="handleSubmit"
          >
            <svg-icon icon-class="bc" />
            保存
          </el-button>
        </div>
      </el-aside>
      <el-main class="right-main">
        <div class="border-item-head">
          <span class="head-title">调试与预览 </span>
          <el-button
            v-if="messageList.length"
            link
            type="primary"
            @click="handleClearMessages"
            >清空对话</el-button
          >
        </div>
        <el-container>
          <el-main>
            <agent-message-list
              ref="messageListRef"
              :list="messageList"
              empty-text="暂无对话，开始和 Bot 聊天吧"
            />
          </el-main>
          <agent-chat-input
            v-model="chatInput"
            :loading="loading"
            @send="handleSendMessage"
          />
        </el-container>
      </el-main>
    </el-container>

    <!-- 知识库选择组件 -->
    <knowledge-base-multiple
      ref="knowledgeBaseMultipleRef"
      @confirm="handleKnowledgeConfirm"
    />

    <!-- 工具选择组件 -->
    <method-multiple-selection
      ref="methodMultipleSelectionRef"
      @confirm="handleToolConfirm"
    />

    <!-- Skills 选择组件 -->
    <skills-selection ref="skillsSelectionRef" @confirm="handleSkillConfirm" />

    <!-- mcp选择组件 -->
    <mcp-multiple-selection
      ref="mcpMultipleSelectionRef"
      @confirm="handleMcpConfirm"
    />

    <!-- Agent / 工作流选择组件 -->
    <callable-bot-multiple-selection
      ref="callableBotMultipleSelectionRef"
      @confirm="handleCallableBotConfirm"
    />
  </div>
</template>

<script setup name="Agent">
import methodMultipleSelection from "@/views/kb/tool/selection/method-multiple-selection.vue";
import knowledgeBaseMultiple from "@/views/kmc/knowledgeBase/selection/knowledgeBaseMultiple.vue";
import skillsSelection from "@/views/kb/skills/selection/skills-selection.vue";
import mcpMultipleSelection from "@/views/kb/mcp/selection/mcp-multiple-selection.vue";
import CallableBotMultipleSelection from "./components/CallableBotMultipleSelection.vue";

import { getChatModelDict } from "@/api/ai/myModel/myModel.js";
import {
  addConfig,
  getConfigByBotId,
  updateConfig,
} from "@/api/kb/agent/config";
import { debugAgent } from "@/api/kb/agent/debug";
import AgentMessageList from "./components/MessageList.vue";
import AgentChatInput from "./components/ChatInput.vue";
import SvgIcon from "@/components/SvgIcon/index.vue";
const { proxy } = getCurrentInstance();
const { kg_bot_type } = proxy.useDict("kg_bot_type");
// 组件引用
const agentFormRef = ref(null);
const knowledgeBaseMultipleRef = ref(null);
const methodMultipleSelectionRef = ref(null);
const skillsSelectionRef = ref(null);
const mcpMultipleSelectionRef = ref(null);
const callableBotMultipleSelectionRef = ref(null);
const messageListRef = ref(null); // 消息列表引用

// 表单数据
const form = reactive({
  id: null,
  modelId: "",
  modelName: "",
  prePrompt: "",
  variables: [{ id: 1, name: "input", description: "用户输入" }],
  knowledges: [],
  tools: [],
  skills: [],
  mcp: [],
  callableBots: [],
  historyMessageCount: 1,
  longTermMemoryEnabled: false,
  showThinkingProcess: true,
});

// 从路由获取 botId（如果有）
const route = useRoute();
const botId = ref(Number(route.query.id));

// 表单验证规则
const formRules = {
  modelId: [{ required: true, message: "请选择大模型", trigger: "change" }],
};

// 聊天输入
const chatInput = ref("");
const modelOptions = ref([]);
const loading = ref(false);
const messageList = ref([]); // 消息列表
const abortController = ref(null); // 用于取消流式请求
const isScrolling = ref(false); // 用于判断用户是否在滚动

// 监听模型选择，自动获取 provider (modelId)
watchEffect(() => {
  const selectedModelName = form.modelName;
  if (!selectedModelName) return;

  for (const group of modelOptions.value) {
    const found = group.models?.find(
      (item) => item.model === selectedModelName
    );
    if (found) {
      form.modelId = group.provider; // 自动设置 modelId 为 provider
      return;
    }
  }
});

// 根据所选模型判断是否支持深度思考控制
const thinkingSupported = computed(() => {
  const modelName = form.modelName;
  if (!modelName) return false;

  // 优先使用模型字典中的能力标识（如 Dify 风格的 features）
  for (const group of modelOptions.value) {
    const found = group.models?.find((item) => item.model === modelName);
    if (found) {
      if (Array.isArray(found.features)) {
        return found.features.some((f) => /think|reason/i.test(`${f}`));
      }
      break;
    }
  }

  // 无能力标识时按模型名称识别常见推理模型
  const name = modelName.toLowerCase();
  return (
    /(^|[^a-z0-9])(o1|o3|o4|qwq|qvq|r1)([^a-z0-9]|$)/.test(name) ||
    /deepseek-reasoner/.test(name) ||
    /(^|[^a-z0-9])qwen3([^a-z0-9]|$)/.test(name) ||
    /(^|[^a-z0-9])gpt-5([^a-z0-9]|$)/.test(name) ||
    /gpt-oss/.test(name) ||
    /glm-(4\.[5-9]|[5-9])/.test(name) ||
    /think|reason/.test(name)
  );
});

const thinkingEnabled = computed({
  get: () => thinkingSupported.value && form.showThinkingProcess,
  set: (value) => {
    form.showThinkingProcess = value;
  },
});

// 加载 Agent 配置
function loadAgentConfig() {
  if (!botId.value) {
    getModelList();
    return;
  }

  // 根据 botId 获取配置
  getConfigByBotId(botId.value)
    .then((res) => {
      const config = res.data;

      // 先加载模型列表（确保选项存在）
      getModelList();

      if (!config) {
        return;
      }

      // 保存配置记录的 ID（用于修改操作）
      form.id = config.id;

      // 解析 modelConfig
      if (config.modelConfig) {
        try {
          const modelConfigObj = JSON.parse(config.modelConfig);
          form.modelId = modelConfigObj.modelId;
          form.modelName = modelConfigObj.modelName;
        } catch (e) {
          console.error("解析 modelConfig 失败:", e);
        }
      }

      // 设置其他字段
      form.prePrompt = config.prePrompt || "";
      if (config.parameters) {
        try {
          const parameters = JSON.parse(config.parameters);
          form.historyMessageCount = normalizeHistoryMessageCount(
            parameters.historyMessageCount
          );
          form.longTermMemoryEnabled =
            parameters.longTermMemoryEnabled === true;
          form.showThinkingProcess =
            parameters.thinkingDisplayMode !== "hidden";
        } catch (e) {
          console.error("解析 parameters 失败:", e);
        }
      }
      // 解析 knowledgeIds 和 knowledgeNames
      if (config.knowledgeIds && config.knowledgeNames) {
        const ids = config.knowledgeIds.split(",").filter((id) => id.trim());
        const names = config.knowledgeNames;
        form.knowledges = ids.map((id, index) => ({
          id: Number(id),
          name: names[index],
          description: "",
        }));
      }

      // 解析 toolMethodIds 和 toolMethodNames
      if (config.toolMethodIds && config.toolMethodNames) {
        const ids = config.toolMethodIds.split(",").filter((id) => id.trim());
        const names = config.toolMethodNames;
        form.tools = ids.map((id, index) => ({
          id: Number(id),
          name: names[index],
          description: "",
        }));
      }

      // 解析 mcpConfigIds 和 mcpConfigNames
      if (config.mcpConfigIds && config.mcpConfigNames) {
        const ids = config.mcpConfigIds.split(",").filter((id) => id.trim());
        const names = config.mcpConfigNames;
        form.mcp = ids.map((id, index) => ({
          id: Number(id),
          name: names[index],
          description: "",
        }));
      }

      // 解析 skillIds 和 skillNames
      if (config.skillIds && config.skillNames) {
        const ids = config.skillIds.split(",").filter((id) => id.trim());
        const names = config.skillNames;
        form.skills = ids.map((id, index) => ({
          id: Number(id),
          name: names[index],
          description: "",
        }));
      }

      // 解析 Agent 可调用的 Agent / 工作流
      if (config.callableBotIds) {
        const ids = config.callableBotIds.split(",").filter((id) => id.trim());
        const names = config.callableBotNames || [];
        form.callableBots = ids.map((id, index) => ({
          id: Number(id),
          name: names[index] || `Bot ${id}`,
          type: config.callableBotTypes?.[index] ?? 0,
          description: "",
        }));
      }
      restoreLongTermMemory();
    })
    .catch((err) => {
      getModelList();
    });
}

function getModelList() {
  getChatModelDict().then((res) => {
    if (res?.code !== 200 || !Array.isArray(res?.data)) {
      modelOptions.value = [];
      return;
    }

    modelOptions.value = res.data;

    // 只有当表单中没有选择模型时，才默认选择第一个大模型
    if (
      !form.modelId &&
      !form.modelName &&
      modelOptions.value.length > 0 &&
      modelOptions.value[0].models?.length > 0
    ) {
      const firstGroup = modelOptions.value[0];
      const firstModel = firstGroup.models[0];
      form.modelId = firstGroup.provider; // 使用 provider 作为 modelId
      form.modelName = firstModel.model; // 使用 model 作为 modelName
    }
  });
}

// 处理添加知识库
function handleAddKnowledge() {
  knowledgeBaseMultipleRef.value.open(form.knowledges || []);
}

// 处理知识库确认选择
function handleKnowledgeConfirm(selectedData) {
  form.knowledges = selectedData.map((item) => ({
    id: item.id,
    name: item.name,
    description: item.description,
  }));
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个知识库`);
}

// 处理删除知识库
function handleDeleteKnowledge(index) {
  form.knowledges.splice(index, 1);
}

function handleAddCallableBot() {
  const ownerBotId = Number(botId.value);
  callableBotMultipleSelectionRef.value.open(
    form.callableBots || [],
    Number.isFinite(ownerBotId) ? ownerBotId : null
  );
}

function handleCallableBotConfirm(selectedData) {
  form.callableBots = selectedData.map((item) => ({
    id: item.id,
    name: item.name,
    type: item.type,
    description: item.description,
  }));
  proxy.$modal.msgSuccess(`已导入 ${selectedData.length} 个调用能力`);
}

function handleDeleteCallableBot(index) {
  form.callableBots.splice(index, 1);
}

// 处理添加工具
function handleAddTool() {
  methodMultipleSelectionRef.value.open(form.tools || []);
}

// 处理工具确认选择
function handleToolConfirm(selectedData) {
  form.tools = selectedData.map((item) => ({
    id: item.id,
    name: item.name,
    description: item.description,
  }));
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个工具`);
}

// 处理删除工具
function handleDeleteTool(index) {
  form.tools.splice(index, 1);
}

// 处理添加 Skills
function handleAddSkill() {
  skillsSelectionRef.value.open(form.skills || []);
}

// 处理 Skills 确认选择
function handleSkillConfirm(selectedData) {
  form.skills = selectedData.map((item) => ({
    id: item.id,
    name: item.name,
    description: item.description,
  }));
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个 Skills`);
}
// 处理添加工具
function handleAddMcp() {
  mcpMultipleSelectionRef.value.open(form.mcp || []);
}

// 处理工具确认选择
function handleMcpConfirm(selectedData) {
  form.mcp = selectedData.map((item) => ({
    id: item.id,
    name: item.name,
    description: item.description,
  }));
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个 Skills`);
}

// 处理删除 Skills
function handleDeleteSkill(index) {
  form.skills.splice(index, 1);
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个mcp`);
}

// 处理删除工具
function handleDeleteMcp(index) {
  form.mcp.splice(index, 1);
}

const memoryStorageKey = computed(
  () => `qknow-agent-debug-memory:${botId.value || "new"}`
);

function normalizeHistoryMessageCount(value) {
  const count = Number(value);
  if (!Number.isFinite(count)) return 1;
  return Math.min(20, Math.max(0, Math.trunc(count)));
}

function parseTaggedThinking(content) {
  const startIndex = content.indexOf("<think");
  if (startIndex === -1) {
    return { answer: content, reasoning: "" };
  }
  const startTagEnd = content.indexOf(">", startIndex);
  if (startTagEnd === -1) {
    return { answer: content.substring(0, startIndex), reasoning: "" };
  }
  const endTag = "</think>";
  const endIndex = content.indexOf(endTag, startTagEnd + 1);
  const reasoning =
    endIndex === -1
      ? content.substring(startTagEnd + 1)
      : content.substring(startTagEnd + 1, endIndex);
  const answer =
    content.substring(0, startIndex) +
    (endIndex === -1 ? "" : content.substring(endIndex + endTag.length));
  return { answer, reasoning };
}

// 不同模型/适配器可能返回“增量片段”或“截至当前的累计文本”，统一合并以避免重复。
function mergeStreamingText(current, incoming) {
  if (!incoming) return current;
  if (!current) return incoming;
  if (incoming.startsWith(current)) return incoming;
  if (current.endsWith(incoming)) return current;
  return current + incoming;
}

// 模型原始文本可能包含函数协议使用的 workflow_<id>/agent_<id>，展示时替换为业务名称。
function replaceCallableBotTechnicalNames(content) {
  if (!content) return content;
  const callableBotNameMap = new Map(
    form.callableBots.map((callableBot) => [
      String(callableBot.id),
      callableBot.name,
    ])
  );
  // 不使用 \b：中文字符和工具名之间没有 ASCII 单词边界，容易漏掉
  // “agent_39生成”这类紧邻中文的情况。
  return content.replace(
    /(^|[^A-Za-z0-9_])((?:workflow|agent)_(\d+))(?![A-Za-z0-9_])/gi,
    (match, prefix, technicalName, id) =>
      `${prefix}${callableBotNameMap.get(id) || "已调用能力"}`
  );
}

function sanitizeAgentDisplayText(content) {
  if (!content) return content;
  return (
    replaceCallableBotTechnicalNames(content)
      // 某些模型会把工具调用协议片段原样带入回答；只移除明确的
      // function/tool call 标记，不对普通 JSON 或用户代码做宽泛替换。
      .replace(/\[\s*(?:tool_call|function_call)\s*\]/gi, "")
      .replace(/<\|(?:tool_call|function_call)\|>/gi, "")
      .trim()
  );
}

function getHistoryMessages() {
  const maxRounds = normalizeHistoryMessageCount(form.historyMessageCount);
  if (maxRounds === 0) return [];

  const completedMessages = [];
  for (let index = 0; index < messageList.value.length - 1; index++) {
    const userMessage = messageList.value[index];
    const assistantMessage = messageList.value[index + 1];
    if (
      userMessage?.type === 0 &&
      assistantMessage?.type === 1 &&
      userMessage.content &&
      assistantMessage.content &&
      assistantMessage.content !== "思考中..." &&
      !assistantMessage.content.startsWith("错误：")
    ) {
      completedMessages.push(userMessage, assistantMessage);
      index++;
    }
  }

  return completedMessages.slice(-maxRounds * 2).map((message) => ({
    role: message.type === 0 ? "user" : "assistant",
    content: message.content,
  }));
}

function persistLongTermMemory() {
  if (!form.longTermMemoryEnabled) return;
  try {
    localStorage.setItem(
      memoryStorageKey.value,
      JSON.stringify(messageList.value.slice(-100))
    );
  } catch (e) {
    console.error("保存长期记忆失败:", e);
  }
}

function restoreLongTermMemory() {
  if (!form.longTermMemoryEnabled) return;
  try {
    const storedMessages = JSON.parse(
      localStorage.getItem(memoryStorageKey.value) || "[]"
    );
    if (Array.isArray(storedMessages)) {
      messageList.value = storedMessages
        .filter(
          (message) =>
            (message?.type === 0 || message?.type === 1) &&
            typeof message?.content === "string"
        )
        .map((message) => ({
          ...message,
          content: replaceCallableBotTechnicalNames(message.content),
          reasoningContent: replaceCallableBotTechnicalNames(
            message.reasoningContent || ""
          ),
        }));
    }
  } catch {
    localStorage.removeItem(memoryStorageKey.value);
  }
}

function handleClearMessages() {
  messageList.value = [];
  localStorage.removeItem(memoryStorageKey.value);
}

watch(
  () => [form.longTermMemoryEnabled, messageList.value],
  ([enabled]) => {
    if (enabled) {
      persistLongTermMemory();
    } else {
      localStorage.removeItem(memoryStorageKey.value);
    }
  },
  { deep: true }
);

// 处理发送消息
function handleSendMessage(content) {
  if (!content || !content.trim()) {
    proxy.$modal.msgWarning("请输入消息内容");
    return;
  }

  // 当前问题入列前截取历史，避免把本轮问题重复发送给模型。
  const historyMessages = getHistoryMessages();

  // 添加用户消息
  const userMessage = {
    type: 0,
    content: content,
    createTime: new Date(),
  };
  messageList.value.push(userMessage);

  // 清空输入框
  chatInput.value = "";

  // 设置加载状态
  loading.value = true;

  // 重置用户滚动状态，确保流式输出时保持在底部
  isScrolling.value = false;

  // 创建机器人消息占位
  const botMessageIndex = messageList.value.length;
  const botStartTime = Date.now();
  const botMessage = {
    type: 1,
    content: "思考中...",
    createTime: new Date(),
    duration: 0,
    thinkingExpanded: true,
  };
  messageList.value.push(botMessage);
  // 实时更新用时（每秒刷新）
  const durationTimer = setInterval(() => {
    const msg = messageList.value[botMessageIndex];
    if (msg) msg.duration = Date.now() - botStartTime;
  }, 1000);

  // 构建请求参数
  const requestData = {
    botId: botId.value,
    question: content,
    input: null,
    modelConfig: JSON.stringify({
      modelId: form.modelId,
      modelName: form.modelName,
    }),
    prePrompt: form.prePrompt,
    parameters: JSON.stringify({
      historyMessageCount: normalizeHistoryMessageCount(
        form.historyMessageCount
      ),
      longTermMemoryEnabled: form.longTermMemoryEnabled,
      thinkingEnabled: thinkingSupported.value
        ? thinkingEnabled.value
        : null,
      reasoningEffort: "high",
      thinkingDisplayMode: thinkingEnabled.value ? "full" : "hidden",
    }),
    historyMessages,
    knowledgeIds: form.knowledges.map((k) => k.id).join(","),
    callableBotIds: form.callableBots
      .map((callableBot) => callableBot.id)
      .join(","),
    toolMethodIds: form.tools.map((t) => t.id).join(","),
    skillIds: form.skills.map((s) => s.id).join(","),
    mcpConfigIds: form.mcp.map((t) => t.id).join(","),
  };

  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollToBottom(true);
    }
  });

  // 创建 AbortController 用于取消请求
  abortController.value = new AbortController();

  let rawBotContent = "";
  let structuredReasoningContent = "";

  // 调用调试接口（流式输出）
  debugAgent(
    requestData,
    // onmessage 回调
    (event) => {
      try {
        const data = JSON.parse(event.data);
        if (Number(data?.code ?? 200) !== 200) {
          const errorText =
            data?.msg ||
            data?.message ||
            data?.data?.msg ||
            "Agent 执行失败，请检查模型配置、工具和 MCP 连接";
          messageList.value[botMessageIndex].content = `错误：${errorText}`;
          loading.value = false;
          throw new Error(errorText);
        }
        if (data.code === 200 && data.data) {
          // 提取机器人回复内容
          const messageContent = data.data.receive?.content || "";
          const reasoningContent = data.data.receive?.reasoningContent || "";
          if (reasoningContent) {
            structuredReasoningContent = mergeStreamingText(
              structuredReasoningContent,
              reasoningContent
            );
          }
          if (messageContent || reasoningContent) {
            rawBotContent = mergeStreamingText(rawBotContent, messageContent);
            // 始终从 content 中剥离 <think...>...</think> 标签，得到干净的正式回答
            // （后端 sanitizeModelText 未剥离该标签，content 里可能仍含思考过程）
            const parsedContent = parseTaggedThinking(rawBotContent);
            const answer = parsedContent.answer;
            // reasoning 优先使用后端已分离的 metadata.reasoningContent，
            // 没有则回退到从 content 中解析出的 <think> 标签内容
            const reasoning = structuredReasoningContent
              ? structuredReasoningContent
              : parsedContent.reasoning;
            // 更新机器人消息
            messageList.value[botMessageIndex].content =
              sanitizeAgentDisplayText(answer) || "思考中...";
            messageList.value[botMessageIndex].reasoningContent =
              thinkingEnabled.value
                ? sanitizeAgentDisplayText(reasoning)
                : "";

            // 每次更新内容后滚动到底部
            nextTick(() => {
              if (messageListRef.value) {
                messageListRef.value.scrollToBottom(true);
              }
            });
          }
        }
      } catch (e) {
        const errorText = e?.message || "Agent 调试响应解析失败";
        messageList.value[botMessageIndex].content = `错误：${errorText}`;
        loading.value = false;
        throw e;
      }
    },
    // onerror 回调
    (error) => {
      clearInterval(durationTimer);
      const msg = messageList.value[botMessageIndex];
      msg.content = `错误：${error?.message || error || "请求失败"}`;
      msg.thinkingExpanded = false;
      loading.value = false;
      throw error; // 抛出错误以终止连接
    },
    // onclose 回调
    () => {
      clearInterval(durationTimer);
      loading.value = false;
      const finalMessage = messageList.value[botMessageIndex];
      if (finalMessage) {
        finalMessage.duration = Date.now() - botStartTime;
        // 思考完毕后自动收起思考过程
        finalMessage.thinkingExpanded = false;
        // 流式事件可能在最后一个事件才补齐完整文本。收尾时再次清洗，
        // 确保历史展示和本次展示使用同一套脱敏规则。
        finalMessage.content = sanitizeAgentDisplayText(finalMessage.content);
        finalMessage.reasoningContent = sanitizeAgentDisplayText(
          finalMessage.reasoningContent || ""
        );
      }
      // 如果最终没有内容，显示默认消息
      if (!messageList.value[botMessageIndex].content) {
        messageList.value[botMessageIndex].content =
          "错误：Agent 执行完成，但模型未返回内容，请检查模型配置、提示词或工具调用";
      }
    }
  ).catch((error) => {
    loading.value = false;
    const msg = messageList.value[botMessageIndex];
    if (msg) {
      msg.thinkingExpanded = false;
      if (!msg.content.includes("错误")) {
        msg.content = `错误：${error?.message || error || "请求失败"}`;
      }
    }
  });
}

// 清空表单
function handleReset() {
  form.modelId = "";
  form.modelName = "";
  form.prePrompt = "";
  form.variables = [{ id: 1, name: "input", description: "用户输入" }];
  form.knowledges = [];
  form.callableBots = [];
  form.tools = [];
  form.skills = [];
  form.historyMessageCount = 1;
  form.longTermMemoryEnabled = false;
  form.showThinkingProcess = true;
  proxy.$modal.msgSuccess("已清空表单");
}

// 新增变量
function addItem() {
  if (!form.variables) {
    form.variables = [];
  }
  const newId =
    form.variables.length > 0
      ? Math.max(...form.variables.map((v) => v.id)) + 1
      : 1;
  form.variables.push({ id: newId, name: "", description: "" });
}

// 删除变量
function handleDeleteVariable(index) {
  form.variables.splice(index, 1);
}

// 提交保存
function handleSubmit() {
  // 校验表单
  agentFormRef.value.validate((valid) => {
    if (!valid) {
      proxy.$modal.msgError("请填写必填项");
      return;
    }

    // 构建提交数据，匹配后端 KbAgentConfigSaveReqVO
    const submitData = {
      id: form.id, // 配置记录的 ID（修改时需要）
      botId: botId.value,
      // 将模型配置转为 JSON 格式
      modelConfig: JSON.stringify({
        modelId: form.modelId,
        modelName: form.modelName,
      }),
      prePrompt: form.prePrompt,
      parameters: JSON.stringify({
        historyMessageCount: normalizeHistoryMessageCount(
          form.historyMessageCount
        ),
        longTermMemoryEnabled: form.longTermMemoryEnabled,
        thinkingEnabled: thinkingSupported.value
          ? thinkingEnabled.value
          : null,
        reasoningEffort: "high",
        thinkingDisplayMode: thinkingEnabled.value ? "full" : "hidden",
      }),
      // 知识库 IDs（从对象数组提取 ID 数组）
      knowledgeIds: form.knowledges.map((k) => k.id).join(","),
      callableBotIds: form.callableBots
        .map((callableBot) => callableBot.id)
        .join(","),
      // 工具方法 IDs（从对象数组提取 ID 数组）
      toolMethodIds: form.tools.map((t) => t.id).join(","),
      // Skills IDs
      skillIds: form.skills.map((s) => s.id).join(","),
      mcpConfigIds: form.mcp.map((t) => t.id).join(","),
      remark: "",
    };

    proxy.$modal.loading("正在保存...");

    // 判断是新增还是修改
    const isUpdate = Boolean(form.id);
    const apiCall = isUpdate ? updateConfig(submitData) : addConfig(submitData);

    // 调用保存接口
    apiCall
      .then((response) => {
        if (!form.id && response?.data) {
          form.id = response.data;
        }
        proxy.$modal.msgSuccess(isUpdate ? "更新成功" : "保存成功");
        proxy.$modal.closeLoading();
      })
      .catch(() => {
        proxy.$modal.closeLoading();
      });
  });
}

// 只在初始化时调用一次
loadAgentConfig();
</script>

<style scoped lang="scss">
.border-item-head {
  height: 50px;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e8e8e8;
  .head-title {
    font-size: 16px;
    color: rgba(0, 0, 0, 0.85);
    display: flex;
    align-items: center;
    font-family: PingFang SC;
    font-weight: 500;

    &::before {
      display: inline-block;
      content: "";
      width: 6px;
      height: 16px;
      border-radius: 3px;
      background: var(--el-color-primary);
      margin-right: 8px;
    }
  }
}
.left-aside {
  margin-bottom: 0px;
  margin-right: 15px;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  height: calc(100vh - 124px);
  padding: 0;
  min-width: 500px;
  overflow: hidden;
  .agent-form {
    flex: 1;
    overflow-y: auto;
    padding: 8px 24px;
  }
  .dialog-footer {
    flex-shrink: 0; // 防止底部被压缩
    margin-left: auto;
    padding: 16px 24px;
    border-top: 1px solid #e8e8e8;
    width: 100%;
    text-align: right;
    button {
      width: 80px;
    }
  }
}
.right-main {
  height: calc(100vh - 124px);
  min-height: 0;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 0;

  .el-container {
    flex: 1;
    min-height: 0;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .el-main {
      flex: 1;
      min-height: 0;
      overflow: hidden;
      padding: 0;
      display: flex;
      flex-direction: column;
    }
  }
}

.memory-settings,
.thinking-settings {
  .memory-switch-help {
    margin-left: 10px;
    color: var(--el-text-color-secondary);
    font-size: 12px;
  }
}

.svg-icon {
  font-size: 12px;
  margin-right: 3px;
  vertical-align: middle;
  margin-top: -3px;
}
</style>
