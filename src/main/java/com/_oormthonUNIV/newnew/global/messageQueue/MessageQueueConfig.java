package com._oormthonUNIV.newnew.global.messageQueue;

import com._oormthonUNIV.newnew.global.messageQueue.task.SurveyStatisticsTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class MessageQueueConfig {

    @Bean
    public BlockingQueue<SurveyStatisticsTask> userTaskQueue() {
        return new LinkedBlockingQueue<>();
    }

}
