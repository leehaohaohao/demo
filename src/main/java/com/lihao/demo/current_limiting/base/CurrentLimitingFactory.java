package com.lihao.demo.current_limiting.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流工厂
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
public class CurrentLimitingFactory{
    //存放限流策略
    private static final ConcurrentHashMap<String,AbstractCurrentLimitingStrategy<?>> CURRENT_LIMITING_STRATEGY_MAP = new ConcurrentHashMap<>(8);

    /**
     * 注册限流策略
     * @param strategyName 限流策略名字
     * @param currentLimitingStrategy 限流策略
     * @param <T> 限流DTO
     */
    public static <T extends AbstractCurrentLimitingStrategy<? extends CurrentLimitingDTO>> void registerCurrentLimitingStrategy(String strategyName,T currentLimitingStrategy){
        if(!CURRENT_LIMITING_STRATEGY_MAP.containsKey(strategyName)){
            CURRENT_LIMITING_STRATEGY_MAP.put(strategyName,currentLimitingStrategy);
        }
    }

    /**
     * 获取限流策略
     * @param strategyName 限流策略名字
     * @return 限流策略
     * @param <T> 限流DTO
     */
    @SuppressWarnings("unchecked")
    public static <T extends CurrentLimitingDTO> AbstractCurrentLimitingStrategy<T> getCurrentLimitingStrategy(String strategyName){
        return (AbstractCurrentLimitingStrategy<T>) CURRENT_LIMITING_STRATEGY_MAP.get(strategyName);
    }
    /**
     * 构造器私有
     */
    private CurrentLimitingFactory() {

    }
}
