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

package tech.qiantong.qknow.module.ext.service.extAttributeMapping;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;
/**
 * 属性映射Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtAttributeMappingService extends IService<ExtAttributeMappingDO> {

    /**
     * 获得属性映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 属性映射分页列表
     */
    PageResult<ExtAttributeMappingDO> getExtAttributeMappingPage(ExtAttributeMappingPageReqVO pageReqVO);

    /**
     * 创建属性映射
     *
     * @param createReqVO 属性映射信息
     * @return 属性映射编号
     */
    Long createExtAttributeMapping(ExtAttributeMappingSaveReqVO createReqVO);

    /**
     * 更新属性映射
     *
     * @param updateReqVO 属性映射信息
     */
    int updateExtAttributeMapping(ExtAttributeMappingSaveReqVO updateReqVO);

    /**
     * 删除属性映射
     *
     * @param idList 属性映射编号
     */
    int removeExtAttributeMapping(Collection<Long> idList);

    /**
     * 获得属性映射详情
     *
     * @param id 属性映射编号
     * @return 属性映射
     */
    ExtAttributeMappingDO getExtAttributeMappingById(Long id);

    /**
     * 获得全部属性映射列表
     *
     * @return 属性映射列表
     */
    List<ExtAttributeMappingDO> getExtAttributeMappingList();

    /**
     * 获得全部属性映射 Map
     *
     * @return 属性映射 Map
     */
    Map<Long, ExtAttributeMappingDO> getExtAttributeMappingMap();


    /**
     * 导入属性映射数据
     *
     * @param importExcelList 属性映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtAttributeMapping(List<ExtAttributeMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
