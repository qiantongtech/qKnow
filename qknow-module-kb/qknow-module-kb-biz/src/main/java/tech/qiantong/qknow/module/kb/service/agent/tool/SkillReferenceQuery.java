package tech.qiantong.qknow.module.kb.service.agent.tool;

import lombok.Data;

/**
 * 读取 Skill references 文件工具入参
 */
@Data
public class SkillReferenceQuery {

    /**
     * skill 目录名称，如 pump-fault-detector-1.0.0
     */
    private String skillDir;

    /**
     * references 文件相对路径，如 references/sample-data.md
     */
    private String referencePath;

}
