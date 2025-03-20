package com.example.demo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// ExecutionTime이라는 어노테이션 만들기
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExecutionTime {
}
