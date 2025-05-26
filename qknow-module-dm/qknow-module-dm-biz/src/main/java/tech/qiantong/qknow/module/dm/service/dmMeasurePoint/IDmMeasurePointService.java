package tech.qiantong.qknow.module.dm.service.dmMeasurePoint;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointSaveReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointRespVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmMeasurePoint.DmMeasurePointDO;
/**
 * 物联网测点Service接口
 *
 * @author qknow
 * @date 2025-02-20
 */
public interface IDmMeasurePointService extends IService<DmMeasurePointDO> {

    /**
     * 获得物联网测点分页列表
     *
     * @param pageReqVO 分页请求
     * @return 物联网测点分页列表
     */
    PageResult<DmMeasurePointDO> getDmMeasurePointPage(DmMeasurePointPageReqVO pageReqVO);

    /**
     * 创建物联网测点
     *
     * @param createReqVO 物联网测点信息
     * @return 物联网测点编号
     */
    Long createDmMeasurePoint(DmMeasurePointSaveReqVO createReqVO);

    /**
     * 更新物联网测点
     *
     * @param updateReqVO 物联网测点信息
     */
    int updateDmMeasurePoint(DmMeasurePointSaveReqVO updateReqVO);

    /**
     * 删除物联网测点
     *
     * @param idList 物联网测点编号
     */
    int removeDmMeasurePoint(Collection<Long> idList);

    /**
     * 获得物联网测点详情
     *
     * @param id 物联网测点编号
     * @return 物联网测点
     */
    DmMeasurePointDO getDmMeasurePointById(Long id);

    /**
     * 获得全部物联网测点列表
     *
     * @return 物联网测点列表
     */
    List<DmMeasurePointDO> getDmMeasurePointList();

    /**
     * 获得全部物联网测点 Map
     *
     * @return 物联网测点 Map
     */
    Map<Long, DmMeasurePointDO> getDmMeasurePointMap();


    /**
     * 导入物联网测点数据
     *
     * @param importExcelList 物联网测点数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmMeasurePoint(List<DmMeasurePointRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
