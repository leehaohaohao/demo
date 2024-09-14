package com.lihao.demo.current_limiting.base;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;

/**
 * 抽象限流策略接口
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Slf4j
public abstract class AbstractCurrentLimitingStrategy<K extends CurrentLimitingDTO> {
    /**
     * 初始化策略
     */
    @PostConstruct
    protected void init(){
        CurrentLimitingFactory.registerCurrentLimitingStrategy(getStrategyName(),this);
        log.info("注册限流策略："+getStrategyName());
    }
    protected abstract Object executeWitheCurrentLimitings(Map<String,CurrentLimiting> keyMap, ProceedingJoinPoint joinPoint) throws Throwable;
    /**
     * 是否达到限流限制
     */
    protected abstract boolean isLimit(Map<String,K> currentLimitingDTOMap);

    /**
     * 增加限流统计次数
     */
    protected abstract void addLimit(Map<String,K> currentLimitingDTOMap);
    /**
     * 获取策略名字
     */
    protected abstract String getStrategyName();
}
