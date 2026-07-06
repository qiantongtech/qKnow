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
import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用分块，现根据正则表达式进行分段，如果段落长度长度超过最大长度，再进行分块操作
 *
 * @author fabian
 */
public class GeneralSplitter extends TextSplitter {

    private String regex = "";// 分段标识符
    private Integer maxChunkSize = 1024;// 分块最大长度
    private Integer chunkOverlapSize = 0;// 分块重叠长度

    public GeneralSplitter(String regex, Integer maxChunkSize, Integer chunkOverlapSize) {
        this.regex = regex;
        this.maxChunkSize = maxChunkSize;
        this.chunkOverlapSize = chunkOverlapSize;
    }

    /**
     * 具体的拆分操作
     * todo：按照 dify 的拆分逻辑，是不是还有一个根据标点符号
     *
     * @param text 文本内容
     * @return 拆分后的文本列表
     */
    @Override
    protected List<String> splitText(String text) {
        String[] split = text.split(regex);
//        List<String> strings1 = FileReader.splitText(text, maxChunkSize, regex);

        List<String> result = new ArrayList<>(split.length);
        for (String s : split) {
            s = s.trim();
            if (StrUtil.isBlank(s)) {
                continue;
            }
            if (s.length() > maxChunkSize) {
                List<String> strings = this.splitByLength(s);
                result.addAll(strings);
                continue;
            }
            result.add(s);
        }
        return result;
    }

    /**
     * 根据最大分块长度进行分块
     *
     * @param text 段落内容
     * @return 分块列表
     */
    private List<String> splitByLength(String text) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() > maxChunkSize) {
            String substring = sb.substring(0, maxChunkSize);
            result.add(substring);
            sb.delete(0, maxChunkSize);
        }
        result.add(sb.toString());
        setOverlap(result);
        return result;
    }

    /**
     * 为文本设置重叠
     *
     * @param chunkList 分块列表
     */
    private void setOverlap(List<String> chunkList) {
        if (chunkOverlapSize <= 0 || CollUtil.isEmpty(chunkList)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        String prev = "";
        String current = chunkList.get(0);
        String next = chunkList.get(1);
        for (int i = 0; i < chunkList.size(); i++) {
            sb.setLength(0);
            sb.append(overlapLast(prev)).append(current).append(overlapFirst(next));
            prev = current;
            current = next;
            if (i >= chunkList.size() - 2) {
                next = "";
            } else {
                next = chunkList.get(i + 2);
            }

            chunkList.set(i, sb.toString());
        }
    }

    /**
     * 获取文段的最后 chunkOverlapSize 个字符
     *
     * @param str 前文段
     * @return 文段的最后 chunkOverlapSize 个字符
     */
    private String overlapLast(String str) {
        if (str.length() <= chunkOverlapSize) {
            return str;
        }
        return str.substring(str.length() - chunkOverlapSize);
    }

    /**
     * 获取文段的最前面 chunkOverlapSize 个字符
     *
     * @param str 文段内容
     * @return 文段的最后 chunkOverlapSize 个字符
     */
    private String overlapFirst(String str) {
        if (str.length() <= chunkOverlapSize) {
            return str;
        }
        return str.substring(0, chunkOverlapSize);
    }
}
