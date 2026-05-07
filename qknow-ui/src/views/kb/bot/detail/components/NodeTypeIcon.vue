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
  <span class="workflow-node-type-icon" :style="wrapperStyle">
    <LlmNodeIcon
      v-if="normalizedType === 'llm'"
      class="workflow-node-type-icon__svg workflow-node-type-icon__svg--llm"
    />
    <svg
      v-else-if="normalizedType === 'reply'"
      viewBox="0 0 24 24"
      fill="currentColor"
      class="workflow-node-type-icon__svg"
    >
      <path
        d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"
      />
    </svg>
    <svg
      v-else-if="normalizedType === 'condition'"
      viewBox="0 0 24 24"
      fill="currentColor"
      class="workflow-node-type-icon__svg"
    >
      <path
        d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 9h-2V5h2v7zM7 9h2v7H7V9zm10 7h-2V9h2v7z"
      />
    </svg>
    <span
      v-else-if="normalizedType === 'loop'"
      class="workflow-node-type-icon__glyph"
      >&#8734;</span
    >
    <span
      v-else-if="normalizedType === 'tool' && normalizedFallback"
      class="workflow-node-type-icon__fallback"
    >
      {{ normalizedFallback }}
    </span>
    <svg
      v-else-if="normalizedType === 'tool'"
      viewBox="0 0 24 24"
      fill="currentColor"
      class="workflow-node-type-icon__svg"
    >
      <path
        d="M20.7 7.3a5.99 5.99 0 0 1-7.95 7.95l-5.02 5.02a1.5 1.5 0 1 1-2.12-2.12l5.02-5.02A6 6 0 0 1 16.7 3.3l-3.06 3.06 1 3 3 1 3.06-3.06z"
      />
    </svg>
    <span v-else class="workflow-node-type-icon__fallback">
      {{ fallback }}
    </span>
  </span>
</template>

<script setup>
import { computed } from "vue";
import LlmNodeIcon from "./LlmNodeIcon.vue";

const props = defineProps({
  type: {
    type: String,
    default: "",
  },
  fallback: {
    type: [String, Number],
    default: "",
  },
});

const normalizedType = computed(() => `${props.type || ""}`.trim());
const normalizedFallback = computed(() => `${props.fallback || ""}`.trim());

const wrapperStyle = computed(() => {
  if (normalizedType.value === "llm") {
    return {
      backgroundColor: "#dbeafe",
    };
  }

  if (normalizedType.value === "reply") {
    return {
      backgroundColor: "#ffedd5",
      color: "#f97316",
    };
  }

  if (normalizedType.value === "condition") {
    return {
      backgroundColor: "#fef3c7",
      color: "#f59e0b",
    };
  }

  if (normalizedType.value === "loop") {
    return {
      background: "linear-gradient(180deg, #14b8a6 0%, #06b6d4 100%)",
      color: "#fff",
      boxShadow: "0 6px 14px rgba(6, 182, 212, 0.18)",
    };
  }

  if (normalizedType.value === "tool") {
    return {
      backgroundColor: "#ede9fe",
      color: "#7c3aed",
    };
  }

  return {
    backgroundColor: "#f9fafb",
    color: "#111827",
  };
});
</script>

<style scoped>
.workflow-node-type-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.workflow-node-type-icon__svg {
  width: 18px;
  height: 18px;
}

.workflow-node-type-icon__svg--llm {
  width: 20px;
  height: 20px;
}

.workflow-node-type-icon__glyph {
  font-size: 18px;
  font-weight: 700;
  line-height: 1;
}

.workflow-node-type-icon__fallback {
  font-size: 20px;
  line-height: 1;
}
</style>
