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

package tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;
/**
 * 任务文件关联Service接口
 *
 * @author qknow
 * @date 2025-02-19
 */
public interface IExtUnstructTaskDocRelService extends IService<ExtUnstructTaskDocRelDO> {

    /**
     * 获得任务文件关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 任务文件关联分页列表
     */
    PageResult<ExtUnstructTaskDocRelDO> getExtUnstructTaskDocRelPage(ExtUnstructTaskDocRelPageReqVO pageReqVO);

    /**
     * 创建任务文件关联
     *
     * @param createReqVO 任务文件关联信息
     * @return 任务文件关联编号
     */
    Long createExtUnstructTaskDocRel(ExtUnstructTaskDocRelSaveReqVO createReqVO);

    /**
     * 更新任务文件关联
     *
     * @param updateReqVO 任务文件关联信息
     */
    int updateExtUnstructTaskDocRel(ExtUnstructTaskDocRelSaveReqVO updateReqVO);

    /**
     * 删除任务文件关联
     *
     * @param idList 任务文件关联编号
     */
    int removeExtUnstructTaskDocRel(Collection<Long> idList);

    /**
     * 获得任务文件关联详情
     *
     * @param id 任务文件关联编号
     * @return 任务文件关联
     */
    ExtUnstructTaskDocRelDO getExtUnstructTaskDocRelById(Long id);

    /**
     * 获得全部任务文件关联列表
     *
     * @return 任务文件关联列表
     */
    List<ExtUnstructTaskDocRelDO> getExtUnstructTaskDocRelList();

    /**
     * 获得全部任务文件关联 Map
     *
     * @return 任务文件关联 Map
     */
    Map<Long, ExtUnstructTaskDocRelDO> getExtUnstructTaskDocRelMap();


    /**
     * 导入任务文件关联数据
     *
     * @param importExcelList 任务文件关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtUnstructTaskDocRel(List<ExtUnstructTaskDocRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
