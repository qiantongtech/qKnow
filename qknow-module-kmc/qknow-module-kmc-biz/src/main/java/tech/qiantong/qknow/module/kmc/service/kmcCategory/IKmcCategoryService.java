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

package tech.qiantong.qknow.module.kmc.service.kmcCategory;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategorySaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;

/**
 * 知识分类Service接口
 *
 * @author qknow
 * @date 2025-02-13
 */
public interface IKmcCategoryService extends IService<KmcCategoryDO> {

    /**
     * 获得知识分类分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识分类分页列表
     */
    PageResult<KmcCategoryDO> getKmcCategoryPage(KmcCategoryPageReqVO pageReqVO);

    /**
     * 创建知识分类
     *
     * @param createReqVO 知识分类信息
     * @return 知识分类编号
     */
    Long createKmcCategory(KmcCategorySaveReqVO createReqVO);

    /**
     * 更新知识分类
     *
     * @param updateReqVO 知识分类信息
     */
    int updateKmcCategory(KmcCategorySaveReqVO updateReqVO);

    /**
     * 删除知识分类
     *
     * @param idList 知识分类编号
     */
    int removeKmcCategory(Collection<Long> idList);

    /**
     * 获得知识分类详情
     *
     * @param id 知识分类编号
     * @return 知识分类
     */
    KmcCategoryDO getKmcCategoryById(Long id);

    /**
     * 获得全部知识分类列表
     *
     * @return 知识分类列表
     */
    List<KmcCategoryDO> getKmcCategoryList();

    /**
     * 获得全部知识分类 Map
     *
     * @return 知识分类 Map
     */
    Map<Long, KmcCategoryDO> getKmcCategoryMap();


    /**
     * 导入知识分类数据
     *
     * @param importExcelList 知识分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcCategory(List<KmcCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获得全部知识分类列表
     *
     * @return 知识分类列表
     */
    List<KmcCategoryDO> getKmcCategoryAllList(KmcCategoryDO kmcCategoryDO);

    /**
     * 获取知识分类树列表
     * @return 知识分类树列表
     */
    List<TreeSelects> selectCategoryTreeList(KmcCategoryDO kmcCategoryDO);

    List<TreeSelects> buildKmcCategoryTreeSelect(List<KmcCategoryDO> kmcCategoryDO);
}
