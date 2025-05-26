package tech.qiantong.qknow.module.ext.api.service;

import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;

import java.util.List;

public interface IExtAttributeApiService {
    public List<ExtSchemaAttributeDO> getExtSchemaAttributeList();
}
