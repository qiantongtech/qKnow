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

package tech.qiantong.qknow.auth.service.impl;

import cn.dev33.satoken.oauth2.consts.GrantType;
import cn.dev33.satoken.oauth2.data.loader.SaOAuth2DataLoader;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import tech.qiantong.qknow.auth.service.IAuthClientService;
import tech.qiantong.qknow.auth.service.IAuthClientUserService;
import tech.qiantong.qknow.auth.domain.AuthClient;
import tech.qiantong.qknow.auth.domain.AuthClientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * OAuth2.0 数据加载实现类
 * 实现 SaOAuth2DataLoader 接口
 * 负责提供 OAuth2.0 客户端信息及用户信息
 */
@Component
public class AuthOAuth2DataLoaderImpl implements SaOAuth2DataLoader {

    @Autowired
    private IAuthClientService clientService;

    @Autowired
    private IAuthClientUserService clientUserService;

    /**
     * 根据 clientId 获取 Client 信息
     *
     * @param clientId 客户端ID
     * @return SaClientModel 客户端模型
     */
    @Override
    public SaClientModel getClientModel(String clientId) {
        AuthClient client = clientService.getById(Convert.toLong(clientId));

        if (client != null) {
            return new SaClientModel()
                    // 客户端ID
                    .setClientId(client.getId().toString())
                    // 客户端密钥
                    .setClientSecret(client.getSecretKey())
                    // 允许的重定向URL
                    .addAllowRedirectUris(client.getRedirectUrl().split(","))
                    // 允许的作用域
                    .addContractScopes("userinfo", "admin")
                    // 允许的授权模式
                    .addAllowGrantTypes(
                            GrantType.authorization_code,
                            GrantType.implicit,
                            GrantType.refresh_token,
                            GrantType.password,
                            GrantType.client_credentials
                    );
        } else {
            return null;
        }
    }

    /**
     * 根据 clientId 和 loginId 获取 openid
     *
     * @param clientId 客户端ID
     * @param loginId 用户ID
     * @return openid 用户唯一标识
     */
    @Override
    public String getOpenid(String clientId, Object loginId) {
        AuthClientUser openid = clientUserService.lambdaQuery()
                .eq(AuthClientUser::getClientId, Convert.toLong(clientId))
                .eq(AuthClientUser::getUserId, Convert.toLong(loginId))
                .one();

        if (openid == null) {
            openid = new AuthClientUser();
            openid.setClientId(Convert.toLong(clientId));
            openid.setUserId(Convert.toLong(loginId));
            openid.setOpenId(IdUtil.simpleUUID());

            clientUserService.save(openid);
        }
        return openid.getOpenId();
    }
}
