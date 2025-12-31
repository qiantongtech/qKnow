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

package tech.qiantong.qknow.module.ext.service.extSchemaAttribute;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
/**
 * 概念属性Service接口
 *
 * @author qknow
 * @date 2025-02-17
 */
public interface IExtSchemaAttributeService extends IService<ExtSchemaAttributeDO> {

    /**
     * 获得概念属性分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念属性分页列表
     */
    PageResult<ExtSchemaAttributeDO> getExtSchemaAttributePage(ExtSchemaAttributePageReqVO pageReqVO);

    /**
     * 创建概念属性
     *
     * @param createReqVO 概念属性信息
     * @return 概念属性编号
     */
    Long createExtSchemaAttribute(ExtSchemaAttributeSaveReqVO createReqVO);

    /**
     * 更新概念属性
     *
     * @param updateReqVO 概念属性信息
     */
    int updateExtSchemaAttribute(ExtSchemaAttributeSaveReqVO updateReqVO);

    /**
     * 删除概念属性
     *
     * @param idList 概念属性编号
     */
    int removeExtSchemaAttribute(Collection<Long> idList);

    /**
     * 获得概念属性详情
     *
     * @param id 概念属性编号
     * @return 概念属性
     */
    ExtSchemaAttributeDO getExtSchemaAttributeById(Long id);

    /**
     * 获得全部概念属性列表
     *
     * @return 概念属性列表
     */
    List<ExtSchemaAttributeDO> getExtSchemaAttributeList();

    /**
     * 获得全部概念属性 Map
     *
     * @return 概念属性 Map
     */
    Map<Long, ExtSchemaAttributeDO> getExtSchemaAttributeMap();


    /**
     * 导入概念属性数据
     *
     * @param importExcelList 概念属性数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtSchemaAttribute(List<ExtSchemaAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<ExtSchemaAttributeDO> getAllExtSchemaAttributeList(ExtSchemaAttributeDO extSchemaAttributeDO);
}
