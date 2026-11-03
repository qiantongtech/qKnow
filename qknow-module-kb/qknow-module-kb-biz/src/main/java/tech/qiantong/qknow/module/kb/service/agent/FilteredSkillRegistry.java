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
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 技能注册器包装类，用于按技能名称过滤，只暴露当前 Agent 关联的 Skills
 *
 * @author qknow
 * @date 2026-06-18
 */
public class FilteredSkillRegistry implements SkillRegistry {

    private final SkillRegistry delegate;
    private final Set<String> allowedSkillNames;

    public FilteredSkillRegistry(SkillRegistry delegate, Set<String> allowedSkillNames) {
        this.delegate = delegate;
        this.allowedSkillNames = allowedSkillNames;
    }

    @Override
    public Optional<SkillMetadata> get(String name) {
        if (!allowedSkillNames.contains(name)) {
            return Optional.empty();
        }
        return delegate.get(name);
    }

    @Override
    public List<SkillMetadata> listAll() {
        return delegate.listAll().stream()
                .filter(skill -> allowedSkillNames.contains(skill.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(String name) {
        return allowedSkillNames.contains(name) && delegate.contains(name);
    }

    @Override
    public int size() {
        return (int) delegate.listAll().stream()
                .filter(skill -> allowedSkillNames.contains(skill.getName()))
                .count();
    }

    @Override
    public void reload() {
        delegate.reload();
    }

    @Override
    public String readSkillContent(String name) throws IOException {
        if (!allowedSkillNames.contains(name)) {
            throw new IOException("Skill not allowed: " + name);
        }
        return delegate.readSkillContent(name);
    }

    @Override
    public String getSkillLoadInstructions() {
        return delegate.getSkillLoadInstructions();
    }

    @Override
    public String getRegistryType() {
        return delegate.getRegistryType();
    }

    @Override
    public SystemPromptTemplate getSystemPromptTemplate() {
        return delegate.getSystemPromptTemplate();
    }
}
