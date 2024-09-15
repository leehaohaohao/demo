package com.lihao.demo.current_limiting.fixed_window;

import com.lihao.demo.context.exception.GlobalException;
import com.lihao.demo.context.pack.ErrorConstants;
import com.lihao.demo.current_limiting.base.AbstractCurrentLimitingStrategy;
import com.lihao.demo.current_limiting.base.CurrentLimiting;
import com.lihao.demo.current_limiting.base.DefaultStrategy;
import com.lihao.demo.lock.Lock;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 固定窗口限流策略
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Component
@AllArgsConstructor
public class FixedWindowStrategy extends AbstractCurrentLimitingStrategy<FixedWindowDto> {
    private final FixedWindowManager fixedWindowManager;
    @Override
    @Lock(key = "fixed_window")
    protected Object executeWitheCurrentLimitings(Map<String, CurrentLimiting> keyMap, ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, FixedWindowDto> fixedWindowDtoMap = new HashMap<>();
        for (Map.Entry<String, CurrentLimiting> entry : keyMap.entrySet()) {
            FixedWindowDto fixedWindowDto = new FixedWindowDto(entry.getValue().count(), entry.getValue().time(), entry.getValue().unit());
            fixedWindowDtoMap.put(entry.getKey(), fixedWindowDto);
            fixedWindowManager.create(entry.getKey(), fixedWindowDto);
        }
        if(isLimit(fixedWindowDtoMap)){
            throw new GlobalException(ErrorConstants.CURRENT_LIMITING_ERROR);
        }
        addLimit(fixedWindowDtoMap);
        return joinPoint.proceed();
    }

    @Override
    protected boolean isLimit(Map<String, FixedWindowDto> fixedWindowDtoMap) {
        return fixedWindowDtoMap.keySet().stream().anyMatch(key -> !fixedWindowManager.tryAcquire(key,1));
    }

    @Override
    protected void addLimit(Map<String, FixedWindowDto> fixedWindowDtoMap) {
        fixedWindowDtoMap.keySet().forEach(key -> fixedWindowManager.deductionToken(key,1));
    }

    @Override
    protected String getStrategyName() {
        return DefaultStrategy.FixedWindowStrategy.getStrategyName();
    }
}
