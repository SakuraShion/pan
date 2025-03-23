package com.example.mywork.config;

import lombok.CustomLog;
import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EmailConfig {

    /**
     * 发件人
     */
    @Value("${spring.mail.username}")
    private String emailFrom;

}
