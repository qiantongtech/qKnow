<!--
 Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 
 This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 
 qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 You may use qKnow for commercial purposes, but you may not remove, hide,
 modify, or replace the qKnow logo, copyright notices, license notices,
 or attribution information without a separate commercial license.
 
 White-label use, OEM distribution, rebranding, or presenting qKnow as
 another product requires separate commercial authorization from
 Jiangsu Qiantong Technology Co., Ltd.
 
 Business License: https://community.qknow.ai/business/policy.html
 See the LICENSE file in the project root for full license information.
-->

<template>
    <div class="sidebar-logo-container" :class="{ collapse: collapse }">
        <transition name="sidebarLogoFade">
            <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
                <!--        <img v-if="logo" :src="simpLogo" class="sidebar-logo" />-->
                <img v-if="logo" :src="displaySimpLogo" class="sidebar-logo" />
                <h1
                    v-else
                    class="sidebar-title"
                    :style="{
                        color:
                            sideTheme === 'theme-dark'
                                ? variables.logoTitleColor
                                : variables.logoLightTitleColor
                    }"
                >
                    {{ title }}
                </h1>
            </router-link>
            <router-link v-else key="expand" class="sidebar-logo-link" to="/">
                <!--        <img v-if="logo" :src="logo" class="sidebar-logo" /> -->
                <span
                    v-if="useSplitLogo"
                    class="sidebar-logo-split"
                    :class="{ 'logo-intro': logoIntroActive }"
                >
                    <img :src="logoK" class="sidebar-logo-k" />
                    <img :src="logoQknow" class="sidebar-logo-word" />
                </span>
                <img v-else-if="logo" :src="displayLogo" class="sidebar-logo" />
            </router-link>
        </transition>
    </div>
</template>

<script setup>
    import variables from '@/assets/system/styles/variables.module.scss';
    import logo from '@/assets/system/logo/logo.png';
    import logo2 from '@/assets/system/logo/logo2.png';
    import simpLogo from '@/assets/system/logo/simpLogo.png';
    import logoK from '@/assets/system/logo/logo-k.png';
    import logoQknow from '@/assets/system/logo/logo-qknow.png';

    import useSettingsStore from '@/store/system/settings';
    import defaultSettings from '@/settings';
    import { getContent } from '@/api/system/system/content';

    import { computed } from 'vue';
    const route = useRoute();
    // 使用 ref 来创建响应式的 logo
    const refLogo = ref(null); // 初始化 logo 为 simpLogo.png
    const refSimpLogo = ref(null); // 初始化 logo 为 simpLogo.png

    const logoIntroActive = ref(false);

    const props = defineProps({
        collapse: {
            type: Boolean,
            required: true
        },
        currentRoute: {
            type: String,
            default: '/'
        }
    });

    const displayLogo = computed(() => {
        console.log('defaultSettings.navbarLogoRoutes', defaultSettings.navbarLogoRoutes);
        const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
        const isSpecialRoute = navbarLogoRoutes.some((logoPath) => route.path.startsWith(logoPath));
        return isSpecialRoute ? logo2 : refLogo.value;
    });

    const useSplitLogo = computed(() => {
        const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
        const isSpecialRoute = navbarLogoRoutes.some((logoPath) => route.path.startsWith(logoPath));
        return !isSpecialRoute && (!refLogo.value || refLogo.value === logo);
    });

    const displaySimpLogo = computed(() => {
        const navbarLogoRoutes = defaultSettings.navbarLogoRoutes || [];
        const isSpecialRoute = navbarLogoRoutes.some((logoPath) => route.path.startsWith(logoPath));
        return isSpecialRoute ? logo : refSimpLogo.value;
    });
    onMounted(() => {
        logoIntroActive.value = true;
        window.setTimeout(() => {
            logoIntroActive.value = false;
        }, 2200);
        fetchContent();
    });
    // 使用 getContent 来获取数据，而不是重新定义一个 getContent 函数
    const fetchContent = async () => {
        try {
            const res = await getContent(1);
            if (res.code == 200) {
                const data = res.data;
                const sysLogo = data.logo;
                refLogo.value = sysLogo ? sysLogo : logo;
                refSimpLogo.value = sysLogo ? sysLogo : simpLogo;
            }

            // this.$message.success('内容加载成功');
        } catch (error) {
            refLogo.value = logo;
            refSimpLogo.value = simpLogo;
        }
    };

    const title = import.meta.env.VITE_APP_TITLE;
    const settingsStore = useSettingsStore();
    const sideTheme = computed(() => settingsStore.sideTheme);
</script>

<style lang="scss" scoped>
    .sidebarLogoFade-enter-active {
        transition: opacity 1.5s;
    }

    .sidebarLogoFade-enter,
    .sidebarLogoFade-leave-to {
        opacity: 0;
    }

    .sidebar-logo-container {
        position: relative;
        width: 100%;
        height: 60px;
        line-height: 50px;
        background: transparent;
        text-align: center;
        overflow: hidden;
        border: 1px solid rgba(255, 255, 255, 0.06);

        & .sidebar-logo-link {
            height: 100%;
            width: 100%;

            & .sidebar-logo {
                height: 48px;
                margin-top: 8px;
                vertical-align: middle;
                transform: scale(0.58);
                margin-left: -12px;
            }

            & .sidebar-logo-split {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                height: 100%;
                gap: 2px;
                transform: translateX(-4px);
                vertical-align: middle;
            }

            & .sidebar-logo-k {
                display: block;
                height: 30px;
                object-fit: contain;
                transform-origin: center;
            }

            & .sidebar-logo-word {
                display: block;
                height: 28px;
                object-fit: contain;
            }

            & .logo-intro,
            &:hover .sidebar-logo-split {
                .sidebar-logo-k {
                    animation: sidebarLogoKRunIn 1.45s cubic-bezier(0.16, 0.88, 0.2, 1) 0.18s both;
                }

                .sidebar-logo-word {
                    animation: sidebarLogoWordSettle 0.55s ease-out 0.72s both;
                }
            }

            & .sidebar-title {
                display: inline-block;
                margin: 0;
                color: #fff;
                font-weight: 600;
                line-height: 50px;
                font-size: 14px;
                font-family:
                    Avenir,
                    Helvetica Neue,
                    Arial,
                    Helvetica,
                    sans-serif;
                vertical-align: middle;
            }
        }

        &.navbar-logo {
            background-color: #fff !important;
        }

        &.collapse {
            .sidebar-logo {
                height: 40px;
                margin-top: 0px;
                margin-right: 0px;
                margin-left: -4px;
            }
        }
    }

    @keyframes sidebarLogoKRunIn {
        0% {
            opacity: 0;
            filter: drop-shadow(0 0 0 rgba(69, 145, 255, 0));
            transform: translateX(-90px) scaleX(0.88) scaleY(1.04) rotate(-8deg);
        }

        38% {
            opacity: 1;
            filter: drop-shadow(12px 0 12px rgba(69, 145, 255, 0.48));
            transform: translateX(18px) scaleX(1.08) scaleY(0.94) rotate(4deg);
        }

        54% {
            transform: translateX(-7px) scaleX(0.98) scaleY(1.02) rotate(-2deg);
        }

        70% {
            transform: translateX(4px) scaleX(1.02) scaleY(0.98) rotate(1deg);
        }

        84% {
            filter: drop-shadow(3px 0 8px rgba(69, 145, 255, 0.28));
            transform: translateX(-1px) scale(1) rotate(0deg);
        }

        100% {
            opacity: 1;
            filter: drop-shadow(0 0 0 rgba(69, 145, 255, 0));
            transform: translateX(0) scale(1) rotate(0deg);
        }
    }

    @keyframes sidebarLogoWordSettle {
        0% {
            opacity: 0;
            transform: translateX(-10px);
        }

        100% {
            opacity: 1;
            transform: translateX(0);
        }
    }
</style>
