<template>
  <!-- 你的页面根容器 -->
  <div class="monaco-ide" ref="monacoIdeRef">
    <div class="toolbar">
      <div class="toolbar-left">
        <span class="toolbar-title">{{ botName }}</span>
      </div>
      <div class="toolbar-actions">
        <el-button type="primary" class="fhbtn" plain @click="routerView">
          <svg-icon :iconClass="'fhs'"/>
          返回
        </el-button>
        <el-button type="primary" @click="openDebugRunPanel">
          <VideoPlay class="ds"/>调试
        </el-button>
        <el-button type="primary" @click="saveCodeNative">
          <img src="@/assets/icons/svg/bc.svg" alt="" class="bcimg"/>保存
        </el-button>
      </div>
    </div>

    <!-- 编辑器 + 抽屉容器 -->
    <div class="ide-wrapper" style="height: 100%">
      <!-- 编辑器 -->
      <div class="editor-body" style="height: 100%">
        <JavaMonacoEditor v-if="code" :code="code" ref="editor"/>
        <div v-else class="loading">加载代码中...</div>

      </div>
    </div>
  </div>

  <!-- ================== 内部抽屉（Element 2.7.6 专用） ================== -->
<!--  <div class="side-drawer" :class="{ open: drawerVisible }">-->
<!--    <div class="drawer-mask" @click="drawerVisible = false"></div>-->
<!--    <div class="drawer-body">-->
<!--      <div class="drawer-header">-->
<!--        <span>执行参数</span>-->
<!--        <i class="el-icon-close" @click="drawerVisible = false"></i>-->
<!--      </div>-->

<!--      <div class="drawer-content">-->
<!--        <el-form label-width="70px">-->
<!--          <el-form-item label="运行参数">-->
<!--            <el-input-->
<!--                v-model="params"-->
<!--                type="textarea"-->
<!--                :rows="3"-->
<!--                placeholder="请输入参数"-->
<!--            />-->
<!--          </el-form-item>-->
<!--        </el-form>-->

<!--        <div class="drawer-footer">-->
<!--          <el-button @click="drawerVisible = false">取消</el-button>-->
<!--          <el-button type="primary" :loading="loading" @click="runCode">-->
<!--            确认执行-->
<!--          </el-button>-->
<!--        </div>-->

<!--        <el-divider>执行结果</el-divider>-->
<!--        <div class="result-box" :class="{ error: isError }">-->
<!--          {{ result }}-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->

  <div class="config-drawer" :class="{ open: drawerVisible }">
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
  </div>


</template>

<script setup>
import JavaMonacoEditor from './JavaMonacoEditor.vue'
import {onMounted, ref} from "vue";
import {useRouter, useRoute} from "vue-router";
import {getByBotId, submitCodeNative} from "@/api/kb/codeNative/codeNative.js";

const route = useRoute();
const router = useRouter();
const {proxy} = getCurrentInstance();

const loading = ref(false)
const isError = ref(false)
const editor = ref(null)
const result = ref(null)
const botId = ref(0)
const botName = ref(null)
const code = ref(null)
const className = ref(null)
const drawerVisible = ref(false);

onMounted(() => {
  botId.value = route.query.id;
  botName.value = route.query.name;
  getCodeNative();
});

/**
 * 获取代码
 */
function getCodeNative() {
  getByBotId(botId.value).then((res) => {
    const data = res.data;
    if (data.code) {
      code.value = data.code;
      className.value = data.className;
    } else {
      genDefaultCode();
    }
  })
}

/**
 * 返回 bot 列表
 */
function routerView() {
  router.push("/system/bot");
}

/**
 * 保存代码
 */
function saveCodeNative() {
  const currentCode = editor.value.getCurrentCode();
  const data = {
    "botId": botId.value,
    "code": currentCode,
    "className": className.value
  }
  submitCodeNative(data).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess("保存成功");
    } else {
      proxy.$modal.msgError(`保存异常!`);
    }
  });
}

function openDebugRunPanel() {
  drawerVisible.value = true;
}

/**
 * todo：生成默认代码
 */
function genDefaultCode() {
  const uuid = crypto.randomUUID().replace(/-/g, '');
  const name = "CN_"+uuid;

  code.value = `
  public class ${name} {

    /**
     * 执行方法
     * 可以使用java基础语法、jdbc、hutool、tech.qiantong.qdata.common.utils下的工具类进行数据操作
     * 注：请勿更改data的字段结构
     *
     * @param data 数据结构如下，[]下为示例数据
     *             {"data":[{"id":1,"name":"张三"},{"id":1,"name":"张三"}]}
     * @return 执行成功必须返回字符1, 返回0表示终止任务
     */
    public String execute(JSONObject data) throws Exception{
        return "1";
    }
}
  `
  className.value = name;
}
// todo：执行代码（对接后端）
const runCode = async () => {
  genDefaultCode();
  console.log(className.value)
  console.log(code.value)
  const currentCode = editor.value.getCurrentCode();
  if (!currentCode) {
    result.value = '请输入代码'
    isError.value = true
    return
  }

  // loading.value = true
  // result.value = '执行中...'
  // isError.value = false
  //
  // try {
  //   const {data} = await axios.post('http://localhost:8080/api/execute', {
  //     methodBody: currentCode
  //   })
  //
  //   if (data.success) {
  //     result.value = data.data
  //     isError.value = false
  //   } else {
  //     result.value = '错误：' + data.msg
  //     isError.value = true
  //   }
  // } catch (err) {
  //   result.value = '请求失败：' + err.message
  //   isError.value = true
  // } finally {
  //   loading.value = false
  // }
}
</script>

<style scoped lang="scss">

.monaco-ide {
  //margin: auto 10px;
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

  .toolbar-actions {
    display: flex;
    gap: 8px;

    .ds {
      width: 1em;
      height: 1em;
      margin-right: 3px;
    }

    .bcimg {
      width: 1em;
      height: 1em;
      color: #fff;
      margin-right: 3px;
    }
  }

}


/* ================== 抽屉样式 ================== */
.side-drawer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 10;
}

/* 遮罩 */
.drawer-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  opacity: 0;
  transition: opacity 0.3s;
}

/* 抽屉主体 */
.drawer-body {
  position: absolute;
  top: 0;
  right: 0;
  width: 520px;
  height: 100%;
  background: #fff;
  transform: translateX(100%);
  transition: transform 0.3s;
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.1);
}

/* 打开状态 */
.side-drawer.open {
  pointer-events: all;
}
.side-drawer.open .drawer-mask {
  opacity: 1;
}
.side-drawer.open .drawer-body {
  transform: translateX(0);
}

/* 抽屉头部 */
.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
  font-size: 16px;
}
.drawer-header i {
  cursor: pointer;
  font-size: 16px;
}

/* 抽屉内容 */
.drawer-content {
  padding: 16px;
  height: calc(100% - 65px);
  overflow-y: auto;
}

.drawer-footer {
  text-align: right;
  margin-bottom: 16px;
}

.result-box {
  padding: 14px;
  background: #f6f8fa;
  border-radius: 6px;
  min-height: 140px;
}
.result-box.error {
  background: #ffebee;
  color: #c62822;
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

.result {
  margin-top: 16px;
  padding: 14px;
  background: #f6f8fa;
  border-radius: 6px;
}

.result.error {
  background: #ffebee;
  color: #c62828;
}
</style>