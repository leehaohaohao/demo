package com.lihao.demo.lock;

import org.springframework.data.annotation.Reference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lihao
 * &#064;date  2024/9/15/13:25
 * @ability 可重入锁注解
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
    String key() default "default";
}
