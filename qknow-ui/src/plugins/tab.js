/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import useTagsViewStore from '@/store/system/tagsView';
import router from '@/router';

function normalizeQuery(query) {
    if (typeof query !== 'string') {
        return query;
    }

    try {
        return JSON.parse(query);
    } catch {
        return query;
    }
}

export default {
    // 刷新当前tab页签
    refreshPage(obj) {
        const { path, query, matched } = router.currentRoute.value;
        let target = obj;
        if (target === undefined) {
            matched.forEach((m) => {
                if (m.components && m.components.default && m.components.default.name) {
                    if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
                        target = { name: m.components.default.name, path: path, query: query };
                    }
                }
            });
        }
        return useTagsViewStore()
            .delCachedView(target)
            .then(() => {
                const { path, query } = target;
                router.replace({
                    path: '/redirect' + path,
                    query: normalizeQuery(query)
                });
            });
    },
    // 关闭当前tab页签，打开新页签
    closeOpenPage(obj) {
        useTagsViewStore().delView(router.currentRoute.value);
        if (obj !== undefined) {
            return router.push(obj);
        }
    },
    // 关闭指定tab页签
    closePage(obj) {
        if (obj === undefined) {
            return useTagsViewStore()
                .delView(router.currentRoute.value)
                .then(({ visitedViews }) => {
                    const latestView = visitedViews.slice(-1)[0];
                    if (latestView) {
                        return router.push(latestView.fullPath);
                    }
                    return router.push('/');
                });
        }
        return useTagsViewStore().delView(obj);
    },
    // 关闭所有tab页签
    closeAllPage() {
        return useTagsViewStore().delAllViews();
    },
    // 关闭左侧tab页签
    closeLeftPage(obj) {
        return useTagsViewStore().delLeftTags(obj || router.currentRoute.value);
    },
    // 关闭右侧tab页签
    closeRightPage(obj) {
        return useTagsViewStore().delRightTags(obj || router.currentRoute.value);
    },
    // 关闭其他tab页签
    closeOtherPage(obj) {
        return useTagsViewStore().delOthersViews(obj || router.currentRoute.value);
    },
    // 打开tab页签
    openPage(url) {
        return router.push(url);
    },
    // 修改tab页签
    updatePage(obj) {
        return useTagsViewStore().updateVisitedView(obj);
    }
};
