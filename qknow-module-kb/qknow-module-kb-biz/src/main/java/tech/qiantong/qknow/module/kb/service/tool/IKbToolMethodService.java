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
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodSaveReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodRespVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
/**
 * 工具方法Service接口
 *
 * @author qknow
 * @date 2026-03-19
 */
public interface IKbToolMethodService extends IService<KbToolMethodDO> {

    /**
     * 获得工具方法分页列表
     *
     * @param pageReqVO 分页请求
     * @return 工具方法分页列表
     */
    PageResult<KbToolMethodDO> getKbToolMethodPage(KbToolMethodPageReqVO pageReqVO);

    /**
     * 创建工具方法
     *
     * @param createReqVO 工具方法信息
     * @return 工具方法编号
     */
    Long createKbToolMethod(KbToolMethodSaveReqVO createReqVO);

    /**
     * 更新工具方法
     *
     * @param updateReqVO 工具方法信息
     */
    int updateKbToolMethod(KbToolMethodSaveReqVO updateReqVO);

    /**
     * 删除工具方法
     *
     * @param idList 工具方法编号
     */
    int removeKbToolMethod(Collection<Long> idList);

    /**
     * 获得工具方法详情
     *
     * @param id 工具方法编号
     * @return 工具方法
     */
    KbToolMethodDO getKbToolMethodById(Long id);

    /**
     * 获得全部工具方法列表
     *
     * @return 工具方法列表
     */
    List<KbToolMethodDO> getKbToolMethodList();

    /**
     * 获得全部工具方法 Map
     *
     * @return 工具方法 Map
     */
    Map<Long, KbToolMethodDO> getKbToolMethodMap();


    /**
     * 导入工具方法数据
     *
     * @param importExcelList 工具方法数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKbToolMethod(List<KbToolMethodRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
