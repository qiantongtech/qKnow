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
  <div v-if="!item.hidden">
    <template
      v-if="
        hasOneShowingChild(item.children, item) &&
        (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&
        !item.alwaysShow
      "
    >
      <app-link
        v-if="onlyOneChild.meta"
        :to="resolvePath(onlyOneChild.path, onlyOneChild.query)"
        @click="handleMenuClick(onlyOneChild)"
      >
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{ 'submenu-title-noDropdown': !isNest, 'nest-menu': props.isNest }"
        >
          <svg-icon
            :style="getTitleStyle(onlyOneChild.meta.title)"
            :icon-class="
              onlyOneChild.meta.icon || (item.meta && item.meta.icon)
            "
            v-if="!props.isNest"
          />
          <template #title
            ><span
              class="menu-title"
              :title="hasTitle(onlyOneChild.meta.title)"
              >{{ onlyOneChild.meta.title }}</span
            ></template
          >
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu
      v-else
      ref="subMenu"
      :index="resolvePath(item.path)"
      teleported
    >
      <template v-if="item.meta" #title>
        <svg-icon
          :style="getTitleStyle(item.meta.title)"
          :icon-class="item.meta && item.meta.icon"
        />
        <span class="menu-title" :title="hasTitle(item.meta.title)">{{
          item.meta.title
        }}</span>
      </template>

      <sidebar-item
        v-for="(child, index) in item.children"
        :key="child.path + index"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { isExternal } from "@/utils/validate";
import AppLink from "./Link";
import { getNormalPath } from "@/utils/anivia.js";
import useUserStore from "@/store/system/user.js";
const route = useRoute();
const router = useRouter();

const props = defineProps({
  // route object
  item: {
    type: Object,
    required: true,
  },
  isNest: {
    type: Boolean,
    default: false,
  },
  basePath: {
    type: String,
    default: "",
  },
});

const onlyOneChild = ref({});
const getTitleStyle = (title) => {
  console.log("🚀 ~ getTitleStyle ~ title:", title);
  // if (title == '首页' || title == '系统工具') {
  //   return {
  //     fontSize: '19px !important',
  //     width: '19px !important',
  //     height: '19px !important'
  //   };
  // } else if (title == '知识中心') {
  //   return {
  //     fontSize: '17px !important',
  //     width: '17px !important',
  //     height: '17px !important'
  //   };
  // }
  return {
    fontSize: "19px !important",
    width: "19px !important",
    height: "19px !important",
  };
};

const demo = (e)=>{
    console.log(e,props.isNest,111111);
    return props.isNest ? 1 :2
}

function hasOneShowingChild(children = [], parent) {
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter((item) => {
    if (item.hidden) {
      return false;
    } else {
      // Temp set(will be used if only has one showing child)
      onlyOneChild.value = item;
      return true;
    }
  });

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) {
    return true;
  }

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: "", noShowingChildren: true };
    return true;
  }

  return false;
}

function resolvePath(routePath, routeQuery) {
  if (isExternal(routePath)) {
    return routePath;
  }
  if (isExternal(props.basePath)) {
    return props.basePath;
  }
  if (routeQuery) {
    let query = JSON.parse(routeQuery);
    return {
      path: getNormalPath(props.basePath + "/" + routePath),
      query: query,
    };
  }
  return getNormalPath(props.basePath + "/" + routePath);
}

function hasTitle(title) {
  if (title.length > 5) {
    return title;
  } else {
    return "";
  }
}

function handleMenuClick(onlyOneChild) {
  const menuPath = resolvePath(onlyOneChild.path, onlyOneChild.query);
  if (!menuPath) return;

  // 从当前路由获取所有 params
  const { params } = route;

  // 替换路径中所有 :param 形式的参数
  const targetPath = menuPath.replace(/:(\w+)/g, (match, key) => {
    if (params && params.hasOwnProperty(key)) {
      return params[key];
    }
    return match; // 没有对应参数就保留原样
  });

  router.push(targetPath);
}
</script>

<style></style>
