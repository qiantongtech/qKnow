package tech.qiantong.qknow.module.ext.service.extRelationMapping.impl;

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
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMapping.ExtRelationMappingDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extRelationMapping.ExtRelationMappingMapper;
import tech.qiantong.qknow.module.ext.service.extRelationMapping.IExtRelationMappingService;
/**
 * 关系映射Service业务层处理
 *
 * @author qknow
 * @date 2025-02-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtRelationMappingServiceImpl  extends ServiceImpl<ExtRelationMappingMapper,ExtRelationMappingDO> implements IExtRelationMappingService {
    @Resource
    private ExtRelationMappingMapper extRelationMappingMapper;

    @Override
    public PageResult<ExtRelationMappingDO> getExtRelationMappingPage(ExtRelationMappingPageReqVO pageReqVO) {
        return extRelationMappingMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtRelationMapping(ExtRelationMappingSaveReqVO createReqVO) {
        ExtRelationMappingDO dictType = BeanUtils.toBean(createReqVO, ExtRelationMappingDO.class);
        extRelationMappingMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtRelationMapping(ExtRelationMappingSaveReqVO updateReqVO) {
        // 相关校验

        // 更新关系映射
        ExtRelationMappingDO updateObj = BeanUtils.toBean(updateReqVO, ExtRelationMappingDO.class);
        return extRelationMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeExtRelationMapping(Collection<Long> idList) {
        // 批量删除关系映射
        return extRelationMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtRelationMappingDO getExtRelationMappingById(Long id) {
        return extRelationMappingMapper.selectById(id);
    }

    @Override
    public List<ExtRelationMappingDO> getExtRelationMappingList() {
        return extRelationMappingMapper.selectList();
    }

    @Override
    public Map<Long, ExtRelationMappingDO> getExtRelationMappingMap() {
        List<ExtRelationMappingDO> extRelationMappingList = extRelationMappingMapper.selectList();
        return extRelationMappingList.stream()
                .collect(Collectors.toMap(
                        ExtRelationMappingDO::getId,
                        extRelationMappingDO -> extRelationMappingDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入关系映射数据
         *
         * @param importExcelList 关系映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importExtRelationMapping(List<ExtRelationMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtRelationMappingRespVO respVO : importExcelList) {
                try {
                    ExtRelationMappingDO extRelationMappingDO = BeanUtils.toBean(respVO, ExtRelationMappingDO.class);
                    Long extRelationMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extRelationMappingId != null) {
                            ExtRelationMappingDO existingExtRelationMapping = extRelationMappingMapper.selectById(extRelationMappingId);
                            if (existingExtRelationMapping != null) {
                                extRelationMappingMapper.updateById(extRelationMappingDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extRelationMappingId + " 的关系映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extRelationMappingId + " 的关系映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtRelationMappingDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extRelationMappingId);
                        ExtRelationMappingDO existingExtRelationMapping = extRelationMappingMapper.selectOne(queryWrapper);
                        if (existingExtRelationMapping == null) {
                            extRelationMappingMapper.insert(extRelationMappingDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extRelationMappingId + " 的关系映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extRelationMappingId + " 的关系映射记录已存在。");
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
