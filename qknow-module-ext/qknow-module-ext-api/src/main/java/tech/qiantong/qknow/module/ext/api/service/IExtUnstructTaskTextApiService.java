package tech.qiantong.qknow.module.ext.api.service;

import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;

import java.util.List;

public interface IExtUnstructTaskTextApiService {
    public List<ExtUnstructTaskTextDO> getUnstructTaskTextList();
}
