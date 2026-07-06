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

package tech.qiantong.qknow.module.ext.convert.extTaskLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDO;

import java.util.List;

/**
 * 抽取任务执行日志 Convert
 *
 * @author qknow
 * @date 2025-12-03
 */
@Mapper
public interface ExtTaskLogConvert {
    ExtTaskLogConvert INSTANCE = Mappers.getMapper(ExtTaskLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extTaskLogPageReqVO 请求参数
     * @return ExtTaskLogDO
     */
     ExtTaskLogDO convertToDO(ExtTaskLogPageReqVO extTaskLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extTaskLogSaveReqVO 保存请求参数
     * @return ExtTaskLogDO
     */
     ExtTaskLogDO convertToDO(ExtTaskLogSaveReqVO extTaskLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extTaskLogDO 实体对象
     * @return ExtTaskLogRespVO
     */
     ExtTaskLogRespVO convertToRespVO(ExtTaskLogDO extTaskLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param extTaskLogDOList 实体对象列表
     * @return List<ExtTaskLogRespVO>
     */
     List<ExtTaskLogRespVO> convertToRespVOList(List<ExtTaskLogDO> extTaskLogDOList);
}
