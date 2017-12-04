package com.xlh.crawler.task;

import com.xlh.crawler.service.IpPoolService;
import com.xlh.crawler.task.async.AsyncTask;
import com.xlh.crawler.task.async.AsyncZhenxin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

public class TaskRunner implements ApplicationRunner, Ordered {

    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private AsyncZhenxin asyncZhenxin;
    private static final Logger logger = LoggerFactory.getLogger(TaskRunner.class);

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        asyncTask.ipPoolInsert();
        asyncZhenxin.startUp();

    }
}