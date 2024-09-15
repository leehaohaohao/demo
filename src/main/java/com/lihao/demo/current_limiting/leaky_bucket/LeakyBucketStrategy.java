package com.lihao.demo.current_limiting.leaky_bucket;

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
 * classname
 *
 * @author lihao
 * &#064;date  2024/9/15--16:00
 * @since 1.0
 */
@Component
@AllArgsConstructor
public class LeakyBucketStrategy extends AbstractCurrentLimitingStrategy<LeakyBucketDto> {
    private final LeakyBucketManager leakyBucketManager;
    @Override
    @Lock(key = "leaky_bucket")
    protected Object executeWitheCurrentLimitings(Map<String, CurrentLimiting> keyMap, ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, LeakyBucketDto> leakyBucketDtoMap = new HashMap<>();
        for(Map.Entry<String, CurrentLimiting> entry:keyMap.entrySet()){
            LeakyBucketDto leakyBucketDto = new LeakyBucketDto(entry.getValue().capacity(), entry.getValue().rate());
            leakyBucketManager.create(entry.getKey(), leakyBucketDto);
            leakyBucketDtoMap.put(entry.getKey(), leakyBucketDto);
        }
        if(isLimit(leakyBucketDtoMap)){
            throw new GlobalException(ErrorConstants.CURRENT_LIMITING_ERROR);
        }
        addLimit(leakyBucketDtoMap);
        return joinPoint.proceed();
    }

    @Override
    protected boolean isLimit(Map<String, LeakyBucketDto> currentLimitingDTOMap) {
        return currentLimitingDTOMap.keySet().stream().anyMatch(key -> !leakyBucketManager.tryAcquire(key, 1));
    }

    @Override
    protected void addLimit(Map<String, LeakyBucketDto> currentLimitingDTOMap) {
        currentLimitingDTOMap.keySet().forEach(key -> leakyBucketManager.deductionToken(key, 1));
    }

    @Override
    protected String getStrategyName() {
        return DefaultStrategy.LeakyBucketStrategy.getStrategyName();
    }
}
