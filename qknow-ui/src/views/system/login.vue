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
    <!-- 上次登录用户登录页面登录页面样式二 -->
    <div class="login-two sysInfo sysInfo-wrap" ref="app-container">
        <div class="left-content">
            <div class="swiper leftSwiper">
                <div class="swiper-wrapper">
                    <!--          <el-carousel  style="width:100%;heght:100%;" arrow="never" autoplay>-->
                    <!--            <el-carousel-item v-for="(item,index) in loginimglist" :key="index">-->
                    <!--              <div :class="'swiper-slide swiper-slide-'+item.id"></div>-->
                    <!--            </el-carousel-item>-->
                    <!--          </el-carousel>-->

                    <el-carousel
                        style="width: 100%; height: 100%"
                        arrow="never"
                        autoplay
                        indicator-position="none"
                    >
                        <el-carousel-item v-for="(item, index) in loginimglist" :key="index">
                            <div class="swiper-slide" :style="getBackgroundStyle(item)"></div>
                        </el-carousel-item>
                    </el-carousel>

                    <div class="benefit-icons-layer" aria-hidden="true">
                        <div class="benefit-icon benefit-icon-efficiency">
                            <img :src="benefitEfficiencyIcon" alt="" />
                        </div>
                        <div class="benefit-icon benefit-icon-reliable benefit-icon-animated">
                            <img :src="benefitOpenIcon" alt="" />
                        </div>
                        <div class="benefit-icon benefit-icon-open benefit-icon-animated">
                            <img :src="benefitReliableIcon" alt="" />
                        </div>
                    </div>

                    <div class="login-effects-layer" aria-hidden="true">
                        <div class="effect-stage effect-dev">
                            <div class="dev-code dev-code-a">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                            <div class="dev-code dev-code-b">
                                <span></span>
                                <span></span>
                            </div>
                            <div class="dev-scan"></div>
                            <div class="effect-particle particle-a"></div>
                            <div class="effect-particle particle-b"></div>
                        </div>

                        <div class="effect-stage effect-workflow">
                            <div class="flow-node node-a"></div>
                            <div class="flow-node node-b"></div>
                            <div class="flow-node node-c"></div>
                            <div class="flow-line line-a"></div>
                            <div class="flow-line line-b"></div>
                            <div class="flow-pulse pulse-a"></div>
                            <div class="flow-pulse pulse-b"></div>
                            <div class="flow-base"></div>
                            <div class="flow-spark spark-a"></div>
                            <div class="flow-spark spark-b"></div>
                            <div class="flow-spark spark-c"></div>
                            <div class="module-platform workflow-platform">
                                <span></span>
                                <span></span>
                            </div>
                        </div>

                        <div class="effect-stage effect-rag">
                            <div class="rag-doc doc-a">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                            <div class="rag-doc doc-b">
                                <span></span>
                                <span></span>
                            </div>
                            <div class="rag-search"></div>
                            <div class="rag-ray ray-a"></div>
                            <div class="rag-ray ray-b"></div>
                        </div>

                        <div class="effect-stage effect-graph">
                            <div class="graph-link link-a"></div>
                            <div class="graph-link link-b"></div>
                            <div class="graph-link link-c"></div>
                            <div class="graph-link link-d"></div>
                            <div class="graph-link link-e"></div>
                            <div class="graph-link link-f"></div>
                            <div class="graph-node graph-a"></div>
                            <div class="graph-node graph-b"></div>
                            <div class="graph-node graph-c"></div>
                            <div class="graph-node graph-d"></div>
                            <div class="graph-node graph-e"></div>
                            <div class="graph-node graph-f"></div>
                            <div class="graph-node graph-g"></div>
                            <div class="graph-wave"></div>
                            <div class="module-platform graph-platform">
                                <span></span>
                                <span></span>
                            </div>
                        </div>

                        <div class="effect-stage effect-agent">
                            <div class="agent-core"></div>
                            <div class="agent-ring ring-a"></div>
                            <div class="agent-ring ring-b"></div>
                            <div class="agent-dot dot-a"></div>
                            <div class="agent-dot dot-b"></div>
                            <div class="agent-dot dot-c"></div>
                            <div class="agent-signal signal-a"></div>
                            <div class="agent-signal signal-b"></div>
                        </div>
                    </div>
                </div>
                <div class="swiper-pagination"></div>
            </div>
        </div>
        <div class="right-content">
            <div class="logo">
                <img :src="logo" />
            </div>
            <div>
                <div class="greeting">
                    <div class="entry_period">亲爱的朋友，{{ greetingsTitle }}！</div>
                    <div class="entry_greeting">
                        🌟 qKnow 智能体构建平台，让企业知识沉淀为智能能力。
                    </div>
                </div>
                <div class="login-panel">
                    <el-form ref="loginRef" :model="loginForm" :rules="loginRules">
                        <p class="titles">
                            <el-text type="primary" class="titles">账号登录</el-text>
                        </p>

                        <div class="titles-bar"></div>
                        <el-form-item prop="username">
                            <el-input
                                v-model="loginForm.username"
                                type="text"
                                auto-complete="off"
                                placeholder="账号"
                            >
                                <template #prefix>
                                    <i class="iconfont">&#xeb44;</i>
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
                                    <i class="iconfont">&#xeb6f;</i>
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
                                    <i class="iconfont">&#xeb4a;</i>
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
                        <!--            <img src="@/assets/system/images/login/phone.png"/>-->
                        <div class="contact-img">
                            <i class="iconfont icon-dianhua-xianxing"></i>
                        </div>
                        <div>
                            <p>联系电话：</p>
                            <!--              <p>400-660-8208</p>-->
                            <p>
                                {{
                                    contentDetail && contentDetail.contactNumber
                                        ? contentDetail.contactNumber
                                        : '400-660-8208'
                                }}
                            </p>
                        </div>
                    </div>
                    <div class="contact" style="padding-left: 24px">
                        <!--            <img src="@/assets/system/images/login/email.png"/>-->
                        <div class="contact-img">
                            <i class="iconfont icon-youjian-xianxing"></i>
                        </div>
                        <div>
                            <p>电子邮箱：</p>
                            <!--              <p>support@qiantong.tech</p>-->
                            <p>
                                {{
                                    contentDetail && contentDetail.email
                                        ? contentDetail.email
                                        : 'support@qiantong.tech'
                                }}
                            </p>
                        </div>
                    </div>
                </div>
                <div class="chrome-wrap">
                    <img src="@/assets/system/images/login/goge-icon.png" style="height: 20px" />
                    <span style="color: #888; font-size: 12px; line-height: 0; margin-left: 10px"
                        >为保证最佳浏览效果，请使用</span
                    >
                    <span style="color: #ee2223; font-size: 12px; line-height: 0">Chrome</span>
                    <span style="color: #888; font-size: 12px; line-height: 0"
                        >浏览器，点击下载安装</span
                    >
                    <a href="https://www.google.cn/chrome/" target="_blank">
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
                                src="@/assets/system/images/login/window-icon.svg"
                                style="height: 25px"
                            />
                            <span
                                style="
                                    color: #888;
                                    font-size: 12px;
                                    line-height: 0;
                                    margin-top: 7px;
                                "
                                >Window</span
                            >
                        </div>
                    </a>
                    <a href="https://www.google.cn/chrome/" target="_blank">
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
                                src="@/assets/system/images/login/mac-icon.svg"
                                style="height: 25px"
                            />
                            <span
                                style="
                                    color: #888;
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
                        Copyright© 2026
                        <a href="https://qiantong.tech" target="_blank">江苏千桐科技有限公司</a>
                        版权所有
                        <!-- {{ contentDetail && contentDetail.copyright ? contentDetail.copyright : `Copyright© ${parseTime(new Date(), "{y}")} 江苏千桐科技有限公司 版权所有` }}-->
                    </div>
                    <div class="record" @click="goKtPage()">
                        <img src="https://www.asktempo.com/statics/images/an.png" alt="" />
                        <!--            &nbsp;&nbsp; 苏ICP备2022008519号-3-->
                        &nbsp;&nbsp;
                        {{
                            contentDetail && contentDetail.recordNumber
                                ? contentDetail.recordNumber
                                : '苏ICP备2022008519号-3'
                        }}
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
        draggable
        destroy-on-close
    >
        <el-form :model="fpForm" label-width="auto" style="padding: 10px 60px 0">
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="用户名">
                        <el-input v-model="fpForm.name" placeholder="请输入手机号或用户名" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="验证码">
                        <div class="wrapper">
                            <el-input v-model="fpForm.code" placeholder="请输入验证码" />
                            <el-button
                                type="primary"
                                :disabled="codeFlag"
                                style="margin-left: 10px"
                                @click="handleFPCodeClick"
                            >
                                {{ codeFlag ? `${codeTime}s` : '获取验证码' }}
                            </el-button>
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="新密码">
                        <el-input v-model="fpForm.password" placeholder="请输入新密码" />
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="确认密码">
                        <el-input v-model="fpForm.password2" placeholder="请输入确认密码" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false"> 重置密码 </el-button>
            </div>
        </template>
    </el-dialog>
    <!-- </div> -->
</template>

<script setup>
    import { ref } from 'vue';
    import { getCodeImg } from '@/api/system/login';
    import Cookies from 'js-cookie';
    import { encrypt, decrypt } from '@/utils/jsencrypt';
    import Swiper from 'swiper';
    import 'swiper/swiper-bundle.min.css';
    import useUserStore from '@/store/system/user.js';
    import { getContent } from '@/api/system/system/content';
    import defaultLogo from '@/assets/system/images/login/logo.png';
    // 使用 import 直接引入图片路径
    import banner1 from '@/assets/system/images/login/banner.png';
    import banner2 from '@/assets/system/images/login/banner2.png';
    import banner3 from '@/assets/system/images/login/banner-gy.jpg';
    import banner4 from '@/assets/system/images/login/banner-sl.png';
    import benefitEfficiencyIcon from '@/assets/system/images/login/hj.gif';
    import benefitReliableIcon from '@/assets/system/images/login/kf.png';
    import benefitOpenIcon from '@/assets/system/images/login/kk.png';

    const userStore = useUserStore();
    const dialogVisible = ref(false);
    const { proxy } = getCurrentInstance();
    const loading = ref(false);
    const codeUrl = ref('');
    const greetingsTitle = ref('');
    const captchaEnabled = ref(true);
    const codeFlag = ref(false);
    const loginForm = ref({
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: ''
    });
    const fpForm = ref({
        username: '',
        password: '',
        password2: '',
        code: ''
    });
    // const loginimglist=ref([
    //   {
    //     id:1,
    //     imgurl:'banner.png',
    //   },
    //   // {
    //   //   id:2,
    //   //   imgurl:'banner2.png',
    //   // },
    //   // {
    //   //   id:3,
    //   //   imgurl:'banner-gy.png',
    //   // },
    //   {
    //     id:4,
    //     imgurl:'banner-sl.png',
    //   }
    // ])
    const defaltImglist = ref([
        {
            id: 1,
            image: new URL('@/assets/system/images/login/banner.png', import.meta.url).href
        }
        // {id: 4, image: new URL('@/assets/system/images/login/banner-sl.png', import.meta.url).href}
    ]);
    const loginimglist = ref([]);

    const getBackgroundStyle = (item) => {
        return {
            background: `url(${item.image}) no-repeat`,
            backgroundSize: `100% 100%`
        };
    };

    const getAssetsFile = (url) => {
        return new URL(`@/assets/system/images/login/${url}`, import.meta.url).href;
    };

    const loginRules = {
        username: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
        password: [{ required: true, trigger: 'blur', message: '请输入您的密码' }],
        code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
    };

    const logo = ref(null);
    const contentDetail = ref(null);

    onMounted(() => {
        fetchContent();
    });
    // 使用 getContent 来获取数据，而不是重新定义一个 getContent 函数
    const fetchContent = async () => {
        // logo.value = defaultLogo;
        // loginimglist.value = defaltImglist.value
        try {
            // 调用你从 API 导入的 getContent 方法
            const res = await getContent(1); // 假设请求的是 id 为 1 的数据
            if (res.code == 200) {
                const data = res.data;
                contentDetail.value = data;
                const sysLogo = data.loginLogo;
                logo.value = sysLogo ? sysLogo : defaultLogo;
                const carouselImageList = data.carouselImage.split(',');
                // console.log('-----contentDetail',contentDetail.value)
                // console.log('-----login-----0----0--0--------0-',carouselImageList)
                const carouselImgList = [];
                for (let i = 0; i <= carouselImageList.length; i++) {
                    let item = carouselImageList[i];
                    if (item) {
                        carouselImgList.push({
                            id: i + 1,
                            image: item
                        });
                    }
                }
                // console.log('-----login-----1----1--1--------1-',carouselImgList)
                if (carouselImgList.length > 0) {
                    loginimglist.value = carouselImgList;
                } else {
                    loginimglist.value = defaltImglist.value;
                }
            } else {
                loginimglist.value = defaltImglist.value;
            }

            // this.$message.success('内容加载成功');
        } catch (error) {
            logo.value = defaultLogo;
            loginimglist.value = defaltImglist.value;
        }
    };

    function judgeDate() {
        var currentTime = new Date();
        var currentHour = currentTime.getHours();
        if (currentHour < 12) {
            greetingsTitle.value = '上午好';
        } else if (currentHour < 18) {
            greetingsTitle.value = '下午好';
        } else {
            greetingsTitle.value = '晚上好';
        }
    }

    judgeDate();

    function getCookie() {
        const username = Cookies.get('username');
        const password = Cookies.get('password');
        const rememberMe = Cookies.get('rememberMe');
        loginForm.value = {
            username: username === undefined ? loginForm.value.username : username,
            password: password === undefined ? loginForm.value.password : decrypt(password),
            rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        };
    }

    getCookie();

    function getCode() {
        getCodeImg().then((res) => {
            captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
            if (captchaEnabled.value) {
                codeUrl.value = 'data:image/gif;base64,' + res.img;
                loginForm.value.uuid = res.uuid;
            }
        });
    }

    getCode();

    function handleLogin() {
        localStorage.setItem('username', loginForm.value.username);
        proxy.$refs.loginRef.validate((valid) => {
            if (valid) {
                loading.value = true;
                // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
                if (loginForm.value.rememberMe) {
                    Cookies.set('username', loginForm.value.username, { expires: 30 });
                    Cookies.set('password', encrypt(loginForm.value.password), {
                        expires: 30
                    });
                    Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 });
                } else {
                    // 否则移除
                    Cookies.remove('username');
                    Cookies.remove('password');
                    Cookies.remove('rememberMe');
                }
                // 调用action的登录方法
                userStore
                    .login(loginForm.value)
                    .then(() => {
                        window.location.href = '/index';
                    })
                    .catch(() => {
                        loading.value = false;
                        // 重新获取验证码
                        if (captchaEnabled.value) {
                            getCode();
                        }
                    });
            }
        });
    }

    const timeValue = 3;
    const codeTime = ref(timeValue);

    function handleFPCodeClick() {
        if (codeFlag.value) return;
        codeFlag.value = !codeFlag.value;
        setInterval(() => {
            if (codeTime.value > 1) {
                codeTime.value--;
            } else {
                clearInterval();
                codeTime.value = timeValue;
                codeFlag.value = false;
            }
        }, 1000);
    }

    function goPage() {
        window.open('https://qiantong.tech/', '_blank'); // 在新窗口打开链接
    }

    //点击备案号调整工信部
    function goKtPage() {
        window.open('https://beian.miit.gov.cn/#/Integrated/index', '_blank'); // 在新窗口打开链接
    }
</script>

<style lang="scss">
    .el-carousel__button {
        background-color: #2666fb;
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

        .el-dialog__body {
            height: 316px !important;
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

        ::v-deep {
            .el-carousel__container {
                height: 100%;
            }
        }

        .swiper-slide {
        }

        /*  .swiper-slide-1{
      background: url(@/assets/system/images/login/banner.png) center center / cover no-repeat;
    }
    .swiper-slide-2{
      background: url(@/assets/system/images/login/banner2.png) center center / cover no-repeat;
    }
    .swiper-slide-3{
      background: url(@/assets/system/images/login/banner-gy.jpg) center center / cover no-repeat;
    }
    .swiper-slide-4{
      background: url(@/assets/system/images/login/banner-sl.png) center center / cover no-repeat;
    }*/

        .form-actions {
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
                font-family: 'sharp';

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
                position: relative;
                overflow: hidden;

                .swiper-wrapper {
                    position: relative;
                    height: 100%;
                }

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
                width: 260px;
                height: 45px;
                margin-left: 20%;

                img {
                    // width: 100%;
                    height: 100%;
                    margin-top: 25px;
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
                    color: #888;
                }
            }

            .login-panel {
                margin-left: 20%;
                box-sizing: border-box;
                margin-top: 30px;
                width: 400px;
                height: 400px;
                box-shadow:
                    -22px -22px 44px 0px rgba(255, 255, 255, 1),
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
                    font-size: 18px;
                    font-weight: bold;
                    margin-bottom: 30px;
                }

                .titles-bar {
                    width: 65px;
                    height: 2px;
                    background: var(--el-color-primary);
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
                    //width: 173px;

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

            .contact-img {
                margin-right: 16px;
                display: flex;
                width: 32px;
                height: 32px;
                align-items: center;
                justify-content: center;
                border-radius: 16px;
                color: #ffffff;
                background-color: var(--el-color-primary);
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

    .app-container {
        padding: 0px;
        margin: 0px;
        background-color: #ffffff;
        min-height: 100%;
    }

    .benefit-icons-layer {
        position: absolute;
        inset: 0;
        z-index: 3;
        pointer-events: none;
        overflow: hidden;
    }

    .benefit-icon {
        position: absolute;
        width: 6.7%;
        aspect-ratio: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        transform: translate(-50%, -50%);

        img {
            display: block;
            width: 62%;
            height: 62%;
            object-fit: contain;
        }
    }

    .benefit-icon-efficiency {
        left: 16.5%;
        top: 84.6%;

        img {
            transform: translate(-4%, -2%);
        }
    }

    .benefit-icon-reliable {
        left: 44.6%;
        top: 85%;

        img {
            transform: translate(0, 3%);
        }
    }

    .benefit-icon-open {
        left: 72.2%;
        top: 84.7%;

        img {
            transform: translate(0, 6%);
        }
    }

    .benefit-icon-animated {
        animation: benefitIconFloat 3s ease-in-out infinite;
    }

    .benefit-icon-open {
        animation-delay: -1.2s;
    }

    .login-effects-layer {
        position: absolute;
        inset: 0;
        z-index: 2;
        pointer-events: none;
        overflow: hidden;
    }

    .effect-stage {
        position: absolute;
        transform: translate(-50%, -50%) translateZ(0);
        transform-origin: center;
        animation: effectBreath 4s ease-in-out infinite;
        will-change: transform, opacity;

        &::before {
            content: '';
            position: absolute;
            inset: 10% 4%;
            border-radius: 50%;
            background: radial-gradient(
                circle,
                rgba(39, 133, 255, 0.34),
                rgba(39, 133, 255, 0.08) 42%,
                transparent 70%
            );
            filter: blur(8px);
            opacity: 0.82;
            animation: effectGlow 3.2s ease-in-out infinite;
        }

        &::after {
            content: '';
            position: absolute;
            left: 6%;
            right: 6%;
            top: 0;
            height: 34%;
            border-radius: 50%;
            background: linear-gradient(
                180deg,
                rgba(255, 255, 255, 0.7),
                rgba(46, 134, 255, 0.16),
                transparent
            );
            opacity: 0.65;
            transform: translateY(-45%);
            animation: scanDown 3.8s ease-in-out infinite;
        }
    }

    .effect-dev {
        left: 12.8%;
        top: 48.8%;
        width: 12.4%;
        height: 18.8%;
    }

    .effect-workflow {
        left: 32.5%;
        top: 47.4%;
        width: 12.8%;
        height: 19%;
        animation-delay: -0.6s;
    }

    .effect-rag {
        left: 51.6%;
        top: 46.2%;
        width: 12%;
        height: 18.4%;
        animation-delay: -1.2s;
    }

    .effect-graph {
        left: 69.5%;
        top: 44.8%;
        width: 12.8%;
        height: 19.4%;
        animation-delay: -1.8s;
    }

    .effect-agent {
        left: 87.9%;
        top: 43.2%;
        width: 12.2%;
        height: 18.2%;
        animation-delay: -2.4s;
    }

    .dev-code {
        position: absolute;
        z-index: 1;
        width: 54%;
        padding: 7% 8%;
        border: 1px solid rgba(84, 160, 255, 0.42);
        border-radius: 8px;
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.86), rgba(104, 171, 255, 0.18));
        box-shadow: 0 0 18px rgba(47, 127, 255, 0.16);
        animation: codeFloat 3.4s ease-in-out infinite;

        span {
            display: block;
            height: 3px;
            margin-bottom: 6px;
            border-radius: 999px;
            background: linear-gradient(90deg, #2d74ff, rgba(72, 170, 255, 0.18));
            animation: codeTyping 2.6s ease-in-out infinite;
        }

        span:nth-child(2) {
            width: 76%;
            animation-delay: -0.5s;
        }

        span:nth-child(3) {
            width: 58%;
            animation-delay: -1s;
        }
    }

    .dev-code-a {
        left: 14%;
        top: 18%;
    }

    .dev-code-b {
        right: 10%;
        bottom: 20%;
        width: 48%;
        animation-delay: -1.5s;
    }

    .dev-scan {
        position: absolute;
        z-index: 2;
        left: 18%;
        top: 16%;
        width: 64%;
        height: 68%;
        border: 1px solid rgba(42, 138, 255, 0.34);
        border-radius: 12px;
        box-shadow: inset 0 0 18px rgba(42, 138, 255, 0.16);
        animation: framePulse 2.4s ease-in-out infinite;
    }

    .effect-particle {
        position: absolute;
        z-index: 3;
        width: 7px;
        height: 7px;
        border-radius: 50%;
        background: #2c7cff;
        box-shadow: 0 0 12px rgba(44, 124, 255, 0.9);
    }

    .particle-a {
        left: 20%;
        top: 24%;
        animation: particleDriftA 3.6s ease-in-out infinite;
    }

    .particle-b {
        right: 18%;
        bottom: 26%;
        animation: particleDriftB 3.2s ease-in-out infinite;
    }

    .flow-node {
        position: absolute;
        z-index: 2;
        width: 20%;
        aspect-ratio: 1;
        border-radius: 50%;
        background: linear-gradient(145deg, #ffffff, #5fb4ff 48%, #1d65ff);
        box-shadow: 0 0 20px rgba(37, 112, 255, 0.42);
        animation: nodePulse 2.2s ease-in-out infinite;
    }

    .node-a {
        left: 12%;
        top: 20%;
    }

    .node-b {
        left: 42%;
        top: 41%;
        animation-delay: -0.7s;
    }

    .node-c {
        right: 12%;
        bottom: 18%;
        animation-delay: -1.4s;
    }

    .flow-line {
        position: absolute;
        z-index: 1;
        height: 3px;
        border-radius: 999px;
        background: linear-gradient(90deg, transparent, rgba(39, 127, 255, 0.8), transparent);
        transform-origin: left center;
    }

    .line-a {
        left: 26%;
        top: 36%;
        width: 38%;
        transform: rotate(24deg);
    }

    .line-b {
        left: 55%;
        top: 58%;
        width: 34%;
        transform: rotate(24deg);
    }

    .flow-pulse {
        position: absolute;
        z-index: 3;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #ffffff;
        box-shadow: 0 0 14px #1e7dff;
    }

    .pulse-a {
        animation: flowPulseA 2.4s ease-in-out infinite;
    }

    .pulse-b {
        animation: flowPulseB 2.4s ease-in-out infinite 1.2s;
    }

    .flow-base {
        position: absolute;
        z-index: 0;
        left: 6%;
        right: 6%;
        bottom: -24%;
        height: 22%;
        border-radius: 50%;
        background: radial-gradient(
            ellipse at center,
            rgba(42, 138, 255, 0.36),
            rgba(42, 138, 255, 0.08) 52%,
            transparent 72%
        );
        filter: blur(2px);
        animation: flowBaseBreath 2.8s ease-in-out infinite;
    }

    .flow-spark {
        position: absolute;
        z-index: 3;
        bottom: 5%;
        width: 5px;
        height: 5px;
        border-radius: 50%;
        background: #ffffff;
        box-shadow: 0 0 10px rgba(36, 119, 255, 0.95);
        animation: flowSpark 2.6s ease-in-out infinite;
    }

    .spark-a {
        left: 24%;
    }

    .spark-b {
        left: 50%;
        animation-delay: -0.8s;
    }

    .spark-c {
        right: 23%;
        animation-delay: -1.6s;
    }

    .module-platform {
        position: absolute;
        z-index: 1;
        left: 12%;
        right: 12%;
        bottom: 1%;
        height: 24%;
        border-radius: 50%;
        background: radial-gradient(
            ellipse at center,
            rgba(255, 255, 255, 0.86) 0 18%,
            rgba(86, 169, 255, 0.42) 34%,
            rgba(34, 118, 255, 0.2) 52%,
            transparent 72%
        );
        box-shadow: 0 14px 24px rgba(36, 112, 255, 0.18);
        filter: drop-shadow(0 0 12px rgba(43, 128, 255, 0.28));
        animation: platformBreath 3s ease-in-out infinite;

        &::before,
        &::after {
            content: '';
            position: absolute;
            left: 10%;
            right: 10%;
            border-radius: 50%;
            border: 1px solid rgba(63, 143, 255, 0.32);
        }

        &::before {
            top: 15%;
            height: 38%;
        }

        &::after {
            top: 32%;
            height: 46%;
            opacity: 0.62;
        }

        span {
            position: absolute;
            left: 14%;
            right: 14%;
            height: 3px;
            border-radius: 999px;
            background: linear-gradient(90deg, transparent, rgba(44, 124, 255, 0.65), transparent);
            animation: platformLine 2.4s ease-in-out infinite;
        }

        span:first-child {
            top: 34%;
        }

        span:last-child {
            top: 53%;
            animation-delay: -1.2s;
        }
    }

    .workflow-platform {
        left: -2%;
        right: -2%;
        bottom: -34%;
        height: 31%;
    }

    .graph-platform {
        left: -1%;
        right: -1%;
        bottom: -27%;
        height: 31%;
    }

    .rag-doc {
        position: absolute;
        z-index: 1;
        width: 34%;
        height: 44%;
        border-radius: 8px;
        border: 1px solid rgba(77, 149, 255, 0.36);
        background: linear-gradient(180deg, rgba(255, 255, 255, 0.92), rgba(99, 169, 255, 0.22));
        box-shadow: 0 10px 24px rgba(34, 108, 255, 0.16);
        animation: docFloat 3.6s ease-in-out infinite;

        span {
            display: block;
            height: 3px;
            margin: 15% 16% 0;
            border-radius: 999px;
            background: rgba(39, 111, 255, 0.62);
        }

        span:nth-child(2) {
            width: 56%;
        }

        span:nth-child(3) {
            width: 42%;
        }
    }

    .doc-a {
        left: 12%;
        top: 16%;
    }

    .doc-b {
        right: 13%;
        bottom: 18%;
        animation-delay: -1.4s;
    }

    .rag-search {
        position: absolute;
        z-index: 3;
        left: 35%;
        top: 34%;
        width: 28%;
        aspect-ratio: 1;
        border: 4px solid rgba(38, 102, 251, 0.82);
        border-radius: 50%;
        box-shadow: 0 0 22px rgba(38, 102, 251, 0.42);
        animation: searchBeat 2.8s ease-in-out infinite;

        &::after {
            content: '';
            position: absolute;
            right: -28%;
            bottom: -10%;
            width: 42%;
            height: 4px;
            border-radius: 999px;
            background: rgba(38, 102, 251, 0.82);
            transform: rotate(45deg);
            transform-origin: left center;
        }
    }

    .rag-ray {
        position: absolute;
        z-index: 2;
        height: 2px;
        border-radius: 999px;
        background: linear-gradient(90deg, transparent, rgba(32, 121, 255, 0.84), transparent);
        animation: rayFlash 2.4s ease-in-out infinite;
    }

    .ray-a {
        left: 20%;
        top: 42%;
        width: 60%;
        transform: rotate(18deg);
    }

    .ray-b {
        left: 18%;
        top: 58%;
        width: 62%;
        transform: rotate(-22deg);
        animation-delay: -1.2s;
    }

    .graph-link {
        position: absolute;
        z-index: 1;
        height: 2px;
        border-radius: 999px;
        background: linear-gradient(
            90deg,
            rgba(49, 128, 255, 0.1),
            rgba(49, 128, 255, 0.72),
            rgba(49, 128, 255, 0.1)
        );
        transform-origin: left center;
        animation: linkGlow 2.8s ease-in-out infinite;
    }

    .link-a {
        left: 25%;
        top: 34%;
        width: 48%;
        transform: rotate(26deg);
    }

    .link-b {
        left: 28%;
        top: 58%;
        width: 48%;
        transform: rotate(-28deg);
        animation-delay: -0.8s;
    }

    .link-c {
        left: 43%;
        top: 42%;
        width: 34%;
        transform: rotate(90deg);
        animation-delay: -1.6s;
    }

    .link-d {
        left: 21%;
        top: 44%;
        width: 38%;
        transform: rotate(8deg);
        animation-delay: -0.4s;
    }

    .link-e {
        left: 47%;
        top: 38%;
        width: 36%;
        transform: rotate(42deg);
        animation-delay: -1.2s;
    }

    .link-f {
        left: 45%;
        top: 66%;
        width: 31%;
        transform: rotate(-12deg);
        animation-delay: -2s;
    }

    .graph-node {
        position: absolute;
        z-index: 2;
        width: 18%;
        aspect-ratio: 1;
        border-radius: 50%;
        border: 2px solid rgba(255, 255, 255, 0.9);
        background: radial-gradient(circle at 32% 28%, #ffffff, #55bcff 36%, #286bff 72%);
        box-shadow: 0 0 18px rgba(43, 116, 255, 0.56);
        animation: graphNodePulse 2.6s ease-in-out infinite;
    }

    .graph-a {
        left: 16%;
        top: 24%;
    }

    .graph-b {
        right: 14%;
        top: 32%;
        animation-delay: -0.5s;
    }

    .graph-c {
        left: 28%;
        bottom: 18%;
        animation-delay: -1s;
    }

    .graph-d {
        right: 22%;
        bottom: 16%;
        animation-delay: -1.5s;
    }

    .graph-e,
    .graph-f,
    .graph-g {
        width: 12%;
        border-width: 1px;
    }

    .graph-e {
        left: 40%;
        top: 14%;
        animation-delay: -0.25s;
    }

    .graph-f {
        right: 10%;
        top: 58%;
        animation-delay: -1.25s;
    }

    .graph-g {
        left: 12%;
        bottom: 34%;
        animation-delay: -2.1s;
    }

    .graph-wave {
        position: absolute;
        z-index: 0;
        left: 22%;
        top: 28%;
        width: 56%;
        aspect-ratio: 1;
        border: 1px solid rgba(49, 128, 255, 0.28);
        border-radius: 50%;
        animation: waveExpand 2.6s ease-out infinite;
    }

    .agent-core {
        position: absolute;
        z-index: 3;
        left: 34%;
        top: 30%;
        width: 32%;
        aspect-ratio: 1;
        border-radius: 50%;
        background: radial-gradient(
            circle at center,
            #ffffff 0 18%,
            #61c4ff 20% 48%,
            #236cff 50% 100%
        );
        box-shadow: 0 0 26px rgba(35, 108, 255, 0.72);
        animation: corePulse 2.2s ease-in-out infinite;
    }

    .agent-ring {
        position: absolute;
        left: 22%;
        top: 18%;
        width: 56%;
        aspect-ratio: 1;
        border-radius: 50%;
        border: 2px solid rgba(44, 124, 255, 0.46);
        animation: agentSpin 4s linear infinite;

        &::before {
            content: '';
            position: absolute;
            right: 7%;
            top: 9%;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background: #ffffff;
            box-shadow: 0 0 12px #227cff;
        }
    }

    .ring-b {
        left: 13%;
        top: 9%;
        width: 74%;
        border-style: dashed;
        animation-duration: 6s;
        animation-direction: reverse;
    }

    .agent-dot {
        position: absolute;
        z-index: 2;
        width: 9px;
        height: 9px;
        border-radius: 50%;
        background: #2a78ff;
        box-shadow: 0 0 14px rgba(42, 120, 255, 0.9);
        animation: decisionDot 2.8s ease-in-out infinite;
    }

    .dot-a {
        left: 16%;
        top: 24%;
    }

    .dot-b {
        right: 14%;
        top: 28%;
        animation-delay: -0.9s;
    }

    .dot-c {
        left: 44%;
        bottom: 14%;
        animation-delay: -1.8s;
    }

    .agent-signal {
        position: absolute;
        z-index: 1;
        left: 26%;
        width: 48%;
        height: 2px;
        border-radius: 999px;
        background: linear-gradient(90deg, transparent, rgba(44, 124, 255, 0.8), transparent);
        animation: signalSweep 2.4s ease-in-out infinite;
    }

    .signal-a {
        top: 38%;
        transform: rotate(24deg);
    }

    .signal-b {
        top: 58%;
        transform: rotate(-24deg);
        animation-delay: -1.2s;
    }

    @keyframes effectBreath {
        0%,
        100% {
            opacity: 0.84;
            transform: translate(-50%, -50%) scale(0.985);
        }

        50% {
            opacity: 1;
            transform: translate(-50%, -50%) scale(1.035);
        }
    }

    @keyframes effectGlow {
        0%,
        100% {
            opacity: 0.42;
            transform: scale(0.86);
        }

        50% {
            opacity: 0.92;
            transform: scale(1.1);
        }
    }

    @keyframes scanDown {
        0% {
            opacity: 0;
            transform: translateY(-46%);
        }

        45% {
            opacity: 0.72;
        }

        100% {
            opacity: 0;
            transform: translateY(245%);
        }
    }

    @keyframes codeFloat {
        0%,
        100% {
            transform: translateY(0);
        }

        50% {
            transform: translateY(-8%);
        }
    }

    @keyframes codeTyping {
        0%,
        100% {
            width: 42%;
            opacity: 0.5;
        }

        50% {
            width: 86%;
            opacity: 1;
        }
    }

    @keyframes framePulse {
        0%,
        100% {
            opacity: 0.4;
            transform: scale(0.96);
        }

        50% {
            opacity: 1;
            transform: scale(1.04);
        }
    }

    @keyframes particleDriftA {
        0%,
        100% {
            transform: translate(0, 0);
        }

        50% {
            transform: translate(48px, 70px);
        }
    }

    @keyframes particleDriftB {
        0%,
        100% {
            transform: translate(0, 0);
        }

        50% {
            transform: translate(-52px, -58px);
        }
    }

    @keyframes nodePulse {
        0%,
        100% {
            transform: scale(0.92);
            opacity: 0.78;
        }

        50% {
            transform: scale(1.12);
            opacity: 1;
        }
    }

    @keyframes flowPulseA {
        0% {
            left: 17%;
            top: 28%;
            opacity: 0;
        }

        20%,
        80% {
            opacity: 1;
        }

        100% {
            left: 49%;
            top: 47%;
            opacity: 0;
        }
    }

    @keyframes flowPulseB {
        0% {
            left: 47%;
            top: 48%;
            opacity: 0;
        }

        20%,
        80% {
            opacity: 1;
        }

        100% {
            left: 76%;
            top: 70%;
            opacity: 0;
        }
    }

    @keyframes flowBaseBreath {
        0%,
        100% {
            opacity: 0.36;
            transform: scaleX(0.82);
        }

        50% {
            opacity: 0.92;
            transform: scaleX(1.08);
        }
    }

    @keyframes flowSpark {
        0%,
        100% {
            opacity: 0.22;
            transform: translateY(10px) scale(0.75);
        }

        45% {
            opacity: 1;
            transform: translateY(-8px) scale(1.25);
        }
    }

    @keyframes platformBreath {
        0%,
        100% {
            opacity: 0.72;
            transform: scale(0.94);
        }

        50% {
            opacity: 1;
            transform: scale(1.05);
        }
    }

    @keyframes platformLine {
        0%,
        100% {
            opacity: 0.2;
            transform: scaleX(0.62);
        }

        50% {
            opacity: 0.95;
            transform: scaleX(1);
        }
    }

    @keyframes docFloat {
        0%,
        100% {
            transform: translateY(0) rotate(-2deg);
        }

        50% {
            transform: translateY(-8%) rotate(2deg);
        }
    }

    @keyframes searchBeat {
        0%,
        100% {
            transform: scale(0.94);
        }

        50% {
            transform: scale(1.08);
        }
    }

    @keyframes rayFlash {
        0%,
        100% {
            opacity: 0.18;
        }

        50% {
            opacity: 0.95;
        }
    }

    @keyframes linkGlow {
        0%,
        100% {
            opacity: 0.32;
        }

        50% {
            opacity: 1;
        }
    }

    @keyframes graphNodePulse {
        0%,
        100% {
            transform: scale(0.92);
        }

        50% {
            transform: scale(1.12);
        }
    }

    @keyframes waveExpand {
        0% {
            opacity: 0.6;
            transform: scale(0.65);
        }

        100% {
            opacity: 0;
            transform: scale(1.35);
        }
    }

    @keyframes corePulse {
        0%,
        100% {
            transform: scale(0.95);
        }

        50% {
            transform: scale(1.08);
        }
    }

    @keyframes agentSpin {
        to {
            transform: rotate(360deg);
        }
    }

    @keyframes decisionDot {
        0%,
        100% {
            opacity: 0.45;
            transform: scale(0.8);
        }

        50% {
            opacity: 1;
            transform: scale(1.2);
        }
    }

    @keyframes signalSweep {
        0%,
        100% {
            opacity: 0.18;
        }

        50% {
            opacity: 0.9;
        }
    }

    @keyframes benefitIconFloat {
        0%,
        100% {
            filter: drop-shadow(0 6px 10px rgba(40, 118, 255, 0.18));
            transform: translate(-50%, -50%) scale(1);
        }

        50% {
            filter: drop-shadow(0 10px 14px rgba(40, 118, 255, 0.3));
            transform: translate(-50%, calc(-50% - 4px)) scale(1.04);
        }
    }
</style>
