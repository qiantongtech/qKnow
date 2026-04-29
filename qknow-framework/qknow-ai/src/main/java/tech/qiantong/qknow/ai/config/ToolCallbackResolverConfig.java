package tech.qiantong.qknow.ai.config;

import jakarta.annotation.Resource;
import org.springframework.ai.tool.resolution.SpringBeanToolCallbackResolver;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.ai.util.json.schema.SchemaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author wang
 */
@Configuration
public class ToolCallbackResolverConfig {

    @Resource
    private GenericApplicationContext applicationContext;

    @Bean
    public ToolCallbackResolver toolCallbackResolver() {
        return new SpringBeanToolCallbackResolver(applicationContext, SchemaType.JSON_SCHEMA);
    }
}
