package com.example.mywork.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Mapping
public @interface GlobalInterceptor {
    /**
     * 校验参数
     * @return
     */

    boolean checkParasm() default false;
}
