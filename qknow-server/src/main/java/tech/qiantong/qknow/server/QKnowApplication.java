package tech.qiantong.qknow.server;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动程序
 *
 * @author qknow
 */
@EnableFileStorage
@ComponentScan(basePackages = {"tech.qiantong"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class QKnowApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(QKnowApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  千知平台启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "        _  __                    \n" +
                "   __ _| |/ /_ __   _____      __\n" +
                "  / _` | ' /| '_ \\ / _ \\ \\ /\\ / /\n" +
                " | (_| | . \\| | | | (_) \\ V  V / \n" +
                "  \\__, |_|\\_\\_| |_|\\___/ \\_/\\_/  \n" +
                "     |_|                         ");
    }
}
