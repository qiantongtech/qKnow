package tech.qiantong.qknow.module.ext.api.extUnstructTaskText;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.api.extUnstructTask.dto.ExtUnstructTaskRespDTO;
import tech.qiantong.qknow.module.ext.api.service.IExtUnstructTaskTextApiService;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskText.IExtUnstructTaskTextService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExtUnstructTaskTextApiServiceImpl implements IExtUnstructTaskTextApiService {

    @Resource
    private IExtUnstructTaskTextService extUnstructTaskTextService;


    @Override
    public List<ExtUnstructTaskRespDTO> getUnstructTaskTextList(){
        List<ExtUnstructTaskTextDO> textList = extUnstructTaskTextService.getExtUnstructTaskTextList();
        List<ExtUnstructTaskRespDTO> respDTOList = Lists.newArrayList();
        for (ExtUnstructTaskTextDO extUnstructTaskTextDO : textList) {
            ExtUnstructTaskRespDTO bean = BeanUtils.toBean(extUnstructTaskTextDO, ExtUnstructTaskRespDTO.class);
            respDTOList.add(bean);
        }
        return respDTOList;
    }
}
