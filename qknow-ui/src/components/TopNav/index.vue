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
    <el-menu
        ref="menuRef"
        :default-active="activeMenu"
        mode="horizontal"
        @select="handleSelect"
        :ellipsis="false"
        class="custom-topmenu"
    >
        <template v-for="(item, index) in topMenus">
            <el-menu-item
                :style="{ '--theme': theme }"
                :index="item.path"
                :key="index"
                v-if="index < visibleNumber"
            >
                <svg-icon
                    v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
                    :icon-class="item.meta.icon"
                />
                {{ item.meta.title }}
            </el-menu-item>
        </template>

        <!-- 顶部菜单超出数量折叠 -->
        <el-sub-menu
            :style="{ '--theme': theme }"
            index="more"
            class="more-top-menu"
            v-if="topMenus.length > visibleNumber"
        >
            <template #title>
                <svg-icon icon-class="menu" />
                更多菜单
            </template>
            <template v-for="(item, index) in topMenus">
                <el-menu-item :index="item.path" :key="index" v-if="index >= visibleNumber">
                    <svg-icon
                        v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
                        :icon-class="item.meta.icon"
                    />
                    {{ item.meta.title }}
                </el-menu-item>
            </template>
        </el-sub-menu>
        <div ref="measureRef" class="topmenu-measure" aria-hidden="true">
            <div
                v-for="(item, index) in topMenus"
                :key="`measure-${index}`"
                class="topmenu-measure-item"
                data-topmenu-measure-item
            >
                <svg-icon
                    v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
                    :icon-class="item.meta.icon"
                />
                <span>{{ item.meta.title }}</span>
            </div>
            <div class="topmenu-measure-item topmenu-measure-more" data-topmenu-measure-more>
                <svg-icon icon-class="menu" />
                <span>更多菜单</span>
                <span class="topmenu-measure-arrow"></span>
            </div>
        </div>
    </el-menu>
</template>

<script setup>
    import { isHttp } from '@/utils/validate';
    import useAppStore from '@/store/system/app';
    import useSettingsStore from '@/store/system/settings';
    import usePermissionStore from '@/store/system/permission';
    import useTagsViewStore from '@/store/system/tagsView';
    const { proxy } = getCurrentInstance();
    // 顶部栏初始数
    const visibleNumber = ref(0);
    const menuRef = ref(null);
    const measureRef = ref(null);
    let resizeObserver = null;
    let calculateFrame = null;
    // 当前激活菜单的 index
    const currentIndex = ref('/system');
    // 隐藏侧边栏路由
    const hideList = ['/index', '/user/profile'];

    const appStore = useAppStore();
    const settingsStore = useSettingsStore();
    const permissionStore = usePermissionStore();
    const route = useRoute();
    const router = useRouter();
    const emit = defineEmits(['getRouter']);
    // 主题颜色
    const theme = computed(() => settingsStore.theme);
    // 所有的路由信息
    const routers = computed(() => permissionStore.topbarRouters);
    // 顶部显示菜单
    const topMenus = computed(() => {
        let topMenus = [];
        routers.value.map((menu) => {
            if (menu.hidden !== true) {
                // 兼容顶部栏一级菜单内部跳转
                if (menu.path === '/') {
                    topMenus.push(menu.children[0]);
                } else {
                    topMenus.push(menu);
                }
            }
        });
        return topMenus;
    });

    // 设置子路由
    const childrenMenus = computed(() => {
        let arr = [];
        routers.value.forEach((router) => {
            if (!router.children) {
                return;
            }
            router.children.forEach((child) => {
                if (child.parentPath === undefined) {
                    if (router.path === '/') {
                        child.path = '/' + child.path;
                    } else {
                        if (!isHttp(child.path)) {
                            child.path = router.path + '/' + child.path;
                        }
                    }
                    child.parentPath = router.path;
                }
                arr.push(child);
            });
        });
        return arr;
    });

    function isRootGroupChild(path) {
        return childrenMenus.value.some(
            (item) =>
                item.parentPath === '' && (item.path === path || path.startsWith(`${item.path}/`))
        );
    }

    // 默认激活的菜单
    const activeMenu = computed(() => {
        const path = route.path;
        let activePath = path;
        emit('getRouter', path);
        console.log('path', path);
        if (path === '/index') {
            const firstMenu = topMenus.value[0];
            if (firstMenu) activePath = firstMenu.path;
        } else if (isRootGroupChild(path)) {
            activePath = '';
            if (!route.meta.link) appStore.toggleSideBarHide(false);
        } else if (
            path !== undefined &&
            path.lastIndexOf('/') > 0 &&
            hideList.indexOf(path) === -1
        ) {
            const tmpPath = path.substring(1, path.length);
            activePath = '/' + tmpPath.substring(0, tmpPath.indexOf('/'));
            if (!route.meta.link) appStore.toggleSideBarHide(false);
        } else if (!route.children) {
            activePath = path;
            appStore.toggleSideBarHide(true);
        }
        activeRoutes(activePath);

        // 根据路由配置，直接控制左侧菜单的隐藏
        if (route.meta.sidebar === false) {
            appStore.toggleSideBarHide(true);
        }
        return activePath;
    });

    // function setVisibleNumber() {
    //     const width = document.body.getBoundingClientRect().width / 3;
    //     visibleNumber.value = parseInt(width / 85);
    // }

    // 计算可用宽度下的顶部导航栏可显示菜单数量
    function scheduleCalculateVisibleMenus() {
        if (calculateFrame) {
            cancelAnimationFrame(calculateFrame);
        }
        calculateFrame = requestAnimationFrame(() => {
            calculateFrame = null;
            calculateVisibleMenus();
        });
    }

    function getElementWidth(element) {
        return Math.ceil(element?.getBoundingClientRect?.().width || 0);
    }

    function getHorizontalPadding(element) {
        const style = window.getComputedStyle(element);
        return (parseFloat(style.paddingLeft) || 0) + (parseFloat(style.paddingRight) || 0);
    }

    function getAvailableMenuWidth(menuEl, rightMenuEl) {
        const safeGap = 24;
        const rightWidth = getElementWidth(rightMenuEl);
        const nextRightOffset = rightWidth > 0 ? rightWidth + safeGap : 430;

        menuEl.style.setProperty('--topnav-right-offset', `${nextRightOffset}px`);

        const menuWidth = getElementWidth(menuEl);
        return Math.max(0, menuWidth - getHorizontalPadding(menuEl));
    }

    function calculateVisibleMenus() {
        const menuEl = menuRef.value?.$el || menuRef.value;
        const measureEl = measureRef.value;
        const navbarEl = menuEl?.closest?.('.navbar');
        const rightMenuEl = navbarEl?.querySelector?.('.right-menu');

        if (!menuEl || !measureEl || !topMenus.value.length) {
            visibleNumber.value = topMenus.value.length;
            return;
        }

        const availableWidth = getAvailableMenuWidth(menuEl, rightMenuEl);

        if (availableWidth <= 0) {
            visibleNumber.value = 0;
            return;
        }

        const itemWidths = Array.from(
            measureEl.querySelectorAll('[data-topmenu-measure-item]')
        ).map(getElementWidth);
        const moreMenuWidth = getElementWidth(
            measureEl.querySelector('[data-topmenu-measure-more]')
        );

        let usedWidth = 0;
        let fullCount = 0;
        for (const width of itemWidths) {
            if (usedWidth + width > availableWidth) {
                break;
            }
            usedWidth += width;
            fullCount += 1;
        }

        if (fullCount >= topMenus.value.length) {
            visibleNumber.value = topMenus.value.length;
            return;
        }

        usedWidth = moreMenuWidth;
        let finalCount = 0;
        for (const width of itemWidths) {
            if (usedWidth + width > availableWidth) {
                break;
            }
            usedWidth += width;
            finalCount += 1;
        }

        visibleNumber.value = finalCount;
    }

    function closePageExclusion(key) {
        const visitedViews = useTagsViewStore().visitedViews;

        for (let i = visitedViews.length - 1; i >= 0; i--) {
            const view = visitedViews[i];
            if (view.path.includes('/index')) {
                continue;
            }
            if (!view.path.includes(key)) {
                proxy.$tab.closePage(view);
            }
        }
    }

    // 处理顶部导航菜单的选择事件
    async function handleSelect(key, keyPath, type) {
        console.log(key, 'key');
        // 查找选中的路由配置
        const route = routers.value.find((item) => item.path === key);

        if (!route || !route.children) {
            proxy.$modal.msgWarning('功能开发中！');
            return;
        }
        //子组件调用父组件
        emit('getRouter', key);

        // 设置当前选中的菜单索引
        currentIndex.value = key;

        if (isHttp(key)) {
            // 如果是http(s)链接,在新窗口打开
            window.open(key, '_blank');
        } else if (!route || !route.children) {
            // 如果没有子路由,在当前窗口打开
            const routeMenu = childrenMenus.value.find((item) => item.path === key);
            if (routeMenu && routeMenu.query) {
                // 如果有query参数,解析后带上
                let query = JSON.parse(routeMenu.query);
                router.push({ path: key, query: query });
            } else {
                // 没有query参数直接跳转
                router.push({ path: key });
            }
            // 隐藏左侧菜单
            appStore.toggleSideBarHide(true);
        } else {
            // 有子路由,显示左侧联动菜单
            let routes = activeRoutes(key);
            if (type) {
                closePageExclusion(key);
                if (routes.length > 0) {
                    // 获取所有标签页

                    if (
                        routes[0].children != null &&
                        routes[0].children != undefined &&
                        routes[0].children.length > 0
                    ) {
                        const lastChild = JSON.parse(JSON.stringify(routes[0].children[0]));
                        const fullPath = `${routes[0].path}/${routes[0].children[0].path}`;
                        lastChild.path = fullPath;
                        proxy.$tab.refreshPage(lastChild);
                    } else if (routes[0].query != null) {
                        const lastChild = JSON.parse(JSON.stringify(routes[0]));
                        const query = JSON.parse(routes[0].query);
                        lastChild.query = query;
                        proxy.$tab.refreshPage(lastChild);
                    } else {
                        proxy.$tab.refreshPage(routes[0]);
                    }
                }
            }
            // 显示左侧菜单
            appStore.toggleSideBarHide(false);
        }
    }

    function activeRoutes(key) {
        let routes = [];
        if (childrenMenus.value && childrenMenus.value.length > 0) {
            childrenMenus.value.map((item) => {
                if (key == item.parentPath || (key == 'index' && '' == item.path)) {
                    routes.push(item);
                }
            });
        }
        if (routes.length > 0) {
            permissionStore.setSidebarRouters(routes);
        }
        return routes;
    }

    onMounted(() => {
        window.addEventListener('resize', scheduleCalculateVisibleMenus);
        nextTick(() => {
            calculateVisibleMenus();
            const menuEl = menuRef.value?.$el || menuRef.value;
            const navbarEl = menuEl?.closest?.('.navbar');
            const rightMenuEl = navbarEl?.querySelector?.('.right-menu');
            if (window.ResizeObserver && navbarEl) {
                resizeObserver = new ResizeObserver(scheduleCalculateVisibleMenus);
                resizeObserver.observe(navbarEl);
                resizeObserver.observe(menuEl);
                if (measureRef.value) {
                    resizeObserver.observe(measureRef.value);
                }
                if (rightMenuEl) {
                    resizeObserver.observe(rightMenuEl);
                }
            }
        });
    });
    watch(topMenus, () => nextTick(scheduleCalculateVisibleMenus), { flush: 'post' });
    onBeforeUnmount(() => {
        window.removeEventListener('resize', scheduleCalculateVisibleMenus);
        resizeObserver?.disconnect();
        if (calculateFrame) {
            cancelAnimationFrame(calculateFrame);
        }
    });
    // 如果需要暴露给父组件使用，可以使用 defineExpose
    defineExpose({
        handleSelect
    });
</script>

<style lang="scss">
    .el-menu--horizontal.el-menu {
        padding-top: 10px;
        border-bottom: none;
    }

    .topmenu-container.el-menu--horizontal > .el-menu-item {
        font-size: 16px;
        font-weight: bold;
        float: left;
        height: 40px !important;
        line-height: 40px !important;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #333 !important;
        padding: 0 23px !important;
        margin: 0 3px !important;
        border-bottom: none !important;
        border-radius: 4px;
        transition:
            background-color 0.22s ease,
            color 0.22s ease,
            box-shadow 0.22s ease;
    }

    /* sub-menu item */
    .topmenu-container.el-menu--horizontal > .el-sub-menu .el-sub-menu__title {
        font-size: 16px;
        font-weight: bold;
        float: left;
        height: 40px !important;
        line-height: 40px !important;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #333 !important;
        padding: 0 23px !important;
        margin: 0 3px !important;
        border-radius: 4px;
        border-bottom: none !important;
        transition:
            background-color 0.22s ease,
            color 0.22s ease,
            box-shadow 0.22s ease;
    }

    .topmenu-container.el-menu--horizontal > .el-menu-item.is-active,
    .el-menu--horizontal > .el-sub-menu.is-active .el-submenu__title,
    .el-menu--horizontal > .el-sub-menu.is-active .el-sub-menu__title {
        background-color: #2666fb !important;
        border-bottom: none !important;
        color: #fff !important;
        box-shadow: 0 6px 16px rgba(38, 102, 251, 0.18);
    }

    .topmenu-container.el-menu--horizontal > .el-menu-item.is-active .svg-icon,
    .el-menu--horizontal > .el-sub-menu.is-active .el-sub-menu__title .svg-icon {
        color: #fff !important;
    }

    /* 背景色隐藏 */
    .topmenu-container.el-menu--horizontal > .el-menu-item:not(.is-disabled):focus,
    .topmenu-container.el-menu--horizontal > .el-menu-item:not(.is-disabled):hover,
    .topmenu-container.el-menu--horizontal > .el-sub-menu .el-sub-menu__title:hover {
        background-color: #2666fb !important;
        color: #fff !important;
        border-bottom: none !important;
        box-shadow: 0 6px 16px rgba(38, 102, 251, 0.18);
    }

    .topmenu-container.el-menu--horizontal > .el-menu-item.is-active:not(.is-disabled):focus,
    .topmenu-container.el-menu--horizontal > .el-menu-item.is-active:not(.is-disabled):hover,
    .el-menu--horizontal > .el-sub-menu.is-active .el-sub-menu__title:hover {
        background-color: #2666fb !important;
        color: #fff !important;
    }

    /* 图标右间距 */
    .topmenu-container .svg-icon {
        margin-right: 4px;
        transition: color 0.22s ease;
    }

    .topmenu-container .topmenu-measure {
        position: absolute;
        left: 0;
        top: 0;
        display: flex;
        align-items: center;
        height: 0;
        overflow: visible;
        pointer-events: none;
        visibility: hidden;
        white-space: nowrap;
    }

    .topmenu-container .topmenu-measure-item {
        box-sizing: border-box;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 40px !important;
        line-height: 40px !important;
        margin: 0 3px !important;
        padding: 0 23px !important;
        border-radius: 4px;
        color: #333 !important;
        font-size: 16px;
        font-weight: bold;
        white-space: nowrap;
    }

    .topmenu-container .topmenu-measure-arrow {
        width: 14px;
        height: 14px;
        margin-left: 8px;
        flex-shrink: 0;
    }

    .topmenu-container.el-menu--horizontal > .el-menu-item:not(.is-disabled):focus .svg-icon,
    .topmenu-container.el-menu--horizontal > .el-menu-item:not(.is-disabled):hover .svg-icon,
    .topmenu-container.el-menu--horizontal > .el-sub-menu .el-sub-menu__title:hover .svg-icon {
        color: #fff !important;
    }

    /* topmenu more arrow */
    .topmenu-container .el-sub-menu .el-sub-menu__icon-arrow {
        position: static;
        flex-shrink: 0;
        vertical-align: middle;
        margin-left: 8px;
        margin-top: 0px;
    }

    .topmenu-container .more-top-menu .el-sub-menu__title .svg-icon,
    .topmenu-container .more-top-menu .el-sub-menu__icon-arrow {
        flex-shrink: 0;
        color: inherit;
        filter: drop-shadow(0.45px 0 0 currentColor) drop-shadow(0 0.45px 0 currentColor);
    }

    .topmenu-container .more-top-menu .el-sub-menu__title {
        white-space: nowrap;
    }

    .el-menu--horizontal .el-menu .el-menu-item {
        height: 40px !important;
        line-height: 40px !important;

        .svg-icon {
            margin-right: 10px;
        }
    }
</style>
