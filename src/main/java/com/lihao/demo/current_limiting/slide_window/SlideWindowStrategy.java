package com.lihao.demo.current_limiting.slide_window;

import com.lihao.demo.context.exception.DemoException;
import com.lihao.demo.context.pack.ErrorConstants;
import com.lihao.demo.current_limiting.base.AbstractCurrentLimitingStrategy;
import com.lihao.demo.current_limiting.base.CurrentLimiting;
import com.lihao.demo.current_limiting.base.DefaultStrategy;
import com.lihao.demo.lock.Lock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 滑动窗口限流策略
 * @author lihao
 * &#064;date  2024/9/15--15:36
 * @since 1.0
 */
@Component
@AllArgsConstructor
@Slf4j
public class SlideWindowStrategy extends AbstractCurrentLimitingStrategy<SlideWindowDto> {
    private final SlideWindowManager slideWindowManager;
    @Override
    @Lock(key = "slide_window")
    protected Object executeWitheCurrentLimitings(Map<String, CurrentLimiting> keyMap, ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, SlideWindowDto> slideWindowDtoMap = new HashMap<>();
        for(Map.Entry<String, CurrentLimiting> entry : keyMap.entrySet()){
            SlideWindowDto slideWindowDto = new SlideWindowDto(entry.getValue().count(), entry.getValue().time(), entry.getValue().unit(),entry.getValue().bucketCount());
            slideWindowManager.create(entry.getKey(), slideWindowDto);
            slideWindowDtoMap.put(entry.getKey(), slideWindowDto);
        }
        if(isLimit(slideWindowDtoMap)){
            throw new DemoException(ErrorConstants.CURRENT_LIMITING_ERROR);
        }
        addLimit(slideWindowDtoMap);
        return joinPoint.proceed();
    }

    @Override
    protected boolean isLimit(Map<String, SlideWindowDto> slideWindowDtoMap) {
        return slideWindowDtoMap.keySet().stream().anyMatch(key -> !slideWindowManager.tryAcquire(key, 1));
    }

    @Override
    protected void addLimit(Map<String, SlideWindowDto> slideWindowDtoMap) {
        slideWindowDtoMap.keySet().forEach(key -> slideWindowManager.deductionToken(key, 1));
    }

    @Override
    protected String getStrategyName() {
        return DefaultStrategy.SlideWindowStrategy.getStrategyName();
    }
}
