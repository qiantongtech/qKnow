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

package tech.qiantong.qknow.ai.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 参数提取器工具
 * 作用：构建符合Dify日志中prompt结构的完整对话文本，并提取对应提取参数的数据。
 */
@Slf4j
public class ParameterExtractionUtils {

    // === 静态缓存：避免重复拼接 ===
    private static final String SYSTEM_MESSAGE_TEMPLATE =
            "You should always follow the instructions and output a valid JSON object.\n" +
                    "The structure of the JSON object you can found in the instructions.\n\n" +
                    "### Memory\n<histories>\n\n</histories>\n\n" +
                    "### Instructions:\n<instructions>\n%s\n</instructions>";

    // 静态示例消息（预构建，线程安全）
    private static final List<Message> EXAMPLE_MESSAGES = Arrays.asList(
            new UserMessage(buildExampleUser1()),
            new AssistantMessage("{\"location\": \"San Francisco\"}"),
            new UserMessage(buildExampleUser2()),
            new AssistantMessage("{\"food\": \"apple pie\"}")
    );

    // 可选：是否启用 few-shot 示例（可通过配置关闭以提升速度）
    private static final boolean ENABLE_FEW_SHOT_EXAMPLES = true;

    // 结构缓存（防止相同 structureList 重复生成）
    private static final Map<String, String> STRUCTURE_CACHE = new ConcurrentHashMap<>();

    // ==================== 公共接口 ====================

    /**
     * 构建Dify风格的完整Prompt
     *
     * @param segment     要分析的业务文本片段
     * @param structureList   提取参数
     * @param instruction 指令内容
     * @return 完整的Prompt字符串，可直接发送给LLM
     */
    public static String buildCompleteDifyPrompt(String segment,
                                                 List<Map<String, String>> structureList,
                                                 String instruction) {
        long start = System.currentTimeMillis();

        // 1. 生成或获取缓存的 schema
        String cacheKey = generateStructureCacheKey(structureList);
        String structure = STRUCTURE_CACHE.computeIfAbsent(cacheKey, k -> generateFinalEscapedSchema(structureList));

        // 2. 构建消息
        List<Message> messages = buildDifyMessages(segment, structure, instruction);

        // 3. 转为 prompt
        String prompt = convertMessagesToPrompt(messages);

        if (log.isDebugEnabled()) {
            log.debug("buildCompleteDifyPrompt took {} ms", System.currentTimeMillis() - start);
        }
        return prompt;
    }

    /**
     * 从LLM输出中提取所有定义的参数（通用方法）
     *
     * @param llmOutput LLM返回的原始输出字符串
     * @param structureList 提取参数结构定义列表，用于验证必需字段
     * @return 包含所有提取参数的Map，key为参数名，value为对应的值
     */
    public static Map<String, Object> extractAllParameters(String llmOutput,
                                                           List<Map<String, String>> structureList) {
        Map<String, Object> result = new HashMap<>();
        try {
            String cleaned = llmOutput.trim();
            if (cleaned.isEmpty()) return result;

            JSONObject json = JSONObject.parseObject(cleaned);
            if (json == null) {
                log.warn("Invalid JSON from LLM: {}", llmOutput);
                return result;
            }

            for (Map<String, String> paramDef : structureList) {
                String name = paramDef.get("name");
                if (name != null && json.containsKey(name)) {
                    result.put(name, json.get(name)); // FastJSON 已保留类型
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse LLM output: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 合并新提取的数据到总数据容器中
     *
     * @param newData 本次提取的新数据，key为参数名，value为参数值
     * @param structureList 提取参数结构定义列表，包含：
     *                      - name: 参数名称（如"relations"、"concepts"）
     *                      - type: 参数类型（"array"、"string"等）
     *                      - description: 参数描述
     *                      - required: 是否必需（"true"/"false"）
     */
    public static Map<String, JSONArray> simpleMergeData(Map<String, Object> newData,
                                                         List<Map<String, String>> structureList) {
        // 1. 参数校验
        if (structureList == null || structureList.isEmpty()) {
            return new HashMap<>();
        }

        // 2. 初始化：预创建所有定义字段的空 JSONArray（关键！保持原始行为）
        Map<String, JSONArray> allExtractedData = new ConcurrentHashMap<>();
        for (Map<String, String> paramDef : structureList) {
            String paramName = paramDef.get("name");
            if (paramName != null && !paramName.trim().isEmpty()) {
                allExtractedData.put(paramName, new JSONArray());
            }
        }

        // 3. 如果 newData 为空，直接返回全空结构
        if (newData == null || newData.isEmpty()) {
            return allExtractedData;
        }

        // 4. 合并新数据（覆盖或追加到预初始化的容器中）
        for (Map.Entry<String, Object> entry : newData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                continue;
            }

            // 只处理在 structureList 中定义过的字段（原始逻辑隐含此行为）
            if (!allExtractedData.containsKey(key)) {
                continue; // 或可记录 warn，但原始代码未处理，故跳过
            }

            JSONArray container = allExtractedData.get(key); // 已预初始化，不会为 null

            if (value instanceof JSONArray) {
                container.addAll((JSONArray) value);
            } else if (value instanceof Collection) {
                container.addAll((Collection<?>) value);
            } else {
                container.add(value);
            }
        }

        return allExtractedData;
    }

    // ==================== 内部工具方法 ====================

    /**
     * 生成最终要求的带多层转义的Schema字符串（动态提取类型，无硬编码）
     * @param propertiesList 输入属性列表（包含动态传入的name、type、description）
     * @return 完全匹配需求的转义JSON字符串
     */
    private static String generateFinalEscapedSchema(List<Map<String, String>> propertiesList) {
        List<String> props = new ArrayList<>();
        List<String> required = new ArrayList<>();

        for (Map<String, String> prop : propertiesList) {
            String name = prop.getOrDefault("name", "").trim();
            if (name.isEmpty()) continue;

            String type = prop.getOrDefault("type", "string").trim();
            String desc = prop.getOrDefault("description", "");
            boolean isRequired = Boolean.parseBoolean(prop.getOrDefault("required", "true"));

            if (isRequired) {
                required.add(name);
            }

            String escapedDesc = escapeJsonString(desc);
            StringBuilder sb = new StringBuilder();
            sb.append("\"").append(name).append("\":{")
                    .append("\"description\":\"").append(escapedDesc).append("\",")
                    .append("\"type\":\"").append(type.toLowerCase()).append("\"");

            if ("array".equalsIgnoreCase(type)) {
                sb.append(",\"items\":{\"type\":\"object\"}");
            }
            sb.append("}");
            props.add(sb.toString());
        }

        String propsStr = String.join(",", props);
        String requiredStr = required.stream()
                .map(f -> "\"" + f + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        return "{\"type\":\"object\",\"properties\":{" + propsStr + "},\"required\":" + requiredStr + "}";
    }

    // 轻量级 JSON 字符串转义（仅处理必要字符）
    private static String escapeJsonString(String s) {
        if (s == null || s.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 构建Dify风格的消息列表
     *
     * @param segment     业务文本片段
     * @param structure   提取参数结构
     * @param instruction 指令内容
     * @return 消息列表
     */
    private static List<Message> buildDifyMessages(String segment, String structure, String instruction) {
        List<Message> messages = new ArrayList<>();

        // System 消息
        messages.add(new SystemMessage(String.format(SYSTEM_MESSAGE_TEMPLATE, instruction)));

        // Few-shot 示例（可选）
        if (ENABLE_FEW_SHOT_EXAMPLES) {
            messages.addAll(EXAMPLE_MESSAGES);
        }

        // 用户实际查询
        String userQuery = "### Structure\n" +
                "Here is the structure of the JSON object, you should always follow the structure.\n" +
                "<structure>\n" + structure + "\n</structure>\n\n" +
                "### Text to be converted to JSON\n" +
                "Inside <text></text> XML tags, there is a text that you should convert to a JSON object.\n" +
                "<text>\n" + segment + "\n</text>";
        messages.add(new UserMessage(userQuery));

        return messages;
    }
    /**
     * 将消息列表转换为Dify风格的提示词格式
     */
    private static String convertMessagesToPrompt(List<Message> messages) {
        StringBuilder sb = new StringBuilder();
        for (Message msg : messages) {
            String role = msg.getMessageType().getValue();
            String capRole = Character.toUpperCase(role.charAt(0)) + role.substring(1);
            sb.append("### ").append(capRole).append("\n")
                    .append(msg.getText()).append("\n\n");
        }
        sb.append("### Assistant\n");
        return sb.toString();
    }

    // 缓存 key 生成（简单 JSON 化）
    private static String generateStructureCacheKey(List<Map<String, String>> list) {
        return list.toString(); // 或使用更健壮的 hash（如 SHA1），但 toString 足够用于内存缓存
    }

    // 静态示例内容（避免每次拼接）
    private static String buildExampleUser1() {
        return "### Structure\n" +
                "Here is the structure of the JSON object, you should always follow the structure.\n" +
                "<structure>\n" +
                "{\"type\":\"object\",\"properties\":{\"location\":{\"type\":\"string\",\"description\":\"The location to get the weather information\",\"required\":true}},\"required\":[\"location\"]}\n" +
                "</structure>\n\n" +
                "### Text to be converted to JSON\n" +
                "Inside <text></text> XML tags, there is a text that you should convert to a JSON object.\n" +
                "<text>\n" +
                "What is the weather today in SF?\n" +
                "</text>";
    }

    private static String buildExampleUser2() {
        return "### Structure\n" +
                "Here is the structure of the JSON object, you should always follow the structure.\n" +
                "<structure>\n" +
                "{\"type\":\"object\",\"properties\":{\"food\":{\"type\":\"string\",\"description\":\"The food to eat\",\"required\":true}},\"required\":[\"food\"]}\n" +
                "</structure>\n\n" +
                "### Text to be converted to JSON\n" +
                "Inside <text></text> XML tags, there is a text that you should convert to a JSON object.\n" +
                "<text>\n" +
                "I want to eat some apple pie.\n" +
                "</text>";
    }
}
