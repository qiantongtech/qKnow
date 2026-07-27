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
