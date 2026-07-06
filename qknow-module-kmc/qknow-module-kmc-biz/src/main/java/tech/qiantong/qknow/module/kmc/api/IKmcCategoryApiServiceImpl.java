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

package tech.qiantong.qknow.module.kmc.api;

import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kmc.api.kmcCategory.dto.KmcCategoryRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcCategoryApiService;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author zyg
 * @version 1.0.0
 * <p>
 * class description goes here
 * descritpion:
 * @date 2025/12/11 11:02
 */
@Service
public class IKmcCategoryApiServiceImpl implements IKmcCategoryApiService {
    @Resource
    private IKmcCategoryService categoryService;

    /**
     * 获取知识库文件分类列表
     */

    public List<KmcCategoryRespDTO> kmcCategoryList(){
        List<KmcCategoryDO> kmcCategoryList = categoryService.getKmcCategoryList();
        List<KmcCategoryRespDTO> dtos = BeanUtils.toBean(kmcCategoryList, KmcCategoryRespDTO.class);
        return dtos;
    }
}
