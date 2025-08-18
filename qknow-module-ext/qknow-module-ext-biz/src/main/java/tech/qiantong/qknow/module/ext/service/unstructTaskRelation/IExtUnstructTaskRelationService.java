package tech.qiantong.qknow.module.ext.service.unstructTaskRelation;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 任务文件关联Service接口
 *
 * @author qknow
 * @date 2025-04-03
 */
public interface IExtUnstructTaskRelationService extends IService<ExtUnstructTaskRelationDO> {

    /**
     * 获得任务文件关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 任务文件关联分页列表
     */
    PageResult<ExtUnstructTaskRelationDO> getExtUnstructTaskRelationPage(ExtUnstructTaskRelationPageReqVO pageReqVO);

    /**
     * 创建任务文件关联
     *
     * @param createReqVO 任务文件关联信息
     * @return 任务文件关联编号
     */
    Long createExtUnstructTaskRelation(ExtUnstructTaskRelationSaveReqVO createReqVO);

    /**
     * 更新任务文件关联
     *
     * @param updateReqVO 任务文件关联信息
     */
    int updateExtUnstructTaskRelation(ExtUnstructTaskRelationSaveReqVO updateReqVO);

    /**
     * 删除任务文件关联
     *
     * @param idList 任务文件关联编号
     */
    int removeExtUnstructTaskRelation(Collection<Long> idList);

    /**
     * 获得任务文件关联详情
     *
     * @param id 任务文件关联编号
     * @return 任务文件关联
     */
    ExtUnstructTaskRelationDO getExtUnstructTaskRelationById(Long id);

    /**
     * 获得全部任务文件关联列表
     *
     * @return 任务文件关联列表
     */
    List<ExtUnstructTaskRelationDO> getExtUnstructTaskRelationList();

    /**
     * 获得全部任务文件关联 Map
     *
     * @return 任务文件关联 Map
     */
    Map<Long, ExtUnstructTaskRelationDO> getExtUnstructTaskRelationMap();


    /**
     * 导入任务文件关联数据
     *
     * @param importExcelList 任务文件关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtUnstructTaskRelation(List<ExtUnstructTaskRelationRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<ExtUnstructTaskRelationDO> findByTaskId(Long taskId);
}
