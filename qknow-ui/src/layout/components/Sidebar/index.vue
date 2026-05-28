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
            height: sidebar.hide ? '60px' : '100%'
        }"
    >
        <logo v-if="showLogo" :collapse="isCollapse" :class="{ 'navbar-logo': displayLogo }" />
        <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper" v-if="!sidebar.hide">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="
                    sideTheme === 'theme-dark'
                        ? variables.menuBackground
                        : variables.menuLightBackground
                "
                :text-color="
                    sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor
                "
                :unique-opened="true"
                :active-text-color="/*系统配置 theme*/ '#fff'"
                :collapse-transition="false"
                mode="vertical"
            >
                <sidebar-item
                    :style="{ '--bgColor': theme }"
                    class="sidebar-item"
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
        </el-scrollbar>

        <div :class="['help', { collapse: isCollapse }]">
            <!-- 折叠状态下只显示图标 -->
            <div v-if="isCollapse" class="help-icon-wrapper">
                <svg-icon class="help-icon" icon-class="help-title" />
                <!-- 悬浮弹出菜单 -->
                <div class="help-popup">
                    <div class="wrap">
                        <div class="help-head">
                            <div class="help-title">
                                <svg-icon class="img" icon-class="help-title" />
                                <span>社区与支持</span>
                            </div>
                            <div class="help-desc">使用帮助、常见问题解答</div>
                        </div>
                        <div @click="handleHelp" class="help-btn">
                            <el-icon><House /></el-icon>
                            <span>qKnow社区</span>
                        </div>
                        <div class="help-second">
                            <span @click="handleFAQ">文档站</span>
                            <span class="line"></span>
                            <span @click="handleAbout">关于</span>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 展开状态显示完整内容 -->
            <div v-else class="wrap">
                <div class="help-head">
                    <div class="help-title">
                        <svg-icon class="img" icon-class="help-title" />
                        <span>社区与支持</span>
                    </div>
                    <div class="help-desc">使用帮助、常见问题解答</div>
                </div>
                <div @click="handleHelp" class="help-btn">
                    <el-icon><House /></el-icon>
                    <span>qKnow社区</span>
                </div>
                <div class="help-second">
                    <span @click="handleFAQ">文档站</span>
                    <span class="line"></span>
                    <span @click="handleAbout">关于</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import Logo from './Logo';
    import SidebarItem from './SidebarItem';
    import variables from '@/assets/system/styles/variables.module.scss';
    import useAppStore from '@/store/system/app';
    import useSettingsStore from '@/store/system/settings';
    import usePermissionStore from '@/store/system/permission';
    import defaultSettings from '@/settings';

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
        console.log('defaultSettings.navbarLogoRoutes', defaultSettings.navbarLogoRoutes);
        const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
        const isSpecialRoute = navbarLogoRoutes.some((logoPath) => route.path.startsWith(logoPath));
        return isSpecialRoute;
    });

    const handleFAQ = () => {
        window.open('https://community.qknow.ai/docs/start/introduction.html', '_blank');
    };
    const handleAbout = () => {
        window.open('https://qiantong.tech/', '_blank');
    };
    const handleHelp = () => {
        window.open('https://community.qknow.ai', '_blank');
    };
</script>

<style lang="scss" scoped>
    /* 子菜单颜色 */
    .theme-dark {
        ::v-deep .nest-menu li {
            // background-color: #0c2135 !important;
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
        height: calc(100% - 220px) !important;
        background: transparent;
        padding: 20px 0;
    }

    .help {
        height: auto;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 0 20px;

        .wrap {
            position: relative;
            flex-shrink: 0;
            overflow: hidden;
            isolation: isolate;
            background:
                linear-gradient(135deg, rgba(76, 129, 255, 0.18), rgba(255, 255, 255, 0.05)),
                rgba(255, 255, 255, 0.06);
            border-radius: 4px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            padding: 12px;
            box-shadow:
                inset 0 0 0 1px rgba(92, 144, 255, 0.08),
                0 10px 28px rgba(33, 103, 255, 0.08);
            animation: helpCardBreath 4.8s ease-in-out infinite;
            transform: translateZ(0);
            backface-visibility: hidden;

            &::before {
                content: '';
                position: absolute;
                inset: -1px;
                z-index: 0;
                border-radius: inherit;
                background: linear-gradient(
                    120deg,
                    transparent 0%,
                    rgba(255, 255, 255, 0.06) 28%,
                    rgba(118, 172, 255, 0.32) 48%,
                    rgba(255, 255, 255, 0.08) 62%,
                    transparent 100%
                );
                transform: translateX(-125%);
                animation: helpCardSweep 5.6s ease-in-out infinite;
                will-change: transform, opacity;
                backface-visibility: hidden;
            }

            &::after {
                content: '';
                position: absolute;
                right: -36px;
                top: -34px;
                width: 86px;
                height: 86px;
                z-index: 0;
                border-radius: 50%;
                background: radial-gradient(circle, rgba(60, 139, 255, 0.32), transparent 68%);
                filter: blur(2px);
                animation: helpCardGlow 4.8s ease-in-out infinite;
                will-change: transform, opacity;
                backface-visibility: hidden;
            }
        }

        .help-head {
            position: relative;
            z-index: 1;
            margin-bottom: 20px;
            color: #fff;
            .svg-icon {
                width: 17px;
                margin-right: 10px !important;
                filter: drop-shadow(0 0 8px rgba(99, 166, 255, 0.36));
                animation: helpIconPulse 3.6s ease-in-out infinite;
            }

            .help-desc {
                color: rgba(255, 255, 255, 0.75);
                font-size: 12px;
            }
        }

        .help-btn {
            cursor: pointer;
            position: relative;
            z-index: 1;
            overflow: hidden;
            width: 153px;
            height: 30px;
            background: linear-gradient(90deg, #5d90f9 0%, #2c6fff 100%);
            border-radius: 2px 2px 2px 2px;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 10px 0;
            box-shadow: 0 8px 18px rgba(44, 111, 255, 0.2);
            transition:
                transform 0.2s ease,
                box-shadow 0.2s ease;
            animation: helpBtnBreath 2.6s ease-in-out infinite;
            will-change: transform, filter, box-shadow;
            backface-visibility: hidden;

            &::after {
                content: '';
                position: absolute;
                top: 0;
                bottom: 0;
                left: -35%;
                width: 28%;
                background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.42), transparent);
                transform: skewX(-18deg);
                animation: helpBtnShine 3.8s ease-in-out infinite;
                will-change: left, opacity;
                backface-visibility: hidden;
            }

            &:hover {
                transform: translateY(-1px);
                box-shadow: 0 10px 22px rgba(44, 111, 255, 0.34);
            }

            .el-icon {
                font-size: 14px;
                color: #fff;
                margin-right: 4px;
                position: relative;
                z-index: 1;
            }

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
                position: relative;
                z-index: 1;
            }
        }

        .help-title {
            position: relative;
            z-index: 1;
            font-family:
                PingFangSC,
                PingFang SC;
            font-weight: 500;
            font-size: 14px;
            color: #ffffff;
            line-height: 20px;
            margin-bottom: 10px;
        }

        .help-second {
            position: relative;
            z-index: 1;
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

        // 折叠状态下的样式
        &.collapse {
            padding: 0;

                .help-icon-wrapper {
                    position: relative;
                display: flex;
                align-items: center;
                justify-content: center;
                width: 60px;
                height: 60px;
                    background: rgba(255, 255, 255, 0.06);
                    border-radius: 4px;
                    border: 1px solid rgba(255, 255, 255, 0.2);
                    box-shadow: 0 10px 24px rgba(33, 103, 255, 0.1);
                    animation: helpCardBreath 4.8s ease-in-out infinite;

                    &::before {
                        content: '';
                        position: absolute;
                        inset: 0;
                        border-radius: inherit;
                        background: linear-gradient(120deg, transparent, rgba(116, 173, 255, 0.35), transparent);
                        background-size: 260% 100%;
                        background-position: 160% 0;
                        animation: helpIconSweep 5.6s ease-in-out infinite;
                        will-change: background-position, opacity;
                        backface-visibility: hidden;
                    }

                    .help-icon {
                        width: 24px;
                        height: 24px;
                        color: #fff;
                        cursor: pointer;
                        margin-right: 0 !important;
                        position: relative;
                        z-index: 1;
                    }

                .help-popup {
                    position: absolute;
                    left: 100%;
                    top: 50%;
                    transform: translateY(-50%);
                    margin-left: 8px;
                    opacity: 0;
                    visibility: hidden;
                    transition: all 0.3s ease;
                    z-index: 1002;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
                    border-radius: 4px;
                    overflow: hidden;

                    .wrap {
                        background: linear-gradient(180deg, #1a365d 0%, #0d1b2a 100%);
                        border: 1px solid rgba(255, 255, 255, 0.1);
                        padding: 16px;
                        min-width: 200px;
                    }
                }

                &:hover .help-popup {
                    opacity: 1;
                    visibility: visible;
                }
            }
        }
    }

    @keyframes helpCardBreath {
        0%,
        100% {
            border-color: rgba(255, 255, 255, 0.2);
            box-shadow:
                inset 0 0 0 1px rgba(92, 144, 255, 0.08),
                0 10px 28px rgba(33, 103, 255, 0.08);
        }

        50% {
            border-color: rgba(111, 168, 255, 0.5);
            box-shadow:
                inset 0 0 0 1px rgba(141, 190, 255, 0.18),
                0 14px 34px rgba(33, 103, 255, 0.18);
        }
    }

    @keyframes helpCardSweep {
        0%,
        62% {
            transform: translateX(-125%);
            opacity: 0;
        }

        72% {
            opacity: 1;
        }

        100% {
            transform: translateX(125%);
            opacity: 0;
        }
    }

    @keyframes helpCardGlow {
        0%,
        100% {
            opacity: 0.36;
            transform: scale(0.92);
        }

        50% {
            opacity: 0.72;
            transform: scale(1.08);
        }
    }

    @keyframes helpIconSweep {
        0%,
        62% {
            background-position: 160% 0;
            opacity: 0;
        }

        72% {
            opacity: 1;
        }

        100% {
            background-position: -60% 0;
            opacity: 0;
        }
    }

    @keyframes helpIconPulse {
        0%,
        100% {
            opacity: 0.86;
            transform: translateY(0);
        }

        50% {
            opacity: 1;
            transform: translateY(-1px);
        }
    }

    @keyframes helpBtnShine {
        0%,
        58% {
            left: -35%;
            opacity: 0;
        }

        68% {
            opacity: 1;
        }

        100% {
            left: 112%;
            opacity: 0;
        }
    }

    @keyframes helpBtnBreath {
        0%,
        100% {
            box-shadow:
                0 0 0 0 rgba(66, 132, 255, 0),
                0 8px 18px rgba(44, 111, 255, 0.2);
            filter: brightness(1);
            transform: scale(1);
        }

        50% {
            box-shadow:
                0 0 0 4px rgba(66, 132, 255, 0.16),
                0 0 22px rgba(88, 151, 255, 0.46);
            filter: brightness(1.08);
            transform: scale(1.035);
        }
    }
</style>
