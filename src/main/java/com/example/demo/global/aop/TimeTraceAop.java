package com.example.demo.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    private static final Logger logger = LoggerFactory.getLogger(TimeTraceAop.class);

    // 특정 패키지 내의 모든 메서드 실행시간 측정
    @Around("execution(* com.example.demo..*(..))") // -> 패키지 경로 맞추기
    public Object measureRunTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        logger.info(" {} 실행시간: {}ms", joinPoint.getSignature(), (endTime - startTime));
        return result;
    }
}
