package com.example.mywork;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.example.mywork.mapper")
@SpringBootApplication
@EnableAsync
public class MyWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWorkApplication.class, args);
    }

}
