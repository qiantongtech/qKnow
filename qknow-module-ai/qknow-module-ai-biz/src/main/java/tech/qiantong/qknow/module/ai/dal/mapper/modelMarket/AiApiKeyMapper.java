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

package tech.qiantong.qknow.module.ai.dal.mapper.modelMarket;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyPageReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;

/**
 * API秘钥Mapper接口
 *
 * @author qknow
 * @date 2025-12-23
 */
public interface AiApiKeyMapper extends BaseMapperX<AiApiKeyDO> {
    @Select({"""
            <script>
            select aak.platform
            from ai_api_key aak
            where aak.del_flag = 0
            	and aak.valid_flag = 1
            	and aak .status <![CDATA[ <> ]]> 0
            <if test='reqVO.name != null and reqVO.name != "" ' >
                and aak.name like concat('%',#{reqVO.name},'%') \s
            </if>
            group by aak.platform
            </script>
            """})
    Page<AiApiKeyPageReqVO> selectPage(IPage<AiApiKeyPageReqVO> page,
                                @Param("reqVO") AiApiKeyPageReqVO reqVO);
}
