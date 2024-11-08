package com.lihao.demo.api_usage.base;

import com.lihao.demo.context.user.UserContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
/**
 * 默认监控接口实现
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
@Component
@Slf4j
@ConditionalOnMissingBean(ApiUsage.class)
public class DefaultApiUsage implements ApiUsage{
    @Override
    public Object monitor(ProceedingJoinPoint joinPoint, long startTime) throws Throwable {
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String apiName = className+"."+methodName;
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            params.append(arg).append(" ");
        }
        log.debug("接口：{}，参数：{}，耗时：{}ms", apiName, params.toString().trim(), responseTime);
        return result;
    }
}
