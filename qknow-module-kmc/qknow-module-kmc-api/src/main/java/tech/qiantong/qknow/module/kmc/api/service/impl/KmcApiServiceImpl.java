package tech.qiantong.qknow.module.kmc.api.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.domain.TreeSelects;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KmcApiServiceImpl implements IKmcApiService {

    @Resource
    public IKmcDocumentService kmcDocumentService;
    @Resource
    public IKmcCategoryService kmcCategoryService;

    @Override
    public List<KmcDocumentDO> getKmcDocumentList(){
        return kmcDocumentService.getKmcDocumentList();
    }

    @Override
    public List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids){
        if (ids.isEmpty()) {
            return Lists.newArrayList();
        }
        return kmcDocumentService.getKmcDocumentListByIds(ids);
    }

    @Override
    public List<TreeSelects> getCategoryTreeList(KmcCategoryDO kmcCategoryDO) {
        return kmcCategoryService.selectCategoryTreeList(kmcCategoryDO);
    }
}
