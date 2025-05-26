package tech.qiantong.qknow.module.dm.service.dmExpertAdvice.impl;

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
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdvicePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmExpertAdvice.DmExpertAdviceDO;
import tech.qiantong.qknow.module.dm.dal.mapper.dmExpertAdvice.DmExpertAdviceMapper;
import tech.qiantong.qknow.module.dm.service.dmExpertAdvice.IDmExpertAdviceService;
/**
 * 专家经验Service业务层处理
 *
 * @author qknow
 * @date 2025-02-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmExpertAdviceServiceImpl  extends ServiceImpl<DmExpertAdviceMapper,DmExpertAdviceDO> implements IDmExpertAdviceService {
    @Resource
    private DmExpertAdviceMapper dmExpertAdviceMapper;

    @Override
    public PageResult<DmExpertAdviceDO> getDmExpertAdvicePage(DmExpertAdvicePageReqVO pageReqVO) {
        return dmExpertAdviceMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmExpertAdvice(DmExpertAdviceSaveReqVO createReqVO) {
        DmExpertAdviceDO dictType = BeanUtils.toBean(createReqVO, DmExpertAdviceDO.class);
        dmExpertAdviceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmExpertAdvice(DmExpertAdviceSaveReqVO updateReqVO) {
        // 相关校验

        // 更新专家经验
        DmExpertAdviceDO updateObj = BeanUtils.toBean(updateReqVO, DmExpertAdviceDO.class);
        return dmExpertAdviceMapper.updateById(updateObj);
    }
    @Override
    public int removeDmExpertAdvice(Collection<Long> idList) {
        // 批量删除专家经验
        return dmExpertAdviceMapper.deleteBatchIds(idList);
    }

    @Override
    public DmExpertAdviceDO getDmExpertAdviceById(Long id) {
        return dmExpertAdviceMapper.selectById(id);
    }

    @Override
    public List<DmExpertAdviceDO> getDmExpertAdviceList() {
        return dmExpertAdviceMapper.selectList();
    }

    @Override
    public Map<Long, DmExpertAdviceDO> getDmExpertAdviceMap() {
        List<DmExpertAdviceDO> dmExpertAdviceList = dmExpertAdviceMapper.selectList();
        return dmExpertAdviceList.stream()
                .collect(Collectors.toMap(
                        DmExpertAdviceDO::getId,
                        dmExpertAdviceDO -> dmExpertAdviceDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入专家经验数据
     *
     * @param importExcelList 专家经验数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDmExpertAdvice(List<DmExpertAdviceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmExpertAdviceRespVO respVO : importExcelList) {
            try {
                DmExpertAdviceDO dmExpertAdviceDO = BeanUtils.toBean(respVO, DmExpertAdviceDO.class);
                Long dmExpertAdviceId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmExpertAdviceId != null) {
                        DmExpertAdviceDO existingDmExpertAdvice = dmExpertAdviceMapper.selectById(dmExpertAdviceId);
                        if (existingDmExpertAdvice != null) {
                            dmExpertAdviceMapper.updateById(dmExpertAdviceDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dmExpertAdviceId + " 的专家经验记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dmExpertAdviceId + " 的专家经验记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmExpertAdviceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmExpertAdviceId);
                    DmExpertAdviceDO existingDmExpertAdvice = dmExpertAdviceMapper.selectOne(queryWrapper);
                    if (existingDmExpertAdvice == null) {
                        dmExpertAdviceMapper.insert(dmExpertAdviceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dmExpertAdviceId + " 的专家经验记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dmExpertAdviceId + " 的专家经验记录已存在。");
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
