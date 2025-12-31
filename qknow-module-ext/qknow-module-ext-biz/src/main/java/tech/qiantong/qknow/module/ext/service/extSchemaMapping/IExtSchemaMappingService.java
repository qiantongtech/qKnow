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

package tech.qiantong.qknow.module.ext.service.extSchemaMapping;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;
/**
 * 概念映射Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtSchemaMappingService extends IService<ExtSchemaMappingDO> {

    /**
     * 获得概念映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念映射分页列表
     */
    PageResult<ExtSchemaMappingDO> getExtSchemaMappingPage(ExtSchemaMappingPageReqVO pageReqVO);

    /**
     * 创建概念映射
     *
     * @param createReqVO 概念映射信息
     * @return 概念映射编号
     */
    Long createExtSchemaMapping(ExtSchemaMappingSaveReqVO createReqVO);

    /**
     * 更新概念映射
     *
     * @param updateReqVO 概念映射信息
     */
    int updateExtSchemaMapping(ExtSchemaMappingSaveReqVO updateReqVO);

    /**
     * 删除概念映射
     *
     * @param idList 概念映射编号
     */
    int removeExtSchemaMapping(Collection<Long> idList);

    /**
     * 获得概念映射详情
     *
     * @param id 概念映射编号
     * @return 概念映射
     */
    ExtSchemaMappingDO getExtSchemaMappingById(Long id);

    /**
     * 获得全部概念映射列表
     *
     * @return 概念映射列表
     */
    List<ExtSchemaMappingDO> getExtSchemaMappingList();

    /**
     * 获得全部概念映射 Map
     *
     * @return 概念映射 Map
     */
    Map<Long, ExtSchemaMappingDO> getExtSchemaMappingMap();


    /**
     * 导入概念映射数据
     *
     * @param importExcelList 概念映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtSchemaMapping(List<ExtSchemaMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
