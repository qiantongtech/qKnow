<template>
  <span
    class="tag"
    :class="[
      styleType,
      type,
      size,
      { 'tag-pill': isPillStyle, 'tag-rect': isRectStyle },
    ]"
    :style="{
      backgroundColor: computedBackgroundColor,
      color: computedColor,
      borderColor: computedBorderColor,
    }"
  >
    {{ name }}
  </span>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  // 标签类型
  type: {
    type: String,
    default: "primary",
    validator: (value) =>
      ["default", "primary", "success", "warning", "danger", "info"].includes(
        value
      ),
  },
  // 标签名称
  name: {
    type: String,
    default: "标签",
  },
  // 标签大小
  size: {
    type: String,
    default: "",
    validator: (value) => ["large", "small"].includes(value),
  },
  // 标签样式类型：pill-圆角标签，rect-直角标签
  styleType: {
    type: String,
    default: "rect",
    validator: (value) => ["pill", "rect"].includes(value),
  },
});

// 判断是否为圆角样式
const isPillStyle = computed(() => props.styleType === "pill");

// 判断是否为直角样式
const isRectStyle = computed(() => props.styleType === "rect");

// 根据样式类型计算字体颜色
const computedColor = computed(() => {
  if (isRectStyle.value) {
    // 直角标签使用深色字体
    const colorMap = {
      default: "#909399",
      primary: "#409eff",
      success: "#67c23a",
      warning: "#e6a23c",
      danger: "#EC544D",
      info: "#8737A3",
    };
    return colorMap[props.type] || "#909399";
  } else {
    // 圆角标签使用白色字体
    return "#ffffff";
  }
});

// 根据样式类型计算背景颜色
const computedBackgroundColor = computed(() => {
  if (isRectStyle.value) {
    // 直角标签使用浅色背景
    const bgColorMap = {
      default: "#f4f4f5",
      primary: "#ecf5ff",
      success: "#f0f9e8",
      warning: "#fdf6ec",
      danger: "#fef0f0",
      info: "#f3ebf6",
    };
    return bgColorMap[props.type] || "#f4f4f5";
  } else {
    // 圆角标签使用深色背景
    const bgColorMap = {
      default: "#909399",
      primary: "#2666FB",
      success: "#009E21",
      warning: "#FF8C00",
      danger: "#EC544D",
      info: "#8737A3",
    };
    return bgColorMap[props.type] || "#909399";
  }
});

// 直角标签的边框颜色
const computedBorderColor = computed(() => {
  if (isRectStyle.value) {
    const borderColorMap = {
      default: "#d3d4d6",
      primary: "#b3d8ff",
      success: "#c2e7b0",
      warning: "#f5dab1",
      danger: "#fbc4c4",
      info: "#d4b8de",
    };
    return borderColorMap[props.type] || "#d3d4d6";
  }
  return "transparent";
});
</script>

<style scoped>
.tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0px 9px;
  height: 24px;
  font-size: 14px;
  line-height: 1;
  box-sizing: border-box;
  white-space: nowrap;
  font-weight: 500;
  cursor: default;
  user-select: none;
  transition: all 0.2s ease;
}

/* 圆角标签样式 */
.tag-pill {
  border-radius: 8px 8px 8px 0px;
  border: none;
}

/* 直角标签样式 */
.tag-rect {
  border-radius: 4px;
  border: 1px solid;
}

/* 大小样式 */
.tag.small {
  padding: 0px 9px;
  height: 20px;
  font-size: 14px;
}

.tag.small.tag-pill {
  border-radius: 6px 6px 6px 0px;
}

.tag.small.tag-rect {
  border-radius: 3px;
}

.tag.large {
  padding: 0px 9px;
  height: 32px;
  font-size: 14px;
}

.tag.large.tag-pill {
  border-radius: 10px 10px 10px 0px;
}

.tag.large.tag-rect {
  border-radius: 5px;
}

/* 类型样式 - 圆角标签 */
/* .tag-pill.default {
  background-color: #909399;
  color: #ffffff;
}

.tag-pill.primary {
  background-color: #409eff;
  color: #ffffff;
}

.tag-pill.success {
  background-color: #67c23a;
  color: #ffffff;
}

.tag-pill.warning {
  background-color: #e6a23c;
  color: #ffffff;
}

.tag-pill.danger {
  background-color: #f56c6c;
  color: #ffffff;
}

.tag-pill.info {
  background-color: #909399;
  color: #ffffff;
} */

/* 类型样式 - 直角标签 */
/* .tag-rect.default {
  background-color: #f4f4f5;
  color: #909399;
  border-color: #d3d4d6;
}

.tag-rect.primary {
  background-color: #ecf5ff;
  color: #409eff;
  border-color: #b3d8ff;
}

.tag-rect.success {
  background-color: #f0f9e8;
  color: #67c23a;
  border-color: #c2e7b0;
}

.tag-rect.warning {
  background-color: #fdf6ec;
  color: #e6a23c;
  border-color: #f5dab1;
}

.tag-rect.danger {
  background-color: #fef0f0;
  color: #f56c6c;
  border-color: #fbc4c4;
}

.tag-rect.info {
  background-color: #f4f4f5;
  color: #909399;
  border-color: #d3d4d6;
} */

/* 悬停效果 */
.tag:hover {
  opacity: 0.9;
}

/* 禁用状态 */
.tag:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
