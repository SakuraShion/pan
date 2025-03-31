package com.example.mywork.annotation;

import com.example.mywork.entity.enums.VerifyRegexEnum;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface VerfiyParam {

    int min() default -1;

    int max() default -1;

    boolean required() default false;

    VerifyRegexEnum regex() default VerifyRegexEnum.NO;


}
