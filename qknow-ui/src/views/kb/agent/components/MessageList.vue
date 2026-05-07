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
  <div ref="messageContainer" class="h-100% overflow-y-auto relative">
    <el-empty description="在下方输入问题，看看 Bot 会如何回答" style="height: 100%;" v-if="list.length <= 0" />
    <div class="chat-list" v-for="(item, index) in list" :key="index">
      <!-- 靠左 message：system、assistant 类型 -->
      <div class="left-message message-item" v-if="item.type === 1">
        <div class="avatar">
          <el-avatar
              :src="roleAvatar"
              :size="46"
              style="background-color: transparent"
          />
        </div>
        <div class="message">
          <div>
            <el-text class="time">{{ parseTime(item.createTime) }}</el-text>
          </div>
          <div class="left-text-container">
            <MarkdownView
                class="left-text"
                ref="markdownViewRef"
                :messageId="item.id"
                :documentNameList="item.documentNameList"
                :content="item.content"
                :documentIdList="item.documentIdList"
            />
          </div>
<!--          <div class="left-btns">-->
<!--            <el-button class="btn-cus" link @click="copyContent(index)">-->
<!--              <img class="btn-image" src="@/assets/app/copy.png" />-->
<!--            </el-button>-->
<!--            <el-divider direction="vertical" class="btn-divider" />-->
<!--            <el-button-->
<!--                v-if="item.id > 0"-->
<!--                class="btn-cus"-->
<!--                link-->
<!--                @click="onDelete(item.id)"-->
<!--            >-->
<!--              <img class="btn-image h-17px" src="@/assets/app/delete.png" />-->
<!--            </el-button>-->
<!--          </div>-->
        </div>
      </div>
      <!-- 靠右 message：user 类型 -->
      <div class="right-message message-item" v-if="item.type === 0">
        <div class="avatar">
          <el-avatar :src="userAvatar" :size="46" />
        </div>
        <div class="message">
          <div>
            <el-text class="time">{{ parseTime(item.createTime) }}</el-text>
          </div>
          <div class="right-text-container">
            <div class="right-text">{{ item.content }}</div>
          </div>
<!--          <div class="right-btns">-->
<!--            <el-button-->
<!--                style="margin-left: 12px"-->
<!--                class="btn-cus"-->
<!--                link-->
<!--                @click="copyContent(item.content)"-->
<!--            >-->
<!--              <img class="btn-image" src="@/assets/app/copy.png" />-->
<!--            </el-button>-->
<!--          </div>-->
        </div>
      </div>
    </div>
  </div>
  <!-- 回到底部 -->
<!--  <div v-if="isScrolling" class="to-bottom" @click="handleGoBottom">-->
<!--    <el-button icon="ArrowDownBold" circle />-->
<!--  </div>-->
</template>
<script setup>
import MarkdownView from "@/components/MarkdownView/index.vue";
import { ChatMessageApi } from "@/api/app/chat/message";
import useUserStore from "@/store/system/user";
import userAvatarDefaultImg from "@/assets/system/images/index/icon (1).png";
import roleAvatarDefaultImg from "@/assets/app/gpt-new.svg";
import { useClipboard } from "@vueuse/core";

const { proxy } = getCurrentInstance();
const message = proxy.$modal; // 消息弹窗
const userStore = useUserStore();
const { copy } = useClipboard(); // 初始化 copy 到粘贴板

// 判断“消息列表”滚动的位置(用于判断是否需要滚动到消息最下方)
const messageContainer = ref(null);
const isScrolling = ref(false); //用于判断用户是否在滚动
const markdownViewRef = ref(null);

const userAvatar = computed(() => userStore.avatar || userAvatarDefaultImg);
const roleAvatar = computed(
  () => roleAvatarDefaultImg
);

// 定义 props
const props = defineProps({
  list: {
    type: Array,
    required: true,
  },
  suggestedList: {
    type: Array,
    default: () => [],
  },
});

const { list } = toRefs(props); // 消息列表

const emits = defineEmits([
  "onDeleteSuccess",
  "onRefresh",
  "onEdit",
  "onPrompt",
]); // 定义 emits

// ============ 处理对话滚动 ==============

/** 滚动到底部 */
const scrollToBottom = async (isIgnore) => {
  // 注意要使用 nextTick 以免获取不到 dom
  await nextTick();
  if (isIgnore || !isScrolling.value) {
    messageContainer.value.scrollTop =
        messageContainer.value.scrollHeight - messageContainer.value.offsetHeight;
  }
};

function handleScroll() {
  const scrollContainer = messageContainer.value;
  const scrollTop = scrollContainer.scrollTop;
  const scrollHeight = scrollContainer.scrollHeight;
  const offsetHeight = scrollContainer.offsetHeight;
  if (scrollTop + offsetHeight < scrollHeight - 100) {
    // 用户开始滚动并在最底部之上，取消保持在最底部的效果
    isScrolling.value = true;
  } else {
    // 用户停止滚动并滚动到最底部，开启保持到最底部的效果
    isScrolling.value = false;
  }
}

/** 回到底部 */
const handleGoBottom = () => {
  const scrollContainer = messageContainer.value;
  scrollContainer.scrollTop = scrollContainer.scrollHeight;
};

/** 回到顶部 */
const handlerGoTop = () => {
  const scrollContainer = messageContainer.value;
  scrollContainer.scrollTop = 0;
};

defineExpose({ scrollToBottom, handlerGoTop, handleGoBottom }); // 提供方法给 parent 调用

// ============ 处理消息操作 ==============

function isStringRobust(value) {
  if (value == null) return false;
  return typeof value === "string" || value instanceof String;
}

/** 复制 */
const copyContent = (index) => {
  if (isStringRobust(index)) {
    copy(index).then(() => {
      message.msgSuccess("复制成功！");
    });
    return;
  }
  let count = -1;
  for (let i = 0; i <= index; i++) {
    if (list.value[i].type === 1) {
      count++;
    }
  }
  markdownViewRef.value[count].copyContent();
};

/** 删除 */
const onDelete = async (id) => {
  // 删除 message
  await ChatMessageApi.deleteChatMessage(id);
  message.msgSuccess("删除成功！");
  // 回调
  emits("onDeleteSuccess");
};

/** 刷新 */
const onRefresh = (message) => {
  emits("onRefresh", message);
};

/** 编辑 */
const onEdit = (message) => {
  emits("onEdit", message);
};

/** 尝试问问 */
const handlerSuggested = (item) => {
  emits("onPrompt", item);
};

/** 初始化 */
onMounted(() => {
  messageContainer.value.addEventListener("scroll", handleScroll);
});
</script>

<style scoped lang="scss">
.message-container {
  position: relative;
  overflow-y: scroll;
}
.h-100\% {
  height: 100%;
}

.overflow-y-auto {
  overflow-y: auto;
}
.relative {
  position: relative;
  padding-bottom: 20px;
  scrollbar-width: none;
}

// 中间
.chat-list {
  display: flex;
  flex-direction: column;
  overflow-y: hidden;
  padding: 0 20px;
  .message-item {
    margin-top: 50px;
  }

  .left-message {
    display: flex;
    flex-direction: row;
  }

  .right-message {
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-start;
  }

  .message {
    display: flex;
    flex-direction: column;
    text-align: right;
    margin: 0 15px;

    .time {
      text-align: left;
      line-height: 22px;
      font-weight: normal;
      font-size: 14px;
      color: rgba(0, 0, 0, 0.65);
      font-style: normal;
      text-transform: none;
    }

    .left-text-container {
      position: relative;
      display: flex;
      flex-direction: column;
      overflow-wrap: break-word;
      background-color: #f0f0f6;
      padding: 10px 10px 5px 10px;
      border-radius: 4px 4px 4px 4px;
      margin-top: 8px;
      .left-text {
        color: #707070;
        font-size: 14px;
      }
    }

    .right-text-container {
      display: flex;
      flex-direction: row-reverse;
      margin-top: 8px;

      .right-text {
        display: inline;
        background: #257fff;
        border-radius: 4px 4px 4px 4px;
        padding: 10px;
        width: auto;
        overflow-wrap: break-word;
        white-space: pre-wrap;
        font-weight: 400;
        font-size: 14px;
        color: #ffffff;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }
    }

    .left-btns {
      display: flex;
      flex-direction: row;
      margin-top: 8px;
      align-items: center;
    }

    .right-btns {
      display: flex;
      flex-direction: row-reverse;
      margin-top: 8px;
      align-items: center;
    }
  }

  .btn-divider {
    --el-border-color: #d9d8e8;
    margin: 11px;
  }

  // 复制、删除按钮
  .btn-cus {
    display: flex;
    background-color: transparent;
    align-items: center;

    .btn-image {
      height: 14px;
    }
  }

  .btn-cus:hover {
    cursor: pointer;
    background-color: #f6f6f6;
  }
}

.suggested-list {
  display: flex;
  justify-content: center;
  gap: 20px;
}

// 回到底部
.to-bottom {
  position: absolute;
  z-index: 1000;
  bottom: 0;
  right: 50%;
}
</style>
