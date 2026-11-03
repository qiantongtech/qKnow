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
 * 源代码的"可访问性"均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
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

package tech.qiantong.qknow.module.kb.service.agent;

import com.alibaba.cloud.ai.graph.skills.SkillMetadata;
import com.alibaba.cloud.ai.graph.skills.registry.AbstractSkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.SkillScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 按需加载技能注册器，仅加载指定的 skill 目录，避免全量扫描
 *
 * @author qknow
 * @date 2026-06-23
 */
public class TargetedSkillRegistry extends AbstractSkillRegistry {

    private static final Logger logger = LoggerFactory.getLogger(TargetedSkillRegistry.class);

    private final SkillScanner scanner = new SkillScanner();
    private final List<String> skillDirectoryPaths;
    private final SystemPromptTemplate systemPromptTemplate;

    /**
     * @param skillDirectoryPaths   需要加载的 skill 目录绝对路径列表
     * @param systemPromptTemplate  系统提示模板，为 null 时使用默认模板
     */
    public TargetedSkillRegistry(List<String> skillDirectoryPaths, SystemPromptTemplate systemPromptTemplate) {
        this.skillDirectoryPaths = skillDirectoryPaths;
        this.systemPromptTemplate = systemPromptTemplate != null
                ? systemPromptTemplate
                : SystemPromptTemplate.builder()
                        .template(FileSystemSkillRegistry.DEFAULT_SYSTEM_PROMPT_TEMPLATE)
                        .build();
        loadSkillsToRegistry();
    }

    @Override
    protected void loadSkillsToRegistry() {
        Map<String, SkillMetadata> map = new HashMap<>();
        for (String dirPath : skillDirectoryPaths) {
            Path path = Path.of(dirPath);
            if (!Files.exists(path)) {
                logger.warn("Skill 目录不存在: {}", dirPath);
                continue;
            }
            try {
                SkillMetadata skill = scanner.loadSkill(path, "project");
                if (skill != null) {
                    map.put(skill.getName(), skill);
                    logger.info("已加载 skill: {} from {}", skill.getName(), dirPath);
                }
            } catch (Exception e) {
                logger.warn("加载 skill 失败: {}", dirPath, e);
            }
        }
        this.skills = map;
        logger.info("TargetedSkillRegistry 加载完成，共加载 {} 个 skill", map.size());
    }

    @Override
    public String readSkillContent(String name) throws IOException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Skill name cannot be null or empty");
        }
        Optional<SkillMetadata> opt = get(name);
        if (opt.isEmpty()) {
            throw new IllegalStateException("Skill not found: " + name);
        }
        return opt.get().loadFullContent();
    }

    @Override
    public String getSkillLoadInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("**Skill Locations:**\n");
        for (String dirPath : skillDirectoryPaths) {
            Path path = Path.of(dirPath);
            String dirName = path.getFileName() != null ? path.getFileName().toString() : dirPath;
            sb.append(String.format("- Skill directory: `%s` (skillDir: `%s`)\n", dirPath, dirName));
        }
        sb.append("\n**Skill References 读取说明:**\n");
        sb.append("当某个 skill 需要读取 references 目录下的文件时，请调用工具 `read_skill_reference`。\n");
        sb.append("参数说明:\n");
        sb.append("- skillDir: 当前 skill 的目录名\n");
        sb.append("- referencePath: references 下的相对路径\n");
        return sb.toString();
    }

    @Override
    public String getRegistryType() {
        return "FileSystem";
    }

    @Override
    public SystemPromptTemplate getSystemPromptTemplate() {
        return systemPromptTemplate;
    }
}
