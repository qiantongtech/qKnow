/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.module.app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import org.springframework.stereotype.Component;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-04-18 14:07
 **/
@Component
public class ApiJwtUtil {

    //TOKEN过期时间 默认7天
    public static final long TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;

    //Refresh-Token过期时间 默认30天
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;

    public static final String TOKEN_KEY = "API_TOKEN";

    public static final String REFRESH_TOKEN_KEY = "REFRESH_API_TOKEN";

    private static JwtParser jwtParser;



    /**
     * 通过token获取username
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);
        if (jws != null) {
            Claims claims = jws.getBody();
            if (claims != null) {
                return claims.getSubject();
            }
        }
        return null;
    }

}
