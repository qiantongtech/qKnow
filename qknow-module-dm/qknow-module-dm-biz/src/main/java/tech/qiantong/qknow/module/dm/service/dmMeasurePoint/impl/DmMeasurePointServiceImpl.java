package tech.qiantong.qknow.module.dm.service.dmMeasurePoint.impl;

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
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmMeasurePoint.DmMeasurePointDO;
import tech.qiantong.qknow.module.dm.dal.mapper.dmMeasurePoint.DmMeasurePointMapper;
import tech.qiantong.qknow.module.dm.service.dmMeasurePoint.IDmMeasurePointService;
/**
 * 物联网测点Service业务层处理
 *
 * @author qknow
 * @date 2025-02-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmMeasurePointServiceImpl  extends ServiceImpl<DmMeasurePointMapper,DmMeasurePointDO> implements IDmMeasurePointService {
    @Resource
    private DmMeasurePointMapper dmMeasurePointMapper;

    @Override
    public PageResult<DmMeasurePointDO> getDmMeasurePointPage(DmMeasurePointPageReqVO pageReqVO) {
        return dmMeasurePointMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmMeasurePoint(DmMeasurePointSaveReqVO createReqVO) {
        DmMeasurePointDO dictType = BeanUtils.toBean(createReqVO, DmMeasurePointDO.class);
        dmMeasurePointMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmMeasurePoint(DmMeasurePointSaveReqVO updateReqVO) {
        // 相关校验

        // 更新物联网测点
        DmMeasurePointDO updateObj = BeanUtils.toBean(updateReqVO, DmMeasurePointDO.class);
        return dmMeasurePointMapper.updateById(updateObj);
    }
    @Override
    public int removeDmMeasurePoint(Collection<Long> idList) {
        // 批量删除物联网测点
        return dmMeasurePointMapper.deleteBatchIds(idList);
    }

    @Override
    public DmMeasurePointDO getDmMeasurePointById(Long id) {
        return dmMeasurePointMapper.selectById(id);
    }

    @Override
    public List<DmMeasurePointDO> getDmMeasurePointList() {
        return dmMeasurePointMapper.selectList();
    }

    @Override
    public Map<Long, DmMeasurePointDO> getDmMeasurePointMap() {
        List<DmMeasurePointDO> dmMeasurePointList = dmMeasurePointMapper.selectList();
        return dmMeasurePointList.stream()
                .collect(Collectors.toMap(
                        DmMeasurePointDO::getId,
                        dmMeasurePointDO -> dmMeasurePointDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入物联网测点数据
     *
     * @param importExcelList 物联网测点数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDmMeasurePoint(List<DmMeasurePointRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmMeasurePointRespVO respVO : importExcelList) {
            try {
                DmMeasurePointDO dmMeasurePointDO = BeanUtils.toBean(respVO, DmMeasurePointDO.class);
                Long dmMeasurePointId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmMeasurePointId != null) {
                        DmMeasurePointDO existingDmMeasurePoint = dmMeasurePointMapper.selectById(dmMeasurePointId);
                        if (existingDmMeasurePoint != null) {
                            dmMeasurePointMapper.updateById(dmMeasurePointDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dmMeasurePointId + " 的物联网测点记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dmMeasurePointId + " 的物联网测点记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmMeasurePointDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmMeasurePointId);
                    DmMeasurePointDO existingDmMeasurePoint = dmMeasurePointMapper.selectOne(queryWrapper);
                    if (existingDmMeasurePoint == null) {
                        dmMeasurePointMapper.insert(dmMeasurePointDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dmMeasurePointId + " 的物联网测点记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dmMeasurePointId + " 的物联网测点记录已存在。");
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
