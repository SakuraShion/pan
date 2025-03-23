package com.example.mywork;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.mywork.mapper")
@SpringBootApplication
public class MyWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWorkApplication.class, args);
    }

}
