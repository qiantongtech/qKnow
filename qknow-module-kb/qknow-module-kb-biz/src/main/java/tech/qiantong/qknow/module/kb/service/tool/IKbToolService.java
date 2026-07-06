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

package tech.qiantong.qknow.module.kb.service.tool;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolRespVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;
/**
 * 工具管理Service接口
 *
 * @author qknow
 * @date 2026-03-19
 */
public interface IKbToolService extends IService<KbToolDO> {

    /**
     * 获得工具管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 工具管理分页列表
     */
    PageResult<KbToolDO> getKbToolPage(KbToolPageReqVO pageReqVO);

    /**
     * 创建工具管理
     *
     * @param createReqVO 工具管理信息
     * @return 工具管理编号
     */
    Long createKbTool(KbToolSaveReqVO createReqVO);

    /**
     * 更新工具管理
     *
     * @param updateReqVO 工具管理信息
     */
    int updateKbTool(KbToolSaveReqVO updateReqVO);

    /**
     * 删除工具管理
     *
     * @param idList 工具管理编号
     */
    int removeKbTool(Collection<Long> idList);

    /**
     * 获得工具管理详情
     *
     * @param id 工具管理编号
     * @return 工具管理
     */
    KbToolDO getKbToolById(Long id);

    /**
     * 获得全部工具管理列表
     *
     * @return 工具管理列表
     */
    List<KbToolDO> getKbToolList();

    /**
     * 获得全部工具管理 Map
     *
     * @return 工具管理 Map
     */
    Map<Long, KbToolDO> getKbToolMap();


    /**
     * 导入工具管理数据
     *
     * @param importExcelList 工具管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKbTool(List<KbToolRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
