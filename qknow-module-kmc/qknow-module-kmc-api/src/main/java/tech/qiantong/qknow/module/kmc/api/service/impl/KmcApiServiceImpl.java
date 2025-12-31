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

package tech.qiantong.qknow.module.kmc.api.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KmcApiServiceImpl implements IKmcApiService {

    @Resource
    public IKmcDocumentService kmcDocumentService;
    @Resource
    public IKmcCategoryService kmcCategoryService;

    @Override
    public List<KmcDocumentDO> getKmcDocumentList(){
        return kmcDocumentService.getKmcDocumentList();
    }

    @Override
    public List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids){
        if (ids.isEmpty()) {
            return Lists.newArrayList();
        }
        return kmcDocumentService.getKmcDocumentListByIds(ids);
    }

    @Override
    public List<TreeSelects> getCategoryTreeList(KmcCategoryDO kmcCategoryDO) {
        return kmcCategoryService.selectCategoryTreeList(kmcCategoryDO);
    }
}
