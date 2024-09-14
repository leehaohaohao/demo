package com.lihao.demo.current_limiting.base;

import com.lihao.demo.context.user.ContextInfo;
import com.lihao.demo.context.user.DefaultContext;
import com.lihao.demo.context.user.UserContext;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 限流切面
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Aspect
@Component
@AllArgsConstructor
public class CurrentLimitingAspect {
    private final UserContext<ContextInfo> userContext;
    @Around("@annotation(com.lihao.demo.current_limiting.base.CurrentLimiting)")
    public Object currentLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取所有限流注解
        CurrentLimiting[] currentLimitings = method.getAnnotationsByType(CurrentLimiting.class);
        Map<String,CurrentLimiting> keyMap = new HashMap<>();
        //先赋值给默认策略-令牌桶算法
        String strategyName = DefaultStrategy.TokenBucketStrategy.getStrategyName();
        for(int i = 0;i<currentLimitings.length;i++){
            CurrentLimiting currentLimiting = currentLimitings[i];
            //限流注解可能多个 所以不为空要区分
            String prefix = currentLimiting.prefix().isEmpty() ? method.toGenericString() + ":index:" + i : currentLimiting.prefix();
            String key = switch (currentLimiting.keyType()) {
                case ID -> userContext.getUserId();
                case IP -> userContext.getIp();
                case ALL -> "ALL";
            };
            keyMap.put(prefix+":"+key,currentLimiting);
            strategyName = currentLimiting.strategyName().isEmpty()?strategyName:currentLimiting.strategyName();
        }
        return CurrentLimitingFactory.getCurrentLimitingStrategy(strategyName).executeWitheCurrentLimitings(keyMap,joinPoint);
    }
}
