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

package tech.qiantong.qknow.auth.domain;

import lombok.Data;
import tech.qiantong.qknow.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 应用管理对象 auth_client
 *
 * @author qknow
 * @date 2024-08-31
 */
@Data
@TableName(value = "auth_client")
public class AuthClient extends BaseEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 应用ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 应用秘钥 */
    @Excel(name = "应用秘钥")
    private String secretKey;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String name;

    /** 应用类型;0：Web，1：App，2：小程序 */
    @Excel(name = "应用类型;0：Web，1：App，2：小程序")
    private Integer type;

    /** 应用图标 */
    @Excel(name = "应用图标")
    private String icon;

    /** 应用首页 */
    @Excel(name = "应用首页")
    private String homeUrl;

    /** 同步地址 */
    @Excel(name = "同步地址")
    private String syncUrl;

    /** 允许授权的url */
    @Excel(name = "允许授权的url")
    private String redirectUrl;

    /** 是否公开 */
    @Excel(name = "是否公开")
    private Integer publicFlag;

    /** 是否有效;0：无效，1：有效 */
    @Excel(name = "是否有效;0：无效，1：有效")
    private Integer validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Integer delFlag;
    /** 创建人id;创建者的sys_user_id */
    @Excel(name = "创建人id;创建者的sys_user_id")
    private Long creatorId;

    /** 更新人id;更新者的sys_user_id */
    @Excel(name = "更新人id;更新者的sys_user_id")
    private Long updatorId;


}
