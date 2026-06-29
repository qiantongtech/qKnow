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

package tech.qiantong.qknow.module.ext.api.extSchemaAttribute;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.api.extSchemaAttribute.dto.ExtSchemaAttributeReqDTO;
import tech.qiantong.qknow.module.ext.api.extSchemaAttribute.dto.ExtSchemaAttributeRespDTO;
import tech.qiantong.qknow.module.ext.api.service.IExtAttributeApiService;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class ExtAttributeApiServiceImpl implements IExtAttributeApiService {
    @Resource
    private IExtSchemaAttributeService extSchemaAttributeService;

    public List<ExtSchemaAttributeRespDTO> getExtSchemaAttributeList() {
        List<ExtSchemaAttributeDO> extSchemaAttributeList = extSchemaAttributeService.getExtSchemaAttributeList();
        List<ExtSchemaAttributeRespDTO> attributeList = Lists.newArrayList();
        for (ExtSchemaAttributeDO attributeReqDTO : extSchemaAttributeList) {
            ExtSchemaAttributeRespDTO bean = BeanUtils.toBean(attributeReqDTO, ExtSchemaAttributeRespDTO.class);
            attributeList.add(bean);
        }
        return attributeList;
    }


}
