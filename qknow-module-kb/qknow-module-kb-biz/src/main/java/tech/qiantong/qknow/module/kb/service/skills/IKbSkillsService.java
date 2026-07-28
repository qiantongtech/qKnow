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

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsBatchImportReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * skillsService接口
 *
 * @author qknow
 * @date 2026-06-17
 */
public interface IKbSkillsService extends IService<KbSkillsDO> {

    /**
     * 获得skills分页列表
     *
     * @param pageReqVO 分页请求
     * @return skills分页列表
     */
    PageResult<KbSkillsDO> getKbSkillsPage(KbSkillsPageReqVO pageReqVO);

    /**
     * 创建skills
     *
     * @param createReqVO skills信息
     * @return skills编号
     */
    Long createKbSkills(KbSkillsSaveReqVO createReqVO);

    /**
     * 更新skills
     *
     * @param updateReqVO skills信息
     */
    int updateKbSkills(KbSkillsSaveReqVO updateReqVO);

    /**
     * 删除skills
     *
     * @param idList skills编号
     */
    int removeKbSkills(Collection<Long> idList);

    /**
     * 获得skills详情
     *
     * @param id skills编号
     * @return skills
     */
    KbSkillsDO getKbSkillsById(Long id);

    /**
     * 获得全部skills列表
     *
     * @return skills列表
     */
    List<KbSkillsDO> getKbSkillsList();

    /**
     * 获得全部skills Map
     *
     * @return skills Map
     */
    Map<Long, KbSkillsDO> getKbSkillsMap();


    /**
     * 导入skills数据
     *
     * @param importExcelList skills数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKbSkills(List<KbSkillsRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 批量创建 skills（从 ZIP 导入）
     *
     * @param reqVO 批量导入请求
     * @return 导入结果
     */
    Map<String, Object> batchCreateKbSkills(KbSkillsBatchImportReqVO reqVO);

    /**
     * 预览 skill 的 SKILL.md 内容
     *
     * @param id skill 编号
     * @return SKILL.md 文件内容
     */
    String previewSkillMd(Long id);

    /**
     * 下载 skill 为 ZIP 包
     *
     * @param id skill 编号
     * @param response HTTP 响应
     */
    void downloadSkill(Long id, jakarta.servlet.http.HttpServletResponse response);

}
