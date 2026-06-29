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

package tech.qiantong.qknow.module.ext.convert.extUnstructTaskText;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;

/**
 * 任务文件段落关联 Convert
 *
 * @author qknow
 * @date 2025-02-21
 */
@Mapper
public interface ExtUnstructTaskTextConvert {
    ExtUnstructTaskTextConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskTextConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskTextPageReqVO 请求参数
     * @return ExtUnstructTaskTextDO
     */
     ExtUnstructTaskTextDO convertToDO(ExtUnstructTaskTextPageReqVO extUnstructTaskTextPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskTextSaveReqVO 保存请求参数
     * @return ExtUnstructTaskTextDO
     */
     ExtUnstructTaskTextDO convertToDO(ExtUnstructTaskTextSaveReqVO extUnstructTaskTextSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskTextDO 实体对象
     * @return ExtUnstructTaskTextRespVO
     */
     ExtUnstructTaskTextRespVO convertToRespVO(ExtUnstructTaskTextDO extUnstructTaskTextDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskTextDOList 实体对象列表
     * @return List<ExtUnstructTaskTextRespVO>
     */
     List<ExtUnstructTaskTextRespVO> convertToRespVOList(List<ExtUnstructTaskTextDO> extUnstructTaskTextDOList);
}
