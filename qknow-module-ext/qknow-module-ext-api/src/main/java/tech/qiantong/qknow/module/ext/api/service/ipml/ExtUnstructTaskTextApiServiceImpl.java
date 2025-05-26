package tech.qiantong.qknow.module.ext.api.service.ipml;

import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.ext.api.service.IExtUnstructTaskTextApiService;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskText.IExtUnstructTaskTextService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExtUnstructTaskTextApiServiceImpl implements IExtUnstructTaskTextApiService {
    @Resource
    private IExtUnstructTaskTextService extUnstructTaskTextService;
    public List<ExtUnstructTaskTextDO> getUnstructTaskTextList(){
        List<ExtUnstructTaskTextDO> textList = extUnstructTaskTextService.getExtUnstructTaskTextList();
        return textList;
    }
}
