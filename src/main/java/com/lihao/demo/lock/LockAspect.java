package com.lihao.demo.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihao
 * &#064;date  2024/9/15/13:27
 * @ability classname
 * @since 1.0
 */
@Aspect
@Component
public class LockAspect {
    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    @Around("@annotation(com.lihao.demo.lock.Lock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String key = lock.key();
        ReentrantLock reentrantLock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        reentrantLock.lock();
        try {
            return joinPoint.proceed();
        }finally {
            reentrantLock.unlock();
        }
    }
}
