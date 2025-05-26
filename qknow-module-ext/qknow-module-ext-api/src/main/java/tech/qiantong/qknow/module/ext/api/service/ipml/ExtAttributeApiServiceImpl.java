package tech.qiantong.qknow.module.ext.api.service.ipml;

import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.ext.api.service.IExtAttributeApiService;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExtAttributeApiServiceImpl implements IExtAttributeApiService {
    @Resource
    private IExtSchemaAttributeService extSchemaAttributeService;

    public List<ExtSchemaAttributeDO> getExtSchemaAttributeList() {
        return extSchemaAttributeService.getExtSchemaAttributeList();
    }
}
