package com.tutorial.kafka.springkafka.controller;

import com.tutorial.kafka.springkafka.common.constant.AppConst;
import com.tutorial.kafka.springkafka.service.kafka.EventSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class EventController {
    private final EventSender eventSender;
    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Value("${spring.kafka.listener.concurrency}")
    private int concurrency;

    @Bean
    public ApplicationRunner runAdditionalClientCacheInitialization() {
        return args -> {
//            eventSender.sendMessages(topic, Instant.now(), AppConst.MESSAGE_COUNT);
            eventSender.sendMessagesWithThread(topic, Instant.now(), AppConst.MESSAGE_COUNT, concurrency);
        };
    }
}
