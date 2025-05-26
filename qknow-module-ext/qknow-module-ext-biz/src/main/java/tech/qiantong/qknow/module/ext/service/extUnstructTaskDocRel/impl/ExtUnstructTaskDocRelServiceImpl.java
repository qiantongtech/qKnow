package tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTaskDocRel.ExtUnstructTaskDocRelMapper;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel.IExtUnstructTaskDocRelService;

/**
 * 任务文件关联Service业务层处理
 *
 * @author qknow
 * @date 2025-02-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtUnstructTaskDocRelServiceImpl  extends ServiceImpl<ExtUnstructTaskDocRelMapper,ExtUnstructTaskDocRelDO> implements IExtUnstructTaskDocRelService {
    @Resource
    private ExtUnstructTaskDocRelMapper extUnstructTaskDocRelMapper;

    @Override
    public PageResult<ExtUnstructTaskDocRelDO> getExtUnstructTaskDocRelPage(ExtUnstructTaskDocRelPageReqVO pageReqVO) {
        return extUnstructTaskDocRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtUnstructTaskDocRel(ExtUnstructTaskDocRelSaveReqVO createReqVO) {
        ExtUnstructTaskDocRelDO dictType = BeanUtils.toBean(createReqVO, ExtUnstructTaskDocRelDO.class);
        extUnstructTaskDocRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtUnstructTaskDocRel(ExtUnstructTaskDocRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新任务文件关联
        ExtUnstructTaskDocRelDO updateObj = BeanUtils.toBean(updateReqVO, ExtUnstructTaskDocRelDO.class);
        return extUnstructTaskDocRelMapper.updateById(updateObj);
    }
    @Override
    public int removeExtUnstructTaskDocRel(Collection<Long> idList) {
        // 批量删除任务文件关联
        return extUnstructTaskDocRelMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtUnstructTaskDocRelDO getExtUnstructTaskDocRelById(Long id) {
        return extUnstructTaskDocRelMapper.selectById(id);
    }

    @Override
    public List<ExtUnstructTaskDocRelDO> getExtUnstructTaskDocRelList() {
        return extUnstructTaskDocRelMapper.selectList();
    }

    @Override
    public Map<Long, ExtUnstructTaskDocRelDO> getExtUnstructTaskDocRelMap() {
        List<ExtUnstructTaskDocRelDO> extUnstructTaskDocRelList = extUnstructTaskDocRelMapper.selectList();
        return extUnstructTaskDocRelList.stream()
                .collect(Collectors.toMap(
                        ExtUnstructTaskDocRelDO::getId,
                        extUnstructTaskDocRelDO -> extUnstructTaskDocRelDO,
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
        public String importExtUnstructTaskDocRel(List<ExtUnstructTaskDocRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (ExtUnstructTaskDocRelRespVO respVO : importExcelList) {
                try {
                    ExtUnstructTaskDocRelDO extUnstructTaskDocRelDO = BeanUtils.toBean(respVO, ExtUnstructTaskDocRelDO.class);
                    Long extUnstructTaskDocRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (extUnstructTaskDocRelId != null) {
                            ExtUnstructTaskDocRelDO existingExtUnstructTaskDocRel = extUnstructTaskDocRelMapper.selectById(extUnstructTaskDocRelId);
                            if (existingExtUnstructTaskDocRel != null) {
                                extUnstructTaskDocRelMapper.updateById(extUnstructTaskDocRelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + extUnstructTaskDocRelId + " 的任务文件关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + extUnstructTaskDocRelId + " 的任务文件关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<ExtUnstructTaskDocRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", extUnstructTaskDocRelId);
                        ExtUnstructTaskDocRelDO existingExtUnstructTaskDocRel = extUnstructTaskDocRelMapper.selectOne(queryWrapper);
                        if (existingExtUnstructTaskDocRel == null) {
                            extUnstructTaskDocRelMapper.insert(extUnstructTaskDocRelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + extUnstructTaskDocRelId + " 的任务文件关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + extUnstructTaskDocRelId + " 的任务文件关联记录已存在。");
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
