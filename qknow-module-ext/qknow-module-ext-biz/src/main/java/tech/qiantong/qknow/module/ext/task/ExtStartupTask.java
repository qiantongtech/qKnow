package tech.qiantong.qknow.module.ext.task;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tech.qiantong.qknow.module.ext.service.extStructTask.IExtStructTaskService;
import tech.qiantong.qknow.module.ext.service.extUnstructTask.IExtUnstructTaskService;

import javax.annotation.Resource;

/**
 * 消费队列任务
 */
@Component
public class ExtStartupTask implements ApplicationRunner {
    @Resource
    private IExtUnstructTaskService extUnstructTaskService;
    @Resource
    private IExtStructTaskService extStructTaskService;

    /**
     * 执行结构化和非结构化抽取任务
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 在这里调用你想执行的方法
        //结构化
        new Thread(() -> extStructTaskService.consumeQueue()).start();
        //非结构化
        new Thread(() -> extUnstructTaskService.consumeQueue()).start();
    }
}
