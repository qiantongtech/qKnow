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

package tech.qiantong.qknow.module.ai.service.modelMarket;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeySaveReqVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyPageReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;

import java.util.List;

/**
 * API秘钥Service接口
 *
 * @author qknow
 * @date 2025-12-23
 */
public interface IAiApiKeyService extends IService<AiApiKeyDO> {

    /**
     * 获得API秘钥分页列表
     *
     * @param pageReqVO 分页请求
     * @return API秘钥分页列表
     */
    PageResult<AiApiKeyPageReqVO> getAiApiKeyPage(AiApiKeyPageReqVO pageReqVO);

    /**
     * 查询我的模型分页
     *
     * @param aiApiKey 查询字段
     * @return 分页数据
     */
    PageResult<AiApiKeyPageReqVO> queryMyModelPage(AiApiKeyPageReqVO aiApiKey);

    /**
     * 更新API秘钥
     *
     * @param updateReqVO API秘钥信息
     */
    int updateAiApiKey(AiApiKeySaveReqVO updateReqVO);

    /**
     * 获得API秘钥详情
     *
     * @param id API秘钥编号
     * @return API秘钥
     */
    AiApiKeyDO getAiApiKeyById(Long id);

    /**
     * 移除 api 密钥
     *
     * @param id id
     * @return 操作是否成功
     */
    Boolean removeKey(Long id);

    /**
     * 根据平台查询 api 密钥
     *
     * @param platform 平台
     * @return 该平台下的密钥信息
     */
    List<AiApiKeyDO> listByPlatform(String platform);

    /**
     * 查询平台基本信息
     *
     * @param platform 平台名
     * @return 平台基本信息
     */
    AiApiKeyDO queryByPlatform(String platform);

    /**
     * 新增 api 密钥
     *
     * @param aiApiKey apiKey
     * @return 新增是否成功
     */
    Boolean saveAiApiKey(AiApiKeySaveReqVO aiApiKey);

    /**
     * 批量提交 apiKey
     *
     * @param platform    平台
     * @param keyList     密钥列表
     * @param workSpaceId 工作空间id
     * @return 操作是否成功
     */
    Boolean submitBatch(String platform, List<AiApiKeySaveReqVO> keyList, Long workSpaceId);
}
