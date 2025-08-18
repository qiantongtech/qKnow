package tech.qiantong.qknow.module.ext.service.extRelationMapping;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMapping.ExtRelationMappingDO;
/**
 * 关系映射Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtRelationMappingService extends IService<ExtRelationMappingDO> {

    /**
     * 获得关系映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系映射分页列表
     */
    PageResult<ExtRelationMappingDO> getExtRelationMappingPage(ExtRelationMappingPageReqVO pageReqVO);

    /**
     * 创建关系映射
     *
     * @param createReqVO 关系映射信息
     * @return 关系映射编号
     */
    Long createExtRelationMapping(ExtRelationMappingSaveReqVO createReqVO);

    /**
     * 更新关系映射
     *
     * @param updateReqVO 关系映射信息
     */
    int updateExtRelationMapping(ExtRelationMappingSaveReqVO updateReqVO);

    /**
     * 删除关系映射
     *
     * @param idList 关系映射编号
     */
    int removeExtRelationMapping(Collection<Long> idList);

    /**
     * 获得关系映射详情
     *
     * @param id 关系映射编号
     * @return 关系映射
     */
    ExtRelationMappingDO getExtRelationMappingById(Long id);

    /**
     * 获得全部关系映射列表
     *
     * @return 关系映射列表
     */
    List<ExtRelationMappingDO> getExtRelationMappingList();

    /**
     * 获得全部关系映射 Map
     *
     * @return 关系映射 Map
     */
    Map<Long, ExtRelationMappingDO> getExtRelationMappingMap();


    /**
     * 导入关系映射数据
     *
     * @param importExcelList 关系映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtRelationMapping(List<ExtRelationMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
