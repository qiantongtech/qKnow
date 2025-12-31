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

package tech.qiantong.qknow.module.dm.convert.datasource;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourcePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.datasource.DmDatasourceDO;


import java.util.List;

/**
 * 数据源 Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DmDatasourceConvert {
    DmDatasourceConvert INSTANCE = Mappers.getMapper(DmDatasourceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDatasourcePageReqVO 请求参数
     * @return DaDatasourceDO
     */
     DmDatasourceDO convertToDO(DmDatasourcePageReqVO daDatasourcePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDatasourceSaveReqVO 保存请求参数
     * @return DaDatasourceDO
     */
     DmDatasourceDO convertToDO(DmDatasourceSaveReqVO daDatasourceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDatasourceDO 实体对象
     * @return DaDatasourceRespVO
     */
     DmDatasourceRespVO convertToRespVO(DmDatasourceDO daDatasourceDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDatasourceDOList 实体对象列表
     * @return List<DaDatasourceRespVO>
     */
     List<DmDatasourceRespVO> convertToRespVOList(List<DmDatasourceDO> daDatasourceDOList);
}
