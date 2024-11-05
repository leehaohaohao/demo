package com.lihao.demo.api_usage.base;

import com.lihao.demo.api_usage.entity.HandTremblingDto;
import com.lihao.demo.context.exception.DemoException;
import com.lihao.demo.context.pack.ErrorConstants;
import com.lihao.demo.context.user.UserContextProvider;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 接口监控、防手抖切面
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
@Aspect
@Component
@AllArgsConstructor
public class MonitorApiUsageAspect {
    private final HandTrembling<HandTremblingDto> handTrembling;
    private final UserContextProvider userContextProvider;
    private final ApiUsage apiUsage;
    @Around("@annotation(com.lihao.demo.api_usage.base.MonitorApiUsage)")
    public Object monitorApiUsage(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        MonitorApiUsage monitorApiUsage = method.getAnnotation(MonitorApiUsage.class);
        if(monitorApiUsage.enableHandTrembling()){
            HandTremblingDto handTremblingDto = new HandTremblingDto(monitorApiUsage.handTime(),monitorApiUsage.handCount(), monitorApiUsage.unit());
            switch (monitorApiUsage.handType()){
                case ID -> handTremblingDto.setPrefix(userContextProvider.getUserId());
                case IP -> handTremblingDto.setPrefix(userContextProvider.getIp());
                case ALL -> handTremblingDto.setPrefix("ALL");
            }
            //防抖
            handTrembling.add(handTremblingDto);
            if(!handTrembling.tryAcquire(handTremblingDto)){
                throw new DemoException(ErrorConstants.HAND_TREMBLING_ERROR);
            }
        }
        return apiUsage.monitor(joinPoint,startTime);
    }
}
