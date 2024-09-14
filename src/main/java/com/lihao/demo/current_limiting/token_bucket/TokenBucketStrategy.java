package com.lihao.demo.current_limiting.token_bucket;

import com.lihao.demo.context.pack.ErrorConstants;
import com.lihao.demo.current_limiting.base.AbstractCurrentLimitingStrategy;
import com.lihao.demo.current_limiting.base.CurrentLimiting;
import com.lihao.demo.current_limiting.base.DefaultStrategy;
import com.lihao.demo.context.exception.GlobalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 令牌桶限流策略
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Component
@AllArgsConstructor
@Slf4j
public class TokenBucketStrategy extends AbstractCurrentLimitingStrategy<TokenBucketDto> {
    private final TokenBucketManager tokenBucketManager;
    @Override
    protected Object executeWitheCurrentLimitings(Map<String, CurrentLimiting> keyMap, ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, TokenBucketDto> tokenBucketDtoMap = new HashMap<>();
        for (Map.Entry<String, CurrentLimiting> entry : keyMap.entrySet()) {
            tokenBucketDtoMap.put(entry.getKey(), new TokenBucketDto(entry.getValue().capacity(), entry.getValue().rate()));
            tokenBucketManager.createTokenBucket(entry.getKey(), entry.getValue().capacity(), entry.getValue().rate());
        }
        if(isLimit(tokenBucketDtoMap)){
            throw new GlobalException(ErrorConstants.CURRENT_LIMITING_ERROR);
        }
        addLimit(tokenBucketDtoMap);
        //返回业务运行结果
        return joinPoint.proceed();
    }

    @Override
    protected boolean isLimit(Map<String, TokenBucketDto> tokenBucketDtoMap) {
        List<String> keyList = new ArrayList<>(tokenBucketDtoMap.keySet());
        for(String key:keyList){
            log.info("key:{},value:{}",key,tokenBucketDtoMap.get(key));
            if(!tokenBucketManager.tryAcquire(key,1)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addLimit(Map<String, TokenBucketDto> tokenBucketDtoMap) {
        for(String key:tokenBucketDtoMap.keySet()){
            tokenBucketManager.deductionToken(key,1);
        }
    }
    @Override
    protected String getStrategyName() {
        return DefaultStrategy.TokenBucketStrategy.getStrategyName();
    }
}
