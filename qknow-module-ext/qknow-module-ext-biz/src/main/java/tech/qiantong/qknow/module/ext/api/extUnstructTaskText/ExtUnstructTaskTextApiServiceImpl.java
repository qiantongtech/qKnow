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

package tech.qiantong.qknow.module.ext.api.extUnstructTaskText;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.api.extUnstructTask.dto.ExtUnstructTaskRespDTO;
import tech.qiantong.qknow.module.ext.api.service.IExtUnstructTaskTextApiService;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskText.IExtUnstructTaskTextService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class ExtUnstructTaskTextApiServiceImpl implements IExtUnstructTaskTextApiService {

    @Resource
    private IExtUnstructTaskTextService extUnstructTaskTextService;


    @Override
    public List<ExtUnstructTaskRespDTO> getUnstructTaskTextList(){
        List<ExtUnstructTaskTextDO> textList = extUnstructTaskTextService.getExtUnstructTaskTextList();
        List<ExtUnstructTaskRespDTO> respDTOList = Lists.newArrayList();
        for (ExtUnstructTaskTextDO extUnstructTaskTextDO : textList) {
            ExtUnstructTaskRespDTO bean = BeanUtils.toBean(extUnstructTaskTextDO, ExtUnstructTaskRespDTO.class);
            respDTOList.add(bean);
        }
        return respDTOList;
    }
}
