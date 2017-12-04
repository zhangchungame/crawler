package com.xlh.crawler.task;

import com.xlh.crawler.task.async.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
@EnableAsync
@Component
public class TaskCenter {

    @Autowired
    private AsyncTask asyncTask;

//    @Scheduled(fixedRate = 15000)
//    public void InserIp() throws InterruptedException {
////        asyncTask.async();
//    }
//    @Scheduled(fixedRate = 15000)
//    public void InserIp2() throws InterruptedException {
////       asyncTask.async2();
//    }

}
