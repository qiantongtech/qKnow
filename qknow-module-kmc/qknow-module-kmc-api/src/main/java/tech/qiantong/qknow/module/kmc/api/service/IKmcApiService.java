package tech.qiantong.qknow.module.kmc.api.service;


import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;

import java.util.List;

public interface IKmcApiService {
    /**
     * 获得全部知识文件列表
     * @return
     */
    public List<KmcDocumentDO> getKmcDocumentList();

    public List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids);

    public List<TreeSelects> getCategoryTreeList(KmcCategoryDO kmcCategoryDO);
}
