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

/*
 * @Date: 2022-08-25 14:13:11
 * @LastEditors: StavinLi 495727881@qq.com
 * @LastEditTime: 2023-05-24 15:00:32
 * @FilePath: /Workflow-Vue3/src/store/index.js
 */
import { defineStore } from 'pinia';

export const useStore = defineStore('store', {
    state: () => ({
        promoterDrawer: false,

        approverDrawer: false,

        delayDrawer: false,

        approverConfigData: {},

        delayConfigData: {},
        starterConfigData: {},
        copyerDrawer: false,

        copyerConfig1: {},

        conditionDrawer: false,
        conditionsConfig1: {
            conditionNodes: []
        }
    }),
    actions: {
        setPromoter(payload) {
            this.promoterDrawer = payload;
        },

        setApprover(payload) {
            this.approverDrawer = payload;
        },

        setDelay(payload) {
            this.delayDrawer = payload;
        },

        setApproverConfig(payload) {
            this.approverConfigData = payload;
        },

        setDelayConfig(payload) {
            this.delayConfigData = payload;
        },

        setStarterConfig(payload) {
            this.starterConfigData = payload;
        },
        setCopyer(payload) {
            this.copyerDrawer = payload;
        },

        setCopyerConfig(payload) {
            this.copyerConfig1 = payload;
        },
        setCondition(payload) {
            this.conditionDrawer = payload;
        },
        setConditionsConfig(payload) {
            this.conditionsConfig1 = payload;
        }
    }
});
