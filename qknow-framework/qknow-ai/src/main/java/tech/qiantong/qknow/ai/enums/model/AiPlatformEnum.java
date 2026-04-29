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

package tech.qiantong.qknow.ai.enums.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * AI 模型平台
 *
 * @author wang
 */
@Getter
@AllArgsConstructor
public enum AiPlatformEnum {

    // ========== 国内平台 ==========

    TONG_YI("TongYi", "通义千问", "1,2,3,4,5,6","通义千问提供的模型。","https://dashscope.aliyuncs.com/api/v1"), // 阿里
    YI_YAN("YiYan", "文心一言", "","",""), // 百度
    DEEP_SEEK("DeepSeek", "DeepSeek", "1,2,3","深度求索提供的模型，例如 deepseek-chat、deepseek-coder 。","https://api.deepseek.com/v1"), // DeepSeek
    ZHI_PU("ZhiPu", "智谱", "","",""), // 智谱 AI
    XING_HUO("XingHuo", "星火","","", ""), // 讯飞
    DOU_BAO("DouBao", "豆包", "","",""), // 字节
    HUN_YUAN("HunYuan", "混元", "","",""), // 腾讯
    SILICON_FLOW("SiliconFlow", "硅基流动", "","",""), // 硅基流动
    MINI_MAX("MiniMax", "MiniMax", "","",""), // 稀宇科技
    MOONSHOT("Moonshot", "月之暗面", "","",""), // KIMI
    BAI_CHUAN("BaiChuan", "百川智能", "","",""), // 百川智能

    // ========== 国外平台 ==========

    OPENAI("OpenAI", "OpenAI", "1,2,3,4,5,6","符合 openai 规范的模型",""), // OpenAI 官方
    AZURE_OPENAI("AzureOpenAI", "AzureOpenAI", "","",""), // OpenAI 微软
    ANTHROPIC("Anthropic", "Anthropic", "","",""), // Anthropic Claude
    GEMINI("Gemini", "Gemini", "","",""), // 谷歌 Gemini
    OLLAMA("Ollama", "Ollama", "1,2,3","Ollama 提供的模型。",""),

    STABLE_DIFFUSION("StableDiffusion", "StableDiffusion", "","",""), // Stability AI
    MIDJOURNEY("Midjourney", "Midjourney", "","",""), // Midjourney
    SUNO("Suno", "Suno", "","",""), // Suno AI
    GROK("Grok", "Grok", "","",""), // Grok

    ;

    /**
     * 平台
     */
    private final String platform;
    /**
     * 平台名
     */
    private final String name;

    /**
     * 平台标签
     */
    private final String platform_tag;

    /**
     * 描述
     */
    private final String description;

    /**
     * openAiUrl 地址，以 v1 结尾，不带 /
     */
    private final String openAiUrl;

    public static final String[] ARRAYS = Arrays.stream(values()).map(AiPlatformEnum::getPlatform).toArray(String[]::new);

    public static AiPlatformEnum validatePlatform(String platform) {
        for (AiPlatformEnum platformEnum : AiPlatformEnum.values()) {
            if (platformEnum.getPlatform().equals(platform)) {
                return platformEnum;
            }
        }
        throw new IllegalArgumentException("非法平台： " + platform);
    }

    public String[] array() {
        return ARRAYS;
    }

}
