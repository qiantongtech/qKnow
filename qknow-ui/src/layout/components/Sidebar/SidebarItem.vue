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
                        :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"
                        v-if="!props.isNest"
                    />
                    <template #title
                        ><span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">{{
                            onlyOneChild.meta.title
                        }}</span></template
                    >
                </el-menu-item>
            </app-link>
        </template>

        <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported>
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
    import { isExternal } from '@/utils/validate';
    import AppLink from './Link';
    import { getNormalPath } from '@/utils/anivia.js';
    import useUserStore from '@/store/system/user.js';
    const route = useRoute();
    const router = useRouter();

    const props = defineProps({
        // route object
        item: {
            type: Object,
            required: true
        },
        isNest: {
            type: Boolean,
            default: false
        },
        basePath: {
            type: String,
            default: ''
        }
    });

    const onlyOneChild = ref({});
    const getTitleStyle = (title) => {
        console.log('🚀 ~ getTitleStyle ~ title:', title);
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
            fontSize: '19px !important',
            width: '19px !important',
            height: '19px !important'
        };
    };

    const demo = (e) => {
        console.log(e, props.isNest, 111111);
        return props.isNest ? 1 : 2;
    };

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
            onlyOneChild.value = { ...parent, path: '', noShowingChildren: true };
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
                path: getNormalPath(props.basePath + '/' + routePath),
                query: query
            };
        }
        return getNormalPath(props.basePath + '/' + routePath);
    }

    function hasTitle(title) {
        if (title.length > 5) {
            return title;
        } else {
            return '';
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
