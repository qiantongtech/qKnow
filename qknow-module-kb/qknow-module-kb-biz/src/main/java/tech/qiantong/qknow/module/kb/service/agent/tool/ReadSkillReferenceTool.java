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

package tech.qiantong.qknow.module.kb.service.agent.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * 读取 Skill references 目录下文件内容的工具
 */
@Slf4j
public class ReadSkillReferenceTool implements Function<SkillReferenceQuery, String> {

    private final String skillsBaseDir;

    public ReadSkillReferenceTool(String skillsBaseDir) {
        this.skillsBaseDir = skillsBaseDir;
    }

    @Override
    public String apply(SkillReferenceQuery query) {
        if (query == null || query.getSkillDir() == null || query.getReferencePath() == null) {
            return "参数错误：skillDir 和 referencePath 不能为空";
        }
        // 防止路径穿越
        String skillDir = query.getSkillDir().replace("..", "").replace("/", "").replace("\\", "");
        String referencePath = query.getReferencePath().replace("..", "");
        File file = new File(skillsBaseDir, skillDir + "/" + referencePath);
        try {
            if (!file.exists() || !file.isFile()) {
                return "文件不存在：" + file.getAbsolutePath();
            }
            // 确保文件在 skillsBaseDir 下，防止读取其他目录
            String canonicalBase = new File(skillsBaseDir).getCanonicalPath();
            String canonicalFile = file.getCanonicalPath();
            if (!canonicalFile.startsWith(canonicalBase)) {
                return "非法路径：" + file.getAbsolutePath();
            }
            return new String(Files.readAllBytes(Paths.get(canonicalFile)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取 skill reference 文件失败: {}", file.getAbsolutePath(), e);
            return "读取文件失败：" + e.getMessage();
        }
    }

    /**
     * 构建工具回调
     */
    public static FunctionToolCallback<SkillReferenceQuery, String> buildToolCallback(String skillsBaseDir) {
        return FunctionToolCallback.builder("read_skill_reference", new ReadSkillReferenceTool(skillsBaseDir))
                .inputType(SkillReferenceQuery.class)
                .description("读取指定 skill 的 references 目录下的文件内容。入参 skillDir 为 skill 目录名（如 pump-fault-detector-1.0.0），referencePath 为 references 下的相对路径（如 references/sample-data.md）。")
                .build();
    }

}
