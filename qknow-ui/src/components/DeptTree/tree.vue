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
    <el-aside :style="{ width: `${leftWidth}px`, marginLeft: leftWidth == 0 ? '-15px' : '0px', }" class="left-pane">
        <div class="left-tree">
            <div class="head-container">
                <el-tree class="dept-tree" :data="deptOptions" :props="{ label: 'name', children: 'children' }"
                    :filter-node-method="filterNode" ref="deptTreeRef" node-key="id" highlight-current
                    :default-expanded-keys="expandedKeys" @node-click="handleNodeClick"
                    :default-expand-all="defaultExpand">
                    <template #default="{ node, data }">
                        <span class="custom-tree-node">
                            <img class="node-icon" src="@/assets/da/asset/folder.svg" alt="" v-if="node.childNodes.length" />
                            <!-- 子级 -->
                            <img class="child-icon" src="@/assets/da/asset/file.svg" alt="" v-show="!node.isCurrent && node.childNodes.length == 0" />
                            <img class="child-icon" src="@/assets/da/asset/file.svg" alt="" v-show="node.isCurrent && node.childNodes.length == 0" />
                            <span class="treelable" @click="getNode(node)">
                                {{ node.label }}
                            </span>
                        </span>
                    </template>
                </el-tree>
            </div>
        </div>
    </el-aside>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, getCurrentInstance } from "vue";

const { proxy } = getCurrentInstance();

const props = defineProps({
    deptOptions: Array,
    leftWidth: {
        type: Number,
        default: 300,
    },
    placeholder: {
        type: String,
        default: "请输入部门名称",
    },
    defaultExpand: {
        type: Boolean,
        default: false,
    },
});

const emit = defineEmits(["node-click", "update:deptName", "update:leftWidth"]);

const deptName = ref("");
const deptTreeRef = ref(null);
const leftWidth = ref(props.leftWidth);
const expandedKeys = ref([]);

// 等 deptOptions 加载后设置一级节点展开
watch(
    () => props.deptOptions,
    (val) => {
        if (Array.isArray(val) && val.length > 0) {
            expandedKeys.value = val.map((item) => item.id); // 展开第一层
        }
    },
    { immediate: true }
);

// 过滤节点
const filterNode = (value, data) => {
    if (!value) return true;
    return data.name.indexOf(value) !== -1;
};

watch(deptName, (val) => {
    if (deptTreeRef.value) {
        deptTreeRef.value.filter(val);
    }
});

watch(
    () => props.leftWidth,
    (val) => {
        leftWidth.value = val;
    }
);

// 拖拽逻辑
const isResizing = ref(false);
let startX = 0;
const startResize = (event) => {
    isResizing.value = true;
    startX = event.clientX;
    document.addEventListener("mousemove", updateResize);
    document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
    isResizing.value = false;
    document.removeEventListener("mousemove", updateResize);
    document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
    if (isResizing.value) {
        const delta = event.clientX - startX;
        leftWidth.value += delta;
        startX = event.clientX;
        requestAnimationFrame(() => { });
    }
};

// 折叠展开
const toggleCollapse = () => {
    if (leftWidth.value === 0) {
        leftWidth.value = 300;
    } else {
        leftWidth.value = 0;
    }
    emit("update:leftWidth", leftWidth.value);
};

function handleNodeClick(data) {
    emit("node-click", data);
}

const getNode = (node) => {
    console.log(node);
};

const resetTree = () => {
    if (deptTreeRef.value) {
        proxy.$refs.deptTreeRef.setCurrentKey(null);
    }
};

defineExpose({ resetTree });
</script>

<style scoped lang="scss">
.left-pane {
    background-color: #ffffff;
    overflow: hidden;
    height: 660px;
    display: flex;
    flex-direction: column;
}

.left-tree {
    padding: 15px;
    flex: 1;
    overflow-y: auto;
    /* 超出显示滚动条 */
    scrollbar-width: thin;
}

.left-tree::-webkit-scrollbar {
    width: 6px;
}

.left-tree::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 3px;
}

.left-tree::-webkit-scrollbar-track {
    background: transparent;
}

.custom-tree-node {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 36px 0 12px;

    .node-icon {
        width: 18px;
        height: 18px;
    }
    .child-icon {
        width: 16px;
        height: 16px;
    }

    .treelable {
        margin-left: 10px;
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-family: PingFang SC;
        font-weight: 400;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.85);
    }
}

.zjiconimg {
    font-size: 12px;
}

.colorxz {
    color: #358cf3;
}

.colorwxz {
    color: var(--el-color-primary);
}

.resize-bar {
    height: 660px;
    cursor: ew-resize;
    display: flex;
    align-items: center;
    justify-content: center;
}

.resize-handle-sx {
    width: 15px;
    text-align: center;
    position: relative;
}

.zjsx {
    display: none;
    width: 5px;
    height: 50px;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
}

.collapse-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 28px;
    color: #aaa;
    cursor: pointer;
    z-index: 10;
    padding: 5px;
}

:deep(.dept-tree) {
    &.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
        background: rgba(51, 103, 252, 0.06) !important;
        border: none;

        .custom-tree-node .treelable {
            color: var(--el-color-primary);
        }
    }

    .el-tree-node__content {
        position: relative;

        .el-tree-node__expand-icon {
            position: absolute;
            right: 10px;
            color: transparent;

            &>svg {
                background: url("@/assets/da/asset/arrow.svg") no-repeat;
                background-size: 100% 100%;
            }
        }
    }
}
</style>
