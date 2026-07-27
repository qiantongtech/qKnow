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

package tech.qiantong.qknow.module.kb.service.skills;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import tech.qiantong.qknow.common.constant.Constants;
import tech.qiantong.qknow.file.util.FileUploadUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Skill 文件处理工具类
 * 负责 SKILL.md 的解析、生成、批量导入解压等技能文件相关操作。
 */
@Slf4j
public class SkillFileUtil {

    /**
     * 批量提取并解析 skills ZIP 包
     */
    public static List<Map<String, String>> batchExtractAndParseSkills(String storagePath, String fileUrl,
                                                                       String originalFilename, String platform) {
        List<Map<String, String>> skills = new ArrayList<>();

        String relativePath = fileUrl;
        if (fileUrl.contains(Constants.RESOURCE_PREFIX)) {
            relativePath = fileUrl.substring(fileUrl.indexOf(Constants.RESOURCE_PREFIX) + Constants.RESOURCE_PREFIX.length());
        }
        File zipFile = new File(storagePath + relativePath);
        if (!zipFile.exists()) {
            throw new RuntimeException("ZIP 文件不存在: " + zipFile.getAbsolutePath());
        }

        String tempDirPath = storagePath + "skills/temp_" + System.currentTimeMillis() + "/";
        File tempDir = new File(tempDirPath);
        if (!tempDir.mkdirs()) {
            throw new RuntimeException("创建临时目录失败: " + tempDirPath);
        }

        try {
            unzipFile(zipFile, tempDirPath);

            File[] topLevelFiles = tempDir.listFiles();
            if (topLevelFiles != null && topLevelFiles.length == 1) {
                File singleEntry = topLevelFiles[0];
                if (singleEntry.isFile() && singleEntry.getName().toLowerCase().endsWith(".zip")) {
                    String nestedTempDir = tempDirPath + "nested/";
                    unzipFile(singleEntry, nestedTempDir);
                    singleEntry.delete();
                    File nestedDir = new File(nestedTempDir);
                    File[] nestedFiles = nestedDir.listFiles();
                    if (nestedFiles != null) {
                        for (File f : nestedFiles) {
                            File dest = new File(tempDir, f.getName());
                            f.renameTo(dest);
                        }
                    }
                    nestedDir.delete();
                }
            }

            String permanentBasePath = storagePath + "skills/";

            File rootSkillMd = new File(tempDir, "SKILL.md");
            if (rootSkillMd.exists()) {
                processRootSkillPackage(tempDir, rootSkillMd, permanentBasePath, skills, originalFilename);
            } else {
                File[] firstLevelDirs = tempDir.listFiles(File::isDirectory);
                if (firstLevelDirs != null) {
                    for (File firstLevelDir : firstLevelDirs) {
                        File firstLevelSkillMd = new File(firstLevelDir, "SKILL.md");
                        if (firstLevelSkillMd.exists()) {
                            processSkillDir(firstLevelDir, permanentBasePath, skills, firstLevelDir.getName());
                        } else {
                            File[] secondLevelDirs = firstLevelDir.listFiles(File::isDirectory);
                            if (secondLevelDirs != null && secondLevelDirs.length > 0) {
                                for (File secondLevelDir : secondLevelDirs) {
                                    File secondLevelSkillMd = new File(secondLevelDir, "SKILL.md");
                                    if (secondLevelSkillMd.exists()) {
                                        processSkillDir(secondLevelDir, permanentBasePath, skills, secondLevelDir.getName());
                                    } else {
                                        skills.add(buildSkipResult(secondLevelDir.getName(), "缺少 SKILL.md"));
                                    }
                                }
                            } else {
                                skills.add(buildSkipResult(firstLevelDir.getName(), "缺少 SKILL.md"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("批量提取 skills 失败", e);
            throw new RuntimeException("批量提取 skills 失败: " + e.getMessage(), e);
        } finally {
            FileUploadUtil.deleteDirectory(tempDir);
            zipFile.delete();
        }

        return skills;
    }

    private static Map<String, String> buildSkipResult(String name, String reason) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("status", "skip");
        result.put("reason", reason);
        return result;
    }

    private static void processRootSkillPackage(File tempDir, File skillMdFile,
                                                String permanentBasePath, List<Map<String, String>> skills,
                                                String originalFilename) {
        Map<String, String> skillInfo = parseSkillMd(skillMdFile);
        if (skillInfo == null) {
            log.warn("跳过无法解析 SKILL.md 的文件夹: {}", tempDir.getName());
            skills.add(buildSkipResult(tempDir.getName(), "SKILL.md 解析失败"));
            return;
        }

        String skillName = skillInfo.get("name");
        if (skillName == null || skillName.isEmpty()) {
            skillName = originalFilename != null ? originalFilename.replaceAll("(?i)\\.zip$", "") : "skill";
        }

        File destDir = new File(permanentBasePath + skillName);
        if (destDir.exists()) {
            FileUploadUtil.deleteDirectory(destDir);
        }

        if (!destDir.mkdirs()) {
            log.warn("创建目标目录失败: {}", destDir.getAbsolutePath());
            skills.add(buildSkipResult(skillName, "创建目标目录失败"));
            return;
        }

        File[] files = tempDir.listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    FileUtils.moveToDirectory(f, destDir, false);
                } catch (IOException e) {
                    skills.add(buildSkipResult(skillName, "移动文件失败: " + e.getMessage()));
                    throw new RuntimeException(e);
                }
            }
        }

        Map<String, String> result = new LinkedHashMap<>();
        result.put("name", skillName);
        result.put("status", "success");
        result.put("description", skillInfo.getOrDefault("description", ""));
        result.put("prompt", skillInfo.getOrDefault("prompt", ""));
        result.put("filePath", "/skills/" + skillName);
        skills.add(result);

        log.info("成功导入 skill: {}", skillName);
    }

    private static void processSkillDir(File skillDir, String permanentBasePath,
                                        List<Map<String, String>> skills, String fallbackName) throws IOException {
        File skillMdFile = new File(skillDir, "SKILL.md");
        if (!skillMdFile.exists()) {
            log.warn("跳过无 SKILL.md 的文件夹: {}", skillDir.getName());
            skills.add(buildSkipResult(skillDir.getName(), "缺少 SKILL.md"));
            return;
        }

        Map<String, String> skillInfo = parseSkillMd(skillMdFile);
        if (skillInfo == null) {
            log.warn("跳过无法解析 SKILL.md 的文件夹: {}", skillDir.getName());
            skills.add(buildSkipResult(skillDir.getName(), "SKILL.md 解析失败"));
            return;
        }

        String skillName = skillInfo.get("name");
        if (skillName == null || skillName.isEmpty()) {
            skillName = fallbackName != null ? fallbackName : skillDir.getName();
        }

        File destDir = new File(permanentBasePath + skillName);
        if (destDir.exists()) {
            FileUploadUtil.deleteDirectory(destDir);
        }

        FileUtils.moveDirectory(skillDir, destDir);

        Map<String, String> result = new LinkedHashMap<>();
        result.put("name", skillName);
        result.put("status", "success");
        result.put("description", skillInfo.getOrDefault("description", ""));
        result.put("prompt", skillInfo.getOrDefault("prompt", ""));
        result.put("filePath", "/skills/" + skillName);
        skills.add(result);

        log.info("成功导入 skill: {}", skillName);
    }

    private static void unzipFile(File zipFile, String destDir) throws IOException {
        Path destPath = Paths.get(destDir);
        if (!Files.exists(destPath)) {
            Files.createDirectories(destPath);
        }
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = destPath.resolve(entry.getName());
                if (!entryPath.normalize().startsWith(destPath.normalize())) {
                    log.warn("跳过不安全的 ZIP 条目: {}", entry.getName());
                    continue;
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
    }

    private static Map<String, String> parseSkillMd(File skillMdFile) {
        try {
            String content = new String(Files.readAllBytes(skillMdFile.toPath()), StandardCharsets.UTF_8);
            int firstDelim = content.indexOf("---");
            if (firstDelim < 0) {
                return null;
            }
            int secondDelim = content.indexOf("---", firstDelim + 3);
            if (secondDelim < 0) {
                return null;
            }
            String yamlContent = content.substring(firstDelim + 3, secondDelim).trim();

            Map<String, String> result = new HashMap<>();
            String[] lines = yamlContent.split("\\r?\\n");
            String currentKey = null;
            StringBuilder currentValue = new StringBuilder();

            for (String line : lines) {
                Matcher keyMatcher = Pattern.compile("^(\\w+):\\s*(.*)").matcher(line);
                if (keyMatcher.matches()) {
                    if (currentKey != null) {
                        result.put(currentKey, currentValue.toString().trim());
                    }
                    currentKey = keyMatcher.group(1);
                    currentValue = new StringBuilder(keyMatcher.group(2));
                } else if (currentKey != null) {
                    currentValue.append(" ").append(line.trim());
                }
            }
            if (currentKey != null) {
                result.put(currentKey, currentValue.toString().trim());
            }

            String prompt = content.substring(secondDelim + 3).trim();
            result.put("prompt", prompt);

            return result;
        } catch (IOException e) {
            log.error("读取 SKILL.md 失败: {}", skillMdFile.getAbsolutePath(), e);
            return null;
        }
    }

    public static String buildSkillMdContent(String name, String description, String prompt) {
        StringBuilder sb = new StringBuilder();
        sb.append("---\n");
        sb.append("name: ").append(name).append("\n");
        if (description != null) {
            sb.append("description: ").append(description).append("\n");
        }
        sb.append("---\n\n");
        if (prompt != null) {
            sb.append(prompt);
        }
        return sb.toString();
    }

    public static void writeSkillMdFile(File skillDir, String name, String description, String prompt) throws IOException {
        if (!skillDir.exists()) {
            skillDir.mkdirs();
        }
        File skillMdFile = new File(skillDir, "SKILL.md");
        String content = buildSkillMdContent(name, description, prompt);
        Files.write(skillMdFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
    }
}
