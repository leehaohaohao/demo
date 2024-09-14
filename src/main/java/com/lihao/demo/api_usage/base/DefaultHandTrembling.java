package com.lihao.demo.api_usage.base;


import com.lihao.demo.api_usage.entity.HandTremblingDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 默认防抖实现
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
@Component
public class DefaultHandTrembling implements HandTrembling<HandTremblingDto> {
    private final ConcurrentHashMap<String, HandTremblingDto> tremblingMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public void add(HandTremblingDto handTremblingDto) {
        if (handTremblingDto == null || handTremblingDto.getPrefix() == null || handTremblingDto.getPrefix().isEmpty()) {
            return;
        }
        tremblingMap.computeIfAbsent(handTremblingDto.getPrefix(), k -> {
            lockMap.put(k, new ReentrantLock());
            return handTremblingDto;
        });
    }

    @Override
    public boolean tryAcquire(HandTremblingDto handTremblingDto) {
        ReentrantLock lock = lockMap.get(handTremblingDto.getPrefix());
        if (lock == null) {
            return false;
        }
        lock.lock();
        try {
            HandTremblingDto tremblingDto = tremblingMap.get(handTremblingDto.getPrefix());
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - tremblingDto.getLastTime();

            if (elapsedTime < tremblingDto.getUnit().toMillis(tremblingDto.getTime())) {
                if (tremblingDto.getToken() + 1 <= tremblingDto.getCount()) {
                    tremblingDto.setToken(tremblingDto.getToken() + 1);
                    return true;
                }
            } else {
                tremblingDto.setToken(1);
                tremblingDto.setLastTime(currentTime);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
