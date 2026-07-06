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

package tech.qiantong.qknow.module.ai.service.modelMarket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.ai.enums.model.AiPlatformEnum;
import tech.qiantong.qknow.common.core.domain.entity.SysDictData;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.DictUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyPageReqVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeySaveReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;
import tech.qiantong.qknow.module.ai.dal.enums.ApiKeyStatus;
import tech.qiantong.qknow.module.ai.dal.mapper.modelMarket.AiApiKeyMapper;
import tech.qiantong.qknow.module.ai.service.modelMarket.IAiApiKeyService;
import tech.qiantong.qknow.mybatis.core.util.MyBatisUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * API秘钥Service业务层处理
 *
 * @author qknow
 * @date 2025-12-23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AiApiKeyServiceImpl extends ServiceImpl<AiApiKeyMapper, AiApiKeyDO> implements IAiApiKeyService {

    @Resource
    private AiApiKeyMapper aiApiKeyMapper;

    @Override
    public PageResult<AiApiKeyPageReqVO> getAiApiKeyPage(AiApiKeyPageReqVO pageReqVO) {
        // MyBatis Plus 查询
        List<SysDictData> platformDictList = DictUtils.getDictCache("ai_model_platform");
        assert platformDictList != null;
        if (StrUtil.isNotBlank(pageReqVO.getName())) {
            platformDictList = platformDictList.stream()
                    .filter(item -> item.getDictValue().contains(pageReqVO.getName()))
                    .toList();
        }

        Integer pageNum = pageReqVO.getPageNum();
        Integer pageSize = pageReqVO.getPageSize();
        if (pageNum < 1) {
            pageNum = 1;
        }
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = pageNum * pageSize;


        if (startIndex >= platformDictList.size()) {
            startIndex = platformDictList.size() / pageSize;
            if (platformDictList.size() % pageSize > 0) {
                startIndex = startIndex + pageSize;
            }
        }
        if (endIndex > platformDictList.size()) {
            endIndex = platformDictList.size();
        }
        List<SysDictData> subList = platformDictList.subList(startIndex, endIndex);


        List<String> platformList = subList.stream().map(SysDictData::getDictValue).toList();
        LambdaQueryWrapper<AiApiKeyDO> queryWrapper = Wrappers.<AiApiKeyDO>lambdaQuery()
                .in(AiApiKeyDO::getPlatform, platformList);
        List<AiApiKeyDO> doList = super.list(queryWrapper);
        Map<String, List<AiApiKeyDO>> collect = doList.stream().collect(Collectors.groupingBy(AiApiKeyDO::getPlatform));

        List<AiApiKeyPageReqVO> resultList = new ArrayList<>();
        subList.forEach(item -> {
            AiPlatformEnum aiPlatformEnum = AiPlatformEnum.validatePlatform(item.getDictValue());
            AiApiKeyPageReqVO vo = new AiApiKeyPageReqVO();
            List<AiApiKeyDO> aiApiKeyDOS = collect.get(item.getDictValue());
            Integer status = ApiKeyStatus.CONFIG.getCode();
            if (CollUtil.isEmpty(aiApiKeyDOS)) {
                status = ApiKeyStatus.NOT_CONFIG.getCode();
            }
            vo.setName(aiPlatformEnum.getName());
            vo.setPlatform(aiPlatformEnum.getPlatform());
            vo.setPlatformTag(aiPlatformEnum.getPlatform_tag());
            vo.setDescription(aiPlatformEnum.getDescription());
            vo.setStatus(status);
            resultList.add(vo);
        });

        return new PageResult<>(resultList, (long) platformDictList.size());
    }

    /**
     * 给 apiKey 字段加密显示
     *
     * @param apiKeyDO do 对象
     */
    private void apiKeyEncrypt(AiApiKeyDO apiKeyDO) {
        if (StrUtil.isBlank(apiKeyDO.getApiKey()) || apiKeyDO.getApiKey().length() <= 4) {
            return;
        }
        String apiKey = apiKeyDO.getApiKey();
        String s = apiKey.substring(0, 4)
                + "*****************"
                + apiKey.substring(apiKey.length() - 3);
        apiKeyDO.setApiKey(s);

    }

    @Override
    public int updateAiApiKey(AiApiKeySaveReqVO updateReqVO) {
        // 更新API秘钥
        AiApiKeyDO updateObj = BeanUtils.toBean(updateReqVO, AiApiKeyDO.class);
        if (StrUtil.isNotBlank(updateObj.getApiKey())
                && updateObj.getApiKey().contains("*****************")) {
            // 如果是加密的数据，那么就不更新这个字段了
            updateObj.setApiKey(null);
        }
        updateObj.setStatus(ApiKeyStatus.CONFIG.getCode());
        // 需要进行验证，验证不成功的话，配置失败
        return aiApiKeyMapper.updateById(updateObj);
    }

    @Override
    public AiApiKeyDO getAiApiKeyById(Long id) {
        AiApiKeyDO aiApiKeyDO = aiApiKeyMapper.selectById(id);
//        this.apiKeyEncrypt(aiApiKeyDO);
        return aiApiKeyDO;
    }

    /**
     * 移除 api 密钥
     *
     * @param id id
     * @return 操作是否成功
     */
    @Override
    public Boolean removeKey(Long id) {
        LambdaUpdateWrapper<AiApiKeyDO> updateWrapper = Wrappers.<AiApiKeyDO>lambdaUpdate()
                .eq(AiApiKeyDO::getId, id);
        return aiApiKeyMapper.delete(updateWrapper) > 0;
    }

    /**
     * 根据平台查询 api 密钥
     *
     * @param platform 平台
     * @return 该平台下的密钥信息
     */
    @Override
    public List<AiApiKeyDO> listByPlatform(String platform) {
        LambdaQueryWrapper<AiApiKeyDO> queryWrapper = Wrappers.<AiApiKeyDO>lambdaQuery()
                .eq(AiApiKeyDO::getPlatform, platform);
        List<AiApiKeyDO> list = super.list(queryWrapper);
//        list.forEach(this::apiKeyEncrypt);
        return list;
    }

    /**
     * 查询平台基本信息
     *
     * @param platform 平台名
     * @return 平台基本信息
     */
    @Override
    public AiApiKeyDO queryByPlatform(String platform) {
        AiPlatformEnum platformEnum = AiPlatformEnum.validatePlatform(platform);
        AiApiKeyDO result = new AiApiKeyDO();
        result.setName(platformEnum.getName());
        result.setPlatform(platformEnum.getPlatform());
        result.setPlatformTag(platformEnum.getPlatform_tag());
        result.setDescription(platformEnum.getDescription());
        return result;
    }

    /**
     * 新增 api 密钥
     *
     * @param aiApiKey apiKey
     * @return 新增是否成功
     */
    @Override
    public Boolean saveAiApiKey(AiApiKeySaveReqVO aiApiKey) {
        AiApiKeyDO aiApiKeyDO = BeanUtils.toBean(aiApiKey, AiApiKeyDO.class);
        aiApiKeyDO.setStatus(ApiKeyStatus.CONFIG.getCode());
        aiApiKeyDO.setDeployType("2");
        return super.save(aiApiKeyDO);
    }

    /**
     * 查询我的模型分页
     *
     * @param aiApiKey 查询字段
     * @return 分页数据
     */
    @Override
    public PageResult<AiApiKeyPageReqVO> queryMyModelPage(AiApiKeyPageReqVO aiApiKey) {
        Page<AiApiKeyPageReqVO> page = MyBatisUtils.buildPage(aiApiKey);
        Page<AiApiKeyPageReqVO> resultPage = baseMapper.selectPage(page, aiApiKey);
        if (CollUtil.isEmpty(resultPage.getRecords())) {
            new PageResult<>(new ArrayList<>(0), 0L);
        }
        resultPage.getRecords().forEach(item -> {
            AiPlatformEnum aiPlatformEnum = AiPlatformEnum.validatePlatform(item.getPlatform());
            item.setStatus(ApiKeyStatus.CONFIG.getCode());
            item.setName(aiPlatformEnum.getName());
            item.setPlatform(aiPlatformEnum.getPlatform());
            item.setPlatformTag(aiPlatformEnum.getPlatform_tag());
            item.setDescription(aiPlatformEnum.getDescription());
        });
        return new PageResult<>(resultPage.getRecords(), resultPage.getTotal());
    }

    /**
     * 批量提交 apiKey
     *
     * @param platform    平台
     * @param keyList     密钥列表
     * @param workSpaceId 工作空间id
     * @return 操作是否成功
     */
    @Override
    public Boolean submitBatch(String platform, List<AiApiKeySaveReqVO> keyList, Long workSpaceId) {
        // 首先根据平台查询出该平台下的密钥
        LambdaQueryWrapper<AiApiKeyDO> queryWrapper = Wrappers.<AiApiKeyDO>lambdaQuery()
                .eq(AiApiKeyDO::getPlatform, platform);
        List<AiApiKeyDO> sqlDOList = aiApiKeyMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(sqlDOList)) {
            // 全部新增
            List<AiApiKeyDO> apiKeyDOList = keyList.stream()
                    .peek(item -> {
                        item.setWorkspaceId(workSpaceId);
                        item.setStatus(ApiKeyStatus.CONFIG.getCode());
                    })
                    .map(item -> BeanUtils.toBean(item, AiApiKeyDO.class)).toList();
            return super.saveBatch(apiKeyDOList);
        }
        if (CollUtil.isEmpty(keyList)) {
            // 全部删除
            LambdaUpdateWrapper<AiApiKeyDO> wrapper = Wrappers.<AiApiKeyDO>lambdaUpdate()
                    .eq(AiApiKeyDO::getPlatform, platform);
            return super.remove(wrapper);
        }
        Map<Long, AiApiKeyDO> sqlDOMap = sqlDOList.stream()
                .collect(Collectors.toMap(AiApiKeyDO::getId, Function.identity(), (a, b) -> a));
        List<AiApiKeyDO> saveList = new ArrayList<>();
        List<AiApiKeyDO> updateList = new ArrayList<>();

        for (AiApiKeySaveReqVO vo : keyList) {
            if (StrUtil.isEmpty(vo.getName()) && StrUtil.isEmpty(vo.getApiKey()) && StrUtil.isEmpty(vo.getUrl())) {
                continue;
            }
            if (Objects.isNull(vo.getId())) {
                // 获取需要新增的
                AiApiKeyDO keyDO = BeanUtils.toBean(vo, AiApiKeyDO.class);
                keyDO.setWorkspaceId(workSpaceId);
                keyDO.setStatus(ApiKeyStatus.CONFIG.getCode());
                saveList.add(keyDO);
                continue;
            }
            AiApiKeyDO sqlDO = sqlDOMap.get(vo.getId());
            sqlDOMap.remove(vo.getId());
            if (Objects.isNull(sqlDO)) {
                continue;
            }
            if (Objects.equals(sqlDO.getApiKey(), vo.getApiKey())
                    && Objects.equals(sqlDO.getName(), vo.getName())
                    && Objects.equals(sqlDO.getUrl(), vo.getUrl())) {
                continue;
            }
            // 需要修改的
            AiApiKeyDO keyDO = BeanUtils.toBean(vo, AiApiKeyDO.class);
            keyDO.setStatus(ApiKeyStatus.CONFIG.getCode());
            updateList.add(keyDO);
        }
        if (CollUtil.isNotEmpty(sqlDOMap)) {
            super.removeByIds(sqlDOMap.keySet());
        }
        if (CollUtil.isNotEmpty(saveList)) {
            super.saveBatch(saveList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            super.updateBatchById(updateList);
        }
        return true;
    }
}
