package com.tutorial.kafka.springkafka.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutorial.kafka.springkafka.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String msg) {
        kafkaTemplate.send(topic, msg);
    }

    public void sendMessages(String topic, Instant startTime, long total) {
        log.info("Producer started...");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.registerModule(new JavaTimeModule());
            for (int i = 0; i < total; i++) {
                String value = RandomStringUtils.random(10, true, true);
                Event event = Event.builder().eventType("test").message(value).createdAt(startTime).build();
                sendMessage(topic, objectMapper.writeValueAsString(event));
            }
        } catch (JsonProcessingException e) {
            log.error(e);
        } finally {
            log.info("Producer finished.");
        }

    }

    public void sendMessagesWithThread(String topic, Instant startTime, long total, int threads) {
        final long messagePerThread = total / threads;
        log.info("messagePerThread:{}", messagePerThread);
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executorService.execute(() -> sendMessages(topic, startTime, messagePerThread));
        }
        executorService.shutdown();
    }
}
