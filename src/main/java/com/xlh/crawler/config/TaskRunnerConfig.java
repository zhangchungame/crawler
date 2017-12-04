package com.xlh.crawler.config;


import com.xlh.crawler.task.TaskRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskRunnerConfig {
    @Bean
    public TaskRunner taskRunner(){
        return new TaskRunner();
    }
}
