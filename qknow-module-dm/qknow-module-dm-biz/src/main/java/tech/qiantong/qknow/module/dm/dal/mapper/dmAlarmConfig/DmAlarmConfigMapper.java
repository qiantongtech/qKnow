package tech.qiantong.qknow.module.dm.dal.mapper.dmAlarmConfig;

import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig.DmAlarmConfigDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 告警配置Mapper接口
 *
 * @author qknow
 * @date 2025-02-19
 */
public interface DmAlarmConfigMapper extends BaseMapperX<DmAlarmConfigDO> {

    default PageResult<DmAlarmConfigDO> selectPage(DmAlarmConfigPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 如果 reqVO.getOperateCond() 不为空且包含逗号分隔的值
        List<String> operateCondList = null;
        if (StringUtils.isNotBlank(reqVO.getOperateCond())) {
            operateCondList = Arrays.asList(reqVO.getOperateCond().split(","));
        }

        // 构造动态查询条件
        LambdaQueryWrapperX<DmAlarmConfigDO> queryWrapper = new LambdaQueryWrapperX<DmAlarmConfigDO>()
                .eqIfPresent(DmAlarmConfigDO::getWorkspaceId, reqVO.getWorkspaceId())
                .likeIfPresent(DmAlarmConfigDO::getName, reqVO.getName())
                .eqIfPresent(DmAlarmConfigDO::getCondCode, reqVO.getCondCode())
                .eqIfPresent(DmAlarmConfigDO::getType, reqVO.getType())
                .eqIfPresent(DmAlarmConfigDO::getUpper, reqVO.getUpper())
                .eqIfPresent(DmAlarmConfigDO::getLower, reqVO.getLower())
                .eqIfPresent(DmAlarmConfigDO::getContent, reqVO.getContent())
                .eqIfPresent(DmAlarmConfigDO::getPlan, reqVO.getPlan())
                .eqIfPresent(DmAlarmConfigDO::getPlanShort, reqVO.getPlanShort())
                .eqIfPresent(DmAlarmConfigDO::getCreateTime, reqVO.getCreateTime());

        // 如果 operateCondList 不为空
        if (operateCondList != null) {
            for (String operateCond : operateCondList) {
                queryWrapper.like(DmAlarmConfigDO::getOperateCond, "%" + operateCond + "%");
            }
        }

        // 按照 createTime 字段降序排序
        return selectPage(reqVO, queryWrapper
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
