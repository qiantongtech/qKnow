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
