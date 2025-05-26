package tech.qiantong.qknow.auth.service.impl;

import cn.dev33.satoken.oauth2.template.SaOAuth2Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Sa-Token OAuth2.0 整合实现 【重写框架】
 * @author qknow
 */
@Component
public class AuthOAuth2TemplateImpl extends SaOAuth2Template {

	private static final Logger log = LoggerFactory.getLogger(AuthOAuth2TemplateImpl.class);

}
