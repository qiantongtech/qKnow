/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

/*
 * @Date: 2022-08-25 14:13:11
 * @LastEditors: StavinLi 495727881@qq.com
 * @LastEditTime: 2023-05-24 15:00:32
 * @FilePath: /Workflow-Vue3/src/store/index.js
 */
import {defineStore} from 'pinia';

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
			conditionNodes: [],
		},
	}),
	actions: {


		setPromoter(payload) {
			this.promoterDrawer = payload
		},

		setApprover(payload) {
			this.approverDrawer = payload
		},

		setDelay(payload) {
			this.delayDrawer = payload
		},

		setApproverConfig(payload) {
			this.approverConfigData = payload
		},

		setDelayConfig(payload) {
			this.delayConfigData = payload
		},

		setStarterConfig(payload) {
			this.starterConfigData = payload
		},
		setCopyer(payload) {
			this.copyerDrawer = payload
		},

		setCopyerConfig(payload) {
			this.copyerConfig1 = payload
		},
		setCondition(payload) {
			this.conditionDrawer = payload
		},
		setConditionsConfig(payload) {
			this.conditionsConfig1 = payload
		},
	}
})
