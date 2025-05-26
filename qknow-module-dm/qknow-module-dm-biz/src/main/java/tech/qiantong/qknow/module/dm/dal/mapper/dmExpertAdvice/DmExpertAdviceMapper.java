package tech.qiantong.qknow.module.dm.dal.mapper.dmExpertAdvice;

import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig.DmAlarmConfigDO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmExpertAdvice.DmExpertAdviceDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdvicePageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 专家经验Mapper接口
 *
 * @author qknow
 * @date 2025-02-20
 */
public interface DmExpertAdviceMapper extends BaseMapperX<DmExpertAdviceDO> {

    default PageResult<DmExpertAdviceDO> selectPage(DmExpertAdvicePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 如果 reqVO.getOperateCond() 不为空且包含逗号分隔的值
        List<String> operateCondList = null;
        if (StringUtils.isNotBlank(reqVO.getOperateCond())) {
            operateCondList = Arrays.asList(reqVO.getOperateCond().split(","));
        }

        LambdaQueryWrapperX<DmExpertAdviceDO> queryWrapper = new LambdaQueryWrapperX<DmExpertAdviceDO>()
                .eqIfPresent(DmExpertAdviceDO::getWorkspaceId, reqVO.getWorkspaceId())
                .likeIfPresent(DmExpertAdviceDO::getName, reqVO.getName())
                .eqIfPresent(DmExpertAdviceDO::getCondCode, reqVO.getCondCode())
                .eqIfPresent(DmExpertAdviceDO::getType, reqVO.getType())
                .eqIfPresent(DmExpertAdviceDO::getAlarmCond, reqVO.getAlarmCond())
                .eqIfPresent(DmExpertAdviceDO::getContent, reqVO.getContent())
                .eqIfPresent(DmExpertAdviceDO::getPlan, reqVO.getPlan())
                .eqIfPresent(DmExpertAdviceDO::getPlanShort, reqVO.getPlanShort())
                .eqIfPresent(DmExpertAdviceDO::getCreateTime, reqVO.getCreateTime());

        // 如果 operateCondList 不为空
        if (operateCondList != null) {
            for (String operateCond : operateCondList) {
                queryWrapper.like(DmExpertAdviceDO::getOperateCond, "%" + operateCond + "%");
            }
        }

        return selectPage(reqVO, queryWrapper
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
