package tech.qiantong.qknow.module.ext.service.extSchemaAttribute;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
/**
 * 概念属性Service接口
 *
 * @author qknow
 * @date 2025-02-17
 */
public interface IExtSchemaAttributeService extends IService<ExtSchemaAttributeDO> {

    /**
     * 获得概念属性分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念属性分页列表
     */
    PageResult<ExtSchemaAttributeDO> getExtSchemaAttributePage(ExtSchemaAttributePageReqVO pageReqVO);

    /**
     * 创建概念属性
     *
     * @param createReqVO 概念属性信息
     * @return 概念属性编号
     */
    Long createExtSchemaAttribute(ExtSchemaAttributeSaveReqVO createReqVO);

    /**
     * 更新概念属性
     *
     * @param updateReqVO 概念属性信息
     */
    int updateExtSchemaAttribute(ExtSchemaAttributeSaveReqVO updateReqVO);

    /**
     * 删除概念属性
     *
     * @param idList 概念属性编号
     */
    int removeExtSchemaAttribute(Collection<Long> idList);

    /**
     * 获得概念属性详情
     *
     * @param id 概念属性编号
     * @return 概念属性
     */
    ExtSchemaAttributeDO getExtSchemaAttributeById(Long id);

    /**
     * 获得全部概念属性列表
     *
     * @return 概念属性列表
     */
    List<ExtSchemaAttributeDO> getExtSchemaAttributeList();

    /**
     * 获得全部概念属性 Map
     *
     * @return 概念属性 Map
     */
    Map<Long, ExtSchemaAttributeDO> getExtSchemaAttributeMap();


    /**
     * 导入概念属性数据
     *
     * @param importExcelList 概念属性数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtSchemaAttribute(List<ExtSchemaAttributeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<ExtSchemaAttributeDO> getAllExtSchemaAttributeList(ExtSchemaAttributeDO extSchemaAttributeDO);
}
