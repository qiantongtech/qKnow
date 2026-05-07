/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
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
