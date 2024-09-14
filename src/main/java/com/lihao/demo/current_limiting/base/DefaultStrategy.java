package com.lihao.demo.current_limiting.base;

import lombok.Getter;
/**
 * 默认限流策略（计时器、漏桶、令牌桶）
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Getter
public enum DefaultStrategy {
    TokenBucketStrategy("TokenBucketStrategy"),
    TimerStrategy("TimerStrategy");
    private final String strategyName;
    DefaultStrategy(String strategyName) {
        this.strategyName = strategyName;
    }
}
