<template>
  <el-footer class="chat-input-footer">
    <form class="prompt-form" @submit.prevent>
      <el-input
        type="textarea"
        class="prompt-input"
        :autosize="{ minRows: 2, maxRows: 2 }"
        v-model="inputValue"
        :placeholder="placeholder"
        @keydown.enter.prevent="handleEnter"
        @keydown.shift.enter="handleShiftEnter"
        resize="none"
      />
      <div class="prompt-btns">
        <el-button
          type="primary"
          size="default"
          class="send-btn"
          @click="handleSend"
          :loading="loading"
        >
          <el-icon><Promotion /></el-icon>
          {{ sendButtonText }}
        </el-button>
      </div>
    </form>
  </el-footer>
</template>

<script setup name="AgentChatInput">
import { Promotion } from '@element-plus/icons-vue';

const props = defineProps({
  // 输入框的值
  modelValue: {
    type: String,
    default: ''
  },
  // 占位符
  placeholder: {
    type: String,
    default: '和 Bot 聊天...（Enter 发送）'
  },
  // 是否加载中
  loading: {
    type: Boolean,
    default: false
  },
  // 发送按钮文字
  sendButtonText: {
    type: String,
    default: '发送'
  }
});

const emit = defineEmits(['update:modelValue', 'send']);

const inputValue = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
});

// 处理发送
function handleSend() {
  if (!inputValue.value.trim()) {
    return;
  }
  emit('send', inputValue.value);
}

// 处理回车发送
function handleEnter() {
  handleSend();
}

// 处理 Shift+Enter 换行（预留）
function handleShiftEnter(event) {
  // 允许默认行为（换行）
}
</script>

<style scoped lang="scss">
.chat-input-footer {
  flex-shrink: 0;
  //padding: 16px 20px;
  background: #fff;
  border-top: 1px solid #e4e7ed;
  padding: 0;
  height: 67px;

  .prompt-form {
    display: flex;
    flex-direction: column;

    .prompt-input {
      margin-bottom: 12px;

      :deep(.el-textarea__inner) {
        height: 60px!important;
        padding: 12px 16px;
        font-size: 14px;
        box-shadow: none;
      }
    }

    .prompt-btns {
      display: flex;
      justify-content: flex-end;
      position: absolute;
      right: 30px;
      bottom: 30px;

      .send-btn {
        min-width: 100px;

        .el-icon {
          margin-right: 4px;
        }
      }
    }
  }
}
</style>
