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

package tech.qiantong.qknow.module.ext.convert.extUnstructTaskDocRel;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;

/**
 * 任务文件关联 Convert
 *
 * @author qknow
 * @date 2025-02-19
 */
@Mapper
public interface ExtUnstructTaskDocRelConvert {
    ExtUnstructTaskDocRelConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskDocRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskDocRelPageReqVO 请求参数
     * @return ExtUnstructTaskDocRelDO
     */
     ExtUnstructTaskDocRelDO convertToDO(ExtUnstructTaskDocRelPageReqVO extUnstructTaskDocRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskDocRelSaveReqVO 保存请求参数
     * @return ExtUnstructTaskDocRelDO
     */
     ExtUnstructTaskDocRelDO convertToDO(ExtUnstructTaskDocRelSaveReqVO extUnstructTaskDocRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskDocRelDO 实体对象
     * @return ExtUnstructTaskDocRelRespVO
     */
     ExtUnstructTaskDocRelRespVO convertToRespVO(ExtUnstructTaskDocRelDO extUnstructTaskDocRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskDocRelDOList 实体对象列表
     * @return List<ExtUnstructTaskDocRelRespVO>
     */
     List<ExtUnstructTaskDocRelRespVO> convertToRespVOList(List<ExtUnstructTaskDocRelDO> extUnstructTaskDocRelDOList);
}
