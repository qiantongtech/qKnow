package tech.qiantong.qknow.module.ext.service.extRelationMappingMiddle.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extRelationMappingMiddle.ExtRelationMappingMiddleMapper;
import tech.qiantong.qknow.module.ext.service.extRelationMappingMiddle.IExtRelationMappingMiddleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 关系映射中间Service业务层处理
 *
 * @author qknow
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtRelationMappingMiddleServiceImpl  extends ServiceImpl<ExtRelationMappingMiddleMapper,ExtRelationMappingMiddleDO> implements IExtRelationMappingMiddleService {
    @Resource
    private ExtRelationMappingMiddleMapper extRelationMappingMiddleMapper;

    @Override
    public PageResult<ExtRelationMappingMiddleDO> getExtRelationMappingMiddlePage(ExtRelationMappingMiddlePageReqVO pageReqVO) {
        return extRelationMappingMiddleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtRelationMappingMiddle(ExtRelationMappingMiddleSaveReqVO createReqVO) {
        ExtRelationMappingMiddleDO dictType = BeanUtils.toBean(createReqVO, ExtRelationMappingMiddleDO.class);
        extRelationMappingMiddleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtRelationMappingMiddle(ExtRelationMappingMiddleSaveReqVO updateReqVO) {
        // 相关校验

        // 更新关系映射中间
        ExtRelationMappingMiddleDO updateObj = BeanUtils.toBean(updateReqVO, ExtRelationMappingMiddleDO.class);
        return extRelationMappingMiddleMapper.updateById(updateObj);
    }
    @Override
    public int removeExtRelationMappingMiddle(Collection<Long> idList) {
        // 批量删除关系映射中间
        return extRelationMappingMiddleMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtRelationMappingMiddleDO getExtRelationMappingMiddleById(Long id) {
        return extRelationMappingMiddleMapper.selectById(id);
    }

    @Override
    public List<ExtRelationMappingMiddleDO> getExtRelationMappingMiddleList() {
        return extRelationMappingMiddleMapper.selectList();
    }

    @Override
    public Map<Long, ExtRelationMappingMiddleDO> getExtRelationMappingMiddleMap() {
        List<ExtRelationMappingMiddleDO> extRelationMappingMiddleList = extRelationMappingMiddleMapper.selectList();
        return extRelationMappingMiddleList.stream()
                .collect(Collectors.toMap(
                        ExtRelationMappingMiddleDO::getId,
                        extRelationMappingMiddleDO -> extRelationMappingMiddleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入关系映射中间数据
         *
         * @param importExcelList 关系映射中间数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importExtRelationMappingMiddle(List<ExtRelationMappingMiddleRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtRelationMappingMiddleRespVO respVO : importExcelList) {
                try {
                    ExtRelationMappingMiddleDO extRelationMappingMiddleDO = BeanUtils.toBean(respVO, ExtRelationMappingMiddleDO.class);
                    Long extRelationMappingMiddleId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extRelationMappingMiddleId != null) {
                            ExtRelationMappingMiddleDO existingExtRelationMappingMiddle = extRelationMappingMiddleMapper.selectById(extRelationMappingMiddleId);
                            if (existingExtRelationMappingMiddle != null) {
                                extRelationMappingMiddleMapper.updateById(extRelationMappingMiddleDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extRelationMappingMiddleId + " 的关系映射中间记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extRelationMappingMiddleId + " 的关系映射中间记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtRelationMappingMiddleDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extRelationMappingMiddleId);
                        ExtRelationMappingMiddleDO existingExtRelationMappingMiddle = extRelationMappingMiddleMapper.selectOne(queryWrapper);
                        if (existingExtRelationMappingMiddle == null) {
                            extRelationMappingMiddleMapper.insert(extRelationMappingMiddleDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extRelationMappingMiddleId + " 的关系映射中间记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extRelationMappingMiddleId + " 的关系映射中间记录已存在。");
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
