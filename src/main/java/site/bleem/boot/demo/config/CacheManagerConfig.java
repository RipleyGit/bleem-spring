package site.bleem.boot.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import site.bleem.boot.demo.manager.CacheManager;
import site.bleem.boot.demo.manager.GuavaCacheManager;

@Configuration
public class CacheManagerConfig {


    @Bean
    @Conditional(ImplementationACondition.class)
    public CacheManager cacheManager(){
        return new GuavaCacheManager();
    }

    static class ImplementationACondition implements Condition {
        @Value("${spjr001.enabled}")
        private String enabledValue;
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // 在此处可以根据配置或其他条件判断是否启用 Implementation A
//            Environment environment = context.getEnvironment();
            // 获取配置参数的值
//            String enabledValue = environment.getProperty("guava.enable");

//            return "true".equalsIgnoreCase(enabledValue);
            return true;
        }
    }

}
