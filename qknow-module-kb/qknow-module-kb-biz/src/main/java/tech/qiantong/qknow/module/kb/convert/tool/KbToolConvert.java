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

package tech.qiantong.qknow.module.kb.convert.tool;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;

import java.util.List;
import java.util.Set;

/**
 * 工具管理 Convert
 *
 * @author qknow
 * @date 2026-03-19
 */
@Mapper
public interface KbToolConvert {
    KbToolConvert INSTANCE = Mappers.getMapper(KbToolConvert.class);

    /**
     * PageReqVO 转换为 DO
     *
     * @param kbToolPageReqVO 请求参数
     * @return KbToolDO
     */
    KbToolDO convertToDO(KbToolPageReqVO kbToolPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     *
     * @param kbToolSaveReqVO 保存请求参数
     * @return KbToolDO
     */
    default KbToolDO convertToDO(KbToolSaveReqVO kbToolSaveReqVO) {
        KbToolDO kbToolDO = BeanUtils.toBean(kbToolSaveReqVO, KbToolDO.class);
        if (StrUtil.isBlank(kbToolDO.getParamSchema())) {
            return kbToolDO;
        }
        JSONArray array = JSONArray.parseArray(kbToolDO.getParamSchema());
        JSONObject result = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONArray requiredArray = new JSONArray();
        result.put("type", "object");

        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String name = jsonObject.getString("name");
            String type = jsonObject.getString("type");
            String description = jsonObject.getString("description");
            Boolean isRequired = jsonObject.getBoolean("isRequired");

            properties.put(name, JSONObject.of("type", type, "description", description));
            if (isRequired) {
                requiredArray.add(name);
            }

        }

        result.put("properties", properties);
        result.put("required", requiredArray);
        kbToolDO.setParamSchema(result.toJSONString());
        return kbToolDO;
    }

    /**
     * DO 转换为 RespVO
     *
     * @param kbToolDO 实体对象
     * @return KbToolRespVO
     */
    default KbToolRespVO convertToRespVO(KbToolDO kbToolDO) {
        KbToolRespVO vo = BeanUtils.toBean(kbToolDO, KbToolRespVO.class);
        if (StrUtil.isBlank(vo.getParamSchema())) {
            return vo;
        }
        String schema = vo.getParamSchema();
        JSONObject schemaJson = JSONObject.parseObject(schema);
        List<String> requiredList = schemaJson.getList("required", String.class);
        JSONObject propertiesJson = schemaJson.getJSONObject("properties");
        Set<String> nameSet = propertiesJson.keySet();
        JSONArray result = new JSONArray();
        for (String name : nameSet) {
            JSONObject jsonObject = propertiesJson.getJSONObject(name);
            String type = jsonObject.getString("type");
            String description = jsonObject.getString("description");
            JSONObject resultObject = JSONObject.of("name", name,
                    "type", type,
                    "description", description,
                    "isRequired", requiredList.contains(name));
            result.add(resultObject);
        }
        vo.setParamSchema(result.toJSONString());
        return vo;
    }

    /**
     * DOList 转换为 RespVOList
     *
     * @param kbToolDOList 实体对象列表
     * @return List<KbToolRespVO>
     */
    List<KbToolRespVO> convertToRespVOList(List<KbToolDO> kbToolDOList);
}
