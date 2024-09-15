package com.lihao.demo.api_usage.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 接口监控、防抖注解
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitorApiUsage {
    /**
     * 是否启用接口计时 默认开启
     * @return
     */
    boolean enableTiming() default true;

    /**
     * 是否启用防抖 默认开启
     * @return
     */
    boolean enableHandTrembling() default true;

    /**
     * 防抖类型 默认根据用户ID防抖
     * @return
     */
    HandType handType() default HandType.ID;
    /**
     * 时间单位 默认秒
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;
    /**
     * 防抖时间 默认1秒
     * @return
     */
    long handTime() default 1;
    /**
     * 单位防抖时间内接口限制调用次数 默认1次
     * @return
     */
    int handCount() default 1;
    /**
     * 防抖类型
     */
    enum HandType{
        ID,
        IP,
        ALL
    }
}
