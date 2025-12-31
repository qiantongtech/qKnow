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

package tech.qiantong.qknow.module.ext.convert.unstructTaskRelation;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;

import java.util.List;

/**
 * 任务文件关联 Convert
 *
 * @author qknow
 * @date 2025-04-03
 */
@Mapper
public interface ExtUnstructTaskRelationConvert {
    ExtUnstructTaskRelationConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskRelationConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskRelationPageReqVO 请求参数
     * @return ExtUnstructTaskRelationDO
     */
     ExtUnstructTaskRelationDO convertToDO(ExtUnstructTaskRelationPageReqVO extUnstructTaskRelationPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskRelationSaveReqVO 保存请求参数
     * @return ExtUnstructTaskRelationDO
     */
     ExtUnstructTaskRelationDO convertToDO(ExtUnstructTaskRelationSaveReqVO extUnstructTaskRelationSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskRelationDO 实体对象
     * @return ExtUnstructTaskRelationRespVO
     */
     ExtUnstructTaskRelationRespVO convertToRespVO(ExtUnstructTaskRelationDO extUnstructTaskRelationDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskRelationDOList 实体对象列表
     * @return List<ExtUnstructTaskRelationRespVO>
     */
     List<ExtUnstructTaskRelationRespVO> convertToRespVOList(List<ExtUnstructTaskRelationDO> extUnstructTaskRelationDOList);
}
