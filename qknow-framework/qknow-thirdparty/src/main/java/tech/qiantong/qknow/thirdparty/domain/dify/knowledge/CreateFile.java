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

package tech.qiantong.qknow.thirdparty.domain.dify.knowledge;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import tech.qiantong.qknow.common.utils.StringUtils;

import java.io.File;

/**
 * 知识库创建实体的body
 * @program: qknow
 * @author wang
 * @date 2025/02/19 16:11
 **/
@Data
public class CreateFile {

    // 源文档 ID(有则更新文档)
    private String originalDocumentId;
    // 索引方式
    private String indexingTechnique;
    // 文档格式
    private String docForm;
    // 文档语言
    private String docLanguage;
    // 清洗、分段模式
    private String mode;
    // 替换连续空格、换行符、制表符
    private Boolean removeExtraSpaces;
    // 替换 URL、邮箱
    private Boolean removeUrlsEmails;
    /** 分段重叠 */
    private String chunkOverlap;
    // 分隔符
    private String separator;
    // 最大 token 数
    private Integer maxTokens;
    // 父级模式
    private String parentMode;
    // 子集分隔符
    private String subchunkSeparator;
    // 子集最大 token 数
    private Integer subchunkMaxTokens;
    // 文档
    private File file;

    @Override
    public String toString() {
        JSONArray processingRules = getJsonArray();

        JSONObject segmentation = new JSONObject();
        segmentation.put("separator", this.separator);
        segmentation.put("max_tokens", this.maxTokens);
        segmentation.put("chunk_overlap", this.chunkOverlap);
        JSONObject rules = new JSONObject();
        rules.put("pre_processing_rules", processingRules);
        rules.put("segmentation", segmentation);
        if (StringUtils.isNotNull(this.parentMode)) {
            rules.put("parent_mode", this.parentMode);
            JSONObject subchunk = new JSONObject();
            subchunk.put("separator", this.subchunkSeparator);
            subchunk.put("max_tokens", this.subchunkMaxTokens);
            rules.put("subchunk_segmentation", subchunk);
        }
        JSONObject rule = new JSONObject();
        rule.put("mode", this.mode);
        rule.put("rules", rules);

        JSONObject data = new JSONObject();
        data.put("indexing_technique", this.indexingTechnique);
        data.put("doc_form", this.docForm);
        data.put("doc_language", this.docLanguage);
        data.put("original_document_id", this.originalDocumentId);
        data.put("process_rule", rule);
        return data.toJSONString();
    }

    private JSONArray getJsonArray() {
        JSONArray processingRules = new JSONArray();
        JSONObject removeExtraSpaces = new JSONObject();
        removeExtraSpaces.put("id", "remove_extra_spaces");
        removeExtraSpaces.put("enabled", this.removeExtraSpaces);
        JSONObject removeUrlsEmails = new JSONObject();
        removeUrlsEmails.put("id", "remove_urls_emails");
        removeUrlsEmails.put("enabled", this.removeUrlsEmails);
        processingRules.add(removeExtraSpaces);
        processingRules.add(removeUrlsEmails);
        return processingRules;
    }
}
