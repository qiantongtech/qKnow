package tech.qiantong.qknow.module.ext.api.service;

import tech.qiantong.qknow.module.ext.api.extUnstructTask.dto.ExtUnstructTaskRespDTO;

import java.util.List;

public interface IExtUnstructTaskTextApiService {
    public List<ExtUnstructTaskRespDTO> getUnstructTaskTextList();
}
