package tech.qiantong.qknow.module.kmc.convert.kmcDocument;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;


/**
 * 知识文件 Convert
 *
 * @author qknow
 * @date 2025-02-14
 */
@Mapper
public interface KmcDocumentConvert {
    KmcDocumentConvert INSTANCE = Mappers.getMapper(KmcDocumentConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param kmcDocumentPageReqVO 请求参数
     * @return KmcDocumentDO
     */
    KmcDocumentDO convertToDO(KmcDocumentPageReqVO kmcDocumentPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param kmcDocumentSaveReqVO 保存请求参数
     * @return KmcDocumentDO
     */
    KmcDocumentDO convertToDO(KmcDocumentSaveReqVO kmcDocumentSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param kmcDocumentDO 实体对象
     * @return KmcDocumentRespVO
     */
    KmcDocumentRespVO convertToRespVO(KmcDocumentDO kmcDocumentDO);

    /**
     * DOList 转换为 RespVOList
     * @param kmcDocumentDOList 实体对象列表
     * @return List<KmcDocumentRespVO>
     */
    List<KmcDocumentRespVO> convertToRespVOList(List<KmcDocumentDO> kmcDocumentDOList);
}
