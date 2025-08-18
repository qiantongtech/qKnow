package tech.qiantong.qknow.module.ext.service.extSchemaMapping.impl;

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
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaMapping.ExtSchemaMappingMapper;
import tech.qiantong.qknow.module.ext.service.extSchemaMapping.IExtSchemaMappingService;
/**
 * 概念映射Service业务层处理
 *
 * @author qknow
 * @date 2025-02-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtSchemaMappingServiceImpl  extends ServiceImpl<ExtSchemaMappingMapper,ExtSchemaMappingDO> implements IExtSchemaMappingService {
    @Resource
    private ExtSchemaMappingMapper extSchemaMappingMapper;

    @Override
    public PageResult<ExtSchemaMappingDO> getExtSchemaMappingPage(ExtSchemaMappingPageReqVO pageReqVO) {
        return extSchemaMappingMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtSchemaMapping(ExtSchemaMappingSaveReqVO createReqVO) {
        ExtSchemaMappingDO dictType = BeanUtils.toBean(createReqVO, ExtSchemaMappingDO.class);
        extSchemaMappingMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtSchemaMapping(ExtSchemaMappingSaveReqVO updateReqVO) {
        // 相关校验

        // 更新概念映射
        ExtSchemaMappingDO updateObj = BeanUtils.toBean(updateReqVO, ExtSchemaMappingDO.class);
        return extSchemaMappingMapper.updateById(updateObj);
    }
    @Override
    public int removeExtSchemaMapping(Collection<Long> idList) {
        // 批量删除概念映射
        return extSchemaMappingMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtSchemaMappingDO getExtSchemaMappingById(Long id) {
        return extSchemaMappingMapper.selectById(id);
    }

    @Override
    public List<ExtSchemaMappingDO> getExtSchemaMappingList() {
        return extSchemaMappingMapper.selectList();
    }

    @Override
    public Map<Long, ExtSchemaMappingDO> getExtSchemaMappingMap() {
        List<ExtSchemaMappingDO> extSchemaMappingList = extSchemaMappingMapper.selectList();
        return extSchemaMappingList.stream()
                .collect(Collectors.toMap(
                        ExtSchemaMappingDO::getId,
                        extSchemaMappingDO -> extSchemaMappingDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入概念映射数据
         *
         * @param importExcelList 概念映射数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importExtSchemaMapping(List<ExtSchemaMappingRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtSchemaMappingRespVO respVO : importExcelList) {
                try {
                    ExtSchemaMappingDO extSchemaMappingDO = BeanUtils.toBean(respVO, ExtSchemaMappingDO.class);
                    Long extSchemaMappingId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extSchemaMappingId != null) {
                            ExtSchemaMappingDO existingExtSchemaMapping = extSchemaMappingMapper.selectById(extSchemaMappingId);
                            if (existingExtSchemaMapping != null) {
                                extSchemaMappingMapper.updateById(extSchemaMappingDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extSchemaMappingId + " 的概念映射记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extSchemaMappingId + " 的概念映射记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtSchemaMappingDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extSchemaMappingId);
                        ExtSchemaMappingDO existingExtSchemaMapping = extSchemaMappingMapper.selectOne(queryWrapper);
                        if (existingExtSchemaMapping == null) {
                            extSchemaMappingMapper.insert(extSchemaMappingDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extSchemaMappingId + " 的概念映射记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extSchemaMappingId + " 的概念映射记录已存在。");
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
