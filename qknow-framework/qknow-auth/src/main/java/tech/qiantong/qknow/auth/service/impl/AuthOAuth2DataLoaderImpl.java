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
