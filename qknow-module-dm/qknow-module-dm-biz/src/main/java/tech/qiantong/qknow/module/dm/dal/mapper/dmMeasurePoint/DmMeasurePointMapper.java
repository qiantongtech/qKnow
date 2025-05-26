package tech.qiantong.qknow.module.dm.dal.mapper.dmMeasurePoint;

import tech.qiantong.qknow.module.dm.dal.dataobject.dmMeasurePoint.DmMeasurePointDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 物联网测点Mapper接口
 *
 * @author qknow
 * @date 2025-02-20
 */
public interface DmMeasurePointMapper extends BaseMapperX<DmMeasurePointDO> {

    default PageResult<DmMeasurePointDO> selectPage(DmMeasurePointPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DmMeasurePointDO>()
                .eqIfPresent(DmMeasurePointDO::getWorkspaceId, reqVO.getWorkspaceId())
                .likeIfPresent(DmMeasurePointDO::getName, reqVO.getName())
                .eqIfPresent(DmMeasurePointDO::getCode, reqVO.getCode())
                .likeIfPresent(DmMeasurePointDO::getDeviceName, reqVO.getDeviceName())
                .eqIfPresent(DmMeasurePointDO::getType, reqVO.getType())
                .eqIfPresent(DmMeasurePointDO::getDeviceKey, reqVO.getDeviceKey())
                .eqIfPresent(DmMeasurePointDO::getPrefix, reqVO.getPrefix())
                .eqIfPresent(DmMeasurePointDO::getRealtimeFlag, reqVO.getRealtimeFlag())
                .eqIfPresent(DmMeasurePointDO::getFrequency, reqVO.getFrequency())
                .eqIfPresent(DmMeasurePointDO::getUnit, reqVO.getUnit())
                .eqIfPresent(DmMeasurePointDO::getFailureFlag, reqVO.getFailureFlag())
                .eqIfPresent(DmMeasurePointDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DmMeasurePointDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
