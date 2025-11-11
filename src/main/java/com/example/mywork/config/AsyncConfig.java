package com.example.mywork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean("transerExecutor")
    public TaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);         //核心线程数
        executor.setMaxPoolSize(50);         // 最大线程数
        executor.setQueueCapacity(200); //队列容量
        executor.setThreadNamePrefix("async-task-");
        executor.setKeepAliveSeconds(60*10);
        executor.initialize();
        return executor;
    }
}
