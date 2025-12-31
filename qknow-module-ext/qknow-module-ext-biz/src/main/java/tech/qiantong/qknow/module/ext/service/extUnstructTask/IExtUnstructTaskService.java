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

package tech.qiantong.qknow.module.ext.service.extUnstructTask;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;

/**
 * 非结构化抽取任务Service接口
 *
 * @author qknow
 * @date 2025-02-18
 */
public interface IExtUnstructTaskService extends IService<ExtUnstructTaskDO> {

    public void consumeQueue();

    public AjaxResult executeExtraction(ExtUnstructTaskSaveReqVO createReqVO);

    public AjaxResult getExtExtraction(ExtExtractionDO extExtractionDO);

    /**
     * 获得非结构化抽取任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 非结构化抽取任务分页列表
     */
    PageResult<ExtUnstructTaskDO> getExtUnstructTaskPage(ExtUnstructTaskPageReqVO pageReqVO);

    /**
     * 创建非结构化抽取任务
     *
     * @param createReqVO 非结构化抽取任务信息
     * @return 非结构化抽取任务编号
     */
    Long createExtUnstructTask(ExtUnstructTaskSaveReqVO createReqVO);

    /**
     * 更新非结构化抽取任务
     *
     * @param updateReqVO 非结构化抽取任务信息
     */
    int updateExtUnstructTask(ExtUnstructTaskSaveReqVO updateReqVO);

    /**
     * 删除非结构化抽取任务
     *
     * @param idList 非结构化抽取任务编号
     */
    int removeExtUnstructTask(Collection<Long> idList);

    /**
     * 获得非结构化抽取任务详情
     *
     * @param id 非结构化抽取任务编号
     * @return 非结构化抽取任务
     */
    ExtUnstructTaskDO getExtUnstructTaskById(Long id);

    /**
     * 发布
     *
     * @param
     * @return
     */
    public AjaxResult releaseByTaskId(ExtUnstructTaskDO unstructTaskDO);

    /**
     * 取消发布
     *
     * @param
     * @return
     */
    public AjaxResult cancelReleaseByTaskId(ExtUnstructTaskDO unstructTaskDO);

    /**
     * 导入非结构化抽取任务数据
     *
     * @param importExcelList 非结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtUnstructTask(List<ExtUnstructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
