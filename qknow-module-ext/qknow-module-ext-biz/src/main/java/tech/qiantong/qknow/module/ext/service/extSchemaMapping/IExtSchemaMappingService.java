package tech.qiantong.qknow.module.ext.service.extSchemaMapping;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;
/**
 * 概念映射Service接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface IExtSchemaMappingService extends IService<ExtSchemaMappingDO> {

    /**
     * 获得概念映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 概念映射分页列表
     */
    PageResult<ExtSchemaMappingDO> getExtSchemaMappingPage(ExtSchemaMappingPageReqVO pageReqVO);

    /**
     * 创建概念映射
     *
     * @param createReqVO 概念映射信息
     * @return 概念映射编号
     */
    Long createExtSchemaMapping(ExtSchemaMappingSaveReqVO createReqVO);

    /**
     * 更新概念映射
     *
     * @param updateReqVO 概念映射信息
     */
    int updateExtSchemaMapping(ExtSchemaMappingSaveReqVO updateReqVO);

    /**
     * 删除概念映射
     *
     * @param idList 概念映射编号
     */
    int removeExtSchemaMapping(Collection<Long> idList);

    /**
     * 获得概念映射详情
     *
     * @param id 概念映射编号
     * @return 概念映射
     */
    ExtSchemaMappingDO getExtSchemaMappingById(Long id);

    /**
     * 获得全部概念映射列表
     *
     * @return 概念映射列表
     */
    List<ExtSchemaMappingDO> getExtSchemaMappingList();

    /**
     * 获得全部概念映射 Map
     *
     * @return 概念映射 Map
     */
    Map<Long, ExtSchemaMappingDO> getExtSchemaMappingMap();


    /**
     * 导入概念映射数据
     *
     * @param importExcelList 概念映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtSchemaMapping(List<ExtSchemaMappingRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
