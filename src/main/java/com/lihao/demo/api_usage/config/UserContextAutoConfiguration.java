package com.lihao.demo.api_usage.config;

import com.lihao.demo.context.user.ContextInfo;
import com.lihao.demo.context.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户上下文自动配置
 *
 * @author lihao
 * &#064;date  2024/11/5--18:58
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(UserContextProperties.class)
public class UserContextAutoConfiguration {
    @Autowired
    private UserContextProperties properties;

    @Bean
    public UserContext<ContextInfo> userContext() throws IllegalAccessException, InstantiationException {
        return properties.getImplementationClass().newInstance();
    }
}
