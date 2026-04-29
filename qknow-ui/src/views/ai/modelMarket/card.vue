<template>
  <div class="card-container">
    <div v-for="(item, index) in data" :key="index" class="card">
      <!-- 头部 -->
      <div class="card-top">
        <div class="card-top-left">
          <div class="card-img">
            <img :src="getImage(item)" />
          </div>

          <div class="card-title-tag">
            <div class="card-title">
              {{ item.name }}
            </div>

            <div class="card-title-tags">
              <el-tooltip
                effect="light"
                placement="top"
                :content="
                  getTags(item)
                    .map((tag) => tag)
                    .join(' ｜ ')
                "
                popper-class="no-border-tooltip"
              >
                <div class="card-title-tags">
                  <el-tag
                    v-for="tag in getTags(item)"
                    size="medium"
                    class="card-tag"
                    :key="tag"
                  >
                    {{ tag }}
                  </el-tag>
                </div>
              </el-tooltip>
            </div>
          </div>
        </div>

        <div class="card-status">
          <dict-tag :options="ai_apikey_status" :value="item.status" />
        </div>
      </div>
      <el-divider class="card-divider"></el-divider>
      <!--      底部-->
      <div class="card-bottom">
        <el-tooltip
          class="desc-tooltip"
          effect="light"
          placement="top"
          :disabled="!isTextOverflow(index)"
          popper-class="no-border-tooltip"
        >
          <!-- 具名插槽 #content 替代 content 属性，这里写tooltip的弹窗内容 -->
          <template #content>
            <div
              style="width: 450px; font-size: 14px; color: #333; padding: 7px"
            >
              {{ item.description }}
            </div>
          </template>

          <!-- 触发tooltip的原内容不变 -->
          <div class="card-bottom-desc" :ref="(el) => (descRefs[index] = el)">
            {{ item.description }}
          </div>
        </el-tooltip>
        <div class="card-bottom-button">
          <el-button
            class="card-bottom-operation"
            type="primary"
            @click="handleUpdate(item)"
            v-hasPermi="['ai:modelMarket:key:edit']"
          >
            <div class="card-bottom-icon">
              <img src="@/assets/ai/set.svg" alt="图标" />
            </div>
            <div class="card-bottom-name" >
              密钥设置
            </div>
          </el-button>

          <el-button
            class="card-bottom-operation"
            type="primary"
            @click="routeTo('', item)"
            :disabled="item.status === 0"
            v-hasPermi="['ai:modelMarket:key:query']"
          >
            <div class="card-bottom-icon">
              <img src="@/assets/ai/info.svg" alt="图标" />
            </div>
            <div class="card-bottom-name">模型详情</div>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import ollama from "@/assets/ai/ollama.svg";
import tongyi from "@/assets/ai/tongyi.svg";
import deepseek from "@/assets/ai/deepseek.svg";
import openai from "@/assets/ai/chatgpt.svg";

const { proxy } = getCurrentInstance();

const emit = defineEmits(["handleUpdate", "routeTo"]);
const { ai_apikey_status, ai_model_tag } = proxy.useDict(
  "ai_apikey_status",
  "ai_model_tag"
);

const props = defineProps({
  data: {
    type: Array,
    required: true,
  },
});

// 存储每个描述 DOM 的引用
const descRefs = ref([]);
// 缓存每个 index 是否溢出
const overflowStates = ref({});

// 判断是否溢出（基于缓存）
const isTextOverflow = (index) => {
  return !!overflowStates.value[index];
};

// 计算所有描述区域是否溢出
const calculateOverflow = () => {
  // 等待 DOM 更新完成
  nextTick(() => {
    const states = {};
    descRefs.value.forEach((el, index) => {
      if (el) {
        states[index] = el.scrollHeight > el.clientHeight;
      }
    });
    overflowStates.value = states;
  });
};

// 监听 data 变化
watch(
  () => props.data,
  () => {
    setTimeout(calculateOverflow, 50);
  },
  { immediate: true, deep: true }
);

// 平台图标映射
const platformImage = {
  Ollama: ollama,
  TongYi: tongyi,
  DeepSeek: deepseek,
  OpenAI: openai,
};

// 获取平台图标
function getImage(row) {
  return platformImage[row.platform];
}

function getTags(row) {
  // 兼容：字段为空/undefined/null的情况，返回空数组，防止报错
  if (!row?.platformTag) {
    return [];
  }
  // 逗号拆分字符串 → 去空值 → 去重，返回纯字符串数组
  const keyArray = row.platformTag
    .split(",")
    .map((tag) => tag.trim())
    .filter((tag) => tag)
    .filter((item, idx, arr) => arr.indexOf(item) === idx); // 去重

  return keyArray.map((key) => {
    const dictItem = ai_model_tag.value.find((t) => t.value === key);
    return dictItem ? dictItem.label : key;
  });
}

function handleUpdate(row) {
  emit("handleUpdate", row);
}

function routeTo(link, row) {
  emit("routeTo", link, row);
}
</script>

<style scoped lang="scss">
.card-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(448px, 1fr));
  gap: 14px;
  padding: 16px 0px;
  box-sizing: border-box;
  width: 100%;
  transition: all 0.3s ease;
  @media (min-width: 448px) {
    grid-template-columns: repeat(1, minmax(448px, 1fr));
  }
  @media (min-width: 1140px) {
    grid-template-columns: repeat(2, minmax(448px, 1fr));
  }
  @media (min-width: 1712px) {
    grid-template-columns: repeat(3, minmax(448px, 1fr));
  }

  .card {
    position: relative;
    display: flex;
    flex-direction: column;
    min-width: 448px;
    min-height: 200px;
    padding: 18px 28px 21px 28px;
    background-color: #ffffff;
    border-radius: 2px;
    cursor: pointer;
    transition: transform 0.2s ease, box-shadow 0.2s ease;

    .card-divider {
      margin: 0px 0px 12px 0px;
      border-color: #eeeeee;
    }

    .card-top {
      display: flex;
      justify-content: space-between;
      line-height: 10px;
      flex: 1;

      .card-top-left {
        display: flex;
        min-width: 0; // 允许收缩
      }

      .card-img {
        margin: 0px 14px 8px 0px;
        object-fit: cover;
        flex-shrink: 0;

        @media (max-width: 767px) {
          margin: 0px 10px 8px 0px;
        }
      }

      .card-title-tag {
        display: flex;
        flex-direction: column;
        margin-top: 3px;
        flex: 1;
        min-width: 0; // 允许收缩

        .card-title {
          margin-bottom: 5px;
          font-family: PingFang SC-Heavy;
          font-size: 20px;
          color: #333333;
          line-height: 28px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;

          @media (max-width: 767px) {
            font-size: 18px;
          }
        }

        .card-title-tags {
          width: 100%;
          min-width: 0;
          white-space: nowrap;
          overflow: hidden; /* 超出部分隐藏 */
          text-overflow: ellipsis; /* 文本溢出时显示省略号 */
          word-break: break-all;

          .card-tag {
            margin-right: 6px;

            @media (max-width: 767px) {
              font-size: 11px;
              max-width: 80px;
            }
          }
        }
      }
    }

    .card-status {
      position: absolute;
      top: 24px;
      right: 30px;

      @media (max-width: 767px) {
        top: 18px;
        right: 20px;
      }
    }
  }

  .card-bottom {
    display: flex;
    flex-direction: column;
    flex: 1;

    .desc-tooltip {
    }

    .card-bottom-desc {
      width: 100%;
      flex: 1;
      min-height: 38px;
      font-family: PingFang SC, PingFang SC;
      font-size: 14px;
      color: #333333;
      line-height: 18px;
      text-align: left;
      font-style: normal;
      text-transform: none;
      white-space: normal;
      word-break: break-all;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;

      @media (min-width: 768px) {
        min-height: 57px; /* 桌面端3行高度 */
        -webkit-line-clamp: 3; /* 桌面端显示3行 */
      }
    }

    .card-bottom-button {
      display: flex;
      flex-direction: row;
      gap: 12px;
      margin-top: 8px;

      .card-bottom-operation {
        flex: 1;
        min-width: 0;
        height: 28px;
        padding: 0 12px;

        .card-bottom-icon {
          margin-top: 2px;
        }

        .card-bottom-name {
          margin-left: 8px;
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 12px;
          color: #ffffff;
          line-height: 18px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
}

:deep(.el-tag.el-tag--primary.diy) {
  background: #bbbbbb !important;
  color: #ffffff !important;
  font-family: PingFang SC-Regular;
  font-size: 14px;
  border-radius: 4px;
}

:deep(.el-tag.el-tag--success) {
  background: #0baa84 !important;
  color: #ffffff !important;
  font-family: PingFang SC-Regular;
  font-size: 14px;
  border-radius: 4px;
}
</style>

<style>
/* 这是tooltip的根容器，箭头就在这个容器上，所有外层样式写这里！ */
.no-border-tooltip {
  background: #ffffff !important;
  border-radius: 4px !important; /* 和系统默认的圆角一致 */
  box-shadow: 0 0px 8px rgba(0, 0, 0, 0.2) !important; /* 阴影效果*/
  /* border: none !important; 去掉原生的边框 */
}
</style>
