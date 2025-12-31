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

package tech.qiantong.qknow.module.ext.service.extStructTask;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestBody;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTask;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;

/**
 * 结构化抽取任务Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtStructTaskService extends IService<ExtStructTaskDO> {
    /**
     * 执行抽取
     *
     * @param extStructTask
     * @return
     */
    public AjaxResult executeExtraction(ExtStructTaskPageReqVO extStructTask);

    public AjaxResult selectByTaskId(Long taskId, Integer extractType);

    public AjaxResult getAttributeInformation(List<String> list);

    /**
     * 发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult releaseByTaskId(ExtStructTaskDO structTaskDO);

    /**
     * 取消发布
     *
     * @param structTaskDO
     * @return
     */
    public AjaxResult cancelReleaseByTaskId(ExtStructTaskDO structTaskDO);

    /**
     * 新建结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    public AjaxResult addDataMapping(ExtStructTask extStructTask);

    public AjaxResult updateDataMapping(ExtStructTask extStructTask);

    /**
     * 获得结构化抽取任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 结构化抽取任务分页列表
     */
    PageResult<ExtStructTaskDO> getExtStructTaskPage(ExtStructTaskPageReqVO pageReqVO);

    /**
     * 创建结构化抽取任务
     *
     * @param createReqVO 结构化抽取任务信息
     * @return 结构化抽取任务编号
     */
    Long createExtStructTask(ExtStructTaskSaveReqVO createReqVO);

    /**
     * 更新结构化抽取任务
     *
     * @param updateReqVO 结构化抽取任务信息
     */
    int updateExtStructTask(ExtStructTaskSaveReqVO updateReqVO) throws SchedulerException;

    /**
     * 删除结构化抽取任务
     *
     * @param idList 结构化抽取任务编号
     */
    int removeExtStructTask(Collection<Long> idList) throws SchedulerException;

    /**
     * 获得结构化抽取任务详情
     *
     * @param id 结构化抽取任务编号
     * @return 结构化抽取任务
     */
    ExtStructTaskDO getExtStructTaskById(Long id);

    AjaxResult getInfo(Long id);

    /**
     * 获得全部结构化抽取任务列表
     *
     * @return 结构化抽取任务列表
     */
    List<ExtStructTaskDO> getExtStructTaskList();

    /**
     * 获得全部结构化抽取任务 Map
     *
     * @return 结构化抽取任务 Map
     */
    ConcurrentHashMap<Long, ExtStructTaskDO> getExtStructTaskMap();


    /**
     * 导入结构化抽取任务数据
     *
     * @param importExcelList 结构化抽取任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importExtStructTask(List<ExtStructTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);



    /**
     * 定时任务立即执行一次
     *
     * @param extStructTask
     * @return
     */
    void  runStructTask(ExtStructTask extStructTask) throws Exception;
}
