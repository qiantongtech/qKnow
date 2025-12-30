package tech.qiantong.qknow.module.ext.service.extRelationMappingMiddle;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 关系映射中间Service接口
 *
 * @author qknow
 * @date 2025-12-16
 */
public interface IExtRelationMappingMiddleService extends IService<ExtRelationMappingMiddleDO> {

    /**
     * 获得关系映射中间分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系映射中间分页列表
     */
    PageResult<ExtRelationMappingMiddleDO> getExtRelationMappingMiddlePage(ExtRelationMappingMiddlePageReqVO pageReqVO);

    /**
     * 创建关系映射中间
     *
     * @param createReqVO 关系映射中间信息
     * @return 关系映射中间编号
     */
    Long createExtRelationMappingMiddle(ExtRelationMappingMiddleSaveReqVO createReqVO);

    /**
     * 更新关系映射中间
     *
     * @param updateReqVO 关系映射中间信息
     */
    int updateExtRelationMappingMiddle(ExtRelationMappingMiddleSaveReqVO updateReqVO);

    /**
     * 删除关系映射中间
     *
     * @param idList 关系映射中间编号
     */
    int removeExtRelationMappingMiddle(Collection<Long> idList);

    /**
     * 获得关系映射中间详情
     *
     * @param id 关系映射中间编号
     * @return 关系映射中间
     */
    ExtRelationMappingMiddleDO getExtRelationMappingMiddleById(Long id);

    /**
     * 获得全部关系映射中间列表
     *
     * @return 关系映射中间列表
     */
    List<ExtRelationMappingMiddleDO> getExtRelationMappingMiddleList();

    /**
     * 获得全部关系映射中间 Map
     *
     * @return 关系映射中间 Map
     */
    Map<Long, ExtRelationMappingMiddleDO> getExtRelationMappingMiddleMap();


    /**
     * 导入关系映射中间数据
     *
     * @param importExcelList 关系映射中间数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtRelationMappingMiddle(List<ExtRelationMappingMiddleRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
