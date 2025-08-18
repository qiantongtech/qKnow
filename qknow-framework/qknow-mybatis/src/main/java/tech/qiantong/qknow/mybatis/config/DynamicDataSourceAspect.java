package tech.qiantong.qknow.mybatis.config;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 数据源切换切面
 * @author Ming
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Before("@annotation(ds)")
    public void beforeChangeDataSource(DS ds) {
        log.info("切换数据源: {}", ds.value());
    }
}
