package com.lihao.demo.api_usage.base;

import org.aspectj.lang.ProceedingJoinPoint;
/**
 * 监控接口接口
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
public interface ApiUsage {
    Object monitor(ProceedingJoinPoint joinPoint,long startTime) throws Throwable;
}
