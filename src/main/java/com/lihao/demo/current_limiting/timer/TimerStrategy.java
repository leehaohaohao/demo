package com.lihao.demo.current_limiting.timer;

import com.lihao.demo.current_limiting.base.AbstractCurrentLimitingStrategy;
import com.lihao.demo.current_limiting.base.CurrentLimiting;
import com.lihao.demo.current_limiting.base.DefaultStrategy;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
/**
 * 计时器限流策略
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Component
@AllArgsConstructor
public class TimerStrategy extends AbstractCurrentLimitingStrategy<TimerDto> {
    private final TimerManager timerManager;
    @Override
    protected Object executeWitheCurrentLimitings(Map<String, CurrentLimiting> keyMap, ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, TimerDto> timerDtoMap = new HashMap<>();
        for (Map.Entry<String, CurrentLimiting> entry : keyMap.entrySet()) {
            timerDtoMap.put(entry.getKey(), new TimerDto(entry.getValue().count(), entry.getValue().time(), entry.getValue().unit()));
        }

        return joinPoint.proceed();
    }

    @Override
    protected boolean isLimit(Map<String, TimerDto> currentLimitingDTOMap) {
        return false;
    }

    @Override
    protected void addLimit(Map<String, TimerDto> currentLimitingDTOMap) {

    }

    @Override
    protected String getStrategyName() {
        return DefaultStrategy.TimerStrategy.getStrategyName();
    }
}
