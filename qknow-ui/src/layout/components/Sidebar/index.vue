<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div
    :class="{ 'has-logo': showLogo, 'navbar-logo': displayLogo }"
    :style="{
      backgroundColor:
        sideTheme === 'theme-dark'
          ? variables.menuBackground
          : variables.menuLightBackground,
      height: sidebar.hide ? '60px' : '100%',
    }"
  >
    <logo
      v-if="showLogo"
      :collapse="isCollapse"
      :class="{ 'navbar-logo': displayLogo }"
    />
    <el-scrollbar
      :class="sideTheme"
      wrap-class="scrollbar-wrapper"
      v-if="!sidebar.hide"
    >
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="
          sideTheme === 'theme-dark'
            ? variables.menuBackground
            : variables.menuLightBackground
        "
        :text-color="
          sideTheme === 'theme-dark'
            ? variables.menuColor
            : variables.menuLightColor
        "
        :unique-opened="true"
        :active-text-color="/*系统配置 theme*/ '#fff'"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          :style="{ '--bgColor': theme }"
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>

    <div :class="['help', { collapse: isCollapse }]">
        <div @click="handleHelp" class="help-btn">
          <svg-icon class="img" icon-class="help" style="width: 12px !important; height: 12px !important; margin-right: 6px;" />
          <span>帮助中心</span>
        </div>
        <div class="help-second">
          <span @click="handleFAQ">FAQ</span>
          <span class="line"></span>
          <span @click="handleAbout">关于</span>
        </div>
      </div>
  </div>
</template>

<script setup>
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/system/styles/variables.module.scss";
import useAppStore from "@/store/system/app";
import useSettingsStore from "@/store/system/settings";
import usePermissionStore from "@/store/system/permission";
import defaultSettings from "@/settings";

const route = useRoute();
const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();

const sidebarRouters = computed(() => permissionStore.sidebarRouters);
const showLogo = computed(() => settingsStore.sidebarLogo);
const sideTheme = computed(() => settingsStore.sideTheme);
const theme = computed(() => settingsStore.theme);
const isCollapse = computed(() => !appStore.sidebar.opened);
const sidebar = computed(() => useAppStore().sidebar);

const activeMenu = computed(() => {
  const { meta, path } = route;
  // if set path, the sidebar will highlight the path you set
  console.log(meta.activeMenu, sidebarRouters);
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
});

const displayLogo = computed(() => {
  console.log(
    "defaultSettings.navbarLogoRoutes",
    defaultSettings.navbarLogoRoutes
  );
  const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
  const isSpecialRoute = navbarLogoRoutes.some((logoPath) =>
    route.path.startsWith(logoPath)
  );
  return isSpecialRoute;
});

const handleFAQ = () => {
  window.open("https://qknow.qiantong.tech/docs/others/faq.html", "_blank");
};
const handleAbout = () => {
  window.open("https://qknow.qiantong.tech/", "_blank");
};
const handleHelp = () => {
  window.open("http://114.66.57.2:8089/docs/start/introduction.html", "_blank");
};
</script>

<style lang="scss" scoped>
/* 子菜单颜色 */
.theme-dark {
  ::v-deep .nest-menu li {
    background-color: #0c2135 !important;
  }
}

/* 选中子菜单颜色 */
.theme-dark {
  ::v-deep div .nest-menu li.is-active {
    background-color: var(--bgColor) !important;
  }
}
.navbar-logo {
  // background-color: #fff !important;
  // webkit-box-shadow: 2px 0 6px rgb(255 255 255 / 35%) !important;
  // box-shadow: 2px 0 6px rgb(255 255 255 / 35%) !important;
}

::v-deep(.el-scrollbar) {
    height: calc(100% - 146px) !important;
    background: transparent;
}

.help {
  height: 86px;
  display: flex;
  flex-direction: column;
  align-items: center;

  .help-btn {
    cursor: pointer;
    width: 153px;
    height: 30px;
    background: linear-gradient(90deg, #5d90f9 0%, #2c6fff 100%);
    border-radius: 2px 2px 2px 2px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 10px 0;

    .img {
      width: 15px !important;
      height: 15px !important;
      margin-right: 10px;
    }

    span {
      font-family: PingFang SC;
      font-weight: 400;
      font-size: 12px;
      color: #ffffff;
    }
  }

  .help-second {
    display: flex;
    justify-content: center;
    align-items: center;
    font-family: PingFang SC;
    font-size: 12px;
    color: #a8b2bc;

    span {
      cursor: pointer;
    }

    .line {
      cursor: default;
      width: 1px;
      height: 8px;
      background: #a8b2bc;
      margin: 0 15px;
    }
  }

  &.collapse {
    .help-btn {
      width: 30px;
      height: 30px;

      .img {
        margin: 0 !important;
      }

      span {
        display: none;
      }
    }

    .help-second {
      display: flex;
      flex-direction: column;

      span {
        font-size: 10px;
      }

      .line {
        width: 10px;
        height: 1px;
        margin: 2px 0;
      }
    }
  }
}
</style>
