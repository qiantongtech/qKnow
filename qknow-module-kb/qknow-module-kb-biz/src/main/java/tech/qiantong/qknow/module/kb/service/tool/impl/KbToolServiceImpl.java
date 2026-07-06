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

package tech.qiantong.qknow.module.kb.service.tool.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;
import tech.qiantong.qknow.module.kb.dal.mapper.tool.KbToolMapper;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolService;
/**
 * 工具管理Service业务层处理
 *
 * @author qknow
 * @date 2026-03-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbToolServiceImpl  extends ServiceImpl<KbToolMapper,KbToolDO> implements IKbToolService {
    @Resource
    private KbToolMapper kbToolMapper;

    @Override
    public PageResult<KbToolDO> getKbToolPage(KbToolPageReqVO pageReqVO) {
        return kbToolMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKbTool(KbToolSaveReqVO createReqVO) {
        KbToolDO dictType = BeanUtils.toBean(createReqVO, KbToolDO.class);
        kbToolMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKbTool(KbToolSaveReqVO updateReqVO) {
        // 相关校验

        // 更新工具管理
        KbToolDO updateObj = BeanUtils.toBean(updateReqVO, KbToolDO.class);
        return kbToolMapper.updateById(updateObj);
    }
    @Override
    public int removeKbTool(Collection<Long> idList) {
        // 批量删除工具管理
        return kbToolMapper.deleteBatchIds(idList);
    }

    @Override
    public KbToolDO getKbToolById(Long id) {
        return kbToolMapper.selectById(id);
    }

    @Override
    public List<KbToolDO> getKbToolList() {
        return kbToolMapper.selectList();
    }

    @Override
    public Map<Long, KbToolDO> getKbToolMap() {
        List<KbToolDO> kbToolList = kbToolMapper.selectList();
        return kbToolList.stream()
                .collect(Collectors.toMap(
                        KbToolDO::getId,
                        kbToolDO -> kbToolDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入工具管理数据
         *
         * @param importExcelList 工具管理数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importKbTool(List<KbToolRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KbToolRespVO respVO : importExcelList) {
                try {
                    KbToolDO kbToolDO = BeanUtils.toBean(respVO, KbToolDO.class);
                    Long kbToolId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kbToolId != null) {
                            KbToolDO existingKbTool = kbToolMapper.selectById(kbToolId);
                            if (existingKbTool != null) {
                                kbToolMapper.updateById(kbToolDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kbToolId + " 的工具管理记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kbToolId + " 的工具管理记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KbToolDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kbToolId);
                        KbToolDO existingKbTool = kbToolMapper.selectOne(queryWrapper);
                        if (existingKbTool == null) {
                            kbToolMapper.insert(kbToolDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kbToolId + " 的工具管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kbToolId + " 的工具管理记录已存在。");
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
