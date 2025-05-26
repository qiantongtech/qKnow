package tech.qiantong.qknow.module.kmc.service.kmcDocument;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;

/**
 * 知识文件Service接口
 *
 * @author qknow
 * @date 2025-02-14
 */
public interface IKmcDocumentService extends IService<KmcDocumentDO> {

    /**
     * 获得知识文件分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识文件分页列表
     */
    PageResult<KmcDocumentDO> getKmcDocumentPage(KmcDocumentPageReqVO pageReqVO);

    List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids);

    /**
     * 创建知识文件
     *
     * @param createReqVO 知识文件信息
     * @return 知识文件编号
     */
    Long createKmcDocument(KmcDocumentSaveReqVO createReqVO);

    /**
     * 更新知识文件
     *
     * @param updateReqVO 知识文件信息
     */
    int updateKmcDocument(KmcDocumentSaveReqVO updateReqVO);

    /**
     * 删除知识文件
     *
     * @param idList 知识文件编号
     */
    int removeKmcDocument(Collection<Long> idList);

    /**
     * 获得知识文件详情
     *
     * @param id 知识文件编号
     * @return 知识文件
     */
    KmcDocumentDO getKmcDocumentById(Long id);

    /**
     * 获得全部知识文件列表
     *
     * @return 知识文件列表
     */
    List<KmcDocumentDO> getKmcDocumentList();

    /**
     * 获得全部知识文件 Map
     *
     * @return 知识文件 Map
     */
    Map<Long, KmcDocumentDO> getKmcDocumentMap();


    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcDocument(List<KmcDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    File getFileByHttpURL(String path);

    List<KmcDocumentDO> selectList(KmcDocumentPageReqVO kmcDocument);

    List<Map<String, Object>> getFileTypes();
}
