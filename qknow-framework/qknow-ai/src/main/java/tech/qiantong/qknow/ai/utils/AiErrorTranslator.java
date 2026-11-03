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

package tech.qiantong.qknow.ai.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * AI 执行错误翻译工具。
 * <p>
 * 将模型厂商/SDK 抛出的英文技术异常（如 InvalidParameter、url error、
 * RateLimitExceeded 等）翻译为对终端用户友好的中文描述。
 * 原始异常消息会记录到日志，便于管理员排查真实原因。
 *
 * @author qKnow
 */
@Slf4j
public final class AiErrorTranslator {

    private AiErrorTranslator() {
    }

    /**
     * 翻译规则（按顺序匹配，命中即返回）。
     * keyword 统一转小写后匹配，新增规则时注意顺序：更具体的关键字靠前。
     */
    private static final List<ErrorRule> RULES = List.of(
            // ===== 认证 / API 密钥 =====
            new ErrorRule("invalidauthentication", "模型 API 密钥无效或已过期，请在「系统管理-模型配置」中检查密钥"),
            new ErrorRule("invalid_api_key", "模型 API 密钥不正确，请检查后重新配置"),
            new ErrorRule("incorrect api key", "模型 API 密钥不正确，请检查后重新配置"),
            new ErrorRule("unauthorized", "模型 API 密钥无效或已过期，请检查密钥配置"),
            new ErrorRule("401", "模型 API 密钥无效或已过期，请检查密钥配置"),

            // ===== 余额 / 配额 =====
            new ErrorRule("insufficient quota", "模型账户余额不足或配额已用完，请充值后重试"),
            new ErrorRule("insufficient balance", "模型账户余额不足，请充值后重试"),
            new ErrorRule("billing", "模型账户余额不足，请充值后重试"),
            new ErrorRule("quota exceeded", "模型调用配额已用完，请联系管理员"),

            // ===== 限流 =====
            new ErrorRule("ratelimitexceeded", "模型调用过于频繁，请稍后重试"),
            new ErrorRule("rate limit", "模型调用过于频繁，请稍后重试"),
            new ErrorRule("429", "模型调用过于频繁，请稍后重试"),
            new ErrorRule("too many requests", "模型调用过于频繁，请稍后重试"),

            // ===== 模型不存在 / 禁用 =====
            new ErrorRule("modelnotfound", "所选模型不存在或已下架，请在「系统管理-模型配置」中更换模型"),
            new ErrorRule("model not found", "所选模型不存在或已下架，请更换模型"),
            new ErrorRule("does not exist", "所选模型不存在或已下架，请更换模型"),
            new ErrorRule("model is disabled", "所选模型已被禁用，请更换模型"),

            // ===== 上下文超长 =====
            new ErrorRule("context length", "对话内容过长，已超出模型上下文限制，请精简历史记录或提示词"),
            new ErrorRule("maximum context", "对话内容过长，已超出模型上下文限制，请精简历史记录"),
            new ErrorRule("context_length_exceeded", "对话内容过长，已超出模型上下文限制，请精简历史记录"),
            new ErrorRule("too long", "对话内容过长，请精简后重试"),
            new ErrorRule("maximum number of tokens", "对话内容过长，已超出模型 Token 限制"),

            // ===== 地址 / 参数错误（截图中的 url error） =====
            new ErrorRule("url error", "模型调用地址配置有误，请检查模型的 API 地址是否正确"),
            new ErrorRule("invalidparameter", "模型参数配置有误，请检查模型配置"),
            new ErrorRule("invalid parameter", "模型参数配置有误，请检查模型配置"),
            new ErrorRule("invalid url", "模型调用地址配置有误，请检查模型的 API 地址"),

            // ===== 超时 =====
            new ErrorRule("timeout", "模型响应超时，请稍后重试；若持续出现请检查网络或模型服务状态"),
            new ErrorRule("timed out", "模型响应超时，请稍后重试；若持续出现请检查网络或模型服务状态"),
            new ErrorRule("read timed out", "模型响应超时，请稍后重试"),
            new ErrorRule("connect timed out", "模型连接超时，请检查网络或模型服务状态"),

            // ===== 服务端错误 =====
            new ErrorRule("internalservererror", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("internal server error", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("bad gateway", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("service unavailable", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("500", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("502", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("503", "模型服务暂时不可用，请稍后重试"),
            new ErrorRule("504", "模型服务暂时不可用，请稍后重试"),

            // ===== 权限 =====
            new ErrorRule("accessdenied", "模型调用权限不足，请联系管理员"),
            new ErrorRule("access denied", "模型调用权限不足，请联系管理员"),
            new ErrorRule("403", "模型调用权限不足，请联系管理员"),
            new ErrorRule("forbidden", "模型调用权限不足，请联系管理员"),

            // ===== 网络 / 连接 =====
            new ErrorRule("connection refused", "无法连接到模型服务，请检查模型服务是否启动"),
            new ErrorRule("connection reset", "模型服务连接被重置，请稍后重试"),
            new ErrorRule("failed to resolve", "无法解析模型服务域名（DNS 解析失败），请检查模型 API 地址是否正确，以及服务器网络/DNS 是否正常"),
            new ErrorRule("unknownhostexception", "无法解析模型服务域名（DNS 解析失败），请检查模型 API 地址是否正确，以及服务器网络/DNS 是否正常"),
            new ErrorRule("unknown host", "无法解析模型服务域名（DNS 解析失败），请检查模型 API 地址是否正确，以及服务器网络/DNS 是否正常"),
            new ErrorRule("no route to host", "无法连接到模型服务，请检查网络连接")
    );

    /**
     * 兜底提示：当无法匹配到任何规则时返回。
     */
    private static final String FALLBACK = "模型调用失败，请检查模型配置、API 密钥和网络连接";

    /**
     * 将原始异常消息翻译为用户可见的中文描述。
     * <p>
     * 处理逻辑：
     * 1. 空消息直接返回兜底文案；
     * 2. 按规则顺序做小写包含匹配，命中即返回友好文案；
     * 3. 未命中返回兜底文案；
     * 4. 原始消息始终记录 warn 日志，便于排查。
     *
     * @param rawMessage 原始异常消息
     * @return 用户可见的中文描述
     */
    public static String translate(String rawMessage) {
        if (StrUtil.isBlank(rawMessage)) {
            return FALLBACK;
        }
        String lower = rawMessage.toLowerCase();
        for (ErrorRule rule : RULES) {
            if (lower.contains(rule.keyword())) {
                log.warn("[AiError] 模型报错已翻译 | keyword={} | raw={}", rule.keyword(), rawMessage);
                return rule.friendly();
            }
        }
        log.warn("[AiError] 未匹配到翻译规则的模型报错 | raw={}", rawMessage);
        return FALLBACK;
    }

    /**
     * 判断原始消息是否命中任何翻译规则（即是否为已知的模型厂商报错）。
     * 业务层可据此决定是直接翻译还是保留原始业务文案。
     *
     * @param rawMessage 原始异常消息
     * @return true 表示命中已知模型报错规则
     */
    public static boolean matches(String rawMessage) {
        if (StrUtil.isBlank(rawMessage)) {
            return false;
        }
        String lower = rawMessage.toLowerCase();
        for (ErrorRule rule : RULES) {
            if (lower.contains(rule.keyword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 翻译规则。
     *
     * @param keyword  匹配关键字（小写）
     * @param friendly 友好中文文案
     */
    private record ErrorRule(String keyword, String friendly) {
    }
}
