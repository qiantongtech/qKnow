/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.ext.service.unstructTaskRelation;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 任务文件关联Service接口
 *
 * @author qknow
 * @date 2025-04-03
 */
public interface IExtUnstructTaskRelationService extends IService<ExtUnstructTaskRelationDO> {

    /**
     * 获得任务文件关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 任务文件关联分页列表
     */
    PageResult<ExtUnstructTaskRelationDO> getExtUnstructTaskRelationPage(ExtUnstructTaskRelationPageReqVO pageReqVO);

    /**
     * 创建任务文件关联
     *
     * @param createReqVO 任务文件关联信息
     * @return 任务文件关联编号
     */
    Long createExtUnstructTaskRelation(ExtUnstructTaskRelationSaveReqVO createReqVO);

    /**
     * 更新任务文件关联
     *
     * @param updateReqVO 任务文件关联信息
     */
    int updateExtUnstructTaskRelation(ExtUnstructTaskRelationSaveReqVO updateReqVO);

    /**
     * 删除任务文件关联
     *
     * @param idList 任务文件关联编号
     */
    int removeExtUnstructTaskRelation(Collection<Long> idList);

    /**
     * 获得任务文件关联详情
     *
     * @param id 任务文件关联编号
     * @return 任务文件关联
     */
    ExtUnstructTaskRelationDO getExtUnstructTaskRelationById(Long id);

    /**
     * 获得全部任务文件关联列表
     *
     * @return 任务文件关联列表
     */
    List<ExtUnstructTaskRelationDO> getExtUnstructTaskRelationList();

    /**
     * 获得全部任务文件关联 Map
     *
     * @return 任务文件关联 Map
     */
    Map<Long, ExtUnstructTaskRelationDO> getExtUnstructTaskRelationMap();


    /**
     * 导入任务文件关联数据
     *
     * @param importExcelList 任务文件关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtUnstructTaskRelation(List<ExtUnstructTaskRelationRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<ExtUnstructTaskRelationDO> findByTaskId(Long taskId);
}
