package tech.qiantong.qknow.module.system.service.message.impl;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qknow.module.system.dal.dataobject.message.MessageTemplateDO;
import tech.qiantong.qknow.module.system.dal.mapper.message.MessageTemplateMapper;
import tech.qiantong.qknow.module.system.service.message.IMessageTemplateService;

/**
 * 消息模板Service业务层处理
 *
 * @author qknow
 * @date 2024-10-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageTemplateServiceImpl  extends ServiceImpl<MessageTemplateMapper,MessageTemplateDO> implements IMessageTemplateService {
    @Resource
    private MessageTemplateMapper messageTemplateMapper;

}
