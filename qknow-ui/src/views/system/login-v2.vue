<template>
  <div class="login-container">

    <div class="login-center">

      <div class="horizontal-layout">

        <!-- 左侧图片 -->
        <div class="layout-left"></div>

        <!-- 登陆区域 -->
        <div class="layout-right">
          <span class="login-title">凤巢协作系统</span>
          <el-form ref="loginRef" :model="loginForm" :rules="loginDataRules">
            <el-form-item prop="username">
              <div class="item-width">
                <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
                <div class="account-icon"></div>
              </div>
            </el-form-item>

            <el-form-item prop="password">
              <div class="item-width">
                <el-input show-password v-model="loginForm.password" placeholder="密码"></el-input>
                <div class="passwork-icon"></div>
              </div>
            </el-form-item>

            <el-form-item prop="code">
              <div class="item-width-min">
                <el-input  @keyup.enter="handleLogin" v-model="loginForm.code" maxlength="8" placeholder="验证码"></el-input>
                <img :src="codeUrl" @click="getCode" class="login-code"/>
              </div>
            </el-form-item>

          </el-form>

          <el-form-item>
            <div class="item-width-min">
              <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
            </div>
          </el-form-item>

          <el-button :loading="loading" @click.native.prevent="handleLogin" class="item-width" style="margin-top: 20px"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>

        </div>
      </div>

    </div>

    <div class="login-bottom">
      <div class="row-layout">
        {{ showCopyright() }}
        <img src="https://www.asktempo.com/statics/images/an.png" alt="" style="margin-left: 20px"/>
        <span style="margin-left: 5px">苏ICP备2022008519号-1</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {getCodeImg} from "@/api/system/login";
import useUserStore from '@/store/system/user.js'
import Cookies from "js-cookie";
import {encrypt, decrypt} from "@/utils/jsencrypt";
import moment from "moment/moment.js";

const {proxy} = getCurrentInstance();
const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
  code: "",
  uuid: ""
});
const codeUrl = ref("");
const loading = ref(false);
const captchaEnabled = ref(false);
const userStore = useUserStore();
const loginDataRules = {
  username: [{required: true, trigger: "blur", message: "请输入您的账号"}],
  password: [{required: true, trigger: "blur", message: "请输入您的密码"}],
  code: [{required: true, trigger: "change", message: "请输入验证码"}]
};

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

function showCopyright() {
  const currentYear = moment(new Date()).format("yyyy");
  return 'Copyright© ' + currentYear + ' 江苏千桐科技有限公司 版权所有';
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
.login-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  position: relative;
  background-color: #fefdfc;

  .login-center {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    position: absolute;
    width: 55vw;
    height: 70vh;
    background-color: #FFFFFF;
    border-radius: 5px;
    box-shadow: 0 0 5px 6px rgb(200, 200, 220, 0.2);
    z-index: 2;

    .horizontal-layout {
      width: 55vw;
      height: 70vh;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;

      .layout-left {
        display: flex;
        flex-direction: column;
        height: 100%;
        width: 22vw;
        background-image: url("@/assets/system/images/login_v2_banner.png");
        background-size: 100% 100%;
      }

      .layout-right {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
        flex: 1;


        .login-title {
          font-size: 34px;
          color:#ED8200;
          user-select: none;
          margin-bottom: 45px;
        }

        .item-width {
          width: 22vw;
          height: 45px;
        }

        .item-width-min {
          display: flex;
          flex-direction: row;
          align-items: center;
          width: 22vw;
          height: 45px;
          justify-content: space-between;

          .login-code {
            width: 150px;
            height: 45px;
            margin-left: 30px;
            background-color: #5c6873;
          }
        }

        .account-icon {
          width: 18px;
          height: 18px;
          background-image: url("@/assets/system/images/user_v2.png");
          background-size: 100% 100%;
          top: 50%;
          right: 5px;
          transform: translate(-50%, -50%);
          position: absolute;
        }

        .passwork-icon {
          width: 18px;
          height: 18px;
          background-image: url("@/assets/system/images/locked_v2.png");
          background-size: 100% 100%;
          top: 50%;
          right: 5px;
          transform: translate(-50%, -50%);
          position: absolute;
        }

      }

    }

  }

  .horizontal {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .login-bottom {
    width: 100vw;
    height: 200px;
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: center;
    font-size: 12px;
    padding-bottom: 15px;
    position: absolute;
    background-image: url("@/assets/system/images/login_bg_v2.png");
    bottom: 0;

    .row-layout{
      display: flex;
      flex-direction: row;
      align-items: center;
      color: #999999;
    }
  }

  :deep(.el-checkbox) {
    --el-checkbox-checked-input-border-color: #ED8200;
    --el-checkbox-checked-bg-color: #ED8200;
    --el-checkbox-checked-text-color: #333333;
  }

  :deep(.el-input) {
    --el-input-focus-border-color: #ED8200;
    height: 45px;
  }

  :deep(.el-button:hover) {
    background-color: #ED8200;
    --el-button-bg-color:#ED8200;
    color: #FFFFFF;
  }

  :deep(.el-button) {
    background-color: #ED8200;
    --el-button-bg-color: #ED8200;
    color: #FFFFFF;
  }

  :deep(.el-input__icon) {
    display: none;
  }

}
</style>
