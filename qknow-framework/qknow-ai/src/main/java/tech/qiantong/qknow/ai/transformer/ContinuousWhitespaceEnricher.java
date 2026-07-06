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

package tech.qiantong.qknow.ai.transformer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 删除连续空格、回车、制表符，将其替换为一个
 *
 * @author fabian
 */
public class ContinuousWhitespaceEnricher implements DocumentTransformer {

    private static final Pattern MIN_2_SPACE_PATTERN = Pattern.compile(" {2,}");// 2个及以上连续空格
    private static final Pattern MIN_2_NEWLINE_PATTERN = Pattern.compile("\\n{2,}");// 2个及以上连续换行
    private static final Pattern MIN_2_TAB_PATTERN = Pattern.compile("\\t{2,}");// 2个及以上连续制表符
    private static final Pattern MIN_2_CR_PATTERN = Pattern.compile("[\\r\\n]{2,}");// 2个及以上连续换行（windows 系统）

    /**
     * Applies this function to the given argument.
     *
     * @param documents the function argument
     * @return the function result
     */
    @Override
    public List<Document> apply(List<Document> documents) {
        if (CollUtil.isEmpty(documents)) {
            return documents;
        }
        List<Document> result = new ArrayList<>(documents.size());
        for (Document document : documents) {
            if (StrUtil.isBlank(document.getText())) {
                continue;
            }
            String text = document.getText();

            text = MIN_2_CR_PATTERN.matcher(text).replaceAll("\r\n"); // 连续回车→单个回车
            text = MIN_2_SPACE_PATTERN.matcher(text).replaceAll(" "); // 连续空格→单个空格
            text = MIN_2_NEWLINE_PATTERN.matcher(text).replaceAll("\n"); // 连续换行→单个换行
            text = MIN_2_TAB_PATTERN.matcher(text).replaceAll("\t"); // 连续制表符→单个制表符

            Document newDoc = new Document(document.getId(), text, document.getMetadata());
            result.add(newDoc);
        }
        return result;
    }
}
