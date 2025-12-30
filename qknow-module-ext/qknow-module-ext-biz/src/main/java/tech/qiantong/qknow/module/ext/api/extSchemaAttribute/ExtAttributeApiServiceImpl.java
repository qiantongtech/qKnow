package tech.qiantong.qknow.module.ext.api.extSchemaAttribute;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.api.extSchemaAttribute.dto.ExtSchemaAttributeReqDTO;
import tech.qiantong.qknow.module.ext.api.extSchemaAttribute.dto.ExtSchemaAttributeRespDTO;
import tech.qiantong.qknow.module.ext.api.service.IExtAttributeApiService;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExtAttributeApiServiceImpl implements IExtAttributeApiService {
    @Resource
    private IExtSchemaAttributeService extSchemaAttributeService;

    public List<ExtSchemaAttributeRespDTO> getExtSchemaAttributeList() {
        List<ExtSchemaAttributeDO> extSchemaAttributeList = extSchemaAttributeService.getExtSchemaAttributeList();
        List<ExtSchemaAttributeRespDTO> attributeList = Lists.newArrayList();
        for (ExtSchemaAttributeDO attributeReqDTO : extSchemaAttributeList) {
            ExtSchemaAttributeRespDTO bean = BeanUtils.toBean(attributeReqDTO, ExtSchemaAttributeRespDTO.class);
            attributeList.add(bean);
        }
        return attributeList;
    }


}
