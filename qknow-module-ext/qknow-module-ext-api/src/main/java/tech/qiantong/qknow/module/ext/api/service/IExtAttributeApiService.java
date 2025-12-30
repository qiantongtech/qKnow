package tech.qiantong.qknow.module.ext.api.service;

import tech.qiantong.qknow.module.ext.api.extSchemaAttribute.dto.ExtSchemaAttributeRespDTO;

import java.util.List;

public interface IExtAttributeApiService {
    public List<ExtSchemaAttributeRespDTO> getExtSchemaAttributeList();
}
