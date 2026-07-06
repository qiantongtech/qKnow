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

package tech.qiantong.qknow.module.kmc.convert.kmcDocument;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;

import java.util.List;


/**
 * 知识文件 Convert
 *
 * @author qknow
 * @date 2025-02-14
 */
@Mapper
public interface KmcDocumentConvert {
    KmcDocumentConvert INSTANCE = Mappers.getMapper(KmcDocumentConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kmcDocumentPageReqVO 请求参数
     * @return KmcDocumentDO
     */
    KmcDocumentDO convertToDO(KmcDocumentPageReqVO kmcDocumentPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kmcDocumentSaveReqVO 保存请求参数
     * @return KmcDocumentDO
     */
    KmcDocumentDO convertToDO(KmcDocumentSaveReqVO kmcDocumentSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kmcDocumentDO 实体对象
     * @return KmcDocumentRespVO
     */
    KmcDocumentRespVO convertToRespVO(KmcDocumentDO kmcDocumentDO);

    /**
     * DOList 转换为 RespVOList
     * @param kmcDocumentDOList 实体对象列表
     * @return List<KmcDocumentRespVO>
     */
    List<KmcDocumentRespVO> convertToRespVOList(List<KmcDocumentDO> kmcDocumentDOList);
}
