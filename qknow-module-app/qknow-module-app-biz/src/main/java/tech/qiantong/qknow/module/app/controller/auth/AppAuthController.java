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

package tech.qiantong.qknow.module.app.controller.auth;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.common.annotation.Anonymous;
import tech.qiantong.qknow.module.app.annotation.AppCheckClientToken;

/**
 * DS模块 OAuth2 Server端 控制器
 * @author Ming
 */
@RestController
@Slf4j
public class AppAuthController {

    /**
     * 处理 OAuth2 凭证式相关请求
     */
    @Anonymous
    @PostMapping("/oauth2/client_token")
    public Object request() {
        log.info("------- 进入请求: " + SaHolder.getRequest().getUrl());
        return SaOAuth2ServerProcessor.instance.clientToken();
    }

    @Anonymous
    @AppCheckClientToken
    @PostMapping("/oauth2/test")
    public Object test() {
        return "test";
    }
}
