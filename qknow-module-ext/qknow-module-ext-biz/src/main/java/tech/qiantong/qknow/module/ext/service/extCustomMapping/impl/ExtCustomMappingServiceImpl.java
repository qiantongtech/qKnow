package tech.qiantong.qknow.module.ext.service.extCustomMapping.impl;

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
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extCustomMapping.ExtCustomMappingDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extCustomMapping.ExtCustomMappingMapper;
import tech.qiantong.qknow.module.ext.service.extCustomMapping.IExtCustomMappingService;
/**
 * 自定义映射Service业务层处理
 *
 * @author qknow
 * @date 2025-02-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtCustomMappingServiceImpl  extends ServiceImpl<ExtCustomMappingMapper,ExtCustomMappingDO> implements IExtCustomMappingService {
    @Resource
    private ExtCustomMappingMapper extCustomMappingMapper;

    @Override
    public PageResult<ExtCustomMappingDO> getExtCustomMappingPage(ExtCustomMappingPageReqVO pageReqVO) {
        return extCustomMappingMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtCustomMapping(ExtCustomMappingSaveReqVO createReqVO) {
        ExtCustomMappingDO dictType = BeanUtils.toBean(createReqVO, ExtCustomMappingDO.class);
        extCustomMappingMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtCustomMapping(ExtCustomMappingSaveReqVO updateReqVO) {
        // 相关校验

        // 更新自定义映射
        ExtCustomMappingDO updateObj = BeanUtils.toBean(updateReqVO, ExtCustomMappingDO.class);
        return extCustomMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeExtCustomMapping(Collection<Long> idList) {
        // 批量删除自定义映射
        return extCustomMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtCustomMappingDO getExtCustomMappingById(Long id) {
        return extCustomMappingMapper.selectById(id);
    }

    @Override
    public List<ExtCustomMappingDO> getExtCustomMappingList() {
        return extCustomMappingMapper.selectList();
    }

    @Override
    public Map<Long, ExtCustomMappingDO> getExtCustomMappingMap() {
        List<ExtCustomMappingDO> extCustomMappingList = extCustomMappingMapper.selectList();
        return extCustomMappingList.stream()
                .collect(Collectors.toMap(
                        ExtCustomMappingDO::getId,
                        extCustomMappingDO -> extCustomMappingDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入自定义映射数据
         *
         * @param importExcelList 自定义映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importExtCustomMapping(List<ExtCustomMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtCustomMappingRespVO respVO : importExcelList) {
                try {
                    ExtCustomMappingDO extCustomMappingDO = BeanUtils.toBean(respVO, ExtCustomMappingDO.class);
                    Long extCustomMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extCustomMappingId != null) {
                            ExtCustomMappingDO existingExtCustomMapping = extCustomMappingMapper.selectById(extCustomMappingId);
                            if (existingExtCustomMapping != null) {
                                extCustomMappingMapper.updateById(extCustomMappingDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extCustomMappingId + " 的自定义映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extCustomMappingId + " 的自定义映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtCustomMappingDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extCustomMappingId);
                        ExtCustomMappingDO existingExtCustomMapping = extCustomMappingMapper.selectOne(queryWrapper);
                        if (existingExtCustomMapping == null) {
                            extCustomMappingMapper.insert(extCustomMappingDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extCustomMappingId + " 的自定义映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extCustomMappingId + " 的自定义映射记录已存在。");
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
