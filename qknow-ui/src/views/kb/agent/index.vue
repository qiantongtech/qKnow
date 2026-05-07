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
          <div class="head-title">
            编排
          </div>
        </div>
        <el-form class="agent-form" ref="agentFormRef" :model="form" :rules="formRules" label-width="80px" @submit.prevent>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="模型" prop="modelId">
                <el-select
                    v-model="form.modelName"
                    placeholder="请选择模型"
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
                    maxlength="1024"
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
                <el-button type="primary" size="small" @click="handleAddKnowledge" plain>
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入知识库</el-button>
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
                      <el-button type="danger" size="small" @click="handleDeleteKnowledge($index)">删除</el-button>
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
                <el-button type="primary" size="small" @click="handleAddTool" plain>
                  <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入工具</el-button>
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
                      <el-button type="danger" size="small" @click="handleDeleteTool($index)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
        </el-form>
        <div class="dialog-footer">
<!--          <el-button size="mini" @click="handleReset">清 空</el-button>-->
          <el-button type="primary" size="mini" :loading="loading" @click="handleSubmit">
            <svg-icon icon-class="bc" />
            保存
          </el-button>
        </div>
      </el-aside>
      <el-main class="right-main">
        <div class="border-item-head">
          <span class="head-title">调试与预览 </span>
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
  </div>
</template>

<script setup name="Agent">
import methodMultipleSelection from "@/views/kb/tool/selection/method-multiple-selection.vue"
import knowledgeBaseMultiple from "@/views/kmc/knowledgeBase/selection/knowledgeBaseMultiple.vue"
import {getChatModelDict} from "@/api/ai/myModel/myModel.js";
import {addConfig, getConfigByBotId, updateConfig} from "@/api/kb/agent/config";
import {debugAgent} from "@/api/kb/agent/debug";
import AgentMessageList from './components/MessageList.vue';
import AgentChatInput from './components/ChatInput.vue';
import SvgIcon from "@/components/SvgIcon/index.vue";
const { proxy } = getCurrentInstance();

// 组件引用
const agentFormRef = ref(null);
const knowledgeBaseMultipleRef = ref(null);
const methodMultipleSelectionRef = ref(null);
const messageListRef = ref(null); // 消息列表引用

// 表单数据
const form = reactive({
  id: null,
  modelId: '',
  modelName: '',
  prePrompt: '',
  variables: [
    { id: 1, name: 'input', description: '用户输入' }
  ],
  knowledges: [],
  tools: []
});

// 从路由获取 botId（如果有）
const route = useRoute();
const botId = ref(Number(route.query.id));

// 表单验证规则
const formRules = {
  modelId: [
    { required: true, message: '请选择大模型', trigger: 'change' }
  ],
};

// 聊天输入
const chatInput = ref('');
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
    const found = group.models?.find((item) => item.model === selectedModelName);
    if (found) {
      form.modelId = group.provider; // 自动设置 modelId 为 provider
      return;
    }
  }
});

// 加载 Agent 配置
function loadAgentConfig() {
  if (!botId.value) {
    getModelList();
    return;
  }

  // 根据 botId 获取配置
  getConfigByBotId(botId.value).then((res) => {
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
        console.error('解析 modelConfig 失败:', e);
      }
    }

    // 设置其他字段
    form.prePrompt = config.prePrompt || '';

    // 解析 knowledgeIds 和 knowledgeNames
    if (config.knowledgeIds && config.knowledgeNames) {
      const ids = config.knowledgeIds.split(',').filter(id => id.trim());
      const names = config.knowledgeNames;
      form.knowledges = ids.map((id, index) => ({
        id: Number(id),
        name: names[index],
        description: ''
      }));
    }

    // 解析 toolMethodIds 和 toolMethodNames
    if (config.toolMethodIds && config.toolMethodNames) {
      const ids = config.toolMethodIds.split(',').filter(id => id.trim());
      const names = config.toolMethodNames;
      form.tools = ids.map((id, index) => ({
        id: Number(id),
        name: names[index],
        description: ''
      }));
    }
  }).catch((err) => {
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
    if (!form.modelId && !form.modelName && modelOptions.value.length > 0 && modelOptions.value[0].models?.length > 0) {
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
  form.knowledges = selectedData.map(item => ({
    id: item.id,
    name: item.name,
    description: item.description
  }));
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个知识库`);
}

// 处理删除知识库
function handleDeleteKnowledge(index) {
  form.knowledges.splice(index, 1);
}

// 处理添加工具
function handleAddTool() {
  methodMultipleSelectionRef.value.open(form.tools || []);
}

// 处理工具确认选择
function handleToolConfirm(selectedData) {
  form.tools = selectedData.map(item => ({
    id: item.id,
    name: item.name,
    description: item.description
  }));
  proxy.$modal.msgSuccess(`已选择 ${selectedData.length} 个工具`);
}

// 处理删除工具
function handleDeleteTool(index) {
  form.tools.splice(index, 1);
}

// 处理发送消息
function handleSendMessage(content) {
  if (!content || !content.trim()) {
    proxy.$modal.msgWarning('请输入消息内容');
    return;
  }

  // 添加用户消息
  const userMessage = {
    type: 0,
    content: content,
    createTime: new Date()
  };
  messageList.value.push(userMessage);

  // 清空输入框
  chatInput.value = '';

  // 设置加载状态
  loading.value = true;

  // 重置用户滚动状态，确保流式输出时保持在底部
  isScrolling.value = false;

  // 创建机器人消息占位
  const botMessageIndex = messageList.value.length;
  const botMessage = {
    type: 1,
    content: '思考中',
    createTime: new Date()
  };
  messageList.value.push(botMessage);

  // 构建请求参数
  const requestData = {
    botId: botId.value,
    question: content,
    input: null,
    modelConfig: JSON.stringify({
      modelId: form.modelId,
      modelName: form.modelName
    }),
    prePrompt: form.prePrompt,
    parameters: '{}',
    knowledgeIds: form.knowledges.map(k => k.id).join(','),
    toolMethodIds: form.tools.map(t => t.id).join(',')
  };

  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollToBottom(true);
    }
  });

  // 创建 AbortController 用于取消请求
  abortController.value = new AbortController();

  let botContent = "";

  // 调用调试接口（流式输出）
  debugAgent(
    requestData,
    // onmessage 回调
    (event) => {
      try {
        const data = JSON.parse(event.data);
        if (data.code === 200 && data.data) {
          // 提取机器人回复内容
          const messageContent = data.data.receive?.content || '';
          if (messageContent) {
            botContent += messageContent;
            // 更新机器人消息
            messageList.value[botMessageIndex].content = botContent;

            // 每次更新内容后滚动到底部
            nextTick(() => {
              if (messageListRef.value) {
                messageListRef.value.scrollToBottom(true);
              }
            });
          }
        }
      } catch (e) {
      }
    },
    // onerror 回调
    (error) => {
      messageList.value[botMessageIndex].content = `错误：${error.message || '请求失败'}`;
      loading.value = false;
      throw error; // 抛出错误以终止连接
    },
    // onclose 回调
    () => {
      loading.value = false;
      // 如果最终没有内容，显示默认消息
      if (!messageList.value[botMessageIndex].content) {
        messageList.value[botMessageIndex].content = '暂无回复';
      }
    }
  ).catch((error) => {
    loading.value = false;
    if (!messageList.value[botMessageIndex].content.includes('错误')) {
      messageList.value[botMessageIndex].content = `错误：${error.message || '请求失败'}`;
    }
  });
}

// 清空表单
function handleReset() {
  form.modelId = '';
  form.modelName = '';
  form.prePrompt = '';
  form.variables = [{ id: 1, name: 'input', description: '用户输入' }];
  form.knowledges = [];
  form.tools = [];
  proxy.$modal.msgSuccess('已清空表单');
}

// 新增变量
function addItem() {
  if (!form.variables) {
    form.variables = [];
  }
  const newId = form.variables.length > 0 ? Math.max(...form.variables.map(v => v.id)) + 1 : 1;
  form.variables.push({ id: newId, name: '', description: '' });
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
      proxy.$modal.msgError('请填写必填项');
      return;
    }

    // 构建提交数据，匹配后端 KbAgentConfigSaveReqVO
    const submitData = {
      id: form.id, // 配置记录的 ID（修改时需要）
      botId: botId.value,
      // 将模型配置转为 JSON 格式
      modelConfig: JSON.stringify({
        modelId: form.modelId,
        modelName: form.modelName
      }),
      prePrompt: form.prePrompt,
      // 参数配置（暂时传空字符串或根据实际需求填写）
      parameters: '{}',
      // 知识库 IDs（从对象数组提取 ID 数组）
      knowledgeIds: form.knowledges.map(k => k.id).join(','),
      // 工具方法 IDs（从对象数组提取 ID 数组）
      toolMethodIds: form.tools.map(t => t.id).join(','),
      remark: ''
    };

    proxy.$modal.loading('正在保存...');

    // 判断是新增还是修改
    const apiCall = form.id ? updateConfig(submitData) : addConfig(submitData);

    // 调用保存接口
    apiCall.then(() => {
      proxy.$modal.msgSuccess(form.id ? '更新成功' : '保存成功');
      proxy.$modal.closeLoading();
    }).catch(() => {
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
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 0;

  .el-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .el-main {
      flex: 1;
      overflow-y: auto;
      padding: 0;
      display: flex;
      flex-direction: column;
    }
  }
}

.svg-icon {
  font-size: 12px;
  margin-right: 3px;
  vertical-align: middle;
  margin-top: -3px;
}
</style>
