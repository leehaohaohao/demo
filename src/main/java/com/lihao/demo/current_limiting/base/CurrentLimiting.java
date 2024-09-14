package com.lihao.demo.current_limiting.base;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CurrentLimitingContainer.class)
public @interface CurrentLimiting {
    /**
     * 限流策略名字 若为空则默认令牌桶算法
     * @return 限流策略名字
     */
    String strategyName() default "";

    /**
     * 限流键值前缀 默认方法全限定名
     * @return 限流键值前缀
     */
    String prefix() default "";

    /**
     * 限流对象 默认是根据用户ID
     * @return 限流对象
     */
    KeyType keyType() default KeyType.ID;

    /**
     * 单位时间限流次数
     * @return 限流次数
     */
    int count() default 10;

    /**
     * 限流时间范围 默认1秒
     * @return 限流时间范围
     */
    long time() default 1;

    /**
     * 时间单位 默认秒
     * @return 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 令牌桶容量 默认10
     * @return 令牌桶容量
     */
    long capacity() default 10;

    /**
     * 每秒补充令牌数 默认1
     * @return 每秒补充令牌数
     */
    double rate() default 1;
    enum KeyType{
        ID,
        IP,
        ALL
    }
}
