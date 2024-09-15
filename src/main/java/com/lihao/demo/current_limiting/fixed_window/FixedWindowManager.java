package com.lihao.demo.current_limiting.fixed_window;

import com.lihao.demo.current_limiting.base.BaseManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 固定窗口限流管理器
 * @author lihao
 * &#064;date  2024/9/14
 * @since 1.0
 */
@Component
public class FixedWindowManager implements BaseManager<String,FixedWindowDto> {
    private final ConcurrentHashMap<String, FixedWindowDto> fixedWindowMap = new ConcurrentHashMap<>();
    @Override
    public void create(String key, FixedWindowDto fixedWindowDto){
        fixedWindowMap.computeIfAbsent(key, k -> fixedWindowDto);
    }
    @Override
    public void remove(String key) {
        fixedWindowMap.remove(key);
    }
    @Override
    public boolean tryAcquire(String key, int permits) {
        if(fixedWindowMap.containsKey(key)){
            return fixedWindowMap.get(key).tryAcquire(permits);
        }
        return false;
    }
    @Override
    public void deductionToken(String key, int permits) {
        if(fixedWindowMap.containsKey(key)){
            fixedWindowMap.get(key).deduction(permits);
        }
    }
}
