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

package tech.qiantong.qknow.module.kmc.convert.kmcDocumentLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcDocumentLog.KmcDocumentLogDO;

import java.util.List;

/**
 * 文件操作日志 Convert
 *
 * @author qknow
 * @date 2025-03-24
 */
@Mapper
public interface KmcDocumentLogConvert {
    KmcDocumentLogConvert INSTANCE = Mappers.getMapper(KmcDocumentLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kmcDocumentLogPageReqVO 请求参数
     * @return KmcDocumentLogDO
     */
    KmcDocumentLogDO convertToDO(KmcDocumentLogPageReqVO kmcDocumentLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kmcDocumentLogSaveReqVO 保存请求参数
     * @return KmcDocumentLogDO
     */
    KmcDocumentLogDO convertToDO(KmcDocumentLogSaveReqVO kmcDocumentLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kmcDocumentLogDO 实体对象
     * @return KmcDocumentLogRespVO
     */
    KmcDocumentLogRespVO convertToRespVO(KmcDocumentLogDO kmcDocumentLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param kmcDocumentLogDOList 实体对象列表
     * @return List<KmcDocumentLogRespVO>
     */
    List<KmcDocumentLogRespVO> convertToRespVOList(List<KmcDocumentLogDO> kmcDocumentLogDOList);
}
