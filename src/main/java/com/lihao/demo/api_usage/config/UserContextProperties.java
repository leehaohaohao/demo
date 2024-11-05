package com.lihao.demo.api_usage.config;

import com.lihao.demo.context.user.DefaultContext;
import com.lihao.demo.context.user.UserContext;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 用户上下文配置
 *
 * @author lihao
 * &#064;date  2024/11/5--18:57
 * @since 1.0
 */
@ConfigurationProperties(prefix = "api-usage.user-context")
@Data
public class UserContextProperties {
    private Class<? extends UserContext> implementationClass = DefaultContext.class;
}
