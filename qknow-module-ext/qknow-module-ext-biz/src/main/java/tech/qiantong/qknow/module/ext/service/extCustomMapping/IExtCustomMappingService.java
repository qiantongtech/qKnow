package tech.qiantong.qknow.module.ext.service.extCustomMapping;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extCustomMapping.ExtCustomMappingDO;
/**
 * 自定义映射Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtCustomMappingService extends IService<ExtCustomMappingDO> {

    /**
     * 获得自定义映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 自定义映射分页列表
     */
    PageResult<ExtCustomMappingDO> getExtCustomMappingPage(ExtCustomMappingPageReqVO pageReqVO);

    /**
     * 创建自定义映射
     *
     * @param createReqVO 自定义映射信息
     * @return 自定义映射编号
     */
    Long createExtCustomMapping(ExtCustomMappingSaveReqVO createReqVO);

    /**
     * 更新自定义映射
     *
     * @param updateReqVO 自定义映射信息
     */
    int updateExtCustomMapping(ExtCustomMappingSaveReqVO updateReqVO);

    /**
     * 删除自定义映射
     *
     * @param idList 自定义映射编号
     */
    int removeExtCustomMapping(Collection<Long> idList);

    /**
     * 获得自定义映射详情
     *
     * @param id 自定义映射编号
     * @return 自定义映射
     */
    ExtCustomMappingDO getExtCustomMappingById(Long id);

    /**
     * 获得全部自定义映射列表
     *
     * @return 自定义映射列表
     */
    List<ExtCustomMappingDO> getExtCustomMappingList();

    /**
     * 获得全部自定义映射 Map
     *
     * @return 自定义映射 Map
     */
    Map<Long, ExtCustomMappingDO> getExtCustomMappingMap();


    /**
     * 导入自定义映射数据
     *
     * @param importExcelList 自定义映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtCustomMapping(List<ExtCustomMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
