package tech.qiantong.qknow.module.ext.service.unstructTaskRelation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;
import tech.qiantong.qknow.module.ext.dal.mapper.unstructTaskRelation.ExtUnstructTaskRelationMapper;
import tech.qiantong.qknow.module.ext.service.unstructTaskRelation.IExtUnstructTaskRelationService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务文件关联Service业务层处理
 *
 * @author qknow
 * @date 2025-04-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtUnstructTaskRelationServiceImpl  extends ServiceImpl<ExtUnstructTaskRelationMapper,ExtUnstructTaskRelationDO> implements IExtUnstructTaskRelationService {
    @Resource
    private ExtUnstructTaskRelationMapper extUnstructTaskRelationMapper;

    @Override
    public PageResult<ExtUnstructTaskRelationDO> getExtUnstructTaskRelationPage(ExtUnstructTaskRelationPageReqVO pageReqVO) {
        return extUnstructTaskRelationMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtUnstructTaskRelation(ExtUnstructTaskRelationSaveReqVO createReqVO) {
        ExtUnstructTaskRelationDO dictType = BeanUtils.toBean(createReqVO, ExtUnstructTaskRelationDO.class);
        extUnstructTaskRelationMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtUnstructTaskRelation(ExtUnstructTaskRelationSaveReqVO updateReqVO) {
        // 相关校验

        // 更新任务文件关联
        ExtUnstructTaskRelationDO updateObj = BeanUtils.toBean(updateReqVO, ExtUnstructTaskRelationDO.class);
        return extUnstructTaskRelationMapper.updateById(updateObj);
    }
    @Override
    public int removeExtUnstructTaskRelation(Collection<Long> idList) {
        // 批量删除任务文件关联
        return extUnstructTaskRelationMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtUnstructTaskRelationDO getExtUnstructTaskRelationById(Long id) {
        return extUnstructTaskRelationMapper.selectById(id);
    }

    @Override
    public List<ExtUnstructTaskRelationDO> getExtUnstructTaskRelationList() {
        return extUnstructTaskRelationMapper.selectList();
    }

    @Override
    public Map<Long, ExtUnstructTaskRelationDO> getExtUnstructTaskRelationMap() {
        List<ExtUnstructTaskRelationDO> extUnstructTaskRelationList = extUnstructTaskRelationMapper.selectList();
        return extUnstructTaskRelationList.stream()
                .collect(Collectors.toMap(
                        ExtUnstructTaskRelationDO::getId,
                        extUnstructTaskRelationDO -> extUnstructTaskRelationDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入任务文件关联数据
     *
     * @param importExcelList 任务文件关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importExtUnstructTaskRelation(List<ExtUnstructTaskRelationRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtUnstructTaskRelationRespVO respVO : importExcelList) {
                try {
                    ExtUnstructTaskRelationDO extUnstructTaskRelationDO = BeanUtils.toBean(respVO, ExtUnstructTaskRelationDO.class);
                    Long extUnstructTaskRelationId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extUnstructTaskRelationId != null) {
                            ExtUnstructTaskRelationDO existingExtUnstructTaskRelation = extUnstructTaskRelationMapper.selectById(extUnstructTaskRelationId);
                            if (existingExtUnstructTaskRelation != null) {
                                extUnstructTaskRelationMapper.updateById(extUnstructTaskRelationDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extUnstructTaskRelationId + " 的任务文件关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extUnstructTaskRelationId + " 的任务文件关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtUnstructTaskRelationDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extUnstructTaskRelationId);
                        ExtUnstructTaskRelationDO existingExtUnstructTaskRelation = extUnstructTaskRelationMapper.selectOne(queryWrapper);
                        if (existingExtUnstructTaskRelation == null) {
                            extUnstructTaskRelationMapper.insert(extUnstructTaskRelationDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extUnstructTaskRelationId + " 的任务文件关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extUnstructTaskRelationId + " 的任务文件关联记录已存在。");
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

    @Override
    public List<ExtUnstructTaskRelationDO> findByTaskId(Long taskId) {
        return extUnstructTaskRelationMapper.selectList(new LambdaQueryWrapperX<ExtUnstructTaskRelationDO>()
                .eq(ExtUnstructTaskRelationDO::getTaskId, taskId));
    }

}
