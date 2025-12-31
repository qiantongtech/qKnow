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

//package tech.qiantong.qknow.controller;
//
//import cn.dev33.satoken.context.SaHolder;
//import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
//import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
//import cn.dev33.satoken.oauth2.strategy.SaOAuth2Strategy;
//import cn.dev33.satoken.stp.SaLoginModel;
//import cn.dev33.satoken.stp.StpUtil;
//import cn.dev33.satoken.util.SaResult;
//import cn.hutool.core.convert.Convert;
//import tech.qiantong.qknow.entity.domain.core.common.SysUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * OAuth2 Server 的 Controller
// * 负责 OAuth2 相关的认证、登录、授权操作
// *
// * @author qknow
// */
//@RestController
//@RequestMapping("/oauth2")
//public class AuthOAuth2ServerController {
//
//    @Autowired
//    private ISysUserService userService;
//
//    /**
//     * 配置 OAuth2 认证服务
//     */
//    @Autowired
//    public void configOAuth2Server(SaOAuth2ServerConfig oauth2Server) {
//
//        // 配置登录处理
//        oauth2Server.doLoginHandle = (name, pwd) -> {
//            Boolean rememberMe = Convert.toBool(SaHolder.getRequest().getParam("rememberMe"));
//            SysUser user = userService.findUserByNameOrPhone(name);
//
//            if (user != null && user.comparePwd(pwd)) {
//                SaLoginModel loginModel = new SaLoginModel()
//                        // 此次登录的客户端设备标识，用于在 [同账号异地登录] 时指定此次登录的设备标识
//                        .setDevice("PC")
//                        // 是否为持久 Cookie，持久 Cookie 在浏览器关闭时不会自动删除，下次打开仍然存在
//                        .setIsLastingCookie(rememberMe)
//                        .setIsWriteHeader(true);
//                StpUtil.login(user.getUserId(), loginModel);
//                // TODO: 缺少登录日志记录
//                return SaResult.ok();
//            } else {
//                return SaResult.error("账号或密码错误");
//            }
//        };
//
//        // 配置未登录时返回的 View
//        oauth2Server.notLoginView = () -> {
//            return new ModelAndView("sso/index.html");
//        };
//
//        // 配置授权确认视图
//        oauth2Server.confirmView = (clientId, scopes) -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("clientId", clientId);
//            map.put("scope", scopes);
//            return new ModelAndView("sso/confirm.html", map);
//        };
//
//        // OAuth2 会话数据存储与访问策略
//        SaOAuth2Strategy.instance.createAccessToken = (clientId, loginId, scopes) -> {
//            return StpUtil.getOrCreateLoginSession(loginId);
//        };
//    }
//
//    /**
//     * 处理 OAuth2 授权请求
//     * 支持授权码模式和隐式模式
//     *
//     * 请求参数：
//     * response_type 响应类型（必填项）
//     * client_id 应用 ID（必填项）
//     * redirect_uri 用户确认后授权的重定向 URL 地址（必填项）
//     * scope 请求授权范围，用空格分隔（必填项）
//     * state 用于防止 CSRF 攻击的状态值，可选项，返回时会附加到重定向 URL（非必填项）
//     *
//     * @return 返回授权结果的视图
//     */
//    @RequestMapping("/authorize")
//    public Object authorize() {
//        return SaOAuth2ServerProcessor.instance.authorize();
//    }
//
//    /**
//     * 处理用户登录请求
//     *
//     * 请求参数：
//     * name 用户名（必填项）
//     * pwd 密码（必填项）
//     * rememberMe 是否记住登录状态（必填项）
//     *
//     * @return 用户登录的结果
//     */
//    @PostMapping("/doLogin")
//    public Object doLogin() {
//        return SaOAuth2ServerProcessor.instance.doLogin();
//    }
//
//    /**
//     * 处理用户确认授权请求
//     *
//     * 请求参数：
//     * client_id 应用 ID（必填项）
//     * scope 确认的授权范围，用空格分隔（必填项）
//     * build_redirect_uri 是否重新构建重定向 URL（true/false）（非必填项）
//     * response_type URL 上的 response_type 参数值（必填项）
//     * redirect_uri URL 上的 redirect_uri 参数值（必填项）
//     * state URL 上的 state 参数值（必填项）
//     *
//     * @return 用户确认授权的结果
//     */
//    @RequestMapping("/doConfirm")
//    public Object doConfirm() {
//        return SaOAuth2ServerProcessor.instance.doConfirm();
//    }
//
//    /**
//     * 通过 Code 获取 Access-Token 请求
//     * 支持授权码模式（Code 模式）
//     *
//     * 请求参数：
//     * grant_type 授权类型，应填写 authorization_code（必填项）
//     * client_id 应用 ID（必填项）
//     * client_secret 应用密钥（必填项）
//     * code 授权码（必填项）
//     *
//     * @return 返回 Token 结果
//     */
//    @PostMapping("/token")
//    public Object token() {
//        return SaOAuth2ServerProcessor.instance.token();
//    }
//
//    /**
//     * 使用 Refresh-Token 刷新 Access-Token 请求
//     *
//     * 请求参数：
//     * grant_type 授权类型，应填写 refresh_token（必填项）
//     * client_id 应用 ID（必填项）
//     * client_secret 应用密钥（必填项）
//     * refresh_token 刷新的 Refresh-Token（必填项）
//     *
//     * @return 刷新 Token 的结果
//     */
//    @PostMapping("/refresh")
//    public Object refresh() {
//        return SaOAuth2ServerProcessor.instance.refresh();
//    }
//
//    /**
//     * 撤销 Access-Token 请求
//     *
//     * 请求参数：
//     * client_id 应用 ID（必填项）
//     * client_secret 应用密钥（必填项）
//     * access_token 要撤销的 Access-Token（必填项）
//     *
//     * @return 撤销 Token 的结果
//     */
//    @PostMapping("/revoke")
//    public Object revoke() {
//        return SaOAuth2ServerProcessor.instance.revoke();
//    }
//
//    /**
//     * 处理客户端凭证模式（Client Credentials）获取 Access-Token 请求
//     *
//     * 请求参数：
//     * grant_type 授权类型，应填写 client_credentials（必填项）
//     * client_id 应用 ID（必填项）
//     * client_secret 应用密钥（必填项）
//     * scope 请求的授权范围，用空格分隔（非必填项）
//     *
//     * @return 凭证模式获取的 Token 结果
//     */
//    @PostMapping("/client_token")
//    public Object clientToken() {
//        return SaOAuth2ServerProcessor.instance.clientToken();
//    }
//}
