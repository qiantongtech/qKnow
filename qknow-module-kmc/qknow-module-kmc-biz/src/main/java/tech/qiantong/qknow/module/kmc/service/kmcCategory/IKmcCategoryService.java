package tech.qiantong.qknow.module.kmc.service.kmcCategory;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategorySaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;

/**
 * 知识分类Service接口
 *
 * @author qknow
 * @date 2025-02-13
 */
public interface IKmcCategoryService extends IService<KmcCategoryDO> {

    /**
     * 获得知识分类分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识分类分页列表
     */
    PageResult<KmcCategoryDO> getKmcCategoryPage(KmcCategoryPageReqVO pageReqVO);

    /**
     * 创建知识分类
     *
     * @param createReqVO 知识分类信息
     * @return 知识分类编号
     */
    Long createKmcCategory(KmcCategorySaveReqVO createReqVO);

    /**
     * 更新知识分类
     *
     * @param updateReqVO 知识分类信息
     */
    int updateKmcCategory(KmcCategorySaveReqVO updateReqVO);

    /**
     * 删除知识分类
     *
     * @param idList 知识分类编号
     */
    int removeKmcCategory(Collection<Long> idList);

    /**
     * 获得知识分类详情
     *
     * @param id 知识分类编号
     * @return 知识分类
     */
    KmcCategoryDO getKmcCategoryById(Long id);

    /**
     * 获得全部知识分类列表
     *
     * @return 知识分类列表
     */
    List<KmcCategoryDO> getKmcCategoryList();

    /**
     * 获得全部知识分类 Map
     *
     * @return 知识分类 Map
     */
    Map<Long, KmcCategoryDO> getKmcCategoryMap();


    /**
     * 导入知识分类数据
     *
     * @param importExcelList 知识分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcCategory(List<KmcCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获得全部知识分类列表
     *
     * @return 知识分类列表
     */
    List<KmcCategoryDO> getKmcCategoryAllList(KmcCategoryDO kmcCategoryDO);

    /**
     * 获取知识分类树列表
     * @return 知识分类树列表
     */
    List<TreeSelects> selectCategoryTreeList(KmcCategoryDO kmcCategoryDO);

    List<TreeSelects> buildKmcCategoryTreeSelect(List<KmcCategoryDO> kmcCategoryDO);
}
