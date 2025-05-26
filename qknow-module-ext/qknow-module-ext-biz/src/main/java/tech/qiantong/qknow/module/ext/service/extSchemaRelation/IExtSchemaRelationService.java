package tech.qiantong.qknow.module.ext.service.extSchemaRelation;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationPageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;
/**
 * 关系配置Service接口
 *
 * @author qknow
 * @date 2025-02-18
 */
public interface IExtSchemaRelationService extends IService<ExtSchemaRelationDO> {

    /**
     * 获得关系配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 关系配置分页列表
     */
    PageResult<ExtSchemaRelationDO> getExtSchemaRelationPage(ExtSchemaRelationPageReqVO pageReqVO);

    /**
     * 创建关系配置
     *
     * @param createReqVO 关系配置信息
     * @return 关系配置编号
     */
    Long createExtSchemaRelation(ExtSchemaRelationSaveReqVO createReqVO);

    /**
     * 更新关系配置
     *
     * @param updateReqVO 关系配置信息
     */
    int updateExtSchemaRelation(ExtSchemaRelationSaveReqVO updateReqVO);

    /**
     * 删除关系配置
     *
     * @param idList 关系配置编号
     */
    int removeExtSchemaRelation(Collection<Long> idList);

    /**
     * 获得关系配置详情
     *
     * @param id 关系配置编号
     * @return 关系配置
     */
    ExtSchemaRelationDO getExtSchemaRelationById(Long id);

    /**
     * 获得全部关系配置列表
     *
     * @return 关系配置列表
     */
    List<ExtSchemaRelationDO> getExtSchemaRelationList();

    /**
     * 获得全部关系配置 Map
     *
     * @return 关系配置 Map
     */
    Map<Long, ExtSchemaRelationDO> getExtSchemaRelationMap();


    /**
     * 导入关系配置数据
     *
     * @param importExcelList 关系配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtSchemaRelation(List<ExtSchemaRelationRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
