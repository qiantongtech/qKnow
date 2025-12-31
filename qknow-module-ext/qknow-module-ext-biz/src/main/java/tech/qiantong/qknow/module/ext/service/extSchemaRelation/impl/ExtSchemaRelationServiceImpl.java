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

package tech.qiantong.qknow.module.ext.service.extSchemaRelation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaRelation.ExtSchemaRelationMapper;
import tech.qiantong.qknow.module.ext.service.extSchema.IExtSchemaService;
import tech.qiantong.qknow.module.ext.service.extSchemaRelation.IExtSchemaRelationService;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 关系配置Service业务层处理
 *
 * @author qknow
 * @date 2025-02-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtSchemaRelationServiceImpl extends ServiceImpl<ExtSchemaRelationMapper, ExtSchemaRelationDO> implements IExtSchemaRelationService {

    @Resource
    private ExtSchemaRelationMapper extSchemaRelationMapper;

    @Override
    public PageResult<ExtSchemaRelationDO> getExtSchemaRelationPage(ExtSchemaRelationPageReqVO pageReqVO) {
        return extSchemaRelationMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtSchemaRelation(ExtSchemaRelationSaveReqVO createReqVO) {
        ExtSchemaRelationDO dictType = BeanUtils.toBean(createReqVO, ExtSchemaRelationDO.class);
        extSchemaRelationMapper.insert(dictType);
        return dictType.getId();

    }
    @Override
    public int updateExtSchemaRelation(ExtSchemaRelationSaveReqVO updateReqVO) {
        // 相关校验

        // 更新关系配置
        ExtSchemaRelationDO updateObj = BeanUtils.toBean(updateReqVO, ExtSchemaRelationDO.class);
        return extSchemaRelationMapper.updateById(updateObj);
    }

    @Override
    public int removeExtSchemaRelation(Collection<Long> idList) {
        // 批量删除关系配置
        return extSchemaRelationMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtSchemaRelationDO getExtSchemaRelationById(Long id) {
        return extSchemaRelationMapper.selectById(id);
    }

    @Override
    public List<ExtSchemaRelationDO> getExtSchemaRelationList() {
        return extSchemaRelationMapper.selectList();
    }

    @Override
    public Map<Long, ExtSchemaRelationDO> getExtSchemaRelationMap() {
        List<ExtSchemaRelationDO> extSchemaRelationList = extSchemaRelationMapper.selectList();
        return extSchemaRelationList.stream()
                .collect(Collectors.toMap(
                        ExtSchemaRelationDO::getId,
                        extSchemaRelationDO -> extSchemaRelationDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入关系配置数据
     *
     * @param importExcelList 关系配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importExtSchemaRelation(List<ExtSchemaRelationRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (ExtSchemaRelationRespVO respVO : importExcelList) {
            try {
                ExtSchemaRelationDO extSchemaRelationDO = BeanUtils.toBean(respVO, ExtSchemaRelationDO.class);
                Long extSchemaRelationId = respVO.getId();
                if (isUpdateSupport) {
                    if (extSchemaRelationId != null) {
                        ExtSchemaRelationDO existingExtSchemaRelation = extSchemaRelationMapper.selectById(extSchemaRelationId);
                        if (existingExtSchemaRelation != null) {
                            extSchemaRelationMapper.updateById(extSchemaRelationDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + extSchemaRelationId + " 的关系配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + extSchemaRelationId + " 的关系配置记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<ExtSchemaRelationDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", extSchemaRelationId);
                    ExtSchemaRelationDO existingExtSchemaRelation = extSchemaRelationMapper.selectOne(queryWrapper);
                    if (existingExtSchemaRelation == null) {
                        extSchemaRelationMapper.insert(extSchemaRelationDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + extSchemaRelationId + " 的关系配置记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + extSchemaRelationId + " 的关系配置记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }
}
