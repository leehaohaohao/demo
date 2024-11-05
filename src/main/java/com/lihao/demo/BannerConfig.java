package com.lihao.demo;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * banner配置类
 *
 * @author lihao
 * &#064;date  2024/11/5--20:54
 * @since 1.0
 */
@Configuration
@Slf4j
public class BannerConfig {
    @PostConstruct
    public void init() {
        log.info("\n{}",Banner.banner);
    }
}
