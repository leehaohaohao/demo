package com.lihao.demo.current_limiting.slide_window;

import com.lihao.demo.current_limiting.base.BaseManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 滑动窗口限流管理器
 * @author lihao
 * &#064;date  2024/9/15--15:24
 * @since 1.0
 */
@Component
public class SlideWindowManager implements BaseManager<String, SlideWindowDto> {
    private final ConcurrentHashMap<String, SlideWindowDto> slideWindowMap = new ConcurrentHashMap<>();
    @Override
    public void create(String key, SlideWindowDto slideWindowDto){
        slideWindowMap.computeIfAbsent(key, k -> slideWindowDto);
    }
    @Override
    public void remove(String key) {
        slideWindowMap.remove(key);
    }
    @Override
    public boolean tryAcquire(String key, int permits) {
        if(slideWindowMap.containsKey(key)){
            return slideWindowMap.get(key).tryAcquire(permits);
        }
        return false;
    }
    @Override
    public void deductionToken(String key, int permits) {
        if(slideWindowMap.containsKey(key)){
            slideWindowMap.get(key).deductionToken(permits);
        }
    }
}
