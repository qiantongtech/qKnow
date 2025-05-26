<template>
  <!-- 上次登录用户登录页面登录页面样式二 -->
  <div class="login-two sysInfo sysInfo-wrap">
    <div class="left-content">
      <div class="swiper leftSwiper">
        <div class="swiper-wrapper">
          <div
            class="swiper-slide swiper-slide-1"
          >
          </div>
          <el-carousel  style="width:100%;heght:100%;" arrow="never" autoplay>
            <el-carousel-item v-for="(item,index) in loginimglist" :key="index">
              <div :class="'swiper-slide swiper-slide-'+item.id"></div>
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="swiper-pagination"></div>
      </div>
    </div>
    <div class="right-content">
      <div class="logo">
        <img src="@/assets/system/images/sso_login/sso-logo.png" />
      </div>
      <div>
        <div class="greeting">
          <div class="entry_period">亲爱的朋友，{{ greetingsTitle }}！</div>
          <div class="entry_greeting">
            相信自己，你可以成为你想成为的人。
          </div>
        </div>
        <div class="login-panel">
          <el-form
            ref="loginRef" :model="loginForm" :rules="loginRules"
          >
            <p class="titles">账号登录</p>
            <div class="titles-bar"></div>
            <el-form-item prop="username">
              <el-input
              v-model="loginForm.username"
              type="text"
              auto-complete="off"
              placeholder="账号"
              >
                <template #prefix>
                  <i class="iconfont">&#xebc0;</i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
              v-model="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="密码"
              @keyup.enter="handleLogin"
              >
              <template #prefix>
                <i class="iconfont">&#xeb8d;</i>
              </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaEnabled">
              <el-input
                v-model="loginForm.code"
                auto-complete="off"
                placeholder="验证码"
                class="code-class"
                @keyup.enter.native="handleLogin"
              >
                <template #prefix>
                  <i class="iconfont">&#xeb9e;</i>
                </template>
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" />
              </div>
            </el-form-item>

            <el-form-item style="width: 100%">
              <el-button
                :loading="loading"
                type="primary"
                style="width: 100%"
                @click.native.prevent="handleLogin"
              >
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>

            <div class="form-actions">
              <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
              <el-text type="primary" @click="dialogVisible = true">忘记密码</el-text>
            </div>
          </el-form>
        </div>
      </div>
      <div>
        <div class="description">
          <div class="contact" style="float: left">
            <img src="@/assets/system/images/login/phone.png" />
            <div>
              <p>联系电话：</p>
              <p>400-660-8208</p>
            </div>
          </div>
          <div class="contact" style="margin-left: 24px">
            <img src="@/assets/system/images/login/email.png" />
            <div>
              <p>电子邮箱：</p>
              <p>sales@qiantong.tech</p>
            </div>
          </div>
        </div>
        <div class="chrome-wrap">
          <img
            src="@/assets/system/images/sso_login/chrome.png"
            style="height: 20px"
          />
          <span
            style="
              color: #999999;
              font-size: 12px;
              line-height: 0;
              margin-left: 10px;
            "
            >为保证最佳浏览效果，请使用</span
          >
          <span style="color: #ee2223; font-size: 12px; line-height: 0"
            >Chrome</span
          >
          <span style="color: #999999; font-size: 12px; line-height: 0"
            >浏览器，点击下载安装</span
          >
          <a
            href="https://ikm.yonyou.com/chromepath/ChromeStandaloneSetup64.exe"
          >
            <div
              style="
                margin-left: 15px;
                display: flex;
                flex-direction: column;
                align-items: center;
              "
            >
              <img
                id="window_img"
                src="@/assets/system/images/sso_login/win.svg"
                style="height: 25px"
              />
              <span
                style="
                  color: #999999;
                  font-size: 12px;
                  line-height: 0;
                  margin-top: 7px;
                "
                >Window</span
              >
            </div>
          </a>
          <a href="https://ikm.yonyou.com/chromepath/GoogleChromeForMac.dmg">
            <div
              style="
                margin-left: 15px;
                display: flex;
                flex-direction: column;
                align-items: center;
              "
            >
              <img
                id="mac_img"
                src="@/assets/system/images/sso_login/ios.svg"
                style="height: 25px"
              />
              <span
                style="
                  color: #999999;
                  font-size: 12px;
                  line-height: 0;
                  margin-top: 7px;
                "
                >Mac</span
              >
            </div>
          </a>
        </div>
        <div class="bottom-info">
          <div class="copy-right">
            Copyright© 2024 江苏干桐科技有限公司 版权所有
          </div>
          <div class="record" @click="goKtPage()">
            <img src="@/assets/system/images/sso_login/an.png" alt="" />
            &nbsp;&nbsp; 苏ICP备2022008519号-1
          </div>
        </div>
      </div>
    </div>
  </div>

  <el-dialog
    v-model="dialogVisible"
    title="忘记密码"
    class="fp-form-dialog"
    width="650px"
    :append-to="$refs['app-container']" 
  >
    <el-form :model="fpForm" label-width="auto" style="padding: 10px 60px 0;">
      <el-form-item label="用户名">
        <el-input v-model="fpForm.name" placeholder="请输入手机号或用户名" />
      </el-form-item>

      <el-form-item label="验证码">
        <div class="wrapper">
          <el-input v-model="fpForm.code" placeholder="请输入验证码" />
          <el-button type="primary" :disabled="codeFlag" style="margin-left: 10px;" @click="handleFPCodeClick">{{codeFlag?`${codeTime}s`:'获取验证码'}}</el-button>
        </div>
      </el-form-item>

      <el-form-item label="新密码">
        <el-input v-model="fpForm.password" placeholder="请输入新密码" />
      </el-form-item>

      <el-form-item label="确认密码">
        <el-input v-model="fpForm.password2" placeholder="请输入确认密码" />
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
  <!-- </div> -->
</template>

<script setup>
import { ref } from "vue";
import { getCodeImg, ssoLoginPage } from "@/api/system/sso-login-page.js";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import Swiper from "swiper";
import "swiper/swiper-bundle.min.css";
import useUserStore from '@/store/system/user.js'


const userStore = useUserStore();
const dialogVisible = ref(false);
const { proxy } = getCurrentInstance();
const loading = ref(false);
const codeUrl = ref("");
const greetingsTitle = ref("");
const captchaEnabled = ref(false);
const codeFlag = ref(false);
const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
  code: "",
  uuid: ""
});
const fpForm = ref({
  username:"",
  password:"",
  password2:"",
  code:""
})


const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
};

function judgeDate() {
  var currentTime = new Date();
  var currentHour = currentTime.getHours();
  if (currentHour < 12) {
    greetingsTitle.value = "上午好";
  } else if (currentHour < 18) {
    greetingsTitle.value = "下午好";
  } else {
    greetingsTitle.value = "晚上好";
  }
}
judgeDate();

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


function getCode() {
  getCodeImg().then(res => {
   res= {"msg":"操作成功","img":"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","code":200,"captchaEnabled":true,"uuid":"afa144bff52c495abe1d9a8b78a0da67"}
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}
getCode()

function generateOAuth2Link() {
  // 步骤 1: 获取当前 URL
  const currentUrl = new URL(window.location.href);

  // 步骤 2: 获取协议（http 或 https）和 IP/端口
  const { protocol, hostname, port } = window.location;
  const baseUrl = `${protocol}//${hostname}${port ? `:${port}` : ''}/oauth2/authorize`;

  // 步骤 3: 生成新的 URL
  const newUrl = `${baseUrl}?${currentUrl.searchParams.toString()}`;

  return newUrl;
}

function handleLogin(){
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 });
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 });
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 });
      } else {
        // 否则移除
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("rememberMe");
      }

      const username = loginForm.value.username.trim()
      const password = loginForm.value.password
      const code = loginForm.value.code
      const uuid = loginForm.value.uuid

      ssoLoginPage(username, password, loginForm.value.rememberMe).then(res => {
        console.log(res, "res")
        if (res.code === 200) {
          // 设置浏览器重定向到生成的新链接
          window.location.href = generateOAuth2Link();
        }
      })

      // 调用action的登录方法
      // userStore.login(loginForm.value).then(() => {
      //   window.location.href = "/index";
      // }).catch(() => {
      //   loading.value = false;
      //   // 重新获取验证码
      //   if (captchaEnabled.value) {
      //     getCode();
      //   }
      // });
    }
  });
}
const timeValue = 3;
const codeTime = ref(timeValue)
function handleFPCodeClick(){
  if(codeFlag.value) return
  codeFlag.value = !codeFlag.value;
  setInterval(()=>{
    if(codeTime.value>1){
      codeTime.value--
    }else{
      clearInterval();
      codeTime.value = timeValue;
      codeFlag.value = false;
    }
  },1000)
}
</script>

<style lang="scss">
.fp-form-dialog{
  .wrapper{
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .el-button{
      width: 102px;
      min-width: 102px;
    }
  }

  .el-dialog__body{
    height: 316px !important;
  }

  .el-button{
    background: #2666FB;
    border-radius: 2px 2px 2px 2px;
    border: 1px solid #2666FB;
  }
}
</style>

<style scoped lang="scss">
.login-two {
  height: 100%;
  width: 100%;
  min-width: 1300px;
  overflow: auto;
  display: flex;
  .swiper-slide-1{
    background: url(@/assets/system/images/sso_login/banner1.png) center center / cover no-repeat;
  }

  .form-actions{
    display: flex;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
  }

  ::v-deep .el-form-item__content {
    justify-content: space-between;
  }

  ::v-deep .el-input--medium .el-input__inner {
    height: 40px;
    line-height: 40px;
    padding-left: 35px;
    border-radius: 4px;
    border: 1px solid #e6e6e6;
  }
  ::v-deep .el-input__icon.input-icon.svg-icon {
    height: 45px;
    width: 14px;
    margin-left: 5px;
  }

  ::v-deep .el-form-item__error {
    color: #ff4949;
    font-size: 12px;
    line-height: 1;
    // padding-top: 9px;
    position: absolute;
    top: 100%;
    left: 0;
  }
  ::v-deep .el-checkbox {
    padding: 0 !important;
  }
  .login-code-img {
    height: 100%;
    width: 102px;
  }

  .login-code {
    width: 104px;
    height: 34px;
    float: right;
  }
  ::v-deep .el-form-item {
    margin-bottom: 24px;
  }


  ::v-deep .el-input__prefix {
    top: 1px !important;
    left: 15px !important;
  }


  .left-content {
    width: 55%;
    min-height: 750px;
    .contents {
      position: absolute;
      margin: 6% 0px 0px 13%;
      letter-spacing: 0em;
      font-family: "sharp";
      .title {
        font-size: 53px;
        margin-bottom: -5px;
        color: #000000;
      }
      .enTitle {
        font-size: 19px;
        margin-bottom: 70px;
        color: #000000;
      }
      .digest {
        font-size: 28px;
        color: #000000;
      }
      .content {
        width: 78%;
        font-size: 18px;
        line-height: 40px;
        color: #000000;
      }
    }
    .leftSwiper {
      width: 100%;
      height: 100%;
      .swiper-imagesize {
        width: 100%;
        height: 100%;
        // object-fit: cover;
      }
      ::v-deep .swiper-pagination-bullets .swiper-pagination-bullet {
        width: 36px;
        height: 4px;
        background: rgba(0, 0, 0, 0.5);
        border-radius: 3px;
        margin-bottom: 30px;
        //opacity: 0.5;
      }
    }
  }
  .iconfont {
    font-size: 17px !important;
  }
  .right-content {
    width: 45%;
    min-height: 750px;
    background: #fff;
    position: relative;
    padding: 30px 0 20px 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .logo {
      width: 300px;
      height: 75px;
      margin-left: 20%;
      img {
        // width: 100%;
        height: 100%;
      }
    }
    .greeting {
      margin-left: 20%;
      .entry_period {
        font-size: 25px;
        font-weight: 400;
        margin-bottom: 6px;
        color: rgba(57, 63, 79, 1);
      }
      .entry_greeting {
        font-size: 12px;
        font-weight: 400;
        color: #5c6873;
      }
    }
    .login-panel {
      margin-left: 20%;
      box-sizing: border-box;
      margin-top: 30px;
      width: 400px;
      height: 400px;
      box-shadow: -22px -22px 44px 0px rgba(255, 255, 255, 1),
        22px 22px 44px 0px rgba(217, 217, 217, 1);
      border-radius: 8px;
      padding: 0 38px;
      position: relative;
      overflow: hidden;

      ::v-deep .el-form-item {
        margin-bottom: 20px;
      }
      .titles {
        text-align: center;
        line-height: 65px;
        height: 65px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 30px;
      }
      .titles-bar {
        width: 65px;
        height: 2px;
        background: #2666fb;
        top: 66px;
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
      }
      .code-class {
        width: 63%;
      }
    }

    ::v-deep .el-button--medium {
      padding: 12px 20px;
      // margin-top: 10px;
    }
    .description {
      margin-top: 30px;
      margin-left: 15%;
      .contact {
        font-size: 14px;
        color: #666666;
        display: flex;
        align-items: center;
        height: 42px;
        line-height: 42px;
        width: 173px;
        img {
          width: 32px;
          height: 32px;
          margin-right: 16px;
        }
        p {
          height: 20px;
          line-height: 20px;
          font-size: 16px;
          // font-weight: 500px;
          color: #000000;
          margin: 0;
        }
        p:first-child {
          font-weight: 500px;
          font-size: 14px;
          color: #6d6d6d;
        }
      }
    }
    .chrome-wrap {
      display: flex;
      margin-left: 15%;
      align-items: center;
      margin-top: 8px;
    }
    .bottom-info {
      margin-top: 20px;
      margin-left: 15%;
      height: 17px;
      font-size: 12px;
      font-weight: 400px;
      left: 0px;
      color: #6d6d6d;
      line-height: 17px;
      display: flex;
      align-items: center;
      .record {
        cursor: pointer;
        margin-left: 20px;
        display: flex;
        align-items: center;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
@media screen and (max-width: 1280px) {
}
@media screen and (max-width: 992px) {
}
@media screen and (max-width: 768px) {
}
@media screen and (max-width: 576px) {
  .login-two {
    min-width: 300px;
    .left-content {
      display: none;
    }
    .right-content {
      width: 100%;
      min-height: 300px;
      justify-content: flex-start;
      align-items: center;
      padding: 20px 14px 20px 14px;
      .logo {
        margin-left: 0;
        text-align: center;
      }
      .greeting {
        margin-left: 0;
        margin-top: 20px;
        text-align: center;
        .entry_period {
          font-size: 16px;
        }
      }
      .login-panel {
        margin-left: 0;
        margin-top: 20px;
        width: auto;
        height: 360px;
        padding: 0 20px;
        box-shadow: 5px 5px 10px 2px #eee;
        .titles {
          margin: 0 0 20px 0;
        }
        .titles-bar {
          top: 48px;
        }
        .code-class {
          width: 55%;
        }
      }
      .description {
        width: 100%;
        margin-left: 0;
        margin-top: 10px;
        .contact {
          width: 48%;
          img {
            width: 28px;
            height: 28px;
            margin-right: 12px;
          }
          p {
            font-size: 12px;
          }
        }
      }
      .chrome-wrap {
        margin-left: 0;
        display: none;
      }
      .bottom-info {
        margin-left: 0;
        margin-top: 10px;
        flex-direction: column;
      }
    }
  }
}
</style>
