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

package tech.qiantong.qknow.module.ext.api.extDatasource.dto;

import lombok.*;

/**
 * 数据源 DTO 对象 ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Data
public class ExtDatasourceReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据库连接名称 */
    private String name;

    /** 数据库类型 */
    private Integer type;

    /** 数据库地址 */
    private String host;

    /** 端口号 */
    private Long port;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 连接状态 */
    private Integer status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
