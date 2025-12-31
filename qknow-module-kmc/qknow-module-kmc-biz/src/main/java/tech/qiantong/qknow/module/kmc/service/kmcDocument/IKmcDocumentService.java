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

package tech.qiantong.qknow.module.kmc.service.kmcDocument;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;

/**
 * 知识文件Service接口
 *
 * @author qknow
 * @date 2025-02-14
 */
public interface IKmcDocumentService extends IService<KmcDocumentDO> {

    /**
     * 获得知识文件分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识文件分页列表
     */
    PageResult<KmcDocumentDO> getKmcDocumentPage(KmcDocumentPageReqVO pageReqVO);

    List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids);

    /**
     * 创建知识文件
     *
     * @param createReqVO 知识文件信息
     * @return 知识文件编号
     */
    Long createKmcDocument(KmcDocumentSaveReqVO createReqVO);

    /**
     * 更新知识文件
     *
     * @param updateReqVO 知识文件信息
     */
    int updateKmcDocument(KmcDocumentSaveReqVO updateReqVO);

    /**
     * 删除知识文件
     *
     * @param idList 知识文件编号
     */
    int removeKmcDocument(Collection<Long> idList);

    /**
     * 获得知识文件详情
     *
     * @param id 知识文件编号
     * @return 知识文件
     */
    KmcDocumentDO getKmcDocumentById(Long id);

    /**
     * 获得全部知识文件列表
     *
     * @return 知识文件列表
     */
    List<KmcDocumentDO> getKmcDocumentList();

    /**
     * 获得全部知识文件 Map
     *
     * @return 知识文件 Map
     */
    Map<Long, KmcDocumentDO> getKmcDocumentMap();


    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcDocument(List<KmcDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    File getFileByHttpURL(String path);

    List<KmcDocumentDO> selectList(KmcDocumentPageReqVO kmcDocument);

    List<Map<String, Object>> getFileTypes();
}
