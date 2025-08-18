package tech.qiantong.qknow.module.ext.dal.mapper.extStructTask;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.neo4j.repository.query.Query;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import javax.websocket.server.PathParam;

/**
 * 结构化抽取任务Mapper接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface ExtStructTaskMapper extends BaseMapperX<ExtStructTaskDO> {

    default PageResult<ExtStructTaskDO> selectPage(ExtStructTaskPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "publish_time","create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtStructTaskDO>()
                .eqIfPresent(ExtStructTaskDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(ExtStructTaskDO::getId, reqVO.getId())
                .likeIfPresent(ExtStructTaskDO::getName, reqVO.getName())
                .eqIfPresent(ExtStructTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ExtStructTaskDO::getPublishStatus, reqVO.getPublishStatus())
                .eqIfPresent(ExtStructTaskDO::getPublishTime, reqVO.getPublishTime())
                .eqIfPresent(ExtStructTaskDO::getPublisherId, reqVO.getPublisherId())
                .eqIfPresent(ExtStructTaskDO::getPublishBy, reqVO.getPublishBy())
                .eqIfPresent(ExtStructTaskDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(ExtStructTaskDO::getDatasourceName, reqVO.getDatasourceName())
                .eqIfPresent(ExtStructTaskDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(ExtStructTaskDO::getCreateTime, reqVO.getParamByKey("beginCreateTime"), reqVO.getParamByKey("endCreateTime"))
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtStructTaskDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

}
