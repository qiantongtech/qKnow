package tech.qiantong.qknow.module.ext.service.extSchemaAttribute.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extSchemaAttribute.ExtSchemaAttributeMapper;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;
/**
 * 概念属性Service业务层处理
 *
 * @author qknow
 * @date 2025-02-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtSchemaAttributeServiceImpl  extends ServiceImpl<ExtSchemaAttributeMapper,ExtSchemaAttributeDO> implements IExtSchemaAttributeService {
    @Resource
    private ExtSchemaAttributeMapper extSchemaAttributeMapper;

    @Override
    public PageResult<ExtSchemaAttributeDO> getExtSchemaAttributePage(ExtSchemaAttributePageReqVO pageReqVO) {
        return extSchemaAttributeMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExtSchemaAttribute(ExtSchemaAttributeSaveReqVO createReqVO) {
        ExtSchemaAttributeDO dictType = BeanUtils.toBean(createReqVO, ExtSchemaAttributeDO.class);
        extSchemaAttributeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateExtSchemaAttribute(ExtSchemaAttributeSaveReqVO updateReqVO) {
        // 更新概念属性
        ExtSchemaAttributeDO updateObj = BeanUtils.toBean(updateReqVO, ExtSchemaAttributeDO.class);
        // 如果最大值、最小值、校验方式 为空 更新数据库
        UpdateWrapper<ExtSchemaAttributeDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", updateObj.getId());

        // 标识是否需要执行更新
        boolean needUpdate = false;

        // 判断 updateReqVO 中的字段值是否为空，如果为空才 set
        if (updateReqVO.getMinValue() == null) {
            updateWrapper.set("min_value", null);
            needUpdate = true;
        }
        if (updateReqVO.getMaxValue() == null) {
            updateWrapper.set("max_value", null);
            needUpdate = true;
        }
        if (updateReqVO.getValidateType() == null) {
            updateWrapper.set("validate_type", null);
            needUpdate = true;
        }

        // 如果需要更新，则执行更新操作
        if (needUpdate) {
            extSchemaAttributeMapper.update(null, updateWrapper);
        }
        return extSchemaAttributeMapper.updateById(updateObj);
    }
    @Override
    public int removeExtSchemaAttribute(Collection<Long> idList) {
        // 批量删除概念属性
        return extSchemaAttributeMapper.deleteBatchIds(idList);
    }

    @Override
    public ExtSchemaAttributeDO getExtSchemaAttributeById(Long id) {
        return extSchemaAttributeMapper.selectById(id);
    }

    @Override
    public List<ExtSchemaAttributeDO> getExtSchemaAttributeList() {
        return extSchemaAttributeMapper.selectList();
    }

    @Override
    public Map<Long, ExtSchemaAttributeDO> getExtSchemaAttributeMap() {
        List<ExtSchemaAttributeDO> extSchemaAttributeList = extSchemaAttributeMapper.selectList();
        return extSchemaAttributeList.stream()
                .collect(Collectors.toMap(
                        ExtSchemaAttributeDO::getId,
                        extSchemaAttributeDO -> extSchemaAttributeDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入概念属性数据
     *
     * @param importExcelList 概念属性数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importExtSchemaAttribute(List<ExtSchemaAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (ExtSchemaAttributeRespVO respVO : importExcelList) {
            try {
                ExtSchemaAttributeDO extSchemaAttributeDO = BeanUtils.toBean(respVO, ExtSchemaAttributeDO.class);
                Long extSchemaAttributeId = respVO.getId();
                if (isUpdateSupport) {
                    if (extSchemaAttributeId != null) {
                        ExtSchemaAttributeDO existingExtSchemaAttribute = extSchemaAttributeMapper.selectById(extSchemaAttributeId);
                        if (existingExtSchemaAttribute != null) {
                            extSchemaAttributeMapper.updateById(extSchemaAttributeDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + extSchemaAttributeId + " 的概念属性记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + extSchemaAttributeId + " 的概念属性记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<ExtSchemaAttributeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", extSchemaAttributeId);
                    ExtSchemaAttributeDO existingExtSchemaAttribute = extSchemaAttributeMapper.selectOne(queryWrapper);
                    if (existingExtSchemaAttribute == null) {
                        extSchemaAttributeMapper.insert(extSchemaAttributeDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + extSchemaAttributeId + " 的概念属性记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + extSchemaAttributeId + " 的概念属性记录已存在。");
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
    public List<ExtSchemaAttributeDO> getAllExtSchemaAttributeList(ExtSchemaAttributeDO extSchemaAttributeDO) {
        QueryWrapper<ExtSchemaAttributeDO> queryWrapper = new QueryWrapper<>();

        // 根据传入的 extSchemaAttributeDO 的 schema_id 进行查询
        if (extSchemaAttributeDO.getSchemaId() != null) {
            queryWrapper.eq("schema_id", extSchemaAttributeDO.getSchemaId());
        }

        return extSchemaAttributeMapper.selectList(queryWrapper);
    }
}
