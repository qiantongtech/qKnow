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

package tech.qiantong.qknow.module.ext.service.extAttributeMapping.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extAttributeMapping.ExtAttributeMappingMapper;
import tech.qiantong.qknow.module.ext.service.extAttributeMapping.IExtAttributeMappingService;
/**
 * 属性映射Service业务层处理
 *
 * @author qknow
 * @date 2025-02-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtAttributeMappingServiceImpl  extends ServiceImpl<ExtAttributeMappingMapper,ExtAttributeMappingDO> implements IExtAttributeMappingService {
    @Resource
    private ExtAttributeMappingMapper extAttributeMappingMapper;

    @Override
    public PageResult<ExtAttributeMappingDO> getExtAttributeMappingPage(ExtAttributeMappingPageReqVO pageReqVO) {
        return extAttributeMappingMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtAttributeMapping(ExtAttributeMappingSaveReqVO createReqVO) {
        ExtAttributeMappingDO dictType = BeanUtils.toBean(createReqVO, ExtAttributeMappingDO.class);
        extAttributeMappingMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtAttributeMapping(ExtAttributeMappingSaveReqVO updateReqVO) {
        // 相关校验

        // 更新属性映射
        ExtAttributeMappingDO updateObj = BeanUtils.toBean(updateReqVO, ExtAttributeMappingDO.class);
        return extAttributeMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeExtAttributeMapping(Collection<Long> idList) {
        // 批量删除属性映射
        return extAttributeMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtAttributeMappingDO getExtAttributeMappingById(Long id) {
        return extAttributeMappingMapper.selectById(id);
    }

    @Override
    public List<ExtAttributeMappingDO> getExtAttributeMappingList() {
        return extAttributeMappingMapper.selectList();
    }

    @Override
    public Map<Long, ExtAttributeMappingDO> getExtAttributeMappingMap() {
        List<ExtAttributeMappingDO> extAttributeMappingList = extAttributeMappingMapper.selectList();
        return extAttributeMappingList.stream()
                .collect(Collectors.toMap(
                        ExtAttributeMappingDO::getId,
                        extAttributeMappingDO -> extAttributeMappingDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入属性映射数据
         *
         * @param importExcelList 属性映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importExtAttributeMapping(List<ExtAttributeMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtAttributeMappingRespVO respVO : importExcelList) {
                try {
                    ExtAttributeMappingDO extAttributeMappingDO = BeanUtils.toBean(respVO, ExtAttributeMappingDO.class);
                    Long extAttributeMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extAttributeMappingId != null) {
                            ExtAttributeMappingDO existingExtAttributeMapping = extAttributeMappingMapper.selectById(extAttributeMappingId);
                            if (existingExtAttributeMapping != null) {
                                extAttributeMappingMapper.updateById(extAttributeMappingDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extAttributeMappingId + " 的属性映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extAttributeMappingId + " 的属性映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtAttributeMappingDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extAttributeMappingId);
                        ExtAttributeMappingDO existingExtAttributeMapping = extAttributeMappingMapper.selectOne(queryWrapper);
                        if (existingExtAttributeMapping == null) {
                            extAttributeMappingMapper.insert(extAttributeMappingDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extAttributeMappingId + " 的属性映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extAttributeMappingId + " 的属性映射记录已存在。");
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
