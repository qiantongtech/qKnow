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

package tech.qiantong.qknow.module.kb.service.skills.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.constant.Constants;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.file.util.FileUploadUtil;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsBatchImportReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;
import tech.qiantong.qknow.module.kb.dal.mapper.skills.KbSkillsMapper;
import tech.qiantong.qknow.module.kb.service.skills.IKbSkillsService;
import tech.qiantong.qknow.module.kb.service.skills.SkillFileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * skillsService业务层处理
 *
 * @author qknow
 * @date 2026-06-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbSkillsServiceImpl  extends ServiceImpl<KbSkillsMapper,KbSkillsDO> implements IKbSkillsService {
    @Resource
    private KbSkillsMapper kbSkillsMapper;

    @Override
    public PageResult<KbSkillsDO> getKbSkillsPage(KbSkillsPageReqVO pageReqVO) {
        return kbSkillsMapper.selectPage(pageReqVO);
    }

    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String storagePath;

    @Override
    public Long createKbSkills(KbSkillsSaveReqVO createReqVO) {
        KbSkillsDO dictType = BeanUtils.toBean(createReqVO, KbSkillsDO.class);
        String skillName = dictType.getName();

        // 检查名称是否已存在
        QueryWrapper<KbSkillsDO> wrapper = new QueryWrapper<>();
        wrapper.eq("name", skillName);
        if (kbSkillsMapper.selectCount(wrapper) > 0) {
            throw new ServiceException("技能名称已存在：" + skillName);
        }

        String filePath = "/skills/" + skillName;
        dictType.setFilePath(filePath);

        // 生成 SKILL.md 文件
        java.io.File skillDir = new java.io.File(storagePath + "/skills/" + skillName);
        try {
            SkillFileUtil.writeSkillMdFile(skillDir, skillName, dictType.getDescription(), dictType.getPrompt());
        } catch (IOException e) {
            log.error("生成 SKILL.md 失败: {}", skillName, e);
            throw new ServiceException("生成 SKILL.md 失败: " + e.getMessage());
        }
        dictType.setStatus(0);
        kbSkillsMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKbSkills(KbSkillsSaveReqVO updateReqVO) {
        KbSkillsDO existing = kbSkillsMapper.selectById(updateReqVO.getId());
        if (existing == null) {
            throw new ServiceException("skills 不存在");
        }

        KbSkillsDO updateObj = BeanUtils.toBean(updateReqVO, KbSkillsDO.class);
        String oldName = existing.getName();
        String newName = updateObj.getName();
        String oldFilePath = existing.getFilePath();

        // 如果名称变更，重命名目录
        if (StringUtils.isNotEmpty(newName) && !newName.equals(oldName)) {
            // 检查新名称是否已被其他 skill 占用
            QueryWrapper<KbSkillsDO> wrapper = new QueryWrapper<>();
            wrapper.eq("name", newName);
            wrapper.ne("id", updateReqVO.getId());
            if (kbSkillsMapper.selectCount(wrapper) > 0) {
                throw new ServiceException("技能名称已存在：" + newName);
            }

            java.io.File oldDir = new java.io.File(storagePath + "/skills/" + oldName);
            java.io.File newDir = new java.io.File(storagePath + "/skills/" + newName);
            if (oldDir.exists()) {
                if (!oldDir.renameTo(newDir)) {
                    log.error("重命名 skill 目录失败: {} -> {}", oldDir.getAbsolutePath(), newDir.getAbsolutePath());
                    throw new ServiceException("重命名 skill 目录失败");
                }
            }
            updateObj.setFilePath("/skills/" + newName);
        } else {
            updateObj.setFilePath(oldFilePath);
        }

        // 更新 SKILL.md 文件
        String skillName = StringUtils.isNotEmpty(newName) ? newName : oldName;
        java.io.File skillDir = new java.io.File(storagePath + "/skills/" + skillName);
        try {
            SkillFileUtil.writeSkillMdFile(skillDir, skillName, updateObj.getDescription(), updateObj.getPrompt());
        } catch (IOException e) {
            log.error("更新 SKILL.md 失败: {}", skillName, e);
            throw new ServiceException("更新 SKILL.md 失败: " + e.getMessage());
        }

        return kbSkillsMapper.updateById(updateObj);
    }
    @Override
    public int removeKbSkills(Collection<Long> idList) {
        // 删除 skills 对应的本地目录
        List<KbSkillsDO> skills = kbSkillsMapper.selectBatchIds(idList);
        if (skills != null) {
            for (KbSkillsDO skill : skills) {
                String filePath = skill.getFilePath();
                if (StringUtils.isEmpty(filePath)) {
                    continue;
                }
                // 去掉资源前缀，得到实际相对路径 /skills/{name}
                String relativePath = filePath;
                if (relativePath.startsWith(Constants.RESOURCE_PREFIX)) {
                    relativePath = relativePath.substring(Constants.RESOURCE_PREFIX.length());
                }
                java.io.File skillDir = new java.io.File(storagePath + relativePath);
                if (skillDir.exists()) {
                    try {
                        FileUploadUtil.deleteDirectory(skillDir);
                    } catch (Exception e) {
                        log.error("删除 skill 目录失败: {}", skillDir.getAbsolutePath(), e);
                    }
                }
            }
        }

        // 批量删除skills
        return kbSkillsMapper.deleteBatchIds(idList);
    }

    @Override
    public KbSkillsDO getKbSkillsById(Long id) {
        return kbSkillsMapper.selectById(id);
    }

    @Override
    public List<KbSkillsDO> getKbSkillsList() {
        return kbSkillsMapper.selectList();
    }

    @Override
    public Map<Long, KbSkillsDO> getKbSkillsMap() {
        List<KbSkillsDO> kbSkillsList = kbSkillsMapper.selectList();
        return kbSkillsList.stream()
                .collect(Collectors.toMap(
                        KbSkillsDO::getId,
                        kbSkillsDO -> kbSkillsDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入skills数据
         *
         * @param importExcelList skills数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKbSkills(List<KbSkillsRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KbSkillsRespVO respVO : importExcelList) {
                try {
                    KbSkillsDO kbSkillsDO = BeanUtils.toBean(respVO, KbSkillsDO.class);
                    Long kbSkillsId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kbSkillsId != null) {
                            KbSkillsDO existingKbSkills = kbSkillsMapper.selectById(kbSkillsId);
                            if (existingKbSkills != null) {
                                kbSkillsMapper.updateById(kbSkillsDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kbSkillsId + " 的skills记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kbSkillsId + " 的skills记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KbSkillsDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kbSkillsId);
                        KbSkillsDO existingKbSkills = kbSkillsMapper.selectOne(queryWrapper);
                        if (existingKbSkills == null) {
                            kbSkillsMapper.insert(kbSkillsDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kbSkillsId + " 的skills记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kbSkillsId + " 的skills记录已存在。");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
            }
            return resultMsg.toString();
        }

    @Override
    public Map<String, Object> batchCreateKbSkills(KbSkillsBatchImportReqVO reqVO) {
        // 1. 调用 SkillFileUtil 批量提取并解析 skills
        List<Map<String, String>> skillList = SkillFileUtil.batchExtractAndParseSkills(
                storagePath,
                reqVO.getFileUrl(),
                reqVO.getOriginalFilename(),
                reqVO.getPlatForm()
        );

        if (skillList == null || skillList.isEmpty()) {
            throw new ServiceException("未从 ZIP 中解析到任何有效的 skill");
        }

        int successCount = 0;
        int skipCount = 0;
        List<String> successNames = new ArrayList<>();
        List<Map<String, String>> skipInfos = new ArrayList<>();

        // 2. 逐个插入数据库
        for (Map<String, String> skillInfo : skillList) {
            String skillName = skillInfo.get("name");
            String description = skillInfo.get("description");
            String prompt = skillInfo.get("prompt");
            String filePath = skillInfo.get("filePath");
            String status = skillInfo.get("status");
            String reason = skillInfo.get("reason");

            // 文件层已标记为跳过
            if ("skip".equals(status)) {
                skipCount++;
                skipInfos.add(buildSkipInfo(skillName, reason));
                continue;
            }

            // 检查是否已存在同名 skill
            QueryWrapper<KbSkillsDO> wrapper = new QueryWrapper<>();
            wrapper.eq("name", skillName);
            if (kbSkillsMapper.selectCount(wrapper) > 0) {
                log.warn("技能 \"{}\" 已存在，跳过", skillName);
                skipCount++;
                skipInfos.add(buildSkipInfo(skillName, "技能已存在"));
                continue;
            }

            KbSkillsDO skillsDO = new KbSkillsDO();
            skillsDO.setName(skillName);
            skillsDO.setDescription(description != null && description.length() > 512
                    ? description.substring(0, 512) : description);
            skillsDO.setPrompt(prompt);
            skillsDO.setFilePath(filePath);
            skillsDO.setWorkspaceId(reqVO.getWorkspaceId());
            skillsDO.setCreateBy(reqVO.getCreateBy());
            skillsDO.setCreatorId(reqVO.getCreatorId());
            skillsDO.setStatus(0);

            kbSkillsMapper.insert(skillsDO);
            successCount++;
            successNames.add(skillName);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("skipCount", skipCount);
        result.put("successNames", successNames);
        result.put("skipInfos", skipInfos);
        result.put("totalCount", skillList.size());
        return result;
    }

    private Map<String, String> buildSkipInfo(String name, String reason) {
        Map<String, String> info = new HashMap<>();
        info.put("name", name);
        info.put("reason", reason);
        return info;
    }

    @Override
    public String previewSkillMd(Long id) {
        KbSkillsDO skill = kbSkillsMapper.selectById(id);
        if (skill == null) {
            throw new ServiceException("skills 不存在");
        }
        String filePath = skill.getFilePath();
        if (StringUtils.isEmpty(filePath)) {
            throw new ServiceException("该 skill 未关联文件路径");
        }
        String relativePath = filePath;
        if (relativePath.startsWith(Constants.RESOURCE_PREFIX)) {
            relativePath = relativePath.substring(Constants.RESOURCE_PREFIX.length());
        }
        java.io.File skillMdFile = new java.io.File(storagePath + relativePath + "/SKILL.md");
        if (!skillMdFile.exists()) {
            throw new ServiceException("SKILL.md 文件不存在: " + skillMdFile.getAbsolutePath());
        }
        try {
            return Files.readString(skillMdFile.toPath());
        } catch (IOException e) {
            log.error("读取 SKILL.md 失败: {}", skillMdFile.getAbsolutePath(), e);
            throw new ServiceException("读取 SKILL.md 失败: " + e.getMessage());
        }
    }

    @Override
    public void downloadSkill(Long id, HttpServletResponse response) {
        KbSkillsDO skill = kbSkillsMapper.selectById(id);
        if (skill == null) {
            throw new ServiceException("skills 不存在");
        }
        String filePath = skill.getFilePath();
        if (StringUtils.isEmpty(filePath)) {
            throw new ServiceException("该 skill 未关联文件路径");
        }
        String relativePath = filePath;
        if (relativePath.startsWith(Constants.RESOURCE_PREFIX)) {
            relativePath = relativePath.substring(Constants.RESOURCE_PREFIX.length());
        }
        java.io.File skillDir = new java.io.File(storagePath + relativePath);
        if (!skillDir.exists() || !skillDir.isDirectory()) {
            throw new ServiceException("skill 目录不存在: " + skillDir.getAbsolutePath());
        }

        String zipName = skill.getName() + ".zip";
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipName + "\"");

        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            zipDirectory(skillDir, skillDir.getName(), zos);
        } catch (IOException e) {
            log.error("打包 skill 目录失败: {}", skillDir.getAbsolutePath(), e);
            throw new ServiceException("下载 skill 失败: " + e.getMessage());
        }
    }

    private void zipDirectory(java.io.File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.closeEntry();
            } else {
                zos.putNextEntry(new ZipEntry(fileName + "/"));
                zos.closeEntry();
            }
            java.io.File[] children = fileToZip.listFiles();
            if (children != null) {
                for (java.io.File childFile : children) {
                    zipDirectory(childFile, fileName + "/" + childFile.getName(), zos);
                }
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
        }
    }
}
