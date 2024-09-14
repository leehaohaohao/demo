package com.lihao.demo.current_limiting.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解容器
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentLimitingContainer {
    CurrentLimiting[] value();
}
