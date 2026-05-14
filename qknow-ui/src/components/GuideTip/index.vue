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
  <div v-if="visible" :class="['guide-tip', config.type]">
    <div class="tip-header">
            <span class="tip-title" ref="titleRef">
                <svg-icon v-if="config.type === 'warning' || config.type === 'danger'" iconClass="warning" class="tip-icon" />
                <svg-icon v-if="config.type === 'remind'" iconClass="remind" class="tip-icon" />
                <span v-html="config.title"></span>
            </span>
      <div class="header-buttons">
        <el-button v-if="config.type !== 'danger'" class="btn-never-show" @click="neverShow">不再展示</el-button>
        <el-button :class="['btn-close', config.type]" @click="close">关闭</el-button>
      </div>
    </div>
    <div v-if="config.content" class="tip-content" v-html="config.content" @click="handleClick"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onActivated, computed } from 'vue'
import { guideTipConfig } from './guideTipConfig'
import { useRouter } from 'vue-router'
import useUserStore from "@/store/system/user";

const { proxy } = getCurrentInstance();
const userStore = useUserStore()
const STORAGE_KEY = 'guide_tip_status'

const props = defineProps({
  tipId: { type: String, required: true }
})

const router = useRouter()
const visible = ref(true)

const config = computed(() => guideTipConfig[props.tipId] || {})

// 获取存储对象
function getGuideTipStorage() {
  const stored = localStorage.getItem(STORAGE_KEY)
  if (stored) {
    return JSON.parse(stored)
  }
  return {}
}

// 生成存储 key，按用户区分
function getStorageKey() {
  return `${userStore.id}_${props.tipId}_v${config.value.version}`
}

// 当前 guideTip 是否显示
function isGuideTipShown() {
  if (!config.value.version) return true
  const storage = getGuideTipStorage()
  const key = getStorageKey()
  return !storage[key] || storage[key].status === 'shown'
}

// 更新 guideTip 状态
function setGuideTipStatus(status) {
  if (!config.value.version) return
  const storage = getGuideTipStorage()
  const key = getStorageKey()
  storage[key] = { status, timestamp: Date.now() }
  localStorage.setItem(STORAGE_KEY, JSON.stringify(storage))
}

// 激活时检查显示状态
function checkVisible() {
  visible.value = isGuideTipShown()
}

onMounted(() => {
  checkVisible()
})
onActivated(() => {
  checkVisible()
})

// 不再提醒
function neverShow() {
  setGuideTipStatus('hidden')
  visible.value = false
  // proxy.$modal.msgSuccess("随时可在【个人中心】→【新手引导】重新开启，不怕找不到！");
}

function close() {
  visible.value = false
}

// 点击内容处理
function handleClick(event) {
  if (event.target.tagName.toLowerCase() === 'a') return
  const funcName = event.target.dataset.func
  if (!funcName) return
  const link = event.target.dataset.link || ''
  const id = event.target.dataset.id
  const info = event.target.dataset.info
  const row = { id, info }
  if (methods[funcName] && typeof methods[funcName] === 'function') {
    methods[funcName](link, row)
  }
}

// 在组件内定义的方法
const methods = {
  routeTo(link, row) {
    if (link !== '' && link.indexOf('http') !== -1) {
      window.location.href = link
      return
    }
    if (link !== '') {
      if (link === router.currentRoute.value.path) {
        window.location.reload()
      } else {
        router.push({
          path: link,
          query: {
            id: row.id,
            info: row.info
          }
        })
      }
    }
  }
}
</script>
<style lang="less" scoped>
@font-title: 'PingFang SC', 'Microsoft YaHei';
@font-content: 'Microsoft YaHei';
@color-text-title: #000;
@color-text-content: #666;

.guide-tip {
  width: 100%;
  margin: 0 0 15px 0;
  padding: 8px 19px 8px 19px;
  position: relative;
  border-radius: 2px;

  &.remind {
    background-color: #fff7e6;
    border: 1px solid #FFE58F;
  }

  &.warning {
    background-color: #FCEAEA;
    border: 1px solid #FFACAE;
  }

  &.danger {
    background-color: #FFACAE;
    border: 1px solid #FE4F4F;
  }

  &.info {
    background-color: #fff;
    border: 1px solid #ddd;
  }

  .tip-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .tip-title {
      font-family: @font-title;
      font-weight: 600;
      font-size: 14px;
      color: @color-text-title;
      line-height: 24px;
      display: flex;
      align-items: center;

      .tip-icon {
        font-size: 16px;
        margin-right: 7px;
      }
    }

    .header-buttons {
      display: flex;
      margin-top: 2px;

      .el-button {
        height: 22px;
        font-family: PingFangSC-Regular, PingFangSC-Regular;
        font-weight: normal;
        font-size: 12px;
        line-height: 18px;
        text-align: left;
        text-transform: none;
        padding: 2px 4px;
        border-radius: 2px;

        &.btn-never-show {
          background-color: #fff;
          color: #565656;
          border: 1px solid #ffffff;
        }

        &.btn-close.remind {
          background-color: #F88825;
          color: #fff;
          border: none;
          padding: 4px 7px;
          margin-left: 9px;
        }

        &.btn-close.warning, &.btn-close.danger,  {
          background-color: #FE4F4F;
          color: #fff;
          border: none;
          padding: 4px 7px;
          margin-left: 9px;

        }
      }
    }
  }

  .tip-content {
    font-family: @font-content;
    font-weight: 400;
    font-size: 14px;
    color: @color-text-content;
    line-height: 22px;
    margin-top: 0px;
    padding-left: 23px;
    cursor: default;
  }
}
</style>
<style>
.tip-content a {
  color: var(--el-color-primary);
}
.clickable {
  color: var(--el-color-primary);
  cursor: pointer;
}
</style>
