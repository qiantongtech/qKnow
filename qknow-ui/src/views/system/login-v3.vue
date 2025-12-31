<!--
  Copyright © 2026 Qiantong Technology Co., Ltd.
  qKnow Knowledge Platform
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
  More information: https://qknow.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2026 江苏千桐科技有限公司
  qKnow 知识平台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qknow.qiantong.tech/business.html
-->

<template>
  <div class="login-v3-bg">
    <div class="login-left-container">
      <div class="title-content">
        <span class="login-desc">Hi,你好!</span>
        <span class="login-desc">欢迎进入冰凤管理框架</span>
        <span class="login-desc-en">Welcome to the Ice Phoenix Management Framework</span>
      </div>
    </div>

    <div class="login-right-container">
      <div class="login-content">
        <el-text type="primary" class="login-title">账号登录</el-text>
        <div class="titles-bar"></div>
        <el-form class="login-info" ref="loginRef" :model="loginForm" :rules="loginDataRules">
          <el-form-item class="item-box" prop="username">
            <el-input v-model="loginForm.username" style="height: 100%" placeholder="用户名">
              <template #prefix>
                <i class="iconfont">&#xeb44;</i>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="item-box" prop="password">
            <el-input
                v-model="loginForm.password"
                show-password
                style="height: 100%"
                placeholder="密码"
                class="input-item">
              <template #prefix>
                <i class="iconfont">&#xeb6f;</i>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="item-box" prop="code">
            <div class="code-container">
              <el-input
                  v-model="loginForm.code"
                  style="height: 100%"
                  placeholder="验证码"
                  maxlength="8"
                  @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <i class="iconfont">&#xeb4a;</i>
                </template>
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img"/>
              </div>
            </div>
          </el-form-item>
          <el-form-item class="item-box">
            <el-button class="login-button" type="primary" :loading="loading" @click.native.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
          <el-form-item class="item-box">
            <div class="item-passwork">
              <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
              <el-text type="primary" @click="dialogVisible = true">忘记密码</el-text>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 忘记密码 -->
        <el-dialog
            v-model="dialogVisible"
            title="忘记密码"
            class="fp-form-dialog"
            width="650px"
            draggable
            destroy-on-close
        >
          <el-form :model="fpForm" label-width="auto" style="padding: 10px 60px 0;">
            <el-form-item label="用户名">
              <el-input v-model="fpForm.name" placeholder="请输入手机号或用户名"/>
            </el-form-item>

            <el-form-item label="验证码">
              <div class="wrapper">
                <el-input v-model="fpForm.code" placeholder="请输入验证码"/>
                <el-button type="primary" :disabled="codeFlag" style="margin-left: 10px;" @click="handleFPCodeClick">
                  {{ codeFlag ? `${codeTime}s` : '获取验证码' }}
                </el-button>
              </div>
            </el-form-item>

            <el-form-item label="新密码">
              <el-input v-model="fpForm.password" placeholder="请输入新密码"/>
            </el-form-item>

            <el-form-item label="确认密码">
              <el-input v-model="fpForm.password2" placeholder="请输入确认密码"/>
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button type="primary" @click="dialogVisible = false">
                重置密码
              </el-button>
            </div>
          </template>
        </el-dialog>

  </div>
</template>
<script setup>
import {ref} from "vue";
import {getCodeImg} from "@/api/system/login";
import useUserStore from '@/store/system/user.js'
import Cookies from "js-cookie";
import {encrypt, decrypt} from "@/utils/jsencrypt";

const {proxy} = getCurrentInstance();
const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
  code: "",
  uuid: ""
});
const dialogVisible = ref(false);
const codeFlag = ref(false);
const timeValue = 3;
const codeTime = ref(timeValue)
const codeUrl = ref("");
const loading = ref(false);
const captchaEnabled = ref(false);
const userStore = useUserStore();
const loginDataRules = {
  username: [{required: true, trigger: "blur", message: "请输入您的账号"}],
  password: [{required: true, trigger: "blur", message: "请输入您的密码"}],
  code: [{required: true, trigger: "change", message: "请输入验证码"}]
};
const fpForm = ref({
  username: "",
  password: "",
  password2: "",
  code: ""
})

function handleFPCodeClick() {
  if (codeFlag.value) return
  codeFlag.value = !codeFlag.value;
  setInterval(() => {
    if (codeTime.value > 1) {
      codeTime.value--
    } else {
      clearInterval();
      codeTime.value = timeValue;
      codeFlag.value = false;
    }
  }, 1000)
}

function handleLogin() {
  localStorage.setItem("username", loginForm.value.username)
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, {expires: 30});
        Cookies.set("password", encrypt(loginForm.value.password), {expires: 30});
        Cookies.set("rememberMe", loginForm.value.rememberMe, {expires: 30});
      } else {
        // 否则移除
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("rememberMe");
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        window.location.href = "/index";
      }).catch(() => {
        loading.value = false;
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode();
        }
      });
    }
  });

}


function getCookie() {
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  };
}

getCookie();

//获取验证码
function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}

getCode()
</script>
<style scoped lang="scss">
.login-v3-bg {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100%;
  background: url("@/assets/system/images/login_bg_v3.jpeg");
  background-size: 100% 100%;


  .login-left-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    flex: 5;
    height: 60%;
    color: #ffffff;

    .title-content {
      display: flex;
      flex-direction: column;
      width: 50%;
      margin-left: 30%;
    }

    .login-desc {
      margin-top: 15px;
      font-size: 40px;
    }

    .login-desc-en {
      font-size: 20px;
      margin-top: 15px;
      align-items: center;
    }
  }

  .login-right-container {
    display: flex;
    flex: 4;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .login-content {
      width: 60%;
      height: 40%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: #f5f5f5;
      opacity: 0.92;
      border-radius: 15px;

      .login-title {
        font-size: 18px;
        text-align: center;
        margin-top: 15px;
        font-weight: bold;
      }

      .titles-bar {
        width: 65px;
        height: 2px;
        background: var(--el-color-primary);
        margin-top: 5px;
      }

      .login-info {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        width: 100%;
        flex: 1;

        .item-box {
          width: 80%;
          height: 40px;
        }

        .login-button {
          width: 100%;
          height: 45px;
          margin-top: 15px;
        }

        .item-passwork {
          margin-top: 15px;
          width: 100%;
          color: #2666fb;
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          cursor: pointer;

          .forget-passwork {
            cursor: pointer;
          }
        }

        .login-code-img {
          height: 100%;
          width: 102px;
        }

        .login-code {
          width: 104px;
          height: 40px;
          float: right;
          margin-left: 50px;
        }

        .code-container {
          width: 100%;
          display: flex;
          height: 40px;
          flex-direction: row;
          align-items: center;
        }
      }
    }
  }

  .fp-form-dialog {
    .wrapper {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .el-button {
        width: 102px;
        min-width: 102px;
      }
    }

    :deep(.el-dialog__body) {
      height: 316px !important;
    }
  }
}

.app-container {
  padding: 0px;
  margin: 0px;
  background-color: #FFFFFF;
  min-height: 100%;
}

</style>
